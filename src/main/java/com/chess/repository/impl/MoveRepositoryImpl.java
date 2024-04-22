package com.chess.repository.impl;

import com.chess.model.Board;
import com.chess.model.Move;
import com.chess.npc.ConcurrentMinMaxPlayer;
import com.chess.npc.MinMaxPlayer;
import com.chess.npc.Player;
import com.chess.repository.MoveRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("moveRepository")
public class MoveRepositoryImpl implements MoveRepository {

    private final Board board;

    private final Player bot;
    public MoveRepositoryImpl(Board board) {
        this.board = board;
        this.bot = new ConcurrentMinMaxPlayer(16, 3);
    }

    @Override
    public Board undoMove() {
        board.undo();
        board.processPosFen();
        return board;
    }

    @Override
    public Board botMove(String fen) {
        board.processFEN(fen);
        Move move = bot.nextMove(board);

        board.makeMove(move, false);
        board.processPosFen();
        return board;
    }

    @Override
    public Board makeMove(Move move) {
        board.makeMove(move, false);
        board.processPosFen();
        return board;
    }

    @Override
    public List<Integer> getPossibleMoves(String fen, int index) {
        board.processFEN(fen);
        return board.getPossibleMoves(index);
    }
}
