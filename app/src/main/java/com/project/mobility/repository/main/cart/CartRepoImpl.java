package com.project.mobility.repository.main.cart;

import com.project.mobility.app.MobilityApplication;
import com.project.mobility.model.product.cart.CartProduct;
import com.project.mobility.storage.persistence.room.AppDatabase;
import com.project.mobility.storage.persistence.room.dao.CartDao;
import com.project.mobility.storage.persistence.room.entities.CartEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class CartRepoImpl implements CartRepo {

    private CartDao cartDao;

    @Inject
    public CartRepoImpl() {
        AppDatabase appDatabase = MobilityApplication.getInstance().getAppDatabase();
        cartDao = appDatabase.cartDao();
    }

    @Override
    public Observable<List<CartProduct>> getCart() {
        return getFromLocal();
    }

    @Override
    public Completable clearProduct(int productId) {
        return cartDao.clearProduct(productId);
    }

    @Override
    public Completable decreaseProductAmount(int productId) {
        return cartDao.productCount(productId)
                .flatMapCompletable(cartElement -> cartElement.quantity == 0 ?
                        cartDao.updateQuantity(productId, 0) :
                        cartDao.updateQuantity(productId, cartElement.quantity - 1)
                );
    }

    @Override
    public Completable increaseProductAmount(int productId) {
        return cartDao.productCount(productId).flatMapCompletable(cartElement -> cartDao.updateQuantity(productId, cartElement.quantity + 1));
    }

    @Override
    public Single<Integer> getCartTotalPrice() {
        return cartDao.getCartTotalPrice();
    }

    private Observable<List<CartProduct>> getFromLocal() {
        return convertToProductsObservable(cartDao.getCart());
    }

    private Observable<List<CartProduct>> convertToProductsObservable(Observable<List<CartEntity>> productsEntityObservable) {
        return productsEntityObservable.flatMap(list ->
                Observable.fromIterable(list)
                        .map(this::convertEntityToProduct)
                        .toList()
                        .toObservable());
    }

    private CartProduct convertEntityToProduct(CartEntity cartEntity) {
        CartProduct cartProduct = new CartProduct();
        cartProduct.setId(cartEntity.id);
        cartProduct.setName(cartEntity.name);
        cartProduct.setPrice(cartEntity.price);
        cartProduct.setQuantity(cartEntity.quantity);
        cartProduct.setImageUrl(cartEntity.coverImage);
        return cartProduct;
    }
}
