package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendServiceAsync;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.HelpDocumentItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Dilshod Madrahimov
 * Date: 2/27/13
 * Time: 6:52 PM
 */
public class AddEditHelpDocumentForm extends View implements Constants {

    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final BackendServiceAsync backendService = BackendService.App.get();

    private TextBox title;
    private TextArea2 description;
    private TextBox link;
    private DataListBox sectionNameBox;
    private DataListBox formNameBox;
    private DataListBox blockNameBox;
    private DataListBox hostNameBox;

    private final Integer objectID;
    private FlexTable customForm;
    private HelpDocumentItem item;
    private WfmButton2 saveButton ;
    private WfmButton2 cancelButton;


    public AddEditHelpDocumentForm(Integer objectID) {
        super(HELP_DOCUMENT, objectID != null ? backendStrings.editHelpDocument():backendStrings.addHelpDocument());
        this.objectID = objectID;
    }

    @Override
    public String getIconStyle() {
        return "doc documents";
    }

    protected Widget onInitialize() {
        title = new TextBox();
        title.addStyleName(DEFAULT_WIDTH);
        description = new TextArea2(1000);
        description.setWidth(410);
        description.setHeight(110);


        link = new TextBox();
        link.addStyleName(DEFAULT_WIDTH);

        hostNameBox = new DataListBox();
        hostNameBox.addStyleName(DEFAULT_WIDTH);

        SelectItem[] hostname = new SelectItem[]{new SelectItem(0, Utils.getHostName())};
        hostNameBox.setItems(hostname);
        hostNameBox.setSelected(0);
        if (Utils.getHostName().contains("app.kpi.com") || Utils.getHostName().contains("aws.kpi.com")) {
            backendService.getCompanyHosts(new AsyncCallback<SelectItem[]>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(SelectItem[] hosts) {
                    if (hosts != null && hosts.length > 0) {
                        hostNameBox.setItems(hosts);
                    }
                }
            });
        }
        sectionNameBox = new DataListBox();
        sectionNameBox.setAllowFirstItem(true);
        sectionNameBox.addStyleName(DEFAULT_WIDTH);
        sectionNameBox.setItems(LayoutRPC.getSection());

        formNameBox = new DataListBox();
        formNameBox.addStyleName(DEFAULT_WIDTH);
        formNameBox.setAllowFirstItem(true);
        sectionNameBox.addValueChangeHandler(changeEvent -> formNameBox.setItems(LayoutRPC.getViewBySection(sectionNameBox.getSelectedItem().getName())));
        blockNameBox = new DataListBox();
        blockNameBox.addStyleName(DEFAULT_WIDTH);
        blockNameBox.setItems(HelpDocumentItem.getPosition());

        customForm = new FlexTable();
//        customForm.setCellPadding(10);
//        customForm.setCellSpacing(13);
        customForm.addStyleName("AddHelpForm-table");
        int row = 0;
        customForm.setHTML(row, 0, getCustomTITLE(wfmStrings.title(), true));
        customForm.setWidget(row, 1, title);
        row++;
        customForm.setHTML(row, 0, getCustomTITLE(wfmStrings.description(), false));
        customForm.setWidget(row, 1, description);
        row++;
        customForm.setHTML(row, 0, getCustomTITLE(wfmStrings.link(), true));
        customForm.setWidget(row, 1, link);
        row++;
        customForm.setHTML(row, 0, getCustomTITLE(backendStrings.hostName(), true));
        customForm.setWidget(row, 1, hostNameBox);
        row++;
        customForm.setHTML(row, 0, getCustomTITLE(wfmStrings.section(), true));
        customForm.setWidget(row, 1, sectionNameBox);
        row++;
        customForm.setHTML(row, 0, getCustomTITLE(wfmStrings.form(), true));
        customForm.setWidget(row, 1, formNameBox);
        row++;
        customForm.setHTML(row, 0, getCustomTITLE(wfmStrings.block(), true));
        customForm.setWidget(row, 1, blockNameBox);
        row++;

        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        cancelButton = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);

        cancelButton.addClickHandler(event -> closeTab());

        saveButton.addClickHandler(event -> {
            if (validate()) {
                saveButton.setEnabled(false);
                save();
            }
        });

        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.addStyleName("AddHelpForm-table__footer");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        buttonPanel.setSpacing(10);

        customForm.setWidget(row, 1, buttonPanel);

        if (objectID != null) {
            backendService.getHelpDocuments(objectID, new AsyncCallback<HelpDocumentItem>() {
                @Override
                public void onFailure(Throwable caught) {
                    item = null;
                }

                @Override
                public void onSuccess(HelpDocumentItem result) {
                    item = result;
                    fillFormWithData();
                }
            });
        }
        add(customForm);
        return null;
    }


    private String getCustomTITLE(String text, boolean required) {
        return "<b class=customTitle>" + text + (required ? "<font color='red'>*</font>:</b>" : ":</b>");
    }

    private void fillFormWithData() {
        if (item == null) {
            item = new HelpDocumentItem();
        }
        title.setText(item.getTitle());
        description.setText(item.getDescription());
        link.setText(item.getLink());
        hostNameBox.setSelectedByValue(item.getHostName());
        sectionNameBox.setSelectedByValue(item.getSection());
        formNameBox.setItems(LayoutRPC.getViewBySection(item.getSection()));
        formNameBox.setSelectedByValue(item.getForm());
        blockNameBox.setSelectedByValue(item.getBlock());

    }
    private void save() {
        if (!validate()) {
            return;
        }
        if (item == null) {
            item = new HelpDocumentItem();
        }
        item.setTitle(title.getText());
        item.setDescription(description.getText());
        item.setLink(link.getText());
        item.setHostName(hostNameBox.getSelectedItem().getName());
        item.setSection(sectionNameBox.getSelectedItem().getName());
        item.setForm(formNameBox.getSelectedItem().getName());
        item.setBlock(blockNameBox.getSelectedItem().getName());
        if (HelpDocumentItem.RIGHT_TOP.equals(item.getBlock())) {
            backendService.isExistHelpDocument(item.getObjectID(),item.getForm(), item.getBlock(), new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(Boolean result) {
                    if (result) {
                        Info.show("You already have a help document with these selected fields", Info.Type.WARNING);
                        saveButton.setEnabled(true);
                    } else {
                        saveDocument();
                    }
                }
            });
        } else {
            saveDocument();
        }
    }
    private void saveDocument() {
        backendService.saveHelpDocument(item, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Integer result) {
                closeTab();
                Info.show("Document added", Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_HELP_DOCUMENT_FORM_ADD, item, AddEditHelpDocumentForm.this);
            }
        });
    }

    private boolean validate() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(title)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(link)) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(hostNameBox, new HTML(), "")) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(blockNameBox, new HTML(), "")) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(formNameBox, new HTML(), "")) {
            errors++;
        }
        if (errors > 0) {
            WfmWindow.alert(wfmStrings.sureEnteredAllData());
            return false;
        }

        return true;
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
