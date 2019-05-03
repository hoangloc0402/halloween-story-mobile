package com.halloween.GameScreens;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.halloween.Constants;
import com.halloween.GameContents.HealthBarBoss;
import com.halloween.GameContents.HealthBarMainCharacter;
import com.halloween.GameContents.JoyStick;
import com.halloween.GameObjects.MainCharacter;
import com.halloween.R;

import java.util.ArrayList;

public class BossScreen implements GameScreen {

    private MainCharacter mainCharacter;

    private HealthBarMainCharacter healthBarMainCharacter;
    private JoyStick joyStick;
    private HealthBarBoss healthBarBoss;
    private Bitmap background, backgroundBlock, backgroundCloud, backgroundCloudSmall;
    private Rect backgroundBlockWhat;
    private RectF backgroundBlockWhere;
    private float backgroundCloudOffset, backgroundCloudSmallOffset;
    private int backgroundCloudCount, backgroundCloudSmallCount;
    private Paint paint;

    public BossScreen() {
        super();
        this.paint = new Paint();
        this.healthBarBoss = new HealthBarBoss();
        this.healthBarBoss.setNewHealth(1000);

        this.healthBarMainCharacter = new HealthBarMainCharacter();
        this.healthBarMainCharacter.setNewHealth(1000);
        this.healthBarMainCharacter.setNewScore(1000);

        this.background = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.map_bg);
        this.background = Bitmap.createScaledBitmap(background, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, false);

        this.backgroundBlock = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.map_boss_merged);
        this.backgroundBlock = Bitmap.createScaledBitmap(backgroundBlock, 1800, 780, false);
        this.backgroundBlockWhat = new Rect(0, 0, (Constants.SCREEN_WIDTH *  backgroundBlock.getHeight() / Constants.SCREEN_HEIGHT ), backgroundBlock.getHeight());
        this.backgroundBlockWhere = new RectF((float)0.0, (float)0.0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        this.backgroundCloud = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.map_boss_cloud);
        this.backgroundCloud = Bitmap.createScaledBitmap(backgroundCloud, (int) (Constants.SCREEN_HEIGHT * 970 * 0.5 / 546), (int) (Constants.SCREEN_HEIGHT * 0.5), false);
        this.backgroundCloudCount = Math.round((float) Constants.SCREEN_WIDTH / this.backgroundCloud.getWidth()) + 2;

        this.backgroundCloudSmall = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.map_boss_cloud_small);
        this.backgroundCloudSmall = Bitmap.createScaledBitmap(backgroundCloudSmall, (int) (Constants.SCREEN_HEIGHT * 500 * 0.6 / 400), (int) (Constants.SCREEN_HEIGHT * 0.6), false);
        this.backgroundCloudSmallCount = Math.round((float) Constants.SCREEN_WIDTH / this.backgroundCloudSmall.getWidth()) + 2;

        this.mainCharacter = new MainCharacter(600,600);
        this.joyStick = new JoyStick();

    }

    @Override
    public void update() {
        this.healthBarBoss.update();
        this.healthBarMainCharacter.update();
        mainCharacter.update(new ArrayList<RectF>());
        joyStick.update();

        backgroundCloudOffset += 1;
        backgroundCloudSmallOffset += 1.5f;
        if (backgroundCloudOffset > backgroundCloud.getWidth()) backgroundCloudOffset = 0f;
        if (backgroundCloudSmallOffset > backgroundCloudSmall.getWidth()) backgroundCloudSmallOffset = 0f;

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
        canvas.drawBitmap(background, 0, 0, paint);
        for (int i = 0; i < backgroundCloudCount; i ++) {
            canvas.drawBitmap(backgroundCloud, -backgroundCloudOffset + backgroundCloud.getWidth()*i, Constants.SCREEN_HEIGHT * 0.3f - backgroundCloud.getHeight(), paint);
        }
        for (int i = 0; i < backgroundCloudSmallCount; i ++) {
            canvas.drawBitmap(backgroundCloudSmall, -backgroundCloudSmallOffset + backgroundCloudSmall.getWidth()*i, Constants.SCREEN_HEIGHT * 0.8f - backgroundCloudSmall.getHeight(), paint);
        }
        canvas.drawBitmap(backgroundBlock, backgroundBlockWhat, backgroundBlockWhere, paint);
        this.mainCharacter.draw(canvas);
        this.healthBarBoss.draw(canvas);
        this.healthBarMainCharacter.draw(canvas);
        this.joyStick.draw(canvas);
    }

    @Override
    public void terminate() {

    }

    @Override
    public void reset() {

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
