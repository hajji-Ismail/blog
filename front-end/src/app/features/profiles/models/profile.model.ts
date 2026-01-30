export interface ProfileDto {
   username:string 
      profileImage:string
  followers: number;
  following: number;
  post: PostFeedResponse[];
    profile : boolean;

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
