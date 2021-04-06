package com.example.friendchatting.Repositorio;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.friendchatting.Modelo.ReviewModel;
import com.example.friendchatting.database.ComprasDB;
import com.example.friendchatting.database.ReviewDao;

import java.util.List;

public class ReviewRepositorio {

    private ComprasDB dataBase;

    public ReviewRepositorio(Application application) {
        dataBase = ComprasDB.getINSTANCE(application);
    }


    public LiveData<List<ReviewModel>> getAllModel(String comprasId) {
        return dataBase.reviewDao().getAllModel(comprasId);
    }

    public void insert(ReviewModel reviewModel) {
        new InsertAsynTask(dataBase).execute(reviewModel);
    }

    // to insert data in background
    static class InsertAsynTask extends AsyncTask<ReviewModel, Void, Void> {

        private ReviewDao reviewDao;

        InsertAsynTask(ComprasDB comprasDB) {
            reviewDao = comprasDB.reviewDao();
        }

        @Override
        protected Void doInBackground(ReviewModel... lists) {
            //list[0] element
            reviewDao.insert(lists[0]);
            return null;
        }
    }
}
