package com.biblioteca.controlador;

import com.biblioteca.interfaces.ServicioBiblioteca;
import com.biblioteca.modelo.Libro;
import com.biblioteca.util.Config;
import com.biblioteca.util.Logger;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class ClienteBiblioteca {
    private ServicioBiblioteca servicio;
    private boolean conectado;
    private final List<ClientEventListener> listeners;
    private final String host;
    private final int puerto;
    private int intentosReconexion;
    private static final int MAX_INTENTOS_RECONEXION = 3;

    public interface ClientEventListener {
        void onConnectionStatusChange(boolean isConnected);
        void onError(String message);
    }

    public ClienteBiblioteca() {
        this.conectado = false;
        this.listeners = new ArrayList<>();
        this.host = Config.getHost();
        this.puerto = Config.getPuerto();
        this.intentosReconexion = 0;
    }

    public void addClientEventListener(ClientEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeClientEventListener(ClientEventListener listener) {
        listeners.remove(listener);
    }

    private void notifyConnectionStatus() {
        for (ClientEventListener listener : listeners) {
            listener.onConnectionStatusChange(conectado);
        }
    }

    private void notifyError(String message) {
        Logger.error(message, null);
        for (ClientEventListener listener : listeners) {
            listener.onError(message);
        }
    }

    public void conectar() throws Exception {
        try {
            Registry registry = LocateRegistry.getRegistry(host, puerto);
            servicio = (ServicioBiblioteca) registry.lookup("ServicioBiblioteca");
            conectado = true;
            intentosReconexion = 0;
            Logger.info("Cliente conectado a " + host + ":" + puerto);
            notifyConnectionStatus();
        } catch (Exception e) {
            conectado = false;
            Logger.error("Error de conexión", e);
            notifyConnectionStatus();
            throw new Exception("Error al conectar con el servidor: " + e.getMessage());
        }
    }

    public void desconectar() {
        servicio = null;
        conectado = false;
        Logger.info("Cliente desconectado");
        notifyConnectionStatus();
    }

    public boolean estaConectado() {
        return conectado;
    }

    private void verificarConexion() throws Exception {
        if (!conectado || servicio == null) {
            throw new Exception("No hay conexión con el servidor");
        }
    }

    private void manejarErrorConexion(Exception e) throws Exception {
        conectado = false;
        notifyConnectionStatus();
        
        if (intentosReconexion < MAX_INTENTOS_RECONEXION) {
            intentosReconexion++;
            Logger.info("Intento de reconexión " + intentosReconexion + " de " + MAX_INTENTOS_RECONEXION);
            try {
                conectar();
            } catch (Exception ex) {
                throw new Exception("Error de conexión persistente después de " + intentosReconexion + " intentos");
            }
        } else {
            throw new Exception("Se superó el número máximo de intentos de reconexión");
        }
    }

    public List<Libro> buscarLibros(String criterio, String valor) throws Exception {
        verificarConexion();
        try {
            List<Libro> resultados = servicio.buscarLibros(criterio, valor);
            Logger.info("Búsqueda realizada - Criterio: " + criterio + ", Valor: " + valor);
            return resultados;
        } catch (Exception e) {
            String mensaje = "Error al buscar libros: " + e.getMessage();
            notifyError(mensaje);
            manejarErrorConexion(e);
            throw new Exception(mensaje);
        }
    }

    public List<Libro> listarTodos() throws Exception {
        verificarConexion();
        try {
            List<Libro> libros = servicio.listarTodos();
            Logger.info("Listado completo de libros obtenido");
            return libros;
        } catch (Exception e) {
            String mensaje = "Error al listar libros: " + e.getMessage();
            notifyError(mensaje);
            manejarErrorConexion(e);
            throw new Exception(mensaje);
        }
    }

    public boolean agregarLibro(String titulo, String autor, String categoria) throws Exception {
        verificarConexion();
        try {
            boolean resultado = servicio.agregarLibro(titulo, autor, categoria);
            if (resultado) {
                Logger.info("Libro agregado: " + titulo);
            }
            return resultado;
        } catch (Exception e) {
            String mensaje = "Error al agregar libro: " + e.getMessage();
            notifyError(mensaje);
            manejarErrorConexion(e);
            throw new Exception(mensaje);
        }
    }

    public boolean actualizarLibro(String tituloOriginal, String nuevoTitulo, 
            String nuevoAutor, String nuevaCategoria) throws Exception {
        verificarConexion();
        try {
            boolean resultado = servicio.actualizarLibro(tituloOriginal, nuevoTitulo, 
                                                       nuevoAutor, nuevaCategoria);
            if (resultado) {
                Logger.info("Libro actualizado: " + tituloOriginal + " -> " + nuevoTitulo);
            }
            return resultado;
        } catch (Exception e) {
            String mensaje = "Error al actualizar libro: " + e.getMessage();
            notifyError(mensaje);
            manejarErrorConexion(e);
            throw new Exception(mensaje);
        }
    }

    public boolean eliminarLibro(String titulo) throws Exception {
        verificarConexion();
        try {
            boolean resultado = servicio.eliminarLibro(titulo);
            if (resultado) {
                Logger.info("Libro eliminado: " + titulo);
            }
            return resultado;
        } catch (Exception e) {
            String mensaje = "Error al eliminar libro: " + e.getMessage();
            notifyError(mensaje);
            manejarErrorConexion(e);
            throw new Exception(mensaje);
        }
    }

    // Métodos de utilidad para pruebas y depuración
    public void simularDesconexion() {
        desconectar();
        notifyError("Simulación de desconexión");
    }

    public void reiniciarContadorReconexion() {
        intentosReconexion = 0;
    }

    public int getIntentosReconexion() {
        return intentosReconexion;
    }

    public String getHost() {
        return host;
    }

    public int getPuerto() {
        return puerto;
    }
}