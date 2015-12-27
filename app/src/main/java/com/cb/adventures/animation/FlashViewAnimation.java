package com.cb.adventures.animation;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import com.cb.adventures.view.BaseView;

/**
 * 闪烁动画
 * Created by jenics on 2015/12/27.
 */
public class FlashViewAnimation extends ViewAnimation {
    /**
     * 闪烁的时间，单位ms
     */
    protected long timeDuration;
    protected long timeCycle = 200;
    public FlashViewAnimation(BaseView view) {
        super(view);
    }

    public void setTimeDuration(long timeDuration) {
        this.timeDuration = timeDuration;
    }

    @Override
    public boolean animate() {
        long timeNow = System.currentTimeMillis();
        long timePassed = timeNow - mStartTime;
        if (timePassed >= timeDuration) {
            mView.setmPaint(new Paint());       ///还原paint
            return true;
        }
        long timePassedCorrect = timePassed % timeCycle;
        float ratio = timePassedCorrect * 1.0f / timeCycle;
        Paint paint = mView.getmPaint();
        if (paint != null) {
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(ratio);
            ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
                    colorMatrix);
            paint.setColorFilter(colorMatrixFilter);
            paint.setAlpha((int) (255-(128 * ratio)));
        }
        return false;
    }
}
