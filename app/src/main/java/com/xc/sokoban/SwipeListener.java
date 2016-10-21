package com.xc.sokoban;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Aaron on 2016-04-22.
 */
public class SwipeListener extends GestureDetector.SimpleOnGestureListener {

    static final int SWIPE_DIS = 50;
    static final int SWIPE_VEL = 100;
    GameView gameView;

    public SwipeListener(GameView gameView) {
        this.gameView = gameView;
        Log.i("init", "SwipeListener received gameView");
    }



    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float deltaX = e1.getX() - e2.getX();
        float deltaY = e1.getY() - e2.getY();
        if (gameView.gt.gameState == GameThread.RUNNING) {
            try {
                if ((deltaY > SWIPE_DIS) && (Math.abs(velocityY) > SWIPE_VEL)) {
                    Log.i("Touch", "Up");
                    gameView.player.move(Tile.Status.UP);
                } else if ((-deltaY > SWIPE_DIS) && (Math.abs(velocityY) > SWIPE_VEL)) {
                    Log.i("Touch", "Down");
                    gameView.player.move(Tile.Status.DOWN);
                } else if ((deltaX > SWIPE_DIS) && (Math.abs(velocityX) > SWIPE_VEL)) {
                    Log.i("Touch", "Left");
                    gameView.player.move(Tile.Status.LEFT);
                } else if ((-deltaX > SWIPE_DIS) && (Math.abs(velocityX) > SWIPE_VEL)) {
                    Log.i("Touch", "Right");
                    gameView.player.move(Tile.Status.RIGHT);
                }
            } catch (Exception e) {
                //ignore
            }
            gameView.checkWin();
            return true;
        }
        else if (gameView.gt.gameState == GameThread.OVER){
            try {
                if ((deltaY > SWIPE_DIS) && (Math.abs(velocityY) > SWIPE_VEL)) {
                    Log.i("Touch", "Up");
                    gameView.loadGame(++gameView.level);
                } else if ((-deltaY > SWIPE_DIS) && (Math.abs(velocityY) > SWIPE_VEL)) {
                    Log.i("Touch", "Down");
                    gameView.loadGame(++gameView.level);
                } else if ((deltaX > SWIPE_DIS) && (Math.abs(velocityX) > SWIPE_VEL)) {
                    Log.i("Touch", "Left");
                    gameView.loadGame(++gameView.level);
                } else if ((-deltaX > SWIPE_DIS) && (Math.abs(velocityX) > SWIPE_VEL)) {
                    Log.i("Touch", "Right");
                    gameView.loadGame(++gameView.level);
                }
            } catch (Exception e) {
                //ignore
            }
            gameView.gt.setGameState(GameThread.RUNNING);
            //gameView.level++;
            return true;
        }
        return false;
    }

}
