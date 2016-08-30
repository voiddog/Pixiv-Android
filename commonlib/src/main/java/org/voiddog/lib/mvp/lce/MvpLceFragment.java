/*
 * Copyright 2015 Hannes Dorfmann.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.voiddog.lib.mvp.lce;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import org.voiddog.lib.mvp.MvpFragment;
import org.voiddog.lib.mvp.MvpPresenter;

/**
 * A {@link MvpFragment} that implements {@link MvpLceView} which gives you 3 options:
 * <ul>
 * <li>Display a loading view: A view with <b>R.id.loadingView</b> must be specified in your
 * inflated xml layout</li>
 * <li>Display a error view: A <b>TextView</b> with <b>R.id.errorView</b> must be declared in your
 * inflated xml layout</li>
 * <li>Display content view: A view with <b>R.id.contentView</b> must be specified in your
 * inflated
 * xml layout</li>
 * </ul>
 *
 * @param <M> The underlying data model that will be displayed with this view
 * @param <V> The View interface that must be implemented by this view. You can use {@link
 * MvpLceView}, but if you want to add more methods you have to provide your own view interface
 * that
 * extends from {@link MvpLceView}
 * @param <P> The type of the Presenter. Must extend from {@link MvpPresenter}
 * @author Hannes Dorfmann
 * @since 1.0.0
 */
public abstract class MvpLceFragment<M, V extends MvpLceView<M>, P extends MvpPresenter<V>>
        extends MvpFragment<V, P> implements MvpLceView<M> {

    protected View loadingView;
    protected View contentView;
    protected View errorView;
    protected ILceAnimation lceAnimation;

    @CallSuper @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadingView = createLoadingView();
        contentView = createContentView();
        errorView = createErrorView();
        lceAnimation = createLceAnimation();

        if (contentView == null) {
            throw new NullPointerException("content view must not be null, return at createContentView");
        }

        if(lceAnimation == null){
            throw new NullPointerException("LceAnimation should not be null");
        }

        if(errorView != null){
            errorView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    onErrorViewClicked();
                }
            });
        }
    }


    @Override public void showLoading(boolean showLoading) {
        if (showLoading) {
            animateLoadingViewIn();
        }
        // otherwise the pull to refresh widget will already display a loading animation
    }

    /**
     * Override this method if you want to provide your own animation for showing the loading view
     */
    protected void animateLoadingViewIn() {
        lceAnimation.showLoading(loadingView, contentView, errorView);
    }

    @Override public void showContent() {
        animateContentViewIn();
    }

    /**
     * Called to animate from loading view to content view
     */
    protected void animateContentViewIn() {
        lceAnimation.showContent(loadingView, contentView, errorView);
    }

    /**
     * Get the error message for a certain Exception that will be shown on {@link
     * #showError(Throwable, boolean)}
     */
    protected abstract String getErrorMessage(Throwable e, boolean pullToRefresh);

    /**
     * get the Loading View can be null
     * @return
     */
    protected abstract View createLoadingView();

    /**
     * get the Content View should not be null
     * @return
     */
    protected abstract  View createContentView();

    /**
     * get the error View should not be null
     * @return
     */
    protected abstract View createErrorView();

    /**
     * 返回 load content error 切换动画
     * @return
     */
    protected ILceAnimation createLceAnimation(){
        return new DefaultLceAnimation();
    }

    /**
     * 显示页面错误
     * @param msg
     */
    protected abstract void showPageError(String msg);

    /**
     * The default behaviour is to display a toast message as light error (i.e. pull-to-refresh
     * error).
     * Override this method if you want to display the light error in another way (like crouton).
     */
    protected void showLightError(String msg) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called if the error view has been clicked. To disable clicking on the errorView use
     * <code>errorView.setClickable(false)</code>
     */
    protected void onErrorViewClicked() {
        loadData(true);
    }

    @Override public void showError(Throwable e, boolean showPageError) {

        String errorMsg = getErrorMessage(e, showPageError);

        if (showPageError) {
            showPageError(errorMsg);
            animateErrorViewIn();
        } else {
            showLightError(errorMsg);
        }
    }

    /**
     * Animates the error view in (instead of displaying content view / loading view)
     */
    protected void animateErrorViewIn() {
        lceAnimation.showError(loadingView, contentView, errorView);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        loadingView = null;
        contentView = null;
        errorView = null;
    }
}
