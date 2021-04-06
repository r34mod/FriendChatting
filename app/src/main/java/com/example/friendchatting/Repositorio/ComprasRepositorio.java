package com.example.friendchatting.Repositorio;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.friendchatting.Modelo.Compras;
import com.example.friendchatting.database.ComprasDB;
import com.example.friendchatting.database.ComprasDao;

import java.util.List;

public class ComprasRepositorio {
    private ComprasDB dataBase;

    public ComprasRepositorio(Application application) {
        dataBase = ComprasDB.getINSTANCE(application);
    }

    public void insert(List<Compras> modelList) {

        new InsertAsynTask(dataBase).execute(modelList);
    }

    public LiveData<List<Compras>> getAllModel(String latlonString) {
        return dataBase.comprasDao().getAllModel(latlonString);
    }

    public void update(Compras restaurantModel) {
        new UpdateAsynTask(dataBase).execute(restaurantModel);
    }

    // to update data in background
    static class UpdateAsynTask extends AsyncTask<Compras, Void, Void> {

        private ComprasDao comprasDao;

        UpdateAsynTask(ComprasDB comprasDB) {
            comprasDao = comprasDB.comprasDao();

        }

        @Override
        protected Void doInBackground(Compras... compras) {
            //list[0] element
            Compras compras1 = compras[0];
            comprasDao.update(!compras1.thumb, compras1.comprasId);
            return null;
        }
    }

    // to insert data in background
    static class InsertAsynTask extends AsyncTask<List<Compras>, Void, Void> {

        private ComprasDao comprasDao;

        InsertAsynTask(ComprasDB comprasDB) {

            comprasDao = comprasDB.comprasDao();
        }

        @Override
        protected Void doInBackground(List<Compras>... lists) {
            //list[0] element
            comprasDao.insert(lists[0]);
            return null;
        }
    }
}
