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

public class SmallManaPotion extends Potion {
    private static float scale = 0.35f * Constants.SCREEN_HEIGHT / Constants.backgroundBossMapAssetHeight;
    private Bitmap smallManaPotion;
    private PointF droppingPosition;

    public SmallManaPotion(PointF position, ArrayList<RectF> boxes) {
        surroundingBox = new RectF();
        this.isActive = false;
        this.position = position;
        this.droppingPosition = new PointF(position.x, position.y);
        getPotionPosition(boxes);
        this.volume = Constants.SMALL_MANA_POTION_VOLUME;
        this.isHealth = false;
        this.smallManaPotion = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.mana_potion);
        this.potionHeight = (int) (this.smallManaPotion.getHeight() * scale);
        this.potionWidth = (int) (this.smallManaPotion.getWidth() * scale);
        this.smallManaPotion = Bitmap.createScaledBitmap(this.smallManaPotion, potionWidth, potionHeight, false);
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
        canvas.drawBitmap(this.smallManaPotion, Constants.getRelativeXPosition(this.droppingPosition.x, Constants.CURRENT_GAME_STATE), this.droppingPosition.y, new Paint());
    }

    @Override
    public void update() {

        if (this.droppingPosition.y < this.position.y) {
            if (this.position.y - this.droppingPosition.y >= 10f)
                this.droppingPosition.y += 10f;
            else
                this.droppingPosition.y += this.position.y - this.droppingPosition.y;
        }
    }

    @Override
    public void setPosition(float x, float y){
        super.setPosition(x, y);
        this.droppingPosition.set(x, y);
    }
}
