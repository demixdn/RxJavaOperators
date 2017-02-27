package com.github.rxjavaoperators.rxbinding2extends;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;

import io.reactivex.Observable;

import static com.github.rxjavaoperators.rxbinding2extends.Preconditions.checkNotNull;


/**
 * Date: 24.02.2017
 * Time: 19:44
 *
 * @author Aleks Sander
 *         Project RxJavaOperators
 */

public class RxView {
    /**
     * Create an observable which emits on {@code view} click events. The emitted value is
     * view and should only be used as notification.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}.
     * Dispose to free this reference.
     * <p>
     * <em>Warning:</em> The created observable uses {@link View#setOnClickListener} to observe
     * clicks. Only one observable can be used for a view at a time.
     */
    @CheckResult
    @NonNull
    public static Observable<View> clicks(@NonNull View view) {
        checkNotNull(view, "view == null");
        return Observable.create(new ViewClickOnSubscribe(view));
    }


}
