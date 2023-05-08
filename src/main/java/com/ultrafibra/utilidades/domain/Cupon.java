package com.ultrafibra.utilidades.domain;

import java.time.LocalDate;

public class Cupon {
  private String numero_cupon, numero_cliente;
    private String razon_social_cliente, mail, numero_cuenta, cbu, titular_cuenta, id_moneda_sirplus, codigo_barras;
    private String id_moneda_vencimiento_sirplus, documentoDeOrigen;
    private LocalDate fecha_vencimiento, fecha, fecha_segundoVencimiento, fecha_tercerVencimiento;
    private int anio, periodo;
    private double valor_pesos_vencimiento, valor_pesos, total, importe, difVencimientos;
    private LocalDate[] feriados = {LocalDate.of(2023, 1, 1), LocalDate.of(2023, 2, 20), LocalDate.of(2023, 2, 21), LocalDate.of(2023, 3, 24),
        LocalDate.of(2023, 4, 2), LocalDate.of(2023, 4, 7), LocalDate.of(2023, 5, 1), LocalDate.of(2023, 5, 25), LocalDate.of(2023, 5, 26), LocalDate.of(2023, 6, 19),
        LocalDate.of(2023, 6, 20), LocalDate.of(2023, 6, 17), LocalDate.of(2023, 7, 9), LocalDate.of(2023, 8, 21), LocalDate.of(2023, 10, 10), LocalDate.of(2023, 10, 16),
        LocalDate.of(2023, 11, 20), LocalDate.of(2023, 12, 8), LocalDate.of(2023, 12, 25)};

    public Cupon() {

    }

    public double getDifVencimientos() {
        return difVencimientos;
    }

    public void setDifVencimientos(double difVencimientos) {
        this.difVencimientos = difVencimientos;
    }

    public LocalDate[] getFeriados() {
        return feriados;
    }

    public void setFeriados(LocalDate[] feriados) {
        this.feriados = feriados;
    }

    public LocalDate getFecha_segundoVencimiento() {
        return fecha_segundoVencimiento;
    }

    public void setFecha_segundoVencimiento(LocalDate fecha_segundoVencimiento) {
        this.fecha_segundoVencimiento = fecha_segundoVencimiento;
    }

    public LocalDate getFecha_tercerVencimiento() {
        return fecha_tercerVencimiento;
    }

    public void setFecha_tercerVencimiento(LocalDate fecha_tercerVencimiento) {
        this.fecha_tercerVencimiento = fecha_tercerVencimiento;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public String getDocumentoDeOrigen() {
        return documentoDeOrigen;
    }

    public void setDocumentoDeOrigen(String documentoDeOrigen) {
        this.documentoDeOrigen = documentoDeOrigen;
    }

    public String getNumero_cupon() {
        return numero_cupon;
    }

    public void setNumero_cupon(String numero_cupon) {
        this.numero_cupon = numero_cupon;
    }

    public String getNumero_cliente() {
        return numero_cliente;
    }

    public void setNumero_cliente(String numero_cliente) {
        this.numero_cliente = numero_cliente;
    }

    public String getRazon_social_cliente() {
        return razon_social_cliente;
    }

    public void setRazon_social_cliente(String razon_social_cliente) {
        this.razon_social_cliente = razon_social_cliente;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNumero_cuenta() {
        return numero_cuenta;
    }

    public void setNumero_cuenta(String numero_cuenta) {
        this.numero_cuenta = numero_cuenta;
    }

    public String getCbu() {
        return cbu;
    }

    public void setCbu(String cbu) {
        this.cbu = cbu;
    }

    public String getTitular_cuenta() {
        return titular_cuenta;
    }

    public void setTitular_cuenta(String titular_cuenta) {
        this.titular_cuenta = titular_cuenta;
    }

    public String getId_moneda_sirplus() {
        return id_moneda_sirplus;
    }

    public void setId_moneda_sirplus(String id_moneda_sirplus) {
        this.id_moneda_sirplus = id_moneda_sirplus;
    }

    public String getCodigo_barras() {
        return codigo_barras;
    }

    public void setCodigo_barras(String codigo_barras) {
        this.codigo_barras = codigo_barras;
    }

    public LocalDate getFecha_vencimiento() {
        return fecha_vencimiento;
    }

    public void setFecha_vencimiento(LocalDate fecha_vencimiento) {
        this.fecha_vencimiento = fecha_vencimiento;
    }

    public String getId_moneda_vencimiento_sirplus() {
        return id_moneda_vencimiento_sirplus;
    }

    public void setId_moneda_vencimiento_sirplus(String id_moneda_vencimiento_sirplus) {
        this.id_moneda_vencimiento_sirplus = id_moneda_vencimiento_sirplus;
    }

    public double getValor_pesos_vencimiento() {
        return valor_pesos_vencimiento;
    }

    public void setValor_pesos_vencimiento(double valor_pesos_vencimiento) {
        this.valor_pesos_vencimiento = valor_pesos_vencimiento;
    }

    public double getValor_pesos() {
        return valor_pesos;
    }

    public void setValor_pesos(double valor_pesos) {
        this.valor_pesos = valor_pesos;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

   
}
