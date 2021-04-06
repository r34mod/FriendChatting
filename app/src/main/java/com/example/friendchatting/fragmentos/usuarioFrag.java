package com.example.friendchatting.fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.friendchatting.Ajustes;
import com.example.friendchatting.Peds.Users;
import com.example.friendchatting.R;
import com.example.friendchatting.adapters.adapUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class usuarioFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    public usuarioFrag() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ProgressBar progressBar;

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        View view = inflater.inflate(R.layout.fragment_usuario, container, false);
        TextView textoUser = view.findViewById(R.id.textoUser);
        ImageView img_user = view.findViewById(R.id.img_profile);

        progressBar = view.findViewById(R.id.progres);
        assert firebaseUser!=null;
        textoUser.setText(firebaseUser.getDisplayName());
        Glide.with(this).load(firebaseUser.getPhotoUrl()).into(img_user);

        final RecyclerView recyclerView;
        final ArrayList<Users> usersArrayList;
        final adapUser adapUser;
        LinearLayoutManager linearLayoutManager;

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView = view.findViewById(R.id.user_recycle);
        recyclerView.setLayoutManager(linearLayoutManager);

        usersArrayList=new ArrayList<>();
        adapUser = new adapUser(usersArrayList, getContext());
        recyclerView.setAdapter(adapUser);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference= database.getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    recyclerView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    usersArrayList.removeAll(usersArrayList);
                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                        Users user = snapshot1.getValue(Users.class);
                        usersArrayList.add(user);
                    }
                    adapUser.notifyDataSetChanged();

                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "No existe usuarios", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        View rootView = inflater.inflate(R.layout.fragment_usuario, container, false);




        return view;
    }

}