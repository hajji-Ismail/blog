export interface ProfileDto {
  username: string;
  profileImage: string;
  followers: number;
  following: number;
  post: PostFeedResponse[];
  profile: boolean; // True if this profile belongs to the logged-in user
  isFollowing: boolean; // True if logged-in user follows this profile
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
  display: boolean;
  comments: CommentDto[];
}

export interface ReportDto {
  Username: string; 
  reason: string;    
  post_id: number | null;
}

export interface CommentDto {
  id: number;
  postId: number;
  userId: number;
  content: string;
  createdAt: string;
  username?: string;
  profileImageUrl?: string;
}
