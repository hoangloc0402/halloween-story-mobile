package com.halloween.GameScreens;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.halloween.Constants;
import com.halloween.GameContents.HealthBarBoss;
import com.halloween.GameContents.HealthBarMainCharacter;
import com.halloween.GameContents.JoyStick;
import com.halloween.GameObjects.Enemies.Gargoyle;
import com.halloween.GameObjects.Enemies.Zombie;
import com.halloween.GameObjects.MainCharacter;
import com.halloween.R;

public class PlayingScreen implements GameScreen{
    private MainCharacter mainCharacter;
    private JoyStick joyStick;
    private Bitmap pauseButton;
    private Bitmap jumpButton;
    private Bitmap atkButton;
    private Bitmap background;
    private Bitmap backgroundBlock;
    private Rect backgroundBlockWhat;
    private RectF backgroundBlockWhere;
    private Bitmap backgroundMoon;
//    private float backgroundXAxis = 0;
    private Point pauseButtonPosition;
    private Point jumpButtonPosition;
    private Point atkButtonPosition;
    private HealthBarMainCharacter healthBarMainCharacter;
    private HealthBarBoss healthBarBoss;

    private Paint paint;

    private Zombie zombie;

    private Gargoyle gargoyle;

//  comment this and use main char pos
//    private Point tempWatchPosition;

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

        // button
        this.pauseButton = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.pause_button);
        this.pauseButton = Bitmap.createScaledBitmap(pauseButton, 150, 150, false);
        this.pauseButtonPosition = new Point(Constants.SCREEN_WIDTH - 200, 50);

        this.jumpButton = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.pause_button);
        this.jumpButton = Bitmap.createScaledBitmap(jumpButton, 150, 150, false);
        this.jumpButtonPosition = new Point(Constants.SCREEN_WIDTH - 200, Constants.SCREEN_HEIGHT - 200);

        this.atkButton = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.pause_button);
        this.atkButton = Bitmap.createScaledBitmap(atkButton, 150, 150, false);
        this.atkButtonPosition = new Point(Constants.SCREEN_WIDTH - 400, Constants.SCREEN_HEIGHT - 200);

//        this.tempWatchPosition = new Point(50,50);

        this.reset();
        System.out.println("hello");
        this.joyStick = new JoyStick();

        this.healthBarMainCharacter = new HealthBarMainCharacter();
        this.healthBarMainCharacter.setNewHealth(1000);
        this.healthBarMainCharacter.setNewScore(1000);
        this.healthBarBoss = new HealthBarBoss();
        this.healthBarBoss.setNewHealth(1000);
    }

    @Override
    public void reset() {
        this.mainCharacter = new MainCharacter();
        this.zombie = new Zombie(new PointF(100, 900), new PointF(700, 900));
        this.gargoyle = new Gargoyle(new PointF(200, 500), new PointF(900, 500));
    }

    @Override
    public void update() {
        mainCharacter.update();
        gargoyle.update(mainCharacter.getSurroundingBox());
        joyStick.update();
//        zombie.update(mainCharacter.getSurroundingBox());
        healthBarMainCharacter.update();

        // This is just a template for watch position, comment this & use the Main Character Position
//        if (Constants.CURRENT_JOYSTICK_STATE == Constants.JOYSTICK_STATE.RIGHT) {
//            tempWatchPosition.x += 10.0;
//        } else
//            if (Constants.CURRENT_JOYSTICK_STATE == Constants.JOYSTICK_STATE.LEFT) {
//                tempWatchPosition.x -= 10.0;
//            }
//        tempWatchPosition = mainCharacter.getCurrentPosition();
        // Update background X axis pos
        PointF mainPosition = mainCharacter.getCurrentPosition();
//        Log.d("POS 1", mainPosition.x + " , " + Constants.BACKGROUND_X_AXIS);
        if (mainPosition.x < Constants.BACKGROUND_X_AXIS + Constants.SCREEN_WIDTH * 0.15) {
            Constants.BACKGROUND_X_AXIS = (float) (mainPosition.x - Constants.SCREEN_WIDTH * 0.15);
        } else if (mainPosition.x > Constants.BACKGROUND_X_AXIS + Constants.SCREEN_WIDTH * 0.7) {
            Constants.BACKGROUND_X_AXIS = (float) (mainPosition.x - Constants.SCREEN_WIDTH * 0.7);
        }
//        Log.d("POS 2", mainPosition.x + " , " + Constants.BACKGROUND_X_AXIS);
        Constants.BACKGROUND_X_AXIS = Math.max(Constants.BACKGROUND_X_AXIS, (float) 0.0);
        Constants.BACKGROUND_X_AXIS = Math.min(Constants.BACKGROUND_X_AXIS, (float) backgroundBlock.getWidth() - (Constants.SCREEN_WIDTH *  backgroundBlock.getHeight() / Constants.SCREEN_HEIGHT ));

        this.backgroundBlockWhat.set((int) Constants.BACKGROUND_X_AXIS, (int) 0, (int) (Constants.BACKGROUND_X_AXIS + (Constants.SCREEN_WIDTH *  backgroundBlock.getHeight() / Constants.SCREEN_HEIGHT )), backgroundBlock.getHeight());
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, 0, 0, paint);
        canvas.drawBitmap(backgroundMoon, (float) (Constants.SCREEN_WIDTH * 0.7), (float) (Constants.SCREEN_HEIGHT * 0.2), paint);
        canvas.drawBitmap(backgroundBlock, backgroundBlockWhat, backgroundBlockWhere, paint);
        canvas.drawBitmap(pauseButton, pauseButtonPosition.x, pauseButtonPosition.y, paint);
        this.mainCharacter.draw(canvas);
        gargoyle.draw(canvas);
