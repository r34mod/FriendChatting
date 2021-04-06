package com.example.friendchatting.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendchatting.Modelo.Compras;
import com.example.friendchatting.R;

import org.w3c.dom.Text;

import java.util.List;

public class ComprasAdap extends RecyclerView.Adapter<ComprasAdap.ComprasHolder> {

    private Context context;
    public List<Compras> models;
    ThumbListener viewModel;

    public ComprasAdap(Context context,
                             List<Compras> models, ThumbListener viewModel) {
        this.context = context;
        this.models = models;
        this.viewModel = viewModel;

    }

    @NonNull
    @Override
    public ComprasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.design_item, parent, false);
        return new ComprasHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComprasHolder holder, int position) {
        Compras compras = models.get(position);
        holder.comprasName.setText(compras.name);
        holder.comprasDescripcion.setText(compras.address);
        //holder.compraContacto.setText(compras.contact);
        holder.rating.setImageResource(compras.thumb? R.drawable.ic_star : R.drawable.ic_baseline_star_outline_24);
    }



    public void getAllModels(List<Compras> models) {
        this.models = models;
    }

    @Override
    public int getItemCount() {
        return models.size();
    }


    public interface ThumbListener {
        void onThumb(Compras compras);

        void onComments(Compras compras);
    }

    public class ComprasHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView comprasImage;
        TextView comprasName;
        TextView comprasDescripcion;
        TextView compraContacto;
        ImageButton rating;

        public ComprasHolder(@NonNull View itemView) {
            super(itemView);
            comprasImage = itemView.findViewById(R.id.compraImage);
            comprasName = itemView.findViewById(R.id.compraName);
            comprasDescripcion = itemView.findViewById(R.id.compraDescripcion);
            compraContacto = itemView.findViewById(R.id.compraContacto);
            rating = itemView.findViewById(R.id.Rating);
            rating.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.Rating) {
                viewModel.onThumb(models.get(getAdapterPosition()));
            } else {
                viewModel.onComments(models.get(getAdapterPosition()));
            }
        }
    }





}
