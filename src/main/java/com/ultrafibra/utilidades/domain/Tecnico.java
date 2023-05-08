package com.ultrafibra.utilidades.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Data;

@Data
@Entity
@Table(name = "tecnico")
public class Tecnico implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tecnico")
    private Long idTecnico;
    
     @Column(name = "nombre_tecnico")
    private String nombre_tecnico;
    
     @Column(name = "celular", nullable = false)
    private String celular;

    public Tecnico(String nombre_tecnico, String celular) {
        this.nombre_tecnico = nombre_tecnico;
        this.celular = celular;
    }

    public Tecnico() {
    }
     
     
}
