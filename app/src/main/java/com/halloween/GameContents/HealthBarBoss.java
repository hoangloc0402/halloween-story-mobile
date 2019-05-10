package com.halloween.GameContents;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Shader;

import com.halloween.Constants;
import com.halloween.GameObjects.GameObject;
import com.halloween.R;

public class HealthBarBoss implements GameObject {
    private PointF healthBarBase = new PointF();
    private double scale;
    private int healthBarWidth;
    private int healthBarHeight;
    private int currentHealth;
    private int decreasingHealth;
    private int increasingHealth;
    private int newHealth;
    private long lastUpdateTime;
    private int blueHealth;
    private int yellowHealth;
    private int greyHealth;
    private HealthBarBoss.HEALTH_STATE healthBarState;
    private Bitmap healthBarBorder;
    private Paint paint;
    public HealthBarBoss() {
        this.healthBarBorder = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.health_bar_border_boss);
        scale = Constants.SCREEN_WIDTH * 0.4 / this.healthBarBorder.getWidth();
        int healthBarBorderWidth = (int) (this.healthBarBorder.getWidth() * scale);
        int healthBarBorderHeight = (int) (this.healthBarBorder.getHeight() * scale);
        this.healthBarBorder = Bitmap.createScaledBitmap(this.healthBarBorder, healthBarBorderWidth, healthBarBorderHeight, false);

        healthBarWidth = this.healthBarBorder.getWidth() * 410 / 480;
        healthBarHeight = this.healthBarBorder.getHeight() * 37 / 81;

        this.healthBarBase.x = (float) (Constants.SCREEN_WIDTH - this.healthBarBorder.getWidth() + this.healthBarBorder.getWidth() * 420 / 480);
        this.healthBarBase.y = (float) (this.healthBarBorder.getHeight() * 40 / 81);

        this.currentHealth = 0;
        this.decreasingHealth = 0;
        this.increasingHealth = 0;
        this.blueHealth = 0;
        this.yellowHealth = 0;
        this.greyHealth = Constants.MAX_HEALTH_BOSS;
        this.healthBarState = HealthBarBoss.HEALTH_STATE.NORMAL;

