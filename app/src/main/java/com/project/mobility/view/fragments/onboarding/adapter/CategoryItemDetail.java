package com.project.mobility.view.fragments.onboarding.adapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;

public class CategoryItemDetail extends ItemDetailsLookup.ItemDetails<Long> {
    private int position;
    private Long key;

    public CategoryItemDetail(int position, Long key) {
        this.position = position;
        this.key = key;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Nullable
    @Override
    public Long getSelectionKey() {
        return key;
    }
}
