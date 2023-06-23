package com.ultrafibra.utilidades.domain.ticketbackup;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "tickets_backup")
public class TicketBackup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_backup")
    private Long idBackup;

    @Column(name = "ticket_id")
    private String ticketId;
    
    private String asunto;
    
    private String asignatario;
    
    @Column (name = "fecha_de_creacion")
    private String fechaCreacion;
   
    @Column (name = "etapa_ticket")
    private String etapaTicket;
    
    @Column (name = "fecha_de_cierre")
    private String fechaCierre;
    
    @Column (name = "tipo_de_vale")
    private String tipoVale;
    
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "clientes_id_cliente")
    private Cliente cliente;
    
    @OneToMany(mappedBy = "ticket")
    private List<Mensaje> mensajes;

    public TicketBackup() {
    }

    public TicketBackup(String ticketId, String asunto, String asignatario, String fechaCreacion, String etapaTicket,
            String fechaCierre, String tipoVale, String descripcion) {
        
        this.ticketId = ticketId;
        this.asunto = asunto;
        this.asignatario = asignatario;
        this.fechaCreacion = fechaCreacion;
        this.etapaTicket = etapaTicket;
        this.fechaCierre = fechaCierre;
        this.tipoVale = tipoVale;
        this.descripcion = descripcion;
    }
    
    
}
