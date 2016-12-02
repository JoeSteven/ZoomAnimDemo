package com.joe.zoomdemo.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;


/**
 * Describe
 * Author Joe
 * created at 16/11/6.
 */

public class ZoomAnimationUtils {
    public static final int ANIMATION_DURATION = 300;


    public static void startZoomUpAnim(final ZoomInfo preViewInfo, final View targetView, final Animator.AnimatorListener listener) {
        targetView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                targetView.getViewTreeObserver().removeOnPreDrawListener(this);
                int startWidth = preViewInfo.getWidth();
                int startHeight = preViewInfo.getHeight();
                int endWidth = targetView.getWidth();
                int endHeight = targetView.getHeight();
                int[] screenLocation = new int[2];
                targetView.getLocationOnScreen(screenLocation);
                int endX = screenLocation[0];
                int endY = screenLocation[1];
                float startScaleX = (float) endWidth / startWidth;
                float startScaleY = (float) endHeight / startHeight;
                int translationX = preViewInfo.getScreenX() - endX;
                int translationY = preViewInfo.getScreenY() - endY;

                targetView.setPivotX(0);
                targetView.setPivotY(0);
                targetView.setTranslationX(translationX);
                targetView.setTranslationY(translationY);
                targetView.setScaleX(1 / startScaleX);
                targetView.setScaleY(1 / startScaleY);
                targetView.setVisibility(View.VISIBLE);
                ViewPropertyAnimator animator = targetView.animate();
                animator.setDuration(ANIMATION_DURATION)
                        .scaleX(1f)
                        .scaleY(1f)
                        .translationX(0)
                        .translationY(0)
                        .start();
                if (listener != null) {
                    animator.setListener(listener);
                }
                return true;
            }
        });
    }

    public static void startZoomDownAnim(ZoomInfo preViewInfo, final View targetView, final Animator.AnimatorListener listener){
        int endWidth = preViewInfo.getWidth();
        int endHeight = preViewInfo.getHeight();
        int startWidth = targetView.getWidth();
        int startHeight = targetView.getHeight();
        int endX = preViewInfo.getScreenX();
        int endY = preViewInfo.getScreenY();
        float endScaleX = (float) endWidth / startWidth;
        float endScaleY = (float) endHeight / startHeight;
        int[] screenLocation = new int[2];
        targetView.getLocationOnScreen(screenLocation);
        int startX = screenLocation[0];
        int startY = screenLocation[1];
        int translationX = endX - startX;
        int translationY = endY - startY;

        targetView.setPivotX(0);
        targetView.setPivotY(0);
        targetView.setVisibility(View.VISIBLE);
        ViewPropertyAnimator animator = targetView.animate();
        animator.setDuration(ANIMATION_DURATION)
                .scaleX(endScaleX)
                .scaleY(endScaleY)
                .translationX(translationX)
                .translationY(translationY)
                .start();
        if (listener != null) {
            animator.setListener(listener);
        }
    }

    public static void startBackgroundAlphaAnim(final View targetView, final ColorDrawable color, int...values) {
        if (values == null || values.length == 0) {
            values = new int[]{0, 255};
        }
        ObjectAnimator bgAnim = ObjectAnimator
                .ofInt(color, "alpha", values);
        bgAnim.setDuration(ANIMATION_DURATION);
        bgAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                targetView.setBackgroundDrawable(color);
            }
        });
        bgAnim.start();
    }

    public static ZoomAnimationUtils.ZoomInfo getZoomInfo(@NonNull View view) {
        int[] screenLocation = new int[2];
        view.getLocationOnScreen(screenLocation);
        return new ZoomInfo(screenLocation[0], screenLocation[1], view.getWidth(), view.getHeight());
    }

    public static class ZoomInfo implements Parcelable {
        int screenX;
        int screenY;
        int width;
        int height;

        public ZoomInfo(int screenX, int screenY, int width, int height) {
            this.screenX = screenX;
            this.screenY = screenY;
            this.width = width;
            this.height = height;
        }

        public int getScreenX() {
            return screenX;
        }

        public void setScreenX(int screenX) {
            this.screenX = screenX;
        }

        public int getScreenY() {
            return screenY;
        }

        public void setScreenY(int screenY) {
            this.screenY = screenY;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.screenX);
            dest.writeInt(this.screenY);
            dest.writeInt(this.width);
            dest.writeInt(this.height);
        }

        protected ZoomInfo(Parcel in) {
            this.screenX = in.readInt();
            this.screenY = in.readInt();
            this.width = in.readInt();
            this.height = in.readInt();
        }

        public static final Parcelable.Creator<ZoomInfo> CREATOR = new Parcelable.Creator<ZoomInfo>() {
            @Override
            public ZoomInfo createFromParcel(Parcel source) {
                return new ZoomInfo(source);
            }

            @Override
            public ZoomInfo[] newArray(int size) {
                return new ZoomInfo[size];
            }
        };
    }
}
