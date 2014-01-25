package com.dexter.navlg.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailNotifier
{
	/**
     * Sends an email to the specified email address.
     *
     * @param from The from email address.
     * @param to The recipient of the email.
     * @param subject The subject of the email.
     * @param body The message content of the email.
     * */
    public static boolean sendEmail(String from, String to, String subject,
            String body)
    {
        boolean ret = false;
        
        EmailNotifier e = new EmailNotifier();
        
        try
        {
            Properties p = new Properties();
            p.put("mail.smtp.host", "smtp.gmail.com"); // pod51014.outlook.com
            p.put("mail.smtp.port", "587"); // 587
            p.put("mail.smtp.auth", "true");
            p.put("mail.smtp.starttls.enable", "true");
            
            String smtp_username = "dele.olaore@gmail.com"; // oladele.olaore@vanso.com
            String smtp_password = "Olax1010";
            
            Authenticator auth = e.new SMTPAuthenticator(smtp_username, smtp_password);
            Session session = Session.getDefaultInstance(p, auth); // 
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            InternetAddress to_address[] = new InternetAddress[1];
            to_address[0] = new InternetAddress(to);
            message.setRecipients(Message.RecipientType.TO, to_address);
            message.setSubject(subject);
            message.setContent(body, "text/html");
            message.setSentDate(new java.util.Date());
            Transport.send(message);
            
            ret = false;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            
            String exStr = ex.getMessage();
            try
            {
                exStr = exStr.substring(exStr.lastIndexOf("]")+1);
            }
            catch(Exception ignore){}
            
            ret = true; //"Error: Could not send the email, Message: " + exStr;
        }
        finally
        {
            //nothing to dispose
        }
        
        return ret;
    }
    
    /**
     * SimpleAuthenticator is used to do simple authentication
     * when the SMTP server requires it.
     */
    private class SMTPAuthenticator extends Authenticator
    {
        private String SMTP_AUTH_USER;
        private String SMTP_AUTH_PWD;
        
        public SMTPAuthenticator(String SMTP_AUTH_USER, String SMTP_AUTH_PWD)
        {
            this.SMTP_AUTH_USER = SMTP_AUTH_USER;
            this.SMTP_AUTH_PWD = SMTP_AUTH_PWD;
        }
        
        public PasswordAuthentication getPasswordAuthentication()
        {
            String username = SMTP_AUTH_USER;
            String password = SMTP_AUTH_PWD;
            return new PasswordAuthentication(username, password);
        }
    }
}
