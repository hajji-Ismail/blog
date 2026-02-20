import { HttpClient } from '@angular/common/http';
import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';

interface User {
  username: string;
  baned: boolean;
  profileImageUrl: string | null;
}

interface PostReport {
  id: number;
  reason: string;
  time: string;
  postId: number;
  postTitle: string;
  postOwnerUsername: string;
  reporterUsername: string;
}

interface UserReport {
  id: number;
  reason: string;
  time: string;
  reportedUsername: string;
  reporterUsername: string;
}

interface AdminResponse {
  numberOfBandUser: number;
  numberOfUnbandUser: number;
  users: User[];
  postReports: PostReport[];
  userReports: UserReport[];
  postRerports?: PostReport[];
}

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin.html',
  styleUrls: ['./admin.css'],
})
export class Admin implements OnInit {
loading = signal<boolean>(true)
  error = '';
  toastMessage = '';
  toastType: 'success' | 'error' = 'success';

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
    console.log("hio");
    
    this.loadAdminData();
  }

  loadAdminData(): void {

    this.loading.set(true)
    this.error = '';

    this.http
      .get<AdminResponse>('http://localhost:8080/api/v1/admin/load', {
        withCredentials: true,
      })
      .subscribe({
        next: (value) => {
          console.log(value , "ihjpiouhipuhoiuhu");
          
          this.data = {
            ...value,
            postReports: value.postReports ?? value.postRerports ?? [],
            userReports: value.userReports ?? [],
            users: value.users ?? [],
          };
          this.loading.set(false);
        },
        error: () => {
          this.loading.set(false);
          this.error = 'Failed to load admin data';
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

  toggleBan(user: User) {
    const endpoint = user.baned
      ? 'http://localhost:8080/api/v1/admin/unbaneUser'
      : 'http://localhost:8080/api/v1/admin/baneUser';

    this.http.post(endpoint, { username: user.username }, { withCredentials: true })
      .subscribe({
        next: () => {
          const wasBanned = user.baned;
          user.baned = !user.baned;

          if (wasBanned) {
            this.data.numberOfBandUser = Math.max(0, this.data.numberOfBandUser - 1);
            this.data.numberOfUnbandUser += 1;
            this.showToast(`User ${user.username} unbanned.`, 'success');
          } else {
            this.data.numberOfBandUser += 1;
            this.data.numberOfUnbandUser = Math.max(0, this.data.numberOfUnbandUser - 1);
            this.showToast(`User ${user.username} banned.`, 'success');
          }
        },
        error: (err) => this.showToast(err?.error?.message || 'Unable to update user status.', 'error')
      });
  }

  deletePost(postId: number) {
    this.http.post('http://localhost:8080/api/v1/admin/deletPost', { post_id: postId }, { withCredentials: true })
      .subscribe({
        next: () => {
          this.data.postReports = this.data.postReports.filter(report => report.postId !== postId);
          this.showToast(`Post #${postId} deleted.`, 'success');
        },
        error: (err) => this.showToast(err?.error?.message || 'Unable to delete post.', 'error')
      });
  }

  private showToast(message: string, type: 'success' | 'error') {
    this.toastMessage = message;
    this.toastType = type;

    setTimeout(() => {
      if (this.toastMessage === message) {
        this.toastMessage = '';
      }
    }, 3000);
  }
}
