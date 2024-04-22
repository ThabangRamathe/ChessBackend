package com.chess.model;
import java.util.*;

public class Board {

    private final int[] dirOffsets = {8, -8, -1, 1, 7, -7, 9, -9};

    private final MoveData[] squaresToEdge;

    public List<Move> moves;

    public final Square[] squares;

    private String fen;

    private List<Integer> viablePieces; // for castling

    private List<Move> enPassant = new ArrayList<>();
    private List<Move> castling = new ArrayList<>();
    private List<String> positions = new ArrayList<>();
    private final List<Move> moveHistory = new ArrayList<>();

    public static int colourToMove;

    public Board() {
        squaresToEdge = new MoveData[64];
        moves = new ArrayList<>();
        squares = new Square[64];
        String f1 = "RNBKQBNR/PPPPPPPP/8/8/8/8/pppppppp/rnbkqbnr";
        String f2 = "RNBKQBNR/P2PPPPP/2P5/1P6/3p4/8/ppp1pppp/rnbkqbnr";
        String f = "RNBKQBNR/PP1PPPPP/2P5/8/3p4/8/ppp1pppp/rnbkqbnr";
        processFEN(f);
        positions.add(f);
        colourToMove = 8;
        moveData();
        populateViablePieces();
    }

    private void populateViablePieces() {
        viablePieces = new ArrayList<>();

        for (int i = 0; i < 57; i+=56) {
            viablePieces.add(i);
            viablePieces.add(i + 3);
            viablePieces.add(i + 7);
        }
    }

    public void processFEN(String fen) {
        this.fen = fen;
//        String[] temp = fen.replace("/", "").split("");
        String[] temp = fen.split("/");
//        int index = 0;
        for (int i = 0; i < 8; i++) {
//            int k = index;
            String[] row = getRow(temp[i]);
            for (int j = 0; j < 8; j++) {
                int sqIndex = (i * 8) + j;
                Piece piece = getPiece(row[j]);
//                if (isNumeric(temp[index])){
//                    temp[index] = subtractOne(temp[index]);
//                    index--;
//                }
//                else{
//                    System.out.println("i = " + i);
//                    System.out.println("j = " + j);
//                }
                Square square = new Square(piece);
                squares[sqIndex] = square;
//
//                index++;
//                if (piece.type != 0) index++;
            }
//            if(k==index) index++;
        }
    }

    private String[] getRow(String rowFEN) {
        String[] row = new String[8];
        String[] temp = rowFEN.split("");

        int index = 0;
        for (int i = 0; i < 8; i++) {
            if (isNumeric(temp[index])) {
                temp[index] = subtractOne(temp[index]);
                if (!temp[index].equals("0")) {
                    index--;
                }
                row[i] = 0 + "";
            } else {
                row[i] = temp[index];
            }

            index++;
        }
        return row;
    }

    private String subtractOne(String s) {
        int num = Integer.parseInt(s) - 1;
        return num + "";
    }

    private Piece getPiece(String n) {
        if (isNumeric(n)) return Piece.EMPTY_PIECE;

        Piece piece;
        int p = 0, c = 8;

        if (n.equalsIgnoreCase("R")) {
            p = 5;
            if (n.equals("r")) c = 16;
        } else if (n.equalsIgnoreCase("N")) {
            p = 3;
            if (n.equals("n")) c = 16;
        } else if (n.equalsIgnoreCase("B")) {
            p = 4;
            if (n.equals("b")) c = 16;
        } else if (n.equalsIgnoreCase("q")) {
            p = 9;
            if (n.equals("q")) c = 16;
        } else if (n.equalsIgnoreCase("k")) {
            p = 2;
            if (n.equals("k")) c = 16;
        } else if (n.equalsIgnoreCase("p")) {
            p = 1;
            if (n.equals("p")) c = 16;
        }

        piece = new Piece(c, p);

        return piece;
    }

    public Piece getPiece(int index) {
        return squares[index].getPiece();
    }

    public String getFEN() {
        return fen;
    }

    public String getPosFEN() {
        StringBuilder fen = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int empty = 0;
            for (int j = 0; j < 8; j++) {
                int index = (i * 8) + j;
                Piece piece = squares[index].getPiece();
                if (piece.isNull()) {
                    empty++;
                    continue;
                }
                if (empty != 0) {
                    fen.append(empty);
                    empty = 0;
                }

                fen.append(piece.getPieceChar());
            }
            if (empty != 0) {
                fen.append(empty);
            }
            fen.append("/");
        }

