import { Routes } from '@angular/router';
import { register } from './features/register/register'; // Import your component class
import { login } from './features/login/login';
import { Posts } from './features/post/post';
import { authGuard } from './core/guards/auth-guard';

export const routes: Routes = [
  { path: 'register', component: register },
  {path : 'login', component: login},// When user goes to /register, show this
  { path: '', component: Posts , canActivate:[authGuard]} 
];
