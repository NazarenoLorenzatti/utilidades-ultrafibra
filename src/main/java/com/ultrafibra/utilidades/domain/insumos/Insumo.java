package com.ultrafibra.utilidades.domain.insumos;

import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import lombok.Data;

@Entity
@Data
@Table(name = "insumo")
public class Insumo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_insumo")
    private Long idInsumo;

    @Column(name="codigo_barras", unique = true)
    private String codigoBarras;

    @Column(name = "nombre_insumo", nullable = false, unique = true)
    private String nombre_insumo;

    private String descripcion;


    private Date fecha_compra;

    @OneToOne
    @JoinColumn(name = "id_sucursal")
    private Sucursal sucursal;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @ManyToOne()
    @JoinColumn(name = "id_titular")
    private Titular titular;

    @ManyToOne()
    @JoinColumn(name = "id_departamento")
    private Departamento departamento;

    public Insumo() {
    }
    
    public Insumo(String codigoBarras, String nombre_insumo, String descripcion, Date fecha_compra, Sucursal sucursal, Categoria categoria, Titular titular, Departamento departamento) {
        this.codigoBarras = codigoBarras;
        this.nombre_insumo = nombre_insumo;
        this.descripcion = descripcion;
        this.fecha_compra = fecha_compra;
        this.sucursal = sucursal;
        this.categoria = categoria;
        this.titular = titular;
        this.departamento = departamento;
    }
    
    
}
