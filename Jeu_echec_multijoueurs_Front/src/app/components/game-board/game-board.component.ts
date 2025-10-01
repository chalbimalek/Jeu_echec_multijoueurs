import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { GameService } from '../../services/game.service';
import { ChessLogicService } from '../../services/chess-logic.service';
import { AuthService } from '../../services/auth.service';
import { BoardState } from '../../models/BoardState';
import { Game } from '../../models/Game';
import { Move } from '../../models/Move';
import { WebSocketService } from '../../services/web-socket.service';
import { ChessPiece } from '../../models/ChessPiece';

@Component({
  selector: 'app-game-board',
  templateUrl: './game-board.component.html',
  styleUrls: ['./game-board.component.css']
})
export class GameBoardComponent implements OnInit, OnDestroy {
  gameId!: number;
  game!: Game;
  board: BoardState = {};
  selectedSquare: string | null = null;
  currentTurn: 'WHITE' | 'BLACK' = 'WHITE';
  playerColor: 'WHITE' | 'BLACK' = 'WHITE';
  currentUsername: string = '';
  moveHistory: Move[] = [];
  isGameOver = false;
  winner: string | null = null;

  files = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'];
  ranks = [8, 7, 6, 5, 4, 3, 2, 1];

  private destroy$ = new Subject<void>();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private gameService: GameService,
    private wsService: WebSocketService,
    private chessLogic: ChessLogicService,
    private authService: AuthService
  ) {}

  async ngOnInit(): Promise<void> {

  this.currentUsername = (await this.authService.getLoggedInUsername()) || '';

    this.route.params.subscribe(params => {
      this.gameId = +params['id'];
      this.loadGame();
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

loadGame(): void {
    this.gameService.getActiveGame().subscribe({
      next: (response) => {
        if (response.id) {
          this.game = response as Game;

          if (this.game.status === 'FINISHED') {
            this.isGameOver = true;
this.winner = this.game.winner ?? null;
          }

          this.determinePlayerColor();
          this.loadMoves();
          this.setupWebSocket();
        }
      },
      error: (err) => {
        console.error('Erreur chargement partie:', err);
        this.router.navigate(['/lobby']);
      }
    });
}

  determinePlayerColor(): void {
    this.playerColor = this.game.whitePlayer === this.currentUsername ? 'WHITE' : 'BLACK';
  }

  loadMoves(): void {
    this.gameService.getGameMoves(this.gameId).subscribe({
      next: (moves) => {
        this.moveHistory = moves;
        this.replayMoves();
      },
      error: (err) => console.error('Erreur chargement coups:', err)
    });
  }

  replayMoves(): void {
    this.board = this.chessLogic.createInitialBoard();

    this.moveHistory.forEach(move => {
      this.applyMove(move.from, move.to);
    });

    this.currentTurn = this.moveHistory.length % 2 === 0 ? 'WHITE' : 'BLACK';
  }

  setupWebSocket(): void {
    this.wsService.subscribeToGame(this.gameId);

    this.wsService.moves$
      .pipe(takeUntil(this.destroy$))
      .subscribe(move => {
        this.applyMove(move.from, move.to);
        this.moveHistory.push(move);
        this.currentTurn = this.currentTurn === 'WHITE' ? 'BLACK' : 'WHITE';

                this.checkGameOver();

      });

    this.wsService.gameFinish$
      .pipe(takeUntil(this.destroy$))
      .subscribe(data => {
        this.isGameOver = true;
        this.winner = data.winner;
      });
  }

  onSquareClick(file: string, rank: number): void {
    if (this.isGameOver) return;
    if (this.currentTurn !== this.playerColor) return;

    const position = `${file}${rank}`;

    if (this.selectedSquare) {
      this.attemptMove(this.selectedSquare, position);
      this.selectedSquare = null;
    } else {
      const piece = this.board[position];
      if (piece && piece.color === this.playerColor) {
        this.selectedSquare = position;
      }
    }
  }

  attemptMove(from: string, to: string): void {
    if (!this.chessLogic.isValidMove(from, to, this.board, this.currentTurn)) {
      return;
    }

    const piece = this.board[from];
    const capturedPiece = this.board[to];

    const move: Move = {
      from,
      to,
      piece: piece.type,
      color: piece.color,
      capturedPiece: capturedPiece?.type
    };

    this.wsService.sendMove(this.gameId, move);
    // this.applyMove(from, to);
    // this.moveHistory.push(move);
    // this.currentTurn = this.currentTurn === 'WHITE' ? 'BLACK' : 'WHITE';

    this.checkGameOver();
  }

  applyMove(from: string, to: string): void {
    const piece = this.board[from];
    if (piece) {
      piece.position = to;
      piece.hasMoved = true;
      this.board[to] = piece;
      delete this.board[from];
    }
  }

  checkGameOver(): void {
    const hasWhiteKing = Object.values(this.board).some(p => p.type === 'KING' && p.color === 'WHITE');
    const hasBlackKing = Object.values(this.board).some(p => p.type === 'KING' && p.color === 'BLACK');

    if (!hasWhiteKing) {
      this.endGame(this.game.blackPlayer);
    } else if (!hasBlackKing) {
      this.endGame(this.game.whitePlayer);
    }
  }

endGame(winner: string): void {
    if (this.isGameOver) return; // Protection contre double appel

    this.isGameOver = true;
    this.winner = winner;
    this.wsService.finishGame(this.gameId, winner);
}

  getPiece(file: string, rank: number): ChessPiece | null {
    return this.board[`${file}${rank}`] || null;
  }

  getPieceSymbol(piece: ChessPiece): string {
    return this.chessLogic.getPieceSymbol(piece);
  }

  isSquareSelected(file: string, rank: number): boolean {
    return this.selectedSquare === `${file}${rank}`;
  }

  isLightSquare(file: string, rank: number): boolean {
    const fileIndex = this.files.indexOf(file);
    return (fileIndex + rank) % 2 === 0;
  }

  getOpponentName(): string {
    return this.playerColor === 'WHITE' ? this.game.blackPlayer : this.game.whitePlayer;
  }

  returnToLobby(): void {
    this.router.navigate(['/lobby']);
  }

  resign(): void {
    const winner = this.playerColor === 'WHITE' ? this.game.blackPlayer : this.game.whitePlayer;
    this.endGame(winner);
  }
}
