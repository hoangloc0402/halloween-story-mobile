package com.halloween.GameObjects.Traps;

import android.graphics.PointF;

public class TrapEffectingBox {
    private PointF topLeft;
    private PointF bottomRight;

    public TrapEffectingBox(PointF topLeft, PointF bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public PointF getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(PointF topLeft) {
        this.topLeft = topLeft;
    }

    public float getWidth() {
        return bottomRight.x - topLeft.x;
    }

    public float getHeight() {
        return bottomRight.y - topLeft.y;
    }

    public PointF getBottomRight() {
        return bottomRight;
    }

    public void setBottomRight(PointF bottomRight) {
        this.bottomRight = bottomRight;
    }
}
