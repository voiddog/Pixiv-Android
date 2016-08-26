package org.voiddog.lib.mvp.lce;

import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * 默认的 lce 动画
 * Created by qigengxin on 16/8/25.
 */
public class DefaultLceAnimation implements ILceAnimation{
    @Override
    public void showLoading(View loadingView, @NonNull View contentView, View errorView) {
        // reset all and stop animation
        showView(loadingView);
        hideView(contentView);
        hideView(errorView);
    }

    @Override
    public void showError(View loadingView, @NonNull View contentView, View errorView) {
        hideView(loadingView);
        hideView(contentView);
        showView(errorView);
    }

    @Override
    public void showContent(View loadingView, @NonNull View contentView, View errorView) {
        hideView(loadingView);
        showView(contentView);
        hideView(errorView);
    }

    private static void showView(final View view){
        if(view == null){
            return;
        }

        view.clearAnimation();
        view.setVisibility(View.VISIBLE);
        float startAlpha = view.getAlpha();
        ValueAnimator animator = new ValueAnimator();
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                view.setAlpha(value);
            }
        });
        animator.setFloatValues(startAlpha, 1.0f);
        animator.start();
    }

    private static void hideView(final View view){
        if(view == null){
            return;
        }
        if(view.getVisibility() == View.GONE
                || view.getAlpha() == 0){
            view.setAlpha(0);
            view.setVisibility(View.GONE);
            return;
        }

        view.clearAnimation();
        float startAlpha = view.getAlpha();
        ValueAnimator animator = new ValueAnimator();
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                view.setAlpha(value);
                if(value <= 0){
                    view.setVisibility(View.GONE);
                }
            }
        });
        animator.setFloatValues(startAlpha, 0f);
        animator.start();
    }
}
