package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HasApprovers;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.view.EmployeeDataDetail;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentCalculationDetail;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollPdfTemplateList;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shohruh on 27 Oct 2016.
 */
public class AdditionalPayment extends HasApprovers implements IsSerializable, ListingCustomFields {
    public static final String OBJECT_ID = "objectId";
    public static final String PERIOD = "period";
    public static final String STATUS = "status";
    public static final String REFERENCE = "reference";
    public static final String APPROVER = "approver";
    public static final String CREATOR = "creator";
    public static final String PAYMENT_CREATOR = "paymentCreator";
    public static final String TOTAL = "total";
    public static final String CATEGORY = "category";
    public static final String PAYMENT_TYPE = "paymentType";
    public static final String FIXED_AMOUNT = "fixedAmount";
    public static final String PERCENTAGE = "percentage";
    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String MONTH_ID = "monthId";
    public static final String IS_SHOW_INPAYSLIPS = "isShowInPayslips";
    public static final String ADDITIONAL_PAYMENT_ITEMTABLE_DATA = "itemTableData";
    public static final String CALCULATE_BY_LAST_MONT = "calculateByLastMonth";
    public static final String UPDATED_TABLE_ITEMS = "updatedTableItems";
    public static final String DELETED_TABLE_ITEMS = "deletedTableItems";
    public static final String EMPLOYEE_IDS = "employeeIds";
    public static final String DEPARTMENT_ID = "departmetnId";
    public static final String LOCATION_ID = "locationId";
    public static final String SUPERVISOR_ID = "supervisorId";
    public static final String GROUP_TYPE_ID = "groupTypeId";
    public static final String ENTITY_TYPE = "entityType";
    public static final String DEFAULT_CATEGORY = "defaultCategory";
    public static final String DEFAULT_DATE = "defaultDate";
    public static final String ALLOWANCE_PAYMENT_CATEGORY_CODES = "allowancePaymentCategoryCodes";
    public static final String IS_BASIC_PLUS_ALLOWANCE_TYPE = "isBasicPlusAllowanceType";
    public static final String IS_BASIC_SALARY = "isBasicSalary";

    private SelectItem category;
    private HashMap<String, String> valueMap;

    public SelectItem getCategory() {
        return category;
    }

    public void setCategory(SelectItem category) {
        this.category = category;
    }

    private Integer objectID;
    private String status;
    private String statusCode;
    private FileItem[] attachments;
    private String oldStatusCode;
    private String reference;
    private String type;
    private String entityType;
    private String month;
    private Integer monthID;
    private Integer year;
    private Integer pdfTemplateID;
    private BigDecimal total;
    private SelectItem creator;
    private SelectItem approver;
    private SelectItem employee;
    private SelectItem department;
    private SelectItem[] departmentList;
    private SelectItem location;
    private SelectItem supervisor;
    private String employeeCode;
    private SelectItem payrollBatch;
    private PaymentDeductionSelectItem defaultCategory;
    private CurrencyItem currency;
    private List<PaymentDeductionObject> items;
    private List<PayrollPayment> payments;
    private DateNonConvertable date;
    private DateNonConvertable approvedDate;
    private String categoryType = PayrollConstants.CATEGORY_PAYMENT;
    private String employeePosition;
    private String employeePayMethod;
    private PayrollPdfTemplateList pdfTemplateList;
    private DateNonConvertable defaultDate;
    private String paymentType;
    private BigDecimal fixedAmount;
    private boolean showInPayslip = false;
    private boolean fromView = false;
    private BigDecimal percentage;
    private Integer defaultPayrollCategoryId;
    private boolean isBasicPlusAllowance = false;
    private ArrayList<PaymentDeductionSelectItem> allowancePaymentCategories;
    private DateNonConvertable updatedTime;
    private SelectItem updater;
    private String categoryLookUp;
    private boolean isFromCopy;
    private HistoryListItem[] historyList;
    private ArrayList<CompanyCustomFieldItem> itemCustomFields;
    private ArrayList<CompanyCustomFieldItem> customFields;
    private ColumnConfigs[] columnConfigs;
    private Integer currentUserId;
    private Integer leaveRequestId;
    private SelectItem fromObject;
    private Integer backupsEmployeeId;
    private boolean isApprover;
    private boolean isApproverSaved;
    private boolean makePayment = true;
    private String employeeIds;
    private ArrayList<PaymentCalculationDetail> calculationDetails;
    private EmployeeDataDetail employeeDataDetail;
    private HashMap<String, Object> customFieldsMap;
    private Integer totalItems;
    private HashMap<String, PaymentDeductionObject> changedItems;
    private HashMap<String, Boolean> deletedItems;
    private ListingFilterParameter filterParameter;
    private Integer fromId;
    private String fromType;
    private boolean deleted;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getOldStatusCode() {
        return this.oldStatusCode;
    }

