package com.halloween.GameContents;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;

import com.halloween.Constants;
import com.halloween.GameObjects.GameObject;
import com.halloween.R;

public class JoyStick implements GameObject {
    private Bitmap joystickBase, joystickButton;

    private float offset = (float)(Math.min(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT) * 0.05);
    private float baseSize = (float)(Math.min(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT) * 0.3);
    private float buttonSize = (float)(baseSize * 0.5);
    private Paint paint = new Paint();
    private PointF joystickBasePosition = new PointF(offset, (float) (Constants.SCREEN_HEIGHT - offset - baseSize));
    private PointF joystickCenterPosition = new PointF((float)(joystickBasePosition.x + 0.5*baseSize), (float) (joystickBasePosition.y + 0.5*baseSize));
    private PointF joystickButtonPosition = new PointF((float)(joystickCenterPosition.x - 0.5*buttonSize), (float) (joystickCenterPosition.y - 0.5*buttonSize));
    private PointF joystickButtonOriginalPosition = new PointF((float)(joystickCenterPosition.x - 0.5*buttonSize), (float) (joystickCenterPosition.y - 0.5*buttonSize));

    public JoyStick() {
        this.joystickBase = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.joystick_base);
        this.joystickBase = Bitmap.createScaledBitmap(joystickBase, (int) baseSize, (int) baseSize, false);
        this.joystickButton = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.joystick_button);
        this.joystickButton = Bitmap.createScaledBitmap(joystickButton, (int) buttonSize, (int) buttonSize, false);
        this.paint.setAlpha(100);
    }

    public boolean isInRange(float x, float y) {
        float range = (float) (Math.sqrt(Math.pow(x-joystickCenterPosition.x, 2) + Math.pow(y-joystickCenterPosition.y, 2)));
        if (range <= (baseSize + buttonSize) / 2) {
            return true;
        } else {
            return false;
        }
    }

    public void backToCenter() {
        float xV;
        float yV;
        if (joystickButtonPosition.x > joystickButtonOriginalPosition.x) {
            xV = (float) -1;
        } else {
            xV = (float) 0.75;
        }
        if (joystickButtonPosition.y > joystickButtonOriginalPosition.y) {
            yV = (float) -1;
        } else {
            yV = (float) 0.75;
        }
        xV = joystickButtonPosition.x + 15 * xV;
        yV = joystickButtonPosition.y + 15 * yV;

        if (Math.abs(joystickButtonOriginalPosition.x - joystickButtonPosition.x) < 20) {
            xV = joystickButtonOriginalPosition.x;
        }
        if (Math.abs(joystickButtonOriginalPosition.y - joystickButtonPosition.y) < 20) {
            yV = joystickButtonOriginalPosition.y;
        }
        joystickButtonPosition.set(xV, yV);
    }

    public void updatePosition(float x, float y) {
        joystickButtonPosition.set((x - buttonSize / 2), (y - buttonSize /2));
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(joystickBase, joystickBasePosition.x, joystickBasePosition.y, paint);
        canvas.drawBitmap(joystickButton, joystickButtonPosition.x, joystickButtonPosition.y, paint);
    }

    @Override
    public void update() {

    }
}
