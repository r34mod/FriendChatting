package com.example.friendchatting.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.friendchatting.Modelo.ReviewModel;

import java.util.List;

@Dao
public interface ReviewDao {
    @Insert
    void insert(ReviewModel review);
    @Query("SELECT * from review WHERE comprasId = :comprasId")
    LiveData<List<ReviewModel>> getAllModel(String comprasId);

}
