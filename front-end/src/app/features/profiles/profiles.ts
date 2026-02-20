import { HttpClient, HttpParams } from '@angular/common/http';
import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommentDto, PostFeedResponse, ProfileDto } from './models/profile.model';
import { CommonModule } from '@angular/common';
import { getProfileImage } from '../../services/profile.service';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-profiles',
  standalone: true,
  templateUrl: './profile.html',
  styleUrl: './profile.css',
  imports: [CommonModule, FormsModule]
})
export class ProfilesComponent implements OnInit {

  profile = signal<ProfileDto | undefined>(undefined);
  loading = signal<boolean>(true);
  userName = signal<string>('');

  editingPost = signal<PostFeedResponse | undefined>(undefined);
  editTitle = signal<string>('');
  editContent = signal<string>('');
  editFiles = signal<File[]>([]);
  editMediaPreview = signal<string[]>([]);

  deletingPost = signal<PostFeedResponse | undefined>(undefined);

  reportMode = signal<'user' | 'post' | undefined>(undefined);
  reportingPost = signal<PostFeedResponse | undefined>(undefined);
  reportReason = signal<string>('');

  toastMessage = signal<string>('');
  toastType = signal<'success' | 'error'>('success');

  commentDraft = signal<Record<number, string>>({});

  private readonly BASE_URL = 'http://localhost:8080/api/v1';

  constructor(
    private http: HttpClient,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.userName.set(params.get('username')!);
      this.profile.set(undefined);
      this.loadProfile();
    });
  }

  loadProfile() {
    this.loading.set(true);

    const params = new HttpParams().set('param', this.userName());

    this.http.get<ProfileDto>(`${this.BASE_URL}/user/profile/profile`, { params, withCredentials: true })
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (res) => {
          this.profile.set({
            ...res,
            post: (res.post ?? []).map(post => ({
              ...post,
              medias: post.medias ?? [],
              display: false,
              comments: []
            }))
          });
        },
        error: () => this.showToast('Unable to load profile.', 'error')
      });
  }

  getProfileImage(url: string | null | undefined) {
    return getProfileImage(url);
  }

  // ===================== FOLLOW =====================
  onFollow(username: string) {
 console.log("cdcdcfddssdfdfdssdsdfqsdfsdfqsdfqsdfqsdf");
 
    this.http.post(`${this.BASE_URL}/user/profile/follow`,
      { followed: username, Followed: username },
      { withCredentials: true }
    ).subscribe({
      next: (res) => {
        console.log(res, "dscdcdcvdfcdcdc");
        
      this.profile.update(current => ({
      ...current!,
      isFollowing: !current!.isFollowing,
      following: current!.following + (current!.isFollowing ? -1 : 1)
    }));

      },
      error: (res) => {
        // console.log(res);
        
        
        this.profile.update(current => ({
          ...current!,
          isFollowing: !current!.isFollowing,
          following: current!.following + (current!.isFollowing ? -1 : 1)
        }));
        this.showToast('Unable to update follow state.', 'error');
      }
    });
  }
