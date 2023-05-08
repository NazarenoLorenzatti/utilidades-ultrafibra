package com.ultrafibra.utilidades.domain.insumos;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "titular")
public class Titular implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTitular;

    private String nombre;

    private String apellido;

    @OneToMany(mappedBy = "titular")
    private List<Insumo> insumos;

    @ManyToOne()
    @JoinColumn(name = "id_departamento")
    private Departamento departamento;

    public Titular() {
    }

    public Titular(String nombre, String apellido, Departamento departamento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.departamento = departamento;
    }

    
}
