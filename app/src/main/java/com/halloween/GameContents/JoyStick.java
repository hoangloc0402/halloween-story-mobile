package com.halloween.GameContents;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.view.MotionEvent;

import com.halloween.Constants;
import com.halloween.GameObjects.GameObject;
import com.halloween.R;

public class JoyStick implements GameObject {
    public boolean isPressedJoyStick = false, isPressedPause = false, isPressedTransform = false;
    private Bitmap joystickBase, joystickButton;
    private float offset = (float) (Math.min(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT) * 0.05);
    private float baseSize = (float) (Math.min(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT) * 0.32);
    private float buttonSize = (float) (baseSize * 0.5);
    private Paint paintLeft = new Paint(), paintRight = new Paint();
    private PointF joystickBasePosition = new PointF(offset * 2, (float) (Constants.SCREEN_HEIGHT - offset - baseSize * 0.8));
    private PointF joystickCenterPosition = new PointF((float) (joystickBasePosition.x + 0.5 * baseSize), (float) (joystickBasePosition.y + 0.5 * baseSize));
    private PointF joystickButtonPosition = new PointF((float) (joystickCenterPosition.x - 0.5 * buttonSize), (float) (joystickCenterPosition.y - 0.5 * buttonSize));
    private PointF joystickButtonOriginalPosition = new PointF((float) (joystickCenterPosition.x - 0.5 * buttonSize), (float) (joystickCenterPosition.y - 0.5 * buttonSize));

    private Bitmap pauseButton, pauseButtonHover, jumpButton, jumpButtonHover, atkButton, atkButtonHover, transformButton, transformButtonHover;
    private Point pauseButtonPosition, jumpButtonPosition, atkButtonPosition, transformButtonPosition;
    MediaPlayer attackSound;

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
        this.pauseButtonPosition = new Point((int) (Constants.SCREEN_WIDTH - offset * 2 - pauseButton.getWidth()), 50);

