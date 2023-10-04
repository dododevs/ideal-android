package revolver.ideal.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;

import revolver.ideal.R;

public class CircularLayout extends FrameLayout {

    private final int mRadius;
    private final View mCenterView;
    private View mLastAddedView;

    private static final Interpolator itemInsertionInterpolator = new AccelerateDecelerateInterpolator();

    public CircularLayout(Context context) {
        this(context, null);
    }

    public CircularLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CircularLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.CircularLayout, defStyleAttr, defStyleRes);
        mRadius = a.getDimensionPixelSize(R.styleable.CircularLayout_radius, -1);

        final @LayoutRes int centerLayout =
                a.getResourceId(R.styleable.CircularLayout_centerLayout, 0);
        if (centerLayout != 0) {
            mCenterView = LayoutInflater.from(context).inflate(centerLayout, this, false);

            final LayoutParams lp = (LayoutParams) mCenterView.getLayoutParams();
            lp.gravity = Gravity.CENTER;
            addView(mCenterView, lp);
        } else {
            mCenterView = null;
        }
        a.recycle();
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        mLastAddedView = child;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        final int parentLeft = getPaddingLeft();
        final int parentTop = getPaddingTop();

        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child == mCenterView) {
                continue;
            }
            final ViewGroup.LayoutParams lp = child.getLayoutParams();
            final int childLeft = (int) Math.round(Math.cos(
                    Math.toRadians(45 * (i - 1))) * mRadius) + (bottom - top - child.getMeasuredHeight()) / 2;
            final int childTop = (int) Math.round(Math.sin(
                    Math.toRadians(45 * (i - 1))) * mRadius) + (right - left - child.getMeasuredWidth()) / 2;

            if (child == mLastAddedView) {
                child.setAlpha(0.f);
                child.setScaleX(0.5f);
                child.setScaleY(0.5f);
            }

            child.layout(parentLeft + childLeft, parentTop + childTop,
                    parentLeft + childLeft + lp.width, parentTop + childTop + lp.height);

            if (child == mLastAddedView) {
                child.animate()
                        .alpha(1.f)
                        .scaleX(1.f)
                        .scaleY(1.f)
                        .setDuration(100L)
                        .setInterpolator(itemInsertionInterpolator)
                        .start();
            }
        }
    }

    @Override
    public void removeView(View view) {
        view.animate().scaleX(0.5f).scaleY(0.5f)
                .alpha(0.f).setDuration(200L)
                .setInterpolator(itemInsertionInterpolator)
                .withEndAction(() -> super.removeView(view))
                .start();
    }

    public void setOnCenterViewClickListener(OnClickListener l) {
        if (mCenterView != null) {
            mCenterView.setFocusable(true);
            mCenterView.setClickable(true);
            mCenterView.setOnClickListener(l);
        }
    }
}
