package com.project.mobility.view.fragments.onboarding.keyprovider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.widget.RecyclerView;

public class CustomKeyProvider extends ItemKeyProvider<Long> {

    private RecyclerView recyclerView;

    public CustomKeyProvider(RecyclerView recyclerView) {
        super(SCOPE_MAPPED);
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public Long getKey(int position) {
        return recyclerView.getAdapter().getItemId(position);
    }

    @Override
    public int getPosition(@NonNull Long key) {
        RecyclerView.ViewHolder viewHolderForItemId = recyclerView.findViewHolderForItemId(key);
        return viewHolderForItemId.getLayoutPosition() >= 0 ? viewHolderForItemId.getLayoutPosition() : RecyclerView.NO_POSITION;
    }
}
