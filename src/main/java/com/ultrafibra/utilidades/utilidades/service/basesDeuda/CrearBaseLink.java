package com.ultrafibra.utilidades.utilidades.service.basesDeuda;

import com.ultrafibra.utilidades.utilidades.service.util.EscritorTxt;
import static java.lang.String.valueOf;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

@Data
@Service
public class CrearBaseLink {

    private List<List<String>> filas = new ArrayList();
    private double montoTotalPrimerVto = 0;
    private double montoTotalSegundoVto = 0;
    private LocalDate ultFecVto;
    private String idOk = null, documentoOrigen = null, concepto = null, conceptoAnterior = null, idDeuda = null, fechaPrimerVto = null,
            fechaSegundoVto = null, ultimaFecha = null, montoPrimerVto = null, montoSegundoVto = null, numeroFactura = null;
    private EscritorTxt escritor;
    private EscritorTxt escritorControl;
    private DecimalFormat df;

    @Autowired
    public CrearBaseLink() {       
        this.df = new DecimalFormat("#.00");
    }

    //Metodos
    public FileSystemResource crearArchivo(List<List<String>> filas, String path) {
        this.filas = filas;
        this.escritor = new EscritorTxt(path + ".txt");

        int cont = 1;

        String fechaStr = DateTimeFormatter.ofPattern("MMyy").format(LocalDateTime.now());
        String fechaPrimerFila = DateTimeFormatter.ofPattern("yyMMdd").format(LocalDateTime.now());

        String fechaRespaldo = filas.get(0).get(3);
        ultFecVto = stringToLocalDate(fechaRespaldo);

        String primerFila = asignarEspacios("HRFACTURACIONGK0" + fechaPrimerFila + "00001", 130);

        this.escritor.escribir(primerFila, true);

        //recorremos todas las filas del archivo
        for (int j = 0; j < filas.size(); j++) {
            List<String> fila = filas.get(j);

            // Recorremos las celdas de la fila de izquierda a derecha
            for (int i = 0; i < fila.size(); i++) {

                String celda = fila.get(i);

                //ID  del cliente
                if (i == 0) {
                    celda = celda.replace(",0", "");
                    celda = celda.replace(".0", "");
                    idOk = cerosIzquierda(celda, 8);
                }

                // Tomo la informacion de la columna con el documento de origen
                if (i == 1) {
                    asignarConcepto(celda, fila.get(4));
                }

                // Se comprueba si el cliente tiene varias deudas del mismo concepto para eso se compara el id
                // y los conceptos que tiene anteriormente
                if (i == 2) {
                    String idAnterior = null;
                    String IdActual = null;
                    if (j != 0) {
                        IdActual = fila.get(0);
                        idAnterior = filas.get(i - 1).get(0);

                        if (idAnterior.equals(IdActual) && concepto.equals(conceptoAnterior)) {
                            idDeuda = cont + fechaStr;
                            cont++;
                        } else {
                            idDeuda = "0" + fechaStr;
                            cont = 1;
                        }
                    } else {
                        idDeuda = "0" + fechaStr;
                    }

                }

                // Establezco las fechas de vencimiento
                if (i == 3) {
                    establecerFechas(celda);
                }

                //Establezco los montos del primer vencimiento
                if (i == 4) {

                    String totalAdeudado = fila.get(7);
                    totalAdeudado = totalAdeudado.replace(",", ".");

                    String totalConRecargo = fila.get(5);
                    totalConRecargo = totalConRecargo.replace(",", ".");

                    String totalSinRecargo = celda.replace(",", ".");

                    establecerMontos(totalAdeudado, totalConRecargo, totalSinRecargo);

                }

                // Establezco los montos del segundo vencimiento
                if (i == 5) {
                    String monto = fila.get(7);
                    establecerMontos(celda, monto);
                }

                // Obtengo el numero de factura
                if (i == 6) {
                    numeroFactura = celda.replace("-", " ");
                    numeroFactura = numeroFactura.substring(3);
                }
            }

            String filaTxt = idDeuda + concepto + idOk + "           "
                    + fechaPrimerVto + montoPrimerVto + fechaSegundoVto + montoSegundoVto + "000000000000000000"
                    + numeroFactura + " " + documentoOrigen;

            filaTxt = asignarEspacios(filaTxt, 130);

            this.escritor.escribir(filaTxt, true);
            conceptoAnterior = concepto;
        }

        String ultimaFila = "TRFACTURACION" + cerosIzquierda(String.valueOf(filas.size() + 2), 8)
                + cerosIzquierda(String.valueOf(df.format(montoTotalPrimerVto)).replace(",", ""), 18)
                + cerosIzquierda(String.valueOf(df.format(montoTotalSegundoVto)).replace(",", ""), 18) + "000000000000000000";

        ultimaFila = asignarEspacios(ultimaFila, 130);

        this.escritor.escribir(ultimaFila, true);

        return new FileSystemResource(escritor.getArchivo());
    }

