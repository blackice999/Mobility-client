package com.project.mobility.storage.persistence.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class UserEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "firebase_token")
    public String firebaseToken;

    @ColumnInfo(name = "display_name")
    public String displayName;

    @ColumnInfo(name = "delivery_address")
    public String deliveryAddress;

    @ColumnInfo(name = "phone_number")
    public String phoneNumber;

    @ColumnInfo(name = "email_address")
    public String emailAddress;
}
