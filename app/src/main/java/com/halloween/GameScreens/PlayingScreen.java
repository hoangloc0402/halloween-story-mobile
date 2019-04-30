package com.halloween.GameScreens;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import com.halloween.Constants;
import com.halloween.GameContents.HealthBarMainCharacter;
import com.halloween.GameContents.JoyStick;
import com.halloween.GameContents.Portal;
import com.halloween.GameObjects.MainCharacter;
import com.halloween.R;

public class PlayingScreen implements GameScreen{
    private MainCharacter mainCharacter;
    private JoyStick joyStick;
    private HealthBarMainCharacter healthBarMainCharacter;
    private Portal portal;

    private Bitmap background, backgroundBlock, backgroundCloud, backgroundCloudSmall, backgroundMoon;
    private Rect backgroundBlockWhat;
    private RectF backgroundBlockWhere;
    private float backgroundCloudOffset, backgroundCloudSmallOffset;
    private int backgroundCloudCount, backgroundCloudSmallCount;

    private Paint paint;

    public PlayingScreen() {
        this.paint = new Paint();

        // Map Stuff
        this.background = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.map_bg);
        this.background = Bitmap.createScaledBitmap(background, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, false);

        this.backgroundMoon = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.map_moon);
        this.backgroundMoon = Bitmap.createScaledBitmap(backgroundMoon, (int) (Constants.SCREEN_HEIGHT * 156 * 0.2 / 137), (int) (Constants.SCREEN_HEIGHT * 0.2), false);

        this.backgroundBlock = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.map_merged);
        this.backgroundBlock = Bitmap.createScaledBitmap(backgroundBlock, 10000, 578, false);
        this.backgroundBlockWhat = new Rect(0, 0, (Constants.SCREEN_WIDTH *  backgroundBlock.getHeight() / Constants.SCREEN_HEIGHT ), backgroundBlock.getHeight());
        this.backgroundBlockWhere = new RectF((float)0.0, (float)0.0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        this.backgroundCloud = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.map_cloud);
        this.backgroundCloud = Bitmap.createScaledBitmap(backgroundCloud, (int) (Constants.SCREEN_HEIGHT * 800 * 0.4 / 320), (int) (Constants.SCREEN_HEIGHT * 0.4), false);
        this.backgroundCloudCount = Math.round((float) Constants.SCREEN_WIDTH / this.backgroundCloud.getWidth()) + 1;

        this.backgroundCloudSmall = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.map_cloud_small);
        this.backgroundCloudSmall = Bitmap.createScaledBitmap(backgroundCloudSmall, (int) (Constants.SCREEN_HEIGHT * 1818 * 0.3 / 158), (int) (Constants.SCREEN_HEIGHT * 0.3), false);
        this.backgroundCloudSmallCount = Math.round((float) Constants.SCREEN_WIDTH / this.backgroundCloudSmall.getWidth()) + 1;

        this.reset();
        this.joyStick = new JoyStick();
        this.portal = new Portal();

        this.healthBarMainCharacter = new HealthBarMainCharacter();
        this.healthBarMainCharacter.setNewHealth(1000);
        this.healthBarMainCharacter.setNewScore(1000);

    }

    @Override
    public void reset() {
        this.mainCharacter = new MainCharacter();
    }

    @Override
    public void update() {
        backgroundCloudOffset += 1;
        backgroundCloudSmallOffset += 1.5f;
        if (backgroundCloudOffset > backgroundCloud.getWidth()) backgroundCloudOffset = 0f;
        if (backgroundCloudSmallOffset > backgroundCloudSmall.getWidth()) backgroundCloudSmallOffset = 0f;

        mainCharacter.update();
        joyStick.update();
        if (portal.isInRange()) {this.portal.update();}
        healthBarMainCharacter.update();

        // Update background X axis pos
        PointF mainPosition = mainCharacter.getCurrentPosition();
        if (mainPosition.x < Constants.BACKGROUND_X_AXIS + Constants.SCREEN_WIDTH * 0.15) {
            Constants.BACKGROUND_X_AXIS = (float) (mainPosition.x - Constants.SCREEN_WIDTH * 0.15);
        } else if (mainPosition.x > Constants.BACKGROUND_X_AXIS + Constants.SCREEN_WIDTH * 0.5) {
            Constants.BACKGROUND_X_AXIS = (float) (mainPosition.x - Constants.SCREEN_WIDTH * 0.5);
        }
        Constants.BACKGROUND_X_AXIS = Math.max(Constants.BACKGROUND_X_AXIS, (float) 0.0);
        Constants.BACKGROUND_X_AXIS = Math.min(Constants.BACKGROUND_X_AXIS, (float) backgroundBlock.getWidth() - (Constants.SCREEN_WIDTH *  backgroundBlock.getHeight() / Constants.SCREEN_HEIGHT ));
        this.backgroundBlockWhat.set((int) Constants.BACKGROUND_X_AXIS, (int) 0, (int) (Constants.BACKGROUND_X_AXIS + (Constants.SCREEN_WIDTH *  backgroundBlock.getHeight() / Constants.SCREEN_HEIGHT )), backgroundBlock.getHeight());
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d("BG: ", Constants.BACKGROUND_X_AXIS + " " + backgroundBlock.getWidth() + " " + mainCharacter.getCurrentPosition().x);
        canvas.drawBitmap(background, 0, 0, paint);
        canvas.drawBitmap(backgroundMoon, (float) (Constants.SCREEN_WIDTH * 0.7), (float) (Constants.SCREEN_HEIGHT * 0.125), paint);
        for (int i = 0; i < backgroundCloudCount; i ++) {
            canvas.drawBitmap(backgroundCloud, -backgroundCloudOffset + backgroundCloud.getWidth()*i, Constants.SCREEN_HEIGHT * 0.8f - backgroundCloud.getHeight(), paint);
        }
        for (int i = 0; i < backgroundCloudSmallCount; i ++) {
            canvas.drawBitmap(backgroundCloudSmall, -backgroundCloudSmallOffset + backgroundCloudSmall.getWidth()*i, Constants.SCREEN_HEIGHT * 0.3f - backgroundCloudSmall.getHeight(), paint);
        }
        canvas.drawBitmap(backgroundBlock, backgroundBlockWhat, backgroundBlockWhere, paint);
        this.mainCharacter.draw(canvas);
        if (portal.isInRange()) {this.portal.draw(canvas);}
        this.healthBarMainCharacter.draw(canvas);
        this.joyStick.draw(canvas);
    }

    @Override
    public void terminate() {

    }

    @Override
    public void receiveTouch(MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        int maskedAction = event.getActionMasked();
        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);
        

        switch(maskedAction)
        {
            case MotionEvent.ACTION_UP:
                // Log.d("MOTION:", "ACTION_UP" + x + " " + y );
                if (joyStick.isInRangeOfPauseButton(x, y)) {
                    joyStick.backToCenter();
                    joyStick.isPressedPause = false;
                    Constants.CURRENT_GAME_STATE = Constants.GAME_STATE.PAUSE;
                }
//                if (isInRangeOfAtkButton(x, y)) {
                    Constants.JOYSTICK_ATK_STATE = false;
//                }
//                if (isInRangeOfJumpButton(x, y)) {
                    Constants.JOYSTICK_JUMP_STATE = false;
//                }
                if (joyStick.isInRangeOfJoyStick(x, y)) {
                    joyStick.backToCenter();
                }
                break;
            case MotionEvent.ACTION_DOWN:
                // Log.d("MOTION:", "ACTION_DOWN" + x + " " + y);
                if (joyStick.isInRangeOfJoyStick(x, y)) {
                    joyStick.updatePosition(x, y);
                }
                if (joyStick.isInRangeOfJumpButton(x, y)) {
                    Constants.JOYSTICK_JUMP_STATE = true;
                }
                if (joyStick.isInRangeOfAtkButton(x, y)) {
                    Constants.JOYSTICK_ATK_STATE = true;
                }
                if (joyStick.isInRangeOfPauseButton(x, y)) {
                    joyStick.isPressedPause = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                // Log.d("MOTION:", "ACTION_MOVE" + x + " " + y);
                if (joyStick.isInRangeOfJoyStick(x, y)) {
                    joyStick.updatePosition(x, y);
                } else {
                    if (joyStick.isPressedJoyStick) {
                        joyStick.backToCenter();
                    }
                }
//                if (isInRangeOfJumpButton(x, y)) {
//                    Constants.JOYSTICK_JUMP_STATE = true;
//                }
//                if (isInRangeOfAtkButton(x, y)) {
//                    Constants.JOYSTICK_ATK_STATE = true;
//                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                // Log.d("MOTION:", "ACTION_POINTER_DOWN" + x + " " + y);
                if (joyStick.isInRangeOfJoyStick(x, y)) {
                    joyStick.updatePosition(x, y);
                }
                if (joyStick.isInRangeOfJumpButton(x, y)) {
                    Constants.JOYSTICK_JUMP_STATE = true;
                }
                if (joyStick.isInRangeOfAtkButton(x, y)) {
                    Constants.JOYSTICK_ATK_STATE = true;
                }
                if (joyStick.isInRangeOfPauseButton(x, y)) {
                    joyStick.isPressedPause = true;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                // Log.d("MOTION:", "ACTION_POINTER_UP" + x + " " + y);
                if (joyStick.isInRangeOfPauseButton(x, y)) {
                    joyStick.backToCenter();
                    joyStick.isPressedPause = false;
                    Constants.CURRENT_GAME_STATE = Constants.GAME_STATE.PAUSE;
                }
                if (joyStick.isInRangeOfJoyStick(x, y)) {
                    joyStick.backToCenter();
                }
//                if (isInRangeOfJumpButton(x, y)) {
                    Constants.JOYSTICK_JUMP_STATE = false;
//                }
//                if (isInRangeOfAtkButton(x, y)) {
                    Constants.JOYSTICK_ATK_STATE = false;
//                }
                break;
            case MotionEvent.ACTION_OUTSIDE:
                break;
            case MotionEvent.ACTION_CANCEL:
                Constants.JOYSTICK_ATK_STATE = false;
                Constants.JOYSTICK_JUMP_STATE = false;
                joyStick.isPressedJoyStick = false;
                joyStick.isPressedPause = false;
                break;
        }
    }



}