    private String asignarEspacios(String fila, int pos) {
        int x = pos - fila.length();
        for (int i = 0; i <= x; i++) {
            fila += " ";
        }
        return fila;
    }

    private String cerosIzquierda(String fila, int pos) {
        String filaRet = null;
        char[] arrayFila = fila.toCharArray();
        char[] filaArray = new char[pos];

        for (int i = 0; i < filaArray.length; i++) {
            filaArray[i] = '0';
        }

        int y = pos - arrayFila.length;

        for (char c : arrayFila) {
            filaArray[y] = c;
            y++;
        }
        filaRet = valueOf(filaArray);
        return filaRet;
    }

    private void asignarConcepto(String documentoOrigen, String monto) {
        this.documentoOrigen = documentoOrigen;

        if (documentoOrigen.contains("SUB")) {
            concepto = "001"; // Facturas de abono

        } else if (documentoOrigen.contains("SO") && monto.equals("3500,00") || monto.equals("4500,00")) {
            concepto = "003"; // Facturas Instalacion

        } else if (documentoOrigen.contains("SO")) {
            concepto = "002"; // Facturas de proporcional

        } else {
            concepto = "004"; // Otros conceptos

        }
    }

    private LocalDate stringToLocalDate(String fecha) {
        int intDia, intAño;
        int intMes = 1;

        fecha = fecha.replace(".", "");
        String dia = fecha.substring(0, 2);
        String mes = fecha.substring(3, 6);
        String año = fecha.substring(7);

        intDia = Integer.parseInt(dia);
        intAño = Integer.parseInt(año);

        switch (mes) {
            case "ene":
                intMes = 1;
                break;
            case "feb":
                intMes = 2;
                break;
            case "mar":
                intMes = 3;
                break;
            case "abr":
                intMes = 4;
                break;
            case "may":
                intMes = 5;
                break;
            case "jun":
                intMes = 6;
                break;
            case "jul":
                intMes = 7;
                break;
            case "ago":
                intMes = 8;
                break;
            case "sep":
                intMes = 9;
                break;
            case "oct":
                intMes = 10;
                break;
            case "nov":
                intMes = 11;
                break;
            case "dic":
                intMes = 12;
                break;
        }
        return LocalDate.of(intAño, intMes, intDia);
    }

    private void establecerFechas(String celda) {
        LocalDate fechaHoy = LocalDate.now();
        LocalDate fechaVto = stringToLocalDate(celda);

        if (fechaVto.isBefore(fechaHoy)) {
            fechaPrimerVto = DateTimeFormatter.ofPattern("yyMMdd").format(fechaHoy.plusDays(2));
            fechaSegundoVto = DateTimeFormatter.ofPattern("yyMMdd").format(fechaHoy.plusDays(5));
        } else {
            fechaPrimerVto = DateTimeFormatter.ofPattern("yyMMdd").format(fechaVto);
            fechaSegundoVto = DateTimeFormatter.ofPattern("yyMMdd").format(fechaVto.plusDays(10));
        }

        if (fechaVto.plusDays(15).isAfter(ultFecVto)) {
            ultFecVto = fechaVto.plusDays(15);
        }

        ultimaFecha = DateTimeFormatter.ofPattern("yyyyMMdd").format(ultFecVto);
    }

