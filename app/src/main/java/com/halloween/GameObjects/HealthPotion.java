package com.halloween.GameObjects;

import android.graphics.PointF;
import android.graphics.RectF;

public abstract class HealthPotion implements GameObject {
    protected boolean isActive;
    protected int healthVolume;
    protected PointF position;
    protected RectF surroundingBox;
    protected int potionHeight;
    protected int potionWidth;

    public RectF getSurroundingBox(){
        return null;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getHealthVolume() {
        return healthVolume;
    }
}
