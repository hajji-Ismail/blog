import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Notification } from './notification.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  imports:[CommonModule]
})
export class NotificationComponent implements OnInit {

  notifications: Notification[] = [];
  loading = false;
  error = '';

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.loadNotifications();
  }

  loadNotifications() {
    this.loading = true;
    this.error = '';
    this.http.get<any>('http://localhost:8080/api/v1/user/notification/load', { withCredentials: true })
      .subscribe({
        next: (res) => {
          try {
            const body = Array.isArray(res?.body)
              ? res.body
              : (Array.isArray(res) ? res : []);

            this.notifications = body.map((n: any) => ({
              id: n.id,
              senderUsername: n.senderUsername || 'Unknown',
              receiverUsername: n.receiverUsername || 'Unknown',
              message: n.message,
              nature: n.nature,
              read: !!n.read
            }));
          } catch (e) {
            console.error(e, 'Failed to parse notifications response');
            this.notifications = [];
            this.error = 'Failed to parse notifications';
          } finally {
            this.loading = false;
          }
        },
        error: (err) => {
          this.loading = false;
          console.error(err, "Failed to load notifications");
          this.error = err?.error?.message || 'Failed to load notifications';
        }
      });
  }

  onNotificationClick(n: Notification) {
    if (n.nature === 'report') {
      this.router.navigate(['/admin']); // go to admin dashboard
    } else if (n.nature === 'post') {
      this.router.navigate(['/']); // go to home page
    }
  }

  getProfileImage(username: string): string {
    return `https://ui-avatars.com/api/?name=${username}&background=random`;
  }

  markAsRead(n: Notification, event: MouseEvent) {
    event.stopPropagation();
    if (n.read) return;

    this.http.post<any>('http://localhost:8080/api/v1/user/notification/mark-read', n.id, { withCredentials: true })
      .subscribe({
        next: () => {
          this.notifications = this.notifications.map(item =>
            item.id === n.id ? { ...item, read: true } : item
          );
        },
        error: (err) => console.error(err, 'Failed to mark notification as read')
      });
  }

}
