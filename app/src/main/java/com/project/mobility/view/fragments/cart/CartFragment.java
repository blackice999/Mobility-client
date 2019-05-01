package com.project.mobility.view.fragments.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mobility.R;
import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.product.cart.CartProduct;
import com.project.mobility.util.currency.CurrencyUtil;
import com.project.mobility.view.fragments.cart.adapter.CartRecyclerViewAdapter;
import com.project.mobility.viewmodel.main.cart.CartViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartFragment extends Fragment implements CartRecyclerViewAdapter.CartQuantityObserver {

    @Inject CartRecyclerViewAdapter cartRecyclerViewAdapter;

    @BindView(R.id.cart_container) ConstraintLayout cartContainer;
    @BindView(R.id.progress_bar) ContentLoadingProgressBar progressBar;
    @BindView(R.id.textview_error_loading) AppCompatTextView errorLoadingDataTextView;
    @BindView(R.id.empty_cart_text) AppCompatTextView emptyCartText;
    @BindView(R.id.recycler_view_cart) RecyclerView recyclerView;
    @BindView(R.id.product_total_price) AppCompatTextView totalPriceTextView;

    private CartViewModel cartViewModel;

    public CartFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injection.inject(this);
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        setupViewModel();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Injection.closeScope(this);
    }

    private void setupViewModel() {
        cartViewModel.getCartMutableLiveData().observe(this, products -> {
            if (products != null) {
                if (products.isEmpty()) {
                    emptyCartText.setVisibility(View.VISIBLE);
                    cartContainer.setVisibility(View.GONE);
                } else {
                    showCartList(products);
                }
            }
        });

        cartViewModel.getLoadingMutableLiveData().observe(this, loading -> {
            if (loading != null) {
                progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
                cartContainer.setVisibility(loading ? View.GONE : View.VISIBLE);

            }
        });

        cartViewModel.getRepoErrorMutableLiveData().observe(this, repoError -> {
            if (repoError != null) {
                if (repoError) {
                    showError();
                } else {
                    hideError();
                }
            }
        });

        cartViewModel.getIncreaseProductAmount().observe(this, increased -> {
            if (increased != null) {
                if (increased) {
                    Toast.makeText(getContext(), "Successfully increased amount", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed increasing amount", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cartViewModel.getDecreaseProductAmount().observe(this, decreased -> {
            if (decreased != null) {
                if (decreased) {
                    Toast.makeText(getContext(), "Successfully decreased amount", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed decreasing amount", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cartViewModel.getClearProduct().observe(this, cleared -> {
            if (cleared != null) {
                if (cleared) {
                    Toast.makeText(getContext(), "Successfully cleared product", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed clear product", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cartViewModel.getTotalCartPriceData().observe(this, totalPrice -> {
            if (totalPrice != null) {
                totalPriceTextView.setText(totalPrice + CurrencyUtil.getLocalCurrencySymbol());
            }
        });
    }

    private void showCartList(List<CartProduct> products) {
        cartRecyclerViewAdapter.setProducts(products);
        cartRecyclerViewAdapter.setCartQuantityObserver(CartFragment.this);
        recyclerView.setAdapter(cartRecyclerViewAdapter);
    }

    private void hideError() {
        cartContainer.setVisibility(View.VISIBLE);
        errorLoadingDataTextView.setVisibility(View.GONE);
    }

    private void showError() {
        cartContainer.setVisibility(View.GONE);
        errorLoadingDataTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDecreaseAmount(int id) {
        cartViewModel.decreaseProductAmount(id);
    }

    @Override
    public void onIncreaseAmount(int id) {
        cartViewModel.increaseProductAmount(id);
    }

    @Override
    public void onClear(int id) {
        cartViewModel.clearProduct(id);
    }
}
