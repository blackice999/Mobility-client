package com.project.mobility.storage.persistence.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.project.mobility.model.product.cart.CartElementCount;
import com.project.mobility.storage.persistence.room.entities.CartEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface CartDao extends BaseDao<CartEntity> {
    @Query("SELECT * FROM cart")
    Observable<List<CartEntity>> getCart();

    @Query("UPDATE cart SET quantity = :quantity WHERE id = :id")
    Completable updateQuantity(int id, int quantity);

    @Query("SELECT quantity, COUNT(name) AS count FROM cart WHERE id = :id")
    Maybe<CartElementCount> productCount(int id);

    @Query("DELETE FROM cart WHERE id = :id")
    Completable clearProduct(int id);

    @Query("SELECT COUNT(name) FROM cart")
    Maybe<Integer> getCartCount();

    @Query("SELECT SUM(quantity * price) FROM cart")
    Single<Integer> getCartTotalPrice();
}
