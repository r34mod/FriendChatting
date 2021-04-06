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
import com.example.friendchatting.Peds.Users;
import com.example.friendchatting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class AdapterCharList extends RecyclerView.Adapter<AdapterCharList.viewHolderApdaterChatList> {


    List<Users> usersList;
    Context context;

    FirebaseUser fireUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    SharedPreferences pref;

    public AdapterCharList(List<Users> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolderApdaterChatList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat, parent,false);
        viewHolderApdaterChatList holderApdater = new viewHolderApdaterChatList(view);

        return holderApdater;
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolderApdaterChatList holder, int position) {
        final Users us = usersList.get(position);
        final Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);

        holder.txtUser.setText(us.getNombre());
        Glide.with(context).load(us.getPhoto()).into(holder.img_user);

        DatabaseReference ref_soli = database.getReference("Solicitudes").child(fireUser.getUid());
        ref_soli.child(us.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String estado = snapshot.child("estado").getValue(String.class);
                if(snapshot.exists()){
                    if(estado.equals("amigos")){
                        holder.cardView.setVisibility(View.VISIBLE);
                    }else{
                        holder.cardView.setVisibility(View.GONE);
                    }
                }else{
                    holder.cardView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        final Calendar c = Calendar.getInstance();
        final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        DatabaseReference ref_estado = database.getReference("Estado").child(us.getId());
        ref_estado.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String estado = snapshot.child("estado").getValue(String.class);
                String fecha = snapshot.child("fecha").getValue(String.class);
                String hora = snapshot.child("hora").getValue(String.class);

                if(snapshot.exists()){
                    if(estado.equals("conectado")){

                        holder.conecTextView.setVisibility(View.VISIBLE);
                        holder.icon_conect.setVisibility(View.VISIBLE);
                        holder.descTextView.setVisibility(View.GONE);
                        holder.icon_desc.setVisibility(View.GONE);
                    }else{
                        holder.conecTextView.setVisibility(View.GONE);
                        holder.icon_conect.setVisibility(View.GONE);
                        holder.descTextView.setVisibility(View.VISIBLE);
                        holder.icon_desc.setVisibility(View.VISIBLE);

                        if(fecha.equals(format.format(c.getTime()))){
                            holder.descTextView.setText("Ult. vez hoy a las "+hora);
                        }else{
                            holder.descTextView.setText("Ult. vez "+fecha+ " a las "+hora);
                        }
                    }
                }else{
                    holder.descTextView.setText("Fecha");
                    holder.icon_desc.setVisibility(View.VISIBLE);
                    holder.descTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.cardView.setOnClickListener(new View.OnClickListener() {
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

        //Hacemos una refencia a los usuarios y despues a la ID del usuario especifico. Creamos una soolicitud y mira la id del usuario q nos la envio



    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class viewHolderApdaterChatList extends RecyclerView.ViewHolder{

        TextView txtUser;
        ImageView img_user;
        CardView cardView;
        TextView descTextView;
        TextView conecTextView;
        ImageView icon_conect;
        ImageView icon_desc;

        public viewHolderApdaterChatList(@NonNull View itemView) {

            super(itemView);
            txtUser = itemView.findViewById(R.id.textoUser);
            img_user = itemView.findViewById(R.id.img_profile);
            cardView = itemView.findViewById(R.id.carView);
            conecTextView = itemView.findViewById(R.id.conectado);
            descTextView = itemView.findViewById(R.id.desconectado);
            icon_conect = itemView.findViewById(R.id.iconoOn);
            icon_desc = itemView.findViewById(R.id.iconoOff);


        }
    }
}
