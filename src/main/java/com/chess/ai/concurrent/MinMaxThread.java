package com.chess.ai.concurrent;

import com.chess.ai.PieceEval;
import com.chess.model.Board;
import com.chess.model.Move;
import com.chess.model.Piece;

import java.util.List;

public class MinMaxThread implements Runnable{

    private Board board;

    private int depth;

    private int color;

    private float eval;

    MinMaxThread(Board board, int depth, int color){
        this.board = board;
        this.depth = depth;
        this.color = color;
    }


    @Override
    public void run() {
        eval = max(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, depth);
    }

    public float getEval() {
        return eval;
    }

    private float min(float alpha, float beta, int depth) {
        if (depth == 0) {
            return eval(board, nextColor());
//      if(color==Color.WHITE) return (float) eval(board, Color.BLACK);
//      else if(color==Color.BLACK) return (float) eval(board, Color.WHITE);
        }

//    Color c=Color.WHITE;
//    if(color==Color.WHITE) c=Color.BLACK;

        List<Move> moves = board.getMoves();
        if (moves.size() == 0) return Float.NEGATIVE_INFINITY;

        float evalVal = Float.POSITIVE_INFINITY;

        for (Move move : moves) {
            // System.out.println(moves.get(i).getString());
            board.makeMove(move, true);
            evalVal = Math.min(evalVal, max(alpha, beta, depth - 1));
            board.undo();

            beta = Math.min(beta, evalVal);
            if (beta <= alpha) break;
        }

        return evalVal;
    }

    private float max(float alpha, float beta, int depth) {
        if (depth == 0) {
            return eval(board, nextColor());
//      if(color==Color.WHITE) return (float) eval(board, Color.BLACK);
//      else if(color==Color.BLACK) return (float) eval(board, Color.WHITE);
        }

//    Color c=Color.WHITE;
//    if(color==Color.WHITE) c=Color.BLACK;

        List<Move> moves = board.getMoves();
        if (moves.size() == 0) return Float.POSITIVE_INFINITY;

        float evalVal = Float.NEGATIVE_INFINITY;

        for (Move move : moves) {
            // System.out.println(moves.get(i).getString());
            board.makeMove(move, true);
            evalVal = Math.max(evalVal, min(alpha, beta, depth - 1));
            board.undo();

            alpha = Math.max(alpha, evalVal);
            if (beta <= alpha) break;
        }

        return evalVal;
    }

    private float eval(Board board, int color) {
        int whiteEval = 0;
        int blackEval = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int index = (i * 8) + j;
                Piece piece = board.getPiece(index);
                int eval = piece.getEval();
//        eval += PieceEval.getEval(piece.type, piece.color)[index];
                if (piece.color == 8) {
                    whiteEval += eval;
                    whiteEval += PieceEval.getEval(piece.type)[63-index];
                }
                if (piece.color == 16) {
                    blackEval += eval;
                    blackEval += PieceEval.getEval(piece.type)[index];
                }
            }
        }
        if (color == 16) return whiteEval - blackEval;

        return blackEval - whiteEval;
    }

    private int nextColor() {
        int[] colors = {16, 8};
        int color = this.color;
        color /= 8;
        color += 1;
        color %= 2;
        color = colors[color];

        return color;
    }
}
