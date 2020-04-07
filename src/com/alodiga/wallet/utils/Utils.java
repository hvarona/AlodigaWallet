package com.alodiga.wallet.utils;

import com.alodiga.wallet.model.BalanceHistory;
import com.alodiga.wallet.model.Transaction;
import com.ericsson.alodiga.ws.Usuario;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.Random;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import org.apache.log4j.Logger;

public class Utils {
    
     static {
	    //for localhost testing only
	    javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
	    new javax.net.ssl.HostnameVerifier(){

	        public boolean verify(String string, SSLSession ssls) {
    return true;
}
	    });
	}

    private static Properties messages;
    private static Properties validationMessages;
    private static String ALODIGA_WALLET_ADDRESS = "alodigawallet@alodiga.com";

    private static final Logger logger = Logger.getLogger(Utils.class);

    public static String generarCodigoRandom(int longitud) {
        String codigoGenerado = "";
        long milis = new java.util.GregorianCalendar().getTimeInMillis();
        Random r = new Random(milis);
        int i = 0;
        while (i < longitud) {
            codigoGenerado += "" + r.nextInt(10);
            i++;
        }
        System.out.println("codGenerado: ----> " + codigoGenerado);
        return codigoGenerado;
    }

    public static String generarNumeroDeCuenta() {
        return "256344" + Utils.generarCodigoRandom(10);

    }





    public static String obtienePropiedad(String key) {
        try {
            if (messages == null) {
                messages = new Properties();
                String path = Utils.class.getClassLoader()
                        .getResource("config.properties").getFile();
                messages.load(new FileInputStream(path));
            }
            return messages.getProperty(key).trim();
        } catch (IOException ex) {
            return "(El sistema no puede encontrar el archivo .properties)";
        }
    }

    public static String obtieneMensaje(String key) {
        try {
            if (validationMessages == null) {
                validationMessages = new Properties();
                String path = Utils.class.getClassLoader()
                        .getResource("ValidationMessages.properties").getFile();
                validationMessages.load(new FileInputStream(path));
            }
            return validationMessages.getProperty(key).trim();
        } catch (IOException ex) {
            return "(El sistema no puede encontrar el archivo .properties)";
        }
    }
    
    public static Timestamp[] DateTransaction() {
        Timestamp[ ] dates = new Timestamp[2];
        Calendar calTodayBeggining = Calendar.getInstance();
        calTodayBeggining.set(Calendar.HOUR_OF_DAY,0);
        calTodayBeggining.set(Calendar.MINUTE,0);            
        calTodayBeggining.set(Calendar.SECOND,0);            
        calTodayBeggining.set(Calendar.MILLISECOND,0);
        Timestamp begginingDateTime = new Timestamp(calTodayBeggining.getTimeInMillis());
        dates[0] = begginingDateTime;
        Calendar calTodayEnding = (Calendar) calTodayBeggining.clone();
        calTodayEnding.set(Calendar.HOUR,23);
        calTodayEnding.set(Calendar.MINUTE,59);            
        calTodayEnding.set(Calendar.SECOND,59);            
        calTodayEnding.set(Calendar.MILLISECOND,999);
        Timestamp endingDateTime = new Timestamp(calTodayEnding.getTimeInMillis());
        dates[1] = endingDateTime;
        return dates;   
    }
    
    
            
        //PAGO A COMERCIO 
        public static Mail sendMailUserPaymentCommerce(String idioma, Float amountPayment,  String conceptTransaction, String userDestinationName, String emailUser)  {
        String hello = "Hola";
        String subject = "Alodiga Wallet: Pago a Comercio";
        String text1 = "Alodiga Wallet: Pago a Comercio";
        String text2 = "Datos de su Pago:";
        String amount_Payment = "Monto de Pago: ";
        String concept_Transaction = "Concepto de la transacci&oacute;n: ";
        String date = "Fecha: ";
        String usuario_Destination = "Nombre de Usuario de destino: ";
        String moreInfo = "Para mayor informaci&oacute;n visiste";
        String mailInvite = "Le invitamos a seguir disfrutando los beneficios y de los atractivos productos y servicios que le ofrece Alodiga.";
        String thanks = "Gracias por preferirnos, Alodiga Mejora tu vida";
        String messageFooter1 = "Este mensaje ha sido enviado desde una cuenta de correo electr&oacute;nico exclusivamente de notificaciones que no admite mensajes. No responda esta comunicaci&oacute;n.";
        String allRights = "Todos los derechos reservados";
        if (idioma.equalsIgnoreCase("EN")) {
            hello = "Hello";
            subject = "Alodiga Wallet: Balance purchase";
            text1 = "Alodiga Wallet: Balance purchase";
            text2 = "Payment Details: ";
            amount_Payment = "Amount Payment: ";
            concept_Transaction = "Concept Transaction: ";
            date = "Date:";
            usuario_Destination = "Usuario Destination: ";
            moreInfo = "Form more info visit";
            mailInvite = "We invite you to continue enjoying the benefits and attractive products and services offered by Alodiga.";
            thanks = "Thank you for choosing Alodiga, Alodiga Mejora tu vida";
            messageFooter1 = "This message was sent from an email distributor solely for notification messages that are not supported. Do not respond to this communication";
            allRights = "All rights reserved";
        }
        String style1 = "style='font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #666; font-size:13px;'";
        String style2 = "style='background-color: #07b49c;color:#ffff;font:16px/1.8em Arial,Helvetica,sans-serif,lighter;font-weight:bold;padding-left:10px'";
        String body = "";
        body = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>";
        body += "<html xmlns='http://www.w3.org/1999/xhtml'>";
        body += "<head>";
        body += "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/><style type='text/css'>.Estilo11 {font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #333333; font-size:13px; font-weight:bold;}</style><style type='text/css'>.Estilo12 {font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #666; font-size:13px;}</style><style type='text/css'>.EstiloColumn {background-color: #555555;color:#FFBF00;font:12px/1.8em Arial,Helvetica,sans-serif,lighter;font-weight:bold;padding-left:10px}</style>" + "<div align='center'>"
                + "<table width='756' height='600' border='0'>"
                + "<tr><th width='750' height='595'><p>"
                + "<img src='http://sales.alodiga.com/images/img-alodiga-logo.png' align='left' width='114' height='90' longdesc='Logo alodiga' />"
                + "</p><p>&nbsp;</p>" + "<p>&nbsp;</p>"
                + "<table  width='730' border='0' >"
                + "<tr><th width='728' height='20' align='right' bgcolor='#4c8e41' style='color:#FFFF;font:16px/1.8em Arial,Helvetica,sans-serif,lighter;'>" + text2 + "</th></tr>"
                + "<tr><th width='728' height='5' bgcolor='#232323'></th></tr>"
                + "</table>"
                + "<table width='728' border='0'>"
                + "<tr><th width='728'>"
                + "<p align='left' class='Estilo11'><br/><br/>&iexcl;"
                + hello + " "  + "&nbsp;"
                + "!<br/><br/>"
                + text1 + "<br><br></p>"
                + "</th>"
                + "</tr>"
                + "<tr>"
                + "<th><p align='left' style='font: 16px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; font-weight:bold; display: table;  margin: 0; padding:0;' >"
                + text2
                + "</p></th></tr>"
                + "<tr height='3px'><th width='728' bgcolor='#232323'></th></tr>"
                + "<tr>"
                + "<th>"
                + "<div><table width='728' border='0' cellpadding='2' cellspancing='0' style='border:inherit'>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + amount_Payment + "" + amountPayment +"</div></td>"
                + "</tr>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left' >"
                + concept_Transaction + "" + conceptTransaction +"</div></td>"

                       
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + usuario_Destination + "" + userDestinationName + "</div></td>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + date + new Timestamp(new java.util.Date().getTime()) + "</div></td>"
                
               
                
                + "<tr height='3px'><th width='728' bgcolor='#232323'></th></tr>"
                + "<tr height='40px'>"
                + "<th height='40px'><div class='Estilo11' align='left'>"
                + moreInfo
                + "<span style='font-size: 13px'> "
                + "<a href='https://www.alodiga.com/'>  www.alodiga.com</a></span></div>"
                + "</th>"
                + "</tr>"
                + "<tr>"
                + "<th height='31' bordercolor='#999999'><div align='center'><p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;' >"
                + thanks
                + "</p>"
                + " <p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;'>"
                + messageFooter1
                + "</p>"

                + "</div>"
                + "</th>"
                + "</tr>"
                + " </table>"
                + "<div align='center'>"
                + "<p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;'>&copy; Copyright 2013 - Alodiga, C.A. " + allRights + "<br> </div></th></tr></table></div></body></html>";

        Mail mail = new Mail(subject,body);
        mail.setSubject(subject);
        mail.setFrom(ALODIGA_WALLET_ADDRESS);
        mail.setBody(body);
        ArrayList<String> recipients = new ArrayList<String>();
        recipients.add(emailUser);
        mail.setTo(recipients);
        return mail;
    }
        
        //PAGO A COMERCIO RECIBE
        public static Mail sendMailUserPaymentCommerceReciber(String idioma, Float amountPayment,  String conceptTransaction, String userDestinationName, String emailUser)  {
        String hello = "Hola";
        String subject = "Alodiga Wallet: Pago a Comercio";
        String text1 = "Alodiga Wallet: Pago a Comercio";
        String text2 = "Datos de su Recepci&oacute;n de Pago:";
        String amount_Payment = "Monto de Pago: ";
        String concept_Transaction = "Concepto de la transacci&oacute;n: ";
        String date = "Fecha: ";
        String usuario_Destination = "Nombre de Usuario de destino: ";
        String moreInfo = "Para mayor informaci&oacute;n visiste";
        String mailInvite = "Le invitamos a seguir disfrutando los beneficios y de los atractivos productos y servicios que le ofrece Alodiga.";
        String thanks = "Gracias por preferirnos, Alodiga Mejora tu vida";
        String messageFooter1 = "Este mensaje ha sido enviado desde una cuenta de correo electr&oacute;nico exclusivamente de notificaciones que no admite mensajes. No responda esta comunicaci&oacute;n.";
        String allRights = "Todos los derechos reservados";
        if (idioma.equalsIgnoreCase("EN")) {
            hello = "Hello";
            subject = "Alodiga Wallet: Balance purchase";
            text1 = "Alodiga Wallet: Balance purchase";
            text2 = "Data of your Payment Receipt: ";
            amount_Payment = "Amount Payment: ";
            concept_Transaction = "Concept Transaction: ";
            date = "Date:";
            usuario_Destination = "Usuario Destination: ";
            moreInfo = "Form more info visit";
            mailInvite = "We invite you to continue enjoying the benefits and attractive products and services offered by Alodiga.";
            thanks = "Thank you for choosing Alodiga, Alodiga Mejora tu vida";
            messageFooter1 = "This message was sent from an email distributor solely for notification messages that are not supported. Do not respond to this communication";
            allRights = "All rights reserved";
        }
        String style1 = "style='font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #666; font-size:13px;'";
        String style2 = "style='background-color: #07b49c;color:#ffff;font:16px/1.8em Arial,Helvetica,sans-serif,lighter;font-weight:bold;padding-left:10px'";
        String body = "";
        body = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>";
        body += "<html xmlns='http://www.w3.org/1999/xhtml'>";
        body += "<head>";
        body += "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/><style type='text/css'>.Estilo11 {font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #333333; font-size:13px; font-weight:bold;}</style><style type='text/css'>.Estilo12 {font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #666; font-size:13px;}</style><style type='text/css'>.EstiloColumn {background-color: #555555;color:#FFBF00;font:12px/1.8em Arial,Helvetica,sans-serif,lighter;font-weight:bold;padding-left:10px}</style>" + "<div align='center'>"
                + "<table width='756' height='600' border='0'>"
                + "<tr><th width='750' height='595'><p>"
                + "<img src='http://sales.alodiga.com/images/img-alodiga-logo.png' align='left' width='114' height='90' longdesc='Logo alodiga' />"
                + "</p><p>&nbsp;</p>" + "<p>&nbsp;</p>"
                + "<table  width='730' border='0' >"
                + "<tr><th width='728' height='20' align='right' bgcolor='#4c8e41' style='color:#FFFF;font:16px/1.8em Arial,Helvetica,sans-serif,lighter;'>" + text2 + "</th></tr>"
                + "<tr><th width='728' height='5' bgcolor='#232323'></th></tr>"
                + "</table>"
                + "<table width='728' border='0'>"
                + "<tr><th width='728'>"
                + "<p align='left' class='Estilo11'><br/><br/>&iexcl;"
                + hello + " "  + "&nbsp;"
                + "!<br/><br/>"
                + text1 + "<br><br></p>"
                + "</th>"
                + "</tr>"
                + "<tr>"
                + "<th><p align='left' style='font: 16px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; font-weight:bold; display: table;  margin: 0; padding:0;' >"
                + text2
                + "</p></th></tr>"
                + "<tr height='3px'><th width='728' bgcolor='#232323'></th></tr>"
                + "<tr>"
                + "<th>"
                + "<div><table width='728' border='0' cellpadding='2' cellspancing='0' style='border:inherit'>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + amount_Payment + "" + amountPayment +"</div></td>"
                + "</tr>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left' >"
                + concept_Transaction + "" + conceptTransaction +"</div></td>"

                       
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + usuario_Destination + "" + userDestinationName + "</div></td>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + date + new Timestamp(new java.util.Date().getTime()) + "</div></td>"
                
               
                
                + "<tr height='3px'><th width='728' bgcolor='#232323'></th></tr>"
                + "<tr height='40px'>"
                + "<th height='40px'><div class='Estilo11' align='left'>"
                + moreInfo
                + "<span style='font-size: 13px'> "
                + "<a href='https://www.alodiga.com/'>  www.alodiga.com</a></span></div>"
                + "</th>"
                + "</tr>"
                + "<tr>"
                + "<th height='31' bordercolor='#999999'><div align='center'><p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;' >"
                + thanks
                + "</p>"
                + " <p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;'>"
                + messageFooter1
                + "</p>"

                + "</div>"
                + "</th>"
                + "</tr>"
                + " </table>"
                + "<div align='center'>"
                + "<p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;'>&copy; Copyright 2013 - Alodiga, C.A. " + allRights + "<br> </div></th></tr></table></div></body></html>";

        Mail mail = new Mail(subject,body);
        mail.setSubject(subject);
        mail.setFrom(ALODIGA_WALLET_ADDRESS);
        mail.setBody(body);
        ArrayList<String> recipients = new ArrayList<String>();
        recipients.add(emailUser);
        mail.setTo(recipients);
        return mail;
    }
        
        //Transferencia
        public static Mail sendMailUserTransfer(String idioma, Float amountPayment,  String conceptTransaction, String userDestinationName, String emailUser)  {
        String hello = "Hola";
        String subject = "Alodiga Wallet: Transferencia:";
        String text1 = "Alodiga Wallet: Transferencia";
        String text2 = "Datos de su Transferencia:";
        String amount_Payment = "Monto Transferido: ";
        String concept_Transaction = "Concepto de la transacci&oacute;n: ";
        String date = "Fecha: ";
        String usuario_Destination = "Nombre de Usuario de destino: ";
        String moreInfo = "Para mayor informaci&oacute;n visiste";
        String mailInvite = "Le invitamos a seguir disfrutando los beneficios y de los atractivos productos y servicios que le ofrece Alodiga.";
        String thanks = "Gracias por preferirnos, Alodiga Mejora tu vida";
        String messageFooter1 = "Este mensaje ha sido enviado desde una cuenta de correo electr&oacute;nico exclusivamente de notificaciones que no admite mensajes. No responda esta comunicaci&oacute;n.";
        String allRights = "Todos los derechos reservados";
        if (idioma.equalsIgnoreCase("EN")) {
            hello = "Hello";
            subject = "Alodiga Wallet: Transfer:";
            text1 = "Alodiga Wallet: transfer:";
            text2 = "Data of your transfer: ";
            amount_Payment = "Amount Transferred: ";
            concept_Transaction = "Concept Transaction: ";
            date = "Date:";
            usuario_Destination = "Usuario Destination: ";
            moreInfo = "Form more info visit";
            mailInvite = "We invite you to continue enjoying the benefits and attractive products and services offered by Alodiga.";
            thanks = "Thank you for choosing Alodiga, Alodiga Mejora tu vida";
            messageFooter1 = "This message was sent from an email distributor solely for notification messages that are not supported. Do not respond to this communication";
            allRights = "All rights reserved";
        }
        String style1 = "style='font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #666; font-size:13px;'";
        String style2 = "style='background-color: #07b49c;color:#ffff;font:16px/1.8em Arial,Helvetica,sans-serif,lighter;font-weight:bold;padding-left:10px'";
        String body = "";
        body = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>";
        body += "<html xmlns='http://www.w3.org/1999/xhtml'>";
        body += "<head>";
        body += "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/><style type='text/css'>.Estilo11 {font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #333333; font-size:13px; font-weight:bold;}</style><style type='text/css'>.Estilo12 {font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #666; font-size:13px;}</style><style type='text/css'>.EstiloColumn {background-color: #555555;color:#FFBF00;font:12px/1.8em Arial,Helvetica,sans-serif,lighter;font-weight:bold;padding-left:10px}</style>" + "<div align='center'>"
                + "<table width='756' height='600' border='0'>"
                + "<tr><th width='750' height='595'><p>"
                + "<img src='http://sales.alodiga.com/images/img-alodiga-logo.png' align='left' width='114' height='90' longdesc='Logo alodiga' />"
                + "</p><p>&nbsp;</p>" + "<p>&nbsp;</p>"
                + "<table  width='730' border='0' >"
                + "<tr><th width='728' height='20' align='right' bgcolor='#4c8e41' style='color:#FFFF;font:16px/1.8em Arial,Helvetica,sans-serif,lighter;'>" + text2 + "</th></tr>"
                + "<tr><th width='728' height='5' bgcolor='#232323'></th></tr>"
                + "</table>"
                + "<table width='728' border='0'>"
                + "<tr><th width='728'>"
                + "<p align='left' class='Estilo11'><br/><br/>&iexcl;"
                + hello + " "  + "&nbsp;"
                + "!<br/><br/>"
                + text1 + "<br><br></p>"
                + "</th>"
                + "</tr>"
                + "<tr>"
                + "<th><p align='left' style='font: 16px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; font-weight:bold; display: table;  margin: 0; padding:0;' >"
                + text2
                + "</p></th></tr>"
                + "<tr height='3px'><th width='728' bgcolor='#232323'></th></tr>"
                + "<tr>"
                + "<th>"
                + "<div><table width='728' border='0' cellpadding='2' cellspancing='0' style='border:inherit'>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + amount_Payment + "" + amountPayment +"</div></td>"
                + "</tr>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left' >"
                + concept_Transaction + "" + conceptTransaction +"</div></td>"

                       
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + usuario_Destination + "" + userDestinationName + "</div></td>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + date + new Timestamp(new java.util.Date().getTime()) + "</div></td>"
                
               
                
                + "<tr height='3px'><th width='728' bgcolor='#232323'></th></tr>"
                + "<tr height='40px'>"
                + "<th height='40px'><div class='Estilo11' align='left'>"
                + moreInfo
                + "<span style='font-size: 13px'> "
                + "<a href='https://www.alodiga.com/'>  www.alodiga.com</a></span></div>"
                + "</th>"
                + "</tr>"
                + "<tr>"
                + "<th height='31' bordercolor='#999999'><div align='center'><p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;' >"
                + thanks
                + "</p>"
                + " <p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;'>"
                + messageFooter1
                + "</p>"

                + "</div>"
                + "</th>"
                + "</tr>"
                + " </table>"
                + "<div align='center'>"
                + "<p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;'>&copy; Copyright 2013 - Alodiga, C.A. " + allRights + "<br> </div></th></tr></table></div></body></html>";

        Mail mail = new Mail(subject,body);
        mail.setSubject(subject);
        mail.setFrom(ALODIGA_WALLET_ADDRESS);
        mail.setBody(body);
        ArrayList<String> recipients = new ArrayList<String>();
        recipients.add(emailUser);
        mail.setTo(recipients);
        return mail;
    }
        
        //Transferencia recibida
        public static Mail sendMailUserTransferReciber(String idioma, Float amountPayment,  String conceptTransaction, String userDestinationName, String emailUser)  {
        String hello = "Hola";
        String subject = "Alodiga Wallet: Transferencia:";
        String text1 = "Alodiga Wallet: Transferencia";
        String text2 = "Datos de su Transferencia Recibida:";
        String amount_Payment = "Monto Recibido: ";
        String concept_Transaction = "Concepto de la transacci&oacute;n: ";
        String date = "Fecha: ";
        String usuario_Destination = "Nombre de Usuario de destino: ";
        String moreInfo = "Para mayor informaci&oacute;n visiste";
        String mailInvite = "Le invitamos a seguir disfrutando los beneficios y de los atractivos productos y servicios que le ofrece Alodiga.";
        String thanks = "Gracias por preferirnos, Alodiga Mejora tu vida";
        String messageFooter1 = "Este mensaje ha sido enviado desde una cuenta de correo electr&oacute;nico exclusivamente de notificaciones que no admite mensajes. No responda esta comunicaci&oacute;n.";
        String allRights = "Todos los derechos reservados";
        if (idioma.equalsIgnoreCase("EN")) {
            hello = "Hello";
            subject = "Alodiga Wallet: Transfer:";
            text1 = "Alodiga Wallet: transfer:";
            text2 = "Data of your Transfer Received: ";
            amount_Payment = "Amount received: ";
            concept_Transaction = "Concept Transaction: ";
            date = "Date:";
            usuario_Destination = "Usuario Destination: ";
            moreInfo = "Form more info visit";
            mailInvite = "We invite you to continue enjoying the benefits and attractive products and services offered by Alodiga.";
            thanks = "Thank you for choosing Alodiga, Alodiga Mejora tu vida";
            messageFooter1 = "This message was sent from an email distributor solely for notification messages that are not supported. Do not respond to this communication";
            allRights = "All rights reserved";
        }
        String style1 = "style='font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #666; font-size:13px;'";
        String style2 = "style='background-color: #07b49c;color:#ffff;font:16px/1.8em Arial,Helvetica,sans-serif,lighter;font-weight:bold;padding-left:10px'";
        String body = "";
        body = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>";
        body += "<html xmlns='http://www.w3.org/1999/xhtml'>";
        body += "<head>";
        body += "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/><style type='text/css'>.Estilo11 {font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #333333; font-size:13px; font-weight:bold;}</style><style type='text/css'>.Estilo12 {font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #666; font-size:13px;}</style><style type='text/css'>.EstiloColumn {background-color: #555555;color:#FFBF00;font:12px/1.8em Arial,Helvetica,sans-serif,lighter;font-weight:bold;padding-left:10px}</style>" + "<div align='center'>"
                + "<table width='756' height='600' border='0'>"
                + "<tr><th width='750' height='595'><p>"
                + "<img src='http://sales.alodiga.com/images/img-alodiga-logo.png' align='left' width='114' height='90' longdesc='Logo alodiga' />"
                + "</p><p>&nbsp;</p>" + "<p>&nbsp;</p>"
                + "<table  width='730' border='0' >"
                + "<tr><th width='728' height='20' align='right' bgcolor='#4c8e41' style='color:#FFFF;font:16px/1.8em Arial,Helvetica,sans-serif,lighter;'>" + text2 + "</th></tr>"
                + "<tr><th width='728' height='5' bgcolor='#232323'></th></tr>"
                + "</table>"
                + "<table width='728' border='0'>"
                + "<tr><th width='728'>"
                + "<p align='left' class='Estilo11'><br/><br/>&iexcl;"
                + hello + " "  + "&nbsp;"
                + "!<br/><br/>"
                + text1 + "<br><br></p>"
                + "</th>"
                + "</tr>"
                + "<tr>"
                + "<th><p align='left' style='font: 16px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; font-weight:bold; display: table;  margin: 0; padding:0;' >"
                + text2
                + "</p></th></tr>"
                + "<tr height='3px'><th width='728' bgcolor='#232323'></th></tr>"
                + "<tr>"
                + "<th>"
                + "<div><table width='728' border='0' cellpadding='2' cellspancing='0' style='border:inherit'>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + amount_Payment + "" + amountPayment +"</div></td>"
                + "</tr>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left' >"
                + concept_Transaction + "" + conceptTransaction +"</div></td>"

                       
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + usuario_Destination + "" + userDestinationName + "</div></td>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + date + new Timestamp(new java.util.Date().getTime()) + "</div></td>"
                
               
                
                + "<tr height='3px'><th width='728' bgcolor='#232323'></th></tr>"
                + "<tr height='40px'>"
                + "<th height='40px'><div class='Estilo11' align='left'>"
                + moreInfo
                + "<span style='font-size: 13px'> "
                + "<a href='https://www.alodiga.com/'>  www.alodiga.com</a></span></div>"
                + "</th>"
                + "</tr>"
                + "<tr>"
                + "<th height='31' bordercolor='#999999'><div align='center'><p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;' >"
                + thanks
                + "</p>"
                + " <p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;'>"
                + messageFooter1
                + "</p>"

                + "</div>"
                + "</th>"
                + "</tr>"
                + " </table>"
                + "<div align='center'>"
                + "<p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;'>&copy; Copyright 2013 - Alodiga, C.A. " + allRights + "<br> </div></th></tr></table></div></body></html>";

        Mail mail = new Mail(subject,body);
        mail.setSubject(subject);
        mail.setFrom(ALODIGA_WALLET_ADDRESS);
        mail.setBody(body);
        ArrayList<String> recipients = new ArrayList<String>();
        recipients.add(emailUser);
        mail.setTo(recipients);
        return mail;
    }
    
        //Transferencia recibida
        public static Mail sendMailUserExchangeProduct(String idioma, String productSource, String productDestination, Float amountExchange, String userSource, String conceptTransaction, String emailUser)  {
        String hello = "Hola";
        String subject = "Alodiga Wallet: Convertir Producto:";
        String text1 = "Alodiga Wallet: Convertir Producto";
        String text2 = "Datos de su Conversi&oacute;n de Producto:";
        String product_Source = "Producto Origen: ";
        String product_Destination = "Producto Destino: ";
        String amount_Exchange = "Monto de Cambio: ";
        String user_Source = "Nombre";
        String concept_Transaction = "Concepto de la transacci&oacute;n:";
        String date = "Fecha: ";
        String moreInfo = "Para mayor informaci&oacute;n visiste";
        String mailInvite = "Le invitamos a seguir disfrutando los beneficios y de los atractivos productos y servicios que le ofrece Alodiga.";
        String thanks = "Gracias por preferirnos, Alodiga Mejora tu vida";
        String messageFooter1 = "Este mensaje ha sido enviado desde una cuenta de correo electr&oacute;nico exclusivamente de notificaciones que no admite mensajes. No responda esta comunicaci&oacute;n.";
        String allRights = "Todos los derechos reservados";
        if (idioma.equalsIgnoreCase("EN")) {
            hello = "Hello";
            subject = "Alodiga Wallet: Convert Product:";
            text1 = "Alodiga Wallet: Convert Product:";
            text2 = "Data of your Product Conversion: ";
            product_Source = "Product Source: ";
            concept_Transaction = "Concept Transaction: ";
            product_Destination = "Product Destination";
            amount_Exchange = "amount_Exchange";
            user_Source = "Name";
            date = "Date:";
            moreInfo = "Form more info visit";
            mailInvite = "We invite you to continue enjoying the benefits and attractive products and services offered by Alodiga.";
            thanks = "Thank you for choosing Alodiga, Alodiga Mejora tu vida";
            messageFooter1 = "This message was sent from an email distributor solely for notification messages that are not supported. Do not respond to this communication";
            allRights = "All rights reserved";
        }
        String style1 = "style='font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #666; font-size:13px;'";
        String style2 = "style='background-color: #07b49c;color:#ffff;font:16px/1.8em Arial,Helvetica,sans-serif,lighter;font-weight:bold;padding-left:10px'";
        String body = "";
        body = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>";
        body += "<html xmlns='http://www.w3.org/1999/xhtml'>";
        body += "<head>";
        body += "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/><style type='text/css'>.Estilo11 {font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #333333; font-size:13px; font-weight:bold;}</style><style type='text/css'>.Estilo12 {font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #666; font-size:13px;}</style><style type='text/css'>.EstiloColumn {background-color: #555555;color:#FFBF00;font:12px/1.8em Arial,Helvetica,sans-serif,lighter;font-weight:bold;padding-left:10px}</style>" + "<div align='center'>"
                + "<table width='756' height='600' border='0'>"
                + "<tr><th width='750' height='595'><p>"
                + "<img src='http://sales.alodiga.com/images/img-alodiga-logo.png' align='left' width='114' height='90' longdesc='Logo alodiga' />"
                + "</p><p>&nbsp;</p>" + "<p>&nbsp;</p>"
                + "<table  width='730' border='0' >"
                + "<tr><th width='728' height='20' align='right' bgcolor='#4c8e41' style='color:#FFFF;font:16px/1.8em Arial,Helvetica,sans-serif,lighter;'>" + text2 + "</th></tr>"
                + "<tr><th width='728' height='5' bgcolor='#232323'></th></tr>"
                + "</table>"
                + "<table width='728' border='0'>"
                + "<tr><th width='728'>"
                + "<p align='left' class='Estilo11'><br/><br/>&iexcl;"
                + hello + " "  + "&nbsp;"
                + "!<br/><br/>"
                + text1 + "<br><br></p>"
                + "</th>"
                + "</tr>"
                + "<tr>"
                + "<th><p align='left' style='font: 16px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; font-weight:bold; display: table;  margin: 0; padding:0;' >"
                + text2
                + "</p></th></tr>"
                + "<tr height='3px'><th width='728' bgcolor='#232323'></th></tr>"
                + "<tr>"
                + "<th>"
                + "<div><table width='728' border='0' cellpadding='2' cellspancing='0' style='border:inherit'>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + product_Source + "" + productSource +"</div></td>"
                + "</tr>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left' >"
                + product_Destination + "" + productDestination +"</div></td>"

                       
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + amount_Exchange + "" + amountExchange + "</div></td>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + concept_Transaction + "" + conceptTransaction + "</div></td>"
                                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + date + new Timestamp(new java.util.Date().getTime()) + "</div></td>"
                     
                + "<tr height='3px'><th width='728' bgcolor='#232323'></th></tr>"
                + "<tr height='40px'>"
                + "<th height='40px'><div class='Estilo11' align='left'>"
                + moreInfo
                + "<span style='font-size: 13px'> "
                + "<a href='https://www.alodiga.com/'>  www.alodiga.com</a></span></div>"
                + "</th>"
                + "</tr>"
                + "<tr>"
                + "<th height='31' bordercolor='#999999'><div align='center'><p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;' >"
                + thanks
                + "</p>"
                + " <p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;'>"
                + messageFooter1
                + "</p>"

                + "</div>"
                + "</th>"
                + "</tr>"
                + " </table>"
                + "<div align='center'>"
                + "<p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;'>&copy; Copyright 2013 - Alodiga, C.A. " + allRights + "<br> </div></th></tr></table></div></body></html>";

        Mail mail = new Mail(subject,body);
        mail.setSubject(subject);
        mail.setFrom(ALODIGA_WALLET_ADDRESS);
        mail.setBody(body);
        ArrayList<String> recipients = new ArrayList<String>();
        recipients.add(emailUser);
        mail.setTo(recipients);
        return mail;
    }

        //COMPRA DE SALDO
        public static Mail SendMailUserRecharge(String idioma, String referenceNumberOperation, Float amountRecharge, String conceptTransaction,String userSource ,String emailUser)  {
        String hello = "Hola";
        String subject = "Alodiga Wallet: Recarga de saldo.";
        String text1 = "Alodiga Wallet: Recarga de saldo.";
        String text2 = "Datos de su Recarga:";
        String referenceNumber = "N&uacute;mero de Referencia: ";
        String amount_Recharge = "Monto a Recargar: ";
        String date = "Fecha: ";
        String concept_Transaction = "Concepto de la transacci&oacute;n: ";
        String moreInfo = "Para mayor informaci&oacute;n visiste";
        String mailInvite = "Le invitamos a seguir disfrutando los beneficios y de los atractivos productos y servicios que le ofrece Alodiga.";
        String thanks = "Gracias por preferirnos, Alodiga Mejora tu vida";
        String messageFooter1 = "Este mensaje ha sido enviado desde una cuenta de correo electr&oacute;nico exclusivamente de notificaciones que no admite mensajes. No responda esta comunicaci&oacute;n.";
        String allRights = "Todos los derechos reservados";
        if (idioma.equalsIgnoreCase("EN")) {
            hello = "Hello";
            subject = "Alodiga Wallet: Balance Recharge";
            text1 = "Alodiga Wallet: Balance Recharge";
            text2 = "Recharge Data: ";
            referenceNumber = "Reference Number: ";
            amount_Recharge = "Amount Recharge: ";
            date = "Date:";
            concept_Transaction = "Concept Transaction: ";
            moreInfo = "Form more info visit";
            mailInvite = "We invite you to continue enjoying the benefits and attractive products and services offered by Alodiga.";
            thanks = "Thank you for choosing Alodiga, Alodiga Mejora tu vida";
            messageFooter1 = "This message was sent from an email distributor solely for notification messages that are not supported. Do not respond to this communication";
            allRights = "All rights reserved";
        }
        String style1 = "style='font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #666; font-size:13px;'";
        String style2 = "style='background-color: #07b49c;color:#ffff;font:16px/1.8em Arial,Helvetica,sans-serif,lighter;font-weight:bold;padding-left:10px'";
        String body = "";
        body = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>";
        body += "<html xmlns='http://www.w3.org/1999/xhtml'>";
        body += "<head>";
        body += "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/><style type='text/css'>.Estilo11 {font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #333333; font-size:13px; font-weight:bold;}</style><style type='text/css'>.Estilo12 {font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #666; font-size:13px;}</style><style type='text/css'>.EstiloColumn {background-color: #555555;color:#FFBF00;font:12px/1.8em Arial,Helvetica,sans-serif,lighter;font-weight:bold;padding-left:10px}</style>" + "<div align='center'>"
                + "<table width='756' height='600' border='0'>"
                + "<tr><th width='750' height='595'><p>"
                + "<img src='http://sales.alodiga.com/images/img-alodiga-logo.png' align='left' width='114' height='90' longdesc='Logo alodiga' />"
                + "</p><p>&nbsp;</p>" + "<p>&nbsp;</p>"
                + "<table  width='730' border='0' >"
                + "<tr><th width='728' height='20' align='right' bgcolor='#4c8e41' style='color:#FFFF;font:16px/1.8em Arial,Helvetica,sans-serif,lighter;'>" + text2 + "</th></tr>"
                + "<tr><th width='728' height='5' bgcolor='#232323'></th></tr>"
                + "</table>"
                + "<table width='728' border='0'>"
                + "<tr><th width='728'>"
                + "<p align='left' class='Estilo11'><br/><br/>&iexcl;"
                + hello + " "+ userSource + "&nbsp;"
                + "!<br/><br/>"
                + text1 + "<br><br></p>"
                + "</th>"
                + "</tr>"
                + "<tr>"
                + "<th><p align='left' style='font: 16px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; font-weight:bold; display: table;  margin: 0; padding:0;' >"
                + text2
                + "</p></th></tr>"
                + "<tr height='3px'><th width='728' bgcolor='#232323'></th></tr>"
                + "<tr>"
                + "<th>"
                + "<div><table width='728' border='0' cellpadding='2' cellspancing='0' style='border:inherit'>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + referenceNumber + "" + referenceNumberOperation +"</div></td>"
                + "</tr>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left' >"
                + amount_Recharge + "" + amountRecharge +"</div></td>"
            
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + concept_Transaction + "" + conceptTransaction + "</div></td>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + date + new Timestamp(new java.util.Date().getTime()) + "</div></td>"
                              
                + "<tr height='3px'><th width='728' bgcolor='#232323'></th></tr>"
                + "<tr height='40px'>"
                + "<th height='40px'><div class='Estilo11' align='left'>"
                + moreInfo
                + "<span style='font-size: 13px'> "
                + "<a href='https://www.alodiga.com/'>  www.alodiga.com</a></span></div>"
                + "</th>"
                + "</tr>"
                + "<tr>"
                + "<th height='31' bordercolor='#999999'><div align='center'><p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;' >"
                + thanks
                + "</p>"
                + " <p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;'>"
                + messageFooter1
                + "</p>"

                + "</div>"
                + "</th>"
                + "</tr>"
                + " </table>"
                + "<div align='center'>"
                + "<p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;'>&copy; Copyright 2013 - Alodiga, C.A. " + allRights + "<br> </div></th></tr></table></div></body></html>";

        Mail mail = new Mail(subject,body);
        mail.setSubject(subject);
        mail.setFrom(ALODIGA_WALLET_ADDRESS);
        mail.setBody(body);
        ArrayList<String> recipients = new ArrayList<String>();
        recipients.add(emailUser);
        mail.setTo(recipients);
        return mail;
    }
        
        
        //CAMBIO DE CONTRASEÑA
        public static Mail SendMailUserChangePassword(String idioma, Usuario usuario) {

        String hello = "Hola";
        String subject = "Alodiga Wallet: Cambio de clave.";
        String text1 = "Nos complace notificarle que su clave de acceso ha sido modificada.";
        String text2 = "Datos de su cuenta:";
        String text3 = "Cambio de clave.";
        String Name = "Nombre: ";
        String pass = "Nueva Clave: ";
        String date = "Fecha: ";
        String Email= "Email: ";
        String moreInfo = "Para acceder al sistema visite:";
        String thanks = "Gracias por preferirnos, Alodiga Mejora tu vida";
        String messageFooter1 = "Este mensaje ha sido enviado desde una cuenta de correo electr&oacute;nico exclusivamente de notificaciones que no admite mensajes. No responda esta comunicaci&oacute;n.";
        String allRights = "Todos los derechos reservados";
        if (idioma.equalsIgnoreCase("EN")) {
            hello = "Hello";
            subject = "Alodiga Wallet: Change of password.";
            text1 = "Alodiga Wallet: Change of password.";
            text2 = "Payment Details: ";
            text3 = "Change of password.";
            Name = "Account: ";
            date = "Date:";
            Email = "Email: ";
            moreInfo = "Form more info visit";
            thanks = "Thank you for choosing Alodiga, Alodiga Mejora tu vida";
            messageFooter1 = "This message was sent from an email distributor solely for notification messages that are not supported. Do not respond to this communication";
            allRights = "All rights reserved";
        }
        String style1 = "style='font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #666; font-size:13px;'";
        String style2 = "style='background-color: #07b49c;color:#ffff;font:16px/1.8em Arial,Helvetica,sans-serif,lighter;font-weight:bold;padding-left:10px'";
        String body = "";
        body = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>";
        body += "<html xmlns='http://www.w3.org/1999/xhtml'>";
        body += "<head>";
        body += "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/><style type='text/css'>.Estilo11 {font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #333333; font-size:13px; font-weight:bold;}</style><style type='text/css'>.Estilo12 {font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #666; font-size:13px;}</style><style type='text/css'>.EstiloColumn {background-color: #555555;color:#FFBF00;font:12px/1.8em Arial,Helvetica,sans-serif,lighter;font-weight:bold;padding-left:10px}</style>" + "<div align='center'>"
                + "<table width='756' height='600' border='0'>"
                + "<tr><th width='750' height='595'><p>"
                + "<img src='http://sales.alodiga.com/images/img-alodiga-logo.png' align='left' width='114' height='90' longdesc='Logo alodiga' />"
                + "</p><p>&nbsp;</p>" + "<p>&nbsp;</p>"
                + "<table  width='730' border='0' >"
                + "<tr><th width='728' height='20' align='right' bgcolor='#4c8e41' style='color:#FFFF;font:16px/1.8em Arial,Helvetica,sans-serif,lighter;'>" + text2 + "</th></tr>"
                + "<tr><th width='728' height='5' bgcolor='#232323'></th></tr>"
                + "</table>"
                + "<table width='728' border='0'>"
                + "<tr><th width='728'>"
                + "<p align='left' class='Estilo11'><br/><br/>&iexcl;"
                + hello + " " + usuario.getNombre() + "&nbsp;" + "!</p>"
                + "<p align='left' class='Estilo11'>"
                + text1 + "<br><br></p>"
                + "</th>"
                + "</tr>"
                + "<tr>"
                + "<th><p align='left' style='font: 16px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; font-weight:bold; display: table;  margin: 0; padding:0;' >"
                + text2
                + "</p></th></tr>"
                + "<tr height='3px'><th width='728' bgcolor='#232323'></th></tr>"
                + "<tr>"
                + "<th>"
                + "<div><table width='728' border='0' cellpadding='2' cellspancing='0' style='border:inherit'>"
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + Name + "" + usuario.getNombre()+ "</div></td>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + Email + "" + usuario.getEmail()+" </div></td>"
                + "</tr>"
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + date + new Timestamp(new java.util.Date().getTime()) + "</div></td>"
//              + "<tr height='3px'><th width='728' bgcolor='#232323'></th></tr>"
                + "<tr height='40px'>"
                + "<th height='40px'><div class='Estilo11' align='left'>"
                + moreInfo
                + "<span style='font-size: 13px'> "
                + "<a href='https://www.alodiga.com/'>  www.alodiga.com</a></span></div>"
                + "</th>"
                + "</tr>"
                + "<tr>"
                + "<th height='31' bordercolor='#999999'><div align='center'><p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;' >"
                + thanks
                + "</p>"
                + " <p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;'>"
                + messageFooter1
                + "</p>"

                + "</div>"
                + "</th>"
                + "</tr>"
                + " </table>"
                + "<div align='center'>"
                + "<p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;'>&copy; Copyright 2013 - Alodiga, C.A. " + allRights + "<br> </div></th></tr></table></div></body></html>";


        Mail mail = new Mail(subject,body);
        mail.setSubject(subject);
        mail.setFrom(ALODIGA_WALLET_ADDRESS);
        mail.setBody(body);
        ArrayList<String> recipients = new ArrayList<String>();
        recipients.add(usuario.getEmail());
        mail.setTo(recipients);
        return mail;
    }
        
        
        
        //RETIRO
        public static Mail SendMailUserWithdrawal(String idioma, String accountBank, Float amountWithdrawal, String conceptTransaction,String userSource ,String emailUser)  {
        String hello = "Hola";
        String subject = "Alodiga Wallet: Retiro de la billetera";
        String text1 = "Alodiga Wallet: Retiro de la billetera.";
        String text2 = "Datos de su Retiro:";
        String user_Source = "Nombre: ";
        String account_Bank = "Cuenta del Banco: ";
        String amount_Withdrawal = "Monto de Retiro: ";
        String date = "Fecha: ";
        String concept_Transaction = "Concepto de la Transacci&oacute;n: ";
        String moreInfo = "Para mayor informaci&oacute;n visiste";
        String mailInvite = "Le invitamos a seguir disfrutando los beneficios y de los atractivos productos y servicios que le ofrece Alodiga.";
        String thanks = "Gracias por preferirnos, Alodiga Mejora tu vida";
        String messageFooter1 = "Este mensaje ha sido enviado desde una cuenta de correo electr&oacute;nico exclusivamente de notificaciones que no admite mensajes. No responda esta comunicaci&oacute;n.";
        String allRights = "Todos los derechos reservados";
        if (idioma.equalsIgnoreCase("EN")) {
            hello = "Hello";
            subject = "Alodiga Wallet: Withdrawal of the wallet";
            text1 = "Alodiga Wallet: Withdrawal of the wallet";
            text2 = "Data of your Withdrawal: ";
            user_Source ="Name:";
            account_Bank = "Account Bank: ";
            amount_Withdrawal = "Amount Withdrawal: ";
            date = "Date:";
            concept_Transaction="Concept Transaction: ";
            moreInfo = "Form more info visit";
            mailInvite = "We invite you to continue enjoying the benefits and attractive products and services offered by Alodiga.";
            thanks = "Thank you for choosing Alodiga, Alodiga Mejora tu vida";
            messageFooter1 = "This message was sent from an email distributor solely for notification messages that are not supported. Do not respond to this communication";
            allRights = "All rights reserved";
        }
        String style1 = "style='font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #666; font-size:13px;'";
        String style2 = "style='background-color: #07b49c;color:#ffff;font:16px/1.8em Arial,Helvetica,sans-serif,lighter;font-weight:bold;padding-left:10px'";
        String body = "";
        body = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>";
        body += "<html xmlns='http://www.w3.org/1999/xhtml'>";
        body += "<head>";
        body += "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/><style type='text/css'>.Estilo11 {font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #333333; font-size:13px; font-weight:bold;}</style><style type='text/css'>.Estilo12 {font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #666; font-size:13px;}</style><style type='text/css'>.EstiloColumn {background-color: #555555;color:#FFBF00;font:12px/1.8em Arial,Helvetica,sans-serif,lighter;font-weight:bold;padding-left:10px}</style>" + "<div align='center'>"
                + "<table width='756' height='600' border='0'>"
                + "<tr><th width='750' height='595'><p>"
                + "<img src='http://sales.alodiga.com/images/img-alodiga-logo.png' align='left' width='114' height='90' longdesc='Logo alodiga' />"
                + "</p><p>&nbsp;</p>" + "<p>&nbsp;</p>"
                + "<table  width='730' border='0' >"
                + "<tr><th width='728' height='20' align='right' bgcolor='#4c8e41' style='color:#FFFF;font:16px/1.8em Arial,Helvetica,sans-serif,lighter;'>" + text2 + "</th></tr>"
                + "<tr><th width='728' height='5' bgcolor='#232323'></th></tr>"
                + "</table>"
                + "<table width='728' border='0'>"
                + "<tr><th width='728'>"
                + "<p align='left' class='Estilo11'><br/><br/>&iexcl;"
                + hello + " " + userSource + "&nbsp;"
                + "!<br/><br/>"
                + text1 + "<br><br></p>"
                + "</th>"
                + "</tr>"
                + "<tr>"
                + "<th><p align='left' style='font: 16px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; font-weight:bold; display: table;  margin: 0; padding:0;' >"
                + text2
                + "</p></th></tr>"
                + "<tr height='3px'><th width='728' bgcolor='#232323'></th></tr>"
                + "<tr>"
                + "<th>"
                + "<div><table width='728' border='0' cellpadding='2' cellspancing='0' style='border:inherit'>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + account_Bank + "" + accountBank +"</div></td>"
                + "</tr>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left' >"
                + amount_Withdrawal + "" + amountWithdrawal +"</div></td>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + concept_Transaction + "" + conceptTransaction + "</div></td>"

                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + date + new Timestamp(new java.util.Date().getTime()) + "</div></td>"
                
                    
                + "<tr height='3px'><th width='728' bgcolor='#232323'></th></tr>"
                + "<tr height='40px'>"
                + "<th height='40px'><div class='Estilo11' align='left'>"
                + moreInfo
                + "<span style='font-size: 13px'> "
                + "<a href='https://www.alodiga.com/'>  www.alodiga.com</a></span></div>"
                + "</th>"
                + "</tr>"
                + "<tr>"
                + "<th height='31' bordercolor='#999999'><div align='center'><p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;' >"
                + thanks
                + "</p>"
                + " <p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;'>"
                + messageFooter1
                + "</p>"

                + "</div>"
                + "</th>"
                + "</tr>"
                + " </table>"
                + "<div align='center'>"
                + "<p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;'>&copy; Copyright 2013 - Alodiga, C.A. " + allRights + "<br> </div></th></tr></table></div></body></html>";

        Mail mail = new Mail(subject,body);
        mail.setSubject(subject);
        mail.setFrom(ALODIGA_WALLET_ADDRESS);
        mail.setBody(body);
        ArrayList<String> recipients = new ArrayList<String>();
        recipients.add(emailUser);
        mail.setTo(recipients);
        return mail;
    }
        
        //PAGO A COMERCIO 
        public static Mail sendMailUserTopUp(String idioma, String destinationNumber,  String senderNumber, Float amountRecharge, Float amountPayment, String userSource, String emailUser)  {
        String hello = "Hola";
        String subject = "Alodiga Wallet: Compra de TopUp";
        String text1 = "Alodiga Wallet: Compra de TopUp";
        String text2 = "Datos de su compra:";
        String destination_Number = "N&uacute;mero de destino:";
        String sender_Number = "N&uacute;mero de env&iacute;o:";
        String amount_Recharge = "Monto de recarga:";
        String amount_Payment = "Monto de pago:";
        String user_Source = "Nombre:";
        String date = "Fecha: ";
        String usuario_Destination = "Nombre de Usuario de destino: ";
        String moreInfo = "Para mayor informaci&oacute;n visiste";
        String mailInvite = "Le invitamos a seguir disfrutando los beneficios y de los atractivos productos y servicios que le ofrece Alodiga.";
        String thanks = "Gracias por preferirnos, Alodiga Mejora tu vida";
        String messageFooter1 = "Este mensaje ha sido enviado desde una cuenta de correo electr&oacute;nico exclusivamente de notificaciones que no admite mensajes. No responda esta comunicaci&oacute;n.";
        String allRights = "Todos los derechos reservados";
        if (idioma.equalsIgnoreCase("EN")) {
            hello = "Hello";
            subject = "Alodiga Wallet: Balance purchase";
            text1 = "Alodiga Wallet: Balance purchase";
            text2 = "Payment Details: ";
            destination_Number = "Destination number:";
            sender_Number = "Sender number:";
            amount_Recharge = "Amount recharge:";
            amount_Payment = "Amount payment:";
            user_Source = "Name:";
            date = "Date:";
            moreInfo = "Form more info visit";
            mailInvite = "We invite you to continue enjoying the benefits and attractive products and services offered by Alodiga.";
            thanks = "Thank you for choosing Alodiga, Alodiga Mejora tu vida";
            messageFooter1 = "This message was sent from an email distributor solely for notification messages that are not supported. Do not respond to this communication";
            allRights = "All rights reserved";
        }
        String style1 = "style='font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #666; font-size:13px;'";
        String style2 = "style='background-color: #07b49c;color:#ffff;font:16px/1.8em Arial,Helvetica,sans-serif,lighter;font-weight:bold;padding-left:10px'";
        String body = "";
        body = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>";
        body += "<html xmlns='http://www.w3.org/1999/xhtml'>";
        body += "<head>";
        body += "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/><style type='text/css'>.Estilo11 {font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #333333; font-size:13px; font-weight:bold;}</style><style type='text/css'>.Estilo12 {font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #666; font-size:13px;}</style><style type='text/css'>.EstiloColumn {background-color: #555555;color:#FFBF00;font:12px/1.8em Arial,Helvetica,sans-serif,lighter;font-weight:bold;padding-left:10px}</style>" + "<div align='center'>"
                + "<table width='756' height='600' border='0'>"
                + "<tr><th width='750' height='595'><p>"
                + "<img src='http://sales.alodiga.com/images/img-alodiga-logo.png' align='left' width='114' height='90' longdesc='Logo alodiga' />"
                + "</p><p>&nbsp;</p>" + "<p>&nbsp;</p>"
                + "<table  width='730' border='0' >"
                + "<tr><th width='728' height='20' align='right' bgcolor='#4c8e41' style='color:#FFFF;font:16px/1.8em Arial,Helvetica,sans-serif,lighter;'>" + text2 + "</th></tr>"
                + "<tr><th width='728' height='5' bgcolor='#232323'></th></tr>"
                + "</table>"
                + "<table width='728' border='0'>"
                + "<tr><th width='728'>"
                + "<p align='left' class='Estilo11'><br/><br/>&iexcl;"
                + hello + " "  + user_Source + "&nbsp;"
                + "!<br/><br/>"
                + text1 + "<br><br></p>"
                + "</th>"
                + "</tr>"
                + "<tr>"
                + "<th><p align='left' style='font: 16px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; font-weight:bold; display: table;  margin: 0; padding:0;' >"
                + text2
                + "</p></th></tr>"
                + "<tr height='3px'><th width='728' bgcolor='#232323'></th></tr>"
                + "<tr>"
                + "<th>"
                + "<div><table width='728' border='0' cellpadding='2' cellspancing='0' style='border:inherit'>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + destination_Number + "" + destinationNumber +"</div></td>"
                + "</tr>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left' >"
                + sender_Number + "" + senderNumber +"</div></td>"

                       
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + amount_Recharge + "" + amountRecharge + "</div></td>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + amount_Payment + "" + amountPayment + "</div></td>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + date + new Timestamp(new java.util.Date().getTime()) + "</div></td>"
                
                + "<tr height='3px'><th width='728' bgcolor='#232323'></th></tr>"
                + "<tr height='40px'>"
                + "<th height='40px'><div class='Estilo11' align='left'>"
                + moreInfo
                + "<span style='font-size: 13px'> "
                + "<a href='https://www.alodiga.com/'>  www.alodiga.com</a></span></div>"
                + "</th>"
                + "</tr>"
                + "<tr>"
                + "<th height='31' bordercolor='#999999'><div align='center'><p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;' >"
                + thanks
                + "</p>"
                + " <p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;'>"
                + messageFooter1
                + "</p>"

                + "</div>"
                + "</th>"
                + "</tr>"
                + " </table>"
                + "<div align='center'>"
                + "<p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;'>&copy; Copyright 2013 - Alodiga, C.A. " + allRights + "<br> </div></th></tr></table></div></body></html>";

        Mail mail = new Mail(subject,body);
        mail.setSubject(subject);
        mail.setFrom(ALODIGA_WALLET_ADDRESS);
        mail.setBody(body);
        ArrayList<String> recipients = new ArrayList<String>();
        recipients.add(emailUser);
        mail.setTo(recipients);
        return mail;
    }
        
        
        
        public static String sendSmsSimbox(String text, String phoneNumber) {
        HttpsURLConnection connection = null;
        InputStream is = null;
        try {
            Authenticator.setDefault(new MyAuthenticator(Constants.USER_SIMBOX_1, Constants.PASSWORD_SIMBOX_1));
            java.net.URL url = new java.net.URL(Constants.URL_SIMBOX);
            
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod(Constants.TYPE_REQUEST_SIMBOX);
            connection.setRequestProperty(Constants.REQUEST_PROPERTIE_CONTENT_TYPE, Constants.REQUEST_PROPERTIE_CONTENT_TYPE_VALUE);
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setConnectTimeout(3000);
            
            //   REQUEST_PROPERTIE_CONTENT_TYPE_VALUE
            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(getRequestJsonDinstar(text, phoneNumber).toString());
            wr.close();
            //Get Response  
            try {
                is = connection.getInputStream();
            } catch (IOException ioe) {
                if (connection instanceof HttpURLConnection) {
                    HttpURLConnection httpConn = (HttpURLConnection) connection;
                    int statusCode = httpConn.getResponseCode();
                    System.out.println(httpConn.getResponseCode());
                    System.out.println(statusCode);
                    if (statusCode != 200) {
                        is = httpConn.getErrorStream();
                    }
                }
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            System.out.println(response.toString());
            return response.toString();            
        } catch (java.net.SocketTimeoutException e) {
            e.printStackTrace();
            return null;  
        }   catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }      
        
        
        
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    
    
    private static StringBuilder getRequestJsonDinstar(String text,String phoneNumber) {
            StringBuilder builderParam = new StringBuilder();
            builderParam.append("{\"text\":\"");
            builderParam.append(text);
            builderParam.append("\",\"param\":[{\"number\":\"");
            builderParam.append(phoneNumber);
            builderParam.append("\"}]}");
        return builderParam;
    }
   
    
    
    //Transferencia
        public static Mail sendMailUserTransferCardToCard(String idioma, Float amountPayment,  String conceptTransaction, String userDestinationName, String emailUser)  {
        String hello = "Hola";
        String subject = "Alodiga Wallet: Transferencia Tarjeta a Tarjeta:";
        String text1 = "Alodiga Wallet: Transferencia Tarjeta a Tarjeta";
        String text2 = "Datos de su Transferencia:";
        String amount_Payment = "Monto Transferido: ";
        String concept_Transaction = "Concepto de la transacci&oacute;n: ";
        String date = "Fecha: ";
        String usuario_Destination = "Nombre de Usuario de destino: ";
        String moreInfo = "Para mayor informaci&oacute;n visiste";
        String mailInvite = "Le invitamos a seguir disfrutando los beneficios y de los atractivos productos y servicios que le ofrece Alodiga.";
        String thanks = "Gracias por preferirnos, Alodiga Mejora tu vida";
        String messageFooter1 = "Este mensaje ha sido enviado desde una cuenta de correo electr&oacute;nico exclusivamente de notificaciones que no admite mensajes. No responda esta comunicaci&oacute;n.";
        String allRights = "Todos los derechos reservados";
        if (idioma.equalsIgnoreCase("EN")) {
            hello = "Hello";
            subject = "Alodiga Wallet: Transfer Card To Card:";
            text1 = "Alodiga Wallet: transfer Card To Card:";
            text2 = "Data of your transfer: ";
            amount_Payment = "Amount Transferred: ";
            concept_Transaction = "Concept Transaction: ";
            date = "Date:";
            usuario_Destination = "Usuario Destination: ";
            moreInfo = "Form more info visit";
            mailInvite = "We invite you to continue enjoying the benefits and attractive products and services offered by Alodiga.";
            thanks = "Thank you for choosing Alodiga, Alodiga Mejora tu vida";
            messageFooter1 = "This message was sent from an email distributor solely for notification messages that are not supported. Do not respond to this communication";
            allRights = "All rights reserved";
        }
        String style1 = "style='font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #666; font-size:13px;'";
        String style2 = "style='background-color: #07b49c;color:#ffff;font:16px/1.8em Arial,Helvetica,sans-serif,lighter;font-weight:bold;padding-left:10px'";
        String body = "";
        body = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>";
        body += "<html xmlns='http://www.w3.org/1999/xhtml'>";
        body += "<head>";
        body += "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/><style type='text/css'>.Estilo11 {font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #333333; font-size:13px; font-weight:bold;}</style><style type='text/css'>.Estilo12 {font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #666; font-size:13px;}</style><style type='text/css'>.EstiloColumn {background-color: #555555;color:#FFBF00;font:12px/1.8em Arial,Helvetica,sans-serif,lighter;font-weight:bold;padding-left:10px}</style>" + "<div align='center'>"
                + "<table width='756' height='600' border='0'>"
                + "<tr><th width='750' height='595'><p>"
                + "<img src='http://sales.alodiga.com/images/img-alodiga-logo.png' align='left' width='114' height='90' longdesc='Logo alodiga' />"
                + "</p><p>&nbsp;</p>" + "<p>&nbsp;</p>"
                + "<table  width='730' border='0' >"
                + "<tr><th width='728' height='20' align='right' bgcolor='#4c8e41' style='color:#FFFF;font:16px/1.8em Arial,Helvetica,sans-serif,lighter;'>" + text2 + "</th></tr>"
                + "<tr><th width='728' height='5' bgcolor='#232323'></th></tr>"
                + "</table>"
                + "<table width='728' border='0'>"
                + "<tr><th width='728'>"
                + "<p align='left' class='Estilo11'><br/><br/>&iexcl;"
                + hello + " "  + "&nbsp;"
                + "!<br/><br/>"
                + text1 + "<br><br></p>"
                + "</th>"
                + "</tr>"
                + "<tr>"
                + "<th><p align='left' style='font: 16px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; font-weight:bold; display: table;  margin: 0; padding:0;' >"
                + text2
                + "</p></th></tr>"
                + "<tr height='3px'><th width='728' bgcolor='#232323'></th></tr>"
                + "<tr>"
                + "<th>"
                + "<div><table width='728' border='0' cellpadding='2' cellspancing='0' style='border:inherit'>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + amount_Payment + "" + amountPayment +"</div></td>"
                + "</tr>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left' >"
                + concept_Transaction + "" + conceptTransaction +"</div></td>"

                       
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + usuario_Destination + "" + userDestinationName + "</div></td>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + date + new Timestamp(new java.util.Date().getTime()) + "</div></td>"
                
               
                
                + "<tr height='3px'><th width='728' bgcolor='#232323'></th></tr>"
                + "<tr height='40px'>"
                + "<th height='40px'><div class='Estilo11' align='left'>"
                + moreInfo
                + "<span style='font-size: 13px'> "
                + "<a href='https://www.alodiga.com/'>  www.alodiga.com</a></span></div>"
                + "</th>"
                + "</tr>"
                + "<tr>"
                + "<th height='31' bordercolor='#999999'><div align='center'><p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;' >"
                + thanks
                + "</p>"
                + " <p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;'>"
                + messageFooter1
                + "</p>"

                + "</div>"
                + "</th>"
                + "</tr>"
                + " </table>"
                + "<div align='center'>"
                + "<p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;'>&copy; Copyright 2013 - Alodiga, C.A. " + allRights + "<br> </div></th></tr></table></div></body></html>";

        Mail mail = new Mail(subject,body);
        mail.setSubject(subject);
        mail.setFrom(ALODIGA_WALLET_ADDRESS);
        mail.setBody(body);
        ArrayList<String> recipients = new ArrayList<String>();
        recipients.add(emailUser);
        mail.setTo(recipients);
        return mail;
    }
    
    //Transferencia recibida
        public static Mail sendMailUserTransferCardToCardReciber(String idioma, Float amountPayment,  String conceptTransaction, String userDestinationName, String emailUser)  {
        String hello = "Hola";
        String subject = "Alodiga Wallet: Transferencia Tarjeta a Tarjeta:";
        String text1 = "Alodiga Wallet: Transferencia Tarjeta a Tarjeta";
        String text2 = "Datos de su Transferencia Recibida:";
        String amount_Payment = "Monto Recibido: ";
        String concept_Transaction = "Concepto de la transacci&oacute;n: ";
        String date = "Fecha: ";
        String usuario_Destination = "Nombre de Usuario de destino: ";
        String moreInfo = "Para mayor informaci&oacute;n visiste";
        String mailInvite = "Le invitamos a seguir disfrutando los beneficios y de los atractivos productos y servicios que le ofrece Alodiga.";
        String thanks = "Gracias por preferirnos, Alodiga Mejora tu vida";
        String messageFooter1 = "Este mensaje ha sido enviado desde una cuenta de correo electr&oacute;nico exclusivamente de notificaciones que no admite mensajes. No responda esta comunicaci&oacute;n.";
        String allRights = "Todos los derechos reservados";
        if (idioma.equalsIgnoreCase("EN")) {
            hello = "Hello";
            subject = "Alodiga Wallet: Transfer Card to Card:";
            text1 = "Alodiga Wallet: transfer Card to Card:";
            text2 = "Data of your Transfer Received: ";
            amount_Payment = "Amount received: ";
            concept_Transaction = "Concept Transaction: ";
            date = "Date:";
            usuario_Destination = "Usuario Destination: ";
            moreInfo = "Form more info visit";
            mailInvite = "We invite you to continue enjoying the benefits and attractive products and services offered by Alodiga.";
            thanks = "Thank you for choosing Alodiga, Alodiga Mejora tu vida";
            messageFooter1 = "This message was sent from an email distributor solely for notification messages that are not supported. Do not respond to this communication";
            allRights = "All rights reserved";
        }
        String style1 = "style='font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #666; font-size:13px;'";
        String style2 = "style='background-color: #07b49c;color:#ffff;font:16px/1.8em Arial,Helvetica,sans-serif,lighter;font-weight:bold;padding-left:10px'";
        String body = "";
        body = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>";
        body += "<html xmlns='http://www.w3.org/1999/xhtml'>";
        body += "<head>";
        body += "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/><style type='text/css'>.Estilo11 {font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #333333; font-size:13px; font-weight:bold;}</style><style type='text/css'>.Estilo12 {font:13px/0.6em Arial,Helvetica,sans-serif,lighter; color: #666; font-size:13px;}</style><style type='text/css'>.EstiloColumn {background-color: #555555;color:#FFBF00;font:12px/1.8em Arial,Helvetica,sans-serif,lighter;font-weight:bold;padding-left:10px}</style>" + "<div align='center'>"
                + "<table width='756' height='600' border='0'>"
                + "<tr><th width='750' height='595'><p>"
                + "<img src='http://sales.alodiga.com/images/img-alodiga-logo.png' align='left' width='114' height='90' longdesc='Logo alodiga' />"
                + "</p><p>&nbsp;</p>" + "<p>&nbsp;</p>"
                + "<table  width='730' border='0' >"
                + "<tr><th width='728' height='20' align='right' bgcolor='#4c8e41' style='color:#FFFF;font:16px/1.8em Arial,Helvetica,sans-serif,lighter;'>" + text2 + "</th></tr>"
                + "<tr><th width='728' height='5' bgcolor='#232323'></th></tr>"
                + "</table>"
                + "<table width='728' border='0'>"
                + "<tr><th width='728'>"
                + "<p align='left' class='Estilo11'><br/><br/>&iexcl;"
                + hello + " "  + "&nbsp;"
                + "!<br/><br/>"
                + text1 + "<br><br></p>"
                + "</th>"
                + "</tr>"
                + "<tr>"
                + "<th><p align='left' style='font: 16px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; font-weight:bold; display: table;  margin: 0; padding:0;' >"
                + text2
                + "</p></th></tr>"
                + "<tr height='3px'><th width='728' bgcolor='#232323'></th></tr>"
                + "<tr>"
                + "<th>"
                + "<div><table width='728' border='0' cellpadding='2' cellspancing='0' style='border:inherit'>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + amount_Payment + "" + amountPayment +"</div></td>"
                + "</tr>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left' >"
                + concept_Transaction + "" + conceptTransaction +"</div></td>"

                       
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + usuario_Destination + "" + userDestinationName + "</div></td>"
                
                + "<tr height='30px'><td " + style2 + " width='305'><div align='left'>"
                + date + new Timestamp(new java.util.Date().getTime()) + "</div></td>"
                
               
                
                + "<tr height='3px'><th width='728' bgcolor='#232323'></th></tr>"
                + "<tr height='40px'>"
                + "<th height='40px'><div class='Estilo11' align='left'>"
                + moreInfo
                + "<span style='font-size: 13px'> "
                + "<a href='https://www.alodiga.com/'>  www.alodiga.com</a></span></div>"
                + "</th>"
                + "</tr>"
                + "<tr>"
                + "<th height='31' bordercolor='#999999'><div align='center'><p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;' >"
                + thanks
                + "</p>"
                + " <p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;'>"
                + messageFooter1
                + "</p>"

                + "</div>"
                + "</th>"
                + "</tr>"
                + " </table>"
                + "<div align='center'>"
                + "<p align='center' style='font: 10px/1.8em Arial,Helvetica,sans-serif,lighter ; color: #666; display: table;  margin: 0; padding:0;'>&copy; Copyright 2013 - Alodiga, C.A. " + allRights + "<br> </div></th></tr></table></div></body></html>";

        Mail mail = new Mail(subject,body);
        mail.setSubject(subject);
        mail.setFrom(ALODIGA_WALLET_ADDRESS);
        mail.setBody(body);
        ArrayList<String> recipients = new ArrayList<String>();
        recipients.add(emailUser);
        mail.setTo(recipients);
        return mail;
    }
}
