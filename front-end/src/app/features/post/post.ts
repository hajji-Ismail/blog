import { Component, signal, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommentDto, PostFeedDto } from './models/Post.models';
import { CommonModule, DatePipe } from '@angular/common';
import { getProfileImage } from '../../services/profile.service';


@Component({
  selector: 'app-posts',
  templateUrl: 'post.html',
  imports: [DatePipe, CommonModule],
})
export class Posts implements OnInit {
  posts = signal<PostFeedDto[]>([]);

 

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.http.get<PostFeedDto[]>('http://localhost:8080/api/v1/user/post/load', { withCredentials: true })
      .subscribe({
        next: data => {

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
    posts.map(post => {
      if (post.id !== postId) return post;
      const reacted = !post.reacted; 
      post.reacted = !post.reacted; 
      const newCount = reacted 
        ? post.reactionCount + 1  
        : post.reactionCount - 1; 

      return { 
        ...post, 
        reactionCount: newCount,
        userReacted: reacted
      };
    })
  );
}
,
        error: err => console.error(err),
      });
  }
onDisplay(postId: number) {
  this.posts.update(posts =>
    posts.map(post => {
      if (post.id !== postId) return post;

      // Toggle display
      const newDisplay = !post.display;

      // If now showing, fetch comments
      if (newDisplay) {
       this.http.get<CommentDto[]>(
    "http://localhost:8080/api/v1/user/comment/load",
    {
      params: { param: postId.toString() }, // query param as string
      withCredentials: true
    }
  ).subscribe({
          next: comments => {
            console.log(comments, "khgvxjhsxvkjdhvcjhdvjkchhhhhhhhhhhhhhhhhhhhhhhhhh");
            
            // Update the post with loaded comments
            this.posts.update(innerPosts =>
              innerPosts.map(p =>
                p.id === postId ? { ...p, comments } : p
              )
            );
          },
          error: err => console.error(err)
        });
      }

      return {
        ...post,
        display: newDisplay
      };
    })
  );
}

}
