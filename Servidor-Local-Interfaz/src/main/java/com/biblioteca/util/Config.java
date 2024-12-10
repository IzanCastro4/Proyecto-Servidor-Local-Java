package com.biblioteca.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);
    private static final Properties props = new Properties();
    
    static {
        try {
            props.load(Config.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            logger.error("Error al cargar configuración", e);
        }
    }
    
    public static int getPuerto() {
        return Integer.parseInt(props.getProperty("puerto", "1099"));
    }
    
    public static String getHost() {
        return props.getProperty("host", "localhost");
    }
    
    public static String getRutaArchivo() {
        return props.getProperty("ruta.archivo", "data/biblioteca.txt");
    }

    public static String getSeparadorArchivo() {
        return props.getProperty("archivo.separador", ";");
    }

    public static String getEncoding() {
        return props.getProperty("archivo.encoding", "UTF-8");
    }
}