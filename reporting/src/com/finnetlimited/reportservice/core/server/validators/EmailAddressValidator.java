package com.finnetlimited.reportservice.core.server.validators;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Hashtable;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: nodir
 * Date: 12.06.2010
 * Time: 19:28:27
 * To change this template use File | Settings | File Templates.
 */
public class EmailAddressValidator {

     public static final String EMAIL_INVALID = "Email address is invalid";
     public static final String EMAIL_VALID = "<span class=valid>Email address is valid</span>";
     public static final String EMAIL_EXSIST = "Email address already exists in database";
     public static final String EMAIL_SUCH_NOT_EXSIST = "Email address does not exsist";
     public static final String EMAIL_REQUIRED = "Enter Your email address";

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^([A-Za-z0-9-_\\&*\\=+\\?\\'\\^{}\\~]+([.]{1})?){0,}" +
            "[A-Za-z0-9-_\\&*\\=+\\?\\'\\^{}\\~]{1}[\\@]{1}(([A-Za-z0-9]+[-.]?){0,}[A-Za-z0-9]{1}){1,}[.]{1}[A-Za-z]{2,7}$");

    public static String getHost(String email) {
        int index;
        if (email != null && !"".equals(email) && (index = email.indexOf("@")) > 0)
            return email.substring(index + 1);
        return null;
    }

    public static boolean checkEmail(String email) {
        if (getHost(email) != null)
            return EMAIL_PATTERN.matcher(email).matches();
        return false;
    }

    public static boolean checkHost(String email) {
          return checkHostMX(email) != null;
    }

    // Check whether the given host exists, using MX query to DNS server.
    private static String checkHostMX(String email) {
        String mxServer = null;
        if (!checkEmail(email))
            return null;

        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            DirContext ictx = new InitialDirContext(env);
            Attributes attrs = ictx.getAttributes( getHost(email), new String[] { "MX" });
            Attribute attr = attrs.get("MX");
            if (attr != null && attr.size() > 0) {
                mxServer = attr.get().toString().split(" ")[1];
                return mxServer;
            }

        } catch (Exception e) {
//            e.printStackTrace();
        }

        return null;
    }

   // Checks the email address existence by sending SMTP commands to MX server.
   // For some hosts it would not work due to spamming concerns
    public static boolean checkEmailExistense(String email) {
        String mxServer = checkHostMX(email);//getting MX address for given host
        if (mxServer == null)
            return false;

        Socket smtpSocket = null;
        DataOutputStream os = null;
        DataInputStream is = null;

        try {// Open port to server
            smtpSocket = new Socket(mxServer, 25);
            smtpSocket.setSoTimeout(14000);
            os = new DataOutputStream(smtpSocket.getOutputStream());
            is = new DataInputStream(smtpSocket.getInputStream());

            if (smtpSocket != null && os != null && is != null) { // Connection was made.  Socket is ready for use.
                os.writeBytes("HELLO workforcetrack.com\r\n");
                is.readLine();
                is.readLine();
                os.writeBytes("MAIL From: <mailer@workforcetracksupport.com>\r\n");
                is.readLine();
                // Who's email we are going to check
                os.writeBytes("RCPT To: <"+ email +">\r\n");

                // If we get answer with 550 code, email is invalid.
			    // return false only and only if we get 550 or 511.
                String responseline;
                while ((responseline = is.readLine()) != null) {
                    System.out.println(responseline);
                    if (responseline.startsWith("550") || responseline.startsWith("511")) {
                        System.out.println(email +" is invalid");
                        smtpSocket.close();
                        return false;
                    }
                }
                smtpSocket.close();
            }

        } catch(Exception e){
//            e.printStackTrace();
        }

        return true;
    }
}
