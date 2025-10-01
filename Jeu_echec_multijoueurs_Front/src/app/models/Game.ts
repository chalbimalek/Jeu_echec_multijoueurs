export interface Game {
  id: number;
  whitePlayer: string;
  blackPlayer: string;
  status: 'WAITING' | 'ACTIVE' | 'FINISHED' | 'ABANDONED';
  winner?: string;
}
