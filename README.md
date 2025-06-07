# HOJA DE CALCULO

Este programa es una hoja de cálculo personalizada desarrollada en **Java** siguiendo el patrón de diseño **MVC (Modelo-Vista-Controlador)**. Está diseñada para simular algunas funcionalidades básicas de aplicaciones como Microsoft Excel, pero usando estructuras de datos propias (como matrices ortogonales y listas enlazadas) sin depender de bibliotecas externas.

## **Características principales:**

-   **Interfaz gráfica (GUI)** amigable y responsiva usando `Swing`.
    
-   **Soporte para múltiples hojas de trabajo**, donde cada hoja se maneja como un nodo en una **lista enlazada simple**.
    
-   **Estructura de datos basada en matriz ortogonal clásica**, representando celdas con punteros derecha/abajo.
    
-   **Fórmulas integradas** que permiten realizar operaciones aritméticas como:
    
    -   `suma(A1,B2)`
        
    -   `resta(A1,B2)`
        
    -   `multiplicacion(A1,B2)`
        
    -   `division(A1,B2)`
        
-   **Selector de celdas y barra de fórmulas**, que muestran y permiten editar el contenido o fórmula activa.
    
-   **Guardado y apertura de hojas de cálculo** desde el disco local (`.txt` o similar), permitiendo almacenamiento y recuperación de datos.
    
-   **Gestor de Tabla Hash personalizada**:
    
    -   Permite ingresar datos en la columna A.
        
    -   Calcula automáticamente su índice Hash y lo muestra en la columna B.
        
    -   Incluye opción de guardar y abrir la tabla Hash desde disco.
        
-   **Menú de ayuda ("Acerca de")** con información de versión y autores.
    

### **Autores del proyecto:**

-   Rode Emanuel
    
-   Gerson Díaz
    
-   Yovany García

## CODIGO

## *Com.hojacalculo*

# Main.java

    package com.hojacalculo;
    
    import com.hojacalculo.model.*;
    import javax.swing.*;
    import javax.swing.event.ListSelectionListener;
    import javax.swing.table.DefaultTableCellRenderer;
    import java.awt.*;
    import java.awt.event.ActionEvent;
    
    public class Main {
        private static ListaHojas listaHojas = new ListaHojas();
        private static HojaTrabajo hojaActual;
        private static JTable tabla;
        private static TablaModelo modelo;
    
        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Hoja de Cálculo con Múltiples Hojas");
                frame.setSize(1200, 600);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
    
                JMenuBar menuBar = new JMenuBar();
                JMenu archivoMenu = new JMenu("Archivo");
                JMenu ayudaMenu = new JMenu("Ayuda");
    
                JMenuItem itemGuardarHoja = new JMenuItem("Guardar hoja");
                JMenuItem itemAbrirHoja = new JMenuItem("Abrir hoja");
                JMenuItem itemTablaHash = new JMenuItem("Tabla hash");
    
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
                menuBar.add(new JMenu("Insertar"));
                menuBar.add(ayudaMenu);
                frame.setJMenuBar(menuBar);
    
                JComboBox<String> selectorHojas = new JComboBox<>();
                JButton btnNuevaHoja = new JButton("Nueva Hoja");
    
                JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
                panelSuperior.add(new JLabel("Hoja actual: "));
                panelSuperior.add(selectorHojas);
                panelSuperior.add(btnNuevaHoja);
                frame.add(panelSuperior, BorderLayout.NORTH);
    
                listaHojas.agregarHoja("Hoja1");
                hojaActual = listaHojas.getPrimeraHoja();
                selectorHojas.addItem(hojaActual.getNombre());
    
                modelo = new TablaModelo(hojaActual.getMatriz());
                tabla = new JTable(modelo);
                tabla.setRowHeight(25);
                tabla.setAutoCreateRowSorter(true);
                tabla.setShowGrid(true);
                tabla.setGridColor(Color.LIGHT_GRAY);
    
                DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
                renderer.setHorizontalAlignment(SwingConstants.CENTER);
                for (int i = 0; i < tabla.getColumnCount(); i++) {
                    tabla.getColumnModel().getColumn(i).setCellRenderer(renderer);
                }
    
                JScrollPane scrollPane = new JScrollPane(tabla);
                scrollPane.setRowHeaderView(new TableRowHeader(tabla));
                frame.add(scrollPane, BorderLayout.CENTER);
    
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
    
                campoFormula.addActionListener(e -> {
                    int fila = tabla.getSelectedRow();
                    int col = tabla.getSelectedColumn();
                    if (fila >= 0 && col >= 0) {
                        String entrada = campoFormula.getText();
                        tabla.setValueAt(entrada, fila, col);
                    }
                });
    
                selectorHojas.addActionListener((ActionEvent e) -> {
                    String seleccion = (String) selectorHojas.getSelectedItem();
                    if (seleccion != null) {
                        hojaActual = listaHojas.buscarHoja(seleccion);
                        modelo.setMatriz(hojaActual.getMatriz());
                        modelo.fireTableDataChanged();
                    }
                });
    
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
    
                itemTablaHash.addActionListener(e -> {
                    VentanaHash hashVentana = new VentanaHash();
                    hashVentana.setVisible(true);
                });
    
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
    
                frame.setVisible(true);
            });
        }
    }

