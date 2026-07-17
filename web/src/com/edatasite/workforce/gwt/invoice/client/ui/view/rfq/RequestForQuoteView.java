package com.edatasite.workforce.gwt.invoice.client.ui.view.rfq;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.quickadd.ProductQuickAddForm;
import com.edatasite.workforce.gwt.client.client.ui.view.quickadd.CusSuppQuickAddView;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.FooteredCustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyAddress;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.PdfTemplateTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateTimeWidget;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.widget.LinkableCrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MeasurementsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadPanel;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceFormFields;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.Params;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQData;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartCrmAccountLookup;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartProductLookUp;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.edatasite.workforce.gwt.invoice.client.ui.view.ContactPopup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.InvoiceCustomFieldsView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.ItemUploadTable;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.CusSupAddress;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.QuoteAdditionalFields;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDatePicker;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDateTime;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDropDownField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldMultiLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomPercentageField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomTextAreaField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomTextBoxField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.TermsAndDuePanel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.TermsAndDueProvider;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;
import org.gwt.advanced.client.ui.widget.EditableGrid;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.DUE_TYPE;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.TERMS_TYPE;
import static com.edatasite.workforce.gwt.core.client.rpc.RelationItem.TYPE_REQUEST_FOR_QUOTE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.ADMIN;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.APPROVE_AND_SEND;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.BTN_PRIMARY;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.ERROR_FORM_STYLE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.RECEIVABLE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.RFQ_APPROVED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_MULTI_LOOKUP;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/26/12
 * Time: 6:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class RequestForQuoteView extends FooteredCustomForm implements Colapse, FittedContent, HasLinksInterface {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();


    private static final Integer DEFAULT_ITEM_ROWS = 3;
    private Integer objectID;
    private Integer currentUserId;
    //    private DataListBox requestFrom;
    private ProjectLookUp projectLookUp;
    private LookUp crmAccountLookUp;
    private DateTimeWidget dateTime;
    private TermsAndDuePanel termsAndDuePanel;
    private Numbering numberWidget;
    private TextBox sqNumberTxtBox;
    private CusSupAddress companyShippingAddress;
    private TextArea introduction;
    private EditableTable itemsTable;
    private EditableGrid grid;
    private WfmButton2 saveAsDraft;
    private WfmButton2 submitButton;
    private SplitButton approveButtons;
    private SplitButtonItem approveButton;
    private SplitButtonItem emailButtonItem;
    private NoteHistoryWidget noteHistoryWidget;
    private Params formParameters;
    private MaterialLink showMoreLink;
    //    private KpiSwitcher sendNotificationToSupplierBox;
    private FooterUploadPanel footerUploadPanel;
    private QuoteAdditionalFields quoteAdditionalFields;
    private MaterialLink customerBalanceLink;
    private ChosenApproversWidget approver;
    private ColumnConfig[] columnConfigs;
    private HashMap<String, CompanyCustomFieldItem> customFieldsMap;
    private SplitButton actions;
    private InvoiceCustomFieldsView firstCustomFieldsView;
    private boolean isApprover = false;
    private RFQData rfqData;
    private FooterInformer link;
    private SplitButton printPdfSplitButton;
    private DataListBox templates;
    private Integer relationID;
    private String relationType;
    private String relationName;
    Date currentDate = new Date();


    public RequestForQuoteView(String[] params) {
        super("requestforquoteadd", wfmStrings.add() + "&nbsp;");
        property = new Property(getPropertyCode());
        setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.requestForQuote()));
        initFormParameters(params);
    }

    public RequestForQuoteView(Integer objectID) {
        super("edit", wfmStrings.edit() + "&nbsp;");
        property = new Property(getPropertyCode());
        setDescription(property.getSingular(wfmStrings.requestForQuote()));
        this.objectID = objectID;
        initFormParameters(null);
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    @Override
    protected String getWikiCode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void initialize() {

        setLabelEndParameter("");

        customerBalanceLink = new MaterialLink(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        customerBalanceLink.setHref("javaScript:void(0)");
        customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "none");

        boolean hasPermissonCustomerQuick = Utils.hasPermission(PermissionConstants.ACCOUNTING_CUSTOMER_QUICK_ADD);
        boolean hasPermissonCustomerAdd = Utils.hasPermission(PermissionConstants.ACCOUNTING_CUSTOMER_ADD);

        crmAccountLookUp = new SmartCrmAccountLookup(CrmAccountLookUp.CUSTOMER, true, () -> {
            if (hasPermissonCustomerQuick) {
                new CusSuppQuickAddView(CrmAccountLookUp.CUSTOMER, crmAccountLookUp.getLastValueBeforeClick());
            } else if (hasPermissonCustomerAdd) {
                SinksContainerFactory.entryPoint.onHistoryChanged("client|add/add");
            }
        }, false, hasPermissonCustomerQuick || hasPermissonCustomerAdd);
        crmAccountLookUp.ensureDebugId("rfq_view_crmAccountLookUp");

        crmAccountLookUp.getSuggestBox().addSelectionHandler(sh -> {
            if (crmAccountLookUp.getSelectedItemID() != null) {
                InvoiceService.App.get().getClientOrSupplier(crmAccountLookUp.getSelectedItemID(), RECEIVABLE, new AsyncCallback<TypeItem>() {
                    @Override
                    public void onFailure(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(TypeItem typeItem) {
                        if (typeItem.getSupplierCustomerBalance() >= 0) {
                            customerBalanceLink.setText(AccountingUtils.get().formatPrice(typeItem.getSupplierCustomerBalance()));
                        } else {
                            customerBalanceLink.setText("(" + AccountingUtils.get().formatPrice((-1) * typeItem.getSupplierCustomerBalance()) + ")");
                        }
                        customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                        customerBalanceLink.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("customerBalance|customerBalance/" + crmAccountLookUp.getSelectedItemID() + "/" + CrmAccountItem.CUSTOMER,
                                wfmStrings.balance() + ": " + typeItem.getName(), typeItem.getName()));

                        if (termsAndDuePanel != null && typeItem != null && typeItem.getTermsItem() != null) {
                            termsAndDuePanel.setData(TERMS_TYPE, termsAndDuePanel.getDueDate(), typeItem.getTermsItem());
                        }
                    }
                });
            }
        });

        actions = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        actions.setVisible(false);
        saveAsDraft = new WfmButton2(wfmStrings.draft(), Constants.BTN_DEFAULT_OUTLINE, event -> {
            saveAsDraft.setEnabled(false);
            save(Constants.RFQ_DRAFT);
        });
        saveAsDraft.setVisible(false);
        saveAsDraft.ensureDebugId("saveAsDraft-button");

        submitButton = new WfmButton2(wfmStrings.submitForApproval(), WfmButton2.BTN_PRIMARY);
        submitButton.setVisible(false);
        submitButton.ensureDebugId("submit-button");
        submitButton.addClickHandler(clickEvent -> {
            submitButton.setEnabled(false);
            save(Constants.RFQ_SUBMITTED);
        });

        approveButtons = new SplitButton(97, BTN_PRIMARY);
        approveButtons.ensureDebugId("saveAndApprove");
        approveButtons.setVisible(false);

        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        printPdfSplitButton.setVisible(false);

        approver = new ChosenApproversWidget(RelationItem.TYPE_REQUEST_FOR_QUOTE, objectID);
        projectLookUp = new ProjectLookUp(null);
        projectLookUp.ensureDebugId("projectLookUp");
        dateTime = new DateTimeWidget(30);
        dateTime.getTime().setPaddingLeft(6);
        termsAndDuePanel = new TermsAndDuePanel(accountingStrings.validDate(), true);
        termsAndDuePanel.ensureDebugId("termsAndDuePanel");
        numberWidget = new Numbering();
        numberWidget.ensureDebugId("requestNumber");
        sqNumberTxtBox = new TextBox(true);
        sqNumberTxtBox.ensureDebugId("sqNumber");
        companyShippingAddress = new CusSupAddress();

        companyShippingAddress.getAddAddressLink().ensureDebugId("addShippingAddrLink");
        companyShippingAddress.getEditAddressLink().ensureDebugId("editShippingAddrLink");

        companyShippingAddress.getAddAddressLink().setVisible(true);
        companyShippingAddress.getEditAddressLink().setVisible(true);

        companyShippingAddress.setVisible(true);

        CommonService.App.get().getCompanyAddress(new AbstractAsyncCallback<CompanyAddress>() {
            @Override
            public void success(CompanyAddress result) {
                if (result != null && result.getMailAddresses() != null) {
                    companyShippingAddress.getAddressList().setItems(result.getMailAddresses());
                }
            }
        });

//        requestFrom = new DataListBox();
//        requestFrom.ensureDebugId("requestFrom-list");
//        requestFrom.setWithoutNullLabel(true);
//        requestFrom.setItems(new SelectItem[]{
//                new SelectItem(Constants.COMPANY_SUPPLIERS, accountingStrings.companySuppliers())
//        });
//        requestFrom.setSelected(Constants.COMPANY_SUPPLIERS);
//        requestFrom.addValueChangeHandler(event -> onRequestFromChanged());

//        sendNotificationToSupplierBox = new KpiSwitcher();
//        sendNotificationToSupplierBox.setValue(false);
//        sendNotificationToSupplierBox.ensureDebugId("sendNotificatin-checkBox");

        quoteAdditionalFields = createAdvancedOptions();
        quoteAdditionalFields.getOptionsContainer().removeFromParent();
        quoteAdditionalFields.addToMailAddressBodyContainer(companyShippingAddress);
        showMoreLink = new MaterialLink(wfmStrings.showAdditionalFields());
        showMoreLink.addClickHandler(ch -> showAdvancedOptions(wfmStrings.additionalFields(), quoteAdditionalFields));
        showMoreLink.addStyleName("btn-flat file--RequestForQuoteVew"); //https://prnt.sc/rmfh75

        dateTime.setDateTime(currentDate);

        numberWidget.getTxtPrefix().setWidth("60px");
        numberWidget.getTxtNumber().setWidth("100px");
        numberWidget.getTxtNumber().setAlignment(ValueBoxBase.TextAlignment.RIGHT);

        property = new Property(getPropertyCode());

        if (relationID != null && relationType != null) {
            getRelationName(relationID, relationType);
        }

        addTitleField(AccountingCustomFormConstants.TITLE, getMainTitle(property.getSingular(wfmStrings.requestForQuote())));

        FormGroup showMoreField = new FormGroup(showMoreLink);
        showMoreField.setLabel("&nbsp;");

        FormGroup customerField = new FormGroup(crmAccountLookUp);
        customerField.ensureDebugId(InvoiceFormFields.CUSTOMER);

        Div clientFieldLabel = customerField.getGroupLabel();
        clientFieldLabel.addStyleName("label-group");

        clientFieldLabel.add(new Span(Property.get(Constants.CLIENT_LIST, wfmStrings.customer())));

        Span balance = new Span(wfmStrings.balance() + ": ");
        balance.add(customerBalanceLink);
        clientFieldLabel.add(balance);

        addField(AccountingCustomFormConstants.INPUT_SHOW_MORE, showMoreField);

        addField(AccountingCustomFormConstants.CUSTOMER, customerField);
        addField(AccountingCustomFormConstants.DATE, new FormGroup(accountingStrings.requestDate(), dateTime));
        addField(AccountingCustomFormConstants.DUE_DATE, termsAndDuePanel.getTermsDueAsField());
        addField(AccountingCustomFormConstants.NUMBER, new FormGroup(property.getShortForNumber(wfmStrings.requestForQuote()), numberWidget));
        addField(AccountingCustomFormConstants.SQ_NUMBER, new FormGroup(accountingStrings.sq(), sqNumberTxtBox));

        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.customFields());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, RequestForQuoteView.this, (sender, args) -> {
            if (approver.getFirstApproverLookUp() != null) {
                approver.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                    SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                    Integer itemId = item != null ? item.getId() : null;
                    Integer currentUserId = this.currentUserId != null ? this.currentUserId : Utils.getUserID();
                    if (currentUserId.equals(itemId)) {
                        approveButtons.setVisible(true);
                        submitButton.setVisible(false);
                    } else {
                        submitButton.setVisible(true);
                        approveButtons.setVisible(false);
                    }
                });
                if (approver.getFirstApproverLookUp().getSelectedItem() != null) {
                    SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        approveButtons.setVisible(true);
                        submitButton.setVisible(false);
                    } else {
                        approveButtons.setVisible(false);
                        submitButton.setVisible(true);
                    }
                }
            }
        });

        show();
    }

    /**
     * Try to pass parameter
     */
    private void getAndApplyIntroduction(RFQData data) {
        if (objectID != null && data != null && data.getIntroduction() != null && !"".equals(data.getIntroduction())) {
            introduction.setText(data.getIntroduction());
        } else {
            InvoiceService.App.get().getPaymentIntroduction(Constants.REQUEST_FOR_QUOTE_INTR, new AbstractAsyncCallback<SelectItem[]>() {
                public void failure(Throwable caught) {
                }

                public void success(SelectItem[] result) {
                    if (result != null && result.length > 0) {
                        introduction.setText(result[0].getDescription());
                    }
                }
            });
        }
    }

