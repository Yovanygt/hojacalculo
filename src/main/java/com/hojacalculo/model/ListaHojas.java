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
