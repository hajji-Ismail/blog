import { Component, OnInit, signal, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-save-post',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './save-post.html',
  styleUrls: ['./save-post.css'],
})
export class SavePost implements OnInit {
  // Signals for backend validation errors
  titleError = signal<string>('');
  contentError = signal<string>('');
  isSaving = signal<boolean>(false);
  postForm!: FormGroup;
  readonly MAX_FILES = 5;
  fileLimitError = signal<string>('');
  // Store selected media files with preview
  selectedMedia: { file: File; preview: string; type: string }[] = [];

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
    public auth: AuthService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit() {
    this.postForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3),  Validators.maxLength(50)]],
      content: ['', [Validators.required , Validators.maxLength(10000)]],
    });
  }

  /** Triggered when files are selected */
  onFilesSelected(event: any) {
    const files: File[] = Array.from(event.target.files);

    // Remaining slots
    const remainingSlots = this.MAX_FILES - this.selectedMedia.length;

    if (remainingSlots <= 0) {
      this.fileLimitError.set(`Maximum ${this.MAX_FILES} files allowed.`);
      event.target.value = '';
      return;
    }

    if (files.length > remainingSlots) {
      this.fileLimitError.set(
        `You can only upload ${this.MAX_FILES} files total.`
      );
    } else {
      this.fileLimitError.set('');
    }

    // Only take allowed number of files
    const allowedFiles = files.slice(0, remainingSlots);

    allowedFiles.forEach((file) => {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.selectedMedia.push({
          file,
          preview: e.target.result,
          type: file.type,
        });

        this.cdr.detectChanges();
      };
      reader.readAsDataURL(file);
    });

    event.target.value = '';
  }

  /** Remove a media file */
  removeMedia(index: number) {
    this.selectedMedia.splice(index, 1);
  }

  submitPost() {

    if (this.selectedMedia.length > this.MAX_FILES) {
      this.fileLimitError.set(`Maximum ${this.MAX_FILES} files allowed.`);
      return;
    }
    if (this.isSaving()) return;

    if (this.postForm.invalid) {
      this.postForm.markAllAsTouched();
      return;
    }

    this.isSaving.set(true);

    const formData = new FormData();
    formData.append('title', this.postForm.get('title')?.value);
    formData.append('content', this.postForm.get('content')?.value);

    this.selectedMedia.forEach((item) => {
      formData.append('mediaFiles', item.file);
    });

    this.http
      .post('http://localhost:8080/api/v1/user/post/save', formData, { withCredentials: true })
      .subscribe({
        next: () => {
          this.isSaving.set(false);
          this.router.navigate(['/']);
        },
        error: (err) => {
          console.log(err);
          
          if (err.error.Code == 401) {
            this.auth.logout()
            this.router.navigate(['/login']);


          }
          this.isSaving.set(false);

          if (err.error) {
            this.titleError.set(err.error.title || '');
            this.contentError.set(err.error.content || '');
          }
        },
      });
  }

  trackByIndex(index: number, item: any) {
    return index;
  }
}
