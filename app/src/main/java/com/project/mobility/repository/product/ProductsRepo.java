package com.project.mobility.repository.product;

import com.project.mobility.model.product.Product;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface ProductsRepo {
    Observable<List<Product>> getProductsByCategoryId(int id, int page);

    Completable addToCart(Product product);
}
