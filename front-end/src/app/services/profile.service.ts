export function  getProfileImage(url: string | null | undefined): string {
  
  if (!url || url.trim() === '') {
    
    return 'assets/images/h.jpeg';
  }
  return url;
}