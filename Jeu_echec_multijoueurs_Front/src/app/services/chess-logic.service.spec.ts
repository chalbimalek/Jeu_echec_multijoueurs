import { TestBed } from '@angular/core/testing';

import { ChessLogicService } from './chess-logic.service';

describe('ChessLogicService', () => {
  let service: ChessLogicService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChessLogicService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
