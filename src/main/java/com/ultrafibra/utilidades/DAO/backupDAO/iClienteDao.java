package com.ultrafibra.utilidades.DAO.backupDAO;

import com.ultrafibra.utilidades.domain.ticketbackup.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iClienteDao  extends JpaRepository<Cliente, Long> {
    
    public Cliente findByIdOdoo(String idOdoo);
}
