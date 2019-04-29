package com.halloween.GameObjects.Enemies;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;

import com.halloween.Animation;
import com.halloween.Constants;
import com.halloween.R;

public class Zombie extends Enemy {



    public Zombie(PointF leftLandMark, PointF rightLandMark){
        super(Constants.ZOMBIE_STARTING_HP, leftLandMark, rightLandMark);

        LoadAnimation();

        currentState = previousState = State.Move;

        currentAnimation = moveAnimation;

        this.surroundingBox = new RectF();

        this.currentPosition = new PointF(700,100);

        this.currentAnimation.play();

        this.isMovingForward = false;
    }

    public void LoadAnimation() {
        this.moveAnimation = new Animation(R.drawable.zombie_move_83x97x4, 83 * Constants.ZOMBIE_SCALE, 97 * Constants.ZOMBIE_SCALE, 4, 100,
                new PointF(0,0), new PointF(0,0));
        this.attackAnimation = new Animation(R.drawable.attack_83x97x4, 83 * Constants.ZOMBIE_SCALE,
                97 * Constants.ZOMBIE_SCALE, 4, 100, new PointF(0,0), new PointF(0,0));
        this.diedAnimation = new Animation(R.drawable.zombie_die_83x97x11, 83 * Constants.ZOMBIE_SCALE,
                97 * Constants.ZOMBIE_SCALE, 11, 100, new PointF(0,0), new PointF(0,0));
        this.hurtAnimation = new Animation(R.drawable.attack_83x97x4, 83 * Constants.ZOMBIE_SCALE,
                97 * Constants.ZOMBIE_SCALE, 4, 100, new PointF(0,0), new PointF(0,0));
    }

    public void UpdateMovement(){
        switch (currentState){
            case Move:
                ChangeState(State.Move);
                currentHP -= 0.1f;
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
            default:
                break;
        }
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(isActive){
            if (this.isInScreen()){
                this.currentAnimation.draw(canvas, new PointF(this.currentPosition.x - Constants.BACKGROUND_X_AXIS, this.currentPosition.y));
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if(isActive){
            switch (currentState){
                case Died:
                    ChangeState(State.Died);
                    isAlive = false;
                    break;
                case Hurt:
                    ChangeState(State.Hurt);
                    isAlive = currentHP > 0;
                    break;
                case Attack:
                    ChangeState(State.Attack);
                    break;
                case Move:
                    ChangeState(State.Move);
                    if(isAlive && isInScreen()){
                        currentHP -= 0.000001f;
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
                    }
                    break;
                default:
                    break;
            }
            isActive = isAlive || !this.currentAnimation.isLastFrame();
            this.currentAnimation.flip(isMovingForward);
            currentAnimation.update();
        }

    }

    public void hit(int attack){
        if (isAlive){
            currentHP -= attack;
            isAlive = currentHP > 0;
            ChangeState(State.Hurt);
            update();
        }
    }


}
