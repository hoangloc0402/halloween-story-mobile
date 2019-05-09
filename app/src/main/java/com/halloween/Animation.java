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
    public boolean isFlip;
    private int currentFrameIndex;
    private boolean isPlaying;
    private long frameInterval;
    private long lastFrameTime;
    private int frameCount;
    private int frameWidth, frameHeight;
    private float animationWidth, animationHeight;
    private PointF offsetTopLeft, offsetBottomRight;
    private Rect sourceRect;
    private RectF destinationRect, surroundingRect;
    private Paint myPaint;

    public Animation(int drawable, int frameWidth, int frameHeight, int frameCount, int animTime, PointF offsetTopLeft, PointF offsetBottomRight) {
        Bitmap bitmap = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), drawable);
        this.sourceBitmap = Bitmap.createScaledBitmap(bitmap, frameWidth * frameCount, frameHeight, false);
        this.isFlip = false;
        this.currentFrameIndex = 0;
        this.frameCount = frameCount;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.offsetTopLeft = offsetTopLeft;
        this.offsetBottomRight = offsetBottomRight;
        this.animationWidth = this.frameWidth - offsetTopLeft.x - offsetBottomRight.x;
        this.animationHeight = this.frameHeight - offsetTopLeft.y - offsetBottomRight.y;
//        this.animationWidth = Constants.getAbsoluteXLength(this.animationWidth);
//        this.animationHeight = Constants.getAbsoluteXLength(this.animationHeight);
        this.sourceRect = new Rect(0, 0, frameWidth, frameHeight);
        this.destinationRect = new RectF();
        this.surroundingRect = new RectF();
        this.frameInterval = animTime * 1000000;
        this.isPlaying = true;
        this.myPaint = new Paint();
        this.lastFrameTime = System.nanoTime();
    }

    public Animation(int drawable, float frameWidth, float frameHeight, int frameCount, int animTime) {
        this(drawable, (int)frameWidth, (int)frameHeight, frameCount, animTime, new PointF(0f, 0f), new PointF(0f, 0f));
    }
    public Animation(int drawable, float frameWidth, float frameHeight, int frameCount, int animTime, PointF offsetTopLeft) {
        this(drawable, (int)frameWidth, (int)frameHeight, frameCount, animTime, offsetTopLeft, new PointF(0f, 0f));
    }
    public Animation(int drawable, float frameWidth, float frameHeight, int frameCount, int animTime, PointF offsetTopLeft, PointF offsetBottomRight){
        this(drawable, (int)frameWidth, (int)frameHeight, frameCount, animTime, offsetTopLeft, offsetBottomRight);
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public void flip(boolean isFlip) {
        this.isFlip = isFlip;
    }

    public void play() {
        this.isPlaying = true;
    }

    public void stop() {
        this.isPlaying = false;
    }

    public void draw(Canvas canvas, PointF position, Paint paint) {
        if (!this.isPlaying) currentFrameIndex = 0;
        Rect whatToDraw = getCurrentFrame();
        RectF whereToDraw = getDestinationRect(position);
        if (isFlip) {
            canvas.save();
            canvas.scale(-1,1, position.x + animationWidth/2, 0);
//            canvas.translate(- offsetTopLeft.x, 0);
            canvas.drawBitmap(this.sourceBitmap, whatToDraw, whereToDraw, paint);
            canvas.restore();
        }
        else
            canvas.drawBitmap(this.sourceBitmap, whatToDraw, whereToDraw, paint);
    }

    public void draw(Canvas canvas, PointF position) {
        this.draw(canvas, position, myPaint);
    }

    public void update() {
        if (!this.isPlaying) return;
        if (System.nanoTime() - this.lastFrameTime > this.frameInterval) {
            this.currentFrameIndex++;
            if (this.currentFrameIndex >= this.frameCount)
                this.currentFrameIndex = 0;
            this.lastFrameTime = System.nanoTime();
        }
    }

    public Rect getCurrentFrame() {
//        if (this.isFlip) {
//            this.sourceRect.left = (this.currentFrameIndex + 1) * this.frameWidth;
//            this.sourceRect.right = this.sourceRect.left - this.frameWidth;
//        } else {
        this.sourceRect.left = this.currentFrameIndex * this.frameWidth;
        this.sourceRect.right = this.sourceRect.left + this.frameWidth;
//        }
        return sourceRect;
    }

    public RectF getDestinationRect(PointF position) {
        float left, top;
//        if (this.isFlip) {
//            left = position.x - this.offsetBottomRight.x;
//            top = position.y - this.offsetTopLeft.y;
//        } else {
        left = position.x - this.offsetTopLeft.x;
        top = position.y - this.offsetTopLeft.y;
//        }
        this.destinationRect.set(left, top, left + this.frameWidth, top + this.frameHeight);
        return destinationRect;
    }

    public RectF getSurroundingBox(PointF position) {
        this.surroundingRect.set(position.x, position.y, position.x + this.getAbsoluteAnimationWidth(), position.y + this.animationHeight);
        return this.surroundingRect;
    }

    public boolean isLastFrame() {
        return this.currentFrameIndex == this.frameCount - 1;
    }

    public void reset() {
        this.currentFrameIndex = 0;
        this.lastFrameTime = System.nanoTime();
    }

    public int getCurrentFrameIndex() {
        return currentFrameIndex;
    }

    public float getAbsoluteFrameWidth() { return Constants.getAbsoluteXLength(frameWidth); }

    public float getAbsoluteFrameHeight() { return frameHeight; }

    public float getAbsoluteOffsetTopLeftX() { return Constants.getAbsoluteXLength(offsetTopLeft.x); }

    public float getAbsoluteOffsetTopLeftY() { return offsetTopLeft.y; }

    public float getAbsoluteOffsetBottomRightX() { return Constants.getAbsoluteXLength(offsetBottomRight.x); }

    public float getAbsoluteOffsetBottomRightY() { return offsetBottomRight.y; }

    public float getAbsoluteAnimationWidth() { return Constants.getAbsoluteXLength(animationWidth); }

    public float getAbsoluteAnimationHeight() { return animationHeight; }
}