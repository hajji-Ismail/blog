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
  // Signals
  loading = signal(true);
  error = signal('');
  toastMessage = signal('');
  toastType = signal<'success' | 'error'>('success');

  data = signal<AdminResponse>({
    numberOfBandUser: 0,
    numberOfUnbandUser: 0,
    users: [],
    postReports: [],
    userReports: [],
  });

  selectedView = signal<'post' | 'user'>('post');

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    console.log('Component initialized');
    this.loadAdminData();
  }

  loadAdminData(): void {
    this.loading.set(true);
    this.error.set('');

    this.http
      .get<AdminResponse>('http://localhost:8080/api/v1/admin/load', { withCredentials: true })
      .subscribe({
        next: (value) => {
          console.log('Admin data loaded', value);

          this.data.set({
            ...value,
            postReports: value.postReports ?? value.postRerports ?? [],
            userReports: value.userReports ?? [],
            users: value.users ?? [],
          });

          this.loading.set(false);
        },
        error: () => {
          this.loading.set(false);
          this.error.set('Failed to load admin data');
        },
      });
  }

  select(view: 'post' | 'user'): void {
    this.selectedView.set(view);
  }

  get totalUsers(): number {
    return this.data().numberOfBandUser + this.data().numberOfUnbandUser;
  }

  get unbannedPercentage(): number {
    const total = this.totalUsers;
    return total === 0 ? 0 : Math.round((this.data().numberOfUnbandUser / total) * 100);
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

          const currentData = { ...this.data() };

          if (wasBanned) {
            currentData.numberOfBandUser = Math.max(0, currentData.numberOfBandUser - 1);
            currentData.numberOfUnbandUser += 1;
            this.showToast(`User ${user.username} unbanned.`, 'success');
          } else {
            currentData.numberOfBandUser += 1;
            currentData.numberOfUnbandUser = Math.max(0, currentData.numberOfUnbandUser - 1);
            this.showToast(`User ${user.username} banned.`, 'success');
          }

          // Update the signal
          this.data.set(currentData);
        },
        error: (err) => this.showToast(err?.error?.message || 'Unable to update user status.', 'error'),
      });
  }

  deletePost(postId: number) {
    this.http.post('http://localhost:8080/api/v1/admin/deletPost', { post_id: postId }, { withCredentials: true })
      .subscribe({
        next: () => {
          const currentData = { ...this.data() };
          currentData.postReports = currentData.postReports.filter(report => report.postId !== postId);
          this.data.set(currentData);
          this.showToast(`Post #${postId} deleted.`, 'success');
        },
        error: (err) => this.showToast(err?.error?.message || 'Unable to delete post.', 'error'),
      });
  }

  private showToast(message: string, type: 'success' | 'error') {
    this.toastMessage.set(message);
    this.toastType.set(type);

    setTimeout(() => {
      if (this.toastMessage() === message) {
        this.toastMessage.set('');
      }
    }, 3000);
  }
}