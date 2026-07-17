package com.finnetlimited.reportservice.core.server.telegram.recurrence.service;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.core.domain.EdsTelegramChat;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramSettingsItem;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.TelegramChatManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.profile.server.app.RecurrenceService;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.xml.RpcConvertToXmlLocal;
import com.finnetlimited.reportservice.core.server.ReportingRecurrencePdfService;
import com.finnetlimited.reportservice.core.server.db.schema.TelegramReportingRecurrenceManager;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsReport;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsTelegramReportingScheduleRule;
import com.finnetlimited.reportservice.core.server.generate.GenerateReportToCsv;
import com.finnetlimited.reportservice.core.server.handler.ExcelReportHandler;
import com.finnetlimited.reportservice.core.server.telegram.recurrence.exception.FileSizeExceeds50MBException;
import com.finnetlimited.reportservice.core.server.telegram.recurrence.exception.RecurrenceNotExistsException;
import com.finnetlimited.reportservice.core.server.telegram.recurrence.exception.ReportingSettingsNotExistsException;
import com.finnetlimited.reportservice.core.server.telegram.recurrence.utils.FileMessage;
import com.finnetlimited.reportservice.core.server.telegram.recurrence.utils.MessageContentDetails;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.*;

/**
 * @author Sardorbek Khalimboev
 * @since 2022-02-01
 * This service is created to manage recurring reporting messages through Telegram
 */

@Service
public class TelegramReportingRecurrenceServiceImpl implements TelegramReportingRecurrenceService {
    private static final Logger log = LoggerFactory.getLogger(TelegramReportingRecurrenceServiceImpl.class);

    private final String[] messageTypes = {REPORT_RULE_NAME, REPORT_CSV, REPORT_PDF, REPORT_XLS, REPORT_CHART, REPORT_SUMMARY, REPORT_DESCRIPTION, REPORT_FOLDER, REPORT_NAME, REPORT_CREATION_DATE, REPORT_CREATED_BY, REPORT_MODIFICATION_DATE, REPORT_MODIFIED_BY, REPORT_CURRENT_TIME};

    @Autowired
    private RecurrenceService recurrenceService;
    @Autowired
    private TelegramReportingRecurrenceManager reportingRecurrenceManager;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private TelegramChatManager telegramChatManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private ReportingRecurrencePdfService recurrencePdfService;

    private EdsRecurrence edsRecurrence;
    private EdsTelegramReportingScheduleRule edsTelegramReportingScheduleRule;
    private Integer recurrenceId;
    private TelegramSettingsItem telegramSettingsItem;
    private TelegramBot telegramBot;
    private Set<EdsTelegramChat> edsTelegramChats;
    private String bareMessage;
    private ReportRpc reportRpc;
    private EdsReport edsReport;
    private Integer companyId;
    private EdsUser edsUser;
    private ResultSet resultSet;
    private FileMessage pdfFile;
    private FileMessage xlsFile;
    private FileMessage csvFile;
    private FileMessage chartFile;

