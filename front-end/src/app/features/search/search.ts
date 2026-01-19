import { Component } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

interface UserSearchResult {
  id: number;
  username: string;
  profileImage?: string | null;
}

@Component({
  selector: 'search',
  templateUrl: 'search.html',
  styleUrls: ['search.css'],
  imports:[CommonModule, FormsModule]
})
export class Search {

  query = '';
  results: UserSearchResult[] = [];
  loading = false;

  constructor(private http: HttpClient) {}

  onSearchChange(): void {
    if (!this.query || this.query.length < 2) {
      this.results = [];
      return;
    }

    this.loading = true;

    const params = new HttpParams().set('param', this.query);

    this.http.get<UserSearchResult[]>(
      'http://localhost:8080/api/v1/user/profile/search',
      { params, withCredentials: true }
    ).subscribe({
      next: res => {
        this.results = res;
        this.loading = false;
      },
      error: err => {
        console.error(err);
        this.loading = false;
      }
    });
  }
}
