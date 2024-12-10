package com.biblioteca.controlador;

import com.biblioteca.dao.CapaAccesoDatos;
import com.biblioteca.interfaces.ServicioBiblioteca;
import com.biblioteca.modelo.Libro;
import com.biblioteca.util.Config;
import com.biblioteca.util.Logger;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ServidorBiblioteca implements ServicioBiblioteca {
    private final CapaAccesoDatos capaAcceso;
    private Registry registro;
    private boolean activo;
    private final List<ServerEventListener> listeners;
    private final int puerto;

    public interface ServerEventListener {
        void onLogMessage(String message);
        void onStatusChange(boolean isActive);
    }

    public ServidorBiblioteca() {
        this.capaAcceso = new CapaAccesoDatos();
        this.listeners = new ArrayList<>();
        this.activo = false;
        this.puerto = Config.getPuerto();
    }

    public void addServerEventListener(ServerEventListener listener) {
        listeners.add(listener);
    }

    private void notifyLog(String message) {
        Logger.info(message);
        for (ServerEventListener listener : listeners) {
            listener.onLogMessage(message);
        }
    }

    private void notifyStatusChange() {
        for (ServerEventListener listener : listeners) {
            listener.onStatusChange(activo);
        }
    }

    public void iniciar() throws Exception {
        if (!activo) {
            try {
                ServicioBiblioteca stub = (ServicioBiblioteca) 
                    UnicastRemoteObject.exportObject(this, 0);
                registro = LocateRegistry.createRegistry(puerto);
                registro.rebind("ServicioBiblioteca", stub);
                activo = true;
                notifyLog("Servidor iniciado en puerto " + puerto);
                notifyStatusChange();
            } catch (Exception e) {
                Logger.error("Error al iniciar servidor", e);
                throw e;
            }
        }
    }

    public void parar() throws Exception {
        if (activo && registro != null) {
            try {
                registro.unbind("ServicioBiblioteca");
                UnicastRemoteObject.unexportObject(this, true);
                activo = false;
                notifyLog("Servidor detenido");
                notifyStatusChange();
            } catch (Exception e) {
                Logger.error("Error al detener servidor", e);
                throw e;
            }
        }
    }

    public boolean isActivo() {
        return activo;
    }

    @Override
    public List<Libro> buscarLibros(String criterio, String valor) throws RemoteException {
        try {
            List<Libro> todosLibros = capaAcceso.obtenerTodos();
            List<Libro> resultados = new ArrayList<>();
            
            for (Libro libro : todosLibros) {
                switch (criterio.toLowerCase()) {
                    case "titulo":
                        if (libro.getTitulo().toLowerCase().contains(valor.toLowerCase())) {
                            resultados.add(libro);
                        }
                        break;
                    case "autor":
                        if (libro.getAutor().toLowerCase().contains(valor.toLowerCase())) {
                            resultados.add(libro);
                        }
                        break;
                    case "categoria":
                        if (libro.getCategoria().toLowerCase().contains(valor.toLowerCase())) {
                            resultados.add(libro);
                        }
                        break;
                }
            }
            
            notifyLog("Búsqueda realizada - Criterio: " + criterio + ", Valor: " + valor);
            return resultados;
        } catch (Exception e) {
            Logger.error("Error en búsqueda", e);
            throw new RemoteException("Error al buscar libros", e);
        }
    }

    @Override
    public boolean agregarLibro(String titulo, String autor, String categoria) throws RemoteException {
        try {
            boolean resultado = capaAcceso.agregarLibro(new Libro(titulo, autor, categoria));
            if (resultado) {
                notifyLog("Libro agregado: " + titulo);
            }
            return resultado;
        } catch (Exception e) {
            Logger.error("Error al agregar libro", e);
            throw new RemoteException("Error al agregar libro", e);
        }
    }

    @Override
    public boolean actualizarLibro(String tituloOriginal, String nuevoTitulo, 
            String nuevoAutor, String nuevaCategoria) throws RemoteException {
        try {
            Libro libroActualizado = new Libro(nuevoTitulo, nuevoAutor, nuevaCategoria);
            boolean resultado = capaAcceso.actualizarLibro(tituloOriginal, libroActualizado);
            if (resultado) {
                notifyLog("Libro actualizado: " + tituloOriginal + " -> " + nuevoTitulo);
            }
            return resultado;
        } catch (Exception e) {
            Logger.error("Error al actualizar libro", e);
            throw new RemoteException("Error al actualizar libro", e);
        }
    }

    @Override
    public boolean eliminarLibro(String titulo) throws RemoteException {
        try {
            boolean resultado = capaAcceso.eliminarLibro(titulo);
            if (resultado) {
                notifyLog("Libro eliminado: " + titulo);
            }
            return resultado;
        } catch (Exception e) {
            Logger.error("Error al eliminar libro", e);
            throw new RemoteException("Error al eliminar libro", e);
        }
    }

    @Override
    public List<Libro> listarTodos() throws RemoteException {
        try {
            List<Libro> libros = capaAcceso.obtenerTodos();
            notifyLog("Listado completo de libros solicitado");
            return libros;
        } catch (Exception e) {
            Logger.error("Error al listar libros", e);
            throw new RemoteException("Error al listar libros", e);
        }
    }
}