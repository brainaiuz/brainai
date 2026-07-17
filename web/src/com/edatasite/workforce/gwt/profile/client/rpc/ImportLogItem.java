package com.edatasite.workforce.gwt.profile.client.rpc;

import com.edatasite.workforce.gwt.core.client.enums.ImportStatusEnum;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class ImportLogItem implements IsSerializable {
    public static final String TYPE = "TYPE";
    public static final String STATUS = "STATUS";
    public static final String DATE = "DATE";
    public static final String IMPORT_FILE = "IMPORT_FILE";
    public static final String LOG_FILE = "LOG_FILE";
    public static final String IMPORTED = "IMPORTED";
    public static final String REJECTED = "REJECTED";
    public static final String REQUESTED = "REQUESTED";
    public static final String SKIPPED = "SKIPPED";
    public static final String OVERWRITTEN = "OVERWRITTEN";
    public static final String ERROR = "ERROR";
    private Integer objectID;
    private ImportTypeEnum type;
    private ImportStatusEnum status;
    private Date date;
    private SelectItem importFile;
    private SelectItem logFile;
    private Integer requestedRows;
    private Integer importedRows;
    private Integer rejectedRows;
    private Integer skippedRows;
    private Integer overwrittenRows;
    private String errorMessage;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public ImportTypeEnum getType() {
        return type;
    }

    public void setType(ImportTypeEnum type) {
        this.type = type;
    }

    public ImportStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ImportStatusEnum status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public SelectItem getImportFile() {
        return importFile;
    }

    public void setImportFile(SelectItem importFile) {
        this.importFile = importFile;
    }

    public SelectItem getLogFile() {
        return logFile;
    }

    public void setLogFile(SelectItem logFile) {
        this.logFile = logFile;
    }

    public Integer getRequestedRows() {
        return requestedRows;
    }

    public void setRequestedRows(Integer requestedRows) {
        this.requestedRows = requestedRows;
    }

    public Integer getImportedRows() {
        return importedRows;
    }

    public void setImportedRows(Integer importedRows) {
        this.importedRows = importedRows;
    }

    public Integer getRejectedRows() {
        return rejectedRows;
    }

    public void setRejectedRows(Integer rejectedRows) {
        this.rejectedRows = rejectedRows;
    }

    public Integer getSkippedRows() {
        return skippedRows;
    }

    public void setSkippedRows(Integer skippedRows) {
        this.skippedRows = skippedRows;
    }

    public Integer getOverwrittenRows() {
        return overwrittenRows;
    }

    public void setOverwrittenRows(Integer overwrittenRows) {
        this.overwrittenRows = overwrittenRows;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
