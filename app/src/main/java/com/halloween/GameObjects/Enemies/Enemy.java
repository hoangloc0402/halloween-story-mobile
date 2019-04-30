package com.halloween.GameObjects.Enemies;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;

import com.halloween.Animation;
import com.halloween.Constants;
import com.halloween.GameObjects.GameObject;
import com.halloween.GameObjects.MainCharacter;


public class Enemy implements GameObject {

    float currentHP;
    public Animation currentAnimation;
    Animation diedAnimation;
    Animation moveAnimation;
    Animation attackAnimation;
    Animation defenseAnimation;
    Animation hurtAnimation;
    Animation ultimateAttackAnimation;

    boolean isAlive, isActive;

    PointF currentPosition;
    PointF leftLandMark;
    PointF rightLandMark;
    RectF surroundingBox;

    float followDistance, attackDistance;

    public Enemy(float currentHP, PointF leftLandMark, PointF rightLandMark, float followDistance, float attackDistance) {
        this.currentHP = currentHP;
        this.leftLandMark = leftLandMark;
        this.rightLandMark = rightLandMark;
        this.followDistance = followDistance;
        this.attackDistance = attackDistance;
        isAlive = true;
        isActive = true;
    }

    boolean isMovingForward;

    //Check if the object is in the playing screen
    public boolean IsInScreen(){
        return currentPosition.x + currentAnimation.frameWidth >= Constants.BACKGROUND_X_AXIS && currentPosition.x <= Constants.BACKGROUND_X_AXIS + Constants.SCREEN_WIDTH;
    }

    public boolean IsPlayerInRange(RectF playerSurroundingBox, float maxDistance){
        float dy = playerSurroundingBox.centerY() - getSurroundingBox().centerY();
        float dx = playerSurroundingBox.centerX() - getSurroundingBox().centerX();
        float d =  dx*dx + dy*dy;
//        float d =  dx*dx;
//        System.out.println("Distance = " + playerSurroundingBox.centerX() + " " + surroundingBox.centerX());
        if (d <maxDistance)
            return true;
        return false;
    }

    public PointF getCurrentPosition() {
        return currentPosition;
    }

//    public boolean IsAlive(){
//        return currentHP > 0;
//    }

    public enum State { Move , Attack, Defense , UltimateAttack, Hurt, Died} //Các state có thể có của Enemy
    protected State currentState, previousState;

    @Override
    public void draw(Canvas canvas){}

    @Override
    public void update(){

    }

    public void update(RectF playerSurroundingBox){
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
                case Attack:
                    if (!IsPlayerInRange(playerSurroundingBox, attackDistance)) {
                        ChangeState(State.Move);
                    } else
                        ChangeState(State.Attack);
                    isMovingForward = playerSurroundingBox.centerX() > getSurroundingBox().centerX();
                    break;
                case Move:
                    ChangeState(State.Move);
                    if (isAlive && IsInScreen()) {
                        currentHP -= 0.01f;
                        if (isMovingForward) {
                            currentPosition.x = currentPosition.x + Constants.ZOMBIE_V_X;
                        } else {
                            currentPosition.x = currentPosition.x - Constants.ZOMBIE_V_X;
                        }
                        if (currentPosition.x <= leftLandMark.x - Constants.BACKGROUND_X_AXIS) {
                            isMovingForward = true;
                        } else if (currentPosition.x >= rightLandMark.x - Constants.BACKGROUND_X_AXIS) {
                            isMovingForward = false;
                        }
                    }
                    if (IsPlayerInRange(playerSurroundingBox, followDistance)) {
                        if (IsPlayerInRange(playerSurroundingBox, attackDistance))
                            ChangeState(State.Attack);
                        isMovingForward = playerSurroundingBox.centerX() > getSurroundingBox().centerX();
                    }
                    break;
                default:
                    break;
            }
            this.currentAnimation.flip(isMovingForward);
            currentAnimation.update();
        }
    }

    public RectF getSurroundingBox(){
        this.surroundingBox.left = currentPosition.x;
        this.surroundingBox.top =  currentPosition.y;
        this.surroundingBox.right = currentPosition.x + currentAnimation.frameWidth;
        this.surroundingBox.bottom = currentPosition.y + currentAnimation.frameHeight;
        return this.surroundingBox;
    }

    public void ChangeState(State state){
        previousState = currentState;
        currentState = state;

        if (currentState != previousState){
            switch (currentState){
                case Move:
                    currentAnimation = moveAnimation;
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

    public void Hurt(int attack) {
        if (isAlive) {
            currentHP -= attack;
            isAlive = currentHP > 0;
            if (isAlive) {
                ChangeState(State.Hurt);
            } else {
                ChangeState(State.Died);
            }
        }
    }
}