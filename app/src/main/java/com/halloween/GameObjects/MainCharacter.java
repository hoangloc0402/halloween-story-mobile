package com.halloween.GameObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import com.halloween.Animation;
import com.halloween.R;

public class MainCharacter implements GameObject {

    private Animation currentAnimation;
    private Point position;
    private Rect surroundingBox;
    private Animation idleAnimation;
    private Animation walkAnimation;
    private Animation jumpAnimation;
    private Animation attackAnimation;

    public MainCharacter(int x, int y){
        this.idleAnimation = new Animation(R.drawable.attack_83x97x4, 166,194,4, 100);
        this.currentAnimation = idleAnimation;
        this.surroundingBox = new Rect();
        this.position = new Point(x,y);
        this.currentAnimation.play();
    }


    @Override
    public void draw(Canvas canvas) {
        currentAnimation.draw(canvas, this.position);
    }

    @Override
    public void update() {
        currentAnimation.update();
    }

    public Rect getSurroundingBox(){
        this.surroundingBox.left = position.x;
        this.surroundingBox.top =  position.y;
        this.surroundingBox.right = position.x + currentAnimation.frameWidth;
        this.surroundingBox.bottom = position.y + currentAnimation.frameHeight;
        return this.surroundingBox;
    }

    public Boolean collide(Rect targetBox){
        return targetBox.intersect(this.getSurroundingBox());
    }
}
