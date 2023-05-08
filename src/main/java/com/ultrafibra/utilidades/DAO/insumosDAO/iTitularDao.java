package com.ultrafibra.utilidades.DAO.insumosDAO;

import com.ultrafibra.utilidades.domain.insumos.Titular;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface iTitularDao extends CrudRepository<Titular, Long> {

    @Override
    public Optional<Titular> findById(Long id);
}
