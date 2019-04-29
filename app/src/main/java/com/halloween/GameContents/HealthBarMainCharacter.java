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

public class HealthBarMainCharacter implements GameObject {
    private enum HEALTH_STATE {INCREASING, DECREASING, NORMAL}

    private enum SCORE_STATE {INCREASING, NORMAL}

    private PointF healthBarBase = new PointF();
    private PointF scoreBarBase = new PointF();
    private double scale;
    private int healthBarWidth;
    private int healthBarHeight;
    private int currentHealth;
    private int decreasingHealth;
    private int increasingHealth;
    private int newHealth;
    private long lastUpdateTime;
    private int redHealth;
    private int yellowHealth;
    private int greyHealth;
    private int scoreBarWidth;
    private int scoreBarHeight;
    private int currentScore;
    private int newScore;
    private int increasingScore;
    private int greenScore;
    private int greyScore;
    private SCORE_STATE scoreBarState;
    private HEALTH_STATE healthBarState;
    private Bitmap healthBarBorder;
    private Paint paint;

    public HealthBarMainCharacter() {
        this.healthBarBorder = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.health_bar_border_main_character);
        scale = Constants.SCREEN_WIDTH * 0.4 / this.healthBarBorder.getWidth();
        int healthBarBorderWidth = (int) (this.healthBarBorder.getWidth() * scale);
        int healthBarBorderHeight = (int) (this.healthBarBorder.getHeight() * scale);
        this.healthBarBorder = Bitmap.createScaledBitmap(this.healthBarBorder, healthBarBorderWidth, healthBarBorderHeight, false);

        this.healthBarWidth = this.healthBarBorder.getWidth() * 330 / 432;
        this.healthBarHeight = this.healthBarBorder.getHeight() * 28 / 92;

        this.healthBarBase.x = (float) (this.healthBarBorder.getWidth() * 85 / 432);
        this.healthBarBase.y = (float) (this.healthBarBorder.getHeight() * 30 / 92);

        this.currentHealth = 0;
        this.newHealth = 0;
        this.decreasingHealth = 0;
        this.increasingHealth = 0;
        this.redHealth = 0;
        this.yellowHealth = 0;
        this.greyHealth = Constants.MAX_HEALTH_MAIN_CHARACTER;
        this.healthBarState = HEALTH_STATE.NORMAL;

        this.scoreBarWidth = this.healthBarBorder.getWidth() * 353 / 432;
        this.scoreBarHeight = this.healthBarBorder.getHeight() * 28 / 92;

        this.scoreBarBase.x = (float) (this.healthBarBorder.getWidth() * 65 / 432);
        this.scoreBarBase.y = (float) (this.healthBarBorder.getHeight() * 62 / 92);

        this.currentScore = 0;
        this.newScore = 0;
        this.increasingScore = 0;
        this.scoreBarState = SCORE_STATE.NORMAL;

