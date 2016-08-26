package org.voiddog.lib.mvp.lce;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * 提供load content error 切换动画
 * Created by qigengxin on 16/8/25.
 */
public interface ILceAnimation {

    void showLoading(View loadingView, @NonNull View contentView, View errorView);

    void showError(View loadingView, @NonNull View contentView, View errorView);

    void showContent(View loadingView, @NonNull View contentView, View errorView);
}
