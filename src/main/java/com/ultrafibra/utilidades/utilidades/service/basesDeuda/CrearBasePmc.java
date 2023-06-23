package com.ultrafibra.utilidades.utilidades.service.basesDeuda;

import com.ultrafibra.utilidades.utilidades.service.util.EscritorTxt;
import static java.lang.String.valueOf;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

@Data
@Service
public class CrearBasePmc {

    private List<List<String>> filas = new ArrayList();
    private EscritorTxt escritor;
    private int contadorFilas = 0;
    private double montoTotalPrimerVto = 0;
    private DecimalFormat df;
    private String mensajeTicket;
    private String mensajePantalla;
    private String IdOk = null, fechaPrimerVto = null, fechaSegundoVto = null, montoPrimerVto = null, montoSegundoVto = null,
            numeroFactura = null, dniCliente = null, fechaTercerVto = null;
    private int cont = 1;
    private double montoDbl1erVto = 0, montoDbl2doVto = 0;

    public CrearBasePmc() {
        this.df = new DecimalFormat("#.00");
    }

    public FileSystemResource crearArchivo(List<List<String>> filas, String path) {
        this.filas = filas;
        this.escritor = new EscritorTxt(path);

        String fechaPrimerFila = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now());

        String primerFila = "0400SBHN" + fechaPrimerFila + cerosDerecha("", 264);
        this.escritor.escribir(primerFila, true);
        int contador = 0;
        

        //Filas
        for (int j = 0; j < filas.size(); j++) {
            List<String> fila = filas.get(j);
            this.contadorFilas++;
            //Columnas
            for (int i = 0; i < fila.size(); i++) {
                String celda = fila.get(i);
                switch (i) {
                    // Creo el ID
                    case 0:
                        asignarId(celda, j, i);
                        break;
                    // Tomo la informacion de la columna con el Dni, informacion por medio de la cual el cliente va a buscar su deuda
                    case 1:
                        dniCliente = "5" + asignarEspacios(celda.replace(",00", ""), 19);
                        break;
                    //Establezco los montos del primer vencimiento
                    case 2:
                        String totalAdeudado = fila.get(i + 4);
                        String totalConRecargo = fila.get(i + 1);
                        establecerMontoPrimerVto(celda, totalAdeudado, totalConRecargo);
                        break;
                    // Establezco los montos del segundo vencimiento
                    case 3:
                        if (!celda.equals(",00")) {
                            totalAdeudado = fila.get(i + 3);
                            establecerMontoSegundoVto(celda, totalAdeudado);
                        } else {
                            montoDbl2doVto = montoDbl1erVto;
                            montoSegundoVto = montoPrimerVto; // si es igual a 0 el primer vencimiento no tiene re cargo y es igual al monto del primer vencimiento
                        }
                        break;
                    // Establecemos las fechas de vencimiento               
                    case 4:
                        LocalDate fechaDeFactura = stringToLocalDate(fila.get(7));
                        asignarMes(fechaDeFactura, fila.get(2));
                        LocalDate fechaHoy = LocalDate.now();
                        LocalDate fechaVto = stringToLocalDate(celda);
                        establecerFechas(fechaHoy, fechaVto);
                        break;
                    // Obtengo el numero de factura
                    case 5:
                        numeroFactura = celda.replaceFirst("-", " ");
                        numeroFactura = numeroFactura.substring(3);
                        break;
                }

            }

            this.montoTotalPrimerVto += montoDbl1erVto;

            String filaArchivo = dniCliente + IdOk + "0" + fechaPrimerVto + montoPrimerVto + fechaSegundoVto + montoSegundoVto + fechaTercerVto + montoSegundoVto
                    + "0000000000000000000" + dniCliente.substring(1) + asignarEspacios(mensajeTicket, 40) + asignarEspacios(mensajePantalla, 15)
                    + asignarEspacios(numeroFactura, 60) + cerosDerecha("", 29);
            this.escritor.escribir(filaArchivo, true);
            
//            System.out.println("Una nueva fila cargada " + contador++);
        }

        String ultimaFila = "9400SBHN" + fechaPrimerFila + cerosIzquierda(valueOf(this.contadorFilas), 7) + "0000000"
                + cerosIzquierda(String.valueOf(df.format(montoTotalPrimerVto)).replace(",", ""), 16)
                + cerosDerecha("", 234);
        
        System.out.println(String.valueOf(df.format(montoTotalPrimerVto)));
        System.out.println("ultimaFila = " + ultimaFila);
        
