package com.biblioteca.modelo;

import java.io.Serializable;

public class Libro implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String titulo;
    private String autor;
    private String categoria;

    public Libro(String titulo, String autor, String categoria) {
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
    }

    // Getters y setters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    @Override
    public String toString() {
        return "Título: " + titulo + ", Autor: " + autor + ", Categoría: " + categoria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Libro libro = (Libro) o;
        return titulo.equals(libro.titulo) && 
               autor.equals(libro.autor) && 
               categoria.equals(libro.categoria);
    }

    @Override
    public int hashCode() {
        return titulo.hashCode() + autor.hashCode() + categoria.hashCode();
    }
}