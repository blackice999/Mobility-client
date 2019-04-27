package com.project.mobility.repository.product.detail;

import com.project.mobility.app.MobilityApplication;
import com.project.mobility.model.product.Product;
import com.project.mobility.storage.persistence.room.AppDatabase;
import com.project.mobility.storage.persistence.room.entities.ProductEntity;
import com.project.mobility.util.server.ServerUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;

public class ProductDetailRepoImpl implements ProductDetailRepo {
    private boolean isServerOnline;
    private AppDatabase appDatabase;

    @Inject
    public ProductDetailRepoImpl() {
        isServerOnline = ServerUtil.isServerOnline();
        appDatabase = MobilityApplication.getInstance().getAppDatabase();
    }

    @Override
    public Single<Product> getProductById(int id) {
        return isServerOnline ? fetch(id) : loadFromLocal(id);
    }

    private Single<Product> fetch(int id) {
        //TODO - fetch from server
        Product product = ServerUtil.createDummyProduct(id);

        if (product.getImagesUrl().isEmpty() || product.getImagesUrl() == null) {
            String placeHolderImageUrl = "https://www.xda-developers.com/files/2018/12/Galaxy-S10-new-renders.jpg";
            List<String> placeHolderImage = new ArrayList<>();
            placeHolderImage.add(placeHolderImageUrl);
            product.setImagesUrl(placeHolderImage);
        }

        return Single.just(product);
    }

    private Single<Product> loadFromLocal(int id) {
        return convertToProductsObservable(appDatabase.productDao().getProductById(id));
    }

    private Single<Product> convertToProductsObservable(Single<ProductEntity> productsEntityObservable) {
        return productsEntityObservable.flatMap(this::convertEntityToProduct);
    }

    private SingleSource<Product> convertEntityToProduct(ProductEntity productEntity) {
        Product product = new Product();

        product.setId(productEntity.id);
        product.setName(productEntity.name);
        product.setPrice(productEntity.price);
        product.setDescription(productEntity.description);
        product.setCategoryId(productEntity.categoryId);
        List<String> coverImages = new ArrayList<>();
        coverImages.add(productEntity.coverImage);
        product.setImagesUrl(coverImages);
        return Single.just(product);
    }

}
