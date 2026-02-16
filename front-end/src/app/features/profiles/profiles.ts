import { HttpClient, HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProfileDto } from './models/profile.model';
import { CommonModule, DatePipe } from '@angular/common';
import { switchMap } from 'rxjs/operators';
import { getProfileImage } from '../../services/profile.service';

@Component({
  selector: 'app-profiles',
  standalone: true,
  templateUrl: './profile.html',
  imports: [CommonModule, DatePipe]
})
export class ProfilesComponent implements OnInit {

  userName!: string;
  profile?: ProfileDto;

  constructor(
    private http: HttpClient,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.paramMap.pipe(
      switchMap(params => {

        // Clear old profile to force re-render
        this.profile = undefined;

        this.userName = params.get('username')!;
        const httpParams = new HttpParams().set('param', this.userName);

        return this.http.get<ProfileDto>(
          'http://localhost:8080/api/v1/user/profile/profile',
          { params: httpParams, withCredentials: true }
        );
      })
    ).subscribe({
      next: (res) => {
        this.profile = res;
      },
      error: (err) => {
        console.error('Failed to fetch profile:', err);
      }
    });
  }

  getProfileImage(url: string | null | undefined) {
    return getProfileImage(url);
  }

  onfollow(Followed: string) {
    this.http.post(
      'http://localhost:8080/api/v1/user/profile/follow',
      { Followed },
      { withCredentials: true }
    ).subscribe({
      next: () => {
        console.log('Followed successfully');
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  trackByPost(index: number, post: any) {
    return post.id;
  }
}
