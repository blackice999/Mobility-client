package com.project.mobility.model.product.cart;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class CartTotalPriceSubject {
    private static CartTotalPriceSubject instance;
    private BehaviorSubject<Integer> subject = BehaviorSubject.create();

    public static CartTotalPriceSubject getInstance() {
        if (instance == null) {
            instance = new CartTotalPriceSubject();
        }

        return instance;
    }

    private CartTotalPriceSubject() {
    }

    public void setValue(Integer value) {
        subject.onNext(value);
    }

    public Observable<Integer> getObservable() {
        return subject;
    }
}
