package com.ultrafibra.utilidades.domain.insumos;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "departamento")
public class Departamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDepartamento;

    @Column(name = "nombre_departamento", nullable = false)
    private String nombre_departamento;

    @OneToMany(mappedBy = "departamento")
    private List<Insumo> insumos;

    public Departamento() {
    }

    public Departamento(String nombre_departamento) {
        this.nombre_departamento = nombre_departamento;
    }



}
