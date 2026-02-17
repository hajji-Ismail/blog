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

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.loadNotifications();
  }

  loadNotifications() {
    this.loading = true;
    this.http.get<any>('http://localhost:8080/api/v1/user/notification/load', { withCredentials: true })
      .subscribe({
        next: (res) => {
          console.log(res, "Loaded notifications");
          // Map backend DTO directly
          this.notifications = res.body.map((n: any) => ({
            id: n.id,
            senderUsername: n.senderUsername || 'Unknown',
            receiverUsername: n.receiverUsername || 'Unknown',
            message: n.message,
            nature: n.nature
          }));
          this.loading = false;
        },
        error: (err) => {
          this.loading = false;
          console.error(err, "Failed to load notifications");
          alert('Failed to load notifications');
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

}
