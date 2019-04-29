package com.project.mobility.storage.persistence.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.project.mobility.storage.persistence.room.entities.UserFavoriteProductsEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

@Dao
public interface UserFavoriteProductsDao extends BaseDao<UserFavoriteProductsEntity> {
    @Query("SELECT * FROM user_favorite_products WHERE user_id = :userId")
    Observable<List<UserFavoriteProductsEntity>> getUserHistory(int userId);

    @Query("DELETE FROM user_favorite_products")
    Completable deleteAll();
}
