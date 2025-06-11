package com.hojacalculo;

import com.hojacalculo.model.*;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Main {

    /** Lista enlazada de hojas de cálculo disponibles en la aplicación. */
    private static ListaHojas listaHojas = new ListaHojas();

    /** Referencia a la hoja actualmente seleccionada. */
    private static HojaTrabajo hojaActual;

    /** Tabla que representa visualmente la hoja de cálculo. */
    private static JTable tabla;

    /** Modelo personalizado de tabla que permite visualizar la matriz ortogonal. */
    private static TablaModelo modelo;

    /**
     * Método principal que inicializa y ejecuta la interfaz gráfica.
     *
     * @param args argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // Crear y configurar la ventana principal
            JFrame frame = new JFrame("Hoja de Cálculo con Múltiples Hojas");
            frame.setSize(1200, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            // Menú superior
            JMenuBar menuBar = new JMenuBar();
            JMenu archivoMenu = new JMenu("Archivo");
            JMenu ayudaMenu = new JMenu("Ayuda");

            JMenuItem itemGuardarHoja = new JMenuItem("Guardar hoja");
            JMenuItem itemAbrirHoja = new JMenuItem("Abrir hoja");
            JMenuItem itemTablaHash = new JMenuItem("Tabla hash");

            // Acerca de
            JMenuItem itemAcercaDe = new JMenuItem("Acerca de");
            itemAcercaDe.addActionListener(e -> {
                String mensaje = """
                    Hoja de Cálculo - Versión 1.0

                    Desarrollado por:
                    - Rode Emanuel
                    - Gerson Díaz
                    - Yovany García

                    Este proyecto fue creado como parte de un ejercicio académico que implementa una matriz ortogonal para representar datos, listas enlazadas para manejar múltiples hojas y generación de tabla hash.
                    """;
                JOptionPane.showMessageDialog(null, mensaje, "Acerca de", JOptionPane.INFORMATION_MESSAGE);
            });

            archivoMenu.add(itemGuardarHoja);
            archivoMenu.add(itemAbrirHoja);
            archivoMenu.addSeparator();
            archivoMenu.add(itemTablaHash);
            ayudaMenu.add(itemAcercaDe);

            menuBar.add(archivoMenu);
            menuBar.add(new JMenu("Insertar")); // sin funcionalidad actual
            menuBar.add(ayudaMenu);
            frame.setJMenuBar(menuBar);

            // Panel para selección de hojas
            JComboBox<String> selectorHojas = new JComboBox<>();
            JButton btnNuevaHoja = new JButton("Nueva Hoja");

            JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panelSuperior.add(new JLabel("Hoja actual: "));
            panelSuperior.add(selectorHojas);
            panelSuperior.add(btnNuevaHoja);
            frame.add(panelSuperior, BorderLayout.NORTH);

            // Crear primera hoja y tabla inicial
            listaHojas.agregarHoja("Hoja1");
            hojaActual = listaHojas.getPrimeraHoja();
            selectorHojas.addItem(hojaActual.getNombre());

            modelo = new TablaModelo(hojaActual.getMatriz());
            tabla = new JTable(modelo);
            tabla.setRowHeight(25);
            tabla.setAutoCreateRowSorter(true);
            tabla.setShowGrid(true);
            tabla.setGridColor(Color.LIGHT_GRAY);

            // Centrar contenido de las celdas
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
            renderer.setHorizontalAlignment(SwingConstants.CENTER);
            for (int i = 0; i < tabla.getColumnCount(); i++) {
                tabla.getColumnModel().getColumn(i).setCellRenderer(renderer);
            }

            JScrollPane scrollPane = new JScrollPane(tabla);
            scrollPane.setRowHeaderView(new TableRowHeader(tabla)); // Para índices de fila
            frame.add(scrollPane, BorderLayout.CENTER);

            // Panel inferior para mostrar celda activa y fórmula
            JLabel etiquetaCelda = new JLabel("Celda:");
            etiquetaCelda.setFont(new Font("Arial", Font.BOLD, 14));

            JLabel celdaActiva = new JLabel("A1");
            celdaActiva.setFont(new Font("Arial", Font.PLAIN, 14));

            JLabel etiquetaFormula = new JLabel(" Fórmula:");
            etiquetaFormula.setFont(new Font("Arial", Font.BOLD, 14));

            JTextField campoFormula = new JTextField(50);
            campoFormula.setFont(new Font("Consolas", Font.PLAIN, 14));
            campoFormula.setPreferredSize(new Dimension(800, 30));

            JPanel panelFormula = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panelFormula.setPreferredSize(new Dimension(1200, 50));
            panelFormula.add(etiquetaCelda);
            panelFormula.add(celdaActiva);
            panelFormula.add(etiquetaFormula);
            panelFormula.add(campoFormula);
            frame.add(panelFormula, BorderLayout.SOUTH);

            // Manejador para cambio de selección de celda
            ListSelectionListener listener = e -> {
                int fila = tabla.getSelectedRow();
                int col = tabla.getSelectedColumn();
                if (fila >= 0 && col >= 0) {
                    char colLetra = (char) ('A' + col);
                    celdaActiva.setText("" + colLetra + (fila + 1));

                    NodoCelda nodo = hojaActual.getMatriz().buscar(fila, col);
                    if (nodo != null && nodo.hasFormula()) {
                        campoFormula.setText(nodo.getFormula());
                    } else {
                        Object valor = tabla.getValueAt(fila, col);
                        campoFormula.setText(valor != null ? valor.toString() : "");
                    }
                }
            };
            tabla.getSelectionModel().addListSelectionListener(listener);
            tabla.getColumnModel().getSelectionModel().addListSelectionListener(listener);

            // Manejador para ingresar o modificar fórmulas
            campoFormula.addActionListener(e -> {
                int fila = tabla.getSelectedRow();
                int col = tabla.getSelectedColumn();
                if (fila >= 0 && col >= 0) {
                    String entrada = campoFormula.getText();
                    tabla.setValueAt(entrada, fila, col);
                }
            });

            // Cambiar de hoja desde el JComboBox
            selectorHojas.addActionListener((ActionEvent e) -> {
                String seleccion = (String) selectorHojas.getSelectedItem();
                if (seleccion != null) {
                    hojaActual = listaHojas.buscarHoja(seleccion);
                    modelo.setMatriz(hojaActual.getMatriz());
                    modelo.fireTableDataChanged();
                }
            });

            // Crear nueva hoja
            btnNuevaHoja.addActionListener(e -> {
                String nombre = JOptionPane.showInputDialog("Nombre de la nueva hoja:");
                if (nombre != null && !nombre.trim().isEmpty()) {
                    if (listaHojas.buscarHoja(nombre) == null) {
                        listaHojas.agregarHoja(nombre);
                        selectorHojas.addItem(nombre);
                        selectorHojas.setSelectedItem(nombre);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Ya existe una hoja con ese nombre.");
                    }
                }
            });

            // Mostrar ventana de tabla hash
            itemTablaHash.addActionListener(e -> {
                VentanaHash hashVentana = new VentanaHash();
                hashVentana.setVisible(true);
            });

            // Guardar hoja en archivo .txt
            itemGuardarHoja.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                int opcion = fileChooser.showSaveDialog(frame);
                if (opcion == JFileChooser.APPROVE_OPTION) {
                    try {
                        java.io.File file = fileChooser.getSelectedFile();
                        java.io.PrintWriter writer = new java.io.PrintWriter(file);
                        for (int i = 0; i < hojaActual.getMatriz().getFilas(); i++) {
                            for (int j = 0; j < hojaActual.getMatriz().getColumnas(); j++) {
                                NodoCelda nodo = hojaActual.getMatriz().buscar(i, j);
                                if (nodo != null) {
                                    Object val = nodo.hasFormula() ? nodo.getFormula() : nodo.getValor();
                                    if (val != null) {
                                        writer.println(i + "," + j + "," + val.toString());
                                    }
                                }
                            }
                        }
                        writer.close();
                        JOptionPane.showMessageDialog(frame, "Hoja guardada con éxito.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Error al guardar: " + ex.getMessage());
                    }
                }
            });

            // Abrir hoja desde archivo .txt
            itemAbrirHoja.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                int opcion = fileChooser.showOpenDialog(frame);
                if (opcion == JFileChooser.APPROVE_OPTION) {
                    try {
                        java.io.File file = fileChooser.getSelectedFile();
                        java.util.Scanner scanner = new java.util.Scanner(file);
                        hojaActual.getMatriz().limpiar();

                        while (scanner.hasNextLine()) {
                            String linea = scanner.nextLine();
                            String[] partes = linea.split(",", 3);
                            int fila = Integer.parseInt(partes[0]);
                            int col = Integer.parseInt(partes[1]);
                            String valor = partes[2];

                            if (valor.toLowerCase().startsWith("suma(") || valor.toLowerCase().startsWith("resta(")
                                    || valor.toLowerCase().startsWith("multiplicacion(") || valor.toLowerCase().startsWith("division(")) {
                                hojaActual.getMatriz().setFormula(fila, col, valor);
                            } else {
                                try {
                                    double num = Double.parseDouble(valor);
                                    hojaActual.getMatriz().setValor(fila, col, num);
                                } catch (NumberFormatException nfe) {
                                    hojaActual.getMatriz().setValor(fila, col, valor);
                                }
                            }
                        }
                        scanner.close();
                        modelo.fireTableDataChanged();
                        JOptionPane.showMessageDialog(frame, "Hoja cargada con éxito.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Error al abrir: " + ex.getMessage());
                    }
                }
            });

            // Mostrar ventana
            frame.setVisible(true);
        });
    }
}
