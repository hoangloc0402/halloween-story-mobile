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
import java.util.Random;

public class MainCharacter{
    private Animation currentAnimation;
    private Rect surroundingBox;
    private Animation idleAnimation, walkAnimation, jumpAnimation, dieAnimation;
    private Animation[] attackAnimation;
    private PointF position, velocity;
    public int current_score;
    public int currentHP, previousHP, attackPower;
    private boolean isJumping, isAttacking , isAlive, isActive, isInvincible;
    private boolean isFlip;
    private boolean allowLeft, allowRight, hit;
//    KeyboardState previousKeyState;
//    KeyboardState currentKeyState;
    private double elapsedTime;
    private Color color;
    private int attackIndex = -1;
    private Random rand = new Random();
    private long jumpTime;

    public MainCharacter(){
        this.loadAnimation();
        this.currentAnimation = idleAnimation;
        this.surroundingBox = new Rect();
        this.position = new PointF(0,0.8f*Constants.SCREEN_HEIGHT - currentAnimation.frameHeight);
        this.velocity = new PointF(0,0);
        this.currentAnimation.flip(true);
        this.current_score = 0;
        this.currentHP = Constants.MAX_HEALTH_MAIN_CHARACTER;
        this.previousHP = this.currentHP;
        this.attackPower = Constants.MAIN_CHARACTER_ATTACK_POWER;
        this.isActive = this.isAlive = true;
        this.isJumping = this.isAttacking =  this.isInvincible =  false;
        this.isFlip = true;
        hit = false;
        elapsedTime = 0;
    }

    public void loadAnimation(){
        int SCALE = 3;
        this.idleAnimation = new Animation(R.drawable.main_character_idle_103x97x8, 103*SCALE,97*SCALE,8, 100, new PointF(SCALE*60, SCALE*26), new PointF(0,0));
        this.walkAnimation = new Animation(R.drawable.main_character_walk_103x97x4, 103*SCALE,97*SCALE,4, 100, new PointF(SCALE*60, SCALE*22), new PointF(0,0));
        this.jumpAnimation = new Animation(R.drawable.main_character_jump_1, 103*SCALE,97*SCALE,1, 300, new PointF(SCALE*60, SCALE*26), new PointF(0,0));
        this.dieAnimation = new Animation(R.drawable.main_character_die_12, 103*SCALE,97*SCALE,12, 100, new PointF(SCALE*47, SCALE*26), new PointF(0,0));
        this.attackAnimation = new Animation[4];
        this.attackAnimation[0] = new Animation(R.drawable.main_character_attack_6, 103*SCALE,97*SCALE,6, 75, new PointF(SCALE*60, SCALE*26), new PointF(0,0));
        this.attackAnimation[1] = new Animation(R.drawable.main_character_attack2_4, 120*SCALE,73*SCALE,4, 75, new PointF(SCALE*60, SCALE*0), new PointF(SCALE*17,SCALE*2));
        this.attackAnimation[2] = new Animation(R.drawable.main_character_attack4_4, 131*SCALE,85*SCALE,4, 25, new PointF(SCALE*54, SCALE*13), new PointF(SCALE*35,0));
        this.attackAnimation[3] = new Animation(R.drawable.main_character_attack3_7, 180*SCALE,102*SCALE,7, 75, new PointF(SCALE*84, SCALE*14), new PointF(SCALE*56,SCALE*13));
    }


    public void draw(Canvas canvas) {
        this.currentAnimation.draw(canvas, new PointF(this.position.x - Constants.BACKGROUND_X_AXIS, this.position.y));
//        System.out.println(this.position);
    }


    public void update() {
        this.updateMovement();
        this.updateAnimation();
    }


    private void updateMovement(){
        if (this.isAlive){
            if (Constants.CURRENT_JOYSTICK_STATE == Constants.JOYSTICK_STATE.LEFT)
                this.allowLeft = true;
            else if (Constants.CURRENT_JOYSTICK_STATE == Constants.JOYSTICK_STATE.RIGHT)
                this.allowRight = true;

            if(Constants.JOYSTICK_ATK_STATE)
                this.isAttacking = true;
            else this.isAttacking = false;

            if (!isJumping && Constants.JOYSTICK_JUMP_STATE) {
                this.velocity.y = Constants.MAIN_CHARACTER_V_Y;
                this.isJumping = true;
                this.jumpTime = System.nanoTime();
            }
            if (isJumping){
                long elapsed_time = (System.nanoTime() - this.jumpTime) / 100000000;
                this.velocity.y =  (Constants.GRAVITY*elapsed_time + Constants.MAIN_CHARACTER_V_Y);
//                if (this.velocity.y < 3 && this.velocity.y > 0){
//                    this.jumpTime = System.nanoTime();
//                    this.velocity.y = 0;
//                }

                System.out.println(this.velocity.y);
            }
            else
            this.velocity.y = 0;

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

            if (this.position.y > 600){
                this.position.y = 600;
                this.isJumping = false;
            }
        }
        else {

        }
    }

    private void updateAnimation(){
        if (this.currentHP < 0){
            currentHP = 0;
            this.isAlive = false;
            this.currentAnimation = this.dieAnimation;
            if (this.dieAnimation.isLastFrame())
                this.isActive = false;
        }
        else if (this.isAttacking){
            if (this.currentAnimation != this.attackAnimation[0] &&
                this.currentAnimation != this.attackAnimation[1] &&
                this.currentAnimation != this.attackAnimation[2] &&
                this.currentAnimation != this.attackAnimation[3]){
                this.currentAnimation = this.attackAnimation[rand.nextInt(4)];
            }
            else if (this.currentAnimation.isLastFrame()){
                this.currentAnimation.reset();
                this.isAttacking = false;
                this.currentAnimation = this.attackAnimation[rand.nextInt(4)];
            }
        }
        else if (!this.isJumping && this.velocity.x !=0) {
            this.currentAnimation = this.walkAnimation;
        }
        else if (this.isJumping){
            this.currentAnimation = this.jumpAnimation;
        }
        else
            this.currentAnimation = this.idleAnimation;

        if (this.velocity.x > 0){
            this.isFlip = true;
        } else if (this.velocity.x <0){
            this.isFlip = false;
        }
        this.currentAnimation.flip(this.isFlip);
        this.currentAnimation.update();
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
