package com.xc.sokoban;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Created by Jimmy on 2016/4/19.
 */
public class Wall extends Tile {

    public static int numOfWall;

    public Wall(Context context, int x, int y, int space){
        super(x, y, space);
        type = Type.WALL;
        numOfWall++;
        Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.wall);
        bitmap = Bitmap.createScaledBitmap(tmp, space, space, false);
    }

    public void draw(Canvas c){
        c.drawBitmap(bitmap, null, rect, null);
    }



}
