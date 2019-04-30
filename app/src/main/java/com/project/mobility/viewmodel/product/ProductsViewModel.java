package com.project.mobility.viewmodel.product;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.product.Product;
import com.project.mobility.model.product.ProductsModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ProductsViewModel extends ViewModel {

    @Inject ProductsModel productsModel;
    @Inject CompositeDisposable compositeDisposable;

    private int categoryId;
    private int page;

    private MutableLiveData<List<Product>> productMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> loadingMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> repoErrorLoadingMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> addedToCartStatusMutableLiveData = new MutableLiveData<>();

    public ProductsViewModel() {
        Injection.inject(this);
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void fetchProducts() {
        loadingMutableLiveData.setValue(true);

        compositeDisposable.add(productsModel.getProductsByCategoryId(categoryId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Product>>() {
                    @Override
                    public void onNext(List<Product> products) {
                        Timber.d("Start fetching products");
                        productMutableLiveData.postValue(products);
                        repoErrorLoadingMutableLiveData.setValue(false);
                        loadingMutableLiveData.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d("Error fetching products");
                        e.printStackTrace();
                        loadingMutableLiveData.setValue(false);
                        repoErrorLoadingMutableLiveData.setValue(true);
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("Complete fetching products");
                    }
                })
        );
    }

    public LiveData<List<Product>> getProducts() {
        return productMutableLiveData;
    }

    public LiveData<Boolean> getLoadingData() {
        return loadingMutableLiveData;
    }

    public LiveData<Boolean> getRepoErrorLoading() {
        return repoErrorLoadingMutableLiveData;
    }

    public MutableLiveData<Boolean> getAddedToCartStatus() {
        return addedToCartStatusMutableLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }

        Injection.closeScope(this);
    }

    public void addToCart(Product product) {
        compositeDisposable.add(productsModel.addToCart(product)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Timber.d("Product added successfully to cart");
                    addedToCartStatusMutableLiveData.setValue(true);
                }, throwable -> {
                    Timber.d("Failed adding product to cart");
                    throwable.printStackTrace();
                    addedToCartStatusMutableLiveData.setValue(false);
                })
        );
    }
}
