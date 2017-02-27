package com.github.rxjavaoperators.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.github.rxjavaoperators.adapter.delegates.CategoryDelegate;
import com.github.rxjavaoperators.adapter.delegates.OperatorDelegate;
import com.github.rxjavaoperators.entity.MarkedItem;
import com.github.rxjavaoperators.entity.Operator;
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager;

import java.util.List;

/**
 * Date: 22.02.2017
 * Time: 18:47
 *
 * @author Aleks Sander
 *         Project RxJavaOperators
 */

public class MainAdapter extends RecyclerView.Adapter {

    private final AdapterDelegatesManager<List<MarkedItem>> delegatesManager;
    private final List<MarkedItem> items;

    public MainAdapter(@NonNull List<MarkedItem> items, DelegateItemSelected itemSelected) {
        this.items = items;
        this.delegatesManager = new AdapterDelegatesManager<>();
        this.delegatesManager.addDelegate(new OperatorDelegate(itemSelected));
        this.delegatesManager.addDelegate(new CategoryDelegate());
    }

    @Override
    public int getItemViewType(int position) {
        return delegatesManager.getItemViewType(items, position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return delegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        delegatesManager.onBindViewHolder(items, position, holder);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        delegatesManager.onBindViewHolder(items, position, holder, payloads);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @FunctionalInterface
    public interface DelegateItemSelected {
        void onOperatorSelected(Operator item);
    }
}
