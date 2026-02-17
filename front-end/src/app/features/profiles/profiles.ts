import { HttpClient, HttpParams } from '@angular/common/http';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProfileDto, PostFeedResponse } from './models/profile.model';
import { CommonModule } from '@angular/common';
import { getProfileImage } from '../../services/profile.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-profiles',
  standalone: true,
  templateUrl: './profile.html',
  imports: [CommonModule, FormsModule]
})
export class ProfilesComponent implements OnInit {
  profile?: ProfileDto;
  userName!: string;

  // For edit modal
  editingPost?: PostFeedResponse;
  editTitle: string = '';
  editContent: string = '';

  private readonly BASE_URL = 'http://localhost:8080/api/v1';

  constructor(
    private http: HttpClient,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.userName = params.get('username')!;
      this.loadProfile();
    });
  }

  loadProfile() {
    const params = new HttpParams().set('param', this.userName);
    this.http.get<ProfileDto>(`${this.BASE_URL}/user/profile/profile`, { params, withCredentials: true })
      .subscribe({
        next: (res) => {
          this.profile = res;
          this.cdr.detectChanges();
        },
        error: err => console.error(err)
      });
  }

  getProfileImage(url: string | null | undefined) {
    return getProfileImage(url);
  }

  onFollow(username: string) {
    this.http.post(`${this.BASE_URL}/user/profile/follow`, { Followed: username }, { withCredentials: true })
      .subscribe(() => this.loadProfile());
  }

  // REPORT POST
  reportPost(post: PostFeedResponse) {
    const reason = prompt("Why are you reporting this post?");
    if (!reason) return;

    const reportPayload = {
      Username: post.username,
      reason: reason,
      post_id: post.id
    };

    this.http.post(`${this.BASE_URL}/user/report/post`, reportPayload, { withCredentials: true })
      .subscribe({
        next: () => alert("Post reported."),
        error: () => alert("Error reporting post.")
      });
  }

  // REPORT USER
  reportUser() {
    if (!this.profile) return;
    const reason = prompt(`Why are you reporting ${this.profile.username}?`);
    if (!reason) return;

    const reportPayload = {
      Username: this.profile.username,
      reason: reason,
      post_id: null
    };

    this.http.post(`${this.BASE_URL}/user/report/user`, reportPayload, { withCredentials: true })
      .subscribe({
        next: () => alert("User reported."),
        error: () => alert("Error reporting user.")
      });
  }

  // DELETE POST
  deletePost(post: PostFeedResponse) {
    if (confirm('Delete this post?')) {
      this.http.post(`${this.BASE_URL}/posts/${post.id}`, { withCredentials: true })
        .subscribe(() => this.loadProfile());
    }
  }

  // EDIT POST LOGIC
  openEdit(post: PostFeedResponse) {
    this.editingPost = post;
    this.editTitle = post.title;
    this.editContent = post.content;
  }

  saveEdit() {
    if (!this.editingPost) return;
    this.http.post(`${this.BASE_URL}/posts/${this.editingPost.id}`, 
      { title: this.editTitle, content: this.editContent }, 
      { withCredentials: true }
    ).subscribe(() => {
      this.editingPost = undefined;
      this.loadProfile();
    });
  }

  cancelEdit() {
    this.editingPost = undefined;
  }
}