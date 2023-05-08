package com.ultrafibra.utilidades.utilidades.service.extractos;

import com.ultrafibra.utilidades.utilidades.service.util.EscritorXLS;
import static java.lang.String.valueOf;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;

public class ExtractoMacroClicks extends EscritorXLS {

    private int contador;

    public ExtractoMacroClicks(List<List<String>> filas, List<String> cabeceros) {
        super(filas,cabeceros);
    }

    public Workbook generarExtracto() {
        cabeceros.add("id");
        for (int j = 0; j < filas.size(); j++) {
            List<String> fila = filas.get(j);
            for (int i = 0; i < fila.size(); i++) {
                String valorCelda = fila.get(i);
                if (i == 1) {
                    fila.set(i, getFecha(valorCelda));
                }
                if (i == 2) {
                    fila = getClaves(valorCelda, fila);
                    
                    if(this.contador<contadorNewHeaders(valorCelda)){
                        this.contador = contadorNewHeaders(valorCelda);
                    }
                }
            }
            this.filas.set(j, fila);
        }
        for(int i=0; i<this.contador;i++){
            this.cabeceros.add("Referencia "+i);
        }
        return exportar();
    }    

    private List<String> getClaves(String valorCelda, List<String> fila) {

        char[] charArray = valorCelda.toCharArray();
        String ref = "";

        // Comienza busqueda de ID
        String Id = "";
        int index = valorCelda.indexOf("Clave2");

        if (valorCelda != null) {

            boolean flag = true;
            int cont = index + 9;

            while (flag) {
                char caracter = charArray[cont];

                if (caracter != ' ' && caracter != '-') {
                    Id = Id + String.valueOf(caracter);
                    cont++;
                } else {
                    flag = false;
                }
            }
            fila.add(Id);

            while (valorCelda.indexOf("Clave3") > -1) {
                ref = "";
                index = valorCelda.indexOf("Clave3");

                cont = index + 9;

                for (int y = cont; y < cont + 15; y++) {
                    char caracter = charArray[y];
                    ref = ref + String.valueOf(caracter);
                }
                if(!ref.contains(",")){
                   fila.add("FA-" + ref); 
                }               
                valorCelda = valorCelda.substring(valorCelda.indexOf("Clave3") + "Clave3".length(), valorCelda.length());
                charArray = valorCelda.toCharArray();
            }

        }

        fila.set(2, "MacroClick");
        return fila;
    }

    private int contadorNewHeaders(String sTexto) {
        int contador = 0;
        String sTextoBuscado = "Clave3";
        while (sTexto.indexOf(sTextoBuscado) > -1) {
            sTexto = sTexto.substring(sTexto.indexOf(
                    sTextoBuscado) + sTextoBuscado.length(), sTexto.length());
            contador++;
        }
        return contador;
    }

    private String getFecha(String valorCelda) {
        String fechaCompleta = valorCelda.substring(0, 10);
        String dia = fechaCompleta.substring(0, 3);
        String mes = fechaCompleta.substring(3, 6);
        String año = fechaCompleta.substring(6, 10);

        char car = dia.charAt(0);
        String comp = valueOf(car);

        if (comp.equals("0")) {
            dia = dia.replaceFirst("0", "");
        }

        car = mes.charAt(0);
        comp = valueOf(car);
        if (comp.equals("0")) {
            mes = mes.replaceFirst("0", "");

        }
        String fecha = dia + mes + año;
        return fecha;
    }
}
