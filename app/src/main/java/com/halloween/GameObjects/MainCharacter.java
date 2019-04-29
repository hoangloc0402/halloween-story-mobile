package com.halloween.GameObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import com.halloween.Animation;
import com.halloween.Constants;
import com.halloween.R;

public class MainCharacter implements GameObject {
    private Animation currentAnimation;
    private Rect surroundingBox;
    private Animation idleAnimation, walkAnimation, jumpAnimation, dieAnimation;
    private Animation[] attackAnimation;
    private PointF position, velocity;
    public int current_score;
    public int currentHP, previousHP, attackPower;
    private boolean isJumping, isAttacking , isAlive, isActive, isInvincible;
    private boolean allowLeft, allowRight, hit;
//    KeyboardState previousKeyState;
//    KeyboardState currentKeyState;
    private double elapsedTime;
    private Color color;
    private int attackIndex = -1;
    private long jumpTime;

    public MainCharacter(){
        this.loadAnimation();
        this.currentAnimation = idleAnimation;
        this.surroundingBox = new Rect();
        this.position = new PointF(250,500);
        this.velocity = new PointF(0,0);
        this.currentAnimation.play();
        this.currentAnimation.flip(true);
        this.current_score = 0;
        this.currentHP = Constants.MAX_HEALTH_MAIN_CHARACTER;
        this.previousHP = this.currentHP;
        this.attackPower = Constants.MAIN_CHARACTER_ATTACK_POWER;
        this.isJumping = this.isActive = this.isAlive = true;
        this.isAttacking =  this.isInvincible =  false;
        hit = false;
        elapsedTime = 0;
    }

    public void loadAnimation(){
        this.idleAnimation = new Animation(R.drawable.main_character_idle_103x97x8, 103*3,97*3,8, 100);
        this.walkAnimation = new Animation(R.drawable.main_character_walk_103x97x4, 103*3,97*3,4, 100);
        this.jumpAnimation = new Animation(R.drawable.main_character_jump_1, 103*3,97*3,1, 300);
        this.dieAnimation = new Animation(R.drawable.main_character_die_12, 103*3,97*3,12, 100);
        this.attackAnimation = new Animation[4];
        this.attackAnimation[0] = new Animation(R.drawable.main_character_attack_6, 103*3,97*3,6, 75);
        this.attackAnimation[1] = new Animation(R.drawable.main_character_attack2_4, 103*3,97*3,4, 75);
        this.attackAnimation[2] = new Animation(R.drawable.main_character_attack4_4, 103*3,97*3,4, 75);
        this.attackAnimation[3] = new Animation(R.drawable.main_character_attack3_7, 103*3,97*3,7, 75);
    }

    @Override
    public void draw(Canvas canvas) {
        currentAnimation.draw(canvas, this.position);
    }

    @Override
    public void update() {
        this.updateMovement();
        this.updateAnimation();
        currentAnimation.update();
    }

    private void updateMovement(){
        if (this.isAlive){
            if (Constants.CURRENT_JOYSTICK_STATE == Constants.JOYSTICK_STATE.LEFT)
                this.allowLeft = true;
            else if (Constants.CURRENT_JOYSTICK_STATE == Constants.JOYSTICK_STATE.RIGHT)
                this.allowRight = true;

            if (!isJumping && Constants.JOYSTICK_JUMP_STATE) {
                this.velocity.y = -Constants.MAIN_CHARACTER_V_Y;
                this.isJumping = true;
                this.jumpTime = System.nanoTime();
            }
            if (isJumping){
                long elapsed_time = (System.nanoTime() - this.jumpTime) / 100000000;
//                this.velocity.y += Math.sqrt(2*Constants.GRAVITY*Constants.MAIN_CHARACTER_JUMP_HEIGHT) - Constants.GRAVITY*elapsed_time;
            }
            else this.velocity.y = 0;

//            isJumping = true;

//            Check va chạm
            if (this.allowLeft){
                this.velocity.x = - Constants.MAIN_CHARACTER_V_X;
                this.allowLeft = false;
            } else if (this.allowRight){
                this.velocity.x = Constants.MAIN_CHARACTER_V_X;
                this.allowRight = false;
            } else this.velocity.x = 0;

            this.position.x += this.velocity.x;
            this.position.y += this.velocity.y;

            if (this.position.y > 500)
                this.position.y = 500;
        }
        else {

        }
    }

    private void updateAnimation(){
//        if (this.currentHP < 0){
//            currentHP = 0;
//            this.isAlive = false;
//            this.currentAnimation = this.dieAnimation;
//            if (this.dieAnimation.isLastFrame())
//                this.isActive = false;
//        }
//        else if (this.isAttacking){
//
//        }
//        else if (!this.isJumping && this.velocity.x !=0) {
//            this.currentAnimation = this.walkAnimation;
//        }
//        else if (this.isJumping){
//            this.currentAnimation = this.jumpAnimation;
//        }
//        else this.currentAnimation = this.idleAnimation;

        if (this.velocity.x > 0){
            this.currentAnimation.flip(true);
        } else if (this.velocity.x <0){
            this.currentAnimation.flip(false);
        }
    }

    public Rect getSurroundingBox(){
        this.surroundingBox.left = (int)position.x;
        this.surroundingBox.top =  (int)position.y;
        this.surroundingBox.right = (int)position.x + currentAnimation.frameWidth;
        this.surroundingBox.bottom = (int)position.y + currentAnimation.frameHeight;
        return this.surroundingBox;
    }

    public PointF getCurrentPosition(){ return this.position;}

    public Boolean collide(Rect targetBox){
        return targetBox.intersect(this.getSurroundingBox());
    }
}
