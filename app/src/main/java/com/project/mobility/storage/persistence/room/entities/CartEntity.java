package com.project.mobility.storage.persistence.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart")
public class CartEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    public int price;

    public int quantity;

    @ColumnInfo(name = "cover_image")
    public String coverImage;

}
