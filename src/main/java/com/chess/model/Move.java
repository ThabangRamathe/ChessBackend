package com.chess.model;

import java.util.Objects;

public class Move {
    private int dest;
    private int start;

    private boolean isCastling;
    private Square destSquare = new Square();
    private Square startSquare = new Square();

    public Move(int start, int dest){
        this.start = start;
        this.dest = dest;
        this.isCastling = false;
    }

    public boolean isCastling() {
        return isCastling;
    }

    public void setCastling() {
        isCastling = true;
    }

    public int getDest() {
        return dest;
    }

    public int getStart() {
        return start;
    }

    public void moveRecord(Piece start, Piece dest){
        startSquare.setPiece(start);
        destSquare.setPiece(dest);
    }

    public Square getDestSquare() {
        return destSquare;
    }

    public Square getStartSquare() {
        return startSquare;
    }

    @Override
    public String toString() {
        return "Move{" +
                "dest=" + dest +
                ", start=" + start +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return dest == move.dest && start == move.start;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dest, start);
    }
}