package com.halloween.GameObjects;

import android.graphics.PointF;

import com.halloween.GameObjects.Traps.TrapEffectingBox;

public abstract class Trap {
    protected PointF position;
    protected int frameWidth;
    protected int frameHeight;
    protected long lastWorkingTime;
    protected boolean isWorking;
    protected TrapEffectingBox[] trapEffectingBoxes;
    protected long timeBetweenTwoAnimation;
    protected float scale;

    public PointF getPosition() {
        return position;
    }
}
