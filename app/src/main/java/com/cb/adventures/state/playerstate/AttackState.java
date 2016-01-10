package com.cb.adventures.state.playerstate;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.music.MusicManager;
import com.cb.adventures.view.Map;
import com.cb.adventures.view.Player;

/**
 * Created by jenics on 2015/10/12.
 * 攻击状态
 */
public class AttackState extends PlayerBaseState {
    private int frameIndex;
    private Bitmap bitmap;
    private int frameCount;
    private int rowIndex;
    private int width;
    private int height;
    private OnAttackListener onAttackListener;
    public interface OnAttackListener {
        void onAttackOver();
    }

    public AttackState(int id,Player player,int frameCount,int rowIndex , Bitmap bitmap,int width,int height,OnAttackListener listener) {
        super(id,player);
        frameIndex = 0;
        this.rowIndex = rowIndex;
        this.frameCount = frameCount;
        this.bitmap = bitmap;
        this.width = width;
        this.height = height;
        onAttackListener = listener;

    }

    @Override
    public boolean nextFrame() {
        //return super.nextFrame();
        frameIndex++;
        if (frameIndex >= frameCount) {
            frameIndex = 0;

            player.changeState(GameConstants.getDirection(stateId) == 0 ?
                    GameConstants.STATE_STOP_LEFT : GameConstants.STATE_STOP_RIGHT);
            onAttackListener.onAttackOver();
        }

        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        //super.draw(canvas);
        int disWidth = (int) (width*GameConstants.zoomRatio);
        int disHeight = (int) (height*GameConstants.zoomRatio);
        float x = player.getPt().x - disWidth/2;
        float y = player.getPt().y - disHeight/2;

        PointF ptScreem = Map.toScreemPt(new PointF(x, y));
        x = ptScreem.x;
        y = ptScreem.y;

        ///画攻击效果
        if(stateId == GameConstants.STATE_ATTACK_LEFT) {
            canvas.drawBitmap(bitmap,
                    new Rect(   ///src rect
                            width * frameIndex,
                            rowIndex * height,
                            width * frameIndex + width,
                            rowIndex * height + height),
                    new RectF(x,
                            y,
                            x + disWidth,
                            y + disHeight), player.getPaint());

        }else if(stateId == GameConstants.STATE_ATTACK_RIGHT) {
            Matrix matrix = new Matrix();
            matrix.postScale(-1, 1); //镜像垂直翻转

            Bitmap bmpTmp = Bitmap.createBitmap(bitmap,
                    width * frameIndex,
                    rowIndex * height,
                    width,
                    height,
                    matrix,
                    true);

            canvas.drawBitmap(bmpTmp,
                    new Rect(   ///src rect
                            0,
                            0,
                            width,
                            height),
                    new RectF(x,
                            y,
                            x + disWidth,
                            y + disHeight), player.getPaint());
            bmpTmp.recycle();
        }
    }

    @Override
    public void entry() {
        frameIndex = 0;
        super.entry();
        MusicManager.getInstance().playSound(MusicManager.STATIC_SOUND_TYPE_ATTACK,3);
    }

    @Override
    public void leave() {
        super.leave();
        frameIndex = 0;
    }
}
