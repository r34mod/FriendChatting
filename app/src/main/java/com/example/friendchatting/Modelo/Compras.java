package com.example.friendchatting.Modelo;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "compras",indices = @Index(value = {"comprasId"},unique = true))
public class Compras {

    @PrimaryKey
    @NonNull
    public String comprasId;

    @SerializedName("name")
    @ColumnInfo(name="name")
    public String name;

    @SerializedName("latlong")
    @ColumnInfo(name="latlong")
    public String latlong;

    @SerializedName("image")
    @ColumnInfo(name="image")
    public String image;

    @SerializedName("contact")
    @ColumnInfo(name="contact")
    public int contact;

    @ColumnInfo(name="rating")
    public float rating;

    @ColumnInfo(name="thumb")
    public boolean thumb;

    public String headerLocation;

    public String address;

    @Override
    public String toString() {
        return "Model{" +
                "coomprasId=" + comprasId +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", venue=" + contact +
                ", headerLocation='" + headerLocation + '\'' +
                '}';
    }
}
