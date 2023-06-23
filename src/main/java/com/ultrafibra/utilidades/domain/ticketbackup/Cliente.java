package com.ultrafibra.utilidades.domain.ticketbackup;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(name = "id_odoo")
    private String idOdoo;

    @Column(name = "nombre_cliente")
    private String nombreCliente;

    @Column(name = "dni_cliente")
    private String dniCliente;

    @Column(name = "direccion_cliente")
    private String direccionCliente;

    @OneToMany
    @JoinColumn(name = "clientes_id_cliente")
    private List<TicketBackup> tickets;

    public Cliente() {
    }

    public Cliente(String idOdoo, String nombreCliente, String dniCliente, String direccionCliente, List<TicketBackup> tickets) {
        this.idOdoo = idOdoo;
        this.nombreCliente = nombreCliente;
        this.dniCliente = dniCliente;
        this.direccionCliente = direccionCliente;
        this.tickets = tickets;
    }
    
    
    
}
