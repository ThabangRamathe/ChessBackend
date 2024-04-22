package com.chess.service;

import com.chess.model.Board;

public interface BoardService {

    Board getInitialBoard();

    int getGameState(String fen);
}
