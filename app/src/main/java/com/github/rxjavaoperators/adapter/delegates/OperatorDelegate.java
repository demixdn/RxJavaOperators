package com.github.rxjavaoperators.adapter.delegates;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.rxjavaoperators.R;
import com.github.rxjavaoperators.adapter.MainAdapter;
import com.github.rxjavaoperators.entity.MarkedItem;
import com.github.rxjavaoperators.entity.Operator;
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date: 20.02.2017
 * Time: 19:56
 *
 * @author Aleks Sander
 *         Project RxJavaOperators
 */

public class OperatorDelegate extends AdapterDelegate<List<MarkedItem>> {


    private final MainAdapter.DelegateItemSelected itemSelected;

    public OperatorDelegate(MainAdapter.DelegateItemSelected itemSelected) {
        this.itemSelected = itemSelected;
    }

    @Override
    protected boolean isForViewType(@NonNull List<MarkedItem> items, int position) {
        return items.get(position) instanceof Operator;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new OperatorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_operator, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<MarkedItem> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        OperatorViewHolder viewHolder = (OperatorViewHolder) holder;
        Operator operator = (Operator) items.get(position);
        viewHolder.title.setText(operator.getName());
        viewHolder.description.setText(operator.getDescription());
        viewHolder.itemView.setTag(operator);
        viewHolder.itemView.setOnClickListener(this::onItemClicked);
    }

    private void onItemClicked(View v) {
        itemSelected.onOperatorSelected((Operator) v.getTag());
    }

    static class OperatorViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvOperatorItemTitle)
        TextView title;
        @BindView(R.id.tvOperatorItemDescription)
        TextView description;

        OperatorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
