import { HttpClient } from '@angular/common/http';
import { Component, signal } from "@angular/core";
import { Router, RouterLink } from "@angular/router";


@Component({
    selector: 'login',
      styleUrl:'login.css',
    templateUrl: 'login.html',
    imports: [RouterLink]
})
export class login {
  message=signal<String>('')
  constructor(private Http: HttpClient , private router : Router) {

  }
 onlogin(identifierValue: string, passwordValue: string): void {

 const url = "http://localhost:8080/api/v1/Auth/login"
 let data = {
  email_or_username : identifierValue,
password: passwordValue


 }
this.Http.post(url,data).subscribe({
  next: (_) =>{
  this.message.set('Registration successful!');
      this.router.navigate(['/']); // navigate to login component
  },
  error :(err) => {


    this.message.set(err.error.user.message)
  }
});
}
}
