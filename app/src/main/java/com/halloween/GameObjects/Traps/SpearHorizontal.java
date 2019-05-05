package com.halloween.GameObjects.Traps;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.halloween.Animation;
import com.halloween.Constants;
import com.halloween.GameObjects.Trap;
import com.halloween.R;

public class SpearHorizontal extends Trap {
    private Animation burningFire;

    public SpearHorizontal(PointF postition, long timeBetweenTwoAnimation, float scale) {
        this.position = postition;
        this.timeBetweenTwoAnimation = timeBetweenTwoAnimation;
        this.scale = scale;
        this.frameHeight = (int) (69 * scale);
        this.frameWidth = (int) (107 * scale);
        this.burningFire = new Animation(R.drawable.spearh_16, frameWidth, frameHeight, 16, 100);
        this.trapEffectingBoxes = new TrapEffectingBox[]{
                new TrapEffectingBox(new PointF(0f, 0f), new PointF(0f, 0f)),
                new TrapEffectingBox(new PointF(10f, 15f), new PointF(20f, 55f)),
                new TrapEffectingBox(new PointF(10f, 15f), new PointF(25f, 55f)),
                new TrapEffectingBox(new PointF(10f, 15f), new PointF(30f, 55f)),
                new TrapEffectingBox(new PointF(10f, 15f), new PointF(95f, 55f)),
                new TrapEffectingBox(new PointF(10f, 15f), new PointF(95f, 55f)),
                new TrapEffectingBox(new PointF(10f, 15f), new PointF(95f, 55f)),
                new TrapEffectingBox(new PointF(10f, 15f), new PointF(95f, 55f)),
                new TrapEffectingBox(new PointF(10f, 15f), new PointF(30f, 55f)),
                new TrapEffectingBox(new PointF(10f, 15f), new PointF(25f, 55f)),
                new TrapEffectingBox(new PointF(10f, 15f), new PointF(20f, 55f)),
                new TrapEffectingBox(new PointF(0f, 0f), new PointF(0f, 0f)),
                new TrapEffectingBox(new PointF(0f, 0f), new PointF(0f, 0f)),
                new TrapEffectingBox(new PointF(0f, 0f), new PointF(0f, 0f)),
                new TrapEffectingBox(new PointF(0f, 0f), new PointF(0f, 0f)),
                new TrapEffectingBox(new PointF(0f, 0f), new PointF(0f, 0f))
        };
        this.isWorking = true;
        this.lastWorkingTime = System.currentTimeMillis();
    }

    public RectF getSurroundingBox() {
        RectF surroundingBox = new RectF();
        TrapEffectingBox trapEffectingBox = trapEffectingBoxes[burningFire.getCurrentFrameIndex()];
        PointF topLeft = trapEffectingBox.getTopLeft();
        PointF bottomRight = trapEffectingBox.getBottomRight();
        surroundingBox.set(Constants.getRelativeXPosition(this.position.x, Constants.CURRENT_GAME_STATE) + topLeft.x * scale, topLeft.y * scale + this.position.y, Constants.getRelativeXPosition(this.position.x, Constants.CURRENT_GAME_STATE) + bottomRight.x * scale, bottomRight.y * scale + this.position.y);
        return surroundingBox;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint burningFirePaint = new Paint();
        burningFirePaint.setColor(Color.WHITE);
        burningFirePaint.setAlpha(100);
        Paint effectPaint = new Paint();
        effectPaint.setColor(Color.RED);
        effectPaint.setAlpha(100);
        this.burningFire.draw(canvas, new PointF(Constants.getRelativeXPosition(this.position.x, Constants.CURRENT_GAME_STATE), this.position.y));
        canvas.drawRect(Constants.getRelativeXPosition(this.position.x, Constants.CURRENT_GAME_STATE), this.position.y, Constants.getRelativeXPosition(this.position.x, Constants.CURRENT_GAME_STATE) + frameWidth, this.position.y + frameHeight, burningFirePaint);
        canvas.drawRect(getSurroundingBox(), effectPaint);
    }

    @Override
    public void update() {
        this.burningFire.update();
        if (burningFire.getCurrentFrameIndex() == 9) {
            this.burningFire.stop();
            isWorking = false;
            lastWorkingTime = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - lastWorkingTime > timeBetweenTwoAnimation && !isWorking) {
            this.burningFire.play();
            isWorking = true;
        }
    }
}
