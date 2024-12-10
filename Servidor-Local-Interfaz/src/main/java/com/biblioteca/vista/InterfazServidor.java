package com.biblioteca.vista;

import com.biblioteca.controlador.ServidorBiblioteca;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InterfazServidor extends JFrame {
    private JTextArea consolaArea;
    private JButton btnIniciar, btnParar, btnReiniciar;
    private JLabel estadoLabel;
    private ServidorBiblioteca servidor;
    private Timer animacionTimer;
    private int animacionFrame = 0;

    public InterfazServidor() {
        configurarEstilos();
        inicializarComponentes();
        configurarServidor();
        configurarEventos();
        configurarVentana();
    }

    private void configurarEstilos() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void inicializarComponentes() {
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de control
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnIniciar = crearBoton("Iniciar", new Color(46, 204, 113));
        btnParar = crearBoton("Parar", new Color(231, 76, 60));
        btnReiniciar = crearBoton("Reiniciar", new Color(52, 152, 219));
        estadoLabel = new JLabel("Estado: Detenido");
        estadoLabel.setForeground(Color.WHITE);

        controlPanel.setBackground(new Color(30, 30, 30));
        controlPanel.add(btnIniciar);
        controlPanel.add(btnParar);
        controlPanel.add(btnReiniciar);
        controlPanel.add(estadoLabel);

        // Área de consola
        consolaArea = new JTextArea();
        consolaArea.setEditable(false);
        consolaArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        consolaArea.setBackground(new Color(40, 44, 52));
        consolaArea.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(consolaArea);

        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.setBackground(new Color(30, 30, 30));

        add(mainPanel);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(color.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(color);
            }
        });

        return btn;
    }

    private void configurarServidor() {
        servidor = new ServidorBiblioteca();
        servidor.addServerEventListener(new ServidorBiblioteca.ServerEventListener() {
            @Override
            public void onLogMessage(String message) {
                log(message);
            }

            @Override
            public void onStatusChange(boolean isActive) {
                actualizarEstadoUI(isActive);
            }
        });
    }

    private void configurarEventos() {
        btnIniciar.addActionListener(e -> iniciarServidor());
        btnParar.addActionListener(e -> pararServidor());
        btnReiniciar.addActionListener(e -> reiniciarServidor());

        animacionTimer = new Timer(500, e -> actualizarAnimacion());
    }

    private void configurarVentana() {
        setTitle("Servidor de Biblioteca");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        actualizarEstadoBotones(false);
    }

    private void iniciarServidor() {
        try {
            servidor.iniciar();
            animacionTimer.start();
            actualizarEstadoBotones(true);
        } catch (Exception ex) {
            log("Error al iniciar el servidor: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, 
                "Error al iniciar el servidor: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pararServidor() {
        try {
            servidor.parar();
            animacionTimer.stop();
            actualizarEstadoBotones(false);
        } catch (Exception ex) {
            log("Error al detener el servidor: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                "Error al detener el servidor: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void reiniciarServidor() {
        pararServidor();
        iniciarServidor();
    }

    private void actualizarEstadoBotones(boolean servidorActivo) {
        btnIniciar.setEnabled(!servidorActivo);
        btnParar.setEnabled(servidorActivo);
        btnReiniciar.setEnabled(servidorActivo);
    }

    private void actualizarAnimacion() {
        if (servidor.isActivo()) {
            String[] frames = {" ⠋", " ⠙", " ⠹", " ⠸", " ⠼", " ⠴", " ⠦", " ⠧", " ⠇", " ⠏"};
            estadoLabel.setText("Estado: Activo " + frames[animacionFrame]);
            animacionFrame = (animacionFrame + 1) % frames.length;
        }
    }

    private void actualizarEstadoUI(boolean activo) {
        SwingUtilities.invokeLater(() -> {
            actualizarEstadoBotones(activo);
            if (!activo) {
                estadoLabel.setText("Estado: Detenido");
                animacionTimer.stop();
            }
        });
    }

    public void log(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            consolaArea.append("[" + sdf.format(new Date()) + "] " + mensaje + "\n");
            consolaArea.setCaretPosition(consolaArea.getDocument().getLength());
        });
    }
}