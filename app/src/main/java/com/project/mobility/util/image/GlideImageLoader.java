package com.project.mobility.util.image;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.project.mobility.module.glide.GlideApp;

import javax.inject.Inject;

public class GlideImageLoader implements ImageLoader {

    @Inject
    public GlideImageLoader() {

    }

    @Override
    public void load(Context context, String url, AppCompatImageView target) {
        GlideApp.with(context).load(url).into(target);
    }

    @Override
    public void loadWithPlaceholder(Context context, String url, AppCompatImageView target, Drawable placeHolderDrawable) {
        GlideApp.with(context).load(url).placeholder(placeHolderDrawable).into(target);
    }

    @Override
    public void loadWithPlaceholder(Context context, String url, AppCompatImageView target, int placeHolderDrawableId) {
        GlideApp.with(context).load(url).placeholder(placeHolderDrawableId).into(target);
    }

    @Override
    public void loadWithPlaceholderAndTransition(Context context, String url, AppCompatImageView target, Drawable placeHolderDrawable, DrawableTransitionOptions drawableTransitionOptions) {
        GlideApp.with(context).load(url).placeholder(placeHolderDrawable).transition(drawableTransitionOptions).into(target);
    }

    @Override
    public void loadWithPlaceholderAndTransition(Context context, String url, AppCompatImageView target, int placeHolderDrawableId, DrawableTransitionOptions drawableTransitionOptions) {
        GlideApp.with(context).load(url).placeholder(placeHolderDrawableId).transition(drawableTransitionOptions).into(target);
    }

    @Override
    public void loadCropped(Context context, String url, AppCompatImageView target) {

    }

    @Override
    public void clear(Context context, AppCompatImageView target) {
        GlideApp.with(context).clear(target);
    }
}
