package com.halloween.GameScreens;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.halloween.Constants;
import com.halloween.R;

public class GameOverScreen implements GameScreen {
    private Bitmap background;
    private Bitmap currentButtonRestart, buttonRestart, buttonRestartHover;
    private Bitmap currentButtonExit, buttonExit, buttonExitHover;
    private Bitmap scoreBar;
    private Point buttonRestartPosition;
    private Point buttonExitPosition;
    private Point scoreBarPosition;

    private float currentPercent = 0;
    private boolean isFill = true;
    private Rect whatToDraw;
    private RectF whereToDraw;
    private MENU_STATE currentMenuState;
    private Paint paint;
    public GameOverScreen() {
        this.background = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.gameover_bg);
        this.background = Bitmap.createScaledBitmap(background, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, false);

        this.scoreBar = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.score);
        this.scoreBar = Bitmap.createScaledBitmap(scoreBar, (int) (0.26135f * Constants.SCREEN_WIDTH), (int) (0.036458f * Constants.SCREEN_HEIGHT), false);

        int width = Constants.SCREEN_WIDTH / 4;
        int height = Constants.SCREEN_HEIGHT / 10;

        this.buttonRestart = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.restart);
        this.buttonRestart = Bitmap.createScaledBitmap(buttonRestart, width, height, false);
        this.buttonRestartHover = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.restart_hover);
        this.buttonRestartHover = Bitmap.createScaledBitmap(buttonRestartHover, width, height, false);

        this.buttonExit = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.exit);
        this.buttonExit = Bitmap.createScaledBitmap(buttonExit, width, height, false);
        this.buttonExitHover = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.exit_hover);
        this.buttonExitHover = Bitmap.createScaledBitmap(buttonExitHover, width, height, false);

        this.buttonRestartPosition = new Point((Constants.SCREEN_WIDTH - buttonRestart.getWidth()) / 2, (int) (0.61f * Constants.SCREEN_HEIGHT));
        this.buttonExitPosition = new Point(buttonRestartPosition.x, buttonRestartPosition.y + height + 25);
        this.scoreBarPosition = new Point((int) (0.36896f * Constants.SCREEN_WIDTH), (int) (0.52865f * Constants.SCREEN_HEIGHT));
        this.paint = new Paint();
        this.reset();
    }

    @Override
    public void reset() {
        currentMenuState = MENU_STATE.LOADING;
        currentButtonRestart = buttonRestart;
        currentButtonExit = buttonExit;
        paint.setAlpha(0);
        currentPercent = 0;
        isFill = true;
        whatToDraw = new Rect(0, 0, scoreBar.getWidth(), scoreBar.getHeight());
        whereToDraw = new RectF(scoreBarPosition.x, scoreBarPosition.y, scoreBarPosition.x, scoreBarPosition.y + scoreBar.getHeight());

    }

    @Override
    public void update() {
        if (isFill) {
            currentPercent += 2f;
            float maxPercent = Math.min(100 * Constants.CURRENT_SCORE / Constants.MAX_SCORE, 100);
            if (currentPercent > maxPercent) {
                isFill = false;
            } else {
                whereToDraw.set(scoreBarPosition.x, scoreBarPosition.y, scoreBarPosition.x + currentPercent * scoreBar.getWidth() / 100, scoreBarPosition.y + scoreBar.getHeight());
            }
        }

        switch (currentMenuState) {
            case WAITING:
                break;
            case LOADING:
                this.paint.setAlpha(this.paint.getAlpha() + 10);
                if (this.paint.getAlpha() >= 225) {
                    this.paint.setAlpha(255);
                    this.currentMenuState = MENU_STATE.WAITING;
                }
                break;
            case BACK_TO_MAIN:
                this.paint.setAlpha(this.paint.getAlpha() - 10);
                if (this.paint.getAlpha() <= 50) {
                    Constants.CURRENT_GAME_STATE = Constants.GAME_STATE.MAIN_MENU;
                    this.reset();
                }
                break;
            case RESTART:
                this.paint.setAlpha(this.paint.getAlpha() - 10);
                if (this.paint.getAlpha() <= 50) {
                    if (Constants.isInGraveyard) {
                        Constants.IS_SWITCH_GAME_STATE = true;
                        Constants.CURRENT_GAME_STATE = Constants.GAME_STATE.PLAY;
                    } else {
                        Constants.IS_SWITCH_GAME_STATE = true;
                        Constants.CURRENT_GAME_STATE = Constants.GAME_STATE.BOSS;
                    }
                    this.reset();
                }
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, 0, 0, paint);
        canvas.drawBitmap(scoreBar, whatToDraw, whereToDraw, paint);
        canvas.drawBitmap(currentButtonRestart, buttonRestartPosition.x, buttonRestartPosition.y, paint);
        canvas.drawBitmap(currentButtonExit, buttonExitPosition.x, buttonExitPosition.y, paint);
    }

    @Override
    public void terminate() {

    }

    @Override
    public void receiveTouch(MotionEvent event) {
        if (currentMenuState != MENU_STATE.WAITING)
            return;

        float x = event.getX();
        float y = event.getY();

        if (isInRangeOfRestartButton(x, y))
            currentButtonRestart = buttonRestartHover;
        else
            currentButtonRestart = buttonRestart;

        if (isInRangeOfExitButton(x, y))
            currentButtonExit = buttonExitHover;
        else
            currentButtonExit = buttonExit;

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (isInRangeOfRestartButton(x, y)) {
                    currentMenuState = MENU_STATE.RESTART;
                } else if (isInRangeOfExitButton(x, y)) {
                    currentMenuState = MENU_STATE.BACK_TO_MAIN;
                }
                break;
        }
    }

    public boolean isInRangeOfRestartButton(float x, float y) {
        if (
                x > buttonRestartPosition.x &&
                        x < buttonRestartPosition.x + currentButtonRestart.getWidth() &&
                        y > buttonRestartPosition.y &&
                        y < buttonRestartPosition.y + currentButtonRestart.getHeight())
            return true;
        else return false;
    }

    public boolean isInRangeOfExitButton(float x, float y) {
        if (
                x > buttonExitPosition.x &&
                        x < buttonExitPosition.x + currentButtonExit.getWidth() &&
                        y > buttonExitPosition.y &&
                        y < buttonExitPosition.y + currentButtonExit.getHeight())
            return true;
        else return false;
    }

    private enum MENU_STATE {LOADING, WAITING, BACK_TO_MAIN, RESTART}
}
