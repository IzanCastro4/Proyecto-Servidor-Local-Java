package com.biblioteca.vista;

import com.biblioteca.controlador.ClienteBiblioteca;
import com.biblioteca.modelo.Libro;
import com.biblioteca.vista.dialogos.DialogoAgregarLibro;
import com.biblioteca.vista.dialogos.DialogoActualizarLibro;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InterfazCliente extends JFrame {
    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;
    private JTextField txtBusqueda;
    private JComboBox<String> comboCriterio;
    private JButton btnBuscar, btnAgregar, btnActualizar, btnEliminar, btnReconectar;
    private JLabel estadoLabel;
    private ClienteBiblioteca cliente;
    private Timer animacionTimer;
    private int animacionFrame = 0;

    public InterfazCliente() {
        configurarEstilos();
        inicializarComponentes();
        configurarCliente();
        configurarEventos();
        configurarVentana();
        conectarAlServidor();
    }

    private void configurarEstilos() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void inicializarComponentes() {
        // Panel principal con color de fondo oscuro
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(30, 30, 30));

        // Panel superior con búsqueda y acciones
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(new Color(30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Panel de búsqueda
        JPanel busquedaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        busquedaPanel.setBackground(new Color(30, 30, 30));

        JLabel lblBuscar = new JLabel("Buscar por:");
        lblBuscar.setForeground(Color.WHITE);
        
        comboCriterio = new JComboBox<>(new String[]{"Título", "Autor", "Categoría"});
        estilizarComboBox(comboCriterio);
        
        txtBusqueda = new JTextField(20);
        estilizarTextField(txtBusqueda);
        
        btnBuscar = crearBoton("Buscar", new Color(52, 152, 219));
        btnReconectar = crearBoton("Reconectar", new Color(142, 68, 173));
        
        estadoLabel = new JLabel("Estado: Desconectado");
        estadoLabel.setForeground(Color.WHITE);

        busquedaPanel.add(lblBuscar);
        busquedaPanel.add(comboCriterio);
        busquedaPanel.add(txtBusqueda);
        busquedaPanel.add(btnBuscar);
        busquedaPanel.add(btnReconectar);
        busquedaPanel.add(estadoLabel);

        // Panel de acciones
        JPanel accionesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        accionesPanel.setBackground(new Color(30, 30, 30));

        btnAgregar = crearBoton("Agregar Libro", new Color(46, 204, 113));
        btnActualizar = crearBoton("Actualizar Libro", new Color(241, 196, 15));
        btnEliminar = crearBoton("Eliminar Libro", new Color(231, 76, 60));

        accionesPanel.add(btnAgregar);
        accionesPanel.add(btnActualizar);
        accionesPanel.add(btnEliminar);

        // Agregar paneles al panel superior
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        topPanel.add(busquedaPanel, gbc);

        gbc.gridy = 1;
        topPanel.add(accionesPanel, gbc);

        // Configurar tabla
        modeloTabla = new DefaultTableModel(
            new String[]{"Título", "Autor", "Categoría"}, 0
        ) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaLibros = new JTable(modeloTabla);
        estilizarTabla(tablaLibros);

        JScrollPane scrollPane = new JScrollPane(tablaLibros);
        scrollPane.getViewport().setBackground(new Color(40, 44, 52));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }
    
    private void mostrarMensaje(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Información",
                JOptionPane.INFORMATION_MESSAGE
            );
        });
    }

    private void mostrarError(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        });
    }

    private void estilizarTabla(JTable tabla) {
        tabla.setBackground(new Color(40, 44, 52));
        tabla.setForeground(Color.WHITE);
        tabla.setSelectionBackground(new Color(61, 90, 254));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setGridColor(new Color(50, 50, 50));
        tabla.setFont(new Font("Arial", Font.PLAIN, 12));
        tabla.setRowHeight(25);
        tabla.getTableHeader().setBackground(new Color(30, 30, 30));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
    }

    private void estilizarComboBox(JComboBox<?> comboBox) {
        comboBox.setBackground(new Color(45, 45, 45));
        comboBox.setForeground(Color.WHITE);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        ((JComponent) comboBox.getRenderer()).setBackground(new Color(45, 45, 45));
    }

    private void estilizarTextField(JTextField textField) {
        textField.setBackground(new Color(45, 45, 45));
        textField.setForeground(Color.WHITE);
        textField.setCaretColor(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        textField.setFont(new Font("Arial", Font.PLAIN, 12));
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
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

    private void configurarCliente() {
        cliente = new ClienteBiblioteca();
        cliente.addClientEventListener(new ClienteBiblioteca.ClientEventListener() {
            @Override
            public void onConnectionStatusChange(boolean isConnected) {
                actualizarEstadoConexion(isConnected);
            }

            @Override
            public void onError(String message) {
                mostrarError(message);
            }
        });
    }

    private void configurarEventos() {
        btnBuscar.addActionListener(e -> buscarLibros());
        btnAgregar.addActionListener(e -> mostrarDialogoAgregar());
        btnActualizar.addActionListener(e -> mostrarDialogoActualizar());
        btnEliminar.addActionListener(e -> eliminarLibro());
        btnReconectar.addActionListener(e -> reconectarAlServidor());
        
        txtBusqueda.addActionListener(e -> buscarLibros());

        animacionTimer = new Timer(500, e -> actualizarAnimacion());
    }

    private void configurarVentana() {
        setTitle("Cliente de Biblioteca");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void conectarAlServidor() {
        try {
            cliente.conectar();
            animacionTimer.start();
            cargarLibros();
        } catch (Exception e) {
            mostrarError("Error al conectar con el servidor: " + e.getMessage());
        }
    }

    private void reconectarAlServidor() {
        try {
            cliente.conectar();
            cargarLibros();
        } catch (Exception e) {
            mostrarError("Error al reconectar con el servidor: " + e.getMessage());
        }
    }

    private void actualizarEstadoConexion(boolean conectado) {
        SwingUtilities.invokeLater(() -> {
            btnBuscar.setEnabled(conectado);
            btnAgregar.setEnabled(conectado);
            btnActualizar.setEnabled(conectado);
            btnEliminar.setEnabled(conectado);
            btnReconectar.setEnabled(!conectado);
            
            if (!conectado) {
                estadoLabel.setText("Estado: Desconectado");
                animacionTimer.stop();
            }
        });
    }

    private void actualizarAnimacion() {
        if (cliente.estaConectado()) {
            String[] frames = {" ⠋", " ⠙", " ⠹", " ⠸", " ⠼", " ⠴", " ⠦", " ⠧", " ⠇", " ⠏"};
            estadoLabel.setText("Estado: Conectado " + frames[animacionFrame]);
            animacionFrame = (animacionFrame + 1) % frames.length;
        }
    }

    private void cargarLibros() {
        try {
            List<Libro> libros = cliente.listarTodos();
            actualizarTabla(libros);
        } catch (Exception e) {
            mostrarError("Error al cargar libros: " + e.getMessage());
        }
    }

    private void buscarLibros() {
        try {
            String criterio = comboCriterio.getSelectedItem().toString();
            String valor = txtBusqueda.getText().trim();
            
            if (valor.isEmpty()) {
                cargarLibros();
                return;
            }

            List<Libro> resultados = cliente.buscarLibros(criterio, valor);
            actualizarTabla(resultados);
        } catch (Exception e) {
            mostrarError("Error al buscar libros: " + e.getMessage());
        }
    }

    private void actualizarTabla(List<Libro> libros) {
        SwingUtilities.invokeLater(() -> {
            modeloTabla.setRowCount(0);
            for (Libro libro : libros) {
                modeloTabla.addRow(new Object[]{
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getCategoria()
                });
            }
        });
    }

    private void mostrarDialogoAgregar() {
        DialogoAgregarLibro dialogo = new DialogoAgregarLibro(this);
        dialogo.setVisible(true);

        if (dialogo.isConfirmado()) {
            try {
                boolean resultado = cliente.agregarLibro(
                    dialogo.getTitulo(),
                    dialogo.getAutor(),
                    dialogo.getCategoria()
                );
                
                if (resultado) {
                    mostrarMensaje("Libro agregado correctamente");
                    cargarLibros();
                }
            } catch (Exception e) {
                mostrarError("Error al agregar libro: " + e.getMessage());
            }
        }
    }

    private void mostrarDialogoActualizar() {
        int filaSeleccionada = tablaLibros.getSelectedRow();
        if (filaSeleccionada == -1) {
            mostrarError("Por favor, seleccione un libro para actualizar");
            return;
        }

        String tituloOriginal = (String) tablaLibros.getValueAt(filaSeleccionada, 0);
        String autorOriginal = (String) tablaLibros.getValueAt(filaSeleccionada, 1);
        String categoriaOriginal = (String) tablaLibros.getValueAt(filaSeleccionada, 2);

        DialogoActualizarLibro dialogo = new DialogoActualizarLibro(
            this, tituloOriginal, autorOriginal, categoriaOriginal
        );
        dialogo.setVisible(true);

        if (dialogo.isConfirmado()) {
            try {
                boolean resultado = cliente.actualizarLibro(
                    tituloOriginal,
                    dialogo.getTitulo(),
                    dialogo.getAutor(),
                    dialogo.getCategoria()
                );
                
                if (resultado) {
                    mostrarMensaje("Libro actualizado correctamente");
                    cargarLibros();
                }
            } catch (Exception e) {
                mostrarError("Error al actualizar libro: " + e.getMessage());
            }
        }
    }

    private void eliminarLibro() {
        int filaSeleccionada = tablaLibros.getSelectedRow();
        if (filaSeleccionada == -1) {
            mostrarError("Por favor, seleccione un libro para eliminar");
            return;
        }

        String titulo = (String) tablaLibros.getValueAt(filaSeleccionada, 0);
        
        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de que desea eliminar el libro '" + titulo + "'?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                boolean resultado = cliente.eliminarLibro(titulo);
                if (resultado) {
                    mostrarMensaje("Libro eliminado correctamente");
                    cargarLibros();
                }
            } catch (Exception e) {
                mostrarError("Error al eliminar libro: " + e.getMessage());
            }
        }
    }
    
}