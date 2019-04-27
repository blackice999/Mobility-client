package com.project.mobility.view.activities.product.detail.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.project.mobility.view.fragments.product.detail.ProductDetailImageFragment;

import java.util.List;

public class ImagesFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<String> imagesUrl;

    public ImagesFragmentPagerAdapter(@NonNull FragmentManager fm, List<String> imagesUrl) {
        super(fm);
        this.imagesUrl = imagesUrl;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return ProductDetailImageFragment.newInstance(imagesUrl.get(position));
    }

    @Override
    public int getCount() {
        return imagesUrl.size();
    }
}
