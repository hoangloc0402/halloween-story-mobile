package com.halloween.GameObjects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.halloween.Animation;

import com.halloween.Constants;
import com.halloween.R;

import java.util.concurrent.locks.LockSupport;

public class Bullet implements GameObject {
    public static final float EPSILON = 0.00000000001f;
    Animation powerAnimation;
    Animation powerHitAnimation;
    Animation currentAnimation;
    State currentState, previousState;
    PointF currentPosition;
    float V_X;
    float V_Y;
    boolean isPower, isPowerHit;
    PointF target, temp;
    float DAMAGE;


    public PointF getTarget() {
        return target;
    }

    public boolean isPower() {
        return isPower;
    }

    public void setPower(boolean power) {
        isPower = power;
    }

    public boolean isPowerHit() {
        return isPowerHit;
    }

    public void setPowerHit(boolean powerHit) {
        isPowerHit = powerHit;
    }

    public Bullet(PointF currentPosition) {
        this.currentPosition = currentPosition;
        LoadAnimation();
        this.currentAnimation  = powerAnimation;
        this.currentState = this.previousState = State.Power;
        this.currentAnimation.play();
        V_X = 0;
        V_Y = 0;
        isPower = false;
        isPowerHit = false;
        this.DAMAGE = Constants.BULLET_DAMAGE;
        this.temp = new PointF(0,0);
        this.target = new PointF(0,0);
    }

    public void LoadAnimation() {
        this.powerAnimation = new Animation(R.drawable.dragon_power_107x107x8, 107*Constants.BULLET_SCALE ,
                107*Constants.BULLET_SCALE , 8, 50, new PointF(22*Constants.BULLET_SCALE , 22*Constants.BULLET_SCALE),
                new PointF(22*Constants.BULLET_SCALE, 22*Constants.BULLET_SCALE));
        this.powerHitAnimation = new Animation(R.drawable.dragon_power_hit_80x77x1, 80*Constants.BULLET_SCALE ,
                77*Constants.BULLET_SCALE , 1, 100, new PointF(10*Constants.BULLET_SCALE , 10*Constants.BULLET_SCALE),
                new PointF(10*Constants.BULLET_SCALE, 10*Constants.BULLET_SCALE));
    }

    @Override
    public void draw(Canvas canvas) {
//                RectF sur = currentAnimation.getSurroundingBox(currentPosition);
//                canvas.drawRect(Constants.getRelativeXPosition(sur.left), sur.top, Constants.getRelativeXPosition(sur.right), sur.bottom, new Paint());

        if (isPower || isPowerHit) {
            temp.set(Constants.getRelativeXPosition(currentPosition.x), currentPosition.y);
            currentAnimation.draw(canvas, temp);
        }
    }

    @Override
    public void update() {

    }

    void ChangeState(State state){
        previousState = currentState;
        currentState = state;
        if (currentState != previousState){
            switch (currentState){
                case Power:
                    currentAnimation = powerAnimation;
                    break;
                case PowerHit:
                    currentAnimation = powerHitAnimation;
                default:
                    break;
            }
        }
    }

    public void update(RectF playerSurroundingbox, boolean isMovingForward, PointF position) {
        if (!isPower && !isPowerHit) {
            target.set(playerSurroundingbox.centerX(), playerSurroundingbox.centerY());
            isPower = true;
            currentPosition.set(position.x, position.y);
            ChangeState(State.Power);
        }
        switch (currentState){
            case Power:
                ChangeState(State.Power);
                MoveToTarget(target);
                if(isPower && ShootPlayer(playerSurroundingbox)){
                    isPower = false;
                    isPowerHit = true;
                    MoveToTarget(target);
                    ChangeState(State.PowerHit);
                }
                else if(IsCloseToTarget(target)){
                    isPower =false;
                    ChangeState(State.PowerHit);
                }
                else{
                    MoveToTarget(target);
                }
                break;
            case PowerHit:
                ChangeState(State.Power);
                isPowerHit = false;
                break;
        }
        currentAnimation.update();
    }

    boolean ShootPlayer(RectF playerSurroundingBox) {
        RectF sur = currentAnimation.getSurroundingBox(currentPosition);
        if (sur.intersect(playerSurroundingBox)) {
            return true;
        }
        return false;
    }

    boolean IsCloseToTarget(PointF target) {
        RectF targetSur = new RectF(target.x-10, target.y-10, target.x+10, target.y+10);
        RectF sur = currentAnimation.getSurroundingBox(currentPosition);
        return sur.intersect(targetSur);
    }

    public float sign(float x) {
        if (x < 0)
            return -1f;
        else
            return 1f;
    }

    public RectF getSurroundingBox() {
        return currentAnimation.getSurroundingBox(this.currentPosition);
    }


    public void MoveToTarget(PointF target) {
        float dx = target.x - currentPosition.x;
        float dy = target.y - currentPosition.y;

        if (dy == 0) {
            this.V_X = Constants.BULLET_V * (dx / Math.abs(dx + EPSILON));
            this.V_Y = 0;
        } else {
            float d = dx / dy;
            double alpha = Math.atan(d);
            this.V_X = (float) (Constants.BULLET_V * Math.sin(alpha) * sign((float) (alpha * dx)));
            this.V_Y = (float) (Constants.BULLET_V * Math.cos(alpha) * sign(dy));
        }

        currentPosition.x += this.V_X;
        currentPosition.y += this.V_Y;

    }

    public enum State {Power, PowerHit} //Các state có thể có của Enemy
}
