package com.se.music.behavior;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.se.music.R;

import java.lang.ref.WeakReference;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/5/18 23:53
 * 描述：
 * 备注：
 * =======================================================
 */
public class SongListInfoHeadBehavior extends CoordinatorLayout.Behavior<View> {
    private WeakReference<View> dependentView;
    private ArgbEvaluator argbEvaluator;
    private View title;

    public SongListInfoHeadBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        argbEvaluator = new ArgbEvaluator();
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        if (dependency != null && dependency.getId() == R.id.head_image) {
            dependentView = new WeakReference<>(dependency);
            return true;
        }
        return false;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (lp.height == CoordinatorLayout.LayoutParams.MATCH_PARENT) {
            child.layout(0, 0, parent.getWidth(), (int) (parent.getHeight() - getDependentViewCollapsedHeight()));
            return true;
        }
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    private float getDependentViewCollapsedHeight() {
        return getDependentView().getResources().getDimension(R.dimen.collapsed_header_height);
    }

    private View getDependentView() {
        return dependentView.get();
    }
}
