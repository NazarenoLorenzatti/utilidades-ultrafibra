package com.ultrafibra.utilidades.DAO.backupDAO;

import com.ultrafibra.utilidades.domain.ticketbackup.TicketBackup;
import org.springframework.data.jpa.repository.JpaRepository;


public interface iTicketBackupDao extends JpaRepository<TicketBackup, Long> {
    
    
    
}
