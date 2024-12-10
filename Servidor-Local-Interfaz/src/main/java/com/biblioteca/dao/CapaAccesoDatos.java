package com.biblioteca.dao;

import com.biblioteca.modelo.Libro;
import com.biblioteca.util.Config;
import com.biblioteca.util.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CapaAccesoDatos {
    private final String rutaArchivo;
    private final String separador;
    private final Charset encoding;

    public CapaAccesoDatos() {
        this.rutaArchivo = Config.getRutaArchivo();
        this.separador = Config.getSeparadorArchivo();
        this.encoding = Charset.forName(Config.getEncoding());
        inicializarArchivo();
    }

    private void inicializarArchivo() {
        Path path = Paths.get(rutaArchivo);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
                Logger.info("Archivo de biblioteca creado: " + rutaArchivo);
            } catch (IOException e) {
                Logger.error("Error al crear archivo de biblioteca", e);
            }
        }
    }

    public synchronized List<Libro> obtenerTodos() throws IOException {
        List<Libro> libros = new ArrayList<>();
        try {
            List<String> lineas = Files.readAllLines(Paths.get(rutaArchivo), encoding);
            for (String linea : lineas) {
                String[] datos = linea.split(separador);
                if (datos.length == 3) {
                    libros.add(new Libro(datos[0].trim(), datos[1].trim(), datos[2].trim()));
                }
            }
        } catch (IOException e) {
            Logger.error("Error al leer archivo de biblioteca", e);
            throw e;
        }
        return libros;
    }

    public synchronized boolean agregarLibro(Libro libro) throws IOException {
        try {
            String nuevaLinea = String.format("%s%s%s%s%s%n",
                    libro.getTitulo(), separador,
                    libro.getAutor(), separador,
                    libro.getCategoria());
            Files.write(Paths.get(rutaArchivo),
                    nuevaLinea.getBytes(encoding),
                    StandardOpenOption.APPEND);
            Logger.info("Libro agregado: " + libro.getTitulo());
            return true;
        } catch (IOException e) {
            Logger.error("Error al agregar libro", e);
            throw e;
        }
    }

    public synchronized boolean actualizarLibro(String tituloOriginal, Libro libroActualizado) throws IOException {
        try {
            List<String> lineas = Files.readAllLines(Paths.get(rutaArchivo), encoding);
            List<String> nuevasLineas = lineas.stream()
                    .map(linea -> {
                        String[] datos = linea.split(separador);
                        if (datos.length == 3 && datos[0].trim().equals(tituloOriginal)) {
                            return String.format("%s%s%s%s%s",
                                    libroActualizado.getTitulo(), separador,
                                    libroActualizado.getAutor(), separador,
                                    libroActualizado.getCategoria());
                        }
                        return linea;
                    })
                    .collect(Collectors.toList());

            Files.write(Paths.get(rutaArchivo), nuevasLineas, encoding);
            Logger.info("Libro actualizado: " + tituloOriginal + " -> " + libroActualizado.getTitulo());
            return true;
        } catch (IOException e) {
            Logger.error("Error al actualizar libro", e);
            throw e;
        }
    }

    public synchronized boolean eliminarLibro(String titulo) throws IOException {
        try {
            List<String> lineas = Files.readAllLines(Paths.get(rutaArchivo), encoding);
            List<String> nuevasLineas = lineas.stream()
                    .filter(linea -> {
                        String[] datos = linea.split(separador);
                        return datos.length != 3 || !datos[0].trim().equals(titulo);
                    })
                    .collect(Collectors.toList());

            if (lineas.size() != nuevasLineas.size()) {
                Files.write(Paths.get(rutaArchivo), nuevasLineas, encoding);
                Logger.info("Libro eliminado: " + titulo);
                return true;
            }
            return false;
        } catch (IOException e) {
            Logger.error("Error al eliminar libro", e);
            throw e;
        }
    }
}