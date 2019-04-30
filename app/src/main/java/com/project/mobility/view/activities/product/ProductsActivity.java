package com.project.mobility.view.activities.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mobility.R;
import com.project.mobility.model.product.Product;
import com.project.mobility.view.activities.product.adapter.ProductsRecyclerViewAdapter;
import com.project.mobility.view.activities.product.detail.ProductDetailActivity;
import com.project.mobility.viewmodel.product.ProductsViewModel;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ProductsActivity extends AppCompatActivity implements ProductsRecyclerViewAdapter.AddToCartListener {
    public static final String KEY_CATEGORY_ID = "category_item_id";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.products_list) RecyclerView productsRecyclerView;
    @BindView(R.id.progress_bar) ContentLoadingProgressBar progressBar;
    @BindView(R.id.textview_error_loading) AppCompatTextView errorLoadingDataTextView;

    private ProductsViewModel productsViewModel;
    private ProductsRecyclerViewAdapter productsRecyclerViewAdapter;
    private int pageCount;
    private boolean loading = true;
    int pastVisiblesItems;
    int visibleItemCount;
    int totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 2);

        productsRecyclerViewAdapter = new ProductsRecyclerViewAdapter(this);
        productsRecyclerView.setLayoutManager(linearLayoutManager);
        productsRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        productsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        productsViewModel = ViewModelProviders.of(this).get(ProductsViewModel.class);
        productsViewModel.setCategoryId(getIntent().getIntExtra(KEY_CATEGORY_ID, 0));

        productsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            Timber.v("Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                            productsViewModel.setPage(pageCount++);
                            productsViewModel.fetchProducts();
                        }
                    }
                }
            }
        });

        productsRecyclerViewAdapter.setItemClickListener(v -> {
            int position = productsRecyclerView.indexOfChild(v);
            Intent intent = new Intent(this, ProductDetailActivity.class);
            intent.putExtra(ProductDetailActivity.KEY_PRODUCT_ID, (int) productsRecyclerViewAdapter.getItemIdByPosition(position));
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupProductsViewModel();
    }

    private void setupProductsViewModel() {
        productsViewModel.setPage(pageCount);
        productsViewModel.fetchProducts();

        productsViewModel.getLoadingData().observe(this, loading -> {
            if (loading != null) {
                progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
                productsRecyclerView.setVisibility(loading ? View.GONE : View.VISIBLE);
            }
        });

        productsViewModel.getRepoErrorLoading().observe(this, repoError -> {
            if (repoError != null) {
                if (repoError) {
                    productsRecyclerView.setVisibility(View.GONE);
                    errorLoadingDataTextView.setVisibility(View.VISIBLE);
                } else {
                    productsRecyclerView.setVisibility(View.VISIBLE);
                    errorLoadingDataTextView.setVisibility(View.GONE);
                }
            }
        });

        productsViewModel.getProducts().observe(this, products -> {
            if (products != null) {
                productsRecyclerViewAdapter.setProductList(products);
                productsRecyclerView.setAdapter(productsRecyclerViewAdapter);
                productsRecyclerViewAdapter.setAddToCartListener(ProductsActivity.this);
            }
        });

        productsViewModel.getAddedToCartStatus().observe(this, addedToCart -> {
            if (addedToCart != null) {
                if (addedToCart) {
                    Toast.makeText(this, "Added successfully to cart", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "There was an error adding to cart", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onAddToCart(Product product) {
        productsViewModel.addToCart(product);
    }
}
