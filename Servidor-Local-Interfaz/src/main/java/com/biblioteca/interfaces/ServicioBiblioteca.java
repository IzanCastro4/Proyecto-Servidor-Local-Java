package com.biblioteca.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import com.biblioteca.modelo.Libro;

public interface ServicioBiblioteca extends Remote {
    List<Libro> buscarLibros(String criterio, String valor) throws RemoteException;
    boolean agregarLibro(String titulo, String autor, String categoria) throws RemoteException;
    boolean actualizarLibro(String tituloOriginal, String nuevoTitulo, String nuevoAutor, String nuevaCategoria) throws RemoteException;
    boolean eliminarLibro(String titulo) throws RemoteException;
    List<Libro> listarTodos() throws RemoteException;
}