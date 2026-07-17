package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.enums.ImportStatusEnum;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Aug 17, 2010
 * Time: 6:35:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "importFile")
public class EdsImportFile extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private EdsUser owner;

    @Column(name = "file_id")
    private Integer fileID;

    @Column(name = "columns")
    @Type(type = "text")
    private String columns;

    @Column(name = "defaultseparator")
    private String defaultSeparator;

    @Column(name = "hasheader")
    private Boolean hasHeader;

    //for custom fieldsOfThe LEAD.... not for all use columns instead if not custom field...
    @Column(name = "extracolumns")
    @Type(type = "text")
    private String extraColumns;

    @Column(name = "categoryColumns")
    @Type(type = "text")
    private String categoryColumns;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "committed")
    private Boolean committed = false;

    @Column (name = "newcolumns")
    private Integer newColumns;

    @Column (name = "overwrittencolumns")
    private Integer overwrittenColumns;

    @Column (name = "skippedcolumns")
    private Integer skippedColumns;

    @Column (name = "ignoredcolumns")
    private Integer ignoredColumns;

    @Column (name = "clonedcolumns")
    private Integer clonedColumns;

    @Column(name = "csvcolumns")
    private Integer csvColumns;

    @Column(name = "importedcolumns")
    private Integer importedColumns;

    @Column(name = "createdDate")
    private Date createdDate = new Date();

    @Column(name = "duplicateAction")
    private String duplicateAction;

    private Integer paymentID;
    private Integer budgetID;
    private String nextSteps;

    @Column(columnDefinition = "boolean default false")
    private boolean exceptionThrowed;

    @Type(type = "text")
    private String exception;

    @Enumerated(EnumType.STRING)
    private ImportStatusEnum status;

    @Enumerated(EnumType.STRING)
    private ImportTypeEnum type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rejectedrecords_id")
    private EdsUpload rejectedRecords;

    @Column(name = "viewType")
    private String viewType;

    @Type(type = "text")
    @Column(name = "dynamiccolumns")
    private String dynamiccolumns;


    @Column(name = "conversionBalanceDate")
    private Date conversionBalanceDate;

    public EdsImportFile() {
        super();
    }

    public EdsImportFile(ImportFile rpc) {
        this();
        if (rpc != null) {
            if (rpc.getObjectID() != null) {
                setObjectID(rpc.getObjectID());
            }
            setType(rpc.getType());
            setColumns(rpc.getColumnsAsString());
            setExtraColumns(rpc.getExtraColumnsAsString());
            setFileID(rpc.getFileID());
            setDefaultSeparator(new String(new char[]{rpc.getDefaultSeparator()}));
            setHasHeader(rpc.isHasHeader());
            setDuplicateAction(rpc.getDuplicateAction());
            setPaymentID(rpc.getPaymentID());
            setViewType(rpc.getViewType());
            setCategoryColumns(rpc.getCategoryColumns());
            setDynamicColumns(rpc.getDynamicColumns());
            setConversionDate(rpc.getConversionDate());
            setBudgetID(rpc.getBudgetID());
        }
    }

    public boolean isExceptionThrowed() {
        return exceptionThrowed;
    }

    public void setExceptionThrowed(boolean exceptionThrowed) {
        this.exceptionThrowed = exceptionThrowed;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        setExceptionThrowed(exception != null);
        this.exception = exception;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsUser getOwner() {
        return owner;
    }

    public void setOwner(EdsUser owner) {
        this.owner = owner;
    }

    public Integer getFileID() {
        return fileID;
    }

    public void setFileID(Integer fileID) {
        this.fileID = fileID;
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public String getExtraColumns() {
        return extraColumns;
    }

    public void setExtraColumns(String extraColumns) {
        this.extraColumns = extraColumns;
    }

    public String getDefaultSeparator() {
        return defaultSeparator;
    }

    public void setDefaultSeparator(String defaultSeparator) {
        this.defaultSeparator = defaultSeparator;
    }

    public Boolean isHasHeader() {
        return hasHeader;
    }

    public void setHasHeader(Boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getCommitted() {
        return committed;
    }

    public void setCommitted(Boolean committed) {
        this.committed = committed;
    }

    public Integer getCsvColumns() {
        return csvColumns;
    }

    public void setCsvColumns(Integer csvColumns) {
        this.csvColumns = hasHeader != null && hasHeader ? csvColumns - 1 : csvColumns;
    }

    public Integer getImportedColumns() {
        return importedColumns!=null ? importedColumns : 0;
    }

    public void setImportedColumns(Integer importedColumns) {
        this.importedColumns = importedColumns;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getDuplicateAction() {
        return duplicateAction;
    }

    public void setDuplicateAction(String duplicateAction) {
        this.duplicateAction = duplicateAction;
    }

    public Integer getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(Integer paymentID) {
        this.paymentID = paymentID;
    }

    public Integer getBudgetID() {
        return this.budgetID;
    }

    public void setBudgetID(final Integer budgetID) {
        this.budgetID = budgetID;
    }

    public String getNextSteps() {
        return nextSteps;
    }

    public void setNextSteps(String nextSteps) {
        this.nextSteps = nextSteps;
    }

    public ImportStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ImportStatusEnum status) {
        this.status = status;
    }

    public ImportTypeEnum getType() {
        return type;
    }

    public void setType(ImportTypeEnum type) {
        this.type = type;
    }

    public EdsUpload getRejectedRecords() {
        return rejectedRecords;
    }

    public void setRejectedRecords(EdsUpload rejectedRecords) {
        this.rejectedRecords = rejectedRecords;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public String getCategoryColumns() {
        return categoryColumns;
    }

    public void setCategoryColumns(String categoryColumns) {
        this.categoryColumns = categoryColumns;
    }

    public Integer getNewColumns() {
        return newColumns != null ? newColumns : 0;
    }

    public void setNewColumns(Integer newColumns) {
        this.newColumns = newColumns;
    }

    public Integer getOverwrittenColumns() {
        return overwrittenColumns != null ? overwrittenColumns : 0;
    }

    public void setOverwrittenColumns(Integer overwrittenColumns) {
        this.overwrittenColumns = overwrittenColumns;
    }

    public Integer getSkippedColumns() {
        return skippedColumns != null ? skippedColumns : 0;
    }

    public void setSkippedColumns(Integer skippedColumns) {
        this.skippedColumns = skippedColumns;
    }

    public Integer getIgnoredColumns() {
        return ignoredColumns != null ? ignoredColumns : 0;
    }

    public void setIgnoredColumns(Integer ignoredColumns) {
        this.ignoredColumns = ignoredColumns;
    }

    public Integer getClonedColumns() {
        return clonedColumns != null ? clonedColumns : 0;
    }

    public void setClonedColumns(Integer clonedColumns) {
        this.clonedColumns = clonedColumns;
    }

    public Date getConversionDate() {
        return this.conversionBalanceDate;
    }

    public void setConversionDate(final Date conversionDate) {
        this.conversionBalanceDate = conversionDate;
    }

    public SelectItem[] getDynamicColumns() {
        return StringUtils.isNotEmpty(dynamiccolumns) ? new Gson().fromJson(dynamiccolumns, new TypeToken<SelectItem[]>() {
        }.getType()) : null;
    }

    public void setDynamicColumns(SelectItem[] serieConfs) {

        if (serieConfs != null && serieConfs.length != 0) {
            this.dynamiccolumns = new Gson().toJson(serieConfs);
        } else {
            this.dynamiccolumns = null;
        }
    }

    public ImportFile getRPC() {
        ImportFile importFile = new ImportFile();
        importFile.setObjectID(getObjectID());
        importFile.setFileID(getFileID());
        importFile.setColumns(getColumns());
        importFile.setExtraColumns(getExtraColumns());
        importFile.setDefaultSeparator(getDefaultSeparator().charAt(0));
        importFile.setHasHeader(isHasHeader());
        importFile.setCsvColumns(getCsvColumns());
        importFile.setImportedColumns(getImportedColumns());
        importFile.setNewColumns(getNewColumns());
        importFile.setOverwrittenColumns(getOverwrittenColumns());
        importFile.setSkippedColumns(getSkippedColumns());
        importFile.setIgnoredColumns(getIgnoredColumns());
        importFile.setClonedColumns(getClonedColumns());
        importFile.setDuplicateAction(getDuplicateAction());
        importFile.setPaymentID(getPaymentID());
        importFile.setNextSteps(getNextSteps());
        importFile.setViewType(getViewType());
        importFile.setType(getType());
        importFile.setCategoryColumns(getCategoryColumns());
        if (getOwner() != null) {
            importFile.setUserID(getOwner().getObjectID());
        }
        importFile.setDynamicColumns(getDynamicColumns());
        importFile.setConversionDate(getConversionDate());
        importFile.setBudgetID(getBudgetID());
        return importFile;
    }
}
