package com.ultrafibra.utilidades.utilidades.service.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Row;

/*
@author Lorenzatti Nazareno
@version 1.0
nl.loragro@gmail.com
 */
// Clase para abrir tabla excel
public class LeectorExcel {

    private String path;
    private FileInputStream inputStream;
    private Workbook wb;
    private List<String> cabeceros;
    private DecimalFormat df;

    private List<List<String>> datos = new ArrayList<>();

    public LeectorExcel(String path) {

        try {
            this.path = path;
            this.inputStream = new FileInputStream(new File(path));
            this.wb = WorkbookFactory.create(new File(path));
//            this.wb = WorkbookFactory.create(inputStream);
            this.df = new DecimalFormat("#.00");
            obtenerCeldas();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(LeectorExcel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "El fichero no funciona");
        } catch (IOException ex) {
            Logger.getLogger(LeectorExcel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "No se puede crear el fichero");
        } catch (EncryptedDocumentException ex) {
            Logger.getLogger(LeectorExcel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Documento encriptado");
        }

    }

    private void obtenerCeldas() throws IOException {
        cabeceros = new ArrayList();
        for (Row filas : wb.getSheetAt(0)) {
            // Crear una lista para almacenar los datos de la fila actual
            List<String> fila = new ArrayList<>();

            // Recorrer todas las celdas de la fila actual
            for (Cell cell : filas) {
                if (filas.getRowNum() == 0) {
                    cabeceros.add(cell.toString());

                } else {
                    
                    // Obtener el valor de la celda y añadirlo a la lista de datos de la fila
                    if (cell.getColumnIndex()!=0 && cell.getCellType().equals(CellType.NUMERIC) && !cell.toString().contains("-")) {
                        String cellNum = df.format(cell.getNumericCellValue());
                        fila.add(cellNum);
                    } else {
                        fila.add(cell.toString());
                    }
                    
                }

            }

            if (filas.getRowNum() != 0) {
                datos.add(fila);
            }
        }
        wb.close();
    }

    public List<List<String>> getDatos() {
        return datos;
    }

    public void setDatos(List<List<String>> datos) {
        this.datos = datos;
    }

    public List<String> getCabeceros() {
        return cabeceros;
    }

    public void setCabeceros(List<String> cabeceros) {
        this.cabeceros = cabeceros;
    }

}
