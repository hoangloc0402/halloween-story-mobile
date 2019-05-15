package com.halloween.GameObjects.Enemies;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.halloween.Animation;
import com.halloween.Constants;
import com.halloween.R;

public class Phantom extends Enemy {

    public Phantom(PointF position) {
        super(Constants.PHANTOM_STARTING_HP, null, null);

        LoadAnimation();

        this.v_x = Constants.PHANTOM_V;
        this.v_y = Constants.PHANTOM_V;

        currentState = previousState = State.Appear;

        currentAnimation = appearAnimation;

        this.surroundingBox = new RectF();

//        this.currentPosition = new PointF(700, 0.8f * Constants.SCREEN_HEIGHT - currentAnimation.frameHeight);
        this.currentPosition = position;


        this.currentAnimation.play();

        this.isMovingForward = false;

        this.damage = Constants.PHANTOM_DAMAGE;
        this.attack = Constants.PHANTOM_ATTACK;
    }

    public void LoadAnimation() {
        this.moveAnimation = new Animation(R.drawable.phantom_move_114x91x6, 114 * Constants.PHANTOM_SCALE, 91 * Constants.PHANTOM_SCALE, 6, 100,
                new PointF(40 * Constants.PHANTOM_SCALE, 10 * Constants.PHANTOM_SCALE), new PointF(5 * Constants.PHANTOM_SCALE, 0 * Constants.PHANTOM_SCALE));
        this.diedAnimation = new Animation(R.drawable.phantom_died_114x91x6, 114 * Constants.PHANTOM_SCALE,
                91 * Constants.PHANTOM_SCALE, 6, 100,
                new PointF(40 * Constants.PHANTOM_SCALE, 10 * Constants.PHANTOM_SCALE), new PointF(5 * Constants.PHANTOM_SCALE, 0 * Constants.PHANTOM_SCALE));
        this.hurtAnimation = new Animation(R.drawable.phantom_hurt_114x91x1, 114 * Constants.PHANTOM_SCALE,
                91 * Constants.PHANTOM_SCALE, 1, 200,
                new PointF(30 * Constants.PHANTOM_SCALE, 10 * Constants.PHANTOM_SCALE), new PointF(5 * Constants.PHANTOM_SCALE, 0 * Constants.PHANTOM_SCALE));
        this.attackAnimation = new Animation(R.drawable.phantom_attack_114x91x12, 114 * Constants.PHANTOM_SCALE,
                91 * Constants.PHANTOM_SCALE, 12, 200,
                new PointF(30 * Constants.PHANTOM_SCALE, 0 * Constants.PHANTOM_SCALE), new PointF(5 * Constants.PHANTOM_SCALE, 0 * Constants.PHANTOM_SCALE));
        this.idleAnimation = new Animation(R.drawable.phantom_idle_114x91x4, 114 * Constants.PHANTOM_SCALE,
                91 * Constants.PHANTOM_SCALE, 4, 200,
                new PointF(40 * Constants.PHANTOM_SCALE, 5 * Constants.PHANTOM_SCALE), new PointF(5 * Constants.PHANTOM_SCALE, 5 * Constants.PHANTOM_SCALE));
        this.appearAnimation = new Animation(R.drawable.phantom_appear_114x91x6, 114 * Constants.PHANTOM_SCALE,
                91 * Constants.PHANTOM_SCALE, 6, 100,
                new PointF(30 * Constants.PHANTOM_SCALE, 10 * Constants.PHANTOM_SCALE), new PointF(0 * Constants.PHANTOM_SCALE, 0 * Constants.PHANTOM_SCALE));
    }

    @Override
    public void draw(Canvas canvas) {
//        if (isActive) {
//            if (this.IsInScreen()) {
//                RectF attack = getAttackRange();
////                RectF sur = getSurroundingBox();
////                canvas.drawRect(Constants.getRelativeXPosition(sur.left), sur.top, Constants.getRelativeXPosition(sur.right), sur.bottom, new Paint());
////                System.out.println(attack);;
////                System.out.println("current Position "+ currentPosition);
//                if (attack != null) {
//                    canvas.drawRect(Constants.getRelativeXPosition(attack.left), attack.top, Constants.getRelativeXPosition(attack.right), attack.bottom, new Paint());
//                }
//
//            }
//        }
        super.draw(canvas);
    }

