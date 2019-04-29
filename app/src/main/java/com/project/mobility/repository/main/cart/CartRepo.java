package com.project.mobility.repository.main.cart;

import com.project.mobility.model.product.cart.CartProduct;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface CartRepo {
    Observable<List<CartProduct>> getCart();

    Completable clearProduct(int productId);

    Completable decreaseProductAmount(int productId);

    Completable increaseProductAmount(int productId);
}
