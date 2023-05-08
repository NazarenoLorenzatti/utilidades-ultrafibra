package com.ultrafibra.utilidades.utilidades.service.alertas;

import com.ultrafibra.utilidades.DAO.tecnicoDAO.iTecnicoDao;
import com.ultrafibra.utilidades.domain.Tecnico;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
public class TecnicoService {

    @Autowired
    private iTecnicoDao tecnicoDao;

    public void editar(String nombre, String celular, String idStr) {
        Tecnico t = tecnicoDao.findById(Long.parseLong(idStr)).get();
        t.setNombre_tecnico(nombre);
        t.setCelular(celular);
        tecnicoDao.save(t);
    }

    public void eliminar(String idStr) {
        tecnicoDao.deleteById(Long.parseLong(idStr));
    }

    public void agregar(String nombre, String celular) {
        tecnicoDao.save(new Tecnico(nombre, celular));
    }

}
