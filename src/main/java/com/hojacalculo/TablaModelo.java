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