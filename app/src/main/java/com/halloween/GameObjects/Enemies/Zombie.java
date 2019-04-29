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
        this.position = new Point(700,100);
        this.currentAnimation.play();
        this.isMovingForward = false;
        currentState = previousState = State.Move;
    }

    public void loadAnimation() {
        this.idleAnimation = new Animation(R.drawable.attack_83x97x4, 83 * Constants.ZOMBIE_SCALE, 97 * Constants.ZOMBIE_SCALE, 4, 100);
        this.defenseAnimation = new Animation(R.drawable.attack_83x97x4, 83 * Constants.ZOMBIE_SCALE, 97 * Constants.ZOMBIE_SCALE, 4, 100);
        this.attackAnimation = new Animation(R.drawable.attack_83x97x4, 83 * Constants.ZOMBIE_SCALE, 97 * Constants.ZOMBIE_SCALE, 4, 300);
        this.diedAnimation = new Animation(R.drawable.attack_83x97x4, 83 * Constants.ZOMBIE_SCALE, 97 * Constants.ZOMBIE_SCALE, 4, 100);
        this.hurtAnimation = new Animation(R.drawable.attack_83x97x4, 83 * Constants.ZOMBIE_SCALE, 97 * Constants.ZOMBIE_SCALE, 4, 100);
        this.ultimateAttackAnimation = new Animation(R.drawable.attack_83x97x4, 83 * Constants.ZOMBIE_SCALE, 97 * Constants.ZOMBIE_SCALE, 4, 100);
    }

    public void UpdateMovement(){
        if(currentState == State.Move){
            ChangeState(State.Move);

            if(isMovingForward){
                position.x += Constants.ZOMBIE_V_X;
            }
            else{
                position.x -= Constants.ZOMBIE_V_X;
            }

            if (position.x <= leftLandMark.x){
                isMovingForward = true;
            }
            else if(position.x >= rightLandMark.x){
                isMovingForward = false;
            }
            this.currentAnimation.flip(isMovingForward);
        }
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.currentAnimation.draw(canvas, new PointF(this.position.x, this.position.y));
    }

    @Override
    public void update() {
        super.update();
        UpdateMovement();
        this.currentAnimation.update();
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
