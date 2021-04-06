package com.example.friendchatting.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendchatting.Peds.Chats;
import com.example.friendchatting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

//Attempt to invoke virtual method 'void android.widget.TextView.setText(java.lang.CharSequence)' on a null object reference
public class AdapChat extends RecyclerView.Adapter<AdapChat.viewHolderAdapter> {
    List<Chats> chatsList;
    Context context;

    public static final int MSG_DRCH=1;
    public static final int MSG_IZQ=0;
    Boolean soloDrch = false;
    FirebaseUser firebaseUser;


    public AdapChat(List<Chats> chatsList, Context context) {
        this.chatsList = chatsList;
        this.context = context;
    }


    @NonNull
    @Override
    public viewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_DRCH){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_derecha, parent,false);
            return new AdapChat.viewHolderAdapter(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.chat_izquierda, parent, false);
            return new AdapChat.viewHolderAdapter(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderAdapter holder, int position) {
        Chats chats = chatsList.get(position);
       // Attempt to invoke virtual method 'void android.widget.TextView.setText(java.lang.CharSequence)' on a null object reference
        holder.tv_msg.setText(chats.getMensaje());

        if(soloDrch){
            if(chats.getVisto().equals("si")){
                holder.entregado.setVisibility(View.GONE);
                holder.leido.setVisibility(View.VISIBLE);
            }else{
                holder.leido.setVisibility(View.GONE);
                holder.entregado.setVisibility(View.VISIBLE);
            }

            final Calendar c = Calendar.getInstance();
            final SimpleDateFormat sformat = new SimpleDateFormat("dd/MM/yyyy");
            if(chats.getFecha().equals(sformat.format(c.getTime()))){
                holder.tv_fecha.setText("Hoy "+chats.getHora());
            }else{
                holder.tv_fecha.setText(chats.getFecha()+" "+chats.getHora());
            }
        }

    }

    @Override
    public int getItemCount() {

        return chatsList.size();
    }

    public class viewHolderAdapter extends RecyclerView.ViewHolder{

        TextView tv_msg;
        TextView tv_fecha;
        ImageView entregado;
        ImageView leido;
        public viewHolderAdapter(@NonNull View itemView) {
            super(itemView);
            tv_msg = itemView.findViewById(R.id.tv_msg);
            tv_fecha = itemView.findViewById(R.id.tv_Fecha);
            entregado = itemView.findViewById(R.id.img_enviado);
            leido = itemView.findViewById(R.id.img_leido);
        }

    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(chatsList.get(position).getEnvia().equals(firebaseUser.getUid())){
            soloDrch=true;
            return MSG_DRCH;
        }else{
            soloDrch=false;
            return  MSG_IZQ;
        }
    }
}
