package com.halloween.GameContents;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

import com.halloween.Constants;
import com.halloween.GameObjects.GameObject;
import com.halloween.R;

public class JoyStick implements GameObject {
    private Bitmap joystickBase, joystickButton;

    public boolean isPressedJoyStick = false;
    public boolean isPressedPause = false;
    private float offset = (float)(Math.min(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT) * 0.05);
    private float baseSize = (float)(Math.min(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT) * 0.32);
    private float buttonSize = (float)(baseSize * 0.5);
    private Paint paint = new Paint();
    private PointF joystickBasePosition = new PointF(offset * 2, (float) (Constants.SCREEN_HEIGHT - offset - baseSize * 0.8));
    private PointF joystickCenterPosition = new PointF((float)(joystickBasePosition.x + 0.5*baseSize), (float) (joystickBasePosition.y + 0.5*baseSize));
    private PointF joystickButtonPosition = new PointF((float)(joystickCenterPosition.x - 0.5*buttonSize), (float) (joystickCenterPosition.y - 0.5*buttonSize));
    private PointF joystickButtonOriginalPosition = new PointF((float)(joystickCenterPosition.x - 0.5*buttonSize), (float) (joystickCenterPosition.y - 0.5*buttonSize));

    private Bitmap pauseButton, pauseButtonHover, jumpButton, jumpButtonHover, atkButton, atkButtonHover;
    private Point pauseButtonPosition, jumpButtonPosition, atkButtonPosition;

    public JoyStick() {
//        this.joystickBase = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.joystick_base);
//        this.joystickBase = Bitmap.createScaledBitmap(joystickBase, (int) baseSize, (int) baseSize, false);
        this.joystickButton = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.joystick_button);
        this.joystickButton = Bitmap.createScaledBitmap(joystickButton, (int) buttonSize, (int) buttonSize, false);

