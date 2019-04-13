package com.project.mobility.util.image;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

public interface ImageLoader {
    void load(Context context, String url, AppCompatImageView target);

    void loadWithPlaceholder(Context context, String url, AppCompatImageView target, Drawable placeHolderDrawable);

    void loadWithPlaceholder(Context context, String url, AppCompatImageView target, @DrawableRes int placeHolderDrawableId);

    void loadWithPlaceholderAndTransition(Context context, String url, AppCompatImageView target, Drawable placeHolderDrawable, DrawableTransitionOptions drawableTransitionOptions);

    void loadWithPlaceholderAndTransition(Context context, String url, AppCompatImageView target, @DrawableRes int placeHolderDrawableId, DrawableTransitionOptions drawableTransitionOptions);

    void loadCropped(Context context, String url, AppCompatImageView target);

    void clear(Context context, AppCompatImageView target);

}
