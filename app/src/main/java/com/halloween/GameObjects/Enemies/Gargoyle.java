package com.halloween.GameObjects.Enemies;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;

import com.halloween.Animation;
import com.halloween.Constants;
import com.halloween.R;

public class Gargoyle extends Enemy {

    public Gargoyle(PointF leftLandMark, PointF rightLandMark) {
        super(Constants.GARGOYLE_STARTING_HP, leftLandMark, rightLandMark, Constants.GARGOYLE_FOLLOW_DISTANCE, Constants.GARGOYLE_ATTACK_DISTANCE);

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
    }

    public void LoadAnimation() {
        this.moveAnimation = new Animation(R.drawable.gargoyle_move_103x118x4_10x20, 103 * Constants.GARGOYLE_SCALE, 118 * Constants.GARGOYLE_SCALE, 4, 100,
                new PointF(0, 0), new PointF(0, 0));
        this.diedAnimation = new Animation(R.drawable.gargoyle_died_103x118x6_0x0, 103 * Constants.GARGOYLE_SCALE,
                118 * Constants.GARGOYLE_SCALE, 6, 100, new PointF(0, 0), new PointF(0, 0));
        this.hurtAnimation = new Animation(R.drawable.gargoyly_hurt_103x118x1_0x0, 103 * Constants.GARGOYLE_SCALE,
                118 * Constants.GARGOYLE_SCALE, 1, 100, new PointF(0, 0), new PointF(0, 0));
    }

    @Override
    public void draw(Canvas canvas) {
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
//                        if(IsInScreen() && IsInReach(new PointF(playerSurroundingBox.left, playerSurroundingBox.top))){
//                            if (IsPlayerInRange(playerSurroundingBox, followDistance)) {
//                                isMovingForward = playerSurroundingBox.centerX() > getSurroundingBox().centerX();
//                            }
//                            MoveToDestination(new PointF(playerSurroundingBox.left + 200 , playerSurroundingBox.top), Constants.GARGOYLE_V);
//                        }
//                        else {
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
