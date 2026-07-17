package com.edatasite.workforce.gwt.invoice.client.ui.view.rfq;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.FooteredCustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.PdfTemplateTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateTimeWidget;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LinkableCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.widget.LinkCellWidget;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
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
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.upload.GeneralAttachmentLinksComponent;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceFormFields;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.Params;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQData;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.edatasite.workforce.gwt.invoice.client.ui.view.ContactPopup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.InvoiceCustomFieldsSummaryView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.QuoteAdditionalFields;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;
import org.gwt.advanced.client.ui.widget.EditableGrid;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.edatasite.workforce.gwt.core.client.rpc.RelationItem.TYPE_REQUEST_FOR_QUOTE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.APPROVE_AND_SEND;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.BTN_PRIMARY;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DATA_TYPE_DATE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.MANAGER_REJECT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.RECEIVABLE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.RFQ_APPROVED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.RFQ_DECLINED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.RFQ_DRAFT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.RFQ_SUBMITTED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_DATEPICKER_TIME;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_MULTI_LOOKUP;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_PERCENTAGE;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/9/12
 * Time: 5:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class RequestForQuoteSummaryView extends FooteredCustomForm implements Colapse, PermissionConstants, FittedContent, HasLinksInterface {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    private final Integer objectID;
    private RFQData rfqData;
    private DataListBox templates;
    private QuoteAdditionalFields quoteAdditionalFields;
    private MaterialLink showMoreLink;
    private FooterUploadPanel footerUploadPanel;
    private FooterInformer link;
    private FooterInformer footerInstruction;
    private NoteHistoryWidget noteHistoryWidget;
    private SplitButton printPdfSplitButton, actions;
    //    private WfmButton2 sendQuoteButton;
    private WfmButton2 pdfButton;
    private WfmButton2 editButton;
    private WfmButton2 convertToPOButton;
    private SplitButton approveButtons;
    private WfmButton2 submitButton;
    private MaterialLink customerBalanceLink;
    private TextArea2 instruction;
    private ColumnConfig[] columnConfigs;

    public RequestForQuoteSummaryView(Integer objectID) {
        super("summary");
        setDescription(property.getSingular(wfmStrings.requestForQuote()));
        this.objectID = objectID;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        Params params = new Params();
        params.setView(true);
        QuoteService.App.get().getRFQData(objectID, null, new AsyncCallback<RFQData>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(RFQData result) {
                initialize(result);
                LoadingPanel.loading(false);
            }
        });

        return null;
    }

    @Override
    protected String getWikiCode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void initialize(RFQData result) {
        this.rfqData = result;

        boolean isManager = rfqData.getCurrentApprover() != null && rfqData.getCurrentApprover().getExactEmployee().getId().equals(Utils.getUserID());
        setLabelEndParameter("");
        if (result.getLinkedEmails() != null && !result.getLinkedEmails().getList().isEmpty()) {
            MainLayout.get().getNavToolBar().drawEmails(result.getLinkedEmails());
        }
        HTML customer = new HTML(result.getCustomer() != null ? result.getCustomer().getName() : Constants.PA_NOT_AVAILABLE_STRING);
        final HTML requestFromLabel = new HTML(Constants.COMPANY_SUPPLIERS.equals(result.getRequestFrom()) ? accountingStrings.companySuppliers() : accountingStrings.directorySuppliers());
        DateTimeWidget dateTime = new DateTimeWidget(30);
        dateTime.getTime().setPaddingLeft(6);
        dateTime.setDateTime(result.getDate().getNonConvertedDate());
        dateTime.setEnabled(false);
        final HTML dateLabel = new HTML(DateUtils.getDateAndTimeFormatShort2(result.getDate().getNonConvertedDate()));
        HTML validUntilLabel = new HTML(result.getInvoiceTermsItem() != null ? result.getInvoiceTermsItem().getName() : DateUtils.format(result.getValidUntil().getNonConvertedDate()));
        final HTML numberLabel = new HTML(result.getNumberData().getNumberString());
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


        customerBalanceLink = new MaterialLink(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        customerBalanceLink.setHref("javaScript:void(0)");
        customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "none");
        if (result.getCustomer() != null) {
            InvoiceService.App.get().getClientOrSupplier(result.getCustomer().getId(), RECEIVABLE, new AsyncCallback<TypeItem>() {
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
                    customerBalanceLink.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("customerBalance|customerBalance/" + result.getCustomer().getId() + "/" + CrmAccountItem.CUSTOMER,
                            wfmStrings.balance() + ": " + typeItem.getName(), typeItem.getName()));
                }
            });
        }

        TextArea introduction = new TextArea();
        introduction.setEnabled(false);
        introduction.setText(result.getIntroduction());

        if (result.getInstructions() != null && result.getInstructions().length > 0) {
            footerInstruction.setVisible(true);
            instruction.getTextArea().setText(result.getInstructions()[0].getName());
        }

        columnConfigs = getColumns(result);
        final EditableTable itemsTable = new EditableTable(columnConfigs, false);
        itemsTable.setDraggable(false);
        for (RFQItem item : result.getItems()) {
            itemsTable.addRow(getWidgets(item, result));
        }
        quoteAdditionalFields = new QuoteAdditionalFields(ArrayList::new);
        quoteAdditionalFields.addToMailAddressBodyContainer(getWidgetAsFormControl(result.getAddressAsString(false)));
        quoteAdditionalFields.getOptionsContainer().removeFromParent();
        showMoreLink = new MaterialLink(wfmStrings.showAdditionalFields());
        showMoreLink.addStyleName("btn-flat RequestForQuoteSummaryView");
        showMoreLink.addClickHandler(ch -> showAdvancedOptions(wfmStrings.additionalFields(), quoteAdditionalFields));

        //Beginning of PDF
        List<SplitButtonItem> pdfTemplatesList = new ArrayList<>();
        Integer defaultTemplateId = null;
        if (result != null && result.getTemplates() != null) {
            result.getTemplates();
            for (SelectItem pdfItem : result.getTemplates()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(panel, pdfItem.getId(), numberLabel.getText(), dateLabel.getText())));
            }
        }
        Integer finalDefaultTemplateId = defaultTemplateId;

        if (Utils.isCRM() ? Utils.hasPermission(CRM_REQUEST_FOR_QUOTE_PDF) : Utils.hasPermission(ACCOUNTING_REQUEST_FOR_QUOTE_SUMMARY)) {
            SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> generatePDF(panel, finalDefaultTemplateId, numberLabel.getText(), dateLabel.getText()), true);
            pdfVersion.ensureDebugId("rfq_" + "pdfVersionItem");
            pdfTemplatesList.add(pdfVersion);
        }
        if (Utils.hasRole(Constants.ADMIN)) {
            pdfTemplatesList.add(new SplitButtonItem("PDF_CUSTOMIZATION", wfmStrings.customize(), () -> Utils.openURL(GWT.getHostPageBaseURL() + "Settings.html#pdftemplate|summary/null/" + PdfTemplateTypeEnum.RFQ.name())));
        }
        printPdfSplitButton.addItemList(pdfTemplatesList);
        printPdfSplitButton.setVisible(true);

        //Ending of PDF


