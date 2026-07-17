package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsBrigada;
import com.edatasite.workforce.core.domain.EdsBrigadaEmployee;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.BrigadaManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeTaskManager;
import com.edatasite.workforce.gwt.core.server.db.IssueManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.lucene.parser.HTMLParser;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextCompanyData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BrigadaViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {

    private static Logger log = LoggerFactory.getLogger(BrigadaViewPDFHandler.class);
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private EmployeeTaskManager employeeTaskManager;
    @Autowired
    private BrigadaManager brigadaManager;
    @Autowired
    private TimeSheetManager timeSheetManager;
    @Autowired
    private IssueManager issueManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HrmsService hrmsService;
    @Autowired
    private EmployeeManager employeeManager;
    private DecimalFormat numberFormat = new DecimalFormat("###.##");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        long begin = System.currentTimeMillis();
        boolean isKendah = company.getObjectID().equals(56895);

        HTMLParser htmlParser = new HTMLParser();
        ITextGenericPdfData pdf = new ITextGenericPdfData();
        pdf.setPdfViewType(ITextPdfViewTypeEnum.SUMMARYVIEW);

        ITextCompanyData companyData = new ITextCompanyData();
        pdf.setCompanyData(companyData);
        pdf.setCompanyData(companyData);

        // Company Data
        companyData.setCompanyName(escapeHtml(company.getName()));
        companyData.setAddress(company.getAddress1() != null ? company.getAddress1() : "");
        companyData.setCity(company.getCity() != null ? company.getCity() : "");
        companyData.setPostCode((company.getPostCode() != null && !"".equals(company.getPostCode())) ? company.getPostCode() : "");
        companyData.setCountry((company.getCountryZone() != null && company.getCountryZone().getCountry() != null) ? company.getCountryZone().getCountry().getName() : "");
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        baseInvoice.setCustomProductTable(getGroups(((RequestObject) dataClass).getObjectID()));
        baseInvoice.setCustomNumberAndDatesTable(getCustomNumberAndDatesTable((((RequestObject) dataClass).getObjectID())));
        pdf.setBaseInvoice(baseInvoice);

        return pdf;
    }

    protected CustomisedITextTable getCustomNumberAndDatesTable(Integer id) {
        CustomisedITextTable numAndDates = new CustomisedITextTable();
        EdsBrigada edsBrigada = brigadaManager.get(id);

        String numberValue = "";
        String numberName = "";
        if (edsBrigada.getNumber() != null) {
            numberName = edsBrigada.getNumber();
            numberValue = commonLocalizer.localize("number");
        }

        String statusValue = "";
        String statusName = "";
        if (edsBrigada.getStatus() != null) {
            statusName = edsBrigada.getStatus().getName();
            statusValue = commonLocalizer.localize("status");
        }

        String nameValue = "";
        String name = "";
        if (edsBrigada.getName() != null) {
            name = edsBrigada.getName();
            nameValue = commonLocalizer.localize("name");
        }

        String managerValue = "";
        String managerName = "";
        if (edsBrigada.getManager() != null) {
            managerName = edsBrigada.getManager().getFullName();
            managerValue = commonLocalizer.localize("manager");
        }

        String backupManagerValue = "";
        String backupManagerName = "";
        if (edsBrigada.getBackupManager() != null) {
            backupManagerName = edsBrigada.getBackupManager().getFullName();
            backupManagerValue = "Backup Manager";
        }
        numAndDates.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);
        numAndDates.addRowWithCode(Constants.NUMBER, commonLocalizer.localize(PdfLocalizationName.number), escapeHtml(numberName));
        numAndDates.addRowWithCode(PDFConstants.STATUS, commonLocalizer.localize(PdfLocalizationName.status), escapeHtml(statusName));
        numAndDates.addRowWithCode(PDFConstants.NAME, commonLocalizer.localize(PdfLocalizationName.name), escapeHtml(name));
        numAndDates.addRowWithCode(PDFConstants.PROJECT_MANAGER, commonLocalizer.localize(PdfLocalizationName.manager), escapeHtml(managerName));
        numAndDates.addRowWithCode(PDFConstants.PROJECT_BACKUP_MANAGERS, commonLocalizer.localize(PdfLocalizationName.backupManager), escapeHtml(backupManagerName));
        numAndDates.addRowWithCode("TEAM_INFORMATION", commonLocalizer.localize("team"), escapeHtml("team"));
        numAndDates.addRowWithCode("EMPLOYEE_NAME", commonLocalizer.localize(PdfLocalizationName.employee), escapeHtml("name"));
        numAndDates.addRowWithCode("POSITION", commonLocalizer.localize(PdfLocalizationName.position), escapeHtml("position"));
        numAndDates.addRowWithCode("DEPARTMENT", commonLocalizer.localize(PdfLocalizationName.department), escapeHtml("department"));

        return numAndDates;
    }


    private CustomisedITextTable getGroups(Integer id) {
        CustomisedITextTable customisedITextTable = new CustomisedITextTable();
        customisedITextTable.addColumn(EMPLOYEE_NAME, commonLocalizer.localize(PdfLocalizationName.name));
        customisedITextTable.addColumn(PDFConstants.POSITION, commonLocalizer.localize(PdfLocalizationName.position));
        customisedITextTable.addColumn(Constants.DEPARTMENT, commonLocalizer.localize(PdfLocalizationName.department));
        List<EdsBrigadaEmployee> employeesByBrigada = brigadaManager.getEmployeesByBrigada(id);
        for (EdsBrigadaEmployee employee : employeesByBrigada) {
            ArrayList<String> row = new ArrayList<>();
            String employeeName = "";
            employeeName = escapeHtml(employee.getEmployeeDepartment().getEmployee().getFullName());

            String position = "";
            position = escapeHtml(employee.getEmployeeDepartment().getEmployee().getPosition().getName());

            String department = "";
            department = escapeHtml(employee.getEmployeeDepartment().getTeam().getName());
            row.add(employeeName);
            row.add(position);
            row.add(department);
            customisedITextTable.addRow(row.toArray(new String[]{}));
        }

        return customisedITextTable;
    }


    public CustomisedITextTable getCurrentData(EdsUser user) {
        CustomisedITextTable customTable = new CustomisedITextTable();
        customTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        customTable.addRowWithCode(CURRENT_DATE, "", shortDateFormat.format(ServerUtils.convertServerDateToUserDate(new Date(), user.getUserTimezone())));
        customTable.addRowWithCode("CURRENT_YEAR", "", Calendar.getInstance().get(Calendar.YEAR) + "");
        customTable.addRowWithCode("CURRENT_TIME", "", timeFormat.format(userManager.getUser().getUserDate()));
        return customTable;
    }


    private String getSize(Long size, double division) {
        Double res = Double.valueOf(size.toString()) / division;
        NumberFormat format = new DecimalFormat("######.#");
        return format.format(res);
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        return ((RequestObject) dataClass).getIS_LANDSCAPE() ? PdfParams.Orientation.landscape : null;
    }

    protected Object getDataClass(HttpServletRequest request) {
        return new RequestObject();
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
        Integer projectId = requestObject.getObjectID();
        EdsBrigada brigada = brigadaManager.get(projectId);
        setFileName((brigada.getName().length() > 24 ? brigada.getName().substring(0, 24) : brigada.getName()) + "_" + dateFormat(new Date()));
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.BRIGADA;
    }

    @Override
    protected String getTableName(Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
        EdsBrigada edsBrigada = brigadaManager.get(requestObject.getObjectID());
        String projectNumber = edsBrigada != null ? escapeHtml(edsBrigada.getNumber()) : "";
        String projectName = brigadaManager != null ? escapeHtml(edsBrigada.getName()) : "";
        return projectNumber + " - " + projectName;
    }
}
