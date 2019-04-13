package com.project.mobility.view.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mobility.R;
import com.project.mobility.di.injection.Injection;
import com.project.mobility.view.activities.product.ProductsActivity;
import com.project.mobility.view.fragments.home.adapter.HomeRecyclerViewAdapter;
import com.project.mobility.viewmodel.main.home.HomeViewModel;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {
    private static final int LIST_COLUMN_COUNT = 2;
    @BindView(R.id.categories_list) RecyclerView recyclerView;

    @Inject HomeRecyclerViewAdapter homeRecyclerViewAdapter;

    private HomeViewModel homeViewModel;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injection.inject(this);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        ButterKnife.bind(this, view);
        setupList();
        return view;
    }

    private void setupList() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), LIST_COLUMN_COUNT));
        homeViewModel.getCategories().observe(this, categories -> {
            homeRecyclerViewAdapter.setCategories(categories);
            recyclerView.setAdapter(homeRecyclerViewAdapter);
        });

        homeRecyclerViewAdapter.setItemClickListener(v -> {
            int position = recyclerView.indexOfChild(v);
            Intent intent = new Intent(getActivity(), ProductsActivity.class);
            intent.putExtra(ProductsActivity.KEY_CATEGORY_ID, (int) homeRecyclerViewAdapter.getItemIdByPosition(position));
            startActivity(intent);
        });
    }
}
