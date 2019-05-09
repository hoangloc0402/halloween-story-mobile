package com.halloween.GameScreens;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;

import com.halloween.Constants;
import com.halloween.GameContents.HealthBarMainCharacter;
import com.halloween.GameContents.JoyStick;
import com.halloween.GameContents.Portal;
import com.halloween.GameObjects.Enemies.Gargoyle;
import com.halloween.GameObjects.Enemies.Zombie;
import com.halloween.GameObjects.MainCharacter;
import com.halloween.GameObjects.Trap;
import com.halloween.GameObjects.Traps.CampFire;
import com.halloween.GameObjects.Traps.FireTrap;
import com.halloween.GameObjects.Traps.Spear;
import com.halloween.GameObjects.Traps.SpearHorizontal;
import com.halloween.GameObjects.Traps.SpearVertical;
import com.halloween.R;

import java.util.ArrayList;
import java.util.Iterator;

import static android.content.ContentValues.TAG;

public class GraveyardScreen implements GameScreen {
    private MainCharacter mainCharacter;
    private JoyStick joyStick;
    private HealthBarMainCharacter healthBarMainCharacter;
    private Portal portal;

    private Bitmap background, backgroundBlock, backgroundCloud, backgroundCloudSmall, backgroundMoon;
    private Rect backgroundBlockWhat;
    private RectF backgroundBlockWhere;
    private float backgroundCloudOffset, backgroundCloudSmallOffset;
    private int backgroundCloudCount, backgroundCloudSmallCount;
    private ArrayList<RectF> boxes;
    private Paint paint;
    private RectF tempRect, tempSurrounding;

    private Zombie zombie;
    private Gargoyle gargoyle;
//    private Zombie zombie;

    private ArrayList<Trap> traps;

    private FireTrap fireTrap;
    private CampFire campFire;
    private Spear spear;
    private SpearHorizontal spearHorizontal;
    private SpearVertical spearVertical;

    public GraveyardScreen() {
        this.paint = new Paint();
        this.boxes = new ArrayList<>();
        this.initBoxes();
        // Map Stuff
        this.background = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.map_bg);
        this.background = Bitmap.createScaledBitmap(background, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, false);

        this.backgroundMoon = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.map_moon);
        this.backgroundMoon = Bitmap.createScaledBitmap(backgroundMoon, (int) (Constants.SCREEN_HEIGHT * 156 * 0.2 / 137), (int) (Constants.SCREEN_HEIGHT * 0.2), false);

        this.backgroundBlock = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.map_merged);
        this.backgroundBlock = Bitmap.createScaledBitmap(backgroundBlock, 10000, 578, false);
        this.backgroundBlockWhat = new Rect(0, 0, (Constants.SCREEN_WIDTH * backgroundBlock.getHeight() / Constants.SCREEN_HEIGHT), backgroundBlock.getHeight());
        this.backgroundBlockWhere = new RectF((float) 0.0, (float) 0.0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        this.backgroundCloud = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.map_cloud);
        this.backgroundCloud = Bitmap.createScaledBitmap(backgroundCloud, (int) (Constants.SCREEN_HEIGHT * 800 * 0.4 / 320), (int) (Constants.SCREEN_HEIGHT * 0.4), false);
        this.backgroundCloudCount = Math.round((float) Constants.SCREEN_WIDTH / this.backgroundCloud.getWidth()) + 2;

        this.backgroundCloudSmall = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.map_cloud_small);
        this.backgroundCloudSmall = Bitmap.createScaledBitmap(backgroundCloudSmall, (int) (Constants.SCREEN_HEIGHT * 1818 * 0.3 / 158), (int) (Constants.SCREEN_HEIGHT * 0.3), false);
        this.backgroundCloudSmallCount = Math.round((float) Constants.SCREEN_WIDTH / this.backgroundCloudSmall.getWidth()) + 2;

        this.reset();
        this.joyStick = new JoyStick();
        this.portal = new Portal();

        this.healthBarMainCharacter = new HealthBarMainCharacter();
