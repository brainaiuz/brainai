package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsQuote;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customform.EdsCustomForm;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomizeFormItem;
import com.edatasite.workforce.gwt.core.client.form.DynamicSectionsRpc;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelForm;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.leaveRequest.LaborPeriodRequest;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.documents.FolderManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.*;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.documents.client.exceptions.DuplicateNameException;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.exceptions.QuotaExceededException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.server.app.DocumentItem;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.DateUtils;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.gwt.team.client.rpc.DepartmentService;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Azam on 09/27/2019.
 * Created date: 10:59
 */
public class CustomFormItemViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {

    @Autowired
    private CustomFormManager customFormManager;
    @Autowired
    private AllInOneService allInOneService;
    @Autowired
    private ItemTableSettingService itemTableSettingService;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private ContactService contactService;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    private AddressManager addressManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private FolderManager folderManager;
    @Autowired
    private DocumentsServiceLocal documentsService;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private PositionManager positionManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private HolidayManager holidayManager;
    @Autowired
    private AvailabilityService availabilityService;

    public static long daysBetween(Date startDate, Date endDate) {
        return (setTimeToMidnight(endDate).getTime() - setTimeToMidnight(startDate).getTime()) / (24 * 60 * 60 * 1000);
    }

    public static Date setTimeToMidnight(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        CustomFormItemRequestObject requestObject = new CustomFormItemRequestObject();
        String objectID = request.getParameter("objectID");
        String fid = request.getParameter("fid");
        String sessionId = request.getParameter("sessionId");
        String templateId = request.getParameter("templateId");
        if (StringUtils.isNotBlank(sessionId)) {
            ServerSecurityContext.getInstance().setSessionId(sessionId);
        }

        if (objectID != null && !StringUtils.isEmpty(objectID)) {
            requestObject.setObjectID(Integer.valueOf(objectID));
        }
        if (fid != null && !StringUtils.isEmpty(fid)) {
            requestObject.setFid(Integer.valueOf(fid));
        }
        if (StringUtils.isNotBlank(templateId)) {
            requestObject.setPdfTemplateID(Integer.valueOf(templateId));
        }
        return requestObject;
    }

    @Override
    protected String getTableName(Object dataClass) {
        CustomFormItemRequestObject requestObject = (CustomFormItemRequestObject) dataClass;
        String tableName = getFileAndTableName(requestObject, false);
        return tableName;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    public ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        CustomFormItemRequestObject requestObject = (CustomFormItemRequestObject) dataClass;
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        HashMap<String, LinkedList<HashMap<String, CustomisedITextTable>>> customDataList = new HashMap<>();
        HashMap<String, LinkedList<HashMap<CustomisedITextTable, CustomisedITextTable>>> customDataList2 = new HashMap<>();
        HashMap<String, CustomisedITextTable> customData = new HashMap<>();

        EdsUser user = uploadManager.getUser();
        Integer objecId = requestObject.getObjectID();
        Integer fid = requestObject.getFid();

        FormItems formItems = commonService.getCustomFormItem(objecId, fid, null, false, null, null, null, null);
        HashMap<String, ArrayList<CustomTableRpc>> tableItems = formItems.getTableItems();
        ArrayList<CompanyCustomFieldItem> customFieldItems = formItems.getCustomFieldItems();

        customDataList.put("CUSTOM_FIELD", getCustomField(formItems, user, fid));
        customData.put("EMPLOYEE_DATA", getEmployeeData(formItems, user));
        customData.put("QUOTE_DATA", getQuoteData(formItems, user));
        customData.put("INVOICE_DATA", getInvoiceData(formItems, user));
        customData.put("CURRENT_DATA", getCurrentData());
        customData.put("CREATION_DATA", getCreationData(formItems));
        customData.put("EMPLOYEE_DATA_FROM_PRODUCT_TABLE", getEmployeeDataFromProductTable(tableItems));
        customData.put("APPROVERS", getApprovers(formItems.getFormID(), objecId, false, user.getObjectID(), false));
        pdfData.setCustomListData(customDataList);
        pdfData.setCustomData(customData);
        if ("EZHEGODNIJ_OTPUSK_2_FORM".equalsIgnoreCase(formItems.getFormID())) {
            customData.put("DAYOFFS", getOffDays(tableItems, user));
        }
        if ("PRINTED_TICKETS_FORM".equalsIgnoreCase(formItems.getFormID())) {
            baseInvoice.setCustomProductCategoriesITextTables(getGroupGroupNameTrainCustomFormData(formItems, fid));
            pdfData.setCustomListData2(customDataList2);
        }
        if ("TRAIN_TICKET_SHAPKA_FORM".equalsIgnoreCase(formItems.getFormID())) {
            customDataList2.put("TRAIN_TICKET_SHAPKA_FORM_DATA", getTraintTicketShapkaData(formItems, user, fid));
            pdfData.setCustomListData2(customDataList2);
        }
        if ("HOTELS_FORM".equalsIgnoreCase(formItems.getFormID())) {
            customData.put("PERIOD_DAYS", getPeriodDays(formItems));
        }
        if ("KOMANDIROVKA_FORM".equalsIgnoreCase(formItems.getFormID())) {
            customData.put("EMPLOYEE_DATA_FROM_ENTITY", getEmployeeDataFromEntityLookUp(tableItems, fid));
            customData.put("ALL_CUSTOM_EMPLOYEES", getAllEmployeesData(formItems));
        }
        if ("UVOLJNENIE2_FORM".equalsIgnoreCase(formItems.getFormID())) {
            //got permission from Faxriddin aka to write this kind of hard code
            pdfData.setCustomEntityTables(getEmployeeDataFromEntityLookupUvolnenie(customFieldItems, fid,customFieldItems.get(3).getFieldDateNonConvertedValue().getDate()));
            customData.put("ALL_CUSTOM_EMPLOYEES", getAllEmployeesData(formItems));
        }
        if ("UVOLNENIE_FORM".equalsIgnoreCase(formItems.getFormID())) {
            pdfData.setCustomEntityTables(getEmployeeDataFromLookupUvolnenie(customFieldItems, fid, user));
            customData.put("ALL_CUSTOM_EMPLOYEES", getAllEmployeesData(formItems));
        }
        baseInvoice.setCustomProductTableList(getCustomProductTaleList(tableItems, fid));
        if (requestObject.isNotForQRCode() && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_PDF_VIEW_QRCODE)) {
            baseInvoice.setCustomNumberAndDatesTable(getCustomNumberAndDatesTable(requestObject));
        }
        baseInvoice.setTableItems(tableItems);
        baseInvoice.setCustomBillToAddress(customBillToAddress(formItems, user));
        pdfData.setBaseInvoice(baseInvoice);

