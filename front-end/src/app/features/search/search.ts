import { Component, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { getProfileImage } from '../../services/profile.service';
import { AuthService } from '../../services/auth.service';

interface UserSearchResult {
  id: number;
  username: string;
  profileImage?: string | null;
}

@Component({
  selector: 'search',
  standalone: true,
  templateUrl: 'search.html',
  styleUrls: ['search.css'],
  imports: [CommonModule, FormsModule, RouterLink]
})
export class Search {

  // ✅ Signals
  query = signal('');
  results = signal<UserSearchResult[]>([]);
  loading = signal(false);

  constructor(private http: HttpClient, public auth: AuthService, private router: Router) { }

  onSearchChange(): void {
    const currentQuery = this.query();

    if (!currentQuery || currentQuery.length < 2) {
      this.results.set([]);
      return;
    }

    this.loading.set(true);

    const params = new HttpParams().set('param', currentQuery);

    this.http.get<UserSearchResult[]>(
      'http://localhost:8080/api/v1/user/profile/search',
      { params, withCredentials: true }
    ).subscribe({
      next: res => {
        this.results.set(res);
        this.loading.set(false);
      },
      error: err => {
        if (err.error.Code == 401) {
          this.auth.logout()
          this.router.navigate(['/login']);
        }
        this.loading.set(false);
      }
    });
  }

  getProfileImage(url: string | null | undefined) {
    return getProfileImage(url);
  }
}