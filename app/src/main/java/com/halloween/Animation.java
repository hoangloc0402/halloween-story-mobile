package com.halloween;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class Animation {
    private Bitmap sourceBitmap;
    private boolean isFlip;
    private int currentFrameIndex;
    private boolean isPlaying = false;
    private float frameInterval;
    private long lastFrameTime;
    private int frameCount;
    public int frameWidth;
    public int frameHeight;
    private Rect sourceRect;

    public Animation(int drawable, int frameWidth, int frameHeight, int frameCount, float animTime){
        this.sourceBitmap = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), drawable);
        this.sourceBitmap = Bitmap.createScaledBitmap(sourceBitmap, frameWidth * frameCount, frameHeight, false);
        this.isFlip = false;
        this.currentFrameIndex = 0;
        this.frameCount = frameCount;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;

        sourceRect = new Rect(0,0,frameWidth, frameHeight);
        frameInterval = animTime / frameCount;
        lastFrameTime = System.currentTimeMillis();
    }

    public boolean isPlaying(){ return this.isPlaying; }
    public void flip(boolean isFlip){this.isFlip = isFlip;}
    public void play(){this.isPlaying = true; }
    public void stop(){this.isPlaying = false; }

    public void draw(Canvas canvas, Point position){
        if (!this.isPlaying) return;
        Rect whatToDraw = getCurrentFrame();
        RectF whereToDraw = new RectF(position.x, position.y,position.x + frameWidth,position.y + frameHeight);
        if (isFlip){
            canvas.scale(-1, 1, frameHeight/2,frameWidth/2);
            canvas.drawBitmap(sourceBitmap, whatToDraw, whereToDraw, new Paint());
            canvas.scale(-1, 1, frameHeight/2,frameWidth/2);
        }
        else {
            canvas.drawBitmap(sourceBitmap, whatToDraw, whereToDraw, new Paint());
        }
    }

    public void update(){
        if (!this.isPlaying) return;
        if (System.currentTimeMillis() - lastFrameTime > frameInterval){
            currentFrameIndex++;
            if (currentFrameIndex >=  frameCount)
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