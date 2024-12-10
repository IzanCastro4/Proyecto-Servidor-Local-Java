// DialogoAgregarLibro.java
package com.biblioteca.vista.dialogos;

import javax.swing.*;
import java.awt.*;

public class DialogoAgregarLibro extends JDialog {
    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtCategoria;
    private boolean confirmado = false;

    public DialogoAgregarLibro(Frame parent) {
        super(parent, "Agregar Libro", true);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos de texto
        txtTitulo = new JTextField(20);
        txtAutor = new JTextField(20);
        txtCategoria = new JTextField(20);

        // Etiquetas
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Título:"), gbc);
        
        gbc.gridx = 1;
        add(txtTitulo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Autor:"), gbc);
        
        gbc.gridx = 1;
        add(txtAutor, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Categoría:"), gbc);
        
        gbc.gridx = 1;
        add(txtCategoria, gbc);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");

        btnAceptar.addActionListener(e -> {
            if (validarCampos()) {
                confirmado = true;
                dispose();
            }
        });

        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(panelBotones, gbc);

        pack();
        setLocationRelativeTo(getParent());
    }

    private boolean validarCampos() {
        if (txtTitulo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El título es obligatorio");
            return false;
        }
        if (txtAutor.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El autor es obligatorio");
            return false;
        }
        if (txtCategoria.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La categoría es obligatoria");
            return false;
        }
        return true;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public String getTitulo() { return txtTitulo.getText().trim(); }
    public String getAutor() { return txtAutor.getText().trim(); }
    public String getCategoria() { return txtCategoria.getText().trim(); }
}