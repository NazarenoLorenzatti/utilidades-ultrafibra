package com.ultrafibra.utilidades.utilidades.service.insumos;

import com.ultrafibra.utilidades.DAO.insumosDAO.*;
import com.ultrafibra.utilidades.domain.insumos.*;
import com.ultrafibra.utilidades.utilidades.service.util.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import lombok.Data;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Data
public class InsumosService {

    @Autowired
    private iInsumoDao insumoDao;

    @Autowired
    private iCategoriaDao categoriaDao;

    @Autowired
    private iSucursalDao sucursalDao;

    @Autowired
    private iTitularDao titularDao;

    @Autowired
    private SubirArchivo subirArchivo;

    @Autowired
    private iDepartamentoDao departamentoDao;

    public List<Insumo> crearInsumo(String codigoBarras, String nombreInsumo, String descripcion, Date fecha,
            String idTitularStr, String idDepartamentoStr, String idCategoriaStr, String idSucursalStr) {
        Long idTitular = Long.parseLong(idTitularStr);
        Long idDepartamento = Long.parseLong(idDepartamentoStr);
        Long idCategoria = Long.parseLong(idCategoriaStr);
        Long idSucursal = Long.parseLong(idSucursalStr);

        insumoDao.save(new Insumo(
                codigoBarras,
                nombreInsumo,
                descripcion,
                fecha,
                sucursalDao.findById(idSucursal).get(),
                categoriaDao.findById(idCategoria).get(),
                titularDao.findById(idTitular).get(),
                departamentoDao.findById(idDepartamento).get())
        );

        return insumoDao.findAll();
    }

    public List<Insumo> editarInsumo(String idInsumoStr, String codigoBarras, String nombreInsumo, String descripcion, Date fecha,
            String idTitularStr, String idDepartamentoStr, String idCategoriaStr, String idSucursalStr) {

        Long idInsumo = Long.parseLong(idInsumoStr);
        Long idTitular = Long.parseLong(idTitularStr);
        Long idDepartamento = Long.parseLong(idDepartamentoStr);
        Long idCategoria = Long.parseLong(idCategoriaStr);
        Long idSucursal = Long.parseLong(idSucursalStr);

        Insumo i = insumoDao.findById(idInsumo).get();
        i.setCodigoBarras(codigoBarras);
        i.setNombre_insumo(nombreInsumo);
        i.setDescripcion(descripcion);
        i.setFecha_compra(fecha);
        i.setSucursal(sucursalDao.findById(idSucursal).get());
        i.setCategoria(categoriaDao.findById(idCategoria).get());
        i.setTitular(titularDao.findById(idTitular).get());
        i.setDepartamento(departamentoDao.findById(idDepartamento).get());

        insumoDao.save(i);
        return insumoDao.findAll();
    }

    public List<Insumo> editarInsumoMasivo(String[] idInsumos, String codigoBarras, String nombreInsumo, String descripcion, Date fecha,
            String idTitularStr, String idDepartamentoStr, String idCategoriaStr, String idSucursalStr) {

        for (String id : idInsumos) {
            Long idInsumo = Long.parseLong(id);
            Long idTitular = Long.parseLong(idTitularStr);
            Long idDepartamento = Long.parseLong(idDepartamentoStr);
            Long idCategoria = Long.parseLong(idCategoriaStr);
            Long idSucursal = Long.parseLong(idSucursalStr);

            Insumo i = insumoDao.findById(idInsumo).get();
            i.setCodigoBarras(codigoBarras);
            i.setNombre_insumo(nombreInsumo);
            i.setDescripcion(descripcion);
            i.setFecha_compra(fecha);
            i.setSucursal(sucursalDao.findById(idSucursal).get());
            i.setCategoria(categoriaDao.findById(idCategoria).get());
            i.setTitular(titularDao.findById(idTitular).get());
            i.setDepartamento(departamentoDao.findById(idDepartamento).get());

            insumoDao.save(i);
        }

        return insumoDao.findAll();
    }

    public List<Insumo> eliminarInsumo(String idStr) {
        Long id = Long.parseLong(idStr);
        insumoDao.deleteById(id);
        return insumoDao.findAll();
    }

    public List<Categoria> crearCategoria(String nombre) {
        categoriaDao.save(new Categoria(nombre));
        return categoriaDao.findAll();
    }

    public List<Categoria> actualizarCategoria(String nombreNuevo, String idStr) {
        Long id = Long.parseLong(idStr);
        Optional<Categoria> CO = categoriaDao.findById(id);
        Categoria c = CO.get();
        c.setNombre_categoria(nombreNuevo);
        categoriaDao.save(c);
        return categoriaDao.findAll();
    }

