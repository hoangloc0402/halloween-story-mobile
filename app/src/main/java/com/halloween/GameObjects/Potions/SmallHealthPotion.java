package com.halloween.GameObjects.Potions;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;

import com.halloween.Constants;
import com.halloween.GameObjects.HealthPotion;
import com.halloween.R;

import static android.content.ContentValues.TAG;

public class SmallHealthPotion extends HealthPotion {
    private Bitmap smallHealthPotion;
    private PointF droppingPosition;
    private static float scale = 0.35f * Constants.SCREEN_HEIGHT / 578f;

    public SmallHealthPotion(PointF position) {
        this.isActive = false;
        this.position = position;
        this.droppingPosition = new PointF(this.position.x, this.position.y - 100f);
        this.healthVolume = Constants.SMALL_HEALTH_POTION_VOLUME;

        this.smallHealthPotion = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.health_potion);
        int potionHeight = (int) (this.smallHealthPotion.getHeight() * scale);
        int potionWidth = (int) (this.smallHealthPotion.getWidth() * scale);
        this.smallHealthPotion = Bitmap.createScaledBitmap(this.smallHealthPotion, potionWidth, potionHeight, false);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.smallHealthPotion, this.droppingPosition.x, this.droppingPosition.y, new Paint());
    }

    @Override
    public void update() {
        if (this.droppingPosition.y < this.position.y) {
            this.droppingPosition.y += 5f;
        }
    }
}