    public void setOldStatusCode(final String oldStatusCode) {
        this.oldStatusCode = oldStatusCode;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<PaymentDeductionObject> getItems() {
        return items;
    }

    public void setItems(List<PaymentDeductionObject> items) {
        this.items = items;
    }

    public void addItem(PaymentDeductionObject item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
    }

    public List<PayrollPayment> getPayments() {
        return payments;
    }

    public void setPayments(List<PayrollPayment> payments) {
        this.payments = payments;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getPeriod() {
        StringBuilder period = new StringBuilder();
        period.append(getMonth() != null ? getMonth() : "")
                .append(period.length() > 0 && getYear() != null ? ", " : "")
                .append(getYear() != null ? getYear() : "");
        return period.length() > 0 ? period.toString() : "N/A";
    }

    public SelectItem getCreator() {
        return creator;
    }

    public void setCreator(SelectItem creator) {
        this.creator = creator;
    }

    public SelectItem getApprover() {
        return approver;
    }

    public void setApprover(SelectItem approver) {
        this.approver = approver;
    }

    public SelectItem getEmployee() {
        return employee;
    }

    public void setEmployee(SelectItem employee) {
        this.employee = employee;
    }

    public SelectItem getDepartment() {
        return department;
    }

    public void setDepartment(SelectItem department) {
        this.department = department;
    }

    public SelectItem[] getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(SelectItem[] departmentList) {
        this.departmentList = departmentList;
    }

    public SelectItem getLocation() {
        return location;
    }

    public void setLocation(SelectItem location) {
        this.location = location;
    }

    public SelectItem getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(SelectItem supervisor) {
        this.supervisor = supervisor;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setMonthID(Integer monthID) {
        this.monthID = monthID;
    }

    public Integer getMonthID() {
        return monthID;
    }

    public void setApprovedDate(DateNonConvertable approvedDate) {
        this.approvedDate = approvedDate;
    }

    public DateNonConvertable getApprovedDate() {
        return approvedDate;
    }

    public void setPayrollBatch(SelectItem payrollBatch) {
        this.payrollBatch = payrollBatch;
    }

    public SelectItem getPayrollBatch() {
        return payrollBatch;
    }

    public void setDefaultCategory(PaymentDeductionSelectItem defaultCategory) {
        this.defaultCategory = defaultCategory;
    }

    public PaymentDeductionSelectItem getDefaultCategory() {
        return defaultCategory;
    }

    public CurrencyItem getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyItem currency) {
        this.currency = currency;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getEntityType() {
        return this.entityType;
    }

    public void setEntityType(final String entityType) {
        this.entityType = entityType;
    }

    public boolean getByCommission() {
        return Constants.BY_COMMISION_TYPE.equals(type);
    }

    public boolean isShowInPayslip() {
        return showInPayslip;
    }

    public void setShowInPayslip(boolean showInPayslip) {
        this.showInPayslip = showInPayslip;
    }

    public boolean isFromView() {
        return this.fromView;
    }

    public void setFromView(final boolean fromView) {
        this.fromView = fromView;
    }

    public Integer getPdfTemplateID() {
        return pdfTemplateID;
    }

    public void setPdfTemplateID(Integer pdfTemplateID) {
        this.pdfTemplateID = pdfTemplateID;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getEmployeePosition() {
        return employeePosition;
    }

    public void setEmployeePosition(String employeePosition) {
        this.employeePosition = employeePosition;
    }

    public String getEmployeePayMethod() {
        return employeePayMethod;
    }

    public void setEmployeePayMethod(String employeePayMethod) {
        this.employeePayMethod = employeePayMethod;
    }

    public PayrollPdfTemplateList getPdfTemplateList() {
        return pdfTemplateList;
    }

    public void setPdfTemplateList(PayrollPdfTemplateList pdfTemplateList) {
        this.pdfTemplateList = pdfTemplateList;
    }

    public DateNonConvertable getDefaultDate() {
        return defaultDate;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getFixedAmount() {
        return this.fixedAmount;
    }

    public void setFixedAmount(final BigDecimal fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public void setDefaultDate(DateNonConvertable defaultDate) {
        this.defaultDate = defaultDate;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public Integer getDefaultPayrollCategoryId() {
        return defaultPayrollCategoryId;
    }

    public void setDefaultPayrollCategoryId(Integer defaultPayrollCategoryId) {
        this.defaultPayrollCategoryId = defaultPayrollCategoryId;
    }

    public SelectItem getCurrentApproverAsSelectItem() {
        if (getCurrentApprover() != null) {
            return getCurrentApprover().getExactEmployee();
        }
        return null;
    }

    public boolean isBasicPlusAllowance() {
        return isBasicPlusAllowance;
    }

    public void setBasicPlusAllowance(boolean basicPlusAllowance) {
        isBasicPlusAllowance = basicPlusAllowance;
    }

    public ArrayList<PaymentDeductionSelectItem> getAllowancePaymentCategories() {
        return allowancePaymentCategories;
    }

    public void setAllowancePaymentCategories(ArrayList<PaymentDeductionSelectItem> allowancePaymentCategories) {
        this.allowancePaymentCategories = allowancePaymentCategories;
    }

    public DateNonConvertable getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(DateNonConvertable updatedTime) {
        this.updatedTime = updatedTime;
    }

    public SelectItem getUpdater() {
        return updater;
    }

    public void setUpdater(SelectItem updater) {
        this.updater = updater;
    }

    public String getCategoryLookUp() {
        return categoryLookUp;
    }

    public void setCategoryLookUp(String categoryLookUp) {
        this.categoryLookUp = categoryLookUp;
    }

    public boolean isFromCopy() {
        return isFromCopy;
    }

    public void setFromCopy(boolean fromCopy) {
        isFromCopy = fromCopy;
    }

    public HistoryListItem[] getHistoryList() {
        return historyList;
    }

    public void setHistoryList(HistoryListItem[] historyList) {
        this.historyList = historyList;
    }

    public ArrayList<CompanyCustomFieldItem> getItemCustomFields() {
        return this.itemCustomFields;
    }

    public void setItemCustomFields(final ArrayList<CompanyCustomFieldItem> itemCustomFields) {
        this.itemCustomFields = itemCustomFields;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return this.customFields;
    }

    public void setCustomFields(final ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
    }

    public ColumnConfigs[] getColumnConfigs() {
        return this.columnConfigs;
    }

    public void setColumnConfigs(final ColumnConfigs[] columnConfigs) {
        this.columnConfigs = columnConfigs;
    }

    public Integer getCurrentUserId() {
        return this.currentUserId;
    }

    public void setCurrentUserId(final Integer currentUserId) {
        this.currentUserId = currentUserId;
    }

    public Integer getLeaveRequestId() {
        return this.leaveRequestId;
    }

    public void setLeaveRequestId(final Integer leaveRequestId) {
        this.leaveRequestId = leaveRequestId;
    }

    public Integer getBackupsEmployeeId() {
        return backupsEmployeeId;
    }

    public void setBackupsEmployeeId(Integer backupsEmployeeId) {
        this.backupsEmployeeId = backupsEmployeeId;
    }

    public boolean isApprover() {
        return this.isApprover;
    }

    public void setApprover(final boolean approver) {
        this.isApprover = approver;
    }

    public boolean isApproverSaved() {
        return this.isApproverSaved;
    }

    public void setApproverSaved(final boolean approverSaved) {
        this.isApproverSaved = approverSaved;
    }

    public String getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(String employeeIds) {
        this.employeeIds = employeeIds;
    }

    public ArrayList<PaymentCalculationDetail> getCalculationDetails() {
        return this.calculationDetails;
    }

    public void setCalculationDetails(final ArrayList<PaymentCalculationDetail> calculationDetails) {
        this.calculationDetails = calculationDetails;
    }

    public EmployeeDataDetail getEmployeeDataDetail() {
        return employeeDataDetail;
    }

    public void setEmployeeDataDetail(EmployeeDataDetail employeeDataDetail) {
        this.employeeDataDetail = employeeDataDetail;
    }

    public boolean isMakePayment() {
        return this.makePayment;
    }

    public void setMakePayment(final boolean makePayment) {
        this.makePayment = makePayment;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public HashMap<String, PaymentDeductionObject> getChangedItems() {
        return changedItems;
    }

    public void setChangedItems(HashMap<String, PaymentDeductionObject> changedItems) {
        this.changedItems = changedItems;
    }

    public ListingFilterParameter getFilterParameter() {
        return this.filterParameter;
    }

    public void setFilterParameter(final ListingFilterParameter filterParameter) {
        this.filterParameter = filterParameter;
    }

    public HashMap<String, Boolean> getDeletedItems() {
        return deletedItems;
    }

    public void setDeletedItems(HashMap<String, Boolean> deletedItems) {
        this.deletedItems = deletedItems;
    }

    public HashMap<String, Object> getCustomFieldsMap() {
        return customFieldsMap;
    }

    public void setCustomFieldsMap(HashMap<String, Object> customFieldsMap) {
        this.customFieldsMap = customFieldsMap;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return getCustomFieldsMap().get(columnCodeKey);
    }

    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        getCustomFieldsMap().put(columnCodeKey, cellValue);
    }

    public HashMap<String, String> getValueMapForExcel() {
        valueMap = new HashMap<>();
        valueMap.put(PAYMENT_CREATOR, getCreator() != null ? getCreator().getName() : "");
        valueMap.put(OBJECT_ID, String.valueOf(getObjectID()));
        valueMap.put(REFERENCE, getReference());
        valueMap.put(APPROVER, getApprover() != null ? getApprover().getName() : "");
        valueMap.put(PAYMENT_TYPE, getPaymentType());
        valueMap.put(FIXED_AMOUNT, getFixedAmount() != null ? getFixedAmount().toString() : BigDecimal.ZERO.toString());
        valueMap.put(PERCENTAGE, getPercentage() != null ? getPercentage().toString() : BigDecimal.ZERO.toString());
        valueMap.put(YEAR, String.valueOf(getYear()));
        valueMap.put(MONTH, getMonth());
        valueMap.put(IS_SHOW_INPAYSLIPS, String.valueOf(isShowInPayslip()));
        valueMap.put(MONTH_ID, String.valueOf(getMonthID()));
        valueMap.put(DEPARTMENT_ID, String.valueOf(getDepartment() != null ? getDepartment().getId() : null));
        valueMap.put(LOCATION_ID, String.valueOf(getLocation() != null ? getLocation().getId() : null));
        valueMap.put(SUPERVISOR_ID, String.valueOf(getSupervisor() != null ? getSupervisor().getId() : null));
        valueMap.put(EMPLOYEE_IDS, getEmployeeIds());
        valueMap.put(STATUS, getStatusCode());
        valueMap.put(ENTITY_TYPE, getEntityType());
        valueMap.put(DEFAULT_CATEGORY, String.valueOf(getDefaultPayrollCategoryId()));
        valueMap.put(IS_BASIC_PLUS_ALLOWANCE_TYPE, String.valueOf(isBasicPlusAllowance()));
        StringBuilder categoryCodes = new StringBuilder();
        if (getAllowancePaymentCategories() != null) {
            for (PaymentDeductionSelectItem selectItem : getAllowancePaymentCategories()) {
                categoryCodes.append(selectItem.getCode()).append(",");
            }
        }
        valueMap.put(ALLOWANCE_PAYMENT_CATEGORY_CODES, categoryCodes.toString());

        return valueMap;
    }

    public void setMapValuesToFields(HashMap<String, String> valuesMap) {
        this.valueMap = valuesMap;
        if (valueMap != null) {
            setApprover(new SelectItem(1, valueMap.get(APPROVER)));
            setReference(valueMap.get(REFERENCE));
            setObjectID(this.getIntegerValueFromString(OBJECT_ID));
            setCreator(valueMap.get(PAYMENT_CREATOR) != null ? new SelectItem(1, valueMap.get(PAYMENT_CREATOR)) : null);
            setPaymentType(valueMap.get(PAYMENT_TYPE));
            setFixedAmount(this.getBigDecimalValueFromString(FIXED_AMOUNT));
            setPercentage(this.getBigDecimalValueFromString(PERCENTAGE));
            setYear(this.getIntegerValueFromString(YEAR));
            setMonth(valueMap.get(MONTH));
            setShowInPayslip(Boolean.valueOf(valueMap.get(IS_SHOW_INPAYSLIPS)));
            setMonthID(this.getIntegerValueFromString(MONTH_ID));
            setEmployeeIds(valueMap.get(EMPLOYEE_IDS));
            setStatusCode(valueMap.get(STATUS));
            setDefaultPayrollCategoryId(this.getIntegerValueFromString(DEFAULT_CATEGORY));
            setBasicPlusAllowance(Boolean.valueOf(valueMap.get(IS_BASIC_PLUS_ALLOWANCE_TYPE)));
        }
    }

    private Integer getIntegerValueFromString(String code) {
        return valueMap.get(code) != null && ("null".equals(valueMap.get(code)) || valueMap.get(code).isEmpty()) ? null : Integer.valueOf(valueMap.get(code));
    }

    private BigDecimal getBigDecimalValueFromString(String code) {
        return valueMap.get(code) != null ? new BigDecimal(valueMap.get(code)) : BigDecimal.ZERO;
    }

    protected String getAsString(final Object value) {
        return value == null ? null : value.toString();
    }

    public HashMap<String, String> getValueMap() {
        return valueMap;
    }

    public void setValueMap(HashMap<String, String> valueMap) {
        this.valueMap = valueMap;
    }


    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public SelectItem getFromObject() {
        return fromObject;
    }

    public void setFromObject(SelectItem leaveRequest) {
        this.fromObject = leaveRequest;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
