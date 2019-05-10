package com.halloween.GameContents;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Pair;

import com.halloween.Animation;
import com.halloween.Constants;
import com.halloween.GameObjects.GameObject;
import com.halloween.R;

public class Portal implements GameObject {

    private Animation portalAnimation;
    private PointF portalPosition;

    public Portal() {
        this.portalAnimation = new Animation(R.drawable.portal_300x300x12, 300, 300, 12, 100, new PointF(0, 0), new PointF(0, 0));
        this.portalAnimation.play();
        this.portalPosition = new PointF(9810f, Constants.SCREEN_HEIGHT * 0.8f - portalAnimation.getAbsoluteAnimationHeight() * 0.8f);
    }

    public boolean isInRange() {
        return Constants.isInScreenRange(this.portalPosition.x, this.portalAnimation.getAbsoluteAnimationWidth(), Constants.GAME_STATE.PLAY);
    }

    public Pair<Boolean, PointF> isInSuckingRange(RectF box) {
        float left = this.portalPosition.x - this.portalAnimation.getAbsoluteAnimationWidth();
        float top = this.portalPosition.y - this.portalAnimation.getAbsoluteAnimationHeight();
        float right = this.portalPosition.x + this.portalAnimation.getAbsoluteAnimationWidth() * 2;
        float bottom = this.portalPosition.y + this.portalAnimation.getAbsoluteAnimationHeight() * 2;
        Boolean isInRange = false;
        PointF point = new PointF(0.0f, 0.0f);
        if (((left <= box.left && box.left <= right) || (left <= box.right && box.right <= right))
                && ((top <= box.top && box.top <= bottom) || (top <= box.bottom && box.bottom <= bottom)) & !isInTransitionRange(box)) {
            isInRange = true;
            PointF centerPortal = new PointF(left + Math.abs(right - left) / 2, top + this.portalAnimation.getAbsoluteAnimationHeight() / 2);
            PointF centerObject = new PointF(box.left + Math.abs(box.right - box.left) / 2, box.top + Math.abs(box.bottom - box.top) / 2);
            float x = (centerPortal.x > centerObject.x ? 1f : -1f);
            float y = (centerPortal.y > centerPortal.y ? 1f : -1f);
            point = new PointF(x, y);
        }
        return Pair.create(isInRange, point);
    }

    public boolean isInTransitionRange(RectF box) {
        float left = this.portalPosition.x + this.portalAnimation.getAbsoluteAnimationWidth() * 0.25f;
        float top = this.portalPosition.y + this.portalAnimation.getAbsoluteAnimationHeight() * 0.25f;
        float right = this.portalPosition.x + this.portalAnimation.getAbsoluteAnimationWidth() * 0.75f;
        float bottom = this.portalPosition.y + this.portalAnimation.getAbsoluteAnimationHeight() * 0.75f;
//        Log.d("portal", left + " " + top + " " + right + " " + bottom);
//        Log.d("main", box.toShortString());
        return ((left <= box.left && box.left <= right) || (left <= box.right && box.right <= right))
                && ((top <= box.top && box.top <= bottom) || (top <= box.bottom && box.bottom <= bottom));
    }

    @Override
    public void draw(Canvas canvas) {
        this.portalAnimation.draw(canvas, new PointF(Constants.getRelativeXPosition(this.portalPosition.x), this.portalPosition.y));
    }

    @Override
    public void update() {
        this.portalAnimation.update();
    }
}