    private void establecerMontos(String totalAdeudado, String totalConRecargo, String totalSinRecargo) {
        if (totalSinRecargo.equals(totalAdeudado)) {

            this.montoTotalPrimerVto += Double.parseDouble(totalSinRecargo);
            montoPrimerVto = totalSinRecargo.replace(".", ""); // si se debe el total de la factura y el adeudado es igual 
            montoPrimerVto = cerosIzquierda(montoPrimerVto, 12);

        } else {

            if (!totalAdeudado.equals(totalConRecargo)) {
                this.montoTotalPrimerVto += Double.parseDouble(totalAdeudado);

                montoPrimerVto = totalAdeudado;
                montoPrimerVto = montoPrimerVto.replace(".", ""); // Si la factura esta abierta pero no se debe completa
                montoPrimerVto = cerosIzquierda(montoPrimerVto, 12);

            } else {
                this.montoTotalPrimerVto += Double.parseDouble(totalSinRecargo);
                montoPrimerVto = totalSinRecargo.replace(".", ""); // si se debe el total de la factura y el adeudado es igual 
                montoPrimerVto = cerosIzquierda(montoPrimerVto, 12);
            }

        }
    }

    private void establecerMontos(String celda, String monto) {
        
        if (!celda.equals("0,00") && !celda.equals("0.0") && !celda.equals(",00")) {
            monto = monto.replace(",", ".");
            celda = celda.replace(",", ".");

            if (celda.equals(monto)) {
                celda = celda.replace(",", ".");
                this.montoTotalSegundoVto += Double.parseDouble(celda);

                montoSegundoVto = celda.replace(".", ""); // Si es distinto de 0 el primer vencimiento tiene recargo                            
                montoSegundoVto = cerosIzquierda(montoSegundoVto, 12);
            } else {
                this.montoTotalSegundoVto += Double.parseDouble(celda);
                montoSegundoVto = celda;
                montoSegundoVto = montoSegundoVto.replace(".", ""); // Si la factura esta abierta pero no se debe completa
                montoSegundoVto = cerosIzquierda(montoSegundoVto, 12);
            }

        } else {
            monto = monto.replace(",", ".");
            this.montoTotalSegundoVto += Double.parseDouble(monto);
            montoSegundoVto = montoPrimerVto; // si es igual a 0 el primer vencimiento no tiene re cargo y es igual al monto del primer vencimiento
        }

    }

    public FileSystemResource archivoDeControl(List<List<String>> filas,String filename) {
        
        String nombreArchivo = filename.replace("P", "C");
        this.escritorControl = new EscritorTxt(nombreArchivo);

        String fechaStr = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now());

        String registros = valueOf((filas.size() + 2) * 133);
        registros = cerosIzquierda(registros, 10);

        String fila1 = "HRPASCTRL" + fechaStr + "GK0" + filename + registros;
        fila1 = asignarEspacios(fila1, 74);

        String fila2 = "LOTES" + "00001" + cerosIzquierda(valueOf(filas.size()), 8)
                + cerosIzquierda(String.valueOf(df.format(montoTotalPrimerVto)).replace(",", ""), 18)
                + cerosIzquierda(String.valueOf(df.format(montoTotalSegundoVto)).replace(",", ""), 18) + "000000000000000000";

        fila2 = asignarEspacios(fila2, 74);

        String fila3 = "FINAL" + cerosIzquierda(valueOf(filas.size()), 8) + cerosIzquierda(String.valueOf(df.format(montoTotalPrimerVto)).replace(",", ""), 18)
                + cerosIzquierda(String.valueOf(df.format(montoTotalSegundoVto)).replace(",", ""), 18) + "000000000000000000" + ultimaFecha;

        this.escritorControl.escribir(fila1, true);
        this.escritorControl.escribir(fila2, true);
        this.escritorControl.escribir(fila3, true);

        return new FileSystemResource(escritorControl.getArchivo());
    }

}
