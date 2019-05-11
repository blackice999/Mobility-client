package com.project.mobility.module.toothpick;

import android.app.Application;
import android.content.Context;

import com.facebook.CallbackManager;
import com.google.gson.Gson;
import com.project.mobility.repository.login.LoginRepo;
import com.project.mobility.repository.login.LoginRepoImpl;
import com.project.mobility.repository.main.cart.CartRepo;
import com.project.mobility.repository.main.cart.CartRepoImpl;
import com.project.mobility.repository.main.home.HomeRepo;
import com.project.mobility.repository.main.home.HomeRepoImpl;
import com.project.mobility.repository.main.navigation.MainNavigationRepo;
import com.project.mobility.repository.main.navigation.MainNavigationRepoImpl;
import com.project.mobility.repository.onboarding.OnboardingCategoryRepo;
import com.project.mobility.repository.onboarding.OnboardingCategoryRepoImpl;
import com.project.mobility.repository.product.ProductsRepo;
import com.project.mobility.repository.product.ProductsRepoImpl;
import com.project.mobility.repository.product.detail.ProductDetailRepo;
import com.project.mobility.repository.product.detail.ProductDetailRepoImpl;
import com.project.mobility.util.image.GlideImageLoader;
import com.project.mobility.util.image.ImageLoader;

import io.reactivex.disposables.CompositeDisposable;
import toothpick.config.Module;

public class ToothpickModule extends Module {
    public ToothpickModule(Application application) {
        bind(Context.class).toInstance(application);
        bind(LoginRepo.class).to(LoginRepoImpl.class);
        bind(Gson.class).toInstance(new Gson());
        bind(CallbackManager.class).toInstance(CallbackManager.Factory.create());
        bind(OnboardingCategoryRepo.class).to(OnboardingCategoryRepoImpl.class);
        bind(HomeRepo.class).to(HomeRepoImpl.class);
        bind(ProductsRepo.class).to(ProductsRepoImpl.class);
        bind(ImageLoader.class).to(GlideImageLoader.class);
        bind(CompositeDisposable.class).toInstance(new CompositeDisposable());
        bind(ProductDetailRepo.class).to(ProductDetailRepoImpl.class);
        bind(CartRepo.class).to(CartRepoImpl.class);
        bind(MainNavigationRepo.class).to(MainNavigationRepoImpl.class);
    }
}
