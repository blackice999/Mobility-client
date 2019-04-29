package com.project.mobility.repository.product;

import com.project.mobility.app.MobilityApplication;
import com.project.mobility.model.product.Product;
import com.project.mobility.storage.persistence.room.AppDatabase;
import com.project.mobility.storage.persistence.room.entities.CartEntity;
import com.project.mobility.storage.persistence.room.entities.CategoryEntity;
import com.project.mobility.storage.persistence.room.entities.ProductEntity;
import com.project.mobility.util.server.ServerUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProductsRepoImpl implements ProductsRepo {

    private boolean isServerOnline;
    private AppDatabase appDatabase;

    @Inject
    public ProductsRepoImpl() {
        isServerOnline = ServerUtil.isServerOnline();
        appDatabase = MobilityApplication.getInstance().getAppDatabase();
        addTestCategoriesToDb();
    }

    private void addTestCategoriesToDb() {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.categoryName = "Phones";
        appDatabase.categoryDao().insert(categoryEntity).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Observable<List<Product>> getProductsByCategoryId(int categoryId, int page) {
        return isServerOnline ? fetch(categoryId) : loadFromLocal(categoryId);
    }

    @Override
    public Completable addToCart(Product product) {
        return appDatabase.cartDao().productCount(product.getId()).flatMapCompletable(
                cartElement -> cartElement.count == 0 ?
                        appDatabase.cartDao().insert(convertToCartEntity(product)) :
                        appDatabase.cartDao().updateQuantity(product.getId(), cartElement.quantity + 1)
        );
    }

    private Observable<List<Product>> fetch(int categoryId) {

        //TODO - fetch from server
        List<Product> productsList = ServerUtil.createDummyProductsList(categoryId);
        return Observable.just(productsList);
//        ProductEntity productEntity = convertToProductEntity(productsList.get(0));
//        Completable insert = appDatabase.productDao().insert(productEntity);
//        insert.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()).
//                subscribe(new CompletableObserver() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//                });

//        return convertToProductsObservable(appDatabase.productDao().getProductsByCategoryId(categoryId).subscribeOn(Schedulers.io()));
    }

    private Observable<List<Product>> loadFromLocal(int categoryId) {
//        return convertToProductsObservable(appDatabase.productDao().getProductsByCategoryId(categoryId));
        return Observable.just(ServerUtil.createDummyProductsList(categoryId));
    }

    private Observable<List<Product>> convertToProductsObservable(Observable<List<ProductEntity>> productsEntityObservable) {
        return productsEntityObservable.flatMap(list ->
                Observable.fromIterable(list)
                        .map(this::convertProductEntityToProduct)
                        .toList()
                        .toObservable());
    }

    private Product convertProductEntityToProduct(ProductEntity productEntity) {
        Product product = new Product();

        product.setId(productEntity.id);
        product.setName(productEntity.name);
        product.setPrice(productEntity.price);
        product.setDescription(productEntity.description);
        product.setCategoryId(productEntity.categoryId);
        List<String> coverImages = new ArrayList<>();
        coverImages.add(productEntity.coverImage);
        product.setImagesUrl(coverImages);
        return product;
    }

    private ProductEntity convertToProductEntity(Product product) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.name = product.getName();
        productEntity.description = product.getDescription();
        productEntity.categoryId = product.getCategoryId();
        productEntity.coverImage = product.getImagesUrl().get(0);
        productEntity.price = product.getPrice();
        return productEntity;
    }

    private CartEntity convertToCartEntity(Product product) {
        CartEntity cartEntity = new CartEntity();
        cartEntity.name = product.getName();
        cartEntity.id = product.getId();
        cartEntity.quantity = 1;
        cartEntity.coverImage = product.getImagesUrl().get(0);
        cartEntity.price = product.getPrice();
        return cartEntity;
    }
}
