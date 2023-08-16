package com.ultrafibra.utilidades.utilidades.service.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Data
@Service
public class SubirArchivo {

    private String originalFileName;
    private String fileLocation;
    private LeectorExcel lectorExcel;
    private String ret;

    public SubirArchivo() {

    }

    public void upload(MultipartFile file) throws IOException {

        try {
            if (file.getOriginalFilename().endsWith("xls") || file.getOriginalFilename().endsWith("xlsx")) {

                InputStream in = file.getInputStream();
                File currDir = new File(".");
                String path = currDir.getAbsolutePath();
                fileLocation = path.substring(0, path.length() - 1) + file.getOriginalFilename();
                this.originalFileName = file.getOriginalFilename();
                FileOutputStream f = new FileOutputStream(fileLocation);
                int ch = 0;
                while ((ch = in.read()) != -1) {
                    f.write(ch);
                }
                f.flush();
                f.close();

                lectorExcel = new LeectorExcel(fileLocation);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar el archivo excel");
        }

    }

    public String getUri(String uri) {
        if (ret == null){
            this.ret = uri.substring(uri.indexOf("comercial"));
        }
        return ret;
    }

}
