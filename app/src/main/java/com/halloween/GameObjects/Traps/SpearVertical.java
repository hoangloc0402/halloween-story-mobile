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

public class SpearVertical extends Trap {
    private Animation burningFire;

    public SpearVertical(PointF postition, long timeBetweenTwoAnimation, float scale) {
        this.position = postition;
        this.timeBetweenTwoAnimation = timeBetweenTwoAnimation;
        this.scale = scale;
        this.frameHeight = (int) (106 * scale);
        this.frameWidth = (int) (69 * scale);
        this.burningFire = new Animation(R.drawable.spearv_16, frameWidth, frameHeight, 16, 100);
        this.trapEffectingBoxes = new TrapEffectingBox[]{
                new TrapEffectingBox(new PointF(0f, 0f), new PointF(0f, 0f)),
                new TrapEffectingBox(new PointF(12f, 87f), new PointF(54f, 95f)),
                new TrapEffectingBox(new PointF(12f, 82f), new PointF(57f, 95f)),
                new TrapEffectingBox(new PointF(12f, 77f), new PointF(57f, 95f)),
                new TrapEffectingBox(new PointF(12f, 12f), new PointF(57f, 95f)),
                new TrapEffectingBox(new PointF(12f, 17f), new PointF(57f, 95f)),
                new TrapEffectingBox(new PointF(12f, 19f), new PointF(57f, 95f)),
                new TrapEffectingBox(new PointF(12f, 21f), new PointF(57f, 95f)),
                new TrapEffectingBox(new PointF(12f, 77f), new PointF(57f, 95f)),
                new TrapEffectingBox(new PointF(12f, 82f), new PointF(57f, 95f)),
                new TrapEffectingBox(new PointF(12f, 87f), new PointF(54f, 95f)),
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
