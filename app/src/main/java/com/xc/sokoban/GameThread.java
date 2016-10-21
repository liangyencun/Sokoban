package com.xc.sokoban;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by Aaron on 2016-04-19.
 */
public class GameThread extends Thread {
    boolean gameLoaded;
    GameView gv;
    int gameState;
    static final int LOADING = 0;
    static final int RUNNING = 1;
    static final int OVER = 2;

    public GameThread(GameView gv) {
        this.gv=gv;
        gameLoaded = false;
    }

    public void run() {
        SurfaceHolder sh = gv.getHolder();
        Canvas c;
        gameState = LOADING;
        while( !Thread.interrupted() ) {
            if (!gameLoaded){
                gv.loadGame(1);
                gameLoaded = true;
            }
            switch(gameState){
                case LOADING:
                    gameState = RUNNING;
                    break;
                case RUNNING:
                    c = sh.lockCanvas(null);
                    try {
                        synchronized (sh) {
                            gv.draw(c);
                            //Log.d("Draw", "Frame Complete");
                        }
                    } catch (Exception e) {
                    } finally {
                        if (c != null) {
                            sh.unlockCanvasAndPost(c);
                        }
                    }
                    // Set the frame rate by setting this delay
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        // Thread was interrupted while sleeping.
                        return;
                    }
                    break;
                case OVER:
                    c = sh.lockCanvas(null);
                    try {
                        synchronized (sh) {
                            gv.draw(c);
                        }
                    } catch (Exception e) {
                    } finally {
                        if (c != null) {
                            sh.unlockCanvasAndPost(c);
                        }
                    }
                    // Set the frame rate by setting this delay
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        // Thread was interrupted while sleeping.
                        return;
                    }
                    break;
            }
        }
    }

    public void setGameState(int gameState){
        this.gameState = gameState;
    }

}
