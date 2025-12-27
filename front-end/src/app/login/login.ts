import { HttpClient } from '@angular/common/http';
import { Component } from "@angular/core";


@Component({
    selector: 'login',

    templateUrl: 'login.html'
})
export class login {
  constructor(private Http: HttpClient) {

  }
 onlogin(identifierValue: string, passwordValue: string): void {

 const url = "http://localhost:8080/api/v1/Auth/login"
 let data = {
  email_or_username : identifierValue,
password: passwordValue


 }
this.Http.post(url,data).subscribe({
  next: (res) => console.log(res),
  error :(err) => console.log(err)
});
}
}
