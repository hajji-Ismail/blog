import { Component, OnInit, signal, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-save-post',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './save-post.html',
  styleUrls: ['./save-post.css'], // ✅ Fixed typo
})
export class SavePost implements OnInit {
  // Signals for backend validation errors
  titleError = signal<string>('');
  contentError = signal<string>('');
isSaving = signal<boolean>(false);
  postForm!: FormGroup;

  // Store selected media files with preview
  selectedMedia: { file: File; preview: string; type: string }[] = [];

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private cdr: ChangeDetectorRef 
  ) {}

  ngOnInit() {
    this.postForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3)]],
      content: ['', [Validators.required]],
    });
  }

  /** Triggered when files are selected */
  onFilesSelected(event: any) {
    const files: File[] = Array.from(event.target.files);

    files.forEach((file) => {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        // Add file immediately for preview
        this.selectedMedia.push({
          file,
          preview: e.target.result,
          type: file.type,
        });

        // Force Angular to update the template immediately
        this.cdr.detectChanges();
      };
      reader.readAsDataURL(file);
    });

    // Reset input so the same file can be selected again
    event.target.value = '';
  }

  /** Remove a media file */
  removeMedia(index: number) {
    this.selectedMedia.splice(index, 1);
  }

submitPost() {

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
