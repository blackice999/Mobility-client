package com.project.mobility.view.activities.product.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.project.mobility.R;
import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.product.Product;
import com.project.mobility.util.currency.CurrencyUtil;
import com.project.mobility.util.image.ImageLoader;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsRecyclerViewAdapter extends RecyclerView.Adapter<ProductsRecyclerViewAdapter.ViewHolder> {

    private static final String PLACEHOLDER_IMAGE_URL = "https://via.placeholder.com/300x300.png?text=Mobility";

    @Inject ImageLoader imageLoader;

    private List<Product> productList;
    private Context context;
    private View.OnClickListener itemClickListener;

    public void setItemClickListener(View.OnClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ProductsRecyclerViewAdapter(Context context) {
        Injection.inject(this);
        this.context = context;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public long getItemIdByPosition(int position) {
        return productList.get(position).getId();
    }

    @NonNull
    @Override
    public ProductsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bind(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_product) AppCompatImageView coverImage;
        @BindView(R.id.product_title) AppCompatTextView productTitle;
        @BindView(R.id.product_price) AppCompatTextView productPrice;

        private View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this, itemView);
        }

        public void bind(Product product) {
            productTitle.setText(product.getName());
            view.setOnClickListener(v -> itemClickListener.onClick(v));
            String priceWithSymbol = product.getPrice() + CurrencyUtil.getLocalCurrencySymbol();
            productPrice.setText(priceWithSymbol);

            if (product.getImagesUrl() != null) {
                imageLoader.loadWithPlaceholderAndTransition(
                        context,
                        product.getImagesUrl().get(0),
                        coverImage,
                        R.drawable.background_splash,
                        new DrawableTransitionOptions().crossFade()
                );
            } else {
                imageLoader.clear(context, coverImage);
                imageLoader.load(context, PLACEHOLDER_IMAGE_URL, coverImage);
            }
        }
    }
}
