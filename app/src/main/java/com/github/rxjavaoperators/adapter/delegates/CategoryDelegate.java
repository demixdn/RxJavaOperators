package com.github.rxjavaoperators.adapter.delegates;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.rxjavaoperators.R;
import com.github.rxjavaoperators.entity.Category;
import com.github.rxjavaoperators.entity.MarkedItem;
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

public class CategoryDelegate extends AdapterDelegate<List<MarkedItem>> {


    public CategoryDelegate() {
        //empty
    }

    @Override
    protected boolean isForViewType(@NonNull List<MarkedItem> items, int position) {
        return items.get(position) instanceof Category;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<MarkedItem> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        CategoryViewHolder viewHolder = (CategoryViewHolder) holder;
        Category item = (Category) items.get(position);
        viewHolder.title.setText(item.getName());
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvCategoryItemTitle)
        TextView title;

        CategoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
