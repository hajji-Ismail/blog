export interface ProfileDto {
  username: string;
  profileImage: string;
  followers: number;
  following: number;
  post: PostFeedResponse[];
  profile: boolean; // True if this profile belongs to the logged-in user
}

export interface PostFeedResponse {
  id: number;
  title: string;
  content: string;
  username: string;
  profileImageUrl: string;
  reactionCount: number;
  commentCount: number;
  createdAt: string;
  medias: string[];
  reacted: boolean;
}

export interface ReportDto {
  Username: string; 
  reason: string;    
  post_id: number | null;
}