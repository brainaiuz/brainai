package com.edatasite.workforce.gwt.backend.client.ui.view.fingerprint;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendServiceAsync;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Muhammad on 13.04.2016.
 */
public class FingerprintSetup extends Composite implements Constants {
    interface FingerprintSetupUiBinder extends UiBinder<HTMLPanel, FingerprintSetup> {
    }

    private static final FingerprintSetupUiBinder ourUiBinder = GWT.create(FingerprintSetupUiBinder.class);
    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final BackendServiceAsync backendService = BackendService.App.get();
    private final LinkedHashMap<Integer, ArrayList<FingerprintSetupTable>> setupTableMap = new LinkedHashMap<>();
    private List<CompanyDomain> itemList = new ArrayList<>();
    private final Integer companyID;
    ArrayList<Widget> errorWidgets = new ArrayList<>();
    ArrayList<Widget> errorSenderWidgets;
    private int errors = 0;

    @UiField
    HTMLPanel headerPanel;
    @UiField
    HorizontalPanel addedLine;
    @UiField
    Anchor addNewLine;
    @UiField
    ButtonElement saveButton;

    public FingerprintSetup(Integer companyID) {
        this.companyID = companyID;
        HTMLPanel rootPanel = ourUiBinder.createAndBindUi(this);
        initWidget(rootPanel);
        onInitialize();
    }

    public void onInitialize() {
        LoadingPanel.loading(true);
        backendService.getFingerprintSetup(companyID, new AsyncCallback<ArrayList<CompanyDomain>>() {

            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ArrayList<CompanyDomain> result) {
                LoadingPanel.loading(false);
                itemList = result;
                drawSetup();
            }
        });
        saveButton.setInnerHTML(wfmStrings.save());
        DOM.sinkEvents(saveButton.cast(), Event.ONCLICK);
        DOM.setEventListener(saveButton.cast(), event -> save());
    }

    private void drawSetup() {
        initLabels(headerPanel);
        final FingerprintSetupTable printTable = new FingerprintSetupTable();
        printTable.draw(itemList);
        ArrayList<FingerprintSetupTable> printTableList = new ArrayList<>();
        printTableList.add(printTable);
        addedLine.add(printTable);
        setupTableMap.put(1, printTableList);
        addNewLine.setText(backendStrings.addNewDevice());
        addNewLine.addClickHandler(clickEvent -> printTable.addNewLine());
    }

    private void initLabels(HTMLPanel headerPanel) {
        HorizontalPanel hPanel = new HorizontalPanel();
        Label label = new Label("");
        label.getElement().getStyle().setWidth(75, Style.Unit.PX);
        hPanel.add(label);

        Label label1 = new Label(backendStrings.deviceId());
        label1.getElement().getStyle().setWidth(120, Style.Unit.PX);
        label1.getElement().getStyle().setPaddingLeft(50, Style.Unit.PX);
        label1.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        hPanel.add(label1);

        Label label2 = new Label(backendStrings.branchName());
        label2.getElement().getStyle().setWidth(150, Style.Unit.PX);
        label2.getElement().getStyle().setPaddingLeft(25, Style.Unit.PX);
        label2.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        hPanel.add(label2);

        Label label3 = new Label(backendStrings.dynamicStatus());
        label3.getElement().getStyle().setWidth(150, Style.Unit.PX);
        label3.getElement().getStyle().setPaddingLeft(25, Style.Unit.PX);
        label3.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        hPanel.add(label3);

        headerPanel.add(hPanel);
    }

    private void save() {
        ArrayList<CompanyDomain> setupTOs = new ArrayList<>();
        getFingerprintSetup(setupTOs);
        if (errors > 0) return;
        LoadingPanel.loading(true);

        backendService.saveFingerPrintSetup(companyID, setupTOs, new AsyncCallback<ArrayList<String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, wfmStrings.errorOccurredSavingChanges());
                messageBox.setTitle("Error");
                messageBox.open();
            }

            @Override
            public void onSuccess(ArrayList<String> errorList) {
                LoadingPanel.loading(false);
                StringBuilder errorText = new StringBuilder();
                WfmMessageBox messageBox;
                if (errorList == null || errorList.size() == 0) {
                    messageBox = new WfmMessageBox(IconEnum.INFO, Action.OK, Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.fingerprintSetup()));
                    messageBox.setTitle("Success");
                } else {
                    errorText.append("You have ").append(errorList.size()).append(" errors <br/>");
                    for (String item : errorList) {
                        errorText.append(item).append("<br/> ");
                    }
                    messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, errorText.toString());
                    messageBox.setTitle("Error");
                }
                messageBox.open();
            }
        });

    }

    private void getFingerprintSetup(ArrayList<CompanyDomain> setupTOs) {
        clearErrorStyle();
        Map<String, Integer> errorMap = new HashMap<>();

        for (Integer key : setupTableMap.keySet()) {
            ArrayList<FingerprintSetupTable> setupTebles = setupTableMap.get(key);
            for (FingerprintSetupTable setupTable : setupTebles) {
                int setupTableRowCount = setupTable.getRowCount();
                for (int i = 0; i < setupTableRowCount; i++) {
                    errorSenderWidgets = new ArrayList<>();
                    CompanyDomain companyDomain = new CompanyDomain();

                    companyDomain.setDomain(((Label) setupTable.getWidget(i, 0)).getText() != null ? ((Label) setupTable.getWidget(i, 0)).getText() : "");
                    companyDomain.setWebsiteNumber(((Label) setupTable.getWidget(i, 1)).getText() != null ? ((Label) setupTable.getWidget(i, 0)).getText() : "");

                    if (((TextBox) setupTable.getWidget(i, 3)).getText() != null && !"".equals(((TextBox) setupTable.getWidget(i, 3)).getText())) {
                        companyDomain.setCompanyUniqueID(((TextBox) setupTable.getWidget(i, 3)).getText());

                        if (errorMap.size() > 0 && errorMap.containsKey(companyDomain.getCompanyUniqueID())) {
                            markAsError(setupTable.getWidget(i, 3));
                        } else {
                            errorMap.put(companyDomain.getCompanyUniqueID(), i);
                        }

                    } else {
                        markAsError(setupTable.getWidget(i, 3));
                    }
                    if (((TextBox) setupTable.getWidget(i, 4)).getText() != null && !"".equals(((TextBox) setupTable.getWidget(i, 4)).getText())) {
                        companyDomain.setCompanyBranchName(((TextBox) setupTable.getWidget(i, 4)).getText());
                    } else {
                        markAsError(setupTable.getWidget(i, 4));
                    }
                    companyDomain.setDynamicStatus(((KpiCheckBox) setupTable.getWidget(i, 5)).getValue());
                    setupTOs.add(companyDomain);
                }
            }
        }

    }

    private void markAsError(Widget widget) {
        if (widget != null) {
            errorWidgets.add(widget);
            errors += 1;
            widget.addStyleName(Constants.ERROR_FORM_STYLE);
        }
    }

    private void clearErrorStyle() {
        errors = 0;
        if (errorWidgets != null && errorWidgets.size() > 0) {
            for (Widget widget : errorWidgets) {
                widget.removeStyleName(Constants.ERROR_FORM_STYLE);
            }
        }
    }

}