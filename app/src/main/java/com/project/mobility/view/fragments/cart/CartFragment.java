package com.project.mobility.view.fragments.cart;

import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.biometric.BiometricPrompt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.project.mobility.R;
import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.product.cart.CartProduct;
import com.project.mobility.util.currency.CurrencyUtil;
import com.project.mobility.view.activities.login.LoginActivity;
import com.project.mobility.view.fragments.cart.adapter.CartRecyclerViewAdapter;
import com.project.mobility.viewmodel.main.cart.CartViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class CartFragment extends Fragment implements CartRecyclerViewAdapter.CartQuantityObserver {

    @Inject CartRecyclerViewAdapter cartRecyclerViewAdapter;

    @BindView(R.id.cart_container) ConstraintLayout cartContainer;
    @BindView(R.id.progress_bar) ContentLoadingProgressBar progressBar;
    @BindView(R.id.textview_error_loading) AppCompatTextView errorLoadingDataTextView;
    @BindView(R.id.empty_cart_text) AppCompatTextView emptyCartText;
    @BindView(R.id.recycler_view_cart) RecyclerView recyclerView;
    @BindView(R.id.product_total_price) AppCompatTextView totalPriceTextView;
    @BindView(R.id.button_purchase) AppCompatButton buttonPurchase;
    @BindView(R.id.login_to_purchase_button) AppCompatButton loginToPurchaseButton;

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

    @OnClick(R.id.button_purchase)
    public void purchase() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManager fingerprintManager = (FingerprintManager) getContext().getSystemService(Context.FINGERPRINT_SERVICE);
            if (!fingerprintManager.isHardwareDetected()) {
                purchaseWithoutFingerPrint();
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                Snackbar.make(cartContainer, "Please set a fingerprint", Snackbar.LENGTH_LONG).show();
            } else {
                purchaseWithFingerPrint();
            }
        }
    }

    @OnClick(R.id.login_to_purchase_button)
    public void openLoginScreen() {
        startActivity(new Intent(getContext(), LoginActivity.class));
    }

    private void purchaseWithFingerPrint() {
        Executor executor = Executors.newSingleThreadExecutor();

        //TODO: Called when an unrecoverable error has been encountered and the operation is complete.
        BiometricPrompt biometricPrompt = new BiometricPrompt(getActivity(), executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    Timber.d("Authenticate with other method clicked");
                } else {
                    //TODO: Called when an unrecoverable error has been encountered and the operation is complete.
                }
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Timber.d("Biometric was recognized");
                cartViewModel.purchase();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Timber.d("Biometric is valid but not recognized");
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(R.string.authenticate_title))
                .setNegativeButtonText(getString(R.string.authenticate_other_method))
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private void purchaseWithoutFingerPrint() {
        //TODO - add other verification methods to purchase
        cartViewModel.purchase();
    }

    private void setupViewModel() {
        cartViewModel.getCartMutableLiveData().observe(this, products -> {
            if (products.isEmpty()) {
                emptyCartText.setVisibility(View.VISIBLE);
                cartContainer.setVisibility(View.GONE);
            } else {
                showCartList(products);
            }
        });

        cartViewModel.getLoadingMutableLiveData().observe(this, loading -> {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            cartContainer.setVisibility(loading ? View.GONE : View.VISIBLE);
        });

        cartViewModel.getRepoErrorMutableLiveData().observe(this, repoError -> {
            if (repoError) {
                showError();
            } else {
                hideError();
            }
        });

        cartViewModel.getIncreaseProductAmount().observe(this, increased -> {
            if (increased) {
                Toast.makeText(getContext(), "Successfully increased amount", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed increasing amount", Toast.LENGTH_SHORT).show();
            }
        });

        cartViewModel.getDecreaseProductAmount().observe(this, decreased -> {
            if (decreased) {
                Toast.makeText(getContext(), "Successfully decreased amount", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed decreasing amount", Toast.LENGTH_SHORT).show();
            }
        });

        cartViewModel.getClearProduct().observe(this, cleared -> {
            if (cleared) {
                Toast.makeText(getContext(), "Successfully cleared product", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed clear product", Toast.LENGTH_SHORT).show();
            }
        });

        cartViewModel.getTotalCartPriceData().observe(this, totalPrice -> totalPriceTextView.setText(totalPrice + CurrencyUtil.getLocalCurrencySymbol()));

        cartViewModel.getPurchaseStatus().observe(this, purchased -> {
            if (purchased) {
                Toast.makeText(getContext(), "Purchase succesful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Purchase failed", Toast.LENGTH_SHORT).show();
            }
        });

        cartViewModel.canPurchase().observe(this, canPurchase -> {
            buttonPurchase.setVisibility(canPurchase ? View.VISIBLE : View.GONE);
            loginToPurchaseButton.setVisibility(canPurchase ? View.GONE : View.VISIBLE);
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
