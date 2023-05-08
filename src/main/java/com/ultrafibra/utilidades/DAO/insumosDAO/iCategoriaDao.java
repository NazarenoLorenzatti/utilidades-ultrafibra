package com.ultrafibra.utilidades.DAO.insumosDAO;

import com.ultrafibra.utilidades.domain.insumos.Categoria;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface iCategoriaDao extends JpaRepository<Categoria, Long> {
    @Override
    public Optional<Categoria> findById(Long id);
}