# TablaHashModelo.java

    package com.hojacalculo;
    
    import javax.swing.table.AbstractTableModel;
    
    public class TablaHashModelo extends AbstractTableModel {
        private final Object[][] datos;
        private final String[] columnas = {"Dato", "Índice Hash"};
    
        public TablaHashModelo(int filas) {
            datos = new Object[filas][2]; // columna 0 = dato, columna 1 = hash
        }
    
        @Override
        public int getRowCount() {
            return datos.length;
        }
    
        @Override
        public int getColumnCount() {
            return columnas.length;
        }
    
        @Override
        public String getColumnName(int col) {
            return columnas[col];
        }
    
        @Override
        public Object getValueAt(int fila, int columna) {
            return datos[fila][columna];
        }
    
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return columna == 0; // Solo se edita la columna de datos
        }
    
        @Override
        public void setValueAt(Object valor, int fila, int columna) {
            if (columna == 0) {
                String texto = valor.toString();
                datos[fila][0] = texto;
                datos[fila][1] = calcularHash(texto);
    
                // ✅ Forzar redibujo de la fila entera (dato + hash)
                fireTableRowsUpdated(fila, fila);
            }
        }
    
        private int calcularHash(String texto) {
            int hash = 0;
            for (char c : texto.toCharArray()) {
                hash += c;
            }
            return hash % 997; // Número primo para dispersión
        }
    }

# TablaModelo.java

    package com.hojacalculo;
    
    import com.hojacalculo.model.MatrizOrtogonal;
    import com.hojacalculo.model.NodoCelda;
    import com.hojacalculo.model.Formula;
    import javax.swing.table.AbstractTableModel;
    
    public class TablaModelo extends AbstractTableModel {
        private MatrizOrtogonal matriz;
        private Formula formula;
        private String[] columnas;
    
        public TablaModelo(MatrizOrtogonal matriz) {
            this.matriz = matriz;
            this.formula = new Formula(matriz);
            this.columnas = new String[matriz.getColumnas()];
            for (int i = 0; i < columnas.length; i++) {
                columnas[i] = String.valueOf((char) ('A' + i));
            }
        }
    
        public void setMatriz(MatrizOrtogonal nuevaMatriz) {
            this.matriz = nuevaMatriz;
            this.formula = new Formula(nuevaMatriz);
            fireTableDataChanged();
        }
    
        @Override
        public int getRowCount() {
            return matriz.getFilas();
        }
    
        @Override
        public int getColumnCount() {
            return matriz.getColumnas();
        }
    
        @Override
        public String getColumnName(int col) {
            return columnas[col];
        }
    
        @Override
        public Object getValueAt(int fila, int columna) {
            NodoCelda celda = matriz.buscar(fila, columna);
            if (celda == null) return "";
            if (celda.hasFormula()) {
                try {
                    return formula.evaluar(celda.getFormula());
                } catch (Exception e) {
                    return "Error";
                }
            }
            return celda.getValor() != null ? celda.getValor() : "";
        }
    
        @Override
        public void setValueAt(Object valor, int fila, int columna) {
            String entrada = valor.toString().trim();
            if (entrada.isEmpty()) return;
    
            NodoCelda celda = new NodoCelda(fila, columna);
    
            if (entrada.toLowerCase().startsWith("suma(")
                    || entrada.toLowerCase().startsWith("resta(")
                    || entrada.toLowerCase().startsWith("multiplicacion(")
                    || entrada.toLowerCase().startsWith("division(")) {
                celda.setFormula(entrada);
            } else {
                try {
                    double numero = Double.parseDouble(entrada);
                    celda.setValor(numero);
                } catch (NumberFormatException e) {
                    celda.setValor(entrada);
                }
            }
    
            matriz.insertar(celda);
            fireTableDataChanged();
        }
    
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }
    }

