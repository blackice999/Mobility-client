package com.project.mobility.storage.persistence.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.project.mobility.storage.persistence.room.entities.UserEntity;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface UserDao extends BaseDao<UserEntity> {
    @Query("SELECT * FROM user")
    Observable<UserEntity> getCurrentUser();

    @Query("DELETE FROM user")
    Completable deleteAll();

    @Query("SELECT COUNT(display_name) FROM user")
    Maybe<Integer> getLoggedInUserCount();
}
