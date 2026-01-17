import { HttpClient, HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProfileDto } from './models/profile.model';
import { CommonModule, DatePipe } from '@angular/common';

@Component({
  selector: 'app-profiles',
  templateUrl: './profile.html',
  imports:[DatePipe, CommonModule]
})
export class ProfilesComponent implements OnInit {

  userName!: string;
  profile?: ProfileDto; // store the fetched profile for template binding

  constructor(
    private http: HttpClient,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.userName = params.get('username')!;

      const httpParams = new HttpParams().set('param', this.userName);

      this.http.get<ProfileDto>(
        'http://localhost:8080/api/v1/user/profile/profile',
        { params: httpParams, withCredentials: true }
      ).subscribe({
        next: (res: ProfileDto) => {
          this.profile = res; // store result for template
          console.log('Profile fetched:', res);
        },
        error: (err) => {
          console.error('Failed to fetch profile:', err);
        }
      });
    });
  }
}
