export interface PostFeedDto {
  id: number;
  title: string;
  content: string;
  createdAt: string;
  username: string;
  profileImageUrl: string;
  reactionCount: number;
  commentCount: number;  
  medias: { media: string }[]; 
}