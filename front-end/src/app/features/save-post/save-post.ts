import { HttpClient } from '@angular/common/http';
import { Component, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, ɵInternalFormsSharedModule } from '@angular/forms';
import { Route, Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-save-post',
  imports: [ɵInternalFormsSharedModule , ReactiveFormsModule],

  templateUrl: './save-post.html',
  styleUrl: './save-post.css',
})
export class SavePost {
title = signal<String> ('');
content = signal<String> ('');

  postForm!: FormGroup;
  selectedFiles: File[] = [];

  constructor(private fb: FormBuilder, private http: HttpClient,  private router : Router) {
  
  }

  ngOnInit() {
  this.postForm = this.fb.group({
      title: [''],
      content: [''],
      mediaFiles: [null]
    });
  }
  onFilesSelected(event: any) {
    if (event.target.files) {
      this.selectedFiles = Array.from(event.target.files);
    }
  }

  submitPost() {
    if (this.postForm.invalid) {
      return;
    }

    const formData = new FormData();
    formData.append('title', this.postForm.get('title')?.value);
    formData.append('content', this.postForm.get('content')?.value);

    // Append all selected files
    this.selectedFiles.forEach((file, index) => {
      formData.append('mediaFiles', file);
    });

    this.http.post("http://localhost:8080/api/v1/user/post/save", formData, { withCredentials: true }).subscribe({
      next: (_) => {
     
        this.postForm.reset();
        this.selectedFiles = [];
        this.router.navigate(['/']);
      },
      error: (err) => {
        console.error(err);
        this.title.set(err.error.title)
        this.content.set(err.error.content)
      }
    });
  }
}
