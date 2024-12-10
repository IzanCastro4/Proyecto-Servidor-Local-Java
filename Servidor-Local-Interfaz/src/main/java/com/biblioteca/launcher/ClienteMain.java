// ClienteMain.java
package com.biblioteca.launcher;

import com.biblioteca.vista.InterfazCliente;
import com.biblioteca.util.Logger;

public class ClienteMain {
    public static void main(String[] args) {
        try {
            Logger.info("Iniciando aplicación del cliente");
            InterfazCliente interfaz = new InterfazCliente();
            interfaz.setVisible(true);
        } catch (Exception e) {
            Logger.error("Error al iniciar la aplicación del cliente", e);
            System.exit(1);
        }
    }
}