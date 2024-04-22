package com.chess.app;

import com.chess.model.Board;
import com.chess.model.Move;
import com.chess.npc.ConcurrentMinMaxPlayer;
import com.chess.npc.MinMaxPlayer;
import com.chess.npc.Player;

import java.util.List;

public class Game {
    public static void main(String[] args) {
        Board board = new Board();
        Player npc = new MinMaxPlayer(8,2);

        long tick = System.currentTimeMillis();
        Move move = npc.nextMove(board);
        System.out.println(move);
        float tock = (System.currentTimeMillis() - tick) / 1000f;
        System.out.println("tock = " + tock);

        board = new Board();
        Player concurrentNPC = new ConcurrentMinMaxPlayer(8,3);
        tick = System.currentTimeMillis();
        Move move1 = concurrentNPC.nextMove(board);
        System.out.println(move1);
        tock = (System.currentTimeMillis() - tick) / 1000f;
        System.out.println("tock = " + tock);
//
//        board.makeMove(new Move(27, 36));
//        board.makeMove(new Move(53, 37));
//        board.makeMove(new Move(60, 24), false);
//        List<Integer> moves = board.getPossibleMoves(23);
//        System.out.println(board.getPosFEN());
//        board.makeMove(new Move(36, 43));

//        System.out.println(board.squares[36].getPiece());

        System.out.println(board.gameState());

//        for (Integer move : moves) {
//            System.out.println(move);
//        }
    }
}
