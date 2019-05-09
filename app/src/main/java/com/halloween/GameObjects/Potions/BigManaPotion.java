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

public class BigManaPotion extends Potion {
    private Bitmap bigManaPotion;
    private PointF droppingPosition;
    private static float scale = 0.25f * Constants.SCREEN_HEIGHT / 578f;

    public BigManaPotion(PointF position, ArrayList<RectF> boxes) {
        surroundingBox = new RectF();
        this.isActive = false;
        this.position = position;
        this.droppingPosition = new PointF(position.x, position.y);
        getPotionPosition(boxes);
        this.healthVolume = Constants.BIG_MANA_POTION_VOLUME;

        this.bigManaPotion = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.big_mana_potion);
        this.potionHeight = (int) (this.bigManaPotion.getHeight() * scale);
        this.potionWidth = (int) (this.bigManaPotion.getWidth() * scale);
        this.bigManaPotion = Bitmap.createScaledBitmap(this.bigManaPotion, potionWidth, potionHeight, false);
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
        canvas.drawBitmap(this.bigManaPotion, Constants.getRelativeXPosition(this.droppingPosition.x, Constants.CURRENT_GAME_STATE), this.droppingPosition.y - this.potionHeight, new Paint());
    }

    @Override
    public void update() {
        if (this.droppingPosition.y < this.position.y) {
            this.droppingPosition.y += 10f;
        }
    }
}
