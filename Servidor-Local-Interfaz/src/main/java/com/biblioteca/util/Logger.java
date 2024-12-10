package com.biblioteca.util;

import org.slf4j.LoggerFactory;

public class Logger {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Logger.class);

    public static void info(String mensaje) {
        logger.info(mensaje);
    }

    public static void error(String mensaje, Throwable e) {
        logger.error(mensaje, e);
    }

    public static void warn(String mensaje) {
        logger.warn(mensaje);
    }

    public static void debug(String mensaje) {
        logger.debug(mensaje);
    }
}