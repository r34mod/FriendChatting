package com.example.friendchatting.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.friendchatting.Mensajes;
import com.example.friendchatting.Peds.Solicitud;
import com.example.friendchatting.Peds.Users;
import com.example.friendchatting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class adapUser extends RecyclerView.Adapter<adapUser.viewHolderApdater> {


    List<Users> usersList;
    Context context;

    FirebaseUser fireUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    SharedPreferences pref;
    public adapUser(List<Users> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public adapUser.viewHolderApdater onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_users, parent,false);
        viewHolderApdater holderApdater = new viewHolderApdater(view);

        return holderApdater;
    }

    @Override
    public void onBindViewHolder(@NonNull final adapUser.viewHolderApdater holder, int position) {
        final Users us = usersList.get(position);
        final Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        Glide.with(context).load(us.getPhoto()).into(holder.img_user);
        holder.txtUser.setText(us.getNombre());
        if(us.getId().equals(fireUser.getUid())){
            holder.cardView.setVisibility(View.GONE);
        }else{
            holder.cardView.setVisibility(View.VISIBLE);
        }

        //Hacemos una refencia a los usuarios y despues a la ID del usuario especifico. Creamos una soolicitud y mira la id del usuario q nos la envio
        final DatabaseReference refBoton = database.getReference("Solicitudes").child(fireUser.getUid());

        refBoton.child(us.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String estado = snapshot.child("estado").getValue(String.class);
                if(snapshot.exists()){

                    if(estado.equals("enviado")){

                        holder.send.setVisibility(View.VISIBLE);
                        holder.add.setVisibility(View.GONE);
                        holder.amigo.setVisibility(View.GONE);
                        holder.solicitud.setVisibility(View.GONE);
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    if(estado.equals("amigos")){
                        holder.send.setVisibility(View.GONE);
                        holder.add.setVisibility(View.GONE);
                        holder.amigo.setVisibility(View.VISIBLE);
                        holder.solicitud.setVisibility(View.GONE);
                        holder.progressBar.setVisibility(View.GONE);
                    }
                    if(estado.equals("solicitud")){
                        holder.send.setVisibility(View.GONE);
                        holder.add.setVisibility(View.GONE);
                        holder.amigo.setVisibility(View.GONE);
                        holder.solicitud.setVisibility(View.VISIBLE);
                        holder.progressBar.setVisibility(View.GONE);
                    }
                }else{
                    holder.send.setVisibility(View.GONE);
                    holder.add.setVisibility(View.VISIBLE);
                    holder.amigo.setVisibility(View.GONE);
                    holder.solicitud.setVisibility(View.GONE);
                    holder.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference refe = database.getReference("Solicitudes").child(fireUser.getUid());
                refe.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Solicitud soli = new Solicitud("enviado","");
                        refe.child(us.getId()).setValue(soli);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                final DatabaseReference refeB = database.getReference("Solicitudes").child(us.getId());
                refeB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Solicitud soli = new Solicitud("solicitud","");
                        refeB.child(fireUser.getUid()).setValue(soli);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                final DatabaseReference contador = database.getReference("Contador").child(us.getId());
                contador.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Integer cont = snapshot.getValue(Integer.class);
                            if(cont==0){
                                contador.setValue(1);
                            }else{
                                contador.setValue(cont+1);
                            }
                        }else{
                            contador.setValue(1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                vibrator.vibrate(400);
            }
        });


        holder.solicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String idchat = refBoton.push().getKey();

                final DatabaseReference refe = database.getReference("Solicitudes").child(us.getId()).child(fireUser.getUid());
                refe.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Solicitud soli = new Solicitud("amigos",idchat);
                        refe.setValue(soli);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                final DatabaseReference refeB = database.getReference("Solicitudes").child(fireUser.getUid()).child(us.getId());
                refeB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       Solicitud soli = new Solicitud("amigos",idchat);
                        refeB.setValue(soli);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                vibrator.vibrate(400);
            }
        });

        holder.amigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                pref = view.getContext().getSharedPreferences("usuario_sp", Context.MODE_PRIVATE);
                final SharedPreferences.Editor edit = pref.edit();
                final DatabaseReference refe = database.getReference("Solicitudes").child(fireUser.getUid()).child(us.getId()).child("idChat");
                refe.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String id = snapshot.getValue(String.class);
                        if(snapshot.exists()){
                            Intent i = new Intent(view.getContext(), Mensajes.class);
                            i.putExtra("nombre",us.getNombre());
                            i.putExtra("img", us.getPhoto());
                            i.putExtra("id_user", us.getId());
                            i.putExtra("id_unico", id);
                            edit.putString("usuario_sp", us.getId());
                            edit.apply();
                            view.getContext().startActivity(i);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class viewHolderApdater extends RecyclerView.ViewHolder{

        TextView txtUser;
        ImageView img_user;
        CardView cardView;
        Button add;
        Button send;
        Button amigo;
        Button solicitud;
        ProgressBar progressBar;

        public viewHolderApdater(@NonNull View itemView) {

            super(itemView);
            txtUser = itemView.findViewById(R.id.textoUser);
            img_user = itemView.findViewById(R.id.img_profile);
            cardView = itemView.findViewById(R.id.carView);
            add = itemView.findViewById(R.id.button_add);
            send = itemView.findViewById(R.id.button_send);
            amigo = itemView.findViewById(R.id.button_amigos);
            solicitud = itemView.findViewById(R.id.button_solicitud);
            progressBar = itemView.findViewById(R.id.barProgres);


        }
    }
}