    public List<Categoria> eliminarCategoria(String idStr) throws Exception {
        Long id = Long.parseLong(idStr);
        categoriaDao.deleteById(id);
        return categoriaDao.findAll();
    }

    public List<Sucursal> crearSucursal(String nombre) {
        sucursalDao.save(new Sucursal(nombre));
        return sucursalDao.findAll();
    }

    public List<Sucursal> actualizarSucursal(String nombreNuevo, String idStr) {
        Long id = Long.parseLong(idStr);
        Optional<Sucursal> O = sucursalDao.findById(id);
        Sucursal s = O.get();
        s.setLocalidad_sucursal(nombreNuevo);
        sucursalDao.save(s);
        return sucursalDao.findAll();
    }

    public List<Sucursal> eliminarSucursal(String idStr) throws Exception {
        Long id = Long.parseLong(idStr);
        sucursalDao.deleteById(id);
        return sucursalDao.findAll();
    }

    public List<Titular> crearTitular(String nombre, String apellido, Long idDepartamento) {
        Optional<Departamento> OP = departamentoDao.findById(idDepartamento);
        Departamento dep = OP.get();
        titularDao.save(new Titular(nombre, apellido, dep));
        return (List<Titular>) titularDao.findAll();
    }

    public List<Titular> actualizarTitular(String idStr, String nombreNuevo, String apellidoNuevo, String idDepartamento) {
        Long id = Long.parseLong(idStr);
        Optional<Titular> OT = titularDao.findById(id);
        Titular T = OT.get();
        T.setNombre(nombreNuevo);
        T.setApellido(apellidoNuevo);
        T.setDepartamento(departamentoDao.findById(Long.parseLong(idDepartamento)).get());
        titularDao.save(T);
        return (List<Titular>) titularDao.findAll();
    }

    public List<Titular> eliminarTitular(String idStr) throws Exception {
        Long id = Long.parseLong(idStr);
        titularDao.deleteById(id);
        return (List<Titular>) titularDao.findAll();
    }

    public List<Departamento> crearDepartamento(String nombre) {
        departamentoDao.save(new Departamento(nombre));
        return departamentoDao.findAll();
    }

    public List<Departamento> actualizarDepartamento(String nombreNuevo, String idStr) {
        Long id = Long.parseLong(idStr);
        Optional<Departamento> DO = departamentoDao.findById(id);
        Departamento d = DO.get();
        d.setNombre_departamento(nombreNuevo);
        departamentoDao.save(d);
        return departamentoDao.findAll();
    }

    public List<Departamento> eliminarDepartamento(String idStr) {
        Long id = Long.parseLong(idStr);
        departamentoDao.deleteById(id);
        return departamentoDao.findAll();
    }

    public List<Insumo> importar(MultipartFile file) throws IOException {
        subirArchivo.upload(file);
        for (List<String> fila : subirArchivo.getLectorExcel().getDatos()) {
            Long sucursalId = Long.parseLong(fila.get(5).replace(",00", ""));
            Long categoriaId = Long.parseLong(fila.get(6).replace(",00", ""));
            Long titularId = Long.parseLong(fila.get(7).replace(",00", ""));
            Long departamentoId = Long.parseLong(fila.get(8).replace(",00", ""));

            Insumo i = new Insumo(fila.get(0), fila.get(1), fila.get(2),
                    stringToSqlDate(fila.get(4)),
                    sucursalDao.findById(sucursalId).get(),
                    categoriaDao.findById(categoriaId).get(),
                    titularDao.findById(titularId).get(),
                    departamentoDao.findById(departamentoId).get());

            insumoDao.save(i);
        }
        return insumoDao.findAll();
    }

    public Workbook imprimirListado() {
        List<List<String>> filas = new ArrayList();
        List<String> cabeceros = Arrays.asList("ID del Insumo", "Codigo De barras", "Insumo",
                "Descripcion", "Fecha de Adquisicion", "Titular", "Departamento", "Categoria", "Sucursal");

        for (Insumo i : insumoDao.findAll()) {
            List<String> fila = new ArrayList();
            fila.add(i.getIdInsumo().toString());
            fila.add(i.getCodigoBarras());
            fila.add(i.getNombre_insumo());
            fila.add(i.getDescripcion());
            fila.add(i.getFecha_compra().toString());
            fila.add(i.getTitular().getNombre() + " " + i.getTitular().getApellido());
            fila.add(i.getDepartamento().getNombre_departamento());
            fila.add(i.getCategoria().getNombre_categoria());
            fila.add(i.getSucursal().getLocalidad_sucursal());

            filas.add(fila);

        }

        var escritorXLS = new EscritorXLS(filas, cabeceros);
        return escritorXLS.exportar();
    }
    
    
        private Date stringToSqlDate(String fecha) {
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
        return Date.valueOf(LocalDate.of(intAño, intMes, intDia));
    }
}
