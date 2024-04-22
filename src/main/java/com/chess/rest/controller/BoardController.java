package com.chess.rest.controller;

import com.chess.model.Board;
import com.chess.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping(path = "/", produces = "application/json")
    public ResponseEntity<Board> getInitialBoard(){
        return new ResponseEntity<>(boardService.getInitialBoard(), HttpStatus.OK);
    }

    @PostMapping(path = "/state", produces = "application/json")
    public ResponseEntity<Integer> gameState(@RequestBody String fen){
        return new ResponseEntity<>(boardService.getGameState(fen), HttpStatus.OK);
    }
}
