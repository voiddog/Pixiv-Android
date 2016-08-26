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

package org.voiddog.lib.mvp.delegate;

import android.content.Intent;
import android.os.Bundle;

import org.voiddog.lib.mvp.MvpPresenter;
import org.voiddog.lib.mvp.MvpView;


/**
 * The concrete implementation of {@link}
 *
 * @param <V> The type of {@link MvpView}
 * @param <P> The type of {@link MvpPresenter}
 * @author Hannes Dorfmann
 * @see ActivityMvpDelegate
 * @since 1.1.0
 */
public class ActivityMvpDelegateImpl<V extends MvpView, P extends MvpPresenter<V>>
    implements ActivityMvpDelegate {

  protected MvpInternalDelegate<V, P> internalDelegate;
  protected ActivityMvpDelegateCallback<V, P> delegateCallback;

  public ActivityMvpDelegateImpl(ActivityMvpDelegateCallback<V, P> delegateCallback) {
    if (delegateCallback == null) {
      throw new NullPointerException("MvpDelegateCallback is null!");
    }
    this.delegateCallback = delegateCallback;
  }

  /**
   * Get the internal delegate.
   */
  protected MvpInternalDelegate<V, P> getInternalDelegate() {
    if (internalDelegate == null) {
      internalDelegate = new MvpInternalDelegate<>(delegateCallback);
    }

    return internalDelegate;
  }

  @Override
  public void onCreate(Bundle bundle) {

    ActivityMvpNonConfigurationInstances<V, P> nci =
        (ActivityMvpNonConfigurationInstances<V, P>) delegateCallback.getLastCustomNonConfigurationInstance();

    if (nci != null && nci.presenter != null) {
      delegateCallback.setPresenter(nci.presenter);
    } else {
      getInternalDelegate().createPresenter();
    }

    getInternalDelegate().attachView();
  }

  @Override
  public void onNewIntent(Intent intent) {
    ActivityMvpNonConfigurationInstances<V, P> nci =
            (ActivityMvpNonConfigurationInstances<V, P>) delegateCallback.getLastCustomNonConfigurationInstance();

    if (nci != null && nci.presenter != null) {
      delegateCallback.setPresenter(nci.presenter);
    } else {
      getInternalDelegate().createPresenter();
    }

    getInternalDelegate().attachView();
  }

  @Override
  public void onDestroy() {
    getInternalDelegate().detachView();
  }

  @Override
  public void onPause() {
    P presenter = delegateCallback.getPresenter();
    if (presenter != null) {
      presenter.onPause();
    }
  }

  @Override
  public void onResume() {
    P presenter = delegateCallback.getPresenter();
    if (presenter != null) {
      presenter.onResume();
    }
  }

  @Override
  public void onPostResume() {
    P presenter = delegateCallback.getPresenter();
    if (presenter != null) {
      presenter.onPostResume();
    }
  }


  @Override
  public void onStart() {

  }

  @Override
  public void onStop() {
    P presenter = delegateCallback.getPresenter();
    if (presenter != null) {
      presenter.onStop();
    }
  }

  @Override
  public void onRestart() {

  }

  @Override
  public void onContentChanged() {

  }

  @Override
  public void onSaveInstanceState(Bundle outState) {

  }

  @Override
  public void onPostCreate(Bundle savedInstanceState) {

  }

  @Override
  public Object onRetainCustomNonConfigurationInstance() {

    P presenter = delegateCallback.shouldInstanceBeRetained() ? delegateCallback.getPresenter() : null;
    Object nonMosbyConfiguraionInstance =
        delegateCallback.onRetainNonMosbyCustomNonConfigurationInstance();

    if (presenter == null && nonMosbyConfiguraionInstance == null) {
      return null;
    }

    return new ActivityMvpNonConfigurationInstances<>(presenter, nonMosbyConfiguraionInstance);
  }

  @Override
  public Object getNonMosbyLastCustomNonConfigurationInstance() {
    ActivityMvpNonConfigurationInstances last =
        (ActivityMvpNonConfigurationInstances) delegateCallback.getLastCustomNonConfigurationInstance();
    return last == null ? null : last.nonMosbyCustomConfigurationInstance;
  }
}
