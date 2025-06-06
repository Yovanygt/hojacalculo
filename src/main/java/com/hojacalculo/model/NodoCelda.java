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
