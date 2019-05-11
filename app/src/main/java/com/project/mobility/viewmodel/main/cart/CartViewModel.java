package com.project.mobility.viewmodel.main.cart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.main.cart.CartModel;
import com.project.mobility.model.product.cart.CartProduct;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class CartViewModel extends ViewModel {
    @Inject CartModel cartModel;
    @Inject CompositeDisposable compositeDisposable;

    private MutableLiveData<List<CartProduct>> cartMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> loadingMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> repoErrorMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<Boolean> clearProductMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> decreaseProductAmountMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> increaseProductAmountMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> totalCartPriceMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<Boolean> purchaseStatusMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> canPurchaseMutableLiveData = new MutableLiveData<>();

    public CartViewModel() {
        Injection.inject(this);
        getCart();
        getCartTotalPrice();
        getCanPurchase();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }

        Injection.closeScope(this);
    }

    public LiveData<List<CartProduct>> getCartMutableLiveData() {
        return cartMutableLiveData;
    }

    public LiveData<Boolean> getLoadingMutableLiveData() {
        return loadingMutableLiveData;
    }

    public LiveData<Boolean> getRepoErrorMutableLiveData() {
        return repoErrorMutableLiveData;
    }

    public LiveData<Boolean> getClearProduct() {
        return clearProductMutableLiveData;
    }

    public LiveData<Boolean> getDecreaseProductAmount() {
        return decreaseProductAmountMutableLiveData;
    }

    public LiveData<Boolean> getIncreaseProductAmount() {
        return increaseProductAmountMutableLiveData;
    }

    public LiveData<Integer> getTotalCartPriceData() {
        return totalCartPriceMutableLiveData;
    }

    public LiveData<Boolean> getPurchaseStatus() {
        return purchaseStatusMutableLiveData;
    }

    public LiveData<Boolean> canPurchase() {
        return canPurchaseMutableLiveData;
    }

    private void getCart() {
        loadingMutableLiveData.setValue(true);
        compositeDisposable.add(cartModel.getCart()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<CartProduct>>() {
                    @Override
                    public void onNext(List<CartProduct> products) {
                        Timber.d("Fetch cart products");
                        loadingMutableLiveData.setValue(false);
                        cartMutableLiveData.setValue(products);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d("Error fetching cart products");
                        e.printStackTrace();
                        repoErrorMutableLiveData.setValue(true);
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("Complete fetching cart products");
                    }
                })
        );
    }

    private void getCartTotalPrice() {
        compositeDisposable.add(cartModel.getCartTotalPrice()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        Timber.d("Got total cart price: %s", integer);
                        totalCartPriceMutableLiveData.setValue(integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d("Failed returning total cart price");
                        e.printStackTrace();
                    }
                })
        );
    }

    public void decreaseProductAmount(int productId) {
        compositeDisposable.add(cartModel.decreaseProductAmount(productId)
                .andThen(cartModel.getCartTotalPrice())
                .flatMapCompletable(totalPrice -> {
                    totalCartPriceMutableLiveData.postValue(totalPrice);
                    return Completable.complete();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Timber.d("Product amount decreased");
                    decreaseProductAmountMutableLiveData.setValue(true);
                }, throwable -> {
                    Timber.d("Failed decreasing product amount");
                    throwable.printStackTrace();
                    decreaseProductAmountMutableLiveData.setValue(false);
                })
        );
    }

    public void increaseProductAmount(int productId) {
        compositeDisposable.add(cartModel.increaseProductAmount(productId)
                .andThen(cartModel.getCartTotalPrice())
                .flatMapCompletable(totalPrice -> {
                    totalCartPriceMutableLiveData.postValue(totalPrice);
                    return Completable.complete();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Timber.d("Product amount increased");
                    increaseProductAmountMutableLiveData.setValue(true);
                }, throwable -> {
                    Timber.d("Failed increasing product amount");
                    throwable.printStackTrace();
                    increaseProductAmountMutableLiveData.setValue(false);
                })
        );
    }

    public void clearProduct(int productId) {
        compositeDisposable.add(cartModel.clearProduct(productId)
                .andThen(cartModel.getCartTotalPrice())
                .flatMapCompletable(totalPrice -> {
                    totalCartPriceMutableLiveData.postValue(totalPrice);
                    return Completable.complete();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Timber.d("Product cleared from cart");
                    clearProductMutableLiveData.setValue(true);
                }, throwable -> {
                    Timber.d("Failed clearing product from cart");
                    throwable.printStackTrace();
                    clearProductMutableLiveData.setValue(false);
                })
        );
    }

    public void purchase() {
        compositeDisposable.add(cartModel.purchase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Timber.d("Purchase successful");
                    purchaseStatusMutableLiveData.setValue(true);
                }, throwable -> {
                    Timber.d("Purchase failed");
                    throwable.printStackTrace();
                    purchaseStatusMutableLiveData.setValue(false);
                })
        );
    }

    private void getCanPurchase() {
        canPurchaseMutableLiveData.setValue(false);
        compositeDisposable.add(cartModel.getLoggedInUserCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(count -> {
                    Timber.d("Logged in user count %s", count);
                    canPurchaseMutableLiveData.setValue(count != 0);
                }, throwable -> {
                    Timber.d("No logged in user");
                    throwable.printStackTrace();
                    canPurchaseMutableLiveData.setValue(false);
                })
        );
    }
}