    @Override
    public void reset(){
        super.reset();
        this.currentAnimation = appearAnimation;
        this.isMovingForward = false;
        this.currentState = State.Appear;
        this.currentAnimation.play();
        this.currentHP = Constants.PHANTOM_STARTING_HP;
    }

    @Override
    public RectF getAttackRange() {
        if (currentAnimation != attackAnimation) {
            return null;
        }
        int frameIndex = currentAnimation.getCurrentFrameIndex();
        if (frameIndex >= 7 && frameIndex <= 11) {
            float top = this.currentPosition.y;
            float bottom = this.currentPosition.y + this.currentAnimation.getAbsoluteFrameHeight();
            float left, right;
            float width = currentAnimation.getAbsoluteFrameWidth();
            if (currentAnimation.isFlip) {
                right = this.currentPosition.x + 9 * width / 10;
                left = right - Constants.getAbsoluteXLength(currentAnimation.getAbsoluteFrameWidth());
            } else {
                left = this.currentPosition.x - currentAnimation.getAbsoluteOffsetTopLeftX();
                right = left + Constants.getAbsoluteXLength(currentAnimation.getAbsoluteFrameWidth());
            }
            this.attackRect.set(left, top, right, bottom);
            return this.attackRect;
        } else return null;
    }

    @Override
    public void update(RectF playerSurroundingBox) {
        super.update();
        if (isActive) {
            if (currentHP <= 0) {
                isAlive = false;
                ChangeState(State.Died);
            }
            switch (currentState) {
                case Died:
                    ChangeState(State.Died);
                    if (this.currentAnimation.isLastFrame())
                        isActive = false;
                    break;
                case Hurt:
                    ChangeState(State.Hurt);
                    break;
                case Attack:
                    if (!IsPlayerInRange(playerSurroundingBox, Constants.PHANTOM_ATTACK_DISTANCE_X, Constants.PHANTOM_ATTACK_DISTANCE_Y)) {
                        ChangeState(State.Move);
                    } else
                        ChangeState(State.Attack);
                    isMovingForward = playerSurroundingBox.centerX() > this.currentPosition.x;
                    break;
                case Move:
                    ChangeState(State.Move);
                    if (isAlive) {
                        if (IsPlayerInRange(playerSurroundingBox, Constants.PHANTOM_ATTACK_DISTANCE_X, Constants.PHANTOM_ATTACK_DISTANCE_Y)) {
                            ChangeState(State.Attack);
                        } else {
                            float x, y;
                            if (isMovingForward) {
                                x = playerSurroundingBox.centerX() - (currentAnimation.getAbsoluteFrameWidth() / 2 + playerSurroundingBox.width() / 2);
                            } else {
                                x = playerSurroundingBox.centerX() + (currentAnimation.getAbsoluteFrameWidth() / 2 - playerSurroundingBox.width() / 2);
                            }
                            isMovingForward = playerSurroundingBox.centerX() > currentPosition.x;
                            y = playerSurroundingBox.centerY() - currentAnimation.getAbsoluteFrameHeight() / 2;
                            temp.set(x,y);
                            MoveToDestination(temp, Constants.PHANTOM_V);
                        }
                    }
                    break;
                case Idle:
                    ChangeState(State.Idle);
                    break;
                case Appear:
                    ChangeState(State.Appear);
                    if (this.currentAnimation.isLastFrame()) {
                        ChangeState(State.Move);
                    }
                    break;
                default:
                    break;
            }
            this.currentAnimation.flip(isMovingForward);
            currentAnimation.update();
        }
    }

    public void reset(float newX, float newY) {
        this.currentAnimation = appearAnimation;
        this.currentAnimation.reset();
        this.diedAnimation.reset();
        this.isActive = this.isAlive = true;
        this.currentHP = Constants.PHANTOM_STARTING_HP;
        this.currentPosition.set(newX, newY);
        this.currentState = this.previousState = State.Appear;
    }

}
