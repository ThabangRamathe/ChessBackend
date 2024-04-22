package com.chess.model;

public class MoveData {
    public int north;
    public int south;
    public int west;
    public int east;
    public int nw;
    public int ne;
    public int sw;
    public int se;

    public int[] dirData;

    public MoveData(){
        dirData = new int[8];
    }

    void calc(){
        nw = Math.min(north, west);
        ne = Math.min(north, east);
        sw = Math.min(south, west);
        se = Math.min(south, east);
        populateData();
    }

    public void populateData(){
        dirData[0] = north;
        dirData[1] = south;
        dirData[2] = west;
        dirData[3] = east;
        dirData[4] = nw;
        dirData[5] = se;
        dirData[6] = ne;
        dirData[7] = sw;
    }

    @Override
    public String toString() {
        return "MoveData{" +
                "nw=" + nw +
                ", ne=" + ne +
                ", sw=" + sw +
                ", se=" + se +
                '}';
    }

}