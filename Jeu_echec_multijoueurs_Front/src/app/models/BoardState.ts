import { ChessPiece } from "./ChessPiece";

export interface BoardState {
  [position: string]: ChessPiece;
}
