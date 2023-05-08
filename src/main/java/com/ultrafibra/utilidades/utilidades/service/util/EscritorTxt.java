package com.ultrafibra.utilidades.utilidades.service.util;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EscritorTxt {

    private File archivo;

    public EscritorTxt(String nombreArchivo) {

        this.archivo = new File(nombreArchivo);
        crear();

    }

    public void escribir(String fila, boolean anexar) {

        if (archivo.exists()) {
            try {
                PrintWriter salida = new PrintWriter(new FileWriter(archivo, anexar));
                salida.println(fila);
                salida.close();

            } catch (IOException ex) {
                ex.printStackTrace(System.out);

            }

        }

    }

    public void borrar() {
        archivo.delete();
    }

    public void crear() {
        try {
            PrintWriter salida = new PrintWriter(this.archivo);
            salida.close();

        } catch (FileNotFoundException ex) {
            ex.printStackTrace(System.out);

        }

    }

    public void abrirarchivo() {
        
        try {
            Desktop.getDesktop().open(this.archivo);
        } catch (IOException ex) {
            Logger.getLogger(EscritorTxt.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public File getArchivo() {
        return archivo;
    }
    
    
}

