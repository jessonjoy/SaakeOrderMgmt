package com.saake.invoicer.util;

import com.saake.invoicer.reports.ByteArrayDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.activation.DataSource;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailServices {
    protected static final Log log = LogFactory.getLog(MailServices.class);
    private static final String mailer = "JavaMailer";
    protected String jndiLookup;

    public MailServices(String jndiLookup) {
        this.jndiLookup = jndiLookup;
    }

    public void sendEmail(InternetAddress from, InternetAddress[] recTO,
                          InternetAddress[] recCC, String subject,
                          String message, String appStage,
                          String[] fileNames, String[] putFileNames) throws Exception {
        // we are going to override the application stage from mail.application.stage
//        appStage = ElmDBConfig.getProperty("application.stage");
        
        try {
            if (recTO == null || recTO.length == 0) {
                throw new Exception("Cannot Send Email as no \"TO\" recipient(s) found");
            }

            Context initial = new InitialContext();
            Session session =
                    (Session) initial.lookup(this.jndiLookup);
            log.debug("Found mail session " + session);

            Message msg = new MimeMessage(session);

            msg.setFrom(from);
            msg.setRecipients(Message.RecipientType.TO, recTO);
            msg.setRecipients(Message.RecipientType.CC, recCC);
            msg.setSubject(subject);


            msg.setHeader("X-Mailer", mailer);
            msg.setSentDate(new Date(System.currentTimeMillis()));

            if ((fileNames != null) && (fileNames.length > 0)) {
                Multipart mp = new MimeMultipart();
                MimeBodyPart mbpMessage = new MimeBodyPart();
                mbpMessage.setDataHandler(new DataHandler(message, "text/html"));
                mp.addBodyPart(mbpMessage);
                for (int i = 0; i < fileNames.length; i++) {
                    // mbpMessage.setText(message);
                    String fileName = fileNames[i];
                    String putFileName = null;
                    if ( putFileNames != null && putFileNames.length > 0){
                        putFileName = putFileNames[i];
                    }

                    if (fileName != null && !fileName.trim().equals("")) {
                        MimeBodyPart mbpFile = new MimeBodyPart();
                        // attach the file to the message
                        FileDataSource fds = new FileDataSource(fileName);
                        mbpFile.setDataHandler(new DataHandler(fds));
                        if ((putFileName != null) && (!putFileName.trim().equals(""))) {
                            mbpFile.setFileName(putFileName);
                        } else {
                            mbpFile.setFileName(getFileName(fileName));
                        }
                        mp.addBodyPart(mbpFile);
                    }
                }

                msg.setContent(mp);
            } else {
                msg.setDataHandler(new DataHandler(message, "text/html"));
            }


//            Multipart multipart = new MimeMultipart("related");
//            BodyPart messageBodyPart = new MimeBodyPart();
//
//            String htmlText = "<p style=\"border: 1px solid gray\"> <img src=\"cid:connectlogo\">";
//            String enhancedMsg = htmlText+message + "</p>";
//            messageBodyPart.setContent(enhancedMsg, "text/html");
//            multipart.addBodyPart(messageBodyPart);
//
//            messageBodyPart = new MimeBodyPart();
//            FileDataSource fds1 = new FileDataSource ("C:\\elm\\connectWeb\\images\\connect_logo.png");
//            messageBodyPart.setDataHandler(new DataHandler(fds1));
//            messageBodyPart.addHeader("Content-ID","<connectlogo>");
//            multipart.addBodyPart(messageBodyPart);
//
//            msg.setContent(multipart);

            try {
                Transport.send(msg);
            } catch (SendFailedException e) {
                try {
                    Transport.send(msg, e.getValidUnsentAddresses());

                } catch (SendFailedException e2) {
                    log.error("There was an error trying to send email in the second block:", e);
                    throw new SendMailAddressException("Following error addresses had problem", e2.getValidUnsentAddresses(), combineArrays(e.getInvalidAddresses(), e2.getInvalidAddresses()), combineArrays(e.getValidSentAddresses(), e2.getValidSentAddresses()));
                }

                log.error("The following was the send mail error:", e);
                throw new SendMailAddressException("Following error addresses had problem", null, e.getInvalidAddresses(), combineArrays(e.getValidSentAddresses(), e.getValidUnsentAddresses()));
            }

            log.debug("Mail sent");
        } catch (NamingException e) {
            if (log.isDebugEnabled())
                log.error("", e);
            log.error("The naming context was not found:" + e.getMessage());
            throw new Exception("The naming context was not found:" + e.getMessage());
        }
    }

    //Used in CMHandler and creditreviewform handler
    public void sendEmail(String fromAddress, String toAddressstr, String subject, String message, List<ByteArrayDataSource> imgDSList) throws Exception {
        try {
                // From address
            InternetAddress fromAddr = new InternetAddress(fromAddress);

            // To address
            InternetAddress[] toAddress = {new InternetAddress(toAddressstr)};
                        
            Context initial = new InitialContext();
            Session session =
                    (Session) initial.lookup(this.jndiLookup);
            log.debug("Found mail session " + session);

            Message msg = new MimeMessage(session);

            msg.setFrom(fromAddr);
            msg.setRecipients(Message.RecipientType.TO, toAddress);
//            msg.setRecipients(Message.RecipientType.CC, recCC);
            msg.setSubject(subject);

            msg.setHeader("X-Mailer", mailer);
            msg.setSentDate(new Date(System.currentTimeMillis()));

            // This HTML mail have to 2 part, the BODY and the embedded image
            MimeMultipart multipart = new MimeMultipart("related");

            // first part  (the html)
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(message, "text/html");
            // add it
            multipart.addBodyPart(bodyPart);
            
            for(ByteArrayDataSource bads: imgDSList){
                // second part (the image)
                bodyPart = new MimeBodyPart();
                String strName = bads.getName();
                bodyPart.setFileName(strName);
                bodyPart.setContentID(strName);
                bodyPart.setDisposition(javax.mail.Part.ATTACHMENT);
                bodyPart.setDataHandler(new DataHandler(bads));
                bodyPart.setHeader("Content-ID","<"+ strName +">");
                
                multipart.addBodyPart(bodyPart);
            }
           
            // put everything together
            msg.setContent(multipart);

//            if ((fileNames != null) && (fileNames.length > 0)) {
//                Multipart mp = new MimeMultipart();
//                MimeBodyPart mbpMessage = new MimeBodyPart();
//                mbpMessage.setDataHandler(new DataHandler(message, "text/html"));
//                mp.addBodyPart(mbpMessage);
//                for (int i = 0; i < fileNames.length; i++) {
//                    // mbpMessage.setText(message);
//                    String fileName = fileNames[i];
//                    String putFileName = null;
//                    if ( putFileNames != null && putFileNames.length > 0){
//                        putFileName = putFileNames[i];
//                    }
//
//                    if (fileName != null && !fileName.trim().equals("")) {
//                        MimeBodyPart mbpFile = new MimeBodyPart();
//                        // attach the file to the message
//                        FileDataSource fds = new FileDataSource(fileName);
//                        mbpFile.setDataHandler(new DataHandler(fds));
//                        if ((putFileName != null) && (!putFileName.trim().equals(""))) {
//                            mbpFile.setFileName(putFileName);
//                        } else {
//                            mbpFile.setFileName(getFileName(fileName));
//                        }
//                        mp.addBodyPart(mbpFile);
//                    }
//                }
//
//                msg.setContent(mp);
//            } else {
//                msg.setDataHandler(new DataHandler(message, "text/html"));
//            }


//            Multipart multipart = new MimeMultipart("related");
//            BodyPart messageBodyPart = new MimeBodyPart();
//
//            String htmlText = "<p style=\"border: 1px solid gray\"> <img src=\"cid:connectlogo\">";
//            String enhancedMsg = htmlText+message + "</p>";
//            messageBodyPart.setContent(enhancedMsg, "text/html");
//            multipart.addBodyPart(messageBodyPart);
//
//            messageBodyPart = new MimeBodyPart();
//            FileDataSource fds1 = new FileDataSource ("C:\\elm\\connectWeb\\images\\connect_logo.png");
//            messageBodyPart.setDataHandler(new DataHandler(fds1));
//            messageBodyPart.addHeader("Content-ID","<connectlogo>");
//            multipart.addBodyPart(messageBodyPart);
//
//            msg.setContent(multipart);

            try {
                Transport.send(msg);
            } catch (SendFailedException e) {
                try {
                    Transport.send(msg, e.getValidUnsentAddresses());

                } catch (SendFailedException e2) {
                    log.error("There was an error trying to send email in the second block:", e);
                    throw new SendMailAddressException("Following error addresses had problem", e2.getValidUnsentAddresses(), combineArrays(e.getInvalidAddresses(), e2.getInvalidAddresses()), combineArrays(e.getValidSentAddresses(), e2.getValidSentAddresses()));
                }

                log.error("The following was the send mail error:", e);
                throw new SendMailAddressException("Following error addresses had problem", null, e.getInvalidAddresses(), combineArrays(e.getValidSentAddresses(), e.getValidUnsentAddresses()));
            }

            log.debug("Mail sent");
        } catch (NamingException e) {
            if (log.isDebugEnabled())
                log.error("", e);
            log.error("The naming context was not found:" + e.getMessage());
            throw new Exception("The naming context was not found:" + e.getMessage());
        }
    }

    //Used in notification email deliverer
    public void sendEmail(String fromAddress, Collection<String> userEmailList,
                          Collection<String> ccList, String subject, String message,
                          String[] fileName, String[] attachFileName) throws Exception {

        // From address
        InternetAddress fromAddr = new InternetAddress(fromAddress);

        // To address
        InternetAddress[] toAddress = new InternetAddress[userEmailList.size()];
        int i = 0;
        for (String email : userEmailList) {

            if(Utils.notBlank(email)){
                toAddress[i++] = new InternetAddress(email);
            }
        }

        // In Production this property can be blank
//        String env = ElmDBConfig.getProperty("environment.name");
        String env =  "";
        
        subject = env + subject;

        InternetAddress[] ccAddress = null;

        if ( ccList !=null && ccList.size() > 0){
            ccAddress = new InternetAddress[ccList.size()];

            int j = 0;
            for (String ccAdd : ccList) {
               ccAddress[j++] = new InternetAddress(ccAdd);
            }
        }

        if ( fileName == null || fileName.length < 1){
            fileName = new String[1];
        }

//        fileName[0] = "C:\\elm\\connectWeb\\images\\connect_logo.png";

        if ( attachFileName == null || attachFileName.length < 1){
            attachFileName = new String[0];
        }

        sendEmail(fromAddr, toAddress, ccAddress, subject, message, null, fileName, attachFileName);
    }

    private void sendEmail(String fromAddress, Collection<String> userEmailList, String subject, String message,
                          String[] fileName, String[] attachFileName) throws Exception {

        // From address
        InternetAddress fromAddr = new InternetAddress(fromAddress);

        // To address
        InternetAddress[] toAddress = new InternetAddress[userEmailList.size()];
        int i = 0;
        for (String email : userEmailList) {
            toAddress[i++] = new InternetAddress(email);
        }

        // In Production this property can be blank
//        String env = ElmDBConfig.getProperty("environment.name");
        String env = "";
        
        subject = env + subject;

        sendEmail(fromAddr, toAddress, null, subject, message, null, fileName, attachFileName);
    }

    private String getFileName(String fileName) {
        String fn;
        int indx = fileName.indexOf("/");
        int indx2 = fileName.indexOf("\\");
        if (indx > -1) {
            fn = fileName.substring(indx + 1, fileName.length());
            indx = fn.indexOf("/");
            while (indx > -1) {
                fn = fn.substring(indx + 1, fn.length());
                indx = fn.indexOf("/");
            }
        } else if (indx2 > -1) {
            fn = fileName.substring(indx + 1, fileName.length());
            indx = fn.indexOf("\\");
            while (indx > -1) {
                fn = fn.substring(indx + 1, fn.length());
                indx = fn.indexOf("\\");
            }
        } else {
            fn = fileName;
        }
        return fn;
    }

    private Address[] combineArrays(Address[] arr1, Address[] arr2) {
        Address[] arr3 = null;
        if ((arr1 != null) && (arr2 != null)) {
            arr3 = new Address[arr1.length + arr2.length];
            System.arraycopy(arr1, 0, arr3, 0, arr1.length);
            System.arraycopy(arr2, 0, arr3, arr1.length, arr2.length);
        } else if (arr1 != null) {
            arr3 = arr1;
        } else if (arr2 != null) {
            arr3 = arr2;
        }
        return arr3;
    }
}

