package com.halloween.GameObjects.Enemies;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.halloween.Animation;
import com.halloween.Constants;
import com.halloween.R;

public class Phantom extends Enemy {

    public Phantom(PointF leftLandMark, PointF rightLandMark) {
        super(Constants.PHANTOM_STARTING_HP, leftLandMark, rightLandMark, Constants.PHANTOM_FOLLOW_DISTANCE, Constants.PHANTOM_ATTACK_DISTANCE);

        LoadAnimation();

        this.v_x = Constants.PHANTOM_V;
        this.v_y = this.v_x;

        currentState = previousState = State.Appear;

        currentAnimation = appearAnimation;

        this.surroundingBox = new RectF();

//        this.currentPosition = new PointF(700, 0.8f * Constants.SCREEN_HEIGHT - currentAnimation.frameHeight);
        this.currentPosition = new PointF(rightLandMark.x, rightLandMark.y);


        this.currentAnimation.play();

        this.isMovingForward = false;

        this.damage = Constants.PHANTOM_DAMAGE;
        this.attack  = Constants.PHANTOM_ATTACK;
    }

    public void LoadAnimation() {
        this.moveAnimation = new Animation(R.drawable.phantom_move_114x91x6, 114 * Constants.PHANTOM_SCALE, 91 * Constants.PHANTOM_SCALE, 6, 100,
                new PointF(15 *Constants.PHANTOM_SCALE, 47*Constants.PHANTOM_SCALE), new PointF(40 *Constants.PHANTOM_SCALE, 0*Constants.PHANTOM_SCALE));
        this.diedAnimation = new Animation(R.drawable.phantom_died_114x91x6, 114 * Constants.PHANTOM_SCALE,
                91 * Constants.PHANTOM_SCALE, 6, 100,
                new PointF(10 *Constants.PHANTOM_SCALE, 60*Constants.PHANTOM_SCALE), new PointF(40 *Constants.PHANTOM_SCALE, 0*Constants.PHANTOM_SCALE));
        this.hurtAnimation = new Animation(R.drawable.phantom_hurt_114x91x1, 114 * Constants.PHANTOM_SCALE,
                91 * Constants.PHANTOM_SCALE, 1, 100,
                new PointF(10 *Constants.PHANTOM_SCALE, 60*Constants.PHANTOM_SCALE), new PointF(40 *Constants.PHANTOM_SCALE, 0*Constants.PHANTOM_SCALE));
        this.attackAnimation = new Animation(R.drawable.phantom_attack_114x91x12, 114 * Constants.PHANTOM_SCALE,
                91 * Constants.PHANTOM_SCALE, 12, 100,
                new PointF(10 *Constants.PHANTOM_SCALE, 60*Constants.PHANTOM_SCALE), new PointF(40 *Constants.PHANTOM_SCALE, 0*Constants.PHANTOM_SCALE));
        this.idleAnimation = new Animation(R.drawable.phantom_idle_114x91x4, 114 * Constants.PHANTOM_SCALE,
                91 * Constants.PHANTOM_SCALE, 4, 100,
                new PointF(10 *Constants.PHANTOM_SCALE, 60*Constants.PHANTOM_SCALE), new PointF(40 *Constants.PHANTOM_SCALE, 0*Constants.PHANTOM_SCALE));
        this.appearAnimation = new Animation(R.drawable.phantom_appear_114x91x6, 114 * Constants.PHANTOM_SCALE,
                91 * Constants.PHANTOM_SCALE, 6, 5000,
                new PointF(10 *Constants.PHANTOM_SCALE, 60*Constants.PHANTOM_SCALE), new PointF(40 *Constants.PHANTOM_SCALE, 0*Constants.PHANTOM_SCALE));
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
                if(attack !=null){
                    canvas.drawRect(Constants.getRelativeXPosition(attack.left), attack.top, Constants.getRelativeXPosition(attack.right), attack.bottom, new Paint());
                }

            }
        }
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
                case Idle:
                    break;
                case Appear:
                    if(this.currentAnimation.isLastFrame()){
                        ChangeState(State.Move);
                    }else{
                        ChangeState(State.Appear);
                    }
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
