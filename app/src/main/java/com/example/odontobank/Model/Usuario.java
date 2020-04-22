package com.example.odontobank.Model;

public class Usuario {
    private String usuario_email;
    private String imagenURL;

    public Usuario(String usuario_email, String imagenURL) {
        this.usuario_email = usuario_email;
        this.imagenURL = imagenURL;
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
}
