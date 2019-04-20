package com.halloween;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class Animation {
    private Bitmap sourceBitmap;
    private  int currentFrameIndex;
    private boolean isPlaying = false;
    private float frameInterval;
    private long lastFrameTime;
    private int frameCount;
    private int frameWidth;
    private int frameHeight;
    private Rect sourceRect;

    public Animation(String bitmapName, int frameWidth, int frameHeight, int frameCount, float animTime){
        this.sourceBitmap = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.attack_83x97x4);
        this.sourceBitmap = Bitmap.createScaledBitmap(sourceBitmap, frameWidth * frameCount, frameHeight, false);

        this.currentFrameIndex = 0;
        this.frameCount = frameCount;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        sourceRect = new Rect(0,0,frameWidth, frameHeight);
        frameInterval = animTime / frameCount;
        lastFrameTime = System.currentTimeMillis();
    }

    public boolean isPlaying(){ return this.isPlaying; }
    public void play(){this.isPlaying = true; }
    public void stop(){this.isPlaying = false; }

    public void draw(Canvas canvas){
        if (!this.isPlaying) return;
        Rect whatToDraw = getCurrentFrame();
        RectF whereToDraw = new RectF(50,50,50 + frameWidth,50 + frameHeight);
        canvas.drawBitmap(sourceBitmap, whatToDraw, whereToDraw, new Paint());
    }

    public void update(){
        if (!this.isPlaying) return;
        if (System.currentTimeMillis() - lastFrameTime > frameInterval){
            currentFrameIndex++;
            if (currentFrameIndex >= frameCount)
                currentFrameIndex =  0;
            lastFrameTime = System.currentTimeMillis();
        }
    }

    public Rect getCurrentFrame(){
        this.sourceRect.left = this.currentFrameIndex * frameWidth;
        this.sourceRect.right = this.sourceRect.left + frameWidth;
        return sourceRect;
    }

}