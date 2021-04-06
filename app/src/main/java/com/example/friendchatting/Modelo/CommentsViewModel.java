package com.example.friendchatting.Modelo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.friendchatting.Repositorio.ReviewRepositorio;

import java.util.List;

public class CommentsViewModel extends AndroidViewModel {
    private final ReviewRepositorio reviewRepository;
        String comprasId;
        LiveData<List<ReviewModel>> restaurantOnLatLon;

        public CommentsViewModel(@NonNull Application application, String comprasId) {
            super(application);
            this.comprasId = comprasId;
            reviewRepository = new ReviewRepositorio(application);
            restaurantOnLatLon = reviewRepository.getAllModel(comprasId);
        }

        public void insert(String review) {
            ReviewModel reviewModel = new ReviewModel();
            reviewModel.comment = review;
            reviewModel.comprasId = comprasId;
            reviewRepository.insert(reviewModel);
        }

        public LiveData<List<ReviewModel>> getComments() {
            return restaurantOnLatLon;
        }
}
