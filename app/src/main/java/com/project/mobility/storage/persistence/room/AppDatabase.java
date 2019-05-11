package com.project.mobility.storage.persistence.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.project.mobility.storage.persistence.room.dao.CartDao;
import com.project.mobility.storage.persistence.room.dao.CategoryDao;
import com.project.mobility.storage.persistence.room.dao.ProductDao;
import com.project.mobility.storage.persistence.room.dao.UserDao;
import com.project.mobility.storage.persistence.room.dao.UserFavoriteProductsDao;
import com.project.mobility.storage.persistence.room.entities.CartEntity;
import com.project.mobility.storage.persistence.room.entities.CategoryEntity;
import com.project.mobility.storage.persistence.room.entities.ProductEntity;
import com.project.mobility.storage.persistence.room.entities.UserEntity;
import com.project.mobility.storage.persistence.room.entities.UserFavoriteProductsEntity;

@Database(entities = {ProductEntity.class, CategoryEntity.class, UserEntity.class, UserFavoriteProductsEntity.class, CartEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "mobility";

    public abstract ProductDao productDao();

    public abstract CategoryDao categoryDao();

    public abstract UserDao userDao();

    public abstract UserFavoriteProductsDao userFavoriteProductsDao();

    public abstract CartDao cartDao();

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
        }

        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }
}
