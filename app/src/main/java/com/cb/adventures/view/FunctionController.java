package com.cb.adventures.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.data.ConsumablePropetry;
import com.cb.adventures.data.SkillPropetry;
import com.cb.adventures.utils.ImageLoader;

/**
 * Created by jenics on 2015/11/1.
 */
public class FunctionController extends BaseView {
    public static float WIDTH_RATIO = 0.1f;
    private Bitmap bitmap;
    private int type;
    private SkillPropetry skillPropetry;
    private ConsumablePropetry consumablePropetry;
    public FunctionController() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public SkillPropetry getSkillPropetry() {
        return skillPropetry;
    }

    public void setSkillPropetry(SkillPropetry skillPropetry) {
        this.skillPropetry = skillPropetry;
        type = GameConstants.FUNCTION_TYPE_SKILL;
    }

    public ConsumablePropetry getConsumablePropetry() {
        return consumablePropetry;

    }

    public void setConsumablePropetry(ConsumablePropetry consumablePropetry) {
        this.consumablePropetry = consumablePropetry;
        type = GameConstants.FUNCTION_TYPE_CONSUMABLE;
    }

    public void init() {
        bitmap = ImageLoader.getmInstance().loadBitmap(GameConstants.GAME_SKILL_NAME);
        ///宽度是屏幕宽度的0.1
        width = height = (int) (GameConstants.sGameWidth*WIDTH_RATIO);
        ///
        //pt.x = GameConstants.sGameWidth - width/2 - 15;
        //pt.y = (float) (GameConstants.sGameHeight - height/2 - GameConstants.sGameHeight*0.1);
    }



    @Override
    public void draw(Canvas canvas) {
        float x = getPt().x - width/2;
        float y = getPt().y - height/2;

        ///画控制器框框
        canvas.drawBitmap(bitmap,
                new Rect(   ///src rect
                        0,
                        0,
                        bitmap.getWidth(),
                        bitmap.getHeight()),
                new RectF(x,
                        y,
                        x + width,
                        y + height), null);

    }
}
