package com.example.friendchatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.friendchatting.Peds.Chats;
import com.example.friendchatting.Peds.Estado;
import com.example.friendchatting.adapters.AdapChat;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.EmojiTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;

public class Mensajes extends AppCompatActivity {

    CircleImageView img_user;
    TextView nombre;
    ImageView ic_conect;
    ImageView ic_desc;
    SharedPreferences preferences;


    FirebaseUser fireUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref_estado = database.getReference("Estado").child(fireUser.getUid());
    DatabaseReference ref_chat = database.getReference("Chats");


    EditText txtMensaje;
    ImageButton img_enviar;
    ImageButton img_emojis;

    String id_chat_general;
    Boolean online = false;

    RecyclerView recyclerViewChat;
    RelativeLayout relativeMsg;
    AdapChat adapChat;
    ArrayList<Chats> chatsArrayList;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes);
        Toolbar tolbar = findViewById(R.id.tolbar);
        setSupportActionBar(tolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferences = getApplicationContext().getSharedPreferences("usuario_sp", MODE_PRIVATE);
        img_user = findViewById(R.id.img_profile);
        nombre = findViewById(R.id.textoUser);
        ic_conect = findViewById(R.id.iconoOn);
        ic_desc = findViewById(R.id.iconoOff);

        String usuario = getIntent().getExtras().getString("nombre");
        String foto = getIntent().getExtras().getString("img");
        final String id = getIntent().getExtras().getString("id_user");
        id_chat_general = getIntent().getExtras().getString("id_unico");

        colocarVisto();

        txtMensaje = findViewById(R.id.txtMensaje);
        img_enviar = findViewById(R.id.btn_enviar);
        img_emojis = findViewById(R.id.bnt_emoji);
        relativeMsg = findViewById(R.id.mensajes_view);



        img_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String mensaje = txtMensaje.getText().toString();
                if(mensaje.isEmpty()){
                    Toast.makeText(Mensajes.this, "Mensaje vacio", Toast.LENGTH_SHORT).show();;
                }else{
                    final Calendar c = Calendar.getInstance();
                    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                    final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    String id_push = ref_chat.push().getKey();

                    if(online){
                        Chats chat = new Chats(fireUser.getUid(), id,mensaje,"si",  format.format(c.getTime()),simpleDateFormat.format(c.getTime()), id_push);
                        //Chats chat = new Chats(id_push,fireUser.getUid(), mensaje,"si",  format.format(c.getTime()),simpleDateFormat.format(c.getTime()), id);
                        ref_chat.child(id_chat_general).child(id_push).setValue(chat);
                        txtMensaje.setText("");
                    }else{
                        Chats chat = new Chats(fireUser.getUid(),id, mensaje, "no",  format.format(c.getTime()),simpleDateFormat.format(c.getTime()),id_push);
                        ref_chat.child(id_chat_general).child(id_push).setValue(chat);
                        txtMensaje.setText("");
                    }


                }

           }
        });




        final String id_user_sp = preferences.getString("usuario_sp","");
        nombre.setText(usuario);
        Glide.with(this).load(foto).into(img_user);

        final DatabaseReference ref = database.getReference("Estado").child(id_user_sp).child("chatting");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String chatting = snapshot.getValue(String.class);
                if(snapshot.exists()){
                    if(chatting.equals(fireUser.getUid())){
                        online = true;
                       // ic_conect.setVisibility(View.VISIBLE);
                        //ic_desc.setVisibility(View.GONE);
                    }else{
                        online = false;
                       // java.lang.NullPointerException: Attempt to invoke virtual method 'void android.widget.ImageView.setVisibility(int)' on a null object reference
                       // ic_conect.setVisibility(View.GONE);
                       // ic_desc.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerViewChat = findViewById(R.id.recycle);
        recyclerViewChat.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewChat.setLayoutManager(linearLayoutManager);

        chatsArrayList = new ArrayList<>();
        adapChat = new AdapChat(chatsArrayList, this);
        recyclerViewChat.setAdapter(adapChat);

        leerMsg();
    }

    private void leerMsg() {

        ref_chat.child(id_chat_general).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    chatsArrayList.removeAll(chatsArrayList);
                    for(DataSnapshot snapshot1: snapshot.getChildren()){
                        Chats chat = snapshot1.getValue(Chats.class);
                        chatsArrayList.add(chat);
                        setScroll();
                    }
                    adapChat.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setScroll() {
        recyclerViewChat.scrollToPosition(adapChat.getItemCount()-1);
    }

    private  void colocarVisto(){
        ref_chat.child(id_chat_general).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Chats chat = snapshot1.getValue(Chats.class);
                    if(chat.getRecibe().equals(fireUser.getUid())){
                        ref_chat.child(id_chat_general).child(chat.getId()).child("visto").setValue("si");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void estadoUser(final String estado) {
        ref_estado.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String id_user_sp = preferences.getString("usuario_sp","");
                Estado st = new Estado(estado, "","",id_user_sp);
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





}