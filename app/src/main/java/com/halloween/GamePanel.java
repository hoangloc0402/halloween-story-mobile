package com.halloween;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.halloween.GameObjects.MainCharacter;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
    private MainThread mainThread;
    private MainCharacter mainCharacter = new MainCharacter();
//    private SceneManager manager;

    public GamePanel(Context context){
        super(context);
//        manager = new SceneManager();
        getHolder().addCallback(this);
        mainThread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    public void surfaceCreated(SurfaceHolder holder){
        mainThread = new MainThread(getHolder(), this);
        mainThread.setRunning(true);
        mainThread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while(retry){
            try{
                mainThread.setRunning(false);
                mainThread.join();
                retry = false;
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event){
//        manager.recieveTouch(event);
        return true;
        //return super.onTouchEvent(event);
    }

    public void update(){
//        manager.update();
        mainCharacter.update();
    }

    public void draw(Canvas canvas){
        super.draw(canvas);
        canvas.drawColor(Color.WHITE);
        mainCharacter.draw(canvas);
//        manager.draw(canvas);
    }

}