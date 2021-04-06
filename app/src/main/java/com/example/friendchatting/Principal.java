package com.example.friendchatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.friendchatting.Peds.Estado;
import com.example.friendchatting.Peds.Users;
import com.example.friendchatting.adapters.AdapterPages;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Principal extends AppCompatActivity {

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference_user = database.getReference("Users").child(firebaseUser.getUid());
    DatabaseReference ref_soli_cont = database.getReference("Contador").child(firebaseUser.getUid());
    DatabaseReference ref_estado = database.getReference("Estado").child(firebaseUser.getUid());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        ViewPager2 view2 = findViewById(R.id.viewPage);
        view2.setAdapter(new AdapterPages(this));

        final TabLayout tabLayout = findViewById(R.id.tabla);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, view2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Usuarios");
                        tab.setIcon(R.drawable.ic_users);
                        break;
                    case 1:
                        tab.setText("Chats");
                        tab.setIcon(R.drawable.ic_chat);
                        break;
                    case 2:
                        tab.setText("Solicitudes");
                        tab.setIcon(R.drawable.ic_contacto);
                        final BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                        badgeDrawable.setBackgroundColor(
                                ContextCompat.getColor(getApplicationContext(), R.color.colorAccent)
                        );
                        badgeDrawable.setVisible(true);
                        //badgeDrawable.setNumber(2);
                        ref_soli_cont.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    Integer count = snapshot.getValue(Integer.class);
                                    badgeDrawable.setVisible(true);
                                    if(count.equals("0")){

                                        badgeDrawable.setVisible(false);
                                    }else{
                                        badgeDrawable.setNumber(count);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        break;
                    case 3:
                        tab.setText("Mis solicitudes");
                        tab.setIcon(R.drawable.ic_baseline_people_outline_24);
                        break;

                }
            }
        });

        tabLayoutMediator.attach();
        view2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position){
                super.onPageSelected(position);
                BadgeDrawable badgeDrawable = tabLayout.getTabAt(position).getOrCreateBadge();
                badgeDrawable.setVisible(false);

                if(position==2){
                    contadorcero();
                }
            }
        });

        final FirebaseUser usuarios = FirebaseAuth.getInstance().getCurrentUser();

        usuario();


    }

    private void estadoUser(final String estado) {
        ref_estado.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Estado st = new Estado(estado, "","","");
                ref_estado.setValue(st);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        estadoUser("conectado");
    }

    @Override
    protected void onPause() {
        super.onPause();
        estadoUser("desconectado");
        cogerFecha();
    }

    private void cogerFecha() {
        final Calendar c = Calendar.getInstance();
        final SimpleDateFormat format = new SimpleDateFormat("HH:MM");
        final SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");

        ref_estado.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ref_estado.child("fecha").setValue(date.format(c.getTime()));
                ref_estado.child("hora").setValue(format.format(c.getTime()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void contadorcero(){
        ref_soli_cont.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ref_soli_cont.setValue(0);
                    Toast.makeText(Principal.this, "Es 0", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void usuario() {
        reference_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Users us = new Users(
                        firebaseUser.getUid(),
                            firebaseUser.getDisplayName(),
                            firebaseUser.getEmail(),
                            firebaseUser.getPhotoUrl().toString(),
                            "desconectado",
                            "2/02/2021",
                            "12:23",
                            0,0,"+34690127634"
                    );
                    reference_user.setValue(us);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.boton_cerrar:
                AuthUI.getInstance().signOut(this).addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                finish();
                                Toast.makeText(Principal.this, "Cerrando sesion", Toast.LENGTH_SHORT).show();
                                login();

                            }
                        });

            break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void login() {
        Intent princi = new Intent(this, MainActivity.class);
        princi.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(princi);
    }
}