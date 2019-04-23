package com.halloween.GameScreens;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.halloween.MainThread;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
    private MainThread mainThread;
    private ScreenManager screenManager;

    public GamePanel(Context context){
        super(context);
        screenManager = new ScreenManager();
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
        screenManager.receiveTouch(event);
        return true;
        //return super.onTouchEvent(event);
    }

    public void update(){
        screenManager.update();
    }

    public void draw(Canvas canvas){
        super.draw(canvas);
//        canvas.drawColor(Color.WHITE);
//        mainCharacter.draw(canvas);
        screenManager.draw(canvas);
    }
}