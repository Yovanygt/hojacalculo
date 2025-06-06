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