//        zombie.draw(canvas);
        this.joyStick.draw(canvas);
        this.healthBarMainCharacter.draw(canvas);
        this.healthBarBoss.draw(canvas);
        canvas.drawBitmap(atkButton, atkButtonPosition.x, atkButtonPosition.y, paint);
        canvas.drawBitmap(jumpButton, jumpButtonPosition.x, jumpButtonPosition.y, paint);
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
                if (isInRangeOfPauseButton(x, y)) {
                    joyStick.backToCenter();
                    Constants.CURRENT_GAME_STATE = Constants.GAME_STATE.PAUSE;
                }
                if (isInRangeOfAtkButton(x, y)) {
                    Constants.JOYSTICK_ATK_STATE = false;
                }
                if (isInRangeOfJumpButton(x, y)) {
                    Constants.JOYSTICK_JUMP_STATE = false;
                }
                if (joyStick.isInRange(x, y)) {
                    joyStick.backToCenter();
                }
                break;
            case MotionEvent.ACTION_DOWN:
                // Log.d("MOTION:", "ACTION_DOWN" + x + " " + y);
                if (joyStick.isInRange(x, y)) {
                    joyStick.updatePosition(x, y);
                }
                if (isInRangeOfJumpButton(x, y)) {
                    Constants.JOYSTICK_JUMP_STATE = true;
                }
                if (isInRangeOfAtkButton(x, y)) {
                    Constants.JOYSTICK_ATK_STATE = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                // Log.d("MOTION:", "ACTION_MOVE" + x + " " + y);
                if (joyStick.isInRange(x, y)) {
                    joyStick.updatePosition(x, y);
                } else {
                    if (joyStick.isPressed) {
                        joyStick.backToCenter();
                    }
                }
                if (isInRangeOfJumpButton(x, y)) {
                    Constants.JOYSTICK_JUMP_STATE = true;
                }
                if (isInRangeOfAtkButton(x, y)) {
                    Constants.JOYSTICK_ATK_STATE = true;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                // Log.d("MOTION:", "ACTION_POINTER_DOWN" + x + " " + y);
                if (joyStick.isInRange(x, y)) {
                    joyStick.updatePosition(x, y);
                }
                if (isInRangeOfJumpButton(x, y)) {
                    Constants.JOYSTICK_JUMP_STATE = true;
                }
                if (isInRangeOfAtkButton(x, y)) {
                    Constants.JOYSTICK_ATK_STATE = true;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                // Log.d("MOTION:", "ACTION_POINTER_UP" + x + " " + y);
                if (isInRangeOfPauseButton(x, y)) {
                    joyStick.backToCenter();
                    Constants.CURRENT_GAME_STATE = Constants.GAME_STATE.PAUSE;
                }
                if (joyStick.isInRange(x, y)) {
                    joyStick.backToCenter();
                }
                if (isInRangeOfJumpButton(x, y)) {
                    Constants.JOYSTICK_JUMP_STATE = false;
                }
                if (isInRangeOfAtkButton(x, y)) {
                    Constants.JOYSTICK_ATK_STATE = false;
                }
                break;
            case MotionEvent.ACTION_OUTSIDE:
                // Log.d("MOTION:", "ACTION_OUTSIDE" + x + " " + y);
                break;
            case MotionEvent.ACTION_CANCEL:
                // Log.d("MOTION:", "ACTION_CANCEL" + x + " " + y);
                Constants.JOYSTICK_ATK_STATE = false;
                Constants.JOYSTICK_JUMP_STATE = false;
                break;
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

}
