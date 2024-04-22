package com.chess.npc;

import com.chess.model.Board;
import com.chess.model.Move;

public abstract class Player {

    protected int color;

    public Player(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public abstract Move nextMove(Board board);

}