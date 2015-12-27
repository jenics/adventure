package com.cb.adventures.animation;
import android.graphics.PointF;
import com.cb.adventures.view.BaseView;


/**
 * Created by jenics on 2015/9/17.
 */
public class SlideViewAnimation extends ViewAnimation {
    public SlideViewAnimation(BaseView view) {
        super(view);
    }

    private PointF mPtDis;
    private PointF mPtSrc;
    private long mMillisecond;
    private long mStartTime;
    public void setDestination(PointF pt,long millisecond){
        mPtSrc = new PointF();
        mPtSrc.x = mView.getPt().x;
        mPtSrc.y = mView.getPt().y;
        mPtDis = new PointF();
        mPtDis.x = pt.x;
        mPtDis.y = pt.y;
        mMillisecond = millisecond;
        mStartTime = System.currentTimeMillis();
    }
    public void setDestination(float x,float y,long millisecond){
        mPtSrc = new PointF();
        mPtSrc.x = mView.getPt().x;
        mPtSrc.y = mView.getPt().y;

        mPtDis = new PointF();
        mPtDis.x = x;
        mPtDis.y = y;
        mMillisecond = millisecond;
    }

    @Override
    public void startAnimation() {
        mStartTime = System.currentTimeMillis();
        super.startAnimation();
    }


    @Override
    public boolean animate() {
        long curTime = System.currentTimeMillis();
        long timeDifference = curTime-mStartTime;
        float ratio = 0.0f;
        if(timeDifference != 0) {
            ratio = (timeDifference*1.0f) / (mMillisecond*1.0f) ;
        }else {
            return false;
        }

        boolean finish = timeDifference >= mMillisecond;
        if(!finish) {
            float left = mPtSrc.x + ratio * (mPtDis.x - mPtSrc.x);
            float top = mPtSrc.y + ratio * (mPtDis.y - mPtSrc.y);
            mView.setPt(
                    left,
                    top
            );

        }else {
            mView.setPt(
                    mPtDis.x,
                    mPtDis.y

            );
        }
        return finish;
    }
}
