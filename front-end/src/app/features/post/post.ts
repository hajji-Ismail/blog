import { Component, signal, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { PostFeedDto } from './models/Post.models';
import { DatePipe } from '@angular/common';


@Component({
  selector: 'app-posts',
  templateUrl: 'post.html',
    imports: [DatePipe],
})
export class Posts implements OnInit {
  posts = signal<PostFeedDto[]>([]);

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<PostFeedDto[]>('http://localhost:8080/api/v1/user/post/load', { withCredentials: true })
      .subscribe({
        next: data => {console.log(data, "fdfd");
        
          this.posts.set(data)}
        ,
        error: err => {
          console.error(err);
          this.posts.set([]);
        }
      });
  }
}
