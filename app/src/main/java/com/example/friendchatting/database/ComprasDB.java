package com.example.friendchatting.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.friendchatting.Modelo.Compras;
import com.example.friendchatting.Modelo.ReviewModel;

@Database(entities = {Compras.class, ReviewModel.class}, version=1)
public abstract class ComprasDB  extends RoomDatabase {

    private static final String DATABASE_NAME = "ComprasDB";


    public abstract ComprasDao comprasDao();


    public abstract ReviewDao reviewDao();

    //for single instance
    private static volatile ComprasDB INSTANCE;

    public static ComprasDB getINSTANCE(Context context) {
        if (INSTANCE == null) {
            synchronized (ComprasDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, ComprasDB.class, DATABASE_NAME)
                            .addCallback(callback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;

    }

    static Callback callback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}
