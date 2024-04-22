package com.chess.service;

import com.chess.model.Board;
import com.chess.model.Move;

import java.util.List;

public interface MoveService {

    Board makeMove(Move move);

    Board botMove(String fen);

    Board undoMove();

    List<Integer> getPossibleMoves(String fen, Integer index);
}
