package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.PDFTemplatesListItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ReportingListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.WfmFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.Workarea;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Sep 19, 2011
 * Time: 5:44:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportingTemplatesAddView extends View implements CommandConstants {


    private final BackendStrings backendStrings = BackendStrings.App.get();

    private int reportId;
    private int companyId;
    private WfmForm table;
    private WfmForm.Field pdfTemplate;
    private WfmForm.Field excelTemplate;
    private WfmForm.Field maxRowCountField;
    private WfmFormPanel form;

    private ListBox templateList;
    private FileUpload fileUpload;
    private TextBox textMaxRowCount;


    public ReportingTemplatesAddView(Integer reportId) {
        super("add", "Edit Template");
        this.reportId = reportId;

    }

    public ReportingTemplatesAddView(Integer reportId, Integer companyId) {
        super("add", "Edit Template");
        this.reportId = reportId;
        this.companyId = companyId;
    }

    public void init(Workarea workarea) {

    }

    protected Widget onInitialize() {
        initInternal();
        loadData();
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void initInternal() {
        initForm();
        add(form);

        table = new WfmForm(new String[]{"7%", "68%", "25%"});
        templateList = new ListBox();
        templateList.setWidth("180px");

        fileUpload = new FileUpload();
        textMaxRowCount = new TextBox();
        textMaxRowCount.setText("64000");

        pdfTemplate = table.addField("Pdf template", templateList);
        excelTemplate = table.addField("Excel template", fileUpload);
        maxRowCountField = table.addField("Max. Row Count", textMaxRowCount);


        WfmButton2 buttonCancel = new WfmButton2("Cancel", (ClickHandler) clickEvent -> closeTab());
        WfmButton2 buttonSave = new WfmButton2("Save", WfmButton2.BTN_PRIMARY, (ClickHandler) clickEvent -> {

            if (fileUpload.getFilename() != null && !fileUpload.getFilename().equals("")) {
                fileUpload.setName(EXCEL_TEMPLATE_REPORT);
            }

            form.submit();

        });

        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.add(buttonSave);
        buttonPanel.add(buttonCancel);
        buttonPanel.setSpacing(10);
        buttonPanel.addStyleName("FooterButtonsTable");

        VerticalPanel panel = new VerticalPanel();
        panel.add(table);
        panel.add(buttonPanel);
        panel.add(new Hidden("COMPANY_ID", String.valueOf(companyId)));

        form.add(panel);


    }

    private void initForm() {
        form = new WfmFormPanel("/CreateExcelReportTemplateHandler");
        form.setObjectID(companyId);
        //  form.setParameter(COMPANY__ID, String.valueOf(companyId));
        form.addSubmitCompleteHandler(submitCompleteEvent -> {
            if (form.isSuccess()) {
                ReportingListItem reportingListItem = new ReportingListItem();
                reportingListItem.setReportId(reportId);
                if (!"".equals(textMaxRowCount.getText()) && Integer.parseInt(textMaxRowCount.getText()) > 0)
                    reportingListItem.setMaxExcelRowCount(Integer.parseInt(textMaxRowCount.getText()));

                String returnValue = form.getReturnValue();

                String[] returnObject = returnValue.split(";");

                if (returnObject.length > 0) {

                    for (String aReturnObject : returnObject) {
                        if (aReturnObject.toLowerCase().startsWith(EXCEL_TEMPLATE_REPORT.toLowerCase())) {
                            reportingListItem.setExceltemplateId(Integer.parseInt(aReturnObject.toLowerCase().replace(EXCEL_TEMPLATE_REPORT.toLowerCase() + "=", "")));
                        }
                    }

                    if (templateList.getItemCount() > 0)
                        reportingListItem.setTemplateId(Integer.parseInt(templateList.getValue(templateList.getSelectedIndex())));
                    reportingListItem.setCompanyId(companyId);
                    BackendService.Reporting.get().updateReport(reportingListItem, new AbstractAsyncCallback<Boolean>() {

                        @Override
                        public void failure(Throwable throwable) {
                            Info.show("Error occured.", Info.Type.WARNING);
                        }

                        @Override
                        public void success(Boolean aBoolean) {
                            Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.template()), Info.Type.INFO);
                        }
                    });
                    closeTab();
                }

            } else {
                LoadingPanel.loading(false);
                Info.show("Error occured.", Info.Type.WARNING);
            }
        });
    }

    private void loadData() {


        CoreService.App.get().getReport(reportId, companyId, new AbstractAsyncCallback<ReportRpc>() {
            @Override
            public void failure(Throwable throwable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void success(ReportRpc reportRpc) {
                if (reportRpc.getMaxExcelRowCount() != null && reportRpc.getMaxExcelRowCount() > 0) {
                    textMaxRowCount.setText(reportRpc.getMaxExcelRowCount().toString());
                }
                if (reportRpc.getPdfTemplateId() != null) {
                    final int pdftemplateId = reportRpc.getPdfTemplateId();

                    BackendService.App.get().getCompanyPDFTemplates(null, new AbstractAsyncCallback<ListResult<PDFTemplatesListItem>>() {

                        @Override
                        public void failure(Throwable throwable) {
                            //To change body of implemented methods use File | Settings | File Templates.
                        }

                        @Override
                        public void success(ListResult<PDFTemplatesListItem> pdfTemplatesList) {
                            int selected = 0;
                            int index = 0;
                            for (PDFTemplatesListItem item : pdfTemplatesList.getList()) {
                                templateList.addItem(item.getTemplateName(), item.getObjectID().toString());
                                if (item.getObjectID().equals(pdftemplateId)) {
                                    selected = index + 1;
                                }
                                index++;
                            }

                            templateList.setSelectedIndex(selected);
                        }
                    });
                }
            }
        });


    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }


}
