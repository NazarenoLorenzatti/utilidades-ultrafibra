package com.ultrafibra.utilidades.domain.insumos;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "categoria")
public class Categoria  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long idCategoria;
    
    @Column(name = "nombre_categoria", nullable = false)
    private String nombre_categoria;
    
    @OneToMany(mappedBy = "categoria")
    private List<Insumo> insumos;
    
    public Categoria(){
        
    }
    
    public Categoria(String nombre){
        this.nombre_categoria=nombre;
    }
    
}
