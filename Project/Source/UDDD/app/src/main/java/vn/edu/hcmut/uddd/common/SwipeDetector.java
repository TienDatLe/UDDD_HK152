package vn.edu.hcmut.uddd.common;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by TRAN VAN HEN on 3/30/2016.
 */
public class SwipeDetector implements View.OnTouchListener {

    private float downX, downY, upX, upY;
    private View v;

    private onSwipeEvent swipeEventListener;

    public SwipeDetector(View v){
        this.v=v;
        this.v.setOnTouchListener(this);
    }

    public void setOnSwipeListener(onSwipeEvent listener)
    {
        this.swipeEventListener=listener;
    }


    public void onRightToLeftSwipe() {
        if (this.swipeEventListener != null) {
            this.swipeEventListener.SwipeEventDetected(this.v, SwipeDirection.RIGHT_TO_LEFT);
        }
    }

    public void onLeftToRightSwipe(){
        if (this.swipeEventListener != null) {
            this.swipeEventListener.SwipeEventDetected(this.v, SwipeDirection.LEFT_TO_RIGHT);
        }
    }

    public void onTopToBottomSwipe(){
        if (this.swipeEventListener != null) {
            this.swipeEventListener.SwipeEventDetected(this.v, SwipeDirection.TOP_TO_BOTTOM);
        }
    }

    public void onBottomToTopSwipe(){
        if (this.swipeEventListener != null) {
            this.swipeEventListener.SwipeEventDetected(this.v, SwipeDirection.BOTTOM_TO_TOP);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                upX = event.getX();
                upY = event.getY();
                float deltaX = downX - upX;
                float deltaY = downY - upY;

                if(Math.abs(deltaX) > Math.abs(deltaY) && Math.abs(deltaX) > ConstCommon.MIN_SWIPE_DICTANCE)
                {
                    if(deltaX < 0){
                        this.onLeftToRightSwipe();
                        return true;
                    }
                    else{
                        this.onRightToLeftSwipe();
                        return true;
                    }
                }
                else if (Math.abs(deltaX) < Math.abs(deltaY) && Math.abs(deltaY) > ConstCommon.MIN_SWIPE_DICTANCE)
                {
                    if(deltaY < 0){
                        this.onTopToBottomSwipe();
                        return true;
                    }
                    else{
                        this.onBottomToTopSwipe();
                        return true;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public interface onSwipeEvent
    {
        void SwipeEventDetected(View v, SwipeDirection SwipeType);
    }
}
