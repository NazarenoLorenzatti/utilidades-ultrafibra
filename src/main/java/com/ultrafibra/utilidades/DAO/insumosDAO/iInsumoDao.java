package com.ultrafibra.utilidades.DAO.insumosDAO;

import com.ultrafibra.utilidades.domain.insumos.Insumo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iInsumoDao extends JpaRepository<Insumo, Long>{
    
    public Insumo findByCodigoBarras(String codigoBarras);
    
    public List<Insumo> findByTitular(String titular);
    
     public List<Insumo> findByDepartamento(String departamento);

    public List<Insumo> findBySucursal(String sucursal);

    public List<Insumo> findByCategoria(Long idCategoria);
}
