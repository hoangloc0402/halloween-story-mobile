package com.halloween.GameObjects.Enemies;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;

import com.halloween.Animation;
import com.halloween.Constants;
import com.halloween.R;

public class Zombie extends Enemy {

    int currentHP;


    public Zombie(Point leftLandMark, Point rightLandMark){
        super();
        loadAnimation();
        currentAnimation = idleAnimation;
        this.leftLandMark = leftLandMark;
        this.rightLandMark = rightLandMark;
//        currentHP = Constants.ZOMBIE_STARTING_HP;
//        this.currentAnimation = idleAnimation;
        this.surroundingBox = new Rect();
        this.currentPosition = new Point(700,100);
        this.currentAnimation.play();
        this.isMovingForward = false;
        currentState = previousState = State.Move;
    }

    public void loadAnimation() {
        this.idleAnimation = new Animation(R.drawable.attack_83x97x4, 83 * Constants.ZOMBIE_SCALE, 97 * Constants.ZOMBIE_SCALE, 4, 100);
//        this.defenseAnimation = new Animation(R.drawable.attack_83x97x4, 83 * Constants.ZOMBIE_SCALE, 97 * Constants.ZOMBIE_SCALE, 4, 100);
//        this.attackAnimation = new Animation(R.drawable.attack_83x97x4, 83 * Constants.ZOMBIE_SCALE, 97 * Constants.ZOMBIE_SCALE, 4, 300);
//        this.diedAnimation = new Animation(R.drawable.attack_83x97x4, 83 * Constants.ZOMBIE_SCALE, 97 * Constants.ZOMBIE_SCALE, 4, 100);
//        this.hurtAnimation = new Animation(R.drawable.attack_83x97x4, 83 * Constants.ZOMBIE_SCALE, 97 * Constants.ZOMBIE_SCALE, 4, 100);
//        this.ultimateAttackAnimation = new Animation(R.drawable.attack_83x97x4, 83 * Constants.ZOMBIE_SCALE, 97 * Constants.ZOMBIE_SCALE, 4, 100);
    }

    public void UpdateMovement(){
        switch (currentState){
            case Move:
                ChangeState(State.Move);
                if(isMovingForward){
                    currentPosition.x =  currentPosition.x + Constants.ZOMBIE_V_X;
                }
                else{
                    currentPosition.x = currentPosition.x - Constants.ZOMBIE_V_X;
                }
                if (currentPosition.x <= leftLandMark.x - Constants.BACKGROUND_X_AXIS){
                    isMovingForward = true;
                }
                else if(currentPosition.x >= rightLandMark.x - Constants.BACKGROUND_X_AXIS){
                    isMovingForward = false;
                }
                this.currentAnimation.flip(isMovingForward);
                break;
            case Idle:
                break;
            case Died:
                break;
            default:
                break;
        }
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.isInScreen()){
            this.currentAnimation.draw(canvas, new PointF(this.currentPosition.x - Constants.BACKGROUND_X_AXIS, this.currentPosition.y));
        }

    }

    @Override
    public void update() {
        super.update();
        System.out.println("Current Position = " + (currentPosition.x));
        if(isInScreen()){
            UpdateMovement();
            this.currentAnimation.update();
        }
    }

    public void hit(int attack){
        if (isAlive){
            currentHP -= attack;
            if(currentHP<=0){
                isAlive = false;
            }
            update();
        }
    }


}