//    private void onRequestFromChanged() {
//        boolean isSupplierEnabled = Constants.COMPANY_SUPPLIERS.equals(requestFrom.getSelectedId());
//        EditableGrid grid = itemsTable.getGrid();
//        for (int i = 0; i < grid.getRowCount(); i++) {
//            ((CrmAccountLookUp) itemsTable.getColumnById(i, ItemTableConstants.SUPPLIER)).setEnabled(isSupplierEnabled);
//        }
//    }

    private Widget[] getWidgets(RFQItem rfqItem) {
        Widget[] rowWidgets = new Widget[columnConfigs.length];
        boolean validRfqItem = rfqItem != null;
        int index = 0;
        final SmartProductLookUp productLookUp = new SmartProductLookUp(Constants.PAYABLE);
        TextArea2 description = new TextArea2(10000);
        ItemUploadTable uploadPanel = new ItemUploadTable(Constants.F_RFQ);
        CustomCellTextBox qtyTxtBox = new CustomCellTextBox();
        CustomCellTextBox costPriceTxtBox = new CustomCellTextBox();
        MeasurementsLookUp measurementsLookUp = new MeasurementsLookUp();
        CustomCellTextBox txtCommission = new CustomCellTextBox();
        CustomCellTextBox txtRemarks = new CustomCellTextBox();
        final LinkableCrmAccountLookUp supplierLookUp = new LinkableCrmAccountLookUp(CrmAccountLookUp.SUPPLIER, true);
        for (ColumnConfig config : columnConfigs) {
            switch (config.getName()) {
                case ItemTableConstants.PRODUCT:
                    productLookUp.getSuggestBox().setWidth("200px");
                    productLookUp.setAutocompleteOff();
                    productLookUp.setEnabled(!config.isDisabled());
                    productLookUp.setLinkCommand(() -> new ProductQuickAddForm(true, item -> {
                        productLookUp.addProductItem(item);
                        description.setText(((ProductSelectItem) productLookUp.getSelectedData()).getDescription());
                        if (productLookUp.getOnSelectListener() != null) {
                            productLookUp.getOnSelectListener().execute();
                        }
                    }));

                    productLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
                        supplierLookUp.clear();
                        setItemValues(productLookUp.getSelectedItem(), itemsTable.getGrid().getCurrentRow());
                        description.setText(((ProductSelectItem) productLookUp.getSelectedData()).getDescription());
                        CustomCell descriptionCustomCell = (CustomCell) itemsTable.getColumnCellWidgetById(grid.getCurrentRow(), ItemTableConstants.DESCRIPTION);
                        descriptionCustomCell.InActive();
                        if (formParameters != null && "supplier".equals(formParameters.getCrmFormName()) && formParameters.getSupplierId() != null) {
                            QuoteService.App.get().getSupplier(formParameters.getSupplierId(), new AbstractAsyncCallback<SelectItem>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    super.onFailure(caught);
                                }

                                @Override
                                public void onSuccess(SelectItem result) {
                                    super.onSuccess(result);
                                    supplierLookUp.setSelected(result);
                                    supplierLookUp.setEnabled(false);
                                }
                            });

                        }
                    });


                    rowWidgets[index++] = productLookUp;

                    if (validRfqItem) {
                        if (rfqItem.getProduct() != null && rfqItem.getProduct().getId() != null && rfqItem.getProduct().getName() != null) {
                            productLookUp.addProductItem(rfqItem.getProduct());
                        } else if (rfqItem.getName() != null) {
                            productLookUp.getSuggestBox().setText(rfqItem.getName());
                        }
                    }
                    break;
                case ItemTableConstants.DESCRIPTION:
                    description.hideCharacterLimitPanel();
                    description.setEnabled(!config.isDisabled());

                    rowWidgets[index++] = description;

                    if (validRfqItem) {
                        description.setText(rfqItem.getDescription());
                    }
                    break;
                case ItemTableConstants.QTY:
                    qtyTxtBox.setWidth("110px");
                    qtyTxtBox.setEnabled(!config.isDisabled());
                    Validation.addNumericKeyboardListener(qtyTxtBox, 2);
                    qtyTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

                    rowWidgets[index++] = qtyTxtBox;

                    if (validRfqItem) {
                        if (rfqItem.getQty() != null) {
                            qtyTxtBox.setText(AccountingUtils.get().formatQty(rfqItem.getQty()));
                        }
                    }
                    break;
                case ItemTableConstants.MEASUREMENT:
                    measurementsLookUp.getSuggestBox().setWidth("110px");
                    measurementsLookUp.setEnabled(!config.isDisabled());

                    rowWidgets[index++] = measurementsLookUp;

                    if (validRfqItem) {
                        if (rfqItem.getMeasurement() != null) {
                            measurementsLookUp.addItem(rfqItem.getMeasurement());
                        }
                    }
                    break;
                case ItemTableConstants.UNITPRICE:
                    costPriceTxtBox.setWidth("110px");
                    costPriceTxtBox.setEnabled(!config.isDisabled());
                    Validation.addNumericKeyboardListener(costPriceTxtBox, 2);
                    costPriceTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

                    rowWidgets[index++] = costPriceTxtBox;

                    if (validRfqItem) {
                        if (rfqItem.getUnitCost() != null) {
                            costPriceTxtBox.setText(AccountingUtils.get().formatPrice(rfqItem.getUnitCost()));
                        }
                    }
                    break;
                case ItemTableConstants.RECEIPTS:
                    rowWidgets[index++] = uploadPanel;
                    if (validRfqItem) {
                        uploadPanel.setFiles(rfqItem.getAttachments());
                    }
                    break;
                case ItemTableConstants.COMISSION:
                    txtCommission.setWidth("110px");
                    txtCommission.setEnabled(!config.isDisabled());
                    Validation.addNumericKeyboardListener(txtCommission, 2);
                    txtCommission.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

                    rowWidgets[index++] = txtCommission;

                    if (validRfqItem) {
                        if (rfqItem.getCommission() != null) {
                            txtCommission.setText(AccountingUtils.get().formatQty(rfqItem.getCommission()));
                        }
                    }
                    break;
                case ItemTableConstants.REMARK:
                    rowWidgets[index++] = txtRemarks;
                    txtRemarks.setEnabled(!config.isDisabled());

                    if (validRfqItem) {
                        txtRemarks.setText(rfqItem.getReMarks());
                    }
                    break;
                case ItemTableConstants.SUPPLIER:
                    supplierLookUp.getSuggestBox().setWidth("165px");
                    supplierLookUp.setEnabled(!config.isDisabled());
                    supplierLookUp.setClickHandler(() -> {
                        if (rfqItem.getSupplier() != null) {
                            ContactPopup contactPopup = new ContactPopup(rfqItem.getSupplier().getId(), rfqData.getObjectID());
                            contactPopup.open();
                        }
                    });

                    rowWidgets[index++] = supplierLookUp;

                    if (validRfqItem) {
                        if (rfqItem.getSupplier() != null) {
                            supplierLookUp.addItem(rfqItem.getSupplier());
                        }
                    }
                    break;
                default:
                    CompanyCustomFieldItem fieldItem = customFieldsMap.get(config.getName()).cloneObject();
                    if (Constants.UI_TYPE_TEXTBOX.equals(fieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(fieldItem.getUiType()) || Constants.UI_TYPE_URL.equals(fieldItem.getUiType())) {
                        rowWidgets[index] = new CustomTextBoxField(fieldItem);
                    }
                    if (Constants.UI_TYPE_PERCENTAGE.equals(fieldItem.getUiType())) {
                        rowWidgets[index] = new CustomPercentageField(fieldItem);
                    } else if (Constants.UI_TYPE_DROPDOWN.equals(fieldItem.getUiType())) {
                        rowWidgets[index] = new CustomDropDownField(fieldItem);
                    } else if (Constants.UI_TYPE_DATEPICKER.equals(fieldItem.getUiType())) {
                        rowWidgets[index] = new CustomDatePicker(fieldItem);
                    } else if (Constants.UI_TYPE_DATEPICKER_TIME.equals(fieldItem.getUiType())) {
                        rowWidgets[index] = new CustomDateTime(fieldItem);
                    } else if (Constants.UI_TYPE_TEXTAREA.equals(fieldItem.getUiType())) {
                        rowWidgets[index] = new CustomTextAreaField(fieldItem);
                    } else if (Constants.UI_TYPE_LOOKUP.equals(fieldItem.getUiType())) {
                        rowWidgets[index] = new CustomFieldLookUpField(fieldItem);
                    } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(fieldItem.getUiType())) {
                        rowWidgets[index] = new CustomFieldMultiLookUpField(fieldItem);
                    }
                    if (validRfqItem && rfqItem.getItemCustomFields() != null && !rfqItem.getItemCustomFields().isEmpty()) {
                        CompanyCustomFieldItem fitem = rfqItem.getCustomFieldByCode(fieldItem.getColumnCode());
                        if (fitem != null) {
                            ((CustomFieldInterface) rowWidgets[index]).setFieldItem(fitem);
                        }
                    }
                    index++;

                    break;


            }
        }

        return rowWidgets;
    }

    private ColumnConfig[] getColumns(RFQData rfqData) {
        ColumnConfig[] columns;
        int index = 0;
        if (rfqData.getCustomItemColumns() != null && rfqData.getCustomItemColumns().length > 0) {
            columns = new ColumnConfig[rfqData.getCustomItemColumns().length];
            for (ColumnConfigs column : rfqData.getCustomItemColumns()) {

                boolean isPixel = (column.getWidth() == null || column.getWidth() == 0);
                ColumnConfig columnConfig;

                switch (column.getCode()) {
                    case ItemTableConstants.PRODUCT:
                        columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.PRODUCT, column.isChanged() ? column.getTitle() : wfmStrings.item(), Utils.getColumnWidth(column.getWidth(), 200), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setDisabled(column.isDisabled());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.DESCRIPTION:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.DESCRIPTION, column.isChanged() ? column.getTitle() : wfmStrings.description(), Utils.getColumnWidth(column.getWidth(), 250), column.isRequired());
                        columnConfig.setDisabled(column.isDisabled());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.QTY:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.QTY, column.isChanged() ? column.getTitle() : wfmStrings.qty(), Utils.getColumnWidth(column.getWidth(), 75), column.isRequired(), Constants.RIGHT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setDisabled(column.isDisabled());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.MEASUREMENT:
                        columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.MEASUREMENT, column.isChanged() ? column.getTitle() : wfmStrings.measurement(), Utils.getColumnWidth(column.getWidth(), 75), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setDisabled(column.isDisabled());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.UNITPRICE:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.UNITPRICE, column.isChanged() ? column.getTitle() : wfmStrings.cost(), Utils.getColumnWidth(column.getWidth(), 75), column.isRequired(), Constants.RIGHT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setDisabled(column.isDisabled());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.COMISSION:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.COMISSION, column.isChanged() ? column.getTitle() : wfmStrings.commission() + " (%)", Utils.getColumnWidth(column.getWidth(), 75), column.isRequired(), Constants.RIGHT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setDisabled(column.isDisabled());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.RECEIPTS:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.RECEIPTS, column.isChanged() ? column.getTitle() : accountingStrings.receipts(), Utils.getColumnWidth(column.getWidth(), 80), column.isRequired(), Constants.LEFT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setDisabled(column.isDisabled());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.SUPPLIER:
                        columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.SUPPLIER, column.isChanged() ? column.getTitle() : Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), Utils.getColumnWidth(column.getWidth(), 175), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setDisabled(column.isDisabled());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.WAREHOUSE:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.REMARK, column.isChanged() ? column.getTitle() : wfmStrings.remarks(), Utils.getColumnWidth(column.getWidth(), 150), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setDisabled(column.isDisabled());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    default:
                        if (column.getCode() != null && column.getCode().contains("date_value")) {
                            columnConfig = new ColumnConfig(CustomCell.class, column.getCode(), column.getTitle(), Utils.getColumnWidth(column.getWidth(), 165), column.isRequired(), true);
                            columnConfig.setPixel(isPixel);
                            columnConfig.setDisabled(column.isDisabled());
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columns[index++] = columnConfig;
                        } else {
                            columnConfig = new ColumnConfig(CustomCell.class, column.getCode(), column.getTitle(), Utils.getColumnWidth(column.getWidth(), 100), column.isRequired(), true);
                            columnConfig.setPixel(isPixel);
                            columnConfig.setDisabled(column.isDisabled());
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columns[index++] = columnConfig;
                        }
                        break;
                }
            }
        } else {
            columns = new ColumnConfig[6];
            columns[index++] = new ColumnConfig(LookUpCell.class, ItemTableConstants.PRODUCT, wfmStrings.item(), 200, true);
            columns[index++] = new ColumnConfig(CustomCell.class, ItemTableConstants.DESCRIPTION, wfmStrings.description(), 250/*280*/, false);
            columns[index++] = new ColumnConfig(CustomCell.class, ItemTableConstants.QTY, wfmStrings.qty(), 75, true, Constants.RIGHT_ALIGN_CELL);
            columns[index++] = new ColumnConfig(LookUpCell.class, ItemTableConstants.MEASUREMENT, wfmStrings.measurement(), 75, false);
            columns[index++] = new ColumnConfig(CustomCell.class, ItemTableConstants.UNITPRICE, wfmStrings.cost(), 75, true, Constants.RIGHT_ALIGN_CELL);
//            columns[index++] = new ColumnConfig(CustomCell.class, ItemTableConstants.COMISSION, accountingStrings.comission2() + " (%)", 75, false, Constants.RIGHT_ALIGN_CELL);
//            columns[index++] = new ColumnConfig(CustomCell.class, ItemTableConstants.RECEIPTS, accountingStrings.receipts(), 80, false, Constants.LEFT_ALIGN_CELL);
            columns[index] = new ColumnConfig(LookUpCell.class, ItemTableConstants.SUPPLIER, Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), 175, true);
