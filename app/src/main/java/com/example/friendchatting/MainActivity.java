package com.example.friendchatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth authfire;
    private  FirebaseAuth.AuthStateListener authlistener;

    public static final int SIGN_IN=1;
    List<AuthUI.IdpConfig> provider = Arrays.asList(
            new AuthUI.IdpConfig.GoogleBuilder().build(),
            new AuthUI.IdpConfig.FacebookBuilder().build());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.startTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authfire = FirebaseAuth.getInstance();
        authlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser usuario = firebaseAuth.getCurrentUser();
                if(usuario != null){
                    principal();
                    Toast.makeText(MainActivity.this, "Log in", Toast.LENGTH_SHORT).show();;
                }else{
                    startActivityForResult(
                            AuthUI.getInstance().createSignInIntentBuilder()
                    .setAvailableProviders(provider).setIsSmartLockEnabled(false).build(),SIGN_IN);
                }
            }
        };
    }
    @Override
    protected void onResume(){
        super.onResume();
        authfire.addAuthStateListener(authlistener);
    }
    @Override
    protected void onPause(){
        super.onPause();
        authfire.removeAuthStateListener(authlistener);
    }

    private void principal(){
        Intent princi = new Intent(this, Ajustes.class);
        princi.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(princi);
    }
}