        return pdfData;
    }

    protected CustomisedITextTable getCustomNumberAndDatesTable(CustomFormItemRequestObject requestObject) {
        CustomisedITextTable numAndDates = new CustomisedITextTable();
        numAndDates.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);
        String qrCodeUrl = "https://chart.googleapis.com/chart?chs=250x250&cht=qr&chl=" + getFileUrl(requestObject);
        numAndDates.addRowWithCode(AccountingConstants.QRCODE, "QR code", qrCodeUrl);

        return numAndDates;
    }

    private String getFileUrl(CustomFormItemRequestObject requestObject) {
        requestObject.setIsNotForQRCode(false);
        ByteArrayOutputStream baos = getPdfArrayOutputStream(requestObject);
        EdsUser user = userManager.getUser();

        if (baos != null) {
            DocumentItem fileBody = new DocumentItem();
            fileBody.setInputStream(new ByteArrayInputStream(baos.toByteArray()));
            fileBody.setContentType("application/pdf");
            fileBody.setName(fileName + ".pdf");

            EdsFolder folder = folderManager.getPublicFolder(user.getCompany() != null ? user.getCompany().getObjectID() : null);
            Integer folderID = folder != null ? folder.getObjectID() : null;
            fileBody.setFolderId(folderID);

            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            FileResource fileResource = null;
            try {
                fileResource = documentsService.createFile(fileBody, EdsContextParams.getUploadType(), Constants.F_COMPANY_PUBLIC_ROOT, null);
            } catch (DuplicateNameException | QuotaExceededException | InsufficientPermissionsException |
                     ObjectNotFoundException e) {
                e.printStackTrace();
            }
            return fileResource.getAmazonLink();
        }
        return null;
    }

    private CustomisedITextTable getApprovers(String formId, Integer objecId, boolean isLeaveRequest, Integer userId, boolean fromSettings) {
        CustomisedITextTable table = new CustomisedITextTable();
        ApprovalListResult approvers = allInOneService.getApprovers(formId, objecId, isLeaveRequest, userId, fromSettings);
        if (approvers != null && approvers.getList().size() > 0) {
            for (ApproverItem item : approvers.getList()) {
                table.addColumn(PDFConstants.APPROVER, "APPROVER");
                ArrayList<String> row = new ArrayList<>();
                if (item.getExactEmployee() != null) {
                    EdsEmployee employee = employeeManager.get(item.getExactEmployee().getId());
                    row.add(employee.getLastName() != null && employee.getFirstName() != null && employee.getMiddleName() != null ?
                            employee.getLastName() + " " + employee.getFirstName() + " " + employee.getMiddleName() : "");
                }
                table.addRow(row.toArray(new String[]{}));
            }
        }
        return table;
    }

    private CustomisedITextTable getPeriodDays(FormItems item) {
        CustomisedITextTable periodTable = new CustomisedITextTable();
        periodTable.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);

        Date checkInDate = null;
        Date checkOutDate = null;
        if (item.getCustomFieldItems() != null && item.getCustomFieldItems().size() > 0) {
            for (CompanyCustomFieldItem field : item.getCustomFieldItems()) {
                if (CompanyCustomFieldItem.DATE.equals(field.getDataType()) && field.getFieldDateNonConvertedValue() != null) {
                    if ("Check in date ".equalsIgnoreCase(field.getFieldName())) {
                        checkInDate = field.getFieldDateNonConvertedValue().getNonConvertedDate();
                    }
                    if ("Check out date".equalsIgnoreCase(field.getFieldName())) {
                        checkOutDate = field.getFieldDateNonConvertedValue().getNonConvertedDate();
                    }
                }
            }
        }

        Long periodDays = null;
        if (checkInDate != null && checkOutDate != null) {
            periodDays = daysBetween(checkInDate, checkOutDate);
        }

        periodTable.addRowWithCode(PERIOD_DAYS, PERIOD_DAYS, escapeHtml(periodDays == null ? null : (periodDays.intValue() == 0 ? "1" : Integer.valueOf(periodDays.intValue()).toString())));
        return periodTable;
    }

    private CustomisedITextTable getCurrentData() {
        CustomisedITextTable customTable = new CustomisedITextTable();
        customTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        Date currentDate = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        customTable.addRow(CURRENT_DATE, shortDateFormat.format(currentDate));
        customTable.addRow("CURRENT_YEAR", Calendar.getInstance().get(Calendar.YEAR) + "");
        customTable.addRow("CURRENT_TIME", timeFormat.format(userManager.getUser().getUserDate()));

        return customTable;
    }

    private CustomisedITextTable getCreationData(FormItems formItems) {
        CustomisedITextTable customTable = new CustomisedITextTable();
        customTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if (formItems == null) {
            return customTable;
        }

        String creationDate = formItems.getCreatedDate() != null ? shortDateFormat.format(formItems.getCreatedDate()) : "";
        String creatorBy = formItems.getCreator() != null ? escapeHtml(formItems.getCreator()) : "";
        String modifiedDate = formItems.getModifiedData() != null ? shortDateFormat.format(formItems.getModifiedData()) : "";
        String modifiedBy = formItems.getUpdater() != null ? escapeHtml(formItems.getUpdater()) : "";

        customTable.addRowWithCode("CREATED_DATE", "", creationDate);
        customTable.addRowWithCode("MODIFIED_DATE", "", modifiedDate);
        customTable.addRowWithCode("CREATED_BY", "", creatorBy);
        customTable.addRowWithCode("MODIFIED_BY", "", modifiedBy);

        return customTable;
    }


    private CustomisedITextTable getOffDays(Map<String, ArrayList<CustomTableRpc>> items, EdsUser user) {
        CustomisedITextTable table = new CustomisedITextTable();
        table.addColumn("DAYOFFS", "DAYOFFS");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        int index = 0;
        if (items != null && items.entrySet().size() > 0) {
            for (Map.Entry<String, ArrayList<CustomTableRpc>> item : items.entrySet()) {
                for (int j = 0; j < item.getValue().size(); j++) {
                    ArrayList<String> row = new ArrayList<>();
                    Calendar c1 = Calendar.getInstance();
                    Calendar c2 = Calendar.getInstance();
                    int sundays = 0;
                    int saturdays = 0;
                    int holiday = 0;
                    for (CompanyCustomFieldItem field : item.getValue().get(j).getItemCustomFields()) {
                        if (field.getFieldDateNonConvertedValue() != null) {
                            if (index == 0) {
                                if (field.getFieldDateNonConvertedValue().getDateLong() != 0) {
                                    Date startDate = new Date(field.getFieldDateNonConvertedValue().getDateLong());
                                    c1.setTime(startDate);
                                }
                            } else {
                                if (field.getFieldDateNonConvertedValue().getDateLong() != 0) {
                                    Date endDate = new Date(field.getFieldDateNonConvertedValue().getDateLong());
                                    c2.setTime(endDate);
                                    List<EdsHoliday> calendarHolidays = holidayManager.getCalendarHolidays(user.getLocation(), c1.getTime(), c2.getTime());
                                    Calendar calendar1 = Calendar.getInstance();
                                    Calendar calendar2 = Calendar.getInstance();
                                    if (calendarHolidays.size() > 0) {
                                        calendar1.setTime(calendarHolidays.get(0).getStartDate());
                                        calendar2.setTime(calendarHolidays.get(0).getEndDate());
                                        if (calendar1.get(Calendar.DATE) == calendar2.get(Calendar.DATE)) {
                                            holiday++;
                                        } else {
                                            while (!calendar1.after(calendar2)) {
                                                if (calendar1.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY || calendar1.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                                                    holiday++;
                                                }
                                                calendar1.add(Calendar.DATE, 1);
                                            }

                                        }
                                    }
                                    while (!c1.after(c2)) {
                                        if (c1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                                            sundays++;
                                        } else if (c1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                                            saturdays++;
                                        }
                                        c1.add(Calendar.DATE, 1);
                                    }
                                    row.add(String.valueOf(sundays + saturdays + holiday));
                                }
                            }
                            index++;
                        }
                    }
                    index = 0;
                    table.addRowWithCode(Integer.toString(j), row.toArray(new String[]{}));

                }
            }
        }
        return table;
    }

    private CustomisedITextTable customBillToAddress(FormItems formItems, EdsUser user) {
        CustomisedITextTable table = new CustomisedITextTable();
        table.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);
        Integer customerId = null;
        if (formItems != null && formItems.getCustomFieldItems() != null && formItems.getCustomFieldItems().size() > 0) {
            for (CompanyCustomFieldItem field : formItems.getCustomFieldItems()) {
                if (field.getLookUpTypeEnum() != null && field.getLookUpTypeEnum().name().equalsIgnoreCase(Constants.CUSTOMER) && field.getSelectedId() != null) {
                    customerId = field.getSelectedId();
                    break;
                }
            }
        }
        EdsCrmAccount customer = customerId != null ? crmAccountManager.get(customerId) : null;
        EdsAddress billAddress = customer != null && customer.getBillingAddress() != null ? customer.getBillingAddress() : null;
        if (billAddress != null) {
            table.addRowWithCode(BILL_ADDRESS_NAME, "", escapeHtml(billAddress.getName()));
            table.addRowWithCode(BILL_ADDRESS, "", escapeHtml(billAddress.getAddress()));
            table.addRowWithCode(BILL_ADDRESS2, "", escapeHtml(billAddress.getAddressb()));
            table.addRowWithCode(BILL_CITY, "", escapeHtml(billAddress.getCity()));
            table.addRowWithCode(BILL_COUNTRY, "", escapeHtml(billAddress.getCountryName()));
            table.addRowWithCode(BILL_ZIPCODE, "", escapeHtml(billAddress.getZipCode()));
        }

        String customerContact = customer != null && customer.getPrimaryContact() != null ? escapeHtml(customer.getPrimaryContact().getName()) : "";
        String customerPhone = customer != null ? escapeHtml(customer.getPhone()) : "";
        String customerEmail = customer != null ? escapeHtml(customer.getEmail()) : "";
        String paymentTerms = customer != null && customer.getTerms() != null ? escapeHtml(customer.getTerms().getName()) : "";
        String creatorName = formItems != null ? escapeHtml(formItems.getCreator()) : "";
        String currentApproverName = formItems != null ? escapeHtml(formItems.getCurrentApproverName()) : "";
        String statusName = formItems != null ? escapeHtml(formItems.getStatus()) : "";
        String prevApproverName = formItems != null ? escapeHtml(formItems.getPrevApproverName()) : "";

        table.addRowWithCode(CLIENT_CONTACT, "", customerContact);
        table.addRowWithCode(CLIENT_PHONE, "", customerPhone);
        table.addRowWithCode(CLIENT_EMAIL, "", customerEmail);
        table.addRowWithCode(PAYMENT_TERMS, "", paymentTerms);
        table.addRowWithCode(CREATOR, "", creatorName);
        table.addRowWithCode("CURRENT_APPROVER", "", currentApproverName);
        table.addRowWithCode("PREV_APPROVER", "", prevApproverName);
        table.addRowWithCode(STATUS, "", statusName);

        return table;
    }


    private CustomisedITextTable getEmployeeDataFromProductTable(Map<String, ArrayList<CustomTableRpc>> items) {
        CustomisedITextTable table = new CustomisedITextTable();
        table.addColumn(EMPLOYEE_NAME, EMPLOYEE_NAME);
        table.addColumn("EMP_FIRST_NAME", "EMP_FIRST_NAME");
        table.addColumn("EMP_LAST_NAME", "EMP_LAST_NAME");
        table.addColumn("EMP_MIDDLE_NAME", "EMP_MIDDLE_NAME");
        table.addColumn("EMP_POSITION", "EMP_POSITION");
        table.addColumn("EMP_POSITION_EN", "EMP_POSITION_EN");
        table.addColumn("EMP_POSITION_RU", "EMP_POSITION_RU");
        table.addColumn("EMP_POSITION_UZ", "EMP_POSITION_UZ");
        table.addColumn("EMP_DEPARTMENT", "EMP_DEPARTMENT");
        table.addColumn("EMP_DEPARTMENT_EN", "EMP_DEPARTMENT_EN");
        table.addColumn("EMP_DEPARTMENT_RU", "EMP_DEPARTMENT_RU");
        table.addColumn("EMP_DEPARTMENT_UZ", "EMP_DEPARTMENT_UZ");
        table.addColumn("EMP_DEPARTMENT_EN", "EMP_DEPARTMENT_EN");
        table.addColumn("EMP_DEPARTMENT_RU", "EMP_DEPARTMENT_RU");
        table.addColumn("EMP_DEPARTMENT_UZ", "EMP_DEPARTMENT_UZ");
        table.addColumn(PARENT_NAME, PARENT_NAME);
        table.addColumn("PARENT_NAME_UZ", "PARENT_NAME_UZ");
        table.addColumn("PARENT_NAME1", "PARENT_NAME1");
        table.addColumn("PARENT_NAME1_UZ", "PARENT_NAME1_UZ");
        table.addColumn("PARENT_NAME2", "PARENT_NAME2");
        table.addColumn("PARENT_NAME2_UZ", "PARENT_NAME2_UZ");
        table.addColumn("EMPLOYEE_NAME1", "EMPLOYEE_NAME1");
        table.addColumn("EMP_FIRST_NAME1", "EMP_FIRST_NAME1");
        table.addColumn("EMP_LAST_NAME1", "EMP_LAST_NAME");
        table.addColumn("EMP_MIDDLE_NAME1", "EMP_MIDDLE_NAME1");
        table.addColumn("EMP_POSITION1", "EMP_POSITION1");
        table.addColumn("EMP_POSITION_EN1", "EMP_POSITION_EN1");
        table.addColumn("EMP_POSITION_RU1", "EMP_POSITION_RU1");
        table.addColumn("EMP_POSITION_UZ1", "EMP_POSITION_UZ1");
        table.addColumn("EMP_DEPARTMENT1", "EMP_DEPARTMENT1");
        table.addColumn("EMP_DEPARTMENT_EN1", "EMP_DEPARTMENT_EN1");
        table.addColumn("EMP_DEPARTMENT_RU1", "EMP_DEPARTMENT_RU1");
        table.addColumn("EMP_DEPARTMENT_UZ1", "EMP_DEPARTMENT_UZ1");
        table.addColumn("EMP_DEPARTMENT_EN1", "EMP_DEPARTMENT_EN1");
        table.addColumn("EMP_DEPARTMENT_RU1", "EMP_DEPARTMENT_RU1");
        table.addColumn("EMP_DEPARTMENT_UZ1", "EMP_DEPARTMENT_UZ1");
        table.addColumn("PARENT_NAME1", "PARENT_NAME1");
        table.addColumn("PARENT_NAME_UZ1", "PARENT_NAME_UZ1");
        table.addColumn("PARENT_NAME11", "PARENT_NAME11");
        table.addColumn("PARENT_NAME1_UZ1", "PARENT_NAME1_UZ1");
        table.addColumn("PARENT_NAME21", "PARENT_NAME21");
        table.addColumn("PARENT_NAME2_UZ1", "PARENT_NAME2_UZ1");

        Integer employeeId = null;

        if (items != null && items.entrySet().size() > 0) {
            for (Map.Entry<String, ArrayList<CustomTableRpc>> item : items.entrySet()) {
                for (int j = 0; j < item.getValue().size(); j++) {
                    ArrayList<String> row = new ArrayList<>();
                    for (CompanyCustomFieldItem field : item.getValue().get(j).getItemCustomFields()) {
                        if (field.getLookUpTypeEnum() != null && field.getLookUpTypeEnum().name().equalsIgnoreCase("EMPLOYEE") && field.getSelectedId() != null) {
                            employeeId = field.getSelectedId();

                            if (employeeId == null) {
                                return null;
                            }
                            EdsEmployee employee = employeeManager.get(employeeId);
                            if (employee == null) {
                                return null;
                            }

                            String employeeName = escapeHtml(employee.getFullName());
                            String employeeFirstName = escapeHtml(employee.getFirstName());
                            String employeeLastName = escapeHtml(employee.getLastName());
                            String employeeMiddleName = escapeHtml(employee.getMiddleName());
                            String empPosition = employee.getPosition() != null ? escapeHtml(employee.getPosition().getName()) : "";
                            String empDepartment = employee.getTeam() != null ? employee.getTeam().getName() : "";


                            EdsDepartment edsDepartment = employee.getTeam() != null ? departmentManager.get(employee.getTeam().getObjectID()) : null;
                            String departmentEn = "";
                            String departmentRu = "";
                            String departmentUz = "";
                            if (edsDepartment != null && edsDepartment.getLocale() != null) {
                                departmentEn = edsDepartment.getLocale().getEnglish();
                                departmentRu = edsDepartment.getLocale().getRussian();
                                departmentUz = edsDepartment.getLocale().getUzbek();
                            }

                            EdsPosition edsPosition = employee.getPosition() != null ? positionManager.get(employee.getPosition().getObjectID()) : null;
                            String positionEn = "";
                            String positionRu = "";
                            String positionUz = "";
                            if (edsPosition != null && edsPosition.getLocale() != null) {
                                positionEn = edsPosition.getLocale().getEnglish();
                                positionRu = edsPosition.getLocale().getRussian();
                                positionUz = edsPosition.getLocale().getUzbek();
                            }

                            String parentDepartment = "";
                            String parentDepartmentUz = "";
                            String parentDepartment1 = "";
                            String parentDepartment1Uz = "";


                            String parentDepartment2 = "";
                            String parentDepartment2Uz = "";
                            Integer depId = employee.getEmployeeDepartment().getTeam().getObjectID();
                            if (departmentService.getTeam(employee.getEmployeeDepartment().getTeam().getObjectID()).getParentDepartment() != null) {
                                Integer parentDepartmentId = departmentService.getTeam(employee.getEmployeeDepartment().getTeam().getObjectID()).getParentDepartment().getId();
                                Integer parentDepartment1Id = departmentService.getTeam(parentDepartmentId).getParentDepartment().getId();

                                if (departmentService.getTeam(employee.getEmployeeDepartment().getTeam().getObjectID()).getParentDepartment() != null) {
                                    if (departmentService.getTeam(employee.getEmployeeDepartment().getTeam().getObjectID()).getParentDepartment().getName() != null) {
                                        parentDepartment = departmentService.getTeam(employee.getEmployeeDepartment().getTeam().getObjectID()).getParentDepartment().getName();
                                        if (departmentManager.getDeparmentLocalization(departmentService.getTeam(employee.getEmployeeDepartment().getTeam().getObjectID()).getParentDepartment().getId()) != null) {
                                            parentDepartmentUz = departmentManager.getDeparmentLocalization(departmentService.getTeam(employee.getEmployeeDepartment().getTeam().getObjectID()).getParentDepartment().getId()).getUzbek();
                                        }
                                        if (departmentService.getTeam(parentDepartmentId).getParentDepartment() != null) {
                                            if (departmentService.getTeam(parentDepartmentId).getParentDepartment().getName() != null) {
                                                parentDepartment1 = departmentService.getTeam(parentDepartmentId).getParentDepartment().getName();
                                                if (departmentManager.getDeparmentLocalization(departmentService.getTeam(parentDepartmentId).getParentDepartment().getId()) != null) {
                                                    parentDepartment1Uz = departmentManager.getDeparmentLocalization(departmentService.getTeam(parentDepartmentId).getParentDepartment().getId()).getUzbek();
                                                }
                                                if (departmentService.getTeam(parentDepartment1Id).getParentDepartment() != null) {
                                                    if (departmentService.getTeam(parentDepartment1Id).getParentDepartment().getName() != null) {
                                                        parentDepartment2 = departmentService.getTeam(parentDepartment1Id).getParentDepartment().getName();
                                                        if (departmentManager.getDeparmentLocalization(departmentService.getTeam(parentDepartment1Id).getParentDepartment().getId()) != null) {
                                                            parentDepartment2Uz = departmentManager.getDeparmentLocalization(departmentService.getTeam(parentDepartment1Id).getParentDepartment().getId()).getUzbek();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            row.add(employeeName);
                            row.add(employeeFirstName);
                            row.add(employeeLastName);
                            row.add(employeeMiddleName);
                            row.add(empPosition);
                            row.add(positionEn);
                            row.add(positionRu);
                            row.add(positionUz);
                            row.add(empDepartment);
                            row.add(departmentEn);
                            row.add(departmentRu);
                            row.add(departmentUz);
                            row.add(departmentEn);
                            row.add(departmentRu);
                            row.add(departmentUz);
                            row.add(parentDepartment != null ? escapeHtml(parentDepartment) : "");
                            row.add(parentDepartmentUz != null ? escapeHtml(parentDepartmentUz) : "");
                            row.add(parentDepartment1 != null ? escapeHtml(parentDepartment1) : "");
                            row.add(parentDepartment1Uz != null ? escapeHtml(parentDepartment1Uz) : "");
                            row.add(parentDepartment2 != null ? escapeHtml(parentDepartment2) : "");
                            row.add(parentDepartment2Uz != null ? escapeHtml(parentDepartment2Uz) : "");
                        }
                    }
                    table.addRowWithCode(Integer.toString(j), row.toArray(new String[]{}));
                }
            }
        }
        return table;
    }


    private CustomisedITextTable getAllEmployeesData(FormItems formItems) {
        CustomisedITextTable table = new CustomisedITextTable();
        table.addColumn(EMPLOYEE_NAME, EMPLOYEE_NAME);
        table.addColumn("EMP_FIRST_NAME", "EMP_FIRST_NAME");
        table.addColumn("EMP_LAST_NAME", "EMP_LAST_NAME");
        table.addColumn("EMP_MIDDLE_NAME", "EMP_MIDDLE_NAME");
        table.addColumn("EMP_DEPARTMENT", "EMP_DEPARTMENT");
        table.addColumn("EMP_POSITION", "EMP_POSITION");
        table.addColumn("EMP_POSITION_EN", "EMP_POSITION_EN");
        table.addColumn("EMP_POSITION_RU", "EMP_POSITION_RU");
        table.addColumn("EMP_POSITION_UZ", "EMP_POSITION_UZ");
        Integer employeeId = null;
        int index = 0;
        if (formItems != null && formItems.getCustomFieldItems() != null && formItems.getCustomFieldItems().size() > 0) {
            for (CompanyCustomFieldItem field : formItems.getCustomFieldItems()) {
                if (field.getLookUpTypeEnum() != null && field.getLookUpTypeEnum().name().equalsIgnoreCase("EMPLOYEE") && field.getSelectedId() != null) {
                    LinkedList<String> row = new LinkedList<>();
                    if (field.getLookUpTypeEnum() != null && field.getLookUpTypeEnum().name().equalsIgnoreCase("EMPLOYEE") && field.getSelectedId() != null) {
                        employeeId = field.getSelectedId();
                        if (employeeId == null) {
                            return null;
                        }
                        EdsEmployee employee = employeeManager.get(employeeId);
                        if (employee == null) {
                            return null;
                        }
                        String employeeName = escapeHtml(employee.getFullName());
                        String employeeFirstName = escapeHtml(employee.getFirstName());
                        String employeeLastName = escapeHtml(employee.getLastName());
                        String employeeMiddleName = escapeHtml(employee.getMiddleName());
                        String empDepartment = employee.getTeam() != null ? employee.getTeam().getName() : "";

                        row.add(employeeName);
                        row.add(employeeFirstName);
                        row.add(employeeLastName);
                        row.add(employeeMiddleName);
                        row.add(empDepartment);

                        EdsPosition edsPosition = employee.getPosition() != null ? positionManager.get(employee.getPosition().getObjectID()) : null;
                        String empPosition = employee.getPosition() != null ? escapeHtml(employee.getPosition().getName()) : "";
                        row.add(empPosition);
                        String positionEn = "";
                        String positionRu = "";
                        String positionUz = "";
                        if (edsPosition != null && edsPosition.getLocale() != null) {
                            positionEn = edsPosition.getLocale().getEnglish() != null && !edsPosition.getLocale().getEnglish().isEmpty() ? edsPosition.getLocale().getEnglish() : "-";
                            positionRu = edsPosition.getLocale().getRussian() != null && !edsPosition.getLocale().getRussian().isEmpty() ? edsPosition.getLocale().getRussian() : "-";
                            positionUz = edsPosition.getLocale().getUzbek() != null && !edsPosition.getLocale().getUzbek().isEmpty() ? edsPosition.getLocale().getUzbek() : "-";
                        }

                        row.add(positionEn);
                        row.add(positionRu);
                        row.add(positionUz);

                    }
                    table.addRowWithCode(Integer.toString(index), row.toArray(new String[]{}));
                    index++;
                }
            }
        }
        return table;
    }

    private CustomisedITextTable getEmployeeData(FormItems formItems, EdsUser user) {
        SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        CustomisedITextTable table = new CustomisedITextTable();
        table.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);
        Integer employeeId = null;
        int i = 0;
        String count = "";
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();
        if (formItems != null && formItems.getCustomFieldItems() != null && formItems.getCustomFieldItems().size() > 0) {
            for (CompanyCustomFieldItem field : formItems.getCustomFieldItems()) {
                if (field.getLookUpTypeEnum() != null && field.getLookUpTypeEnum().name().equalsIgnoreCase("EMPLOYEE") && field.getSelectedId() != null) {
                    employeeId = field.getSelectedId();
                    if (employeeId == null) {
                        return null;
                    }
                    EdsEmployee employee = employeeManager.get(employeeId);
                    if (employee == null) {
                        return null;
                    }
                    String employeeName = escapeHtml(employee.getFullName());
                    String employeeFirstName = escapeHtml(employee.getFirstName());
                    String employeeLastName = escapeHtml(employee.getLastName());
                    String employeeMiddleName = escapeHtml(employee.getMiddleName());
                    String hireDate = employee.getStartDate() != null ? shortDateFormat.format(employee.getStartDate()) : "";
                    String empPosition = employee.getPosition() != null ? escapeHtml(employee.getPosition().getName()) : "";
                    String empDepartment = employee.getTeam() != null ? employee.getTeam().getName() : "";
                    String empPassportNumber = "";
                    String empPassportIssueBy = "";
                    String empPassportIssueDate = "";
                    if (employee.getProfile() != null) {
                        empPassportNumber = escapeHtml(employee.getProfile().getPassportNumber());
                        empPassportIssueBy = employee.getProfile().getCountry() != null ? escapeHtml(employee.getProfile().getCountry().getName()) : "";
                        empPassportIssueDate = employee.getProfile().getPassportIssueDate() != null ? shortDateFormat.format(employee.getProfile().getPassportIssueDate()) : "";
                    }

                    EdsDepartment edsDepartment = employee.getTeam() != null ? departmentManager.get(employee.getTeam().getObjectID()) : null;
                    String departmentEn = "";
                    String departmentRu = "";
                    String departmentUz = "";
                    if (edsDepartment != null && edsDepartment.getLocale() != null) {
                        departmentEn = edsDepartment.getLocale().getEnglish() != null && !edsDepartment.getLocale().getEnglish().isEmpty() ? edsDepartment.getLocale().getEnglish() : "-";
                        departmentRu = edsDepartment.getLocale().getRussian() != null && !edsDepartment.getLocale().getRussian().isEmpty() ? edsDepartment.getLocale().getRussian() : "-";
                        departmentUz = edsDepartment.getLocale().getUzbek() != null && !edsDepartment.getLocale().getUzbek().isEmpty() ? edsDepartment.getLocale().getUzbek() : "-";
                    }

                    EdsPosition edsPosition = employee.getPosition() != null ? positionManager.get(employee.getPosition().getObjectID()) : null;
                    String positionEn = "";
                    String positionRu = "";
                    String positionUz = "";
                    if (edsPosition != null && edsPosition.getLocale() != null) {
                        positionEn = edsPosition.getLocale().getEnglish() != null && !edsPosition.getLocale().getEnglish().isEmpty() ? edsPosition.getLocale().getEnglish() : "-";
                        positionRu = edsPosition.getLocale().getRussian() != null && !edsPosition.getLocale().getRussian().isEmpty() ? edsPosition.getLocale().getRussian() : "-";
                        positionUz = edsPosition.getLocale().getUzbek() != null && !edsPosition.getLocale().getUzbek().isEmpty() ? edsPosition.getLocale().getUzbek() : "-";
                    }

                    EdsLocation edsLocation = employee.getLocation() != null ? locationManager.get(employee.getLocation().getObjectID()) : null;
                    String locationEn = "";
                    String locationRu = "";
                    String locationUz = "";
                    if (edsLocation != null && edsLocation.getLocale() != null) {
                        locationEn = edsLocation.getLocale().getEnglish() != null && !edsLocation.getLocale().getEnglish().isEmpty() ? edsLocation.getLocale().getEnglish() : "-";
                        locationRu = edsLocation.getLocale().getRussian() != null && !edsLocation.getLocale().getRussian().isEmpty() ? edsLocation.getLocale().getRussian() : "-";
                        locationUz = edsLocation.getLocale().getUzbek() != null && !edsLocation.getLocale().getUzbek().isEmpty() ? edsLocation.getLocale().getUzbek() : "-";
                    }
                    if (i > 0) {
                        count = "_" + i;
                    }

                    String parentDepartment = "";
                    String parentDepartmentUz = "";
                    String parentDepartment1 = "";
                    String parentDepartment1Uz = "";


                    String parentDepartment2 = "";
                    String parentDepartment2Uz = "";
                    if (departmentService.getTeam(employee.getEmployeeDepartment().getTeam().getObjectID()).getParentDepartment() != null) {
                        Integer parentDepartmentId = departmentService.getTeam(employee.getEmployeeDepartment().getTeam().getObjectID()).getParentDepartment().getId();
                        Integer parentDepartment1Id = null;
                        if (departmentService.getTeam(employee.getEmployeeDepartment().getTeam().getObjectID()).getParentDepartment().getId() != null) {
                            if (departmentService.getTeam(parentDepartmentId) != null && departmentService.getTeam(parentDepartmentId).getParentDepartment() != null) {
                                parentDepartment1Id = departmentService.getTeam(parentDepartmentId).getParentDepartment().getId();
                            }
                        }

                        if (departmentService.getTeam(employee.getEmployeeDepartment().getTeam().getObjectID()).getParentDepartment() != null) {
                            if (departmentService.getTeam(employee.getEmployeeDepartment().getTeam().getObjectID()).getParentDepartment().getName() != null) {
                                parentDepartment = departmentService.getTeam(employee.getEmployeeDepartment().getTeam().getObjectID()).getParentDepartment().getName();
                                if (departmentManager.getDeparmentLocalization(departmentService.getTeam(employee.getEmployeeDepartment().getTeam().getObjectID()).getParentDepartment().getId()) != null) {
                                    parentDepartmentUz = departmentManager.getDeparmentLocalization(departmentService.getTeam(employee.getEmployeeDepartment().getTeam().getObjectID()).getParentDepartment().getId()).getUzbek();
                                }
                                if (departmentManager.getDepartmentLocalizationByReferenceId(parentDepartmentId) != null && departmentService.getTeam(employee.getEmployeeDepartment().getTeam().getObjectID()).getDepartmentNameid() != null) {
                                    parentDepartmentUz = departmentManager.getDepartmentLocalizationByReferenceId(parentDepartmentId).getUzbek();
                                }
                                if (departmentService.getTeam(parentDepartmentId).getParentDepartment() != null) {
                                    if (departmentService.getTeam(parentDepartmentId).getParentDepartment().getName() != null) {
                                        parentDepartment1 = departmentService.getTeam(parentDepartmentId).getParentDepartment().getName();
                                        if (departmentManager.getDeparmentLocalization(departmentService.getTeam(parentDepartmentId).getParentDepartment().getId()) != null) {
                                            parentDepartment1Uz = departmentManager.getDeparmentLocalization(departmentService.getTeam(parentDepartmentId).getParentDepartment().getId()).getUzbek();
                                        }
                                        if (departmentManager.getDepartmentLocalizationByReferenceId(parentDepartment1Id) != null && departmentService.getTeam(parentDepartmentId).getDepartmentNameid() != null) {
                                            parentDepartment1Uz = departmentManager.getDepartmentLocalizationByReferenceId(parentDepartment1Id).getUzbek();
                                        }
                                        if (parentDepartment1Id != null) {
                                            if (departmentService.getTeam(parentDepartment1Id).getParentDepartment() != null) {
                                                if (departmentService.getTeam(parentDepartment1Id).getParentDepartment().getName() != null) {
                                                    parentDepartment2 = departmentService.getTeam(parentDepartment1Id).getParentDepartment().getName();
                                                    if (departmentManager.getDeparmentLocalization(departmentService.getTeam(parentDepartment1Id).getParentDepartment().getId()) != null) {
                                                        parentDepartment2Uz = departmentManager.getDeparmentLocalization(departmentService.getTeam(parentDepartment1Id).getParentDepartment().getId()).getUzbek();
                                                    }
                                                    if (departmentManager.getDepartmentLocalizationByReferenceId(parentDepartment1Id) != null && departmentService.getTeam(parentDepartment1Id).getDepartmentNameid() != null) {
                                                        parentDepartment2Uz = departmentManager.getDepartmentLocalizationByReferenceId(parentDepartment1Id).getUzbek();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    String departmentLeader = "";
                    if (employee.getTeam() != null) {
                        EdsDepartment department = departmentManager.get(employee.getTeam().getObjectID());
                        if (department.getLeader() != null && department.getLeader() != null) {
                            departmentLeader = department.getLeader().getFullName();
                        }
                    }


                    table.addRowWithCode(PARENT_NAME, commonLocalizer.localize(PdfLocalizationName.reportsTo), parentDepartment != null ? escapeHtml(parentDepartment) : "");
                    table.addRowWithCode("PARENT_NAME_UZ", "PARENT_NAME_UZ", parentDepartmentUz != null ? escapeHtml(parentDepartmentUz) : "");
                    table.addRowWithCode("PARENT_NAME1", "PARENT_NAME1", parentDepartment1 != null ? escapeHtml(parentDepartment1) : "");
                    table.addRowWithCode("PARENT_NAME1_UZ", "PARENT_NAME1_UZ", parentDepartment1Uz != null ? escapeHtml(parentDepartment1Uz) : "");
                    table.addRowWithCode("PARENT_NAME2", "PARENT_NAME2", parentDepartment2 != null ? escapeHtml(parentDepartment2) : "");
                    table.addRowWithCode("PARENT_NAME2_UZ", "PARENT_NAME2_UZ", parentDepartment2Uz != null ? escapeHtml(parentDepartment2Uz) : "");


                    table.addRowWithCode(EMPLOYEE_NAME + count, "", employeeName);
                    table.addRowWithCode("EMP_FIRST_NAME" + count, "", employeeFirstName);
                    table.addRowWithCode("EMP_LAST_NAME" + count, "", employeeLastName);
                    table.addRowWithCode("EMP_MIDDLE_NAME" + count, "", employeeMiddleName);
                    table.addRowWithCode("EMP_HIRE_DATE" + count, "", hireDate);
                    table.addRowWithCode("EMP_POSITION" + count, "", empPosition);
                    table.addRowWithCode("EMP_DEPARTMENT" + count, "", empDepartment);
                    table.addRowWithCode("EMP_PASSPORT_NUMBER" + count, "", empPassportNumber);
                    table.addRowWithCode("EMP_PASSPORT_ISSUE_DATE" + count, "", empPassportIssueDate);
                    table.addRowWithCode("EMP_PASSPORT_ISSUE_BY" + count, "", empPassportIssueBy);

                    table.addRowWithCode("EMP_DEPARTMENT_EN" + count, "", departmentEn);
                    table.addRowWithCode("EMP_DEPARTMENT_RU" + count, "", departmentRu);
                    table.addRowWithCode("EMP_DEPARTMENT_UZ" + count, "", departmentUz);

                    table.addRowWithCode("EMP_POSITION_EN" + count, "", positionEn);
                    table.addRowWithCode("EMP_POSITION_RU" + count, "", positionRu);
                    table.addRowWithCode("EMP_POSITION_UZ" + count, "", positionUz);

                    table.addRowWithCode("EMP_LOCATION_EN" + count, "", locationEn);
                    table.addRowWithCode("EMP_LOCATION_RU" + count, "", locationRu);
                    table.addRowWithCode("EMP_LOCATION_UZ" + count, "", locationUz);

                    table.addRowWithCode(DEPARTMENT_LEADER + count, "", departmentLeader);
                    i++;
                    customFields.put("EMPLOYEE" + count, getEmployeeCustomFields(employee));
                }
            }
            table.setCustomFields(customFields);
        }
        return table;
    }

    private CustomisedITextTable getQuoteData(FormItems formItems, EdsUser user) {
        EdsCompany company = user.getCompany();
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(company);
        CustomisedITextTable table = new CustomisedITextTable();
        table.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);
        Integer quoteId = null;
        if (formItems != null && formItems.getCustomFieldItems() != null && formItems.getCustomFieldItems().size() > 0) {
            for (CompanyCustomFieldItem field : formItems.getCustomFieldItems()) {
                if (field.getLookUpTypeEnum() != null && field.getLookUpTypeEnum().name().equalsIgnoreCase("SALES_QUOTE") && field.getSelectedId() != null) {
                    quoteId = field.getSelectedId();
                    break;
                }
            }
        }
        if (quoteId == null) {
            return null;
        }
        EdsQuote edsQuote = quoteManager.get(quoteId);
        if (edsQuote == null) {
            return null;
        }

        String number = escapeHtml(edsQuote.getNumber());
        String currency = edsQuote.getCurrency() != null ? edsQuote.getCurrency().getName() : "";
        String invoiceDate = edsQuote.getInvoiceDate() != null ? shortDateFormat.format(edsQuote.getInvoiceDate()) : "";
        String clientName = edsQuote.getClientOrSupplier() != null ? edsQuote.getClientOrSupplier().getName() : "";
        String clientContactName = edsQuote.getClientContact() != null ? edsQuote.getClientContact().getName() : "";

        DecimalFormat numberFormat = getPriceScaleNumberFormat(user.getCompany(), null);
        String total = numberFormat.format(Optional.ofNullable(edsQuote.getTotal()).orElse(BigDecimal.ZERO));

        EdsAddress billAddress = null;
        if (edsQuote.getBillAddressID() != null) {
            billAddress = addressManager.get(edsQuote.getBillAddressID());
        }
        if (billAddress != null) {
            table.addRowWithCode(BILL_ADDRESS_NAME, "", escapeHtml(billAddress.getName()));
            table.addRowWithCode(BILL_ADDRESS, "", escapeHtml(billAddress.getAddress()));
            table.addRowWithCode(BILL_ADDRESS2, "", escapeHtml(billAddress.getAddressb()));
            table.addRowWithCode(BILL_CITY, "", escapeHtml(billAddress.getCity()));
            table.addRowWithCode(BILL_COUNTRY, "", escapeHtml(billAddress.getCountryName()));
            table.addRowWithCode(BILL_ZIPCODE, "", escapeHtml(billAddress.getZipCode()));
        }

        table.addRowWithCode("QUOTE_NUMBER", "", number);
        table.addRowWithCode("QUOTE_CURRENCY", "", currency);
        table.addRowWithCode("QUOTE_TOTAL", "", total);
        table.addRowWithCode("QUOTE_DATE", "", invoiceDate);
        table.addRowWithCode("QUOTE_CLIENT_NAME", "", clientName);
        table.addRowWithCode("CLIENT_CONTACT", "", clientContactName);

        return table;
    }

    private CustomisedITextTable getInvoiceData(FormItems formItems, EdsUser user) {
        EdsCompany company = user.getCompany();
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(company);
        CustomisedITextTable table = new CustomisedITextTable();
        table.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);
        Integer invoiceId = null;
        if (formItems != null && formItems.getCustomFieldItems() != null && formItems.getCustomFieldItems().size() > 0) {
            for (CompanyCustomFieldItem field : formItems.getCustomFieldItems()) {
                if (field.getLookUpTypeEnum() != null && field.getLookUpTypeEnum().name().equalsIgnoreCase("SALES_INVOICE") && field.getSelectedId() != null) {
                    invoiceId = field.getSelectedId();
                    break;
                }
            }
        }
        if (invoiceId == null) {
            return null;
        }
        EdsInvoice edsInvoice = invoiceManager.get(invoiceId);
        if (edsInvoice == null) {
            return null;
        }

        String number = escapeHtml(edsInvoice.getNumber());
        String currency = edsInvoice.getCurrency() != null ? edsInvoice.getCurrency().getName() : "";
        String invoiceDate = edsInvoice.getInvoiceDate() != null ? shortDateFormat.format(edsInvoice.getInvoiceDate()) : "";
        String clientName = edsInvoice.getClientOrSupplier() != null ? edsInvoice.getClientOrSupplier().getName() : "";
        String clientContactName = edsInvoice.getClientContact() != null ? edsInvoice.getClientContact().getName() : "";
        String projectName = edsInvoice.getRelatedProject() != null ? escapeHtml(edsInvoice.getRelatedProject().getName()) : "";

        String companyVATNumber = "";
        EdsFinancialSettings edsFinancial = financialSettingsManager.getFinancialSettings();
        if (edsFinancial != null && edsFinancial.getTaxIdNumber() != null) {
            companyVATNumber = edsFinancial.getTaxIdNumber();
        }

        DecimalFormat numberFormat = getPriceScaleNumberFormat(user.getCompany(), null);
        String total = numberFormat.format(Optional.ofNullable(edsInvoice.getTotal()).orElse(BigDecimal.ZERO));

        EdsAddress billAddress = null;
        if (edsInvoice.getBillAddressID() != null) {
            billAddress = addressManager.get(edsInvoice.getBillAddressID());
        }
        if (billAddress != null) {
            table.addRowWithCode(BILL_ADDRESS_NAME, "", escapeHtml(billAddress.getName()));
            table.addRowWithCode(BILL_ADDRESS, "", escapeHtml(billAddress.getAddress()));
            table.addRowWithCode(BILL_ADDRESS2, "", escapeHtml(billAddress.getAddressb()));
            table.addRowWithCode(BILL_CITY, "", escapeHtml(billAddress.getCity()));
            table.addRowWithCode(BILL_STATE, "", escapeHtml(billAddress.getStateName()));
            table.addRowWithCode(BILL_COUNTRY, "", escapeHtml(billAddress.getCountryName()));
            table.addRowWithCode(BILL_ZIPCODE, "", escapeHtml(billAddress.getZipCode()));
            table.addRowWithCode("BILL_FULL_ADDRESSES", "", escapeHtml(billAddress.getAddressDataAsHTML()));
        }

        table.addRowWithCode("INVOICE_NUMBER", "", number);
        table.addRowWithCode("INVOICE_CURRENCY", "", currency);
        table.addRowWithCode("INVOICE_TOTAL", "", total);
        table.addRowWithCode("INVOICE_DATE", "", invoiceDate);
        table.addRowWithCode("INVOICE_CLIENT_NAME", "", clientName);
        table.addRowWithCode("CLIENT_CONTACT", "", clientContactName);
        table.addRowWithCode("PROJECT_NAME", "", projectName);
        table.addRowWithCode("COMP_VAT_NUMBER", "", companyVATNumber);

        return table;
    }

    private LinkedHashMap<String, Map<String, String>> getEmployeeCustomFields(EdsEmployee employee) {
        LinkedHashMap<String, Map<String, String>> customFields = new LinkedHashMap<>();
        if (employee.getCustomFields() != null) {
            List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(employee.getCustomFields(), commonService.getCompanyCustomFields(ViewName.Employee));
            if (customFieldItems != null && customFieldItems.size() > 0) {
                for (CompanyCustomFieldItem item : customFieldItems) {
                    if (item != null) {
                        Map<String, String> cols = new HashMap<>();
                        cols.put(COLUMN_NAME, item.getFieldName() != null ? escapeHtml(item.getFieldName()) : "");
                        if (CompanyCustomFieldItem.DATE.equals(item.getDataType())) {
                            if (item.getFieldDateNonConvertedValue() != null && item.getFieldDateNonConvertedValue().getNonConvertedDate() != null) {
                                SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String dateValue = shortDateFormat.format(item.getFieldDateNonConvertedValue().getNonConvertedDate());
                                cols.put(COLUMN_VALUE, dateValue);
                            }
                        } else {
                            cols.put(COLUMN_VALUE, item.getFieldStringValue() != null ? escapeHtml(item.getFieldStringValue()) : "");
                        }
                        if (item.getFieldName() != null) {
                            customFields.put(escapeHtml(item.getFieldName()), cols);
                        }
                    }
                }
            }
        }
        return customFields;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        CustomFormItemRequestObject requestObject = (CustomFormItemRequestObject) dataClass;
        String fileName = getFileAndTableName(requestObject, true);
        setFileName(fileName + "_" + dateFormat(new Date()));
    }

    private List<CustomisedITextTable> getCustomProductTaleList(Map<String, ArrayList<CustomTableRpc>> tableItems, Integer fid) {
        List<CustomisedITextTable> iTextTableList = new ArrayList<>();
        List<String> names = new ArrayList<>();

        EdsCustomForm customForm = customFormManager.get(fid);
        String formId = customForm.getFormID();
        ModelForm modelForm = allInOneService.getModelGridForm(formId);
        Map<String, LinkedList<CustomizeFormItem>> gridColumnMap = modelForm.getGridColumnMap();
        Map<String, DynamicSectionsRpc> sectionsRpcMap = modelForm.getSectionsRpcMap();

        if (gridColumnMap != null && gridColumnMap.size() > 0) {
            for (Map.Entry map : gridColumnMap.entrySet()) {
                for (CustomizeFormItem customFieldItem : (List<CustomizeFormItem>) map.getValue()) {
                    if (customFieldItem.getUiType().equals(UI_TYPE_ITEM_TABLE) && tableItems.containsKey(customFieldItem.getName())) {
                        names.add(sectionsRpcMap != null && sectionsRpcMap.get(map.getKey()) != null ? sectionsRpcMap.get(map.getKey()).getLabel() : "");
                    }
                }
            }
        }
        int i = 0;
        for (Map.Entry<String, ArrayList<CustomTableRpc>> item : tableItems.entrySet()) {
            Map<String, List<CustomTableRpc>> items = new HashMap<>();
            CustomisedITextTable iTextTable = new CustomisedITextTable();

            items.put(item.getKey(), item.getValue());
            iTextTable = getCustomProductTable(items, fid);

            iTextTable.setName(names.get(i++));
            iTextTableList.add(iTextTable);
        }

        return iTextTableList;
    }

    private CustomisedITextTable getCustomProductTable(Map<String, List<CustomTableRpc>> tableItems, Integer fid) {
        SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if (tableItems == null || tableItems.size() <= 0) {
            return null;
        }
        CustomisedITextTable productsTable = new CustomisedITextTable();
        for (Map.Entry map : tableItems.entrySet()) {
            int count = 0;
            boolean isOne = false;
            boolean isSecond = false;
            for (CustomTableRpc item : (List<CustomTableRpc>) map.getValue()) {
                ItemTableSettingsItem tableOrder = itemTableSettingService.getTableSettingsColumnConfigsNew(ItemTableEnum.CUSTOM_FORM, item.getUuid());

                LinkedList<String> row = new LinkedList<>();
                count += 1;
                row.add(count + ".");


                if (!isOne) {
                    //header uchun custom field
                    productsTable.addColumn("No", "No");
                }
                if (tableOrder != null) {
                    for (ColumnConfigs column : tableOrder.getAllColumns()) {
                        for (CompanyCustomFieldItem customFieldItem : item.getItemCustomFields()) {
                            if (column.getAliasName().equals(customFieldItem.getAliasName())) {
                                if (!isOne) {
                                    //custom field for header
                                    productsTable.addColumn(customFieldItem.getAliasName(), customFieldItem.getFieldName());
                                    isSecond = true;
                                }
                                if (isOne || isSecond) {
                                    //custom field for value
                                    if (customFieldItem.getItem() != null) {
                                        row.add(escapeHtml(customFieldItem.getItem().getName()) + "\n" + escapeHtml(customFieldItem.getItem().getDescription()));
                                    } else {
                                        if ("Number".equals(customFieldItem.getDataType()) && !ServerUtils.isNullOrEmpty(customFieldItem.getFieldStringValue())) {
                                            row.add(escapeHtml(getMoneyFormat(new BigDecimal(customFieldItem.getFieldStringValue()))));
                                        } else if ("Date".equals(customFieldItem.getDataType()) && customFieldItem.getFieldDateNonConvertedValue() != null) {
                                            row.add(shortDateFormat.format(customFieldItem.getFieldDateNonConvertedValue().getNonConvertedDate()));
                                        } else if (TYPE_ENTITY_LOOKUP.equals(customFieldItem.getUiType())) {
                                            String defaultValue = "";
                                            if (StringUtils.isNotEmpty(customFieldItem.getFieldStringValue())) {
                                                Integer id = null;
                                                try {
                                                    id = Integer.valueOf(customFieldItem.getFieldStringValue());
                                                } catch (final NumberFormatException e) {
                                                    e.printStackTrace();
                                                }
                                                if (id != null && customFieldItem.getQueryItems() != null) {
                                                    for (final SelectItem selectItem : customFieldItem.getQueryItems()) {
                                                        if (selectItem.getId().equals(id)) {
                                                            defaultValue = escapeHtml(selectItem.getName());
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            row.add(escapeHtml(defaultValue));
                                        } else {
                                            row.add(escapeHtml(customFieldItem.getFieldStringValue()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    isOne = true;
                }
                productsTable.addRow(row.toArray(new String[]{}));
            }
        }

        return productsTable;
    }


    private CustomisedITextTable getEmployeeDataFromEntityLookUp(Map<String, ArrayList<CustomTableRpc>> tableItems, Integer fid) {
        CustomisedITextTable table = new CustomisedITextTable();
        table.addColumn("EMP_FIRST_NAME", "EMP_FIRST_NAME");
        table.addColumn("EMP_LAST_NAME", "EMP_LAST_NAME");
        table.addColumn("EMP_MIDDLE_NAME", "EMP_MIDDLE_NAME");
        table.addColumn("EMP_POSITION_EN", "EMP_POSITION_EN");
        table.addColumn("EMP_POSITION_RU", "EMP_POSITION_RU");
        table.addColumn("EMP_POSITION_UZ", "EMP_POSITION_UZ");
        table.addColumn("EMP_DEPARTMENT_EN", "EMP_DEPARTMENT_EN");
        table.addColumn("EMP_DEPARTMENT_RU", "EMP_DEPARTMENT_RU");
        table.addColumn("EMP_DEPARTMENT_UZ", "EMP_DEPARTMENT_UZ");
        table.addColumn("EMP_PASSPORT_SERIES", "EMP_PASSPORT_SERIES");
        table.addColumn("EMP_FIRST_NAME_ENG", "EMP_FIRST_NAME_ENG");
        table.addColumn("EMP_LAST_NAME_ENG", "EMP_LAST_NAME_ENG");
        table.addColumn("EMP_PASSPORT_NUMBER", "EMP_PASSPORT_NUMBER");


        if (tableItems == null || tableItems.size() <= 0) {
            return null;
        }
        for (Map.Entry map : tableItems.entrySet()) {
            int count = 0;
            int index = 0;
            for (CustomTableRpc item : (List<CustomTableRpc>) map.getValue()) {
                ItemTableSettingsItem tableOrder = itemTableSettingService.getTableSettingsColumnConfigsNew(ItemTableEnum.CUSTOM_FORM, item.getUuid());

                LinkedList<String> row = new LinkedList<>();

                if (tableOrder != null) {
                    for (ColumnConfigs column : tableOrder.getAllColumns()) {
                        for (CompanyCustomFieldItem customFieldItem : item.getItemCustomFields()) {
                            if (column.getAliasName().equals(customFieldItem.getAliasName())) {
                                if (TYPE_ENTITY_LOOKUP.equals(customFieldItem.getUiType())) {
                                    String defaultValue = "";
                                    if (StringUtils.isNotEmpty(customFieldItem.getFieldStringValue())) {
                                        Integer id = null;
                                        try {
                                            id = Integer.valueOf(customFieldItem.getFieldStringValue());
                                        } catch (final NumberFormatException e) {
                                            e.printStackTrace();
                                        }
                                        if (id != null && customFieldItem.getQueryItems() != null) {
                                            for (final SelectItem selectItem : customFieldItem.getQueryItems()) {
                                                if (selectItem.getId().equals(id)) {
                                                    EdsEmployee employee = employeeManager.get(selectItem.getId());

                                                    row.add(employee.getFirstName() != null ? employee.getFirstName() : "");
                                                    row.add(employee.getLastName() != null ? employee.getLastName() : "");
                                                    row.add(employee.getMiddleName() != null ? employee.getMiddleName() : "");
                                                    EdsPosition edsPosition = employee.getPosition() != null ? positionManager.get(employee.getPosition().getObjectID()) : null;
                                                    String positionEn = "";
                                                    String positionRu = "";
                                                    String positionUz = "";
                                                    if (edsPosition != null && edsPosition.getLocale() != null) {
                                                        positionEn = edsPosition.getLocale().getEnglish();
                                                        positionRu = edsPosition.getLocale().getRussian();
                                                        positionUz = edsPosition.getLocale().getUzbek();
                                                    }
                                                    row.add(positionEn);
                                                    row.add(positionRu);
                                                    row.add(positionUz);
                                                    EdsDepartment edsDepartment = employee.getTeam() != null ? departmentManager.get(employee.getTeam().getObjectID()) : null;
                                                    String departmentEn = "";
                                                    String departmentRu = "";
                                                    String departmentUz = "";
                                                    if (edsDepartment != null && edsDepartment.getLocale() != null) {
                                                        departmentEn = edsDepartment.getLocale().getEnglish();
                                                        departmentRu = edsDepartment.getLocale().getRussian();
                                                        departmentUz = edsDepartment.getLocale().getUzbek();
                                                    }
                                                    row.add(departmentEn);
                                                    row.add(departmentRu);
                                                    row.add(departmentUz);

                                                    String empPassportNumber = "";
                                                    if (employee.getProfile() != null) {
                                                        empPassportNumber = escapeHtml(employee.getProfile().getPassportNumber());
                                                        EdsEmployeeProfile profile = employee.getProfile();
                                                        ProfileItem profile1 = hrmsServiceLocal.getProfile(employee.getObjectID());
                                                        ArrayList<CompanyCustomFieldItem> customFields = profile1.getCustomFields();
                                                        LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
                                                        Map<String, String> cols = new HashMap<>();
                                                        for (CompanyCustomFieldItem field : profile1.getCustomFields()) {
                                                            if (field != null && field.getFieldName().equals("Серия Паспорта")) {
                                                                row.add(field.getFieldStringValue() != null ? field.getFieldStringValue() : "");
                                                            }
                                                            if (field != null && field.getFieldName().equals("Имя (eng)")) {
                                                                row.add(field.getFieldStringValue() != null ? field.getFieldStringValue() : "");
                                                            }
                                                            if (field != null && field.getFieldName().equals("Фамилия (eng)")) {
                                                                row.add(field.getFieldStringValue() != null ? field.getFieldStringValue() : "");
                                                            }

                                                        }

                                                    }
                                                    row.add(empPassportNumber);
                                                    table.addRowWithCode(Integer.toString(index), row.toArray(new String[]{}));
                                                    index++;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }

            }
        }

        return table;
    }

    private List<CustomisedITextTable> getEmployeeDataFromLookupUvolnenie(ArrayList<CompanyCustomFieldItem> tableItems, Integer fid, EdsUser user) {
        List<CustomisedITextTable> emps = new ArrayList<>();
        int i = 0;
        for (CompanyCustomFieldItem map : tableItems) {
            String fullName = "";
            String middleName = "";
            String lastName = "";
            String firstName = "";
            double leftLeaveDays = 0;
            CustomisedITextTable table = new CustomisedITextTable();
            table.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);
            if ("LOOKUP".equals(map.getUiType())) {

                    EdsEmployee employee = employeeManager.get(map.getSelectedId());
                    fullName = employee.getFullName() != null ? employee.getFullName() : "";
                    firstName = employee.getFirstName() != null ? employee.getFirstName() : "";
                    lastName = employee.getLastName() != null ? employee.getLastName() : "";
                    middleName = employee.getMiddleName() != null ? employee.getMiddleName() : "";
                    EdsPosition edsPosition = employee.getPosition() != null ? positionManager.get(employee.getPosition().getObjectID()) : null;

                    ArrayList<LaborPeriodRequest> periodList = availabilityService.getPeriodList(employee.getObjectID(), null, LR_TYPE_ANNUAL_LEAVE, false, false);

                    if (periodList != null) {
                        for (LaborPeriodRequest laborPeriodRequest : periodList) {
                            if (laborPeriodRequest.getApprovedTakenDays() < 0) {
                                leftLeaveDays += laborPeriodRequest.getApprovedTakenDays();
                            } else {
                                leftLeaveDays += laborPeriodRequest.getAllowance() - laborPeriodRequest.getApprovedTakenDays();
                            }
                        }
                    }

                    table.addRowWithCode("EMPLOYEE_NAME", "EMPLOYEE_NAME", fullName);
                    table.addRowWithCode("EMP_FIRST_NAME", "EMP_FIRST_NAME", firstName);
                    table.addRowWithCode("EMP_LAST_NAME", "EMP_LAST_NAME", lastName);
                    table.addRowWithCode("EMP_MIDDLE_NAME", "EMP_MIDDLE_NAME", middleName);
                    table.addRowWithCode("EMP_LEAVE_LEFT_DAYS", "EMP_LEAVE_LEFT_DAYS", String.valueOf(leftLeaveDays));
                    i++;
                    emps.add(table);

            }

        }

        return emps;
    }


    private List<CustomisedITextTable> getEmployeeDataFromEntityLookupUvolnenie(ArrayList<CompanyCustomFieldItem> tableItems, Integer fid,Date resignDate) {
        List<CustomisedITextTable> emps = new ArrayList<>();
        Map<String, LinkedHashMap<String, Map<String, String>>> customFieldsEmployee = new HashMap<>();
        SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if (tableItems == null || tableItems.size() <= 0) {
            return null;
        }
        int i = 0;
        String count = "";
        LinkedHashMap<String, Map<String, String>> itemFields = new LinkedHashMap<>();
        for (CompanyCustomFieldItem map : tableItems) {
            String fullName = "";
            String middleName = "";
            String lastName = "";
            String firstName = "";
            double leftLeaveDays = 0;

            if (map != null) {
                if (TYPE_ENTITY_LOOKUP.equals(map.getUiType())) {
                    String defaultValue = "";
                    if (StringUtils.isNotEmpty(map.getFieldStringValue())) {
                        Integer id = null;
                        try {
                            id = Integer.valueOf(map.getFieldStringValue());
                        } catch (final NumberFormatException e) {
                            e.printStackTrace();
                        }
                        if (id != null && map.getQueryItems() != null) {
                            String positionEn = "";
                            String positionRu = "";
                            String positionUz = "";

                            String departmentEn = "";
                            String departmentRu = "";
                            String departmentUz = "";

                            String seriyaPassport = "";
                            String nameEng = "";
                            String familiyaEng = "";

                            String empPassportNumber = "";
                            String empHireDate = "";
                            String fieldname = map.getFieldName();
                            CustomisedITextTable table = new CustomisedITextTable();
                            table.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);
                            for (final SelectItem selectItem : map.getQueryItems()) {
                                if (selectItem.getId().equals(id)) {
                                    EdsEmployee employee = employeeManager.get(selectItem.getId());
                                    fullName = employee.getFullName() != null ? employee.getFullName() : "";
                                    firstName = employee.getFirstName() != null ? employee.getFirstName() : "";
                                    lastName = employee.getLastName() != null ? employee.getLastName() : "";
                                    middleName = employee.getMiddleName() != null ? employee.getMiddleName() : "";
                                    EdsPosition edsPosition = employee.getPosition() != null ? positionManager.get(employee.getPosition().getObjectID()) : null;

                                    if (edsPosition != null && edsPosition.getLocale() != null) {
                                        positionEn = edsPosition.getLocale().getEnglish();
                                        positionRu = edsPosition.getLocale().getRussian();
                                        positionUz = edsPosition.getLocale().getUzbek();
                                    }
                                    EdsDepartment edsDepartment = employee.getTeam() != null ? departmentManager.get(employee.getTeam().getObjectID()) : null;

                                    if (edsDepartment != null && edsDepartment.getLocale() != null) {
                                        departmentEn = edsDepartment.getLocale().getEnglish();
                                        departmentRu = edsDepartment.getLocale().getRussian();
                                        departmentUz = edsDepartment.getLocale().getUzbek();
                                    }

                                    if (i > 0) {
                                        count = "_" + i;
                                    }


                                    customFieldsEmployee.put("EMPLOYEE" + count, getEmployeeCustomFields(employee));


                                    if (employee.getProfile() != null) {
                                        empPassportNumber = escapeHtml(employee.getProfile().getPassportNumber());
                                        EdsEmployeeProfile profile = employee.getProfile();
                                        ProfileItem profile1 = hrmsServiceLocal.getProfile(employee.getObjectID());
                                        if (profile1 != null && profile1.getHireDate() != null) {
                                            empHireDate = shortDateFormat.format(profile1.getHireDate().getNonConvertedDate());
                                        }

                                        ArrayList<CompanyCustomFieldItem> customFields = profile1.getCustomFields();
                                        LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
                                        Map<String, String> cols = new HashMap<>();
                                        for (CompanyCustomFieldItem field : profile1.getCustomFields()) {
                                            if (field != null && field.getFieldName().equals("Серия Паспорта")) {
                                                seriyaPassport = field.getFieldStringValue() != null ? field.getFieldStringValue() : "";
                                            }
                                            if (field != null && field.getFieldName().equals("Имя (eng)")) {
                                                nameEng = field.getFieldStringValue() != null ? field.getFieldStringValue() : "";
                                            }
                                            if (field != null && field.getFieldName().equals("Фамилия (eng)")) {
                                                familiyaEng = field.getFieldStringValue() != null ? field.getFieldStringValue() : "";
                                            }

                                        }

                                    }

                                    ArrayList<LaborPeriodRequest> periodList = availabilityService.getPeriodList(employee.getObjectID(), null, LR_TYPE_ANNUAL_LEAVE, false, false);

                                    for (LaborPeriodRequest laborPeriodRequest : periodList) {
                                        if (DateUtils.compare(resignDate,laborPeriodRequest.getStartDate())) {
                                            if (laborPeriodRequest.getApprovedTakenDays() < 0) {
                                                leftLeaveDays += laborPeriodRequest.getApprovedTakenDays();
                                            } else {
                                                leftLeaveDays += laborPeriodRequest.getAllowance() - laborPeriodRequest.getApprovedTakenDays();
                                            }
                                        }
                                    }
                                }
                            }

                            table.addRowWithCode("EMPLOYEE_NAME", "EMPLOYEE_NAME", fullName);
                            table.addRowWithCode("EMP_FIRST_NAME", "EMP_FIRST_NAME", firstName);
                            table.addRowWithCode("EMP_LAST_NAME", "EMP_LAST_NAME", lastName);
                            table.addRowWithCode("EMP_MIDDLE_NAME", "EMP_MIDDLE_NAME", middleName);
                            table.addRowWithCode("EMP_POSITION_EN", "EMP_POSITION_EN", positionEn);
                            table.addRowWithCode("EMP_POSITION_RU", "EMP_POSITION_RU", positionRu);
                            table.addRowWithCode("EMP_POSITION_UZ", "EMP_POSITION_UZ", positionUz);
                            table.addRowWithCode("EMP_DEPARTMENT_EN", "EMP_DEPARTMENT_EN", departmentEn);
                            table.addRowWithCode("EMP_DEPARTMENT_RU", "EMP_DEPARTMENT_RU", departmentRu);
                            table.addRowWithCode("EMP_DEPARTMENT_UZ", "EMP_DEPARTMENT_UZ", departmentUz);
                            table.addRowWithCode("EMP_PASSPORT_SERIES", "EMP_PASSPORT_SERIES", seriyaPassport);
                            table.addRowWithCode("EMP_FIRST_NAME_ENG", "EMP_FIRST_NAME_ENG", nameEng);
                            table.addRowWithCode("EMP_LAST_NAME_ENG", "EMP_LAST_NAME_ENG", familiyaEng);
                            table.addRowWithCode("EMP_PASSPORT_NUMBER", "EMP_PASSPORT_NUMBER", empPassportNumber);
                            table.addRowWithCode("EMP_HIRE_DATE", "EMP_HIRE_DATE", empHireDate);
                            table.addRowWithCode("EMP_LEAVE_LEFT_DAYS", "EMP_LEAVE_LEFT_DAYS", String.valueOf(leftLeaveDays));
                            table.addRowWithCode("FIELD_NAME", "FIELD_NAME", fieldname);
                            i++;
                            emps.add(table);
                        }
                    }
                }

            }
        }


        return emps;
    }

    private LinkedList<HashMap<String, CustomisedITextTable>> getCustomField(FormItems item, EdsUser user, Integer fid) {
        LinkedList<HashMap<String, CustomisedITextTable>> customFieldItems = new LinkedList<>();
        LinkedHashMap<String, CustomisedITextTable> customFieldListDetials = new LinkedHashMap<>();

        DecimalFormat numberFormat = getPriceScaleNumberFormat(user.getCompany(), null);

        EdsCustomForm customForm = customFormManager.get(fid);
        ModelForm modelForm = allInOneService.getModelGridForm(customForm.getFormID());
        Map<String, LinkedList<CustomizeFormItem>> gridColumnMap = modelForm.getGridColumnMap();
        Map<String, DynamicSectionsRpc> sectionsRpcMap = modelForm.getSectionsRpcMap();

        if (gridColumnMap != null && gridColumnMap.size() > 0) {
            for (Map.Entry map : gridColumnMap.entrySet()) {
                /*String keyName = sectionsRpcMap != null && sectionsRpcMap.get(map.getKey()) != null ? sectionsRpcMap.get(map.getKey()).getLabel() : "";*/
                CustomisedITextTable customFieldTable = new CustomisedITextTable();
                customFieldTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
                for (CustomizeFormItem customFieldItem : (List<CustomizeFormItem>) map.getValue()) {
                    if (item.getCustomFieldItems() != null && item.getCustomFieldItems().size() > 0) {
                        for (CompanyCustomFieldItem field : item.getCustomFieldItems()) {
                            if (field.getFieldName().equalsIgnoreCase(customFieldItem.getLabel()) &&
                                    field.getColumnCode().equalsIgnoreCase(customFieldItem.getName())) {
                                switch (field.getDataType()) {
                                    case CompanyCustomFieldItem.DATE -> {
                                        String dateValue = "—";
                                        EdsCompany company = userManager.getUser().getCompany();
                                        if (field.getFieldDateNonConvertedValue() != null) {
                                            if ("DateTime".equals(field.getUiType())) {
                                                SimpleDateFormat longDateFormat = getCompanyLongDateFormat(company);
                                                if (company.getLocale() != null && "ru".equals(company.getLocale())) {
                                                    Locale ruLocale = new Locale("ru", "RU");
                                                    SimpleDateFormat ruDateFormat = new SimpleDateFormat(longDateFormat.toPattern(), ruLocale);
                                                    dateValue = field.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? ruDateFormat.format(field.getFieldDateNonConvertedValue().getNonConvertedDate()) : "—";
                                                } else {
                                                    dateValue = field.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? longDateFormat(field.getFieldDateNonConvertedValue().getNonConvertedDate(), true) : "—";
                                                }
                                            } else {
                                                SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                                dateValue = field.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? shortDateFormat.format(field.getFieldDateNonConvertedValue().getNonConvertedDate()) : "—";
                                            }
                                        }
                                        customFieldTable.addRowWithCode(field.getDefaultName(), field.getFieldName(), dateValue);
                                    }
                                    case CompanyCustomFieldItem.NUMBER -> {
                                        String numberValue = "—";
                                        if (StringUtils.isNotEmpty(field.getFieldStringValue())) {
                                            numberValue = escapeHtml(numberFormat.format(Double.valueOf(field.getFieldStringValue())));
                                        }
                                        customFieldTable.addRowWithCode(field.getDefaultName(), field.getFieldName(), numberValue);
                                    }
                                    case CompanyCustomFieldItem.TEXT -> {
                                        if (UI_TYPE_MULTI_LOOKUP.equals(field.getUiType())) {
                                            StringBuilder name = new StringBuilder("");
                                            if (StringUtils.isNotEmpty(field.getFieldStringValue())) {
                                                Gson gson = new Gson();
                                                SelectItem[] object = gson.fromJson(field.getFieldStringValue(), SelectItem[].class);
                                                for (SelectItem data : object) {
                                                    name.append(data.getName().trim()).append(", ");
                                                }
                                                if (field.getFieldName().equals("QR code link")) {
                                                    String qrCodeUrlLink = "https://chart.googleapis.com/chart?chs=250x250&cht=qr&chl=" + field.getFieldStringValue();
                                                    field.setFieldStringValue(qrCodeUrlLink);
                                                }
                                            }
                                            customFieldTable.addRowWithCode(field.getDefaultName(), field.getFieldName(), name.toString().replaceAll(", $", ""));
                                        } else if (TYPE_ENTITY_LOOKUP.equals(field.getUiType())) {
                                            String defaultValue = "";
                                            if (StringUtils.isNotEmpty(field.getFieldStringValue())) {
                                                Integer id = null;
                                                try {
                                                    id = Integer.valueOf(field.getFieldStringValue());
                                                } catch (final NumberFormatException e) {
                                                    e.printStackTrace();
                                                }
                                                if (id != null && field.getQueryItems() != null) {
                                                    for (final SelectItem selectItem : field.getQueryItems()) {
                                                        if (selectItem.getId().equals(id)) {
                                                            defaultValue = escapeHtml(selectItem.getName());
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            customFieldTable.addRowWithCode(field.getDefaultName(), field.getFieldName(), escapeHtml(defaultValue));
                                        } else if (field.getUiType().equals(TYPE_ENTITY_MULTI_LOOKUP)) {
                                            StringBuilder name = new StringBuilder("");
                                            if (StringUtils.isNotEmpty(field.getFieldStringValue())) {
                                                Gson gson = new Gson();
                                                SelectItem[] object = gson.fromJson(field.getFieldStringValue(), SelectItem[].class);
                                                for (SelectItem data : object) {
                                                    name.append(data.getName().trim()).append(", ");
                                                }
                                                if (field.getFieldName().equals("QR code link")) {
                                                    String qrCodeUrlLink = "https://chart.googleapis.com/chart?chs=250x250&cht=qr&chl=" + field.getFieldStringValue();
                                                    field.setFieldStringValue(qrCodeUrlLink);
                                                }
                                            }
                                            customFieldTable.addRowWithCode(field.getDefaultName(), field.getFieldName(), name.toString().replaceAll(", $", ""));
                                        } else {
                                            if (field.getLookUpTypeEnum() != null && field.getLookUpTypeEnum().name().equals("DEPARTMENT") && field.getSelectedId() != null) {
                                                EdsEmployee leader = departmentManager.get(field.getSelectedId()).getLeader();
                                                if (leader != null) {
                                                    customFieldTable.addRowWithCode(PDFConstants.DEPARTMENT_LEADER, "Department Leader", escapeHtml(leader.getFullName()));
                                                }
                                            }
                                            String defaultValue = StringUtils.isNotBlank(field.getFieldStringValue()) ? field.getFieldStringValue() : "—";
                                            customFieldTable.addRowWithCode(field.getDefaultName(), field.getFieldName(), escapeHtml(defaultValue));
                                        }
                                    }
                                    default -> {
                                        String defaultValue = StringUtils.isNotBlank(field.getFieldStringValue()) ? field.getFieldStringValue() : "—";
                                        customFieldTable.addRowWithCode(field.getDefaultName(), field.getFieldName(), escapeHtml(defaultValue));
                                    }
                                }
                            }
                        }
                    }
                }
                if (customFieldTable.getRows().size() > 0) {
                    customFieldListDetials.put(map.getKey().toString(), customFieldTable);

                }
            }
            customFieldItems.add(customFieldListDetials);
        }
        return customFieldItems;
    }

    private List<CustomisedProductCategoriesITextTable> getGroupGroupNameTrainCustomFormData(FormItems item, Integer fid) {
        LinkedList<HashMap<CustomisedITextTable, CustomisedITextTable>> trainDatas = new LinkedList<>();

        CustomisedITextTable touristTable = null;
        CustomisedITextTable trainTable = null;

        EdsCustomForm customForm = customFormManager.get(fid);
        ModelForm modelForm = allInOneService.getModelGridForm(customForm.getFormID());
        Map<String, LinkedList<CustomizeFormItem>> gridColumnMap = modelForm.getGridColumnMap();

        if (gridColumnMap != null && gridColumnMap.size() > 0) {
            for (Map.Entry map : gridColumnMap.entrySet()) {
                CustomisedITextTable customFieldTable = new CustomisedITextTable();
                customFieldTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
                for (CustomizeFormItem customFieldItem : (List<CustomizeFormItem>) map.getValue()) {
                    if (item.getCustomFieldItems() != null && item.getCustomFieldItems().size() > 0) {
                        for (CompanyCustomFieldItem field : item.getCustomFieldItems()) {
                            if (field.getFieldName().equalsIgnoreCase(customFieldItem.getLabel()) &&
                                    field.getColumnCode().equalsIgnoreCase(customFieldItem.getName())) {
                                if (CompanyCustomFieldItem.TEXT.equals(field.getDataType())) {
                                    if (StringUtils.isNotEmpty(field.getFieldStringValue()) && (field.getUiType().equals(UI_TYPE_MULTI_LOOKUP) || field.getUiType().equals(TYPE_ENTITY_MULTI_LOOKUP)) && !Objects.equals("Exclusions", field.getAliasName())) {
                                        Gson gson = new Gson();
                                        SelectItem[] object = gson.fromJson(field.getFieldStringValue(), SelectItem[].class);
                                        for (SelectItem data : object) {
                                            FormItems formItems = commonService.getCustomFormItem(data.getId(), 4, null, false, null, null, null, null);
                                            HashMap<CustomisedITextTable, CustomisedITextTable> agenda = new HashMap<>();
                                            touristTable = new CustomisedITextTable();
                                            trainTable = new CustomisedITextTable();
                                            trainTable.addColumn("FROM", "");
                                            trainTable.addColumn("DEPARTURE_TIME", "");
                                            trainTable.addColumn("CLASS", "");
                                            trainTable.addColumn("TO", "");
                                            trainTable.addColumn("TRAIN", "");
                                            trainTable.addColumn("GROUP_NAME", "");
                                            List<String> values = Lists.newLinkedList();
                                            if (formItems.getCustomFieldItems() != null && formItems.getCustomFieldItems().size() > 0) {
                                                for (CompanyCustomFieldItem fieldItem : formItems.getCustomFieldItems()) {
                                                    if ("From".equalsIgnoreCase(fieldItem.getFieldName())) {
                                                        for (SelectItem selected : fieldItem.getQueryItems()) {
                                                            if (fieldItem.getFieldStringValue().equalsIgnoreCase(String.valueOf(selected.getId()))) {
                                                                values.add(selected.getName());
                                                            }
                                                        }
                                                    } else if ("To".equalsIgnoreCase(fieldItem.getFieldName())) {
                                                        for (SelectItem selected : fieldItem.getQueryItems()) {
                                                            if (fieldItem.getFieldStringValue().equalsIgnoreCase(String.valueOf(selected.getId()))) {
                                                                values.add(selected.getName());
                                                            }
                                                        }
                                                    } else if ("Departure time".equalsIgnoreCase(fieldItem.getFieldName())) {
                                                        String longDateFormat = "yyyy-MM-dd HH:mm";
                                                        SimpleDateFormat dateFormat = new SimpleDateFormat(longDateFormat);
                                                        String departureTime = fieldItem.getFieldDateNonConvertedValue() != null ? dateFormat.format(fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                                                        values.add(departureTime);
                                                    } else if ("Class".equalsIgnoreCase(fieldItem.getFieldName())) {
                                                        values.add(escapeHtml(fieldItem.getFieldStringValue()));
                                                    } else if ("Train".equalsIgnoreCase(fieldItem.getFieldName())) {
                                                        values.add(escapeHtml(fieldItem.getFieldStringValue()));
                                                    } else if ("Group Name".equalsIgnoreCase(fieldItem.getFieldName())) {
                                                        values.add(escapeHtml(fieldItem.getFieldStringValue()) + "_" + fieldItem.getSelectedId());
                                                    }
                                                }
                                                trainTable.addRow(values.toArray(new String[]{}));
                                                values.clear();
                                            }
                                            agenda.put(trainTable, touristTable);
                                            trainDatas.add(agenda);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Map<String, ArrayList<CustomisedITextTable>> itemMap = new LinkedHashMap<>();
        for (HashMap<CustomisedITextTable, CustomisedITextTable> trainData : trainDatas) {
            for (CustomisedITextTable customisedITextTable : trainData.keySet()) {
                for (HashMap<String, String> hashMap : customisedITextTable.getRows().values()) {
                    if (itemMap.containsKey(hashMap.get("GROUP_NAME"))) {
                        itemMap.get(hashMap.get("GROUP_NAME")).add(customisedITextTable);
                    } else {
                        itemMap.put(hashMap.get("GROUP_NAME"), new ArrayList<>(Collections.singletonList(customisedITextTable)));
                    }
                }
            }
        }

        List<CustomisedProductCategoriesITextTable> productCategoriesITextTable = new ArrayList<>();

        boolean isExclusion = false;
        for (Map.Entry<String, ArrayList<CustomisedITextTable>> entry : itemMap.entrySet()) {

            if (entry.getKey() == null)
                return productCategoriesITextTable;


            if (item.getCustomFieldItems() != null && item.getCustomFieldItems().size() > 0 && item.getCustomFieldItems().get(3) != null
                    && !item.getCustomFieldItems().get(3).getFieldStringValue().isEmpty()) {
                isExclusion = true;
            }

            if (isExclusion) {
                String longDateFormat = "yyyy-MM-dd HH:mm";
                SimpleDateFormat dateFormat = new SimpleDateFormat(longDateFormat);
                Date departureTimeDate = null;
                String groupName = "";
                List<Integer> sameDateAndGroupNameContactIds = new ArrayList<>();

                for (CustomisedITextTable groupValue : entry.getValue()) {
                    for (HashMap<String, String> hashMap : groupValue.getRows().values()) {
                        String[] groupNameArray = hashMap.get("GROUP_NAME").split("_");
                        groupName = groupNameArray[0];
                        String departureTime = hashMap.get("DEPARTURE_TIME");
                        try {
                            departureTimeDate = dateFormat.parse(departureTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (item.getCustomFieldItems() != null && item.getCustomFieldItems().size() > 0) {
                            for (CompanyCustomFieldItem field : item.getCustomFieldItems()) {
                                if (StringUtils.isNotEmpty(field.getFieldStringValue()) && (field.getUiType().equals(UI_TYPE_MULTI_LOOKUP) || field.getUiType().equals(TYPE_ENTITY_MULTI_LOOKUP)) && Objects.equals("Exclusions", field.getAliasName())) {
                                    Gson gson = new Gson();
                                    SelectItem[] object = gson.fromJson(field.getFieldStringValue(), SelectItem[].class);
                                    for (SelectItem data : object) {
                                        String[] name = data.getName().split(": ");
                                        String exclusionDepartureTime = name[3];
                                        String exclusionGroupName = !ServerUtils.isNullOrEmpty(name[2]) ? name[2].replace(" DepartureTime", "") : "";
                                        Date exclusionDepartureTimeDate = null;

                                        try {
                                            exclusionDepartureTimeDate = dateFormat.parse(exclusionDepartureTime);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        CustomisedITextTable table = new CustomisedITextTable();
                                        CustomisedProductCategoriesITextTable categoriesTable = new CustomisedProductCategoriesITextTable();
                                        Map<String, String> rows = new HashMap<>();
                                        String[] arrays = entry.getKey().split("_");
                                        rows.put(ITEM_CATEGORY, arrays[0]);
                                        categoriesTable.setRows(rows);

                                        if (departureTimeDate != null && exclusionDepartureTimeDate != null) {
                                            if (departureTimeDate.equals(exclusionDepartureTimeDate) && Objects.equals(groupName, exclusionGroupName)) {
                                                if (data.getSelectedId() != null) {
                                                    sameDateAndGroupNameContactIds.add(data.getSelectedId());
                                                }
                                                table = getGroupNameValuesTable(hashMap);
                                                categoriesTable.setTable(table);

                                                Integer groupId = Integer.valueOf(arrays[1]);
                                                categoriesTable.setInnerTable(getGroupNameContacts(groupId, sameDateAndGroupNameContactIds));
                                            } else if (!departureTimeDate.equals(exclusionDepartureTimeDate) && Objects.equals(groupName, exclusionGroupName)) {
                                                table = getGroupNameValuesTable(hashMap);
                                                categoriesTable.setTable(table);

                                                Integer groupId = Integer.valueOf(arrays[1]);
                                                categoriesTable.setInnerTable(getGroupNameContacts(groupId, sameDateAndGroupNameContactIds));
                                            } else {
                                                table = getGroupNameValuesTable(hashMap);
                                                categoriesTable.setTable(table);

                                                Integer groupId = Integer.valueOf(arrays[1]);
                                                categoriesTable.setInnerTable(getGroupNameContacts(groupId, sameDateAndGroupNameContactIds));
                                            }
                                            productCategoriesITextTable.add(categoriesTable);

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                CustomisedProductCategoriesITextTable categoriesTable = new CustomisedProductCategoriesITextTable();
                Map<String, String> rows = new HashMap<>();
                String[] arrays = entry.getKey().split("_");
                rows.put(ITEM_CATEGORY, arrays[0]);
                categoriesTable.setRows(rows);
                CustomisedITextTable table = getGroupNameValuesTableWithoutExculision(entry.getValue());
                categoriesTable.setTable(table);

                if (arrays != null && arrays.length > 0) {
                    Integer groupId = Integer.valueOf(arrays[1]);
                    categoriesTable.setInnerTable(getGroupNameContacts(groupId, null));
                }
                productCategoriesITextTable.add(categoriesTable);
            }

        }

        return productCategoriesITextTable;
    }

    private CustomisedITextTable getGroupNameValuesTableWithoutExculision(ArrayList<CustomisedITextTable> groupValues) {
        CustomisedITextTable groupNameTable = new CustomisedITextTable();
        groupNameTable.addColumn("FROM", "");
        groupNameTable.addColumn("DEPARTURE_TIME", "");
        groupNameTable.addColumn("CLASS", "");
        groupNameTable.addColumn("TO", "");
        groupNameTable.addColumn("TRAIN", "");
        groupNameTable.addColumn("GROUP_NAME", "");

        List<String> groupNameTableValues = Lists.newLinkedList();
        for (CustomisedITextTable groupValue : groupValues) {
            for (HashMap<String, String> hashMap : groupValue.getRows().values()) {
                groupNameTableValues.add(hashMap.get("FROM"));
                groupNameTableValues.add(hashMap.get("DEPARTURE_TIME"));
                groupNameTableValues.add(hashMap.get("CLASS"));
                groupNameTableValues.add(hashMap.get("TO"));
                groupNameTableValues.add(hashMap.get("TRAIN"));
                groupNameTableValues.add(hashMap.get("GROUP_NAME"));
            }
            groupNameTable.addRow(groupNameTableValues.toArray(new String[]{}));
            groupNameTableValues.clear();
        }
        return groupNameTable;
    }

    private CustomisedITextTable getGroupNameValuesTable(HashMap<String, String> hashMap) {
        CustomisedITextTable groupNameTable = new CustomisedITextTable();
        groupNameTable.addColumn("FROM", "");
        groupNameTable.addColumn("DEPARTURE_TIME", "");
        groupNameTable.addColumn("CLASS", "");
        groupNameTable.addColumn("TO", "");
        groupNameTable.addColumn("TRAIN", "");
        groupNameTable.addColumn("GROUP_NAME", "");

        List<String> groupNameTableValues = Lists.newLinkedList();

        groupNameTableValues.add(hashMap.get("FROM"));
        groupNameTableValues.add(hashMap.get("DEPARTURE_TIME"));
        groupNameTableValues.add(hashMap.get("CLASS"));
        groupNameTableValues.add(hashMap.get("TO"));
        groupNameTableValues.add(hashMap.get("TRAIN"));
        groupNameTableValues.add(hashMap.get("GROUP_NAME"));
        groupNameTable.addRow(groupNameTableValues.toArray(new String[]{}));
        groupNameTableValues.clear();

        return groupNameTable;
    }

    private CustomisedITextTable getGroupNameContacts(Integer groupId, List<Integer> sameDateAndGroupNameContactIds) {
        CustomisedITextTable contactTable = new CustomisedITextTable();
        contactTable.addColumn("NO", "");
        contactTable.addColumn("NAME", "");
        contactTable.addColumn("DATE_OF_BIRTH", "");
        contactTable.addColumn("NATIONALITY", "");
        contactTable.addColumn("SERIAL_NUMBER", "");
        contactTable.addColumn("GENDER", "");
        List<EdsCrmContact> crmContacts = groupId != null ? clientManager.getContacts(groupId) : null;

        if (crmContacts != null && crmContacts.size() > 0 && sameDateAndGroupNameContactIds != null && sameDateAndGroupNameContactIds.size() > 0) {
            for (Integer id : sameDateAndGroupNameContactIds) {
                crmContacts.removeIf(e -> e.getObjectID().equals(id));
            }
        }

        List<String> conctactValues = Lists.newLinkedList();
        int count = 0;
        if (crmContacts != null && crmContacts.size() > 0) {
            for (EdsCrmContact contactData : crmContacts) {
                ContactListItem contactItem = contactService.getContact(contactData.getObjectID(), false);
                count = count + 1;
                conctactValues.add(String.valueOf(count));
                conctactValues.add(contactItem.getName());
                if (contactItem.getCustomFields() != null && !contactItem.getCustomFields().isEmpty()) {
                    for (CompanyCustomFieldItem customField : contactItem.getCustomFields()) {
                        if ("Nationality".equalsIgnoreCase(customField.getFieldName())) {
                            conctactValues.add(customField.getFieldStringValue());
                        } else if ("Serial number".equalsIgnoreCase(customField.getFieldName())) {
                            conctactValues.add(customField.getFieldStringValue());
                        } else if ("Date of birth".equalsIgnoreCase(customField.getFieldName())) {
                            String dateValue = "";
                            if (customField.getFieldDateNonConvertedValue() != null) {
                                EdsUser user = userManager.getUser();
                                EdsCompany company = user.getCompany();
                                SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(company);
                                if (company.getLocale() != null && "ru".equals(company.getLocale())) {
                                    Locale ruLocale = new Locale("ru", "RU");
                                    SimpleDateFormat ruDateFormat = new SimpleDateFormat(shortDateFormat.toPattern(), ruLocale);
                                    dateValue = customField.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? ruDateFormat.format(customField.getFieldDateNonConvertedValue().getNonConvertedDate()) : "—";
                                } else {
                                    dateValue = customField.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? shortDateFormat.format(customField.getFieldDateNonConvertedValue().getNonConvertedDate()) : "—";
                                }
                            }
                            conctactValues.add(dateValue);
                        } else if ("Sex".equalsIgnoreCase(customField.getFieldName())) {
                            conctactValues.add(customField.getFieldStringValue());
                        }
                    }
                }
                contactTable.addRow(conctactValues.toArray(new String[]{}));
                conctactValues.clear();
            }
        }
        return contactTable;
    }

    private LinkedList<HashMap<CustomisedITextTable, CustomisedITextTable>> getTraintTicketShapkaData(FormItems item, EdsUser user, Integer fid) {
        LinkedList<HashMap<CustomisedITextTable, CustomisedITextTable>> trainDatas = new LinkedList<>();

        EdsCustomForm customForm = customFormManager.get(fid);
        ModelForm modelForm = allInOneService.getModelGridForm(customForm.getFormID());
        Map<String, LinkedList<CustomizeFormItem>> gridColumnMap = modelForm.getGridColumnMap();

        if (gridColumnMap != null && gridColumnMap.size() > 0) {
            for (Map.Entry map : gridColumnMap.entrySet()) {
                CustomisedITextTable customFieldTable = new CustomisedITextTable();
                customFieldTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
                for (CustomizeFormItem customFieldItem : (List<CustomizeFormItem>) map.getValue()) {
                    if (item.getCustomFieldItems() != null && item.getCustomFieldItems().size() > 0) {
                        for (CompanyCustomFieldItem field : item.getCustomFieldItems()) {
                            if (field.getFieldName().equalsIgnoreCase(customFieldItem.getLabel()) &&
                                    field.getColumnCode().equalsIgnoreCase(customFieldItem.getName())) {
                                if (CompanyCustomFieldItem.TEXT.equals(field.getDataType())) {
                                    if (StringUtils.isNotEmpty(field.getFieldStringValue()) && (field.getUiType().equals(UI_TYPE_MULTI_LOOKUP) || field.getUiType().equals(TYPE_ENTITY_MULTI_LOOKUP))) {
                                        Gson gson = new Gson();
                                        SelectItem[] object = gson.fromJson(field.getFieldStringValue(), SelectItem[].class);
                                        for (SelectItem data : object) {
                                            FormItems formItems = commonService.getCustomFormItem(data.getId(), 4, null, false, null, null, null, null);
                                            HashMap<CustomisedITextTable, CustomisedITextTable> agenda = new HashMap<>();
                                            CustomisedITextTable touristTable = new CustomisedITextTable();
                                            CustomisedITextTable trainTable = new CustomisedITextTable();
                                            trainTable.addColumn("FROM", "");
                                            trainTable.addColumn("DEPARTURE_DATE", "");
                                            trainTable.addColumn("DEPARTURE_TIME", "");
                                            trainTable.addColumn("CLASS", "");
                                            trainTable.addColumn("TO", "");
                                            trainTable.addColumn("TRAIN", "");
                                            trainTable.addColumn("ARRIVAL_TIME", "");
                                            trainTable.addColumn("CARRIAGE", "");
                                            trainTable.addColumn("GROUP_NAME", "");
                                            trainTable.addColumn("SEAT", "");
                                            trainTable.addColumn("TRAIN_NO", "");
                                            List<String> values = Lists.newLinkedList();
                                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                                            if (formItems.getCustomFieldItems() != null && formItems.getCustomFieldItems().size() > 0) {
                                                for (CompanyCustomFieldItem fieldItem : formItems.getCustomFieldItems()) {
                                                    if ("From".equalsIgnoreCase(fieldItem.getFieldName())) {
                                                        for (SelectItem selected : fieldItem.getQueryItems()) {
                                                            if (fieldItem.getFieldStringValue().equalsIgnoreCase(String.valueOf(selected.getId()))) {
                                                                values.add(selected.getName());
                                                            }
                                                        }
                                                    } else if ("To".equalsIgnoreCase(fieldItem.getFieldName())) {
                                                        for (SelectItem selected : fieldItem.getQueryItems()) {
                                                            if (fieldItem.getFieldStringValue().equalsIgnoreCase(String.valueOf(selected.getId()))) {
                                                                values.add(selected.getName());
                                                            }
                                                        }
                                                    } else if ("Departure time".equalsIgnoreCase(fieldItem.getFieldName())) {
                                                        String departureDate = "";
                                                        EdsCompany company = userManager.getUser().getCompany();
                                                        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(company);
                                                        if (company.getLocale() != null && "ru".equals(company.getLocale())) {
                                                            Locale ruLocale = new Locale("ru", "RU");
                                                            SimpleDateFormat ruDateFormat = new SimpleDateFormat(shortDateFormat.toPattern(), ruLocale);
                                                            departureDate = fieldItem.getFieldDateNonConvertedValue() != null ? ruDateFormat.format(fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                                                        } else {
                                                            departureDate = fieldItem.getFieldDateNonConvertedValue() != null ? shortDateFormat.format(fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                                                        }
                                                        String departureTime = fieldItem.getFieldDateNonConvertedValue() != null ? timeFormat.format(fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                                                        values.add(departureDate);
                                                        values.add(departureTime);
                                                    } else if ("Class".equalsIgnoreCase(fieldItem.getFieldName())) {
                                                        values.add(escapeHtml(fieldItem.getFieldStringValue()));
                                                    } else if ("Train".equalsIgnoreCase(fieldItem.getFieldName())) {
                                                        values.add(escapeHtml(fieldItem.getFieldStringValue()));
                                                    } else if ("Group Name".equalsIgnoreCase(fieldItem.getFieldName())) {
                                                        values.add(escapeHtml(fieldItem.getFieldStringValue()));
                                                    } else if ("Arr date".equalsIgnoreCase(fieldItem.getFieldName())) {
                                                        String arrivalTime = fieldItem.getFieldDateNonConvertedValue() != null ? timeFormat.format(fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                                                        values.add(arrivalTime);
                                                    } else if ("Seat".equalsIgnoreCase(fieldItem.getFieldName())) {
                                                        values.add(escapeHtml(fieldItem.getFieldStringValue()));
                                                    } else if ("Vagon".equalsIgnoreCase(fieldItem.getFieldName())) {
                                                        values.add(escapeHtml(fieldItem.getFieldStringValue()));
                                                    } else if ("Train №".equalsIgnoreCase(fieldItem.getFieldName())) {
                                                        values.add(escapeHtml(fieldItem.getFieldStringValue()));
                                                    }
                                                    //rows data
                                                }
                                                trainTable.addRow(values.toArray(new String[]{}));
                                                values.clear();
                                            }
                                            agenda.put(trainTable, touristTable);
                                            trainDatas.add(agenda);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return trainDatas;
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.CUSTOM_FORM_ITEM_VIEW;
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        return ((CustomFormItemRequestObject) dataClass).getIS_LANDSCAPE() ? PdfParams.Orientation.landscape : null;
    }

    private String getFileAndTableName(CustomFormItemRequestObject requestObject, boolean fileNameForCustomer) {
        EdsCustomForm customForm = null;
        Integer objecId = null;
        Integer fid = null;
        if (requestObject != null && requestObject.getFid() != null) {
            customForm = customFormManager.get(requestObject.getFid());
        }
        if (requestObject != null) {
            objecId = requestObject.getObjectID();
            fid = requestObject.getFid();
        }
        String numberValue = "";
        Integer customerId = null;
        boolean autoNumber = false;
        FormItems formItems = commonService.getCustomFormItem(objecId, fid, null, false, null, null, null, null);
        if (formItems.getCustomFieldItems() != null && formItems.getCustomFieldItems().size() > 0) {
            for (CompanyCustomFieldItem field : formItems.getCustomFieldItems()) {
                if (Objects.equals("AutoNumber", field.getUiType())) {
                    numberValue = field.getFieldStringValue();
                    autoNumber = true;
                } else if (!autoNumber) {
                    numberValue = field.getFieldStringValue();
                }
                if (field.getLookUpTypeEnum() != null && field.getLookUpTypeEnum().name().equalsIgnoreCase(Constants.CUSTOMER) && field.getSelectedId() != null && fileNameForCustomer) {
                    customerId = field.getSelectedId();
                }
            }
        }
        EdsCrmAccount customer = customerId != null ? crmAccountManager.get(customerId) : null;
        if (StringUtils.isNotEmpty(numberValue)) {
            numberValue = " - " + numberValue;
        }
        String tableName = "";
        if (fileNameForCustomer) {
            if (customer != null) {
                tableName = escapeHtml(customer.getName()) + numberValue;
            } else {
                tableName = customForm != null ? escapeHtml(customForm.getName()) + numberValue : "";
            }
        } else {
            tableName = customForm != null ? escapeHtml(customForm.getName()) + numberValue : "List";
        }
        return tableName;
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        CustomFormItemRequestObject requestObject = (CustomFormItemRequestObject) object;
        return requestObject.getPdfTemplateID();
    }
}
