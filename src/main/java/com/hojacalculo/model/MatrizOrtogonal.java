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
