package com.hojacalculo;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class VentanaHash extends JFrame {
    private JTable tabla;
    private TablaHashModelo modelo;

    public VentanaHash() {
        setTitle("Tabla Hash Personalizada");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Menú
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");

        JMenuItem itemGuardar = new JMenuItem("Guardar");
        JMenuItem itemAbrir = new JMenuItem("Abrir");

        menuArchivo.add(itemGuardar);
        menuArchivo.add(itemAbrir);
        menuBar.add(menuArchivo);
        setJMenuBar(menuBar);

        // Modelo personalizado
        modelo = new TablaHashModelo(30); // puedes ajustar el número de filas
        tabla = new JTable(modelo);
        tabla.setRowHeight(25);
        tabla.setGridColor(Color.LIGHT_GRAY);
        tabla.setShowGrid(true);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Acción guardar
        itemGuardar.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int opcion = chooser.showSaveDialog(this);
            if (opcion == JFileChooser.APPROVE_OPTION) {
                try (PrintWriter writer = new PrintWriter(chooser.getSelectedFile())) {
                    for (int i = 0; i < modelo.getRowCount(); i++) {
                        Object dato = modelo.getValueAt(i, 0);
                        Object hash = modelo.getValueAt(i, 1);
                        if (dato != null && hash != null) {
                            writer.println(dato + "," + hash);
                        }
                    }
                    JOptionPane.showMessageDialog(this, "Archivo guardado con éxito.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
                }
            }
        });

        // Acción abrir
        itemAbrir.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int opcion = chooser.showOpenDialog(this);
            if (opcion == JFileChooser.APPROVE_OPTION) {
                try (Scanner scanner = new Scanner(chooser.getSelectedFile())) {
                    modelo = new TablaHashModelo(30); // nueva instancia limpia
                    tabla.setModel(modelo);
                    int fila = 0;
                    while (scanner.hasNextLine() && fila < modelo.getRowCount()) {
                        String[] partes = scanner.nextLine().split(",", 2);
                        if (partes.length == 2) {
                            modelo.setValueAt(partes[0], fila, 0); // esto genera automáticamente el hash
                        }
                        fila++;
                    }
                    JOptionPane.showMessageDialog(this, "Archivo cargado con éxito.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al abrir: " + ex.getMessage());
                }
            }
        });
    }
}