    /**
     * Root kickoff method
     *
     * @param recurrenceId
     * @param messageContentDetails
     */
    @Override
    public void initialize(Integer recurrenceId, MessageContentDetails messageContentDetails) {
        initializeProperties(recurrenceId, messageContentDetails);

        try {
            checkRecurrenceExists();
            checkReportingSettingsExists();
            createTelegramBot();
            parseTelegramChatsOfReceivers();
            sendMessage(sendFileMessages());
        } catch (RecurrenceNotExistsException e) {
            System.err.println("EdsRecurrence is null");
            e.printStackTrace();
        } catch (ReportingSettingsNotExistsException e) {
            System.err.println("EdsReportingSettings is null");
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setting values to properties necessary for message content generation
     *
     * @param recurrenceId
     * @param messageContentDetails
     */
    @Override
    public void initializeProperties(Integer recurrenceId, MessageContentDetails messageContentDetails) {
        this.recurrenceId = recurrenceId;
        this.reportRpc = messageContentDetails.getReportRpc();
        this.edsReport = messageContentDetails.getEdsReport();
        this.companyId = messageContentDetails.getCompanyId();
        this.edsUser = messageContentDetails.getEdsUser();
        this.resultSet = messageContentDetails.getResultSet();
    }

    /**
     * Checks if EdsRecurrence is not null
     *
     * @throws RecurrenceNotExistsException
     */
    @Override
    public void checkRecurrenceExists() throws RecurrenceNotExistsException {
        edsRecurrence = recurrenceService.getRecurrence(recurrenceId);
        if (edsRecurrence == null) {
            System.err.println("EdsRecurrence with id: " + recurrenceId + " is null!");
            throw new RecurrenceNotExistsException("EdsRecurrence with id: " + recurrenceId + " is null!");
        }
    }

    /**
     * Checks if EdsReportingSettings is not null
     *
     * @throws ReportingSettingsNotExistsException
     */
    @Override
    public void checkReportingSettingsExists() throws ReportingSettingsNotExistsException {
        int recurrenceId = edsRecurrence.getObjectID();
        edsTelegramReportingScheduleRule = reportingRecurrenceManager.getByRecurrenceId(recurrenceId);
        if (edsTelegramReportingScheduleRule == null) {
            System.err.println("EdsReportingSettings with recurrenceId: " + recurrenceId);
            throw new ReportingSettingsNotExistsException("EdsReportingSettings with busObjectId: " + recurrenceId);
        }
    }

    /**
     * Creating telegram bot
     */
    @Override
    public void createTelegramBot() {
        Integer botId = edsTelegramReportingScheduleRule.getBotId();
        TelegramSettingsItem telegramSettingsItem = globalAuthJdbcSpringManager.getTelegramSettingsItem(botId);
        String token = telegramSettingsItem.getToken();
        telegramBot = new TelegramBot(token);
    }

    //needs optimization

    /**
     * Getting Telegram Chats of users via their ids
     */
    @Override
    public void parseTelegramChatsOfReceivers() {
        edsTelegramChats = new HashSet<>();
        edsTelegramChats = edsTelegramReportingScheduleRule.getChats();
    }

    /**
     * Root method to send file messages
     */
    @Override
    public String sendFileMessages() {
        bareMessage = edsTelegramReportingScheduleRule.getMessage();
        for (String messageType : messageTypes) {
            if (bareMessage.contains(messageType)) {
                checkIfToIncludeThisMessageType(messageType);
            }
        }
        return bareMessage;
    }

    /**
     * Checking and generating message content via message type
     *
     * @param messageType
     */
    @Override
    public void checkIfToIncludeThisMessageType(String messageType) {
        switch (messageType) {
            case REPORT_RULE_NAME -> retrieveRuleName();
            case REPORT_PDF -> sendPDFFile();
            case REPORT_XLS -> sendXLSFile();
            case REPORT_CSV -> sendCSVFile();
            case REPORT_CHART -> sendChart();
            case REPORT_DESCRIPTION -> retrieveDescription();
            case REPORT_FOLDER -> retrieveFolderName();
            case REPORT_NAME -> retrieveName();
            case REPORT_CREATION_DATE -> retrieveCreationDate();
            case REPORT_CREATED_BY -> retrieveCreatedByName();
            case REPORT_MODIFICATION_DATE -> retrieveModificationDate();
            case REPORT_MODIFIED_BY -> retrieveModifiedByName();
            case REPORT_CURRENT_TIME -> retrieveCurrentTime();
            case REPORT_SUMMARY -> retrieveSummary();
        }
    }

    /**
     * Retrieves rule name of the report
     */
    @Override
    public void retrieveRuleName() {
        String ruleName = edsTelegramReportingScheduleRule.getName();
        if (ruleName == null) {
            ruleName = "";
        }
        bareMessage = bareMessage.replace(REPORT_RULE_NAME, ruleName);
    }

    /**
     * Root method to send PDF FILE
     */
    @Override
    public void sendPDFFile() {
        bareMessage = bareMessage.replace(REPORT_PDF, "");
        try {
            generatePDFFile();
            sendFileMessage(pdfFile);
        } catch (NullPointerException e) {
            System.err.println("PDF file is null");
            e.printStackTrace();
        } catch (FileSizeExceeds50MBException e) {
            System.err.println("PDF file size exceeds 50 MB");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error while generating PDF file");
            e.printStackTrace();
        }
    }

    /**
     * Prepares PDF FILE
     *
     * @throws FileSizeExceeds50MBException
     * @throws NullPointerException
     */
    @Override
    public void generatePDFFile() throws FileSizeExceeds50MBException, NullPointerException {
        byte[] pdfBytes = generatePdfFileBytes();
        pdfFile = new FileMessage(pdfBytes);
        pdfFile.setCaption(caption(REPORT_PDF_CAPTION));
        pdfFile.setName(reportRpc.getName() + ".pdf");
    }

    /**
     * Generates bytes of PDF FILE
     *
     * @return pdfBytes
     * @throws FileSizeExceeds50MBException
     * @throws NullPointerException
     */
    @Override
    public byte[] generatePdfFileBytes() throws FileSizeExceeds50MBException, NullPointerException {
        System.out.println("---------------------------Generating PDF file---------------------------");
        EdsCompany company = companyManager.getCompany(companyId);
        byte[] pdfBytes = recurrencePdfService.generatePDFFromHTML(reportRpc, edsReport, company, PdfReferenceCodeNameEnum.REPORTING_SYSTEM.getUrl(), null).toByteArray();
        checkFileNotNull(pdfBytes, REPORT_PDF);
        checkFileSizeLess50MB(pdfBytes, REPORT_PDF);
        System.out.println("---------------------------Successfully generated PDF file---------------------------");
        return pdfBytes;
    }

    /**
     * Root method to send XLS FILE
     */
    @Override
    public void sendXLSFile() {
        bareMessage = bareMessage.replace(REPORT_XLS, "");
        try {
            generateXLSFile();
            sendFileMessage(xlsFile);
        } catch (FileSizeExceeds50MBException e) {
            System.err.println("XLS file size exceeds 50 MB");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error while generating XLS file");
            e.printStackTrace();
        }
    }

    /**
     * Prepares XLS FILE
     *
     * @throws FileSizeExceeds50MBException
     * @throws NullPointerException
     */
    @Override
    public void generateXLSFile() throws FileSizeExceeds50MBException, NullPointerException {
        byte[] xlsBytes = generateXLSFileBytes();
        xlsFile = new FileMessage(xlsBytes);
        xlsFile.setCaption(caption(REPORT_XLS_CAPTION));
        xlsFile.setName(reportRpc.getName() + ".xls");
    }

    /**
     * Generate bytes of XLS FILE
     *
     * @return
     * @throws FileSizeExceeds50MBException
     * @throws NullPointerException
     */
    @Override
    public byte[] generateXLSFileBytes() throws FileSizeExceeds50MBException, NullPointerException {
        System.out.println("---------------------------Generating XLS file---------------------------");
        RpcConvertToXmlLocal rpcToXml = new RpcConvertToXmlLocal(reportRpc);
        String xmlText = rpcToXml.generate();
        ExcelReportHandler excelReportHandler = (ExcelReportHandler) ApplicationContextProvider.applicationContext.getBean("excelReportHandler");
        byte[] xlsBytes = excelReportHandler.run(xmlText, companyId, edsUser).toByteArray();
        checkFileNotNull(xlsBytes, REPORT_XLS);
        checkFileSizeLess50MB(xlsBytes, REPORT_XLS);
        System.out.println("---------------------------Successfully generated XLS file---------------------------");
        return xlsBytes;
    }

    /**
     * Root method to send CSV FILE
     */
    @Override
    public void sendCSVFile() {
        bareMessage = bareMessage.replace(REPORT_CSV, "");
        try {
            generateCSVFile();
            sendFileMessage(csvFile);
        } catch (FileSizeExceeds50MBException e) {
            System.err.println("CSV file size exceeds 50 MB");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error while generating CSV file");
            e.printStackTrace();
        }
    }

    /**
     * Prepares CSV FILE
     *
     * @throws FileSizeExceeds50MBException
     */
    @Override
    public void generateCSVFile() throws FileSizeExceeds50MBException {
        byte[] csvBytes = generateCSVFileBytes();
        csvFile = new FileMessage(csvBytes);
        csvFile.setCaption(caption(REPORT_CSV_CAPTION));
        csvFile.setName(reportRpc.getName() + ".csv");
    }

    /**
     * Generates bytes of CSV FILE
     *
     * @return
     * @throws FileSizeExceeds50MBException
     * @throws NullPointerException
     */
    @Override
    public byte[] generateCSVFileBytes() throws FileSizeExceeds50MBException, NullPointerException {
        System.out.println("---------------------------Generating CSV file---------------------------");
        RpcConvertToXmlLocal rpcToXml = new RpcConvertToXmlLocal(reportRpc);
        String xmlText = rpcToXml.generate();
        ExcelReportHandler excelReportHandler = (ExcelReportHandler) ApplicationContextProvider.applicationContext.getBean("excelReportHandler");
        ByteArrayOutputStream csvOutputStream = excelReportHandler.run(xmlText, companyId, edsUser);
        GenerateReportToCsv csv = new GenerateReportToCsv(reportRpc, resultSet, csvOutputStream);
        byte[] csvBytes = csv.getStream().toByteArray();
        csv.generate(log);
        checkFileNotNull(csvBytes, REPORT_CSV);
        checkFileSizeLess50MB(csvBytes, REPORT_CSV);
        System.out.println("---------------------------Successfully generated CSV file---------------------------");
        return csvBytes;
    }

    /**
     * Generates PNG Charts via byte []
     * Click the link below to learn more
     * <a>https://www.highcharts.com</a>
     *
     * @return byte []
     */
    @Override
    public byte[] sendChart() {
        return null;
    }

    /**
     * Retrieves the description of report
     */
    @Override
    public void retrieveDescription() {
        String description = reportRpc.getDiscreption();
        if (description == null) {
            description = "";
        }
        bareMessage = bareMessage.replace(REPORT_DESCRIPTION, description);
    }

    /**
     * Retrieves the folder name of report
     */
    @Override
    public void retrieveFolderName() {
        String folderName = reportRpc.getDiscreption();
        if (folderName == null) {
            folderName = "";
        }
        bareMessage = bareMessage.replace(REPORT_FOLDER, folderName);
    }

    /**
     * Retrieves the name of the report
     */
    @Override
    public void retrieveName() {
        String name = reportRpc.getName();
        if (name == null) {
            name = "";
        }
        bareMessage = bareMessage.replace(REPORT_NAME, name);
    }

    /**
     * Retrieves creation date of report
     */
    @Override
    public void retrieveCreationDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String stringDate = dateFormat.format(edsReport.getAuditInfo().getCreationDate());
        bareMessage = bareMessage.replace(REPORT_CREATION_DATE, stringDate);
    }

    /**
     * Retrieves the name of the person who created report
     */
    @Override
    public void retrieveCreatedByName() {
        String createdByName = "";
        try {
            createdByName = edsReport.getAuditInfo().getCreatedBy().getName();
        } catch (Exception e) {
            System.err.println("Error in retrieving createdByName!");
            e.printStackTrace();
        }
        bareMessage = bareMessage.replace(REPORT_CREATED_BY, createdByName);
    }

    /**
     * Retrieves last modification date of report
     */
    @Override
    public void retrieveModificationDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String stringDate = dateFormat.format(edsReport.getAuditInfo().getModificationDate());
        bareMessage = bareMessage.replace(REPORT_MODIFICATION_DATE, stringDate);
    }

