package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueItem;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueService;
import com.google.common.collect.Lists;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * User: Xushnud
 * Date: 28.12.2009
 * Time: 14:32:11
 */
public class IssueListViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {

    private IssueService issueService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        RequestObject requestObject = (RequestObject) dataClass;
        IssueItem item = issueService.editIssueItem(requestObject.getObjectID(), null);

        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        CustomisedITextTable departmentTable = new CustomisedITextTable();
        departmentTable.setName(commonLocalizer.localize(PdfLocalizationName.issueDetails));
        departmentTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        departmentTable.addRowWithCode("NUMBER", commonLocalizer.localize(PdfLocalizationName.number), item.getNumberData() != null ? escapeHtml(item.getNumberData().getNumberString()) : "");
        departmentTable.addRowWithCode("PROJECT_NAME", commonLocalizer.localize(PdfLocalizationName.project), escapeHtml(item.getProjectName()));
        departmentTable.addRowWithCode("ISSUE_NAME", commonLocalizer.localize(PdfLocalizationName.issue), item.getName());
        departmentTable.addRowWithCode("ISSUE_PERIOD", commonLocalizer.localize(PdfLocalizationName.period), dateFormat(item.getStartDate()) + " - " + dateFormat(item.getEndDate()));
        departmentTable.addRowWithCode("ISSUE_STATUS", commonLocalizer.localize(PdfLocalizationName.status), referenceWfmMessageSource.localize(item.getStatusCode(), item.getStatusName()));
        departmentTable.addRowWithCode("REPORTED_BY", commonLocalizer.localize(PdfLocalizationName.reportedBy), escapeHtml(item.getResolverName()));
        departmentTable.addRowWithCode("RESOLVER_OWNER", commonLocalizer.localize(PdfLocalizationName.resolver), escapeHtml(item.getReportedByName()));
        departmentTable.addRowWithCode("TIMESHEET", commonLocalizer.localize(PdfLocalizationName.timesheet), item.isTimeSheetEnabled() ? commonLocalizer.localize(PdfLocalizationName.enabled) : commonLocalizer.localize(PdfLocalizationName.disabled));
        departmentTable.addRowWithCode("PRIORITY", commonLocalizer.localize(PdfLocalizationName.priority), referenceWfmMessageSource.localize(item.getPriorityCode(), item.getPriorityName()));
        customData.put("VIEW_TABLE", departmentTable);

        CustomisedITextTable employeeTable = new CustomisedITextTable();
        employeeTable.setName(commonLocalizer.localize(PdfLocalizationName.assignees));
        employeeTable.addColumn("EMPLOYEE", commonLocalizer.localize(PdfLocalizationName.employee));
        employeeTable.addColumn("DEPARTMENT_NAME", commonLocalizer.localize(PdfLocalizationName.department));
        employeeTable.addColumn("POSITION", commonLocalizer.localize(PdfLocalizationName.position));
        employeeTable.addColumn("ESTIMATED_TIME", commonLocalizer.localize(PdfLocalizationName.estimatedTime));
        if (item.isTimeSheetEnabled()) {
            employeeTable.addColumn("ACTUALTIME", commonLocalizer.localize(PdfLocalizationName.actualTime));
        }
        List<String> columnsValue = Lists.newArrayList();
        if (item.getIssueEmployees() != null) {
            item.getIssueEmployees();
            for (PositionsSelectItem position : item.getIssueEmployees()) {
                columnsValue.clear();
                columnsValue.add(escapeHtml(position.getName()));
                columnsValue.add(escapeHtml(position.getDepartmentName()));
                columnsValue.add(escapeHtml(position.getPositionName()));
                columnsValue.add(ServerUtils.getTimeSpentHM(position.getTime()));
                if (item.isTimeSheetEnabled()) {
                    columnsValue.add(ServerUtils.getTimeSpentHM(position.getActualTime()));
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

    public void setIssueService(IssueService issueService) {
        this.issueService = issueService;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getName() + "_" + user.getLastName() + commonLocalizer.localize(PdfLocalizationName.issueListFileName) + dateFormat(new Date()));
    }

    @Override
    protected String getTableName(Object dataClass) {
        return commonLocalizer.localize(PdfLocalizationName.issue);
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.ISSUE;
    }
}
