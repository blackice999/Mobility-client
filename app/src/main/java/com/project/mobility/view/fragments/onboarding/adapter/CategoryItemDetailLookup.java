package com.project.mobility.view.fragments.onboarding.adapter;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryItemDetailLookup extends ItemDetailsLookup<Long> {

    private RecyclerView recyclerView;

    public CategoryItemDetailLookup(RecyclerView recyclerView) {
        super();
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (view != null) {
            return ((CategoryRecyclerViewAdapter.ViewHolder) recyclerView.getChildViewHolder(view)).getCategoryDetails();
        }

        return null;
    }
}
