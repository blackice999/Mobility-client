package com.project.mobility.view.fragments.onboarding.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.mobility.R;
import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.onboarding.category.Category;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder> {

    @Inject Context context;

    private List<Category> categories;
    private SelectionTracker<Long> selectionTracker;
    private List<Integer> categoryPositions;
    private int i;

    @Inject
    public CategoryRecyclerViewAdapter(List<Category> categories) {
        Injection.inject(this);
        this.categories = categories;
        setHasStableIds(true);
    }

    public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
        this.selectionTracker = selectionTracker;
    }

    public void setSelectedCategoriesPosition(List<Integer> categoryPositions) {
        this.categoryPositions = categoryPositions;
    }

    public void shuffleList() {
        i = 0;
        Collections.shuffle(categories);
        notifyItemRangeChanged(0, getItemCount());
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
        holder.contentView.setText(categories.get(position).getName());

        int resourceId = context.getResources().getIdentifier(categories.get(position).getImage(), "drawable", context.getPackageName());
        holder.imageView.setImageResource(resourceId);
        holder.bind(selectionTracker.isSelected(categories.get(position).getId()));
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
        @BindView(R.id.content) TextView contentView;
        @BindView(R.id.image_category) ImageView imageView;

        public final View mView;
        public Category mItem;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
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
            return super.toString() + " '" + contentView.getText() + "'";
        }
    }
}
