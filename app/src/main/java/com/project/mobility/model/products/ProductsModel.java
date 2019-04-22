package com.project.mobility.model.products;

import com.project.mobility.di.injection.Injection;
import com.project.mobility.repository.products.ProductsRepo;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class ProductsModel {
    @Inject ProductsRepo productsRepo;

    public ProductsModel() {
        Injection.inject(this);
    }

    public Single<List<Product>> getProductsByCategoryId(int id, int page) {
        return productsRepo.getProductsByCategoryId(id, page);
    }
}
