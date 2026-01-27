import { Component, signal, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule, DatePipe } from '@angular/common';
import { PostFeedDto, CommentDto } from './models/Post.models';
import { getProfileImage } from '../../services/profile.service';

@Component({
  selector: 'app-posts',
  templateUrl: 'post.html',
  imports: [CommonModule, DatePipe],
})
export class Posts implements OnInit {

  posts = signal<PostFeedDto[]>([]);

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
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
                this.posts.update(inner =>
                  inner.map(p =>
                    p.id === postId ? { ...p, comments } : p
                  )
                );
              },
              error: err => console.error(err),
            });
        }

        return { ...post, display };
      })
    );
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

  // ---------------- MENU ACTIONS ----------------

  onEdit(postId: number) {
    console.log('Edit post', postId);
    // open modal or navigate
  }

  onDelete(postId: number) {
    if (!confirm('Delete this post?')) return;

    this.http
      .delete(
        `http://localhost:8080/api/v1/user/post/delete/${postId}`,
        { withCredentials: true }
      )
      .subscribe({
        next: () => {
          this.posts.update(posts =>
            posts.filter(p => p.id !== postId)
          );
        },
        error: err => console.error(err),
      });
  }

  onReport(postId: number) {
    this.http
      .post(
        'http://localhost:8080/api/v1/user/post/report',
        { postId },
        { withCredentials: true }
      )
      .subscribe({
        next: () => alert('Post reported'),
        error: err => console.error(err),
      });
  }
}
