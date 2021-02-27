package com.example.omdbapi;

public class Pelicula {



    public static String Titulo;
    public static String ImdbId;
    public static String Año;

    public Pelicula() {
    }

    public Pelicula(String Titulo,String ImdbId,String Año) {
        this.Titulo=Titulo;
        this.Año=Año;
        this.ImdbId=ImdbId;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getImdbId() {
        return ImdbId;
    }

    public void setImdbId(String imdbId) {
        ImdbId = imdbId;
    }

    public String getAño() {
        return Año;
    }

    public void setAño(String año) {
        Año = año;
    }
}
