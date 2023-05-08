
package com.ultrafibra.utilidades.DAO.tecnicoDAO;

import com.ultrafibra.utilidades.domain.Tecnico;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iTecnicoDao extends JpaRepository<Tecnico, Long> {
    @Override
    public Optional<Tecnico> findById(Long id);
}

