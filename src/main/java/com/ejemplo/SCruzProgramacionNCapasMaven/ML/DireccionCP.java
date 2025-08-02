package com.ejemplo.SCruzProgramacionNCapasMaven.ML;

import java.util.List;

public class DireccionCP {

    private int idPais;
    private String nombrePais;
    private int idEstado;
    private String nombreEstado;
    private int idMunicipio;
    private String nombreMunicipio;
    private int idColonia;
    private String nombreColonia;
    private String codigoPostal;

    public DireccionCP() {
    }

    public DireccionCP(int idPais, String nombrePais, int idEstado, String nombreEstado, int idMunicipio, String nombreMunicipio, int idColonia, String nombreColonia, String codigoPostal) {
        this.idPais = idPais;
        this.nombrePais = nombrePais;
        this.idEstado = idEstado;
        this.nombreEstado = nombreEstado;
        this.idMunicipio = idMunicipio;
        this.nombreMunicipio = nombreMunicipio;
        this.idColonia = idColonia;
        this.nombreColonia = nombreColonia;
        this.codigoPostal = codigoPostal;
    }

    public int getIdPais() {
        return idPais;
    }

    public void setIdPais(int idPais) {
        this.idPais = idPais;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public int getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(int idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public String getNombreMunicipio() {
        return nombreMunicipio;
    }

    public void setNombreMunicipio(String nombreMunicipio) {
        this.nombreMunicipio = nombreMunicipio;
    }

    public int getIdColonia() {
        return idColonia;
    }

    public void setIdColonia(int idColonia) {
        this.idColonia = idColonia;
    }

    public String getNombreColonia() {
        return nombreColonia;
    }

    public void setNombreColonia(String nombreColonia) {
        this.nombreColonia = nombreColonia;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }



}
