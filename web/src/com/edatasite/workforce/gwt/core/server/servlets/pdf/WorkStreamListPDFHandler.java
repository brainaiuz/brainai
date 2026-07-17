package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.project.WbsItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.lucene.parser.HTMLParser;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextSummaryView;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.workstream.client.rpc.WbsService;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 03-Dec-2010
 * Time: 13:46:09
 */
public class WorkStreamListPDFHandler extends AbstractITextPostPdfHandler {

    private WbsService wbsService;
    private CommonService commonService;
    private ProjectManager projectManager;
    private TaskManager taskManager;

    public void setWbsService(WbsService wbsService) {
        this.wbsService = wbsService;
    }

    public void setCommonService(CommonService commonService) {
        this.commonService = commonService;
    }

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.WORK_STREAM_LIST;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        RequestObject ro = (RequestObject) dataClass;

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.SUMMARYVIEW);
        pdfData.setCompanyData(getCompanyData(company, true, hasPhantom));

        ITextSummaryView summaryView = new ITextSummaryView();
        ITextTableList projectTable = new ITextTableList(2);
        EdsProject project = projectManager.get(ro.getObjectID());
        projectTable.addPdfTableHeader("Project Number", "Project Name");
        projectTable.addPdfTableRows((project.getNumber() != null ? project.getNumber() : " "), project.getName());

        List<String> headerColumns = new LinkedList<>();
        headerColumns.add("Name");
        headerColumns.add("Description");
        headerColumns.add("Assignees");
        headerColumns.add("Start date");
        headerColumns.add("End date");

        List<String> customFields = new LinkedList<>();
        ArrayList<CompanyCustomFieldItem> customFieldItems = commonService.getCompanyCustomFields(ViewName.Task);
        for (CompanyCustomFieldItem cfi : customFieldItems) {
            customFields.add(cfi.getFieldName());
            headerColumns.add(cfi.getFieldName());
        }

        ITextTableList listTable = new ITextTableList(headerColumns.size());
        listTable.addPdfTableHeader(headerColumns.toArray(new String[]{}));
        SimpleDateFormat shortFormat = getCompanyShortDateFormat(projectManager.getUser().getCompany());
        List<WbsItem> itemList = wbsService.getWorkStreamList(ro.getObjectID());
        int workstream = 0;
        for (WbsItem item : itemList) {
            if (WbsItem.WORKSTREAM == item.getNodeType()) {
                workstream++;
            }
            HTMLParser htmlParser = new HTMLParser();

            String name;
            try {
                name = item.getName() != null ? htmlParser.performParse(item.getName()) : " ";
            } catch (SAXException | IOException e) {
                name = " ";
            }

            String description;
            try {
                description = item.getDescription() != null ? htmlParser.performParse(item.getDescription()) : " ";
            } catch (SAXException | IOException e) {
                description = " ";
            }

            String start = item.getStartDate() != null ? shortFormat.format(item.getStartDate()) : " ";
            String end = item.getEndDate() != null ? shortFormat.format(item.getEndDate()) : " ";
            StringBuilder assignees = new StringBuilder();
            if (item.getAssignees() != null) {
                for (int j = 0; j < item.getAssignees().length; j++) {
                    if (j == 0) {
                        assignees.append(item.getAssignees()[j]);
                    } else {
                        assignees.append(", ");
                        assignees.append(item.getAssignees()[j]);
                    }
                }
            }
            List<String> rowCells = new LinkedList<>();
            rowCells.add(name);
            rowCells.add(description);
            rowCells.add(assignees.toString());
            rowCells.add(start);
            rowCells.add(end);

            if (item.getNodeType() == WbsItem.TASK) {
                List<CompanyCustomFieldItem> taskCustomFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(taskManager.get(item.getId()).getTaskCustomFields(), customFieldItems);
                for (String c : customFields) {
                    String cellValue = "";
                    for (CompanyCustomFieldItem tcfi : taskCustomFieldItems) {
                        if (c.equals(tcfi.getFieldName())) {
                            if (Constants.DATA_TYPE_DATE.equals(tcfi.getDataType())) {
                                cellValue = tcfi.getFieldDateNonConvertedValue() != null ? dateFormat(tcfi.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                            } else {
                                cellValue = tcfi.getFieldStringValue() != null ? tcfi.getFieldStringValue() : "";
                            }
                        }
                    }
                    rowCells.add(cellValue);
                }
            } else {
                for (String c : customFields) {
                    rowCells.add("");
                }
            }

            listTable.addPdfTableRows(rowCells.toArray(new String[]{}));
        }
        summaryView.addTable(projectTable);
        summaryView.addTable(listTable);
        pdfData.setTotal(workstream);
        pdfData.setSummaryView(summaryView);
        return pdfData;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ITextTableList tableList = new ITextTableList(5);
        tableList.addPdfTableHeader("Name", "Description", "Assignees", "Start date", "End date");
        RequestObject ro = (RequestObject) dataClass;
        SimpleDateFormat shortFormat = getCompanyShortDateFormat(projectManager.getUser().getCompany());
        List<WbsItem> itemList = wbsService.getWorkStreamList(ro.getObjectID());
        HTMLParser htmlParser = new HTMLParser();
        for (WbsItem item : itemList) {
            String name = null;
            try {
                name = item.getName() != null ? htmlParser.performParse(item.getName()) : " ";
            } catch (SAXException e) {
                name = " ";
            }
            String description = null;
            try {
                description = item.getDescription() != null ? htmlParser.performParse(item.getDescription()) : " ";
            } catch (SAXException e) {
                description = " ";
            }
            String start = item.getStartDate() != null ? shortFormat.format(item.getStartDate()) : " ";
            String end = item.getEndDate() != null ? shortFormat.format(item.getEndDate()) : " ";
            StringBuilder assignees = new StringBuilder();
            if (item.getAssignees() != null) {
                for (int j = 0; j < item.getAssignees().length; j++) {
                    if (j == 0) {
                        assignees.append(item.getAssignees()[j]);
                    } else {
                        assignees.append(", ");
                        assignees.append(item.getAssignees()[j]);
                    }
                }
            }
            tableList.addPdfTableRows(name, description, assignees.toString(), start, end);
        }
        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        return new RequestObject();
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("WorkstreamList_" + dateFormat(new Date()));
    }
}
