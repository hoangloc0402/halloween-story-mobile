package com.halloween.GameObjects.Enemies;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.halloween.Animation;
import com.halloween.Constants;
import com.halloween.GameObjects.Bullet;
import com.halloween.R;

public class Dragon extends Enemy {
    Bullet bullet;
    RectF offSet;


    public Dragon(PointF leftLandMark, PointF rightLandMark) {
        super(Constants.DRAGON_STARTING_HP, leftLandMark, rightLandMark, Constants.DRAGON_FOLLOW_DISTANCE, Constants.DRAGON_ATTACK_DISTANCE);

        LoadAnimation();

        currentState = previousState = State.Appear;

        currentAnimation = appearAnimation;

        this.surroundingBox = new RectF();

        this.currentPosition = new PointF(rightLandMark.x, rightLandMark.y);

        this.currentAnimation.play();

        this.isMovingForward = false;

        this.bullet = new Bullet(new PointF(0, 0));

        this.damage = Constants.DRAGON_DAMAGE;
        this.attack = Constants.DRAGON_ATTACK;
    }

    public void LoadAnimation() {
        this.moveAnimation = new Animation(R.drawable.dragon_move_188x176x6, 188 * Constants.DRAGON_SCALE, 176 * Constants.DRAGON_SCALE, 6, 100,
                new PointF(22 * Constants.DRAGON_SCALE, 60 * Constants.DRAGON_SCALE), new PointF(65 * Constants.DRAGON_SCALE, 20 * Constants.DRAGON_SCALE));
        this.attackAnimation = new Animation(R.drawable.dragon_attack_227x187x6, 227 * Constants.DRAGON_SCALE,
                187 * Constants.DRAGON_SCALE, 6, 100,
                new PointF(60 * Constants.DRAGON_SCALE, 50 * Constants.DRAGON_SCALE),
                new PointF(60 * Constants.DRAGON_SCALE, 20 * Constants.DRAGON_SCALE));
        this.diedAnimation = new Animation(R.drawable.dragon_died_175x176x5, 175 * Constants.DRAGON_SCALE,
                176 * Constants.DRAGON_SCALE, 5, 100, new PointF(22 * Constants.DRAGON_SCALE, 60 * Constants.DRAGON_SCALE),
                new PointF(50 * Constants.DRAGON_SCALE, 20 * Constants.DRAGON_SCALE));
        this.hurtAnimation = new Animation(R.drawable.dragon_hurt_175x176x1, 175 * Constants.DRAGON_SCALE,
                176 * Constants.DRAGON_SCALE, 1, 1000, new PointF(22 * Constants.DRAGON_SCALE, 60 * Constants.DRAGON_SCALE),
                new PointF(65 * Constants.DRAGON_SCALE, 15 * Constants.DRAGON_SCALE));
        this.ultimateAttackAnimation = new Animation(R.drawable.dragon_ultimate_280x185x12, 280 * Constants.DRAGON_SCALE,
                185 * Constants.DRAGON_SCALE, 12, 100,
                new PointF(90 * Constants.DRAGON_SCALE, 60 * Constants.DRAGON_SCALE),
                new PointF(80 * Constants.DRAGON_SCALE, 20 * Constants.DRAGON_SCALE));
        this.idleAnimation = new Animation(R.drawable.dragon_idle_188x176x6, 188 * Constants.DRAGON_SCALE,
                176 * Constants.DRAGON_SCALE, 6, 1000, new PointF(22 * Constants.DRAGON_SCALE, 0), new PointF(0, 0));
        this.appearAnimation = this.moveAnimation;
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
//                if (attack != null) {
//                    canvas.drawRect(Constants.getRelativeXPosition(attack.left), attack.top, Constants.getRelativeXPosition(attack.right), attack.bottom, new Paint());
//                }

            }
            bullet.draw(canvas);
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
            float top = this.currentPosition.y - currentAnimation.getAbsoluteOffsetTopLeftY()/2;
            float bottom = this.currentPosition.y +4 *this.currentAnimation.getAbsoluteFrameHeight()/5;
            float left, right;
            float width = currentAnimation.getAbsoluteFrameWidth();
            if (currentAnimation.isFlip) {
                left = this.currentPosition.x + currentAnimation.getAbsoluteFrameWidth()/2  - currentAnimation.getAbsoluteOffsetBottomRightX();
            } else {
                left = this.currentPosition.x - currentAnimation.getAbsoluteOffsetTopLeftX();
            }
            right = left + currentAnimation.getAbsoluteFrameWidth() / 2;
            this.attackRect.set(left, top, right, bottom);
            return this.attackRect;
        } else return null;
    }

    @Override
    public void update(RectF playerSurroundingBox) {
        super.update();
        if(isActive){
            if (currentHP > 70*Constants.DRAGON_STARTING_HP/100){
                UpdateStage1(playerSurroundingBox);
            }else
            if (currentHP >40 *Constants.DRAGON_STARTING_HP/100){
                UpdateStage2(playerSurroundingBox);
            }
            else{
                UpdateStage3(playerSurroundingBox);
            }
            this.currentAnimation.flip(isMovingForward);
            currentAnimation.update();
        }


    }

    public void UpdateStage1(RectF playerSurroundingBox){

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
                        MoveToDestination(rightLandMark, Constants.DRAGON_V);
                    } else {
                        MoveToDestination(leftLandMark, Constants.DRAGON_V);
                    }
                    if (currentPosition.x <= leftLandMark.x) {
                        isMovingForward = true;
                    } else if (currentPosition.x >= rightLandMark.x) {
                        isMovingForward = false;
                    }
                }
                break;
            case Idle:
                ChangeState(State.Idle);
                if(currentAnimation.isLastFrame()){
                    ChangeState(State.Move);
                }
                isMovingForward = playerSurroundingBox.centerX() > this.currentPosition.x;
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
    }

    public void UpdateStage2(RectF playerSurroundingBox){
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
                if (!IsPlayerInRange(playerSurroundingBox, Constants.DRAGON_ATTACK_DISTANCE_X, Constants.DRAGON_ATTACK_DISTANCE_Y)) {
                    ChangeState(State.Move);
                } else
                    ChangeState(State.Attack);
                isMovingForward = playerSurroundingBox.left > this.currentPosition.x;
                break;
            case Move:
                ChangeState(State.Move);
                if (isAlive) {
                    if (IsPlayerInRange(playerSurroundingBox, Constants.DRAGON_ATTACK_DISTANCE_X, Constants.DRAGON_ATTACK_DISTANCE_Y)){
                        ChangeState(State.Attack);
                    }else
                    if(IsPlayerInRange(playerSurroundingBox, followDistance) && IsInReach(new PointF(playerSurroundingBox.left, playerSurroundingBox.top))){
                        float x, y;
                        if (isMovingForward) {
                            x = playerSurroundingBox.centerX() - ( 5*playerSurroundingBox.width() / 4);
                        } else {
                            x = playerSurroundingBox.centerX() + 1*playerSurroundingBox.width() / 4;
                        }
                        isMovingForward = playerSurroundingBox.centerX() > currentPosition.x;
                        y = playerSurroundingBox.centerY() - currentAnimation.getAbsoluteFrameHeight() / 4;

                        MoveToDestination(new PointF(x, y), Constants.DRAGON_V);
                    }else{
                        if (isMovingForward) {
                            MoveToDestination(rightLandMark, Constants.DRAGON_V);
                        } else {
                            MoveToDestination(leftLandMark, Constants.DRAGON_V);
                        }
                        if (currentPosition.x <= leftLandMark.x) {
                            isMovingForward = true;
                        } else if (currentPosition.x >= rightLandMark.x) {
                            isMovingForward = false;
                        }
                    }

                }
                break;
            case Idle:
                ChangeState(State.Idle);
                if(currentAnimation.isLastFrame()){
                    ChangeState(State.Move);
                }
                isMovingForward = playerSurroundingBox.centerX() > this.currentPosition.x;
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
    }

    public void UpdateStage3(RectF playerSurroundingBox){
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
                if (!IsPlayerInRange(playerSurroundingBox, Constants.DRAGON_ATTACK_DISTANCE_X, Constants.DRAGON_ATTACK_DISTANCE_Y)) {
                    ChangeState(State.UltimateAttack);
                } else
                    ChangeState(State.Attack);
                isMovingForward = playerSurroundingBox.left > this.currentPosition.x;
                break;
            default:
                ChangeState(State.UltimateAttack);
                if(IsInReach(new PointF(playerSurroundingBox.left, playerSurroundingBox.top)) &&
                        IsPlayerInRange(playerSurroundingBox, Constants.DRAGON_ATTACK_DISTANCE_X, Constants.DRAGON_ATTACK_DISTANCE_Y)){
                    ChangeState(State.Attack);
                }
                else{
                    if (isMovingForward) {
                        MoveToDestination(rightLandMark, Constants.DRAGON_V);
                    } else {
                        MoveToDestination(leftLandMark, Constants.DRAGON_V);
                    }
                    if (currentPosition.x <= leftLandMark.x) {
                        isMovingForward = true;
                    } else if (currentPosition.x >= rightLandMark.x) {
                        isMovingForward = false;
                    }
                    offSet = currentAnimation.getSurroundingBox(currentPosition);
                    bullet.update(playerSurroundingBox, isMovingForward,
                            new PointF(currentAnimation.getSurroundingBox(currentPosition).centerX(), currentAnimation.getSurroundingBox(currentPosition).centerY()) );
                }
                break;
        }
        currentAnimation.update();
    }

    @Override
    public boolean IsInReach(PointF position) {
        if (position.x < rightLandMark.x && position.x > leftLandMark.x) {
            return true;
        }
        return false;
    }

    public boolean IsPlayerInRange(RectF playerSurroundingBox, float maxX, float maxY) {
        float dx4, dx1, dx2, dx3, dx5;
        float dy1 = playerSurroundingBox.top - getSurroundingBox().top;
        float dy2 = playerSurroundingBox.bottom - getSurroundingBox().bottom;
        float dy3 = playerSurroundingBox.centerY() - getSurroundingBox().centerY();
        float dy4 = playerSurroundingBox.bottom - getSurroundingBox().top;
        float dy5 = playerSurroundingBox.top - getSurroundingBox().bottom;
        float dy = Math.min(Math.abs(dy1), Math.abs(dy2));
        dy = Math.abs(Math.min(dy, Math.abs(dy3)));
        dy = Math.abs(Math.min(dy, Math.abs(dy4)));
        dy = Math.abs(Math.min(dy, Math.abs(dy5)));

        dx1 = playerSurroundingBox.left - getSurroundingBox().left;
        dx2 = playerSurroundingBox.right - getSurroundingBox().right;
        dx3 = playerSurroundingBox.left - getSurroundingBox().right;
        dx4 = playerSurroundingBox.right - getSurroundingBox().left;
        dx5 = playerSurroundingBox.centerX() - getSurroundingBox().centerX();
        float dx = Math.min(Math.abs(dx1), Math.abs(dx2));
        dx = Math.abs(Math.min(dx, Math.abs(dx3)));
        dx = Math.abs(Math.min(dx, Math.abs(dx4)));
        dx = Math.abs(Math.min(dx, Math.abs(dx5)));

        System.out.println(dx);
        System.out.println(dy);

        return dx < maxX && dy<maxY;
    }


}
