package com.github.rxjavaoperators.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.rxjavaoperators.R;
import com.github.rxjavaoperators.adapter.MainAdapter;
import com.github.rxjavaoperators.entity.Category;
import com.github.rxjavaoperators.entity.MarkedItem;
import com.github.rxjavaoperators.entity.Operator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainAdapter.DelegateItemSelected {

    private static final String TAG = "MainActivity";

    @BindView(R.id.rvOperators)
    RecyclerView recyclerOperators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        List<Operator> operators = getOperators();
        List<MarkedItem> result = getMarkedItems(operators);
        MainAdapter adapter = new MainAdapter(result, this);
        recyclerOperators.setLayoutManager(new LinearLayoutManager(this));
        recyclerOperators.setAdapter(adapter);
    }

    @NonNull
    private List<Operator> getOperators() {
        try {
            InputStream assetsStream = getAssets().open("operators.json");
            Reader reader = new InputStreamReader(assetsStream);
            Type type = new TypeToken<List<Operator>>() {
            }.getType();
            List<Operator> operators = new Gson().fromJson(reader, type);
            reader.close();
            assetsStream.close();
            return operators;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @NonNull
    private List<MarkedItem> getMarkedItems(List<Operator> operators) {
        List<MarkedItem> result = new ArrayList<>();
        List<Operator> categoryPart = new ArrayList<>();
        Collections.sort(operators, this::compareCategories);
        Category currentCategory = null;
        for (int i = 0; i < operators.size(); i++) {
            Operator item = operators.get(i);
            if (currentCategory == null) {
                currentCategory = item.getCategory();
                categoryPart.clear();
                categoryPart.add(item);
            } else {
                if (currentCategory.equals(item.getCategory())) {
                    categoryPart.add(item);
                    checkIfLastAddToResult(i, operators, result, categoryPart, currentCategory);
                } else {
                    result.add(currentCategory);
                    result.addAll(categoryPart);
                    currentCategory = item.getCategory();
                    categoryPart.clear();
                    categoryPart.add(item);
                    checkIfLastAddToResult(i, operators, result, categoryPart, currentCategory);
                }
            }
        }
        return result;
    }

    private void checkIfLastAddToResult(int i, List<Operator> operators, List<MarkedItem> result, List<Operator> categoryPart, Category currentCategory) {
        if (i == operators.size() - 1) {
            result.add(currentCategory);
            result.addAll(categoryPart);
        }
    }

    private int compareCategories(Operator o1, Operator o2) {
        return o1.getCategory().compareTo(o2.getCategory());
    }

    @Override
    public void onOperatorSelected(Operator item) {
        OperatorActivity.navigate(MainActivity.this, item);
    }
}
