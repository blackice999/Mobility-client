package com.project.mobility.viewmodel.product.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.product.Product;
import com.project.mobility.model.product.detail.ProductDetailModel;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import toothpick.Toothpick;

public class ProductDetailViewModel extends ViewModel {
    @Inject ProductDetailModel productDetailModel;
    @Inject CompositeDisposable compositeDisposable;

    private MutableLiveData<Product> productMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> loadingMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> repoErrorMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> imagePositionMutableLiveData = new MutableLiveData<>();

    private int productId;
    private int position;

    @Inject
    public ProductDetailViewModel() {
        Injection.inject(this);
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void fetchProduct() {
        loadingMutableLiveData.setValue(true);
        compositeDisposable.add(productDetailModel.getProductById(productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(product -> {
                    loopImages(product.getImagesUrl().size());
                    return Single.just(product);
                })
                .subscribeWith(new DisposableSingleObserver<Product>() {
                    @Override
                    public void onSuccess(Product product) {
                        Timber.d("Complete fetching product detail");
                        productMutableLiveData.setValue(product);
                        loadingMutableLiveData.setValue(false);
                        repoErrorMutableLiveData.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d("Error fetching product detail");
                        e.printStackTrace();
                        loadingMutableLiveData.setValue(false);
                        repoErrorMutableLiveData.setValue(true);
                    }
                })
        );
    }

    private void loopImages(int imagesUrlSize) {
        compositeDisposable.add(Observable.interval(3, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        if (position >= imagesUrlSize) {
                            position = 0;
                        } else {
                            position++;
                        }

                        imagePositionMutableLiveData.setValue(position);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    public LiveData<Product> getProduct() {
        return productMutableLiveData;
    }

    public LiveData<Boolean> getLoading() {
        return loadingMutableLiveData;
    }

    public LiveData<Boolean> getRepoErrorData() {
        return repoErrorMutableLiveData;
    }

    public LiveData<Integer> getImagePosition() {
        return imagePositionMutableLiveData;
    }

    protected void onCleared() {
        super.onCleared();
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }

        Toothpick.closeScope(this);
    }
}
