import { Routes } from '@angular/router';
import { register } from './register/register'; // Import your component class

export const routes: Routes = [
  { path: 'register', component: register }, // When user goes to /register, show this
  { path: '', redirectTo: '/register', pathMatch: 'full' } // Optional: default to register
];