updateCommentDraft(postId: number, value: string) {
  this.commentDraft.update(d => ({ ...d, [postId]: value }));
}
  // ===================== REACT =====================
  onReact(post: PostFeedResponse) {
    this.profile.update(current => ({
      ...current!,
      post: current!.post.map(p => {
        if (p.id !== post.id) return p;
        const reacted = !p.reacted;
        return { ...p, reacted, reactionCount: reacted ? p.reactionCount + 1 : p.reactionCount - 1 };
      })
    }));

    this.http.post(`${this.BASE_URL}/user/post/react`, post.id, { withCredentials: true })
      .subscribe({
        next: () => {},
        error: () => {
          // rollback
          this.profile.update(current => ({
            ...current!,
            post: current!.post.map(p => {
              if (p.id !== post.id) return p;
              const reacted = !p.reacted;
              return { ...p, reacted, reactionCount: reacted ? p.reactionCount + 1 : p.reactionCount - 1 };
            })
          }));
          this.showToast('Unable to react to post.', 'error');
        }
      });
  }

  // ===================== COMMENTS =====================
  toggleComments(post: PostFeedResponse) {
    this.profile.update(current => ({
      ...current!,
      post: current!.post.map(p => p.id === post.id ? { ...p, display: !p.display } : p)
    }));

    const updated = this.profile()!.post.find(p => p.id === post.id);
    if (updated?.display) this.loadComments(post.id);
  }

  onComment(post: PostFeedResponse) {
    const draft = this.commentDraft();
    const content = (draft[post.id] ?? '').trim();
    if (!content) return;

    // Optimistic comment count update
    this.profile.update(current => ({
      ...current!,
      post: current!.post.map(p => p.id === post.id ? { ...p, commentCount: p.commentCount + 1 } : p)
    }));
    this.commentDraft.set({ ...draft, [post.id]: '' });

    this.http.post(`${this.BASE_URL}/user/comment/save`,
      { postId: post.id, content },
      { withCredentials: true }
    ).subscribe({
      next: () => {
        if (post.display) this.loadComments(post.id);
      },
      error: () => {
        // rollback
        this.profile.update(current => ({
          ...current!,
          post: current!.post.map(p => p.id === post.id ? { ...p, commentCount: p.commentCount - 1 } : p)
        }));
        this.showToast('Unable to post comment.', 'error');
      }
    });
  }

  // ===================== REPORT =====================
  openReportPost(post: PostFeedResponse) {
    this.reportMode.set('post');
    this.reportingPost.set(post);
    this.reportReason.set('');
  }

  openReportUser() {
    this.reportMode.set('user');
    this.reportingPost.set(undefined);
    this.reportReason.set('');
  }

  closeReportModal() {
    this.reportMode.set(undefined);
    this.reportingPost.set(undefined);
    this.reportReason.set('');
  }

  submitReport() {
    const current = this.profile();
    if (!current || !this.reportMode()) return;

    const reason = this.reportReason().trim();
    if (!reason) {
      this.showToast('Please provide a report reason.', 'error');
      return;
    }

    const postId = this.reportMode() === 'post'
      ? this.reportingPost()?.id ?? null
      : null;

    const payload = {
      Username: this.reportMode() === 'post'
        ? this.reportingPost()?.username
        : current.username,
      reason,
      Post_id: postId,
      post_id: postId
    };

    const endpoint = this.reportMode() === 'post'
      ? `${this.BASE_URL}/user/report/post`
      : `${this.BASE_URL}/user/report/user`;

    this.http.post(endpoint, payload, { withCredentials: true })
      .subscribe({
        next: () => {
          this.closeReportModal();
          this.showToast('Report submitted successfully.', 'success');
        },
        error: () => this.showToast('Unable to submit report.', 'error')
      });
  }

  // ===================== DELETE =====================
  openDelete(post: PostFeedResponse) {
    this.deletingPost.set(post);
  }

  closeDeleteModal() {
    this.deletingPost.set(undefined);
  }

  confirmDelete() {
    const post = this.deletingPost();
    if (!post) return;

    this.http.post(`${this.BASE_URL}/user/post/delete`, post.id, { withCredentials: true })
      .subscribe({
        next: () => {
          this.closeDeleteModal();
          this.showToast('Post deleted.', 'success');
          this.loadProfile();
        },
        error: () => this.showToast('Unable to delete post.', 'error')
      });
  }

  // ===================== EDIT =====================
  openEdit(post: PostFeedResponse) {
    this.editingPost.set(post);
    this.editTitle.set(post.title);
    this.editContent.set(post.content);
    this.clearEditSelection();
  }

  onEditFilesChange(event: Event) {
    const input = event.target as HTMLInputElement;
    const files = Array.from(input.files ?? []);

    this.clearEditSelection();
    this.editFiles.set(files);
    this.editMediaPreview.set(files.map(file => URL.createObjectURL(file)));
  }

  saveEdit() {
    const post = this.editingPost();
    if (!post) return;

    const title = this.editTitle().trim();
    const content = this.editContent().trim();

    if (!title || !content) {
      this.showToast('Title and content are required.', 'error');
      return;
    }

    const formData = new FormData();
    formData.append('ID', post.id.toString());
    formData.append('title', title);
    formData.append('content', content);

    this.editFiles().forEach(file => formData.append('mediaFiles', file));

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
    this.editingPost.set(undefined);
    this.editTitle.set('');
    this.editContent.set('');
    this.clearEditSelection();
  }

  private clearEditSelection() {
    this.editFiles.set([]);
    this.editMediaPreview().forEach(url => URL.revokeObjectURL(url));
    this.editMediaPreview.set([]);
  }

 private loadComments(postId: number) {
  this.http.get<CommentDto[]>(`${this.BASE_URL}/user/comment/load`, {
    params: { param: postId.toString() },
    withCredentials: true
  }).subscribe({
    next: comments => {

      const sorted = [...comments].sort(
        (a, b) =>
          new Date(b.createdAt).getTime() -
          new Date(a.createdAt).getTime()
      );

      this.profile.update(current => ({
        ...current!,
        post: current!.post.map(p =>
          p.id === postId ? { ...p, comments: sorted } : p
        )
      }));
    },
    error: () => this.showToast('Unable to load comments.', 'error')
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