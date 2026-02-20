import { Component, OnInit, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Notification } from './notification.model';
import { CommonModule } from '@angular/common';
import { getProfileImage } from '../../services/profile.service';

@Component({
  selector: 'app-notification',
  standalone: true,
  templateUrl: './notification.component.html',
  imports: [CommonModule]
})
export class NotificationComponent implements OnInit {

  notifications = signal<Notification[]>([]);
  loading = signal<boolean>(false);
  error = signal<string>('');

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.loadNotifications();
  }

  loadNotifications() {
    this.loading.set(true);
    this.error.set('');

    this.http.get<any>('http://localhost:8080/api/v1/user/notification/load', { withCredentials: true })
      .subscribe({
        next: (res) => {
          try {
            const body = Array.isArray(res?.body)
              ? res.body
              : (Array.isArray(res) ? res : []);

            const mapped: Notification[] = body.map((n: any) => ({
              id: n.id,
              senderUsername: n.senderUsername || 'Unknown',
              receiverUsername: n.receiverUsername || 'Unknown',
              message: n.message,
              nature: n.nature,
              read: !!n.read
            }));

            this.notifications.set(mapped);

          } catch (e) {
            console.error(e, 'Failed to parse notifications response');
            this.notifications.set([]);
            this.error.set('Failed to parse notifications');
          } finally {
            this.loading.set(false);
          }
        },
        error: (err) => {
          this.loading.set(false);
          console.error(err, "Failed to load notifications");
          this.error.set(err?.error?.message || 'Failed to load notifications');
        }
      });
  }

  onNotificationClick(n: Notification) {
    if (n.nature === 'report') {
      this.router.navigate(['/admin']);
    } else if (n.nature === 'post') {
      this.router.navigate(['/']);
    }
  }

  getProfileImage(url: string | null | undefined) {
      return getProfileImage(url);
    }
  markAsRead(n: Notification, event: MouseEvent) {
    event.stopPropagation();
    if (n.read) return;

    this.http.post<any>(
      'http://localhost:8080/api/v1/user/notification/mark-read',
      n.id,
      { withCredentials: true }
    ).subscribe({
      next: () => {
        this.notifications.update(items =>
          items.map(item =>
            item.id === n.id ? { ...item, read: true } : item
          )
        );
      },
      error: (err) => console.error(err, 'Failed to mark notification as read')
    });
  }
}