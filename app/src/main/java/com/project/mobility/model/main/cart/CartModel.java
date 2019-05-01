package com.project.mobility.model.main.cart;

import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.product.Product;
import com.project.mobility.model.product.cart.CartProduct;
import com.project.mobility.repository.main.cart.CartRepo;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class CartModel {
    @Inject CartRepo cartRepo;

    @Inject
    public CartModel() {
        Injection.inject(this);
    }

    public Observable<List<CartProduct>> getCart() {
        return cartRepo.getCart();
    }

    public Completable clearProduct(int productId) {
        return cartRepo.clearProduct(productId);
    }

    public Completable decreaseProductAmount(int productId) {
        return cartRepo.decreaseProductAmount(productId);
    }

    public Completable increaseProductAmount(int productId) {
        return cartRepo.increaseProductAmount(productId);
    }

    public Single<Integer> getCartTotalPrice() {
        return cartRepo.getCartTotalPrice();
    }
}