        this.lastUpdateTime = System.currentTimeMillis();
        this.paint = new Paint();
    }

    @Override
    public void draw(Canvas canvas) {
        Paint blueHealthBarPaint = new Paint();
        Paint greyHealthBarPaint = new Paint();
        Paint yellowHealthBarPaint = new Paint();

        blueHealthBarPaint.setShader(new LinearGradient(0, 0, healthBarWidth, 0, Color.rgb(199, 240, 253), Color.rgb(4, 96, 125), Shader.TileMode.CLAMP));
        greyHealthBarPaint.setShader(new LinearGradient(0, 0, healthBarWidth, 0, Color.rgb(230, 214, 255), Color.rgb(122, 118, 119), Shader.TileMode.MIRROR));
        yellowHealthBarPaint.setShader(new LinearGradient(0, 0, healthBarWidth, 0, Color.rgb(244, 225, 124), Color.rgb(194, 145, 32), Shader.TileMode.MIRROR));

        float blueHeathWidth = this.healthBarWidth * this.blueHealth / Constants.MAX_HEALTH_BOSS;
        float greyHealthWidth = this.healthBarWidth * this.greyHealth / Constants.MAX_HEALTH_BOSS;
        float yellowHealthWidth = this.healthBarWidth * this.yellowHealth / Constants.MAX_HEALTH_BOSS;

        drawRectWithOneRounded(healthBarBase.x - blueHeathWidth - greyHealthWidth,
                healthBarBase.y, healthBarBase.x - blueHeathWidth, healthBarBase.y + this.healthBarHeight, greyHealthBarPaint, canvas);
        drawRectWithOneRounded(healthBarBase.x - blueHeathWidth - yellowHealthWidth,
                healthBarBase.y, healthBarBase.x - blueHeathWidth, healthBarBase.y + this.healthBarHeight, yellowHealthBarPaint, canvas);
        drawRectWithOneRounded(healthBarBase.x - blueHeathWidth, healthBarBase.y, healthBarBase.x, healthBarBase.y + this.healthBarHeight, blueHealthBarPaint, canvas);

        canvas.drawBitmap(this.healthBarBorder, Constants.SCREEN_WIDTH - this.healthBarBorder.getWidth(), 0, paint);
    }

    public void drawRectWithOneRounded(float left, float top, float right, float bottom, Paint paint, Canvas canvas) {
        canvas.drawRect(left, top, right, bottom, paint);
        if (left != right)

            canvas.drawRoundRect((left - this.healthBarBorder.getWidth() * 7 / 480), top, (left + this.healthBarBorder.getWidth() * 7 / 480), bottom, 30f, 30f, paint);
    }

    @Override
    public void update() {
        if (this.newHealth != this.currentHealth)
            updateNewHealth();
        if (System.currentTimeMillis() - this.lastUpdateTime > 1000 / 60) {
            updateHealthBar();
            this.lastUpdateTime = System.currentTimeMillis();
        }
    }

    public void updateHealthBar() {
        switch (this.healthBarState) {
            case INCREASING:
                this.increasingHealth -= 5;
                this.blueHealth += 5;
                this.greyHealth = this.increasingHealth;
                this.yellowHealth = this.decreasingHealth;
                if (this.increasingHealth == 0)
                    this.healthBarState = HealthBarBoss.HEALTH_STATE.NORMAL;
                break;
            case DECREASING:
                this.decreasingHealth -= 5;
                this.blueHealth = this.currentHealth;
                this.yellowHealth = this.decreasingHealth;
                this.greyHealth = this.increasingHealth;
                if (this.decreasingHealth == 0)
                    this.healthBarState = HealthBarBoss.HEALTH_STATE.NORMAL;
                break;
            case NORMAL:
                break;
            default:
                break;
        }
    }

    public void updateNewHealth() {
        switch (this.healthBarState) {
            case NORMAL:
                if (this.newHealth > this.currentHealth) {
                    this.increasingHealth = this.newHealth - this.currentHealth;
                    this.currentHealth = this.newHealth;
                    this.healthBarState = HealthBarBoss.HEALTH_STATE.INCREASING;
                } else {
                    this.decreasingHealth = this.currentHealth - this.newHealth;
                    this.currentHealth = this.newHealth;
                    this.healthBarState = HealthBarBoss.HEALTH_STATE.DECREASING;
                }
                break;
            case DECREASING:
                System.out.println("decresing");
                if (this.newHealth > this.currentHealth) {
                    if (this.newHealth - this.currentHealth >= this.decreasingHealth) {
                        System.out.println("new - current >= decreasing");
                        this.increasingHealth = this.newHealth - this.currentHealth;
                        this.currentHealth = this.newHealth;
                        this.decreasingHealth = 0;
                        this.healthBarState = HealthBarBoss.HEALTH_STATE.INCREASING;
                    } else {
                        this.decreasingHealth -= this.newHealth - this.currentHealth;
                        this.currentHealth = this.newHealth;
                    }
                } else {
                    this.decreasingHealth += this.currentHealth - this.newHealth;
                    this.currentHealth = this.newHealth;
                }
                break;
            case INCREASING:
                if (this.newHealth > this.currentHealth) {
                    this.increasingHealth += this.newHealth - this.currentHealth;
                    this.currentHealth = this.newHealth;
                } else {
                    if (this.currentHealth - this.newHealth <= this.increasingHealth) {
                        this.increasingHealth -= this.currentHealth - this.newHealth;
                        this.currentHealth = this.newHealth;
                    } else {
                        this.decreasingHealth = this.currentHealth - this.newHealth;
                        this.increasingHealth = 0;
                        this.currentHealth = this.newHealth;
                        this.healthBarState = HealthBarBoss.HEALTH_STATE.DECREASING;
                    }
                }
                break;
            default:
                break;
        }
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setNewHealth(int newHealth) {
        this.newHealth = newHealth;
    }

    private enum HEALTH_STATE {INCREASING, DECREASING, NORMAL}
}