# TableRowHeader.java

    package com.hojacalculo;
    
    import javax.swing.*;
    import java.beans.PropertyChangeEvent;
    import java.beans.PropertyChangeListener;
    
    public class TableRowHeader extends JList<String> {
        public TableRowHeader(JTable table) {
            setFixedCellWidth(50);
            setFixedCellHeight(table.getRowHeight());
            setFont(table.getTableHeader().getFont());
    
            DefaultListModel<String> model = new DefaultListModel<>();
            for (int i = 0; i < table.getRowCount(); i++) {
                model.addElement(String.valueOf(i + 1));
            }
            setModel(model);
    
            table.addPropertyChangeListener("model", new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    updateModel(table);
                }
            });
        }
    
        private void updateModel(JTable table) {
            DefaultListModel<String> model = new DefaultListModel<>();
            for (int i = 0; i < table.getRowCount(); i++) {
                model.addElement(String.valueOf(i + 1));
            }
            setModel(model);
        }
    }

# VentanaHash.java

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


## *com.hojacalculo.model*

# Formula.java

    package com.hojacalculo.model;
    
    public class Formula {
        private MatrizOrtogonal matriz;
    
        public Formula(MatrizOrtogonal matriz) {
            this.matriz = matriz;
        }
    
        public Object evaluar(String formula) {
            if (formula == null) return null;
            System.out.println("Evaluando fórmula: " + formula);
            formula = formula.trim().toLowerCase();
    
            if (formula.startsWith("suma(")) {
                return calcularSuma(formula);
            } else if (formula.startsWith("resta(")) {
                return calcularResta(formula);
            } else if (formula.startsWith("multiplicacion(")) {
                return calcularMultiplicacion(formula);
            } else if (formula.startsWith("division(")) {
                return calcularDivision(formula);
            }
    
            throw new IllegalArgumentException("Operación no soportada: " + formula);
        }
    
        private Object calcularSuma(String formula) {
            String contenido = formula.substring("suma(".length(), formula.length() - 1).trim();
            String[] partes = contenido.split("\\s*,\\s*");
            double resultado = 0;
            for (String parte : partes) {
                int[] coords = parsearCoordenadas(parte);
                NodoCelda celda = matriz.buscarOInsertar(coords[0], coords[1]);
                validarCelda(celda);
                resultado += ((Number) celda.getValor()).doubleValue();
            }
            return resultado;
        }
    
        private Object calcularResta(String formula) {
            String contenido = formula.substring("resta(".length(), formula.length() - 1).trim();
            String[] partes = contenido.split("\\s*,\\s*");
            int[] coords1 = parsearCoordenadas(partes[0]);
            int[] coords2 = parsearCoordenadas(partes[1]);
            NodoCelda celda1 = matriz.buscarOInsertar(coords1[0], coords1[1]);
            NodoCelda celda2 = matriz.buscarOInsertar(coords2[0], coords2[1]);
            validarCelda(celda1);
            validarCelda(celda2);
            return ((Number) celda1.getValor()).doubleValue() - ((Number) celda2.getValor()).doubleValue();
        }
    
        private Object calcularMultiplicacion(String formula) {
            String contenido = formula.substring("multiplicacion(".length(), formula.length() - 1).trim();
            String[] partes = contenido.split("\\s*,\\s*");
            double resultado = 1;
            for (String parte : partes) {
                int[] coords = parsearCoordenadas(parte);
                NodoCelda celda = matriz.buscarOInsertar(coords[0], coords[1]);
                validarCelda(celda);
                resultado *= ((Number) celda.getValor()).doubleValue();
            }
            return resultado;
        }
    
        private Object calcularDivision(String formula) {
            String contenido = formula.substring("division(".length(), formula.length() - 1).trim();
            String[] partes = contenido.split("\\s*,\\s*");
            int[] coords1 = parsearCoordenadas(partes[0]);
            int[] coords2 = parsearCoordenadas(partes[1]);
            NodoCelda celda1 = matriz.buscarOInsertar(coords1[0], coords1[1]);
            NodoCelda celda2 = matriz.buscarOInsertar(coords2[0], coords2[1]);
            validarCelda(celda1);
            validarCelda(celda2);
            double denominador = ((Number) celda2.getValor()).doubleValue();
            if (denominador == 0) throw new ArithmeticException("División por cero");
            return ((Number) celda1.getValor()).doubleValue() / denominador;
        }
    
        private int[] parsearCoordenadas(String referencia) {
            referencia = referencia.replace("(", "").replace(")", "").trim().toUpperCase();
            char letraCol = referencia.charAt(0);
            int columna = letraCol - 'A';
            int fila = Integer.parseInt(referencia.substring(1)) - 1; // A1 → fila 0
            return new int[]{fila, columna};
        }
    
        private void validarCelda(NodoCelda celda) {
            if (celda == null || celda.getValor() == null) {
                throw new IllegalArgumentException("Celda vacía o sin valor");
            }
            if (celda.hasFormula()) {
                throw new IllegalArgumentException("No se pueden anidar fórmulas");
            }
            if (!(celda.getValor() instanceof Number)) {
                throw new IllegalArgumentException("Valor no numérico en celda");
            }
        }
    }

