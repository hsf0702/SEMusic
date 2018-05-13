package com.se.music.behavior;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.se.music.R;

import java.lang.ref.WeakReference;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/4/23 19:34
 * 描述：
 * 备注：
 * =======================================================
 */
public class HeaderFloatBehavior extends CoordinatorLayout.Behavior<View> {

    private WeakReference<View> dependentView;
    private ArgbEvaluator argbEvaluator;
    private View title;

    public HeaderFloatBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        argbEvaluator = new ArgbEvaluator();
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        if (dependency != null && dependency.getId() == R.id.head_image) {
            dependentView = new WeakReference<>(dependency);
            title = child.findViewById(R.id.hot_list_title);
            return true;
        }
        return false;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        Resources resources = getDependentView().getResources();
        final float progress = 1.f -
                Math.abs(dependency.getTranslationY() / (dependency.getHeight() - resources.getDimension(R.dimen.collapsed_header_height)));
        title.setAlpha((float) (1.0 - progress));

        return true;
    }

    private View getDependentView() {
        return dependentView.get();
    }
}
