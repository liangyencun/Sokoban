package com.xc.sokoban;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

/**
 * Created by Jimmy on 2016/4/19.
 */
public class Player extends Tile{

    public static int numOfSpot;


    public Player(Context context, int x, int y, int space, GameView gameView){
        super(x, y, space);
        numOfSpot++;
        rect = new RectF(x+space*0.25f, y, x+space*0.75f, y+space);
        Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        bitmap = Bitmap.createScaledBitmap(tmp, space, space, false);
        status = Status.STOP;
        this.gameView = gameView;
    }

    public void draw(Canvas c){
        c.drawBitmap(bitmap, null, rect, null);
    }

    public void update(){
        /*switch (status){ movement is taken care of elsewhere
            case STOP:
                break;
            case UP:
                y -= space;
                break;
            case DOWN:
                y += space;
                break;
            case RIGHT:
                x += space;
                break;
            case LEFT:
                x -= space;
                break;
        }*/

        rect = new RectF(x+space*0.25f, y, x+space*0.75f, y+space); //just for aesthetic aspect ratio
    }

    public void setRect(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void move(Status dir){
        gameView.maze.deleteMazeTile(getXTile(), getYTile());
        if (gameView.collisionDetection(dir)){
            setStatus(dir);
            Log.i("Move", "Player from "+getXTile()+", "+getYTile());
            movePlayer(dir);
            update();
            Log.i("Move", "Player to "+getXTile()+", "+getYTile());
            setStatus(Status.STOP);
        }
        gameView.maze.addMazeTile(this, getXTile(), getYTile());
    }

    /* Moves the player. This function assumes that it is possible to move!
     * Logic: if target is present at destination, move it to hidden layer and place player over it
     * Logic: if the origin had a target underneath, move it back to surface after tile is moved away
     * @param Status dir: direction to move
     */
    public void movePlayer(Status dir){
        Tile dest;
        Tile originBasement = gameView.maze.mazeTiles[getXTile()][getYTile()][1]; //target underneath origin
        int destX = -1;
        int destY = -1;
        switch(dir){
            case UP:
                destX = getXTile();
                destY = getYTile()-1;
                break;
            case DOWN:
                destX = getXTile();
                destY = getYTile()+1;
                break;
            case LEFT:
                destX = getXTile()-1;
                destY = getYTile();
                break;
            case RIGHT:
                destX = getXTile()+1;
                destY = getYTile();
                break;
        }
        dest = gameView.maze.mazeTiles[destX][destY][0];
        if (dest != null && dest.getType() == Tile.Type.TARGET) {
            //target at destination
            gameView.maze.moveTileDown(dest);
        }
        gameView.maze.addMazeTile(this, destX, destY);
        gameView.maze.deleteMazeTile(getXTile(), getYTile());
        setXTile(destX);
        setYTile(destY);
        if (originBasement != null){
            //target at origin
            gameView.maze.moveTileUp(originBasement);
        }
    }

}
