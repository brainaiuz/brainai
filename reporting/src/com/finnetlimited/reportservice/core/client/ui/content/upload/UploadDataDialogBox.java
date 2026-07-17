package com.finnetlimited.reportservice.core.client.ui.content.upload;

import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.ui.listbox.DRSListBox;
import com.finnetlimited.reportservice.core.client.ui.loading.DRSLoadingPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;


/**
 * Created by IntelliJ IDEA.
 * User: Power
 * Date: 3/16/12
 * Time: 3:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class UploadDataDialogBox extends KpiModal {

    private static final String REQUIRED = "<span style='color:red'> * </span>";

    private FlexTable flexTable;
    private FormPanel formPanel;
    private DRSListBox reportCategoriesLstBox;
    private TextBox txtName;


    public UploadDataDialogBox() {
        setCloseButton(true);

        setSize(400, 160);
        setScrollable(true);
        setTitle(wfmStrings.importYourData());

        getScrollPanel().setStyleName("folder-content");
        // render ui controls
        renderContent();
        //fetching all data form server
        fetchData();
    }

    private void fetchData() {
        getReportCategories();
    }

    private void renderContent() {
        flexTable = new FlexTable();
        flexTable.setStyleName("f-table");

        formPanel = new FormPanel();
        txtName = new TextBox();
        reportCategoriesLstBox = new DRSListBox();
        reportCategoriesLstBox.setName("reportCategoriesFormElementViewName");

        int row = 0;

        txtName = new TextBox();
        txtName.setName("textBoxFormElementViewName");
        txtName.getElement().setAttribute("style", "width:180px;");
        setRow(row++, wfmStrings.nameOfDataType(), txtName, true);

        reportCategoriesLstBox.getElement().setAttribute("style", "width:180px;");
        setRow(row++, wfmStrings.chooseTemplateCategory(), reportCategoriesLstBox, true);

        final FileUpload fileUpload = new FileUpload();
        fileUpload.setName("uploadImportFile");

        setRow(row++, wfmStrings.file(), fileUpload, false);

        colSpan(row++, 2, "<span style='color:#FF7300;'>" + wfmStrings.allowedFormat() + "</span>");

        Button button = new Button(wfmStrings.upload(), (ClickHandler) event -> {

            if (!IsValidToSubmit()) {
                return;
            }

            if (!ValidateList()) {
                return;
            }

            formPanel.submit();


        });
        button.getElement().setAttribute("style", "float:right;");
        colSpan(row++, 2, button);

        formPanel.setMethod(FormPanel.METHOD_POST);
        formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
        formPanel.setAction(GWT.getHostPageBaseURL() + "common/importExcel");
        formPanel.addSubmitHandler(event -> DRSLoadingPanel.show());
        formPanel.addSubmitCompleteHandler(event -> {
            DRSLoadingPanel.hide();
            close();
        });

        formPanel.add(flexTable);

        add(formPanel);
    }

    private void setRow(int row, String label, Widget widget, boolean isrequired) {

        if (isrequired)
        //flexTable.setHTML(row, 0, String.format("%s: %s", label, REQUIRED));
        {
            flexTable.setHTML(row, 0, label + ": " + REQUIRED);
        } else {
            flexTable.setHTML(row, 0, label + ": ");
        }
        //flexTable.setHTML(row, 0, String.format("%s: ", label));
        flexTable.setWidget(row, 1, widget);
    }

    private void colSpan(int row, int colspan, Widget widget) {
        flexTable.setWidget(row, 0, widget);
        colSpan(row, colspan);
    }

    private void colSpan(int row, int colspan, String s) {
        flexTable.setHTML(row, 0, s);
        colSpan(row, colspan);

    }

    private void colSpan(int row, int colspan) {
        flexTable.getFlexCellFormatter().setColSpan(row, 0, colspan);
    }

    private void getReportCategories() {
        CoreService.App.get().getReportTemplateCategories(new AsyncCallback<ArrayList<SelectListRpc>>() {
            @Override
            public void onFailure(Throwable throwable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onSuccess(ArrayList<SelectListRpc> selectListRpcs) {
                reportCategoriesLstBox.addItems(selectListRpcs);
            }
        });
    }

    private boolean IsValidToSubmit() {
        boolean isvalid = true;
        String temp = "/\\'`\",.!@#$%^&*()+[]{}?><|";
        for (char ch : txtName.getText().toCharArray()) {
            if (temp.contains("" + ch)) {
                isvalid = false;
                break;
            }
        }

        if ("".equals(txtName.getText().trim())) {
            isvalid = false;
        }
        if (!isvalid) {
            final WfmMessageBox msg = new WfmMessageBox(IconEnum.ERROR, Action.OK, wfmStrings.pleaseEnterName());
            msg.open();
        }

        return isvalid;
    }

    private boolean ValidateList() {
        if (reportCategoriesLstBox.getSelectedIndex() <= 0) {
            final WfmMessageBox msg = new WfmMessageBox(IconEnum.ERROR, Action.OK, wfmStrings.pleaseChooseTemplate());
            msg.open();
            return false;
        }

        return true;
    }

    public FormPanel getFormPanel() {
        return formPanel;
    }

    @Override
    public void close() {
        txtName.setText("");
        reportCategoriesLstBox.setSelectedIndex(0);
        super.close();
    }
}