        this.lastUpdateTime = System.currentTimeMillis();
        this.paint = new Paint();
    }

    @Override
    public void draw(Canvas canvas) {
        Paint redHealthBarPaint = new Paint();
        Paint greyHealthBarPaint = new Paint();
        Paint yellowHealthBarPaint = new Paint();

        redHealthBarPaint.setShader(new LinearGradient(0, 0, healthBarWidth, 0, Color.rgb(228, 182, 177), Color.rgb(234, 83, 66), Shader.TileMode.CLAMP));
        greyHealthBarPaint.setShader(new LinearGradient(0, 0, healthBarWidth, 0, Color.rgb(230, 214, 255), Color.rgb(122, 118, 119), Shader.TileMode.MIRROR));
        yellowHealthBarPaint.setShader(new LinearGradient(0, 0, healthBarWidth, 0, Color.rgb(244, 225, 124), Color.rgb(194, 145, 32), Shader.TileMode.MIRROR));
//        canvas.drawRoundRect(healthBarBase.x, healthBarBase.y, healthBarBase.x + this.healthBarWidth*this.redHealth/Constants.MAX_HEALTH_MAIN_CHARACTER, healthBarBase.y + this.healthBarHeight, 45f, 45f, redHealthBarPaint);

        float redHeathWidth = this.healthBarWidth * this.redHealth / Constants.MAX_HEALTH_MAIN_CHARACTER;
        float greyHealthWidth = this.healthBarWidth * this.greyHealth / Constants.MAX_HEALTH_MAIN_CHARACTER;
        float yellowHealthWidth = this.healthBarWidth * this.yellowHealth / Constants.MAX_HEALTH_MAIN_CHARACTER;

        drawRectWithOneRounded(healthBarBase.x + redHeathWidth,
                healthBarBase.y, healthBarBase.x + redHeathWidth + greyHealthWidth, healthBarBase.y + this.healthBarHeight, greyHealthBarPaint, canvas);
        drawRectWithOneRounded(healthBarBase.x + redHeathWidth,
                healthBarBase.y, healthBarBase.x + redHeathWidth + yellowHealthWidth, healthBarBase.y + this.healthBarHeight, yellowHealthBarPaint, canvas);
        drawRectWithOneRounded(healthBarBase.x, healthBarBase.y, healthBarBase.x + redHeathWidth, healthBarBase.y + this.healthBarHeight, redHealthBarPaint, canvas);

        Paint greenScoreBarPaint = new Paint();
        Paint greyScoreBarPaint = new Paint();

        greenScoreBarPaint.setShader(new LinearGradient(0, 0, healthBarWidth, 0, Color.rgb(218, 255, 187), Color.rgb(77, 170, 0), Shader.TileMode.CLAMP));
        greyScoreBarPaint.setShader(new LinearGradient(0, 0, healthBarWidth, 0, Color.rgb(230, 214, 255), Color.rgb(122, 118, 119), Shader.TileMode.CLAMP));

        float greenScoreWidth = this.scoreBarWidth * this.greenScore / Constants.MAX_SCORE;
        float greyScoreWidth = this.scoreBarWidth * this.greyScore / Constants.MAX_SCORE;

        drawRectWithOneRounded(scoreBarBase.x + greenScoreWidth,
                scoreBarBase.y, scoreBarBase.x + greenScoreWidth + greyScoreWidth, scoreBarBase.y + this.scoreBarHeight, greyScoreBarPaint, canvas);
        drawRectWithOneRounded(scoreBarBase.x, scoreBarBase.y, scoreBarBase.x + greenScoreWidth, scoreBarBase.y + this.scoreBarHeight, greenScoreBarPaint, canvas);

        canvas.drawBitmap(this.healthBarBorder, 0, 0, paint);
    }

    public void drawRectWithOneRounded(float left, float top, float right, float bottom, Paint paint, Canvas canvas) {
        canvas.drawRect(left, top, right, bottom, paint);
        if (left != right)
            canvas.drawArc(right - this.healthBarHeight / 2, top, right + this.healthBarHeight / 2, bottom, -90f, 180f, true, paint);
    }

    @Override
    public void update() {
        if (this.newHealth != this.currentHealth)
            updateNewHealth();
        if (this.newScore != this.currentScore)
            updateNewScore();
        if (System.currentTimeMillis() - this.lastUpdateTime > 1000 / 60) {
            updateHealthBar();
            updateScoreBar();
            this.lastUpdateTime = System.currentTimeMillis();
        }
    }

    public void updateScoreBar() {
        switch ((this.healthBarState)) {
            case INCREASING:
                this.increasingScore -= 5;
                this.greenScore += 5;
                this.greyScore = this.increasingScore;
                if (this.increasingScore == 0) this.healthBarState = HEALTH_STATE.NORMAL;
                break;
            case NORMAL:
                break;
            default:
                break;
        }
    }

    public void updateHealthBar() {
        switch (this.healthBarState) {
            case INCREASING:
                this.increasingHealth -= 5;
                this.redHealth += 5;
                this.greyHealth = this.increasingHealth;
                this.yellowHealth = this.decreasingHealth;
                if (this.increasingHealth == 0) this.healthBarState = HEALTH_STATE.NORMAL;
                break;
            case DECREASING:
                this.decreasingHealth -= 5;
                this.redHealth = this.currentHealth;
                this.yellowHealth = this.decreasingHealth;
                this.greyHealth = this.increasingHealth;
                if (this.decreasingHealth == 0) this.healthBarState = HEALTH_STATE.NORMAL;
                break;
            case NORMAL:
                break;
            default:
                break;
        }
    }

    public void updateNewScore() {
        switch (this.scoreBarState) {
            case NORMAL:
                this.increasingScore = this.newScore - this.currentScore;
                this.currentScore = this.newScore;
                this.scoreBarState = SCORE_STATE.INCREASING;
                break;
            case INCREASING:
                this.increasingScore += this.newScore - this.currentScore;
                this.currentScore = this.newScore;
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
                    this.healthBarState = HEALTH_STATE.INCREASING;
                } else {
                    this.decreasingHealth = this.currentHealth - this.newHealth;
                    this.currentHealth = this.newHealth;
                    this.healthBarState = HEALTH_STATE.DECREASING;
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
                        this.healthBarState = HEALTH_STATE.INCREASING;
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
                        this.healthBarState = HEALTH_STATE.DECREASING;
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

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public void setNewHealth(int newHealth) {
        this.newHealth = newHealth;
    }

    public void setNewScore(int newScore) {
        this.newScore = newScore;
    }

    public int getCurrentScore() {
        return currentScore;
    }
}
