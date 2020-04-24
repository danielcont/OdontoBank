package com.example.odontobank.Model;

public class Usuario {
    private String usuario_email;
    private String imagenURL;
    private String apellido;
    private String nombre;
    private String escuela;

    public Usuario(String usuario_email, String imagenURL ,String apellido, String nombre, String escuela) {
        this.usuario_email = usuario_email;
        this.imagenURL = imagenURL;
        this.apellido = apellido;
        this.nombre = nombre;
        this.escuela = escuela;
    }

    public Usuario() {

    }

    public String getUsuario_email() {
        return usuario_email;
    }

    public void setUsuario_email(String usuario_email) {
        this.usuario_email = usuario_email;
    }

    public String getImagenURL() {
        return imagenURL;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEscuela() {
        return escuela;
    }

    public void setEscuela(String escuela) {
        this.escuela = escuela;
    }
}
