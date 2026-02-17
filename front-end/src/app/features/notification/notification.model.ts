export interface Notification {
  id: number;
  senderUsername: string;
  receiverUsername: string;
  message: string;
  nature: 'report' | 'post';
}
