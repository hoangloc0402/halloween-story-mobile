package com.halloween.GameContents;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import com.halloween.Animation;
import com.halloween.Constants;
import com.halloween.GameObjects.GameObject;
import com.halloween.R;

public class Portal implements GameObject {

    private Animation portalAnimation;
    private PointF portalPosition;

    public Portal() {
        this.portalAnimation = new Animation(R.drawable.portal_300x300x12, 300, 300, 12, 100, new PointF(0,0), new PointF(0, 0));
        this.portalAnimation.play();
        this.portalPosition = new PointF(9600f, Constants.SCREEN_HEIGHT * 0.8f - portalAnimation.frameHeight);
    }

    public boolean isInRange() {
        return Constants.isInScreenRange(this.portalPosition.x, this.portalAnimation.frameWidth, Constants.GAME_STATE.PLAY);
    }

    public boolean isInSuckingRange(RectF box) {
        float left = this.portalPosition.x + this.portalAnimation.frameWidth * 0.25f;
        float top = this.portalPosition.y + this.portalAnimation.frameHeight * 0.25f;
        float right = this.portalPosition.x + this.portalAnimation.frameWidth * 0.75f;
        float bottom = this.portalPosition.y + this.portalAnimation.frameHeight * 0.75f;
        Log.d("portal", left + " " + top + " " + right + " " + bottom);
        Log.d("main", box.toShortString());
        return ((left <= box.left && box.left <= right) || (left <= box.right && box.right <= right))
                && ((top <= box.top && box.top <= bottom)||(top <= box.bottom && box.bottom <= bottom));
    }

    @Override
    public void draw(Canvas canvas) {
        this.portalAnimation.draw(canvas, new PointF(Constants.getRelativeXPosition(this.portalPosition.x, Constants.GAME_STATE.PLAY),this.portalPosition.y));
    }

    @Override
    public void update() {
        this.portalAnimation.update();
    }
}
