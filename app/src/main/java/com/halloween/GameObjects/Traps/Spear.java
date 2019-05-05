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

public class Spear extends Trap {
    private Animation workingSpear;

    public Spear(PointF postition, long timeBetweenTwoAnimation) {
        this.position = postition;
        this.timeBetweenTwoAnimation = timeBetweenTwoAnimation;
        this.scale = 3f;
        this.frameHeight = (int) (86 * scale);
        this.frameWidth = (int) (77 * scale);
        this.workingSpear = new Animation(R.drawable.spear_4, frameWidth, frameHeight, 4, 150);
        this.trapEffectingBoxes = new TrapEffectingBox[]{
                new TrapEffectingBox(new PointF(15f, 10f), new PointF(60f, 70f))
        };
        this.isWorking = true;
        this.lastWorkingTime = System.currentTimeMillis();
    }

    public RectF getSurroundingBox() {
        RectF surroundingBox = new RectF();
        TrapEffectingBox trapEffectingBox = trapEffectingBoxes[0];
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
        this.workingSpear.draw(canvas, new PointF(Constants.getRelativeXPosition(this.position.x, Constants.CURRENT_GAME_STATE), this.position.y));
        canvas.drawRect(Constants.getRelativeXPosition(this.position.x, Constants.CURRENT_GAME_STATE), this.position.y, Constants.getRelativeXPosition(this.position.x, Constants.CURRENT_GAME_STATE) + frameWidth, this.position.y + frameHeight, burningFirePaint);
        canvas.drawRect(getSurroundingBox(), effectPaint);
    }

    @Override
    public void update() {
        this.workingSpear.update();
        if (workingSpear.getCurrentFrameIndex() == 9) {
            this.workingSpear.stop();
            isWorking = false;
            lastWorkingTime = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - lastWorkingTime > timeBetweenTwoAnimation && !isWorking) {
            this.workingSpear.play();
            isWorking = true;
        }
    }
}
