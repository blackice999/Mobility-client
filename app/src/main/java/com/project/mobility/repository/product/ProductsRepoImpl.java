package com.project.mobility.repository.product;

import com.project.mobility.app.MobilityApplication;
import com.project.mobility.model.product.Product;
import com.project.mobility.model.product.cart.CartElementCount;
import com.project.mobility.model.product.cart.CartCountSubject;
import com.project.mobility.storage.persistence.room.AppDatabase;
import com.project.mobility.storage.persistence.room.dao.CartDao;
import com.project.mobility.storage.persistence.room.dao.CategoryDao;
import com.project.mobility.storage.persistence.room.dao.ProductDao;
import com.project.mobility.storage.persistence.room.entities.CartEntity;
import com.project.mobility.storage.persistence.room.entities.CategoryEntity;
import com.project.mobility.storage.persistence.room.entities.ProductEntity;
import com.project.mobility.util.StringUtils;
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

    private final CartCountSubject cartCountSubject;
    private boolean isServerOnline;
    private CartDao cartDao;
    private ProductDao productDao;
    private CategoryDao categoryDao;

    @Inject
    public ProductsRepoImpl() {
        isServerOnline = ServerUtil.isServerOnline();
        AppDatabase appDatabase = MobilityApplication.getInstance().getAppDatabase();
        cartDao = appDatabase.cartDao();
        productDao = appDatabase.productDao();
        categoryDao = appDatabase.categoryDao();
        addTestCategoriesToDb();
        cartCountSubject = CartCountSubject.getInstance();
    }

    private void addTestCategoriesToDb() {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.categoryName = "Phones";
        categoryDao.insert(categoryEntity).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
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
        return cartDao.productCount(product.getId())
                .flatMapCompletable(cartElement -> cartElement.count == 0 ?
                        insertNewProduct(product) : updateQuantity(product, cartElement));
    }

    private Completable insertNewProduct(Product product) {
        return cartDao.insert(convertToCartEntity(product))
                .andThen(cartDao.getCartCount()
                        .flatMapCompletable(count -> {
                            cartCountSubject.setValue(count);
                            return Completable.complete();
                        })
                );
    }

    private Completable updateQuantity(Product product, CartElementCount cartElement) {
        return cartDao.updateQuantity(product.getId(), cartElement.quantity + 1);
    }

    @Override
    public Observable<List<Product>> searchForProducts(int categoryId, String query) {
        return isServerOnline ? searchRemote(categoryId, query) : searchLocal(categoryId, query);
    }

    private Observable<List<Product>> searchLocal(int categoryId, String query) {
        return convertToProductsObservable(productDao.search(categoryId, query));
    }

    private Observable<List<Product>> searchRemote(int categoryId, String query) {
        //TODO - search from server
        return fetch(categoryId)
                .flatMap(Observable::fromIterable)
                .filter(product -> StringUtils.containsIgnoreCase(product.getName(), query))
                .toList()
                .toObservable();
    }

    private Observable<List<Product>> fetch(int categoryId) {

        //TODO - fetch from server
        List<Product> productsList = ServerUtil.getDummyProductsList(categoryId);
        return Observable.just(productsList);
//        ProductEntity productEntity = convertToProductEntity(productsList.get(0));
//        Completable insert = productDao.insert(productEntity);
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

//        return convertToProductsObservable(productDao.getProductsByCategoryId(categoryId).subscribeOn(Schedulers.io()));
    }

    private Observable<List<Product>> loadFromLocal(int categoryId) {
//        return convertToProductsObservable(productDao.getProductsByCategoryId(categoryId));
        return Observable.just(ServerUtil.getDummyProductsList(categoryId));
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
