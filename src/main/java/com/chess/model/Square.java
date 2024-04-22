package com.chess.model;

public class Square {
    private Piece piece;

    Square(){
        this.piece = Piece.EMPTY_PIECE;
    }

    Square(Piece piece){
        this.piece = piece;
    }

    public void setPiece(Piece p){ this.piece = p;}

    public Piece getPiece() {
        return piece;
    }

    public boolean hasPiece(){ return !piece.equals(Piece.EMPTY_PIECE);}

}