package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

//let's go- Summary View form logic starts from here

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.WarehouseItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

// Warehouse view form
public class WarehouseView extends CustomForm2 implements Constants, FittedContent, HasLinksInterface {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private final Integer objectID;
    private SplitButton printPdfSplitButton;
    private Anchor contactNameTextBox;
    private HTML  owners,nameTextBox,notesTextArea,phoneTextBox,emailTextBox,addressTextBox,warehouseCode;
    protected WarehouseItem warehouseItem;
    private ListingPanel<WarehouseItem> list;
    private final boolean saveAndNew = false;
    private FormHasCustomField customFieldUtil;

    public WarehouseView(Integer objectID) {
        super("summary", wfmStrings.summaryView());
        this.objectID = objectID;
    }

    public String getIconStyle() {
        return null;
    }



    @Override
    protected void addButtons() {

        //Print PDF



        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
            addRightButton(printPdfSplitButton);

        //Delete
        addRemoveButton().addClickHandler(event -> {

            final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo);
            message.setTitle(wfmStrings.deleting());
            message.setMessage(wfmStrings.sureYouWantToDelete());
            message.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    LoadingPanel.loading(true);
                    AccountingService.App.get().deleteWarehouse(warehouseItem.getObjectID(), new AbstractAsyncCallback<Boolean>() {
                        public void failure(Throwable caught) {
                            Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
                        }
                        public void success(Boolean deleted) {
                            if (deleted) {
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), accountingStrings.warehouse()), Info.Type.INFO);
                                list.reloadPage();
                            } else {
                                Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
                            }
                        }
                    });
                    LoadingPanel.loading(false);
                    closeTab();
                    list.reloadPage();

                }
            });
            message.open();
        });

        //Edit Button
        if (Utils.hasPermission(ACCOUNTING_WAREHOUSES_EDIT)) {
            addEditButton().addClickHandler(event -> closeTab("warehouse|edit/" +  warehouseItem.getObjectID(), wfmStrings.edit() ));
        }

    }

    //Input Data
    @Override
    protected void getDataToFillFields() {
        if (objectID != null) {
            LoadingPanel.loading(true);

            AccountingService.App.get().getWarehouse(objectID, new AbstractAsyncCallback<WarehouseItem>() {

                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                public void success(WarehouseItem result) {
                    LoadingPanel.loading(false);
                    warehouseItem = result;

                    nameTextBox.setText(result.getName());
                    notesTextArea.setText(result.getNotes());
                    owners.setText(result.getSelectedOwners().stream().map(SelectItem::getName).collect(Collectors.joining(", ")));
                    contactNameTextBox.setText(result.getContactname());
                    warehouseCode.setText(String.valueOf(result.getObjectID()));
                    phoneTextBox.setText(result.getPhone());
                    emailTextBox.setText(result.getEmail());
                    addressTextBox.setText(result.getAddress());
                    pdfTool(result);

                    getCustomFieldUtil().fillCustomFieldsWithData(result.getCustomFieldItems(), true);
                }

            });
        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.WAREHOUSE_FORM;
    }

    @Override
    protected String getFormType() {
        return objectID != null ? LayoutRPC.EDIT : LayoutRPC.ADD;
    }

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFields(ViewName.WareHouses, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {

            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result);
                    WarehouseView.super.onInitialize();
                }
            }
        });
        return null;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    protected void registerFields() {

        //Auto Number
        warehouseCode = new HTML();
        warehouseCode.addStyleName(DEFAULT_WIDTH);
        warehouseCode.ensureDebugId("warehouses_code");

        //Name
        nameTextBox = new HTML();
        nameTextBox.addStyleName(DEFAULT_WIDTH);
        nameTextBox.ensureDebugId("warehouses_name");

        //Notes
        notesTextArea = new HTML();
        notesTextArea.addStyleName(DEFAULT_WIDTH);
        notesTextArea.setHeight(SHORT_HEIGHT);
        notesTextArea.ensureDebugId("warehouses_notes");

        //Contact
        contactNameTextBox = new Anchor(wfmStrings.notAvailable());
        contactNameTextBox.addStyleName(DEFAULT_WIDTH);
        contactNameTextBox.ensureDebugId("warehouse_contact");


        contactNameTextBox.addClickHandler(event -> {
            if (Utils.hasPermission(CRM_CONTACTS_SUMMARY)) {
                if (warehouseItem.getContactname() != null) {
                   //TO DO
                    //  SinksContainerFactory.entryPoint.onHistoryChanged("contact|summary/" + contactListItem.getContactId() + "//" + contactListItem.getAccountId(), contactListItem.getContact());
                } else {
                    Info.warn(wfmStrings.noDataAvailable() + "- " + wfmStrings.contact());
                }

            } else {
                Info.warn(wfmStrings.youDontHavePermission());
            }
        });


        //Phone
        phoneTextBox = new HTML();
        phoneTextBox.addStyleName(DEFAULT_WIDTH);
        phoneTextBox.ensureDebugId("warehouses_phone");

        //Email
        emailTextBox = new HTML();
        emailTextBox.addStyleName(DEFAULT_WIDTH);
        emailTextBox.ensureDebugId("warehouses_email");

        //Address
        addressTextBox = new HTML();
        addressTextBox.addStyleName(DEFAULT_WIDTH);
        addressTextBox.setHeight(SHORT_HEIGHT);
        addressTextBox.ensureDebugId("warehouses_address");

        //Owners list value
        owners = new HTML();
        owners.addStyleName(DEFAULT_WIDTH);
        owners.ensureDebugId("warehouses_owners");

        //Block --> Basic Information
        addTitleField(POSITIONS.BASIC_INFORMATION, wfmStrings.basicDetails());

        addField(CustomFormConstants.NUMBER, warehouseCode, getTitle(wfmStrings.number()));
        addField(NAME, nameTextBox, getTitle(wfmStrings.name()));
        addField(OWNERS, owners, getTitle(wfmStrings.assignee()));
        addField(PRIMARY_CONTACT, contactNameTextBox, getTitle(wfmStrings.contact()));
        addField(Constants.PHONE, phoneTextBox, getTitle(wfmStrings.phone()));
        addField(Constants.EMAIL, emailTextBox, getTitle(wfmStrings.email()));
        addField(ADDRESS, addressTextBox, getTitle(wfmStrings.address()));
        addField(CustomFormConstants.NOTES, notesTextArea, getTitle(wfmStrings.description()));

        getCustomFieldUtil().drawCustomFields(this, objectID, true);
        show();
    }

    @Override
    protected void initPredefinedValues() {
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            return getLocalizer().localizeByFieldID(getFormID(), fieldID);
        }
        return null;
    }

    private void saveWarehouse() {
        AccountingService.App.get().saveWarehouse(warehouseItem, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                enableButton(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer result) {
                enableButton(true);
                LoadingPanel.loading(false);
                {
                    String successMessage = Utils.textFormat(wfmStrings.messSuccessfullyAdded(), accountingStrings.warehouse());
                    Info.show(successMessage, Info.Type.INFO);
                    if (!saveAndNew) {
                        closeTab("edit" + result, warehouseItem.getName());
                    } else {
                        closeTab("warehouseadd");
                    }
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WAREHOUSE_SAVED, result, WarehouseView.this);
                }
            }
        });
    }

    public void pdfTool(WarehouseItem result) {
        if (printPdfSplitButton == null) {
            return;
        }
        List<SplitButtonItem> pdfTemplatesList = new ArrayList<>();
        Integer defaultTemplateId = null;
        if (result != null && result.getTemplates() != null && result.getTemplates().length > 0) {
            for (SelectItem pdfItem : result.getTemplates()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(panel, pdfItem.getId(), false)));
            }
        } else {
            pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_LANDSCAPE", wfmStrings.landscape(), () -> generatePDF(panel, null, true)));
        }
        Integer finalDefaultTemplateId = defaultTemplateId;

        SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> generatePDF(panel, finalDefaultTemplateId, false), true);
        pdfTemplatesList.add(pdfVersion);
        printPdfSplitButton.addItemList(pdfTemplatesList);
    }

    private void generatePDF(HTMLPanel panel, Integer templateID, boolean landscape) {
        RequestObject requestObject = new RequestObject(objectID);
        requestObject.setIS_LANDSCAPE(landscape);
        HashMap<String, String> parameters = requestObject.getRequestParams();

        if (templateID != null) {
            parameters.put("pdfTemplateID", String.valueOf(templateID));
        }
        String pdfURL = CommandConstants.PDF_URL + "/warehouseProductsViewPDFHandler";

        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    public HasLinks getLinkingUtil() {
        return null;
    }
}