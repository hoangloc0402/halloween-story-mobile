package com.halloween.GameObjects.Enemies;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.halloween.Animation;
import com.halloween.Constants;
import com.halloween.R;

public class Gargoyle extends Enemy {

    public Gargoyle(PointF leftLandMark, PointF rightLandMark) {
        super(Constants.GARGOYLE_STARTING_HP, leftLandMark, rightLandMark);

        LoadAnimation();

        this.v_x = Constants.GARGOYLE_V;
        this.v_y = this.v_x;

        currentState = previousState = State.Move;

        currentAnimation = moveAnimation;

        this.surroundingBox = new RectF();

//        this.currentPosition = new PointF(700, 0.8f * Constants.SCREEN_HEIGHT - currentAnimation.frameHeight);
        this.currentPosition = new PointF(rightLandMark.x, rightLandMark.y);


        this.currentAnimation.play();

        this.isMovingForward = false;

        this.damage = Constants.GARGOYLE_DAMAGE;
        this.attack = Constants.GARGOYLE_ATTACK;
    }

    public void LoadAnimation() {
        this.moveAnimation = new Animation(R.drawable.gargoyle_move_103x118x4_10x20, 103 * Constants.GARGOYLE_SCALE, 118 * Constants.GARGOYLE_SCALE, 4, 100,
                new PointF(15 * Constants.GARGOYLE_SCALE, 47 * Constants.GARGOYLE_SCALE), new PointF(40 * Constants.GARGOYLE_SCALE, 0 * Constants.GARGOYLE_SCALE));
        this.diedAnimation = new Animation(R.drawable.gargoyle_died_103x118x6_0x0, 103 * Constants.GARGOYLE_SCALE,
                118 * Constants.GARGOYLE_SCALE, 6, 100,
                new PointF(10 * Constants.GARGOYLE_SCALE, 60 * Constants.GARGOYLE_SCALE), new PointF(40 * Constants.GARGOYLE_SCALE, 0 * Constants.GARGOYLE_SCALE));
        this.hurtAnimation = new Animation(R.drawable.gargoyly_hurt_103x118x1_0x0, 103 * Constants.GARGOYLE_SCALE,
                118 * Constants.GARGOYLE_SCALE, 1, 100,
                new PointF(10 * Constants.GARGOYLE_SCALE, 60 * Constants.GARGOYLE_SCALE), new PointF(40 * Constants.GARGOYLE_SCALE, 0 * Constants.GARGOYLE_SCALE));
    }

    @Override
    public void reset(){
        super.reset();
        this.currentAnimation = moveAnimation;
        this.isMovingForward = false;
        this.currentState = State.Move;
        this.currentAnimation.play();
        this.currentHP = Constants.GARGOYLE_STARTING_HP;
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
                case Move:
                    ChangeState(State.Move);
                    if (isAlive) {
                        if (isMovingForward) {
                            MoveToDestination(rightLandMark, Constants.GARGOYLE_V);
                        } else {
                            MoveToDestination(leftLandMark, Constants.GARGOYLE_V);
                        }
                        if (currentPosition.x <= leftLandMark.x) {
                            isMovingForward = true;
                        } else if (currentPosition.x >= rightLandMark.x) {
                            isMovingForward = false;
                        }
                    }
                    break;
                default:
                    break;
            }
            this.currentAnimation.flip(isMovingForward);
            currentAnimation.update();
        }
    }


}
