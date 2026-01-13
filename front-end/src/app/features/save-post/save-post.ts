import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, ɵInternalFormsSharedModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-save-post',
  imports: [ɵInternalFormsSharedModule , ReactiveFormsModule, RouterLink],

  templateUrl: './save-post.html',
  styleUrl: './save-post.css',
})
export class SavePost {

  postForm!: FormGroup;
  selectedFiles: File[] = [];

  constructor(private fb: FormBuilder, private http: HttpClient) {
  
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
    alert("enterd")
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
console.log(formData.get('title'), "sdasdadfsdafvsdfasdafsdfaf");

    this.http.post("http://localhost:8080/api/v1/user/post/save", formData, { withCredentials: true }).subscribe({
      next: (res) => {
        console.log('Post saved!', res);
        alert('Post saved successfully!');
        this.postForm.reset();
        this.selectedFiles = [];
      },
      error: (err) => {
        console.error(err);
        alert('Error saving post');
      }
    });
  }
}
