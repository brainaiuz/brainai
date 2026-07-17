package com.finnetlimited.reportservice.core.server.telegram.recurrence.service;

import com.finnetlimited.reportservice.core.server.telegram.recurrence.exception.FileSizeExceeds50MBException;
import com.finnetlimited.reportservice.core.server.telegram.recurrence.exception.RecurrenceNotExistsException;
import com.finnetlimited.reportservice.core.server.telegram.recurrence.exception.ReportingSettingsNotExistsException;
import com.finnetlimited.reportservice.core.server.telegram.recurrence.utils.FileMessage;
import com.finnetlimited.reportservice.core.server.telegram.recurrence.utils.MessageContentDetails;

public interface TelegramReportingRecurrenceService {

    void initialize(Integer recurrenceId, MessageContentDetails messageContentDetails);

    void initializeProperties(Integer recurrenceId, MessageContentDetails messageContentDetails);

    void checkRecurrenceExists() throws RecurrenceNotExistsException;

    void checkReportingSettingsExists() throws ReportingSettingsNotExistsException;

    void createTelegramBot();

    void parseTelegramChatsOfReceivers();

    String sendFileMessages();

    void generateCSVFile() throws FileSizeExceeds50MBException;

    byte[] generateCSVFileBytes() throws FileSizeExceeds50MBException, NullPointerException;

    byte[] sendChart();

    void checkIfToIncludeThisMessageType(String messageType) throws InterruptedException;

    void retrieveRuleName();

    void sendPDFFile();

    void generatePDFFile() throws FileSizeExceeds50MBException, NullPointerException;

    byte[] generatePdfFileBytes() throws FileSizeExceeds50MBException, NullPointerException;

    void sendXLSFile();

    void generateXLSFile() throws FileSizeExceeds50MBException, NullPointerException;

    byte[] generateXLSFileBytes() throws FileSizeExceeds50MBException, NullPointerException;

    void sendCSVFile() throws InterruptedException;

    void retrieveDescription();

    void retrieveFolderName();

    void retrieveName();

    void retrieveCreationDate();

    void retrieveCreatedByName();

    void retrieveModificationDate();

    void retrieveModifiedByName();

    void retrieveCurrentTime();

    void sendFileMessage(FileMessage pdfFile);

    void sendMessage(String message) throws InterruptedException;
}
