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
import com.halloween.GameObjects.Enemies.Dragon;
import com.halloween.GameObjects.Enemies.Enemy;
import com.halloween.GameObjects.Enemies.Phantom;
import com.halloween.GameObjects.MainCharacter;
import com.halloween.GameObjects.Potion;
import com.halloween.GameObjects.Potions.SmallHealthPotion;
import com.halloween.GameObjects.Potions.SmallManaPotion;
import com.halloween.R;

import java.util.ArrayList;
import java.util.Random;

public class BossScreen implements GameScreen {

    ArrayList<Phantom> phantoms = new ArrayList<>();
    SmallHealthPotion healthPotion;
    SmallManaPotion manaPotion;
    Dragon dragon;
    //for transition
    private boolean isStarting;
    private long startingTime;
    private int startingAlpha;
    private MainCharacter mainCharacter;
    private HealthBarMainCharacter healthBarMainCharacter;
    private JoyStick joyStick;
    private HealthBarBoss healthBarBoss;
    private Bitmap background, backgroundBlock, backgroundCloud, backgroundCloudSmall;
    private Rect backgroundBlockWhat;
    private RectF backgroundBlockWhere;
    private float backgroundCloudOffset, backgroundCloudSmallOffset;
    private int backgroundCloudCount, backgroundCloudSmallCount;
    private ArrayList<RectF> boxes;
    private RectF tempSurrounding, tempSurroundingMain;
    private RectF tempAttackRange, tempAttackRangeMain;
    private Paint paint;
    private Random rand = new Random();

    public BossScreen() {
        super();
        this.paint = new Paint();
        this.boxes = new ArrayList<>();
        this.initBoxes();
        this.healthBarBoss = new HealthBarBoss();
        this.healthBarBoss.setNewHealth(1000);

        this.healthBarMainCharacter = new HealthBarMainCharacter();
        this.healthBarMainCharacter.setNewHealth(1000);
        this.healthBarMainCharacter.setNewMana(1000);

        this.background = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.map_bg);
        this.background = Bitmap.createScaledBitmap(background, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, false);

