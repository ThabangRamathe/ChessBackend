package com.chess.npc;

import com.chess.ai.MinMax;
import com.chess.model.Board;
import com.chess.model.Move;

public class MinMaxPlayer extends Player{

    private MinMax minMax;

    public MinMaxPlayer(int color, int depth) {
        super(color);
        minMax = new MinMax(depth, color);
    }

    @Override
    public Move nextMove(Board board) {
        return minMax.decision(board);
    }

}