package com.ultrafibra.utilidades.utilidades.service.alertas;

import com.mashape.unirest.http.*;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.ultrafibra.utilidades.DAO.tecnicoDAO.iEventoDao;
import com.ultrafibra.utilidades.DAO.tecnicoDAO.iTecnicoDao;
import com.ultrafibra.utilidades.domain.Evento;
import com.ultrafibra.utilidades.domain.Tecnico;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
public class EnviarAlerta {

    private static String alertaMensaje;
    private static String token;
    private static String remitente;
    private static String asunto;
    private static int retorno;
    
    private volatile boolean detenerLeerMails = false;
    
    @Autowired
    private iTecnicoDao tecnicoDao;

    @Autowired
    private iEventoDao eventoDao;

    public EnviarAlerta() {

    }
    
    public void iniciarLecturaMails() {
        Thread leerMailsThread = new Thread(() -> {
            try {
                leerMails();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        leerMailsThread.start();
        System.out.println("INICIO A LEER LOS MAILS");
    }
    
    public void detenerLecturaMails() {
        detenerLeerMails = true;
        System.out.println("PARO DE LEER LOS MAILS");
    }

    public void leerMails() throws InterruptedException {
        while (!detenerLeerMails) {
            System.out.println("VUELTA DE CICLO");
            try {
                Properties prop = new Properties();

                // Deshabilitamos TLS
                prop.setProperty("mail.pop3.starttls.enable", "false");

                // Usamos SSL
                prop.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                prop.setProperty("mail.pop3.socketFactory.fallback", "false");

                // Puerto 995 para conectarse.
                prop.setProperty("mail.pop3.port", "995");
                prop.setProperty("mail.pop3.socketFactory.port", "995");

                Session sesion = Session.getInstance(prop);

                try {
                    Store store = sesion.getStore("pop3");
                    store.connect("pop.gmail.com", "alertas.ups.ultrafibra@gmail.com", "spgsodpzqyxfqjxh");

                    Folder folder = store.getFolder("INBOX");
                    folder.open(Folder.READ_ONLY);

                    Message[] mensajes = folder.getMessages();

                    for (int i = 0; i < mensajes.length; i++) {
                        int ret = 0;
                        this.remitente = mensajes[i].getFrom()[0].toString();
                        this.asunto = mensajes[i].getSubject();

                        if (this.remitente.contains("ups-notificaciones@ultra.net.ar") || this.remitente.contains("nlorenzatti@ultra.net.ar") || this.remitente.contains("cacti@ultra.net.ar")) {
                            analizaParteDeMensajeUPS(mensajes[i]);
                            if (this.remitente.contains("cacti@ultra.net.ar")) {
                                alertaMensaje = alertaMensaje.substring(0, 280);
                                System.out.println("alertaMensaje = " + alertaMensaje);
//                            alertaMensaje = alertaMensaje.replaceAll("%", "");
                                alertaMensaje = alertaMensaje.trim();
                                ret = enviarMensaje(this.alertaMensaje);
                            } else {
                                ret = enviarMensaje(this.alertaMensaje);
                            }
                        } else if (this.remitente.contains("catchall@vangrow.ar") || this.remitente.contains("alertas.ups.ultrafibra@gmail.com")) {
                            analizaParteDeMensajeOLT(mensajes[i]);
                            ret = enviarMensaje(this.alertaMensaje);
                        }

                        if (ret == 401) {
                            nuevoToken();
                            i--; // reinicio i para que no se pierda el mensaje que ya paso
                        }
                        Thread.sleep(8000);

                    }

                    folder.close(false);
                    store.close();

                } catch (NoSuchProviderException ex) {

                    Logger.getLogger(EnviarAlerta.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Error al obtener el protocolo de sesion");

                } catch (InterruptedException ex) {
                    Logger.getLogger(EnviarAlerta.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Error al pausar el Hilo");
                }

            } catch (MessagingException ex) {
                Logger.getLogger(EnviarAlerta.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error al establecer la conexion y/o obtener la carpeta de correos");
            }
            Thread.sleep(25000);
        }
        
    }

    //ANALIZAR EL MAIL PARA OBTENER LA INFORMACION DE LA ALERTA
    private static void analizaParteDeMensajeUPS(Part unaParte) {

        try {
            // Si es multiparte, se analiza cada una de sus partes recursivamente, Obteniendo el mensaje solamente. 
            if (unaParte.isMimeType("multipart/*")) {
                Multipart multi;
                multi = (Multipart) unaParte.getContent();

                for (int j = 0; j < multi.getCount(); j++) {
                    Part bodyPart = multi.getBodyPart(j);
                    if (bodyPart.isMimeType("text/*")) {
                        alertaMensaje = (String) bodyPart.getContent();
                        break;
                    }
                    analizaParteDeMensajeUPS(multi.getBodyPart(j));
                }
            }
            // si es un texto simple se guarda directamente el mensaje
            if (unaParte.isMimeType("text/*")) {
                alertaMensaje = (String) unaParte.getContent();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al leer el correo");
        }

    }

    private static void analizaParteDeMensajeOLT(Part unaParte) {

        try {
            // Si es multiparte, se analiza cada una de sus partes recursivamente, Obteniendo el mensaje solamente. 
            if (unaParte.isMimeType("multipart/*")) {
                Multipart multi;
                multi = (Multipart) unaParte.getContent();

                for (int j = 0; j < multi.getCount(); j++) {
                    Part bodyPart = multi.getBodyPart(j);
                    if (bodyPart.isMimeType("text/*")) {
                        alertaMensaje = (String) bodyPart.getContent();
                        alertaMensaje = alertaMensaje.substring(14480, 14547);
                        alertaMensaje = alertaMensaje.replaceAll("</p>", "");
                        alertaMensaje = alertaMensaje.replaceAll("</td>", "").replaceAll("<", "").replaceAll(">", "");
                        alertaMensaje = alertaMensaje.replaceAll("left", "").replace("align", "").replace("=", "").replace("lign", "");
                        alertaMensaje = alertaMensaje.replaceAll("\"", "");
                        alertaMensaje = alertaMensaje.trim();
                        alertaMensaje = asunto + ">>>>" + alertaMensaje;
                        break;
                    }
                    analizaParteDeMensajeOLT(multi.getBodyPart(j));
                }
            }
            // si es un texto simple se guarda directamente el mensaje
            if (unaParte.isMimeType("text/*")) {
                alertaMensaje = (String) unaParte.getContent();
                alertaMensaje = alertaMensaje.substring(14480, 14547);
                alertaMensaje = alertaMensaje.replaceAll("</p>", "");
                alertaMensaje = alertaMensaje.replaceAll("</td>", "").replaceAll("<", "").replaceAll(">", "");
                alertaMensaje = alertaMensaje.replaceAll("left", "").replace("align", "").replace("=", "").replace("lign", "");
                alertaMensaje = alertaMensaje.replaceAll("\"", "");
                alertaMensaje = alertaMensaje.trim();
                alertaMensaje = asunto + ">>>>" + alertaMensaje;

            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al leer el correo");
        }

    }

    public int enviarMensaje(String alerta) {
        int ret = 0;
        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.post("https://mayten.cloud/api/Mensajes/Texto")
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .body(bodyResponse(alerta))
                    .asString();
            ret = response.getStatus();
            System.out.println("retorno = " + ret);
            registrarEvento(ret);
        } catch (UnirestException ex) {
            Logger.getLogger(EnviarAlerta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    //SOLICITAR TOKEN NUEVO
    private static void nuevoToken() {
        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.post("http://mayten.cloud/auth")
                    .header("Content-Type", "application/json")
                    .body("{\r\n\t\"username\": \"megalink\",\r\n\t\"password\": \"megalink123\"\r\n}")
                    .asString();
            // Leo el body de la llamda donde esta el token
            String body = response.getBody();
            // extraigo el token y lo guardo en el atributo
            token = body.substring(10, 314);

        } catch (UnirestException ex) {
            Logger.getLogger(EnviarAlerta.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private String bodyResponse(String alerta) {
        List<Tecnico> tecnicos = tecnicoDao.findAll();
        String body = "{\r\n    \"origen\": \"SMS_CORTO\",\r\n    \"mensajes\": [";
        for (Tecnico t : tecnicos) {
            body += "\r\n        {\r\n            \"mensaje\": \"" + alerta + "\",\r\n            \"telefono\": \"" + t.getCelular() + "\",\r\n            \"identificador\": \"\"\r\n        },";
        }
        body = body.substring(0, body.length()-1);
        body += "\r\n    ]\r\n}";
        return body;
    }

    public static int getRetorno() {
        return retorno;
    }

    private void registrarEvento(int ret) {
        switch (ret) {
            case 200:
                eventoDao.save(new Evento(String.valueOf(ret) + " - Envio de Mensaje Correcto" + LocalDate.now()));
                break;
            case 401:
                eventoDao.save(new Evento(String.valueOf(ret) + " - No autorizado, el proceso de autenticación ha sido incorrecto." + LocalDate.now()));
                break;
            case 400:
                eventoDao.save(new Evento(String.valueOf(ret) + " -  Petición incorrecta: existe algún error de sintaxis en el cuerpo de la petición." + LocalDate.now()));
                break;
            case 402:
                eventoDao.save(new Evento(String.valueOf(ret) + " - Pago requerido." + LocalDate.now()));
                break;
            case 404:
                eventoDao.save(new Evento(String.valueOf(ret) + " - Recurso no encontrado. Probablemente tengas mal el endpoint al que apuntas." + LocalDate.now()));
                break;
            case 500:
                eventoDao.save(new Evento(String.valueOf(ret) + " - Se ha producido algún error en el servidor. Infórmanos para que lo arreglemos." + LocalDate.now()));
                break;
            case 503:
                eventoDao.save(new Evento(String.valueOf(ret) + " - Service Temporarily Unavailable. Ha superado el límite máximo admitido de request por segundo." + LocalDate.now()));
                break;
        }

    }

}
