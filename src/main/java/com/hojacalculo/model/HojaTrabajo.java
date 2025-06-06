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
