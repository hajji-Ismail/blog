import { Component, signal, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { PostFeedDto } from './models/Post.models';
import { DatePipe } from '@angular/common';
import { getProfileImage } from '../../services/profile.service';


@Component({
  selector: 'app-posts',
  templateUrl: 'post.html',
  imports: [DatePipe],
})
export class Posts implements OnInit {
  posts = signal<PostFeedDto[]>([]);

 
  display = signal<boolean>(false);

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.http.get<PostFeedDto[]>('http://localhost:8080/api/v1/user/post/load', { withCredentials: true })
      .subscribe({
        next: data => {
          console.log(data, "fdfd");

          this.posts.set(data)
        }
        ,
        error: err => {
          console.error(err);
          this.posts.set([]);
        }
      });
  }
  getProfileImage(url: string | null | undefined) {
    return getProfileImage(url);
  }

  onDesplay() {

    this.display.update(status => !status);
  }
  onComment(postId: number, content: string) {
    this.http
      .post(
        'http://localhost:8080/api/v1/user/comment/save',
        { postId, content },
        { withCredentials: true }
      )
      .subscribe({
        next: _ => {
          // ✅ update ONLY the matching post
          this.posts.update(posts =>
            posts.map(post =>
              post.id === postId
                ? { ...post, commentCount: post.commentCount + 1 }
                : post
            )
          );
        },
        error: err => console.error(err),
      });
  }
  onReact(postId: number){
      this.http
      .post(
        'http://localhost:8080/api/v1/user/post/react',
         postId,
        { withCredentials: true }
      )
      .subscribe({
        next: _ => {
          this.posts.update(posts =>
            posts.map(post =>
              post.id === postId
                ? { ...post, reactionCount: post.reactionCount + 1 }
                : post
            )
          );
        },
        error: err => console.error(err),
      });
  }
}
