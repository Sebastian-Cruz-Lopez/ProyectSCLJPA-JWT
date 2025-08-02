package com.ejemplo.SCruzProgramacionNCapasMaven.JPA;


public class ResultValidaDatos {
    
    private int fila;
    private String texto;
    private String descripcion;

    public ResultValidaDatos(int fila, String texto, String descripcion) {
        this.fila = fila;
        this.texto = texto;
        this.descripcion = descripcion;
    }
    
    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
    
}
