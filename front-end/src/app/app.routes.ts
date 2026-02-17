import { Routes } from '@angular/router';
import { register } from './features/register/register'; // Import your component class
import { login } from './features/login/login';
import { Posts } from './features/post/post';
import { authGuard, roleGuard } from './core/guards/auth-guard';
import { Sidebar } from './core/layout/sidebar/sidebar';
import { SavePost } from './features/save-post/save-post';
import { ProfilesComponent } from './features/profiles/profiles';
import { Search } from './features/search/search';
import { Admin } from './features/admin/admin';
import { NotificationComponent } from './features/notification/notification.component';

export const routes: Routes = [
  { path: 'register', component: register },
  { path: 'login', component: login },
  {
    path: '',
    component: Sidebar,
    canActivate: [authGuard],
    children: [
      { path: 'home', component: Posts },
      { path: '', redirectTo: 'home', pathMatch: 'full' },
      { path: 'create', component: SavePost },
        { path: 'notification', component: NotificationComponent },
      {
  path: 'profile/:username',
  component: ProfilesComponent,
  runGuardsAndResolvers: 'always'
},

      { path: 'search', component: Search },
      {
        path: 'admin', component: Admin, canActivate: [
          authGuard, roleGuard
        ], data: { role: 'ADMIN' }
      },

    ]
  }
];