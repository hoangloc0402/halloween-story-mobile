package com.halloween.GameObjects.Enemies;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.halloween.Animation;
import com.halloween.Constants;
import com.halloween.GameObjects.GameObject;


public class Enemy implements GameObject {

    public static final float EPSILON = 0.00000000001f;
    public Animation currentAnimation;
    protected State currentState, previousState;
    float currentHP;
    Animation diedAnimation;
    Animation idleAnimation;
    Animation appearAnimation;
    Animation moveAnimation;
    Animation attackAnimation;
    Animation defenseAnimation;
    Animation hurtAnimation;
    Animation ultimateAttackAnimation;
    boolean isAlive;
    boolean isActive;
    boolean isInvincible;

    long invincibleStartTime;

    PointF currentPosition;
    PointF leftLandMark;
    PointF rightLandMark;
    PointF temp;
    RectF surroundingBox;

    int damage, attack;

    float v_x, v_y;
    RectF attackRect;

//    float followDistance, attackDistance;
    boolean isMovingForward;

    public Enemy(float currentHP, PointF leftLandMark, PointF rightLandMark) {
        this.currentHP = currentHP;
        this.leftLandMark = leftLandMark;
        this.rightLandMark = rightLandMark;
        isAlive = true;
        isActive = true;
        this.isInvincible = false;
        this.attackRect = new RectF();
        temp = new PointF(0,0);
    }

    public boolean isActive() {
        return isActive;
    }

    //Check if the object is in the playing screen
    public boolean IsInScreen() {
        return Constants.isInScreenRange(currentPosition.x, currentAnimation.getAbsoluteAnimationWidth(), Constants.CURRENT_GAME_STATE);
//        return currentPosition.x + currentAnimation.frameWidth >= Constants.BACKGROUND_X_AXIS && currentPosition.x <= Constants.BACKGROUND_X_AXIS + Constants.SCREEN_WIDTH;
    }

    public RectF getAttackRange() {
        return null;
    }

    public int getDamage() {
        if (currentAnimation == diedAnimation)
            return 0;
        return this.damage;
    }

    public int getAttack() {
        if (currentAnimation == diedAnimation)
            return 0;
        return this.attack;
    }

    public boolean IsPlayerInRange(RectF playerSurroundingBox, float maxX, float maxY) {
        float dx4, dx1, dx2, dx3, dx5;
        float dy1 = playerSurroundingBox.top - getSurroundingBox().top;
        float dy2 = playerSurroundingBox.bottom - getSurroundingBox().bottom;
        float dy3 = playerSurroundingBox.centerY() - getSurroundingBox().centerY();
//        float dy4 = playerSurroundingBox.bottom - getSurroundingBox().top;
//        float dy5 = playerSurroundingBox.top - getSurroundingBox().bottom;
        float dy = Math.min(Math.abs(dy1), Math.abs(dy2));
        dy = Math.abs(Math.min(dy, Math.abs(dy3)));
//        dy = Math.abs(Math.min(dy, Math.abs(dy4)));
//        dy = Math.abs(Math.min(dy, Math.abs(dy5)));

        dx1 = playerSurroundingBox.left - getSurroundingBox().left;
        dx2 = playerSurroundingBox.right - getSurroundingBox().right;
        dx3 = playerSurroundingBox.left - getSurroundingBox().right;
        dx4 = playerSurroundingBox.right - getSurroundingBox().left;
        dx5 = playerSurroundingBox.centerX() - getSurroundingBox().centerX();
        float dx = Math.min(Math.abs(dx1), Math.abs(dx2));
        dx = Math.abs(Math.min(dx, Math.abs(dx3)));
        dx = Math.abs(Math.min(dx, Math.abs(dx4)));
        dx = Math.abs(Math.min(dx, Math.abs(dx5)));

        return Math.abs(dx)<maxX && Math.abs(dy) <maxY;
    }


