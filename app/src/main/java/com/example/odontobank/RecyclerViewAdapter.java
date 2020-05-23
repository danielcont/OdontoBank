package com.example.odontobank;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
//import com.example.odontobank.Model.Publicacion;

import java.util.List;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.PublicacionViewHolder> {
    CardView cardView;
    ImageView imagenAtencion;

    private final List<Publicacion> arrayPublicacion;
    private OnPublicacionListener mOnPublicacionListener;

    RecyclerViewAdapter(List<Publicacion> arrayPublicacion, OnPublicacionListener onPublicacionListener) {
        this.arrayPublicacion = arrayPublicacion;
        this.mOnPublicacionListener = onPublicacionListener;
    }

    @Override
    public void onBindViewHolder(PublicacionViewHolder holder, int position) {
        holder.nombreEstudiante.setText(arrayPublicacion.get(position).getNombre());
        holder.atencionMedica.setText(arrayPublicacion.get(position).getAtencion_medica());
        holder.ubicacion_publicacion.setText(arrayPublicacion.get(position).getCiudad());

        if(arrayPublicacion.get(position).getAtencion_medica().equals("Endodoncia")) {
            imagenAtencion.setImageResource(R.drawable.endodoncia);
        } else if(arrayPublicacion.get(position).getAtencion_medica().equals("Cirugía Oral")) {
            imagenAtencion.setImageResource(R.drawable.cigugia_oral);
        } else if(arrayPublicacion.get(position).getAtencion_medica().equals("Odontología Preventiva")) {
            imagenAtencion.setImageResource(R.drawable.odontologia_preventiva);
        } else if(arrayPublicacion.get(position).getAtencion_medica().equals("Periodoncia")) {
            imagenAtencion.setImageResource(R.drawable.periodoncia);
        } else if(arrayPublicacion.get(position).getAtencion_medica().equals("Terapéutica Dental")) {
            imagenAtencion.setImageResource(R.drawable.terapeutica_dental);
        } else if(arrayPublicacion.get(position).getAtencion_medica().equals("Prótesis Dental")) {
            imagenAtencion.setImageResource(R.drawable.protesis_dental);
        } else if(arrayPublicacion.get(position).getAtencion_medica().equals("Odontología Infantil")) {
            imagenAtencion.setImageResource(R.drawable.odontologia_infantil);
        } else if(arrayPublicacion.get(position).getAtencion_medica().equals("Dolor Orofacial")) {
            imagenAtencion.setImageResource(R.drawable.dolor_orofacial);
        } else if(arrayPublicacion.get(position).getAtencion_medica().equals("Ortodoncia")) {
            imagenAtencion.setImageResource(R.drawable.ortodoncia);
        } else if(arrayPublicacion.get(position).getAtencion_medica().equals("Cariología")) {
            imagenAtencion.setImageResource(R.drawable.cariologia);
        } else if(arrayPublicacion.get(position).getAtencion_medica().equals("Implatología")) {
            imagenAtencion.setImageResource(R.drawable.implantologia);
        }

        if(!arrayPublicacion.get(position).getActivado().equals("1")) {
            holder.atencionMedica.setTextColor(Color.argb(85, 37, 58, 75));
            holder.nombreEstudiante.setTextColor(Color.argb(85, 37, 58, 75));
            imagenAtencion.setAlpha(0.4f);
            holder.ubicacion_publicacion.setTextColor(Color.argb(85, 242, 58, 95));
        }

    }

    @Override
    public PublicacionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        PublicacionViewHolder cvh = new PublicacionViewHolder(view, mOnPublicacionListener);

        return cvh;
    }

    public class PublicacionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nombreEstudiante;
        TextView atencionMedica;
        TextView ubicacion_publicacion;
        OnPublicacionListener onPublicacionListener;

        PublicacionViewHolder(View itemView, OnPublicacionListener onPublicacionListener) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            nombreEstudiante = itemView.findViewById(R.id.cvNombre);
            atencionMedica = itemView.findViewById(R.id.cvAtencionMedica);
            ubicacion_publicacion = itemView.findViewById(R.id.cvUbicacion);
            imagenAtencion = itemView.findViewById(R.id.imgAtencion);
            this.onPublicacionListener = onPublicacionListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPublicacionListener.onPublicacionClick(getAdapterPosition(), arrayPublicacion.get(getAdapterPosition()));
        }

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

class Publicacion implements Parcelable {

    public static final Creator<Publicacion> CREATOR = new Creator<Publicacion>() {
        @Override
        public Publicacion createFromParcel(Parcel in) {
            return new Publicacion(in);
        }

        @Override
        public Publicacion[] newArray(int size) {
            return new Publicacion[size];
        }
    };
    String uid;
    String nombre;
    String atencion_medica;
    String contacto;
    String latitud;
    String longitud;
    String activado;
    String ciudad;

    protected Publicacion(Parcel in) {
        uid = in.readString();
        nombre = in.readString();
        atencion_medica = in.readString();
        contacto = in.readString();
        latitud = in.readString();
        longitud = in.readString();
        activado = in.readString();
        ciudad = in.readString();
    }

    public Publicacion(String uid, String nombre, String atencion_medica, String contacto, String latitud, String longitud, String activado, String ciudad) {
        this.uid = uid;
        this.nombre = nombre;
        this.atencion_medica = atencion_medica;
        this.contacto = contacto;
        this.latitud = latitud;
        this.longitud = longitud;
        this.activado = activado;
        this.ciudad = ciudad;
    }

    public Publicacion() {}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(nombre);
        dest.writeString(atencion_medica);
        dest.writeString(contacto);
        dest.writeString(latitud);
        dest.writeString(longitud);
        dest.writeString(activado);
        dest.writeString(ciudad);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAtencion_medica() {
        return atencion_medica;
    }

    public void setAtencion_medica(String atencion_medica) {
        this.atencion_medica = atencion_medica;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getActivado() {
        return activado;
    }

    public void setActivado(String activado) {
        this.activado = activado;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}