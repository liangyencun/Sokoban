package com.xc.sokoban;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Aaron on 2016-04-22.
 */
public class Tile {
    public RectF rect;
    public int width;
    public int height;
    public int x;
    public int y;
    public int space;
    public Bitmap bitmap;
    public enum Type{WALL, BOX, TARGET}
    public enum Status{STOP, UP, DOWN, RIGHT, LEFT}
    Status status;
    Type type;
    GameView gameView;

    public Tile(int x, int y, int space){
        this.x = x;
        this.y = y;
        this.space = space;
        rect = new RectF(x, y, x+space, y+space);
    }

    public void draw(Canvas c){
        Paint p = new Paint();
        p.setColor(Color.LTGRAY);
        c.drawRect(rect, p);
    }

    public void setStatus(Status s){
        this.status = s;
    }

    public Type getType(){
        return type;
    }

    public int getXTile(){
        return x/space;
    }
    public int getYTile(){
        return y/space;
    }
    public void setXTile(int cellX){
        x = cellX*space;
    }
    public void setYTile(int cellY){
        y = cellY*space;
    }
    
}
