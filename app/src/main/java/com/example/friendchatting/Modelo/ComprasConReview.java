package com.example.friendchatting.Modelo;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ComprasConReview {
    @Embedded
    public Compras compras;
    @Relation(
            parentColumn = "comprasId",
            entityColumn = "comprasId"
    )
    public List<ReviewModel> playlists;
}
