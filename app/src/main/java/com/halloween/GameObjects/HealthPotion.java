package com.halloween.GameObjects;

import android.graphics.PointF;
import android.graphics.RectF;

public abstract class HealthPotion implements GameObject {
    protected boolean isActive;
    protected int healthVolume;
    protected PointF position;

    public RectF getSurroundingBox(){
        return null;
    }
}
