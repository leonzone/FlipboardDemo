package com.reiser.flipboarddemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by reiserx on 2017/10/14.
 * desc :
 */

public class FlipboardView extends View {
    boolean close;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    Camera camera = new Camera();
    /**
     * 右半部分翻起的角度
     */
    int degree1;
    /**
     * 中心旋转的角度
     */
    int degree2;
    /**
     * 上半部分翻起的角度
     */
    int degree3;

    AnimatorSet animatorSet = new AnimatorSet();

    public FlipboardView(Context context) {
        super(context);
    }

    public FlipboardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FlipboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flipboard);
        //第一组：右半部分翻起
        ObjectAnimator animator1 = ObjectAnimator.ofInt(this, "degree1", 0, -45);
        animator1.setDuration(1000);
        animator1.setStartDelay(750);
        animator1.setInterpolator(new LinearInterpolator());
        //第二组：旋转翻起
        ObjectAnimator animator2 = ObjectAnimator.ofInt(this, "degree2", 0, 270);
        animator2.setDuration(750);
        animator2.setInterpolator(new DecelerateInterpolator());
        animator2.setStartDelay(750);
        //第三组：上半部分翻起
        ObjectAnimator animator3 = ObjectAnimator.ofInt(this, "degree3", 0, 45);
        animator3.setDuration(1000);
        animator3.setInterpolator(new LinearInterpolator());
        animator3.setStartDelay(750);
        //第四组：还原
        PropertyValuesHolder holder1 = PropertyValuesHolder.ofInt("degree1", -45, 0);
        PropertyValuesHolder holder2 = PropertyValuesHolder.ofInt("degree3", 45, 0);
        ObjectAnimator animator4 = ObjectAnimator.ofPropertyValuesHolder(this, holder1, holder2);
        animator4.setDuration(750);
        animator4.setStartDelay(500);
        animator4.setInterpolator(new LinearInterpolator());

        animatorSet.playSequentially(animator1, animator2, animator3, animator4);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!close) {
                    degree2 = 0;
                    animatorSet.start();
                }
            }
        });

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        animatorSet.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animatorSet != null && animatorSet.isRunning()) {
            close = true;
            animatorSet.end();
        }
    }

    @SuppressWarnings("unused")
    public void setDegree2(int degree2) {
        this.degree2 = degree2;
        invalidate();
    }

    @SuppressWarnings("unused")
    public int getDegree1() {
        return degree1;
    }

    @SuppressWarnings("unused")
    public void setDegree1(int degree1) {
        this.degree1 = degree1;
        invalidate();
    }

    @SuppressWarnings("unused")
    public int getDegree3() {
        return degree3;
    }

    @SuppressWarnings("unused")
    public void setDegree3(int degree3) {
        this.degree3 = degree3;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int x = centerX - bitmapWidth / 2;
        int y = centerY - bitmapHeight / 2;

        //第一部分,此处牺牲了绘制效率，简化了代码逻辑
        canvas.save();
        camera.save();
        canvas.translate(centerX, centerY);
        canvas.rotate(-degree2);
        camera.rotateY(degree3);
        camera.applyToCanvas(canvas);
        canvas.clipRect(-centerX, -centerY, 0, centerY);
        canvas.rotate(degree2);
        canvas.translate(-centerX, -centerY);
        camera.restore();
        canvas.drawBitmap(bitmap, x, y, paint);
        canvas.restore();

        //第二部分
        canvas.save();
        camera.save();
        canvas.translate(centerX, centerY);
        canvas.rotate(-degree2);
        camera.rotateY(degree1);
        camera.applyToCanvas(canvas);
        canvas.clipRect(0, -centerY, centerX, centerY);
        canvas.rotate(degree2);
        canvas.translate(-centerX, -centerY);
        camera.restore();
        canvas.drawBitmap(bitmap, x, y, paint);
        canvas.restore();

    }
}
