//package com.halloween.GameObjects.Enemies;
//
//import android.graphics.Canvas;
//import android.graphics.Point;
//import android.graphics.Rect;
//
//import com.halloween.Animation;
//import com.halloween.Constants;
//import com.halloween.R;
//
//public class Zombie extends Enemy {
//
//    int currentHP;
//
//
//    public Zombie(Point leftLandMark, Point rightLandMark){
//        super();
//        this.leftLandMark = leftLandMark;
//        this.rightLandMark = rightLandMark;
////        currentHP = Constants.ZOMBIE_STARTING_HP;
//        this.idleAnimation = new Animation(R.drawable.attack_83x97x4, 830,970,4, 100);
//        this.currentAnimation = idleAnimation;
//        this.surroundingBox = new Rect();
//        this.position = new Point(50,50);
//        this.currentAnimation.play();
//        currentState = previousState = State.Move;
//    }
//
//    public void UpdateMovement(){
//        if(currentState == State.Move){
//            ChangeState(State.Move);
//
//            if(isMovingForward){
//                position.x += Constants.ZOMBIE_V_X;
//            }
//            else{
//                position.x -= Constants.ZOMBIE_V_X;
//            }
//
//            if (position.x <= leftLandMark.x){
//                isMovingForward = true;
//            }
//            else if(position.x >= rightLandMark.x){
//                isMovingForward = false;
//            }
//        }
//    }
//
//
//    @Override
//    public void draw(Canvas canvas) {
//        super.draw(canvas);
//        currentAnimation.draw(canvas, this.position);
//    }
//
//    @Override
//    public void update() {
//        super.update();
//        UpdateMovement();
//
//
//    }
//
//    public void hit(int attack){
//        if (isAlive){
//            currentHP -= attack;
//            if(currentHP<=0){
//                isAlive = false;
//            }
//            update();
//        }
//    }
//
//
//}
