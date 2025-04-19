package com.gamebai.tienlenmiennam.main;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

/**
 * "khung tranh" của game
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
    Game game;
    private SurfaceHolder holder;
    public GamePanel(MainActivity mainActivity){
        super(mainActivity);
        holder=getHolder();
        holder.addCallback(this);
        game=new Game(holder,mainActivity);
        setKeepScreenOn(true);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        return game.touchEvent(event);
    }
    /**
     * @param surfaceHolder
     */
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

    }

    /**
     * @param surfaceHolder
     * @param i
     * @param i1
     * @param i2
     */
    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    /**
     * @param surfaceHolder
     */
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    public Game getGame() {
        return game;
    }
}
