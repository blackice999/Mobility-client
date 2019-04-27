package com.project.mobility.view.activities.product.detail;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.project.mobility.R;
import com.project.mobility.model.product.Product;
import com.project.mobility.util.currency.CurrencyUtil;
import com.project.mobility.view.activities.product.detail.adapter.ImagesFragmentPagerAdapter;
import com.project.mobility.viewmodel.product.detail.ProductDetailViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String KEY_PRODUCT_ID = "product_id";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.progress_bar) ContentLoadingProgressBar progressBar;
    @BindView(R.id.tab_navigation) TabLayout tabNavigation;
    @BindView(R.id.product_container) ConstraintLayout productContainer;
    @BindView(R.id.textview_error_loading) AppCompatTextView errorLoadingDataTextView;
    @BindView(R.id.product_title) AppCompatTextView productTitle;
    @BindView(R.id.product_images) ViewPager imagesViewPager;
    @BindView(R.id.product_price) AppCompatTextView productPrice;
    @BindView(R.id.product_description) AppCompatTextView productDescription;

    private ProductDetailViewModel productDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabNavigation.setupWithViewPager(imagesViewPager);

        productDetailViewModel = ViewModelProviders.of(this).get(ProductDetailViewModel.class);
        productDetailViewModel.setProductId(getIntent().getIntExtra(KEY_PRODUCT_ID, 0));
        setupViewModel();
    }

    private void setupViewModel() {
        productDetailViewModel.fetchProduct();
        productDetailViewModel.getLoading().observe(this, loading -> {
            if (loading != null) {
                progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
                productContainer.setVisibility(loading ? View.GONE : View.VISIBLE);
            }
        });

        productDetailViewModel.getRepoErrorData().observe(this, repoError -> {
            if (repoError != null) {
                if (repoError) {
                    showError();
                } else {
                    hideError();
                }
            }
        });

        productDetailViewModel.getProduct().observe(this, product -> {
            if (product != null) {
                showProduct(product);
            }
        });

        productDetailViewModel.getImagePosition().observe(this, this::changeImageViewPagerElementPosition);
    }

    private void showProduct(Product product) {
        productTitle.setText(product.getName());
        productDescription.setText(product.getDescription());
        String priceWithSymbol = product.getPrice() + CurrencyUtil.getLocalCurrencySymbol();
        productPrice.setText(priceWithSymbol);
        imagesViewPager.setAdapter(new ImagesFragmentPagerAdapter(getSupportFragmentManager(), product.getImagesUrl()));
    }

    private void hideError() {
        productContainer.setVisibility(View.VISIBLE);
        errorLoadingDataTextView.setVisibility(View.GONE);
    }

    private void showError() {
        productContainer.setVisibility(View.GONE);
        errorLoadingDataTextView.setVisibility(View.VISIBLE);
    }

    private void changeImageViewPagerElementPosition(int position) {
        imagesViewPager.setCurrentItem(position);
    }

    @OnClick(R.id.fab_add_to_cart)
    public void addToCart(View v) {
        Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
