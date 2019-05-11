package com.halloween.GameObjects.Enemies;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.halloween.Animation;
import com.halloween.Constants;
import com.halloween.R;

public class Dragon extends Enemy {
    Animation powerAnimation, powerHitAnimation;

    boolean power, powerHit;


    public Dragon(PointF leftLandMark, PointF rightLandMark) {
        super(Constants.ZOMBIE_STARTING_HP, leftLandMark, rightLandMark, Constants.DRAGON_FOLLOW_DISTANCE, Constants.DRAGON_ATTACK_DISTANCE);

        LoadAnimation();

        this.v_x = Constants.DRAGON_V;
        this.v_y = 0;

        currentState = previousState = State.Hurt;

        currentAnimation = hurtAnimation;

        this.surroundingBox = new RectF();

        this.currentPosition = new PointF(rightLandMark.x, rightLandMark.y);

        this.currentAnimation.play();

        this.isMovingForward = false;

        this.power = false;
        this.powerHit = false;

        this.damage = Constants.DRAGON_DAMAGE;
        this.attack = Constants.DRAGON_ATTACK;
    }

    public void LoadAnimation() {
        this.moveAnimation = new Animation(R.drawable.dragon_move_188x176x6, 188 * Constants.DRAGON_SCALE, 176 * Constants.DRAGON_SCALE, 6, 100,
                new PointF(22 * Constants.DRAGON_SCALE, 60 * Constants.DRAGON_SCALE), new PointF(65 * Constants.DRAGON_SCALE, 20 * Constants.DRAGON_SCALE));
        this.attackAnimation = new Animation(R.drawable.dragon_attack_227x187x6, 227 * Constants.DRAGON_SCALE,
                187 * Constants.DRAGON_SCALE, 6, 100, new PointF(30 * Constants.DRAGON_SCALE, 25 * Constants.DRAGON_SCALE),
                new PointF(0, 0 * Constants.DRAGON_SCALE));
        this.diedAnimation = new Animation(R.drawable.dragon_died_175x176x5, 175 * Constants.DRAGON_SCALE,
                176 * Constants.DRAGON_SCALE, 5, 100, new PointF(22 * Constants.DRAGON_SCALE, 60 * Constants.DRAGON_SCALE),
                new PointF(50 * Constants.DRAGON_SCALE, 20 * Constants.DRAGON_SCALE));
        this.hurtAnimation = new Animation(R.drawable.dragon_hurt_175x176x1, 175 * Constants.DRAGON_SCALE,
                176 * Constants.DRAGON_SCALE, 1, 1000, new PointF(22 * Constants.DRAGON_SCALE, 60 * Constants.DRAGON_SCALE),
                new PointF(65 * Constants.DRAGON_SCALE, 15 * Constants.DRAGON_SCALE));
        this.ultimateAttackAnimation = new Animation(R.drawable.dragon_ultimate_280x185x12, 280 * Constants.DRAGON_SCALE,
                185 * Constants.DRAGON_SCALE, 12, 1000, new PointF(22 * Constants.DRAGON_SCALE, 0), new PointF(0, 0));
        this.idleAnimation = new Animation(R.drawable.dragon_idle_188x176x6, 188 * Constants.DRAGON_SCALE,
                176 * Constants.DRAGON_SCALE, 6, 1000, new PointF(22 * Constants.DRAGON_SCALE, 0), new PointF(0, 0));
        this.powerAnimation = new Animation(R.drawable.dragon_power_107x107x8, 107 * Constants.DRAGON_SCALE,
                107 * Constants.DRAGON_SCALE, 8, 1000, new PointF(22 * Constants.DRAGON_SCALE, 0), new PointF(0, 0));
        this.powerHitAnimation = new Animation(R.drawable.dragon_power_hit_80x77x1, 80 * Constants.DRAGON_SCALE,
                77 * Constants.DRAGON_SCALE, 1, 1000, new PointF(22 * Constants.DRAGON_SCALE, 0), new PointF(0, 0));
        this.appearAnimation = this.moveAnimation;
    }


    @Override
    public void draw(Canvas canvas) {
        if (isActive) {
            if (this.IsInScreen()) {
                RectF attack = getAttackRange();
                RectF sur = getSurroundingBox();
                canvas.drawRect(Constants.getRelativeXPosition(sur.left), sur.top, Constants.getRelativeXPosition(sur.right), sur.bottom, new Paint());
//                System.out.println(attack);;
//                System.out.println("current Position "+ currentPosition);
                if (attack != null) {
                    canvas.drawRect(Constants.getRelativeXPosition(attack.left), attack.top, Constants.getRelativeXPosition(attack.right), attack.bottom, new Paint());
                }

            }
        }
        super.draw(canvas);
    }

    @Override
    public RectF getAttackRange() {
        if (currentAnimation != attackAnimation) {
            return null;
        }
        int frameIndex = currentAnimation.getCurrentFrameIndex();
        if (frameIndex >= 1 && frameIndex <= 5) {
            float top = this.currentPosition.y;
            float bottom = this.currentPosition.y + 4*this.currentAnimation.getAbsoluteFrameHeight()/5;
            float left, right;
            float width = currentAnimation.getAbsoluteFrameWidth();
            if (currentAnimation.isFlip) {
                left = this.currentPosition.x + width /4;
            } else {
                left = this.currentPosition.x - currentAnimation.getAbsoluteOffsetTopLeftX();
            }
            right = left + 3 * currentAnimation.getAbsoluteFrameWidth() / 4;
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
            System.out.println("current state "+ currentState);
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
                    if (!IsPlayerInRange(playerSurroundingBox, attackDistance)) {
                        ChangeState(State.Move);
                    } else
                        ChangeState(State.Attack);
                    isMovingForward = playerSurroundingBox.centerX() > this.currentPosition.x;
                    break;
                case Move:
                    ChangeState(State.Move);
                    if (isAlive) {
                        if (IsPlayerInRange(playerSurroundingBox, attackDistance)) {
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
//                            System.out.println("is moving forward " + isMovingForward);

                            MoveToDestination(new PointF(x, y), Constants.PHANTOM_V);
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

    @Override
    public boolean IsInReach(PointF position) {
        if (position.x < rightLandMark.x && position.x > leftLandMark.x) {
            return true;
        }
        return false;
    }


}