        this.backgroundBlock = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.map_boss_merged);
        this.backgroundBlock = Bitmap.createScaledBitmap(backgroundBlock, 1800, 780, false);
        this.backgroundBlockWhat = new Rect(0, 0, (Constants.SCREEN_WIDTH * backgroundBlock.getHeight() / Constants.SCREEN_HEIGHT), backgroundBlock.getHeight());
        this.backgroundBlockWhere = new RectF((float) 0.0, (float) 0.0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        this.backgroundCloud = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.map_boss_cloud);
        this.backgroundCloud = Bitmap.createScaledBitmap(backgroundCloud, (int) (Constants.SCREEN_HEIGHT * 970 * 0.5 / 546), (int) (Constants.SCREEN_HEIGHT * 0.5), false);
        this.backgroundCloudCount = Math.round((float) Constants.SCREEN_WIDTH / this.backgroundCloud.getWidth()) + 2;

        this.backgroundCloudSmall = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.map_boss_cloud_small);
        this.backgroundCloudSmall = Bitmap.createScaledBitmap(backgroundCloudSmall, (int) (Constants.SCREEN_HEIGHT * 500 * 0.6 / 400), (int) (Constants.SCREEN_HEIGHT * 0.6), false);
        this.backgroundCloudSmallCount = Math.round((float) Constants.SCREEN_WIDTH / this.backgroundCloudSmall.getWidth()) + 2;
        this.mainCharacter = MainCharacter.getInstance();

        this.joyStick = new JoyStick();
        tempSurrounding = new RectF();
        tempSurroundingMain = new RectF();
        this.tempAttackRange = new RectF();
        this.tempAttackRangeMain = new RectF();

        for (int i = 0; i < Constants.MAX_PHANTOM_COUNT; i++) {
            PointF newPoint = new PointF(rand.nextInt(Constants.SCREEN_WIDTH), rand.nextInt(Constants.SCREEN_HEIGHT));
            newPoint.x = Constants.getAbsoluteXLength(newPoint.x) + Constants.BACKGROUND_X_AXIS;
            this.phantoms.add(new Phantom(newPoint));
        }
        dragon = new Dragon(new PointF(100,100), new PointF(1700,100));
        healthPotion = new SmallHealthPotion(new PointF(0,0), boxes);
        healthPotion.setActive(false);
        manaPotion = new SmallManaPotion(new PointF(0,0), boxes);
        manaPotion.setActive(false);
    }

    private void initBoxes() {
        this.boxes.add(new RectF(0, 0, 0, Constants.SCREEN_HEIGHT));
        this.boxes.add(new RectF(1800, 0, 1800, Constants.SCREEN_HEIGHT));
        this.boxes.add(new RectF(0, 0.8f * Constants.SCREEN_HEIGHT, 1800, Constants.SCREEN_HEIGHT));
        this.boxes.add(new RectF(247, 0.576923077f * Constants.SCREEN_HEIGHT, 472, 0.653846154f * Constants.SCREEN_HEIGHT));
        this.boxes.add(new RectF(562, 0.346153846f * Constants.SCREEN_HEIGHT, 607, 0.423076923f * Constants.SCREEN_HEIGHT));
        this.boxes.add(new RectF(787, 0.192307692f * Constants.SCREEN_HEIGHT, 922, 0.269230769f * Constants.SCREEN_HEIGHT));
        this.boxes.add(new RectF(697, 0.576923077f * Constants.SCREEN_HEIGHT, 1012, 0.653846154f * Constants.SCREEN_HEIGHT));
        this.boxes.add(new RectF(1102, 0.346153846f * Constants.SCREEN_HEIGHT, 1147, 0.423076923f * Constants.SCREEN_HEIGHT));
        this.boxes.add(new RectF(1237, 0.576923077f * Constants.SCREEN_HEIGHT, 1462, 0.653846154f * Constants.SCREEN_HEIGHT));
    }

    @Override
    public void update() {

        if (Constants.IS_SWITCH_GAME_STATE) {
            Constants.IS_SWITCH_GAME_STATE = false;
            this.isStarting = true;
            this.startingTime = System.currentTimeMillis();
            this.startingAlpha = 0;
            this.paint.setAlpha(this.startingAlpha);
            this.reset();
        }
        if (this.isStarting) {
            long now = System.currentTimeMillis();
            if (now - this.startingTime > 50) {
                this.startingTime = now;
                this.startingAlpha += 10;
                this.paint.setAlpha(Math.min(this.startingAlpha, 255));
                if (startingAlpha >= 255) {
                    this.isStarting = false;
                }
            }
        }

        tempSurroundingMain = mainCharacter.getSurroundingBox();
        tempAttackRangeMain = mainCharacter.getAttackRange();
        for (Phantom phantom : phantoms) {
            if (!phantom.isActive()) {
                phantom.reset(Constants.getAbsoluteXLength(rand.nextInt(Constants.SCREEN_WIDTH)) + Constants.BACKGROUND_X_AXIS, rand.nextInt(Constants.SCREEN_HEIGHT));
                continue;
            }
            tempSurrounding = phantom.getSurroundingBox();
            tempAttackRange = phantom.getAttackRange();
            if (tempAttackRangeMain != null) {
                if (tempAttackRangeMain.intersect(tempSurrounding))
                    phantom.decreaseHealth(mainCharacter.getAttackPower());
            }
            if (tempAttackRange != null) {
                if (tempAttackRange.intersect(tempSurroundingMain))
                    mainCharacter.decreaseHealth(phantom.getAttack());
            } else if (tempSurroundingMain.intersect(tempSurrounding))
                mainCharacter.decreaseHealth(phantom.getDamage());
            phantom.update(tempSurroundingMain);

            if (!phantom.isActive()) {
                mainCharacter.increaseScore(Constants.PHANTOM_POINT);
                if (Constants.HEALTH_POTION_PROB > rand.nextInt(100) && !healthPotion.isActive()){
                    healthPotion.setActive(true);
                    healthPotion.setPosition(phantom.getCurrentPosition().x, phantom.getCurrentPosition().y);
                    healthPotion.getPotionPosition(boxes);
                }

                else if (Constants.MANA_POTION_PROB > rand.nextInt(100) && !manaPotion.isActive()){
                    manaPotion.setActive(true);
                    manaPotion.setPosition(phantom.getCurrentPosition().x, phantom.getCurrentPosition().y);
                    manaPotion.getPotionPosition(boxes);
                }
            }
        }


        if (healthPotion.isActive()){
            if (tempSurroundingMain.intersect(healthPotion.getSurroundingBox())) {
                mainCharacter.increaseHealth(healthPotion.getVolume());
                healthPotion.setActive(false);
            }
            healthPotion.update();
        }

        if (manaPotion.isActive()){
            if (tempSurroundingMain.intersect(manaPotion.getSurroundingBox())) {
                mainCharacter.increaseMana(manaPotion.getVolume());
                manaPotion.setActive(false);
            }
            manaPotion.update();
        }


        dragon.update(tempSurroundingMain);
        if (tempAttackRangeMain!=null && tempAttackRangeMain.intersect(dragon.getSurroundingBox())){
            dragon.decreaseHealth(mainCharacter.getAttackPower());
            if (!dragon.isAlive())
                mainCharacter.increaseScore(Constants.DRAGON_POINT);
        }


        tempAttackRange = dragon.getAttackRange();
        tempSurrounding = dragon.getSurroundingBox();
        if (tempAttackRange!=null && tempAttackRange.intersect(tempSurroundingMain))
            mainCharacter.decreaseHealth(dragon.getAttack());
        if (tempSurrounding!=null && tempSurrounding.intersect(tempSurroundingMain))
            mainCharacter.decreaseHealth(dragon.getDamage());

        tempSurrounding = dragon.bullet.getSurroundingBox();
        if (tempSurrounding!=null && tempSurrounding.intersect(tempSurroundingMain))
            mainCharacter.decreaseHealth(dragon.getAttack());

        if (!dragon.isActive())
            Constants.CURRENT_GAME_STATE = Constants.GAME_STATE.WIN;
        healthBarBoss.setNewHealth((int)dragon.getHealth());

        this.healthBarMainCharacter.setNewMana(mainCharacter.getManaPoint());
        this.healthBarMainCharacter.setNewHealth(mainCharacter.getHealthPoint());
        this.healthBarBoss.update();
        this.healthBarMainCharacter.update();
        this.mainCharacter.update(boxes);
        joyStick.update();

        backgroundCloudOffset += 1;
        backgroundCloudSmallOffset += 1.5f;
        if (backgroundCloudOffset > backgroundCloud.getWidth()) backgroundCloudOffset = 0f;
        if (backgroundCloudSmallOffset > backgroundCloudSmall.getWidth())
            backgroundCloudSmallOffset = 0f;

        // Update background X axis pos
        PointF mainPosition = mainCharacter.getCurrentPosition();
        if (mainPosition.x < Constants.BACKGROUND_X_AXIS + Constants.SCREEN_WIDTH * 0.15) {
            Constants.BACKGROUND_X_AXIS = (float) (mainPosition.x - Constants.SCREEN_WIDTH * 0.15);
        } else if (mainPosition.x > Constants.BACKGROUND_X_AXIS + Constants.SCREEN_WIDTH * 0.5) {
            Constants.BACKGROUND_X_AXIS = (float) (mainPosition.x - Constants.SCREEN_WIDTH * 0.5);
        }
        Constants.BACKGROUND_X_AXIS = Math.max(Constants.BACKGROUND_X_AXIS, (float) 0.0);
        Constants.BACKGROUND_X_AXIS = Math.min(Constants.BACKGROUND_X_AXIS, (float) backgroundBlock.getWidth() - (Constants.SCREEN_WIDTH * backgroundBlock.getHeight() / Constants.SCREEN_HEIGHT));
        this.backgroundBlockWhat.set((int) Constants.BACKGROUND_X_AXIS, (int) 0, (int) (Constants.BACKGROUND_X_AXIS + (Constants.SCREEN_WIDTH * backgroundBlock.getHeight() / Constants.SCREEN_HEIGHT)), backgroundBlock.getHeight());

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, 0, 0, paint);
        for (int i = 0; i < backgroundCloudCount; i++) {
            canvas.drawBitmap(backgroundCloud, -backgroundCloudOffset + backgroundCloud.getWidth() * i, Constants.SCREEN_HEIGHT * 0.3f - backgroundCloud.getHeight(), paint);
        }
        for (int i = 0; i < backgroundCloudSmallCount; i++) {
            canvas.drawBitmap(backgroundCloudSmall, -backgroundCloudSmallOffset + backgroundCloudSmall.getWidth() * i, Constants.SCREEN_HEIGHT * 0.8f - backgroundCloudSmall.getHeight(), paint);
        }
        canvas.drawBitmap(backgroundBlock, backgroundBlockWhat, backgroundBlockWhere, paint);

