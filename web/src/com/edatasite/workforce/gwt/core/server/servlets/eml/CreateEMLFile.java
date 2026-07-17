package com.edatasite.workforce.gwt.core.server.servlets.eml;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * User: Ilhombek
 * Date: 16.09.2010
 * Time: 12:54:19
 */
public class CreateEMLFile {

    private static EML eml;
    private static java.util.List<EML> emlList;
    private static boolean isCreateDirectory;
    public static File directoryFile;
    public static String folderName;

    public CreateEMLFile(EML eml, String folderName) {
        this.eml = eml;
        this.folderName = folderName;
        draw();
    }

    public CreateEMLFile(java.util.List<EML> emlList, String folderName) {
        this.emlList = emlList;
        this.folderName = folderName;
        draw();
    }

    public static void createEMLFile(EML emll) {
        eml = emll;
        draw();
    }

    private static void draw() {
        try {
            directoryFile = new File(EML.getEMLFileDirectory() + folderName);
//            if (directoryFile.exists()) {
//                directoryFile = new File(EML._DEFAULT_FOLDER + folderName + "1");
//            }
            isCreateDirectory = directoryFile.mkdirs();
            System.out.println("Directory " + directoryFile.getName() + " created");

            if (eml != null) {
                generate(eml);
            } else {
                for (EML emlL : emlList) {
                    generate(emlL);
                }
            }
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
    }

    private static void generate(EML emlL) throws MessagingException, IOException {
        eml = emlL;

        //Get System properties
        Properties properties = System.getProperties();

        Session session = Session.getInstance(properties);

        MimeMessage message = new MimeMessage(session);

        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setText(eml.get_HTMLBody(), "utf-8");
        bodyPart.setHeader("Content-Type", "text/html");

        Multipart mp = new MimeMultipart();
        mp.addBodyPart(bodyPart);

        message.setContent(mp);

        createEMLFile(message);
    }

    private static void createEMLFile(MimeMessage message) throws IOException, MessagingException {

        String fileName = eml.get_FileName();
        String from = eml.get_From();
        String to = eml.get_To();


        File eFile = new File(directoryFile.getPath() + "/" + fileName + ".eml");
        eFile.createNewFile();


        System.out.println("-------------------------------------------------");
        System.out.println("-------------------------------------------------");
        System.out.println(message.getContent());
        System.out.println("-------------------------------------------------");
        System.out.println(eml.get_FileName());
        System.out.println(eml.get_Subject());
        System.out.println(eml.get_From());
        System.out.println(eml.get_To());
        System.out.println(eml.get_Content_Type());
        System.out.println(eml.get_HTMLBody());

        System.out.println(eml.get_Category_Id());
        System.out.println(eml.get_Company_Id());
        System.out.println(eml.get_Template_Name());
        System.out.println(eml.is_Is_Default());

        System.out.println("-------------------------------------------------");
        System.out.println("-------------------------------------------------");

        message.setFrom(new InternetAddress(from));
        message.setSubject(eml.get_Subject(), "utf-8");
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

        message.addHeader(EML._TEMPLATE_NAME, eml.get_Template_Name());
        message.addHeader(EML._CATEGORY_ID, eml.get_Category_Id());
        message.addHeader(EML._CATEGORY_NAME, eml.get_Category_Name());
        message.addHeader(EML._COMPANY_ID, eml.get_Company_Id());
        message.addHeader(EML._IS_DEFAULT, eml.is_Is_Default());
        message.addHeader(EML._FROM_USER_ID, eml.get_From_User_Id());
        message.addHeader(EML._FROM_USER_NAME, eml.get_From_User_Name());

        FileOutputStream ff = new FileOutputStream(eFile);
        message.writeTo(ff);
        ff.close();

        // do something with the EML file
//            Desktop.getDesktop().open(eFile);
    }


    public static void main(String args[]) {

        EML email = new EML();

        String css = "table#tscr {" +
                "	border: 1px dashed;" +
                "	margin: 8px;" +
                "}" +
                "table#tscr td {" +
                "	font-family: Verdana;" +
                "	font-size: 9pt;" +
                "}";

        String html = "<html>" +
                "<head>" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"CID:" + "cid5.getString()" + "\" />" +
                "</head>" +
                "<body>" +
                "<table id=\"tscr\" cellpadding=\"0\" cellspacing=\"10\">" +
                "<tbody><tr> " +
                "<td colspan=\"4\"><b>NEW:</b> Chilkat for Java and Scripting Languages</td>" +
                "</tr><tr>" +
                "<td><a href=\"#\">Java</a><br><a href=\"#\"><img src=\"CID:" + "cid1.getString()" + "\" border=\"0\"></a></td>" +
                "<td><a href=\"#\">Perl</a><br><a href=\"#\"><img src=\"CID:" + "cid3.getString()" + "\" border=\"0\"></a></td>" +
                "<td><a href=\"#\">Python</a><br><a href=\"#\"><img src=\"CID:" + "cid2.getString()" + "\" border=\"0\"></a></td>" +
                "<td><a href=\"#\">Ruby</a><br><a href=\"#\"><img src=\"CID:" + "cid4.getString()" + "\" border=\"0\"></a></td>" +
                "</tr>" +
                "</tbody></table>" +
                "</body>" +
                "</html>";
        email.set_HTMLBody(html);
        email.set_Subject("SUBJECT_TO");
        email.set_From("ilhombeks@gmail.com");
        email.set_To("ilhombeks@gmail.com");
        email.set_FileName("SIMPLE");
        email.set_File(new File(""));

        createEMLFile(email);

        System.out.println(eml.get_FileName());
        System.out.println(eml.get_Subject());
        System.out.println(eml.get_From());
        System.out.println(eml.get_To());
        System.out.println(eml.get_Content_Type());
        System.out.println(eml.get_HTMLBody());

    }
}
