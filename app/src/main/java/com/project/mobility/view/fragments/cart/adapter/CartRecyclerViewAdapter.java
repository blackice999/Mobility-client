package com.project.mobility.view.fragments.cart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.project.mobility.R;
import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.product.Product;
import com.project.mobility.model.product.cart.CartProduct;
import com.project.mobility.util.currency.CurrencyUtil;
import com.project.mobility.util.image.ImageLoader;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {
    @Inject ImageLoader imageLoader;

    private List<CartProduct> products;
    private Context context;
    private CartQuantityObserver cartQuantityObserver;

    public CartRecyclerViewAdapter(List<CartProduct> products, Context context) {
        Injection.inject(this);
        this.products = products;
        this.context = context;
    }

    public void setCartQuantityObserver(CartQuantityObserver cartQuantityObserver) {
        this.cartQuantityObserver = cartQuantityObserver;
    }


    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Injection.closeScope(this);
    }

    @NonNull
    @Override
    public CartRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public int getCartPrice() {
        int sum = 0;
        for (CartProduct product : products) {
            sum += product.getPrice() * product.getQuantity();
        }

        return sum;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.product_image) AppCompatImageView coverImage;
        @BindView(R.id.product_name) AppCompatTextView name;
        @BindView(R.id.product_quantity) AppCompatTextView quantity;
        @BindView(R.id.product_price) AppCompatTextView price;

        @BindView(R.id.button_decrease_amount) AppCompatImageButton decreaseAmount;
        @BindView(R.id.button_increase_amount) AppCompatImageButton increaseAmount;
        @BindView(R.id.button_clear) AppCompatImageButton clear;

        private View view;
        private Product item;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.view = itemView;
        }

        public void bind(CartProduct product) {
            decreaseAmount.setOnClickListener(view -> cartQuantityObserver.onDecreaseAmount(product.getId()));
            increaseAmount.setOnClickListener(view -> cartQuantityObserver.onIncreaseAmount(product.getId()));
            clear.setOnClickListener(view -> cartQuantityObserver.onClear(product.getId()));

            name.setText(product.getName());
            quantity.setText(String.valueOf(product.getQuantity()));
            String priceWithSymbol = product.getPrice() + CurrencyUtil.getLocalCurrencySymbol();
            price.setText(priceWithSymbol);

            if (product.getImageUrl() != null) {
                imageLoader.loadWithPlaceholderAndTransition(
                        context,
                        product.getImageUrl(),
                        coverImage,
                        R.drawable.background_splash,
                        new DrawableTransitionOptions().crossFade()
                );
            } else {
                imageLoader.clear(context, coverImage);
                imageLoader.load(context, ImageLoader.PLACEHOLDER_IMAGE_URL, coverImage);
            }

        }
    }

    public interface CartQuantityObserver {
        void onDecreaseAmount(int id);

        void onIncreaseAmount(int id);

        void onClear(int id);
    }
}
