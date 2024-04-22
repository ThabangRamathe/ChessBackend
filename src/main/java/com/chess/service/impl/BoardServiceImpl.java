package com.chess.service.impl;

import com.chess.model.Board;
import com.chess.repository.BoardRepository;
import com.chess.service.BoardService;
import org.springframework.stereotype.Service;

@Service("boardService")
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public int getGameState(String fen) {
        return boardRepository.getGameState(fen);
    }

    @Override
    public Board getInitialBoard() {
        return boardRepository.getInitialBoard();
    }
}
