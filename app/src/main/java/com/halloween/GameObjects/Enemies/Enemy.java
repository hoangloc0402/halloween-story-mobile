package com.halloween.GameObjects.Enemies;

import android.graphics.Canvas;
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
    RectF surroundingBox;

    int damage, attack;

    float v_x, v_y;
    RectF attackRect;

    float followDistance, attackDistance;
    boolean isMovingForward;

    public Enemy(float currentHP, PointF leftLandMark, PointF rightLandMark, float followDistance, float attackDistance) {
        this.currentHP = currentHP;
        this.leftLandMark = leftLandMark;
        this.rightLandMark = rightLandMark;
        this.followDistance = followDistance;
        this.attackDistance = attackDistance;
        isAlive = true;
        isActive = true;
        this.isInvincible = false;
        this.attackRect = new RectF();
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
        return this.damage;
    }

    public int getAttack() {
        return this.attack;
    }

    public boolean IsPlayerInRange(RectF playerSurroundingBox, float maxDistance) {
        float dx;
        float dy1 = playerSurroundingBox.top - getSurroundingBox().top;
        float dy2 = playerSurroundingBox.bottom - getSurroundingBox().bottom;
        float dy3 = playerSurroundingBox.centerY() - getSurroundingBox().centerY();
        float dy = Math.min(Math.abs(dy1), Math.abs(dy2));
        dy = Math.min(dy, Math.abs(dy3));
        if (currentAnimation.isFlip) {
            dx = playerSurroundingBox.left - getSurroundingBox().left - currentAnimation.getAbsoluteAnimationWidth();
        } else {
            dx = playerSurroundingBox.right - getSurroundingBox().left;
        }
        float d = dx * dx + dy * dy;
        if (d < maxDistance)
            return true;
        return false;
    }

//    public boolean IsAlive(){
//        return currentHP > 0;
//    }

    public PointF getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public void draw(Canvas canvas) {
        if (isActive) {
            if (this.IsInScreen()) {
                this.currentAnimation.draw(canvas, new PointF(Constants.getRelativeXPosition(this.currentPosition.x, Constants.CURRENT_GAME_STATE), this.currentPosition.y));
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
                case Defense:
                    currentAnimation = defenseAnimation;
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
        if (this.isInvincible)
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

    public enum State {Appear, Idle, Move, Attack, Defense, UltimateAttack, Hurt, Died} //Các state có thể có của Enemy


}