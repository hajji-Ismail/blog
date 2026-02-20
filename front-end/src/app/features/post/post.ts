import { Component, signal, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule, DatePipe } from '@angular/common';
import { PostFeedDto, CommentDto } from './models/Post.models';
import { getProfileImage } from '../../services/profile.service';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

interface CurrentUserDto {
  username: string;
}

@Component({
  selector: 'app-posts',
  templateUrl: './post.html',
  imports: [CommonModule, FormsModule, DatePipe, RouterModule],
  standalone: true,
})
export class Posts implements OnInit {

  posts = signal<PostFeedDto[]>([]);

  // ----- REPORT POST -----
  reportPostId = signal<number | null>(null);
  reportReason = '';

  currentUsername = '';
  editingCommentId: number | null = null;
  editCommentContent = '';

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.loadCurrentUser();
    this.loadPosts();
  }

  loadCurrentUser() {
    this.http
      .get<CurrentUserDto>('http://localhost:8080/api/v1/user/profile/user', {
        withCredentials: true,
      })
      .subscribe({
        next: data => {
          this.currentUsername = data.username;
        },
        error: err => console.error(err),
      });
  }

  loadPosts() {
    this.http
      .get<PostFeedDto[]>(
        'http://localhost:8080/api/v1/user/post/load',
        { withCredentials: true }
      )
      .subscribe({
        next: data => this.posts.set(data),
        error: err => {
          console.error(err);
          this.posts.set([]);
        },
      });
  }

  getProfileImage(url: string | null | undefined) {
    return getProfileImage(url);
  }

  trackByPost(index: number, post: PostFeedDto) {
    return post.id;
  }

  // ---------------- COMMENTS ----------------
  onComment(postId: number, input: HTMLInputElement) {
    const content = input.value.trim();
    if (!content) return;

    this.http
      .post(
        'http://localhost:8080/api/v1/user/comment/save',
        { postId, content },
        { withCredentials: true }
      )
      .subscribe({
        next: () => {
          this.posts.update(posts =>
            posts.map(p =>
              p.id === postId
                ? { ...p, commentCount: p.commentCount + 1 }
                : p
            )
          );
          input.value = '';
          this.loadComments(postId);
        },
        error: err => console.error(err),
      });
  }

  onDisplay(postId: number) {
    this.posts.update(posts =>
      posts.map(post => {
        if (post.id !== postId) return post;

        const display = !post.display;

        if (display) {
          this.loadComments(postId);
        }

        return { ...post, display };
      })
    );
  }

  startEditComment(comment: CommentDto) {
    this.editingCommentId = comment.id;
    this.editCommentContent = comment.content;
  }

  cancelEditComment() {
    this.editingCommentId = null;
    this.editCommentContent = '';
  }

  saveEditComment(postId: number) {
    if (!this.editingCommentId) return;
    const content = this.editCommentContent.trim();
    if (!content) return;

    this.http
      .post(
        'http://localhost:8080/api/v1/user/comment/edit',
        { id: this.editingCommentId, content },
        { withCredentials: true }
      )
      .subscribe({
        next: () => {
          this.cancelEditComment();
          this.loadComments(postId);
        },
        error: err => console.error(err),
      });
  }

  deleteComment(postId: number, commentId: number) {
    this.http
      .post(
        'http://localhost:8080/api/v1/user/comment/delete',
        commentId,
        { withCredentials: true }
      )
      .subscribe({
        next: () => {
          this.posts.update(posts =>
            posts.map(p =>
              p.id === postId
                ? { ...p, commentCount: Math.max(0, p.commentCount - 1) }
                : p
            )
          );
          this.cancelEditComment();
          this.loadComments(postId);
        },
        error: err => console.error(err),
      });
  }

  // ---------------- REACT ----------------
  onReact(postId: number) {
    this.http
      .post(
        'http://localhost:8080/api/v1/user/post/react',
        postId,
        { withCredentials: true }
      )
      .subscribe({
        next: () => {
          this.posts.update(posts =>
            posts.map(post => {
              if (post.id !== postId) return post;

              const reacted = !post.reacted;
              return {
                ...post,
                reacted,
                reactionCount: reacted
                  ? post.reactionCount + 1
                  : post.reactionCount - 1,
              };
            })
          );
        },
        error: err => console.error(err),
      });
  }

  // ---------------- REPORT POST ----------------
  openReport(postId: number) {
    this.reportPostId.set(postId);
    this.reportReason = '';
  }

  closeReport() {
    this.reportPostId.set(null);
  }

  submitReport() {
    if (!this.reportPostId()) return;

    this.http
      .post(
        'http://localhost:8080/api/v1/user/report/post',
        { Post_id: this.reportPostId(), reason: this.reportReason },
        { withCredentials: true }
      )
      .subscribe({
        next: () => this.closeReport(),
        error: err => console.error(err),
      });
  }

  private loadComments(postId: number) {
    this.http
      .get<CommentDto[]>(
        'http://localhost:8080/api/v1/user/comment/load',
        {
          params: { param: postId.toString() },
          withCredentials: true,
        }
      )
      .subscribe({
        next: comments => {

        const sorted = [...comments].sort(
          (a, b) =>
            new Date(b.createdAt).getTime() -
            new Date(a.createdAt).getTime()
        );

        this.posts.update(inner =>
          inner.map(p =>
            p.id === postId ? { ...p, comments: sorted } : p
          )
        );
      },
        error: err => console.error(err),
      });
}
}
