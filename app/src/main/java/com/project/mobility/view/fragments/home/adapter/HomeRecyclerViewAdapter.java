package com.project.mobility.view.fragments.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mobility.R;
import com.project.mobility.model.onboarding.category.Category;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.CategoriesViewHolder> {
    private List<Category> categories;
    private View.OnClickListener itemClickListener;

    @Inject
    public HomeRecyclerViewAdapter() {
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
        holder.categoryName.setText(categories.get(position).getName());
//        holder.circularView.setCircleText(categories.get(position).getName());
        holder.view.setOnClickListener(v -> itemClickListener.onClick(v));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoriesViewHolder extends RecyclerView.ViewHolder {
        public View view;
        @BindView(R.id.category_name) TextView categoryName;
//        @BindView(R.id.circular_view) CircularView circularView;

        public CategoriesViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}
