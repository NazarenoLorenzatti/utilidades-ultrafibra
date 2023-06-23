package com.ultrafibra.utilidades.utilidades.service.ticketBackup;

import com.ultrafibra.utilidades.domain.ticketbackup.TicketBackup;
import com.ultrafibra.utilidades.domain.ticketbackup.Mensaje;
import com.ultrafibra.utilidades.DAO.backupDAO.iClienteDao;
import com.ultrafibra.utilidades.DAO.backupDAO.iMensajeDao;
import com.ultrafibra.utilidades.utilidades.service.util.SubirArchivo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.ultrafibra.utilidades.DAO.backupDAO.iTicketBackupDao;
import com.ultrafibra.utilidades.domain.ticketbackup.Cliente;

@Data
@Service
public class BackupService {

    @Autowired
    private SubirArchivo subirArchivo;

    @Autowired
    private iClienteDao clienteDao;

    @Autowired
    private iMensajeDao mensajeDao;

    @Autowired
    private iTicketBackupDao ticketBackupDao;

    public List<TicketBackup> listarTickets() {
        return ticketBackupDao.findAll();
    }

    public TicketBackup buscarPorId(String idStr) {
        return ticketBackupDao.findById(Long.valueOf(idStr)).get();
    }

    public List<String> cabecerosBackup() {
        List<String> c = new ArrayList();
        c.add("ID del Cliente");
        c.add("Nombre del Cliente");
        c.add("DNI del cliente");
        c.add("ID del Ticket");
        c.add("Asunto Ticket");
        c.add("Asignado A:");
        c.add("Fecha de Creacion");
        c.add("Fecha de Cierre");
        c.add("Etapa del Ticket");
        c.add("Tipo de Vale");
        return c;
    }

    public TicketBackup buscarTicketSiguiente(String idStr) {
        Long id = null;
        for (TicketBackup t : ticketBackupDao.findAll()) {
            if (Long.valueOf(idStr) <= t.getIdBackup()) {
                id = Long.valueOf(idStr);
            } else {
                id = Long.valueOf("-1");
            }
        }
        if (id != Long.valueOf("-1")) {
            return ticketBackupDao.findById(id).get();
        } else {
            return ticketBackupDao.findAll().get(0);
        }
    }

    public TicketBackup buscarTicketAnterior(String idStr) {
        Long id = null;
        List<TicketBackup> lista = ticketBackupDao.findAll();
        
        for(int i = lista.size()-1; i > 1; i--) {
            if (Long.valueOf(idStr) >= lista.get(i).getIdBackup()) {
                id = Long.valueOf(idStr);
            } else {
                id = Long.valueOf("-1");
            }
        }
        
        if (id != Long.valueOf("-1")) {
            return ticketBackupDao.findById(id).get();
        } else {
            return ticketBackupDao.findAll().get(ticketBackupDao.findAll().size() -1);
        }
    }

    public int obtenerIndice(String idStr) {
        Long id = null;
        for (TicketBackup t : ticketBackupDao.findAll()) {
            if (Long.valueOf(idStr) <= t.getIdBackup()) {
                id = Long.valueOf(idStr);
            } else {
                id = Long.valueOf("-1");
            }
        }
        if (id != Long.valueOf("-1")) {
            return ticketBackupDao.findAll().indexOf(ticketBackupDao.findById(Long.valueOf(idStr)).get());
        } else {
            return 0;
        }

    }

    public int obtenerIndiceAnterior(String idStr) {
        Long id = null;
        List<TicketBackup> lista = ticketBackupDao.findAll();
        for (int i = lista.size()-1; i > 0; i--) {
            if (Long.valueOf(idStr) >= lista.get(i).getIdBackup()) {
                id = Long.valueOf(idStr);
            } else {
                id = Long.valueOf("-1");
            }
        }
        if (id != Long.valueOf("-1")) {
            return ticketBackupDao.findAll().indexOf(ticketBackupDao.findById(Long.valueOf(idStr)).get());
        } else {
            return ticketBackupDao.findAll().size()-1;
        }
    }

    public List<TicketBackup> importar(MultipartFile file) throws IOException {
        for (TicketBackup t : ticketBackupDao.findAll()) {
            System.out.println("t = " + t.getCliente().getIdCliente());
            System.out.println("t = " + t.getCliente().getDniCliente());
            System.out.println("t = " + t.getCliente().getNombreCliente());
            System.out.println("t = " + t.getCliente().getIdOdoo());
        }
        subirArchivo.upload(file);
        TicketBackup anterior = null;
        for (List<String> fila : subirArchivo.getLectorExcel().getDatos()) {
            if (fila.get(0) != null && !fila.get(0).equals("")) {
                TicketBackup t = new TicketBackup();
                Mensaje m = new Mensaje();

                if (clienteDao.findByIdOdoo(fila.get(0)) == null) {
                    Cliente c = new Cliente();
                    c.setIdOdoo(fila.get(0));
                    c.setNombreCliente(fila.get(1));
                    c.setDniCliente(fila.get(2));
                    c.setDireccionCliente(fila.get(3));
                    clienteDao.save(c);
                }
                t.setCliente(clienteDao.findByIdOdoo(fila.get(0)));
                t.setTicketId(fila.get(4));
                t.setAsunto(fila.get(5));
                t.setAsignatario(fila.get(6));
                t.setFechaCreacion(fila.get(7));
                t.setEtapaTicket(fila.get(8));
                t.setFechaCierre(fila.get(9));
                t.setDescripcion(fila.get(10));
                t.setTipoVale(fila.get(11));

                anterior = ticketBackupDao.save(t);
                System.out.println("llego hasta ACA 3");

                if (fila.get(12) == "") {
                    m.setContenido("MENSAJE GENERICO DE CREACION DE TICKET \n\n MEGALINK-SRL");
                } else {
                    m.setContenido(fila.get(12).replace("<p>", "").replace("</p>", ""));
                }
                m.setFechaDelMensaje(fila.get(13));
                m.setCreador(fila.get(14));
                m.setTicket(t);
                mensajeDao.save(m);

            } else {

                TicketBackup t = ticketBackupDao.findById(anterior.getIdBackup()).get();
                Mensaje m = new Mensaje();
                if (fila.get(12) == "") {
                    m.setContenido("MENSAJE GENERICO DE CREACION DE TICKET \n\n MEGALINK-SRL");
                } else {
                    m.setContenido(fila.get(12).replace("<p>", "").replace("</p>", ""));
                }
                m.setFechaDelMensaje(fila.get(13));
                m.setCreador(fila.get(14));
                m.setTicket(t);
                mensajeDao.save(m);
            }
        }
        return ticketBackupDao.findAll();
    }

}