//        this.healthBarMainCharacter.setNewHealth(1000);
//        this.healthBarMainCharacter.setNewScore(1000);

        this.traps = new ArrayList<>();
        this.initTraps();
        this.tempRect = new RectF();
        this.tempSurrounding = new RectF();
    }

    private void initTraps() {
        float scale = Constants.SCREEN_HEIGHT / 578f;

        float spearVerticalTrapScale = scale * 1.0f;
        traps.add(new SpearVertical(new PointF(4990, (float) (0.8 * Constants.SCREEN_HEIGHT - 93 * spearVerticalTrapScale)), 1000, spearVerticalTrapScale));
        traps.add(new SpearVertical(new PointF(5400, (float) (0.8 * Constants.SCREEN_HEIGHT - 93 * spearVerticalTrapScale)), 1000, spearVerticalTrapScale));
        traps.add(new SpearVertical(new PointF(5765, (float) (0.8 * Constants.SCREEN_HEIGHT - 315 * spearVerticalTrapScale)), 1000, spearVerticalTrapScale));
        traps.add(new SpearVertical(new PointF(6237, (float) (0.8 * Constants.SCREEN_HEIGHT - 268 * spearVerticalTrapScale)), 1000, spearVerticalTrapScale));
        traps.add(new SpearVertical(new PointF(6295, (float) (0.8 * Constants.SCREEN_HEIGHT - 268 * spearVerticalTrapScale)), 1000, spearVerticalTrapScale));
        traps.add(new SpearVertical(new PointF(8250, (float) (0.8 * Constants.SCREEN_HEIGHT - 93 * spearVerticalTrapScale)), 1000, spearVerticalTrapScale));

        float spearHorizontalTrapScale = scale * 1.0f;
        traps.add(new SpearHorizontal(new PointF(8145, (float) (0.8 * Constants.SCREEN_HEIGHT - 373 * spearHorizontalTrapScale)), 1000, spearHorizontalTrapScale));
        traps.add(new SpearHorizontal(new PointF(8145, (float) (0.8 * Constants.SCREEN_HEIGHT - 193 * spearHorizontalTrapScale)), 1000, spearHorizontalTrapScale));
        traps.add(new SpearHorizontal(new PointF(8145, (float) (0.8 * Constants.SCREEN_HEIGHT - 63 * spearHorizontalTrapScale)), 1000, spearHorizontalTrapScale));

        float campFireTrapScale = scale * 1.0f;
        traps.add(new CampFire(new PointF(7375, (float) (0.8 * Constants.SCREEN_HEIGHT - 120 * campFireTrapScale)), campFireTrapScale));

        float spearTrapScale = scale * 1.0f;
        traps.add(new Spear(new PointF(5620, (float) (0.8 * Constants.SCREEN_HEIGHT - 75 * spearTrapScale)), 0, spearTrapScale));
        traps.add(new Spear(new PointF(5690, (float) (0.8 * Constants.SCREEN_HEIGHT - 75 * spearTrapScale)), 0, spearTrapScale));
        traps.add(new Spear(new PointF(5815, (float) (0.8 * Constants.SCREEN_HEIGHT - 75 * spearTrapScale)), 0, spearTrapScale));
        traps.add(new Spear(new PointF(5890, (float) (0.8 * Constants.SCREEN_HEIGHT - 75 * spearTrapScale)), 0, spearTrapScale));
        traps.add(new Spear(new PointF(6020, (float) (0.8 * Constants.SCREEN_HEIGHT - 75 * spearTrapScale)), 0, spearTrapScale));
        traps.add(new Spear(new PointF(6090, (float) (0.8 * Constants.SCREEN_HEIGHT - 75 * spearTrapScale)), 0, spearTrapScale));
        traps.add(new Spear(new PointF(6160, (float) (0.8 * Constants.SCREEN_HEIGHT - 75 * spearTrapScale)), 0, spearTrapScale));

        float fireTrapScale = scale * 1.0f;
        traps.add(new FireTrap(new PointF(7755, (float) (0.8 * Constants.SCREEN_HEIGHT - 201 * fireTrapScale)), 2000, fireTrapScale));
        traps.add(new FireTrap(new PointF(7885, (float) (0.8 * Constants.SCREEN_HEIGHT - 377 * fireTrapScale)), 2000, fireTrapScale));
    }

    @Override
    public void reset() {
        this.gargoyle = new Gargoyle(new PointF(500, 700), new PointF(800, 200));
        this.mainCharacter = MainCharacter.getInstance(600, 600);
        this.zombie = new Zombie(new PointF(100, 700), new PointF(900, 700));
    }

    @Override
    public void update() {

        if (Constants.IS_SWITCH_GAME_STATE) {
            Constants.IS_SWITCH_GAME_STATE = false;
            this.reset();
        }

        backgroundCloudOffset += 1;
        backgroundCloudSmallOffset += 1.5f;
        if (backgroundCloudOffset > backgroundCloud.getWidth()) backgroundCloudOffset = 0f;
        if (backgroundCloudSmallOffset > backgroundCloudSmall.getWidth())
            backgroundCloudSmallOffset = 0f;

        mainCharacter.update(boxes);
        this.zombie.update(mainCharacter.getSurroundingBox());
//        System.out.println("Zombie hereeeeeeeeeeeeeeeeeeeeeee");
        this.gargoyle.update(mainCharacter.getSurroundingBox());

        joyStick.update();
        if (portal.isInRange()) {this.portal.update();}

        healthBarMainCharacter.setNewHealth(mainCharacter.getHealthPoint());
        healthBarMainCharacter.setNewMana(mainCharacter.getManaPoint());
        healthBarMainCharacter.update();

        tempSurrounding = mainCharacter.getSurroundingBox();
        for (Trap trap : traps) {
            tempRect = trap.getSurroundingBox();
            if (tempRect!=null){
                if (tempSurrounding.intersect(tempRect))
                    mainCharacter.decreaseHealth(trap.getDamage());
            }
            trap.update();
        }

        // Update background X axis pos
        PointF mainPosition = mainCharacter.getCurrentPosition();
        if (mainPosition.x < Constants.BACKGROUND_X_AXIS + (Constants.SCREEN_WIDTH * backgroundBlock.getHeight() / Constants.SCREEN_HEIGHT) * 0.3) {
            Constants.BACKGROUND_X_AXIS = (float) (mainPosition.x - (Constants.SCREEN_WIDTH * backgroundBlock.getHeight() / Constants.SCREEN_HEIGHT) * 0.3);
        } else if (mainPosition.x > Constants.BACKGROUND_X_AXIS + 0.6f * (Constants.SCREEN_WIDTH * backgroundBlock.getHeight() / Constants.SCREEN_HEIGHT)) {
            Constants.BACKGROUND_X_AXIS = (float) (mainPosition.x - 0.6f * (Constants.SCREEN_WIDTH * backgroundBlock.getHeight() / Constants.SCREEN_HEIGHT));
        }
        Constants.BACKGROUND_X_AXIS = Math.max(Constants.BACKGROUND_X_AXIS, (float) 0.0);
        Constants.BACKGROUND_X_AXIS = Math.min(Constants.BACKGROUND_X_AXIS, (float) backgroundBlock.getWidth() - (Constants.SCREEN_WIDTH * backgroundBlock.getHeight() / Constants.SCREEN_HEIGHT));
        this.backgroundBlockWhat.set((int) Constants.BACKGROUND_X_AXIS, (int) 0, (int) (Constants.BACKGROUND_X_AXIS + (Constants.SCREEN_WIDTH * backgroundBlock.getHeight() / Constants.SCREEN_HEIGHT)), backgroundBlock.getHeight());


        Pair<Boolean,PointF> pair = portal.isInSuckingRange(mainCharacter.getSurroundingBox());
        if (pair.first) {
            MainCharacter.getInstance((int) (pair.second.x * Constants.MAIN_CHARACTER_V_X * 0.75f  + mainCharacter.getCurrentPosition().x), (int) (mainCharacter.getCurrentPosition().y));
        }
        if (portal.isInTransitionRange(mainCharacter.getSurroundingBox())) {
            Constants.CURRENT_GAME_STATE = Constants.GAME_STATE.BOSS;
            Constants.IS_SWITCH_GAME_STATE = true;
        }
    }

    @Override
    public void draw(Canvas canvas) {
//        Log.d("BG: ", Constants.BACKGROUND_X_AXIS + " " + backgroundBlock.getWidth() + " " + mainCharacter.getCurrentPosition().x);
        canvas.drawBitmap(background, 0, 0, paint);
        canvas.drawBitmap(backgroundMoon, (float) (Constants.SCREEN_WIDTH * 0.7), (float) (Constants.SCREEN_HEIGHT * 0.125), paint);
        for (int i = 0; i < backgroundCloudCount; i++) {
            canvas.drawBitmap(backgroundCloud, -backgroundCloudOffset + backgroundCloud.getWidth() * i, Constants.SCREEN_HEIGHT * 0.8f - backgroundCloud.getHeight(), paint);
        }
        for (int i = 0; i < backgroundCloudSmallCount; i++) {
            canvas.drawBitmap(backgroundCloudSmall, -backgroundCloudSmallOffset + backgroundCloudSmall.getWidth() * i, Constants.SCREEN_HEIGHT * 0.3f - backgroundCloudSmall.getHeight(), paint);
        }
        canvas.drawBitmap(backgroundBlock, backgroundBlockWhat, backgroundBlockWhere, paint);
        if (portal.isInRange()) {this.portal.draw(canvas);}
        RectF temp = new RectF();
        for (RectF box : boxes) {
            temp.set(Constants.getRelativeXPosition(box.left, Constants.CURRENT_GAME_STATE), box.top, Constants.getRelativeXPosition(box.right, Constants.CURRENT_GAME_STATE), box.bottom);
            canvas.drawRect(temp, paint);
        }

//        if (Constants.JOYSTICK_JUMP_STATE)
//            mainCharacter.hurt(1);
//        RectF temp = this.mainCharacter.getAttackRange();
//        if (temp!=null){
//            temp.left = Constants.getRelativeXPosition(temp.left, Constants.GAME_STATE.PLAY);
//            temp.right = Constants.getRelativeXPosition(temp.right, Constants.GAME_STATE.PLAY);
//            canvas.drawRect(temp, paint);
//        }


        this.healthBarMainCharacter.draw(canvas);
        for (Trap trap : traps) {
            trap.draw(canvas);
        }
        /*this.fireTrap.draw(canvas);
        this.campFire.draw(canvas);
        this.spear.draw(canvas);
        this.spearHorizontal.draw(canvas);
        this.spearVertical.draw(canvas);*/
//        this.zombie.draw(canvas);


//        this.zombie.draw(canvas);
        this.gargoyle.draw(canvas);
        this.mainCharacter.draw(canvas);

        this.joyStick.draw(canvas);
    }

    @Override
    public void terminate() {

    }

    private void initBoxes() {
        this.boxes.add(new RectF(0, 0, 1, Constants.SCREEN_HEIGHT));
        this.boxes.add(new RectF(10000f, 0, 10010f, Constants.SCREEN_HEIGHT));
        this.boxes.add(new RectF(0, 0.8f * Constants.SCREEN_HEIGHT, 10000, Constants.SCREEN_HEIGHT));
        this.boxes.add(new RectF(382, Constants.SCREEN_HEIGHT * 0.578f, 816, Constants.SCREEN_HEIGHT * 0.656f));
        this.boxes.add(new RectF(650, Constants.SCREEN_HEIGHT * 0.347f, 883, Constants.SCREEN_HEIGHT * 0.424f));
        this.boxes.add(new RectF(1116.3f, Constants.SCREEN_HEIGHT * 0.578f, 1283, Constants.SCREEN_HEIGHT * 0.656f));
        this.boxes.add(new RectF(1249.6f, Constants.SCREEN_HEIGHT * 0.347f, 1416.3f, Constants.SCREEN_HEIGHT * 0.424f));
        this.boxes.add(new RectF(1449.6f, Constants.SCREEN_HEIGHT * 0.116f, 1683, Constants.SCREEN_HEIGHT * 0.193f));
        this.boxes.add(new RectF(1716.296296f, Constants.SCREEN_HEIGHT * 0.57840617f, 2349.62963f, Constants.SCREEN_HEIGHT * 0.655526992f));
        this.boxes.add(new RectF(2416.296296f, Constants.SCREEN_HEIGHT * 0.424164524f, 2449.62963f, Constants.SCREEN_HEIGHT * 0.501285347f));
        this.boxes.add(new RectF(2549.62963f, Constants.SCREEN_HEIGHT * 0.269922879f, 2582.962963f, Constants.SCREEN_HEIGHT * 0.347043702f));
        this.boxes.add(new RectF(2649.62963f, Constants.SCREEN_HEIGHT * 0.115681234f, 2882.962963f, Constants.SCREEN_HEIGHT * 0.192802057f));
        this.boxes.add(new RectF(2949.62963f, Constants.SCREEN_HEIGHT * 0.269922879f, 2982.962963f, Constants.SCREEN_HEIGHT * 0.347043702f));
        this.boxes.add(new RectF(3082.962963f, Constants.SCREEN_HEIGHT * 0.424164524f, 3116.296296f, Constants.SCREEN_HEIGHT * 0.501285347f));
        this.boxes.add(new RectF(3449.62963f, Constants.SCREEN_HEIGHT * 0.347043702f, 3682.962963f, Constants.SCREEN_HEIGHT * 0.424164524f));
        this.boxes.add(new RectF(3782.962963f, Constants.SCREEN_HEIGHT * 0.501285347f, 3949.62963f, Constants.SCREEN_HEIGHT * 0.57840617f));
        this.boxes.add(new RectF(4049.62963f, Constants.SCREEN_HEIGHT * 0.655526992f, 4482.962963f, Constants.SCREEN_HEIGHT * 0.809768638f));
        this.boxes.add(new RectF(4049.62963f, Constants.SCREEN_HEIGHT * 0.269922879f, 4349.62963f, Constants.SCREEN_HEIGHT * 0.347043702f));
        this.boxes.add(new RectF(5082.962963f, Constants.SCREEN_HEIGHT * 0.57840617f, 5116.296296f, Constants.SCREEN_HEIGHT * 0.655526992f));
        this.boxes.add(new RectF(5216.296296f, Constants.SCREEN_HEIGHT * 0.424164524f, 5249.62963f, Constants.SCREEN_HEIGHT * 0.501285347f));
        this.boxes.add(new RectF(5349.62963f, Constants.SCREEN_HEIGHT * 0.269922879f, 5382.962963f, Constants.SCREEN_HEIGHT * 0.347043702f));
        this.boxes.add(new RectF(5575f, Constants.SCREEN_HEIGHT * 0.269922879f, 5625f, Constants.SCREEN_HEIGHT * 0.809768638f));
        this.boxes.add(new RectF(5545f, Constants.SCREEN_HEIGHT * 0.45f, 5600f, Constants.SCREEN_HEIGHT * 0.51f));
        this.boxes.add(new RectF(5625f, Constants.SCREEN_HEIGHT * 0.36f, 5655f, Constants.SCREEN_HEIGHT * 0.43f));this.boxes.add(new RectF(5775f, Constants.SCREEN_HEIGHT * 0.424164524f, 5825f, Constants.SCREEN_HEIGHT * 0.809768638f));
        this.boxes.add(new RectF(5820f, Constants.SCREEN_HEIGHT * 0.467128027f, 5850f, Constants.SCREEN_HEIGHT * 0.536332179f));
        this.boxes.add(new RectF(5975f, Constants.SCREEN_HEIGHT * 0.269922879f, 6025f, Constants.SCREEN_HEIGHT * 0.809768638f));
        this.boxes.add(new RectF(5935f, Constants.SCREEN_HEIGHT * 0.389273356f, 5980f, Constants.SCREEN_HEIGHT * 0.458477509f));
        this.boxes.add(new RectF(6010f, Constants.SCREEN_HEIGHT * 0.302768166f, 6045f, Constants.SCREEN_HEIGHT * 0.380622837f));
        this.boxes.add(new RectF(6010f, Constants.SCREEN_HEIGHT * 0.553633218f, 6095f, Constants.SCREEN_HEIGHT * 0.6f));
        this.boxes.add(new RectF(6249.62963f, Constants.SCREEN_HEIGHT * 0.501285347f, 6382.962963f, Constants.SCREEN_HEIGHT * 0.57840617f));
        this.boxes.add(new RectF(6366.666667f, Constants.SCREEN_HEIGHT * 0.44344473f, 6400f, Constants.SCREEN_HEIGHT * 0.501285347f));
        this.boxes.add(new RectF(6316.296296f, Constants.SCREEN_HEIGHT * 0.57840617f, 6382.962963f, Constants.SCREEN_HEIGHT * 0.655526992f));
        this.boxes.add(new RectF(6416.296296f, Constants.SCREEN_HEIGHT * 0.655526992f, 6549.62963f, Constants.SCREEN_HEIGHT * 0.9f));
        this.boxes.add(new RectF(6382.962963f, Constants.SCREEN_HEIGHT * 0.501285347f, 6416.296296f, Constants.SCREEN_HEIGHT * 0.809768638f));
        this.boxes.add(new RectF(6716.296296f, Constants.SCREEN_HEIGHT * 0.424164524f, 7082.962963f, Constants.SCREEN_HEIGHT * 0.501285347f));
        this.boxes.add(new RectF(7149.62963f, Constants.SCREEN_HEIGHT * 0.192802057f, 7182.962963f, Constants.SCREEN_HEIGHT * 0.269922879f));
        this.boxes.add(new RectF(7482.962963f, Constants.SCREEN_HEIGHT * 0.115681234f, 7516.296296f, Constants.SCREEN_HEIGHT * 0.192802057f));
        this.boxes.add(new RectF(8316.296296f, Constants.SCREEN_HEIGHT * -0.642673522f, 8516.296296f, Constants.SCREEN_HEIGHT * 0.115681234f));
        this.boxes.add(new RectF(8516.296296f, Constants.SCREEN_HEIGHT * -0.642673522f, 8549.62963f, Constants.SCREEN_HEIGHT * 0.424164524f));
        this.boxes.add(new RectF(8349.62963f, Constants.SCREEN_HEIGHT * 0.347043702f, 8516.296296f, Constants.SCREEN_HEIGHT * 0.424164524f));
        this.boxes.add(new RectF(8316.296296f, Constants.SCREEN_HEIGHT * 0.347043702f, 8349.62963f, Constants.SCREEN_HEIGHT * 0.655526992f));
        this.boxes.add(new RectF(8349.62963f, Constants.SCREEN_HEIGHT * 0.57840617f, 8749.62963f, Constants.SCREEN_HEIGHT * 0.655526992f));
        this.boxes.add(new RectF(8049.62963f, Constants.SCREEN_HEIGHT * 0.115681234f, 8149.62963f, Constants.SCREEN_HEIGHT * 0.809768638f));
        this.boxes.add(new RectF(7982.962963f, Constants.SCREEN_HEIGHT * 0.269922879f, 8049.62963f, Constants.SCREEN_HEIGHT * 0.809768638f));
        this.boxes.add(new RectF(7916.296296f, Constants.SCREEN_HEIGHT * 0.424164524f, 7982.962963f, Constants.SCREEN_HEIGHT * 0.809768638f));
        this.boxes.add(new RectF(7849.62963f, Constants.SCREEN_HEIGHT * 0.57840617f, 7916.296296f, Constants.SCREEN_HEIGHT * 0.809768638f));
        this.boxes.add(new RectF(7782.962963f, Constants.SCREEN_HEIGHT * 0.732647815f, 7849.62963f, Constants.SCREEN_HEIGHT * 0.809768638f));
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        joyStick.receiveTouch(event);
    }
}