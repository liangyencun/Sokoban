package com.xc.sokoban;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Jimmy on 2016/4/19.
 */
public class Box extends Tile{

    public static int numOfSpot;

    public Box(Context context, int x, int y, int space, GameView gameView){
        super(x, y, space);
        numOfSpot++;
        type = Type.BOX;
        Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.box);
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
                y -= space/10;
                break;
            case DOWN:
                y += space/10;
                break;
            case RIGHT:
                x += space/10;
                break;
            case LEFT:
                x -= space/10;
                break;
        }*/
        rect = new RectF(x, y, x+space, y+space);
    }

    public void setRect(int x, int y){
        this.x = x;
        this.y = y;
    }

}
