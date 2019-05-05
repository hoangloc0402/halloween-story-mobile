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

public class CampFire extends Trap {
    private Animation burningFire;

    public CampFire(PointF postition, float scale) {
        this.position = postition;
        this.scale = scale;
        this.frameHeight = (int) (190 * scale);
        this.frameWidth = (int) (189 * scale);
        this.burningFire = new Animation(R.drawable.campfire_6, frameWidth, frameHeight, 6, 100);
        this.trapEffectingBoxes = new TrapEffectingBox[]{
                new TrapEffectingBox(new PointF(80f, 80f), new PointF(110f, 120f))
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
        this.burningFire.draw(canvas, new PointF(Constants.getRelativeXPosition(this.position.x, Constants.CURRENT_GAME_STATE), this.position.y));
        canvas.drawRect(Constants.getRelativeXPosition(this.position.x, Constants.CURRENT_GAME_STATE), this.position.y, Constants.getRelativeXPosition(this.position.x, Constants.CURRENT_GAME_STATE) + frameWidth, this.position.y + frameHeight, burningFirePaint);
        canvas.drawRect(getSurroundingBox(), effectPaint);
    }

    @Override
    public void update() {
        this.burningFire.update();
    }
}
