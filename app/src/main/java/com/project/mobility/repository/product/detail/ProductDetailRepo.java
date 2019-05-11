package com.project.mobility.repository.product.detail;

import com.project.mobility.model.product.Product;

import io.reactivex.Single;

public interface ProductDetailRepo {
    Single<Product> getProductById(int id);
}
