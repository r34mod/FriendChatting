package com.example.friendchatting.Modelo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "review")
public class ReviewModel {
    @PrimaryKey(autoGenerate = true)
    public int reviewId;

    public String comprasId;

    public String comment;
}
