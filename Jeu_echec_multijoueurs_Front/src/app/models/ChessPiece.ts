import { PieceType } from "./Move";

export interface ChessPiece {
  type: PieceType;
  color: 'WHITE' | 'BLACK';
  position: string;
  hasMoved?: boolean;
}
