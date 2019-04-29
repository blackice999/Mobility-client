package com.project.mobility.storage.persistence.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "product", foreignKeys = @ForeignKey(
        entity = CategoryEntity.class,
        parentColumns = "id",
        childColumns = "category_id",
        onDelete = CASCADE)
)
public class ProductEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "category_id")
    public int categoryId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "price")
    public int price;

    @ColumnInfo(name = "product_cover_image")
    public String coverImage;
}
