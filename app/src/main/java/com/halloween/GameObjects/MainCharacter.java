package com.halloween.GameObjects;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.halloween.Animation;

public class MainCharacter implements GameObject {

    Animation currentAnimation;

    public MainCharacter(){
        currentAnimation = new Animation("idle", 830,970,4, 100);
        currentAnimation.play();
    }


    @Override
    public void draw(Canvas canvas) {
        currentAnimation.draw(canvas);
    }

    @Override
    public void update() {
        currentAnimation.update();
    }


}
