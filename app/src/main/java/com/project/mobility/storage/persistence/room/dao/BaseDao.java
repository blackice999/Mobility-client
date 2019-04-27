package com.project.mobility.storage.persistence.room.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import io.reactivex.Completable;

public interface BaseDao<T> {
    @Insert
    Completable insert(T object);

    @Delete
    Completable delete(T object);

    @Update
    Completable update(T object);
}
