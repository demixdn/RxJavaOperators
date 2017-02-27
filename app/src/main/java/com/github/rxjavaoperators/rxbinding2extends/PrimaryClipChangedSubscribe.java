package com.github.rxjavaoperators.rxbinding2extends;

import android.content.ClipData;
import android.content.ClipboardManager;

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

public final class PrimaryClipChangedSubscribe implements ObservableOnSubscribe<ClipData> {
    private final ClipboardManager clipboardManager;

    public PrimaryClipChangedSubscribe(ClipboardManager clipboardManager) {
        this.clipboardManager = clipboardManager;
    }

    @Override
    public void subscribe(ObservableEmitter<ClipData> emitter) throws Exception {
        verifyMainThread();

        ClipboardManager.OnPrimaryClipChangedListener changedListener = new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                if (!emitter.isDisposed()) {
                    emitter.onNext(clipboardManager.getPrimaryClip());
                }
            }
        };

        emitter.setDisposable(new MainThreadDisposable() {
            @Override
            protected void onDispose() {
                clipboardManager.removePrimaryClipChangedListener(changedListener);
            }
        });

        clipboardManager.addPrimaryClipChangedListener(changedListener);
    }
}
