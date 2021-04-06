package com.example.friendchatting.Peds;

import android.annotation.SuppressLint;
import android.app.Application;
import android.location.Location;
import android.location.LocationListener;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.friendchatting.Modelo.Compras;
import com.example.friendchatting.Repositorio.ComprasRepositorio;
import com.example.friendchatting.compraPojos.CompraApi;
import com.example.friendchatting.compraPojos.FourSquareResource;
import com.example.friendchatting.compraPojos.Item;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class CompraViewModel extends AndroidViewModel implements LocationListener {

    public final ComprasRepositorio restaurantRepository;
    String client_id = "BMRA0OSIBFAMQ3BB253NLRK24HMER2PDWMO4BWKKKKJGOD1E";
    String client_secret = "T3K2YNMKHVWGAKGAJNTP44P0STB1IQRON50JDAKYY0ABPYA1";
    MutableLiveData<String> latlon = new MutableLiveData<String>();
    LiveData<List<Compras>> restaurantOnLatLon;

    public CompraViewModel(@NonNull Application application) {
        super(application);
        restaurantRepository = new ComprasRepositorio(application);
        restaurantOnLatLon = Transformations.switchMap(latlon, new Function<String, LiveData<List<Compras>>>() {
            @Override
            public LiveData<List<Compras>> apply(String input) {
                return restaurantRepository.getAllModel(input);
            }
        });
    }

    public void update(Compras restaurantModel) {
        restaurantRepository.update(restaurantModel);
    }

    public LiveData<List<Compras>> getRestaurantOnLatLon() {
        return restaurantOnLatLon;
    }

    @SuppressLint("MissingPermission")
    public void getLocation() {
        try {
            FusedLocationProviderClient fusedLocationClient = LocationServices.
                    getFusedLocationProviderClient(getApplication());
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        String ll = location.getLatitude() + "," + location.getLongitude();
                        getData(ll);
                        latlon.postValue(ll);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getData(final String ll) {
        final CompraApi.FourSquareService list = CompraApi.getFourSquareService();
        Call<FourSquareResource> call = list.getForsquareResponse(client_id, client_secret, "20210303", ll, "restaurant");
        call.enqueue(new Callback<FourSquareResource>() {
            @Override
            public void onResponse(Call<FourSquareResource> call, Response<FourSquareResource> response) {
                FourSquareResource responce = response.body();
                List<Item> item = responce.response.groups.get(0).items;
                final List<Compras> listModel = new ArrayList<>();
                for (int i = 0; i < item.size(); i++) {
                    Compras model = new Compras();
                    model.name = item.get(i).venue.name;
                    model.comprasId = item.get(i).venue.id;
                    model.address = item.get(i).venue.location.address;
                    model.latlong = ll;
                    listModel.add(model);
                }
                if (response.isSuccessful()) {
                    restaurantRepository.insert(listModel);
                }
            }

            @Override
            public void onFailure(Call<FourSquareResource> call, Throwable t) {

                    Log.d("API", "onERROR" + t.getMessage());
                }





        });

    }



    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double lat = location.getLatitude();
        double lan = location.getLongitude();
        String ll = lat + "," + lan;
        getData(ll);
        latlon.postValue(ll);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}
