package com.chess.model;

import java.util.Objects;

public class Piece {
    // public final int none = 0;
    // public final int king = 2;
    // public final int pawn = 1;
    // public final int knight = 3;
    // public final int bishop = 4;
    // public final int rook = 5;
    // public final int queen = 9;

    // public final int none = 0;
    // public final int white = 8;
    // public final int black = 16;

    public static final Piece EMPTY_PIECE = new Piece(0, 0);

    public int type;

    public int color;

    public Piece(int c, int p) {
        type = p;
        color = c;
    }

    public boolean isSliding() {
        return type > 3;
    }

    public boolean isPawn() {
        return type == 1;
    }

    public boolean isNull() {
        return this.equals(EMPTY_PIECE);
    }

    public boolean isKing() {
        return type == 2;
    }

    public boolean isKnight() {
        return type == 3;
    }

    public boolean isBishop() {
        return type == 4;
    }

    public boolean isRook() {
        return type == 5;
    }

    public boolean isQueen() {
        return type == 9;
    }

    public boolean isBlack() { return color == 16;}

    public boolean isWhite() { return color == 8;}

    public int getEval() {
//        if (type == 1) return 1;
        return type == 4 ? 333 : type * 100;
    }

    public String getPieceChar() {
        String p = "_";
        if (isKing()) {
            p = "K";
        }
        if (isKnight()) {
            p = "N";
        }
        if (isPawn()) {
            p = "P";
        }
        if (isBishop()) {
            p = "B";
        }
        if (isRook()) {
            p = "R";
        }
        if (isQueen()) {
            p = "Q";
        }

        if (color == 16) {
            p = p.toLowerCase();
        }
        return p;
    }

    @Override
    public String toString() {
        return "Piece{" +
                "type=" + type +
                ", color=" + color +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        boolean hash = hashCode() == o.hashCode();
        return type == piece.type && color == piece.color && hashCode() == o.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, color);
    }
}