    /**
     * Retrieves the name of the person who modified report last
     */
    @Override
    public void retrieveModifiedByName() {
        String modifiedByName = "";
        try {
            modifiedByName = edsReport.getAuditInfo().getModifiedBy().getName();
        } catch (Exception e) {
            System.err.println("Error in retrieving modifiedByName!");
            e.printStackTrace();
        }
        bareMessage = bareMessage.replace(REPORT_MODIFIED_BY, modifiedByName);
    }

    /**
     * Retrieves the recurrent time the report is sent
     */
    @Override
    public void retrieveCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String currentTime = dateFormat.format(timestamp);
        bareMessage = bareMessage.replace(REPORT_CURRENT_TIME, currentTime);
    }

    private void retrieveSummary() {
        List<String> selectedColumns = reportRpc.getSelectedColumns().stream()
                .map(ColumnRpc::getName)
                .toList();
        Optional<String> sortableColumn = selectedColumns.stream()
                .filter(c -> Objects.equals(c, reportRpc.getSortTableByColumn()))
                .findFirst();
        String s = "";
        try {
            s = summaryTable(selectedColumns.size() + 1, sortableColumn.isPresent());
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        bareMessage = bareMessage.replace(REPORT_SUMMARY, "<code>" + s + "</code>");
    }

    private String summaryTable(int columnCount, boolean presentSortableColumn) throws SQLException {
        resultSet.next();
        StringBuilder totalValue = new StringBuilder();
        totalValue.append(tableColumn("Total"));
        for (int i = 2; i <= reportRpc.getSelectedColumns().size(); i++) {
            totalValue.append(tableColumn(resultSet.getString(i)));
        }
        StringBuilder result = new StringBuilder();
        StringBuilder rowSplitter = new StringBuilder();
        for (int i = 0; i < reportRpc.getSelectedColumns().size(); i++) {
            result.append(tableColumn(reportRpc.getSelectedColumns().get(i).getTitle()));
            rowSplitter.append(tableColumn("", '-'));
        }
        result.append("\n");
        result.append(rowSplitter).append("\n");
        while (resultSet.next()) {
            int depth = resultSet.getInt(presentSortableColumn ? columnCount + 1 : columnCount) + 1;
            if (depth == 2) {
                continue;
            }
            for (int i = 1; i <= reportRpc.getSelectedColumns().size(); i++) {
                result.append(tableColumn(resultSet.getString(i)));
            }
            result.append("\n");
        }
        result.append(rowSplitter).append("\n");
        result.append(totalValue);
        return result.toString();
    }

    private String tableColumn(String text) {
        return tableColumn(text, ' ');
    }

    private String tableColumn(String text, char filler) {
        if (text == null) return "";
        text = text.trim();
        if (text.length() > 12) {
            text = text.substring(0, 9) + "...";
        }
        StringBuilder s = new StringBuilder(text);
        for (int i = s.length(); i < 12; i++) {
            s.append(filler);
        }
        s.append("|");
        return s.toString();
    }

    /**
     * Sends File message only
     *
     * @param file
     */
    @Override
    public void sendFileMessage(FileMessage file) {
        for (EdsTelegramChat edsTelegramChat : edsTelegramChats) {
            SendDocument fileMessage = new SendDocument(edsTelegramChat.getChatId(), file.getFileInBytes()).fileName(file.getName()).caption(file.getCaption());
            telegramBot.execute(fileMessage);
        }
    }

    /**
     * Sends String message only
     */
    @Override
    public void sendMessage(String bareMessage) throws InterruptedException {
        for (EdsTelegramChat edsTelegramChat : edsTelegramChats) {
            SendMessage sendMessage = new SendMessage(edsTelegramChat.getChatId(), bareMessage).parseMode(ParseMode.HTML);
            telegramBot.execute(sendMessage);
            Thread.sleep(3000);
        }
    }

    private void checkFileNotNull(byte[] pdfBytes, String fileType) throws NullPointerException {
        if (pdfBytes == null) {
            System.err.println(fileType + " file content is null");
            throw new NullPointerException(fileType + " file content is null");
        }
    }

    /**
     * Telegram does not allow to send files greater than 50 mb via bots
     * Checks files are less than 50 mb
     *
     * @param fileBytes
     * @throws FileSizeExceeds50MBException
     */
    private void checkFileSizeLess50MB(byte[] fileBytes, String fileType) throws FileSizeExceeds50MBException {
        if (fileBytes.length > 50 * 1000 * 1000) {
            System.err.println(fileType + " file size is greater than 50 mb");
            throw new FileSizeExceeds50MBException(fileType + " file size is greater than 50 mb");
        }
    }

    private String caption(String fileType) {
        String caption = "";
        if (bareMessage.contains(fileType)) {
            caption = reportRpc.getDiscreption();
            bareMessage = bareMessage.replace(fileType, "");
        }
        return caption;
    }
}