# HojaTrabajo.java

    package com.hojacalculo.model;
    
    public class HojaTrabajo {
        private String nombre;
        private MatrizOrtogonal matriz;
        private HojaTrabajo siguiente;
    
        public HojaTrabajo(String nombre) {
            this.nombre = nombre;
            this.matriz = new MatrizOrtogonal(100, 26); // ← aquí el cambio
            this.siguiente = null;
        }
    
        public String getNombre() {
            return nombre;
        }
    
        public MatrizOrtogonal getMatriz() {
            return matriz;
        }
    
        public HojaTrabajo getSiguiente() {
            return siguiente;
        }
    
        public void setSiguiente(HojaTrabajo siguiente) {
            this.siguiente = siguiente;
        }
    }

# ListaHojas.java

    package com.hojacalculo.model;
    
    public class ListaHojas {
        private HojaTrabajo cabeza;
    
        public void agregarHoja(String nombre) {
            HojaTrabajo nueva = new HojaTrabajo(nombre);
            if (cabeza == null) {
                cabeza = nueva;
            } else {
                HojaTrabajo temp = cabeza;
                while (temp.getSiguiente() != null) {
                    temp = temp.getSiguiente();
                }
                temp.setSiguiente(nueva);
            }
        }
    
        public HojaTrabajo buscarHoja(String nombre) {
            HojaTrabajo temp = cabeza;
            while (temp != null) {
                if (temp.getNombre().equalsIgnoreCase(nombre)) {
                    return temp;
                }
                temp = temp.getSiguiente();
            }
            return null;
        }
    
        public HojaTrabajo getPrimeraHoja() {
            return cabeza;
        }
    }