//            templates = new DataListBox();
//            templates.setItems(result.getTemplates());
//            if (result.getSelectedTemplateId() != null) {
//                templates.setSelected(result.getSelectedTemplateId());
//            }
//            GBoxItem templateField = new GBoxItem(templates, getCustomTitle(wfmStrings.pdfTemplates()));
//            templateField.setStyleWidthFree(true);
//            templateField.setWidthLinear("200px");
//            addField(CUSTOM_HTML_TEMPLATE, templateField);

//        if (result.isSupplier() && Utils.hasPermission(SEND_RFQ_QUOTE_NOTE)) {
//            if (!RFQ_SUBMITTED.equals(result.getStatusCode())
//                || !RFQ_DECLINED.equals(result.getStatusCode())) {
//                sendQuoteButton.setVisible(true);
//                sendQuoteButton.addClickHandler(event -> {
//
//                    List<RFQSupplierBid> bidsList = new LinkedList<>();
//
//                    EditableGrid grid = itemsTable.getGrid();
//                    for (int i = 0; i < grid.getRowCount(); i++) {
//                        CustomCellLabel productLabel = (CustomCellLabel) itemsTable.getColumnById(i, ItemTableConstants.PRODUCT);
//                        CustomCellTextBox bidTxtBox = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.UNITPRICE);
//                        DataListBox supplierListBox = (DataListBox) itemsTable.getColumnById(i, ItemTableConstants.SUPPLIER);
//                        if (!"".equals(bidTxtBox.getText().trim())) {
//                            RFQSupplierBid bid = new RFQSupplierBid();
//                            bid.setRfqItemID(productLabel.getRfqItemID());
//                            if (supplierListBox.getSelectedId() != null) {
//                                bid.setSupplier(supplierListBox.getSelectedItem());
//                            }
//                            bid.setAmount(AccountingUtils.get().parseToBigDecimal(bidTxtBox.getText()));
//                            bidsList.add(bid);
//                        }
//                    }
//
//                    if (bidsList.size() == 0) {
//                        WfmWindow.alert(accountingMessages.pleaseEnterAmountToSend());
//                        return;
//                    }
//
//                    QuoteService.App.get().saveRFQSupplierBids(bidsList.toArray(new RFQSupplierBid[]{}), new AsyncCallback<Void>() {
//                        @Override
//                        public void onFailure(Throwable caught) {
//                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
//                        }
//
//                        @Override
//                        public void onSuccess(Void result12) {
//                            Info.show(accountingMessages.quoteSentSuccessfully(), Info.Type.INFO);
//                            closeTab();
//                        }
//                    });
//                });
//            }
//        }

        if (RFQ_DRAFT.equals(result.getStatusCode())) {
            submitButton.setVisible(true);
            submitButton.addClickHandler(event -> {
                updateStatus(RFQ_SUBMITTED);
            });
        }
        List<SplitButtonItem> splitButtonItems = new ArrayList<>();
        if (result.getCurrentApprover() != null && RFQ_SUBMITTED.equals(result.getStatusCode()) && (isManager || Utils.hasPermission(RFQ_APPROVE))) {


            SplitButtonItem approveButton = new SplitButtonItem(RFQ_APPROVED, wfmStrings.approve(), () -> updateStatus(RFQ_APPROVED));
            approveButton.ensureDebugId("approve-button");
            splitButtonItems.add(approveButton);

            SplitButtonItem rejectButtonItem = new SplitButtonItem(MANAGER_REJECT, wfmStrings.reject(), () -> updateStatus(RFQ_DECLINED));
            rejectButtonItem.ensureDebugId("rejectButtonItem_Rfq");
            splitButtonItems.add(rejectButtonItem);

            if (Utils.hasPermission(PermissionConstants.RFQ_SEND_EMAIL)) {
                SplitButtonItem emailButtonItem = new SplitButtonItem(APPROVE_AND_SEND, wfmStrings.sendEmail(), () -> sendEmail(result));
                emailButtonItem.ensureDebugId("emailButtonItem_Rfq");
                splitButtonItems.add(emailButtonItem);
            }

            approveButtons.addItemList(splitButtonItems);
            approveButtons.setVisible(true);

        } else if (!result.isApprover() && Utils.hasPermission(PermissionConstants.RFQ_SEND_EMAIL)) {

            SplitButtonItem emailButtonItem = new SplitButtonItem(APPROVE_AND_SEND, wfmStrings.sendEmail(), () -> sendEmail(result));
            emailButtonItem.ensureDebugId("emailButtonItem_Rfq");
            splitButtonItems.add(emailButtonItem);
            approveButtons.setVisible(true);
        }

        if ((Utils.isCRM() ? Utils.hasPermission(CRM_REQUEST_FOR_QUOTE_EDIT) : Utils.hasPermission(ACCOUNTING_REQUEST_FOR_QUOTE_EDIT)) && result.isEditable()) {
            editButton.setVisible(true);
            editButton.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("requestforquote|edit/" + objectID.toString()));
        }

        if (Utils.hasPermission(ACCOUNTING_REQUEST_FOR_QUOTE_CONVERT) && result.isNotConvertedSupplierBidExists()) {
            if (!RFQ_SUBMITTED.equals(result.getStatusCode())
                    || !RFQ_DECLINED.equals(result.getStatusCode())) {
                convertToPOButton.setVisible(true);
                convertToPOButton.addClickHandler(event -> {
                    RFQData rfqData = new RFQData();
                    rfqData.setObjectID(objectID);
                    rfqData.setStatusCode(Constants.CONVERTED);

                    ArrayList<RFQItem> rfqItemsList = new ArrayList<>();
                    EditableGrid grid = itemsTable.getGrid();
                    for (int i = 0; i < grid.getRowCount(); i++) {
                        CustomCellLabel productLabel = (CustomCellLabel) itemsTable.getColumnById(i, ItemTableConstants.PRODUCT);
                        CustomCellTextBox unitCostLabel = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.UNITPRICE);
                        DataListBox supplierListBox = (DataListBox) itemsTable.getColumnById(i, ItemTableConstants.SUPPLIER);
                        ExtendedDataListBox convert = (ExtendedDataListBox) itemsTable.getColumnById(i, "convert");
                        BigDecimal bidAmount = AccountingUtils.get().parseToBigDecimal(unitCostLabel.getText());
                        if (supplierListBox.getSelectedId() != null && bidAmount.compareTo(BigDecimal.ZERO) > 0 && convert.isConvertToPo()) {
                            RFQItem itemsToConvert = new RFQItem();
                            itemsToConvert.setObjectID(productLabel.getRfqItemID());
                            itemsToConvert.setSupplier(supplierListBox.getSelectedItem());
                            itemsToConvert.setUnitCost(bidAmount);
                            rfqItemsList.add(itemsToConvert);
                        } else if (!convert.isConverted()) {
                            rfqData.setStatusCode(Constants.PARTIAL_CONVERTED);
                        }
                    }
                    rfqData.setItems(rfqItemsList);

                    if (rfqItemsList.size() == 0) {
                        WfmWindow.alert(accountingMessages.thereIsNoBidToConvert());
                        return;
                    }

                    QuoteService.App.get().convertRFQToPurchaseOrder(rfqData, new AsyncCallback<Integer>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void onSuccess(Integer result1) {
                            Info.show(property.getSingular(accountingMessages.requestForQuoteSuccessfullyConverted(), wfmStrings.requestForQuote()), Info.Type.INFO);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_REQUEST_FOR_QUOTE_CONVERTED, result1, RequestForQuoteSummaryView.this);
                            closeTab();
                        }
                    });
                });
            }
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

        addTitleField(AccountingCustomFormConstants.TITLE, getMainTitle(property.getSingular(wfmStrings.requestForQuote())));

        FormGroup customerField;
        if (result.getCustomer() != null) {
            customerField = new FormGroup(wrapWidgetToFormControl(customer));
            customerField.ensureDebugId(InvoiceFormFields.CUSTOMER);

            Div clientFieldLabel = customerField.getGroupLabel();
            clientFieldLabel.addStyleName("label-group");
            clientFieldLabel.add(new Span(Property.get(Constants.CLIENT_LIST, wfmStrings.customer())));


            Span balance = new Span(wfmStrings.balance() + ": ");
            balance.add(customerBalanceLink);
            clientFieldLabel.add(balance);
        } else {
            customerField = new FormGroup(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), wrapWidgetToFormControl(customer));
        }
        addField(AccountingCustomFormConstants.CUSTOMER, customerField);
        addField(AccountingCustomFormConstants.REQUEST_FROM, new FormGroup(accountingStrings.requestFrom(), wrapWidgetToFormControl(requestFromLabel)));

        addField(AccountingCustomFormConstants.DATE, new FormGroup(property.getSingular(wfmStrings.requestForQuote()), dateTime));
        addField(AccountingCustomFormConstants.DUE_DATE, new FormGroup(result.getInvoiceTermsItem() != null ? wfmStrings.terms() : accountingStrings.validDate(), wrapWidgetToFormControl(validUntilLabel)));
        FormGroup numberField = new FormGroup(property.getShortForNumber(wfmStrings.requestForQuote()), wrapWidgetToFormControl(numberLabel));
        addField(AccountingCustomFormConstants.NUMBER, numberField);
        addField(AccountingCustomFormConstants.SQ_NUMBER, new FormGroup(accountingStrings.sq(), getWidgetAsFormControl(result.getSqNumber())));
