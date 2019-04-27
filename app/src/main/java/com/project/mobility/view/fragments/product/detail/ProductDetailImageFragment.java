package com.project.mobility.view.fragments.product.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.project.mobility.R;
import com.project.mobility.di.injection.Injection;
import com.project.mobility.util.image.ImageLoader;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDetailImageFragment extends Fragment {

    private static final String KEY_IMAGE_URL = "key_imageUrl";

    @BindView(R.id.product_image) AppCompatImageView productImage;

    @Inject ImageLoader imageLoader;

    private String imageUrl;

    public static ProductDetailImageFragment newInstance(String imageUrl) {
        ProductDetailImageFragment fragment = new ProductDetailImageFragment();
        Bundle args = new Bundle();
        args.putString(KEY_IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injection.inject(this);

        if (getArguments() != null) {
            imageUrl = getArguments().getString(KEY_IMAGE_URL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail_image, container, false);

        ButterKnife.bind(this, view);
        imageLoader.load(getContext(), imageUrl, productImage);
        return view;
    }
}
