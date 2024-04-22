package com.chess.repository.impl;

import com.chess.model.Board;
import com.chess.repository.BoardRepository;
import org.springframework.stereotype.Repository;

@Repository("boardRepository")
public class BoardRepositoryImpl implements BoardRepository {

    private final Board board;

    public BoardRepositoryImpl(Board board) {
        this.board = board;
    }

    @Override
    public Board getInitialBoard() {
        return board;
    }

    @Override
    public int getGameState(String fen) {
        board.processFEN(fen);
        return board.gameState();
    }
}
