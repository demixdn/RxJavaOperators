package com.github.rxjavaoperators.ui.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.rxjavaoperators.R;
import com.github.rxjavaoperators.entity.Operator;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateOperatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateOperatorFragment extends Fragment {
    private static final String TAG = "CreateOperator";
    private static final String ARG_PARAM_OPERATOR = "arg_param_operator";
    @BindView(R.id.imageView)
    ImageView image;
    @BindView(R.id.editText)
    EditText etNumber;
    @BindView(R.id.btOnNext)
    Button btOnNext;
    @BindView(R.id.btOnCompleted)
    Button btOnCompleted;
    @BindView(R.id.btOnError)
    Button btOnError;
    @BindView(R.id.btSubscribe)
    Button btSubscribe;
    @BindView(R.id.viewLogs)
    TextView viewLogs;

    Disposable subscription;
    Observable<Integer> integerObservable;
    Operator operator;
    @BindView(R.id.textViewSubscribeTitle)
    TextView textViewSubscribeTitle;

    Unbinder unbinder;

    public CreateOperatorFragment() {
        // Required empty public constructor
    }

    public static CreateOperatorFragment newInstance(@NonNull Operator operator) {
        CreateOperatorFragment fragment = new CreateOperatorFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_OPERATOR, operator);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            operator = (Operator) getArguments().getSerializable(ARG_PARAM_OPERATOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_operator, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        initRxOperator();
    }

    private void initUI() {
        if (operator != null) {
            Picasso.with(getContext()).load(operator.getOfficialImageLink()).into(image);
            getActivity().setTitle(operator.getName());
        }
    }

    private void initRxOperator() {
        integerObservable = Observable.create(new IntegerObservableOnSubscribe(etNumber, btOnNext, btOnCompleted, btOnError));
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null)
            unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
        super.onDestroy();
    }

    @OnClick(R.id.btSubscribe)
    public void onClick() {
        btSubscribe.setEnabled(false);
        btSubscribe.setVisibility(View.GONE);
        textViewSubscribeTitle.setVisibility(View.GONE);
        subscribe();
    }

    private void subscribe() {
        subscription = integerObservable.subscribe(this::logNext, this::logError, this::logComplete);
    }

    private void logComplete() {
        String onComplete = "onComplete";
        Log.i(TAG, onComplete);
        showLogs(onComplete);
        disableViews();
    }

    private void showLogs(String log) {
        String logs = viewLogs.getText().toString();
        logs = log + '\n' + logs;
        viewLogs.setText(logs);
    }

    private void disableViews() {
        btOnCompleted.setEnabled(false);
        btOnError.setEnabled(false);
        btOnNext.setEnabled(false);
    }

    private void logError(@io.reactivex.annotations.NonNull Throwable throwable) {
        String msg = "onError - " + throwable.getMessage();
        Log.i(TAG, msg);
        showLogs(msg);
        disableViews();
    }

    private void logNext(@io.reactivex.annotations.NonNull Integer integer) {
        String onNext = String.format(Locale.getDefault(), "onNext(%d)", integer);
        Log.i(TAG, onNext);
        showLogs(onNext);
        etNumber.setText(String.valueOf(integer));
    }

    private static class IntegerObservableOnSubscribe implements ObservableOnSubscribe<Integer>, View.OnClickListener {

        private final EditText editText;
        private ObservableEmitter<Integer> emitter;

        IntegerObservableOnSubscribe(@NonNull EditText editText, @NonNull Button btOnNext, @NonNull Button btOnComplete, @NonNull Button btOnError) {
            this.editText = editText;
            btOnNext.setOnClickListener(this);
            btOnComplete.setOnClickListener(this);
            btOnError.setOnClickListener(this);
        }

        @Override
        public void subscribe(ObservableEmitter<Integer> e) throws Exception {
            this.emitter = e;
            Log.i(TAG, "subscribed ");
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btOnNext:
                    sendOnNext();
                    break;
                case R.id.btOnCompleted:
                    if (emitter != null) {
                        emitter.onComplete();
                    }
                    break;
                case R.id.btOnError:
                    if (emitter != null) {
                        emitter.onError(new IllegalArgumentException("On error button clicked"));
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Button not defined.");
            }
        }

        private void sendOnNext() {
            if (emitter != null) {
                Integer value;
                try {
                    value = Integer.decode(editText.getText().toString());
                } catch (NumberFormatException e) {
                    emitter.onError(e);
                    return;
                } catch (NullPointerException ne) {
                    emitter.onError(new NullPointerException("EditText is null"));
                    return;
                }
                value++;
                emitter.onNext(value);
            }
        }
    }
}
