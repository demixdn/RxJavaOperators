package com.github.rxjavaoperators.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.github.rxjavaoperators.R;
import com.github.rxjavaoperators.entity.Operator;
import com.github.rxjavaoperators.rxbinding2extends.PrimaryClipChangedSubscribe;
import com.github.rxjavaoperators.ui.fragments.CreateOperatorFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class OperatorActivity extends AppCompatActivity implements ClipboardManager.OnPrimaryClipChangedListener {

    private static final String EXTRA_KEY_OPERATOR = "extra_key_operator";

    private Operator operator;

    public static void navigate(@NonNull Context context, @NonNull Operator operator) {
        Intent intent = new Intent(context, OperatorActivity.class);
        intent.putExtra(EXTRA_KEY_OPERATOR, operator);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator);

        if (getIntent() != null) {
            operator = (Operator) getIntent().getSerializableExtra(EXTRA_KEY_OPERATOR);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragmentSelector(operator))
                .commit();

        clipboardRx();
    }

    private CompositeDisposable compositeDisposable;

    private void clipboardRx() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(this);
        compositeDisposable = new CompositeDisposable();
        Observable<ClipData> clipDataObservable = Observable.create(new PrimaryClipChangedSubscribe(clipboardManager))
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
        Disposable subscribe = clipDataObservable.subscribe(new Consumer<ClipData>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull ClipData clipData) throws Exception {
                        //use clipData
                    }
                });
        compositeDisposable.add(subscribe);
    }

    @Override
    public void onDestroy() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.onDestroy();
    }

    private Fragment fragmentSelector(@NonNull Operator operator) {
        switch (operator.getName()) {
            case "Create":
                return CreateOperatorFragment.newInstance(operator);
            default:
//                return new AllOperatorsFragment();
                throw new IllegalArgumentException("Operator " + operator.getName() + " not implemented");
        }
    }

    @Override
    public void onPrimaryClipChanged() {

    }
}
