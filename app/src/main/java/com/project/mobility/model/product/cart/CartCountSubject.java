package com.project.mobility.model.product.cart;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class CartCountSubject {
    private static CartCountSubject instance;
    private BehaviorSubject<Integer> subject = BehaviorSubject.create();

    public static CartCountSubject getInstance() {
        if (instance == null) {
            instance = new CartCountSubject();
        }

        return instance;
    }

    private CartCountSubject() {
    }

    public void setValue(Integer value) {
        subject.onNext(value);
    }

    public Observable<Integer> getObservable() {
        return subject;
    }
}
