import { HttpClient } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";

@Component({
    selector: 'register',
    standalone: true, // Check if this is here
    imports: [ReactiveFormsModule],
    templateUrl: 'register.html'
})
export class register implements OnInit {
  registerForm!: FormGroup;
  selectedFile: File | null = null; // To store the file

  constructor(private http: HttpClient, private fb: FormBuilder) {}

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

    // 1. Create the FormData envelope
    const formData = new FormData();


    // 2. Add your text fields
    formData.append('username', this.registerForm.get('username')?.value);
    formData.append('email', this.registerForm.get('email')?.value);
    formData.append('password', this.registerForm.get('password')?.value);
if (this.selectedFile){

  formData.append('profileImage', this.selectedFile);
}


    // 4. Send it! (Angular automatically sets the correct Headers for FormData)
    this.http.post(url, formData).subscribe({
      next: (res) => alert('Registration Successful!'),
      error: (err) => console.log(err),

    });
  }
}
