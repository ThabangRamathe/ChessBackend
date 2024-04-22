package com.chess.repository;

import com.chess.model.Board;

public interface BoardRepository {

    Board getInitialBoard();

    int getGameState(String fen);
}
