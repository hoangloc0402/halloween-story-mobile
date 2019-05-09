package com.halloween.GameObjects.Enemies;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.halloween.Animation;
import com.halloween.Constants;
import com.halloween.R;

public class Zombie extends Enemy {


    public Zombie(PointF leftLandMark, PointF rightLandMark) {
        super(Constants.ZOMBIE_STARTING_HP, leftLandMark, rightLandMark, Constants.ZOMBIE_FOLLOW_DISTANCE, Constants.ZOMBIE_ATTACK_DISTANCE);

        LoadAnimation();

        this.v_x = Constants.ZOMBIE_V;
        this.v_y = 0;

        currentState = previousState = State.Move;

        currentAnimation = moveAnimation;

        this.surroundingBox = new RectF();

        this.currentPosition = new PointF(rightLandMark.x, rightLandMark.y);

        this.currentAnimation.play();

        this.isMovingForward = false;

        this.damage = Constants.ZOMBIE_DAMAGE;
        this.attack = Constants.ZOMBIE_ATTACK;
    }

    public void LoadAnimation() {
        this.moveAnimation = new Animation(R.drawable.zombie_move_83x97x4, 83 * Constants.ZOMBIE_SCALE, 97 * Constants.ZOMBIE_SCALE, 4, 100,
                new PointF(22 *Constants.ZOMBIE_SCALE, 0), new PointF(0, 0));
        this.attackAnimation = new Animation(R.drawable.attack_83x97x4, 83 * Constants.ZOMBIE_SCALE,
                97 * Constants.ZOMBIE_SCALE, 4, 100, new PointF(22 *Constants.ZOMBIE_SCALE, 0), new PointF(0, 0));
        this.diedAnimation = new Animation(R.drawable.zombie_die_83x97x11, 83 * Constants.ZOMBIE_SCALE,
                97 * Constants.ZOMBIE_SCALE, 11, 100, new PointF(22 *Constants.ZOMBIE_SCALE, 0), new PointF(0, 0));
        this.hurtAnimation = new Animation(R.drawable.zombie_hurt_83x97x1, 83 * Constants.ZOMBIE_SCALE,
                97 * Constants.ZOMBIE_SCALE, 1, 1000, new PointF(22 *Constants.ZOMBIE_SCALE, 0), new PointF(0, 0));
    }


    @Override
    public void draw(Canvas canvas) {
        if (isActive) {
            if (this.IsInScreen()) {
                RectF attack = getAttackRange();
//                RectF sur = getSurroundingBox();
//                canvas.drawRect(Constants.getRelativeXPosition(sur.left), sur.top, Constants.getRelativeXPosition(sur.right), sur.bottom, new Paint());
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
    public RectF getAttackRange() {
        if (currentAnimation != attackAnimation){
            return null;
        }
        int frameIndex = currentAnimation.getCurrentFrameIndex();
        if (frameIndex >=2 && frameIndex <=3){
            float top = this.currentPosition.y;
            float bottom = this.currentPosition.y + this.currentAnimation.getAbsoluteFrameHeight();
            float left, right;
            float width = currentAnimation.getAbsoluteFrameWidth();
            if (currentAnimation.isFlip) {
                left = this.currentPosition.x + width/4;
            } else {
                left = this.currentPosition.x -  currentAnimation.getAbsoluteOffsetTopLeftX();
            }
            right = left + 3*currentAnimation.getAbsoluteFrameWidth()/4;
            this.attackRect.set(left, top, right , bottom);
            return this.attackRect;
        }
        else return  null;
    }

    @Override
    public void update(RectF playerSurroundingBox) {
        super.update();
        if (isActive) {
            if (currentHP <= 0) {
                isAlive = false;
                ChangeState(State.Died);
            }
//            System.out.println("current state zombie " + currentState);
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
                    isMovingForward = playerSurroundingBox.centerX() > getSurroundingBox().centerX();
                    break;
                case Move:
                    ChangeState(State.Move);
                    if (isAlive) {
                        if (IsPlayerInRange(playerSurroundingBox, attackDistance)) {
                            ChangeState(State.Attack);
                        } else {
                            if (currentPosition.x <= leftLandMark.x) {
                                isMovingForward = true;
                            } else if (currentPosition.x >= rightLandMark.x) {
                                isMovingForward = false;
                            }else if (IsPlayerInRange(playerSurroundingBox, followDistance) && IsInReach(new PointF(playerSurroundingBox.left, playerSurroundingBox.top))) {
                                    isMovingForward = playerSurroundingBox.left > getSurroundingBox().left;
//                                    System.out.println("isMovingForward "+ isMovingForward);
                            }
                            if (isMovingForward) {
                                MoveToDestination(rightLandMark, Constants.ZOMBIE_V);
                            } else {
                                MoveToDestination(leftLandMark, Constants.ZOMBIE_V);
                            }


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

    @Override
    public boolean IsInReach(PointF position){
        if (position.x < rightLandMark.x && position.x > leftLandMark.x){
            return true;
        }
        return false;
    }



}
