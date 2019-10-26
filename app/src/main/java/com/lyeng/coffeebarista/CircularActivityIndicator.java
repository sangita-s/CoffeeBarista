package com.lyeng.coffeebarista;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

public class CircularActivityIndicator extends View {
    private static final int DEFAULT_FG_COLOR = R.color.colorPrimary;
    private static final int DEFAULT_BG_COLOR = R.color.colorAccent;
    private Paint foregroundPaint;
    private Paint backgroundPaint;
    private double selectedAngle;
    private Path clipPath;

    public CircularActivityIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context pContext, AttributeSet pAttrs) {
        int def_angle = 45;

        backgroundPaint = new Paint();
        backgroundPaint.setColor(getResources().getColor(R.color.colorAccent));
        backgroundPaint.setStyle(Paint.Style.FILL);

        foregroundPaint = new Paint();
        foregroundPaint.setColor(getResources().getColor(R.color.colorPrimary));
        foregroundPaint.setStyle(Paint.Style.FILL);

        //Change in xml to update via this
        if(pAttrs != null){
            TypedArray array = pContext.obtainStyledAttributes(pAttrs, R.styleable.CircularActivityIndicator);
            setAngle((int) array.getInt(0, def_angle));
        }
    }

    //Can ve called from Main Activity directly
    public void setAngle(double angle) {
        selectedAngle = angle;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int circleSize = getWidth();
        if (getHeight() < circleSize) circleSize = getHeight();

        int horMargin = (getWidth() - circleSize) / 2;
        int verMargin = (getHeight() - circleSize) / 2;

        // create a clipPath the first time
        if (clipPath == null) {
            int clipWidth = (int) (circleSize * 0.90);

            int clipX = (getWidth() - clipWidth) / 2;
            int clipY = (getHeight() - clipWidth) / 2;

            clipPath = new Path();
            clipPath.addArc(
                    clipX,
                    clipY,
                    clipX + clipWidth,
                    clipY + clipWidth,
                    0, 360);
        }

        canvas.clipRect(0, 0, getWidth(), getHeight());
        canvas.clipPath(clipPath, Region.Op.DIFFERENCE);
        canvas.save();
        canvas.rotate(-90, getWidth() / 2, getHeight() / 2);


        canvas.drawArc(
                horMargin,
                verMargin,
                horMargin + circleSize,
                verMargin + circleSize,
                0, 360, true, backgroundPaint);

        canvas.drawArc(
                0,
                0,
                getWidth(),
                getHeight(),
                0,
                (float) selectedAngle,
                true,
                foregroundPaint
        );
        canvas.restore();
    }
}
