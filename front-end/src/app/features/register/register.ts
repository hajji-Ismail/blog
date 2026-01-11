import { Router } from '@angular/router';
import { HttpClient } from "@angular/common/http";
import { Component, OnInit, signal } from "@angular/core";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { RouterLink } from "@angular/router";

@Component({
    selector: 'register',
    standalone: true, // Check if this is here
    imports: [ReactiveFormsModule, RouterLink],
    templateUrl: 'register.html',
    styleUrl:'register.css',
})
export class register implements OnInit {
  registerForm!: FormGroup;
  selectedFile: File | null = null;
  username = signal<String>('')
  email = signal<String>('')
  password = signal<String>('')
  message=signal<String>('')

  constructor(private http: HttpClient, private fb: FormBuilder, private router : Router) {}

  ngOnInit() {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  // Captures the file when the user selects it
  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

 onregister() {
  if (this.registerForm.invalid) {
    alert("Please fill all fields ");
    return;
  }

  const url = "http://localhost:8080/api/v1/Auth/register";
  const formData = new FormData();

  formData.append('username', this.registerForm.get('username')?.value);
  formData.append('email', this.registerForm.get('email')?.value);
  formData.append('password', this.registerForm.get('password')?.value);

  if (this.selectedFile) {
    formData.append('profileImage', this.selectedFile);
  }

  this.http.post(url, formData, { withCredentials: true }).subscribe({
    next: (res) => {
      console.log('Registration success:', res);
      this.message.set('Registration successful!');
      this.router.navigate(['/']); // navigate to login component
    },
    error: (err) => {
      console.error('Registration error:', err);

      // Safe property access
      const userError = err?.error?.user;
      if (userError) {
        this.message.set(userError.message || 'Error occurred');
        this.email.set(userError.email || '');
        this.password.set(userError.password || '');
        this.username.set(userError.username || '');
      } else {
        this.message.set('An unknown error occurred.');
      }
    }
  });
}

}
