import { Injectable } from '@angular/core';
import { BoardState } from '../models/BoardState';
import { ChessPiece } from '../models/ChessPiece';

@Injectable({
  providedIn: 'root'
})
export class ChessLogicService {

  createInitialBoard(): BoardState {
    const board: BoardState = {};

    // Pions blancs
    for (let i = 0; i < 8; i++) {
      const col = String.fromCharCode(97 + i);
      board[`${col}2`] = { type: 'PAWN', color: 'WHITE', position: `${col}2` };
    }

    // Pions noirs
    for (let i = 0; i < 8; i++) {
      const col = String.fromCharCode(97 + i);
      board[`${col}7`] = { type: 'PAWN', color: 'BLACK', position: `${col}7` };
    }

    // Pièces blanches
    board['a1'] = { type: 'ROOK', color: 'WHITE', position: 'a1' };
    board['b1'] = { type: 'KNIGHT', color: 'WHITE', position: 'b1' };
    board['c1'] = { type: 'BISHOP', color: 'WHITE', position: 'c1' };
    board['d1'] = { type: 'QUEEN', color: 'WHITE', position: 'd1' };
    board['e1'] = { type: 'KING', color: 'WHITE', position: 'e1' };
    board['f1'] = { type: 'BISHOP', color: 'WHITE', position: 'f1' };
    board['g1'] = { type: 'KNIGHT', color: 'WHITE', position: 'g1' };
    board['h1'] = { type: 'ROOK', color: 'WHITE', position: 'h1' };

    // Pièces noires
    board['a8'] = { type: 'ROOK', color: 'BLACK', position: 'a8' };
    board['b8'] = { type: 'KNIGHT', color: 'BLACK', position: 'b8' };
    board['c8'] = { type: 'BISHOP', color: 'BLACK', position: 'c8' };
    board['d8'] = { type: 'QUEEN', color: 'BLACK', position: 'd8' };
    board['e8'] = { type: 'KING', color: 'BLACK', position: 'e8' };
    board['f8'] = { type: 'BISHOP', color: 'BLACK', position: 'f8' };
    board['g8'] = { type: 'KNIGHT', color: 'BLACK', position: 'g8' };
    board['h8'] = { type: 'ROOK', color: 'BLACK', position: 'h8' };

    return board;
  }

  isValidMove(from: string, to: string, board: BoardState, currentColor: 'WHITE' | 'BLACK'): boolean {
    const piece = board[from];

    if (!piece) return false;
    if (piece.color !== currentColor) return false;

    const targetPiece = board[to];
    if (targetPiece && targetPiece.color === piece.color) return false;

    const fromCol = from.charCodeAt(0) - 97;
    const fromRow = parseInt(from[1]) - 1;
    const toCol = to.charCodeAt(0) - 97;
    const toRow = parseInt(to[1]) - 1;

    const colDiff = toCol - fromCol;
    const rowDiff = toRow - fromRow;

    switch (piece.type) {
      case 'PAWN':
        return this.isValidPawnMove(piece, fromRow, fromCol, toRow, toCol, rowDiff, colDiff, targetPiece);
      case 'KNIGHT':
        return this.isValidKnightMove(colDiff, rowDiff);
      case 'BISHOP':
        return this.isValidBishopMove(colDiff, rowDiff, board, from, to);
      case 'ROOK':
        return this.isValidRookMove(colDiff, rowDiff, board, from, to);
      case 'QUEEN':
        return this.isValidQueenMove(colDiff, rowDiff, board, from, to);
      case 'KING':
        return this.isValidKingMove(colDiff, rowDiff);
      default:
        return false;
    }
  }

  private isValidPawnMove(
    piece: ChessPiece, fromRow: number, fromCol: number,
    toRow: number, toCol: number, rowDiff: number,
    colDiff: number, targetPiece?: ChessPiece
  ): boolean {
    const direction = piece.color === 'WHITE' ? 1 : -1;

    if (colDiff === 0 && !targetPiece) {
      if (rowDiff === direction) return true;
      if (!piece.hasMoved && rowDiff === 2 * direction) return true;
    }

    if (Math.abs(colDiff) === 1 && rowDiff === direction && targetPiece) {
      return true;
    }

    return false;
  }

  private isValidKnightMove(colDiff: number, rowDiff: number): boolean {
    return (Math.abs(colDiff) === 2 && Math.abs(rowDiff) === 1) ||
           (Math.abs(colDiff) === 1 && Math.abs(rowDiff) === 2);
  }

  private isValidBishopMove(
    colDiff: number, rowDiff: number,
    board: BoardState, from: string, to: string
  ): boolean {
    if (Math.abs(colDiff) !== Math.abs(rowDiff)) return false;
    return !this.isPathBlocked(from, to, board);
  }

  private isValidRookMove(
    colDiff: number, rowDiff: number,
    board: BoardState, from: string, to: string
  ): boolean {
    if (colDiff !== 0 && rowDiff !== 0) return false;
    return !this.isPathBlocked(from, to, board);
  }

  private isValidQueenMove(
    colDiff: number, rowDiff: number,
    board: BoardState, from: string, to: string
  ): boolean {
    const isStraight = colDiff === 0 || rowDiff === 0;
    const isDiagonal = Math.abs(colDiff) === Math.abs(rowDiff);

    if (!isStraight && !isDiagonal) return false;
    return !this.isPathBlocked(from, to, board);
  }

  private isValidKingMove(colDiff: number, rowDiff: number): boolean {
    return Math.abs(colDiff) <= 1 && Math.abs(rowDiff) <= 1;
  }

  private isPathBlocked(from: string, to: string, board: BoardState): boolean {
    const fromCol = from.charCodeAt(0) - 97;
    const fromRow = parseInt(from[1]) - 1;
    const toCol = to.charCodeAt(0) - 97;
    const toRow = parseInt(to[1]) - 1;

    const colStep = toCol > fromCol ? 1 : toCol < fromCol ? -1 : 0;
    const rowStep = toRow > fromRow ? 1 : toRow < fromRow ? -1 : 0;

    let currentCol = fromCol + colStep;
    let currentRow = fromRow + rowStep;

    while (currentCol !== toCol || currentRow !== toRow) {
      const pos = String.fromCharCode(97 + currentCol) + (currentRow + 1);
      if (board[pos]) return true;

      currentCol += colStep;
      currentRow += rowStep;
    }

    return false;
  }

  getPieceSymbol(piece: ChessPiece): string {
    const symbols: { [key: string]: string } = {
      'WHITE_PAWN': '♙',
      'WHITE_KNIGHT': '♘',
      'WHITE_BISHOP': '♗',
      'WHITE_ROOK': '♖',
      'WHITE_QUEEN': '♕',
      'WHITE_KING': '♔',
      'BLACK_PAWN': '♟',
      'BLACK_KNIGHT': '♞',
      'BLACK_BISHOP': '♝',
      'BLACK_ROOK': '♜',
      'BLACK_QUEEN': '♛',
      'BLACK_KING': '♚'
    };

    return symbols[`${piece.color}_${piece.type}`] || '';
  }
}
