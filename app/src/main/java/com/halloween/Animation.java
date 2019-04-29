package com.halloween;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class Animation {
    private Bitmap sourceBitmap;
    private boolean isFlip;
    private int currentFrameIndex;
    private boolean isPlaying = true;
    private long frameInterval;
    private long lastFrameTime;
    private int frameCount;
    public int frameWidth;
    public int frameHeight;
    private Rect sourceRect;

    public Animation(int drawable, int frameWidth, int frameHeight, int frameCount, int animTime){
        this.sourceBitmap = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), drawable);
        this.sourceBitmap = Bitmap.createScaledBitmap(sourceBitmap, frameWidth * frameCount, frameHeight, false);
        this.isFlip = false;
        this.currentFrameIndex = 0;
        this.frameCount = frameCount;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.sourceRect = new Rect(0,0,frameWidth, frameHeight);
        this.frameInterval = animTime *1000000;
        this.lastFrameTime = System.nanoTime();
    }

    public boolean isPlaying(){ return this.isPlaying; }
    public void flip(boolean isFlip){this.isFlip = isFlip;}
    public void play(){this.isPlaying = true; }
    public void stop(){this.isPlaying = false; }

    public void draw(Canvas canvas, PointF position, Paint paint){
        if (!this.isPlaying) return;
        Rect whatToDraw = getCurrentFrame();
        RectF whereToDraw;
        if (this.isFlip){
            float x = (float)(position.x + 0.4*this.frameWidth);
            whereToDraw = new RectF(x, position.y,x + this.frameWidth,position.y + this.frameHeight);
        }
        else whereToDraw = new RectF(position.x, position.y,position.x + this.frameWidth,position.y + this.frameHeight);
        canvas.drawBitmap(this.sourceBitmap, whatToDraw, whereToDraw, paint);
    }

    public void draw(Canvas canvas, PointF position){
        this.draw(canvas, position, new Paint());
    }

    public void update(){
        if (!this.isPlaying) return;
        if (System.nanoTime()- this.lastFrameTime > this.frameInterval){
            this.currentFrameIndex++;
            if (this.currentFrameIndex >=  this.frameCount)
                this.currentFrameIndex =  0;
            this.lastFrameTime = System.nanoTime();
        }
    }

    public Rect getCurrentFrame(){
        if (isFlip){
            this.sourceRect.left = (this.currentFrameIndex +1 ) * this.frameWidth;
            this.sourceRect.right = this.sourceRect.left - this.frameWidth;
        }
        else {
            this.sourceRect.left = this.currentFrameIndex * this.frameWidth;
            this.sourceRect.right = this.sourceRect.left + this.frameWidth;
        }
        return sourceRect;
    }
    public boolean isLastFrame(){
        return this.currentFrameIndex == this.frameCount;
    }
    public void reset(){
        this.currentFrameIndex = 0;
        this.lastFrameTime = System.nanoTime();
    }
}
