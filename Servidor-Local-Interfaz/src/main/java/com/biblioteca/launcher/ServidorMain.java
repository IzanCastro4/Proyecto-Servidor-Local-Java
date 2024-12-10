// ServidorMain.java
package com.biblioteca.launcher;

import com.biblioteca.vista.InterfazServidor;
import com.biblioteca.util.Logger;

public class ServidorMain {
    public static void main(String[] args) {
        try {
            Logger.info("Iniciando aplicación del servidor");
            InterfazServidor interfaz = new InterfazServidor();
            interfaz.setVisible(true);
        } catch (Exception e) {
            Logger.error("Error al iniciar la aplicación del servidor", e);
            System.exit(1);
        }
    }
}