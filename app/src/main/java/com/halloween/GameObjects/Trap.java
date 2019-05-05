package com.halloween.GameObjects;

import android.graphics.PointF;
import android.graphics.RectF;

import com.halloween.GameObjects.Traps.TrapEffectingBox;

public abstract class Trap implements GameObject {
    protected PointF position;
    protected int frameWidth;
    protected int frameHeight;
    protected long lastWorkingTime;
    protected boolean isWorking;
    protected TrapEffectingBox[] trapEffectingBoxes;
    protected long timeBetweenTwoAnimation;
    protected float scale;
    protected RectF surroundingBox;
    protected int damage;

    public int getDamage() {
        return damage;
    }

    public PointF getPosition() {
        return position;
    }

    public RectF getSurroundingBox(){
        return null;
    }
}
