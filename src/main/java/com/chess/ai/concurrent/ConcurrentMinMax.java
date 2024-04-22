package com.chess.ai.concurrent;

import com.chess.ai.MinMax;
import com.chess.model.Board;
import com.chess.model.Move;

import java.util.List;

public class ConcurrentMinMax {

    private int maxDepth;

    private int color;
    public ConcurrentMinMax(int maxDepth, int color) {
        this.maxDepth = maxDepth;
        this.color = color;
    }

    // consider caching

    public Move decision(Board board) {
        List<Move> moves = board.getMoves();
        if (moves.isEmpty()) return null;

        Thread[] evalThreads = new Thread[moves.size()];
        int index = -1;
        float max = Float.NEGATIVE_INFINITY;

        for (int i = 0; i < moves.size(); i++) {
//       System.out.println(moves.get(i));
            board.makeMove(moves.get(i), true);
            MinMaxThread minMaxThread = new MinMaxThread(board, maxDepth, color);
            evalThreads[i] = new Thread(minMaxThread, "Thread " + i);
            evalThreads[i].start();
            try {
                evalThreads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // System.out.println("hello");
//      System.out.println(costs[i]);
            board.undo();
            // costs[i]=evalVal;
            System.out.println(moves.get(i) + ": " + minMaxThread.getEval());

            if (minMaxThread.getEval() > max) {
                max = minMaxThread.getEval();
                index = i;
            }
        }

        if (index == -1) return null;
        return moves.get(index);
    }
}
