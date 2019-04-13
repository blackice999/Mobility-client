package com.project.mobility.repository.products;

import com.project.mobility.model.products.Product;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.internal.operators.observable.ObservableToListSingle;

public class ProductsRepoImpl implements ProductsRepo {

    @Inject
    public ProductsRepoImpl() {

    }

    @Override
    public Single<List<Product>> getProductsByCategoryId(int id, int page) {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setId(10);
        product.setDescription("First product description");
        product.setName("First product");

        List<String> imagesUrlList = new ArrayList<>();
        imagesUrlList.add("https://www.xda-developers.com/files/2018/12/Galaxy-S10-new-renders.jpg");
        product.setImagesUrl(imagesUrlList);
        product.setPrice(200);
        products.add(product);
        return ObservableToListSingle.just(products);
    }
}
