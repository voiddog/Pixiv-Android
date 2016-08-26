/*
 *  Copyright 2015 Hannes Dorfmann.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.voiddog.lib.mvp.lce;

import org.voiddog.lib.mvp.MvpBasePresenter;
import org.voiddog.lib.mvp.MvpPresenter;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A presenter for RxJava, that assumes that only one Observable is subscribed by this presenter.
 * The idea is, that you make your (chain of) Observable and pass it to {@link
 * #subscribe(Observable, boolean)}. The presenter internally subscribes himself as Subscriber to
 * the
 * observable
 * (which executes the observable).
 *
 * @author Hannes Dorfmann
 * @since 1.0.0
 */
public abstract class MvpLceRxPresenter<M, V extends MvpLceView<M>>
        extends MvpBasePresenter<V> implements MvpPresenter<V> {

    protected Subscriber<M> subscriber;

    /**
     * Unsubscribes the subscriber and set it to null
     */
    protected void unsubscribe() {
        if (subscriber != null && !subscriber.isUnsubscribed()) {
            subscriber.unsubscribe();
        }

        subscriber = null;
    }

    /**
     * Subscribes the presenter himself as subscriber on the observable
     *
     * @param observable The observable to subscribe
     * @param pullToRefresh Pull to refresh?
     */
    public void subscribe(Observable<M> observable, final boolean pullToRefresh) {

        if (isViewAttached()) {
            getView().showLoading(pullToRefresh);
        }

        unsubscribe();

        subscriber = new Subscriber<M>() {
            private boolean ptr = pullToRefresh;

            @Override public void onCompleted() {
                MvpLceRxPresenter.this.onCompleted();
            }

            @Override public void onError(Throwable e) {
                MvpLceRxPresenter.this.onError(e, ptr);
            }

            @Override public void onNext(M m) {
                MvpLceRxPresenter.this.onNext(m, ptr);
            }
        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    protected void onCompleted() {
        if (isViewAttached()) {
            getView().showContent();
        }
        unsubscribe();
    }

    protected void onError(Throwable e, boolean pullToRefresh) {
        if (isViewAttached()) {
            getView().showError(e, pullToRefresh);
        }
        unsubscribe();
    }

    protected void onNext(M data, boolean pullToRefresh) {
        if (isViewAttached()) {
            if(pullToRefresh){
                getView().setData(data);
            }
            else{
                getView().addData(data);
            }
        }
    }

    @Override public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            unsubscribe();
        }
    }
}
