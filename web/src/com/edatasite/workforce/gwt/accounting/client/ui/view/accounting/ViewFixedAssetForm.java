package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.BarcodeGenerator;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.FeatureConstants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * User: Dilshod Madrahimov
 * Date: 8/22/14
 * Time: 5:44 PM
 */
public class ViewFixedAssetForm extends AddEditFixedAssetForm implements FeatureConstants, Colapse {

    private final Integer objectID;
    private HTML owner;
    private HTML category;
    private HTML code;
    private HTML name;
    private HTML description;
    private HTML cost;
    private HTML purchaseDate;
    private HTML usefulLife;
    private HTML residualValue;
    private HTML account;
    private HTML fixedAssetAccount;
    private HTML expenseAccount;
    private HTML taxLookup;
    private HTML taxCalcTypeListBox;
    private FlowPanel imagePanel, relatedItem;
    private HorizontalPanel buttonPanel;
    private WfmButton2 printBarcodeButton;
    private Image imageLogo;
    private SplitButton printPdfSplitButton;
    private boolean showDescInBarcode;
    private BarcodeGenerator barcodeGenerator;
    private FlowPanel barcodeGeneratorPanel;
    private FlexTable allHistoryTable;
    private DisclosurePanel allHistoryPanel;
    private FlexTable allTable;
    private FooterInformer showJournal;
    private NoteHistoryWidget noteHistoryWidget;
    protected static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    protected final InvoiceServiceAsync invoiceService = InvoiceService.App.get();
    private LinkedHashMap<String, FormProperty> formPropertyMap;

    public ViewFixedAssetForm(Integer objectID) {
        super("summary");
        setDescription(property.getSingular(wfmStrings.summaryView(), wfmStrings.fixedAsset()));
        this.objectID = objectID;
    }

