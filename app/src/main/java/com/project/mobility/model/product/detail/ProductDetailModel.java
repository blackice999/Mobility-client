package com.project.mobility.model.product.detail;

import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.product.Product;
import com.project.mobility.repository.product.detail.ProductDetailRepo;

import javax.inject.Inject;

import io.reactivex.Single;

public class ProductDetailModel {
    @Inject ProductDetailRepo productDetailRepo;

    public ProductDetailModel() {
        Injection.inject(this);
    }

    public Single<Product> getProductById(int id) {
        return productDetailRepo.getProductById(id);
    }
}
