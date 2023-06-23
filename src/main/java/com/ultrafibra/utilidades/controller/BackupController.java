package com.ultrafibra.utilidades.controller;

import com.ultrafibra.utilidades.utilidades.service.ticketBackup.BackupService;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class BackupController {

    @Autowired
    private BackupService ts;

    @PostMapping("/importarTickets")
    public String uploadFileTickets(@RequestPart("file") MultipartFile file, HttpSession session) throws IOException {
        try {
            session.setAttribute("listaDeTickets", ts.importar(file));
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            System.out.println("Error en el Archivo subido");
            return "/errores/errorImportar";
        }
        return "backup/backup";
    }

    @RequestMapping(value = "/abrirTicket/{idTicket}", method = RequestMethod.GET)
    public String abrirTicket(@PathVariable("idTicket") String idStr, Model model) throws IOException {
        model.addAttribute("ticket", ts.buscarPorId(idStr));
        model.addAttribute("indiceActual", ts.obtenerIndice(idStr));
        return "backup/ticket";
    }

    @RequestMapping(value = "/TicketSiguiente/{idTicket}", method = RequestMethod.GET)
    public String ticketSiguiente(@PathVariable("idTicket") String idStr, Model model,HttpSession session) throws IOException {
        model.addAttribute("ticket", ts.buscarTicketSiguiente(idStr));
        model.addAttribute("indiceActual", ts.obtenerIndice(idStr));
        return "backup/ticket";
    }
    
        @RequestMapping(value = "/TicketAnterior/{idTicket}", method = RequestMethod.GET)
    public String ticketAnterior(@PathVariable("idTicket") String idStr, Model model,HttpSession session) throws IOException {
        model.addAttribute("ticket", ts.buscarTicketAnterior(idStr));
        model.addAttribute("indiceActual", ts.obtenerIndiceAnterior(idStr));
        return "backup/ticket";
    }
}
