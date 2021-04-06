package com.example.friendchatting.fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.friendchatting.Peds.Users;
import com.example.friendchatting.R;
import com.example.friendchatting.adapters.AdapterCharList;
import com.example.friendchatting.adapters.adapUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class chatsFragment extends Fragment {



    public chatsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ProgressBar bar;
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        View view = inflater.inflate(R.layout.fragment_chats, container, false);


        bar = view.findViewById(R.id.progres);
        assert user!=null;



        final RecyclerView recyclerView;
        final ArrayList<Users> usersArrayList;
        final AdapterCharList adapChat;
        LinearLayoutManager linearLayoutManager;

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView = view.findViewById(R.id.user_recycle);
        recyclerView.setLayoutManager(linearLayoutManager);

        usersArrayList=new ArrayList<>();
        adapChat = new AdapterCharList(usersArrayList, getContext());
        recyclerView.setAdapter(adapChat);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference= database.getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    recyclerView.setVisibility(View.VISIBLE);
                    bar.setVisibility(View.GONE);
                    usersArrayList.removeAll(usersArrayList);
                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                        Users user = snapshot1.getValue(Users.class);
                        usersArrayList.add(user);
                    }
                    adapChat.notifyDataSetChanged();

                }else{
                    bar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "No existe usuarios", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;

    }
}