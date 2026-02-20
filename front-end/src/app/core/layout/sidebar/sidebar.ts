import { Component, signal } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { Router, RouterLink, RouterOutlet } from "@angular/router";
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { ProfilePecture } from './model/profile.model';
import { getProfileImage } from '../../../services/profile.service';

@Component({
  selector: 'app-sidebar',
  imports: [RouterLink, CommonModule, RouterOutlet],
  templateUrl: 'sidebar.html',
  styleUrl: 'sidebar.css',
})
export class Sidebar {
  profile = signal<ProfilePecture|null>(null);
  constructor(public auth: AuthService, private http: HttpClient, private router: Router) {}
  menuItems = [
    { label : 'Home',route : '/home' },
    { label : 'search',route : '/search' },
    { label : 'create',route : '/create' },

    { label : 'notification',route : '/notification' },
    

  ]

    ngOnInit(): void {
      this.http.get<ProfilePecture>('http://localhost:8080/api/v1/user/profile/user', { withCredentials: true })
        .subscribe({
          next: data => {
            this.profile.set(data)}
          ,
          error: err => {
            console.error(err);
            if (err.error.Code == 401){
               this.auth.logout();
    this.router.navigate(['/login']);
            }
            this.profile.set(null);
          }
        });
    }
    getProfileImage(url: string | null | undefined) {
        return getProfileImage(url);
      }

  onLogout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
  }

