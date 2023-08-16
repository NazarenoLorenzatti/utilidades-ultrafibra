package com.ultrafibra.utilidades.controller;

import com.ultrafibra.utilidades.DAO.insumosDAO.*;
import com.ultrafibra.utilidades.DAO.tecnicoDAO.*;
import com.ultrafibra.utilidades.domain.ticketbackup.TicketBackup;
import com.ultrafibra.utilidades.utilidades.service.ticketBackup.BackupService;
import jakarta.servlet.http.HttpSession;
import java.io.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MappingController {

    @Autowired
    private iTecnicoDao tecnicoDao;
    @Autowired
    private BackupService ticketService;
    @Autowired
    private iEventoDao eventoDao;
    @Autowired
    private iInsumoDao insumoDao;
    @Autowired
    private iCategoriaDao categoriaDao;
    @Autowired
    private iSucursalDao sucursalDao;
    @Autowired
    private iTitularDao titularDao;
    @Autowired
    private iDepartamentoDao departamentoDao;

    @GetMapping("/insumos/crearInsumo")
    public String crearInsumo(Model model) throws IOException {
        return "insumos/crearInsumo";
    }

    @GetMapping("/insumos/insumos")
    public String tablaInsumos(HttpSession session) throws IOException {
        session.setAttribute("listaDeInsumos", insumoDao.findAll());
        session.setAttribute("categorias", categoriaDao.findAll());
        session.setAttribute("sucursales", sucursalDao.findAll());
        session.setAttribute("titulares", titularDao.findAll());
        session.setAttribute("departamentos", departamentoDao.findAll());
        return "insumos/insumos";
    }

    @GetMapping("/alertas/alertas")
    public String alertas(HttpSession session) throws IOException {
        session.setAttribute("tecnicos", tecnicoDao.findAll());
        session.setAttribute("eventos", eventoDao.findAll());
        return "alertas/alertas";
    }

    // REDIRECCIONAMIENTO A PLANTILLAS
    @GetMapping("/comercial/homebankingLink")
    public String homebankingLink(Model model) throws IOException {
        return "comercial/homebankingLink";
    }

    @GetMapping("/comercial/homebankingBanelco")
    public String homebankingBanelco(Model model) throws IOException {
        return "comercial/homebankingBanelco";
    }

    @GetMapping("/comercial/sirplus")
    public String sirplus(Model model) throws IOException {
        return "comercial/sirplus";
    }

    @GetMapping("/comercial/debitosAutomaticos")
    public String debitosAutomaticos(Model model) throws IOException {
        return "comercial/debitosAutomaticos";
    }
    
        @GetMapping("/comercial/repuestaDebitos")
    public String resumenDebitosAutomaticos(Model model) throws IOException {
        return "comercial/repuestaDebitos";
    }

    @GetMapping("/comercial/macroClick")
    public String macroClick(Model model) throws IOException {
        return "comercial/macroClick";
    }

    @GetMapping("/backup/backup")
    public String backup(HttpSession session) throws IOException {
        session.setAttribute("listaDeTickets", ticketService.listarTickets());
        session.setAttribute("cabecerosTicket", ticketService.cabecerosBackup());
        return "backup/backup";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }
    

//    @PostMapping("/login")
//    public String loginSucces() {
//        return "macroClick";
//    }
}
