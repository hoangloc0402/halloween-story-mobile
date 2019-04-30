package com.halloween;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

public class Animation {
    private Bitmap sourceBitmap;
    private boolean isFlip;
    private int currentFrameIndex;
    private boolean isPlaying;
    private long frameInterval;
    private long lastFrameTime;
    private int frameCount;
    public int frameWidth, frameHeight;
    public float animationWidth, animationHeight;
    public PointF offsetTopLeft, offsetBottomRight;
    private Rect sourceRect;
    private RectF surroundingRect;

    public Animation(int drawable, int frameWidth, int frameHeight, int frameCount, int animTime, PointF offsetTopLeft, PointF offsetBottomRight){
        this.sourceBitmap = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), drawable);
        this.sourceBitmap = Bitmap.createScaledBitmap(sourceBitmap, frameWidth * frameCount, frameHeight, false);
        this.isFlip = false;
        this.currentFrameIndex = 0;
        this.frameCount = frameCount;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.animationWidth = frameWidth - offsetTopLeft.x - offsetBottomRight.x;
        this.animationHeight = frameHeight - offsetTopLeft.y - offsetBottomRight.y;
        this.offsetTopLeft = offsetTopLeft;
        this.offsetBottomRight = offsetBottomRight;
        this.sourceRect = new Rect(0,0,frameWidth, frameHeight);
        this.surroundingRect = new RectF();
        this.frameInterval = animTime *1000000;
        this.isPlaying = true;
        this.lastFrameTime = System.nanoTime();
    }

    public boolean isPlaying(){ return this.isPlaying; }
    public void flip(boolean isFlip){this.isFlip = isFlip;}
    public void play(){this.isPlaying = true; }
    public void stop(){this.isPlaying = false;}

    public void draw(Canvas canvas, PointF position, Paint paint){
        if (!this.isPlaying) currentFrameIndex = 0;
        Rect whatToDraw = getCurrentFrame();
        RectF whereToDraw = getDestinationRect(position);
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

    public RectF getDestinationRect(PointF position){
        float left, top;
        if (this.isFlip){
            left = position.x - this.offsetBottomRight.x;
            top = position.y - this.offsetTopLeft.y;
        }
        else {
            left = position.x - this.offsetTopLeft.x;
            top = position.y - this.offsetTopLeft.y;
        }
        return new RectF(left, top,left + this.frameWidth,top + this.frameHeight);
    }

    public RectF getSurroundingBox(PointF position){
        this.surroundingRect.set(position.x, position.y, position.x + this.animationWidth, position.y + animationHeight);
        return this.surroundingRect;
    }

    public boolean isLastFrame(){
        return this.currentFrameIndex == this.frameCount -1;
    }
    public void reset(){
        this.currentFrameIndex = 0;
        this.lastFrameTime = System.nanoTime();
    }
}