//        addressField.setStyleWidthFree(true);
//        addressField.setStyleSplitRight(true);
        addField(AccountingCustomFormConstants.INTRODUCTION, new FormGroup(wfmStrings.introduction(), wrapWidgetToFormControl(introduction)));
        addField(AccountingCustomFormConstants.ITEMS_TABLE, itemsTable, "");
        FormGroup showMoreField = new FormGroup(showMoreLink);
        showMoreField.setLabel("&nbsp;");
        addField(AccountingCustomFormConstants.INPUT_SHOW_MORE, showMoreField);

        if (result.isApprover()) {
            if (Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
                GColumn projectCol = new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.project(), getWidgetAsFormControl(result.getProject() != null ? result.getProject().getName() : "N/A")));
                GColumn approverCol = new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.approver(), getWidgetAsFormControl(result.getApprover() != null ? result.getApprover().getName() : "N/A")));
                addField(AccountingCustomFormConstants.PROJECT_MANAGER, new GRow(projectCol, approverCol));
            } else {
                Widget approver = getWidgetAsFormControl(result.getApprover() != null ? result.getApprover().getName() : "N/A");
                FormGroup approverBox = new FormGroup(wfmStrings.approver(), approver);
                addField(AccountingCustomFormConstants.PROJECT_MANAGER, approverBox);
            }
        } else {
            if (Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
                addField(AccountingCustomFormConstants.PROJECT_MANAGER, new FormGroup(wfmStrings.project(), getWidgetAsFormControl(result.getProject() != null ? result.getProject().getName() : "N/A")));
            }
        }

        if (result.getCustomFieldList() != null && result.getCustomFieldList().size() > 0) {
            List<CompanyCustomFieldItem> customFieldFirstItem = new ArrayList<>();
            List<CompanyCustomFieldItem> customFieldItems = new ArrayList<>();

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


                initCustomFields(customFieldItems);
            }

            InvoiceCustomFieldsSummaryView customFieldsView = new InvoiceCustomFieldsSummaryView(customFieldFirstItem);
            addField(AccountingCustomFormConstants.CUSTOM_FIELD_ITEM, customFieldsView.getCustomsDataView());
        }
        show();
    }

    private void updateStatus(String statusCode) {
        LoadingPanel.loading(true);
        QuoteService.App.get().updateRFQStatus(objectID, statusCode, new AbstractAsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false);
                if (RFQ_APPROVED.equals(statusCode)) {
                    Info.show(accountingMessages.approved(property.getSingular(accountingStrings.rfqWasSuccessfuly(), wfmStrings.requestForQuote())), Info.Type.INFO);
                } else if (RFQ_DECLINED.equals(statusCode)) {
                    Info.show(accountingMessages.declined(property.getSingular(accountingStrings.rfqWasSuccessfuly(), wfmStrings.requestForQuote())), Info.Type.INFO);
                } else if (RFQ_SUBMITTED.equals(statusCode)) {
                    Info.show(accountingMessages.submitted(property.getSingular(accountingStrings.rfqWasSuccessfuly(), wfmStrings.requestForQuote())), Info.Type.INFO);
                }
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_REQUEST_FOR_QUOTE_ADDED, result, RequestForQuoteSummaryView.this);
                closeTab();
            }
        });
    }

    public void sendEmail(RFQData result) {
        if (result != null) {
            ContactPopup contactPopup = new ContactPopup(null, result.getObjectID());
            contactPopup.open();
        }
    }

    private ActivityQuickAddForm addActivity(RFQData result, int callLog) {
        if (result.getCustomer() != null) {
            return new ActivityQuickAddForm(callLog, RelationItem.newEventRelation(TYPE_REQUEST_FOR_QUOTE, objectID, result.getNumberData().getNumberString()), RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, result.getCustomer().getId(), result.getCustomer().getName()));
        } else {
            return new ActivityQuickAddForm(callLog, RelationItem.newEventRelation(TYPE_REQUEST_FOR_QUOTE, objectID, result.getNumberData().getNumberString()));
        }
    }

    private void initCustomFields(List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() > 0) {
            MaterialPanel customFieldsWrapper = new InvoiceCustomFieldsSummaryView(customFieldItems).getCustomsDataView();
            quoteAdditionalFields.initCustomFieldSummaryWidget(customFieldsWrapper);
        }
    }

    private ColumnConfig[] getColumns(RFQData result) {
        int index = 0;
        boolean bidExists = result.isNotConvertedSupplierBidExists();
        ColumnConfig[] columns;
        if (result.getCustomItemColumns() != null && result.getCustomItemColumns().length > 0) {
            columns = new ColumnConfig[bidExists ? result.getCustomItemColumns().length + 1 : result.getCustomItemColumns().length];
            for (ColumnConfigs column : result.getCustomItemColumns()) {

                boolean isPixel = (column.getWidth() == null || column.getWidth() == 0);
                ColumnConfig columnConfig;

                switch (column.getCode()) {
                    case ItemTableConstants.PRODUCT:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.PRODUCT, column.isChanged() ? column.getTitle() : wfmStrings.item(), Utils.getColumnWidth(column.getWidth(), 200), true);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.DESCRIPTION:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.DESCRIPTION, column.isChanged() ? column.getTitle() : wfmStrings.description(), Utils.getColumnWidth(column.getWidth(), 250), false);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.QTY:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.QTY, column.isChanged() ? column.getTitle() : wfmStrings.qty(), Utils.getColumnWidth(column.getWidth(), 75), true, Constants.RIGHT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.MEASUREMENT:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.MEASUREMENT, column.isChanged() ? column.getTitle() : wfmStrings.measurement(), Utils.getColumnWidth(column.getWidth(), 75), false);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.UNITPRICE:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.UNITPRICE, column.isChanged() ? column.getTitle() : wfmStrings.cost(), Utils.getColumnWidth(column.getWidth(), 75), true, Constants.RIGHT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.COMISSION:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.COMISSION, column.isChanged() ? column.getTitle() : wfmStrings.commission() + " (%)", Utils.getColumnWidth(column.getWidth(), 75), false, Constants.RIGHT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.RECEIPTS:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.RECEIPTS, column.isChanged() ? column.getTitle() : accountingStrings.receipts(), Utils.getColumnWidth(column.getWidth(), 80), false, Constants.LEFT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.SUPPLIER:
                        columnConfig = new ColumnConfig(LinkableCell.class, ItemTableConstants.SUPPLIER, column.isChanged() ? column.getTitle() : Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), Utils.getColumnWidth(column.getWidth(), 175), true);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.REMARK:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.REMARK, column.isChanged() ? column.getTitle() : wfmStrings.remarks(), Utils.getColumnWidth(column.getWidth(), 150), false);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    default:
                        columnConfig = new ColumnConfig(CustomCell.class, column.getCode(), column.getTitle(), Utils.getColumnWidth(column.getWidth(), 100), column.isRequired(), true);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                }
            }
            if (bidExists) {
                columns[index] = new ColumnConfig(CustomCell.class, "convert", accountingStrings.convertToPO(), 120, false);
            }
        } else {
            columns = new ColumnConfig[bidExists ? 10 : 9];

            columns[index++] = new ColumnConfig(CustomCell.class, ItemTableConstants.PRODUCT, wfmStrings.item(), 250, true, "left-align-Cell");
            columns[index++] = new ColumnConfig(CustomCell.class, ItemTableConstants.DESCRIPTION, wfmStrings.description(), 200, false);
            columns[index++] = new ColumnConfig(CustomCell.class, ItemTableConstants.RECEIPTS, accountingStrings.receipts(), 80, false);
            columns[index++] = new ColumnConfig(CustomCell.class, ItemTableConstants.QTY, wfmStrings.qty(), 75, true);
            columns[index++] = new ColumnConfig(CustomCell.class, ItemTableConstants.COMISSION, wfmStrings.commission() + "%", 75, false);
            columns[index++] = new ColumnConfig(CustomCell.class, ItemTableConstants.MEASUREMENT, wfmStrings.measurement(), 50, false);
            columns[index++] = new ColumnConfig(CustomCell.class, ItemTableConstants.UNITPRICE, wfmStrings.cost(), 75, true);
            columns[index++] = new ColumnConfig(LinkableCell.class, ItemTableConstants.SUPPLIER, Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), 200, true, "left-align-Cell");
            columns[index++] = new ColumnConfig(CustomCell.class, ItemTableConstants.REMARK, wfmStrings.remarks(), 150, false);
            if (bidExists) {
                columns[index] = new ColumnConfig(CustomCell.class, "convert", accountingStrings.convertToPO(), 120, false);
            }
        }
        return columns;
    }

    private Widget[] getWidgets(final RFQItem rfqItem, RFQData rfqData) {
        Widget[] rowWidgets = new Widget[columnConfigs.length];
        int index = 0;
        final CustomCellTextBox billAmountTxtBox = new CustomCellTextBox();
        billAmountTxtBox.setWidth("170px");
        Validation.addNumericKeyboardListener(billAmountTxtBox, 2);
        billAmountTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        billAmountTxtBox.setText(AccountingUtils.getZero());
        billAmountTxtBox.setEnabled(rfqData.isSupplier() && rfqData.isEditable());
        if (rfqItem.getUnitCost() != null) {
            billAmountTxtBox.setText(AccountingUtils.get().formatPrice(rfqItem.getUnitCost()));
        }
        billAmountTxtBox.addChangeHandler(changeEvent -> {
            RFQItem item = new RFQItem();
            item.setObjectID(rfqItem.getObjectID());
            item.setUnitCost(new BigDecimal(billAmountTxtBox.getText()));
            onRFQItemChanged(item);
        });
        for (ColumnConfig config : columnConfigs) {
            switch (config.getName()) {
                case ItemTableConstants.PRODUCT:
                    rowWidgets[index++] = new CustomCellLabel(((rfqItem.getProduct() != null && rfqItem.getProduct().getName() != null) ? rfqItem.getProduct().getName() : ""), rfqItem.getObjectID());
                    break;
                case ItemTableConstants.DESCRIPTION:
                    rowWidgets[index++] = new CustomCellLabel(rfqItem.getDescription() != null ? rfqItem.getDescription() : "");
                    break;
                case ItemTableConstants.QTY:
                    rowWidgets[index++] = new CustomCellLabel(AccountingUtils.get().formatQty(rfqItem.getQty()));
                    break;
                case ItemTableConstants.MEASUREMENT:
                    rowWidgets[index++] = new CustomCellLabel((rfqItem.getMeasurement() != null && rfqItem.getMeasurement().getName() != null) ? rfqItem.getMeasurement().getName() : "");
                    break;
                case ItemTableConstants.UNITPRICE:
                    rowWidgets[index++] = billAmountTxtBox;
                    break;
                case ItemTableConstants.COMISSION:
                    final CustomCellTextBox commissionLabel = new CustomCellTextBox();
                    commissionLabel.setText((rfqItem.getCommission() != null) ? AccountingUtils.get().formatQty(rfqItem.getCommission()) : "");
                    Validation.addNumericKeyboardListener(commissionLabel, 2);
                    commissionLabel.addChangeHandler(changeEvent -> {
                        RFQItem item = new RFQItem();
                        item.setObjectID(rfqItem.getObjectID());
                        item.setCommission(new BigDecimal(commissionLabel.getText()));
                        onRFQItemChanged(item);
                    });

                    rowWidgets[index++] = commissionLabel;
                    break;
                case ItemTableConstants.RECEIPTS:
                    CustomCellLabel attachmentLabel;
                    final FileResource[] attachments = rfqItem.getAttachments();
                    if (attachments != null && attachments.length > 0) {
                        CustomCellLabel viewLink = new CustomCellLabel(wfmStrings.summaryView());
                        viewLink.addClickHandler(clickEvent -> {
                            final KpiModal dialogBox = new KpiModal();
                            dialogBox.setWidth("740px");
                            GeneralAttachmentLinksComponent attachmentsPanel = new GeneralAttachmentLinksComponent(attachments, true, false);
                            attachmentsPanel.getDataGrid().addStyleName("cellBasedWidget-mod cellBasedWidget-attachment cellBasedWidget-mod--static-body box-radius--top");
                            dialogBox.add(attachmentsPanel);
                            dialogBox.open();
                        });
                        attachmentLabel = viewLink;
                    } else {
                        attachmentLabel = new CustomCellLabel("-");
                    }

                    rowWidgets[index++] = attachmentLabel;
                    break;
                case ItemTableConstants.SUPPLIER:
                    final LinkCellWidget supplierLink = new LinkCellWidget("", null);
                    if (rfqItem.getSupplier() != null) {
                        supplierLink.setText(rfqItem.getSupplier().getName());
                        supplierLink.setClickHandler(() -> {
                            ContactPopup contactPopup = new ContactPopup(rfqItem.getSupplier().getId(), rfqData.getObjectID());
                            contactPopup.open();
                        });
                    }

                    rowWidgets[index++] = supplierLink;
//                    final DataListBox suppliersListBox = new DataListBox();
//                    suppliersListBox.setAllowFirstItem(true);
//                    suppliersListBox.addValueChangeHandler(event -> {
//                        BigDecimal bidAmount = null;
//                        Integer selectedSupplier = suppliersListBox.getSelectedId();
//                        if (selectedSupplier != null) {
//                            RFQSupplierBid[] bids = rfqItem.getSupplierBids();
//                            for (RFQSupplierBid bid : bids) {
//                                if (bid.getSupplier().getId().equals(selectedSupplier)) {
//                                    bidAmount = bid.getAmount();
//                                }
//                            }
//                        }
//                        billAmountTxtBox.setText(AccountingUtils.get().formatPrice(bidAmount != null ? bidAmount : BigDecimal.ZERO));
//                    });
//                    if (rfqItem.getSupplier() != null) {
//                        suppliersListBox.setItems(new SelectItem[]{rfqItem.getSupplier()});
//                        suppliersListBox.setSelected(rfqItem.getSupplier().getId());
//                    }
//
//                    rowWidgets[index++] = suppliersListBox;
                    break;
                case ItemTableConstants.REMARK:
                    final CustomCellTextBox remarkLabel = new CustomCellTextBox();
                    remarkLabel.setText(rfqItem.getReMarks() != null ? rfqItem.getReMarks() : "");

                    remarkLabel.addChangeHandler(changeEvent -> {
                        RFQItem item = new RFQItem();
                        item.setObjectID(rfqItem.getObjectID());
                        item.setReMarks(remarkLabel.getText());
                        onRFQItemChanged(item);
                    });

                    rowWidgets[index++] = remarkLabel;
                    break;
                case "convert":
                    rowWidgets[index++] = new ExtendedDataListBox(rfqItem.isConverted());
                    break;
                default:
                    CompanyCustomFieldItem customFieldItem = rfqItem.getCustomFieldByCode(config.getName());
                    Label label = new Label();
                    if (customFieldItem != null) {
                        if (DATA_TYPE_DATE.equals(customFieldItem.getDataType())) {
                            if (UI_TYPE_DATEPICKER_TIME.equals(customFieldItem.getUiType())) {
                                label.setText(customFieldItem.getFieldDateNonConvertedValue() != null ? DateUtils.dateAndTimeFormatShort2(customFieldItem.getFieldDateNonConvertedValue()) : "");
                            } else {
                                label.setText(customFieldItem.getFieldDateNonConvertedValue() != null ? DateUtils.format(customFieldItem.getFieldDateNonConvertedValue()) : "");
                            }
                        } else if (UI_TYPE_PERCENTAGE.equals(customFieldItem.getUiType())) {
                            label.setText(customFieldItem.getFieldStringValue() != null ? customFieldItem.getFieldStringValue() + " %" : "");
                        } else if (UI_TYPE_MULTI_LOOKUP.equals(customFieldItem.getUiType())) {
                            String finalValue = "";
                            if (customFieldItem.getSelectItems() != null && customFieldItem.getSelectItems().size() > 0) {
                                for (SelectItem selectItem : customFieldItem.getSelectItems()) {
                                    finalValue += selectItem.getName() + "; ";
                                }
                            }
                            label.setText(finalValue);
                        } else {
                            label.setText(customFieldItem.getFieldStringValue() != null ? customFieldItem.getFieldStringValue() : "");
                        }
                    } else {
                        label.setText("");
                    }

                    rowWidgets[index++] = label;
                    break;
            }
        }

        return rowWidgets;
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

    private void onRFQItemChanged(RFQItem rfqItem) {
        LoadingPanel.loading(true);
        QuoteService.App.get().updateRFQItem(rfqItem, new AsyncCallback<TestRPC>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(TestRPC result) {
                Info.show(result.getMessage());
                LoadingPanel.loading(false);
            }
        });
    }

    @Override
    protected List<Widget> getFooterLeftSideWidgets() {
        List<Widget> result = new ArrayList<>();
        noteHistoryWidget = new NoteHistoryWidget(callback -> QuoteService.App.get().getRFQHistoryNotes(objectID, callback));
        noteHistoryWidget.setSaveIntoDatabase((historyListItem) -> {
            LoadingPanel.loading(true);
            QuoteService.App.get().saveRFQNotes(historyListItem, objectID, new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(Integer savedObjectId) {
                    historyListItem.setObjectID(savedObjectId);
                    LoadingPanel.loading(false);
                }
            });
        });
        FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);

        informer.setInitialClasses("informer-item history-notes-container");

        footerUploadPanel = new FooterUploadPanel(Constants.F_RFQ_1, objectID, true, wfmStrings.attachments());

        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);


        instruction = new TextArea2();
        instruction.getTextArea().setReadOnly(true);
        instruction.getTextArea().addStyleName(AccountingCustomFormConstants.STYLE_TERMS_INSTRUCTION);

        footerInstruction = new FooterInformer(SvgEnum.docQuestion, accountingStrings.instruction(), instruction.getTextArea());
        footerInstruction.setInitialClasses("informer-item");
        footerInstruction.setVisible(false);


        result.add(informer);
        result.add(footerUploadPanel);
        result.add(link);
        result.add(footerInstruction);
        return result;
    }

    @Override
    protected List<Widget> getFooterRightSideWidgets() {
        List<Widget> result = new ArrayList<>();
        actions = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        convertToPOButton = new WfmButton2(accountingStrings.convertToPO(), Constants.BTN_DEFAULT_OUTLINE);
        editButton = new WfmButton2(wfmStrings.edit(), Constants.BTN_DEFAULT_OUTLINE);
//        sendQuoteButton = new WfmButton2(accountingStrings.sendQuote(), Constants.BTN_DEFAULT_OUTLINE);
        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);

        approveButtons = new SplitButton(97, BTN_PRIMARY);
        approveButtons.ensureDebugId("saveAndApprove");
        approveButtons.setVisible(false);

        submitButton = new WfmButton2(wfmStrings.submitForApproval(), WfmButton2.BTN_PRIMARY);

        actions.setVisible(false);
        convertToPOButton.setVisible(false);
        editButton.setVisible(false);
