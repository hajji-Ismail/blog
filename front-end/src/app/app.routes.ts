import { Routes } from '@angular/router';
import { register } from './features/register/register'; // Import your component class
import { login } from './features/login/login';
import { Posts } from './features/post/post';
import { authGuard } from './core/guards/auth-guard';
import { Sidebar } from './core/layout/sidebar/sidebar';
import { SavePost } from './features/save-post/save-post';

export const routes: Routes = [
  { path: 'register', component: register },
  { path: 'login', component: login },
  { 
    path: '', 
    component: Sidebar, 
    canActivate: [authGuard],
    children: [
      { path: 'home', component: Posts },
      { path: '', redirectTo: 'home', pathMatch: 'full' } ,
      {path: 'create', component: SavePost}
    ]
  } 
];