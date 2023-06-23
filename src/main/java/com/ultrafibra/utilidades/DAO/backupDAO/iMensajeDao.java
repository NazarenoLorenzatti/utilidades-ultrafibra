package com.ultrafibra.utilidades.DAO.backupDAO;

import com.ultrafibra.utilidades.domain.ticketbackup.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iMensajeDao extends JpaRepository<Mensaje, Long> {
}
