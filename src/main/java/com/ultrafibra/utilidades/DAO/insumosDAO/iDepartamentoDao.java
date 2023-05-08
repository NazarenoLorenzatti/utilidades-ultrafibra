package com.ultrafibra.utilidades.DAO.insumosDAO;

import com.ultrafibra.utilidades.domain.insumos.Departamento;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface iDepartamentoDao extends JpaRepository<Departamento, Long>{
    
    @Override
    public Optional<Departamento> findById(Long id);
}