//            columns[index] = new ColumnConfig(CustomCell.class, ItemTableConstants.REMARK, wfmStrings.remarks(), 150, false);
        }
        return columns;
    }

    public String getMainTitle(String text, boolean... required) {
        return getTitle("<b class=customTitle><font size=+1>" + text + "</font></b>", required);
    }

    private void save(String status) {

        if (!validate(status)) {
            submitButton.setEnabled(true);
            saveAsDraft.setEnabled(true);
            return;
        }

        LoadingPanel.loading(true);

        RFQData rfqData = new RFQData();
        rfqData.setObjectID(objectID);
        rfqData.setCustomer(crmAccountLookUp.getSelectedItem());
        rfqData.setProject(projectLookUp.getSelectedItem());

        rfqData.setDate(new DateNonConvertable(dateTime.getDateTime()));
        if (termsAndDuePanel.isDueTypeSelected()) {
            rfqData.setValidUntil(new DateNonConvertable(termsAndDuePanel.getDueDate()));
        } else {
            rfqData.setValidUntil(new DateNonConvertable(termsAndDuePanel.getDueDate()));
            rfqData.setInvoiceTermsItem(termsAndDuePanel.getInvoiceTerms());
        }
//        rfqData.setValidUntil(new DateNonConvertable(DateUtil.getDayLastTime(validUntil.getDate())));
        rfqData.setNumberData(numberWidget.getNumberData(false));
        rfqData.setSqNumber(sqNumberTxtBox.getText());
        rfqData.setIntroduction(introduction.getText());
        rfqData.setStatusCode(status);
        rfqData.setHistoryList(noteHistoryWidget.getNotes().toArray(new HistoryListItem[]{}));
//        rfqData.setSendNotificationToSuppliers(sendNotificationToSupplierBox.getValue());
        rfqData.setOpportunityID(formParameters.getOpportunityID());
        rfqData.setMailAddressId(companyShippingAddress.getAddressList().getSelectedId());
        rfqData.setRfpIds(formParameters.getExternalObjectIDList());
        rfqData.setAttachments(footerUploadPanel.getAttachedFiles());
        if (quoteAdditionalFields.getCustomFieldsData() != null) {
            ArrayList<CompanyCustomFieldItem> getAllCustomFields = firstCustomFieldsView.getData();
            getAllCustomFields.addAll(quoteAdditionalFields.getCustomFieldsData());
            rfqData.setCustomFieldList(getAllCustomFields);
        } else {
            if (firstCustomFieldsView != null) {
                rfqData.setCustomFieldList(firstCustomFieldsView.getData());
            }
        }
        if (isApprover) {
            rfqData.setApprovers(approver.getChosenApprovers());
        }
        if (relationID != null && relationType != null) {
            ArrayList<RelationItem> relations = new ArrayList<>();
            relations.add(new RelationItem(null, relationID, relationType, relationName, null, TYPE_REQUEST_FOR_QUOTE, null));
            rfqData.setRelations(relations);
        }

        EditableGrid grid = itemsTable.getGrid();
        for (int i = 0; i < grid.getRowCount(); i++) {
            if (itemsTable.isItemValid(i)) {
                SmartProductLookUp productLookUp = (SmartProductLookUp) itemsTable.getColumnById(i, ItemTableConstants.PRODUCT);
                TextArea2 descriptionTxtArea = (TextArea2) itemsTable.getColumnById(i, ItemTableConstants.DESCRIPTION);
                ItemUploadTable uploadPanel = (ItemUploadTable) itemsTable.getColumnById(i, ItemTableConstants.RECEIPTS);
                CustomCellTextBox quantityTxtBox = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.QTY);
                CustomCellTextBox txtCommission = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.COMISSION);
                CustomCellTextBox txtRemark = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.REMARK);
                MeasurementsLookUp measurementLookUp = (MeasurementsLookUp) itemsTable.getColumnById(i, ItemTableConstants.MEASUREMENT);
                CustomCellTextBox costperunit = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.UNITPRICE);
                CrmAccountLookUp supplierLookUp = (CrmAccountLookUp) itemsTable.getColumnById(i, ItemTableConstants.SUPPLIER);

                RFQItem rfqItem = new RFQItem();
                if (productLookUp != null) {
                    if (productLookUp.getSelectedData() != null) {
                        rfqItem.setProduct((ProductSelectItem) productLookUp.getSelectedData());
                    } else {
                        rfqItem.setProduct(new ProductSelectItem(null, productLookUp.getText()));
                    }
                }
                if (descriptionTxtArea != null) {
                    rfqItem.setDescription(descriptionTxtArea.getText());
                }
                if (uploadPanel != null) {
                    rfqItem.setAttachments(uploadPanel.getAttachedFiles());
                }
                if (quantityTxtBox != null) {
                    rfqItem.setQty(AccountingUtils.get().parseToBigDecimal(quantityTxtBox.getText()));
                }
                if (txtCommission != null && txtCommission.getText() != null && !txtCommission.getText().isEmpty()) {
                    rfqItem.setCommission(AccountingUtils.get().parseToBigDecimal(txtCommission.getText()));
                }
                if (measurementLookUp != null) {
                    rfqItem.setMeasurement(measurementLookUp.getSelectedItem());
                }
                if (costperunit != null) {
                    rfqItem.setUnitCost(AccountingUtils.parsePriceToBigDecimal(costperunit.getText() != null && !"".equals(costperunit.getText().trim()) ? costperunit.getText() : "0"));
                }
                if (supplierLookUp != null) {
                    rfqItem.setSupplier(supplierLookUp.getSelectedItem());
                }
                if (txtRemark != null) {
                    rfqItem.setReMarks(txtRemark.getText());
                }

                if (customFieldsMap != null && !customFieldsMap.isEmpty()) {
                    ArrayList<CompanyCustomFieldItem> fieldItems = new ArrayList<>();
                    for (String key : customFieldsMap.keySet()) {
                        CustomFieldInterface customField = (CustomFieldInterface) itemsTable.getColumnById(i, key);
                        if (customField != null) {
                            fieldItems.add(customField.getFieldItem());
                        }
                    }

                    if (!fieldItems.isEmpty()) {
                        rfqItem.setItemCustomFields(fieldItems);
                    }
                }

                rfqData.getItems().add(rfqItem);
            }
        }

        QuoteService.App.get().saveRFQData(rfqData, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Integer result) {
                if (result != null && result == -1) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.numberAlreadyExist(), Info.Type.WARNING);
                } else {
                    LoadingPanel.loading(false);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_REQUEST_FOR_QUOTE_ADDED, result, RequestForQuoteView.this);
                    closeTab();
                }
            }
        });
    }

    public void sendEmail(RFQData result) {
        if (result != null) {
            goTo("emailcompose|add/add/" + "/" + TYPE_REQUEST_FOR_QUOTE + "/" + result.getObjectID() + "/" + result.getNumberData().getNumberString());
        }
    }

    private boolean validate(String status) {
        int errors = 0;

        if (!Constants.RFQ_DRAFT.equals(status) && firstCustomFieldsView != null && !firstCustomFieldsView.validateRequiredFields()) {
            errors++;
        }
        if (!Constants.RFQ_DRAFT.equals(status) && !validateCustomFields()) {
            errors++;
        }
        if (!Validation.validateDateTime(dateTime)) {
            errors++;
        }
        errors += !termsAndDuePanel.validate() ? 1 : 0;

        if (termsAndDuePanel.isDueTypeSelected() && termsAndDuePanel.getDueDate() != null) {
            DateUtil.getDayLastTime(termsAndDuePanel.getDueDate());
        }
        if (!numberWidget.validate()) {
            errors++;
        }
        if (!validateItemsTable(status)) {
            errors++;
        }
        if (isApprover) {
            if (!approver.isValid()) {
                errors++;
            }
        }
        if (dateTime.getDateField().getDate() != null && termsAndDuePanel.getDueDate() != null
                && !Validation.validateDateOrder(dateTime.getDateField().getDate(), DateUtil.getDayLastTime(termsAndDuePanel.getDueDate()))) {
            WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, property.getSingular(accountingStrings.checkRfqDate(), wfmStrings.requestForQuote()));
            messageBox.open();
            errors++;
        }
        return errors <= 0;
    }

    private boolean areOtherRowsAffected(int rowID) {
        boolean result = false;

        SmartProductLookUp productLookUp = (SmartProductLookUp) itemsTable.getColumnById(rowID, ItemTableConstants.PRODUCT);
        CustomCellTextBox qtyTxtBox = (CustomCellTextBox) itemsTable.getColumnById(rowID, ItemTableConstants.QTY);
        CrmAccountLookUp supplierLookUp = (CrmAccountLookUp) itemsTable.getColumnById(rowID, ItemTableConstants.SUPPLIER);

        TextArea2 descriptionTxtArea = (TextArea2) itemsTable.getColumnById(rowID, ItemTableConstants.DESCRIPTION);
        CustomCellTextBox txtCommission = (CustomCellTextBox) itemsTable.getColumnById(rowID, ItemTableConstants.COMISSION);
        CustomCellTextBox txtRemark = (CustomCellTextBox) itemsTable.getColumnById(rowID, ItemTableConstants.REMARK);
        MeasurementsLookUp measurementLookUp = (MeasurementsLookUp) itemsTable.getColumnById(rowID, ItemTableConstants.MEASUREMENT);
        CustomCellTextBox costperunit = (CustomCellTextBox) itemsTable.getColumnById(rowID, ItemTableConstants.UNITPRICE);
        result |= descriptionTxtArea != null && (descriptionTxtArea.getText() != null && !"".equals(descriptionTxtArea.getText().trim()));
        result |= txtCommission != null && (txtCommission.getText() != null && !"".equals(txtCommission.getText().trim()));
        result |= txtRemark != null && (txtRemark.getText() != null && !"".equals(txtRemark.getText().trim()));
        result |= measurementLookUp != null && (measurementLookUp.getSelectedItem() != null && measurementLookUp.getSelectedItem().getId() != null);
        result |= costperunit != null && (costperunit.getText() != null && !"".equals(costperunit.getText().trim()));
        result |= productLookUp != null && (productLookUp.getSelectedItem() != null && productLookUp.getSelectedItem().getId() != null);
        result |= supplierLookUp != null && (supplierLookUp.getSelectedItem() != null && supplierLookUp.getSelectedItem().getId() != null);
        result |= qtyTxtBox != null && (qtyTxtBox.getText() != null && !"".equals(qtyTxtBox.getText().trim()));
        return result;
    }

    private int validateRequiredItems(int rowID, String status, List<CompanyCustomFieldItem> requiredCFs, ArrayList<String> requiredColumnCodes) {
        int errors = 0;
        SmartProductLookUp productLookUp = (SmartProductLookUp) itemsTable.getColumnById(rowID, ItemTableConstants.PRODUCT);
        CustomCellTextBox qtyTxtBox = (CustomCellTextBox) itemsTable.getColumnById(rowID, ItemTableConstants.QTY);
        CrmAccountLookUp supplierLookUp = (CrmAccountLookUp) itemsTable.getColumnById(rowID, ItemTableConstants.SUPPLIER);

        if (productLookUp.getText() == null || productLookUp.getText().isEmpty() || wfmStrings.searchTypeMessage().equals(productLookUp.getText())) {
            itemsTable.setColumnValid(ItemTableConstants.PRODUCT);
            errors++;
        }
        if (!Constants.RFQ_DRAFT.equals(status)) {
            if (requiredColumnCodes.isEmpty()) {
                if (!Validation.validateTextBoxRequired(qtyTxtBox)) {
                    itemsTable.setColumnValid(ItemTableConstants.QTY);
                    errors++;
                }
                if (supplierLookUp.getSelectedItem() == null) {
                    itemsTable.setColumnValid(ItemTableConstants.SUPPLIER);
                    errors++;
                }
            } else {
                if (requiredColumnCodes.contains(ItemTableConstants.DESCRIPTION)) {
                    TextArea2 descriptionTxtArea = (TextArea2) itemsTable.getColumnById(rowID, ItemTableConstants.DESCRIPTION);
                    if (!Validation.validateTextAreaRequired(descriptionTxtArea)) {
                        itemsTable.setColumnValid(ItemTableConstants.DESCRIPTION);
                        errors++;
                    }
                }
                if (requiredColumnCodes.contains(ItemTableConstants.RECEIPTS)) {
                    ItemUploadTable uploadPanel = (ItemUploadTable) itemsTable.getColumnById(rowID, ItemTableConstants.RECEIPTS);
                    if (uploadPanel.getAttachedFiles() == null || (uploadPanel.getAttachedFiles() != null && uploadPanel.getAttachedFiles().length == 0)) {
                        itemsTable.setColumnValid(ItemTableConstants.RECEIPTS);
                        errors++;
                    }
                }
                if (requiredColumnCodes.contains(ItemTableConstants.QTY)) {
                    if (!Validation.validateTextBoxRequired(qtyTxtBox)) {
                        itemsTable.setColumnValid(ItemTableConstants.QTY);
                        errors++;
                    }
                }
                if (requiredColumnCodes.contains(ItemTableConstants.COMISSION)) {
                    CustomCellTextBox txtCommission = (CustomCellTextBox) itemsTable.getColumnById(rowID, ItemTableConstants.COMISSION);
                    if (!Validation.validateTextBoxRequired(txtCommission)) {
                        itemsTable.setColumnValid(ItemTableConstants.COMISSION);
                        errors++;
                    }
                }
                if (requiredColumnCodes.contains(ItemTableConstants.REMARK)) {
                    CustomCellTextBox txtRemark = (CustomCellTextBox) itemsTable.getColumnById(rowID, ItemTableConstants.REMARK);
                    if (!Validation.validateTextBoxRequired(txtRemark)) {
                        itemsTable.setColumnValid(ItemTableConstants.REMARK);
                        errors++;
                    }
                }
                if (requiredColumnCodes.contains(ItemTableConstants.MEASUREMENT)) {
                    MeasurementsLookUp measurementLookUp = (MeasurementsLookUp) itemsTable.getColumnById(rowID, ItemTableConstants.MEASUREMENT);
                    if (!Validation.validateLookUpRequired(measurementLookUp)) {
                        itemsTable.setColumnValid(ItemTableConstants.MEASUREMENT);
                        errors++;
                    }
                }
                if (requiredColumnCodes.contains(ItemTableConstants.UNITPRICE)) {
                    CustomCellTextBox costperunit = (CustomCellTextBox) itemsTable.getColumnById(rowID, ItemTableConstants.UNITPRICE);
                    if (!Validation.validateTextBoxRequired(costperunit)) {
                        itemsTable.setColumnValid(ItemTableConstants.UNITPRICE);
                        errors++;
                    }
                }
                if (requiredColumnCodes.contains(ItemTableConstants.SUPPLIER)) {
                    if (!Validation.validateLookUpRequired(supplierLookUp)) {
                        itemsTable.setColumnValid(ItemTableConstants.SUPPLIER);
                        errors++;
                    }
                }
            }

            for (CompanyCustomFieldItem fieldItem : requiredCFs) {
                if (Constants.UI_TYPE_TEXTBOX.equals(fieldItem.getUiType())) {
                    TextBox t = (TextBox) itemsTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (!Validation.validateTextBoxRequired(t)) {
                        itemsTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                } else if (Constants.UI_TYPE_PERCENTAGE.equals(fieldItem.getUiType())) {
                    CustomPercentageField t = (CustomPercentageField) itemsTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (fieldItem.isRequired()) {
                        if (!Validation.validateIntegerTextBoxRequired(t)) {
                            itemsTable.setColumnValid(fieldItem.getColumnCode());
                            errors++;
                        }/* else {
                            if (t.getText() != null && t.getText().trim().length() > 0 && Double.valueOf(t.getText()).compareTo((double) 100) > 0) {
                                itemsTable.setColumnValid(fieldItem.getColumnCode());
                                errors++;
                            }
                        }*/
                    } else {
                        if (t.getText() != null && t.getText().length() > 0 && Double.valueOf(t.getText()).compareTo((double) 100) > 0) {
                            itemsTable.setColumnValid(fieldItem.getColumnCode());
                            errors++;
                        }
                    }
                } else if (Constants.UI_TYPE_TEXTBOX_EMAIL.equals(fieldItem.getUiType())) {
                    TextBox t = (TextBox) itemsTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (fieldItem.isRequired() || (!fieldItem.isRequired() && t.getText().length() > 0)) {
                        if (!Validation.validateEmailRequired(t)) {
                            itemsTable.setColumnValid(fieldItem.getColumnCode());
                            errors++;
                        }
                    }
                } else if (Constants.UI_TYPE_URL.equals(fieldItem.getUiType())) {
                    TextBox t = (TextBox) itemsTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (fieldItem.isRequired() || (!fieldItem.isRequired() && t.getText().length() > 0)) {
                        if (!Validation.validateUrl(t, null)) {
                            itemsTable.setColumnValid(fieldItem.getColumnCode());
                            errors++;
                        }
                    }
                } else if (Constants.UI_TYPE_DROPDOWN.equals(fieldItem.getUiType())) {
                    DataListBox t = (DataListBox) itemsTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (t.getSelectedItem() == null) {
                        itemsTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                } else if (Constants.UI_TYPE_TEXTAREA.equals(fieldItem.getUiType())) {
                    TextArea t = (TextArea) itemsTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (t.getText() == null) {
                        itemsTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                } else if (Constants.UI_TYPE_DATEPICKER.equals(fieldItem.getUiType())) {
                    DatePicker t = (DatePicker) itemsTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (!Validation.validateDate(t)) {
                        itemsTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                } else if (Constants.UI_TYPE_DATEPICKER_TIME.equals(fieldItem.getUiType())) {
                    DateTimeWidget t = (DateTimeWidget) itemsTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (!Validation.validateDateTime(t)) {
                        itemsTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                } else if (Constants.UI_TYPE_LOOKUP.equals(fieldItem.getUiType())) {
                    CustomFieldLookUpField t = (CustomFieldLookUpField) itemsTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (!Validation.validateLookUpRequired(t)) {
                        itemsTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                } else if (UI_TYPE_MULTI_LOOKUP.equals(fieldItem.getUiType())) {
                    CustomFieldMultiLookUpField t = (CustomFieldMultiLookUpField) itemsTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (t.getSelectedItems() == null || (t.getSelectedItems() != null && t.getSelectedItems().size() == 0)) {
                        t.addStyleName(ERROR_FORM_STYLE);
                        Utils.scrollIntoView(t.getElement());
                        itemsTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                }
            }
        }
        return errors;
    }

    private void colorizeErrorField(int rowID, String status, List<CompanyCustomFieldItem> requiredCFs, ArrayList<String> requiredColumnCodes) {
        SmartProductLookUp productLookUp = (SmartProductLookUp) itemsTable.getColumnById(rowID, ItemTableConstants.PRODUCT);
        CustomCellTextBox qtyTxtBox = (CustomCellTextBox) itemsTable.getColumnById(rowID, ItemTableConstants.QTY);
        CrmAccountLookUp supplierLookUp = (CrmAccountLookUp) itemsTable.getColumnById(rowID, ItemTableConstants.SUPPLIER);

        if (productLookUp.getSelectedItem() == null) {
            itemsTable.notValid(rowID, ItemTableConstants.PRODUCT);
        }
        if (!Constants.RFQ_DRAFT.equals(status)) {
            if (requiredColumnCodes.isEmpty()) {
                if (!Validation.validateTextBoxRequired(qtyTxtBox)) {
                    itemsTable.notValid(rowID, ItemTableConstants.QTY);
                }
                if (supplierLookUp.getSelectedItem() == null) {
                    itemsTable.notValid(rowID, ItemTableConstants.SUPPLIER);
                }
            } else {
                if (requiredColumnCodes.contains(ItemTableConstants.DESCRIPTION)) {
                    TextArea2 descriptionTxtArea = (TextArea2) itemsTable.getColumnById(rowID, ItemTableConstants.DESCRIPTION);
                    if (!Validation.validateTextAreaRequired(descriptionTxtArea)) {
                        itemsTable.notValid(rowID, ItemTableConstants.DESCRIPTION);
                    }
                }
                if (requiredColumnCodes.contains(ItemTableConstants.RECEIPTS)) {
                    ItemUploadTable uploadPanel = (ItemUploadTable) itemsTable.getColumnById(rowID, ItemTableConstants.RECEIPTS);
                    if (uploadPanel.getAttachedFiles() == null || (uploadPanel.getAttachedFiles() != null && uploadPanel.getAttachedFiles().length == 0)) {
                        itemsTable.notValid(rowID, ItemTableConstants.RECEIPTS);
                    }
                }
                if (requiredColumnCodes.contains(ItemTableConstants.QTY)) {
                    if (!Validation.validateTextBoxRequired(qtyTxtBox)) {
                        itemsTable.notValid(rowID, ItemTableConstants.QTY);
                    }
                }
                if (requiredColumnCodes.contains(ItemTableConstants.COMISSION)) {
                    CustomCellTextBox txtCommission = (CustomCellTextBox) itemsTable.getColumnById(rowID, ItemTableConstants.COMISSION);
                    if (!Validation.validateTextBoxRequired(txtCommission)) {
                        itemsTable.notValid(rowID, ItemTableConstants.COMISSION);
                    }
                }
                if (requiredColumnCodes.contains(ItemTableConstants.REMARK)) {
                    CustomCellTextBox txtRemark = (CustomCellTextBox) itemsTable.getColumnById(rowID, ItemTableConstants.REMARK);
                    if (!Validation.validateTextBoxRequired(txtRemark)) {
                        itemsTable.notValid(rowID, ItemTableConstants.REMARK);
                    }
                }
                if (requiredColumnCodes.contains(ItemTableConstants.MEASUREMENT)) {
                    MeasurementsLookUp measurementLookUp = (MeasurementsLookUp) itemsTable.getColumnById(rowID, ItemTableConstants.MEASUREMENT);
                    if (!Validation.validateLookUpRequired(measurementLookUp)) {
                        itemsTable.notValid(rowID, ItemTableConstants.MEASUREMENT);
                    }
                }
                if (requiredColumnCodes.contains(ItemTableConstants.UNITPRICE)) {
                    CustomCellTextBox costperunit = (CustomCellTextBox) itemsTable.getColumnById(rowID, ItemTableConstants.UNITPRICE);
                    if (!Validation.validateTextBoxRequired(costperunit)) {
                        itemsTable.notValid(rowID, ItemTableConstants.UNITPRICE);
                    }
                }
                if (requiredColumnCodes.contains(ItemTableConstants.SUPPLIER)) {
                    if (!Validation.validateLookUpRequired(supplierLookUp)) {
                        itemsTable.notValid(rowID, ItemTableConstants.SUPPLIER);
                    }
                }
            }
            for (CompanyCustomFieldItem fieldItem : requiredCFs) {
                if (Constants.UI_TYPE_TEXTBOX.equals(fieldItem.getUiType())) {
                    TextBox t = (TextBox) itemsTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (!Validation.validateTextBoxRequired(t)) {
                        itemsTable.notValid(rowID, fieldItem.getColumnCode());
                    }
                }
                if (Constants.UI_TYPE_TEXTAREA.equals(fieldItem.getUiType())) {
                    TextArea t = (TextArea) itemsTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (!Validation.validateTextAreaRequired(t)) {
                        itemsTable.notValid(rowID, fieldItem.getColumnCode());
                    }
                } else if (Constants.UI_TYPE_TEXTBOX_EMAIL.equals(fieldItem.getUiType())) {
                    TextBox t = (TextBox) itemsTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (!Validation.validateEmailRequired(t)) {
                        itemsTable.notValid(rowID, fieldItem.getColumnCode());
                    }
                } else if (Constants.UI_TYPE_URL.equals(fieldItem.getUiType())) {
                    TextBox t = (TextBox) itemsTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (!Validation.validateUrl(t, null)) {
                        itemsTable.notValid(rowID, fieldItem.getColumnCode());
                    }
                } else if (Constants.UI_TYPE_PERCENTAGE.equals(fieldItem.getUiType())) {
                    CustomPercentageField t = (CustomPercentageField) itemsTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (fieldItem.isRequired()) {
                        if (!Validation.validateIntegerTextBoxRequired(t)) {
                            itemsTable.notValid(rowID, fieldItem.getColumnCode());
                        } else {
                            if (t.getText() != null && t.getText().trim().length() > 0 && Double.valueOf(t.getText()).compareTo((double) 100) > 0) {
                                itemsTable.notValid(rowID, fieldItem.getColumnCode());
                            }
                        }
                    }/* else {
                        if (t.getText() != null && t.getText().length() > 0 && Double.valueOf(t.getText()).compareTo((double) 100) > 0) {
                            itemsTable.notValid(rowID, fieldItem.getColumnCode());

                        }
                    }*/
                } else if (Constants.UI_TYPE_DROPDOWN.equals(fieldItem.getUiType())) {
                    DataListBox t = (DataListBox) itemsTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (t.getSelectedItem() == null) {
                        itemsTable.notValid(rowID, fieldItem.getColumnCode());
                    }
                } else if (Constants.UI_TYPE_DATEPICKER.equals(fieldItem.getUiType())) {
                    DatePicker t = (DatePicker) itemsTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (!Validation.validateDate(t)) {
                        itemsTable.notValid(rowID, fieldItem.getColumnCode());
                    }
                } else if (Constants.UI_TYPE_DATEPICKER_TIME.equals(fieldItem.getUiType())) {
                    DateTimeWidget t = (DateTimeWidget) itemsTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (!Validation.validateDateTime(t)) {
                        itemsTable.notValid(rowID, fieldItem.getColumnCode());
                    }
                } else if (Constants.UI_TYPE_LOOKUP.equals(fieldItem.getUiType())) {
                    CustomFieldLookUpField t = (CustomFieldLookUpField) itemsTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (!Validation.validateLookUpRequired(t)) {
                        itemsTable.notValid(rowID, fieldItem.getColumnCode());
                    }
                } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(fieldItem.getUiType())) {
                    CustomFieldMultiLookUpField t = (CustomFieldMultiLookUpField) itemsTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (t.getSelectedItems() == null || (t.getSelectedItems() != null && t.getSelectedItems().size() == 0)) {
                        t.addStyleName(ERROR_FORM_STYLE);
                        Utils.scrollIntoView(t.getElement());
                        itemsTable.notValid(rowID, fieldItem.getColumnCode());
                    }
                }
            }

        }
    }

    public boolean validateItemsTable(String status) {
        itemsTable.setValidRows(0);
        EditableGrid grid = itemsTable.getGrid();
        List<CompanyCustomFieldItem> requiredAndEmailCFs = new ArrayList<>();

        for (ColumnConfig config : columnConfigs) {
            if (customFieldsMap != null && customFieldsMap.containsKey(config.getName()) && (customFieldsMap.get(config.getName()).isRequired() ||
                    (Constants.UI_TYPE_TEXTBOX_EMAIL.equals(customFieldsMap.get(config.getName()).getUiType())) ||
                    (Constants.UI_TYPE_URL.equals(customFieldsMap.get(config.getName()).getUiType())) ||
                    (Constants.UI_TYPE_PERCENTAGE.equals(customFieldsMap.get(config.getName()).getUiType())))) {
                requiredAndEmailCFs.add(customFieldsMap.get(config.getName()));
            }
        }
        boolean errorFound = false;

        ArrayList<String> requiredColumnCodes = new ArrayList<>();
        int requiredRow = 0;
        if (rfqData != null && rfqData.getCustomItemColumns() != null && rfqData.getCustomItemColumns().length > 0) {
            for (ColumnConfigs columnConfigs : rfqData.getCustomItemColumns()) {
                if (columnConfigs != null && columnConfigs.isRequired() && columnConfigs.getCompanyCustomFieldID() == null) {
                    requiredRow++;
                    requiredColumnCodes.add(columnConfigs.getCode());
                }
            }
        } else {
            requiredRow = 3;
        }

        for (int rowID = 0; rowID < grid.getRowCount(); rowID++) {
            int rowError = 0;
            itemsTable.resetValidation(rowID);
            rowError = validateRequiredItems(rowID, status, requiredAndEmailCFs, requiredColumnCodes);
            if (rowError == 0) { //if required fields are fullfilled
                itemsTable.setItemValid(rowID, true);
                itemsTable.incValidRow();
            } else if (rowError == requiredRow + requiredAndEmailCFs.size() || (Constants.RFQ_DRAFT.equals(status) && rowError == 1)) { // product, quantity, supplier
                if (!areOtherRowsAffected(rowID)) { // but there are NO secondary fields filled
                    itemsTable.setItemValid(rowID, false); // exclude
                } else {
                    colorizeErrorField(rowID, status, requiredAndEmailCFs, requiredColumnCodes);
                    errorFound = true;
                }
            } else {
                colorizeErrorField(rowID, status, requiredAndEmailCFs, requiredColumnCodes);
                errorFound = true;
            }
        }
        if (itemsTable.getValidRows() == 0) {
            colorizeErrorField(0, status, requiredAndEmailCFs, requiredColumnCodes);
            errorFound = true;
        }
        if (customFieldsMap != null && customFieldsMap.values().size() > 0) {
            return Validation.itemTableNumericCFMinValueValidate(itemsTable, customFieldsMap.values());
        } else {
            return !errorFound;
        }
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        QuoteService.App.get().getRFQData(objectID, formParameters, new AsyncCallback<RFQData>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(RFQData result) {
                rfqData = result;
                currentUserId = result.getCurrentUserId();
                isApprover = result.isApprover();
                if (result.getItemCustomFields() != null && !result.getItemCustomFields().isEmpty()) {
                    customFieldsMap = new HashMap<>();
                    for (CompanyCustomFieldItem field : result.getItemCustomFields()) {
                        customFieldsMap.put(field.getColumnCode(), field);
                    }
                }
                initItemTable(result);
                numberWidget.setNumberData(result.getNumberData());
                companyShippingAddress.getAddressList().setSelected(result.getMailAddressId());
                companyShippingAddress.setAddressDescription(result.getAddressAsString(false));
                if (result.getProject() != null) {
                    projectLookUp.setSelected(result.getProject());
                }

                if (result.getCustomer() != null) {
                    crmAccountLookUp.setSelected(result.getCustomer());
                }

                List<SplitButtonItem> splitButtonItems = new ArrayList<>();

                if (objectID == null) {
                    approveButton = new SplitButtonItem(RFQ_APPROVED, wfmStrings.saveAndApprove(), () -> save(RFQ_APPROVED));
                    approveButton.ensureDebugId("approve-button");
                    splitButtonItems.add(approveButton);
                }

                if (objectID != null) {
//                    requestFrom.setSelected(result.getRequestFrom());

                    dateTime.setDateTime(result.getDate().getNonConvertedDate());
                    sqNumberTxtBox.setText(result.getSqNumber());

                    getAndApplyIntroduction(result);

                    AtomicBoolean firstClick = new AtomicBoolean(true);
                    link.addClickHandler(event -> {
                        if (firstClick.get()) {
                            getLinkingUtil().getAddLinkSideNavBox();
                            getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(result.getRelations(), false);
                            firstClick.set(false);
                        } else {
                            getLinkingUtil().getAddLinkSideNavBox().show();
                        }

                    });
                    link.setBadgeCount(result.getRelations().size());

//                    sendNotificationToSupplierBox.setValue(result.isSendNotificationToSuppliers());

                    for (int i = 0; i < result.getItems().size(); i++) {
                        itemsTable.addRow(getWidgets(result.getItems().get(i)));
                    }

                    Integer additionalRows = DEFAULT_ITEM_ROWS - result.getItems().size();
                    for (int i = 0; i < additionalRows; i++) {
                        itemsTable.addRow(getWidgets(null));
                    }
                    List<SplitButtonItem> actionButtonList = new ArrayList<>();

                    if (Utils.hasPermission(PermissionConstants.RFQ_ADD_NEW_ACTIVITY_EVENT)) {
                        String addEventString = Property.get(Constants.EVENT_LIST, wfmStrings.addMess(), wfmStrings.event());
                        SplitButtonItem addEvent = new SplitButtonItem(addEventString, addEventString, () -> addActivity(result, Appointment.EVENT));
                        addEvent.ensureDebugId("addEvent");
                        actionButtonList.add(addEvent);
                    }
                    if (Utils.hasPermission(PermissionConstants.RFQ_ADD_NEW_ACTIVITY_LOG_A_CALL)) {
                        String callLogString = Property.get(Constants.LOGACALL, wfmStrings.logCall());
                        SplitButtonItem callLog = new SplitButtonItem(callLogString, callLogString, () -> addActivity(result, Appointment.CALL_LOG));
                        callLog.ensureDebugId("callALog");
                        actionButtonList.add(callLog);
                    }

                    actions.addItemList(actionButtonList);
                    actions.setVisible(true);


                    approveButton = new SplitButtonItem(RFQ_APPROVED, accountingStrings.updateAndApprove(), () -> save(RFQ_APPROVED));
                    approveButton.ensureDebugId("approve-button");
                    splitButtonItems.add(approveButton);


                    if (Utils.hasPermission(PermissionConstants.RFQ_SEND_EMAIL)) {
                        emailButtonItem = new SplitButtonItem(APPROVE_AND_SEND, wfmStrings.sendEmail(), () -> sendEmail(result));
                        emailButtonItem.ensureDebugId("emailButtonItem_Rfq");
                        splitButtonItems.add(emailButtonItem);
                    }

                    pdfOption(result);

                } else if (result != null && !result.getItems().isEmpty()) {
                    itemsTable.removeAllRows();
                    for (int i = 0; i < result.getItems().size(); i++) {
                        itemsTable.addRow(getWidgets(result.getItems().get(i)));
                    }

                    int additionalRows = DEFAULT_ITEM_ROWS - result.getItems().size();
                    for (int i = 0; i < additionalRows; i++) {
                        itemsTable.addRow(getWidgets(null));
                    }
                }
                approveButtons.addItemList(splitButtonItems);

                if (result.getCustomFieldList() != null && result.getCustomFieldList().size() > 0) {
                    ArrayList<CompanyCustomFieldItem> customFieldFirstItem = new ArrayList<>();
                    ArrayList<CompanyCustomFieldItem> customFieldItems = new ArrayList<>();

                    if (result.getCustomFieldList().size() == 1) {
                        customFieldFirstItem.add(result.getCustomFieldList().get(0));
                    } else {
                        for (int i = 0; i < result.getCustomFieldList().size(); i++) {
                            if (i == 0) {
                                customFieldFirstItem.add(result.getCustomFieldList().get(0));
                            } else {
                                customFieldItems.add(result.getCustomFieldList().get(i));
                            }
                        }


                        quoteAdditionalFields.createAndAppendQuoteCustomFieldsView(ViewAddFiledsCodeName.RequestForQuoteAdd, customFieldItems);
                    }
                    firstCustomFieldsView = new InvoiceCustomFieldsView(ViewAddFiledsCodeName.RequestForQuoteAdd, customFieldFirstItem, null, 1);
                    addField(AccountingCustomFormConstants.CUSTOM_FIELD_ITEM, firstCustomFieldsView);
                }

                if (result.isApprover()) {
                    if (Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
                        GColumn projectCol = new GColumn(GColumnEnum.COL_6, new FormGroup(Property.get(Constants.PROJECT, wfmStrings.project()), projectLookUp));
                        GColumn approverCol = new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.approver(), approver));
                        addField(AccountingCustomFormConstants.PROJECT_MANAGER, new GRow(projectCol, approverCol));
                    } else {
                        FormGroup approverBox = new FormGroup(wfmStrings.approver(), approver);
                        addField(AccountingCustomFormConstants.PROJECT_MANAGER, approverBox);
                    }
                    if (!result.isApproverSaved()) {
                        approver.reloadApproverWidgets(RelationItem.TYPE_REQUEST_FOR_QUOTE, null);
                    }

                    if (objectID != null) {
                        if (Constants.RFQ_DRAFT.equals(result.getStatusCode())) {
                            saveAsDraft.setVisible(true);
                        }
                    } else {
                        saveAsDraft.setVisible(true);
                    }
                } else {
                    if (Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
                        addField(AccountingCustomFormConstants.PROJECT_MANAGER, new FormGroup(Property.get(Constants.PROJECT, wfmStrings.project()), projectLookUp));
                    }
                    if (!RFQ_APPROVED.equals(result.getStatusCode())) {
                        saveAsDraft.setVisible(true);
                    }
                    approveButtons.setVisible(true);
                    submitButton.setVisible(false);
                }
                termsAndDuePanel.setTermsAndDueProvider(new TermsAndDueProvider() {
                    @Override
                    public void setDueDateAndTermsLabel(String text) {

                    }

                    @Override
                    public Date getInvoiceDate() {
                        return dateTime.getDateTime();
                    }

                    @Override
                    public void applyPaymentInstructionData() {

                    }

                    @Override
                    public boolean isEditForm() {
                        return objectID != null;
                    }
                });
                if (objectID != null) {
                    if (result.getInvoiceTermsItem() != null) {
                        termsAndDuePanel.setData(TERMS_TYPE, (result.getValidUntil() != null ? result.getValidUntil().getNonConvertedDate() : null), result.getInvoiceTermsItem());
                    } else {
                        termsAndDuePanel.setData(DUE_TYPE, result.getValidUntil() != null ? result.getValidUntil().getNonConvertedDate() : null, null);
                    }
                } else {
                    if (TERMS_TYPE.equals(rfqData.getDueDateType())) {
                        termsAndDuePanel.setData(TERMS_TYPE, (result.getValidUntil() != null ? result.getValidUntil().getNonConvertedDate() : null), result.getInvoiceTermsItem());
                    } else {
                        termsAndDuePanel.setData(DUE_TYPE, DateUtil.addDays(currentDate, 30), null);
                    }
                }
                LoadingPanel.loading(false);
            }
        });
    }

    private void initItemTable(RFQData rfqData) {

        columnConfigs = getColumns(rfqData);
        itemsTable = new EditableTable(columnConfigs, true, true);
        itemsTable.setDraggable(true);
        itemsTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                itemsTable.addRow(getWidgets(null));
            }

            @Override
            public void removeRow() {

            }
        });
        grid = itemsTable.getGrid();

        if (objectID == null) {
            getAndApplyIntroduction(null);
            for (int i = 0; i < DEFAULT_ITEM_ROWS; i++) {
                itemsTable.addRow(getWidgets(null));
            }
        }
        addField(AccountingCustomFormConstants.ITEMS_TABLE, itemsTable, "");
    }

    private ActivityQuickAddForm addActivity(RFQData result, int callLog) {
        if (result.getCustomer() != null) {
            return new ActivityQuickAddForm(callLog, RelationItem.newEventRelation(TYPE_REQUEST_FOR_QUOTE, objectID, result.getNumberData().getNumberString()), RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, result.getCustomer().getId(), result.getCustomer().getName()));
        } else {
            return new ActivityQuickAddForm(callLog, RelationItem.newEventRelation(TYPE_REQUEST_FOR_QUOTE, objectID, result.getNumberData().getNumberString()));
        }
    }

    private QuoteAdditionalFields createAdvancedOptions() {
        return new QuoteAdditionalFields(() -> {
            List<Widget> result = new ArrayList<>();
            return result;
        });
    }

    @Override
    protected void addButtons() {

    }

    @Override
    protected String getFormID() {
        return LayoutRPC.REQUEST_FOR_QUOTE_FORM;
    }

    @Override
    protected String getFormType() {
        return objectID == null ? LayoutRPC.ADD : LayoutRPC.EDIT;
    }

    private void setRowData(RFQItem object, Integer position) {
        itemsTable.addRow(position, getWidgets(object));
    }

    private void setItemValues(final ProductSelectItem product, Integer position) {
        QuoteService.App.get().getProductPreferredSupplier(product.getId(), new AsyncCallback<RFQItem>() {
            @Override
            public void onFailure(Throwable caught) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onSuccess(final RFQItem result) {
                if (result != null) {
                    if (result.getSuppliers() != null && result.getSuppliers().length > 1) {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.confirmation());
                        messageBox.setMessage(wfmStrings.theProductHasMultipleSuppliers());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                Integer pos = position;
                                for (SelectItem s : result.getSuppliers()) {
                                    RFQItem item = new RFQItem();
                                    item.setProduct(product);
                                    item.setSupplier(s);
                                    if (result.getMeasurement() != null) {
                                        item.setMeasurement(result.getMeasurement());
                                    }

                                    if (pos < itemsTable.getGrid().getRowCount()) {
                                        setRowData(item, pos);
                                        pos++;
                                    } else {
                                        itemsTable.addRow(getWidgets(item));
                                        pos++;
                                    }
                                }
                            }
                        });
                        messageBox.open();
                    } else if (result.getSuppliers() != null && result.getSuppliers().length == 1) {
                        LookUpCell supplierLookUpCell = (LookUpCell) itemsTable.getColumnCellWidgetById(position, ItemTableConstants.SUPPLIER);
                        supplierLookUpCell.getLookUp().addItem(result.getSuppliers()[0]);
                        supplierLookUpCell.InActive();
                        if (result.getMeasurement() != null) {
                            LookUpCell measurementLookUpCell = (LookUpCell) itemsTable.getColumnCellWidgetById(position, ItemTableConstants.MEASUREMENT);
                            measurementLookUpCell.getLookUp().addItem(result.getMeasurement());
                            measurementLookUpCell.InActive();
                        }
                    }
                }
            }
        });
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    private void initFormParameters(String[] params) {
        formParameters = new Params();
        formParameters.setObjectID(objectID);

        if (params != null) {
            if (params.length > 2 && "opportunity".equals(params[1])) {
                formParameters.setCrmFormName(params[1]);
                formParameters.setOpportunityID(Integer.valueOf(params[2]));
                relationID = Integer.valueOf(params[2]);
                relationType = RelationItem.TYPE_OPPORTUNITY;
            } else if (params.length > 2 && "convertFromRFP".equals(params[1])) {
                formParameters.setExternalFormID(AccountingConstants.CONVERT_RFP_TO_RFQ);
                String[] idsArray = params[2].split(",");
                ArrayList<Integer> idList = new ArrayList<>();
                for (String id : idsArray) {
                    idList.add(Integer.parseInt(id));
                }
                formParameters.setExternalObjectIDList(idList);
            } else if (params.length > 2 && "fromCrmAccount".equals(params[1])) {
                formParameters.setCrmFormName(params[1]);
                formParameters.setClientId(Integer.valueOf(params[2]));
                relationID = Integer.valueOf(params[2]);
                relationType = RelationItem.TYPE_CRM_ACCOUNT;
            } else if (params.length > 2 && "supplier".equals(params[1])) {
                formParameters.setCrmFormName(params[1]);
                formParameters.setSupplierId(Integer.valueOf(params[2]));
                relationID = Integer.valueOf(params[2]);
                relationType = RelationItem.TYPE_CRM_ACCOUNT;
            }
        }

    }

    @Override
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

    @Override
    protected List<Widget> getFooterLeftSideWidgets() {
        List<Widget> result = new ArrayList<>();
        introduction = new TextArea();
        introduction.ensureDebugId("introduction-textArea");
        introduction.setStyleName("keepDropDownOpen");
        footerUploadPanel = new FooterUploadPanel(Constants.F_RFQ_1, objectID, true);
        noteHistoryWidget = new NoteHistoryWidget(callback -> QuoteService.App.get().getRFQHistoryNotes(objectID, callback));
        FooterInformer notes = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);
        FooterInformer intro = new FooterInformer(SvgEnum.docTitle, wfmStrings.introduction(), introduction);
        notes.setInitialClasses("informer-item history-notes-container");
        intro.setInitialClasses("informer-item history-notes-container");

        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);

        result.add(notes);
        result.add(intro);
        result.add(footerUploadPanel);
        if (objectID != null) {
            result.add(link);
        }


        return result;
    }

    private boolean validateCustomFields() {
        boolean validate = quoteAdditionalFields.validateCustomFieldRequiredFields();

        //if there are not validate fields in the custom fields
        //then we should open the advanced pop-up

        if (!validate) {
            quoteAdditionalFields.getCustomFieldContainer().setActive(0);

            showAdvancedOptions(wfmStrings.additionalFields(), quoteAdditionalFields);
        }
        return validate;
    }

    @Override
    protected List<Widget> getFooterRightSideWidgets() {
        List<Widget> rightWidgets = new ArrayList<>();
        Div actionsWrapper = new Div();
        actionsWrapper.add(actions);
        rightWidgets.add(actionsWrapper);

        Div pdfWrapper = new Div();
        pdfWrapper.add(printPdfSplitButton);
        rightWidgets.add(pdfWrapper);

        Div draftButtonWrapper = new Div();
        draftButtonWrapper.add(saveAsDraft);
        rightWidgets.add(draftButtonWrapper);

        Div submitButtonWrapper = new Div();
        submitButtonWrapper.add(submitButton);
        rightWidgets.add(submitButtonWrapper);

        Div approveButtonWrapper = new Div();
        approveButtonWrapper.add(approveButtons);
        rightWidgets.add(approveButtonWrapper);

        return rightWidgets;
    }


    private HasLinks linkingUtil;

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(RequestForQuoteView.this) {
                @Override
                protected boolean isActionEditing() {
                    return false;
                }

                @Override
                public Integer getRelationID() {
                    return objectID;
                }

                @Override
                public String getRelationType() {
                    return RelationItem.TYPE_REQUEST_FOR_QUOTE;
                }

                @Override
                public String getRelationName() {
                    return rfqData != null ? rfqData.getNumberData().getNumberString() : null;
                }

                @Override
                public boolean hasNoSummaryTab() {
                    return true;
                }
            };
        }
        return linkingUtil;
    }

    private void pdfOption(RFQData result) {
        if (result.getTemplates() != null && result.getTemplates().length > 0) {
            templates = new DataListBox();
            templates.setItems(result.getTemplates());
            if (result.getSelectedTemplateId() != null) {
                templates.setSelected(result.getSelectedTemplateId());
            }
        }

        List<SplitButtonItem> pdfTemplatesList = new ArrayList<>();
        Integer defaultTemplateId = null;
        HTML dateLabel = new HTML(DateUtils.getDateAndTimeFormatShort2(result.getDate().getNonConvertedDate()));
        HTML numberLabel = new HTML(result.getNumberData().getNumberString());

        if (templates != null && templates.getItems() != null) {
            templates.getItems();
            for (SelectItem pdfItem : templates.getItems()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(panel, pdfItem.getId(), numberLabel.getText(), dateLabel.getText())));
            }
        }
        Integer finalDefaultTemplateId = defaultTemplateId;

        SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> generatePDF(panel, finalDefaultTemplateId, numberLabel.getText(), dateLabel.getText()), true);
        pdfVersion.ensureDebugId("rfq_" + "pdfVersionItem");
        pdfTemplatesList.add(pdfVersion);
        if (Utils.hasRole(ADMIN)) {
            pdfTemplatesList.add(new SplitButtonItem("PDF_CUSTOMIZATION", wfmStrings.customize(), () -> Utils.openURL(GWT.getHostPageBaseURL() + "Settings.html#pdftemplate|summary/null/" + PdfTemplateTypeEnum.RFQ.name())));
        }
        printPdfSplitButton.addItemList(pdfTemplatesList);
        printPdfSplitButton.setVisible(true);
    }

    private void generatePDF(HTMLPanel panel, Integer templateID, String number, String date) {
        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(objectID);
        HashMap<String, String> parameters = requestObject.getRequestParams();
        parameters.put("number", number);
        parameters.put("date", date);
        if (templateID != null) {
            parameters.put("templateID", String.valueOf(templateID));
        }
        String pdfURL = CommandConstants.PDF_URL + "/requestForQuotePDFHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }

    private void getRelationName(final Integer relationID, final String relType) {
        AllInOneService.App.get().getRelationName(relationID, relType, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    relationName = result;
                }
            }
        });
    }

    @Override
    public String getPropertyCode() {
        return Constants.REQUEST_FOR_QUOTE;
    }
}
