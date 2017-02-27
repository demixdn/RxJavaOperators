package com.github.rxjavaoperators.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.rxjavaoperators.R;
import com.github.rxjavaoperators.rxbinding2extends.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllOperatorsFragment extends Fragment {

    private static final String TAG = "AllOperators";

    Unbinder unbinder;

    CompositeDisposable compositeDisposable;
    @BindView(R.id.btObservable)
    Button btObservable;

    public AllOperatorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_operators, container, false);
        unbinder = ButterKnife.bind(this, view);
        compositeDisposable = new CompositeDisposable();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observableRegistration();
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.onDestroyView();
    }

    @OnClick({R.id.btFlowable, R.id.btSingle, R.id.btCompletable, R.id.btMaybe})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btFlowable:
                showFlowable();
                break;
            case R.id.btSingle:
                showSingle();
                break;
            case R.id.btCompletable:
                showCompletable();
                break;
            case R.id.btMaybe:
                showMaybe();
                break;
        }
    }

    /**
     * <h3>When to use Observable</h3>
     * <ul><li>You have a flow of no more than 1000 elements at its longest: i.e., you have so
     * few elements over time that there is practically no chance for OOME in your application.</li>
     * <li>You deal with GUI events such as mouse moves or touch events: these can rarely be
     * backpressured reasonably and aren't that frequent. You may be able to handle an element
     * frequency of 1000 Hz or less with <code>Observable</code> but consider
     * using sampling/debouncing anyway.</li>
     * <li>Your flow is essentially synchronous but your platform doesn't support Java Streams or you
     * miss features from it. Using Observable has lower overhead in general
     * than <code>Flowable</code>.<i>(You could also consider IxJava which is optimized for
     * Iterable flows supporting Java 6+).</i></li></ul>
     */
    private void observableRegistration() {
        Observable<View> viewClicked = RxView.clicks(btObservable)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
        compositeDisposable.add(viewClicked.subscribe(view -> Log.i(TAG, "onNext: " + view.getId()), throwable -> Log.i(TAG, "onError: "+throwable.getMessage())));
    }

    /**
     * <h3>When to use Flowable</h3>
     * <ul><li>Dealing with 10k+ of elements that are generated in some fashion somewhere and thus
     * the chain can tell the source to limit the amount it generates.</li>
     * <li>Reading (parsing) files from disk is inherently blocking and pull-based which works well
     * with backpressure as you control, for example, how many lines you read from this for
     * a specified request amount).</li>
     * <li>Reading from a database through JDBC is also blocking and pull-based and is controlled
     * by you by calling <code>ResultSet.next()</code> for likely each downstream request.</li>
     * <li>Network (Streaming) IO where either the network helps or the protocol used supports
     * requesting some logical amount.</li>
     * <li>Many blocking and/or pull-based data sources which may eventually get a non-blocking
     * reactive API/driver in the future.</li></ul>
     */
    private void showFlowable() {

    }

    /**
     * <h3>Single</h3>
     * <p>The 2.x <code>Single</code> reactive base type, which can emit a
     * single <code>onSuccess</code> or <code>onError</code> has been redesigned from scratch.
     * Its architecture now derives from the Reactive-Streams design.
     * Its consumer type (<code>rx.Single.SingleSubscriber&lt;T&gt;</code>) has been changed
     * from being a class that accepts <code>rx.Subscription</code> resources to be an interface
     * <code>io.reactivex.SingleObserver&lt;T&gt;</code> that has only 3 methods:</p>
     * <pre><code>
     * interface SingleObserver<T> {
     *     void onSubscribe(Disposable d);
     *     void onSuccess(T value);
     *     void onError(Throwable error);
     * }</code></pre>
     * <p>and follows the protocol <code>onSubscribe (onSuccess | onError)?</code>.</p>
     */
    private void showSingle() {

    }

    /**
     * <h3>Completable</h3>
     * <p>The <code>Completable</code> type remains largely the same. It was already designed along
     * the Reactive-Streams style for 1.x so no user-level changes there.</p>
     * <p>Similar to the naming changes, <code>rx.Completable.CompletableSubscriber</code>
     * has become <code>io.reactivex.CompletableObserver</code> with <code>onSubscribe(Disposable)</code>:</p>
     * <pre><code>
     * CompletableObserver{
     *     void onSubscribe(Disposable d);
     *     void onComplete();
     *     void onError(Throwable error);
     * }</code></pre>
     * <p>and still follows the protocol <code>onSubscribe (onComplete | onError)?</code>.</p>
     */
    private void showCompletable() {

    }

    /**
     * <h3>Maybe</h3>
     * <p>RxJava 2.0.0-RC2 introduced a new base reactive type called <code>Maybe</code>.
     * Conceptually, it is a union of <code>Single</code> and <code>Completable</code> providing
     * the means to capture an emission pattern where there could be 0 or 1 item or an error
     * signalled by some reactive source.</p>
     * <p>The <code>Maybe</code> class is accompanied by <code>MaybeSource</code> as its base
     * interface type, <code>MaybeObserver&lt;T&gt;</code> as its signal-receiving interface
     * and follows the protocol <code>onSubscribe (onSuccess | onError | onComplete)?</code>.
     * Because there could be at most 1 element emitted, the <code>Maybe</code> type has no notion
     * of backpressure (because there is no buffer bloat possible as with unknown length
     * <code>Flowable</code>s or <code>Observable</code>s.</p>
     * <p>This means that an invocation of <code>onSubscribe(Disposable)</code> is potentially
     * followed by one of the other <code>onXXX</code> methods. Unlike <code>Flowable</code>,
     * if there is only a single value to be signalled, only <code>onSuccess</code> is called
     * and <code>onComplete</code> is not.</p>
     * <p>Working with this new base reactive type is practically the same as the others as
     * it offers a modest subset of the <code>Flowable</code> operators that make sense
     * with a 0 or 1 item sequence.</p>
     */
    private void showMaybe() {

    }
}
