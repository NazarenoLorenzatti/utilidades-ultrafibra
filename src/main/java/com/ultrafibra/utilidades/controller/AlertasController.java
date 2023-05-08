package com.ultrafibra.utilidades.controller;

import com.ultrafibra.utilidades.DAO.tecnicoDAO.*;
import com.ultrafibra.utilidades.domain.*;
import com.ultrafibra.utilidades.utilidades.service.alertas.EnviarAlerta;
import com.ultrafibra.utilidades.utilidades.service.alertas.TecnicoService;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AlertasController {
    
    @Autowired    
    private TecnicoService ts;    
    
    @Autowired
    private iTecnicoDao tecnicoDao;
    
    @Autowired
    private iEventoDao eventoDao;
    
    @Autowired
    private EnviarAlerta lm;
    
    @PostMapping("/agregarTecnico")
    public String agregar(@RequestParam("nombreTecnico") String nombre, @RequestParam("celular") String celular,
            HttpSession session) throws IOException {
        ts.agregar(nombre, celular);
        session.setAttribute("tecnicos", tecnicoDao.findAll());
        return "alertas/alertas";
    }
    
    @PostMapping("/editarTecnico")
    public String editar(@RequestParam("nombreTecnico") String nombre, @RequestParam("celular") String celular,
            @RequestParam("idModificarTecnico") String idStr, HttpSession session) throws IOException {
        ts.editar(nombre, celular, idStr);
        session.setAttribute("tecnicos", tecnicoDao.findAll());
        return "alertas/alertas";
    }
    
    @RequestMapping(value = "/eliminarTecnico/{idTecnico}", method = RequestMethod.GET)
    public String eliminarInsumo(@PathVariable("idTecnico") String idStr, HttpSession session) throws IOException {
        ts.eliminar(idStr);
        session.setAttribute("tecnicos", tecnicoDao.findAll());
        return "alertas/alertas";
    }
    
    @PostMapping("/registrar-evento")
    public String registrarEvento(@RequestParam("evento") String evento, HttpSession session) throws IOException {
        eventoDao.save(new Evento(evento));
        session.setAttribute("eventos", eventoDao.findAll());
        return "alertas/alertas";
    }
    
    @GetMapping("/vaciarRegistro")
    public String vaciarLog(HttpSession session) throws IOException {
        eventoDao.deleteAll();
        session.setAttribute("eventos", eventoDao.findAll());
        return "alertas/alertas";
    }
    
    @GetMapping("/iniciar")
    public String iniciarPrograma(Model model) throws IOException {
        lm.iniciarLecturaMails();
        return "alertas/alertas";
    }
    
    @GetMapping("/parar")
    public String pararPrograma(Model model) {
        lm.detenerLecturaMails();
        return "alertas/alertas";
    }
    
}
