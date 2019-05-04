package com.project.mobility.storage.persistence.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.project.mobility.model.product.Product;
import com.project.mobility.storage.persistence.room.entities.ProductEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface ProductDao extends BaseDao<ProductEntity> {
    @Query("SELECT * FROM product")
    Observable<List<ProductEntity>> getProducts();

    @Query("SELECT * FROM product WHERE id = :id")
    Single<ProductEntity> getProductById(int id);

    @Query("SELECT * FROM product WHERE category_id = :categoryId")
    Observable<List<ProductEntity>> getProductsByCategoryId(int categoryId);

    @Query("DELETE FROM product")
    Completable deleteAll();

    @Query("SELECT * FROM product WHERE category_id = :categoryId AND name LIKE '%' || :query || '%'")
    Observable<List<ProductEntity>> search(int categoryId, String query);
}
