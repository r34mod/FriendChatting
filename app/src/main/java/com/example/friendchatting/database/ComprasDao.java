package com.example.friendchatting.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.friendchatting.Modelo.Compras;

import java.util.List;

@Dao
public interface ComprasDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Compras> modelList);

    @Query("SELECT * from compras WHERE latlong = :latlong")
    LiveData<List<Compras>> getAllModel(String latlong);

    @Query("DELETE from compras")
    void deleteAll();

    @Query("UPDATE compras SET thumb=:thumb WHERE comprasId = :comprasId")
    void update(boolean thumb, String comprasId);

}
