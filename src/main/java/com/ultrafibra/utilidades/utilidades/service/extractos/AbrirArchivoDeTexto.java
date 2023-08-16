package com.ultrafibra.utilidades.utilidades.service.extractos;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class AbrirArchivoDeTexto {

    private List<List<String>> filas = new ArrayList();
    private List<String> cabeceros = new ArrayList();
    
    private String ret;

    private SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd");

    public AbrirArchivoDeTexto() {

    }

    public List<List<String>> leerArchivo(InputStream ins) throws FileNotFoundException {
        
        filas.clear();
        cabeceros.clear();

        this.cabeceros.add("Id");
        this.cabeceros.add("Referencia");
        this.cabeceros.add("Fecha de Pago");
        this.cabeceros.add("Etiqueta");
        this.cabeceros.add("Monto");
        this.cabeceros.add("Motivo");

        String fechaSalida = null;
        Scanner scanner = new Scanner(ins);

        while (scanner.hasNextLine()) {

            String filaStr = scanner.nextLine();
            List<String> fila = new ArrayList();

            String id = filaStr.substring(44, 63);
            String fechaEntrada = filaStr.substring(87, 95);
            String monto = filaStr.substring(97, 111);
            String motivo = filaStr.substring(83, 86);

            switch (motivo) {
                case "R02":
                    motivo = "CUENTA CERRADA/SUSPENDIDA";
                    break;
                case "R03":
                    motivo = "CUENTA INEXISTENTE";
                    break;
                case "R08":
                    motivo = "ORDEN DE NO PAGAR";
                    break;
                case "R10":
                    motivo = "FALTA DE FONDOS";
                    break;
                case "R15":
                    motivo = "BAJA DE SERVICIO";
                    break;
                case "R17":
                    motivo = "ERROR DE FORMATO";
                    break;
                case "R20":
                    motivo = "MONEDA DISTINTA A LA CUENTA DE DÉBITO";
                    break;
                default:
                    motivo = "APLICADO";
                    break;
            }

            try {
                Date fecha = formatoEntrada.parse(fechaEntrada);
                fechaSalida = formatoSalida.format(fecha);

            } catch (ParseException e) {
                System.out.println("Error al convertir la fecha.");
                e.printStackTrace();
            }

            double valorNumerico = Double.parseDouble(monto) / 100.0;
            DecimalFormat formatoMoneda = new DecimalFormat("#,##0.00");
            String montoFormateado = formatoMoneda.format(valorNumerico);

            fila.add(id);
            fila.add(id);
            fila.add(fechaSalida);
            fila.add("Debito Automatico");
            fila.add(montoFormateado);
            fila.add(motivo);

            this.filas.add(fila);
        }

        scanner.close();
        return filas;
    }

    public String getUri(String uri) {
        if(ret == null){
            this.ret = uri.substring(uri.indexOf("comercial"));
        }         
        return ret;
    }

}