        // button
        this.buttonSize = Constants.SCREEN_HEIGHT * 0.16f;
        this.pauseButton = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.pause_button);
        this.pauseButton = Bitmap.createScaledBitmap(pauseButton, (int) (buttonSize * 0.5f), (int) (buttonSize * 0.5f), false);
        this.pauseButtonHover = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.pause_button_hover);
        this.pauseButtonHover = Bitmap.createScaledBitmap(pauseButton, (int) (buttonSize * 0.5f), (int) (buttonSize * 0.5f), false);
        this.pauseButtonPosition = new Point((int)(Constants.SCREEN_WIDTH - offset * 2 - pauseButton.getWidth()), 50);

        this.jumpButton = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.jump_button);
        this.jumpButton = Bitmap.createScaledBitmap(jumpButton, (int) buttonSize, (int) buttonSize, false);
        this.jumpButtonHover = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.jump_button_hover);
        this.jumpButtonHover = Bitmap.createScaledBitmap(jumpButtonHover, (int) buttonSize, (int) buttonSize, false);
        this.jumpButtonPosition = new Point((int)(Constants.SCREEN_WIDTH - offset * 2 - jumpButton.getWidth()), (int)(Constants.SCREEN_HEIGHT - 1.5 * offset - jumpButton.getHeight() * 2));

        this.atkButton = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.attack_button);
        this.atkButton = Bitmap.createScaledBitmap(atkButton, (int) buttonSize , (int) buttonSize, false);
        this.atkButtonHover = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.attack_button_hover);
        this.atkButtonHover = Bitmap.createScaledBitmap(atkButtonHover, (int) buttonSize   , (int) buttonSize, false);
        this.atkButtonPosition = new Point((int)(Constants.SCREEN_WIDTH - offset * 2 - jumpButton.getWidth() - atkButton.getWidth()), (int)(Constants.SCREEN_HEIGHT - offset - atkButton.getHeight()));

        this.paint.setAlpha(255);
    }

    public boolean isInRangeOfJoyStick(float x, float y) {
        float range = (float) (Math.sqrt(Math.pow(x-joystickCenterPosition.x, 2) + Math.pow(y-joystickCenterPosition.y, 2)));
        if (range <= (baseSize + buttonSize + Constants.SCREEN_WIDTH / 2) / 2) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isInRangeOfPauseButton(float x, float y){
        if (
                x >pauseButtonPosition.x &&
                        x < pauseButtonPosition.x + pauseButton.getWidth() &&
                        y > pauseButtonPosition.y &&
                        y < pauseButtonPosition.y + pauseButton.getHeight())
            return true;
        else return false;
    }

    public boolean isInRangeOfAtkButton(float x, float y) {
        if ( x > atkButtonPosition.x && x < atkButtonPosition.x + atkButton.getWidth() &&
                y > atkButtonPosition.y && y < atkButtonPosition.y + atkButton.getHeight())
            return true;
        else
            return false;
    }

    public boolean isInRangeOfJumpButton(float x, float y) {
        if ( x > jumpButtonPosition.x && x < jumpButtonPosition.x + jumpButton.getWidth() &&
                y > jumpButtonPosition.y && y < jumpButtonPosition.y + jumpButton.getHeight())
            return true;
        else
            return false;
    }

    public void backToCenter() {
//        float xV;
//        float yV;
//        if (joystickButtonPosition.x > joystickButtonOriginalPosition.x) {
//            xV = (float) -1;
//        } else {
//            xV = (float) 0.75;
//        }
//        if (joystickButtonPosition.y > joystickButtonOriginalPosition.y) {
//            yV = (float) -1;
//        } else {
//            yV = (float) 0.75;
//        }
//        xV = joystickButtonPosition.x + 15 * xV;
//        yV = joystickButtonPosition.y + 15 * yV;
//
//        if (Math.abs(joystickButtonOriginalPosition.x - joystickButtonPosition.x) < 20) {
//            xV = joystickButtonOriginalPosition.x;
//        }
//        if (Math.abs(joystickButtonOriginalPosition.y - joystickButtonPosition.y) < 20) {
//            yV = joystickButtonOriginalPosition.y;
//        }
//        joystickButtonPosition.set(xV, yV);
        joystickButtonPosition.set(joystickButtonOriginalPosition.x, joystickButtonOriginalPosition.y);
        isPressedJoyStick = false;
        Constants.CURRENT_JOYSTICK_STATE = Constants.JOYSTICK_STATE.MIDDLE;
    }

    private float getAngle(float x, float y) {
        float angle = (float) Math.toDegrees(Math.atan2(y - joystickCenterPosition.y, x - joystickCenterPosition.x));
        angle = angle < 0 ? angle + 360 : angle;
        return 360 - angle;
    }

    public void updatePosition(float x, float y) {
        isPressedJoyStick = true;
        float angle = getAngle(x, y);
        float angleRadians = (float) Math.toRadians(angle);
        float xBound = (float) Math.abs(Math.cos(angleRadians) * baseSize * 0.5);
        float yBound = (float) Math.abs(Math.sin(angleRadians) * baseSize * 0.5);
        float newX = (float) (x - buttonSize*0.5), newY = (float) (y - buttonSize*0.5);
        if (Math.abs(x - joystickCenterPosition.x) > xBound)
            if (x > joystickCenterPosition.x)
                newX = (float) (joystickCenterPosition.x + xBound - buttonSize*0.5);
            else
                newX = (float) (joystickCenterPosition.x - xBound - buttonSize*0.5);
        if (Math.abs(y - joystickCenterPosition.y) > yBound)
            if (y > joystickCenterPosition.y)
                newY = (float) (joystickCenterPosition.y + yBound - buttonSize*0.5);
            else
                newY = (float) (joystickCenterPosition.y - yBound - buttonSize*0.5);
        joystickButtonPosition.set(newX, newY);
        if (angle > 90 && angle < 270) {
            Constants.CURRENT_JOYSTICK_STATE = Constants.JOYSTICK_STATE.LEFT;
        } else {
            Constants.CURRENT_JOYSTICK_STATE = Constants.JOYSTICK_STATE.RIGHT;
        }
    }

    @Override
    public void draw(Canvas canvas) {
//        canvas.drawBitmap(joystickBase, joystickBasePosition.x, joystickBasePosition.y, paint);
        if (Constants.CURRENT_JOYSTICK_STATE == Constants.JOYSTICK_STATE.MIDDLE && !Constants.JOYSTICK_JUMP_STATE && !Constants.JOYSTICK_ATK_STATE) {
            paint.setAlpha(185);
        } else {
            paint.setAlpha(255);
        }
        if (isPressedPause) {
            canvas.drawBitmap(pauseButtonHover, pauseButtonPosition.x, pauseButtonPosition.y, new Paint());
        } else {
            canvas.drawBitmap(pauseButton, pauseButtonPosition.x, pauseButtonPosition.y, new Paint());
        }
        if (Constants.JOYSTICK_ATK_STATE) {
            canvas.drawBitmap(atkButtonHover, atkButtonPosition.x, atkButtonPosition.y, paint);
        } else {
            canvas.drawBitmap(atkButton, atkButtonPosition.x, atkButtonPosition.y, paint);
        }
        if (Constants.JOYSTICK_JUMP_STATE) {
            canvas.drawBitmap(jumpButtonHover, jumpButtonPosition.x, jumpButtonPosition.y, paint);
        } else {
            canvas.drawBitmap(jumpButton, jumpButtonPosition.x, jumpButtonPosition.y, paint);
        }
        canvas.drawBitmap(joystickButton, joystickButtonPosition.x, joystickButtonPosition.y, paint);
    }

    @Override
    public void update() {
    }
}
