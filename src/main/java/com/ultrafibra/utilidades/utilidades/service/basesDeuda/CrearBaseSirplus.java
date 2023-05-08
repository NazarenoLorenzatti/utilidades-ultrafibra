package com.ultrafibra.utilidades.utilidades.service.basesDeuda;

import com.ultrafibra.utilidades.domain.Cupon;
import com.ultrafibra.utilidades.utilidades.service.util.EscritorXML;
import static java.lang.String.valueOf;
import java.text.DecimalFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

@Data
@Service
public class CrearBaseSirplus {
    
    private DecimalFormat df;
    
    
    public CrearBaseSirplus() {
        this.df = new DecimalFormat("#.00");
    }

    public ByteArrayResource leerTabla(List<List<String>> filas, String filename) {
        String codigoBarras;
        List<Cupon> cupones = new ArrayList();

        //Filas
        for (int j = 0; j < filas.size(); j++) {
            
            List<String> fila = filas.get(j);
            
            Cupon cupon = new Cupon();
            LocalDate fecha = LocalDate.now();
            int mes = fecha.getMonthValue();
            int anio = fecha.getYear();

            cupon.setFecha(fecha);
            cupon.setAnio(anio);
            cupon.setPeriodo(mes);
            cupon.setId_moneda_sirplus("1");
            cupon.setId_moneda_vencimiento_sirplus("1");

            // Columnas            
            for (int i = 0; i < fila.size(); i++) {
                 String valorCelda = fila.get(i);

                if (i == 0) {
                    valorCelda = valorCelda.substring(5).replace("-", "");
                    cupon.setNumero_cupon(valorCelda);
                }

                if (i == 1) {
                    cupon.setNumero_cliente(valorCelda);
                }
                if (i == 2) {
                    cupon.setRazon_social_cliente(valorCelda);
                }
                if (i == 3) {
                    cupon.setMail(valorCelda);
                }
                if (i == 4) {
                    if (valorCelda.equals("false") || valorCelda.equals("FALSE")) {
                        cupon.setNumero_cuenta(null);
                    } else {
                        cupon.setNumero_cuenta(valorCelda);
                    }
                }
                if (i == 5) {
                    cupon.setCbu(valorCelda);
                }
                if (i == 6) {
                    cupon.setTitular_cuenta(valorCelda);
                }
                if (i == 7) {
                    valorCelda = valorCelda.replace(",", ".");
                    cupon.setTotal(Double.parseDouble(valorCelda));
                    cupon.setValor_pesos(Double.parseDouble(valorCelda));
                    cupon.setImporte(Double.parseDouble(valorCelda));
                    cupon.setValor_pesos_vencimiento(Double.parseDouble(valorCelda));
                }
                if (i == 8) {
                    LocalDate feriados[] = cupon.getFeriados();
                    LocalDate fPrimerVto = stringToLocalDate(valorCelda);
                    LocalDate fSegundoVto = fPrimerVto.plusDays(15);
                    LocalDate fTercerVto = fSegundoVto.plusDays(30);

                    for (int y = 0; y < feriados.length; y++) {

                        if (fPrimerVto.equals(feriados[y]) || fPrimerVto.getDayOfWeek().toString().equals("SUNDAY") || fPrimerVto.getDayOfWeek().toString().equals("SATURDAY")) {
                            fPrimerVto = fPrimerVto.plusDays(1);
                        }

                        if (fSegundoVto.equals(feriados[y]) || fSegundoVto.getDayOfWeek().toString().equals("SUNDAY") || fSegundoVto.getDayOfWeek().toString().equals("SATURDAY")) {
                            fSegundoVto = fSegundoVto.plusDays(1);
                        }

                        if (fTercerVto.equals(feriados[y]) || fTercerVto.getDayOfWeek().toString().equals("SUNDAY") || fTercerVto.getDayOfWeek().toString().equals("SATURDAY")) {
                            fTercerVto = fTercerVto.plusDays(1);
                        }

                    }
                    cupon.setFecha_vencimiento(fPrimerVto);
                    cupon.setFecha_segundoVencimiento(fSegundoVto);
                    cupon.setFecha_tercerVencimiento(fTercerVto);
                }
                if (i == 9) {
                    cupon.setDocumentoDeOrigen(valorCelda);
                }
                if (i == 10) {
                    if(valorCelda.equals("0,00")) {
                        cupon.setDifVencimientos(0);
                    } else {
                        cupon.setDifVencimientos(200);                        
                    }
                }

            }           
                        
            Long dif = DAYS.between(cupon.getFecha_vencimiento(), cupon.getFecha_segundoVencimiento());
            Long dif2 = DAYS.between(cupon.getFecha_vencimiento(), cupon.getFecha_tercerVencimiento());
            
            codigoBarras = "00405000330" + cupon.getNumero_cupon() + DateTimeFormatter.ofPattern("yyMMdd").format(cupon.getFecha_vencimiento())
                    + cerosIzquierda(df.format(cupon.getValor_pesos_vencimiento()).replace(",", ""), 10)
                    + dif + cerosIzquierda(df.format(cupon.getDifVencimientos()).replace(",", ""), 8) 
                    + dif2 + cerosIzquierda(df.format(cupon.getDifVencimientos()).replace(",", ""), 8);
            
            codigoBarras = codigoVerificador(codigoBarras);

            cupon.setCodigo_barras(codigoBarras);
            cupones.add(cupon);
        }
        
        EscritorXML eXML = new EscritorXML(filename.toUpperCase(), cupones);
        
        return new ByteArrayResource(eXML.crearXML());
    }
    
    private String codigoVerificador(String codigoDeBarras){        
        char [] arrayCB = codigoDeBarras.toCharArray();
        int [] arraySC = new int [arrayCB.length];
        int suma = 0;
        int secuencia = 3;
        for (int i = 0; i < arraySC.length ; i++ ){
            if(i>0){
               arraySC[i] = secuencia;               
               if(secuencia==9){
                   secuencia = 3;
               } else {
                   secuencia+=2;
               }
               
            } else {
                arraySC[i] = 1;
            }
        }
        
        for (int i = 0; i < arraySC.length;i++){
            suma += arraySC[i] * Integer.parseInt(String.valueOf(arrayCB[i]));
        }
        suma = suma/2;
        int digito = suma % 10;
        
        return codigoDeBarras+digito;
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

}