//        RectF temp = new RectF();
//        for (RectF box: boxes){
//            temp.set(Constants.getRelativeXPosition(box.left,Constants.CURRENT_GAME_STATE), box.top, Constants.getRelativeXPosition(box.right,Constants.CURRENT_GAME_STATE), box.bottom);
//            canvas.drawRect(temp, paint);
//        }

        if (this.isStarting) {
            this.mainCharacter.draw(canvas);
        } else {
            this.mainCharacter.draw(canvas);
        }

        dragon.draw(canvas);

        for (Phantom phantom : phantoms)
            phantom.draw(canvas);

        if (healthPotion.isActive())
            healthPotion.draw(canvas);
        if (manaPotion.isActive())
            manaPotion.draw(canvas);

        this.healthBarBoss.draw(canvas);
        this.healthBarMainCharacter.draw(canvas);
        this.joyStick.draw(canvas);
    }

    @Override
    public void terminate() {

    }

    @Override
    public void reset() {
        this.mainCharacter = MainCharacter.getInstance(200, 600);
        if (!this.mainCharacter.isActive) {
            this.mainCharacter.resetAllValue();
        }
        for(Phantom phantom : phantoms){
            phantom.reset();
        }
        dragon.reset();
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        joyStick.receiveTouch(event);
    }
}
