package com.project.mobility.storage.persistence.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "user_favorite_products", foreignKeys = @ForeignKey(
        entity = UserEntity.class,
        parentColumns = "id",
        childColumns = "user_id",
        onDelete = CASCADE)
)
public class UserFavoriteProductsEntity {
    @PrimaryKey(autoGenerate = true)
    public int favorite_id;

    @ColumnInfo(name = "user_id")
    public int userId;

    @Embedded
    public ProductEntity products;

}
