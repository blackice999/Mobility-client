package com.project.mobility.view.fragments.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mobility.R;
import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.onboarding.category.Category;
import com.project.mobility.util.image.ImageLoader;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.CategoriesViewHolder> {

    @Inject ImageLoader imageLoader;
    @Inject Context context;

    private List<Category> categories;
    private View.OnClickListener itemClickListener;

    @Inject
    public HomeRecyclerViewAdapter() {
        Injection.inject(this);
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void setItemClickListener(View.OnClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public long getItemIdByPosition(int position) {
        return categories.get(position).getId();
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_category_list, parent, false);
        return new CategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        holder.bind(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Injection.closeScope(this);
    }

    public class CategoriesViewHolder extends RecyclerView.ViewHolder {
        public View view;

        @BindView(R.id.category_name) TextView categoryName;
        @BindView(R.id.category_image) AppCompatImageView categoryImage;

        public CategoriesViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }

        public void bind(Category category) {
            categoryName.setText(category.getName());
            imageLoader.load(context, getImageDrawableFromName(category.getImageName()), categoryImage);
            view.setOnClickListener(v -> itemClickListener.onClick(v));
        }

        private int getImageDrawableFromName(String imageName) {
            return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        }
    }
}
