package com.example.odontobank.Model;

public class Publicacion {
    String uid_estudiante;
    String nombre_estudiante;
    String atencion_medica;
    String telefono_estudiante;
    String latitud_publicacion;
    String longitud_publicacion;
    String activado;

    public Publicacion(String uid_estudiante, String telefono_estudiante, String latitud_publicacion, String longitud_publicacion) {
        this.uid_estudiante = uid_estudiante;
        this.nombre_estudiante = nombre_estudiante;
        this.telefono_estudiante = telefono_estudiante;
        this.latitud_publicacion = latitud_publicacion;
        this.longitud_publicacion = longitud_publicacion;
        this.activado = activado;
    }

    public String getUid_estudiante() {
        return uid_estudiante;
    }

    public void setUid_estudiante(String uid_estudiante) {
        this.uid_estudiante = uid_estudiante;
    }

    public String getNombre_estudiante() {
        return nombre_estudiante;
    }

    public void setNombre_estudiante() {
        this.nombre_estudiante = nombre_estudiante;
    }

    public String getAtencion_medica() {
        return atencion_medica;
    }

    public void setAtencion_medica() {
        this.atencion_medica = atencion_medica;
    }

    public String getTelefono_estudiante() {
        return telefono_estudiante;
    }

    public void setTelefono_estudiante(String telefono_estudiante) {
        this.telefono_estudiante = telefono_estudiante;
    }

    public String getLatitud_publicacion() {
        return latitud_publicacion;
    }

    public void setLatitud_publicacion(String latitud_publicacion) {
        this.latitud_publicacion = latitud_publicacion;
    }

    public String getLongitud_publicacion() {
        return longitud_publicacion;
    }

    public void setLongitud_publicacion(String longitud_publicacion) {
        this.longitud_publicacion = longitud_publicacion;
    }

    public String getActivado() {
        return activado;
    }

    public void setActivado(String activado) {
        this.activado = activado;
    }
}
