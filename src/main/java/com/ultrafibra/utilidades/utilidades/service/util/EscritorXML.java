package com.ultrafibra.utilidades.utilidades.service.util;

import com.ultrafibra.utilidades.domain.Cupon;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class EscritorXML {

    private final List<Cupon> cuponesList;
    private String path;
    private DecimalFormat df;

    public EscritorXML(String path, List<Cupon> cupones) {
        this.path = path;
        this.cuponesList = cupones;
        this.df = new DecimalFormat("#.00");

    }

    public byte[] crearXML() {
        byte[] array = null;

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element recepciones = doc.createElement("recepciones");
            Element recepcion = doc.createElement("recepcion");
            doc.appendChild(recepciones);

            Element fecha_sincronizacion = doc.createElement("fecha_sincronizacion");
            fecha_sincronizacion.setTextContent(DateTimeFormatter.ofPattern("yyyy-mm-dd").format(LocalDateTime.now()));
            recepcion.appendChild(fecha_sincronizacion);

            Element id_sincronizacion = doc.createElement("id_sincronizacion");
            id_sincronizacion.setTextContent("00000000000000000001");
            recepcion.appendChild(id_sincronizacion);

            Element cliente = doc.createElement("Cliente");
            Element numero = doc.createElement("numero");
            numero.setTextContent("330");
            cliente.appendChild(numero);

            Element razon_social = doc.createElement("razon_social");
            razon_social.setTextContent("MegalinkSRL");
            cliente.appendChild(razon_social);

            Element cuit = doc.createElement("cuit");
            cuit.setTextContent("30715652826");
            cliente.appendChild(cuit);

            Element password = doc.createElement("password");
            password.setTextContent("MKAdb7");
            cliente.appendChild(password);

            recepcion.appendChild(cliente);
            Element cupones = doc.createElement("cupones");

            //-------------------------CUPONES ------------------------------------------------------------------------------
            //-------------------------------------------------------------------------------------------------------------
            for (int i = 0; i < this.cuponesList.size(); i++) {

                Cupon cuponDomain = this.cuponesList.get(i);
                Element cupon = doc.createElement("cupon");

                Element fecha = doc.createElement("fecha");
                fecha.setTextContent(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(cuponDomain.getFecha()));
                cupon.appendChild(fecha);

                Element anio = doc.createElement("anio");
                anio.setTextContent(Integer.toString(cuponDomain.getAnio()));
                cupon.appendChild(anio);

                Element periodo = doc.createElement("periodo");
                periodo.setTextContent(Integer.toString(cuponDomain.getPeriodo()));
                cupon.appendChild(periodo);

                Element numero_cupon = doc.createElement("numero_cupon");
                numero_cupon.setTextContent(cuponDomain.getNumero_cupon());
                cupon.appendChild(numero_cupon);

                Element numero_cliente = doc.createElement("numero_cliente");
                numero_cliente.setTextContent(cuponDomain.getNumero_cliente());
                cupon.appendChild(numero_cliente);

                Element razon_social_cliente = doc.createElement("razon_social_cliente");
                razon_social_cliente.setTextContent(cuponDomain.getRazon_social_cliente());
                cupon.appendChild(razon_social_cliente);

                Element mail = doc.createElement("mail");
                mail.setTextContent(cuponDomain.getMail());
                cupon.appendChild(mail);

                Element numero_cuenta = doc.createElement("numero_cuenta");
                numero_cuenta.setTextContent(cuponDomain.getNumero_cuenta());
                cupon.appendChild(numero_cuenta);

                Element cbu = doc.createElement("cbu");
                cbu.setTextContent(cuponDomain.getCbu());
                cupon.appendChild(cbu);

                Element titular_cuenta = doc.createElement("titular_cuenta");
                titular_cuenta.setTextContent(cuponDomain.getTitular_cuenta());
                cupon.appendChild(titular_cuenta);

                Element numero_cuenta_megalink = doc.createElement("numero_cuenta_empresa");
                numero_cuenta_megalink.setTextContent("9421");
                cupon.appendChild(numero_cuenta_megalink);

                Element cbu_empresa = doc.createElement("cbu_empresa");
                cbu_empresa.setTextContent("");
                cupon.appendChild(cbu_empresa);

                Element titular_cuenta_empresa = doc.createElement("titular_cuenta_empresa");
                titular_cuenta_empresa.setTextContent("");
                cupon.appendChild(titular_cuenta_empresa);

                Element total = doc.createElement("total");
                total.setTextContent(df.format(cuponDomain.getTotal()).replace(",", "."));
                cupon.appendChild(total);

                Element id_moneda_sirplus = doc.createElement("id_moneda_sirplus");
                id_moneda_sirplus.setTextContent("1");
                cupon.appendChild(id_moneda_sirplus);

                Element valor_pesos = doc.createElement("valor_pesos");
                valor_pesos.setTextContent(df.format(cuponDomain.getValor_pesos()).replace(",", "."));
                cupon.appendChild(valor_pesos);

                Element codigo_barras = doc.createElement("codigo_barras");
                codigo_barras.setTextContent(cuponDomain.getCodigo_barras());
                cupon.appendChild(codigo_barras);

                //Vencimientos
                Element id_moneda_Primervencimiento_sirplus = doc.createElement("id_moneda_vencimiento_sirplus");
                id_moneda_Primervencimiento_sirplus.setTextContent("1");

                Element id_moneda_Segundovencimiento_sirplus = doc.createElement("id_moneda_vencimiento_sirplus");
                id_moneda_Segundovencimiento_sirplus.setTextContent("1");

                Element id_moneda_Tercervencimiento_sirplus = doc.createElement("id_moneda_vencimiento_sirplus");
                id_moneda_Tercervencimiento_sirplus.setTextContent("1");

                //-----------------------------------------------------------------------------
                Element primerVencimiento = doc.createElement("vencimientos");

                Element fechaPrimerVencimiento = doc.createElement("fecha_vencimiento");
                fechaPrimerVencimiento.setTextContent(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(cuponDomain.getFecha_vencimiento()));
                primerVencimiento.appendChild(fechaPrimerVencimiento);

                Element importePrimerVencimiento = doc.createElement("importe");
                importePrimerVencimiento.setTextContent(df.format(cuponDomain.getValor_pesos_vencimiento()).replace(",", "."));
                primerVencimiento.appendChild(importePrimerVencimiento);

                Element valor_pesos_PrimerVencimiento = doc.createElement("valor_pesos_vencimiento");
                valor_pesos_PrimerVencimiento.setTextContent(df.format(cuponDomain.getValor_pesos_vencimiento()).replace(",", "."));
                primerVencimiento.appendChild(id_moneda_Primervencimiento_sirplus);
                primerVencimiento.appendChild(valor_pesos_PrimerVencimiento);

                cupon.appendChild(primerVencimiento);

                //--------------------------------------------------------------------------
                Element segundoVencimiento = doc.createElement("vencimientos");

                Element fechaSegundoVencimiento = doc.createElement("fecha_vencimiento");
                fechaSegundoVencimiento.setTextContent(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(cuponDomain.getFecha_segundoVencimiento()));
                segundoVencimiento.appendChild(fechaSegundoVencimiento);

                Element importeSegundoVencimiento = doc.createElement("importe");
                importeSegundoVencimiento.setTextContent(df.format(cuponDomain.getValor_pesos_vencimiento() + cuponDomain.getDifVencimientos()).replace(",", "."));
                segundoVencimiento.appendChild(importeSegundoVencimiento);

                Element valor_pesos_SegundoVencimiento = doc.createElement("valor_pesos_vencimiento");
                valor_pesos_SegundoVencimiento.setTextContent(df.format(cuponDomain.getValor_pesos_vencimiento() + cuponDomain.getDifVencimientos()).replace(",", "."));
                segundoVencimiento.appendChild(id_moneda_Segundovencimiento_sirplus);
                segundoVencimiento.appendChild(valor_pesos_SegundoVencimiento);

                cupon.appendChild(segundoVencimiento);

                //------------------------------------------------------------------------------          
                Element tercerVencimiento = doc.createElement("vencimientos");

                Element fechaTercerVencimiento = doc.createElement("fecha_vencimiento");
                fechaTercerVencimiento.setTextContent(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(cuponDomain.getFecha_tercerVencimiento()));
                tercerVencimiento.appendChild(fechaTercerVencimiento);

                Element importeTercerVencimiento = doc.createElement("importe");
                importeTercerVencimiento.setTextContent(df.format(cuponDomain.getValor_pesos_vencimiento() + cuponDomain.getDifVencimientos()).replace(",", "."));
                tercerVencimiento.appendChild(importeTercerVencimiento);

                Element valor_pesos_TercerVencimiento = doc.createElement("valor_pesos_vencimiento");
                valor_pesos_TercerVencimiento.setTextContent(df.format(cuponDomain.getValor_pesos_vencimiento() + cuponDomain.getDifVencimientos()).replace(",", "."));
                tercerVencimiento.appendChild(id_moneda_Tercervencimiento_sirplus);
                tercerVencimiento.appendChild(valor_pesos_TercerVencimiento);

                cupon.appendChild(tercerVencimiento);
                //-----------------------------------------------------------------------------

                cupon.appendChild(primerVencimiento);
                cupon.appendChild(segundoVencimiento);
                cupon.appendChild(tercerVencimiento);

                // ENTIDADES--------------------------------------------------------------------
                Element entidades = doc.createElement("entidades");

                Element entidad1 = doc.createElement("entidad");
                Element id_entidad1 = doc.createElement("id_entidad");
                id_entidad1.setTextContent("1");
                entidad1.appendChild(id_entidad1);

                Element entidad2 = doc.createElement("entidad");
                Element id_entidad2 = doc.createElement("id_entidad");
                id_entidad2.setTextContent("2");
                entidad2.appendChild(id_entidad2);

//                Element entidad3 = doc.createElement("entidad");
//                Element id_entidad3 = doc.createElement("id_entidad");
//                id_entidad3.setTextContent("3");
//                entidad3.appendChild(id_entidad3);
                Element entidad4 = doc.createElement("entidad");
                Element id_entidad4 = doc.createElement("id_entidad");
                id_entidad4.setTextContent("4");
                entidad4.appendChild(id_entidad4);

                Element entidad5 = doc.createElement("entidad");
                Element id_entidad5 = doc.createElement("id_entidad");
                id_entidad5.setTextContent("5");
                entidad5.appendChild(id_entidad5);

                Element entidad6 = doc.createElement("entidad");
                Element id_entidad6 = doc.createElement("id_entidad");
                id_entidad6.setTextContent("6");
                entidad6.appendChild(id_entidad6);

                entidades.appendChild(entidad1);
                entidades.appendChild(entidad2);
//                entidades.appendChild(entidad3);
                entidades.appendChild(entidad4);
                entidades.appendChild(entidad5);
                entidades.appendChild(entidad6);

                cupon.appendChild(entidades);

                cupones.appendChild(cupon);
            }
            recepcion.appendChild(cupones);
            //-------------------------CUPONES ------------------------------------------------------------------------------
            //-------------------------------------------------------------------------------------------------------------  
            recepciones.appendChild(recepcion);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(outputStream);
            transformer.transform(source, result);
            array = outputStream.toByteArray();

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }

        return array;
    }

}
