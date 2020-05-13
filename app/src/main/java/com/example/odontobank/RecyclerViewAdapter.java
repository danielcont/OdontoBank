package com.example.odontobank;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.odontobank.Model.Publicacion;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.PublicacionViewHolder> {

    private final List<Publicacion> arrayPublicacion;
    private OnPublicacionListener mOnPublicacionListener;

    RecyclerViewAdapter(List<Publicacion> arrayPublicacion, OnPublicacionListener onPublicacionListener) {
        this.arrayPublicacion = arrayPublicacion;
        this.mOnPublicacionListener = onPublicacionListener;
    }

    public class PublicacionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        TextView nombre;
        CircleImageView Imagencard;
        TextView atencion;
        TextView ubicacion_publicacion;
        OnPublicacionListener onPublicacionListener;

        PublicacionViewHolder(View itemView, OnPublicacionListener onPublicacionListener) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            nombre = itemView.findViewById(R.id.cvNombre);
            Imagencard = itemView.findViewById(R.id.cvImagen);
            atencion = itemView.findViewById(R.id.cvAtencionMedica);
            ubicacion_publicacion = itemView.findViewById(R.id.cvUbicacion);
            this.onPublicacionListener = onPublicacionListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPublicacionListener.onPublicacionClick(getAdapterPosition(), arrayPublicacion.get(getAdapterPosition()));
        }

    }

    @Override
    public PublicacionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        PublicacionViewHolder cvh = new PublicacionViewHolder(view, mOnPublicacionListener);

        return cvh;
    }

    @Override
    public void onBindViewHolder(PublicacionViewHolder holder, int position) {
        holder.nombre.setText(arrayPublicacion.get(position).getNombre_estudiante());
        holder.atencion.setText(arrayPublicacion.get(position).getAtencion_medica());

        Glide.with(holder.itemView.getContext())
                .load(arrayPublicacion.get(position).getUid_estudiante())
                .into(holder.Imagencard);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return arrayPublicacion.size();
    }

    public interface OnPublicacionListener {
        void onPublicacionClick(int position, Publicacion publicacion_click);
    }
}