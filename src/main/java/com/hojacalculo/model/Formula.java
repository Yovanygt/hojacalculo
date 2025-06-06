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
