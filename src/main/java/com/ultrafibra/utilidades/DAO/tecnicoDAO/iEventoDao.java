package com.ultrafibra.utilidades.DAO.tecnicoDAO;

import com.ultrafibra.utilidades.domain.Evento;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iEventoDao extends JpaRepository<Evento, Long> {
    @Override
    public Optional<Evento> findById(Long id);
    
}
