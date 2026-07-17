package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsEmployeeDepartment;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.EmployeeDepartmentManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.team.client.rpc.DepartmentService;
import com.edatasite.workforce.gwt.team.client.rpc.TeamListItem;
import com.google.common.collect.Lists;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

public class DepartmentViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private EmployeeDepartmentManager employeeDepartmentManager;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        RequestObject requestObject = (RequestObject) dataClass;
        TeamListItem item = departmentService.getTeam(requestObject.getObjectID());

        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        CustomisedITextTable departmentTable = new CustomisedITextTable();
        departmentTable.setName(item.getName());
        departmentTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        departmentTable.addRowWithCode(NAME, commonLocalizer.localize(PdfLocalizationName.department), escapeHtml(item.getName()));
        departmentTable.addRowWithCode(LEADER, commonLocalizer.localize(PdfLocalizationName.leader), escapeHtml(item.getLeader()));
        departmentTable.addRowWithCode(DESCRIPTION, commonLocalizer.localize(PdfLocalizationName.description), escapeHtml(item.getDescription()));
        departmentTable.addRowWithCode(EXP_START_DATE, commonLocalizer.localize(PdfLocalizationName.startDate), item.getStartDate() != null ? ServerUtils.convertDateFormatFromEngToUzb(dateFormat(item.getStartDate())) : "");
        departmentTable.addRowWithCode(EXP_END_DATE, commonLocalizer.localize(PdfLocalizationName.endDate), item.getEndDate() != null ? ServerUtils.convertDateFormatFromEngToUzb(dateFormat(item.getEndDate())) : "");
        departmentTable.addRowWithCode(PARENT_NAME, commonLocalizer.localize(PdfLocalizationName.reportsTo), item.getParentDepartment() != null ? escapeHtml(item.getParentDepartment().getName()) : "");
        customData.put("DEPARTMENT_TABLE", departmentTable);

        CustomisedITextTable employeeTable = new CustomisedITextTable();
        employeeTable.setName(commonLocalizer.localize(PdfLocalizationName.departmentEmployees));
        employeeTable.addColumn(NAME, commonLocalizer.localize(PdfLocalizationName.employee));
        employeeTable.addColumn(EMAIL, commonLocalizer.localize(PdfLocalizationName.email));
        employeeTable.addColumn(PHONE, commonLocalizer.localize(PdfLocalizationName.phone));
        employeeTable.addColumn(POSITION, commonLocalizer.localize(PdfLocalizationName.position));
        List<EdsEmployeeDepartment> edsEmployeeDepartments = employeeDepartmentManager.getTeamEmployees(requestObject.getObjectID());
        List<String> columnsValue = Lists.newArrayList();
        if (edsEmployeeDepartments != null) {
            for (EdsEmployeeDepartment edsEmployeeDepartment : edsEmployeeDepartments) {
                columnsValue.clear();
                if (edsEmployeeDepartment.getEmployee() != null) {
                    columnsValue.add(escapeHtml(edsEmployeeDepartment.getEmployee().getFullName()));
                    columnsValue.add(escapeHtml(edsEmployeeDepartment.getEmployee().getEmail()));
                    if (edsEmployeeDepartment.getEmployee().getPrimaryPhone() != null) {
                        columnsValue.add(escapeHtml(edsEmployeeDepartment.getEmployee().getPrimaryPhone()));
                    } else {
                        columnsValue.add(escapeHtml(edsEmployeeDepartment.getEmployee().getParamByRelation()));
                    }
                    if (edsEmployeeDepartment.getEmployee().getPosition() != null) {
                        columnsValue.add(escapeHtml(edsEmployeeDepartment.getEmployee().getPosition().getName()));
                    } else {
                        columnsValue.add("");
                    }
                } else {
                    columnsValue.add("");
                    columnsValue.add("");
                    columnsValue.add("");
                    columnsValue.add("");
                }
                employeeTable.addRow(columnsValue.toArray(new String[]{}));
            }
        }
        customData.put("EMPLOYEE_TABLE", employeeTable);

        ITextGenericPdfData pdf = new ITextGenericPdfData();
        pdf.setCustomData(customData);
        return pdf;
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        return ((RequestObject) dataClass).getIS_LANDSCAPE() ? PdfParams.Orientation.landscape : null;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        return new RequestObject();
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
        Integer departmentId = requestObject.getObjectID();
        TeamListItem item = departmentService.getTeam(departmentId);
        setFileName((item.getName().length() > 24 ? item.getName().substring(0, 24) : item.getName()) + "_" + dateFormat(user.getUserDate()));
    }

    @Override
    protected String getTableName(Object dataClass) {
        return commonLocalizer.localize(PdfLocalizationName.department);
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.DEPARTMENT;
    }

}
