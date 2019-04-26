package com.halloween;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.view.SurfaceHolder;

import com.halloween.GameScreens.GamePanel;

public class MainThread extends Thread{
    public static final int MAX_FPS = 60;
    private double averageFPS;
    private boolean isRunning;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    public static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel){
        super();
        this.surfaceHolder = surfaceHolder;
        this.surfaceHolder.setFormat(0x00000004);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        this.gamePanel = gamePanel;
    }

    public void setRunning(boolean isRunning){
        this.isRunning = isRunning;
    }

    public void run(){
        long startTime;
        long timeMillis = 1000/MAX_FPS;
        long waitTime;
        int frameCount = 0;
        long totalTime = 0;
        long targetTime = 1000/MAX_FPS;

        while(isRunning){
            startTime = System.nanoTime();
            canvas = null;
            try{
                canvas = this.surfaceHolder.lockHardwareCanvas();
                synchronized (surfaceHolder){
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            } catch(Exception e){
                e.printStackTrace();
            } finally{
                if (canvas != null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
            timeMillis = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - timeMillis;
            try{
                if(waitTime > 0)
                    this.sleep(waitTime);
            } catch(Exception e){
                e.printStackTrace();
            }
            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if(frameCount == MAX_FPS){
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println(averageFPS);
            }
        }
    }
}