        this.escritor.escribir(ultimaFila, true);

        return new FileSystemResource(escritor.getArchivo());
    }

    private void asignarId(String celda, int filaActual, int columnaActual) {
        IdOk = celda;

        if (filaActual != 0) {
            String idAnterior = filas.get(filaActual - 1).get(0);
            if (idAnterior.equals(celda)) {
                cont++;
                IdOk += "-" + cont;
            } else {
                cont = 1;
                IdOk += "-" + cont;
            }
        } else {
            IdOk += "-" + cont;
        }

        this.IdOk = asignarEspacios(IdOk.replace(".0", ""), 20);
    }

    private String asignarEspacios(String fila, int pos) {
        int x = pos - fila.length();
        for (int i = 1; i <= x; i++) {
            fila += " ";
        }
        return fila;
    }

    private String cerosDerecha(String fila, int pos) {
        int x = pos - fila.length();
        for (int i = 1; i <= x; i++) {
            fila += "0";
        }
        return fila;
    }

    private String cerosIzquierda(String fila, int pos) {
        int x = pos - fila.length();
        String ceros = "";
        for (int i = 1; i <= x; i++) {
            ceros += "0";
        }
        return ceros+fila;
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

    private void asignarMes(LocalDate fecha, String monto) {
        int mes = fecha.getMonthValue();
        int dia = fecha.getDayOfMonth();
        switch (mes) {
            case 1:
                if (monto.equals("3500,00") || monto.equals("4500,00")) {
                    mensajeTicket = "FAC INSTALACION";
                    mensajeTicket = asignarEspacios(mensajeTicket, 40);
                } else {
                    if (dia == 1) {
                        mensajeTicket = "FAC ENERO ULTRAFIBRA";
                        mensajeTicket = asignarEspacios(mensajeTicket, 40);
                    } else {
                        mensajeTicket = "FAC PROPORCIONAL ENERO";
                        mensajeTicket = asignarEspacios(mensajeTicket, 40);
                    }
                }
                mensajePantalla = "FAC ENE";
                mensajePantalla = asignarEspacios(mensajePantalla, 15);
                break;
            case 2:
                if (monto.equals("3500,00") || monto.equals("4500,00")) {
                    mensajeTicket = "FAC INSTALACION";
                    mensajeTicket = asignarEspacios(mensajeTicket, 40);
                } else {
                    if (dia == 1) {
                        mensajeTicket = "FAC FEBRERO ULTRAFIBRA";
                        mensajeTicket = asignarEspacios(mensajeTicket, 40);
                    } else {
                        mensajeTicket = "FAC PROPORCIONAL FEBRERO";
                        mensajeTicket = asignarEspacios(mensajeTicket, 40);
                    }
                }
                mensajePantalla = "FAC FEB";
                mensajePantalla = asignarEspacios(mensajePantalla, 15);
                break;
            case 3:
                if (monto.equals("3500,00") || monto.equals("4500,00")) {
                    mensajeTicket = "FAC INSTALACION";
                    mensajeTicket = asignarEspacios(mensajeTicket, 40);
                } else {
                    if (dia == 1) {
                        mensajeTicket = "FAC MARZO ULTRAFIBRA";
                        mensajeTicket = asignarEspacios(mensajeTicket, 40);
                    } else {
                        mensajeTicket = "FAC PROPORCIONAL MARZO";
                        mensajeTicket = asignarEspacios(mensajeTicket, 40);
                    }
                }
                mensajePantalla = "FAC MAR";
                mensajePantalla = asignarEspacios(mensajePantalla, 15);
                break;
            case 4:
                if (monto.equals("3500,00") || monto.equals("4500,00")) {
                    mensajeTicket = "FAC INSTALACION";
                    mensajeTicket = asignarEspacios(mensajeTicket, 40);
                } else {
                    if (dia == 1) {
                        mensajeTicket = "FAC ABRIL ULTRAFIBRA";
                        mensajeTicket = asignarEspacios(mensajeTicket, 40);
                    } else {
                        mensajeTicket = "FAC PROPORCIONAL ABRIL";
                        mensajeTicket = asignarEspacios(mensajeTicket, 40);
                    }
                }
                mensajePantalla = "FAC ABR";
                mensajePantalla = asignarEspacios(mensajePantalla, 15);
                break;
            case 5:
                if (monto.equals("3500,00") || monto.equals("4500,00")) {
                    mensajeTicket = "FAC INSTALACION";
                    mensajeTicket = asignarEspacios(mensajeTicket, 40);
                } else {
                    if (dia == 1) {
                        mensajeTicket = "FAC MAYO ULTRAFIBRA";
                        mensajeTicket = asignarEspacios(mensajeTicket, 40);
                    } else {
                        mensajeTicket = "FAC PROPORCIONAL MAYO";
                        mensajeTicket = asignarEspacios(mensajeTicket, 40);
                    }
                }
                mensajePantalla = "FAC MAY";
                mensajePantalla = asignarEspacios(mensajePantalla, 15);
                break;
            case 6:
                if (monto.equals("3500,00") || monto.equals("4500,00")) {
                    mensajeTicket = "FAC INSTALACION";
                    mensajeTicket = asignarEspacios(mensajeTicket, 40);
                } else {
                    if (dia == 1) {
                        mensajeTicket = "FAC JUNIO ULTRAFIBRA";
                        mensajeTicket = asignarEspacios(mensajeTicket, 40);
                    } else {
                        mensajeTicket = "FAC PROPORCIONAL JUNIO";
                        mensajeTicket = asignarEspacios(mensajeTicket, 40);
                    }
                }
                mensajePantalla = "FAC JUN";
                mensajePantalla = asignarEspacios(mensajePantalla, 15);
                break;
            case 7:
                if (monto.equals("3500,00") || monto.equals("4500,00")) {
                    mensajeTicket = "FAC INSTALACION";
                    mensajeTicket = asignarEspacios(mensajeTicket, 40);
                } else {
                    if (dia == 1) {
                        mensajeTicket = "FAC JULIO ULTRAFIBRA";
                        mensajeTicket = asignarEspacios(mensajeTicket, 40);
                    } else {
                        mensajeTicket = "FAC PROPORCIONAL JULIO";
                        mensajeTicket = asignarEspacios(mensajeTicket, 40);
                    }
                }
                mensajePantalla = "FAC JUL";
                mensajePantalla = asignarEspacios(mensajePantalla, 15);
                break;
            case 8:
                if (monto.equals("3500,00") || monto.equals("4500,00")) {
                    mensajeTicket = "FAC INSTALACION";
                    mensajeTicket = asignarEspacios(mensajeTicket, 40);
                } else {
                    if (dia == 1) {
                        mensajeTicket = "FAC AGOSTO ULTRAFIBRA";
                        mensajeTicket = asignarEspacios(mensajeTicket, 40);
                    } else {
                        mensajeTicket = "FAC PROPORCIONAL AGOSTO";
                        mensajeTicket = asignarEspacios(mensajeTicket, 40);
                    }
                }
                mensajePantalla = "FAC AGO";
                mensajePantalla = asignarEspacios(mensajePantalla, 15);
                break;
            case 9:
                if (monto.equals("3500,00") || monto.equals("4500,00")) {
                    mensajeTicket = "FAC INSTALACION";
                    mensajeTicket = asignarEspacios(mensajeTicket, 40);
                } else {
                    if (dia == 1) {
                        mensajeTicket = "FAC SEPTIEMBRE ULTRAFIBRA";
                        mensajeTicket = asignarEspacios(mensajeTicket, 40);
                    } else {
                        mensajeTicket = "FAC PROPORCIONAL SEPTIEMBRE";
                        mensajeTicket = asignarEspacios(mensajeTicket, 40);
                    }
                }
                mensajePantalla = "FAC SEP";
                mensajePantalla = asignarEspacios(mensajePantalla, 15);
                break;
            case 10:
                if (monto.equals("3500,00") || monto.equals("4500,00")) {
                    mensajeTicket = "FAC INSTALACION";
                    mensajeTicket = asignarEspacios(mensajeTicket, 40);
                } else {
                    if (dia == 1) {
                        mensajeTicket = "FAC OCTUBRE ULTRAFIBRA";
                        mensajeTicket = asignarEspacios(mensajeTicket, 40);
                    } else {
                        mensajeTicket = "FAC PROPORCIONAL OCTUBRE";
                        mensajeTicket = asignarEspacios(mensajeTicket, 40);
                    }
                }
                mensajePantalla = "FAC OCT";
                mensajePantalla = asignarEspacios(mensajePantalla, 15);
                break;
            case 11:
                if (monto.equals("3500,00") || monto.equals("4500,00")) {
                    mensajeTicket = "FAC INSTALACION";
                    mensajeTicket = asignarEspacios(mensajeTicket, 40);
                } else {
                    if (dia == 1) {
                        mensajeTicket = "FAC NOVIEMBRE ULTRAFIBRA";
                        mensajeTicket = asignarEspacios(mensajeTicket, 40);
                    } else {
                        mensajeTicket = "FAC PROPORCIONAL NOVIEMBRE";
                        mensajeTicket = asignarEspacios(mensajeTicket, 40);
                    }
                }
                mensajePantalla = "FAC NOV";
                mensajePantalla = asignarEspacios(mensajePantalla, 15);
                break;
            case 12:
                if (monto.equals("3500,00") || monto.equals("4500,00")) {
                    mensajeTicket = "FAC INSTALACION";
                    mensajeTicket = asignarEspacios(mensajeTicket, 40);
                } else {
                    if (dia == 1) {
                        mensajeTicket = "FAC DICIEMBRE ULTRAFIBRA";
                        mensajeTicket = asignarEspacios(mensajeTicket, 40);
                    } else {
                        mensajeTicket = "FAC PROPORCIONAL DICIEMBRE";
                        mensajeTicket = asignarEspacios(mensajeTicket, 40);
                    }
                }
                mensajePantalla = "FAC DIC";
                mensajePantalla = asignarEspacios(mensajePantalla, 15);
                break;
        }

    }

    private void establecerMontoPrimerVto(String celda, String totalAdeudado, String totalConRecargo) {
        totalAdeudado = totalAdeudado.replace(",", ".");
        totalConRecargo = totalConRecargo.replace(",", ".");
        String totalSinRecargo = celda.replace(",", ".");

        if (totalSinRecargo.equals(totalAdeudado)) {

            montoDbl1erVto = Double.parseDouble(totalSinRecargo);
            montoPrimerVto = totalSinRecargo.replace(".", ""); // si se debe el total de la factura y el adeudado es igual 
            montoPrimerVto = cerosIzquierda(montoPrimerVto, 11);

        } else {

            if (!totalAdeudado.equals(totalConRecargo)) {
                montoDbl1erVto = Double.parseDouble(totalAdeudado);

                montoPrimerVto = totalAdeudado;
                montoPrimerVto = montoPrimerVto.replace(".", ""); // Si la factura esta abierta pero no se debe completa
                montoPrimerVto = cerosIzquierda(montoPrimerVto, 11);

            } else {
                montoDbl1erVto = Double.parseDouble(totalSinRecargo);
                montoPrimerVto = totalSinRecargo.replace(".", ""); // si se debe el total de la factura y el adeudado es igual 
                montoPrimerVto = cerosIzquierda(montoPrimerVto, 11);
            }

        }
    }

    private void establecerMontoSegundoVto(String celda, String totalAdeudado) {
        totalAdeudado = totalAdeudado.replace(",", ".");
        celda = celda.replace(",", ".");

        if (celda.equals(totalAdeudado)) {
//            celda = celda.replace(",", ".");
            montoDbl2doVto = Double.parseDouble(celda);

            montoSegundoVto = celda.replace(".", ""); // Si es distinto de 0 el primer vencimiento tiene recargo
            montoSegundoVto = cerosIzquierda(montoSegundoVto, 11);
        } else {

            montoSegundoVto = celda;
            montoDbl2doVto = Double.parseDouble(celda);
            montoSegundoVto = montoSegundoVto.replace(".", ""); // Si la factura esta abierta pero no se debe completa
            montoSegundoVto = cerosIzquierda(montoSegundoVto, 11);

        }
    }

    private void establecerFechas(LocalDate fechaHoy, LocalDate fechaVto) {
        if (fechaVto.isBefore(fechaHoy)) {
            montoPrimerVto = montoSegundoVto;
            montoDbl1erVto = montoDbl2doVto;
            fechaPrimerVto = fechaHoy.plusDays(2).toString();
            fechaSegundoVto = fechaHoy.plusDays(5).toString();
            fechaTercerVto = fechaHoy.plusDays(25).toString();
        } else {
            fechaPrimerVto = fechaVto.toString();
            fechaSegundoVto = fechaVto.plusDays(10).toString();
            fechaTercerVto = fechaVto.plusDays(30).toString();
        }

        fechaPrimerVto = fechaPrimerVto.replace("-", "");
        fechaSegundoVto = fechaSegundoVto.replace("-", "");
        fechaTercerVto = fechaTercerVto.replace("-", "");
    }
}
