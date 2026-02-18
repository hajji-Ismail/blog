export interface PostFeedDto {
  id: number;
  title: string;
  content: string;
  createdAt: string;
  username: string;
  profileImageUrl: string;
  reactionCount: number;
  commentCount: number;  
  medias: string[]; 
  reacted: boolean
  display: boolean 
  comments : CommentDto[]
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
