package com.ultrafibra.utilidades.controller;

import com.ultrafibra.utilidades.utilidades.service.basesDeuda.*;
import com.ultrafibra.utilidades.utilidades.service.extractos.*;
import com.ultrafibra.utilidades.utilidades.service.util.EscritorXLS;
import com.ultrafibra.utilidades.utilidades.service.util.SubirArchivo;
import jakarta.servlet.http.*;
import org.springframework.core.io.Resource;
import java.io.*;
import java.time.LocalDate;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AppController {

    @Autowired
    private CrearBaseSirplus cbSirplus;

    @Autowired
    private CrearBasePmc cbPMC;

    @Autowired
    private SubirArchivo subirArchivo;

    @Autowired
    private AbrirArchivoDeTexto subirTxt;

    @Autowired
    private CrearBaseLink cbLink;

    @PostMapping("/uploadExcelFile")
    public String uploadFile(@RequestPart("file") MultipartFile file, @RequestParam("uri2") String uri, Model model) throws IOException {
        subirArchivo.upload(file);
        model.addAttribute("message", "El archivo - " + file.getOriginalFilename() + " - Se subio Correctamente!");
        model.addAttribute("cabeceros", subirArchivo.getLectorExcel().getCabeceros());
        model.addAttribute("datos", subirArchivo.getLectorExcel().getDatos());
        // Retorna el uri original donde se sube el archivo para actualizar vista
        return subirArchivo.getUri(uri);
    }

    @PostMapping("/uploadTxtFile")
    public String uploadFileTxt(@RequestPart("file") MultipartFile file, @RequestParam("uri3") String uri, Model model) throws IOException {
        subirTxt.leerArchivo(file.getInputStream());
        model.addAttribute("message", "El archivo - " + file.getOriginalFilename() + " - Se subio Correctamente!");
        model.addAttribute("cabeceros", subirTxt.getCabeceros());
        model.addAttribute("datos", subirTxt.getFilas());
        String retorno = subirTxt.getUri(uri);
        return retorno;
    }

    @GetMapping("comercial/downloadLink")
    public ResponseEntity<Resource> downloadFile(@RequestParam("filename") String filename, Model model) throws IOException {
        // Crea un objeto Resource para el archivo .txt
        FileSystemResource file = cbLink.crearArchivo(subirArchivo.getLectorExcel().getDatos(), filename.toUpperCase());
        // Devuelve el archivo .txt como una respuesta HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "'attachment; filename=" + filename.toUpperCase());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.TEXT_PLAIN)
                .body(file);
    }

    @GetMapping("comercial/downloadLinkControl")
    public ResponseEntity<Resource> downloadFileControl(@RequestParam("filename") String filename) throws IOException {
        FileSystemResource file = cbLink.archivoDeControl(subirArchivo.getLectorExcel().getDatos(), filename.toUpperCase());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename.replace("P", "C").toUpperCase());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.TEXT_PLAIN)
                .body(file);
    }

    @GetMapping("comercial/downloadPmc")
    public ResponseEntity<Resource> downloadFilePmc(@RequestParam("filename") String filename) throws IOException {
        FileSystemResource file = cbPMC.crearArchivo(subirArchivo.getLectorExcel().getDatos(), filename.toUpperCase());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "'attachment; filename=" + filename.toUpperCase());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.TEXT_PLAIN)
                .body(file);
    }

    @GetMapping("comercial/downloadXML")
    public ResponseEntity<Resource> downloadFileXML(@RequestParam("filename") String filename) throws IOException {
        // Crea el Recurso en base al archivo XML creado a partir de la informacion enviada.
        ByteArrayResource resource = cbSirplus.leerTabla(subirArchivo.getLectorExcel().getDatos(), filename.toUpperCase());

        // Devuelve el archivo XML como una respuesta HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "'attachment; filename=" + filename.toUpperCase());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.TEXT_XML)
                .body(resource);
    }

    @GetMapping("comercial/downloadMacroClick")
    public void downloadFileMacroClick(@RequestParam("filename") String filename, HttpServletResponse response) throws IOException {
        var extractoMC = new ExtractoMacroClicks(subirArchivo.getLectorExcel().getDatos(), subirArchivo.getLectorExcel().getCabeceros());
        Workbook workbook = extractoMC.generarExtracto();
        // Configura la respuesta HTTP
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename.toUpperCase());
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @GetMapping("comercial/extractoBancario")
    public void downloadFileDebitosAutomaticos(@RequestParam("tipoDebito") String opcionSeleccionada,
            @RequestParam("fechaSeleccionada") String fechaSeleccionada, HttpServletResponse response, Model model) throws IOException {
        var extractoDA = new ExtractoDebitosAutomaticos(opcionSeleccionada,
                fechaSeleccionada, subirArchivo.getLectorExcel().getDatos(), subirArchivo.getLectorExcel().getCabeceros());

        Workbook workbook = extractoDA.generarExtracto();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + opcionSeleccionada + " " + LocalDate.now().toString() + ".xls");
        workbook.write(response.getOutputStream());
        workbook.close();
        model.addAttribute("cabeceros", null);
        model.addAttribute("datos", null);
    }

    @GetMapping("comercial/repuestaDebitosExtracto")
    public void downloadRespuestaDebitosAutomaticos(HttpServletResponse response, Model model) throws IOException {
        var extracto = new EscritorXLS(subirTxt.getFilas(), subirTxt.getCabeceros());

        Workbook workbook = extracto.exportar();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename= Respuesta Debitos - " + LocalDate.now().toString() + ".xls");
        workbook.write(response.getOutputStream());
        workbook.close();
        model.addAttribute("cabeceros", null);
        model.addAttribute("datos", null);
    }

}
