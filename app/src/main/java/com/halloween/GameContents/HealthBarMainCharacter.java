package com.halloween.GameContents;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.halloween.Constants;
import com.halloween.GameObjects.GameObject;
import com.halloween.R;

public class HealthBarMainCharacter implements GameObject {
    private final PointF healthBarBase = new PointF(88, 30);
    private double scale;
    private int healthBarWidth;
    private int healthBarHeight;
    private int currentHealth;
    private Bitmap healthBarBorder;
    private Bitmap healthBar;

    private Paint paint;

    public HealthBarMainCharacter() {
        this.healthBarBorder = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.health_bar_border_main_character);
        scale = Constants.SCREEN_WIDTH * 0.4 / this.healthBarBorder.getWidth();
        int healthBarBorderWidth = (int) (this.healthBarBorder.getWidth() * scale);
        int healthBarBorderHeight = (int) (this.healthBarBorder.getHeight() * scale);
        this.healthBarBorder = Bitmap.createScaledBitmap(this.healthBarBorder, healthBarBorderWidth, healthBarBorderHeight, false);

        healthBarWidth = (int) (300 * scale);
        healthBarHeight = (int) (15 * scale);
        this.healthBar = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.healthbar);
        this.healthBar = Bitmap.createScaledBitmap(this.healthBar, healthBarWidth, healthBarHeight, false);

        this.healthBarBase.x = (float) (this.healthBarBase.x * scale);
        this.healthBarBase.y = (float) (this.healthBarBase.y * scale);

        this.paint = new Paint();
    }

    @Override
    public void draw(Canvas canvas) {
        System.out.println("Health size: "+healthBarWidth + " " + healthBarHeight);
        System.out.println("Screen size: "+Constants.SCREEN_WIDTH + " "+ Constants.SCREEN_HEIGHT);
        canvas.drawBitmap(this.healthBar, (healthBarBase.x), (healthBarBase.y), paint);
        canvas.drawBitmap(this.healthBarBorder, 0, 0, paint);
    }

    @Override
    public void update() {

    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }
}
