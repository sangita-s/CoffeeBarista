package com.lyeng.coffeebarista;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class CoffeeCaffeineView extends View {
    public static final int EDIT_MODE_CONSTANT = 3;
    private float mOutlineWidth;
    private float mShapeSize;
    private float mSpacing;
    private Rect[] mModuleRectangles;
    private int mOutlineColor;
    private Paint mPaintOutline;
    private int mFillColor;
    private Paint mPaintFill;
    private float mRadius;
    private int mMaxHorizontalModules;

    public boolean[] getCaffeineLevelCup() {
        return mCaffeineLevelCup;
    }
    public void setCaffeineLevelCup(boolean[] pCaffeineLevelCup) {
        mCaffeineLevelCup = pCaffeineLevelCup;
    }
    private boolean[] mCaffeineLevelCup;

    public CoffeeCaffeineView(Context context) {
        super(context);
        init(null, 0);
    }

    public CoffeeCaffeineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CoffeeCaffeineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        if (isInEditMode()) {
            setupEditModeValues();
        }
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CoffeeCaffeineView, defStyle, 0);

//        mExampleString = a.getString(
//                R.styleable.CoffeeCaffeineView_exampleString);
//        mExampleColor = a.getColor(
//                R.styleable.CoffeeCaffeineView_exampleColor,
//                mExampleColor);
//        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
//        // values that should fall on pixel boundaries.
//        mExampleDimension = a.getDimension(
//                R.styleable.CoffeeCaffeineView_exampleDimension,
//                mExampleDimension);
//
//        if (a.hasValue(R.styleable.CoffeeCaffeineView_exampleDrawable)) {
//            mExampleDrawable = a.getDrawable(
//                    R.styleable.CoffeeCaffeineView_exampleDrawable);
//            mExampleDrawable.setCallback(this);
//        }

        a.recycle();

//        // Set up a default TextPaint object
//        mTextPaint = new TextPaint();
//        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
//        mTextPaint.setTextAlign(Paint.Align.LEFT);
//
//        // Update TextPaint and text measurements from attributes
//        invalidateTextPaintAndMeasurements();

        mOutlineWidth = 3f;
        mShapeSize = 72f;
        mSpacing = 10f;
        mRadius = (mShapeSize - mOutlineWidth) / 2;

        mOutlineColor = Color.BLACK;
        mPaintOutline = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintOutline.setStyle(Paint.Style.STROKE);
        mPaintOutline.setStrokeWidth(mOutlineWidth);
        mPaintOutline.setColor(mOutlineColor);

        mFillColor = getContext().getResources().getColor(R.color.coffeeBrown);
        mPaintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintFill.setStyle(Paint.Style.FILL);
        mPaintFill.setColor(mFillColor);
    }

    private void setupEditModeValues() {
        boolean[] exampleModuleValues = new boolean[EDIT_MODE_CONSTANT];
        int middle = EDIT_MODE_CONSTANT / 2;
        for (int i = 0; i < middle; i++)
            exampleModuleValues[i] = true;
        setCaffeineLevelCup(exampleModuleValues);
    }

    private void setupModuleRectangles(int width) {
        int availableWidth = width - getPaddingRight() - getPaddingLeft();
        int horModulesThatCanFit = (int)(availableWidth / (mShapeSize + mSpacing));
        int mMaxHorModules = Math.min(horModulesThatCanFit, mCaffeineLevelCup.length);

        mModuleRectangles = new Rect[mCaffeineLevelCup.length];
        for (int moduleIndex = 0; moduleIndex < mModuleRectangles.length; moduleIndex++) {
            int col = moduleIndex % mMaxHorModules;
            int row = moduleIndex / mMaxHorModules;
            //Left Edge
            int x = getPaddingLeft() + (int) (col * (mShapeSize + mShapeSize));
            //Top Edge
            int y = getPaddingTop() + (int) (row * (mShapeSize + mShapeSize));
            mModuleRectangles[moduleIndex] = new Rect(x, y, x + (int) mShapeSize, y + (int) mShapeSize);
        }
    }

    //For padding and paging
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desitedWidth = 0;
        int desiredHeight = 0;

        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        int availableWidth = specWidth - getPaddingLeft() - getPaddingRight();
        int horizontalModulesThatCanFit = (int) (availableWidth / (mShapeSize + mSpacing));
        mMaxHorizontalModules = Math.min(horizontalModulesThatCanFit, mCaffeineLevelCup.length);

//        desitedWidth = (int) ((mCaffeineLevelCup.length * (mShapeSize + mSpacing)) - mSpacing);
        desitedWidth = (int) ((mMaxHorizontalModules * (mShapeSize + mSpacing)) - mSpacing);
        desitedWidth += getPaddingLeft() + getPaddingRight();

        int rows = (mCaffeineLevelCup.length - 1) / mMaxHorizontalModules + 1;
        desiredHeight = (int) ((rows * (mShapeSize + mSpacing)) - mSpacing);
        desiredHeight += getPaddingTop() + getPaddingBottom();

        int width = resolveSizeAndState(desitedWidth, widthMeasureSpec, 0);
        int height = resolveSizeAndState(desiredHeight, heightMeasureSpec, 0);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        setupModuleRectangles(w);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int mI = 0; mI < mModuleRectangles.length; mI++) {
            float x = mModuleRectangles[mI].centerX();
            float y = mModuleRectangles[mI].centerY();

            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.ic_coffee_brown);

            if (mCaffeineLevelCup[mI]) {
                canvas.drawCircle(x, y, mRadius, mPaintFill);

//                canvas.drawBitmap(bitmap,x,y,mPaintFill);
            }
            canvas.drawCircle(x, y, mRadius, mPaintOutline);

        }

    }
}
