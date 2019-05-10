package com.halloween.GameObjects;

import android.graphics.PointF;
import android.graphics.RectF;

public abstract class Potion implements GameObject {
    protected boolean isActive;
    protected int volume;
    protected PointF position;
    protected RectF surroundingBox;
    protected int potionHeight;
    protected int potionWidth;
    public boolean isHealth;

    public RectF getSurroundingBox(){
        return null;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getVolume() {
        return volume;
    }
}
