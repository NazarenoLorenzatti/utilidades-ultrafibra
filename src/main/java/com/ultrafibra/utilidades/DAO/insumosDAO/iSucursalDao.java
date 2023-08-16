package com.ultrafibra.utilidades.DAO.insumosDAO;

import com.ultrafibra.utilidades.domain.insumos.Sucursal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iSucursalDao extends JpaRepository<Sucursal, Long> {

    @Override
    public Optional<Sucursal> findById(Long id);
    
}
