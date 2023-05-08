package com.ultrafibra.utilidades.DAO.usuariosDAO;

import com.ultrafibra.utilidades.domain.usuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

// No es necesario crear una implementacion de esta interface ya que spring la crea automaticamente
public interface iUsuarioDao extends JpaRepository<Usuario, Long>{
    Usuario findByUsername(String username);
}
