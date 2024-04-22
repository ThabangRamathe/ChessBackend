package com.chess.service.impl;

import com.chess.model.Board;
import com.chess.model.Move;
import com.chess.repository.MoveRepository;
import com.chess.service.MoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("moveService")
public class MoveServiceImpl implements MoveService {

    private final MoveRepository moveRepository;

    @Autowired
    public MoveServiceImpl(MoveRepository moveRepository){
        this.moveRepository = moveRepository;
    }

    @Override
    public Board undoMove() {
        return moveRepository.undoMove();
    }

    @Override
    public Board botMove(String fen) {
        return moveRepository.botMove(fen);
    }

    @Override
    public Board makeMove(Move move) {
        return moveRepository.makeMove(move);
    }

    @Override
    public List<Integer> getPossibleMoves(String fen, Integer index) {
        return moveRepository.getPossibleMoves(fen, index);
    }
}
