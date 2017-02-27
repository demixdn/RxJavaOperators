package com.github.rxjavaoperators.rxbinding2extends;

import android.view.View;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

/**
 * Date: 24.02.2017
 * Time: 19:33
 *
 * @author Aleks Sander
 *         Project RxJavaOperators
 */

final class ViewClickOnSubscribe implements ObservableOnSubscribe<View> {
    private final View view;

    ViewClickOnSubscribe(View view) {
        this.view = view;
    }

    @Override
    public void subscribe(ObservableEmitter<View> emitter) throws Exception {
        verifyMainThread();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!emitter.isDisposed()) {
                    emitter.onNext(view);
                }
            }
        };

        emitter.setDisposable(new MainThreadDisposable() {
            @Override
            protected void onDispose() {
                view.setOnClickListener(null);
            }
        });

        view.setOnClickListener(listener);
    }
}
