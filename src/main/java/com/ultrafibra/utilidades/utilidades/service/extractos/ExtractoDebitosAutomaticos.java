package com.ultrafibra.utilidades.utilidades.service.extractos;

import com.ultrafibra.utilidades.utilidades.service.util.EscritorXLS;
import static java.lang.String.valueOf;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;

public class ExtractoDebitosAutomaticos extends EscritorXLS {

    private final int flag;
    private final String fecha;

    public ExtractoDebitosAutomaticos(String tipoDebito, String fecha, List<List<String>> filas, List<String> cabeceros) {
        super(filas, cabeceros);
        if (tipoDebito.equals("Debitos Abiertos")) {
            this.flag = 1;
        } else {
            this.flag = 2;
        }
        this.fecha = fecha;
    }

    public Workbook generarExtracto() {

        for (int i = 0; i < filas.size(); i++) {
            List<String> fila = filas.get(i);
            fila.add(0, null);
            fila.add(1, null);
            fila.add(2, null);
            fila.add(3, null);
            fila.add(7, null);
            fila.add(8, null);
            for (int j = 0; j < fila.size(); j++) {
                String valorCelda = fila.get(j);

                if (j == 0) {
                    fila.set(0, "00000");
                }

                // Al tipo de Banco de debitos cerrados se le coloca a todos el fijo 285 ya que tambien admite Cuentas corrientes.
                if (j == 1 && this.flag == 2) {
                    fila.set(j, "285");
                }

                // La Columna Sucursal de los debitos abiertos siempre se completa con 0100
                if (j == 2 && this.flag == 1) {
                    fila.set(j, "0100");
                }

                if (j == 3) {
                    // si son debitos abiertos
                    if (this.flag == 1) {
                        fila.set(j, "0");
                        // Si son debitos cerrados
                    } else if (this.flag == 2) {
                        String tipoCuenta = fila.get(j + 1).substring(0, 0);
                        switch (tipoCuenta) {
                            case "3" ->
                                fila.set(j, tipoCuenta);
                            case "4" ->
                                fila.set(j, tipoCuenta);
                            default ->
                                fila.set(j, "0");
                        }
                    }
                }

                // Guarda el tipo de banco el cual son los primeros 3 numeros del CBU en debitos abiertos                
                if (j == 4) {
                    if (this.flag == 1) {
                        fila.set(1, valorCelda.substring(0, 3));
                    } else if (this.flag == 2) { // Si son deditos cerrados saca el numero de sucursal del CBU
                        fila.set(2, valorCelda.substring(4, 7));
                    }
                }

                if (j == 5 && this.flag == 1) {
                    fila.set(j, valorCelda.replace(",00",""));
                    String banco = fila.get(j - 1).substring(0, 3);

                    if (banco.equals("072")) { // BANCO SANTANDER
                        fila.set(j, cerosIzquierda(valorCelda.replace(",00",""), 5));
                    }

                    if (banco.equals("007")) { // BANCO GALICIA
                        fila.set(j, cerosIzquierda(valorCelda.replace(",00",""), 6));
                    }
                }

                if (j == 7) {
                    fila.set(j, this.fecha.replaceAll("-", ""));
                }

                if (j == 8) {
                    fila.set(j, "080");
                }

                // Normaliza el importe segun el formato pedido por el banco, teniendo en cuenta los decimale
                if (j == 9) {
                    fila.set(j, valorCelda.replace(",", ""));
                }
            }
            this.filas.set(i, fila);
        }
        juntarLineas(this.filas);
        
        return exportar();

    }

    private void juntarLineas(List<List<String>> filas) {
        String id = filas.get(0).get(5);
        boolean primeraFila = true;
        int iAux = 0;

        Iterator<List<String>> it = filas.iterator();

        for (int i = 0; i < filas.size(); i++) {
            String idSig = null;
            if (i < filas.size() - 1) {

                idSig = filas.get(i + 1).get(5);

                if (id.equals(idSig)) {
                    if (primeraFila) {
                        iAux = i;
                        primeraFila = false;
                    }
                    String nroFactura = filas.get(i + 1).get(10);
                    filas.get(iAux).add(nroFactura);

                } else {
                    primeraFila = true;
                }
                id = idSig;

            }
        }
        this.filas = eliminarRepetidos(filas);

        this.cabeceros.add(0, "N°empresa sueldo");
        this.cabeceros.add(1, "Tipo_Banco");
        this.cabeceros.add(2, "Sucursal");
        this.cabeceros.add(3, "Tipo_cuenta");
        this.cabeceros.add(7, "Fecha_vto");
        this.cabeceros.add(8, "Importe");

        int cantRef = 0;
        for (int i = 0; i < filas.size(); i++) {
            if (filas.get(i).size() > cantRef) {
                cantRef = filas.get(i).size();
            }
        }
        
        for (int i = 1; i<= cantRef-11; i++){
            this.cabeceros.add("REF "+i);
        }
    }

    private List<List<String>> eliminarRepetidos(List<List<String>> filas) {
        String id = null;
        String idAnt = null;

        for (int y = 0; y < filas.size(); y++) {
            id = filas.get(y).get(5);
            if (id.equals(idAnt)) {
                filas.remove(y);
                y--;
            }
            idAnt = id;
        }
        return filas;
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

}