# MatrizOrtogonal.java

    package com.hojacalculo.model;
    
    public class MatrizOrtogonal {
        private NodoCelda cabeza;
        private int filas;
        private int columnas;
    
        public MatrizOrtogonal(int filas, int columnas) {
            this.filas = filas;
            this.columnas = columnas;
        }
    
        public int getFilas() {
            return filas;
        }
    
        public int getColumnas() {
            return columnas;
        }
    
        public NodoCelda buscar(int fila, int columna) {
            NodoCelda filaActual = cabeza;
            while (filaActual != null && filaActual.getFila() < fila) {
                filaActual = filaActual.getAbajo();
            }
    
            if (filaActual != null && filaActual.getFila() == fila) {
                NodoCelda actual = filaActual;
                while (actual != null && actual.getColumna() < columna) {
                    actual = actual.getDerecha();
                }
                if (actual != null && actual.getColumna() == columna) {
                    return actual;
                }
            }
            return null;
        }
    
        public void insertar(NodoCelda nuevo) {
            int fila = nuevo.getFila();
            int columna = nuevo.getColumna();
    
            // Insertar en la fila correcta (abajo)
            NodoCelda filaActual = cabeza;
            NodoCelda anteriorFila = null;
            while (filaActual != null && filaActual.getFila() < fila) {
                anteriorFila = filaActual;
                filaActual = filaActual.getAbajo();
            }
    
            if (filaActual == null || filaActual.getFila() > fila) {
                nuevo.setAbajo(filaActual);
                if (anteriorFila == null) {
                    cabeza = nuevo;
                } else {
                    anteriorFila.setAbajo(nuevo);
                }
            } else {
                // Insertar en la fila existente (derecha)
                NodoCelda actual = filaActual;
                NodoCelda anterior = null;
                while (actual != null && actual.getColumna() < columna && actual.getFila() == fila) {
                    anterior = actual;
                    actual = actual.getDerecha();
                }
                nuevo.setDerecha(actual);
                if (anterior == null) {
                    if (anteriorFila == null) cabeza = nuevo;
                    else anteriorFila.setAbajo(nuevo);
                } else {
                    anterior.setDerecha(nuevo);
                }
            }
        }
    
        public void insertar(int fila, int columna, Object valor) {
            NodoCelda existente = buscar(fila, columna);
            if (existente != null) {
                existente.setValor(valor);
                existente.setFormula(null);
            } else {
                NodoCelda nuevo = new NodoCelda(fila, columna);
                nuevo.setValor(valor);
                insertar(nuevo);
            }
        }
    
        public void setFormula(int fila, int columna, String formula) {
            NodoCelda existente = buscar(fila, columna);
            if (existente != null) {
                existente.setFormula(formula);
            } else {
                NodoCelda nuevo = new NodoCelda(fila, columna);
                nuevo.setFormula(formula);
                insertar(nuevo);
            }
        }
    
        public void setValor(int fila, int columna, Object valor) {
            insertar(fila, columna, valor);
        }
    
        public NodoCelda buscarOInsertar(int fila, int columna) {
            NodoCelda existente = buscar(fila, columna);
            if (existente != null) {
                return existente;
            }
            NodoCelda nuevo = new NodoCelda(fila, columna);
            insertar(nuevo);
            return nuevo;
        }
    
        public void limpiar() {
            cabeza = null;
        }
    }

# NodoCelda.java

    package com.hojacalculo.model;
    
    public class NodoCelda {
        private int fila;
        private int columna;
        private Object valor;
        private String formula;
        private NodoCelda derecha;
        private NodoCelda abajo;
    
        public NodoCelda(int fila, int columna) {
            this.fila = fila;
            this.columna = columna;
            this.valor = null;
            this.formula = null;
            this.derecha = null;
            this.abajo = null;
        }
    
        public int getFila() {
            return fila;
        }
    
        public int getColumna() {
            return columna;
        }
    
        public Object getValor() {
            return valor;
        }
    
        public void setValor(Object valor) {
            this.valor = valor;
        }
    
        public boolean hasFormula() {
            return formula != null && !formula.isEmpty();
        }
    
        public String getFormula() {
            return formula;
        }
    
        public void setFormula(String formula) {
            this.formula = formula;
        }
    
        public NodoCelda getDerecha() {
            return derecha;
        }
    
        public void setDerecha(NodoCelda derecha) {
            this.derecha = derecha;
        }
    
        public NodoCelda getAbajo() {
            return abajo;
        }
    
        public void setAbajo(NodoCelda abajo) {
            this.abajo = abajo;
        }
    }
