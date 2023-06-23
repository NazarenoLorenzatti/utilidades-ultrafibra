package com.ultrafibra.utilidades.controller;

import com.ultrafibra.utilidades.DAO.insumosDAO.*;
import com.ultrafibra.utilidades.utilidades.service.insumos.InsumosService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class insumosController {

    @Autowired
    private InsumosService is;

    @Autowired
    private iInsumoDao insumoDao;

    @GetMapping("/cerrar-alerta")
    public String cerrarAlerta(Model model) throws IOException {
        model.addAttribute("alertaMensaje", false);
        return "insumos/insumos";
    }

    //---------------------------------------------CATEGORIAS-----------------------------------------------------------------------------
    @GetMapping("/crearCategoria")
    public String crearCategoria(@RequestParam("nombreCategoria") String nombre, HttpSession session) throws IOException {
        session.setAttribute("categorias", is.crearCategoria(nombre));
        return "insumos/insumos";
    }

    @PostMapping("/updateCategoria")
    public String updateCategoria(@RequestParam("idUpdate") String idStr, @RequestParam("nombreUpdate") String nombreNuevo, HttpSession session) throws IOException {
        session.setAttribute("categorias", is.actualizarCategoria(nombreNuevo, idStr));
        session.setAttribute("listaDeInsumos", insumoDao.findAll());
        return "insumos/insumos";
    }

    @PostMapping("/eliminarCategoria")
    public String eliminarCategoria(@RequestParam("idEliminar") String idStr, Model model, HttpSession session) {
        model.addAttribute("alertaMensaje", false);
        try {
            session.setAttribute("categorias", is.eliminarCategoria(idStr));
            return "insumos/insumos";
        } catch (Exception e) {
            session.setAttribute("alertaMensaje", true);
            return "insumos/insumos";
        }
    }

    //--------------------------------------SUCURSALES------------------------------------------------------------------------
    @GetMapping("/crearSucursal")
    public String crearSucursal(@RequestParam("nombreSucursal") String nombre, HttpSession session) throws IOException {
        session.setAttribute("sucursales", is.crearSucursal(nombre));
        return "insumos/insumos";
    }

    @PostMapping("/updateSucursal")
    public String updateSucursal(@RequestParam("idUpdate") String idStr, @RequestParam("nombreUpdate") String nombreNuevo, HttpSession session) throws IOException {
        session.setAttribute("sucursales", is.actualizarSucursal(nombreNuevo, idStr));
        session.setAttribute("listaDeInsumos", insumoDao.findAll());
        return "insumos/insumos";
    }

    @PostMapping("/eliminarSucursal")
    public String eliminarSucursal(@RequestParam("idEliminar") String idStr, Model model, HttpSession session) {
        model.addAttribute("alertaMensaje", false);
        try {
            session.setAttribute("sucursales", is.eliminarSucursal(idStr));
            return "insumos/insumos";
        } catch (Exception e) {
            session.setAttribute("alertaMensaje", true);
            return "insumos/insumos";
        }
    }

    // ---------------------------------------------------------- TITULAR ----------------------------------------------------------------
    @GetMapping("/crearTitular")
    public String crearTitular(@RequestParam("nombreTitular") String nombre, @RequestParam("apellidoTitular") String apellido,
            @RequestParam("departamento") Long idDepartamento, HttpSession session) throws IOException {
        session.setAttribute("titulares", is.crearTitular(nombre, apellido, idDepartamento));
        return "insumos/insumos";
    }

    @PostMapping("/updateTitular")
    public String updateTitular(@RequestParam("textIdTit") String idStr, @RequestParam("nombreUpdate") String nombreNuevo,
            @RequestParam("apellidoUpdate") String apellidoNuevo, @RequestParam("departamento") String idDepartamento, HttpSession session) throws IOException {
        session.setAttribute("titulares", is.actualizarTitular(idStr, nombreNuevo, apellidoNuevo, idDepartamento));
        session.setAttribute("listaDeInsumos", insumoDao.findAll());
        return "insumos/insumos";
    }

    @PostMapping("/eliminarTitular")
    public String eliminarTitular(@RequestParam("idEliminar") String idStr, Model model, HttpSession session) {
        model.addAttribute("alertaMensaje", false);
        try {
            session.setAttribute("titulares", is.eliminarTitular(idStr));
            return "insumos/insumos";
        } catch (Exception e) {
            session.setAttribute("alertaMensaje", true);
            return "insumos/insumos";
        }

    }

    // ------------------------------------------------------------- Departamento -------------------------------------------------------
    @GetMapping("/crearDepartamento")
    public String crearDepartamento(@RequestParam("nombreDepartamento") String nombre, HttpSession session) throws IOException {
        session.setAttribute("departamentos", is.crearDepartamento(nombre));
        return "insumos/insumos";
    }

    @PostMapping("/updateDepartamento")
    public String updateDepartamento(@RequestParam("textIdDep") String idStr, @RequestParam("nombreUpdate") String nombreNuevo, HttpSession session) throws IOException {
        session.setAttribute("departamentos", is.actualizarDepartamento(nombreNuevo, idStr));
        session.setAttribute("listaDeInsumos", insumoDao.findAll());
        return "insumos/insumos";
    }

    @PostMapping("/eliminarDepartamento")
    public String eliminarDepartamento(@RequestParam("idEliminar") String idStr, Model model, HttpSession session) {
        model.addAttribute("alertaMensaje", false);
        try {
            session.setAttribute("departamentos", is.eliminarDepartamento(idStr));
            return "insumos/insumos";
        } catch (Exception e) {
            model.addAttribute("alertaMensaje", true);
            return "insumos/insumos";
        }
    }

    // ------------------------------------------------------------INSUMOS---------------------------------------------------------
    @PostMapping("/guardarInsumo")
    public String guardar(@RequestParam("codigoBarras") String codigoBarras, @RequestParam("nombreInsumo") String nombreInsumo,
            @RequestParam("descripcion") String descripcion, @RequestParam("fecha") Date fecha, @RequestParam("titular") String idTitularStr,
            @RequestParam("departamento") String idDepartamentoStr, @RequestParam("categoria") String idCategoriaStr,
            @RequestParam("sucursal") String idSucursalStr, HttpSession session) throws IOException {

        session.setAttribute("listaDeInsumos", is.crearInsumo(codigoBarras, nombreInsumo, descripcion, fecha,
                idTitularStr, idDepartamentoStr, idCategoriaStr, idSucursalStr));
        return "insumos/insumos";
    }

    @PostMapping("/editarInsumo")
    public String editarInsumo(@RequestParam("idModificarInsumo") String idInsumoStr, @RequestParam("codigoBarras") String codigoBarras, @RequestParam("nombreInsumo") String nombreInsumo,
            @RequestParam("descripcion") String descripcion, @RequestParam("fecha") Date fecha, @RequestParam("titular") String idTitularStr,
            @RequestParam("departamento") String idDepartamentoStr, @RequestParam("categoria") String idCategoriaStr,
            @RequestParam("sucursal") String idSucursalStr, HttpSession session) throws IOException {

        session.setAttribute("listaDeInsumos", is.editarInsumo(idInsumoStr, codigoBarras,
                nombreInsumo, descripcion, fecha, idTitularStr, idDepartamentoStr, idCategoriaStr, idSucursalStr));
        return "insumos/insumos";
    }

    @PostMapping("/editarInsumo-masivo")
    public String editarInsumoMasivo(@RequestParam("arrayId") String[] idInsumos, @RequestParam("codigoBarras") String codigoBarras, @RequestParam("nombreInsumo") String nombreInsumo,
            @RequestParam("descripcion") String descripcion, @RequestParam("fecha") Date fecha, @RequestParam("titular") String idTitularStr,
            @RequestParam("departamento") String idDepartamentoStr, @RequestParam("categoria") String idCategoriaStr,
            @RequestParam("sucursal") String idSucursalStr, HttpSession session) throws IOException {

        session.setAttribute("listaDeInsumos", is.editarInsumoMasivo(idInsumos, codigoBarras, nombreInsumo,
                descripcion, fecha, idTitularStr, idDepartamentoStr, idCategoriaStr, idSucursalStr));
        return "insumos/insumos";
    }

    @RequestMapping(value = "/eliminarInsumo/{idInsumo}", method = RequestMethod.GET)
    public String eliminarInsumo(@PathVariable("idInsumo") String idStr, HttpSession session) throws IOException {
        session.setAttribute("listaDeInsumos", is.eliminarInsumo(idStr));
        return "insumos/insumos";
    }

    @GetMapping("/imprimir-listado")
    public void imprimirListado(HttpServletResponse response) throws IOException {
        Workbook workbook = is.imprimirListado();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=Insumos UltraFibra.xls");
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @PostMapping("/importar")
    public String uploadFile(@RequestPart("file") MultipartFile file, HttpSession session) throws IOException {
        try {
            session.setAttribute("listaDeInsumos", is.importar(file));
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            System.out.println("Error en el Archivo subido");
            return "/errores/errorImportar";
        }
        return "insumos/insumos";
    }

    

}
