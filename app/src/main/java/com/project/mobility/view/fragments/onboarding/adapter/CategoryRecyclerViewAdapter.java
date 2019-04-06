package com.project.mobility.view.fragments.onboarding.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.mobility.R;
import com.project.mobility.model.onboarding.category.Category;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder> {

    private List<Category> categories;
    private SelectionTracker<Long> selectionTracker;
    private List<Integer> categoryPositions;

    @Inject
    public CategoryRecyclerViewAdapter(List<Category> categories) {
        this.categories = categories;
        setHasStableIds(true);
    }

    public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
        this.selectionTracker = selectionTracker;
    }

    public void setSelectedCategoriesPosition(List<Integer> categoryPositions) {
        this.categoryPositions = categoryPositions;
    }

    public List<String> getSelectedCategoriesName() {
        List<String> categoryName = new ArrayList<>();
        if (categoryPositions != null) {
            for (int categoryPosition : categoryPositions) {
                categoryName.add(categories.get(categoryPosition).getName());
            }
        }

        return categoryName;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, int position) {
        holder.mItem = categories.get(position);
        holder.mContentView.setText(categories.get(position).getName());
        holder.bind(selectionTracker.isSelected((long) position));
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public Category mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = view.findViewById(R.id.content);
        }

        void bind(boolean isSelected) {
            mView.setActivated(isSelected);
        }

        public ItemDetailsLookup.ItemDetails<Long> getCategoryDetails() {
            return new CategoryItemDetail(getAdapterPosition(), getItemId());
        }

        @NotNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
