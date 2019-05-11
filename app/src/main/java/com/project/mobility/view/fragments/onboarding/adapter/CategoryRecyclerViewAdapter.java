package com.project.mobility.view.fragments.onboarding.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mobility.R;
import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.onboarding.category.Category;
import com.project.mobility.util.image.ImageLoader;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder> {

    @Inject ImageLoader imageLoader;
    @Inject Context context;

    private List<Category> categories;
    private SelectionTracker<Long> selectionTracker;
    private List<Integer> categoryPositions;

    @Inject
    public CategoryRecyclerViewAdapter() {
        Injection.inject(this);
        setHasStableIds(true);
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_category_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, int position) {
        holder.mItem = categories.get(position);
        holder.categoryName.setText(categories.get(position).getName());
        holder.bind(selectionTracker.isSelected((long) position));
        imageLoader.load(context, getImageDrawableFromName(categories.get(position).getImageName()), holder.categoryImage);
    }

    private int getImageDrawableFromName(String imageName) {
        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
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
        @BindView(R.id.category_name) AppCompatTextView categoryName;
        @BindView(R.id.category_image) AppCompatImageView categoryImage;

        public Category mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
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
            return super.toString() + " '" + categoryName.getText() + "'";
        }
    }
}
