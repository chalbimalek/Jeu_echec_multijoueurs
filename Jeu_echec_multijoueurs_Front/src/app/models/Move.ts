export type PieceType = 'PAWN' | 'KNIGHT' | 'BISHOP' | 'ROOK' | 'QUEEN' | 'KING';

export interface Move {
  gameId?: number;
  moveNumber?: number;
  from: string;
  to: string;
  piece: PieceType;
  color: 'WHITE' | 'BLACK';
  capturedPiece?: PieceType;
}
