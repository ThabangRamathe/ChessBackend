package com.chess.rest.controller;

import com.chess.model.Board;
import com.chess.model.Move;
import com.chess.model.ObjectHolder;
import com.chess.service.MoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/move")
public class MoveController {

    private final MoveService moveService;

    @Autowired
    public MoveController(MoveService moveService){
        this.moveService = moveService;
    }

    @PutMapping(path = "/", produces = "application/json")
    public ResponseEntity<Board> makeMove(@RequestBody Move move){
        return new ResponseEntity<>(moveService.makeMove(move), HttpStatus.OK);
    }

    @PostMapping(path = "/possible-moves", produces = "application/json")
    public ResponseEntity<List<Integer>> getPossibleMoves(@RequestBody ObjectHolder holder){
        return new ResponseEntity<>(moveService.getPossibleMoves(holder.getFen(), holder.getSquare()), HttpStatus.OK);
    }

    @GetMapping(path = "/undo", produces = "application/json")
    public ResponseEntity<Board> undoMove(){
        return new ResponseEntity<>(moveService.undoMove(), HttpStatus.OK);
    }

    @PutMapping(path = "/bot-move", produces = "application/json")
    public ResponseEntity<Board> botMove(@RequestBody String fen){
        return new ResponseEntity<>(moveService.botMove(fen), HttpStatus.OK);
    }

}
