package com.project.mobility.viewmodel.product;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.products.Product;
import com.project.mobility.model.products.ProductsModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ProductsViewModel extends ViewModel {

    @Inject ProductsModel productsModel;
    @Inject CompositeDisposable compositeDisposable;

    private int categoryId;
    private int page;

    private MutableLiveData<List<Product>> productMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> loadingMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> repoErrorLoadingMutableLiveData = new MutableLiveData<>();

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
                .subscribeWith(new DisposableSingleObserver<List<Product>>() {
                    @Override
                    public void onSuccess(List<Product> products) {
                        productMutableLiveData.postValue(products);
                        repoErrorLoadingMutableLiveData.setValue(false);
                        loadingMutableLiveData.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingMutableLiveData.setValue(false);
                        repoErrorLoadingMutableLiveData.setValue(false);
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

    @Override
    protected void onCleared() {
        super.onCleared();
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }
}
