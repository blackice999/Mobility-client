package com.project.mobility.storage.persistence.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.project.mobility.storage.persistence.room.entities.CategoryEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

@Dao
public interface CategoryDao extends BaseDao<CategoryEntity> {
    @Query("SELECT * FROM category")
    Observable<List<CategoryEntity>> getCategories();

    @Query("DELETE FROM category")
    Completable deleteAll();
}
