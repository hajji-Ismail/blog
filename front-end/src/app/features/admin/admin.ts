import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

interface User {
  username: string;
  baned: boolean;
  profileImageUrl: string | null;
}

interface AdminResponse {
  numberOfBandUser: number;
  numberOfUnbandUser: number;
  users: User[];
  postReports: any[];
  userReports: any[];
}

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin.html',
  styleUrls: ['./admin.css'],
})
export class Admin implements OnInit {
  loading = true;
  error: string | null = null;

  data: AdminResponse = {
    numberOfBandUser: 0,
    numberOfUnbandUser: 0,
    users: [],
    postReports: [],
    userReports: [],
  };

  selectedView: 'post' | 'user' = 'post';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadAdminData();
  }

  loadAdminData(): void {
    this.loading = true;
    this.http
      .get<AdminResponse>('http://localhost:8080/api/v1/admin/load', {
        withCredentials: true,
      })
      .subscribe({
        next: (value) => {
          // Map backend typo to proper property name if needed
          if ((value as any).postRerports) {
            value.postReports = (value as any).postRerports;
          }
          this.data = value;
          this.loading = false;
          console.log(value, 'admin data');
        },
        error: (err) => {
          this.loading = false;
          this.error = 'Failed to load admin data';
          console.error(err, 'error admin');
        },
      });
  }

  select(view: 'post' | 'user'): void {
    this.selectedView = view;
  }

  get totalUsers(): number {
    return this.data.numberOfBandUser + this.data.numberOfUnbandUser;
  }

  get unbannedPercentage(): number {
    if (this.totalUsers === 0) return 0;
    return Math.round((this.data.numberOfUnbandUser / this.totalUsers) * 100);
  }

  onBanned(username: string) {
    this.http
      .post(
        'http://localhost:8080/api/v1/admin/baneUser',
        { username },
        { withCredentials: true }
      )
      .subscribe({
        next: (res) => {
          console.log(res);
          // Update local state immediately
          const user = this.data.users.find((u) => u.username === username);
          if (user) user.baned = true;
        },
        error: (err) => {
          console.error(err);
        },
      });
  }
}