        return fen.substring(0, fen.lastIndexOf("/"));
    }

    public void processPosFen(){
        this.fen = getPosFEN();
    }

    private boolean isNumeric(String n) {
        try {
            Integer.parseInt(n);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void printSquares() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int k = (i * 8) + j;
//                if(squares[k].getPiece() == null) System.out.print(0);
//                else System.out.print(squares[k].getPiece().type);
                System.out.print(squares[k].getPiece().getPieceChar());
            }
            System.out.println("");
        }
    }

    private void moveData() {
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                MoveData data = new MoveData();
                data.north = 7 - file;
                data.south = file;
                data.west = rank;
                data.east = 7 - rank;
                data.calc();

                int sqIndex = file * 8 + rank;

                squaresToEdge[sqIndex] = data;
            }
        }
    }

    public List<Move> generateMoves() {
        moves = new ArrayList<>();

        for (int i = 0; i < 64; i++) {
            Piece piece = squares[i].getPiece();
            if (piece.color == Board.colourToMove) {
                if (piece.isSliding()) {
                    generateSlidingMoves(i, piece);
                }
                if (piece.isKnight()) {
                    generateKnightMoves(i);
                }
                if (piece.isPawn()) {
                    generatePawnMoves(i, piece);
                }
                if (piece.isKing()) {
                    generateKingMoves(i);
                }
            }
        }

        return moves;
    }

    public void generateSlidingMoves(int start, Piece piece) {
        int startIndex = piece.type == 4 ? 4 : 0;
        int endIndex = piece.type == 5 ? 4 : 8;

        for (int dirIndex = startIndex; dirIndex < endIndex; dirIndex++) {
            for (int i = 0; i < squaresToEdge[start].dirData[dirIndex]; i++) {
                int dest = start + (dirOffsets[dirIndex] * (i + 1));
                Piece pieceOnDest = squares[dest].getPiece();

                if (pieceOnDest.color == Board.colourToMove) {
                    break;
                }

                moves.add(new Move(start, dest));

                if (pieceOnDest.color != Board.colourToMove && !pieceOnDest.isNull()) {
                    break;
                }
            }
        }
    }

    private void generatePawnMoves(int start, Piece piece) {
        int dirIndex = piece.color == 8 ? 0 : 1;
        int except = dirIndex == 0 ? 1 : 6;

        int dest = start + dirOffsets[dirIndex];
        if (!isValidSquare(dest)) return;
        Piece pieceOnDest = squares[dest].getPiece();

        if (pieceOnDest.isNull()) {
            moves.add(new Move(start, dest));
            if (start / 8 == except) {
                dest = start + (dirOffsets[dirIndex] * 2);
                pieceOnDest = squares[dest].getPiece();
                if (pieceOnDest.isNull()) {
                    moves.add(new Move(start, dest));
                }
            }
        }

        int i = getDirOffset();
//        int startRank =

        dest = start + dirOffsets[i];
        if (Math.abs((dest/8) - (start/8)) == 1) {
            if (!isValidSquare(dest)) return;
            pieceOnDest = squares[dest].getPiece();
            if (!pieceOnDest.isNull() && pieceOnDest.color != Board.colourToMove) {
                moves.add(new Move(start, dest));
            }
        }

        dest = start + dirOffsets[i + 2];
        if (Math.abs((dest/8) - (start/8)) == 1) {
            if (!isValidSquare(dest)) return;
            pieceOnDest = squares[dest].getPiece();
            if (!pieceOnDest.isNull() && pieceOnDest.color != Board.colourToMove) {
                moves.add(new Move(start, dest));
            }
        }

        // En Passant - In Passing
        if(!moveHistory.isEmpty()){
            Move prevMove = moveHistory.get(moveHistory.size() - 1);
            int prevRank = prevMove.getStart()/7;
            int currRank = start/7;
            int prevDest = prevMove.getDest();
            if (prevRank == (currRank + 2) || prevRank == currRank -2){
                if (Math.abs(prevDest - start) == 1){// same rank?
                    if (colourToMove == 16){
                        dest = start > prevDest ? start + dirOffsets[7] : start + dirOffsets[5];
                    }
                    else {
                        dest = start > prevDest ? start + dirOffsets[4] : start + dirOffsets[6];
                    }
                    Move move = new Move(start, dest);
                    moves.add(move);
                    enPassant.add(move);
                }
            }
        }
    }

    private void generateKingMoves(int start) {
        for (int dirIndex = 0; dirIndex < 8; dirIndex++) {
            if (squaresToEdge[start].dirData[dirIndex] > 0) {
                int dest = start + dirOffsets[dirIndex];
                Piece pieceOnDest = squares[dest].getPiece();

                if (pieceOnDest.color == Board.colourToMove) {
                    continue;
                }

                moves.add(new Move(start, dest));
            }
        }

        //Castling moves
        // in check?
        if (castlingPossible(start, start + 4)) {
            Move move = new Move(start, start + 2);
            moves.add(move);
            castling.add(move);
        }

        if (castlingPossible(start, start - 3)) {
            Move move = new Move(start, start - 2);
            moves.add(move);
            castling.add(move);
        }
//        boolean inCheck = false;
//        if (!second) {
//            nextColor();
//            inCheck = isCheck();
//            nextColor();
//        }
//
//        if(!inCheck && viablePieces.contains(piece)) {
//            moves.add(new Move(start, start - 2));
//            moves.add(new Move(start, start + 2));
//        }
    }

    private boolean castlingPossible(int start, int dest){
        if(!viablePieces.contains(start)) return false;

        if(!viablePieces.contains(dest)) return false;

        if (start> dest){
            for (int i = dest + 1; i < start; i++) {
                if (squares[i].hasPiece()) return false;
                if (!isPossibleMove(new Move(start, i))) return false;
            }
        }
        else {
            for (int i = start + 1; i < dest; i++) {
                if (squares[i].hasPiece()) return false;
                if (!isPossibleMove(new Move(start, i)) && i - start < 3) return false;
            }
        }

//        To-Do: make sure not in check

        return true;
    }

    private void generateKnightMoves1(int start, Piece piece) {
        int rank = start / 8;
        int file = start % 8;

        for (int i = -1; i < 2; i += 2) {
            for (int j = -1; j < 2; j += 2) {
                int newRank = rank + (i * 2);
                int newFile = file + j;
                if (isValidSquare(newRank, newFile)) {
//          System.out.println("("+newRank+","+newFile+")");
                    addKnightMove(start, newRank, newFile);
                }

                newRank = rank + i;
                newFile = file + (2 * j);
                if (isValidSquare(newRank, newFile)) {
//          System.out.println("("+newRank+","+newFile+")");
                    addKnightMove(start, newRank, newFile);
                }
            }
        }
    }

    private void generateKnightMoves(int start){
        int rank = start / 8;
        int file = start % 8;
        // (rank, file)

        double radius = Math.sqrt(5);

        for (int i = 4; i> 0; i-=3){
            double phi = Math.atan((i*1.0)/2);
            for (int j = 0; j < 4; j++) {
                double angle = phi + j * Math.PI/2;
                int newRank = Math.round((float) (rank+radius*Math.cos(angle)));
                int newFile = Math.round((float) (file+radius*Math.sin(angle)));
                if (isValidSquare(newRank,newFile)){
//          System.out.println("("+newRank+","+newFile+")");
                    addKnightMove(start,newRank,newFile);
                }
            }
        }
    }

    private boolean isValidSquare(int dest) {
        return dest >= 0 && dest < 64;
    }

    private int getDirOffset() {
        return colourToMove == 8 ? 4 : 5;
    }

    private boolean isValidSquare(int newRank, int newFile) {
        return !(newFile < 0 || newFile > 7 || newRank < 0 || newRank > 7);
    }

    private void addKnightMove(int start, int rank, int file) {
        int dest = (rank * 8) + file;
        Piece pieceOnDest = squares[dest].getPiece();

        if (pieceOnDest.color == Board.colourToMove) {
            return;
        }

        moves.add(new Move(start, dest));
    }

    public List<Move> getMoves(){
        generateMoves();
        return getValidatedMoves();
    }

    public List<Move> getValidatedMoves(){
        List<Move> validMoves = new ArrayList<>();
        for (Move move : moves) {
            if (isPossibleMove(move)) validMoves.add(move);
        }

        return validMoves;
    }

    public void makeMove(Move move, boolean experimental) {
        int start = move.getStart();
        int dest = move.getDest();
//        move.moveRecord(squares[start].getPiece(), squares[dest].getPiece());
        swap(start, dest);


        if (!squares[start].getPiece().isNull()) {
            squares[start].setPiece(Piece.EMPTY_PIECE);
        }
        if (enPassant.contains(move)){
            int sqIndex = dest + dirOffsets[1];
            if (colourToMove == 16){
                sqIndex = dest + dirOffsets[0];
            }
            squares[sqIndex].setPiece(Piece.EMPTY_PIECE);
        }

        if (viablePieces.contains(start)) {
            if(start == 3 || start == 59){
                if (viablePieces.contains(dest-1)){
                    swap(dest-1, dest + 1);
                    if (!experimental) viablePieces.remove((Integer) (dest - 1));
                }
                if (viablePieces.contains(dest+2)){
                    swap(dest+2, dest - 1);
                    if (!experimental) viablePieces.remove((Integer) (dest + 2));
                }
            }
            if (!experimental) viablePieces.remove((Integer) start);
        }

//        if(!experimental) viablePieces.remove(squares[move.getDest()].getPiece());
//        positions.add(squares);
        positions.add(getPosFEN());
        moveHistory.add(move);
        nextColor();
    }

    public void undo() {
        if (moveHistory.size() == 0) return;

        Move move = moveHistory.remove(moveHistory.size() - 1);
        processFEN(positions.get(positions.size() - 2));
        positions.remove(positions.size() - 1);
//        squares = positions.get(positions.size()-1);
//        squares[move.getDest()] = move.getDestSquare();
//        squares[move.getStart()] = move.getStartSquare();
        nextColor();
    }

    private void nextColor() {
        int[] colors = {16, 8};
        colourToMove /= 8;
        colourToMove += 1;
        colourToMove %= 2;
        colourToMove = colors[colourToMove];
    }

    private void swap(int start, int dest) {
        Square temp = squares[start];
        squares[start] = squares[dest];
        squares[dest] = temp;
    }

    public boolean isCheck(){
//        nextColor();//white
        List<Move> ogMoves = moves;
        generateMoves();
//        nextColor();//black
        for (Move move : moves) {
            Piece destPiece = squares[move.getDest()].getPiece();
//      if (destPiece.isNull()) continue;
            if (destPiece.isKing() && destPiece.color != colourToMove){
                moves = ogMoves;
                return true;
            }
        }
        moves =ogMoves;
        return false;
    }

    private boolean isPossibleMove(Move move){
        try {
            makeMove(move, true);
        }
        catch (Exception e){
            return false;
        }
        boolean verdict = isCheck();
        undo();

        return !verdict;
    }

    public List<Integer> getPossibleMoves(int start){
        enPassant = new ArrayList<>();
        Piece piece = squares[start].getPiece();
        if (piece.color != colourToMove) return  new ArrayList<>();
        if(piece.isKing()) return getPosKingMoves(start);
        if(piece.isSliding()) return getPosSlidingMoves(start,piece);
        if(piece.isKnight()) return getPosKnightMoves(start);
        if(piece.isPawn()) return getPosPawnMoves(start, piece);

        return new ArrayList<>();
    }

    private List<Integer> getDestList(List<Move> moves){
        List<Integer> dests = new ArrayList<>();

        for (Move move : moves) {
            dests.add(move.getDest());
        }

        return dests;
    }
    private List<Integer> getPosPawnMoves(int start, Piece piece) {
        moves = new ArrayList<>();
        generatePawnMoves(start,piece);
        return getDestList(getValidatedMoves());
    }

    private List<Integer> getPosKnightMoves(int start) {
        moves = new ArrayList<>();
        generateKnightMoves(start);
        return getDestList(getValidatedMoves());
    }

    private List<Integer> getPosSlidingMoves(int start, Piece piece) {
        moves = new ArrayList<>();
        generateSlidingMoves(start,piece);
        return getDestList(getValidatedMoves());
    }

    private List<Integer> getPosKingMoves(int start) {
        moves = new ArrayList<>();
        generateKingMoves(start);
        return getDestList(getValidatedMoves());
    }

    public int gameState(){
        generateMoves();
        List<Move> validMoves = getValidatedMoves();
        if (validMoves.isEmpty()) {
            nextColor();
            if (isCheck()) {
                nextColor();
                if (colourToMove == 16) return 2;
                if (colourToMove == 8) return 1;
            }
            nextColor();
            return 0;
        }

        return -1;
    }

    public void printString() {
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                int sqIndex = rank * 8 + file;
                System.out.print(squaresToEdge[sqIndex].ne);
            }
            System.out.println();
        }
    }

}