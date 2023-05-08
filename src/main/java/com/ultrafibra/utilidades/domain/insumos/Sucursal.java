package com.ultrafibra.utilidades.domain.insumos;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "sucursal")
public class Sucursal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sucursal")
    private Long idSucursal;

    @Column(name = "localidad_sucursal", nullable = false)
    private String localidad_sucursal;

    @OneToMany(mappedBy = "sucursal")
    private List<Insumo> insumos;

    public Sucursal() {
    }
    
    public Sucursal(String localidad_sucursal) {
        this.localidad_sucursal = localidad_sucursal;
    }
    
    
}
