export interface Invitation {
  id: number;
  fromUsername: string;
  toUsername: string;
  status: 'PENDING' | 'ACCEPTED' | 'REJECTED';
}
