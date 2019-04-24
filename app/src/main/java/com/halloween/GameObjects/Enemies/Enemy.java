package com.halloween.GameObjects.Enemies;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;

import com.halloween.Animation;
import com.halloween.GameObjects.GameObject;


public class Enemy implements GameObject {

    int HP;
    public Animation currentAnimation;
    protected Animation idleAnimation;
    protected Animation diedAnimation;
    protected Animation attackAnimation;
    protected Animation defenseAnimation;
    protected Animation hurtAnimation;
    protected Animation ultimateAttackAnimation;


    Point position, leftLandMark, rightLandMark;
    Rect surroundingBox;

    boolean isAlive, isMovingForward;

    public Enemy(){
        isAlive = true;
    }

    public enum State { Appear, Idle, Move , Attack, Defense , UltimateAttack, Hurt, Died} //Các state có thể có của Enemy
    protected State currentState, previousState;

    @Override
    public void draw(Canvas canvas){}

    @Override
    public void update(){

    }

    public Rect getSurroundingBox(){
        this.surroundingBox.left = position.x;
        this.surroundingBox.top =  position.y;
        this.surroundingBox.right = position.x + currentAnimation.frameWidth;
        this.surroundingBox.bottom = position.y + currentAnimation.frameHeight;
        return this.surroundingBox;
    }

    public void ChangeState(State state){
        previousState = currentState;
        currentState = state;

        if (currentState != previousState){
            switch (currentState){
                case Idle:
                    currentAnimation = idleAnimation;
                    break;
                case Died:
                    currentAnimation = diedAnimation;
                    break;
                case Attack:
                    currentAnimation = attackAnimation;
                    break;
                case Defense:
                    currentAnimation = defenseAnimation;
                    break;
                case Hurt:
                    currentAnimation = hurtAnimation;
                    break;
                case UltimateAttack:
                    currentAnimation = ultimateAttackAnimation;
                    break;
                default:
                    break;

            }
        }
    }
}