        this.jumpButton = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.jump_button);
        this.jumpButton = Bitmap.createScaledBitmap(jumpButton, (int) buttonSize, (int) buttonSize, false);
        this.jumpButtonHover = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.jump_button_hover);
        this.jumpButtonHover = Bitmap.createScaledBitmap(jumpButtonHover, (int) buttonSize, (int) buttonSize, false);
        this.jumpButtonPosition = new Point((int) (Constants.SCREEN_WIDTH - offset * 2 - jumpButton.getWidth()), (int) (Constants.SCREEN_HEIGHT - 1.5 * offset - jumpButton.getHeight() * 2));

        this.atkButton = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.attack_button);
        this.atkButton = Bitmap.createScaledBitmap(atkButton, (int) buttonSize, (int) buttonSize, false);
        this.atkButtonHover = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.attack_button_hover);
        this.atkButtonHover = Bitmap.createScaledBitmap(atkButtonHover, (int) buttonSize, (int) buttonSize, false);
        this.atkButtonPosition = new Point((int) (Constants.SCREEN_WIDTH - offset * 2 - jumpButton.getWidth() - atkButton.getWidth()), (int) (Constants.SCREEN_HEIGHT - offset - atkButton.getHeight()));

        this.transformButton = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.transform_button);
        this.transformButton = Bitmap.createScaledBitmap(transformButton, (int) (0.7f * buttonSize), (int) (0.7f * buttonSize), false);
        this.transformButtonHover = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.transform_button_hover);
        this.transformButtonHover = Bitmap.createScaledBitmap(transformButtonHover, (int) (0.7f * buttonSize), (int) (0.7f * buttonSize), false);
        this.transformButtonPosition = new Point((int) (Constants.SCREEN_WIDTH - offset * 3.5f - jumpButton.getWidth() - atkButton.getWidth() - transformButton.getWidth()), (int) (Constants.SCREEN_HEIGHT - offset - atkButton.getHeight() + 0.5f * (atkButton.getHeight() - transformButton.getHeight())));

        this.paintLeft.setAlpha(255);
        this.paintRight.setAlpha(255);
        this.attackSound = MediaPlayer.create(Constants.CURRENT_CONTEXT, R.raw.attack_sound);
    }

    public boolean isInRangeOfJoyStick(float x, float y) {
        float range = (float) (Math.sqrt(Math.pow(x - joystickCenterPosition.x, 2) + Math.pow(y - joystickCenterPosition.y, 2)));
        if (range <= (baseSize + buttonSize + Constants.SCREEN_WIDTH / 2) / 2) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isInRangeOfPauseButton(float x, float y) {
        if (
                x > pauseButtonPosition.x &&
                        x < pauseButtonPosition.x + pauseButton.getWidth() &&
                        y > pauseButtonPosition.y &&
                        y < pauseButtonPosition.y + pauseButton.getHeight())
            return true;
        else return false;
    }

    public boolean isInRangeOfAtkButton(float x, float y) {
        if (x > atkButtonPosition.x && x < atkButtonPosition.x + atkButton.getWidth() &&
                y > atkButtonPosition.y && y < atkButtonPosition.y + atkButton.getHeight())
            return true;
        else
            return false;
    }

    public boolean isInRangeOfJumpButton(float x, float y) {
        if (x > jumpButtonPosition.x && x < jumpButtonPosition.x + jumpButton.getWidth() &&
                y > jumpButtonPosition.y && y < jumpButtonPosition.y + jumpButton.getHeight())
            return true;
        else
            return false;
    }

    public boolean isInRangeOfTransformButton(float x, float y) {
        if (x > transformButtonPosition.x && x < transformButtonPosition.x + transformButton.getWidth() &&
                y > transformButtonPosition.y && y < transformButtonPosition.y + transformButton.getHeight())
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
        float newX = (float) (x - buttonSize * 0.5), newY = (float) (y - buttonSize * 0.5);
        if (Math.abs(x - joystickCenterPosition.x) > xBound)
            if (x > joystickCenterPosition.x)
                newX = (float) (joystickCenterPosition.x + xBound - buttonSize * 0.5);
            else
                newX = (float) (joystickCenterPosition.x - xBound - buttonSize * 0.5);
        if (Math.abs(y - joystickCenterPosition.y) > yBound)
            if (y > joystickCenterPosition.y)
                newY = (float) (joystickCenterPosition.y + yBound - buttonSize * 0.5);
            else
                newY = (float) (joystickCenterPosition.y - yBound - buttonSize * 0.5);
        joystickButtonPosition.set(newX, newY);
        if (angle > 90 && angle < 270) {
            Constants.CURRENT_JOYSTICK_STATE = Constants.JOYSTICK_STATE.LEFT;
        } else {
            Constants.CURRENT_JOYSTICK_STATE = Constants.JOYSTICK_STATE.RIGHT;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (Constants.CURRENT_GAME_STATE == Constants.GAME_STATE.PLAY || Constants.CURRENT_GAME_STATE == Constants.GAME_STATE.BOSS) {
            if (!Constants.JOYSTICK_JUMP_STATE && !Constants.JOYSTICK_ATK_STATE) {
                paintRight.setAlpha(185);
            } else {
                paintRight.setAlpha(255);
            }
            if (Constants.CURRENT_JOYSTICK_STATE == Constants.JOYSTICK_STATE.MIDDLE) {
                paintLeft.setAlpha(185);
            } else {
                paintLeft.setAlpha(255);
            }
            if (isPressedPause) {
                canvas.drawBitmap(pauseButtonHover, pauseButtonPosition.x, pauseButtonPosition.y, new Paint());
            } else {
                canvas.drawBitmap(pauseButton, pauseButtonPosition.x, pauseButtonPosition.y, new Paint());
            }
            if (Constants.MAIN_CHARACTER_IS_FULL_MANA) {
                if (isPressedTransform) {
                    canvas.drawBitmap(transformButtonHover, transformButtonPosition.x, transformButtonPosition.y, new Paint());
                } else {
                    canvas.drawBitmap(transformButton, transformButtonPosition.x, transformButtonPosition.y, new Paint());
                }
            }
            if (Constants.JOYSTICK_ATK_STATE) {
                canvas.drawBitmap(atkButtonHover, atkButtonPosition.x, atkButtonPosition.y, paintRight);
            } else {
                canvas.drawBitmap(atkButton, atkButtonPosition.x, atkButtonPosition.y, paintRight);
            }
            if (Constants.JOYSTICK_JUMP_STATE) {
                canvas.drawBitmap(jumpButtonHover, jumpButtonPosition.x, jumpButtonPosition.y, paintRight);
            } else {
                canvas.drawBitmap(jumpButton, jumpButtonPosition.x, jumpButtonPosition.y, paintRight);
            }
            canvas.drawBitmap(joystickButton, joystickButtonPosition.x, joystickButtonPosition.y, paintLeft);
        }
    }

    public void receiveTouch(MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        int maskedAction = event.getActionMasked();
        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);

        switch (maskedAction) {
            case MotionEvent.ACTION_UP:
                // Log.d("MOTION:", "ACTION_UP" + x + " " + y );
                if (isInRangeOfPauseButton(x, y)) {
                    backToCenter();
                    isPressedPause = false;
                    Constants.CURRENT_GAME_STATE = Constants.GAME_STATE.PAUSE;
                }
                if (Constants.MAIN_CHARACTER_IS_FULL_MANA && isInRangeOfTransformButton(x, y)) {
                    isPressedTransform = false;
                    Constants.JOYSTICK_TRANSFORM_STATE = true;
                }
//                if (isInRangeOfAtkButton(x, y)) {
                Constants.JOYSTICK_ATK_STATE = false;
//                }
//                if (isInRangeOfJumpButton(x, y)) {
                Constants.JOYSTICK_JUMP_STATE = false;
//                }
                if (isInRangeOfJoyStick(x, y)) {
                    backToCenter();
                }
                break;
            case MotionEvent.ACTION_DOWN:
                // Log.d("MOTION:", "ACTION_DOWN" + x + " " + y);
                if (isInRangeOfJoyStick(x, y)) {
                    updatePosition(x, y);
                }
                if (isInRangeOfJumpButton(x, y)) {
                    Constants.JOYSTICK_JUMP_STATE = true;
                }
                if (isInRangeOfAtkButton(x, y)) {
                    Constants.JOYSTICK_ATK_STATE = true;
                }
                if (isInRangeOfPauseButton(x, y)) {
                    isPressedPause = true;
                }
                if (Constants.MAIN_CHARACTER_IS_FULL_MANA && isInRangeOfTransformButton(x, y)) {
                    isPressedTransform = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                // Log.d("MOTION:", "ACTION_MOVE" + x + " " + y);
                if (isInRangeOfJoyStick(x, y)) {
                    updatePosition(x, y);
                } else {
                    if (isPressedJoyStick) {
                        backToCenter();
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
                if (isInRangeOfJoyStick(x, y)) {
                    updatePosition(x, y);
                }
                if (isInRangeOfJumpButton(x, y)) {
                    Constants.JOYSTICK_JUMP_STATE = true;
                }
                if (isInRangeOfAtkButton(x, y)) {
                    Constants.JOYSTICK_ATK_STATE = true;
                }
                if (isInRangeOfPauseButton(x, y)) {
                    isPressedPause = true;
                }
                if (Constants.MAIN_CHARACTER_IS_FULL_MANA && isInRangeOfTransformButton(x, y)) {
                    isPressedTransform = true;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                // Log.d("MOTION:", "ACTION_POINTER_UP" + x + " " + y);
                if (isInRangeOfPauseButton(x, y)) {
                    backToCenter();
                    isPressedPause = false;
                    Constants.CURRENT_GAME_STATE = Constants.GAME_STATE.PAUSE;
                }
                if (Constants.MAIN_CHARACTER_IS_FULL_MANA && isInRangeOfTransformButton(x, y)) {
                    isPressedTransform = false;
                    Constants.JOYSTICK_TRANSFORM_STATE = true;
                }
                if (isInRangeOfJoyStick(x, y)) {
                    backToCenter();
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
                isPressedJoyStick = false;
                isPressedPause = false;
                isPressedTransform = false;
                break;
        }
    }

    @Override
    public void update() {
        if (Constants.JOYSTICK_ATK_STATE){
            if(!attackSound.isPlaying()) {
                attackSound.start();
            }
        }
        if (Constants.CURRENT_GAME_STATE == Constants.GAME_STATE.PLAY) {
            pauseButtonPosition = new Point((int) (Constants.SCREEN_WIDTH - offset * 2 - pauseButton.getWidth()), 50);
        }
        if (Constants.CURRENT_GAME_STATE == Constants.GAME_STATE.BOSS) {
            pauseButtonPosition = new Point((int) (Constants.SCREEN_WIDTH * 0.5f - pauseButton.getWidth() * 0.5f), 55);
        }
    }
}
