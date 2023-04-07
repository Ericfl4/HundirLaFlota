package com.example.hundirlaflota;

public class Barco {

    private int x;
    private int y;

    public Barco(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Barco(x=" + (x+1) + ", y=" + (y+1) + ")\n";
    }
}