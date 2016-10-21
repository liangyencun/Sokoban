package com.xc.sokoban;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Aaron on 2016-04-19.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    public GameView(Context context) { super(context) ;
        this.context = context;
        getHolder (). addCallback(this);
        setFocusable(true);
    }

    Context context;
    boolean gameLoaded = false;
    GameThread gt;

    public ArrayList<Wall> walls = new ArrayList();
    public ArrayList<Box> boxes = new ArrayList();
    public ArrayList<Target> targets = new ArrayList();
    Wall wall;
    Target target;
    Box box;
    Player player;
    private int SPACE;
    Maze maze;
    int level=1;

    public void clearVars(){
        walls = null;
        boxes = null;
        targets = null;
        wall = null;
        target = null;
        box = null;
        player = null;
        maze = null;
        walls = new ArrayList();
        boxes = new ArrayList();
        targets = new ArrayList();
    }

    public void loadGame(int level){
        clearVars();
        maze = new Maze(level);
        int x = 0;
        int y = 0;
        int c = 0;
        SPACE = getWidth()/10;
        for (int j = 0; j < maze.mazeSizeY; j++) {
            for (int i = 0; i < maze.mazeSizeX; i++) {
                char item = maze.MAZE.charAt(c++);

                if (i==0 && j!=0) {
                    y += SPACE;
                    x = 0;
                }
                if (item == '#') {
                    wall = new Wall(context, x, y, SPACE);
                    walls.add(wall);
                    maze.addMazeTile(wall, i, j);
                    x += SPACE;
                    Log.d("Load", "Wall created at "+i+", "+j);
                } else if (item == ' ') {
                    x += SPACE;
                } else if (item == 'B') {
                    box = new Box(context, x, y, SPACE, this);
                    boxes.add(box);
                    maze.addMazeTile(box, i, j);
                    Log.d("Load", "Box created at "+i+", "+j);
                    x += SPACE;
                } else if (item == 'T') {
                    target = new Target(context, x, y, SPACE);
                    targets.add(target);
                    maze.addMazeTile(target, i, j);
                    Log.d("Load", "Target created at "+i+", "+j);
                    x += SPACE;
                } else if (item == 'P') {
                    player = new Player(context, x, y, SPACE, this);
                    Log.d("Load", "Player created at "+i+", "+j);
                    maze.addMazeTile(player, i, j);
                    x += SPACE;
                }

            }
        }
        Log.d("Load", "Level Loaded");
    }

    @Override
    public void surfaceCreated ( SurfaceHolder holder ) {
        // Launch animator thread
        if (!gameLoaded) {
            gt = new GameThread(this);
            gt.start();
            gameLoaded = true;
        }
        Log.d("Load", "surfaceView/Thread");
    }

    @Override
    public void draw(Canvas c) {
        super.draw(c);
        if (gt.gameState == GameThread.RUNNING) {
            c.drawColor(Color.WHITE);
            for (int i = 0; i < maze.mazeSizeX; i++) {
                for (int j = 0; j < maze.mazeSizeY; j++) {
                    if (maze.mazeTiles[i][j][0] != null) {
                        maze.mazeTiles[i][j][0].draw(c);
                    }
                }
            }
        }
        else if (gt.gameState == GameThread.OVER){
            c.drawColor(Color.WHITE);
            Paint p = new Paint();
            p.setTextAlign(Paint.Align.CENTER);
            p.setColor(Color.BLACK);
            p.setAntiAlias(true);
            p.setFakeBoldText(true);
            p.setTextSize(getHeight()/16);
            c.drawText("Level "+level+" Complete!", getWidth()/2, getHeight()/3, p);
            if (level < 4) {
                p.setTextSize(getHeight() / 21);
                p.setFakeBoldText(false);
                c.drawText("Swipe to play next level", getWidth() / 2, getHeight() / 2, p);
            }
        }
    }

    /* Collision detection checks if the player can move in a certain direction
     *
     * Logic: if a clear tile or target is at the location in which the player wants to move, true
     * Logic: if a wall is at the location in which the player wants to move, false
     * Logic: if a box is present at the location in which the player wants to move, look another tile further
     *          if that tile is a wall or another box, false
     *          else true, and calls moveBox
     * @param Status playerDirection: takes in the direction in which the player wants to move
     * @return boolean: indicates whether it is valid to move in the provided direction or not
     */
    public boolean collisionDetection(Tile.Status playerDirection){
        switch(playerDirection){
            case UP:
                if (player.getYTile()-1 >= 0){
                    Tile compare = maze.mazeTiles[player.getXTile()][player.getYTile()-1][0];
                    if (compare == null || compare.getType() == Tile.Type.TARGET){
                        Log.i("Move", "No collision detected, valid move");
                        return true;
                    }
                    else if (compare.getType() == Tile.Type.WALL){
                        Log.i("Move", "Wall detected, invalid move");
                        return false;
                    }
                    else if (player.getYTile()-2 >= 0){//box, look one space further
                        Tile compare2 = maze.mazeTiles[player.getXTile()][player.getYTile()-2][0];
                        if (compare2 == null || compare2.getType() == Tile.Type.TARGET){
                            moveBox(compare, playerDirection);
                            Log.i("Move", "Box detected, valid move");
                            return true;
                        }
                    }
                }
                Log.i("Move", "Box and wall or 2 boxes detected, invalid move");
                return false;
            case DOWN:
                if (player.getYTile()+1 < maze.mazeSizeY){
                    Tile compare = maze.mazeTiles[player.getXTile()][player.getYTile()+1][0];
                    if (compare == null || compare.getType() == Tile.Type.TARGET){
                        Log.i("Move", "No collision detected, valid move");
                        return true;
                    }
                    else if (compare.getType() == Tile.Type.WALL){
                        Log.i("Move", "Wall detected, invalid move");
                        return false;
                    }
                    else if (player.getYTile()+2 < maze.mazeSizeY){//box, look one space further
                        Tile compare2 = maze.mazeTiles[player.getXTile()][player.getYTile()+2][0];
                        if (compare2 == null || compare2.getType() == Tile.Type.TARGET){
                            moveBox(compare, playerDirection);
                            Log.i("Move", "Box detected, valid move");
                            return true;
                        }
                    }
                }
                Log.i("Move", "Box and wall or 2 boxes detected, invalid move");
                return false;
            case LEFT:
                if (player.getXTile()-1 >= 0){
                    Tile compare = maze.mazeTiles[player.getXTile()-1][player.getYTile()][0];
                    if (compare == null || compare.getType() == Tile.Type.TARGET){
                        Log.i("Move", "No collision detected, valid move");
                        return true;
                    }
                    else if (compare.getType() == Tile.Type.WALL){
                        Log.i("Move", "Wall detected, invalid move");
                        return false;
                    }
                    else if (player.getXTile()-2 >= 0){//box, look one space further
                        Tile compare2 = maze.mazeTiles[player.getXTile()-2][player.getYTile()][0];
                        if (compare2 == null || compare2.getType() == Tile.Type.TARGET){
                            moveBox(compare, playerDirection);
                            Log.i("Move", "Box detected, valid move");
                            return true;
                        }
                    }
                }
                Log.i("Move", "Box and wall or 2 boxes detected, invalid move");
                return false;
            case RIGHT:
                if (player.getXTile()+1 < maze.mazeSizeX){
                    Tile compare = maze.mazeTiles[player.getXTile()+1][player.getYTile()][0];
                    if (compare == null || compare.getType() == Tile.Type.TARGET){
                        Log.i("Move", "No collision detected, valid move");
                        return true;
                    }
                    else if (compare.getType() == Tile.Type.WALL){
                        Log.i("Move", "Wall detected, invalid move");
                        return false;
                    }
                    else if (player.getXTile()+2 < maze.mazeSizeY){//box, look one space further
                        Tile compare2 = maze.mazeTiles[player.getXTile()+2][player.getYTile()][0];
                        if (compare2 == null || compare2.getType() == Tile.Type.TARGET){
                            moveBox(compare, playerDirection);
                            Log.i("Move", "Box detected, valid move");
                            return true;
                        }
                    }
                }
                Log.i("Move", "Box and wall or 2 boxes detected, invalid move");
                return false;
        }
        return false;
    }

    /* Moves a box. This function assumes that it is possible to move a box!
     * Logic: if target is present at destination, move it to hidden layer and place box over it
     * Logic: if the origin had a target underneath, move it back to surface after box is moved away
     * @param Tile box: box to be moved
     * @param Status dir: direction to move
     */
    public void moveBox(Tile box, Tile.Status dir){
        Tile dest;
        Tile originBasement = maze.mazeTiles[box.getXTile()][box.getYTile()][1]; //target underneath the box
        int destX = -1;
        int destY = -1;
        switch(dir){
            case UP:
                destX = box.getXTile();
                destY = box.getYTile()-1;
                break;
            case DOWN:
                destX = box.getXTile();
                destY = box.getYTile()+1;
                break;
            case LEFT:
                destX = box.getXTile()-1;
                destY = box.getYTile();
                break;
            case RIGHT:
                destX = box.getXTile()+1;
                destY = box.getYTile();
                break;
        }
        dest = maze.mazeTiles[destX][destY][0];
        if (dest != null && dest.getType() == Tile.Type.TARGET) {
            //target at destination
            maze.moveTileDown(dest);
        }
        maze.addMazeTile(box, destX, destY);
        maze.deleteMazeTile(box.getXTile(), box.getYTile());
        box.setXTile(destX);
        box.setYTile(destY);
        if (originBasement != null){
            //target at origin
            maze.moveTileUp(originBasement);
        }
        Box tmp = (Box)box;
        tmp.update();
    }

    public void checkWin(){
        int x;
        int y;
        for (int i = 0; i < boxes.size(); i++){
            x=boxes.get(i).getXTile();
            y=boxes.get(i).getYTile();
            if (maze.mazeTiles[x][y][1]==null){
                return;
            }
        }
        Log.i("1a", "Game Over!");
        gt.setGameState(GameThread.OVER);
    }

    @Override
    public void surfaceChanged ( SurfaceHolder holder,
                                 int format , int width , int height ) {

    }
    @Override
    public void surfaceDestroyed ( SurfaceHolder holder ) {

    }

}
