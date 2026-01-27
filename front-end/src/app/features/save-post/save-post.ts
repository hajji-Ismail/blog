import { Component, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; // Important for *ngIf/NgFor
import { HttpClient } from '@angular/common/http';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-save-post',
  standalone: true,
  // Use CommonModule instead of internal Angular modules
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './save-post.html',
  styleUrl: './save-post.css',
})
export class SavePost implements OnInit {
  // Signals to hold error messages from Backend
  titleError = signal<string>('');
  contentError = signal<string>('');

  postForm!: FormGroup;
  // Store objects containing the file and its preview URL
  selectedMedia: { file: File, preview: string, type: string }[] = [];

  constructor(private fb: FormBuilder, private http: HttpClient, private router: Router) {}

  ngOnInit() {
    this.postForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3)]],
      content: ['', [Validators.required]]
    });
  }

  onFilesSelected(event: any) {
    const files: File[] = Array.from(event.target.files);
    
    files.forEach(file => {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.selectedMedia.push({
          file: file,
          preview: e.target.result,
          type: file.type
        });
      };
      reader.readAsDataURL(file);
    });
  }

  removeMedia(index: number) {
    this.selectedMedia.splice(index, 1);
  }

  submitPost() {
    if (this.postForm.invalid) {
      this.postForm.markAllAsTouched();
      return;
    }

    const formData = new FormData();
    formData.append('title', this.postForm.get('title')?.value);
    formData.append('content', this.postForm.get('content')?.value);

    // Append files from our array
    this.selectedMedia.forEach((item) => {
      formData.append('mediaFiles', item.file);
    });

    this.http.post("http://localhost:8080/api/v1/user/post/save", formData, { 
      withCredentials: true 
    }).subscribe({
      next: () => {
        this.router.navigate(['/']);
      },
      error: (err) => {
        // Map backend validation errors to signals
        if (err.error) {
          this.titleError.set(err.error.title || '');
          this.contentError.set(err.error.content || '');
        }
      }
    });
  }
}