package com.chess.npc;

import com.chess.ai.MinMax;
import com.chess.ai.concurrent.ConcurrentMinMax;
import com.chess.model.Board;
import com.chess.model.Move;

public class ConcurrentMinMaxPlayer extends Player {
    private ConcurrentMinMax minMax;

    public ConcurrentMinMaxPlayer(int color, int depth) {
        super(color);
        minMax = new ConcurrentMinMax(depth, color);
    }

    @Override
    public Move nextMove(Board board) {
        return minMax.decision(board);
    }
}
