package com.ultrafibra.utilidades.domain.ticketbackup;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Data;

@Data
@Entity
@Table(name = "mensajes_ticket")
public class Mensaje implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje")
    private Long idMensaje;

    private String contenido;

    @Column(name = "fecha_mensaje")
    private String fechaDelMensaje;

    private String creador;

    @ManyToOne
    @JoinColumn(name = "tickets_id_ticket")
    private TicketBackup ticket;

    public Mensaje() {
    }

    public Mensaje(String contenido, String fechaDelMensaje, String creador) {
        this.contenido = contenido;
        this.fechaDelMensaje = fechaDelMensaje;
        this.creador = creador;
    }

}
