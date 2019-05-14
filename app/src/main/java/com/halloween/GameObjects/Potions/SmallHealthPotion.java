package com.halloween.GameObjects.Potions;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.halloween.Constants;
import com.halloween.GameObjects.Potion;
import com.halloween.R;

import java.util.ArrayList;

public class SmallHealthPotion extends Potion {
    private static float scale = 0.35f * Constants.SCREEN_HEIGHT / 578f;
    private Bitmap smallHealthPotion;
    private PointF droppingPosition;

    public SmallHealthPotion(PointF position, ArrayList<RectF> boxes) {
        surroundingBox = new RectF();
        this.isActive = false;
        this.position = position;
        this.droppingPosition = new PointF(position.x, position.y);
//        getPotionPosition(boxes);
        this.volume = Constants.SMALL_HEALTH_POTION_VOLUME;
        this.isHealth = true;
        this.smallHealthPotion = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.health_potion);
        this.potionHeight = (int) (this.smallHealthPotion.getHeight() * scale);
        this.potionWidth = (int) (this.smallHealthPotion.getWidth() * scale);
        this.smallHealthPotion = Bitmap.createScaledBitmap(this.smallHealthPotion, potionWidth, potionHeight, false);
    }

    public void getPotionPosition(ArrayList<RectF> boxes) {
        getSurroundingBox();
        float minY = 0f;
        float minDeltaY = 1000000f;
        for (RectF box : boxes) {
            if (this.surroundingBox.right > box.left && this.surroundingBox.left < box.right) {
                float deltaY = box.top - this.surroundingBox.bottom;
                if (deltaY < minDeltaY && deltaY > 0) {
                    minDeltaY = box.top - this.surroundingBox.bottom;
                    minY = box.top;
                }
            }
        }
        this.position.y = minY - this.surroundingBox.height();
    }

    @Override
    public RectF getSurroundingBox() {
        surroundingBox.set(this.position.x, this.position.y, this.position.x + Constants.getAbsoluteXLength(this.potionWidth), this.potionHeight + this.position.y);
        return surroundingBox;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.smallHealthPotion, Constants.getRelativeXPosition(this.droppingPosition.x, Constants.CURRENT_GAME_STATE), this.droppingPosition.y - this.potionHeight, new Paint());
    }

    @Override
    public void update() {
        if (this.droppingPosition.y < this.position.y) {
            this.droppingPosition.y += 10f;
        }
    }

    @Override
    public void setPosition(float x, float y){
        super.setPosition(x, y);
        this.droppingPosition.set(x, y);
    }
}
