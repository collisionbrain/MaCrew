package mx.cannavita.utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class OnSwipeListener implements View.OnTouchListener {
    protected boolean isSwipeDetected = false;
    private final GestureDetector gestureDetector;

    public OnSwipeListener (Context ctx) {
        gestureDetector = new GestureDetector(ctx, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }

        });
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean isSwipe = gestureDetector.onTouchEvent(event);
        return isSwipe;
    }


}