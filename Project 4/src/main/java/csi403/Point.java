package csi403;

import java.io.Serializable;

public class Point implements Serializable {
    private int x;
    private int y;

    public Point() {
        // do nothing
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /*
        Getters and Setter for instance fields.
     */

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
