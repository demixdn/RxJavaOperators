package com.github.rxjavaoperators.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.github.rxjavaoperators.R;
import com.github.rxjavaoperators.entity.Operator;
import com.github.rxjavaoperators.ui.fragments.CreateOperatorFragment;

public class OperatorActivity extends AppCompatActivity {

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
}
