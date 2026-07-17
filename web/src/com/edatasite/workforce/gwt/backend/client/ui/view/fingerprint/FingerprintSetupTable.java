package com.edatasite.workforce.gwt.backend.client.ui.view.fingerprint;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import java.util.List;

/**
 * Created by Muhammad on 13.04.2016.
 */
public class FingerprintSetupTable extends FlexTable {

    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private Label deviceLabel;
    private TextBox uniqueIDBox;
    private TextBox branchNameBox;
    private KpiCheckBox dynamicStatus;
    private SimpleLink removeLink;

    public FingerprintSetupTable() {

    }

    public void draw(List<CompanyDomain> itemList) {
        boolean isEmpty = true;
        for (CompanyDomain companyDomain : itemList) {
            isEmpty = false;
            setRowData(getRowCount(), companyDomain);
        }
        if (isEmpty) {
            setRowData(getRowCount(), null);
        }
    }

    private void setRowData(Integer index, CompanyDomain item) {
        Label domain = new Label(item != null ? item.getDomain() : null);
        Label web_num = new Label(item != null ? item.getWebsiteNumber() : null);
        domain.setVisible(false);
        web_num.setVisible(false);

        HTML html = new HTML("Device " + (index + 1));
        html.setWidth("60px");
        uniqueIDBox = createTextBox(item != null ? item.getCompanyUniqueID() : null);
        branchNameBox = createTextBox(item != null ? item.getCompanyBranchName() : null);
        dynamicStatus = new KpiCheckBox();
        dynamicStatus.getElement().setAttribute("style","margin-left:50px; margin-right:60px;");
        dynamicStatus.setValue(item != null && item.getDynamicStatus());
        removeLink = removeAction();
        setWidget(index, 0, domain);
        setWidget(index, 1, web_num);
        setWidget(index, 2, html);
        setWidget(index, 3, uniqueIDBox);
        setWidget(index, 4, branchNameBox);
        setWidget(index, 5, dynamicStatus);
        if (index == 0) {
            setWidget(index, 6, null);
        } else {
            setWidget(index, 6, removeLink);
        }

    }

    private SimpleLink removeAction() {
        SimpleLink link = new SimpleLink("Remove");
        link.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        link.addClickHandler(clickEvent -> {
            final Integer getWidgetId = getCellForEvent(clickEvent).getRowIndex();
            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo);
            messageBox.setTitle(wfmStrings.confirmationMessage());
            messageBox.setMessage("Are you sure you want to delete this Device");
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), "This Device "), Info.Type.INFO);
                    removeRow(getWidgetId);
                    for (int i = 0; i < getRowCount(); i++) {
                        ((HTML) getWidget(i, 2)).setText("Device " + (i + 1));
                    }
                }
            });
            messageBox.open();
        });
        return link;
    }


    private TextBox createTextBox(String s) {
        TextBox textBox = new TextBox();
        textBox.setStyleName("form-control");
        textBox.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        textBox.getElement().getStyle().setWidth(150, Style.Unit.PX);
        textBox.getElement().getStyle().setHeight(22, Style.Unit.PX);
        textBox.getElement().getStyle().setMarginBottom(2, Style.Unit.PX);
        textBox.setText(s);
        return textBox;
    }

    public void addNewLine() {
        setRowData(getRowCount(), null);
    }
}
