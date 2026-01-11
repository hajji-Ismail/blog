import { Routes } from '@angular/router';
import { register } from './features/register/register'; // Import your component class
import { login } from './features/login/login';

export const routes: Routes = [
  { path: 'register', component: register },
  {path : 'login', component: login},// When user goes to /register, show this
  { path: '', redirectTo: '/register', pathMatch: 'full' } // Optional: default to register
];
