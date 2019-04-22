package com.project.mobility.repository.products;

import com.project.mobility.model.products.Product;

import java.util.List;

import io.reactivex.Single;

public interface ProductsRepo {
    Single<List<Product>> getProductsByCategoryId(int id, int page);
}
