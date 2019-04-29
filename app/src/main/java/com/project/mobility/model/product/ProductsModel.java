package com.project.mobility.model.product;

import com.project.mobility.di.injection.Injection;
import com.project.mobility.repository.product.ProductsRepo;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class ProductsModel {
    @Inject ProductsRepo productsRepo;

    public ProductsModel() {
        Injection.inject(this);
    }

    public Observable<List<Product>> getProductsByCategoryId(int id, int page) {
        return productsRepo.getProductsByCategoryId(id, page);
    }

    public Completable addToCart(Product product) {
        return productsRepo.addToCart(product);
    }
}