    public PointF getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public void draw(Canvas canvas) {
        if (isActive) {
            if (this.IsInScreen()) {
                temp.set(Constants.getRelativeXPosition(this.currentPosition.x, Constants.CURRENT_GAME_STATE), this.currentPosition.y);
                this.currentAnimation.draw(canvas, temp);
//                RectF sur = getAttackRange();
//                if(sur!=null)
//                    canvas.drawRect(Constants.getRelativeXPosition(sur.left), sur.top, Constants.getRelativeXPosition(sur.right), sur.bottom, new Paint());
            }
        }
    }

    public void draw(Canvas canvas, Paint paint) {
        if (isActive) {
            if (this.IsInScreen()) {
                temp.set(Constants.getRelativeXPosition(this.currentPosition.x, Constants.CURRENT_GAME_STATE), this.currentPosition.y);
                this.currentAnimation.draw(canvas, temp, paint);
//                RectF sur = getAttackRange();
//                if(sur!=null)
//                    canvas.drawRect(Constants.getRelativeXPosition(sur.left), sur.top, Constants.getRelativeXPosition(sur.right), sur.bottom, new Paint());
            }
        }
    }

    @Override
    public void update() {
        long elapseTime = System.nanoTime();
        if (this.isInvincible) {
            if ((elapseTime - invincibleStartTime) / 1000000 > Constants.INVINCIBLE_TIME_ENEMY) {
                this.isInvincible = false;
                ChangeState(State.Move);
            }
        }
    }

    public void update(RectF playerSurroundingBox) {

    }

    public RectF getSurroundingBox() {
        return currentAnimation.getSurroundingBox(this.currentPosition);
    }

    public void ChangeState(State state) {
        previousState = currentState;
        currentState = state;

        if (currentState != previousState) {
            switch (currentState) {
                case Move:
                    currentAnimation = moveAnimation;
                    break;
                case Died:
                    currentAnimation = diedAnimation;
                    break;
                case Attack:
                    currentAnimation = attackAnimation;
                    break;
                case Hurt:
                    currentAnimation = hurtAnimation;
                    break;
                case UltimateAttack:
                    currentAnimation = ultimateAttackAnimation;
                    break;
                case Idle:
                    currentAnimation = idleAnimation;
                    break;
                case Appear:
                    currentAnimation = appearAnimation;
                    break;
                case Defense:
                    currentAnimation = defenseAnimation;
                default:
                    break;

            }
        }
    }

    public boolean IsInReach(PointF position) {
        if (position.x < rightLandMark.x && position.x > leftLandMark.x) {
            if (position.y < Math.max(leftLandMark.y, rightLandMark.y) && position.y > Math.min(leftLandMark.y, rightLandMark.y)) {
                return true;
            }
        }
        return false;
    }

    public void decreaseHealth(int damage) {
        if (this.isInvincible || currentState == State.Defense)
            return;
        else {
            currentHP -= damage;
            isAlive = currentHP > 0;
            if (isAlive) {
                ChangeState(State.Hurt);
                this.isInvincible = true;
                this.invincibleStartTime = System.nanoTime();
            } else {
                ChangeState(State.Died);
            }
        }
    }

    public float sign(float x) {
        if (x < 0)
            return -1f;
        else
            return 1f;
    }

    public void MoveToDestination(PointF position, float velocity) {
        float dx = position.x - currentPosition.x;
        float dy = position.y - currentPosition.y;

        if (dy == 0) {
            this.v_x = velocity * (dx / Math.abs(dx + EPSILON));
            this.v_y = 0;
        } else {
            float d = dx / dy;
            double alpha = Math.atan(d);
            this.v_x = (float) (velocity * Math.sin(alpha) * sign((float) (alpha * dx)));
            this.v_y = (float) (velocity * Math.cos(alpha) * sign(dy));
        }

        currentPosition.x += this.v_x;
        currentPosition.y += this.v_y;

    }

    public void reset(){
        this.isActive = true;
        this.isAlive = true;
        this.isInvincible = false;
        if (this.diedAnimation != null)
            this.diedAnimation.reset();
        if (rightLandMark != null){
            this.currentPosition.set(rightLandMark.x, rightLandMark.y);
        }
    }

    public enum State {Appear, Idle, Move, Attack, UltimateAttack, Hurt, Died, Defense} //Các state có thể có của Enemy

    public boolean isAlive(){
        return this.isAlive;
    }

}