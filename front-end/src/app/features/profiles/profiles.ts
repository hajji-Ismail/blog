import { HttpClient, HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommentDto, PostFeedResponse, ProfileDto } from './models/profile.model';
import { CommonModule } from '@angular/common';
import { getProfileImage } from '../../services/profile.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-profiles',
  standalone: true,
  templateUrl: './profile.html',
  styleUrl: './profile.css',
  imports: [CommonModule, FormsModule]
})
export class ProfilesComponent implements OnInit {
  profile?: ProfileDto;
  userName!: string;

  editingPost?: PostFeedResponse;
  editTitle = '';
  editContent = '';
  editFiles: File[] = [];
  editMediaPreview: string[] = [];

  deletingPost?: PostFeedResponse;

  reportMode?: 'user' | 'post';
  reportingPost?: PostFeedResponse;
  reportReason = '';

  toastMessage = '';
  toastType: 'success' | 'error' = 'success';

  commentDraft: Record<number, string> = {};

  private readonly BASE_URL = 'http://localhost:8080/api/v1';

  constructor(
    private http: HttpClient,
    private route: ActivatedRoute
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
          this.profile = {
            ...res,
            post: (res.post ?? []).map(post => ({
              ...post,
              medias: post.medias ?? [],
              display: false,
              comments: []
            }))
          };
        },
        error: () => this.showToast('Unable to load profile.', 'error')
      });
  }

  getProfileImage(url: string | null | undefined) {
    return getProfileImage(url);
  }

  onFollow(username: string) {
    this.http.post(`${this.BASE_URL}/user/profile/follow`, { followed: username, Followed: username }, { withCredentials: true })
      .subscribe({
        next: () => this.loadProfile(),
        error: () => this.showToast('Unable to update follow state.', 'error')
      });
  }

  onReact(post: PostFeedResponse) {
    this.http.post(`${this.BASE_URL}/user/post/react`, post.id, { withCredentials: true })
      .subscribe({
        next: () => {
          if (!this.profile) return;
          this.profile.post = this.profile.post.map(p => {
            if (p.id !== post.id) return p;
            const reacted = !p.reacted;
            return {
              ...p,
              reacted,
              reactionCount: reacted ? p.reactionCount + 1 : p.reactionCount - 1
            };
          });
        },
        error: () => this.showToast('Unable to react to post.', 'error')
      });
  }

  toggleComments(post: PostFeedResponse) {
    if (!this.profile) return;

    this.profile.post = this.profile.post.map(p =>
      p.id === post.id ? { ...p, display: !p.display } : p
    );

    const updated = this.profile.post.find(p => p.id === post.id);
    if (!updated?.display) return;

    this.loadComments(post.id);
  }

  onComment(post: PostFeedResponse) {
    const content = (this.commentDraft[post.id] ?? '').trim();
    if (!content) return;

    this.http.post(`${this.BASE_URL}/user/comment/save`, { postId: post.id, content }, { withCredentials: true })
      .subscribe({
        next: () => {
          this.commentDraft[post.id] = '';

          if (!this.profile) return;
          this.profile.post = this.profile.post.map(p =>
            p.id === post.id ? { ...p, commentCount: p.commentCount + 1 } : p
          );

          if (post.display) {
            this.loadComments(post.id);
          }
        },
        error: () => this.showToast('Unable to post comment.', 'error')
      });
  }

  openReportPost(post: PostFeedResponse) {
    this.reportMode = 'post';
    this.reportingPost = post;
    this.reportReason = '';
  }

  openReportUser() {
    this.reportMode = 'user';
    this.reportingPost = undefined;
    this.reportReason = '';
  }

  closeReportModal() {
    this.reportMode = undefined;
    this.reportingPost = undefined;
    this.reportReason = '';
  }

  submitReport() {
    if (!this.profile || !this.reportMode) return;

    const reason = this.reportReason.trim();
    if (!reason) {
      this.showToast('Please provide a report reason.', 'error');
      return;
    }

    const postId = this.reportMode === 'post' ? this.reportingPost?.id ?? null : null;
    const payload = {
      Username: this.reportMode === 'post' ? this.reportingPost?.username : this.profile.username,
      reason,
      Post_id: postId,
      post_id: postId
    };

    const endpoint = this.reportMode === 'post'
      ? `${this.BASE_URL}/user/report/post`
      : `${this.BASE_URL}/user/report/user`;

    this.http.post(endpoint, payload, { withCredentials: true })
      .subscribe({
        next: () => {
          this.closeReportModal();
          this.showToast('Report submitted successfully.', 'success');
        },
        error: (err) => this.showToast(err?.error?.message || 'Unable to submit report.', 'error')
      });
  }

  openDelete(post: PostFeedResponse) {
    this.deletingPost = post;
  }

  closeDeleteModal() {
    this.deletingPost = undefined;
  }

  confirmDelete() {
    if (!this.deletingPost) return;

    this.http.post(`${this.BASE_URL}/user/post/delete`, this.deletingPost.id, { withCredentials: true })
      .subscribe({
        next: () => {
          this.closeDeleteModal();
          this.showToast('Post deleted.', 'success');
          this.loadProfile();
        },
        error: () => this.showToast('Unable to delete post.', 'error')
      });
  }

  openEdit(post: PostFeedResponse) {
    this.editingPost = post;
    this.editTitle = post.title;
    this.editContent = post.content;
    this.clearEditSelection();
  }

  onEditFilesChange(event: Event) {
    const input = event.target as HTMLInputElement;
    const files = Array.from(input.files ?? []);

    this.clearEditSelection();
    this.editFiles = files;
    this.editMediaPreview = files.map(file => URL.createObjectURL(file));
  }

  saveEdit() {
    if (!this.editingPost) return;

    const title = this.editTitle.trim();
    const content = this.editContent.trim();

    if (!title || !content) {
      this.showToast('Title and content are required.', 'error');
      return;
    }

    const formData = new FormData();
    formData.append('ID', this.editingPost.id.toString());
    formData.append('title', title);
    formData.append('content', content);

    this.editFiles.forEach(file => {
      formData.append('mediaFiles', file);
    });

    this.http.post(`${this.BASE_URL}/user/post/edit`, formData, { withCredentials: true })
      .subscribe({
        next: () => {
          this.cancelEdit();
          this.showToast('Post updated.', 'success');
          this.loadProfile();
        },
        error: () => this.showToast('Unable to update post.', 'error')
      });
  }

  cancelEdit() {
    this.editingPost = undefined;
    this.editTitle = '';
    this.editContent = '';
    this.clearEditSelection();
  }

  private clearEditSelection() {
    this.editFiles = [];
    this.editMediaPreview.forEach(url => URL.revokeObjectURL(url));
    this.editMediaPreview = [];
  }

  private loadComments(postId: number) {
    this.http.get<CommentDto[]>(`${this.BASE_URL}/user/comment/load`, {
      params: { param: postId.toString() },
      withCredentials: true
    }).subscribe({
      next: comments => {
        if (!this.profile) return;
        this.profile.post = this.profile.post.map(p =>
          p.id === postId ? { ...p, comments } : p
        );
      },
      error: () => this.showToast('Unable to load comments.', 'error')
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
