package com.example.friendchatting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.friendchatting.Modelo.Compras;
import com.example.friendchatting.Peds.CompraViewModel;
import com.example.friendchatting.adapters.ComprasAdap;
import com.example.friendchatting.fragmentos.commentsFragment;

import java.util.ArrayList;
import java.util.List;

public class ComprasActivity extends AppCompatActivity implements ComprasAdap.ThumbListener {

    ImageButton button, buttonSalir;
    //RecyclerView
    RecyclerView recyclerView;
    //ViewModel
    public CompraViewModel restaurantViewModel;
    public ComprasAdap restaurantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compras);
        button = findViewById(R.id.GPS);
        buttonSalir = findViewById(R.id.salir);
        //recyclerview
        recyclerView = findViewById(R.id.RestaurantList);
        //Attempt to invoke virtual method 'void androidx.recyclerview.widget.RecyclerView.setLayoutManager(androidx.recyclerview.widget.RecyclerView$LayoutManager)' on a null object reference
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        List<Compras> modelList = new ArrayList<>();
        restaurantAdapter = new ComprasAdap(this, modelList, this);
        //viewModel
        //restaurantViewModel = new ViewModelProvider(this).get(CompraViewModel.class);
        restaurantViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(CompraViewModel.class);
        restaurantViewModel.getRestaurantOnLatLon().observe(this, new Observer<List<Compras>>() {
            @Override
            public void onChanged(List<Compras> models) {
                restaurantAdapter.getAllModels(models);
                recyclerView.setAdapter(restaurantAdapter);
                //Toast.makeText(MainActivity.this, "Updating...", Toast.LENGTH_SHORT).show();
            }
        });
        //Permisos
        if (ContextCompat.checkSelfPermission(ComprasActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ComprasActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        } else {
            restaurantViewModel.getLocation();
        }

        //Buscando tu localizacion actual
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ComprasActivity.this, "Localizando...", Toast.LENGTH_LONG).show();
                restaurantViewModel.getLocation();
            }
        });


        //Salir al main
        buttonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              main();
            }
        });


    }

    @Override
    public void onThumb(Compras restaurantModel) {
        restaurantViewModel.update(restaurantModel);
    }

    @Override
    public void onComments(Compras restaurantModel) {
        commentsFragment addPhotoBottomDialogFragment =
                commentsFragment.newInstance(restaurantModel.comprasId);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                "comment");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                //Si se cancela el array resultaria ser 0
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permisos permitidos

                    restaurantViewModel.getLocation();
                } else {
                    //Permiso denegado

                    Toast.makeText(ComprasActivity.this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void main() {
        Intent a = new Intent(this, Ajustes.class);
        startActivity(a);
    }
}