//        sendQuoteButton.setVisible(false);
        printPdfSplitButton.setVisible(false);
        submitButton.setVisible(false);

        Div actionsWrapper = new Div();
        Div convertWrapper = new Div();
        Div editWrapper = new Div();
//        Div sendWrapper = new Div();
        Div pdfWrapper = new Div();
        Div approveWrapper = new Div();
        Div submitWrapper = new Div();

        actionsWrapper.add(actions);
        convertWrapper.add(convertToPOButton);
        editWrapper.add(editButton);
//        sendWrapper.add(sendQuoteButton);
        approveWrapper.add(approveButtons);
        submitWrapper.add(submitButton);
        pdfWrapper.add(printPdfSplitButton);

        result.add(actionsWrapper);
        result.add(editWrapper);
        result.add(pdfWrapper);
        result.add(convertWrapper);
        result.add(submitWrapper);
        result.add(approveWrapper);
        return result;
    }

    public String getMainTitle(String text, boolean... required) {
        return getTitle("<b class=customTitle><font size=+1>" + text + "</font></b>", required);
    }

    public String getCustomTitle(String text, boolean... required) {
        return getTitle("<b class=label>" + text + "</b>", required);
    }

    @Override
    protected void addButtons() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void getDataToFillFields() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.REQUEST_FOR_QUOTE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    public String getIconStyle() {
        return null;
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

    public class CustomCellLabel extends Label implements CustomCellInterface {

        private Integer rfqItemID;

        public CustomCellLabel(String text) {
            super(text);
        }

        public CustomCellLabel(String text, Integer rfqItemID) {
            super(text);
            this.rfqItemID = rfqItemID;
        }

        @Override
        public String getDisplayValue() {
            return getText();
        }

        @Override
        public void setItemValue(Object value) {
            setText((String) value);
        }

        public Integer getRfqItemID() {
            return rfqItemID;
        }

        @Override
        public void setItemFocus(boolean focused) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    public class ExtendedDataListBox extends DataListBox implements CustomCellInterface {

        boolean converted;
        boolean convertToPo;

        public ExtendedDataListBox(boolean converted) {
            super();
            this.converted = converted;
            setWithoutNullLabel(true);
            init();
        }

        private void init() {
            if (!converted) {
                addListItem(new SelectItem(0, wfmStrings.convert()));
                addListItem(new SelectItem(1, accountingStrings.dontConvert()));
                setSelected(0);
                convertToPo = true;
            } else {
                addListItem(new SelectItem(2, wfmStrings.converted()));
                setSelected(2);
                setEnabled(false);
            }

            setChangeEvent(() -> {
                convertToPo = getSelectedId() != null && getSelectedId() == 0;
            });

        }

        public boolean isConvertToPo() {
            return convertToPo;
        }

        public boolean isConverted() {
            return converted;
        }

        @Override
        public String getDisplayValue() {
            return getSelectedItem() != null ? getSelectedItem().getName() : getNullLabel();
        }

        @Override
        public void setItemValue(Object value) {
            setSelected((Integer) value);
        }

        @Override
        public void setItemFocus(boolean focused) {
            setFocus(focused);
        }

    }

    private HasLinks linkingUtil;

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(RequestForQuoteSummaryView.this) {
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

    @Override
    public String getPropertyCode() {
        return Constants.REQUEST_FOR_QUOTE;
    }
}
