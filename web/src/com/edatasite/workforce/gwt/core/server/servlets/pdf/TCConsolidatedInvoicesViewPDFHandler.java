package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.trainingcenter.EdsTCScheduledTask;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.CourseScheduleStudentManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.TCScheduledTaskManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextUserData;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_en;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 9/3/13
 * Time: 12:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class TCConsolidatedInvoicesViewPDFHandler extends AbstractITextPostPdfHandler implements IPostPDFHandler, PDFConstants {

    private static final Integer PDO_CUSTOMER_ID = 33;

    @Autowired
    private TCScheduledTaskManager tcScheduledTaskManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private ClientContactManager clientContactManager;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private CourseScheduleStudentManager courseScheduleStudentManager;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParameter = (ListingFilterParameter) dataClass;
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.CUSTOMVIEW);

        EdsTCScheduledTask tcScheduledTask = tcScheduledTaskManager.get(filterParameter.getObjectId());
        EdsCrmAccount customer = crmAccountManager.get(tcScheduledTask.getCustomerID());
        EdsUser edsUser = userManager.get(tcScheduledTask.getUserID());
        EdsCompany edsCompany = edsUser.getCompany();

        filterParameter.setCrmAccountId(customer.getObjectID());
        filterParameter.setStartDate(tcScheduledTask.getPeriodStart());
        filterParameter.setEndDate(tcScheduledTask.getPeriodEnd());

        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(edsUser.getCompany());
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMMM yyyy");

        // Company Data
        pdfData.setCompanyData(getCompanyData(edsCompany, true, hasPhantom));

        // User Data
        ITextUserData userData = new ITextUserData();
        userData.setFullName(edsUser.getFullName());
        if (edsUser.isEmployee()) {
            EdsEmployee emp = employeeManager.get(edsUser.getObjectID());
            userData.setPhone(Utils.formatPhoneNumber((emp.getWorkPhoneFirst() != null && !emp.getWorkPhoneFirst().equals("")) ? escapeHtml(emp.getWorkPhoneFirst()) : ""));
            userData.setEmail(edsUser.getEmail() != null && !edsUser.getEmail().equals("") ? escapeHtml(edsUser.getEmail()) : "");
            userData.setPosition(emp.getPosition() != null ? emp.getPosition().getName() : "");
        }
        pdfData.setUserData(userData);

        boolean isPDOCustomer = PDO_CUSTOMER_ID.equals(customer.getObjectID());

        //Content Data
        HashMap<String, CustomisedITextTable> customDataMap = new HashMap<>();
        customDataMap.put("ADDRESS_TABLE", getCustomerAddressTable(customer, isPDOCustomer));

        CustomisedITextTable numberDateTable = new CustomisedITextTable();
        numberDateTable.addColumnOrder(COLUMN_VALUE);
        numberDateTable.addRowWithCode(INV_DATE, shortDateFormat.format(edsUser.getUserDate()));
        numberDateTable.addRowWithCode("START_DATE", shortDateFormat.format(tcScheduledTask.getPeriodStart()));
        numberDateTable.addRowWithCode("END_DATE", shortDateFormat.format(tcScheduledTask.getPeriodEnd()));
        numberDateTable.addRowWithCode("START_MONTH", monthYearFormat.format(tcScheduledTask.getPeriodStart()));
        numberDateTable.addRowWithCode("END_MONTH", monthYearFormat.format(tcScheduledTask.getPeriodEnd()));
        customDataMap.put("NUMBER_DATE_TABLE", numberDateTable);

        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(company, null);
        CustomisedITextTable itemsTable = new CustomisedITextTable();
        CustomisedITextTable totalsTable = new CustomisedITextTable();

        BigDecimal invoiceTotalAmount = BigDecimal.ZERO;
        if (isPDOCustomer) {
            itemsTable.addColumnOrder(ITEM_NO, ITEM_NAME, ITEM_AMOUNT);
            List<Object[]> parentSubjectsList = courseScheduleStudentManager.getParentCourseSubjects();
            HashMap<Integer, BigDecimal> dataMap = courseScheduleStudentManager.getCourseSubjectsConsolidatedData(filterParameter, true);
            for (Object[] subjectData : parentSubjectsList) {
                Integer subjectID = (Integer) subjectData[0];
                String subjectName = (String) subjectData[1];
                String subjectCGroup = (String) subjectData[2];
                BigDecimal subjectTotalAmount = dataMap.get(subjectID);
                subjectTotalAmount = (subjectTotalAmount != null ? subjectTotalAmount : BigDecimal.ZERO);
                invoiceTotalAmount = invoiceTotalAmount.add(subjectTotalAmount);

                if (BigDecimal.ZERO.compareTo(subjectTotalAmount) < 0) {
                    itemsTable.addRow(escapeHtml(subjectCGroup != null ? subjectCGroup : ""), escapeHtml(subjectName != null ? subjectName : ""), priceScaleNumberFormat.format(subjectTotalAmount));
                }
            }
        } else {
            itemsTable.addColumnOrder(ITEM_NAME, ITEM_AMOUNT);
            List<EdsLocation> locationsList = locationManager.getLocations(new ListingFilterParameter());
            HashMap<Integer, BigDecimal> dataMap = courseScheduleStudentManager.getCourseSubjectsConsolidatedData(filterParameter, false);
            for (EdsLocation location : locationsList) {
                Integer locationID = location.getObjectID();
                String locationName = location.getName();
                BigDecimal locationTotalAmount = dataMap.get(locationID);
                locationTotalAmount = (locationTotalAmount != null ? locationTotalAmount : BigDecimal.ZERO);
                invoiceTotalAmount = invoiceTotalAmount.add(locationTotalAmount);

                if (BigDecimal.ZERO.compareTo(locationTotalAmount) < 0) {
                    itemsTable.addRow(escapeHtml(locationName != null ? locationName : ""), priceScaleNumberFormat.format(locationTotalAmount));
                }
            }
        }

        totalsTable.addColumnOrder(ITEM_NO, ITEM_NAME, ITEM_AMOUNT);
        NumberToWord numberToWordConverter = new NumberToWord_en();
        String numberAsWordString = numberToWordConverter.toWord(invoiceTotalAmount.abs());
        totalsTable.addRow("Total", escapeHtml(numberAsWordString != null ? numberAsWordString : ""), priceScaleNumberFormat.format(invoiceTotalAmount));

        customDataMap.put("ITEMS_TABLE", itemsTable);
        customDataMap.put("TOTALS_TABLE", totalsTable);

        pdfData.setCustomData(customDataMap);
        return pdfData;
    }

    private CustomisedITextTable getCustomerAddressTable(EdsCrmAccount customer, boolean isPDOCustomer) {
        CustomisedITextTable addressTable = new CustomisedITextTable();
        addressTable.addColumnOrder(COLUMN_VALUE);

        addressTable.addRowWithCode("IS_PDO_CUSTOMER", isPDOCustomer ? "TRUE" : "FALSE");

        EdsCrmContact clientContact = clientContactManager.getPrimaryClientContact(customer.getObjectID());
        if (clientContact == null) {
            List<EdsCrmContact> customerContacts = clientManager.getContacts(customer.getObjectID());
            if (customerContacts != null && customerContacts.size() > 0) {
                clientContact = customerContacts.get(0);
            }
        }
        if (clientContact != null) {
            addressTable.addRowWithCode(CLIENT_CONTACT, escapeHtml(clientContact.getName() != null ? clientContact.getName() : ""));
        }
        addressTable.addRowWithCode(PDFConstants.CUSTOMER, escapeHtml(customer.getName() != null ? customer.getName() : ""));

        EdsAddress billAddress = customer.getBillingAddress();
        if (billAddress != null) {
            addressTable.addRowWithCode(BILL_ADDRESS_NAME, escapeHtml(billAddress.getName() != null ? billAddress.getName() : ""));
            addressTable.addRowWithCode(BILL_ADDRESS, escapeHtml(billAddress.getAddress() != null ? billAddress.getAddress() : ""));
            addressTable.addRowWithCode(BILL_ADDRESS2, escapeHtml(billAddress.getAddressb() != null ? billAddress.getAddressb() : ""));
            addressTable.addRowWithCode(BILL_COUNTRY, escapeHtml(billAddress.getCountry() != null ? billAddress.getCountry().getName() : ""));
            addressTable.addRowWithCode(BILL_CITY, escapeHtml(billAddress.getCity() != null ? billAddress.getCity() : ""));
            addressTable.addRowWithCode(BILL_STATE, escapeHtml((billAddress.getState() != null && billAddress.getState().getName() != null) ? billAddress.getState().getName() : ""));
            addressTable.addRowWithCode(BILL_ZIPCODE, escapeHtml(billAddress.getZipCode() != null ? billAddress.getZipCode() : ""));
        }

        EdsAddress mailAddress = customer.getMailingAddress();
        if (mailAddress != null) {
            addressTable.addRowWithCode(MAIL_ADDRESS_NAME, escapeHtml(mailAddress.getName() != null ? mailAddress.getName() : ""));
            addressTable.addRowWithCode(MAIL_ADDRESS, escapeHtml(mailAddress.getAddress() != null ? mailAddress.getAddress() : ""));
            addressTable.addRowWithCode(MAIL_ADDRESS2, escapeHtml(mailAddress.getAddressb() != null ? mailAddress.getAddressb() : ""));
            addressTable.addRowWithCode(MAIL_COUNTRY, escapeHtml(mailAddress.getCountry() != null ? mailAddress.getCountry().getName() : ""));
            addressTable.addRowWithCode(MAIL_CITY, escapeHtml(mailAddress.getCity() != null ? mailAddress.getCity() : ""));
            addressTable.addRowWithCode(MAIL_STATE, escapeHtml((mailAddress.getState() != null && mailAddress.getState().getName() != null) ? mailAddress.getState().getName() : ""));
            addressTable.addRowWithCode(MAIL_ZIPCODE, escapeHtml(mailAddress.getZipCode() != null ? mailAddress.getZipCode() : ""));
        }

        return addressTable;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        return new ListingFilterParameter();
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        Integer scheduledTaskID = fp.getObjectId();
        EdsTCScheduledTask scheduledTask = tcScheduledTaskManager.get(scheduledTaskID);
        EdsCrmAccount crmAccount = crmAccountManager.get(scheduledTask.getCustomerID());

        StringBuilder fileName = new StringBuilder();
        fileName.append("Consolidated_Invoice_By_Subjects_");
        fileName.append(crmAccount.getName());
        fileName.append(dateFormat.format(scheduledTask.getPeriodStart()) + "_" + dateFormat.format(scheduledTask.getPeriodEnd()) + "_");
        if (fp.getLocationId() != null) {
            EdsLocation location = locationManager.get(fp.getLocationId());
            if (location != null) {
                fileName.append(location.getName());
            }
        }

        setFileName(fileName.toString());
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.TC_CONSOLIDATED_INVOICE;
    }
}