    public void initialize() {
        drawHistoryPanel();
        owner = initHTML(true);
        category = initHTML(true);
        code = initHTML(true);
        name = initHTML(true);
        description = initHTML(true);
        cost = initHTML(true);
        purchaseDate = initHTML(true);
        usefulLife = initHTML(true);
        residualValue = initHTML(true);
        account = initHTML(true);
        fixedAssetAccount = initHTML(true);
        expenseAccount = initHTML(true);
        relatedItem = new FlowPanel();
        imagePanel = new FlowPanel();
        imageLogo = new Image();
        taxLookup = initHTML(true);
        taxCalcTypeListBox = initHTML(true);
        imageLogo.setHeight("200px");
        imagePanel.add(imageLogo);

        addTitleField(FIXED_ASSET_INFORMATION, property.getSingular(wfmStrings.fixedAsset()) + "&nbsp;" + wfmStrings.summaryView());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.OWNER) != null) {
            addField(OWNER, owner, getTitle(formPropertyMap.get(OWNER).isChanged() ? formPropertyMap.get(OWNER).getTitle() : wfmStrings.owner()));
        } else {
            addField(OWNER, owner, getTitle(wfmStrings.owner()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CATEGORY) != null) {
            addField(CATEGORY, category, getTitle(formPropertyMap.get(CATEGORY).isChanged() ? formPropertyMap.get(CATEGORY).getTitle() : wfmStrings.category()));
        } else {
            addField(CATEGORY, category, getTitle(wfmStrings.category()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CODE) != null) {
            addField(CustomFormConstants.CODE, code, getTitle(formPropertyMap.get(CustomFormConstants.CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.CODE).getTitle() : wfmStrings.number()));
        } else {
            addField(CustomFormConstants.CODE, code, getTitle(property.getShortForNumber(wfmStrings.number())));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null) {
            addField(NAME, name, getTitle(formPropertyMap.get(NAME).isChanged() ? formPropertyMap.get(NAME).getTitle() : wfmStrings.name()));
        } else {
            addField(NAME, name, getTitle(wfmStrings.name()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null) {
            addField(DESCRIPTION, description, getTitle(formPropertyMap.get(DESCRIPTION).isChanged() ? formPropertyMap.get(DESCRIPTION).getTitle() : wfmStrings.description()));
        } else {
            addField(DESCRIPTION, description, getTitle(wfmStrings.description()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.COST) != null) {
            addField(COST, cost, getTitle(formPropertyMap.get(COST).isChanged() ? formPropertyMap.get(COST).getTitle() : wfmStrings.cost()));
        } else {
            addField(COST, cost, getTitle(wfmStrings.cost()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PURCHASE_DATE) != null) {
            addField(PURCHASE_DATE, purchaseDate, getTitle(formPropertyMap.get(PURCHASE_DATE).isChanged() ? formPropertyMap.get(PURCHASE_DATE).getTitle() : wfmStrings.purchaseDate()));
        } else {
            addField(PURCHASE_DATE, purchaseDate, getTitle(wfmStrings.purchaseDate()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.USEFUL_LIFE) != null) {
            addField(USEFUL_LIFE, usefulLife, getTitle(formPropertyMap.get(USEFUL_LIFE).isChanged() ? formPropertyMap.get(USEFUL_LIFE).getTitle() : wfmStrings.useFulLife()));
        } else {
            addField(USEFUL_LIFE, usefulLife, getTitle(wfmStrings.useFulLife()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RESIDUAL_VALUE) != null) {
            addField(RESIDUAL_VALUE, residualValue, getTitle(formPropertyMap.get(RESIDUAL_VALUE).isChanged() ? formPropertyMap.get(RESIDUAL_VALUE).getTitle() : wfmStrings.residualValue()));
        } else {
            addField(RESIDUAL_VALUE, residualValue, getTitle(wfmStrings.residualValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TAX_VALUE) != null) {
            addField(TAX_VALUE, taxLookup, getTitle(formPropertyMap.get(TAX_VALUE).isChanged() ? formPropertyMap.get(TAX_VALUE).getTitle() : wfmStrings.tax()));
        } else {
            addField(TAX_VALUE, taxLookup, getTitle(wfmStrings.tax(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TAX_CALC_TYPE) != null) {
            addField(TAX_CALC_TYPE, taxCalcTypeListBox, getTitle(formPropertyMap.get(TAX_CALC_TYPE).isChanged() ? formPropertyMap.get(TAX_CALC_TYPE).getTitle() : accountingStrings.amounts()));
        } else {
            addField(TAX_CALC_TYPE, taxCalcTypeListBox, getTitle(accountingStrings.amounts(), false));
        }

        addTitleField(FIXED_ASSET_FINANCING, property.getSingular(wfmStrings.financialInformation()));
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ACCOUNT_NAME) != null) {
            addField(ACCOUNT_NAME, account, getTitle(formPropertyMap.get(ACCOUNT_NAME).isChanged() ? formPropertyMap.get(ACCOUNT_NAME).getTitle() : wfmStrings.account()));
        } else {
            addField(ACCOUNT_NAME, account, getTitle(wfmStrings.account()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RELATED_ITEM) != null) {
            addField(RELATED_ITEM, relatedItem, getTitle(formPropertyMap.get(RELATED_ITEM).isChanged() ? formPropertyMap.get(RELATED_ITEM).getTitle() : wfmStrings.convertedItem()));
        } else {
            addField(RELATED_ITEM, relatedItem, getTitle(wfmStrings.convertedItem()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.UPLOAD_FORM) != null) {
            addField(UPLOAD_FORM, imagePanel, getTitle(formPropertyMap.get(UPLOAD_FORM).isChanged() ? formPropertyMap.get(UPLOAD_FORM).getTitle() : wfmStrings.image()));
        } else {
            addField(UPLOAD_FORM, imagePanel, null);
        }

        addField(BUTTONS, buttonPanel, null);
//        addTitleField(DEPRECIATION_ACCOUNT, wfmStrings.depreciationAccounts());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.FIXED_ASSET_ACCOUNT) != null) {
            addField(FIXED_ASSET_ACCOUNT, fixedAssetAccount, getTitle(formPropertyMap.get(FIXED_ASSET_ACCOUNT).isChanged() ? formPropertyMap.get(FIXED_ASSET_ACCOUNT).getTitle() : accountingStrings.accumulatedDepreciationAccount()));
        } else {
            addField(FIXED_ASSET_ACCOUNT, fixedAssetAccount, accountingStrings.accumulatedDepreciationAccount());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EXPENSE_ACCOUNT) != null) {
            addField(EXPENSE_ACCOUNT, expenseAccount, getTitle(formPropertyMap.get(EXPENSE_ACCOUNT).isChanged() ? formPropertyMap.get(EXPENSE_ACCOUNT).getTitle() : accountingStrings.depreciationExpenseAccount()));
        } else {
            addField(EXPENSE_ACCOUNT, expenseAccount, accountingStrings.depreciationExpenseAccount());
        }

        addTitleField(ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());

        getCustomFieldUtil().drawCustomFields(this, objectID, true);
addButtons();
        show();

    }

    private void drawHistoryPanel() {
        this.noteHistoryWidget = new NoteHistoryWidget(null);
        FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, View.wfmStrings.historyAndNotes(), this.noteHistoryWidget);
        informer.addClickHandler(click -> noteHistoryWidget.setLoadData(callback -> {
            if (objectID == null) {
                return;
            }
            invoiceService.loadFixedAssetHistoryNote(objectID, FIXED_ASSETS, callback);
        }));
        informer.setInitialClasses("informer-item history-notes-container");
        footer.addToLeftSide(informer);
    }

    protected void addButtons() {
        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        addButton(printPdfSplitButton);

        printBarcodeButton = new WfmButton2(accountingStrings.printQRcode(), WfmButton2.BTN_WHITE_OUTLINE);
        printBarcodeButton.addClickHandler(click -> {
            KpiModal barcodePopUp = new KpiModal();
            barcodePopUp.setSize("400px", "300px");
            barcodeGeneratorPanel = new FlowPanel();
            barcodeGenerator = new BarcodeGenerator();
            barcodeGeneratorPanel.add(barcodeGenerator.createImageWidget());
            barcodePopUp.add(barcodeGeneratorPanel);
            barcodeGenerator.generateBarCode(fixedAssetItem.getBarcodeGenerateText(showDescInBarcode, DateUtils.format(fixedAssetItem.getCreationDate().getNonConvertedDate()), Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_FULL_BARCODE_DATA)), AccountingConstants.LARGE);
            WfmButton2 cancel = new WfmButton2(wfmStrings.cancel());
            cancel.addClickHandler(c -> barcodePopUp.close());
            barcodePopUp.addButton(cancel);

            barcodePopUp.open();
        });
        addButton(printBarcodeButton);


        showJournal = new FooterInformer(SvgEnum.wallet, wfmStrings.showJournal(), null);
        showJournal.addClickHandler(clickEvent -> {
            SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|journalReport/" + fixedAssetItem.getJournalId(), accountingStrings.reportView() + ": " + fixedAssetItem.getNumberData().getNumberString(), accountingStrings.reportView() + ": " + fixedAssetItem.getNumberData().getNumberString());
        });
        showJournal.setBadgeCount(1);
        showJournal.setVisible(false);

        footer.addToLeftSide(showJournal);


    }

    private void fixedAssetDialogBox() {
        FixedAssetDisposeDialogBox faDialogBox = new FixedAssetDisposeDialogBox(fixedAssetItem, () -> WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_FIXED_ASSET_SAVED, objectID, ViewFixedAssetForm.this));
        faDialogBox.setViewCloseListener(() -> closeTab());
        faDialogBox.open();
    }

    public void configurePdfTools(FixedAssetItem result) {
        if (printPdfSplitButton == null) return;

        List<SplitButtonItem> pdfTemplatesList = new ArrayList<>();
        Integer defaultTemplateId = null;

        if (result != null && result.getTemplates() != null && result.getTemplates().length > 0) {
            for (SelectItem pdfItem : result.getTemplates()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(),
                        () -> generatePDF(panel, pdfItem.getId(), false)));
            }
        } else {
            pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_", wfmStrings.landscape(),
                    () -> generatePDF(panel, null, true)));
        }

        final Integer finalDefaultTemplateId = defaultTemplateId;
        SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(),
                () -> generatePDF(panel, finalDefaultTemplateId, false), true);

        pdfTemplatesList.add(pdfVersion);
        printPdfSplitButton.addItemList(pdfTemplatesList);
    }

    private void generatePDF(HTMLPanel panel, Integer templateID, boolean landscape) {
        RequestObject requestObject = new RequestObject(objectID);
        HashMap<String, String> parameters = requestObject.getRequestParams();

        if (templateID != null) {
            parameters.put("pdfTemplateID", String.valueOf(templateID));
        }
        if (landscape) {
            parameters.put("IS_LANDSCAPE", "true");
        }
        String pdfURL = CommandConstants.PDF_URL + "/fixedAssetViewPDFHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.FIXED_ASSET_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        fixedAssetService.getFixedAssetData(objectID, new AsyncCallback<FixedAssetItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(FixedAssetItem item) {
                LoadingPanel.loading(false);
                fixedAssetItem = item;
                if (fixedAssetItem.getJournalId() != null) {
                    showJournal.setVisible(true);
                }
                fillFormWithData();
                configurePdfTools(item);
            }
        });
    }

    protected void fillFormWithData() {
        if (fixedAssetItem.getImageLink() != null) {
            imageLogo.setUrl(fixedAssetItem.getImageLink());
        }
        setInnerHTML(owner, fixedAssetItem.getOwner() != null ? fixedAssetItem.getOwner().getName() : "");
        setInnerHTML(category, fixedAssetItem.getAccount().getName());

        setInnerHTML(code, fixedAssetItem.getNumberData().getNumberString());
        setInnerHTML(name, fixedAssetItem.getName());
        setInnerHTML(description, fixedAssetItem.getDescription());
        setInnerHTML(cost, AccountingUtils.get().formatPrice(fixedAssetItem.getCost()));
        setInnerHTML(purchaseDate, DateUtils.format(fixedAssetItem.getCreationDate()));
        setInnerHTML(usefulLife, fixedAssetItem.getUsefulLife().toString());
        setInnerHTML(residualValue, AccountingUtils.get().formatPrice(fixedAssetItem.getResidualValue()));
        setInnerHTML(taxLookup, fixedAssetItem.getTaxItem() != null ? fixedAssetItem.getTaxItem().getName() : "");
        setInnerHTML(taxCalcTypeListBox, this.getTaxCalculationType(fixedAssetItem.getTaxCalculationType()));

        setInnerHTML(account, fixedAssetItem.getFinancedByAccount() != null ? fixedAssetItem.getFinancedByAccount().getName() : "");
        setInnerHTML(fixedAssetAccount, fixedAssetItem.getFixedAssetAccount() != null ? fixedAssetItem.getFixedAssetAccount().getName() != null ? fixedAssetItem.getFixedAssetAccount().getName() : "" : "");
        setInnerHTML(expenseAccount, fixedAssetItem.getExpenseAccount() != null ? fixedAssetItem.getExpenseAccount().getName() != null ? fixedAssetItem.getExpenseAccount().getName() : "" : "");
        if (fixedAssetItem.getPurchaseOrderID() != null || fixedAssetItem.getPurchaseInvoiceID() != null) {
            Anchor a = new Anchor((fixedAssetItem.getPurchaseInvoiceID() != null ? wfmStrings.purchaseinvoice() + ": " : wfmStrings.purchaseorder() + ": ") + fixedAssetItem.getConvertedItemNumber());
            a.addClickHandler(clickEvent -> {
                String urlConst = Constants.PURCHASE_INVOICE;
                Integer objectID = fixedAssetItem.getPurchaseInvoiceID();
                if (fixedAssetItem.getPurchaseOrderID() != null) {
                    urlConst = Constants.PURCHASE_ORDER;
                    objectID = fixedAssetItem.getPurchaseOrderID();
                }
                ViewFixedAssetForm.goTo(urlConst + "|summary/" + objectID);
            });
            relatedItem.add(a);
        }
        showDescInBarcode = fixedAssetItem.getShowDescInBarcode();
        if (!showDescInBarcode) {
            printBarcodeButton.setVisible(false);
        }
        getCustomFieldUtil().fillCustomFieldsWithData(fixedAssetItem.getCustomFields(), true);
//        disposeButton.setVisible(!(DateUtils.isHasAccountingBeforeBlockDate() && DateUtils.getAccountingBeforeBlockDate().after(fixedAssetItem.getCreationDate().getNonConvertedDate())));
//        if (fixedAssetItem.getDisposed() != null && fixedAssetItem.getDisposed()) {
//            disposeButton.setVisible(false);
//        }

        if (!fixedAssetItem.getDisposed()) {
            addButton(wfmStrings.edit(), WfmButton2.BTN_PRIMARY, event -> SinksContainerFactory.entryPoint.onHistoryChanged("fixedasset|edit/" + fixedAssetItem.getObjectID()));

            addButton(wfmStrings.dispose(), BTN_DEFAULT_OUTLINE, event -> fixedAssetDialogBox());
        }
    }

    private String getTaxCalculationType(Integer type) {
        if (type == null || AccountingConstants.NO_TAX_CALCULATION.equals(type)) {
            return wfmStrings.noTax();
        } else if (type.equals(AccountingConstants.TAX_CALCULATION_INCLUSIVE)) {
            return wfmStrings.taxInclusive();
        } else if (type.equals(AccountingConstants.TAX_CALCULATION_EXCLUSIVE)) {
            return wfmStrings.taxExclusive();
        }
        return wfmStrings.noTax();
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
    public String getPropertyCode() {
        return Constants.FIXED_ASSETS;
    }
}
