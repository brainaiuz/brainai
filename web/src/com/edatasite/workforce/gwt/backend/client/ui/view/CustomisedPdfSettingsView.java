package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.PDFSettingsTransObject;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.enums.PdfGenerateTypeEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import static java.util.Arrays.asList;

/*
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 13.10.2010
 * Time: 17:55:57
 * To change this template use File | Settings | File Templates.
 */
public class CustomisedPdfSettingsView extends CustomForm implements CommandConstants, Colapse {

	private static final BackendStrings backendStrings = BackendStrings.App.get();

	private TextBox templateName;
	private WfmDropdown pdfType;
	private DataListBox font;

	private KpiSwitcher defaultTemplate;
	private KpiSwitcher browserVersion;

    private TextBox shortNumberingFormat;
    private TextBox shortDecimalSeparator;
    private TextBox shortGroupSeparator;

    private TextBox extendedNumberingFormat;
    private TextBox extendedDecimalSeparator;
    private TextBox extendedGroupSeparator;

    private TextArea velocityTemplate;

    private final Integer companyId;
    private TextArea fields;
    private Integer objectId;
    private DataListBox sections;
    private DataListBox customFormItemList;

    public CustomisedPdfSettingsView(Integer companyId) {
        super("pdftemplate", backendStrings.addPDFTemplate());
        this.companyId = companyId;
    }

    public CustomisedPdfSettingsView(Integer companyId, Integer objectId) {
		super("edit", backendStrings.editPDFTemplate());
		this.companyId = companyId;
		this.objectId = objectId;
	}

	@Override
	public String getIconStyle() {
		return null;
	}

	@Override
	protected Widget onInitialize() {
        super.onInitialize();

        templateName = new TextBox();
        pdfType = new WfmDropdown();
        font = new DataListBox();

        defaultTemplate = new KpiSwitcher();
        browserVersion = new KpiSwitcher();

        fields = new TextArea();
        fields.setPixelSize(1000, 700);

        shortNumberingFormat = new TextBox();
        shortNumberingFormat.setPlaceHolder("###.##");
        shortDecimalSeparator = new TextBox();
        shortDecimalSeparator.setPlaceHolder(backendStrings.decimalSeparator());
        shortGroupSeparator = new TextBox();
        shortGroupSeparator.setPlaceHolder(backendStrings.groupSeparator());

        extendedNumberingFormat = new TextBox();
        extendedNumberingFormat.setPlaceHolder("###.##");
        extendedDecimalSeparator = new TextBox();
        extendedDecimalSeparator.setPlaceHolder(backendStrings.decimalSeparator());
        extendedGroupSeparator = new TextBox();
        extendedGroupSeparator.setPlaceHolder(backendStrings.groupSeparator());

        velocityTemplate = new TextArea();
        velocityTemplate.setVisibleLines(25);

        sections = new DataListBox();
        sections.setItems(getSections());
        sections.setVisible(false);

        customFormItemList = new DataListBox();
        customFormItemList.setVisible(false);

        InputGroup shortInputGroup = new InputGroup(shortNumberingFormat, shortDecimalSeparator, shortGroupSeparator);
        InputGroup extendedInputGroup = new InputGroup(extendedNumberingFormat, extendedDecimalSeparator, extendedGroupSeparator);

        addTitleField(CustomFormConstants.INFORMATION, wfmStrings.information());
        addField(CustomFormConstants.NAME, templateName, getTitle(wfmStrings.template(), true));
        addField(CustomFormConstants.TYPE, pdfType, getTitle(backendStrings.pdfType(), true));
        addField(CustomFormConstants.CS_PDF_FONT_TYPE, font, getTitle(backendStrings.pleaseSelectFont(), true));
        addField(CustomFormConstants.IS_DEFAULT, defaultTemplate, getTitle(wfmStrings.defaultTemplate()));
        addField(CustomFormConstants.IS_BROWSER, browserVersion, getTitle(backendStrings.browserVersion()));
        addField(CustomFormConstants.SHORT_NUMBER_FORMAT, shortInputGroup, getTitle(backendStrings.shortNumberFormat()));
        addField(CustomFormConstants.EXTENDED_NUMBER_FORMAT, extendedInputGroup, getTitle(backendStrings.extendedNumberFormat()));
        addField(CustomFormConstants.CONTENT, velocityTemplate, getTitle(wfmStrings.content(), true));
        addField(CustomFormConstants.FIELDS, fields, getTitle(wfmStrings.additionalFields()));
        addField(CustomFormConstants.SECTION, sections, getTitle("Sections"));
        addField(CustomFormConstants.CUSTOM_FORM_ITEM, customFormItemList, getTitle("Custom Form Items"));
        show();

		return null;
	}

	@Override
	protected void addButtons() {
        addButton(wfmStrings.save(), WfmButton2.BTN_PRIMARY, SAVE_AND_ADD, "save_button", event -> {
            save(false);
        });
        addButton(wfmStrings.saveOnlyWithClose(), WfmButton2.BTN_PRIMARY, SAVE_AND_CLOSE, "save_close_button", event -> {
            save(true);
        });
	}

	@Override
	protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        BackendService.App.get().getCompanyPDFSettings(companyId, objectId, new AbstractAsyncCallback<PDFSettingsTransObject>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.errorOccurredSavingChanges(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(PDFSettingsTransObject result) {
                LoadingPanel.loading(false);

                templateName.setText(result.getTemplateName());
                pdfType.setItems(asList(result.getReferences()));
                if (result.getPdfReferenceID() != null) {
                    pdfType.setSelected(result.getPdfReferenceID());
                }

                pdfType.addValueChangeHandler(event -> {
                    onChangeTypes();
                    fillFieldBody();
                });

                sections.addValueChangeHandler(event -> {
                    getCustomFormItemList(companyId, sections.getSelectedItem().getDescription());
                    fields.setText("");
                });
                customFormItemList.addValueChangeHandler(event -> {
                    fillCustomFormItem();
                    fields.setText("");
                });

                if (result.getCustomFormItems() != null) {
                    customFormItemList.setItems(result.getCustomFormItems());
                    for (SelectItem item : result.getCustomFormItems()) {
                        if (!Utils.isNullOrEmpty(result.getCustomFormItemFormId()) && result.getCustomFormItemFormId().equals(item.getDescription())) {
                            customFormItemList.setSelected(item.getId());
                            customFormItemList.setVisible(true);
                        }
                    }
                }

                if (sections.getItems() != null) {
                    for (SelectItem item : sections.getItems()) {
                        if (!Utils.isNullOrEmpty(result.getSection()) && result.getSection().equals(item.getDescription())) {
                            sections.setSelected(item.getId());
                            sections.setVisible(true);
                        }
                    }
                }

                defaultTemplate.setValue(result.isDefaultTemplate());
                browserVersion.setValue(result.isBrowserVersion());
                font.setItems(result.getFonts());
                if (result.getFontFileName() != null && !"".equals(result.getFontFileName())) {
                    for (int i = 0; i < font.getItems().length; i++) {
                        if (result.getFontFileName().equals(font.getItems()[i].getDescription())) {
                            font.setSelected(font.getItems()[i].getId());
                        }
                    }
                }
                shortNumberingFormat.setText(result.getNumFormat());
                shortDecimalSeparator.setText(result.getNumFormatDecSeparator());
                shortGroupSeparator.setText(result.getNumFormatGroupSeparator());

                extendedNumberingFormat.setText(result.getExNumFormat());
                extendedDecimalSeparator.setText(result.getExNumFormatDecSeparator());
                extendedGroupSeparator.setText(result.getExNumFormatGroupSeparator());

                if (result.getContent() != null) {
                    velocityTemplate.setText(result.getContent());
                }

            }
        });
	}

    private void onChangeTypes() {
        if (pdfType.getSelectedItem() != null && !Utils.isNullOrEmpty(pdfType.getSelectedItem().getName()) && pdfType.getSelectedItem().getName().equals("Custom Form Item")) {
            sections.setVisible(true);
            customFormItemList.setVisible(true);
        } else {
            sections.setVisible(false);
            customFormItemList.setVisible(false);
        }
    }

    private void getCustomFormItemList(Integer companyId, String module) {
        BackendService.App.get().getCustomFormItemList(companyId, module, new AbstractAsyncCallback<PDFSettingsTransObject>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.errorOccurredSavingChanges(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(PDFSettingsTransObject result) {
                LoadingPanel.loading(false);
                customFormItemList.setItems(result.getCustomFormItems());
            }
        });
    }

	private void save(final boolean closeTab) {
		if (!validate()) {
			return;
		}

		PDFSettingsTransObject transObject = new PDFSettingsTransObject();
		transObject.setObjectID(objectId);
		transObject.setCompanyID(companyId);
		transObject.setDefaultTemplate(defaultTemplate.getValue());
		transObject.setBrowserVersion(browserVersion.getValue());
		transObject.setPdfReferenceID(pdfType.getSelectedId());
		transObject.setTemplateName(templateName.getText());
		if (font.getSelectedItem() != null) {
            transObject.setFontFileName(font.getSelectedItem().getDescription());
        }
        String encodedHtml = SafeHtmlUtils.htmlEscape(velocityTemplate.getText());
        transObject.setContent(encodedHtml);
		transObject.setNumFormat(shortNumberingFormat.getText());
		transObject.setNumFormatDecSeparator(shortDecimalSeparator.getText());
		transObject.setNumFormatGroupSeparator(shortGroupSeparator.getText());
		transObject.setExNumFormat(extendedNumberingFormat.getText());
		transObject.setExNumFormatDecSeparator(extendedDecimalSeparator.getText());
		transObject.setExNumFormatGroupSeparator(extendedGroupSeparator.getText());
		transObject.setGenerateType(PdfGenerateTypeEnum.I_TEXT);
        if (customFormItemList.getSelectedItem() != null) {
            transObject.setCustomFormItemFormId(customFormItemList.getSelectedItem().getDescription());
        }
        if (sections.getSelectedItem() != null) {
            transObject.setSection(sections.getSelectedItem().getDescription());
        }

		BackendService.App.get().saveCompanyPdfTemplate(transObject, new AbstractAsyncCallback<Integer>() {
			public void failure(Throwable caught) {
				Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
			}

			public void success(Integer result) {
				objectId = result;
				WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PDF_TEMPLATE_SAVED, objectId, CustomisedPdfSettingsView.this);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.template()), Info.Type.INFO);
                if (closeTab) {
					closeTab();
				}
			}
		});
	}

	private boolean validate() {
	    int errors = 0;
        clearErrorStyle();

        if (pdfType.getSelectedItem() != null && !Utils.isNullOrEmpty(pdfType.getSelectedItem().getName()) && pdfType.getSelectedItem().getName().equals("Custom Form Item")) {
            errors += markAsError(sections, !Validation.validateListBoxRequired(sections));
            errors += markAsError(customFormItemList, !Validation.validateListBoxRequired(customFormItemList));
        }

        errors += markAsError(font, !Validation.validateListBoxRequired(font));
        errors += markAsError(pdfType, !Validation.validateWfmDropdown(pdfType));
        errors += markAsError(templateName, !Validation.validateTextBoxRequired(templateName));
        errors += markAsError(velocityTemplate, !Validation.validateTextAreaRequired(velocityTemplate));

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private SelectItem[] getSections() {
        SelectItem[] selectItem = new SelectItem[]{
                new SelectItem(1, "Accounts", ModuleEnum.ACCOUNTING.getCode()),
                new SelectItem(2, "Humans", ModuleEnum.HRMS.getCode()),
                new SelectItem(3, "Projects", ModuleEnum.PM.getCode()),
                new SelectItem(4, "Sales", ModuleEnum.CRM.getCode()),
                new SelectItem(5, "Documents", ModuleEnum.DOCUMENTS.getCode()),
                new SelectItem(6, "Payroll", ModuleEnum.PAYROLL.getCode())
        };
        return selectItem;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.ITEXT_PDF_FORM;
    }

    @Override
    protected String getFormType() {
	    if (objectId == null) {
	        return LayoutRPC.ADD;
        }
        return LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return null;
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

    private void fillFieldBody() {

        StringBuffer f = new StringBuffer();

        TreeMap<String, String> customProductTable = new TreeMap<>();
        customProductTable.put("ITEM_DOUBLE_TAX_RATE", "TAX RATE");
        customProductTable.put("ITEM_DOUBLE_TAX_AMOUNT", "TAX AMOUNT");
        customProductTable.put("ORDERED_QTY", "ORDERED QUANTITY");
        customProductTable.put("NO", "NUMBER");
        customProductTable.put("ACCOUNT", "");
        customProductTable.put("ITEM_PRODUCT_SERIAL", "");
        customProductTable.put("PARENT_ACCOUNT", "");
        customProductTable.put("QTY_HRS", "QUANTITY");
        customProductTable.put("ITEM_RECIEVE", "");
        customProductTable.put("PICK_ITEM_REFERENCE", "");
        customProductTable.put("ITEM_COST_PRICE", "");
        customProductTable.put("TAX_AMOUNT", "");
        customProductTable.put("ITEM_PRODUCT_LOT_NUMBER", "ITEM PRODUCT NUMBER");
        customProductTable.put("ITEM_MANUFACTURER", "");
        customProductTable.put("ITEM_ORIGINAL_PRICE", "");
        customProductTable.put("PICK_ITEM_QTY_PER_PACK", "PICK ITEM QUANTITY PER PACK");
        customProductTable.put("DOUBLE_TAX_LABEL", "");
        customProductTable.put("NET_AMOUNT_IN_BASE", "");
        customProductTable.put("ITEM_TOTAL_SALES_PRICE", "");
        customProductTable.put("ITEM_REVERSED_QTY", "ITEM REVERSED QUANTITY");
        customProductTable.put("ITEM_BATCH_SERIAL_NUMBER", "");
        customProductTable.put("NET_WITHOUT_DISCOUNT", "");
        customProductTable.put("NET_AMOUNT", "");
        customProductTable.put("ITEM_PRODUCT_LOT_NUMBER_QTY", "ITEM PRODUCT NUMBER QUANTITY");
        customProductTable.put("ACCOUNT_NUMBER", "");
        customProductTable.put("DISCOUNT", "");
        customProductTable.put("DESCRIPTION", "");
        customProductTable.put("ITEM_SKU_NUMBER", "");
        customProductTable.put("ITEM_NET_PROFIT", "");
        customProductTable.put("ITEM_SUB_PROJECT", "");
        customProductTable.put("PRODUCT_NAME", "");
        customProductTable.put("DISCOUNT_TYPE", "");
        customProductTable.put("UNIT_PRICE_DISCOUNTED", "");
        customProductTable.put("PICK_ITEM_SHIPPED_QTY", "PICK ITEM SHIPPED QUANTITY");
        customProductTable.put("ITEM_PRODUCT_EXPIRATION_DATE", "");
        customProductTable.put("ITEM_IS_PICKABLE", "");
        customProductTable.put("QTY_ON_HAND", "QUANTITY ON HAND");
        customProductTable.put("TAX_AMOUNT_IN_BASE", "");
        customProductTable.put("ITEM_PROJECT", "");
        customProductTable.put("TAX_LABEL", "");
        customProductTable.put("ITEM_PROFIT_IC", "");
        customProductTable.put("ITEM_CATEGORY", "");
        customProductTable.put("ITEM_TYPE", "");
        customProductTable.put("TOTAL_AMOUNT", "");
        customProductTable.put("UNIT_MEASUREMENT", "");
        customProductTable.put("NAME", "");
        customProductTable.put("ITEM_PART_NUMBER", "");
        customProductTable.put("UNIT_MEASUREMENT_DESCRIPTION", "");
        customProductTable.put("PICK_ITEM_LAST_SHIPPED_QTY", "PICKED ITEM LAST SHIPPED QUANTITY");
        customProductTable.put("TOTAL_AMOUNT_IN_BASE", "");
        customProductTable.put("UNIT_PRICE", "");
        customProductTable.put("ORDERED_PRODUCT_QTY", "");
        customProductTable.put("ITEM_BATCH_QTY", "ITEM BATCH QUANTITY");
        customProductTable.put("ITEM_BRAND_NAME", "");
        customProductTable.put("ITEM_BARCODE", "");
        customProductTable.put("ITEM_BACK_ORDERED", "");
        customProductTable.put("ITEM_QUOTE_QUANTITY_ORDERED", "");
        customProductTable.put("TAX_RATE", "");
        customProductTable.put("ITEM_WAREHOUSE", "");
        customProductTable.put("PREV_INVOICES_PRODUCT_QTY", "PREVIOUS INVOICES PRODUCT QUANTITY");
        customProductTable.put("DISCOUNT_AMOUNT", "");
        customProductTable.put("ITEM_CONVERTED_QUOTE_CUSTOM_FIELDS", "");
        customProductTable.put("ITEM_VENDOR", "");
        customProductTable.put("ITEM_PICTURE", "");
        customProductTable.put("ITEM_QUOTE_NUMBER", "");
        customProductTable.put("DEPARTMENT", "");
        customProductTable.put("ITEM_BATCH_EXPIRE_DATE", "");
        customProductTable.put("UNIT_PRICE_AVERAGE", "");
        customProductTable.put("UNIT_PRICE_IN_BASE", "");
        customProductTable.put("ITEM_QTY", "ITEM QUANTITY");
        customProductTable.put("ITEM_TOTAL_COST_PRICE", "");
        customProductTable.put("PICK_ITEM_NUMBER_PACKS", "");

        TreeMap<String, String> customTotalTable = new TreeMap<String, String>();
        customTotalTable.put("SUBTOTAL", "");
        customTotalTable.put("TOTAL_WORD_ALL", "");
        customTotalTable.put("TOTAL_IN_BASE_WORD", "");
        customTotalTable.put("DUE_AMOUNT_WORD", "");
        customTotalTable.put("BTW_TOTAL", "");
        customTotalTable.put("TAX_TOTAL_IN_BASE", "");
        customTotalTable.put("EXCHANGE_RATE_REVERSE", "");
        customTotalTable.put("DUE_AMOUNT", "");
        customTotalTable.put("TOTAL_AMOUNT_AED", "");
        customTotalTable.put("LAST_PAYMENT", "");
        customTotalTable.put("EXCHANGE_RATE_AED", "");
        customTotalTable.put("TOTAL_WORD", "");
        customTotalTable.put("SUBTOTAL_WORD", "");
        customTotalTable.put("TOTAL_IN_BASE", "");
        customTotalTable.put("TOTAL_WORD_ALL_WITH_CENT", "");
        customTotalTable.put("TOTAL_UZB_WORD_ALL_LOTIN", "");
        customTotalTable.put("TAX_TOTAL", "");
        customTotalTable.put("DUE_AMOUNT_ARABIC_WORD", "");
        customTotalTable.put("TOTAL_QUONTITY", "TOTAL QUANTITY");
        customTotalTable.put("BILL_EXP_TAX_TOTAL", "");
        customTotalTable.put("DISCOUNTED_SUBTOTAL", "");
        customTotalTable.put("EINDTOTAL", "");
        customTotalTable.put("TOTAL_ARABIC_WORD_ALL", "");
        customTotalTable.put("TOTAL_UZB_WORD_ALL", "");
        customTotalTable.put("BTW_MIN_TOTAL", "");
        customTotalTable.put("TOTAL_IN_USD_WORD", "");
        customTotalTable.put("EIND_SUBTOTAL", "");
        customTotalTable.put("DISCOUNT_TOTAL_WORD_ALL", "");
        customTotalTable.put("PAYMENT_TOTAL", "");
        customTotalTable.put("TOTAL_PROFIT_AMOUNT", "");
        customTotalTable.put("EXCHANGE_RATE", "");
        customTotalTable.put("TOTAL", "");
        customTotalTable.put("BILL_EXP_TOTAL", "");
        customTotalTable.put("SUBTOTAL_WORD_ALL", "");
        customTotalTable.put("HAS_BILL_EXP_TOTAL", "");
        customTotalTable.put("TOTAL_OVERALL_DISCOUNT", "");
        customTotalTable.put("INVOICE_QUOTE_UNREC_REVENUE_TOTAL", "");
        customTotalTable.put("SUBTOTAL_IN_BASE", "");
        customTotalTable.put("BTW_MAX_TOTAL", "");

        TreeMap<String, String> customExpenseTable = new TreeMap<String, String>();
        customExpenseTable.put("ITEM_EXPENSE_MARKUP_TAX_RATE", "");
        customExpenseTable.put("ITEM_EXPENSE_MARKUP_AMOUNT", "");
        customExpenseTable.put("ITEM_EXPENSE_DESCRIPTION", "");
        customExpenseTable.put("ITEM_EXPENSE_MARKUP_TAX_AMOUNT", "");
        customExpenseTable.put("ITEM_EXPENSE_DATE", "");
        customExpenseTable.put("ITEM_EXPENSE_CURRENCY", "");
        customExpenseTable.put("ITEM_EXPENSE_TOTAL_AMOUNT", "");
        customExpenseTable.put("ITEM_EXPENSE_BEDRAG_AMOUNT", "");
        customExpenseTable.put("ITEM_EXPENSE_TOTAL_WITH_MARKUP", "");
        customExpenseTable.put("ITEM_EXPENSE_CATEGORY", "");

        TreeMap<String, String> customNumberAndDatesTable = new TreeMap<>();
        customNumberAndDatesTable.put("PERIOD_DAYS", "");
        customNumberAndDatesTable.put("FROM_QUOTE_NUMBER", "");
        customNumberAndDatesTable.put("COMP_VAT_NUMBER", "");
        customNumberAndDatesTable.put("CUSTOMER_BALANCE", "");
        customNumberAndDatesTable.put("END_MONTH", "");
        customNumberAndDatesTable.put("LAST_UPDATED_DATE", "");
        customNumberAndDatesTable.put("QRCODE", "");
        customNumberAndDatesTable.put("LAST_UPDATER", "");
        customNumberAndDatesTable.put("INV_TYPE", "INVOICE TYPE");
        customNumberAndDatesTable.put("PO_NUMBER", "");
        customNumberAndDatesTable.put("START_MONTH", "");
        customNumberAndDatesTable.put("QUOTATION_DATE", "");
        customNumberAndDatesTable.put("INV_DATE", "INVOICE DATE");
        customNumberAndDatesTable.put("REFERENCE", "");
        customNumberAndDatesTable.put("INV_NUMBER", "INVOICE NUMBER");
        customNumberAndDatesTable.put("INV_DUE_DATE", "INVOICE DUE DATE");
        customNumberAndDatesTable.put("QT_DATE", "QUOTE DATE");
        customNumberAndDatesTable.put("TAX_CODE", "");
        customNumberAndDatesTable.put("RECEIPT_NO", "RECEIPT NUMBER");
        customNumberAndDatesTable.put("CREATION_DATE", "");
        customNumberAndDatesTable.put("QT_NUMBER", "QUOTE NUMBER");
        customNumberAndDatesTable.put("SALE_INVOICE_APPROVER", "");
        customNumberAndDatesTable.put("PREVIEW", "");
        customNumberAndDatesTable.put("PERIOD_END_DATE", "");
        customNumberAndDatesTable.put("PERIOD_START_DATE", "");
        customNumberAndDatesTable.put("SHIPPING_METHOD", "");
        customNumberAndDatesTable.put("INVOICE_STATUS", "");
        customNumberAndDatesTable.put("PAYMENT_DATE", "");
        customNumberAndDatesTable.put("PERIOD", "");
        customNumberAndDatesTable.put("INVOICE_DUE_TERMS", "");

        TreeMap<String, String> customBillToAddress = new TreeMap<String, String>();
        customBillToAddress.put("CLIENT_FAX", "");
        customBillToAddress.put("CLIENT_BANK_ACCOUNT_NO", "CLIENT BANK ACCOUNT NUMBER");
        customBillToAddress.put("MAIL_COUNTRY", "");
        customBillToAddress.put("CLIENT_CONTACT", "");
        customBillToAddress.put("BILL_ADDRESS", "");
        customBillToAddress.put("CLIENT_PHONE", "");
        customBillToAddress.put("CLIENT_BANK_IBAN_CODE", "");
        customBillToAddress.put("CLIENT_BANK_BRANCH", "");
        customBillToAddress.put("CLIENT_BANK_SORT_CODE", "");
        customBillToAddress.put("PARENT_ACCOUNT", "");
        customBillToAddress.put("MAIL_COUNTRY_EU_MEMBER", "");
        customBillToAddress.put("BILL_ZIPCODE", "");
        customBillToAddress.put("ACCOUNT_OWNER", "");
        customBillToAddress.put("BILL_ADDRESS_NAME", "");
        customBillToAddress.put("BILL_COUNTRY", "");
        customBillToAddress.put("CLIENT_BANK_NAME", "");
        customBillToAddress.put("MAIL_CITY", "");
        customBillToAddress.put("MAIL_ADDRESS_NAME", "");
        customBillToAddress.put("CONTACT_JOB_TITLE", "");
        customBillToAddress.put("MAIL_ZIPCODE", "");
        customBillToAddress.put("BILL_STATE", "");
        customBillToAddress.put("CONTACT_PHONE", "");
        customBillToAddress.put("CLIENT_EMAIL", "");
        customBillToAddress.put("BILL_ADDRESS2", "");
        customBillToAddress.put("CLIENT_CODE", "");
        customBillToAddress.put("MAIL_ADDRESS", "");
        customBillToAddress.put("CLIENT_VAT_NUMBER", "");
        customBillToAddress.put("CLIENT_BANK_ADDRESS", "");
        customBillToAddress.put("CLIENT_REGISTRATION_NUMBER", "");
        customBillToAddress.put("CLIENT_BANK_SWIFT_CODE", "");
        customBillToAddress.put("CLIENT_TAX_TREATMENT", "");
        customBillToAddress.put("CLIENT_BANK_ACCOUNT_NAME", "");
        customBillToAddress.put("BILL_COUNTRY_EU_MEMBER", "");
        customBillToAddress.put("USER_F_NAME", "");
        customBillToAddress.put("CLIENT_CURRENCY", "");
        customBillToAddress.put("NAME", "");
        customBillToAddress.put("CONTACT_MIDDLE_NAME", "");
        customBillToAddress.put("USER_M_NAME", "USER MIDDLE NAME");
        customBillToAddress.put("CONTACT_LAST_NAME", "");
        customBillToAddress.put("USER_PHONE", "");
        customBillToAddress.put("MAIL_ADDRESS2", "");
        customBillToAddress.put("BILL_CITY", "");
        customBillToAddress.put("USER_EMAIL", "");
        customBillToAddress.put("USERNAME", "");
        customBillToAddress.put("CONTACT_FIRST_NAME", "");
        customBillToAddress.put("USER_L_NAME", "USER LAST NAME");
        customBillToAddress.put("PAYMENT_METHOD", "");
        customBillToAddress.put("CLIENT_WEBSITE", "");
        customBillToAddress.put("CLIENT_OWNER", "");
        customBillToAddress.put("CONTACT_EMAIL", "");
        customBillToAddress.put("MAIL_STATE", "");

        TreeMap<String, String> paymentHistoryTable = new TreeMap<String, String>();
        paymentHistoryTable.put("PAYMENT_DATE", "");
        paymentHistoryTable.put("PAYMENT_USER", "");
        paymentHistoryTable.put("PAYMENT_AMOUNT", "");
        paymentHistoryTable.put("PAYMENT_CUSTOMER", "");
        paymentHistoryTable.put("PAYMENT_REFERENCE", "");
        paymentHistoryTable.put("PAYMENT_PAID_TO", "");
        paymentHistoryTable.put("PAYMENT_TYPE", "");
        paymentHistoryTable.put("PAYMENT_DETAIL", "");


        //TODO SALES QUOTE

        TreeMap<String, String> SQcustomProductTable = new TreeMap<String, String>();
        SQcustomProductTable.put("ATTACHMENTS", "");
        SQcustomProductTable.put("ITEM_PRODUCT_EXPIRATION_DATE", "");
        SQcustomProductTable.put("PICK_ITEM_REFERENCE", "");
        SQcustomProductTable.put("DISCOUNT_AMOUNT", "");
        SQcustomProductTable.put("ITEM_CATEGORY", "");
        SQcustomProductTable.put("ITEM_BARCODE", "");
        SQcustomProductTable.put("NET_AMOUNT_IN_BASE", "");
        SQcustomProductTable.put("ITEM_IS_PICKABLE", "");
        SQcustomProductTable.put("ITEM_BATCH_EXPIRE_DATE", "");
        SQcustomProductTable.put("ITEM_RECIEVE", "");
        SQcustomProductTable.put("ITEM_PROFIT_IC", "");
        SQcustomProductTable.put("DISCOUNT", "");
        SQcustomProductTable.put("DISCOUNT_TYPE", "");
        SQcustomProductTable.put("QTY_HRS", "QUANTITY");
        SQcustomProductTable.put("QTY_ON_HAND", "");
        SQcustomProductTable.put("UNIT_PRICE", "");
        SQcustomProductTable.put("UNIT_PRICE_AVERAGE", "");
        SQcustomProductTable.put("ORDERED_PRODUCT_QTY", "ORDERED PRODUCT QUANTITY");
        SQcustomProductTable.put("ITEM_SKU_NUMBER", "");
        SQcustomProductTable.put("ITEM_DOUBLE_TAX_RATE", "");
        SQcustomProductTable.put("PICK_ITEM_LAST_SHIPPED_QTY", "PICK ITEM LAST SHIPPED QUANTITY");
        SQcustomProductTable.put("ITEM_VENDOR", "");
        SQcustomProductTable.put("UNIT_PRICE_IN_BASE", "");
        SQcustomProductTable.put("TOTAL_AMOUNT_IN_BASE", "");
        SQcustomProductTable.put("ITEM_TOTAL_SALES_PRICE", "");
        SQcustomProductTable.put("DEPARTMENT", "");
        SQcustomProductTable.put("ITEM_QUOTE_NUMBER", "");
        SQcustomProductTable.put("TAX_AMOUNT_IN_BASE", "");
        SQcustomProductTable.put("UNIT_MEASUREMENT_DESCRIPTION", "");
        SQcustomProductTable.put("ITEM_REVERSED_QTY", "ITEM REVERSED QUANTITY");
        SQcustomProductTable.put("ITEM_PICTURE", "");
        SQcustomProductTable.put("ITEM_BRAND_NAME", "");
        SQcustomProductTable.put("TAX_LABEL", "");
        SQcustomProductTable.put("ITEM_WAREHOUSE", "");
        SQcustomProductTable.put("PARENT_ACCOUNT", "");
        SQcustomProductTable.put("UNIT_MEASUREMENT", "");
        SQcustomProductTable.put("ITEM_PROJECT", "");
        SQcustomProductTable.put("NO", "NUMBER");
        SQcustomProductTable.put("ITEM_PRODUCT_LOT_NUMBER", "");
        SQcustomProductTable.put("NET_WITHOUT_DISCOUNT", "");
        SQcustomProductTable.put("ITEM_PRODUCT_LOT_NUMBER_QTY", "");
        SQcustomProductTable.put("ITEM_BATCH_QTY", "ITEM BATCH QUANTITY");
        SQcustomProductTable.put("ITEM_ORIGINAL_PRICE", "");
        SQcustomProductTable.put("ORDERED_QTY", "ORDERED QUANTITY");
        SQcustomProductTable.put("ITEM_DOUBLE_TAX_AMOUNT", "");
        SQcustomProductTable.put("PREV_INVOICES_PRODUCT_QTY", "");
        SQcustomProductTable.put("ITEM_TOTAL_COST_PRICE", "");
        SQcustomProductTable.put("TOTAL_AMOUNT", "");
        SQcustomProductTable.put("PICK_ITEM_SHIPPED_QTY", "PICK ITEM SHIPPED QUANTITY");
        SQcustomProductTable.put("ITEM_QTY", "ITEM QUANTITY");
        SQcustomProductTable.put("PICK_ITEM_NUMBER_PACKS", "");
        SQcustomProductTable.put("NAME", "");
        SQcustomProductTable.put("ACCOUNT_NUMBER", "");
        SQcustomProductTable.put("ACCOUNT", "");
        SQcustomProductTable.put("TAX_AMOUNT", "");
        SQcustomProductTable.put("ITEM_TYPE", "");
        SQcustomProductTable.put("ITEM_COST_PRICE", "");
        SQcustomProductTable.put("ITEM_NET_PROFIT", "");
        SQcustomProductTable.put("UNIT_PRICE_DISCOUNTED", "");
        SQcustomProductTable.put("ITEM_BATCH_SERIAL_NUMBER", "");
        SQcustomProductTable.put("TAX_RATE", "");
        SQcustomProductTable.put("PRODUCT_NAME", "");
        SQcustomProductTable.put("ITEM_SUB_PROJECT", "");
        SQcustomProductTable.put("ITEM_BACK_ORDERED", "");
        SQcustomProductTable.put("NET_AMOUNT", "");
        SQcustomProductTable.put("ITEM_PART_NUMBER", "");
        SQcustomProductTable.put("DOUBLE_TAX_LABEL", "");
        SQcustomProductTable.put("ITEM_QUOTE_QUANTITY_ORDERED", "");
        SQcustomProductTable.put("ITEM_MANUFACTURER", "");
        SQcustomProductTable.put("PICK_ITEM_QTY_PER_PACK", "PICK ITEM QUANTITY PER PACK");
        SQcustomProductTable.put("DESCRIPTION", "");
        SQcustomProductTable.put("ITEM_PRODUCT_SERIAL", "");

        TreeMap<String, String> SQcustomTotalTable = new TreeMap<String, String>();
        SQcustomTotalTable.put("EXCHANGE_RATE_REVERSE", "");
        SQcustomTotalTable.put("SUBTOTAL", "");
        SQcustomTotalTable.put("DUE_AMOUNT", "");
        SQcustomTotalTable.put("TOTAL_WORD_ALL", "");
        SQcustomTotalTable.put("SUBTOTAL_WORD_ALL", "");
        SQcustomTotalTable.put("BTW_MAX_TOTAL", "");
        SQcustomTotalTable.put("DISCOUNT_TOTAL_WORD_ALL", "");
        SQcustomTotalTable.put("PAYMENT_TOTAL", "");
        SQcustomTotalTable.put("TOTAL_IN_BASE", "");
        SQcustomTotalTable.put("TAX_TOTAL", "");
        SQcustomTotalTable.put("TOTAL_WORD", "");
        SQcustomTotalTable.put("TOTAL_AMOUNT_AED", "");
        SQcustomTotalTable.put("TAX_TOTAL_IN_BASE", "");
        SQcustomTotalTable.put("EXCHANGE_RATE_AED", "");
        SQcustomTotalTable.put("BILL_EXP_TAX_TOTAL", "");
        SQcustomTotalTable.put("SUBTOTAL_WORD", "");
        SQcustomTotalTable.put("DUE_AMOUNT_ARABIC_WORD", "");
        SQcustomTotalTable.put("TOTAL_ARABIC_WORD_ALL", "");
        SQcustomTotalTable.put("LAST_PAYMENT", "");
        SQcustomTotalTable.put("INVOICE_QUOTE_UNREC_REVENUE_TOTAL", "");
        SQcustomTotalTable.put("BTW_TOTAL", "");
        SQcustomTotalTable.put("BILL_EXP_TOTAL", "");
        SQcustomTotalTable.put("TOTAL_OVERALL_DISCOUNT", "");
        SQcustomTotalTable.put("EIND_SUBTOTAL", "");
        SQcustomTotalTable.put("TOTAL", "");
        SQcustomTotalTable.put("TOTAL_IN_BASE_WORD", "");
        SQcustomTotalTable.put("TOTAL_QUONTITY", "TOTAL QUANTITY");
        SQcustomTotalTable.put("BTW_MIN_TOTAL", "");
        SQcustomTotalTable.put("TOTAL_UZB_WORD_ALL", "");
        SQcustomTotalTable.put("DISCOUNTED_SUBTOTAL", "");
        SQcustomTotalTable.put("TOTAL_WORD_ALL_WITH_CENT", "");
        SQcustomTotalTable.put("EINDTOTAL", "");
        SQcustomTotalTable.put("HAS_BILL_EXP_TOTAL", "");
        SQcustomTotalTable.put("SUBTOTAL_IN_BASE", "");
        SQcustomTotalTable.put("EXCHANGE_RATE", "");
        SQcustomTotalTable.put("TOTAL_IN_USD_WORD", "");
        SQcustomTotalTable.put("DUE_AMOUNT_WORD", "");
        SQcustomTotalTable.put("TOTAL_UZB_WORD_ALL_LOTIN", "");

        TreeMap<String, String> SQcustomNumberAndDatesTable = new TreeMap<String, String>();
        SQcustomNumberAndDatesTable.put("PERIOD_DAYS", "");
        SQcustomNumberAndDatesTable.put("APPROVER", "");
        SQcustomNumberAndDatesTable.put("SHIPPING_METHOD", "");
        SQcustomNumberAndDatesTable.put("PO_NUMBER", "");
        SQcustomNumberAndDatesTable.put("QUOTATION_DATE", "");
        SQcustomNumberAndDatesTable.put("CREATION_DATE", "");
        SQcustomNumberAndDatesTable.put("CUSTOMER_BALANCE", "");
        SQcustomNumberAndDatesTable.put("COMP_VAT_NUMBER", "");
        SQcustomNumberAndDatesTable.put("SALE_INVOICE_APPROVER", "");
        SQcustomNumberAndDatesTable.put("REFERENCE", "");
        SQcustomNumberAndDatesTable.put("INVOICE_DUE_TERMS", "");
        SQcustomNumberAndDatesTable.put("INV_DUE_DATE", "");
        SQcustomNumberAndDatesTable.put("LAST_UPDATER", "");
        SQcustomNumberAndDatesTable.put("LAST_UPDATED_DATE", "");
        SQcustomNumberAndDatesTable.put("INVOICE_STATUS", "");
        SQcustomNumberAndDatesTable.put("FROM_QUOTE_NUMBER", "");
        SQcustomNumberAndDatesTable.put("INV_NUMBER", "INVOICE NUMBER");
        SQcustomNumberAndDatesTable.put("INV_TYPE", "INVOICE TYPE");
        SQcustomNumberAndDatesTable.put("RECEIPT", "");
        SQcustomNumberAndDatesTable.put("INV_DATE", "INVOICE DATE");
        SQcustomNumberAndDatesTable.put("PREVIEW", "");
        SQcustomNumberAndDatesTable.put("TAX_CODE", "");
        SQcustomNumberAndDatesTable.put("SALE_QUOTE_APPROVER", "");

        TreeMap<String, String> SQcustomBillToAddress = new TreeMap<String, String>();
        SQcustomBillToAddress.put("USER_L_NAME", "USER LAST NAME");
        SQcustomBillToAddress.put("USERNAME", "");
        SQcustomBillToAddress.put("CONTACT_LAST_NAME", "");
        SQcustomBillToAddress.put("CLIENT_BANK_BRANCH", "");
        SQcustomBillToAddress.put("MAIL_ADDRESS2", "");
        SQcustomBillToAddress.put("MAIL_COUNTRY", "");
        SQcustomBillToAddress.put("BILL_CITY", "");
        SQcustomBillToAddress.put("MAIL_CITY", "");
        SQcustomBillToAddress.put("BILL_STATE", "");
        SQcustomBillToAddress.put("USER_EMAIL", "");
        SQcustomBillToAddress.put("CLIENT_BANK_SWIFT_CODE", "");
        SQcustomBillToAddress.put("BILL_COUNTRY", "");
        SQcustomBillToAddress.put("CONTACT_FIRST_NAME", "");
        SQcustomBillToAddress.put("PARENT_ACCOUNT", "");
        SQcustomBillToAddress.put("ACCOUNT_OWNER", "");
        SQcustomBillToAddress.put("BILL_ZIPCODE", "");
        SQcustomBillToAddress.put("CLIENT_BANK_SORT_CODE", "");
        SQcustomBillToAddress.put("CLIENT_CONTACT", "");
        SQcustomBillToAddress.put("MAIL_ADDRESS", "");
        SQcustomBillToAddress.put("CLIENT_PHONE", "");
        SQcustomBillToAddress.put("USER_M_NAME", "");
        SQcustomBillToAddress.put("CLIENT_BANK_ACCOUNT_NO", "");
        SQcustomBillToAddress.put("BILL_ADDRESS", "");
        SQcustomBillToAddress.put("MAIL_COUNTRY_EU_MEMBER", "");
        SQcustomBillToAddress.put("CLIENT_BANK_ADDRESS", "");
        SQcustomBillToAddress.put("USER_F_NAME", "USER FIRST NAME");
        SQcustomBillToAddress.put("CLIENT_BANK_NAME", "");
        SQcustomBillToAddress.put("CONTACT_PHONE", "");
        SQcustomBillToAddress.put("CLIENT_REGISTRATION_NUMBER", "");
        SQcustomBillToAddress.put("CLIENT_VAT_NUMBER", "");
        SQcustomBillToAddress.put("PAYMENT_METHOD", "");
        SQcustomBillToAddress.put("NAME", "");
        SQcustomBillToAddress.put("BILL_ADDRESS2", "");
        SQcustomBillToAddress.put("BILL_COUNTRY_EU_MEMBER", "");
        SQcustomBillToAddress.put("CLIENT_FAX", "");
        SQcustomBillToAddress.put("CLIENT_BANK_ACCOUNT_NAME", "");
        SQcustomBillToAddress.put("CLIENT_TAX_TREATMENT", "");
        SQcustomBillToAddress.put("CLIENT_CURRENCY", "");
        SQcustomBillToAddress.put("CLIENT_WEBSITE", "");
        SQcustomBillToAddress.put("CLIENT_CODE", "");
        SQcustomBillToAddress.put("CLIENT_OWNER", "");
        SQcustomBillToAddress.put("CLIENT_EMAIL", "");
        SQcustomBillToAddress.put("USER_PHONE", "");
        SQcustomBillToAddress.put("BILL_ADDRESS_NAME", "");
        SQcustomBillToAddress.put("CONTACT_JOB_TITLE", "");
        SQcustomBillToAddress.put("CONTACT_EMAIL", "");
        SQcustomBillToAddress.put("CONTACT_MIDDLE_NAME", "");
        SQcustomBillToAddress.put("CLIENT_BANK_IBAN_CODE", "");
        SQcustomBillToAddress.put("MAIL_ADDRESS_NAME", "");
        SQcustomBillToAddress.put("MAIL_ZIPCODE", "");
        SQcustomBillToAddress.put("MAIL_STATE", "");


        //TODO SALES ORDER

        TreeMap<String, String> SOcustomProductTable = new TreeMap<String, String>();
        SOcustomProductTable.put("ITEM_PRODUCT_LOT_NUMBER", "ITEM PRODUCT NUMBER");
        SOcustomProductTable.put("DISCOUNT_TYPE", "");
        SOcustomProductTable.put("PRODUCT_NAME", "");
        SOcustomProductTable.put("UNIT_PRICE_AVERAGE", "");
        SOcustomProductTable.put("ORDERED_PRODUCT_QTY", "ORDERED PRODUCT QUANTITY");
        SOcustomProductTable.put("ITEM_DOUBLE_TAX_RATE", "");
        SOcustomProductTable.put("ITEM_COST_PRICE", "");
        SOcustomProductTable.put("PICK_ITEM_NUMBER_PACKS", "");
        SOcustomProductTable.put("ITEM_PRODUCT_EXPIRATION_DATE", "");
        SOcustomProductTable.put("DISCOUNT_AMOUNT", "");
        SOcustomProductTable.put("ITEM_BATCH_QTY", "ITEM BATCH QUANTITY");
        SOcustomProductTable.put("ITEM_IS_PICKABLE", "");
        SOcustomProductTable.put("NO", "");
        SOcustomProductTable.put("ITEM_DOUBLE_TAX_AMOUNT", "");
        SOcustomProductTable.put("ITEM_RECIEVE", "");
        SOcustomProductTable.put("PICK_ITEM_REFERENCE", "");
        SOcustomProductTable.put("ITEM_QTY", "");
        SOcustomProductTable.put("TAX_RATE", "");
        SOcustomProductTable.put("ITEM_PROFIT_IC", "");
        SOcustomProductTable.put("PARENT_ACCOUNT", "");
        SOcustomProductTable.put("ACCOUNT", "");
        SOcustomProductTable.put("UNIT_PRICE", "");
        SOcustomProductTable.put("QTY_HRS", "QUANTITY");
        SOcustomProductTable.put("DEPARTMENT", "");
        SOcustomProductTable.put("ITEM_TYPE", "");
        SOcustomProductTable.put("TAX_AMOUNT_IN_BASE", "");
        SOcustomProductTable.put("ITEM_TOTAL_SALES_PRICE", "");
        SOcustomProductTable.put("UNIT_MEASUREMENT", "");
        SOcustomProductTable.put("DOUBLE_TAX_LABEL", "");
        SOcustomProductTable.put("ITEM_BARCODE", "");
        SOcustomProductTable.put("UNIT_PRICE_DISCOUNTED", "");
        SOcustomProductTable.put("ITEM_BRAND_NAME", "");
        SOcustomProductTable.put("ITEM_SUB_PROJECT", "");
        SOcustomProductTable.put("TOTAL_AMOUNT_IN_BASE", "");
        SOcustomProductTable.put("ITEM_QUOTE_NUMBER", "");
        SOcustomProductTable.put("ITEM_REVERSED_QTY", "");
        SOcustomProductTable.put("ITEM_BACK_ORDERED", "");
        SOcustomProductTable.put("ITEM_WAREHOUSE", "");
        SOcustomProductTable.put("ITEM_PART_NUMBER", "");
        SOcustomProductTable.put("NET_AMOUNT_IN_BASE", "");
        SOcustomProductTable.put("UNIT_PRICE_IN_BASE", "");
        SOcustomProductTable.put("ATTACHMENTS", "");
        SOcustomProductTable.put("ITEM_CATEGORY", "");
        SOcustomProductTable.put("ITEM_NET_PROFIT", "");
        SOcustomProductTable.put("TAX_AMOUNT", "");
        SOcustomProductTable.put("ITEM_QUOTE_QUANTITY_ORDERED", "");
        SOcustomProductTable.put("ITEM_PICTURE", "");
        SOcustomProductTable.put("TAX_LABEL", "");
        SOcustomProductTable.put("DESCRIPTION", "");
        SOcustomProductTable.put("ITEM_BATCH_EXPIRE_DATE", "");
        SOcustomProductTable.put("TOTAL_AMOUNT", "");
        SOcustomProductTable.put("DISCOUNT", "");
        SOcustomProductTable.put("PICK_ITEM_LAST_SHIPPED_QTY", "PICK ITEM LAST SHIPPED QUANTITY");
        SOcustomProductTable.put("ORDERED_QTY", "ORDERED QUANTITY");
        SOcustomProductTable.put("NAME", "");
        SOcustomProductTable.put("PREV_INVOICES_PRODUCT_QTY", "PREVIOUS INVOICE PRODUCT QUANTITY");
        SOcustomProductTable.put("NET_AMOUNT", "");
        SOcustomProductTable.put("PICK_ITEM_SHIPPED_QTY", "");
        SOcustomProductTable.put("ITEM_PROJECT", "");
        SOcustomProductTable.put("ACCOUNT_NUMBER", "");
        SOcustomProductTable.put("UNIT_MEASUREMENT_DESCRIPTION", "");
        SOcustomProductTable.put("ITEM_ORIGINAL_PRICE", "");
        SOcustomProductTable.put("ITEM_VENDOR", "");
        SOcustomProductTable.put("ITEM_MANUFACTURER", "");
        SOcustomProductTable.put("ITEM_BATCH_SERIAL_NUMBER", "");
        SOcustomProductTable.put("ITEM_PRODUCT_SERIAL", "");
        SOcustomProductTable.put("PICK_ITEM_QTY_PER_PACK", "PICK ITEM QUANTITY PER PACK");
        SOcustomProductTable.put("ITEM_SKU_NUMBER", "ITEM NUMBER");
        SOcustomProductTable.put("ITEM_TOTAL_COST_PRICE", "");
        SOcustomProductTable.put("NET_WITHOUT_DISCOUNT", "");
        SOcustomProductTable.put("ITEM_PRODUCT_LOT_NUMBER_QTY", "");
        SOcustomProductTable.put("QTY_ON_HAND", "");

        TreeMap<String, String> SOcustomTotalTable = new TreeMap<String, String>();
        SOcustomTotalTable.put("DUE_AMOUNT_ARABIC_WORD", "");
        SOcustomTotalTable.put("TOTAL_AMOUNT_AED", "");
        SOcustomTotalTable.put("BTW_MAX_TOTAL", "");
        SOcustomTotalTable.put("INVOICE_QUOTE_UNREC_REVENUE_TOTAL", "");
        SOcustomTotalTable.put("DUE_AMOUNT_WORD", "");
        SOcustomTotalTable.put("TOTAL_UZB_WORD_ALL_LOTIN", "");
        SOcustomTotalTable.put("BILL_EXP_TAX_TOTAL", "");
        SOcustomTotalTable.put("TOTAL_ARABIC_WORD_ALL", "");
        SOcustomTotalTable.put("EXCHANGE_RATE", "");
        SOcustomTotalTable.put("TOTAL_OVERALL_DISCOUNT", "");
        SOcustomTotalTable.put("SUBTOTAL_WORD_ALL", "");
        SOcustomTotalTable.put("BTW_MIN_TOTAL", "");
        SOcustomTotalTable.put("TOTAL", "");
        SOcustomTotalTable.put("TOTAL_IN_BASE", "");
        SOcustomTotalTable.put("TAX_TOTAL", "");
        SOcustomTotalTable.put("EXCHANGE_RATE_AED", "");
        SOcustomTotalTable.put("TOTAL_QUONTITY", "TOTAL QUANTITY");
        SOcustomTotalTable.put("BTW_TOTAL", "");
        SOcustomTotalTable.put("EIND_SUBTOTAL", "");
        SOcustomTotalTable.put("TOTAL_IN_BASE_WORD", "");
        SOcustomTotalTable.put("BILL_EXP_TOTAL", "");
        SOcustomTotalTable.put("SUBTOTAL", "");
        SOcustomTotalTable.put("TOTAL_WORD_ALL", "");
        SOcustomTotalTable.put("EINDTOTAL", "");
        SOcustomTotalTable.put("HAS_BILL_EXP_TOTAL", "");
        SOcustomTotalTable.put("DISCOUNT_TOTAL_WORD_ALL", "");
        SOcustomTotalTable.put("DUE_AMOUNT", "");
        SOcustomTotalTable.put("TOTAL_IN_USD_WORD", "");
        SOcustomTotalTable.put("PAYMENT_TOTAL", "");
        SOcustomTotalTable.put("TOTAL_UZB_WORD_ALL", "");
        SOcustomTotalTable.put("SUBTOTAL_WORD", "");
        SOcustomTotalTable.put("EXCHANGE_RATE_REVERSE", "");
        SOcustomTotalTable.put("SUBTOTAL_IN_BASE", "");
        SOcustomTotalTable.put("TOTAL_WORD_ALL_WITH_CENT", "");
        SOcustomTotalTable.put("LAST_PAYMENT", "");
        SOcustomTotalTable.put("TAX_TOTAL_IN_BASE", "");
        SOcustomTotalTable.put("DISCOUNTED_SUBTOTAL", "");
        SOcustomTotalTable.put("TOTAL_WORD", "");

        TreeMap<String, String> SOcustomNumberAndDatesTable = new TreeMap<String, String>();
        SOcustomNumberAndDatesTable.put("INVOICE_DUE_TERMS", "");
        SOcustomNumberAndDatesTable.put("INV_TYPE", "");
        SOcustomNumberAndDatesTable.put("SHIPPING_METHOD", "");
        SOcustomNumberAndDatesTable.put("FROM_QUOTE_NUMBER", "");
        SOcustomNumberAndDatesTable.put("APPROVER", "");
        SOcustomNumberAndDatesTable.put("REFERENCE", "");
        SOcustomNumberAndDatesTable.put("PREVIEW", "");
        SOcustomNumberAndDatesTable.put("SALE_INVOICE_APPROVER", "");
        SOcustomNumberAndDatesTable.put("PERIOD_DAYS", "");
        SOcustomNumberAndDatesTable.put("COMP_VAT_NUMBER", "");
        SOcustomNumberAndDatesTable.put("INV_DATE", "INVOICE DATE");
        SOcustomNumberAndDatesTable.put("INV_DUE_DATE", "INVOICE DUE DATE");
        SOcustomNumberAndDatesTable.put("CREATION_DATE", "");
        SOcustomNumberAndDatesTable.put("LAST_UPDATER", "");
        SOcustomNumberAndDatesTable.put("QUOTATION_DATE", "");
        SOcustomNumberAndDatesTable.put("PO_NUMBER", "ORDER NUMBER");
        SOcustomNumberAndDatesTable.put("INVOICE_STATUS", "");
        SOcustomNumberAndDatesTable.put("SALE_QUOTE_APPROVER", "");
        SOcustomNumberAndDatesTable.put("LAST_UPDATED_DATE", "");
        SOcustomNumberAndDatesTable.put("INV_NUMBER", "INVOICE NUMBER");
        SOcustomNumberAndDatesTable.put("CUSTOMER_BALANCE", "");
        SOcustomNumberAndDatesTable.put("RECEIPT", "");
        SOcustomNumberAndDatesTable.put("TAX_CODE", "");

        TreeMap<String, String> SOcustomBillToAddress = new TreeMap<String, String>();
        SOcustomBillToAddress.put("CLIENT_CODE", "");
        SOcustomBillToAddress.put("USER_L_NAME", "USER LAST NAME");
        SOcustomBillToAddress.put("USER_M_NAME", "USER MIDDLE NAME");
        SOcustomBillToAddress.put("CONTACT_MIDDLE_NAME", "");
        SOcustomBillToAddress.put("USER_EMAIL", "");
        SOcustomBillToAddress.put("CONTACT_EMAIL", "");
        SOcustomBillToAddress.put("CLIENT_EMAIL", "");
        SOcustomBillToAddress.put("CLIENT_BANK_ACCOUNT_NO", "");
        SOcustomBillToAddress.put("CONTACT_FIRST_NAME", "");
        SOcustomBillToAddress.put("MAIL_STATE", "");
        SOcustomBillToAddress.put("BILL_ADDRESS_NAME", "");
        SOcustomBillToAddress.put("MAIL_COUNTRY_EU_MEMBER", "");
        SOcustomBillToAddress.put("CLIENT_FAX", "");
        SOcustomBillToAddress.put("MAIL_ADDRESS2", "");
        SOcustomBillToAddress.put("USER_PHONE", "");
        SOcustomBillToAddress.put("CLIENT_WEBSITE", "");
        SOcustomBillToAddress.put("ACCOUNT_OWNER", "");
        SOcustomBillToAddress.put("CLIENT_BANK_IBAN_CODE", "");
        SOcustomBillToAddress.put("USER_F_NAME", "");
        SOcustomBillToAddress.put("PARENT_ACCOUNT", "");
        SOcustomBillToAddress.put("BILL_CITY", "");
        SOcustomBillToAddress.put("NAME", "");
        SOcustomBillToAddress.put("BILL_ADDRESS2", "");
        SOcustomBillToAddress.put("CONTACT_LAST_NAME", "");
        SOcustomBillToAddress.put("BILL_STATE", "");
        SOcustomBillToAddress.put("BILL_ADDRESS", "");
        SOcustomBillToAddress.put("USERNAME", "");
        SOcustomBillToAddress.put("CLIENT_PHONE", "");
        SOcustomBillToAddress.put("CLIENT_BANK_ACCOUNT_NAME", "");
        SOcustomBillToAddress.put("CLIENT_BANK_ADDRESS", "");
        SOcustomBillToAddress.put("CLIENT_BANK_SORT_CODE", "");
        SOcustomBillToAddress.put("BILL_COUNTRY", "");
        SOcustomBillToAddress.put("MAIL_CITY", "");
        SOcustomBillToAddress.put("PAYMENT_METHOD", "");
        SOcustomBillToAddress.put("CLIENT_CURRENCY", "");
        SOcustomBillToAddress.put("CLIENT_BANK_NAME", "");
        SOcustomBillToAddress.put("MAIL_COUNTRY", "");
        SOcustomBillToAddress.put("BILL_COUNTRY_EU_MEMBER", "");
        SOcustomBillToAddress.put("MAIL_ZIPCODE", "");
        SOcustomBillToAddress.put("CLIENT_BANK_SWIFT_CODE", "");
        SOcustomBillToAddress.put("MAIL_ADDRESS", "");
        SOcustomBillToAddress.put("CLIENT_REGISTRATION_NUMBER", "");
        SOcustomBillToAddress.put("MAIL_ADDRESS_NAME", "");
        SOcustomBillToAddress.put("CLIENT_TAX_TREATMENT", "");
        SOcustomBillToAddress.put("CLIENT_BANK_BRANCH", "");
        SOcustomBillToAddress.put("CLIENT_VAT_NUMBER", "");
        SOcustomBillToAddress.put("CONTACT_JOB_TITLE", "");
        SOcustomBillToAddress.put("CLIENT_CONTACT", "");
        SOcustomBillToAddress.put("CLIENT_OWNER", "");
        SOcustomBillToAddress.put("CONTACT_PHONE", "");
        SOcustomBillToAddress.put("BILL_ZIPCODE", "");


        //TODO PURCHASE INVOICE

        TreeMap<String, String> PIcustomProductTable = new TreeMap<String, String>();
        PIcustomProductTable.put("ITEM_RECIEVE", "");
        PIcustomProductTable.put("ITEM_BATCH_SERIAL_NUMBER", "");
        PIcustomProductTable.put("ITEM_WAREHOUSE", "");
        PIcustomProductTable.put("PICK_ITEM_REFERENCE", "");
        PIcustomProductTable.put("ITEM_IS_PICKABLE", "");
        PIcustomProductTable.put("ITEM_PRODUCT_LOT_NUMBER_QTY", "");
        PIcustomProductTable.put("DOUBLE_TAX_LABEL", "");
        PIcustomProductTable.put("QTY_ON_HAND", "");
        PIcustomProductTable.put("UNIT_PRICE_IN_BASE", "");
        PIcustomProductTable.put("ITEM_PART_NUMBER", "");
        PIcustomProductTable.put("ITEM_BRAND_NAME", "");
        PIcustomProductTable.put("ITEM_QUOTE_QUANTITY_ORDERED", "");
        PIcustomProductTable.put("ITEM_SUB_PROJECT", "");
        PIcustomProductTable.put("TAX_AMOUNT_IN_BASE", "");
        PIcustomProductTable.put("PARENT_ACCOUNT", "");
        PIcustomProductTable.put("ITEM_NET_PROFIT", "");
        PIcustomProductTable.put("ITEM_QTY", "");
        PIcustomProductTable.put("ITEM_PRODUCT_LOT_NUMBER", "");
        PIcustomProductTable.put("UNIT_MEASUREMENT_DESCRIPTION", "");
        PIcustomProductTable.put("NET_AMOUNT_IN_BASE", "");
        PIcustomProductTable.put("DISCOUNT", "");
        PIcustomProductTable.put("DESCRIPTION", "");
        PIcustomProductTable.put("QTY_HRS", "");
        PIcustomProductTable.put("ITEM_MANUFACTURER", "");
        PIcustomProductTable.put("ITEM_ORIGINAL_PRICE", "");
        PIcustomProductTable.put("ITEM_QUOTE_NUMBER", "");
        PIcustomProductTable.put("ITEM_SKU_NUMBER", "");
        PIcustomProductTable.put("PICK_ITEM_LAST_SHIPPED_QTY", "");
        PIcustomProductTable.put("TAX_RATE", "");
        PIcustomProductTable.put("TOTAL_AMOUNT_IN_BASE", "");
        PIcustomProductTable.put("ITEM_TYPE", "");
        PIcustomProductTable.put("NET_WITHOUT_DISCOUNT", "");
        PIcustomProductTable.put("ITEM_VENDOR", "");
        PIcustomProductTable.put("ITEM_BATCH_EXPIRE_DATE", "");
        PIcustomProductTable.put("ACCOUNT_NUMBER", "");
        PIcustomProductTable.put("UNIT_PRICE", "");
        PIcustomProductTable.put("NO", "");
        PIcustomProductTable.put("PICK_ITEM_QTY_PER_PACK", "");
        PIcustomProductTable.put("ITEM_COST_PRICE", "");
        PIcustomProductTable.put("ITEM_REVERSED_QTY", "");
        PIcustomProductTable.put("ITEM_TOTAL_COST_PRICE", "");
        PIcustomProductTable.put("TOTAL_AMOUNT", "");
        PIcustomProductTable.put("ITEM_PRODUCT_EXPIRATION_DATE", "");
        PIcustomProductTable.put("ITEM_TOTAL_SALES_PRICE", "");
        PIcustomProductTable.put("PREV_INVOICES_PRODUCT_QTY", "");
        PIcustomProductTable.put("ITEM_PROJECT", "");
        PIcustomProductTable.put("ITEM_PRODUCT_SERIAL", "");
        PIcustomProductTable.put("PICK_ITEM_SHIPPED_QTY", "");
        PIcustomProductTable.put("NET_AMOUNT", "");
        PIcustomProductTable.put("UNIT_PRICE_DISCOUNTED", "");
        PIcustomProductTable.put("ITEM_DOUBLE_TAX_RATE", "");
        PIcustomProductTable.put("DEPARTMENT", "");
        PIcustomProductTable.put("ITEM_PROFIT_IC", "ITEM PROFIT");
        PIcustomProductTable.put("DISCOUNT_TYPE", "");
        PIcustomProductTable.put("ORDERED_PRODUCT_QTY", "");
        PIcustomProductTable.put("ITEM_CATEGORY", "");
        PIcustomProductTable.put("ITEM_PICTURE", "");
        PIcustomProductTable.put("UNIT_MEASUREMENT", "");
        PIcustomProductTable.put("NAME", "");
        PIcustomProductTable.put("TAX_LABEL", "");
        PIcustomProductTable.put("PICK_ITEM_NUMBER_PACKS", "");
        PIcustomProductTable.put("TAX_AMOUNT", "");
        PIcustomProductTable.put("ORDERED_QTY", "");
        PIcustomProductTable.put("PRODUCT_NAME", "");
        PIcustomProductTable.put("ACCOUNT", "");
        PIcustomProductTable.put("ITEM_BARCODE", "");
        PIcustomProductTable.put("ITEM_BATCH_QTY", "ITEM BATCH QUANTITY");
        PIcustomProductTable.put("DISCOUNT_AMOUNT", "");
        PIcustomProductTable.put("ITEM_BACK_ORDERED", "");
        PIcustomProductTable.put("UNIT_PRICE_AVERAGE", "");
        PIcustomProductTable.put("ITEM_DOUBLE_TAX_AMOUNT", "");

        TreeMap<String, String> PIcustomTotalTable = new TreeMap<String, String>();
        PIcustomTotalTable.put("PAYMENT_TOTAL", "");
        PIcustomTotalTable.put("LAST_PAYMENT", "");
        PIcustomTotalTable.put("SUBTOTAL_WORD", "");
        PIcustomTotalTable.put("TOTAL_WORD_ALL", "");
        PIcustomTotalTable.put("EINDTOTAL", "");
        PIcustomTotalTable.put("BILL_EXP_TAX_TOTAL", "");
        PIcustomTotalTable.put("TOTAL_IN_BASE", "");
        PIcustomTotalTable.put("TOTAL_QUONTITY", "");
        PIcustomTotalTable.put("TOTAL_UZB_WORD_ALL", "");
        PIcustomTotalTable.put("TAX_TOTAL", "");
        PIcustomTotalTable.put("TOTAL_ARABIC_WORD_ALL", "");
        PIcustomTotalTable.put("SUBTOTAL_IN_BASE", "");
        PIcustomTotalTable.put("BTW_MIN_TOTAL", "");
        PIcustomTotalTable.put("TAX_TOTAL_IN_BASE", "");
        PIcustomTotalTable.put("EXCHANGE_RATE_AED", "");
        PIcustomTotalTable.put("TOTAL_WORD", "");
        PIcustomTotalTable.put("BILL_EXP_TOTAL", "");
        PIcustomTotalTable.put("TOTAL_IN_BASE_WORD", "");
        PIcustomTotalTable.put("BTW_TOTAL", "");
        PIcustomTotalTable.put("TOTAL_UZB_WORD_ALL_LOTIN", "");
        PIcustomTotalTable.put("BTW_MAX_TOTAL", "");
        PIcustomTotalTable.put("TOTAL", "");
        PIcustomTotalTable.put("TOTAL_WORD_ALL_WITH_CENT", "");
        PIcustomTotalTable.put("DUE_AMOUNT_WORD", "");
        PIcustomTotalTable.put("EXCHANGE_RATE_REVERSE", "");
        PIcustomTotalTable.put("DUE_AMOUNT", "");
        PIcustomTotalTable.put("HAS_BILL_EXP_TOTAL", "");
        PIcustomTotalTable.put("TOTAL_OVERALL_DISCOUNT", "");
        PIcustomTotalTable.put("TOTAL_AMOUNT_AED", "");
        PIcustomTotalTable.put("TOTAL_IN_USD_WORD", "");
        PIcustomTotalTable.put("DUE_AMOUNT_ARABIC_WORD", "");
        PIcustomTotalTable.put("SUBTOTAL", "");
        PIcustomTotalTable.put("EIND_SUBTOTAL", "");
        PIcustomTotalTable.put("EXCHANGE_RATE", "");
        PIcustomTotalTable.put("DISCOUNT_TOTAL_WORD_ALL", "");
        PIcustomTotalTable.put("SUBTOTAL_WORD_ALL", "");
        PIcustomTotalTable.put("DISCOUNTED_SUBTOTAL", "");
        PIcustomTotalTable.put("INVOICE_QUOTE_UNREC_REVENUE_TOTAL", "");


        TreeMap<String, String> PIcustomNumberAndDatesTable = new TreeMap<String, String>();
        PIcustomNumberAndDatesTable.put("QUOTATION_DATE", "");
        PIcustomNumberAndDatesTable.put("LAST_UPDATER", "");
        PIcustomNumberAndDatesTable.put("CREATION_DATE", "");
        PIcustomNumberAndDatesTable.put("TAX_CODE", "");
        PIcustomNumberAndDatesTable.put("SHIP_TO_LABEL", "");
        PIcustomNumberAndDatesTable.put("SUPPLIER_LABEL", "");
        PIcustomNumberAndDatesTable.put("INV_DUE_DATE", "");
        PIcustomNumberAndDatesTable.put("INV_NUMBER", "");
        PIcustomNumberAndDatesTable.put("QRCODE", "");
        PIcustomNumberAndDatesTable.put("REFERENCE", "");
        PIcustomNumberAndDatesTable.put("CUSTOMER_BALANCE", "");
        PIcustomNumberAndDatesTable.put("LAST_UPDATED_DATE", "");
        PIcustomNumberAndDatesTable.put("COMP_VAT_NUMBER", "");
        PIcustomNumberAndDatesTable.put("INV_TYPE", "INVOICE TYPE");
        PIcustomNumberAndDatesTable.put("INVOICE_DUE_TERMS", "");
        PIcustomNumberAndDatesTable.put("PREVIEW", "");
        PIcustomNumberAndDatesTable.put("FROM_QUOTE_NUMBER", "");
        PIcustomNumberAndDatesTable.put("INVOICE_STATUS", "");
        PIcustomNumberAndDatesTable.put("PO_NUMBER", "ORDER NUMBER");
        PIcustomNumberAndDatesTable.put("INV_DATE", "INVOICE DATE");
        PIcustomNumberAndDatesTable.put("SALE_INVOICE_APPROVER", "");

        TreeMap<String, String> PIcustomBillToAddress = new TreeMap<String, String>();
        PIcustomBillToAddress.put("CLIENT_BANK_ACCOUNT_NAME", "");
        PIcustomBillToAddress.put("NAME", "");
        PIcustomBillToAddress.put("CLIENT_OWNER", "");
        PIcustomBillToAddress.put("USER_M_NAME", "USER MIDDLE NAME");
        PIcustomBillToAddress.put("CLIENT_FAX", "");
        PIcustomBillToAddress.put("CLIENT_BANK_SORT_CODE", "");
        PIcustomBillToAddress.put("USER_F_NAME", "USER FIRST NAME");
        PIcustomBillToAddress.put("USER_EMAIL", "");
        PIcustomBillToAddress.put("COMP_MAIL_CITY", "");
        PIcustomBillToAddress.put("BILL_CITY", "");
        PIcustomBillToAddress.put("COMP_BILL_COUNTRY", "");
        PIcustomBillToAddress.put("BILL_STATE", "");
        PIcustomBillToAddress.put("BILL_COUNTRY_EU_MEMBER", "");
        PIcustomBillToAddress.put("BILL_ZIPCODE", "");
        PIcustomBillToAddress.put("CONTACT_FIRST_NAME", "");
        PIcustomBillToAddress.put("BILL_COUNTRY", "");
        PIcustomBillToAddress.put("CLIENT_EMAIL", "");
        PIcustomBillToAddress.put("CLIENT_VAT_NUMBER", "");
        PIcustomBillToAddress.put("CLIENT_BANK_BRANCH", "");
        PIcustomBillToAddress.put("CONTACT_MIDDLE_NAME", "");
        PIcustomBillToAddress.put("CLIENT_CURRENCY", "");
        PIcustomBillToAddress.put("COMP_BILL_ZIPCODE", "");
        PIcustomBillToAddress.put("CONTACT_PHONE", "");
        PIcustomBillToAddress.put("COMP_BILL_ADDRESS", "");
        PIcustomBillToAddress.put("CLIENT_WEBSITE", "");
        PIcustomBillToAddress.put("CLIENT_REGISTRATION_NUMBER", "");
        PIcustomBillToAddress.put("COMP_MAIL_STATE", "");
        PIcustomBillToAddress.put("CLIENT_TAX_TREATMENT", "");
        PIcustomBillToAddress.put("CLIENT_BANK_ACCOUNT_NO", "");
        PIcustomBillToAddress.put("CONTACT_JOB_TITLE", "");
        PIcustomBillToAddress.put("WH_PHONE", "WAREHOUSE PHONE");
        PIcustomBillToAddress.put("COMP_MAIL_ADDRESS2", "");
        PIcustomBillToAddress.put("CLIENT_BANK_SWIFT_CODE", "");
        PIcustomBillToAddress.put("ACCOUNT_OWNER", "");
        PIcustomBillToAddress.put("CLIENT_BANK_IBAN_CODE", "");
        PIcustomBillToAddress.put("CLIENT_PHONE", "");
        PIcustomBillToAddress.put("CLIENT_BANK_ADDRESS", "");
        PIcustomBillToAddress.put("PAYMENT_METHOD", "");
        PIcustomBillToAddress.put("PARENT_ACCOUNT", "");
        PIcustomBillToAddress.put("USERNAME", "");
        PIcustomBillToAddress.put("COMP_MAIL_ADDRESS", "");
        PIcustomBillToAddress.put("WH_NAME", "WAREHOUSE NAME");
        PIcustomBillToAddress.put("CLIENT_CONTACT", "");
        PIcustomBillToAddress.put("BILL_ADDRESS", "");
        PIcustomBillToAddress.put("COMP_BILL_STATE", "");
        PIcustomBillToAddress.put("CONTACT_LAST_NAME", "");
        PIcustomBillToAddress.put("CLIENT_CODE", "");
        PIcustomBillToAddress.put("USER_L_NAME", "");
        PIcustomBillToAddress.put("BILL_ADDRESS2", "");
        PIcustomBillToAddress.put("CLIENT_BANK_NAME", "");
        PIcustomBillToAddress.put("COMP_BILL_ADDRESS2", "");
        PIcustomBillToAddress.put("CONTACT_EMAIL", "");
        PIcustomBillToAddress.put("COMP_BILL_CITY", "COMPANY BILL CITY");
        PIcustomBillToAddress.put("COMP_MAIL_ZIPCODE", "COMPANY MAIL ZIPCODE");
        PIcustomBillToAddress.put("USER_PHONE", "");
        PIcustomBillToAddress.put("BILL_ADDRESS_NAME", "");

        //TODO PURCHASE ORDER

        TreeMap<String, String> POcustomProductTable = new TreeMap<String, String>();

        POcustomProductTable.put("QTY_ON_HAND", "QUANTITY ON HAND");
        POcustomProductTable.put("NO", "NUMBER");
        POcustomProductTable.put("ITEM_BATCH_QTY", "ITEM BATCH QUANTITY");
        POcustomProductTable.put("ITEM_BACK_ORDERED", "");
        POcustomProductTable.put("TOTAL_AMOUNT_IN_BASE", "");
        POcustomProductTable.put("ITEM_BRAND_NAME", "");
        POcustomProductTable.put("ITEM_BARCODE", "");
        POcustomProductTable.put("DOUBLE_TAX_LABEL", "");
        POcustomProductTable.put("ITEM_ORIGINAL_PRICE", "");
        POcustomProductTable.put("ACCOUNT", "");
        POcustomProductTable.put("ITEM_TOTAL_COST_PRICE", "");
        POcustomProductTable.put("ORDERED_QTY", "ORDERED QUANTITY");
        POcustomProductTable.put("ITEM_QUOTE_QUANTITY_ORDERED", "");
        POcustomProductTable.put("ITEM_WAREHOUSE", "");
        POcustomProductTable.put("PARENT_ACCOUNT", "");
        POcustomProductTable.put("ITEM_BATCH_EXPIRE_DATE", "");
        POcustomProductTable.put("ITEM_QTY", "");
        POcustomProductTable.put("PICK_ITEM_NUMBER_PACKS", "");
        POcustomProductTable.put("ITEM_DOUBLE_TAX_AMOUNT", "");
        POcustomProductTable.put("UNIT_PRICE", "");
        POcustomProductTable.put("ITEM_TOTAL_SALES_PRICE", "");
        POcustomProductTable.put("TAX_AMOUNT", "");
        POcustomProductTable.put("TAX_RATE", "");
        POcustomProductTable.put("ITEM_PRODUCT_EXPIRATION_DATE", "");
        POcustomProductTable.put("UNIT_MEASUREMENT", "");
        POcustomProductTable.put("ITEM_TYPE", "");
        POcustomProductTable.put("UNIT_MEASUREMENT_DESCRIPTION", "");
        POcustomProductTable.put("PICK_ITEM_REFERENCE", "");
        POcustomProductTable.put("ITEM_QUOTE_NUMBER", "");
        POcustomProductTable.put("ITEM_IS_PICKABLE", "");
        POcustomProductTable.put("UNIT_PRICE_IN_BASE", "");
        POcustomProductTable.put("ITEM_SKU_NUMBER", "");
        POcustomProductTable.put("ITEM_PICTURE", "");
        POcustomProductTable.put("ITEM_SUB_PROJECT", "");
        POcustomProductTable.put("ITEM_CATEGORY", "");
        POcustomProductTable.put("NET_WITHOUT_DISCOUNT", "");
        POcustomProductTable.put("PRODUCT_NAME", "");
        POcustomProductTable.put("ITEM_MANUFACTURER", "");
        POcustomProductTable.put("ITEM_VENDOR", "");
        POcustomProductTable.put("DESCRIPTION", "");
        POcustomProductTable.put("ITEM_REVERSED_QTY", "");
        POcustomProductTable.put("TAX_LABEL", "");
        POcustomProductTable.put("ITEM_RECIEVE", "");
        POcustomProductTable.put("ITEM_PRODUCT_SERIAL", "");
        POcustomProductTable.put("DISCOUNT_AMOUNT", "");
        POcustomProductTable.put("UNIT_PRICE_AVERAGE", "");
        POcustomProductTable.put("TAX_AMOUNT_IN_BASE", "");
        POcustomProductTable.put("ITEM_PRODUCT_LOT_NUMBER", "");
        POcustomProductTable.put("QTY_HRS", "QUANTITY");
        POcustomProductTable.put("ORDERED_PRODUCT_QTY", "ORDERED PRODUCT QUANTITY");
        POcustomProductTable.put("ITEM_DOUBLE_TAX_RATE", "");
        POcustomProductTable.put("UNIT_PRICE_DISCOUNTED", "");
        POcustomProductTable.put("DISCOUNT", "");
        POcustomProductTable.put("PICK_ITEM_LAST_SHIPPED_QTY", "PICK ITEM LAST SHIPPED QUANTITY");
        POcustomProductTable.put("DEPARTMENT", "");
        POcustomProductTable.put("ITEM_NET_PROFIT", "");
        POcustomProductTable.put("ITEM_COST_PRICE", "");
        POcustomProductTable.put("NET_AMOUNT", "");
        POcustomProductTable.put("TOTAL_AMOUNT", "");
        POcustomProductTable.put("NAME", "");
        POcustomProductTable.put("ITEM_PROJECT", "");
        POcustomProductTable.put("DISCOUNT_TYPE", "");
        POcustomProductTable.put("ITEM_PRODUCT_LOT_NUMBER_QTY", "");
        POcustomProductTable.put("PREV_INVOICES_PRODUCT_QTY", "");
        POcustomProductTable.put("ACCOUNT_NUMBER", "");
        POcustomProductTable.put("PICK_ITEM_QTY_PER_PACK", "PICK ITEM QUANTITY PER PACK");
        POcustomProductTable.put("ITEM_PART_NUMBER", "");
        POcustomProductTable.put("NET_AMOUNT_IN_BASE", "");
        POcustomProductTable.put("ITEM_PROFIT_IC", "ITEM_PROFIT");
        POcustomProductTable.put("PICK_ITEM_SHIPPED_QTY", "");

        TreeMap<String, String> POcustomTotalTable = new TreeMap<String, String>();
        POcustomTotalTable.put("EXCHANGE_RATE_REVERSE", "");
        POcustomTotalTable.put("TAX_TOTAL_IN_BASE", "");
        POcustomTotalTable.put("EIND_SUBTOTAL", "");
        POcustomTotalTable.put("BILL_EXP_TAX_TOTAL", "");
        POcustomTotalTable.put("SUBTOTAL", "");
        POcustomTotalTable.put("HAS_BILL_EXP_TOTAL", "");
        POcustomTotalTable.put("BTW_MIN_TOTAL", "");
        POcustomTotalTable.put("TOTAL_WORD_ALL", "");
        POcustomTotalTable.put("DUE_AMOUNT_WORD", "");
        POcustomTotalTable.put("TOTAL_WORD", "");
        POcustomTotalTable.put("TOTAL_IN_BASE_WORD", "");
        POcustomTotalTable.put("SUBTOTAL_IN_BASE", "");
        POcustomTotalTable.put("DISCOUNT_TOTAL_WORD_ALL", "");
        POcustomTotalTable.put("EXCHANGE_RATE_AED", "");
        POcustomTotalTable.put("BTW_MAX_TOTAL", "");
        POcustomTotalTable.put("TOTAL", "");
        POcustomTotalTable.put("TOTAL_OVERALL_DISCOUNT", "");
        POcustomTotalTable.put("DISCOUNTED_SUBTOTAL", "");
        POcustomTotalTable.put("TOTAL_IN_BASE", "");
        POcustomTotalTable.put("SUBTOTAL_WORD", "");
        POcustomTotalTable.put("BTW_TOTAL", "");
        POcustomTotalTable.put("TAX_TOTAL", "");
        POcustomTotalTable.put("SUBTOTAL_WORD_ALL", "");
        POcustomTotalTable.put("TOTAL_IN_USD_WORD", "");
        POcustomTotalTable.put("LAST_PAYMENT", "");
        POcustomTotalTable.put("TOTAL_QUONTITY", "");
        POcustomTotalTable.put("EXCHANGE_RATE", "");
        POcustomTotalTable.put("INVOICE_QUOTE_UNREC_REVENUE_TOTAL", "");
        POcustomTotalTable.put("TOTAL_UZB_WORD_ALL_LOTIN", "");
        POcustomTotalTable.put("DUE_AMOUNT_ARABIC_WORD", "");
        POcustomTotalTable.put("TOTAL_AMOUNT_AED", "");
        POcustomTotalTable.put("BILL_EXP_TOTAL", "");
        POcustomTotalTable.put("DUE_AMOUNT", "");
        POcustomTotalTable.put("TOTAL_UZB_WORD_ALL", "");
        POcustomTotalTable.put("PAYMENT_TOTAL", "");
        POcustomTotalTable.put("TOTAL_WORD_ALL_WITH_CENT", "");
        POcustomTotalTable.put("TOTAL_ARABIC_WORD_ALL", "");
        POcustomTotalTable.put("EINDTOTAL", "");

        TreeMap<String, String> POcustomNumberAndDatesTable = new TreeMap<String, String>();
        POcustomNumberAndDatesTable.put("COMP_VAT_NUMBER", "");
        POcustomNumberAndDatesTable.put("PREVIEW", "");
        POcustomNumberAndDatesTable.put("SHIPPING_TERMS", "");
        POcustomNumberAndDatesTable.put("QRCODE", "");
        POcustomNumberAndDatesTable.put("INVOICE_DUE_TERMS", "");
        POcustomNumberAndDatesTable.put("TAX_CODE", "");
        POcustomNumberAndDatesTable.put("SALE_INVOICE_APPROVER", "");
        POcustomNumberAndDatesTable.put("INV_DUE_DATE", "");
        POcustomNumberAndDatesTable.put("CUSTOMER_BALANCE", "");
        POcustomNumberAndDatesTable.put("INV_DATE", "INVOICE DATE");
        POcustomNumberAndDatesTable.put("PO_NUMBER", "ORDER NUMBER");
        POcustomNumberAndDatesTable.put("REFERENCE", "");
        POcustomNumberAndDatesTable.put("QT_NUMBER", "QUOTE NUMBER");
        POcustomNumberAndDatesTable.put("INVOICE_STATUS", "");
        POcustomNumberAndDatesTable.put("PAYMENT_TERMS", "");
        POcustomNumberAndDatesTable.put("CREATION_DATE", "");
        POcustomNumberAndDatesTable.put("INV_TYPE", "INVOICE TYPE");
        POcustomNumberAndDatesTable.put("QUOTATION_DATE", "");
        POcustomNumberAndDatesTable.put("FROM_QUOTE_NUMBER", "");
        POcustomNumberAndDatesTable.put("LAST_UPDATER", "");
        POcustomNumberAndDatesTable.put("LAST_UPDATED_DATE", "");


        TreeMap<String, String> POcustomBillToAddress = new TreeMap<String, String>();
        POcustomBillToAddress.put("USER_EMAIL", "");
        POcustomBillToAddress.put("CLIENT_TAX_TREATMENT", "");
        POcustomBillToAddress.put("CLIENT_REGISTRATION_NUMBER", "");
        POcustomBillToAddress.put("CLIENT_FAX", "");
        POcustomBillToAddress.put("BILL_STATE", "");
        POcustomBillToAddress.put("BILL_COUNTRY_EU_MEMBER", "");
        POcustomBillToAddress.put("USER_F_NAME", "USER FIRST NAME");
        POcustomBillToAddress.put("CLIENT_PHONE", "");
        POcustomBillToAddress.put("CLIENT_OWNER", "");
        POcustomBillToAddress.put("COMP_MAIL_ADDRESS2", "");
        POcustomBillToAddress.put("CLIENT_BANK_SWIFT_CODE", "");
        POcustomBillToAddress.put("CONTACT_FIRST_NAME", "");
        POcustomBillToAddress.put("BILL_COUNTRY", "");
        POcustomBillToAddress.put("COMP_BILL_ADDRESS", "COMPANY BILL ADDRESS");
        POcustomBillToAddress.put("PARENT_ACCOUNT", "");
        POcustomBillToAddress.put("CLIENT_CONTACT", "");
        POcustomBillToAddress.put("CONTACT_PHONE", "");
        POcustomBillToAddress.put("NAME", "");
        POcustomBillToAddress.put("BILL_ADDRESS2", "");
        POcustomBillToAddress.put("COMP_MAIL_CITY", "");
        POcustomBillToAddress.put("COMP_MAIL_STATE", "");
        POcustomBillToAddress.put("CONTACT_LAST_NAME", "");
        POcustomBillToAddress.put("CONTACT_EMAIL", "");
        POcustomBillToAddress.put("WH_NAME", "WAREHOUSE NAME");
        POcustomBillToAddress.put("CONTACT_MIDDLE_NAME", "");
        POcustomBillToAddress.put("CLIENT_CURRENCY", "");
        POcustomBillToAddress.put("USER_L_NAME", "USER LAST NAME");
        POcustomBillToAddress.put("COMP_BILL_ZIPCODE", "");
        POcustomBillToAddress.put("COMP_MAIL_ADDRESS", "");
        POcustomBillToAddress.put("ACCOUNT_OWNER", "");
        POcustomBillToAddress.put("WH_PHONE", "WAREHOUSE PHONE");
        POcustomBillToAddress.put("CLIENT_EMAIL", "");
        POcustomBillToAddress.put("CLIENT_CODE", "");
        POcustomBillToAddress.put("CLIENT_BANK_ACCOUNT_NO", "");
        POcustomBillToAddress.put("USER_M_NAME", "USER MIDDLE NAME");
        POcustomBillToAddress.put("CLIENT_BANK_NAME", "");
        POcustomBillToAddress.put("COMP_BILL_STATE", "");
        POcustomBillToAddress.put("COMP_BILL_COUNTRY", "");
        POcustomBillToAddress.put("COMP_MAIL_ZIPCODE", "");
        POcustomBillToAddress.put("USER_PHONE", "");
        POcustomBillToAddress.put("CLIENT_WEBSITE", "");
        POcustomBillToAddress.put("BILL_ADDRESS", "");
        POcustomBillToAddress.put("CLIENT_BANK_IBAN_CODE", "");
        POcustomBillToAddress.put("CLIENT_BANK_SORT_CODE", "");
        POcustomBillToAddress.put("BILL_ZIPCODE", "");
        POcustomBillToAddress.put("COMP_BILL_ADDRESS2", "");
        POcustomBillToAddress.put("BILL_CITY", "");
        POcustomBillToAddress.put("CLIENT_BANK_BRANCH", "");
        POcustomBillToAddress.put("COMP_BILL_CITY", "COMPANY BILL CITY");
        POcustomBillToAddress.put("USERNAME", "");
        POcustomBillToAddress.put("BILL_ADDRESS_NAME", "");
        POcustomBillToAddress.put("PAYMENT_METHOD", "");
        POcustomBillToAddress.put("CLIENT_BANK_ACCOUNT_NAME", "");
        POcustomBillToAddress.put("CLIENT_BANK_ADDRESS", "");
        POcustomBillToAddress.put("CONTACT_JOB_TITLE", "");
        POcustomBillToAddress.put("CLIENT_VAT_NUMBER", "");

        //TODO CANDIDATE

        TreeMap<String, String> CandidatecustomNumberAndDatesTable = new TreeMap<String, String>();
        CandidatecustomNumberAndDatesTable.put("LOCATION", "");
        CandidatecustomNumberAndDatesTable.put("CANDIDATE_PROJECT", "");
        CandidatecustomNumberAndDatesTable.put("EXPECTED_SALARY", "");
        CandidatecustomNumberAndDatesTable.put("FIRST_NAME", "");
        CandidatecustomNumberAndDatesTable.put("EMAIL", "");
        CandidatecustomNumberAndDatesTable.put("SKILLS", "");
        CandidatecustomNumberAndDatesTable.put("PHONE", "");
        CandidatecustomNumberAndDatesTable.put("STATUS", "");
        CandidatecustomNumberAndDatesTable.put("VACANCIES", "");
        CandidatecustomNumberAndDatesTable.put("CURRENT_EMPLOYER", "");
        CandidatecustomNumberAndDatesTable.put("LEAD_SOURCE", "");
        CandidatecustomNumberAndDatesTable.put("NUMBER", "");
        CandidatecustomNumberAndDatesTable.put("BIRTH_DAY", "");
        CandidatecustomNumberAndDatesTable.put("LANGUAGE", "");
        CandidatecustomNumberAndDatesTable.put("WORK_EXPERIENCE", "");

        //TODO VACANCY

        TreeMap<String, String> VacancycustomNumberAndDatesTable = new TreeMap<String, String>();
        VacancycustomNumberAndDatesTable.put("JOB_FAMILY", "");
        VacancycustomNumberAndDatesTable.put("RESPONSIBILITIES", "");
        VacancycustomNumberAndDatesTable.put("GENDER", "");
        VacancycustomNumberAndDatesTable.put("PROPOSED_SALARY", "");
        VacancycustomNumberAndDatesTable.put("CONTRACT_PERIOD_LABEL", "");
        VacancycustomNumberAndDatesTable.put("LOCATION", "");
        VacancycustomNumberAndDatesTable.put("START_DATE", "");
        VacancycustomNumberAndDatesTable.put("DESCRIPTION", "");
        VacancycustomNumberAndDatesTable.put("REQUIRED_DEGREE", "");
        VacancycustomNumberAndDatesTable.put("VACANCY_TYPE", "");
        VacancycustomNumberAndDatesTable.put("CONTRACT_FROM", "");
        VacancycustomNumberAndDatesTable.put("JOB_TITLE", "");
        VacancycustomNumberAndDatesTable.put("EMBASSY", "");
        VacancycustomNumberAndDatesTable.put("VACANCY_PLACE_COUNT", "");
        VacancycustomNumberAndDatesTable.put("STATUS", "");
        VacancycustomNumberAndDatesTable.put("END_DATE", "");
        VacancycustomNumberAndDatesTable.put("CONTRACT_TO", "");
        VacancycustomNumberAndDatesTable.put("JOB_TYPE", "");
        VacancycustomNumberAndDatesTable.put("RELIGION", "");
        VacancycustomNumberAndDatesTable.put("MANAGER", "");
        VacancycustomNumberAndDatesTable.put("VACANCY_NUMBER", "");
        VacancycustomNumberAndDatesTable.put("PROJECT_NAME", "");
        VacancycustomNumberAndDatesTable.put("JOB_REQUIREMENTS", "");
        VacancycustomNumberAndDatesTable.put("COUNTRY", "");
        VacancycustomNumberAndDatesTable.put("POSITION", "");


        //TODO PLACEMENT

        TreeMap<String, String> PlacementustomNumberAndDatesTable = new TreeMap<String, String>();
        PlacementustomNumberAndDatesTable.put("ADDRESS", "");
        PlacementustomNumberAndDatesTable.put("CANDIDATE", "");
        PlacementustomNumberAndDatesTable.put("MATCHED_VACANCIES", "");
        PlacementustomNumberAndDatesTable.put("CANDIDATE_EXPECTED_SALARY", "");
        PlacementustomNumberAndDatesTable.put("POSITION", "");
        PlacementustomNumberAndDatesTable.put("TIMESLOT", "");
        PlacementustomNumberAndDatesTable.put("CANDIDATE_EXPECTED_SALARY_IN_WORD_LOTIN", "");
        PlacementustomNumberAndDatesTable.put("LOCATION", "");
        PlacementustomNumberAndDatesTable.put("DATE_OFFERED", "");
        PlacementustomNumberAndDatesTable.put("FULL_NAME", "");
        PlacementustomNumberAndDatesTable.put("DEPARTAMAE", "");
        PlacementustomNumberAndDatesTable.put("CANDIDATE_EXPECTED_SALARY_IN_WORD", "");

        //TODO RENTAL ORDER

        TreeMap<String, String> ROcustomProductTable = new TreeMap<>();
        ROcustomProductTable.put("QTY", "QUANTITY");
        ROcustomProductTable.put("DESCRIPTION", "");
        ROcustomProductTable.put("TAX_RATE", "");
        ROcustomProductTable.put("UNIT_PRICE", "");
        ROcustomProductTable.put("NET_AMOUNT", "");
        ROcustomProductTable.put("PRODUCT", "");
        ROcustomProductTable.put("TOTAL_AMOUNT", "");

        TreeMap<String, String> ROcustomTotalTable = new TreeMap<String, String>();
        ROcustomTotalTable.put("SUBTOTAL", "");
        ROcustomTotalTable.put("TOTAL", "");
        ROcustomTotalTable.put("TAX_TOTAL", "");
        ROcustomTotalTable.put("BASE_TOTAL", "");
        ROcustomTotalTable.put("NET_TOTAL", "");

        TreeMap<String, String> ROcustomNumberAndDatesTable = new TreeMap<String, String>();
        ROcustomNumberAndDatesTable.put("CUSTOMER", "");
        ROcustomNumberAndDatesTable.put("NUMBER", "");
        ROcustomNumberAndDatesTable.put("CLIENT_INVOICE_TERM", "");
        ROcustomNumberAndDatesTable.put("DATE", "");
        ROcustomNumberAndDatesTable.put("TAX_CALC_TYPE", "");
        ROcustomNumberAndDatesTable.put("ITEMS", "");
        ROcustomNumberAndDatesTable.put("ADDITIONAL_INFORMATION", "");

        //TODO RENTAL PRODUCT

        TreeMap<String, String> RPcustomProductTable = new TreeMap<String, String>();
        RPcustomProductTable.put("UNIT", "");
        RPcustomProductTable.put("UNIT_PRICE", "");
        RPcustomProductTable.put("DESCRIPTION", "");

        TreeMap<String, String> RPcustomNumberAndDatesTable = new TreeMap<String, String>();
        RPcustomNumberAndDatesTable.put("TAX", "");
        RPcustomNumberAndDatesTable.put("SALES_ACCOUNT", "");
        RPcustomNumberAndDatesTable.put("OVERTIME_END_DATE", "");
        RPcustomNumberAndDatesTable.put("ADDITIONAL_INFORMATION", "");
        RPcustomNumberAndDatesTable.put("SUPPLIERS", "");
        RPcustomNumberAndDatesTable.put("SALES_PRICE", "");
        RPcustomNumberAndDatesTable.put("ACTIVE", "");
        RPcustomNumberAndDatesTable.put("NUMBER", "");
        RPcustomNumberAndDatesTable.put("IMAGE_UPLOAD", "");
        RPcustomNumberAndDatesTable.put("TYPE", "");
        RPcustomNumberAndDatesTable.put("OVERTIME_HOURS", "");
        RPcustomNumberAndDatesTable.put("BARCODE", "");
        RPcustomNumberAndDatesTable.put("BRAND", "");
        RPcustomNumberAndDatesTable.put("NAME", "");
        RPcustomNumberAndDatesTable.put("CATEGORY", "");
        RPcustomNumberAndDatesTable.put("SECURITY_TIME", "");
        RPcustomNumberAndDatesTable.put("PURCHASE_ACCOUNT", "");
        RPcustomNumberAndDatesTable.put("PURCHASE_PRICE", "");

        //TODO PAYSLIP
        TreeMap<String, String> PcustomProductTable = new TreeMap<>();
        PcustomProductTable.put("AMOUNTS", "");
        PcustomProductTable.put("RATES", "");
        PcustomProductTable.put("UNIT", "");
        PcustomProductTable.put("PAYMENT_", "");
        PcustomProductTable.put("REMARKS", "");
        PcustomProductTable.put("AMOUNTS_BASIC", "");
        PcustomProductTable.put("AMOUNTS_NUMERIC", "");

        TreeMap<String, String> PcustomTotalTable = new TreeMap<String, String>();
        PcustomTotalTable.put("TOTAL_YTD", "");
        PcustomTotalTable.put("TOTAL_OVERTIME", "");
        PcustomTotalTable.put("ADDITIONAL_PAYMENT", "");
        PcustomTotalTable.put("TOTAL_IN_WORDS", "");
        PcustomTotalTable.put("GROSS_SALARY", "");
        PcustomTotalTable.put("TOTAL", "");
        PcustomTotalTable.put("DEDUCTIONS_NUMERIC", "");
        PcustomTotalTable.put("TOTAL_NUMERIC", "");
        PcustomTotalTable.put("TOTAL_IN_BASE", "");
        PcustomTotalTable.put("EXP_TOTAL", "");
        PcustomTotalTable.put("TOTAL_ADDITIONAL", "");
        PcustomTotalTable.put("TOTAL_GROSS_PAY", "");
        PcustomTotalTable.put("DEDUCTIONS", "");
        PcustomTotalTable.put("TOTAL_LIVING", "");

        TreeMap<String, String> PcustomNumberAndDatesTable = new TreeMap<String, String>();
        PcustomNumberAndDatesTable.put("DEDUCTIONS", "");
        PcustomNumberAndDatesTable.put("AMOUNTS", "");
        PcustomNumberAndDatesTable.put("RATES", "");
        PcustomNumberAndDatesTable.put("AMOUNTS_NUMERIC", "");
        PcustomNumberAndDatesTable.put("UNIT", "");
        PcustomNumberAndDatesTable.put("AMOUNTS_BASIC", "");
        PcustomNumberAndDatesTable.put("REMARKS", "");

        TreeMap<String, String> PcustomBillToAddress = new TreeMap<String, String>();
        PcustomBillToAddress.put("TOTAL_GROSS_PAY_LABEL", "");
        PcustomBillToAddress.put("PERIOD", "");
        PcustomBillToAddress.put("PAYMENT_POLICY", "");
        PcustomBillToAddress.put("APPROVED_DATE", "");
        PcustomBillToAddress.put("SALARY_RATE", "");
        PcustomBillToAddress.put("DAYS_OF_MONTH", "");
        PcustomBillToAddress.put("PERIOD_FROM_TO", "");
        PcustomBillToAddress.put("SALARY_RATE_TYPE", "");
        PcustomBillToAddress.put("REGULAR_OVERTIME", "");
        PcustomBillToAddress.put("EXPENSES_LABEL", "");
        PcustomBillToAddress.put("APPROVER_LABEL", "");
        PcustomBillToAddress.put("REFERENCE", "");
        PcustomBillToAddress.put("WORKED_DAYS", "");
        PcustomBillToAddress.put("EXCHANGE_RATE", "");
        PcustomBillToAddress.put("BASE_CURRENCY", "");
        PcustomBillToAddress.put("WEEKEND_OVERTIME", "");
        PcustomBillToAddress.put("CURRENCY_NAME", "");
        PcustomBillToAddress.put("TOTAL_LABEL", "");
        PcustomBillToAddress.put("CREATED_DATE", "");
        PcustomBillToAddress.put("COMMENT", "");
        PcustomBillToAddress.put("Number", "");
        PcustomBillToAddress.put("WPS_NO", "");
        PcustomBillToAddress.put("PAYMENT_METHOD", "");
        PcustomBillToAddress.put("NAME_LABEL", "");
        PcustomBillToAddress.put("TOTALS_THIS_PERIOD_LABEL", "");
        PcustomBillToAddress.put("HOLIDAY_OVERTIME", "");
        PcustomBillToAddress.put("REFERENCE_LABEL", "");
        PcustomBillToAddress.put("DEDUCTIONS_LABEL", "");
        PcustomBillToAddress.put("PAYMENT_LABEL", "");
        PcustomBillToAddress.put("BYPROJECT_TOTAL", "");
        PcustomBillToAddress.put("HOLIDAY_OVERTIME_RATE", "");
        PcustomBillToAddress.put("CODE_LABEL", "");
        PcustomBillToAddress.put("REGULAR_OVERTIME_RATE", "");
        PcustomBillToAddress.put("PERIOD_END_DATE", "");
        PcustomBillToAddress.put("REJECT", "");
        PcustomBillToAddress.put("RESIGNATION_DATE", "");
        PcustomBillToAddress.put("LOCATION", "");
        PcustomBillToAddress.put("WPS_NO_LABEL", "");
        PcustomBillToAddress.put("DRIVER_NUMBER", "");
        PcustomBillToAddress.put("APPROVED_DATE_LABEL", "");
        PcustomBillToAddress.put("PERIOD_START_DATE", "");
        PcustomBillToAddress.put("HIRE_DATE", "");
        PcustomBillToAddress.put("CREATOR", "");
        PcustomBillToAddress.put("BANK_ACCOUNT_NUMBER", "");
        PcustomBillToAddress.put("NAME", "");
        PcustomBillToAddress.put("WEEKEND_OVERTIME_RATE", "");
        PcustomBillToAddress.put("POSITION", "");
        PcustomBillToAddress.put("PROCESSED_DATE", "");
        PcustomBillToAddress.put("APPROVER", "");
        PcustomBillToAddress.put("CREATOR_LABEL", "");
        PcustomBillToAddress.put("PAYMENT_PERIOD_LABEL", "");
        PcustomBillToAddress.put("PROCESSED_DATE_LABEL", "");
        PcustomBillToAddress.put("PAYMENT_PERIOD", "");
        PcustomBillToAddress.put("department", "");
        PcustomBillToAddress.put("IBAN_CODE", "");


        //TODO RECEIVABLE CREDIT NOTE(SALES INOVOICE)
        TreeMap<String, String> RCNcustomProductTable = new TreeMap<String, String>();
        RCNcustomProductTable.put("ORDERED_PRODUCT_QTY", "ORDERED PRODUCT QUANTITY");
        RCNcustomProductTable.put("PREV_INVOICES_PRODUCT_QTY", "");
        RCNcustomProductTable.put("DESCRIPTION", "");
        RCNcustomProductTable.put("ITEM_TOTAL_COST_PRICE", "");
        RCNcustomProductTable.put("ITEM_ORIGINAL_PRICE", "");
        RCNcustomProductTable.put("ITEM_QUOTE_QUANTITY_ORDERED", "");
        RCNcustomProductTable.put("ACCOUNT_NUMBER", "");
        RCNcustomProductTable.put("ITEM_BATCH_EXPIRE_DATE", "");
        RCNcustomProductTable.put("NO", "");
        RCNcustomProductTable.put("TOTAL_AMOUNT", "");
        RCNcustomProductTable.put("NET_AMOUNT", "");
        RCNcustomProductTable.put("ITEM_BARCODE", "");
        RCNcustomProductTable.put("TAX_LABEL", "");
        RCNcustomProductTable.put("ITEM_QUOTE_NUMBER", "");
        RCNcustomProductTable.put("ITEM_PICTURE", "");
        RCNcustomProductTable.put("ITEM_NET_PROFIT", "");
        RCNcustomProductTable.put("UNIT_PRICE_DISCOUNTED", "");
        RCNcustomProductTable.put("NAME", "");
        RCNcustomProductTable.put("UNIT_PRICE_AVERAGE", "");
        RCNcustomProductTable.put("ITEM_BACK_ORDERED", "");
        RCNcustomProductTable.put("ITEM_PRODUCT_SERIAL", "");
        RCNcustomProductTable.put("ITEM_VENDOR", "");
        RCNcustomProductTable.put("ITEM_BRAND_NAME", "");
        RCNcustomProductTable.put("ITEM_CATEGORY", "");
        RCNcustomProductTable.put("DOUBLE_TAX_LABEL", "");
        RCNcustomProductTable.put("ITEM_PROFIT_IC", "ITEM PROFIT");
        RCNcustomProductTable.put("ITEM_CONVERTED_QUOTE_CUSTOM_FIELDS", "");
        RCNcustomProductTable.put("ORDERED_QTY", "ORDERED QUANTITY");
        RCNcustomProductTable.put("TAX_AMOUNT", "");
        RCNcustomProductTable.put("UNIT_PRICE_IN_BASE", "");
        RCNcustomProductTable.put("TAX_RATE", "");
        RCNcustomProductTable.put("ITEM_PART_NUMBER", "");
        RCNcustomProductTable.put("ITEM_PRODUCT_LOT_NUMBER_QTY", "");
        RCNcustomProductTable.put("ACCOUNT", "");
        RCNcustomProductTable.put("NET_WITHOUT_DISCOUNT", "");
        RCNcustomProductTable.put("ITEM_MANUFACTURER", "");
        RCNcustomProductTable.put("ITEM_REVERSED_QTY", "");
        RCNcustomProductTable.put("ITEM_TYPE", "");
        RCNcustomProductTable.put("ITEM_PRODUCT_EXPIRATION_DATE", "");
        RCNcustomProductTable.put("DISCOUNT_TYPE", "");
        RCNcustomProductTable.put("PICK_ITEM_NUMBER_PACKS", "");
        RCNcustomProductTable.put("PARENT_ACCOUNT", "");
        RCNcustomProductTable.put("ITEM_BATCH_QTY", "");
        RCNcustomProductTable.put("DISCOUNT_AMOUNT", "");
        RCNcustomProductTable.put("PICK_ITEM_LAST_SHIPPED_QTY", "");
        RCNcustomProductTable.put("PRODUCT_NAME", "");
        RCNcustomProductTable.put("NET_AMOUNT_IN_BASE", "");
        RCNcustomProductTable.put("ITEM_SKU_NUMBER", "");
        RCNcustomProductTable.put("DISCOUNT", "");
        RCNcustomProductTable.put("ITEM_DOUBLE_TAX_AMOUNT", "");
        RCNcustomProductTable.put("ITEM_TOTAL_SALES_PRICE", "");
        RCNcustomProductTable.put("ITEM_QTY", "");
        RCNcustomProductTable.put("ITEM_DOUBLE_TAX_RATE", "");
        RCNcustomProductTable.put("PICK_ITEM_QTY_PER_PACK", "");
        RCNcustomProductTable.put("UNIT_PRICE", "");
        RCNcustomProductTable.put("UNIT_MEASUREMENT_DESCRIPTION", "");
        RCNcustomProductTable.put("PICK_ITEM_REFERENCE", "");
        RCNcustomProductTable.put("PICK_ITEM_SHIPPED_QTY", "");
        RCNcustomProductTable.put("ITEM_COST_PRICE", "");
        RCNcustomProductTable.put("UNIT_MEASUREMENT", "");
        RCNcustomProductTable.put("ITEM_WAREHOUSE", "");
        RCNcustomProductTable.put("ITEM_IS_PICKABLE", "");
        RCNcustomProductTable.put("ITEM_PRODUCT_LOT_NUMBER", "");
        RCNcustomProductTable.put("ITEM_RECIEVE", "");
        RCNcustomProductTable.put("QTY_HRS", "QUANTITY");
        RCNcustomProductTable.put("TAX_AMOUNT_IN_BASE", "");
        RCNcustomProductTable.put("DEPARTMENT", "");
        RCNcustomProductTable.put("ITEM_SUB_PROJECT", "");
        RCNcustomProductTable.put("ITEM_BATCH_SERIAL_NUMBER", "");
        RCNcustomProductTable.put("TOTAL_AMOUNT_IN_BASE", "");
        RCNcustomProductTable.put("ITEM_PROJECT", "");
        RCNcustomProductTable.put("QTY_ON_HAND", "QUANTITY ON HAND");

        TreeMap<String, String> RCNcustomTotalTable = new TreeMap<String, String>();
        RCNcustomTotalTable.put("TAX_TOTAL_IN_BASE", "");
        RCNcustomTotalTable.put("SUBTOTAL_WORD", "");
        RCNcustomTotalTable.put("TOTAL_PROFIT_AMOUNT", "");
        RCNcustomTotalTable.put("TOTAL_UZB_WORD_ALL_LOTIN", "");
        RCNcustomTotalTable.put("INVOICE_QUOTE_UNREC_REVENUE_TOTAL", "");
        RCNcustomTotalTable.put("EXCHANGE_RATE", "");
        RCNcustomTotalTable.put("EIND_SUBTOTAL", "");
        RCNcustomTotalTable.put("DUE_AMOUNT_ARABIC_WORD", "");
        RCNcustomTotalTable.put("EXCHANGE_RATE_AED", "");
        RCNcustomTotalTable.put("TOTAL_ARABIC_WORD_ALL", "");
        RCNcustomTotalTable.put("BTW_TOTAL", "");
        RCNcustomTotalTable.put("SUBTOTAL_IN_BASE", "");
        RCNcustomTotalTable.put("TOTAL_IN_USD_WORD", "");
        RCNcustomTotalTable.put("BTW_MIN_TOTAL", "");
        RCNcustomTotalTable.put("TOTAL_QUONTITY", "");
        RCNcustomTotalTable.put("DISCOUNTED_SUBTOTAL", "");
        RCNcustomTotalTable.put("SUBTOTAL_WORD_ALL", "");
        RCNcustomTotalTable.put("DUE_AMOUNT_WORD", "");
        RCNcustomTotalTable.put("PAYMENT_TOTAL", "");
        RCNcustomTotalTable.put("TOTAL_OVERALL_DISCOUNT", "");
        RCNcustomTotalTable.put("EINDTOTAL", "");
        RCNcustomTotalTable.put("TOTAL", "");
        RCNcustomTotalTable.put("BTW_MAX_TOTAL", "");
        RCNcustomTotalTable.put("LAST_PAYMENT", "");
        RCNcustomTotalTable.put("TOTAL_WORD", "");
        RCNcustomTotalTable.put("SUBTOTAL", "");
        RCNcustomTotalTable.put("TOTAL_WORD_ALL_WITH_CENT", "");
        RCNcustomTotalTable.put("BILL_EXP_TAX_TOTAL", "");
        RCNcustomTotalTable.put("TOTAL_UZB_WORD_ALL", "");
        RCNcustomTotalTable.put("HAS_BILL_EXP_TOTAL", "");
        RCNcustomTotalTable.put("TOTAL_IN_BASE_WORD", "");
        RCNcustomTotalTable.put("DISCOUNT_TOTAL_WORD_ALL", "");
        RCNcustomTotalTable.put("TOTAL_IN_BASE", "");
        RCNcustomTotalTable.put("TOTAL_WORD_ALL", "");
        RCNcustomTotalTable.put("DUE_AMOUNT", "");
        RCNcustomTotalTable.put("BILL_EXP_TOTAL", "");
        RCNcustomTotalTable.put("TOTAL_AMOUNT_AED", "");
        RCNcustomTotalTable.put("TAX_TOTAL", "");
        RCNcustomTotalTable.put("EXCHANGE_RATE_REVERSE", "");

        TreeMap<String, String> RCNcustomNumberAndDatesTable = new TreeMap<String, String>();
        RCNcustomNumberAndDatesTable.put("QUOTATION_DATE", "");
        RCNcustomNumberAndDatesTable.put("CREATION_DATE", "");
        RCNcustomNumberAndDatesTable.put("PO_NUMBER", "ORDER NUMBER");
        RCNcustomNumberAndDatesTable.put("INV_DATE", "INVOICE DATE");
        RCNcustomNumberAndDatesTable.put("CUSTOMER_BALANCE", "");
        RCNcustomNumberAndDatesTable.put("INV_NUMBER", "INVOICE NUMBER");
        RCNcustomNumberAndDatesTable.put("INV_DUE_DATE", "INVOICE DUE DATE");
        RCNcustomNumberAndDatesTable.put("RELATED_INVOICE_NUMBER", "");
        RCNcustomNumberAndDatesTable.put("TAX_CODE", "");
        RCNcustomNumberAndDatesTable.put("LAST_UPDATED_DATE", "");
        RCNcustomNumberAndDatesTable.put("QRCODE", "");
        RCNcustomNumberAndDatesTable.put("INV_TYPE", "INVOICE TYPE");
        RCNcustomNumberAndDatesTable.put("REFERENCE", "");
        RCNcustomNumberAndDatesTable.put("COMP_VAT_NUMBER", "");
        RCNcustomNumberAndDatesTable.put("FROM_QUOTE_NUMBER", "");
        RCNcustomNumberAndDatesTable.put("PREVIEW", "");
        RCNcustomNumberAndDatesTable.put("LAST_UPDATER", "");
        RCNcustomNumberAndDatesTable.put("RELATED_INVOICE_DATE", "");

        TreeMap<String, String> RCNcustomBillToAddress = new TreeMap<String, String>();
        RCNcustomBillToAddress.put("MAIL_ZIPCODE", "");
        RCNcustomBillToAddress.put("CLIENT_WEBSITE", "");
        RCNcustomBillToAddress.put("BILL_COUNTRY_EU_MEMBER", "");
        RCNcustomBillToAddress.put("BILL_STATE", "");
        RCNcustomBillToAddress.put("CLIENT_BANK_BRANCH", "");
        RCNcustomBillToAddress.put("BILL_ZIPCODE", "");
        RCNcustomBillToAddress.put("MAIL_ADDRESS", "");
        RCNcustomBillToAddress.put("CLIENT_BANK_IBAN_CODE", "");
        RCNcustomBillToAddress.put("CLIENT_CODE", "");
        RCNcustomBillToAddress.put("MAIL_COUNTRY_EU_MEMBER", "");
        RCNcustomBillToAddress.put("CLIENT_CONTACT", "");
        RCNcustomBillToAddress.put("CONTACT_MIDDLE_NAME", "");
        RCNcustomBillToAddress.put("CLIENT_CURRENCY", "");
        RCNcustomBillToAddress.put("CLIENT_BANK_NAME", "");
        RCNcustomBillToAddress.put("MAIL_ADDRESS_NAME", "");
        RCNcustomBillToAddress.put("CONTACT_FIRST_NAME", "");
        RCNcustomBillToAddress.put("CLIENT_BANK_SWIFT_CODE", "");
        RCNcustomBillToAddress.put("MAIL_COUNTRY", "");
        RCNcustomBillToAddress.put("USER_EMAIL", "");
        RCNcustomBillToAddress.put("MAIL_STATE", "");
        RCNcustomBillToAddress.put("CLIENT_BANK_ADDRESS", "");
        RCNcustomBillToAddress.put("CLIENT_VAT_NUMBER", "");
        RCNcustomBillToAddress.put("USERNAME", "");
        RCNcustomBillToAddress.put("CLIENT_FAX", "");
        RCNcustomBillToAddress.put("PAYMENT_METHOD", "");
        RCNcustomBillToAddress.put("CONTACT_PHONE", "");
        RCNcustomBillToAddress.put("CLIENT_TAX_TREATMENT", "");
        RCNcustomBillToAddress.put("PARENT_ACCOUNT", "");
        RCNcustomBillToAddress.put("BILL_ADDRESS2", "");
        RCNcustomBillToAddress.put("USER_PHONE", "");
        RCNcustomBillToAddress.put("MAIL_ADDRESS2", "");
        RCNcustomBillToAddress.put("USER_F_NAME", "");
        RCNcustomBillToAddress.put("CLIENT_PHONE", "");
        RCNcustomBillToAddress.put("CLIENT_OWNER", "");
        RCNcustomBillToAddress.put("MAIL_CITY", "");
        RCNcustomBillToAddress.put("CONTACT_LAST_NAME", "");
        RCNcustomBillToAddress.put("NAME", "");
        RCNcustomBillToAddress.put("CLIENT_BANK_SORT_CODE", "");
        RCNcustomBillToAddress.put("BILL_ADDRESS_NAME", "");
        RCNcustomBillToAddress.put("CLIENT_BANK_ACCOUNT_NAME", "");
        RCNcustomBillToAddress.put("CONTACT_EMAIL", "");
        RCNcustomBillToAddress.put("CONTACT_JOB_TITLE", "");
        RCNcustomBillToAddress.put("USER_M_NAME", "");
        RCNcustomBillToAddress.put("CLIENT_REGISTRATION_NUMBER", "");
        RCNcustomBillToAddress.put("ACCOUNT_OWNER", "");
        RCNcustomBillToAddress.put("BILL_COUNTRY", "");
        RCNcustomBillToAddress.put("CLIENT_BANK_ACCOUNT_NO", "");
        RCNcustomBillToAddress.put("USER_L_NAME", "");
        RCNcustomBillToAddress.put("BILL_CITY", "");
        RCNcustomBillToAddress.put("BILL_ADDRESS", "");
        RCNcustomBillToAddress.put("CLIENT_EMAIL", "");

        //TODO PAYABLE CREDIT NOTE
        TreeMap<String, String> PCNcustomProductTable = new TreeMap<String, String>();
        PCNcustomProductTable.put("ITEM_QUOTE_NUMBER", "");
        PCNcustomProductTable.put("UNIT_MEASUREMENT", "");
        PCNcustomProductTable.put("ITEM_QUOTE_QUANTITY_ORDERED", "");
        PCNcustomProductTable.put("ITEM_PICTURE", "");
        PCNcustomProductTable.put("UNIT_PRICE_DISCOUNTED", "");
        PCNcustomProductTable.put("ITEM_SKU_NUMBER", "");
        PCNcustomProductTable.put("ITEM_QTY", "");
        PCNcustomProductTable.put("PICK_ITEM_LAST_SHIPPED_QTY", "");
        PCNcustomProductTable.put("ITEM_NET_PROFIT", "");
        PCNcustomProductTable.put("TAX_AMOUNT_IN_BASE", "");
        PCNcustomProductTable.put("ITEM_TOTAL_COST_PRICE", "");
        PCNcustomProductTable.put("ORDERED_PRODUCT_QTY", "");
        PCNcustomProductTable.put("PICK_ITEM_QTY_PER_PACK", "");
        PCNcustomProductTable.put("NET_AMOUNT_IN_BASE", "");
        PCNcustomProductTable.put("ITEM_MANUFACTURER", "");
        PCNcustomProductTable.put("ITEM_DOUBLE_TAX_AMOUNT", "");
        PCNcustomProductTable.put("UNIT_PRICE", "");
        PCNcustomProductTable.put("ITEM_SUB_PROJECT", "");
        PCNcustomProductTable.put("PREV_INVOICES_PRODUCT_QTY", "");
        PCNcustomProductTable.put("ITEM_PRODUCT_EXPIRATION_DATE", "");
        PCNcustomProductTable.put("ITEM_BACK_ORDERED", "");
        PCNcustomProductTable.put("NO", "");
        PCNcustomProductTable.put("ITEM_TYPE", "");
        PCNcustomProductTable.put("TAX_AMOUNT", "");
        PCNcustomProductTable.put("ACCOUNT", "");
        PCNcustomProductTable.put("ITEM_PRODUCT_SERIAL", "");
        PCNcustomProductTable.put("QTY_ON_HAND", "QUANTITY ON HAND");
        PCNcustomProductTable.put("ITEM_VENDOR", "");
        PCNcustomProductTable.put("NET_WITHOUT_DISCOUNT", "");
        PCNcustomProductTable.put("NAME", "");
        PCNcustomProductTable.put("ITEM_IS_PICKABLE", "");
        PCNcustomProductTable.put("ITEM_CONVERTED_QUOTE_CUSTOM_FIELDS", "");
        PCNcustomProductTable.put("ITEM_CATEGORY", "");
        PCNcustomProductTable.put("TAX_LABEL", "");
        PCNcustomProductTable.put("ORDERED_QTY", "");
        PCNcustomProductTable.put("PRODUCT_NAME", "");
        PCNcustomProductTable.put("PICK_ITEM_NUMBER_PACKS", "");
        PCNcustomProductTable.put("ITEM_BATCH_QTY", "");
        PCNcustomProductTable.put("DEPARTMENT", "");
        PCNcustomProductTable.put("DESCRIPTION", "");
        PCNcustomProductTable.put("ITEM_REVERSED_QTY", "");
        PCNcustomProductTable.put("UNIT_MEASUREMENT_DESCRIPTION", "");
        PCNcustomProductTable.put("ITEM_WAREHOUSE", "");
        PCNcustomProductTable.put("TAX_RATE", "");
        PCNcustomProductTable.put("TOTAL_AMOUNT_IN_BASE", "");
        PCNcustomProductTable.put("ACCOUNT_NUMBER", "");
        PCNcustomProductTable.put("TOTAL_AMOUNT", "");
        PCNcustomProductTable.put("ITEM_DOUBLE_TAX_RATE", "");
        PCNcustomProductTable.put("DISCOUNT", "");
        PCNcustomProductTable.put("UNIT_PRICE_IN_BASE", "");
        PCNcustomProductTable.put("ITEM_PRODUCT_LOT_NUMBER", "");
        PCNcustomProductTable.put("ITEM_RECIEVE", "");
        PCNcustomProductTable.put("PICK_ITEM_SHIPPED_QTY", "");
        PCNcustomProductTable.put("ITEM_BATCH_SERIAL_NUMBER", "");
        PCNcustomProductTable.put("ITEM_TOTAL_SALES_PRICE", "");
        PCNcustomProductTable.put("ITEM_COST_PRICE", "");
        PCNcustomProductTable.put("ITEM_BATCH_EXPIRE_DATE", "");
        PCNcustomProductTable.put("ITEM_PRODUCT_LOT_NUMBER_QTY", "");
        PCNcustomProductTable.put("DISCOUNT_AMOUNT", "");
        PCNcustomProductTable.put("DISCOUNT_TYPE", "");
        PCNcustomProductTable.put("PICK_ITEM_REFERENCE", "");
        PCNcustomProductTable.put("ITEM_PROJECT", "");
        PCNcustomProductTable.put("ITEM_ORIGINAL_PRICE", "");
        PCNcustomProductTable.put("ITEM_PART_NUMBER", "");
        PCNcustomProductTable.put("DOUBLE_TAX_LABEL", "");
        PCNcustomProductTable.put("NET_AMOUNT", "");
        PCNcustomProductTable.put("ITEM_BARCODE", "");
        PCNcustomProductTable.put("QTY_HRS", "");
        PCNcustomProductTable.put("PARENT_ACCOUNT", "");
        PCNcustomProductTable.put("ITEM_BRAND_NAME", "");
        PCNcustomProductTable.put("UNIT_PRICE_AVERAGE", "");
        PCNcustomProductTable.put("ITEM_PROFIT_IC", "");


        TreeMap<String, String> PCNcustomTotalTable = new TreeMap<String, String>();
        PCNcustomTotalTable.put("BILL_EXP_TAX_TOTAL", "");
        PCNcustomTotalTable.put("TOTAL_IN_BASE_WORD", "");
        PCNcustomTotalTable.put("EXCHANGE_RATE_REVERSE", "");
        PCNcustomTotalTable.put("SUBTOTAL", "");
        PCNcustomTotalTable.put("TOTAL_UZB_WORD_ALL_LOTIN", "");
        PCNcustomTotalTable.put("TOTAL_WORD", "");
        PCNcustomTotalTable.put("TAX_TOTAL_IN_BASE", "");
        PCNcustomTotalTable.put("DUE_AMOUNT_ARABIC_WORD", "");
        PCNcustomTotalTable.put("TOTAL_ARABIC_WORD_ALL", "");
        PCNcustomTotalTable.put("BILL_EXP_TOTAL", "");
        PCNcustomTotalTable.put("HAS_BILL_EXP_TOTAL", "");
        PCNcustomTotalTable.put("TOTAL_QUONTITY", "");
        PCNcustomTotalTable.put("PAYMENT_TOTAL", "");
        PCNcustomTotalTable.put("EIND_SUBTOTAL", "");
        PCNcustomTotalTable.put("TOTAL_OVERALL_DISCOUNT", "");
        PCNcustomTotalTable.put("EINDTOTAL", "");
        PCNcustomTotalTable.put("TOTAL_IN_USD_WORD", "");
        PCNcustomTotalTable.put("LAST_PAYMENT", "");
        PCNcustomTotalTable.put("EXCHANGE_RATE_AED", "");
        PCNcustomTotalTable.put("TOTAL_AMOUNT_AED", "");
        PCNcustomTotalTable.put("TAX_TOTAL", "");
        PCNcustomTotalTable.put("DUE_AMOUNT", "");
        PCNcustomTotalTable.put("TOTAL_IN_BASE", "");
        PCNcustomTotalTable.put("SUBTOTAL_IN_BASE", "");
        PCNcustomTotalTable.put("TOTAL", "");
        PCNcustomTotalTable.put("TOTAL_WORD_ALL", "");
        PCNcustomTotalTable.put("DUE_AMOUNT_WORD", "");
        PCNcustomTotalTable.put("DISCOUNT_TOTAL_WORD_ALL", "");
        PCNcustomTotalTable.put("EXCHANGE_RATE", "");
        PCNcustomTotalTable.put("INVOICE_QUOTE_UNREC_REVENUE_TOTAL", "");
        PCNcustomTotalTable.put("DISCOUNTED_SUBTOTAL", "");
        PCNcustomTotalTable.put("TOTAL_WORD_ALL_WITH_CENT", "");
        PCNcustomTotalTable.put("BTW_MIN_TOTAL", "");
        PCNcustomTotalTable.put("BTW_MAX_TOTAL", "");
        PCNcustomTotalTable.put("BTW_TOTAL", "");
        PCNcustomTotalTable.put("TOTAL_UZB_WORD_ALL", "");
        PCNcustomTotalTable.put("TOTAL_PROFIT_AMOUNT", "");
        PCNcustomTotalTable.put("SUBTOTAL_WORD_ALL", "");
        PCNcustomTotalTable.put("SUBTOTAL_WORD", "");

        TreeMap<String, String> PCNcustomNumberAndDatesTable = new TreeMap<String, String>();
        PCNcustomNumberAndDatesTable.put("RELATED_INVOICE_NUMBER", "");
        PCNcustomNumberAndDatesTable.put("QUOTATION_DATE", "");
        PCNcustomNumberAndDatesTable.put("INV_TYPE", "INVOICE TYPE");
        PCNcustomNumberAndDatesTable.put("INV_DUE_DATE", "INVOICE DUE DATE");
        PCNcustomNumberAndDatesTable.put("RELATED_INVOICE_DATE", "");
        PCNcustomNumberAndDatesTable.put("INV_DATE", "INVOICE DATE");
        PCNcustomNumberAndDatesTable.put("COMP_VAT_NUMBER", "VAT NUMBER");
        PCNcustomNumberAndDatesTable.put("INV_NUMBER", "INVOICE NUMBER");
        PCNcustomNumberAndDatesTable.put("CREATION_DATE", "");
        PCNcustomNumberAndDatesTable.put("PREVIEW", "");
        PCNcustomNumberAndDatesTable.put("CUSTOMER_BALANCE", "");
        PCNcustomNumberAndDatesTable.put("REFERENCE", "");
        PCNcustomNumberAndDatesTable.put("QRCODE", "");
        PCNcustomNumberAndDatesTable.put("LAST_UPDATED_DATE", "");
        PCNcustomNumberAndDatesTable.put("PO_NUMBER", "ORDER NUMBER");
        PCNcustomNumberAndDatesTable.put("FROM_QUOTE_NUMBER", "");
        PCNcustomNumberAndDatesTable.put("TAX_CODE", "");
        PCNcustomNumberAndDatesTable.put("LAST_UPDATER", "");


        TreeMap<String, String> PCNcustomBillToAddress = new TreeMap<String, String>();
        PCNcustomBillToAddress.put("COMP_BILL_STATE", "");
        PCNcustomBillToAddress.put("COMP_BILL_ZIPCODE", "");
        PCNcustomBillToAddress.put("CONTACT_LAST_NAME", "");
        PCNcustomBillToAddress.put("CLIENT_BANK_ACCOUNT_NO", "");
        PCNcustomBillToAddress.put("CONTACT_JOB_TITLE", "");
        PCNcustomBillToAddress.put("CONTACT_MIDDLE_NAME", "");
        PCNcustomBillToAddress.put("NAME", "");
        PCNcustomBillToAddress.put("CONTACT_PHONE", "");
        PCNcustomBillToAddress.put("BILL_STATE", "");
        PCNcustomBillToAddress.put("USERNAME", "");
        PCNcustomBillToAddress.put("BILL_ZIPCODE", "");
        PCNcustomBillToAddress.put("CLIENT_VAT_NUMBER", "");
        PCNcustomBillToAddress.put("PARENT_ACCOUNT", "");
        PCNcustomBillToAddress.put("BILL_ADDRESS2", "");
        PCNcustomBillToAddress.put("CLIENT_BANK_ADDRESS", "");
        PCNcustomBillToAddress.put("BILL_COUNTRY", "");
        PCNcustomBillToAddress.put("COMP_MAIL_STATE", "");
        PCNcustomBillToAddress.put("USER_EMAIL", "");
        PCNcustomBillToAddress.put("COMP_BILL_CITY", "");
        PCNcustomBillToAddress.put("COMP_BILL_ADDRESS2", "");
        PCNcustomBillToAddress.put("CLIENT_FAX", "");
        PCNcustomBillToAddress.put("USER_M_NAME", "USER MIDDLE NAME");
        PCNcustomBillToAddress.put("COMP_BILL_COUNTRY", "");
        PCNcustomBillToAddress.put("CLIENT_PHONE", "");
        PCNcustomBillToAddress.put("COMP_MAIL_ADDRESS2", "");
        PCNcustomBillToAddress.put("BILL_ADDRESS_NAME", "");
        PCNcustomBillToAddress.put("CLIENT_TAX_TREATMENT", "");
        PCNcustomBillToAddress.put("CLIENT_CURRENCY", "");
        PCNcustomBillToAddress.put("CLIENT_BANK_BRANCH", "");
        PCNcustomBillToAddress.put("CLIENT_BANK_SWIFT_CODE", "");
        PCNcustomBillToAddress.put("CLIENT_BANK_NAME", "");
        PCNcustomBillToAddress.put("CONTACT_FIRST_NAME", "");
        PCNcustomBillToAddress.put("USER_PHONE", "");
        PCNcustomBillToAddress.put("COMP_MAIL_CITY", "");
        PCNcustomBillToAddress.put("CLIENT_CODE", "");
        PCNcustomBillToAddress.put("CLIENT_EMAIL", "");
        PCNcustomBillToAddress.put("ACCOUNT_OWNER", "");
        PCNcustomBillToAddress.put("COMP_MAIL_ZIPCODE", "");
        PCNcustomBillToAddress.put("COMP_MAIL_ADDRESS", "");
        PCNcustomBillToAddress.put("COMP_BILL_ADDRESS", "");
        PCNcustomBillToAddress.put("PAYMENT_METHOD", "");
        PCNcustomBillToAddress.put("CLIENT_BANK_SORT_CODE", "");
        PCNcustomBillToAddress.put("CLIENT_REGISTRATION_NUMBER", "");
        PCNcustomBillToAddress.put("USER_F_NAME", "");
        PCNcustomBillToAddress.put("CONTACT_EMAIL", "");
        PCNcustomBillToAddress.put("CLIENT_WEBSITE", "");
        PCNcustomBillToAddress.put("BILL_ADDRESS", "");
        PCNcustomBillToAddress.put("CLIENT_CONTACT", "");
        PCNcustomBillToAddress.put("CLIENT_OWNER", "");
        PCNcustomBillToAddress.put("CLIENT_BANK_ACCOUNT_NAME", "");
        PCNcustomBillToAddress.put("BILL_COUNTRY_EU_MEMBER", "");
        PCNcustomBillToAddress.put("BILL_CITY", "");
        PCNcustomBillToAddress.put("CLIENT_BANK_IBAN_CODE", "");
        PCNcustomBillToAddress.put("USER_L_NAME", "USER LAST NAME");


        //TODO TASK

        //TASK_ASSIGNEES
        TreeMap<String, String> taskAssignees = new TreeMap<>();
        taskAssignees.put("EMPLOYEE_CODE", "");
        taskAssignees.put("TIME_SPENT", "");
        taskAssignees.put("ASSIGNEE_STATUS", "");
        taskAssignees.put("EMPLOYEE_NAME", "");

        //TASK_TIME_ENTRY_MEMBERS_TABLE
        TreeMap<String, String> taskTimeEntryMembers = new TreeMap<>();
        taskTimeEntryMembers.put("EMPLOYEE_NAME", "");
        taskTimeEntryMembers.put("EMPLOYEE_TASK_COMMENT", "");
        taskTimeEntryMembers.put("EMPLOYEE_TIME_SPENT", "");
        taskTimeEntryMembers.put("EMPLOYEE_TASK_NAME", "");
        taskTimeEntryMembers.put("EMPLOYEE_TASK_TIME_ENTRY_DATE", "");
        taskTimeEntryMembers.put("STATUS", "");

        //NOTES_TABLE

        TreeMap<String, String> taskNotesTable = new TreeMap<>();
        taskNotesTable.put("NOTE_DATE", "");
        taskNotesTable.put("NOTE_PUBLISHED_BY", "");
        taskNotesTable.put("NOTE_SUBJECT", "");


        TreeMap<String, String> localizeLabels = new TreeMap<>();
        localizeLabels.put("ESTIMATED_TIME_LABEL", "");
        localizeLabels.put("DUE_DATE_LABEL", "");
        localizeLabels.put("TIME_SPENT_LABEL", "");
        localizeLabels.put("PRIORITY_LABEL", "");
        localizeLabels.put("TASK_INFORMATION_LABEL", "");
        localizeLabels.put("DESCRIPTION_LABEL", "");
        localizeLabels.put("ASSIGNEES_LABEL", "");
        localizeLabels.put("EMPLOYEE_CODE_LABEL", "");
        localizeLabels.put("ASSIGNEES_STATUS_LABEL", "");
        localizeLabels.put("TASK_NUMBER_LABEL", "");
        localizeLabels.put("PROJECT_LABEL", "");
        localizeLabels.put("STATUS_LABEL", "");
        localizeLabels.put("START_DATE_LABEL", "");
        localizeLabels.put("EMPLOYEE_NAME_LABEL", "");
        localizeLabels.put("BILLABLE_LABEL", "");
        localizeLabels.put("ADDITIONAL_INFORMATION_LABEL", "");
        localizeLabels.put("TASK_NAME_LABEL", "");
        localizeLabels.put("TASK_DESCRIPTION_LABEL", "");


        TreeMap<String, String> taskContentTable = new TreeMap<>();
        taskContentTable.put("NAME", "");
        taskContentTable.put("TASK_NUMBER", "");
        taskContentTable.put("PROJECT_NAME", "");
        taskContentTable.put("PROJECT_MANAGER", "");
        taskContentTable.put("BACKUP_MANAGERs", "");
        taskContentTable.put("TASK_ASSIGNEES", "");
        taskContentTable.put("TASK_ASSIGNEES2", "");
        taskContentTable.put("CLIENT_NAME", "");
        taskContentTable.put("TASK_NAME", "");
        taskContentTable.put("BILLABLE", "");
        taskContentTable.put("TASK_DESCRIPTION", "");
        taskContentTable.put("TASK_DESCRIPTION1", "");
        taskContentTable.put("TASK_CREATED_DATE", "");
        taskContentTable.put("TASK_START_DATE", "");
        taskContentTable.put("TASK_DUE_DATE", "");
        taskContentTable.put("TASK_PRIORITY", "");
        taskContentTable.put("TASK_STATUS", "");
        taskContentTable.put("ESTIMATED_TIME", "");
        taskContentTable.put("TIME_SPENT", "");
        taskContentTable.put("PROJECT_NUMBER", "");
        taskContentTable.put("BILL_ADDRESS_NAME", "");
        taskContentTable.put("BILL_ADDRESS", "");
        taskContentTable.put("BILL_ADDRESS2", "");
        taskContentTable.put("BILL_STATE", "");
        taskContentTable.put("BILL_CITY", "");
        taskContentTable.put("BILL_COUNTRY", "");
        taskContentTable.put("BILL_ZIPCODE", "");
        taskContentTable.put("CLIENT_CONTACT", "");
        taskContentTable.put("MAIL_ADDRESS_NAME", "");
        taskContentTable.put("MAIL_ADDRESS", "");
        taskContentTable.put("MAIL_ADDRESS2", "");
        taskContentTable.put("MAIL_STATE", "");
        taskContentTable.put("MAIL_CITY", "");
        taskContentTable.put("MAIL_COUNTRY", "");
        taskContentTable.put("MAIL_ZIPCODE", "");
        taskContentTable.put("TASK_CONTENT_TABLE", "");


        //TODO CASH RECEIPT

        TreeMap<String, String> CRcustomProductTable = new TreeMap<String, String>();
        CRcustomProductTable.put("INV_DUE_DATE", "INVOICE DUE DATE");
        CRcustomProductTable.put("DISCOUNT_AMOUNT", "");
        CRcustomProductTable.put("ITEM_TOTAL_PAYMENT_AMOUNT", "");
        CRcustomProductTable.put("ACCOUNT_NAME", "");
        CRcustomProductTable.put("PO_NUMBER", "ORDER NUMBER");
        CRcustomProductTable.put("ITEM_TAX_OF_PAYMENT_AMOUNT", "");
        CRcustomProductTable.put("AMOUNT", "");
        CRcustomProductTable.put("NO", "");
        CRcustomProductTable.put("PAYMENT_BASE_AMOUNT", "");
        CRcustomProductTable.put("CURRENCY", "");
        CRcustomProductTable.put("TOTAL_AMOUNT", "");
        CRcustomProductTable.put("NET_AMOUNT", "");
        CRcustomProductTable.put("INVOICE_PRODUCT_NAME", "");
        CRcustomProductTable.put("INV_NUMBER", "INVOICE NUMBER");
        CRcustomProductTable.put("PAYMENT_REFERENCE", "");
        CRcustomProductTable.put("INVOICE_ITEM_TAX_AMOUNT", "");
        CRcustomProductTable.put("BASE_AMOUNT", "");
        CRcustomProductTable.put("PROJECT_NAME", "");
        CRcustomProductTable.put("ITEM_SUB_PROJECT", "");
        CRcustomProductTable.put("INVOICE_ITEM_UNIT_PRICE", "");
        CRcustomProductTable.put("PROJECT_NUMBER", "");
        CRcustomProductTable.put("NAME", "");
        CRcustomProductTable.put("INV_DATE", "");
        CRcustomProductTable.put("INVOICE_ITEM_TOTAL_AMOUNT", "");
        CRcustomProductTable.put("INVOICE_ITEM_NET_AMOUNT", "");
        CRcustomProductTable.put("INVOICE_ITEM_QTY", "");
        CRcustomProductTable.put("PARENT_PROJECT", "");
        CRcustomProductTable.put("ACCOUNT_CODE", "");
        CRcustomProductTable.put("INVOICE_ITEM_TAX_RATE", "");
        CRcustomProductTable.put("DUE_AMOUNT", "");


        TreeMap<String, String> CRcustomBillToAddress = new TreeMap<String, String>();
        CRcustomBillToAddress.put("NUMBER", "");
        CRcustomBillToAddress.put("PROJECT_NUMBER", "");
        CRcustomBillToAddress.put("DEPARTMENT", "");
        CRcustomBillToAddress.put("TOTAL_ARABIC_WORD_ALL", "");
        CRcustomBillToAddress.put("TOTAL", "");
        CRcustomBillToAddress.put("BILL_CITY", "");
        CRcustomBillToAddress.put("TOTAL_WORD_ALL", "");
        CRcustomBillToAddress.put("BILL_ADDRESS", "");
        CRcustomBillToAddress.put("ACCOUNT", "");
        CRcustomBillToAddress.put("PAYMENT_TYPE", "");
        CRcustomBillToAddress.put("BANK_ACCOUNT_NUMBER", "");
        CRcustomBillToAddress.put("TOTAL_AMOUNT", "");
        CRcustomBillToAddress.put("DATE", "");
        CRcustomBillToAddress.put("CURRENCY", "");
        CRcustomBillToAddress.put("ACCOUNT_CODE", "");
        CRcustomBillToAddress.put("BASE_CURRENCY", "");
        CRcustomBillToAddress.put("TOTAL_WORD", "");
        CRcustomBillToAddress.put("BILL_COUNTRY", "");
        CRcustomBillToAddress.put("INVOICE_NUMBERS", "");
        CRcustomBillToAddress.put("CLIENT_CODE", "");
        CRcustomBillToAddress.put("DUE_AMOUNT", "");
        CRcustomBillToAddress.put("DESCRIPTION", "");
        CRcustomBillToAddress.put("BILL_ADDRESS_NAME", "");
        CRcustomBillToAddress.put("TITLE", "");
        CRcustomBillToAddress.put("PROJECT_NAME", "");
        CRcustomBillToAddress.put("BILL_ZIPCODE", "");
        CRcustomBillToAddress.put("BANK_ACCOUNT_CODE", "");
        CRcustomBillToAddress.put("CLIENT_REGISTRATION_NUMBER", "");
        CRcustomBillToAddress.put("BILL_STATE", "");
        CRcustomBillToAddress.put("BILL_ADDRESS2", "");
        CRcustomBillToAddress.put("REFERENCE", "");
        CRcustomBillToAddress.put("CRM_ACCOUNT", "");

        //TODO CASH PAYMENT
        TreeMap<String, String> CPcustomProductTable = new TreeMap<String, String>();
        CPcustomProductTable.put("PROJECT", "");
        CPcustomProductTable.put("TAX_RATE", "");
        CPcustomProductTable.put("ACCOUNT_NAME", "");
        CPcustomProductTable.put("ITEM_SUB_PROJECT", "");
        CPcustomProductTable.put("DESCRIPTION", "");
        CPcustomProductTable.put("NAME", "");
        CPcustomProductTable.put("REFERENCE", "");
        CPcustomProductTable.put("AMOUNT", "");
        CPcustomProductTable.put("ACCOUNT_CODE", "");
        CPcustomProductTable.put("TAX_AMOUNT", "");
        CPcustomProductTable.put("DEPARTMENT", "");
        CPcustomProductTable.put("BASE_AMOUNT", "");
        CPcustomProductTable.put("PARENT_PROJECT", "");

        TreeMap<String, String> CPcustomBillToAddress = new TreeMap<String, String>();
        CPcustomBillToAddress.put("NAME_LIST", "");
        CPcustomBillToAddress.put("project", "");
        CPcustomBillToAddress.put("CASH_ACCOUNT", "");
        CPcustomBillToAddress.put("FOREIGN_TOTAL_WORD", "");
        CPcustomBillToAddress.put("REFERENCE", "");
        CPcustomBillToAddress.put("TOTAL", "");
        CPcustomBillToAddress.put("DESCRIPTION", "");
        CPcustomBillToAddress.put("COMP_VAT_NUMBER", "");
        CPcustomBillToAddress.put("CHECK_NUMBER", "");
        CPcustomBillToAddress.put("ENABLED_DEPARTMENT", "");
        CPcustomBillToAddress.put("SUBTOTAL_IN_BASE", "");
        CPcustomBillToAddress.put("BASE_CURRENCY", "");
        CPcustomBillToAddress.put("EXCHANGE_RATE", "");
        CPcustomBillToAddress.put("DESCRIPTION_LIST", "");
        CPcustomBillToAddress.put("TAX_TOTAL", "");
        CPcustomBillToAddress.put("SUBTOTAL", "");
        CPcustomBillToAddress.put("NUMBER", "");
        CPcustomBillToAddress.put("CURRENCY", "");
        CPcustomBillToAddress.put("TOTAL_IN_BASE", "");
        CPcustomBillToAddress.put("FOREIGN_TOTAL", "");
        CPcustomBillToAddress.put("TOTAL_WORD", "");
        CPcustomBillToAddress.put("CASH_ACCOUNT_CODE", "");
        CPcustomBillToAddress.put("TAX_TOTAL_IN_BASE", "");
        CPcustomBillToAddress.put("DATE", "");
        CPcustomBillToAddress.put("FOREIGN_CURRENCY", "");
        CPcustomBillToAddress.put("NARRATION", "");


        //TODO BANK RECEIPT
        TreeMap<String, String> BRcustomProductTable = new TreeMap<String, String>();
        BRcustomProductTable.put("INVOICE_ITEM_TAX_AMOUNT", "");
        BRcustomProductTable.put("NET_AMOUNT", "");
        BRcustomProductTable.put("ITEM_SUB_PROJECT", "");
        BRcustomProductTable.put("INV_DUE_DATE", "");
        BRcustomProductTable.put("PARENT_PROJECT", "");
        BRcustomProductTable.put("TOTAL_AMOUNT", "");
        BRcustomProductTable.put("ITEM_TAX_OF_PAYMENT_AMOUNT", "");
        BRcustomProductTable.put("PROJECT_NUMBER", "");
        BRcustomProductTable.put("NAME", "");
        BRcustomProductTable.put("DUE_AMOUNT", "");
        BRcustomProductTable.put("INVOICE_PRODUCT_NAME", "");
        BRcustomProductTable.put("BASE_AMOUNT", "");
        BRcustomProductTable.put("INV_DATE", "");
        BRcustomProductTable.put("NO", "");
        BRcustomProductTable.put("PROJECT_NAME", "");
        BRcustomProductTable.put("INVOICE_ITEM_UNIT_PRICE", "");
        BRcustomProductTable.put("INVOICE_ITEM_QTY", "");
        BRcustomProductTable.put("DISCOUNT_AMOUNT", "");
        BRcustomProductTable.put("PAYMENT_REFERENCE", "");
        BRcustomProductTable.put("AMOUNT", "");
        BRcustomProductTable.put("PO_NUMBER", "ORDER NUMBER");
        BRcustomProductTable.put("ACCOUNT_NAME", "");
        BRcustomProductTable.put("INVOICE_ITEM_TOTAL_AMOUNT", "");
        BRcustomProductTable.put("ACCOUNT_CODE", "");
        BRcustomProductTable.put("INVOICE_ITEM_TAX_RATE", "");
        BRcustomProductTable.put("PAYMENT_BASE_AMOUNT", "");
        BRcustomProductTable.put("CURRENCY", "");
        BRcustomProductTable.put("ITEM_TOTAL_PAYMENT_AMOUNT", "");
        BRcustomProductTable.put("INV_NUMBER", "");
        BRcustomProductTable.put("INVOICE_ITEM_NET_AMOUNT", "");

        TreeMap<String, String> BRcustomBillToAddress = new TreeMap<String, String>();
        BRcustomBillToAddress.put("TITLE", "");
        BRcustomBillToAddress.put("BILL_COUNTRY", "");
        BRcustomBillToAddress.put("CLIENT_REGISTRATION_NUMBER", "");
        BRcustomBillToAddress.put("BANK_ACCOUNT_CODE", "");
        BRcustomBillToAddress.put("TOTAL_WORD_ALL", "");
        BRcustomBillToAddress.put("TOTAL", "");
        BRcustomBillToAddress.put("BILL_ADDRESS", "");
        BRcustomBillToAddress.put("DESCRIPTION", "");
        BRcustomBillToAddress.put("INVOICE_NUMBERS", "");
        BRcustomBillToAddress.put("ACCOUNT_CODE", "");
        BRcustomBillToAddress.put("BANK_ACCOUNT_NUMBER", "");
        BRcustomBillToAddress.put("PROJECT_NAME", "");
        BRcustomBillToAddress.put("ACCOUNT", "");
        BRcustomBillToAddress.put("TOTAL_AMOUNT", "");
        BRcustomBillToAddress.put("PAYMENT_TYPE", "");
        BRcustomBillToAddress.put("DUE_AMOUNT", "");
        BRcustomBillToAddress.put("BILL_ADDRESS2", "");
        BRcustomBillToAddress.put("DATE", "");
        BRcustomBillToAddress.put("CRM_ACCOUNT", "");
        BRcustomBillToAddress.put("TOTAL_WORD", "");
        BRcustomBillToAddress.put("PROJECT_NUMBER", "");
        BRcustomBillToAddress.put("BILL_CITY", "");
        BRcustomBillToAddress.put("CLIENT_CODE", "");
        BRcustomBillToAddress.put("TOTAL_ARABIC_WORD_ALL", "");
        BRcustomBillToAddress.put("BASE_CURRENCY", "");
        BRcustomBillToAddress.put("BILL_ZIPCODE", "");
        BRcustomBillToAddress.put("NUMBER", "");
        BRcustomBillToAddress.put("CURRENCY", "");
        BRcustomBillToAddress.put("REFERENCE", "");
        BRcustomBillToAddress.put("BILL_STATE", "");
        BRcustomBillToAddress.put("BILL_ADDRESS_NAME", "");
        BRcustomBillToAddress.put("DEPARTMENT", "");

        TreeMap<String, String> BRcustomBankTable = new TreeMap<String, String>();
        BRcustomBankTable.put("BANK_NAME", "");
        BRcustomBankTable.put("STREET_ADDRESS", "");
        BRcustomBankTable.put("BILL_ADDRESS", "");
        BRcustomBankTable.put("BRANCH", "");
        BRcustomBankTable.put("BANK_ACCOUNT_COUNTRY", "");
        BRcustomBankTable.put("BANK_ACCOUNT_CITY", "");
        BRcustomBankTable.put("BANK_ACCOUNT_POSTCODE", "");
        BRcustomBankTable.put("CURRENCY", "");


        TreeMap<String, String> BRcustomAccountTable = new TreeMap<String, String>();
        BRcustomAccountTable.put("SWIFT_BIC", "");
        BRcustomAccountTable.put("ACCOUNT_NUMBER", "");
        BRcustomAccountTable.put("IBAN_CODE", "");
        BRcustomAccountTable.put("ACCOUNT_NAME", "");


        //TODO BANK PAYMENT
        TreeMap<String, String> BPcustomProductTable = new TreeMap<String, String>();
        BPcustomProductTable.put("ITEM_SUB_PROJECT", "");
        BPcustomProductTable.put("REFERENCE", "");
        BPcustomProductTable.put("DESCRIPTION", "");
        BPcustomProductTable.put("PARENT_PROJECT", "");
        BPcustomProductTable.put("TAX_AMOUNT", "");
        BPcustomProductTable.put("NAME", "");
        BPcustomProductTable.put("PROJECT", "");
        BPcustomProductTable.put("TAX_RATE", "");
        BPcustomProductTable.put("DEPARTMENT", "");
        BPcustomProductTable.put("BASE_AMOUNT", "");
        BPcustomProductTable.put("ACCOUNT_NAME", "");
        BPcustomProductTable.put("AMOUNT", "");
        BPcustomProductTable.put("ACCOUNT_CODE", "");

        TreeMap<String, String> BPcustomBillToAddress = new TreeMap<String, String>();
        BPcustomBillToAddress.put("DESCRIPTION", "");
        BPcustomBillToAddress.put("BANK_ACCOUNT_CODE", "");
        BPcustomBillToAddress.put("project", "");
        BPcustomBillToAddress.put("BANK_ACCOUNT_NUMBER", "");
        BPcustomBillToAddress.put("ENABLED_DEPARTMENT", "");
        BPcustomBillToAddress.put("BANK_ACCOUNT", "");
        BPcustomBillToAddress.put("SUBTOTAL", "");
        BPcustomBillToAddress.put("TOTAL", "");
        BPcustomBillToAddress.put("TOTAL_WORD", "");
        BPcustomBillToAddress.put("NUMBER", "");
        BPcustomBillToAddress.put("EXCHANGE_RATE", "");
        BPcustomBillToAddress.put("FOREIGN_TOTAL", "");
        BPcustomBillToAddress.put("REFERENCE", "");
        BPcustomBillToAddress.put("CHECK_NUMBER", "");
        BPcustomBillToAddress.put("DESCRIPTION_LIST", "");
        BPcustomBillToAddress.put("NARRATION", "");
        BPcustomBillToAddress.put("DATE", "");
        BPcustomBillToAddress.put("TAX_TOTAL_IN_BASE", "");
        BPcustomBillToAddress.put("CURRENCY", "");
        BPcustomBillToAddress.put("FOREIGN_TOTAL_WORD", "");
        BPcustomBillToAddress.put("TOTAL_IN_BASE", "");
        BPcustomBillToAddress.put("COMP_VAT_NUMBER", "");
        BPcustomBillToAddress.put("NAME_LIST", "");
        BPcustomBillToAddress.put("SUBTOTAL_IN_BASE", "");
        BPcustomBillToAddress.put("TAX_TOTAL", "");
        BPcustomBillToAddress.put("DEBIT", "");
        BPcustomBillToAddress.put("BASE_CURRENCY", "");
        BPcustomBillToAddress.put("FOREIGN_CURRENCY", "");

        //TODO TRIAL BALANCE

        TreeMap<String, String> TBassets = new TreeMap<>();
        TBassets.put("ACCOUNT_NAME", "");
        TBassets.put("ACCOUNT_CODE", "");
        TBassets.put("BEGINNET_DEBIT", "");
        TBassets.put("BEGINNET_CREDIT", "");
        TBassets.put("CREDIT", "");
        TBassets.put("ENDING_DEBIT", "");
        TBassets.put("ENDING_CREDIT", "");
        TBassets.put("DEBIT", "");
        TBassets.put("PARENT_CODE", "");
        TBassets.put("PARENT_NAME", "");
        TBassets.put("CATEGORY_CODE", "");
        TBassets.put("ACCOUNT_ID", "");
        TBassets.put("BEGINNET_BALANCE", "");
        TBassets.put("HAS_CHILD", "");
        TBassets.put("ENDING_BALANCE", "");

        TreeMap<String, String> TBliabilities = new TreeMap<>();
        TBliabilities.put("ACCOUNT_NAME", "");
        TBliabilities.put("ACCOUNT_CODE", "");
        TBliabilities.put("BEGINNET_DEBIT", "");
        TBliabilities.put("BEGINNET_CREDIT", "");
        TBliabilities.put("CREDIT", "");
        TBliabilities.put("ENDING_DEBIT", "");
        TBliabilities.put("ENDING_CREDIT", "");
        TBliabilities.put("DEBIT", "");
        TBliabilities.put("PARENT_CODE", "");
        TBliabilities.put("PARENT_NAME", "");
        TBliabilities.put("CATEGORY_CODE", "");
        TBliabilities.put("ACCOUNT_ID", "");
        TBliabilities.put("BEGINNET_BALANCE", "");
        TBliabilities.put("HAS_CHILD", "");
        TBliabilities.put("ENDING_BALANCE", "");

        TreeMap<String, String> TBequity = new TreeMap<>();
        TBequity.put("DEBIT", "");
        TBequity.put("ACCOUNT_NAME", "");
        TBequity.put("BEGINNET_CREDIT", "");
        TBequity.put("BEGINNET_DEBIT", "");
        TBequity.put("ACCOUNT_CODE", "");
        TBequity.put("CREDIT", "");
        TBequity.put("ENDING_DEBIT", "");
        TBequity.put("ENDING_CREDIT", "");
        TBequity.put("PARENT_NAME", "");
        TBequity.put("PARENT_CODE", "");
        TBequity.put("CATEGORY_CODE", "");
        TBequity.put("HAS_CHILD", "");
        TBequity.put("ENDING_BALANCE", "");
        TBequity.put("BEGINNET_BALANCE", "");
        TBequity.put("ACCOUNT_ID", "");


        TreeMap<String, String> TBexpenses = new TreeMap<>();
        TBexpenses.put("DEBIT", "");
        TBexpenses.put("ACCOUNT_NAME", "");
        TBexpenses.put("BEGINNET_DEBIT", "");
        TBexpenses.put("ACCOUNT_CODE", "");
        TBexpenses.put("BEGINNET_CREDIT", "");
        TBexpenses.put("PARENT_NAME", "");
        TBexpenses.put("CATEGORY_CODE", "");
        TBexpenses.put("ENDING_DEBIT", "");
        TBexpenses.put("CREDIT", "");
        TBexpenses.put("ENDING_CREDIT", "");
        TBexpenses.put("BEGINNET_BALANCE", "");
        TBexpenses.put("ACCOUNT_ID", "");
        TBexpenses.put("PARENT_CODE", "");
        TBexpenses.put("HAS_CHILD", "");
        TBexpenses.put("ENDING_BALANCE", "");


        TreeMap<String, String> TBrevenue = new TreeMap<>();
        TBrevenue.put("ACCOUNT_NAME", "");
        TBrevenue.put("BEGINNET_CREDIT", "");
        TBrevenue.put("BEGINNET_DEBIT", "");
        TBrevenue.put("ACCOUNT_CODE", "");
        TBrevenue.put("CREDIT", "");
        TBrevenue.put("ENDING_DEBIT", "");
        TBrevenue.put("DEBIT", "");
        TBrevenue.put("ENDING_CREDIT", "");
        TBrevenue.put("CATEGORY_CODE", "");
        TBrevenue.put("PARENT_CODE", "");
        TBrevenue.put("PARENT_NAME", "");
        TBrevenue.put("ACCOUNT_ID", "");
        TBrevenue.put("BEGINNET_BALANCE", "");
        TBrevenue.put("ENDING_BALANCE", "");
        TBrevenue.put("HAS_CHILD", "");


        TreeMap<String, String> TBtotal = new TreeMap<>();
        TBtotal.put("ENDING_BALANCE", "");
        TBtotal.put("ACCOUNT_NAME", "");
        TBtotal.put("BEGINNET_CREDIT", "");
        TBtotal.put("BEGINNET_DEBIT", "");
        TBtotal.put("BEGINNET_BALANCE", "");
        TBtotal.put("ACCOUNT_CODE", "");
        TBtotal.put("ENDING_DEBIT", "");
        TBtotal.put("ENDING_CREDIT", "");
        TBtotal.put("CREDIT", "");
        TBtotal.put("DEBIT", "");

        TreeMap<String, String> TBemptyData = new TreeMap<>();
        TBemptyData.put("ACCOUNT_CODE", "");
        TBemptyData.put("ACCOUNT_NAME", "");
        TBemptyData.put("BEGINNET_DEBIT", "");
        TBemptyData.put("BEGINNET_CREDIT", "");
        TBemptyData.put("DEBIT", "");
        TBemptyData.put("CREDIT", "");
        TBemptyData.put("ENDING_DEBIT", "");
        TBemptyData.put("ENDING_CREDIT", "");

        //TODO REQUEST FOR QUOTE
        TreeMap<String, String> RFQcustomProductTable = new TreeMap<String, String>();
        RFQcustomProductTable.put("SUPPLIER", "");
        RFQcustomProductTable.put("NAME", "");
        RFQcustomProductTable.put("QTY", "");
        RFQcustomProductTable.put("UNIT", "");
        RFQcustomProductTable.put("NO", "");
        RFQcustomProductTable.put("DESCRIPTION", "");
        RFQcustomProductTable.put("REMARKS", "");
        RFQcustomProductTable.put("ITEM_COST_PRICE", "");
        RFQcustomProductTable.put("ITEM_COMISSION", "");

        TreeMap<String, String> RFQcustomBillToAddress = new TreeMap<String, String>();
        RFQcustomBillToAddress.put("BILL_COUNTRY", "");
        RFQcustomBillToAddress.put("STATUS", "");
        RFQcustomBillToAddress.put("CUSTOMER", "");
        RFQcustomBillToAddress.put("PHONE", "");
        RFQcustomBillToAddress.put("PAYMENT_TERMS", "");
        RFQcustomBillToAddress.put("COMPANY_ADDRESS", "");
        RFQcustomBillToAddress.put("CREATOR", "");
        RFQcustomBillToAddress.put("BILL_ADDRESS_NAME", "");
        RFQcustomBillToAddress.put("requestfrom", "");
        RFQcustomBillToAddress.put("BILL_STATE", "");
        RFQcustomBillToAddress.put("APPROVER", "");
        RFQcustomBillToAddress.put("CLIENT_CONTACT", "");
        RFQcustomBillToAddress.put("Date", "");
        RFQcustomBillToAddress.put("BILL_ZIPCODE", "");
        RFQcustomBillToAddress.put("BILL_ADDRESS", "");
        RFQcustomBillToAddress.put("Number", "");
        RFQcustomBillToAddress.put("EMAIL", "");
        RFQcustomBillToAddress.put("SQ_NUMBER", "");
        RFQcustomBillToAddress.put("vlidUntil", "VALID UNTIL");
        RFQcustomBillToAddress.put("project", "");
        RFQcustomBillToAddress.put("BILL_ADDRESS2", "");
        RFQcustomBillToAddress.put("INTRODUCTION", "");
        RFQcustomBillToAddress.put("BILL_CITY", "");

        //TODO REQUEST FOR PURCHASE


        TreeMap<String, String> RFPcustomProductTable = new TreeMap<String, String>();

        RFPcustomProductTable.put("NO", "NUMBER");
        RFPcustomProductTable.put("UNIT_MEASUREMENT", "");
        RFPcustomProductTable.put("ITEM_WAREHOUSE", "");
        RFPcustomProductTable.put("QTY_ON_HAND", "QUANTITY ON HAND");
        RFPcustomProductTable.put("NAME", "");
        RFPcustomProductTable.put("ITEM_QTY", "ITEM QUANTITY");
        RFPcustomProductTable.put("DESCRIPTION", "");

        TreeMap<String, String> RFPcustomBillToAddress = new TreeMap<String, String>();
        RFPcustomBillToAddress.put("CUSTOMER", "");
        RFPcustomBillToAddress.put("Number", "");
        RFPcustomBillToAddress.put("CREATOR", "");
        RFPcustomBillToAddress.put("DUE_DATE", "");
        RFPcustomBillToAddress.put("project", "");
        RFPcustomBillToAddress.put("MANAGER", "");

        //TODO LEAVE REQUEST
        TreeMap<String, String> LRcustomNumberAndDatesTable = new TreeMap<String, String>();
        LRcustomNumberAndDatesTable.put("FROM_DATE", "");
        LRcustomNumberAndDatesTable.put("POSITION", "");
        LRcustomNumberAndDatesTable.put("SHORT_FROM_DATE", "");
        LRcustomNumberAndDatesTable.put("TO_DATE", "");
        LRcustomNumberAndDatesTable.put("TAKEN_FROM_ALLOWANCE", "");
        LRcustomNumberAndDatesTable.put("SHORT_TO_DATE", "");
        LRcustomNumberAndDatesTable.put("DESCRIPTION", "");
        LRcustomNumberAndDatesTable.put("DURATION", "");
        LRcustomNumberAndDatesTable.put("TO_DATE_EN", "");
        LRcustomNumberAndDatesTable.put("LEAVE_TYPE", "");
        LRcustomNumberAndDatesTable.put("LEAVE_TYPE_CODE", "");
        LRcustomNumberAndDatesTable.put("PHONE_NUMBER", "");
        LRcustomNumberAndDatesTable.put("ANNUAL_LEAVE_ALLOWANCE_DAYS", "");
        LRcustomNumberAndDatesTable.put("DEPARTMENT", "");
        LRcustomNumberAndDatesTable.put("FROM_DATE_EN", "");
        LRcustomNumberAndDatesTable.put("SHORT_TO_DATE_UZ", "");
        LRcustomNumberAndDatesTable.put("STATUS", "");
        LRcustomNumberAndDatesTable.put("CREATED_DATE", "");
        LRcustomNumberAndDatesTable.put("SHORT_FROM_DATE_UZ", "");
        LRcustomNumberAndDatesTable.put("SUPERVISOR", "");
        LRcustomNumberAndDatesTable.put("APPROVER", "");
        LRcustomNumberAndDatesTable.put("SUPERVISOR_CODE", "");
        LRcustomNumberAndDatesTable.put("EMPLOYEE_CODE", "");
        LRcustomNumberAndDatesTable.put("BACK_TO_WORK_DATE", "");
        LRcustomNumberAndDatesTable.put("FROM_DATE_UZ", "");
        LRcustomNumberAndDatesTable.put("TYPE", "");
        LRcustomNumberAndDatesTable.put("TO_DATE_UZ", "");
        LRcustomNumberAndDatesTable.put("EMPLOYEE_NAME", "");

        TreeMap<String, String> approversInformation = new TreeMap<>();
        approversInformation.put("APPROVER", "");
        approversInformation.put("Number", "");

        TreeMap<String, String> leaveRequestInformation = new TreeMap<>();
        leaveRequestInformation.put("NAME", "");
        leaveRequestInformation.put("DESCRIPTION", "");
        leaveRequestInformation.put("CREATED_DATE", "");
        leaveRequestInformation.put("PERIOD", "");
        leaveRequestInformation.put("TO_DATE", "");
        leaveRequestInformation.put("DURATION", "");
        leaveRequestInformation.put("TYPE", "");
        leaveRequestInformation.put("REASON", "");
        leaveRequestInformation.put("CREATOR", "");
        leaveRequestInformation.put("STATUS", "");
        leaveRequestInformation.put("CREATOR_POSITION", "");
        leaveRequestInformation.put("CREATOR_DEPARTMENT", "");
        leaveRequestInformation.put("SVG", "");

        TreeMap<String, String> LVemployeeInformation = new TreeMap<>();
        LVemployeeInformation.put("POSITION_UZ", "");
        LVemployeeInformation.put("TITLE", "");
        LVemployeeInformation.put("HIRE_DATE", "");
        LVemployeeInformation.put("GENDER", "");
        LVemployeeInformation.put("DEPARTMENT_RU", "");
        LVemployeeInformation.put("POSITION", "");
        LVemployeeInformation.put("DEPARTMENT_UZ", "");
        LVemployeeInformation.put("RESIGNATION_DATE", "");
        LVemployeeInformation.put("BANK_ACCOUNT_INFORMATION", "");
        LVemployeeInformation.put("DEPARTMENT_EN", "");
        LVemployeeInformation.put("department", "");
        LVemployeeInformation.put("ADDITIONAL_INFORMATION", "");
        LVemployeeInformation.put("POSITION_EN", "");
        LVemployeeInformation.put("ADDRESS_INFORMATION", "");
        LVemployeeInformation.put("SUPERVISOR", "");
        LVemployeeInformation.put("EMPLOYMENT_MODE", "");
        LVemployeeInformation.put("DATE_OF_BIRTH", "");
        LVemployeeInformation.put("CONTACT_INFORMATION", "");
        LVemployeeInformation.put("POSITION_RU", "");
        LVemployeeInformation.put("EMPLOYMENT_INFORMATION", "");
        LVemployeeInformation.put("EMPLOYEE_NAME", "");
        LVemployeeInformation.put("PRIMARY_EMAIL", "");
        LVemployeeInformation.put("MARITAL_STATUS", "");
        LVemployeeInformation.put("TERMS_OF_CONTRACT", "");
        LVemployeeInformation.put("LOCATION", "");
        LVemployeeInformation.put("QUALIFICATION", "");
        LVemployeeInformation.put("EMPLOYEE_PHOTO", "");
        LVemployeeInformation.put("EMPLOYEE_CODE", "");

        TreeMap<String, String> LVpersonalInformation = new TreeMap<>();
        LVpersonalInformation.put("PASSPORT_ISSUE", "");
        LVpersonalInformation.put("PASSPORT_NUMBER", "");
        LVpersonalInformation.put("VISA_ISSUE_DATE", "");
        LVpersonalInformation.put("VISA_EXPIRY_DATE", "");
        LVpersonalInformation.put("VISA_NUMBER", "");
        LVpersonalInformation.put("INSURANCE_EXPIRY_DATE", "");
        LVpersonalInformation.put("PASSPORT_ISSUE_DATE", "");
        LVpersonalInformation.put("INSURANCE_NUMBER", "");


        //TODO STOCK TRANSFER
        TreeMap<String, String> productTable = new TreeMap<String, String>();
        productTable.put("FROM_WAREHOUSE", "");
        productTable.put("TO_WAREHOUSE", "");
        productTable.put("PRODUCT_NAME", "");
        productTable.put("ITEM_QTY", "");
        productTable.put("UNIT_MEASUREMENT", "");
        productTable.put("NO", "");

        TreeMap<String, String> viewTable = new TreeMap<String, String>();
        viewTable.put("NARRATION", "");
        viewTable.put("Date", "");
        viewTable.put("Number", "");
        viewTable.put("CREATOR", "");


        //TODO MEETING MINUTES
        TreeMap<String, String> meetingTable = new TreeMap<String, String>();
        meetingTable.put("PREPARED_BY", "");
        meetingTable.put("START_DATE", "");
        meetingTable.put("MEETING_ID", "");
        meetingTable.put("CALLED_BY", "");
        meetingTable.put("END_DATE", "");
        meetingTable.put("PROJECT_NAME", "");
        meetingTable.put("ABSENT", "");
        meetingTable.put("PURPOSE", "");
        meetingTable.put("LOCATION", "");
        meetingTable.put("TITLE", "");
        meetingTable.put("TYPE", "");
        meetingTable.put("ATTENDEES", "");

        TreeMap<String, String> notesTable = new TreeMap<String, String>();
        notesTable.put("COMMENT", "");
        notesTable.put("ADDED_BY", "");
        notesTable.put("DATE", "");

        TreeMap<String, String> agendaTopicTable = new TreeMap<String, String>();
        agendaTopicTable.put("DISCUSSION_POINTS", "");
        agendaTopicTable.put("ACTION_POINTS", "");
        agendaTopicTable.put("ASSIGNEED_TO", "");
        agendaTopicTable.put("START_DATE", "");
        agendaTopicTable.put("DUE_DATE", "");


        //TODO MANUAL ENTRY
        TreeMap<String, String> MEcustomProductTable = new TreeMap<String, String>();
        MEcustomProductTable.put("DEBIT", "");
        MEcustomProductTable.put("DESCRIPTION", "");
        MEcustomProductTable.put("CREDIT", "");
        MEcustomProductTable.put("department", "");
        MEcustomProductTable.put("NAME", "");
        MEcustomProductTable.put("REFERENCE", "");
        MEcustomProductTable.put("PROJECT", "");
        MEcustomProductTable.put("ACCOUNT_CODE", "");
        MEcustomProductTable.put("ACCOUNT_NAME", "");
        MEcustomProductTable.put("PARENT_PROJECT", "");

        TreeMap<String, String> MEcustomTotalTable = new TreeMap<String, String>();
        MEcustomTotalTable.put("DEBIT_TOTAL", "");
        MEcustomTotalTable.put("BASE_DEBIT_TOTAL", "");
        MEcustomTotalTable.put("TOTAL_WORD_DEBIT", "");
        MEcustomTotalTable.put("TOTAL_WORD_CREDIT", "");
        MEcustomTotalTable.put("CREDIT_TOTAL", "");
        MEcustomTotalTable.put("BASE_CURRENCY", "");
        MEcustomTotalTable.put("BASE_CREDIT_TOTAL", "");
        MEcustomTotalTable.put("CURRENCY", "");

        TreeMap<String, String> MEcustomBillToAddress = new TreeMap<String, String>();
        MEcustomBillToAddress.put("NUMBER", "");
        MEcustomBillToAddress.put("ENABLED_DEPARTMENT", "");
        MEcustomBillToAddress.put("DATE", "");
        MEcustomBillToAddress.put("Number", "");
        MEcustomBillToAddress.put("NARRATION", "");


        //TODO RECEIVE PAYMENT

        TreeMap<String, String> RPAcustomProductTable = new TreeMap<String, String>();
        RPAcustomProductTable.put("PROJECT_NAME", "");
        RPAcustomProductTable.put("PARENT_PROJECT", "");
        RPAcustomProductTable.put("INVOICE_ITEM_NET_AMOUNT", "");
        RPAcustomProductTable.put("ITEM_TOTAL_PAYMENT_AMOUNT", "");
        RPAcustomProductTable.put("ACCOUNT_NAME", "");
        RPAcustomProductTable.put("PAYMENT_REFERENCE", "");
        RPAcustomProductTable.put("INVOICE_ITEM_QTY", "INVOICE ITEM QUANTITY");
        RPAcustomProductTable.put("AMOUNT", "");
        RPAcustomProductTable.put("INV_DUE_DATE", "INVOICE DUE DATE");
        RPAcustomProductTable.put("INVOICE_ITEM_TAX_RATE", "");
        RPAcustomProductTable.put("ITEM_SUB_PROJECT", "");
        RPAcustomProductTable.put("ITEM_TAX_OF_PAYMENT_AMOUNT", "");
        RPAcustomProductTable.put("INVOICE_ITEM_TOTAL_AMOUNT", "");
        RPAcustomProductTable.put("DUE_AMOUNT", "");
        RPAcustomProductTable.put("INV_NUMBER", "");
        RPAcustomProductTable.put("NAME", "");
        RPAcustomProductTable.put("TOTAL_AMOUNT", "");
        RPAcustomProductTable.put("INVOICE_PRODUCT_NAME", "");
        RPAcustomProductTable.put("DISCOUNT_AMOUNT", "");
        RPAcustomProductTable.put("c", "");
        RPAcustomProductTable.put("NET_AMOUNT", "");
        RPAcustomProductTable.put("INV_DATE", "INVOICE DUE DATE");
        RPAcustomProductTable.put("PO_NUMBER", "");
        RPAcustomProductTable.put("PAYMENT_BASE_AMOUNT", "");
        RPAcustomProductTable.put("CURRENCY", "");
        RPAcustomProductTable.put("INVOICE_ITEM_UNIT_PRICE", "");
        RPAcustomProductTable.put("PROJECT_NUMBER", "");
        RPAcustomProductTable.put("ACCOUNT_CODE", "");
        RPAcustomProductTable.put("INVOICE_ITEM_TAX_AMOUNT", "");
        RPAcustomProductTable.put("BASE_AMOUNT", "");


        TreeMap<String, String> RPcustomBillToAddress = new TreeMap<String, String>();
        RPcustomBillToAddress.put("BASE_CURRENCY", "");
        RPcustomBillToAddress.put("BILL_COUNTRY", "");
        RPcustomBillToAddress.put("BILL_ADDRESS_NAME", "");
        RPcustomBillToAddress.put("ACCOUNT_CODE", "");
        RPcustomBillToAddress.put("BILL_ADDRESS2", "");
        RPcustomBillToAddress.put("DEPARTMENT", "");
        RPcustomBillToAddress.put("BILL_CITY", "");
        RPcustomBillToAddress.put("BILL_ZIPCODE", "");
        RPcustomBillToAddress.put("TITLE", "");
        RPcustomBillToAddress.put("TOTAL", "");
        RPcustomBillToAddress.put("DUE_AMOUNT", "");
        RPcustomBillToAddress.put("BANK_ACCOUNT_CODE", "");
        RPcustomBillToAddress.put("TOTAL_WORD_ALL", "");
        RPcustomBillToAddress.put("BANK_ACCOUNT_NUMBER", "");
        RPcustomBillToAddress.put("REFERENCE", "");
        RPcustomBillToAddress.put("CURRENCY", "");
        RPcustomBillToAddress.put("PROJECT_NAME", "");
        RPcustomBillToAddress.put("ACCOUNT", "");
        RPcustomBillToAddress.put("TOTAL_AMOUNT", "");
        RPcustomBillToAddress.put("INVOICE_NUMBERS", "");
        RPcustomBillToAddress.put("PAYMENT_TYPE", "");
        RPcustomBillToAddress.put("BILL_ADDRESS", "");
        RPcustomBillToAddress.put("CLIENT_CODE", "");
        RPcustomBillToAddress.put("PROJECT_NUMBER", "");
        RPcustomBillToAddress.put("DESCRIPTION", "");
        RPcustomBillToAddress.put("CLIENT_REGISTRATION_NUMBER", "");
        RPcustomBillToAddress.put("DATE", "");
        RPcustomBillToAddress.put("BILL_STATE", "");
        RPcustomBillToAddress.put("TOTAL_ARABIC_WORD_ALL", "");
        RPcustomBillToAddress.put("TOTAL_WORD", "");
        RPcustomBillToAddress.put("CRM_ACCOUNT", "");
        RPcustomBillToAddress.put("NUMBER", "");


        //TODO ADDITIONAL PAYMENT
        TreeMap<String, String> APcustomProductTable = new TreeMap<String, String>();
        APcustomProductTable.put("ITEM_PERCENTAGE", "");
        APcustomProductTable.put("ITEM_CATEGORY", "");
        APcustomProductTable.put("NAME", "");
        APcustomProductTable.put("ITEM_BASIC_AMOUNT_PAY", "");
        APcustomProductTable.put("DATE", "");
        APcustomProductTable.put("AMOUNT", "");

        TreeMap<String, String> APcustomTotalTable = new TreeMap<String, String>();
        APcustomTotalTable.put("TOTAL_AMOUNT", "");
        APcustomTotalTable.put("TOTAL_IN_WORDS", "");


        TreeMap<String, String> APcustomNumberAndDatesTable = new TreeMap<String, String>();
        APcustomNumberAndDatesTable.put("REFERENCE", "");
        APcustomNumberAndDatesTable.put("CATEGORY_TYPE", "");
        APcustomNumberAndDatesTable.put("APPROVER", "");
        APcustomNumberAndDatesTable.put("GROUP", "");
        APcustomNumberAndDatesTable.put("TYPE", "");
        APcustomNumberAndDatesTable.put("PERIOD", "");

        //TODO CASH ADVANCE

        TreeMap<String, String> detailsTable = new TreeMap<>();
        detailsTable.put("EMPLOYEE_CODE", "");
        detailsTable.put("EMPLOYEE_NAME", "");
        detailsTable.put("DATE", "");
        detailsTable.put("AMOUNT", "");
        detailsTable.put("AMOUNT_IN_WORD", "");
        detailsTable.put("CURRENCY", "");
        detailsTable.put("PURPOSE", "");
        detailsTable.put("PAYMENT_METHOD", "");
        detailsTable.put("CATEGORY", "");
        detailsTable.put("PAYMENT_AMOUNT", "");
        detailsTable.put("PAYMENT_TERMS", "");
        detailsTable.put("REFERENCE", "");
        detailsTable.put("NUMBER", "");
        detailsTable.put("APPROVER", "");

        //TODO GROUP PAYRUN

        TreeMap<String, String> employerSettingsTable = new TreeMap<>();
        employerSettingsTable.put("BANK_NAME", "");
        employerSettingsTable.put("REFERENCE", "");
        employerSettingsTable.put("BANK_ACCOUNT_NUMBER", "");
        employerSettingsTable.put("BANK_ACCOUNT_NAME", "");
        employerSettingsTable.put("IBAN_CODE", "");
        employerSettingsTable.put("SWIFT_BIC", "");
        employerSettingsTable.put("BILL_ADDRESS", "");
        employerSettingsTable.put("COMPANY_CODE", "");

        TreeMap<String, String> dhofarTable = new TreeMap<>();
        dhofarTable.put("DEDUCTION_AMOUNT", "");
        dhofarTable.put("EMPLOYEE_NAME", "");
        dhofarTable.put("ADDITIONAL_PAYMENT", "");
        dhofarTable.put("PENSION_DEDUCTION", "");
        dhofarTable.put("EXPENSE_AMOUNT", "");
        dhofarTable.put("TO_DATE", "");
        dhofarTable.put("FROM_DATE", "");
        dhofarTable.put("EMPLOYEE_CODE", "");
        dhofarTable.put("BASIC_SALARY", "");
        dhofarTable.put("TOTAL_PAY", "");
        dhofarTable.put("COMMENTS", "");
        dhofarTable.put("ACCOUNT_NUMBER", "");
        dhofarTable.put("BANK_NAME", "");

        TreeMap<String, String> header = new TreeMap<>();
        header.put("CREATOR", "");
        header.put("FREQUENCY", "");
        header.put("GROUP", "");
        header.put("PROCESSED_DATE", "");
        header.put("APPROVER", "");
        header.put("PERIOD", "");
        header.put("CODE_LABEL", "");
        header.put("EMPLOYEE_LABEL", "");
        header.put("BASIC_LABEL", "");
        header.put("ALLOWANCE_LABEL", "");
        header.put("DEDUCTION_LABEL", "");
        header.put("EXPENSE_LABEL", "");
        header.put("TOTAL_LABEL", "");
        header.put("PAY_TOTAL_LABEL", "");

        TreeMap<String, String> employeeTable = new TreeMap<>();
        employeeTable.put("TO_DATE", "");
        employeeTable.put("ADDITIONAL_PAYMENT", "");
        employeeTable.put("EMPLOYEE_CODE", "");
        employeeTable.put("EMPLOYEE_NAME", "");
        employeeTable.put("BASIC_SALARY", "");
        employeeTable.put("FROM_DATE", "");
        employeeTable.put("DEDUCTION_AMOUNT", "");
        employeeTable.put("PENSION_DEDUCTION", "");
        employeeTable.put("COMMENTS", "");
        employeeTable.put("EXPENSE_AMOUNT", "");
        employeeTable.put("TOTAL_PAY", "");
        employeeTable.put("BANK_NAME", "");
        employeeTable.put("ACCOUNT_NUMBER", "");

        TreeMap<String, String> totalTable = new TreeMap<>();
        totalTable.put("DHOFAR_TOTAL", "");
        totalTable.put("TOTAL_IN_WORDS", "");
        totalTable.put("TOTAL", "");
        totalTable.put("PERIOD", "");
        totalTable.put("TOTAL_IN_BASE", "");
        totalTable.put("NON_DHOFAR", "");
        totalTable.put("PROCESSED_DATE", "");
        totalTable.put("NON_DHOFAR_IN_WORD", "");
        totalTable.put("DHOFAR_TOTAL_IN_WORD", "");
        totalTable.put("BASE_CURRENCY", "");

        TreeMap<String, String> nonDhofarTable = new TreeMap<>();
        nonDhofarTable.put("ACCOUNT_NUMBER", "");
        nonDhofarTable.put("COMMENTS", "");
        nonDhofarTable.put("BANK_NAME", "");
        nonDhofarTable.put("EXPENSE_AMOUNT", "");
        nonDhofarTable.put("FROM_DATE", "");
        nonDhofarTable.put("BASIC_SALARY", "");
        nonDhofarTable.put("EMPLOYEE_CODE", "");
        nonDhofarTable.put("TOTAL_PAY", "");
        nonDhofarTable.put("DEDUCTION_AMOUNT", "");
        nonDhofarTable.put("ADDITIONAL_PAYMENT", "");
        nonDhofarTable.put("EMPLOYEE_NAME", "");
        nonDhofarTable.put("PENSION_DEDUCTION", "");
        nonDhofarTable.put("TO_DATE", "");

        //TODO LEAD SUMMARY PDF EXPORT

        TreeMap<String, String> notesInformation = new TreeMap<>();
        notesInformation.put("PUBLISHED_BY", "");
        notesInformation.put("SUBJECT", "");
        notesInformation.put("DATE", "");

        TreeMap<String, String> addressInformation = new TreeMap<>();
        addressInformation.put("HOME_ADDRESS_NAME", "");
        addressInformation.put("HOME_ADDRESS_TITLE", "");
        addressInformation.put("HOME_ADDRESS", "");
        addressInformation.put("HOME_CITY", "");
        addressInformation.put("HOME_ADDRESS2", "");
        addressInformation.put("HOME_STATE", "");
        addressInformation.put("HOME_COUNTRY", "");
        addressInformation.put("HOME_ZIPCODE", "");
        addressInformation.put("CORPORATE_ADDRESS", "");
        addressInformation.put("CORPORATE_ADDRESS_NAME", "");
        addressInformation.put("CORPORATE_ADDRESS_TITLE", "");
        addressInformation.put("HOME_ADDRESS_FULL", "");
        addressInformation.put("CORPORATE_ADDRESS_FULL", "");
        addressInformation.put("CORPORATE_COUNTRY", "");
        addressInformation.put("CORPORATE_STATE", "");
        addressInformation.put("CORPORATE_CITY", "");
        addressInformation.put("CORPORATE_ADDRESS2", "");
        addressInformation.put("CORPORATE_ZIPCODE", "");

        TreeMap<String, String> contactInformation = new TreeMap<>();
        contactInformation.put("NAME", "");
        contactInformation.put("COMPANY_NAME", "");
        contactInformation.put("PHONE_NUMBER", "");
        contactInformation.put("PRIMARY_EMAIL", "");
        contactInformation.put("ASSIGNEE_NAME", "");
        contactInformation.put("CAMPAIGN", "");
        contactInformation.put("LEAD_SOURCE", "");
        contactInformation.put("LEAD_STATUS", "");
        contactInformation.put("CONTACT_INFORMATION", "");
        contactInformation.put("ADDITIONAL_INFORMATION", "");
        contactInformation.put("NOTES_INFORMATION", "");
        contactInformation.put("ADDRESS_INFORMATION", "");

        TreeMap<String, String> taskTable = new TreeMap<>();
        taskTable.put("Number", "");
        taskTable.put("PRIORITY", "");
        taskTable.put("END_DATE", "");
        taskTable.put("START_DATE", "");
        taskTable.put("STATUS", "");
        taskTable.put("DESCRIPTION", "");
        taskTable.put("NAME", "");


        //TODO EXPENSE REPORT

        TreeMap<String, String> ERcustomProductTable = new TreeMap<>();

        ERcustomProductTable.put("TAX_AMOUNT", "");
        ERcustomProductTable.put("UNIT_PRICE", "");
        ERcustomProductTable.put("EXP_BILL_TO", "");
        ERcustomProductTable.put("QTY_HRS", "");
        ERcustomProductTable.put("DATE", "");
        ERcustomProductTable.put("NAME", "");
        ERcustomProductTable.put("BASE_TOTAL", "");
        ERcustomProductTable.put("DEPARTMENT", "");
        ERcustomProductTable.put("TOTAL_AMOUNT", "");
        ERcustomProductTable.put("NO", "");
        ERcustomProductTable.put("PROJECT_BASE_ITEM", "");
        ERcustomProductTable.put("PARENT_CODE", "");
        ERcustomProductTable.put("CURRENCY", "");
        ERcustomProductTable.put("ACCOUNT_CODE", "");
        ERcustomProductTable.put("DESCRIPTION", "");

        TreeMap<String, String> ERcustomTotalTable = new TreeMap<>();
        ERcustomTotalTable.put("EXP_TOTAL", "");
        ERcustomTotalTable.put("EXP_TOTAL_WORD", "");
        ERcustomTotalTable.put("TAX_TOTAL", "");
        ERcustomTotalTable.put("DUE_AMOUNT", "");
        ERcustomTotalTable.put("TOTAL_WORD", "");
        ERcustomTotalTable.put("TOTAL", "");
        ERcustomTotalTable.put("SUBTOTAL", "");
        ERcustomTotalTable.put("EXP_LESS_PAYMENTd", "");

        TreeMap<String, String> ERcustomNumberAndDatesTable = new TreeMap<>();
        ERcustomNumberAndDatesTable.put("EXP_NUMBER", "");
        ERcustomNumberAndDatesTable.put("START_DATE", "");
        ERcustomNumberAndDatesTable.put("PERIOD_START_DATE", "");
        ERcustomNumberAndDatesTable.put("PERIOD_END_DATE", "");
        ERcustomNumberAndDatesTable.put("EXP_DESCRIPTION", "");
        ERcustomNumberAndDatesTable.put("AMOUNTS", "");
        ERcustomNumberAndDatesTable.put("PROJECT", "");
        ERcustomNumberAndDatesTable.put("STATUS", "");
        ERcustomNumberAndDatesTable.put("TAX_TYPE_NAME", "");
        ERcustomNumberAndDatesTable.put("PAID_FROM", "");
        ERcustomNumberAndDatesTable.put("CURRENCY_SYMBOL", "");
        ERcustomNumberAndDatesTable.put("BASE_CURRENCY", "");
        ERcustomNumberAndDatesTable.put("CURRENCY", "");

        TreeMap<String, String> ERcustomBillToAddress = new TreeMap<>();
        ERcustomBillToAddress.put("REPORTER_NUMBER", "");
        ERcustomBillToAddress.put("REPORTER_EMAIL", "");
        ERcustomBillToAddress.put("APPROVER", "");
        ERcustomBillToAddress.put("SUPPLIER", "");
        ERcustomBillToAddress.put("TITLE", "");
        ERcustomBillToAddress.put("REPORTER", "");

        TreeMap<String, String> ERpaymentHistoryTable = new TreeMap<>();
        ERpaymentHistoryTable.put("APPROVERS_DATES", "");
        ERpaymentHistoryTable.put("APPROVERS", "");


        //TODO PAY BILL

        TreeMap<String, String> PBcustomProductTable = new TreeMap<>();
        PBcustomProductTable.put("AMOUNT", "");
        PBcustomProductTable.put("NET_AMOUNT", "");
        PBcustomProductTable.put("CURRENCY", "");
        PBcustomProductTable.put("BASE_AMOUNT", "");
        PBcustomProductTable.put("INVOICE_ITEM_UNIT_PRICE", "");
        PBcustomProductTable.put("DUE_AMOUNT", "");
        PBcustomProductTable.put("ACCOUNT_CODE", "");
        PBcustomProductTable.put("DISCOUNT_AMOUNT", "");
        PBcustomProductTable.put("PO_NUMBER", "");
        PBcustomProductTable.put("TOTAL_AMOUNT", "");
        PBcustomProductTable.put("NAME", "");
        PBcustomProductTable.put("INVOICE_ITEM_QTY", "");
        PBcustomProductTable.put("INV_DUE_DATE", "");
        PBcustomProductTable.put("INV_DATE", "");
        PBcustomProductTable.put("INV_NUMBER", "");
        PBcustomProductTable.put("INVOICE_PRODUCT_NAME", "");
        PBcustomProductTable.put("PROJECT_NUMBER", "");
        PBcustomProductTable.put("INVOICE_ITEM_TOTAL_AMOUNT", "");
        PBcustomProductTable.put("PAYMENT_REFERENCE", "");
        PBcustomProductTable.put("PARENT_PROJECT", "");
        PBcustomProductTable.put("ITEM_SUB_PROJECT", "");
        PBcustomProductTable.put("PAYMENT_BASE_AMOUNT", "");
        PBcustomProductTable.put("INVOICE_ITEM_NET_AMOUNT", "");
        PBcustomProductTable.put("PROJECT_NAME", "");
        PBcustomProductTable.put("ACCOUNT_NAME", "");
        PBcustomProductTable.put("ITEM_TAX_OF_PAYMENT_AMOUNT", "");
        PBcustomProductTable.put("INVOICE_ITEM_TAX_RATE", "");
        PBcustomProductTable.put("INVOICE_ITEM_TAX_AMOUNT", "");
        PBcustomProductTable.put("ITEM_TOTAL_PAYMENT_AMOUNT", "");
        PBcustomProductTable.put("NO", "");


        TreeMap<String, String> PBcustomBillToAddress = new TreeMap<>();
        PBcustomBillToAddress.put("DEPARTMENT", "");
        PBcustomBillToAddress.put("BANK_ACCOUNT_NUMBER", "");
        PBcustomBillToAddress.put("ACCOUNT", "");
        PBcustomBillToAddress.put("CURRENCY", "");
        PBcustomBillToAddress.put("CRM_ACCOUNT", "");
        PBcustomBillToAddress.put("DESCRIPTION", "");
        PBcustomBillToAddress.put("TITLE", "");
        PBcustomBillToAddress.put("BANK_ACCOUNT_CODE", "");
        PBcustomBillToAddress.put("TOTAL", "");
        PBcustomBillToAddress.put("DATE", "");
        PBcustomBillToAddress.put("PAYMENT_TYPE", "");
        PBcustomBillToAddress.put("PROJECT_NUMBER", "");
        PBcustomBillToAddress.put("NUMBER", "");
        PBcustomBillToAddress.put("REFERENCE", "");
        PBcustomBillToAddress.put("BASE_CURRENCY", "");
        PBcustomBillToAddress.put("PROJECT_NAME", "");
        PBcustomBillToAddress.put("CLIENT_REGISTRATION_NUMBER", "");
        PBcustomBillToAddress.put("CLIENT_CODE", "");
        PBcustomBillToAddress.put("DUE_AMOUNT", "");
        PBcustomBillToAddress.put("TOTAL_WORD", "");
        PBcustomBillToAddress.put("TOTAL_WORD_ALL", "");
        PBcustomBillToAddress.put("TOTAL_AMOUNT", "");
        PBcustomBillToAddress.put("INVOICE_NUMBERS", "");
        PBcustomBillToAddress.put("TOTAL_ARABIC_WORD_ALL", "");
        PBcustomBillToAddress.put("ACCOUNT_CODE", "");

        TreeMap<String, String> PBcustomBankTable = new TreeMap<>();
        PBcustomBankTable.put("BANK_ACCOUNT_CITY", "");
        PBcustomBankTable.put("BANK_ACCOUNT_POSTCODE", "");
        PBcustomBankTable.put("CURRENCY", "");
        PBcustomBankTable.put("BANK_NAME", "");
        PBcustomBankTable.put("BILL_ADDRESS", "");
        PBcustomBankTable.put("BANK_ACCOUNT_STATE", "");
        PBcustomBankTable.put("BANK_ACCOUNT_COUNTRY", "");
        PBcustomBankTable.put("STREET_ADDRESS", "");

        TreeMap<String, String> PBcustomAccountTable = new TreeMap<>();
        PBcustomAccountTable.put("ACCOUNT_NAME", "");
        PBcustomAccountTable.put("ACCOUNT_NUMBER", "");
        PBcustomAccountTable.put("SWIFT_BIC", "");

        //TODO PREPAYMENT

        TreeMap<String, String> PrePaymentcustomBillToAddress = new TreeMap<>();
        PrePaymentcustomBillToAddress.put("EMAIL", "");
        PrePaymentcustomBillToAddress.put("PHONE", "");
        PrePaymentcustomBillToAddress.put("CRM_ACCOUNT", "");

        TreeMap<String, String> appliedDocumentsTable = new TreeMap<>();
        appliedDocumentsTable.put("DOCUMENT", "");

        TreeMap<String, String> applyCreditData = new TreeMap<>();
        applyCreditData.put("APPLIED_AMOUNT", "");
        applyCreditData.put("SUPPLIER", "");
        applyCreditData.put("INVOICE_NUMBER", "");
        applyCreditData.put("INVOICE_DATE", "");
        applyCreditData.put("DUE_DATE", "");
        applyCreditData.put("INVOICE_TOTAL", "");

        TreeMap<String, String> paymentTable = new TreeMap<>();
        paymentTable.put("PROJECT", "");
        paymentTable.put("ACCOUNT", "");
        paymentTable.put("RECEIVABLE_PAYABLE", "");
        paymentTable.put("NOTE", "");
        paymentTable.put("NUMBER", "");
        paymentTable.put("REFERENCE", "");
        paymentTable.put("AMOUNT", "");
        paymentTable.put("DATE", "");
        paymentTable.put("CURRENCY", "");
        paymentTable.put("AMOUNT_IN_WORD", "");
        paymentTable.put("CREATOR", "");
        paymentTable.put("APPLIED_TOTAL", "");

        //TODO SUPPLIER CREDIT

        TreeMap<String, String> SPcustomBillToAddress = new TreeMap<>();
        SPcustomBillToAddress.put("CRM_ACCOUNT", "");
        SPcustomBillToAddress.put("PHONE", "");
        SPcustomBillToAddress.put("EMAIL", "");

        TreeMap<String, String> SPapplyCreditData = new TreeMap<>();

        SPapplyCreditData.put("INVOICE_NUMBER", "");
        SPapplyCreditData.put("DUE_DATE", "");
        SPapplyCreditData.put("INVOICE_TOTAL", "");
        SPapplyCreditData.put("SUPPLIER", "");
        SPapplyCreditData.put("APPLIED_AMOUNT", "");
        SPapplyCreditData.put("INVOICE_DATE", "");

        TreeMap<String, String> SPpaymentTable = new TreeMap<>();
        SPpaymentTable.put("CREATOR", "");
        SPpaymentTable.put("NOTE", "");
        SPpaymentTable.put("PROJECT", "");
        SPpaymentTable.put("NUMBER", "");
        SPpaymentTable.put("RECEIVABLE_PAYABLE", "");
        SPpaymentTable.put("ACCOUNT", "");
        SPpaymentTable.put("CURRENCY", "");
        SPpaymentTable.put("AMOUNT_IN_WORD", "");
        SPpaymentTable.put("APPLIED_TOTAL", "");
        SPpaymentTable.put("AMOUNT", "");
        SPpaymentTable.put("REFERENCE", "");
        SPpaymentTable.put("DATE", "");


        //TODO PACKING SLIP
        TreeMap<String, String> PScustomProductTable = new TreeMap<>();
        PScustomProductTable.put("ITEM_PART_NUMBER", "");
        PScustomProductTable.put("DEPARTMENT", "");
        PScustomProductTable.put("PARENT_ACCOUNT", "");
        PScustomProductTable.put("DOUBLE_TAX_LABEL", "");
        PScustomProductTable.put("UNIT_PRICE_DISCOUNTED", "");
        PScustomProductTable.put("ITEM_PROFIT_IC", "");
        PScustomProductTable.put("ITEM_TOTAL_SALES_PRICE", "");
        PScustomProductTable.put("PICK_ITEM_REFERENCE", "");
        PScustomProductTable.put("UNIT_PRICE", "");
        PScustomProductTable.put("PICK_ITEM_LAST_SHIPPED_QTY", "");
        PScustomProductTable.put("PICK_ITEM_QTY_PER_PACK", "");
        PScustomProductTable.put("UNIT_MEASUREMENT", "");
        PScustomProductTable.put("ITEM_BRAND_NAME", "");
        PScustomProductTable.put("ITEM_PICTURE", "");
        PScustomProductTable.put("ITEM_IS_PICKABLE", "");
        PScustomProductTable.put("TOTAL_AMOUNT_IN_BASE", "");
        PScustomProductTable.put("ITEM_BACK_ORDERED", "");
        PScustomProductTable.put("ITEM_BATCH_EXPIRE_DATE", "");
        PScustomProductTable.put("ITEM_PROJECT", "");
        PScustomProductTable.put("ITEM_PRODUCT_SERIAL", "");
        PScustomProductTable.put("TAX_AMOUNT", "");
        PScustomProductTable.put("ACCOUNT_NUMBER", "");
        PScustomProductTable.put("ITEM_ORIGINAL_PRICE", "");
        PScustomProductTable.put("NET_AMOUNT", "");
        PScustomProductTable.put("TAX_RATE", "");
        PScustomProductTable.put("ITEM_QUOTE_NUMBER", "");
        PScustomProductTable.put("ITEM_WAREHOUSE", "");
        PScustomProductTable.put("TOTAL_AMOUNT", "");
        PScustomProductTable.put("TAX_LABEL", "");
        PScustomProductTable.put("PREV_INVOICES_PRODUCT_QTY", "");
        PScustomProductTable.put("ITEM_PRODUCT_LOT_NUMBER", "");
        PScustomProductTable.put("QTY_ON_HAND", "");
        PScustomProductTable.put("ITEM_QTY", "");
        PScustomProductTable.put("ITEM_DOUBLE_TAX_AMOUNT", "");
        PScustomProductTable.put("PICK_ITEM_SHIPPED_QTY", "");
        PScustomProductTable.put("ITEM_PRODUCT_EXPIRATION_DATE", "");
        PScustomProductTable.put("ITEM_TOTAL_COST_PRICE", "");
        PScustomProductTable.put("UNIT_MEASUREMENT_DESCRIPTION", "");
        PScustomProductTable.put("ITEM_VENDOR", "");
        PScustomProductTable.put("ITEM_CATEGORY", "");
        PScustomProductTable.put("ITEM_MANUFACTURER", "");
        PScustomProductTable.put("ITEM_BARCODE", "");
        PScustomProductTable.put("ITEM_NET_PROFIT", "");
        PScustomProductTable.put("ITEM_BATCH_QTY", "");
        PScustomProductTable.put("ACCOUNT", "");
        PScustomProductTable.put("PICK_ITEM_NUMBER_PACKS", "");
        PScustomProductTable.put("ITEM_QUOTE_QUANTITY_ORDERED", "");
        PScustomProductTable.put("ORDERED_PRODUCT_QTY", "");
        PScustomProductTable.put("ITEM_COST_PRICE", "");
        PScustomProductTable.put("ITEM_PRODUCT_LOT_NUMBER_QTY", "");
        PScustomProductTable.put("ITEM_REVERSED_QTY", "");
        PScustomProductTable.put("ORDERED_QTY", "");
        PScustomProductTable.put("PRODUCT_NAME", "");
        PScustomProductTable.put("QTY_HRS", "QUANTITY");
        PScustomProductTable.put("DISCOUNT_TYPE", "");
        PScustomProductTable.put("DISCOUNT_AMOUNT", "");
        PScustomProductTable.put("NAME", "");
        PScustomProductTable.put("ITEM_SUB_PROJECT", "");
        PScustomProductTable.put("ITEM_RECIEVE", "");
        PScustomProductTable.put("ITEM_DOUBLE_TAX_RATE", "");
        PScustomProductTable.put("ITEM_TYPE", "");
        PScustomProductTable.put("DISCOUNT", "");
        PScustomProductTable.put("ITEM_BATCH_SERIAL_NUMBER", "");
        PScustomProductTable.put("NET_AMOUNT_IN_BASE", "");
        PScustomProductTable.put("ITEM_SKU_NUMBER", "");
        PScustomProductTable.put("TAX_AMOUNT_IN_BASE", "");
        PScustomProductTable.put("UNIT_PRICE_IN_BASE", "");
        PScustomProductTable.put("NET_WITHOUT_DISCOUNT", "");
        PScustomProductTable.put("DESCRIPTION", "");
        PScustomProductTable.put("NO", "NUMBER");
        PScustomProductTable.put("UNIT_PRICE_AVERAGE", "");


        TreeMap<String, String> PScustomTotalTable = new TreeMap<>();
        PScustomTotalTable.put("BTW_MAX_TOTAL", "");
        PScustomTotalTable.put("LAST_PAYMENT", "");
        PScustomTotalTable.put("TOTAL", "");
        PScustomTotalTable.put("TOTAL_ARABIC_WORD_ALL", "");
        PScustomTotalTable.put("DISCOUNT_TOTAL_WORD_ALL", "");
        PScustomTotalTable.put("TOTAL_IN_USD_WORD", "");
        PScustomTotalTable.put("HAS_BILL_EXP_TOTAL", "");
        PScustomTotalTable.put("EINDTOTAL", "");
        PScustomTotalTable.put("BILL_EXP_TOTAL", "");
        PScustomTotalTable.put("SUBTOTAL_WORD_ALL", "");
        PScustomTotalTable.put("SUBTOTAL_IN_BASE", "");
        PScustomTotalTable.put("TOTAL_UZB_WORD_ALL_LOTIN", "");
        PScustomTotalTable.put("TOTAL_QUONTITY", "");
        PScustomTotalTable.put("TAX_TOTAL", "");
        PScustomTotalTable.put("TOTAL_WORD_ALL_WITH_CENT", "");
        PScustomTotalTable.put("INVOICE_QUOTE_UNREC_REVENUE_TOTAL", "");
        PScustomTotalTable.put("DISCOUNT_TOTAL", "");
        PScustomTotalTable.put("BTW_TOTAL", "");
        PScustomTotalTable.put("PAYMENT_TOTAL", "");
        PScustomTotalTable.put("TOTAL_UZB_WORD_ALL", "");
        PScustomTotalTable.put("EXCHANGE_RATE_AED", "");
        PScustomTotalTable.put("EXCHANGE_RATE_REVERSE", "");
        PScustomTotalTable.put("TOTAL_AMOUNT_AED", "");
        PScustomTotalTable.put("DUE_AMOUNT_WORD", "");
        PScustomTotalTable.put("TOTAL_IN_BASE", "");
        PScustomTotalTable.put("SUBTOTAL_WORD", "");
        PScustomTotalTable.put("TOTAL_WORD_ALL", "");
        PScustomTotalTable.put("EIND_SUBTOTAL", "");
        PScustomTotalTable.put("DUE_AMOUNT_ARABIC_WORD", "");
        PScustomTotalTable.put("PAYMENT_0", "");
        PScustomTotalTable.put("TOTAL_OVERALL_DISCOUNT", "");
        PScustomTotalTable.put("TAX_TOTAL_IN_BASE", "");
        PScustomTotalTable.put("TOTAL_IN_BASE_WORD", "");
        PScustomTotalTable.put("DUE_AMOUNT", "");
        PScustomTotalTable.put("TOTAL_WORD", "");
        PScustomTotalTable.put("TAX_0", "TAX");
        PScustomTotalTable.put("SUBTOTAL", "");
        PScustomTotalTable.put("BILL_EXP_TAX_TOTAL", "");
        PScustomTotalTable.put("DISCOUNTED_SUBTOTAL", "");
        PScustomTotalTable.put("BTW_MIN_TOTAL", "");
        PScustomTotalTable.put("EXCHANGE_RATE", "");

        TreeMap<String, String> PScustomExpenseTable = new TreeMap<>();

        PScustomExpenseTable.put("ITEM_EXPENSE_MARKUP_TAX_AMOUNT", "");
        PScustomExpenseTable.put("ITEM_EXPENSE_CURRENCY", "");
        PScustomExpenseTable.put("ITEM_EXPENSE_TOTAL_AMOUNT", "");
        PScustomExpenseTable.put("ITEM_EXPENSE_DESCRIPTION", "");
        PScustomExpenseTable.put("ITEM_EXPENSE_CATEGORY", "");
        PScustomExpenseTable.put("ITEM_EXPENSE_TOTAL_WITH_MARKUP", "");
        PScustomExpenseTable.put("ITEM_EXPENSE_MARKUP_AMOUNT", "");
        PScustomExpenseTable.put("ITEM_EXPENSE_BEDRAG_AMOUNT", "");
        PScustomExpenseTable.put("ITEM_EXPENSE_DATE", "");
        PScustomExpenseTable.put("ITEM_EXPENSE_MARKUP_TAX_RATE", "");

        TreeMap<String, String> PScustomNumberAndDatesTable = new TreeMap<>();
        PScustomNumberAndDatesTable.put("LAST_UPDATED_DATE", "");
        PScustomNumberAndDatesTable.put("INV_TYPE", "INVOICE TYPE");
        PScustomNumberAndDatesTable.put("INV_NUMBER", "INVOICE NUMBER");
        PScustomNumberAndDatesTable.put("INVOICE_STATUS", "");
        PScustomNumberAndDatesTable.put("REFERENCE", "");
        PScustomNumberAndDatesTable.put("INV_DUE_DATE", "INVOICE DUE DATE");
        PScustomNumberAndDatesTable.put("FROM_QUOTE_NUMBER", "");
        PScustomNumberAndDatesTable.put("CUSTOMER_BALANCE", "");
        PScustomNumberAndDatesTable.put("SALE_INVOICE_APPROVER", "");
        PScustomNumberAndDatesTable.put("QUOTATION_DATE", "");
        PScustomNumberAndDatesTable.put("RECEIPT_NO", "RECEIPT NUMBER");
        PScustomNumberAndDatesTable.put("PAYMENT_DATE", "");
        PScustomNumberAndDatesTable.put("SHIPPING_METHOD", "");
        PScustomNumberAndDatesTable.put("PO_NUMBER", "");
        PScustomNumberAndDatesTable.put("COMP_VAT_NUMBER", "");
        PScustomNumberAndDatesTable.put("INVOICE_DUE_TERMS", "");
        PScustomNumberAndDatesTable.put("INV_DATE", "");
        PScustomNumberAndDatesTable.put("QT_NUMBER", "");
        PScustomNumberAndDatesTable.put("PERIOD", "");
        PScustomNumberAndDatesTable.put("CREATION_DATE", "");
        PScustomNumberAndDatesTable.put("PREVIEW", "");
        PScustomNumberAndDatesTable.put("TAX_CODE", "");
        PScustomNumberAndDatesTable.put("LAST_UPDATER", "");


        TreeMap<String, String> PScustomBillToAddress = new TreeMap<>();

        PScustomBillToAddress.put("BILL_ZIPCODE", "");
        PScustomBillToAddress.put("CONTACT_EMAIL", "");
        PScustomBillToAddress.put("CLIENT_BANK_ACCOUNT_NO", "");
        PScustomBillToAddress.put("CONTACT_PHONE", "");
        PScustomBillToAddress.put("CONTACT_MIDDLE_NAME", "");
        PScustomBillToAddress.put("COMP_MAIL_ADDRESS", "");
        PScustomBillToAddress.put("CLIENT_BANK_SORT_CODE", "");
        PScustomBillToAddress.put("CLIENT_BANK_IBAN_CODE", "");
        PScustomBillToAddress.put("MAIL_CITY", "");
        PScustomBillToAddress.put("COMP_BILL_ADDRESS2", "");
        PScustomBillToAddress.put("USER_PHONE", "");
        PScustomBillToAddress.put("BILL_ADDRESS_NAME", "");
        PScustomBillToAddress.put("PAYMENT_METHOD", "");
        PScustomBillToAddress.put("COMP_BILL_STATE", "");
        PScustomBillToAddress.put("COMP_MAIL_CITY", "");
        PScustomBillToAddress.put("CLIENT_OWNER", "");
        PScustomBillToAddress.put("CONTACT_JOB_TITLE", "");
        PScustomBillToAddress.put("BILL_CITY", "");
        PScustomBillToAddress.put("ACCOUNT_OWNER", "");
        PScustomBillToAddress.put("USER_L_NAME", "");
        PScustomBillToAddress.put("CLIENT_VAT_NUMBER", "");
        PScustomBillToAddress.put("BILL_COUNTRY", "");
        PScustomBillToAddress.put("CLIENT_CONTACT", "");
        PScustomBillToAddress.put("CLIENT_BANK_BRANCH", "");
        PScustomBillToAddress.put("MAIL_COUNTRY", "");
        PScustomBillToAddress.put("MAIL_STATE", "");
        PScustomBillToAddress.put("CLIENT_BANK_ADDRESS", "");
        PScustomBillToAddress.put("CLIENT_FAX", "");
        PScustomBillToAddress.put("COMP_BILL_ADDRESS", "COMPANY BILL ADDRESS");
        PScustomBillToAddress.put("MAIL_ADDRESS", "");
        PScustomBillToAddress.put("PARENT_ACCOUNT", "");
        PScustomBillToAddress.put("MAIL_ADDRESS_NAME", "");
        PScustomBillToAddress.put("COMP_BILL_CITY", "COMPANY BILL CITY");
        PScustomBillToAddress.put("CLIENT_BANK_SWIFT_CODE", "");
        PScustomBillToAddress.put("CLIENT_REGISTRATION_NUMBER", "");
        PScustomBillToAddress.put("MAIL_COUNTRY_EU_MEMBER", "");
        PScustomBillToAddress.put("CLIENT_BANK_NAME", "");
        PScustomBillToAddress.put("COMP_MAIL_COUNTRY", "");
        PScustomBillToAddress.put("USER_F_NAME", "USER FIRST NAME");
        PScustomBillToAddress.put("COMP_MAIL_STATE", "");
        PScustomBillToAddress.put("MAIL_ADDRESS2", "");
        PScustomBillToAddress.put("USER_M_NAME", "USER MIDDLE NAME");
        PScustomBillToAddress.put("COMP_BILL_COUNTRY", "");
        PScustomBillToAddress.put("CLIENT_BANK_ACCOUNT_NAME", "");
        PScustomBillToAddress.put("CLIENT_CODE", "");
        PScustomBillToAddress.put("CLIENT_PHONE", "");
        PScustomBillToAddress.put("CONTACT_LAST_NAME", "");
        PScustomBillToAddress.put("USERNAME", "");
        PScustomBillToAddress.put("COMP_MAIL_ZIPCODE", "");
        PScustomBillToAddress.put("BILL_ADDRESS", "");
        PScustomBillToAddress.put("CLIENT_EMAIL", "");
        PScustomBillToAddress.put("CLIENT_WEBSITE", "");
        PScustomBillToAddress.put("COMP_MAIL_ADDRESS2", "");
        PScustomBillToAddress.put("CLIENT_TAX_TREATMENT", "");
        PScustomBillToAddress.put("MAIL_ZIPCODE", "");
        PScustomBillToAddress.put("BILL_STATE", "");
        PScustomBillToAddress.put("CLIENT_CURRENCY", "");
        PScustomBillToAddress.put("BILL_COUNTRY_EU_MEMBER", "");
        PScustomBillToAddress.put("COMP_BILL_ZIPCODE", "");
        PScustomBillToAddress.put("BILL_ADDRESS2", "");
        PScustomBillToAddress.put("CONTACT_FIRST_NAME", "");
        PScustomBillToAddress.put("USER_EMAIL", "");
        PScustomBillToAddress.put("NAME", "");

        TreeMap<String, String> PScustomBankTable = new TreeMap<>();
        PScustomBankTable.put("CURRENCY", "");
        PScustomBankTable.put("BILL_ADDRESS", "");
        PScustomBankTable.put("BANK_ACCOUNT_COUNTRY", "");
        PScustomBankTable.put("BANK_ACCOUNT_POSTCODE", "");
        PScustomBankTable.put("BANK_ACCOUNT_STATE", "");
        PScustomBankTable.put("BANK_ACCOUNT_CITY", "");
        PScustomBankTable.put("STREET_ADDRESS", "");
        PScustomBankTable.put("BANK_NAME", "");

        TreeMap<String, String> PScustomAccountTable = new TreeMap<>();
        PScustomAccountTable.put("ACCOUNT_CODE", "");
        PScustomAccountTable.put("SWIFT_BIC", "");
        PScustomAccountTable.put("ACCOUNT_NAME", "");
        PScustomAccountTable.put("ACCOUNT_NUMBER", "");


        //TODO PACKING SLIP ORDER
        TreeMap<String, String> PSOcustomProductTable = new TreeMap<>();
        PSOcustomProductTable.put("ACCOUNT", "");
        PSOcustomProductTable.put("ITEM_BATCH_EXPIRE_DATE", "");
        PSOcustomProductTable.put("PARENT_ACCOUNT", "");
        PSOcustomProductTable.put("ITEM_VENDOR", "");
        PSOcustomProductTable.put("ITEM_ORIGINAL_PRICE", "");
        PSOcustomProductTable.put("ITEM_BACK_ORDERED", "");
        PSOcustomProductTable.put("PICK_ITEM_LAST_SHIPPED_QTY", "");
        PSOcustomProductTable.put("ITEM_SUB_PROJECT", "");
        PSOcustomProductTable.put("ACCOUNT_NUMBER", "");
        PSOcustomProductTable.put("DEPARTMENT", "");
        PSOcustomProductTable.put("PICK_ITEM_REFERENCE", "");
        PSOcustomProductTable.put("ITEM_BATCH_SERIAL_NUMBER", "");
        PSOcustomProductTable.put("TOTAL_AMOUNT", "");
        PSOcustomProductTable.put("ITEM_BATCH_QTY", "");
        PSOcustomProductTable.put("ITEM_PROJECT", "");
        PSOcustomProductTable.put("NET_AMOUNT_IN_BASE", "");
        PSOcustomProductTable.put("ITEM_QTY", "ITEM QUANTITY");
        PSOcustomProductTable.put("QTY_ON_HAND", "");
        PSOcustomProductTable.put("DISCOUNT_AMOUNT", "");
        PSOcustomProductTable.put("ITEM_COST_PRICE", "");
        PSOcustomProductTable.put("ITEM_DOUBLE_TAX_AMOUNT", "");
        PSOcustomProductTable.put("NET_AMOUNT", "");
        PSOcustomProductTable.put("ITEM_NET_PROFIT", "");
        PSOcustomProductTable.put("ITEM_BARCODE", "");
        PSOcustomProductTable.put("ORDERED_QTY", "");
        PSOcustomProductTable.put("ITEM_PICTURE", "");
        PSOcustomProductTable.put("UNIT_PRICE_DISCOUNTED", "");
        PSOcustomProductTable.put("ITEM_BRAND_NAME", "");
        PSOcustomProductTable.put("ITEM_PART_NUMBER", "");
        PSOcustomProductTable.put("UNIT_PRICE", "");
        PSOcustomProductTable.put("ITEM_SKU_NUMBER", "");
        PSOcustomProductTable.put("DESCRIPTION", "");
        PSOcustomProductTable.put("DISCOUNT_TYPE", "");
        PSOcustomProductTable.put("NO", "NUMBER");
        PSOcustomProductTable.put("ITEM_QUOTE_QUANTITY_ORDERED", "");
        PSOcustomProductTable.put("ITEM_PRODUCT_EXPIRATION_DATE", "");
        PSOcustomProductTable.put("UNIT_PRICE_AVERAGE", "");
        PSOcustomProductTable.put("UNIT_MEASUREMENT", "");
        PSOcustomProductTable.put("TAX_LABEL", "");
        PSOcustomProductTable.put("DISCOUNT", "");
        PSOcustomProductTable.put("ITEM_PRODUCT_LOT_NUMBER_QTY", "");
        PSOcustomProductTable.put("ITEM_WAREHOUSE", "");
        PSOcustomProductTable.put("UNIT_MEASUREMENT_DESCRIPTION", "");
        PSOcustomProductTable.put("TAX_RATE", "");
        PSOcustomProductTable.put("ORDERED_PRODUCT_QTY", "");
        PSOcustomProductTable.put("QTY_HRS", "");
        PSOcustomProductTable.put("ITEM_TYPE", "");
        PSOcustomProductTable.put("ITEM_QUOTE_NUMBER", "");
        PSOcustomProductTable.put("PICK_ITEM_QTY_PER_PACK", "");
        PSOcustomProductTable.put("UNIT_PRICE_IN_BASE", "");
        PSOcustomProductTable.put("NET_WITHOUT_DISCOUNT", "");
        PSOcustomProductTable.put("PICK_ITEM_NUMBER_PACKS", "");
        PSOcustomProductTable.put("ITEM_PROFIT_IC", "");
        PSOcustomProductTable.put("ITEM_TOTAL_SALES_PRICE", "");
        PSOcustomProductTable.put("NAME", "");
        PSOcustomProductTable.put("TOTAL_AMOUNT_IN_BASE", "");
        PSOcustomProductTable.put("ITEM_RECIEVE", "");
        PSOcustomProductTable.put("TAX_AMOUNT_IN_BASE", "");
        PSOcustomProductTable.put("PICK_ITEM_SHIPPED_QTY", "");
        PSOcustomProductTable.put("ITEM_REVERSED_QTY", "");
        PSOcustomProductTable.put("ITEM_MANUFACTURER", "");
        PSOcustomProductTable.put("TAX_AMOUNT", "");
        PSOcustomProductTable.put("ITEM_IS_PICKABLE", "");
        PSOcustomProductTable.put("ITEM_PRODUCT_SERIAL", "");
        PSOcustomProductTable.put("ITEM_PRODUCT_LOT_NUMBER", "");
        PSOcustomProductTable.put("ITEM_DOUBLE_TAX_RATE", "");
        PSOcustomProductTable.put("ITEM_TOTAL_COST_PRICE", "");
        PSOcustomProductTable.put("ITEM_CATEGORY", "");
        PSOcustomProductTable.put("PRODUCT_NAME", "");
        PSOcustomProductTable.put("PREV_INVOICES_PRODUCT_QTY", "");
        PSOcustomProductTable.put("DOUBLE_TAX_LABEL", "");


        TreeMap<String, String> PSOcustomTotalTable = new TreeMap<>();

        PSOcustomTotalTable.put("LAST_PAYMENT", "");
        PSOcustomTotalTable.put("BTW_MIN_TOTAL", "");
        PSOcustomTotalTable.put("EIND_SUBTOTAL", "");
        PSOcustomTotalTable.put("TOTAL_IN_USD_WORD", "");
        PSOcustomTotalTable.put("EXCHANGE_RATE", "");
        PSOcustomTotalTable.put("TAX_TOTAL", "");
        PSOcustomTotalTable.put("BILL_EXP_TOTAL", "");
        PSOcustomTotalTable.put("EINDTOTAL", "");
        PSOcustomTotalTable.put("EXCHANGE_RATE_REVERSE", "");
        PSOcustomTotalTable.put("BTW_TOTAL", "");
        PSOcustomTotalTable.put("TOTAL_WORD", "");
        PSOcustomTotalTable.put("DUE_AMOUNT_ARABIC_WORD", "");
        PSOcustomTotalTable.put("DISCOUNT_TOTAL_WORD_ALL", "");
        PSOcustomTotalTable.put("DUE_AMOUNT", "");
        PSOcustomTotalTable.put("SUBTOTAL_WORD_ALL", "");
        PSOcustomTotalTable.put("TOTAL_WORD_ALL_WITH_CENT", "");
        PSOcustomTotalTable.put("TOTAL_OVERALL_DISCOUNT", "");
        PSOcustomTotalTable.put("TOTAL_UZB_WORD_ALL", "");
        PSOcustomTotalTable.put("TOTAL", "");
        PSOcustomTotalTable.put("HAS_BILL_EXP_TOTAL", "");
        PSOcustomTotalTable.put("EXCHANGE_RATE_AED", "");
        PSOcustomTotalTable.put("INVOICE_QUOTE_UNREC_REVENUE_TOTAL", "");
        PSOcustomTotalTable.put("TAX_TOTAL_IN_BASE", "");
        PSOcustomTotalTable.put("BTW_MAX_TOTAL", "");
        PSOcustomTotalTable.put("TOTAL_UZB_WORD_ALL_LOTIN", "");
        PSOcustomTotalTable.put("SUBTOTAL_IN_BASE", "");
        PSOcustomTotalTable.put("TOTAL_IN_BASE", "");
        PSOcustomTotalTable.put("DUE_AMOUNT_WORD", "");
        PSOcustomTotalTable.put("TOTAL_WORD_ALL", "");
        PSOcustomTotalTable.put("TOTAL_QUONTITY", "");
        PSOcustomTotalTable.put("TOTAL_ARABIC_WORD_ALL", "");
        PSOcustomTotalTable.put("SUBTOTAL_WORD", "");
        PSOcustomTotalTable.put("PAYMENT_TOTAL", "");
        PSOcustomTotalTable.put("TOTAL_AMOUNT_AED", "");
        PSOcustomTotalTable.put("DISCOUNTED_SUBTOTAL", "");
        PSOcustomTotalTable.put("TOTAL_IN_BASE_WORD", "");
        PSOcustomTotalTable.put("BILL_EXP_TAX_TOTAL", "");
        PSOcustomTotalTable.put("SUBTOTAL", "");


        TreeMap<String, String> PSOcustomExpenseTable = new TreeMap<>();
        PSOcustomExpenseTable.put("ITEM_EXPENSE_CATEGORY", "");
        PSOcustomExpenseTable.put("ITEM_EXPENSE_TOTAL_AMOUNT", "");
        PSOcustomExpenseTable.put("ITEM_EXPENSE_DESCRIPTION", "");
        PSOcustomExpenseTable.put("ITEM_EXPENSE_MARKUP_AMOUNT", "");
        PSOcustomExpenseTable.put("ITEM_EXPENSE_TOTAL_WITH_MARKUP", "");
        PSOcustomExpenseTable.put("ITEM_EXPENSE_MARKUP_TAX_AMOUNT", "");
        PSOcustomExpenseTable.put("ITEM_EXPENSE_BEDRAG_AMOUNT", "");
        PSOcustomExpenseTable.put("ITEM_EXPENSE_DATE", "");
        PSOcustomExpenseTable.put("ITEM_EXPENSE_CURRENCY", "");
        PSOcustomExpenseTable.put("ITEM_EXPENSE_MARKUP_TAX_RATE", "");

        TreeMap<String, String> PSOcustomNumberAndDatesTable = new TreeMap<>();

        PSOcustomNumberAndDatesTable.put("INV_DUE_DATE", "");
        PSOcustomNumberAndDatesTable.put("TAX_CODE", "");
        PSOcustomNumberAndDatesTable.put("RECEIPT_NO", "");
        PSOcustomNumberAndDatesTable.put("INV_NUMBER", "");
        PSOcustomNumberAndDatesTable.put("LAST_UPDATED_DATE", "");
        PSOcustomNumberAndDatesTable.put("INVOICE_DUE_TERMS", "");
        PSOcustomNumberAndDatesTable.put("CUSTOMER_BALANCE", "");
        PSOcustomNumberAndDatesTable.put("FROM_QUOTE_NUMBER", "");
        PSOcustomNumberAndDatesTable.put("SALE_INVOICE_APPROVER", "");
        PSOcustomNumberAndDatesTable.put("REFERENCE", "");
        PSOcustomNumberAndDatesTable.put("PO_NUMBER", "");
        PSOcustomNumberAndDatesTable.put("COMP_VAT_NUMBER", "");
        PSOcustomNumberAndDatesTable.put("SHIPPING_METHOD", "");
        PSOcustomNumberAndDatesTable.put("INV_TYPE", "");
        PSOcustomNumberAndDatesTable.put("SALE_QUOTE_APPROVER", "");
        PSOcustomNumberAndDatesTable.put("QT_NUMBER", "");
        PSOcustomNumberAndDatesTable.put("PAYMENT_DATE", "");
        PSOcustomNumberAndDatesTable.put("PREVIEW", "");
        PSOcustomNumberAndDatesTable.put("PERIOD", "");
        PSOcustomNumberAndDatesTable.put("INVOICE_STATUS", "");
        PSOcustomNumberAndDatesTable.put("INV_DATE", "");
        PSOcustomNumberAndDatesTable.put("LAST_UPDATER", "");
        PSOcustomNumberAndDatesTable.put("QUOTATION_DATE", "");
        PSOcustomNumberAndDatesTable.put("CREATION_DATE", "");

        TreeMap<String, String> PSOcustomBillToAddress = new TreeMap<>();

        PSOcustomBillToAddress.put("CONTACT_PHONE", "");
        PSOcustomBillToAddress.put("CLIENT_EMAIL", "");
        PSOcustomBillToAddress.put("NAME", "");
        PSOcustomBillToAddress.put("MAIL_ADDRESS_NAME", "");
        PSOcustomBillToAddress.put("COMP_BILL_COUNTRY", "");
        PSOcustomBillToAddress.put("COMP_BILL_ADDRESS", "");
        PSOcustomBillToAddress.put("CLIENT_CODE", "");
        PSOcustomBillToAddress.put("CLIENT_BANK_NAME", "");
        PSOcustomBillToAddress.put("CONTACT_MIDDLE_NAME", "");
        PSOcustomBillToAddress.put("MAIL_ADDRESS2", "");
        PSOcustomBillToAddress.put("BILL_COUNTRY", "");
        PSOcustomBillToAddress.put("COMP_MAIL_ADDRESS2", "");
        PSOcustomBillToAddress.put("BILL_ZIPCODE", "");
        PSOcustomBillToAddress.put("CLIENT_BANK_SWIFT_CODE", "");
        PSOcustomBillToAddress.put("USER_L_NAME", "");
        PSOcustomBillToAddress.put("COMP_BILL_STATE", "");
        PSOcustomBillToAddress.put("COMP_BILL_ADDRESS2", "");
        PSOcustomBillToAddress.put("CLIENT_FAX", "");
        PSOcustomBillToAddress.put("MAIL_CITY", "");
        PSOcustomBillToAddress.put("CLIENT_PHONE", "");
        PSOcustomBillToAddress.put("USERNAME", "");
        PSOcustomBillToAddress.put("BILL_ADDRESS2", "");
        PSOcustomBillToAddress.put("BILL_COUNTRY_EU_MEMBER", "");
        PSOcustomBillToAddress.put("CLIENT_VAT_NUMBER", "");
        PSOcustomBillToAddress.put("CLIENT_CONTACT", "");
        PSOcustomBillToAddress.put("CLIENT_BANK_ACCOUNT_NO", "");
        PSOcustomBillToAddress.put("MAIL_COUNTRY", "");
        PSOcustomBillToAddress.put("CLIENT_CURRENCY", "");
        PSOcustomBillToAddress.put("CLIENT_WEBSITE", "");
        PSOcustomBillToAddress.put("BILL_ADDRESS", "");
        PSOcustomBillToAddress.put("MAIL_ZIPCODE", "");
        PSOcustomBillToAddress.put("USER_F_NAME", "");
        PSOcustomBillToAddress.put("CLIENT_TAX_TREATMENT", "");
        PSOcustomBillToAddress.put("COMP_MAIL_COUNTRY", "");
        PSOcustomBillToAddress.put("CLIENT_BANK_SORT_CODE", "");
        PSOcustomBillToAddress.put("PARENT_ACCOUNT", "");
        PSOcustomBillToAddress.put("CONTACT_JOB_TITLE", "");
        PSOcustomBillToAddress.put("BILL_STATE", "");
        PSOcustomBillToAddress.put("CONTACT_LAST_NAME", "");
        PSOcustomBillToAddress.put("MAIL_COUNTRY_EU_MEMBER", "");
        PSOcustomBillToAddress.put("COMP_MAIL_ADDRESS", "");
        PSOcustomBillToAddress.put("COMP_BILL_ZIPCODE", "");
        PSOcustomBillToAddress.put("COMP_MAIL_STATE", "");
        PSOcustomBillToAddress.put("CLIENT_BANK_ADDRESS", "");
        PSOcustomBillToAddress.put("PAYMENT_METHOD", "");
        PSOcustomBillToAddress.put("MAIL_ADDRESS", "");
        PSOcustomBillToAddress.put("USER_EMAIL", "");
        PSOcustomBillToAddress.put("CONTACT_EMAIL", "");
        PSOcustomBillToAddress.put("USER_M_NAME", "USER MIDDLE NAME");
        PSOcustomBillToAddress.put("MAIL_STATE", "");
        PSOcustomBillToAddress.put("BILL_ADDRESS_NAME", "");
        PSOcustomBillToAddress.put("CLIENT_BANK_ACCOUNT_NAME", "");
        PSOcustomBillToAddress.put("CLIENT_OWNER", "");
        PSOcustomBillToAddress.put("BILL_CITY", "");
        PSOcustomBillToAddress.put("CLIENT_REGISTRATION_NUMBER", "");
        PSOcustomBillToAddress.put("COMP_BILL_CITY", "");
        PSOcustomBillToAddress.put("CONTACT_FIRST_NAME", "");
        PSOcustomBillToAddress.put("COMP_MAIL_ZIPCODE", "");
        PSOcustomBillToAddress.put("CLIENT_BANK_IBAN_CODE", "");
        PSOcustomBillToAddress.put("ACCOUNT_OWNER", "");

        TreeMap<String, String> PSOcustomBankTable = new TreeMap<>();
        PSOcustomBankTable.put("BILL_ADDRESS", "");
        PSOcustomBankTable.put("BANK_ACCOUNT_COUNTRY", "");
        PSOcustomBankTable.put("BANK_ACCOUNT_CITY", "");
        PSOcustomBankTable.put("CURRENCY", "");
        PSOcustomBankTable.put("BRANCH", "");
        PSOcustomBankTable.put("BANK_ACCOUNT_POSTCODE", "");
        PSOcustomBankTable.put("BANK_NAME", "");
        PSOcustomBankTable.put("STREET_ADDRESS", "");


        TreeMap<String, String> PSOcustomAccountTable = new TreeMap<>();

        PSOcustomAccountTable.put("ACCOUNT_NAME", "");
        PSOcustomAccountTable.put("SWIFT_BIC", "");
        PSOcustomAccountTable.put("IBAN_CODE", "");
        PSOcustomAccountTable.put("ACCOUNT_NUMBER", "");
        PSOcustomAccountTable.put("ACCOUNT_CODE", "");


        //TODO CUSTOMER SUPPLIER BALANCE
        TreeMap<String, String> customerAddress = new TreeMap<>();
        customerAddress.put("CONTACT_PHONE", "");
        customerAddress.put("NAME", "");
        customerAddress.put("BILL_ADDRESS", "");
        customerAddress.put("CLIENT_CONTACT", "");
        customerAddress.put("CLIENT_FAX", "");
        customerAddress.put("CLIENT_PHONE", "");
        customerAddress.put("BILL_ADDRESS_NAME", "");
        customerAddress.put("CONTACT_EMAIL", "");
        customerAddress.put("MAIL_CITY", "");
        customerAddress.put("MAIL_ADDRESS2", "");
        customerAddress.put("MAIL_ADDRESS_NAME", "");
        customerAddress.put("BILL_STATE", "");
        customerAddress.put("BILL_CITY", "");
        customerAddress.put("BILL_ADDRESS2", "");
        customerAddress.put("BILL_COUNTRY", "");
        customerAddress.put("BILL_ZIPCODE", "");
        customerAddress.put("MAIL_ADDRESS", "");
        customerAddress.put("CLIENT_CODE", "");
        customerAddress.put("PAYMENT_TERMS", "");
        customerAddress.put("CLIENT_VAT_NUMBER", "");
        customerAddress.put("MAIL_COUNTRY", "");
        customerAddress.put("MAIL_ZIPCODE", "");
        customerAddress.put("MAIL_STATE", "");


        TreeMap<String, String> CSBcustomData = new TreeMap<>();

        CSBcustomData.put("ROW_AMOUNT", "");
        CSBcustomData.put("NET_AMOUNT", "");
        CSBcustomData.put("CUSTOMER", "");
        CSBcustomData.put("PAYMENT_INVOICE_NUMBER", "");
        CSBcustomData.put("ITEM_TRANSACTION", "");
        CSBcustomData.put("CUSTOM_AMOUNT", "");
        CSBcustomData.put("ITEM_BALANCE_EUR", "");
        CSBcustomData.put("ITEM_BALANCE", "");
        CSBcustomData.put("ITEM_CREDIT", "");
        CSBcustomData.put("ITEM_BALANCE_FOREIGN", "");
        CSBcustomData.put("ITEM_BALANCE_USD", "");
        CSBcustomData.put("UNIT_PRICE", "");
        CSBcustomData.put("INV_NUMBER", "INVOICE NUMBER");
        CSBcustomData.put("DESCRIPTION", "");
        CSBcustomData.put("ITEM_PAYMENTS", "");
        CSBcustomData.put("ITEM_REFERENCE", "");
        CSBcustomData.put("DATE", "");
        CSBcustomData.put("ITEM_DEBIT", "");
        CSBcustomData.put("TRANSACTION_TYPE", "");
        CSBcustomData.put("ITEM_DUE_DATE", "");
        CSBcustomData.put("AMOUNT", "");
        CSBcustomData.put("NAME", "");
        CSBcustomData.put("ITEM_DEBIT_FOREIGN", "");
        CSBcustomData.put("ITEM_CREDIT_FOREIGN", "");
        CSBcustomData.put("ITEM_DEBIT_USD", "");
        CSBcustomData.put("ITEM_CREDIT_USD", "");
        CSBcustomData.put("ITEM_DEBIT_EUR", "");
        CSBcustomData.put("ITEM_CREDIT_EUR", "");


        TreeMap<String, String> CSBagingSummary = new TreeMap<>();

        CSBagingSummary.put("TWO_MONTHLY_AMOUNT", "");
        CSBagingSummary.put("THREE_MONTHLY_AMOUNT", "");
        CSBagingSummary.put("TOTAL_AMOUNT", "");
        CSBagingSummary.put("CURRENT_AMOUNT", "");
        CSBagingSummary.put("WEEKLY_AMOUNT", "");
        CSBagingSummary.put("MONTHLY_AMOUNT", "");
        CSBagingSummary.put("FOUR_MONTHLY_AND_OVER_AMOUNT", "");

        TreeMap<String, String> CSBcurrencyData = new TreeMap<>();
        CSBcurrencyData.put("FOREIGN_CURRENCY_NAME", "");
        CSBcurrencyData.put("FOREIGN_CURRENCY_SYMBOL", "");
        CSBcurrencyData.put("BASE_CURRENCY_NAME", "");
        CSBcurrencyData.put("BASE_CURRENCY_SYMBOL", "");

        //TODO GDN
        TreeMap<String, String> GDNtotalTable = new TreeMap<>();
        GDNtotalTable.put("DISCOUNT_TOTAL", "");
        GDNtotalTable.put("NET_TOTAL", "");
        GDNtotalTable.put("TOTAL", "");
        GDNtotalTable.put("TAX_TOTAL", "");
        GDNtotalTable.put("SUBTOTAL", "");

        TreeMap<String, String> GDNitems = new TreeMap<>();
        GDNitems.put("NET_AMOUNT", "");
        GDNitems.put("NO", "NUMBER");
        GDNitems.put("QTY", "QUANTITY");
        GDNitems.put("ITEM_RECIEVE", "");
        GDNitems.put("QTY_MINUS_RECEIVED_QTY", "");
        GDNitems.put("NAME", "");
        GDNitems.put("ITEM_PART_NUMBER", "");
        GDNitems.put("DESCRIPTION", "");
        GDNitems.put("ITEM_WAREHOUSE", "");
        GDNitems.put("PRODUCT_NAME", "");
        GDNitems.put("ITEM_QTY", "ITEM QUANTITY");
        GDNitems.put("UNIT_PRICE_IN_BASE", "");
        GDNitems.put("ITEM_PROJECT", "");
        GDNitems.put("UNIT_MEASUREMENT", "");
        GDNitems.put("PROJECT_NUMBER", "");
        GDNitems.put("ALLOCATE", "");
        GDNitems.put("UNIT_PRICE", "");
        GDNitems.put("TOTAL_AMOUNT", "");
        GDNitems.put("TAX_AMOUNT", "");

        TreeMap<String, String> GDNcustomNumberAndDatesTable = new TreeMap<>();
        GDNcustomNumberAndDatesTable.put("INV_DATE", "");
        GDNcustomNumberAndDatesTable.put("BILL_CITY", "");
        GDNcustomNumberAndDatesTable.put("TITLE", "");
        GDNcustomNumberAndDatesTable.put("BILL_ADDRESS", "");
        GDNcustomNumberAndDatesTable.put("PO_NUMBER", "ORDER NUMBER");
        GDNcustomNumberAndDatesTable.put("CLIENT_CODE", "");
        GDNcustomNumberAndDatesTable.put("MAIL_COUNTRY", "");
        GDNcustomNumberAndDatesTable.put("TERMS_CONDITION", "");
        GDNcustomNumberAndDatesTable.put("MAIL_ADDRESS_NAME", "");
        GDNcustomNumberAndDatesTable.put("SHIPPING_DATE", "");
        GDNcustomNumberAndDatesTable.put("BILL_ADDRESS2", "");
        GDNcustomNumberAndDatesTable.put("CURRENCY", "");
        GDNcustomNumberAndDatesTable.put("MAIL_ADDRESS", "");
        GDNcustomNumberAndDatesTable.put("BILL_ZIPCODE", "");
        GDNcustomNumberAndDatesTable.put("INV_NUMBER", "");
        GDNcustomNumberAndDatesTable.put("CREATOR", "");
        GDNcustomNumberAndDatesTable.put("CLIENT_EMAIL", "");
        GDNcustomNumberAndDatesTable.put("MAIL_ADDRESS2", "");
        GDNcustomNumberAndDatesTable.put("GRN_CREATOR", "");
        GDNcustomNumberAndDatesTable.put("CLIENT_VAT_NUMBER", "");
        GDNcustomNumberAndDatesTable.put("MAIL_CITY", "");
        GDNcustomNumberAndDatesTable.put("QT_NUMBER", "QUOTE NUMBER");
        GDNcustomNumberAndDatesTable.put("MAIL_ZIPCODE", "");
        GDNcustomNumberAndDatesTable.put("BILL_ADDRESS_NAME", "");
        GDNcustomNumberAndDatesTable.put("CLIENT_CONTACT", "");
        GDNcustomNumberAndDatesTable.put("SHIPPING_LABEL", "");

        //TODO GRN

        TreeMap<String, String> GRNtotalTable = new TreeMap<>();
        GRNtotalTable.put("TAX_TOTAL", "");
        GRNtotalTable.put("SUBTOTAL", "");
        GRNtotalTable.put("DISCOUNT_TOTAL", "");
        GRNtotalTable.put("NET_TOTAL", "");
        GRNtotalTable.put("TOTAL", "");

        TreeMap<String, String> GRNitems = new TreeMap<>();
        GRNitems.put("NAME", "");
        GRNitems.put("DESCRIPTION", "");
        GRNitems.put("ITEM_WAREHOUSE", "");
        GRNitems.put("ITEM_RECIEVE", "");
        GRNitems.put("PRODUCT_NAME", "");
        GRNitems.put("ITEM_QTY", "ITEM QUANTITY");
        GRNitems.put("UNIT_MEASUREMENT", "");
        GRNitems.put("ALLOCATE", "");
        GRNitems.put("QTY_MINUS_RECEIVED_QTY", "");
        GRNitems.put("QTY", "QUANTITY");
        GRNitems.put("ITEM_PART_NUMBER", "");
        GRNitems.put("NO", "NUMBER");
        GRNitems.put("NET_AMOUNT", "");
        GRNitems.put("TOTAL_AMOUNT", "");
        GRNitems.put("UNIT_PRICE_IN_BASE", "");
        GRNitems.put("TAX_AMOUNT", "");
        GRNitems.put("UNIT_PRICE", "");
        GRNitems.put("PROJECT_NUMBER", "");
        GRNitems.put("ITEM_PROJECT", "");

        TreeMap<String, String> GRNcustomerNumberAndDatesTable = new TreeMap<>();
        GRNcustomerNumberAndDatesTable.put("CREATOR", "");
        GRNcustomerNumberAndDatesTable.put("CLIENT", "");
        GRNcustomerNumberAndDatesTable.put("CLIENT_PHONE", "");
        GRNcustomerNumberAndDatesTable.put("CLIENT_EMAIL", "");
        GRNcustomerNumberAndDatesTable.put("BILL_CITY", "");
        GRNcustomerNumberAndDatesTable.put("GRN_NUMBER", "");
        GRNcustomerNumberAndDatesTable.put("MAIL_ZIPCODE", "");
        GRNcustomerNumberAndDatesTable.put("BILL_ADDRESS", "");
        GRNcustomerNumberAndDatesTable.put("MAIL_ADDRESS", "");
        GRNcustomerNumberAndDatesTable.put("CLIENT_FAX", "");
        GRNcustomerNumberAndDatesTable.put("BILL_COUNTRY", "");
        GRNcustomerNumberAndDatesTable.put("INV_DUE_DATE", "");
        GRNcustomerNumberAndDatesTable.put("TITLE", "");
        GRNcustomerNumberAndDatesTable.put("INV_NUMBER", "");
        GRNcustomerNumberAndDatesTable.put("INV_DATE", "");
        GRNcustomerNumberAndDatesTable.put("MAIL_ADDRESS2", "");
        GRNcustomerNumberAndDatesTable.put("MAIL_CITY", "");
        GRNcustomerNumberAndDatesTable.put("BILL_ZIPCODE", "");
        GRNcustomerNumberAndDatesTable.put("REFERENCE", "");
        GRNcustomerNumberAndDatesTable.put("CURRENCY", "");
        GRNcustomerNumberAndDatesTable.put("BILL_ADDRESS_NAME", "");
        GRNcustomerNumberAndDatesTable.put("CLIENT_VAT_NUMBER", "");
        GRNcustomerNumberAndDatesTable.put("INTRODUCTION", "");
        GRNcustomerNumberAndDatesTable.put("QT_NUMBER", "");
        GRNcustomerNumberAndDatesTable.put("MAIL_COUNTRY", "");
        GRNcustomerNumberAndDatesTable.put("SHIPPING_LABEL", "");
        GRNcustomerNumberAndDatesTable.put("MAIL_ADDRESS_NAME", "");
        GRNcustomerNumberAndDatesTable.put("CLIENT_CODE", "");
        GRNcustomerNumberAndDatesTable.put("TERMS_CONDITION", "");
        GRNcustomerNumberAndDatesTable.put("SHIPPING_DATE", "");
        GRNcustomerNumberAndDatesTable.put("CLIENT_CONTACT", "");
        GRNcustomerNumberAndDatesTable.put("GRN_CREATOR", "");
        GRNcustomerNumberAndDatesTable.put("COMP_VAT_NUMBER", "");
        GRNcustomerNumberAndDatesTable.put("PO_NUMBER", "");
        GRNcustomerNumberAndDatesTable.put("BILL_ADDRESS2", "");

        //TODO OPPORTUNITY
        TreeMap<String, String> opportunityInformation = new TreeMap<>();

        opportunityInformation.put("STAGE", "");
        opportunityInformation.put("CURRENCY", "");
        opportunityInformation.put("CONTACT_EMAIL", "");
        opportunityInformation.put("OPPORTUNITY_NUMBER", "");
        opportunityInformation.put("CONTACT", "");
        opportunityInformation.put("OPPORTUNITY_NAME", "");
        opportunityInformation.put("CONTACT_PHONE", "");
        opportunityInformation.put("ASSIGNEE", "");
        opportunityInformation.put("ADDITIONAL_INFORMATION", "");
        opportunityInformation.put("OPPORTUNITY_INFORMATION", "");
        opportunityInformation.put("CLOSE_DATE", "");
        opportunityInformation.put("AMOUNT", "");

        TreeMap<String, String> customerDetail = new TreeMap<>();
        customerDetail.put("CUSTOMER", "");
        customerDetail.put("CLIENT_CONTACT", "");
        customerDetail.put("CLIENT_PHONE", "");
        customerDetail.put("CLIENT_EMAIL", "");
        customerDetail.put("PAYMENT_TERMS", "");

        //TODO CONTRACT
        TreeMap<String, String> contractContentTable = new TreeMap<>();
        contractContentTable.put("CLIENT", "");
        contractContentTable.put("PROJECT_NUMBER", "");
        contractContentTable.put("PROJECT_NAME", "");
        contractContentTable.put("CONTRACT_NUMBER", "");
        contractContentTable.put("CONTACT", "");
        contractContentTable.put("ALLOWANCE_BY_CUSTOMER", "");
        contractContentTable.put("BILL_ADDRESS", "");
        contractContentTable.put("DATE_REGISTRATION", "");
        contractContentTable.put("CONTRACT_DETAILS", "");
        contractContentTable.put("END_DATE", "");
        contractContentTable.put("START_DATE", "");
        contractContentTable.put("MAIL_ADDRESS", "");

        TreeMap<String, String> projectPositionTable = new TreeMap<>();
        projectPositionTable.put("CONTRACT_START_DATE", "");
        projectPositionTable.put("POSITION", "");
        projectPositionTable.put("WORKERS_NO", "");
        projectPositionTable.put("UNIT_QTY", "UNIT QUANTITY");
        projectPositionTable.put("RATE", "");
        projectPositionTable.put("CONTRACT_END_DATE", "");

        //TODO PICK LIST VIEW

        TreeMap<String, String> PLVcustomNumberAndDatesTable = new TreeMap<>();
        PLVcustomNumberAndDatesTable.put("GDN_NUMBER", "");
        PLVcustomNumberAndDatesTable.put("CURRENCY", "");
        PLVcustomNumberAndDatesTable.put("BILL_ADDRESS_NAME", "");
        PLVcustomNumberAndDatesTable.put("PICK_CARRIER_ACCOUNT_ID", "");
        PLVcustomNumberAndDatesTable.put("PICK_GROSS_WEIGHT", "");
        PLVcustomNumberAndDatesTable.put("SHIPPING_LABEL", "");
        PLVcustomNumberAndDatesTable.put("INV_DUE_DATE", "");
        PLVcustomNumberAndDatesTable.put("INVOICE_DUE_TERMS", "");
        PLVcustomNumberAndDatesTable.put("PO_NUMBER", "ORDER NUMBER");
        PLVcustomNumberAndDatesTable.put("PICK_DATE", "");
        PLVcustomNumberAndDatesTable.put("BILL_CITY", "");
        PLVcustomNumberAndDatesTable.put("QUOTE_PAYMENT_INSTRUCTION", "");
        PLVcustomNumberAndDatesTable.put("PACK_DATE", "");
        PLVcustomNumberAndDatesTable.put("INV_DATE", "");
        PLVcustomNumberAndDatesTable.put("CREATOR", "");
        PLVcustomNumberAndDatesTable.put("PAYMENT_INSTRUCTION", "");
        PLVcustomNumberAndDatesTable.put("BILL_COUNTRY", "");
        PLVcustomNumberAndDatesTable.put("SALE_QUOTE_APPROVER", "");
        PLVcustomNumberAndDatesTable.put("BILL_ADDRESS", "");
        PLVcustomNumberAndDatesTable.put("INVOICE_STATUS", "");
        PLVcustomNumberAndDatesTable.put("CLIENT_NAME", "");
        PLVcustomNumberAndDatesTable.put("SO_NUMBER", "");
        PLVcustomNumberAndDatesTable.put("BILL_ADDRESS2", "");
        PLVcustomNumberAndDatesTable.put("EXPECTED_DATE", "");
        PLVcustomNumberAndDatesTable.put("CLIENT_PHONE", "");
        PLVcustomNumberAndDatesTable.put("BILL_ZIPCODE", "");
        PLVcustomNumberAndDatesTable.put("BILL_STATE", "");
        PLVcustomNumberAndDatesTable.put("SHIPPING_DATE", "");

        TreeMap<String, String> PLVproductTable = new TreeMap<>();
        PLVproductTable.put("NUMBER_OF_PACKS", "");
        PLVproductTable.put("UNIT_MEASUREMENT", "");
        PLVproductTable.put("PICK_ITEM_REFERENCE", "");
        PLVproductTable.put("QTY_PER_PACK", "");
        PLVproductTable.put("SHIPPED", "");
        PLVproductTable.put("ITEM_NUMBER", "");
        PLVproductTable.put("NAME", "");
        PLVproductTable.put("ITEM_QTY", "");
        PLVproductTable.put("NO", "NUMBER");
        PLVproductTable.put("DESCRIPTION", "");
        PLVproductTable.put("PICK_ITEM_BOOKED_QTY", "");
        PLVproductTable.put("ITEM_PART_NUMBER", "");
        PLVproductTable.put("ITEM_WAREHOUSE", "");
        PLVproductTable.put("PICKED", "");
        PLVproductTable.put("PACKED", "");

        TreeMap<String, String> PLVquoteTotalTable = new TreeMap<>();
        PLVquoteTotalTable.put("DISCOUNT_TOTAL", "");
        PLVquoteTotalTable.put("TOTAL_IN_WORDS", "");
        PLVquoteTotalTable.put("SUBTOTAL", "");
        PLVquoteTotalTable.put("TOTAL", "");
        PLVquoteTotalTable.put("TAX_TOTAL", "");


        TreeMap<String, String> PLVquoteProductTable = new TreeMap<>();
        PLVquoteProductTable.put("NAME", "");
        PLVquoteProductTable.put("TAX_RATE", "");
        PLVquoteProductTable.put("QTY", "QUANTITY");
        PLVquoteProductTable.put("UNIT_MEASUREMENT", "");
        PLVquoteProductTable.put("UNIT_PRICE", "");
        PLVquoteProductTable.put("NET_AMOUNT", "");
        PLVquoteProductTable.put("DESCRIPTION", "");
        PLVquoteProductTable.put("NO", "NUMBER");
        PLVquoteProductTable.put("TAX_AMOUNT", "");
        PLVquoteProductTable.put("TOTAL_AMOUNT", "");
        PLVquoteProductTable.put("ITEM_PRODUCT_NUMBER", "");

        TreeMap<String, String> PLVquoteProductBatchTable = new TreeMap<>();
        PLVquoteProductBatchTable.put("ITEM_NUMBER", "");
        PLVquoteProductBatchTable.put("QTY", "QUANTITY");
        PLVquoteProductBatchTable.put("ITEM_EXPIRATION_DATE", "");
        PLVquoteProductBatchTable.put("ITEM_SERIAL_NUMBER", "");
        PLVquoteProductBatchTable.put("NAME", "");


        //TODO COURSE BOOKING
        TreeMap<String, String> CBcustomerData = new TreeMap<>();
        CBcustomerData.put("REF_IND_NUMBER", "");
        CBcustomerData.put("COMPANY_NAME", "");
        CBcustomerData.put("PHONE_NUMBER", "");
        CBcustomerData.put("CLIENT_FAX", "");
        CBcustomerData.put("EMAIL", "");
        CBcustomerData.put("LOCATION", "");
        CBcustomerData.put("LOCATION_CODE", "");
        CBcustomerData.put("LOCATION_PHONE", "");
        CBcustomerData.put("LOCATION_FAX", "");
        CBcustomerData.put("LOCATION_EMAIL", "");

        TreeMap<String, String> CBcontactData = new TreeMap<>();
        CBcontactData.put("NAME", "");
        CBcontactData.put("PHONE_NUMBER", "");
        CBcontactData.put("POSITION", "");
        CBcontactData.put("REF_IND_NUMBER", "");

        TreeMap<String, String> bookingFormTable = new TreeMap<>();
        bookingFormTable.put("NO", "");
        bookingFormTable.put("NAME", "");
        bookingFormTable.put("REF_IND_NUMBER", "");
        bookingFormTable.put("RES_CARD_NUMBER", "");
        bookingFormTable.put("EMAIL", "");
        bookingFormTable.put("PHONE_NUMBER", "");
        bookingFormTable.put("COURSE_TITLE", "");
        bookingFormTable.put("COLUMN_CODE", "");
        bookingFormTable.put("LANGUAGE", "");
        bookingFormTable.put("COURSE_DATE", "");

        //TODO EMPLOYEE PROFILE ID CARDS

        //Get from EMPLOYEE_DEPENDENTS_INFORMATION
        TreeMap<String, String> employeeDependentsInformation = new TreeMap<>();
        employeeDependentsInformation.put("TOWN", "");
        employeeDependentsInformation.put("CITY", "");
        employeeDependentsInformation.put("RELATIONSHIP", "");
        employeeDependentsInformation.put("LAST_NAME", "");
        employeeDependentsInformation.put("PHONE2", "");
        employeeDependentsInformation.put("MIDDLE_NAME", "");
        employeeDependentsInformation.put("FIRST_NAME", "");
        employeeDependentsInformation.put("COUNTRY", "");
        employeeDependentsInformation.put("ADDRESS1", "");
        employeeDependentsInformation.put("PHONE", "");
        employeeDependentsInformation.put("ADDRESS2", "");

        //get from EMPLOYEE_INFORMATION

        TreeMap<String, String> employeeInformation = new TreeMap<>();
        employeeInformation.put("EMPLOYEE_CODE", "");
        employeeInformation.put("DEGREE", "");
        employeeInformation.put("BANK_ACCOUNT_INFORMATION", "");
        employeeInformation.put("NATIONALITY", "");
        employeeInformation.put("CONTACT_INFORMATION", "");
        employeeInformation.put("PRIMARY_EMAIL", "");
        employeeInformation.put("ADDITIONAL_INFORMATION", "");
        employeeInformation.put("EMPLOYMENT_MODE", "");
        employeeInformation.put("EMPLOYMENT_INFORMATION", "");
        employeeInformation.put("HRMS_SALARY_GRADE", "");
        employeeInformation.put("POSITION", "");
        employeeInformation.put("EMPLOYEE_PHOTO", "");
        employeeInformation.put("SUPERVISOR", "");
        employeeInformation.put("BASIC_SALARY", "");
        employeeInformation.put("department", "");
        employeeInformation.put("ADDRESS_INFORMATION", "");
        employeeInformation.put("TITLE", "");
        employeeInformation.put("SPOKEN_LANGUAGES", "");
        employeeInformation.put("DATE_OF_BIRTH", "");
        employeeInformation.put("LOCATION", "");
        employeeInformation.put("TERMS_OF_CONTRACT", "");
        employeeInformation.put("RESIGNATION_DATE", "");
        employeeInformation.put("MARITAL_STATUS", "");
        employeeInformation.put("GENDER", "");
        employeeInformation.put("EMPLOYEE_PHOTO_ID", "");
        employeeInformation.put("QUALIFICATION", "");
        employeeInformation.put("HIRE_DATE", "");
        employeeInformation.put("EMPLOYEE_NAME", "");

        TreeMap<String, String> mobilePhone = new TreeMap<>();
        mobilePhone.put("MOBILE_PHONE", "");

        TreeMap<String, String> corporateEmail = new TreeMap<>();
        corporateEmail.put("CORPORATE_EMAIL", "");

        //get from BANK_INFORMATION
        TreeMap<String, String> bankInformation = new TreeMap<>();
        bankInformation.put("SWIFT_BIC", "");
        bankInformation.put("SORT_CODE", "");
        bankInformation.put("ACCOUNT_NAME", "");
        bankInformation.put("IBAN_CODE", "");
        bankInformation.put("BANK_ADDRESS", "");
        bankInformation.put("AGENT_ID", "");
        bankInformation.put("BANK_NAME", "");
        bankInformation.put("ACCOUNT_NUMBER", "");

        //get from ADDRESS_INFORMATION

        TreeMap<String, String> employeeAddressInformation = new TreeMap<>();
        employeeAddressInformation.put("CORPORATE_CITY", "");
        employeeAddressInformation.put("HOME_ADDRESS_TITLE", "");
        employeeAddressInformation.put("CORPORATE_ADDRESS2", "");
        employeeAddressInformation.put("HOME_ADDRESS", "");
        employeeAddressInformation.put("HOME_ADDRESS_NAME", "");
        employeeAddressInformation.put("HOME_COUNTRY", "");
        employeeAddressInformation.put("HOME_ZIPCODE", "");
        employeeAddressInformation.put("HOME_STATE", "");
        employeeAddressInformation.put("HOME_CITY", "");
        employeeAddressInformation.put("HOME_ADDRESS2", "");
        employeeAddressInformation.put("HOME_ADDRESS_FULL", "");
        employeeAddressInformation.put("CORPORATE_ADDRESS_TITLE", "");
        employeeAddressInformation.put("CORPORATE_ADDRESS_NAME", "");
        employeeAddressInformation.put("CORPORATE_ADDRESS", "");
        employeeAddressInformation.put("CORPORATE_ADDRESS_FULL", "");
        employeeAddressInformation.put("CORPORATE_ZIPCODE", "");
        employeeAddressInformation.put("CORPORATE_COUNTRY", "");
        employeeAddressInformation.put("CORPORATE_STATE", "");

        //get from PERSONAL_INFORMATION
        TreeMap<String, String> personalInformation = new TreeMap<>();
        personalInformation.put("INSURANCE_NUMBER", "");
        personalInformation.put("VISA_ISSUE_DATE", "");
        personalInformation.put("PASSPORT_NUMBER", "");
        personalInformation.put("INSURANCE_EXPIRY_DATE", "");
        personalInformation.put("VISA_EXPIRY_DATE", "");
        personalInformation.put("PASSPORT_EXPIRY_DATE", "");
        personalInformation.put("PASSPORT_ISSUE_DATE", "");
        personalInformation.put("PASSPORT_ISSUE", "");
        personalInformation.put("VISA_NUMBER", "");


        //get from LEAVE_REQUESTS_BY_PERIOD
        TreeMap<String, String> leaveByPeriod = new TreeMap<>();
        leaveByPeriod.put("LABOR_PERIOD_START", "");
        leaveByPeriod.put("LABOR_PERIOD_END", "");
        leaveByPeriod.put("LEAVE_START", "");
        leaveByPeriod.put("LEAVE_END", "");
        leaveByPeriod.put("LEAVE_DAYS", "");

        //get from BACKUP_EMPLOYEES
        TreeMap<String, String> backupEmployees = new TreeMap<>();
        backupEmployees.put("EMPLOYEE_FULL_NAME", "");
        backupEmployees.put("EMPLOYEE_POSITION", "");

        //get from CORPORATE_PHONE

        TreeMap<String, String> corporatePhone = new TreeMap<>();
        corporatePhone.put("CORPORATE_PHONE", "");


        //TODO SINGLE PAYRUN

        TreeMap<String, String> SinglePayrunCustomProductTable = new TreeMap<>();
        SinglePayrunCustomProductTable.put("AMOUNTS", "");
        SinglePayrunCustomProductTable.put("RATES", "");
        SinglePayrunCustomProductTable.put("PAYMENT_", "");
        SinglePayrunCustomProductTable.put("REMARKS", "");
        SinglePayrunCustomProductTable.put("AMOUNTS_BASIC", "");
        SinglePayrunCustomProductTable.put("UNIT", "");
        SinglePayrunCustomProductTable.put("AMOUNTS_NUMERIC", "");

        TreeMap<String, String> SinglePayrunCustomTotalTable = new TreeMap<>();
        SinglePayrunCustomTotalTable.put("ADDITIONAL_PAYMENT", "");
        SinglePayrunCustomTotalTable.put("TOTAL_NUMERIC", "");
        SinglePayrunCustomTotalTable.put("TOTAL_GROSS_PAY", "");
        SinglePayrunCustomTotalTable.put("TOTAL", "");
        SinglePayrunCustomTotalTable.put("DEDUCTIONS", "");
        SinglePayrunCustomTotalTable.put("TOTAL_ADDITIONAL", "");
        SinglePayrunCustomTotalTable.put("TOTAL_LIVING", "");
        SinglePayrunCustomTotalTable.put("DEDUCTIONS_NUMERIC", "");
        SinglePayrunCustomTotalTable.put("TOTAL_IN_WORDS", "");
        SinglePayrunCustomTotalTable.put("EXP_TOTAL", "EXPENSE TOTAL");
        SinglePayrunCustomTotalTable.put("GROSS_SALARY", "");
        SinglePayrunCustomTotalTable.put("TOTAL_OVERTIME", "");
        SinglePayrunCustomTotalTable.put("TOTAL_IN_BASE", "");
        SinglePayrunCustomTotalTable.put("TOTAL_YTD", "");

        TreeMap<String, String> SinglePayrunCustomNumberAndDatesTable = new TreeMap<>();
        SinglePayrunCustomNumberAndDatesTable.put("AMOUNTS_NUMERIC", "");
        SinglePayrunCustomNumberAndDatesTable.put("AMOUNTS_BASIC", "");
        SinglePayrunCustomNumberAndDatesTable.put("AMOUNTS", "");
        SinglePayrunCustomNumberAndDatesTable.put("RATES", "");
        SinglePayrunCustomNumberAndDatesTable.put("REMARKS", "");
        SinglePayrunCustomNumberAndDatesTable.put("UNIT", "");
        SinglePayrunCustomNumberAndDatesTable.put("DEDUCTIONS", "");

        TreeMap<String, String> SinglePayrunCustomBillToAddress = new TreeMap<>();
        SinglePayrunCustomBillToAddress.put("PAYMENT_PERIOD", "");
        SinglePayrunCustomBillToAddress.put("BYPROJECT_TOTAL", "");
        SinglePayrunCustomBillToAddress.put("COMMENT", "");
        SinglePayrunCustomBillToAddress.put("PROCESSED_DATE", "");
        SinglePayrunCustomBillToAddress.put("CREATED_DATE", "");
        SinglePayrunCustomBillToAddress.put("HOLIDAY_OVERTIME_RATE", "");
        SinglePayrunCustomBillToAddress.put("EXCHANGE_RATE", "");
        SinglePayrunCustomBillToAddress.put("REFERENCE_LABEL", "");
        SinglePayrunCustomBillToAddress.put("PERIOD_START_DATE", "");
        SinglePayrunCustomBillToAddress.put("WPS_NO_LABEL", "");
        SinglePayrunCustomBillToAddress.put("REGULAR_OVERTIME_RATE", "");
        SinglePayrunCustomBillToAddress.put("HIRE_DATE", "");
        SinglePayrunCustomBillToAddress.put("CURRENCY_NAME", "");
        SinglePayrunCustomBillToAddress.put("REFERENCE", "");
        SinglePayrunCustomBillToAddress.put("APPROVED_DATE_LABEL", "");
        SinglePayrunCustomBillToAddress.put("IBAN_CODE", "");
        SinglePayrunCustomBillToAddress.put("REGULAR_OVERTIME", "");
        SinglePayrunCustomBillToAddress.put("APPROVER", "");
        SinglePayrunCustomBillToAddress.put("department", "");
        SinglePayrunCustomBillToAddress.put("TOTALS_THIS_PERIOD_LABEL", "");
        SinglePayrunCustomBillToAddress.put("APPROVED_DATE", "");
        SinglePayrunCustomBillToAddress.put("EXPENSES_LABEL", "");
        SinglePayrunCustomBillToAddress.put("PERIOD", "");
        SinglePayrunCustomBillToAddress.put("DAYS_OF_MONTH", "");
        SinglePayrunCustomBillToAddress.put("SALARY_RATE", "");
        SinglePayrunCustomBillToAddress.put("REJECT", "");
        SinglePayrunCustomBillToAddress.put("Number", "");
        SinglePayrunCustomBillToAddress.put("WEEKEND_OVERTIME_RATE", "");
        SinglePayrunCustomBillToAddress.put("PAYMENT_LABEL", "");
        SinglePayrunCustomBillToAddress.put("CREATOR", "");
        SinglePayrunCustomBillToAddress.put("PAYMENT_POLICY", "");
        SinglePayrunCustomBillToAddress.put("WEEKEND_OVERTIME", "");
        SinglePayrunCustomBillToAddress.put("HOLIDAY_OVERTIME", "");
        SinglePayrunCustomBillToAddress.put("DEDUCTIONS_LABEL", "");
        SinglePayrunCustomBillToAddress.put("PERIOD_END_DATE", "");
        SinglePayrunCustomBillToAddress.put("NAME_LABEL", "");
        SinglePayrunCustomBillToAddress.put("CREATOR_LABEL", "");
        SinglePayrunCustomBillToAddress.put("APPROVER_LABEL", "");
        SinglePayrunCustomBillToAddress.put("CODE_LABEL", "");
        SinglePayrunCustomBillToAddress.put("DRIVER_NUMBER", "");
        SinglePayrunCustomBillToAddress.put("POSITION", "");
        SinglePayrunCustomBillToAddress.put("PAYMENT_METHOD", "");
        SinglePayrunCustomBillToAddress.put("PROCESSED_DATE_LABEL", "");
        SinglePayrunCustomBillToAddress.put("BANK_ACCOUNT_NUMBER", "");
        SinglePayrunCustomBillToAddress.put("WPS_NO", "");
        SinglePayrunCustomBillToAddress.put("BASE_CURRENCY", "");
        SinglePayrunCustomBillToAddress.put("TOTAL_GROSS_PAY_LABEL", "");
        SinglePayrunCustomBillToAddress.put("TOTAL_LABEL", "");
        SinglePayrunCustomBillToAddress.put("WORKED_DAYS", "");
        SinglePayrunCustomBillToAddress.put("PERIOD_FROM_TO", "");
        SinglePayrunCustomBillToAddress.put("LOCATION", "");
        SinglePayrunCustomBillToAddress.put("PAYMENT_PERIOD_LABEL", "");
        SinglePayrunCustomBillToAddress.put("SALARY_RATE_TYPE", "");
        SinglePayrunCustomBillToAddress.put("RESIGNATION_DATE", "");
        SinglePayrunCustomBillToAddress.put("NAME", "");

        //TODO PROJECT

        //got from PROJECT_CONTENT_TABLE
        TreeMap<String, String> projectContentTable = new TreeMap<>();
        projectContentTable.put("START_DATE", "");
        projectContentTable.put("PROJECT_NUMBER", "");
        projectContentTable.put("PROJECT_NAME", "");
        projectContentTable.put("PROJECT_STATUS", "");
        projectContentTable.put("RELATION_CONTACT_NAME", "");
        projectContentTable.put("ACTUAL_TIME_SPENT", "");
        projectContentTable.put("PROJECT_CLIENT_CONTACT", "");
        projectContentTable.put("DUE_DATE", "");
        projectContentTable.put("DESCRIPTION", "");
        projectContentTable.put("PROJECT_CLIENT", "");
        projectContentTable.put("ESTIMATED_TIME", "");
        projectContentTable.put("PROJECT_MANAGER", "");
        projectContentTable.put("RELATION_CONTACT_DATE_OF_BIRTH", "");
        projectContentTable.put("TIME_SPENT", "");

        //got from PROJECT_TASKS
        TreeMap<String, String> projectTasks = new TreeMap<>();
        projectTasks.put("TIME_SPENT", "");
        projectTasks.put("ASSIGNES", "");
        projectTasks.put("ESTIMATED_TIME", "");
        projectTasks.put("PRIORITY", "");
        projectTasks.put("TASK_NUMBER", "");
        projectTasks.put("DUE_DATE", "");
        projectTasks.put("PERCENT", "");
        projectTasks.put("ACTUAL_TIME", "");
        projectTasks.put("DESCRIPTION", "");
        projectTasks.put("START_DATE", "");
        projectTasks.put("TASK_NAME", "");

        //got from MEMBERS_INVOLVED
        TreeMap<String, String> membersInvolved = new TreeMap<>();
        membersInvolved.put("IN_PROGRESS", "");
        membersInvolved.put("COMPLETED", "");
        membersInvolved.put("ESTIMATED_TIME", "");
        membersInvolved.put("WAITING_FOR_SOMEONE", "");
        membersInvolved.put("ACTUAL_TIME_SPENT", "");
        membersInvolved.put("TIME_SPENT", "");
        membersInvolved.put("NOT_STARTED", "");
        membersInvolved.put("CLOSED", "");
        membersInvolved.put("MEMBERS", "");


        if (pdfType.getSelectedItem() != null && !Utils.isNullOrEmpty(pdfType.getSelectedItem().getName())) {
            switch (pdfType.getSelectedItem().getName()) {

                case "Sales Invoice":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>" + "\n");
                    f.append("#foreach($row in $itextGenericPdfData.baseInvoice.customProductTable.rows.values())");


                    customProductTable.forEach((k, v) -> {
                        f.append("\n" + "$row.get('" + k + "')" + "     ========> " + (v.isEmpty() ? k : v));
                    });
                    f.append("\n" + "#end");

                    f.append("      \n");
                    f.append("\n" + "<<Total Table>>");
                    f.append("\n" + "#set($customTotalTable=$itextGenericPdfData.baseInvoice.customTotalTable.rows)");


                    customTotalTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customTotalTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("      \n");
                    f.append("\n" + "<<Expense Table>>");
                    f.append("   \n" + "#set($customExpenseTable=$itextGenericPdfData.baseInvoice.customExpenseTable.rows)");

                    customExpenseTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customExpenseTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("      \n");
                    f.append("\n" + "<<NumberAndDates Table>>");
                    f.append("   \n" + "#set($customNumberAndDatesTable=$itextGenericPdfData.baseInvoice.customNumberAndDatesTable.rows)");


                    customNumberAndDatesTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customNumberAndDatesTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));
                    });

                    f.append("      \n");
                    f.append("\n" + "<<BillToAddress Table>>");
                    f.append("  \n" + "#set($customBillToAddress=$itextGenericPdfData.baseInvoice.customBillToAddress.rows)");

                    customBillToAddress.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customBillToAddress.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));
                    });


                    f.append("      \n");
                    f.append("\n" + "<<PaymentHistory Table>>");
                    f.append("  \n" + "#set($paymentHistoryTable=$itextGenericPdfData.baseInvoice.paymentHistoryTable.rows)");


                    paymentHistoryTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$paymentHistoryTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("\n" + "CUSTOM FIELDS");

                    f.append("\n" + "\n");
                    f.append("<<Invoice Table>>");
                    f.append("\n" + "#set($custom=$itextGenericPdfData.baseInvoice.customProductTable.customFields.get('INVOICE'))");
                    getCustomFields(ViewName.SaleInvoice.toString(), f);

                    f.append("\n" + "\n");
                    f.append("<<Item Table>>");
                    f.append("\n" + "#set($custom = $itextGenericPdfData.customListData.get('CUSTOM_FIELD').get(0).get('GENERAL_INFORMATION')");
                    getItemTableFields("SaleInvoiceItem", f);
                    break;

                case "Sales Quote":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>" + "\n");
                    f.append("#foreach($row in $itextGenericPdfData.baseInvoice.customProductTable.rows.values())");

                    SQcustomProductTable.forEach((k, v) -> {
                        f.append("\n" + "$row.get('" + k + "')" + "     ========> " + (v.isEmpty() ? k : v));
                    });
                    f.append("\n" + "#end" + "\n       ");


                    f.append("      \n");
                    f.append("\n" + "<<Total Table>>");
                    f.append("\n" + "#set($customTotalTable=$itextGenericPdfData.baseInvoice.customTotalTable.rows)");

                    SQcustomTotalTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customTotalTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));
                    });

                    f.append("\n" + "\n");
                    f.append("\n" + "<<NumberAndDates Table>>");
                    f.append("\n" + "#set($customNumberAndDatesTable=$itextGenericPdfData.baseInvoice.customNumberAndDatesTable.rows)");


                    SQcustomNumberAndDatesTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customNumberAndDatesTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "\n");
                    f.append("\n" + "<<BillToAddress Table>>");
                    f.append("\n" + "#set($customBillToAddress=$itextGenericPdfData.baseInvoice.customBillToAddress.rows)");

                    SQcustomBillToAddress.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customBillToAddress.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Invoice Table>>");
                    f.append("\n" + "#set($custom=$itextGenericPdfData.baseInvoice.customProductTable.customFields.get('INVOICE'))");
                    getCustomFields(ViewName.SaleQuote.toString(), f);

                    f.append("\n" + "\n");
                    f.append("<<Item Table>>");
                    f.append("\n" + "#set($custom = $itextGenericPdfData.customListData.get('CUSTOM_FIELD').get(0).get('GENERAL_INFORMATION')");
                    getItemTableFields("SaleQuoteItem", f);

                    break;

                case "Sales Order":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>" + "\n");
                    f.append("#foreach($row in $itextGenericPdfData.baseInvoice.customProductTable.rows.values())");

                    SOcustomProductTable.forEach((k, v) -> {
                        f.append("\n" + "$row.get('" + k + "')" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "#end" + "\n       ");


                    f.append("\n" + "\n");
                    f.append("\n" + "<<Total Table>>");
                    f.append("\n" + "#set($customTotalTable=$itextGenericPdfData.baseInvoice.customTotalTable.rows)");

                    SOcustomTotalTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customTotalTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "\n");
                    f.append("\n" + "<<NumberAndDates Table>>");
                    f.append("\n" + "#set($customNumberAndDatesTable=$itextGenericPdfData.baseInvoice.customNumberAndDatesTable.rows)");

                    SOcustomNumberAndDatesTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customNumberAndDatesTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("\n" + "<<BillToAddress Table>>");
                    f.append("\n" + "#set($customBillToAddress=$itextGenericPdfData.baseInvoice.customBillToAddress.rows)");

                    SOcustomBillToAddress.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customBillToAddress.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Invoice Table>>");
                    f.append("\n" + "#set($custom=$itextGenericPdfData.baseInvoice.customProductTable.customFields.get('INVOICE'))");
                    getCustomFields(ViewName.SaleOrder.toString(), f);

                    f.append("\n" + "\n");
                    f.append("<<Item Table>>");
                    f.append("\n" + "#set($custom = $itextGenericPdfData.customListData.get('CUSTOM_FIELD').get(0).get('GENERAL_INFORMATION')");
                    getItemTableFields("PurchaseOrderItem", f);
                    break;
                case "Purchase Invoice":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>" + "\n");
                    f.append("#foreach($row in $itextGenericPdfData.baseInvoice.customProductTable.rows.values())");

                    PIcustomProductTable.forEach((k, v) -> {
                        f.append("\n" + "$row.get('" + k + "')" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "#end" + "\n       ");


                    f.append("      \n");
                    f.append("<<Total Table>>");
                    f.append("\n" + "#set($customTotalTable=$itextGenericPdfData.baseInvoice.customTotalTable.rows)");

                    PIcustomTotalTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customTotalTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<NumberAndDates Table>>");
                    f.append("\n" + "#set($customNumberAndDatesTable=$itextGenericPdfData.baseInvoice.customNumberAndDatesTable.rows)");

                    PIcustomNumberAndDatesTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customNumberAndDatesTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "      \n");
                    f.append("<<BillToAddress Table>>");
                    f.append("\n" + "#set($customBillToAddress=$itextGenericPdfData.baseInvoice.customBillToAddress.rows)");

                    PIcustomBillToAddress.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customBillToAddress.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Invoice Table>>");
                    f.append("\n" + "#set($custom=$itextGenericPdfData.baseInvoice.customProductTable.customFields.get('INVOICE'))");
                    getCustomFields(ViewName.PurchaseInvoice.toString(), f);

                    f.append("\n" + "\n");
                    f.append("<<Item Table>>");
                    f.append("\n" + "#set($custom = $itextGenericPdfData.customListData.get('CUSTOM_FIELD').get(0).get('GENERAL_INFORMATION')");
                    getItemTableFields("PurchaseInvoiceItem", f);
                    break;


                case "Purchase Order":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>" + "\n");
                    f.append("#foreach($row in $itextGenericPdfData.baseInvoice.customProductTable.rows.values())");

                    POcustomProductTable.forEach((k, v) -> {
                        f.append("\n" + "$row.get('" + k + "')" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "#end" + "\n         ");


                    f.append("      \n");
                    f.append("<<Total Table>>");
                    f.append("\n" + "#set($customTotalTable=$itextGenericPdfData.baseInvoice.customTotalTable.rows)");

                    POcustomTotalTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customTotalTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "      \n");
                    f.append("<<NumberAndDates Table>>");
                    f.append("\n" + "#set($customNumberAndDatesTable=$itextGenericPdfData.baseInvoice.customNumberAndDatesTable.rows)");

                    POcustomNumberAndDatesTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customNumberAndDatesTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "      \n");
                    f.append("<<BillToAddress Table>>");
                    f.append("\n" + "#set($customBillToAddress=$itextGenericPdfData.baseInvoice.customBillToAddress.rows)");

                    PIcustomBillToAddress.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customBillToAddress.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Invoice Table>>");
                    f.append("\n" + "#set($custom=$itextGenericPdfData.baseInvoice.customProductTable.customFields.get('INVOICE'))");
                    getCustomFields(ViewName.PurchaseOrder.toString(), f);

                    f.append("\n" + "\n");
                    f.append("<<Item Table>>");
                    f.append("\n" + "#set($custom = $itextGenericPdfData.customListData.get('CUSTOM_FIELD').get(0).get('GENERAL_INFORMATION')");
                    getItemTableFields("SaleInvoiceItem", f);
                    break;
                case "Candidate":


                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<NumberAndDates Table>>" + "\n");
                    f.append("#set($customNumberAndDatesTable=$itextGenericPdfData.baseInvoice.customNumberAndDatesTable.rows)");

                    CandidatecustomNumberAndDatesTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customNumberAndDatesTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("#set($custom = $itextGenericPdfData.customData.get('CUSTOM_FIELD'))");
                    getCustomFields(ViewName.Candidate.toString(), f);

                    break;

                case "Vacancy":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<NumberAndDates Table>>" + "\n");
                    f.append("#set($customNumberAndDatesTable=$itextGenericPdfData.baseInvoice.customNumberAndDatesTable.rows)");

                    VacancycustomNumberAndDatesTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customNumberAndDatesTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("#set($custom = $itextGenericPdfData.customData.get('CUSTOM_FIELD'))");
                    getCustomFields(ViewName.Vacancy.toString(), f);

                    f.append("\n" + "\n");
                    f.append("<<Item Table>>");
                    f.append("\n" + "#set($custom = $itextGenericPdfData.customListData.get('CUSTOM_FIELD').get(0).get('GENERAL_INFORMATION')");
                    getItemTableFields("VacancyItem", f);
                    break;

                case "Placement":


                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<NumberAndDates Table>>" + "\n");
                    f.append("#set($customNumberAndDatesTable=$itextGenericPdfData.baseInvoice.customNumberAndDatesTable.rows)");

                    PlacementustomNumberAndDatesTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customNumberAndDatesTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("#set($custom = $itextGenericPdfData.customData.get('CUSTOM_FIELD'))");
                    getCustomFields(ViewName.Placement.toString(), f);

                    break;

                case "Rental Order":


                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>" + "\n");
                    f.append("#foreach($row in $itextGenericPdfData.baseInvoice.customProductTable.rows.values())");

                    ROcustomProductTable.forEach((k, v) -> {
                        f.append("\n" + "$row.get('" + k + "')");

                    });

                    f.append("\n" + "#end" + "\n       ");

                    f.append("      \n");
                    f.append("<<Total Table>>");
                    f.append("\n" + "#set($customTotalTable=$itextGenericPdfData.baseInvoice.customTotalTable.rows)");

                    ROcustomTotalTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customTotalTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "      \n");
                    f.append("<<NumberAndDates Table>>");
                    f.append("\n" + "#set($customNumberAndDatesTable=$itextGenericPdfData.baseInvoice.customNumberAndDatesTable.rows)");

                    ROcustomNumberAndDatesTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customNumberAndDatesTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("#set($custom=$itextGenericPdfData.baseInvoice.customProductTable.customFields.get('INVOICE'))");
                    getCustomFields(ViewName.RentalOrdersView.toString(), f);

                    f.append("\n" + "\n");
                    f.append("<<Item Table>>");
                    f.append("\n" + "#set($custom = $itextGenericPdfData.customListData.get('CUSTOM_FIELD').get(0).get('GENERAL_INFORMATION')");
                    getItemTableFields("RentalOrderItem", f);
                    break;

                case "Rental Product":


                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>" + "\n");
                    f.append("#foreach($row in $itextGenericPdfData.baseInvoice.customProductTable.rows.values())");

                    RPcustomProductTable.forEach((k, v) -> {
                        f.append("\n" + "$row.get('" + k + "')" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "#end" + "\n       ");

                    f.append("\n" + "\n");
                    f.append("<<NumberAndDates Table>>");
                    f.append("\n" + "#set($customNumberAndDatesTable=$itextGenericPdfData.baseInvoice.customNumberAndDatesTable.rows)");

                    RPcustomNumberAndDatesTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customNumberAndDatesTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Invoice Table>>");
                    f.append("\n" + "#set($custom=$itextGenericPdfData.baseInvoice.customProductTable.customFields.get('INVOICE'))");
                    getCustomFields(ViewName.RentalProductsView.toString(), f);


                    break;


                case "Payslip":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>" + "\n");
                    f.append("#foreach($row in $itextGenericPdfData.baseInvoice.customProductTable.rows.values())");

                    PcustomProductTable.forEach((k, v) -> {
                        f.append("\n" + "$row.get('" + k + "')" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "#end" + "\n       ");


                    f.append("      \n");
                    f.append("<<Total Table>>");
                    f.append("\n" + "#set($customTotalTable=$itextGenericPdfData.baseInvoice.customTotalTable.rows)");

                    PcustomTotalTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customTotalTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "      \n");
                    f.append("<<NumberAndDates Table>>");
                    f.append("\n" + "#set($customNumberAndDatesTable=$itextGenericPdfData.baseInvoice.customNumberAndDatesTable.rows)");

                    PcustomNumberAndDatesTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customNumberAndDatesTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("      \n");
                    f.append("\n" + "<<BillToAddress Table>>");
                    f.append("\n" + "#set($customBillToAddress=$itextGenericPdfData.baseInvoice.customBillToAddress.rows)");

                    PcustomBillToAddress.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customBillToAddress.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("#set($custom = $itextGenericPdfData.customData.get('CUSTOM_FIELD'))");
                    getCustomFields(ViewName.PayslipTableList.toString(), f);

                    break;

                case "Receivable Credit Note (Sales Invoice)":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>" + "\n");
                    f.append("#foreach($row in $itextGenericPdfData.baseInvoice.customProductTable.rows.values())");

                    RCNcustomProductTable.forEach((k, v) -> {
                        f.append("\n" + "$row.get('" + k + "')" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "#end" + "\n       ");


                    f.append("      \n");
                    f.append("<<Total Table>>");
                    f.append("\n" + "#set($customTotalTable=$itextGenericPdfData.baseInvoice.customTotalTable.rows)");

                    RCNcustomNumberAndDatesTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customTotalTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("      \n");
                    f.append("\n" + "<<NumberAndDates Table>>");
                    f.append("\n" + "#set($customNumberAndDatesTable=$itextGenericPdfData.baseInvoice.customNumberAndDatesTable.rows)");

                    RCNcustomNumberAndDatesTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customNumberAndDatesTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "      \n");
                    f.append("<<BillToAddress Table>>");
                    f.append("\n" + "#set($customBillToAddress=$itextGenericPdfData.baseInvoice.customBillToAddress.rows)");

                    RCNcustomBillToAddress.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customBillToAddress.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

//                    f.append("\n" + "\n");
//
//                    getCustomFields(ViewName.Project.toString(), f);

                    break;

                case "Payable Credit Note (Purchase Invoice)":


                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>" + "\n");
                    f.append("#foreach($row in $itextGenericPdfData.baseInvoice.customProductTable.rows.values())");

                    PCNcustomProductTable.forEach((k, v) -> {
                        f.append("\n" + "$row.get('" + k + "')" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "#end" + "\n       ");


                    f.append("      \n");
                    f.append("<<Total Table>>");
                    f.append("\n" + "#set($customTotalTable=$itextGenericPdfData.baseInvoice.customTotalTable.rows)");

                    PCNcustomTotalTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customTotalTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "      \n");
                    f.append("<<NumberAndDates Table>>");
                    f.append("\n" + "#set($customNumberAndDatesTable=$itextGenericPdfData.baseInvoice.customNumberAndDatesTable.rows)");

                    PCNcustomNumberAndDatesTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customNumberAndDatesTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "      \n");
                    f.append("<<BillToAddress Table>>");
                    f.append("\n" + "#set($customBillToAddress=$itextGenericPdfData.baseInvoice.customBillToAddress.rows)");

                    PCNcustomBillToAddress.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customBillToAddress.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


//                    f.append("\n" + "\n");
//
//                    getCustomFields(ViewName.Project.toString(), f);

                    break;

                case "Task":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<TaskAssignees Table>>" + "\n");
                    f.append("#set($taskAssignees = $itextGenericPdfData.customData.get('TASK_ASSIGNEES'))");

                    taskAssignees.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$taskAssignees.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<TaskTimeEntry Table>>");
                    f.append("\n" + "#set($taskMembers = $itextGenericPdfData.customData.get('TASK_TIME_ENTRY_MEMBERS_TABLE'))");

                    taskTimeEntryMembers.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$taskMembers.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<Notes Table>>");
                    f.append("\n" + "#set($notesTable = $itextGenericPdfData.customData.get('NOTES_TABLE'))");

                    taskNotesTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$notesTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<Labels>>");
                    f.append("\n" + "#set($taskLabels = $itextGenericPdfData.localizeLabels)");

                    localizeLabels.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$taskLabels.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "\n");
                    f.append("<<TaskContent Table>>");
                    f.append("\n" + "#set($contentTable = $itextGenericPdfData.customData.get('TASK_CONTENT_TABLE'))");

                    taskContentTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$contentTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Task Table>>");
                    f.append("\n" + "#set($custom=$itextGenericPdfData.customData.get('CUSTOM_FIELD').customFields.get('TASK'))");
                    getCustomFields(ViewName.Task.toString(), f);

                    break;


                case "Cash Receipt":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>" + "\n");
                    f.append("#foreach($row in $itextGenericPdfData.baseInvoice.customProductTable.rows.values())");

                    CRcustomProductTable.forEach((k, v) -> {
                        f.append("\n" + "$row.get('" + k + "')" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "#end" + "\n       ");


                    f.append("      \n");
                    f.append("<<BillToAddress Table>>");
                    f.append("\n" + "#set($customBillToAddress=$itextGenericPdfData.baseInvoice.customBillToAddress.rows)");

                    CRcustomBillToAddress.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customBillToAddress.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Invoice Table>>");
                    f.append("\n" + "#set($custom=$itextGenericPdfData.baseInvoice.customProductTable.customFields.get('INVOICE'))");
                    getCustomFields(ViewName.CashReceiptItem.toString(), f);

                    f.append("\n" + "\n");
                    f.append("<<Item Table>>");
                    f.append("\n" + "#set($custom = $itextGenericPdfData.customListData.get('CUSTOM_FIELD').get(0).get('GENERAL_INFORMATION')");
                    getItemTableFields("CashReceiptItem", f);
                    break;

                case "Cash Payment":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>" + "\n");
                    f.append("#foreach($row in $itextGenericPdfData.baseInvoice.customProductTable.rows.values())");

                    CPcustomProductTable.forEach((k, v) -> {
                        f.append("\n" + "$row.get('" + k + "')" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "#end" + "\n       ");


                    f.append("      \n");
                    f.append("<<BillToAddress Table>>");
                    f.append("\n" + "#set($customBillToAddress=$itextGenericPdfData.baseInvoice.customBillToAddress.rows)");

                    CPcustomBillToAddress.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customBillToAddress.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Invoice Table>>");
                    f.append("\n" + "#set($custom=$itextGenericPdfData.baseInvoice.customProductTable.customFields.get('INVOICE'))");
                    getCustomFields(ViewName.CashPaymentItem.toString(), f);

                    f.append("\n" + "\n");
                    f.append("<<Item Table>>");
                    f.append("\n" + "#set($custom = $itextGenericPdfData.customListData.get('CUSTOM_FIELD').get(0).get('GENERAL_INFORMATION')");
                    getItemTableFields("CashPaymentItem", f);
                    break;


                case "Bank Receipt":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>" + "\n");
                    f.append("#foreach($row in $itextGenericPdfData.baseInvoice.customProductTable.rows.values())");

                    BRcustomProductTable.forEach((k, v) -> {
                        f.append("\n" + "$row.get('" + k + "')" + "     ========> " + (v.isEmpty() ? k : v));


                    });

                    f.append("\n" + "#end" + "\n       ");

                    f.append("      \n");
                    f.append("<<BillToAddress Table>>");
                    f.append("\n" + "#set($customBillToAddress=$itextGenericPdfData.baseInvoice.customBillToAddress.rows)");

                    BRcustomBillToAddress.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customBillToAddress.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "      \n");
                    f.append("<<Bank Table>>");
                    f.append("\n" + "#set($bankTable = $itextGenericPdfData.baseInvoice.customBankTable.rows)");

                    BRcustomBankTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$bankTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("      \n");
                    f.append("\n" + "<<Account Table>>");
                    f.append("\n" + "#set($accountTable = $itextGenericPdfData.baseInvoice.customAccountTable.rows)");

                    BRcustomAccountTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$accountTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Invoice Table>>");
                    f.append("\n" + "#set($custom=$itextGenericPdfData.baseInvoice.customProductTable.customFields.get('INVOICE'))");
                    getCustomFields(ViewName.BankReceiptItem.toString(), f);

                    f.append("\n" + "\n");
                    f.append("<<Item Table>>");
                    f.append("\n" + "#set($custom = $itextGenericPdfData.customListData.get('CUSTOM_FIELD').get(0).get('GENERAL_INFORMATION')");
                    getItemTableFields("BankReceiptItem", f);
                    break;


                case "Bank Payment":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>");
                    f.append("\n" + "#foreach($row in $itextGenericPdfData.baseInvoice.customProductTable.rows.values())");

                    BPcustomProductTable.forEach((k, v) -> {
                        f.append("\n" + "$row.get('" + k + "')" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "#end" + "\n       ");


                    f.append("      \n");
                    f.append("<<BillToAddress Table>>");
                    f.append("\n" + "#set($customBillToAddress=$itextGenericPdfData.baseInvoice.customBillToAddress.rows)");

                    BPcustomBillToAddress.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customBillToAddress.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Invoice Table>>");
                    f.append("\n" + "#set($custom=$itextGenericPdfData.baseInvoice.customProductTable.customFields.get('INVOICE'))");
                    getCustomFields(ViewName.BankPaymentItem.toString(), f);

                    f.append("\n" + "\n");
                    f.append("<<Item Table>>");
                    f.append("\n" + "#set($custom = $itextGenericPdfData.customListData.get('CUSTOM_FIELD').get(0).get('GENERAL_INFORMATION')");
                    getItemTableFields("BankPaymentItem", f);
                    break;

                case "Trial Balance":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Assets Table>>");
                    f.append("\n" + "#set($assets = $itextGenericPdfData.customData.get('ASSETS'))");

                    TBassets.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$assets.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<Liabilities Table>>");
                    f.append("\n" + "#set($liabilities = $itextGenericPdfData.customData.get('LIABILITIES'))");

                    TBliabilities.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$liabilities.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<Equity Table>>");
                    f.append("\n" + "#set($equity = $itextGenericPdfData.customData.get('EQUITY'))");

                    TBequity.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$equity.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<Revenue Table>>");
                    f.append("\n" + "#set($revenue = $itextGenericPdfData.customData.get('REVENUE'))");

                    TBrevenue.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$revenue.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<Expenses Table>>");
                    f.append("#set($expenses = $itextGenericPdfData.customData.get('EXPENSES'))");

                    TBexpenses.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$expenses.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<Total Table>>");
                    f.append("\n" + "#set($total = $itextGenericPdfData.customData.get('TOTAL'))");

                    TBtotal.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$total.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<EmptyData Table>>");
                    f.append("\n" + "#set($emptyData = $itextGenericPdfData.customData.get('EMPTY_DATA'))");

                    TBemptyData.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$emptyData.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    fields.setText(f.toString());


                    break;

                case "Request for Quote":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>" + "\n");
                    f.append("#foreach($row in $itextGenericPdfData.baseInvoice.customProductTable.rows.values())");

                    RFQcustomProductTable.forEach((k, v) -> {
                        f.append("\n" + "$row.get('" + k + "')" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "#end" + "\n       ");


                    f.append("\n" + "\n");
                    f.append("<<BillToAddress Table>>");
                    f.append("\n" + "#set($customBillToAddress=$itextGenericPdfData.baseInvoice.customBillToAddress.rows)");

                    RFQcustomBillToAddress.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customBillToAddress.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<CUSTOM FIELDS>>");
                    f.append("\n" + "\n");
                    f.append("#set($custom = $itextGenericPdfData.customData.get('CUSTOM_FIELD'))");
                    getCustomFields(ViewName.RequestForQuote.toString(), f);

                    f.append("\n" + "\n");
                    f.append("<<Item Table>>");
                    f.append("\n" + "#set($custom = $itextGenericPdfData.customListData.get('CUSTOM_FIELD').get(0).get('GENERAL_INFORMATION')");
                    getItemTableFields("RFQ_ITEM", f);
                    break;


                case "Request for Purchase":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>" + "\n");
                    f.append("#foreach($row in $itextGenericPdfData.baseInvoice.customProductTable.rows.values())");

                    RFPcustomProductTable.forEach((k, v) -> {
                        f.append("\n" + "$row.get('" + k + "')" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "#end" + "\n       ");


                    f.append("\n" + "\n");
                    f.append("<<BillToAddress Table>>");
                    f.append("\n" + "#set($customBillToAddress=$itextGenericPdfData.baseInvoice.customBillToAddress.rows)");

                    RFPcustomBillToAddress.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customBillToAddress.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("#set($custom = $itextGenericPdfData.customData.get('CUSTOM_FIELD'))");
                    getCustomFields(ViewName.RequestForPurchase.toString(), f);

                    f.append("\n" + "\n");
                    f.append("<<Item Table>>");
                    f.append("\n" + "#set($custom = $itextGenericPdfData.customListData.get('CUSTOM_FIELD').get(0).get('GENERAL_INFORMATION')");
                    getItemTableFields("RFP_ITEM", f);

                    break;

                case "Leave Request":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>");
                    f.append("\n" + "#set($customNumberAndDatesTable=$itextGenericPdfData.baseInvoice.customNumberAndDatesTable.rows)");

                    LRcustomNumberAndDatesTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customNumberAndDatesTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<ApproversInformation Table>>");
                    f.append("\n" + "#set($approvers=$itextGenericPdfData.customData.get('APPROVERS_INFORMATION'))");

                    approversInformation.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$approvers.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<Information Table>>");
                    f.append("\n" + "#set($leave=$itextGenericPdfData.customData.get('LEAVE_REQUEST_INFORMATION'))");

                    leaveRequestInformation.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$leave.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "\n");
                    f.append("<<EmployeeInformation Table>>");
                    f.append("\n" + "#set($approvers=$itextGenericPdfData.customData.get('EMPLOYEE_INFORMATION'))");

                    employeeInformation.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$approvers.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "\n");
                    f.append("<<PersonalInfromation Table>>");
                    f.append("\n" + "#set($approvers=$itextGenericPdfData.customData.get('PERSONAL_INFORMATION'))");

                    personalInformation.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$approvers.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "\n");
                    f.append("<<Leave Request By Period Table>>");
                    f.append("\n" + "#set($leaveByPeriod=$itextGenericPdfData.customData.get('LEAVE_REQUESTS_BY_PERIOD'))");

                    leaveByPeriod.forEach((k, v) -> {
                        f.append("\n" + "#set($periodValue=$leaveByPeriod.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "\n");
                    f.append("<<Backup Employees Table>>");
                    f.append("\n" + "#set($backupEmployees=$itextGenericPdfData.customData.get('BACKUP_EMPLOYEES'))");

                    backupEmployees.forEach((k, v) -> {
                        f.append("\n" + "#set($employee=$backupEmployees.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<LeaveRequest Table>>");
                    f.append("\n" + "#set($custom=$itextGenericPdfData.customData.get('LEAVE_REQUEST_CUSTOM_FIELD').customFields.get('LEAVE_REQUEST'))");
                    getCustomFields(ViewName.LeaveRequest.toString(), f);

                    break;

                case "Stock Transfer":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>");
                    f.append("\n" + "#set($productTable = $itextGenericPdfData.customData.get('PRODUCT_TABLE'))");

                    productTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$productTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "      \n");
                    f.append("<<View Table>>");
                    f.append("\n" + "#set($viewTable = $itextGenericPdfData.customData.get('VIEW_TABLE'))");

                    viewTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$viewTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    fields.setText(f.toString());
//                    f.append("\n" + "\n");
//
//                    getCustomFields(ViewName.StockTransferList.toString(), f);

                    break;

                case "Meeting Minutes":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Meeting Table>>" + "\n");
                    f.append("#set($meetingTable = $itextGenericPdfData.customData.get('MEETING_TABLE'))");

                    meetingTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$meetingTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<NoteDetails Table>>");
                    f.append("\n" + "#set($notesTable = $itextGenericPdfData.customData.get('NOTE_DETAILS'))");

                    notesTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$notesTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "\n");
                    f.append("<<CUSTOM FIELDS>>");
                    f.append("\n" + "\n");
                    f.append("<<Metting Table>>");
                    f.append("\n" + "#set($custom=$itextGenericPdfData.customData.get('MEETING_CUSTOM_FIELD').customFields.get('HRMS'))");
                    getCustomFields(ViewName.MeetingMInutesView.toString(), f);

                    break;


                case "Manual Entry":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>" + "\n");
                    f.append("#foreach($row in $itextGenericPdfData.baseInvoice.customProductTable.rows.values())");

                    MEcustomProductTable.forEach((k, v) -> {
                        f.append("\n" + "$row.get('" + k + "')" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "#end" + "\n       ");


                    f.append("      \n");
                    f.append("<<BillToAddress Table>>");
                    f.append("\n" + "#set($customBillToAddress=$itextGenericPdfData.baseInvoice.customBillToAddress.rows)");

                    MEcustomBillToAddress.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customBillToAddress.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<Total Table>>");
                    f.append("\n" + "#set($customTotalTable=$itextGenericPdfData.baseInvoice.customTotalTable.rows)");

                    MEcustomTotalTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customTotalTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    fields.setText(f.toString());
                    f.append("\n" + "\n");
                    f.append("<<Item Table>>");
                    f.append("\n" + "#set($custom = $itextGenericPdfData.customListData.get('CUSTOM_FIELD').get(0).get('GENERAL_INFORMATION')");
                    getItemTableFields("ManualJournalItem", f);

                    break;


                case "Receive Payment":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>" + "\n");
                    f.append("#foreach($row in $itextGenericPdfData.baseInvoice.customProductTable.rows.values())");

                    RPAcustomProductTable.forEach((k, v) -> {
                        f.append("\n" + "$row.get('" + k + "')" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "#end" + "\n       ");


                    f.append("      \n");
                    f.append("<<BillToAddress Table>>");
                    f.append("\n" + "#set($customBillToAddress=$itextGenericPdfData.baseInvoice.customBillToAddress.rows)");

                    RPcustomBillToAddress.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customBillToAddress.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    fields.setText(f.toString());
//                    f.append("\n" + "\n");
//                    f.append("#set($custom=$itextGenericPdfData.baseInvoice.customProductTable.customFields.get('INVOICE'))");
//                    getCustomFields(ViewName.Payme.toString(), f);


                    break;


                case "Additional Payment":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>" + "\n");
                    f.append("#foreach($row in $itextGenericPdfData.baseInvoice.customProductTable.rows.values())");

                    APcustomProductTable.forEach((k, v) -> {
                        f.append("\n" + "$row.get('" + k + "')" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "#end" + "\n       ");

                    f.append("\n" + "      \n");
                    f.append("<<Total Table>>");
                    f.append("\n" + "#set($customTotalTable=$itextGenericPdfData.baseInvoice.customTotalTable.rows)");

                    APcustomTotalTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customTotalTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "      \n");
                    f.append("<<NumberAndDates Table>>");
                    f.append("\n" + "#set($customNumberAndDatesTable=$itextGenericPdfData.baseInvoice.customNumberAndDatesTable.rows)");

                    APcustomNumberAndDatesTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customNumberAndDatesTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    fields.setText(f.toString());
                    f.append("\n" + "\n");
                    f.append("<<Item Table>>");
                    f.append("\n" + "#set($custom = $itextGenericPdfData.customListData.get('CUSTOM_FIELD').get(0).get('GENERAL_INFORMATION')");
                    getItemTableFields("AdditionalPaymentItem", f);

//                    f.append("\n" + "\n");
//                    getCustomFields(ViewName.AdditionalPayment.toString(), f);


                    break;

                case "Cash Advance":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Details Table>>" + "\n");
                    f.append("#set($detailsTable = $itextGenericPdfData.customData.get('DETAILS_TABLE'))");

                    detailsTable.forEach((k, v) -> {
                        f.append("\n" + "#set($employeeName = $detailsTable.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Employee Table>>");
                    f.append("\n" + "#set($custom=$itextGenericPdfData.baseInvoice.customProductTable.customFields.get('EMPLOYEE'))");
                    getCustomFields(ViewName.CashAdvanceList.toString(), f);

                    break;


                case "Group Payrun":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Header Table>>" + "\n");
                    f.append("#set($headerTable = $itextGenericPdfData.customData.get('HEADER'))");

                    header.forEach((k, v) -> {
                        f.append("\n" + "#set($creatorLabel = $headerTable.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<Employee Table>>");
                    f.append("\n" + "#set($employeeTable = $itextGenericPdfData.customData.get('EMPLOYEE_TABLE'))");

                    employeeTable.forEach((k, v) -> {
                        f.append("\n" + "#set($creatorLabel =$employeeTable.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "\n");
                    f.append("<<Total Table>>");
                    f.append("\n" + " #set($totalTable = $itextGenericPdfData.customData.get('TOTAL_TABLE'))");

                    totalTable.forEach((k, v) -> {
                        f.append("\n" + "#set($creatorLabel = $totalTable.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<EmployerSettings Table>>");
                    f.append("\n" + "#set($employeeSettingsTable = $itextGenericPdfData.customData.get('EMPLOYER_SETTINGS_TABLE'))");

                    employerSettingsTable.forEach((k, v) -> {
                        f.append("\n" + "#set($creatorLabel = $employeeSettingsTable.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<Dhofar Table>>");
                    f.append("\n" + "#set($dhofarTable = $itextGenericPdfData.customData.get('DHOFAR_TABLE'))");

                    dhofarTable.forEach((k, v) -> {
                        f.append("\n" + "#set($creatorLabel = $dhofarTable.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "\n");
                    f.append("<<NonDhofar Table>>");
                    f.append("\n" + "#set($nondhofarTable = $itextGenericPdfData.customData.get('NON_DHOFAR_TABLE'))");

                    nonDhofarTable.forEach((k, v) -> {
                        f.append("\n" + "#set($creatorLabel = $nondhofarTable.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    fields.setText(f.toString());

//                    f.append("\n" + "\n");
//
////                    getCustomFields(ViewName.Project.toString(), f);

                    break;

                case "Lead summary pdf export":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Contact Table>>" + "\n");
                    f.append("#set($contactTable = $itextGenericPdfData.customData.get('CONTACT_INFORMATION'))");

                    contactInformation.forEach((k, v) -> {
                        f.append("\n" + "$leadNameVal = $contactTable.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<Address Table>>");
                    f.append("\n" + "#set($addressTable = $itextGenericPdfData.customData.get('ADDRESS_INFORMATION'))");

                    addressInformation.forEach((k, v) -> {
                        f.append("\n" + "#set($homeAddressTitle = $addressTable.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "\n");
                    f.append("<<Notes Table>>");
                    f.append("\n" + "#set($notesLead = $itextGenericPdfData.customData.get('NOTES_INFORMATION'))");

                    notesInformation.forEach((k, v) -> {
                        f.append("\n" + "#set($homeAddressTitle = $notesLead.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<Task Table>>");
                    f.append("\n" + "#set($taskTable = $itextGenericPdfData.customData.get('TASK_TABLE'))");

                    taskTable.forEach((k, v) -> {
                        f.append("\n" + "$homeAddressTitle = $taskTable.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("#set($custom = $itextGenericPdfData.customData.get('CUSTOM_FIELD'))");
                    getCustomFields(ViewName.Lead.toString(), f);

                    f.append("\n" + "\n");
                    f.append("<<Item Table>>");
                    f.append("\n" + "#set($custom = $itextGenericPdfData.customListData.get('CUSTOM_FIELD').get(0).get('GENERAL_INFORMATION')");
                    getItemTableFields("LeadItem", f);
                    break;


                case "Expense Report":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>" + "\n");
                    f.append("#foreach($row in $itextGenericPdfData.baseInvoice.customProductTable.rows.values())");

                    ERcustomProductTable.forEach((k, v) -> {
                        f.append("\n" + "$row.get('" + k + "')" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "#end" + "\n       ");

                    f.append("      \n");
                    f.append("<<Total Table>>");
                    f.append("\n" + "#set($customTotalTable=$itextGenericPdfData.baseInvoice.customTotalTable.rows)");

                    ERcustomTotalTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customTotalTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "      \n");
                    f.append("<<NumberAndDates Table>>");
                    f.append("\n" + "#set($customNumberAndDatesTable=$itextGenericPdfData.baseInvoice.customNumberAndDatesTable.rows)");

                    ERcustomNumberAndDatesTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customNumberAndDatesTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "      \n");
                    f.append("<<BillToAddress Table>>");
                    f.append("\n" + "#set($customBillToAddress=$itextGenericPdfData.baseInvoice.customBillToAddress.rows)");

                    ERcustomBillToAddress.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customBillToAddress.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "      \n");
                    f.append("<<PaymentHistory Table >>");
                    f.append("  \n" + "#set($paymentHistoryTable=$itextGenericPdfData.baseInvoice.paymentHistoryTable.rows)");

                    ERpaymentHistoryTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$paymentHistoryTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Invoice Table>>");
                    f.append("\n" + "#set($custom=$itextGenericPdfData.baseInvoice.customProductTable.customFields.get('INVOICE'))");
                    getCustomFields(ViewName.ExpenceReportView.toString(), f);

                    f.append("\n" + "\n");
                    f.append("<<Item Table>>");
                    f.append("\n" + "#set($custom = $itextGenericPdfData.customListData.get('CUSTOM_FIELD').get(0).get('GENERAL_INFORMATION')");
                    getItemTableFields("ExpenseClaimItem", f);
                    break;

                case "Pay Bill":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>" + "\n");
                    f.append("#foreach($row in $itextGenericPdfData.baseInvoice.customProductTable.rows.values())");

                    PBcustomProductTable.forEach((k, v) -> {
                        f.append("\n" + "$row.get('" + k + "')" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "#end" + "\n       ");


                    f.append("\n" + "\n");
                    f.append("<<BillToAddress Table>>");
                    f.append("\n" + "#set($customBillToAddress=$itextGenericPdfData.baseInvoice.customBillToAddress.rows)");

                    PBcustomBillToAddress.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customBillToAddress.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<Bank Table>>");
                    f.append("\n" + "#set($bankTable = $itextGenericPdfData.baseInvoice.customBankTable.rows)");

                    PBcustomBankTable.forEach((k, v) -> {
                        f.append("\n" + "#set($bankNameLabel = $bankTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "\n");
                    f.append("<<Account Table>>");
                    f.append("\n" + "#set($accountTable = $itextGenericPdfData.baseInvoice.customAccountTable.rows)");

                    PBcustomAccountTable.forEach((k, v) -> {
                        f.append("\n" + "#set($accountName = $accountTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Invoice Table>>");
                    f.append("\n" + "#set($custom=$itextGenericPdfData.baseInvoice.customProductTable.customFields.get('INVOICE'))");
                    getCustomFields(ViewName.BatchPayBillView.toString(), f);

                    break;

                case "Prepayment":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<BillToAddress Table>>");
                    f.append("\n" + "#set($customBillToAddress=$itextGenericPdfData.baseInvoice.customBillToAddress.rows)");

                    PrePaymentcustomBillToAddress.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customBillToAddress.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<Documents Table>>");
                    f.append("\n" + "#set($appliedDocuments = $itextGenericPdfData.customData.get('APPLIED_DOCUMENTS_TABLE'))");

                    appliedDocumentsTable.forEach((k, v) -> {
                        f.append("\n" + "#set($customerLab = $appliedDocuments.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<AppliedCredits Table>>");
                    f.append("\n" + "#set($appliedCredit = $itextGenericPdfData.customData.get('APPLY_CREDIT_DATA'))");

                    applyCreditData.forEach((k, v) -> {
                        f.append("\n" + "#set($customerLab = $appliedCredit.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<Payment Table>>");
                    f.append("\n" + "#set($paymentTable = $itextGenericPdfData.customData.get('PAYMENT_TABLE'))");

                    paymentTable.forEach((k, v) -> {
                        f.append("\n" + "#set($customerLab = $paymentTable.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Invoice Table>>");
                    f.append("\n" + "#set($custom=$itextGenericPdfData.baseInvoice.customProductTable.customFields.get('INVOICE'))");
                    getCustomFields(ViewName.Prepayment.toString(), f);


                    break;

                case "Supplier Credit":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<BillToAddress Table>>");
                    f.append("\n" + "#set($customBillToAddress=$itextGenericPdfData.baseInvoice.customBillToAddress.rows)");

                    SPcustomBillToAddress.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customBillToAddress.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<AppliedCredits Table>>");
                    f.append("\n" + "set($appliedCreditData = $itextGenericPdfData.customData.get('APPLY_CREDIT_DATA'))");

                    SPapplyCreditData.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$appliedCreditData.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<Payment Table>>");
                    f.append("\n" + "#set($paymentTable = $itextGenericPdfData.customData.get('PAYMENT_TABLE')");

                    SPpaymentTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$paymentTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    fields.setText(f.toString());
//                    f.append("\n" + "\n");
//
//                    getCustomFields(ViewName.Supplier.toString(), f);

                    break;

                case "Packing Slip":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>");
                    f.append("\n" + "#foreach($row in $itextGenericPdfData.baseInvoice.customProductTable.rows.values())");

                    PScustomProductTable.forEach((k, v) -> {
                        f.append("\n" + "$row.get('" + k + "')" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "#end" + "\n       ");

                    f.append("\n" + "\n");
                    f.append("<<Total Table>>");
                    f.append("\n" + "#set($customTotalTable=$itextGenericPdfData.baseInvoice.customTotalTable.rows)");

                    PScustomTotalTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customTotalTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<Expense Table>>");
                    f.append("   \n" + "#set($customExpenseTable=$itextGenericPdfData.baseInvoice.customExpenseTable.rows)");

                    PScustomExpenseTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customExpenseTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<NumberAndDates Table>>");
                    f.append("\n" + "#set($customNumberAndDatesTable=$itextGenericPdfData.baseInvoice.customNumberAndDatesTable.rows)");

                    PScustomNumberAndDatesTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customNumberAndDatesTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<BillToAddress Table>>");
                    f.append("\n" + "#set($customBillToAddress=$itextGenericPdfData.baseInvoice.customBillToAddress.rows)");

                    PScustomBillToAddress.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customBillToAddress.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<Bank Table>>");
                    f.append("\n" + "#set($bankTable = $itextGenericPdfData.baseInvoice.customBankTable.rows)");

                    PScustomBankTable.forEach((k, v) -> {
                        f.append("\n" + "#set($bankNameLabel = $bankTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<Account Table>>");
                    f.append("\n" + "#set($accountTable = $itextGenericPdfData.baseInvoice.customAccountTable.rows)");

                    PScustomAccountTable.forEach((k, v) -> {
                        f.append("\n" + "#set($accountName = $accountTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    fields.setText(f.toString());

//                    f.append("\n" + "\n");
//                    f.append("#set($custom=$itextGenericPdfData.baseInvoice.customProductTable.customFields.get('INVOICE'))");
//                    getCustomFields(ViewName.SaleInvoice.toString(), f);

                    break;

                case "Packing Slip(Order)":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>" + "\n");
                    f.append("#foreach($row in $itextGenericPdfData.baseInvoice.customProductTable.rows.values())");

                    PSOcustomProductTable.forEach((k, v) -> {
                        f.append("\n" + "$row.get('" + k + "')" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "#end" + "\n       ");

                    f.append("\n" + "\n");
                    f.append("<<Total Table>>");
                    f.append("\n" + "#set($customTotalTable=$itextGenericPdfData.baseInvoice.customTotalTable.rows)");

                    PSOcustomTotalTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customTotalTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<Expense Table>>");
                    f.append("\n" + "#set($customExpenseTable=$itextGenericPdfData.baseInvoice.customExpenseTable.rows)");

                    PSOcustomExpenseTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customExpenseTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<NumberAndDates Table>>");
                    f.append("\n" + "#set($customNumberAndDatesTable=$itextGenericPdfData.baseInvoice.customNumberAndDatesTable.rows)");

                    PSOcustomNumberAndDatesTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customNumberAndDatesTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<BillToAddress Table>>");
                    f.append("\n" + "#set($customBillToAddress=$itextGenericPdfData.baseInvoice.customBillToAddress.rows)");

                    PSOcustomBillToAddress.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customBillToAddress.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<Bank Table>>");
                    f.append("\n" + "#set($bankTable = $itextGenericPdfData.baseInvoice.customBankTable.rows)");

                    PSOcustomBankTable.forEach((k, v) -> {
                        f.append("\n" + "#set($bankNameLabel = $bankTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<Account Table>>");
                    f.append("\n" + "#set($accountTable = $itextGenericPdfData.baseInvoice.customAccountTable.rows)");

                    PSOcustomAccountTable.forEach((k, v) -> {
                        f.append("\n" + "#set($accountName = $accountTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    fields.setText(f.toString());

//                    f.append("\n" + "\n");
//                    f.append("#set($custom=$itextGenericPdfData.baseInvoice.customProductTable.customFields.get('INVOICE'))");
//                    getCustomFields(ViewName.SaleOrder.toString(), f);

                    break;


                case "Customer Supplier Balance":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<CustomerAddress Table>>" + "\n");
                    f.append(" #set($customerAddress = $itextGenericPdfData.customData.get('CUSTOMER_ADDRESS'))");

                    customerAddress.forEach((k, v) -> {
                        f.append("\n" + "#set($customerLab = $customerAddress.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "    \n");
                    f.append("<<CustomData Table>>");
                    f.append("\n" + "#set($customData = $itextGenericPdfData.customData.get('CUSTOM_DATA'))");

                    CSBcustomData.forEach((k, v) -> {
                        f.append("\n" + "#set($customerLab = $customData.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "    \n");
                    f.append("<<AgingSummary Table>>");
                    f.append("\n" + "#set($agingSummary = $itextGenericPdfData.customData.get('AGING_SUMMARY'))");

                    CSBagingSummary.forEach((k, v) -> {
                        f.append("\n" + "#set($customerLab = $agingSummary.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "    \n");
                    f.append("<<CurrencyData Table>>");
                    f.append("\n" + "#set($currencyData = $itextGenericPdfData.customData.get('CURRENCY_DATA'))");

                    CSBcurrencyData.forEach((k, v) -> {
                        f.append("\n" + "#set($customerLab = $currencyData.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    fields.setText(f.toString());

//                    f.append("\n" + "\n");
//                    f.append("#set($custom = $itextGenericPdfData.customData.get('CUSTOM_DATA'))");
////                    getCustomFields(ViewName.Project.toString(), f);

                    break;

                case "GDN":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Total Table>>" + "\n");
                    f.append(" #set($totalTable = $itextGenericPdfData.customData.get('TOTAL_TABLE'))");

                    GDNtotalTable.forEach((k, v) -> {
                        f.append("\n" + "#set($customerLab = $totalTable.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "\n");
                    f.append("<<Items Table>>");
                    f.append("\n" + "#set($items = $itextGenericPdfData.customData.get('ITEMS'))");

                    GDNitems.forEach((k, v) -> {
                        f.append("\n" + "#set($customerLab = $items.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "\n");
                    f.append("<<NumberAndDates Table>>");
                    f.append("\n" + "#set($customNumberAndDatesTable=$itextGenericPdfData.baseInvoice.customNumberAndDatesTable.rows)");

                    GDNcustomNumberAndDatesTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customNumberAndDatesTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("#set($custom = $itextGenericPdfData.customData.get('CUSTOM_FIELD'))");
                    getCustomFields(ViewName.GoodsDeliveredNote.toString(), f);

                    break;


                case "GRN":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Total Table>>" + "\n");
                    f.append(" #set($totalTable = $itextGenericPdfData.customData.get('TOTAL_TABLE'))");

                    GRNtotalTable.forEach((k, v) -> {
                        f.append("\n" + "#set($customerLab = $totalTable.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<Items Table>>");
                    f.append("\n" + "#set($items = $itextGenericPdfData.customData.get('ITEMS'))");

                    GRNitems.forEach((k, v) -> {
                        f.append("\n" + "#set($customerLab = $items.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "\n");
                    f.append("<<NumberAndDates Table>>");
                    f.append("\n" + "#set($customNumberAndDatesTable=$itextGenericPdfData.baseInvoice.customNumberAndDatesTable.rows)");

                    GRNcustomerNumberAndDatesTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customNumberAndDatesTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("#set($custom = $itextGenericPdfData.customData.get('CUSTOM_FIELD'))");
                    getCustomFields(ViewName.GoodsReceivedNote.toString(), f);

                    break;


                case "Opportunity":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<OpportunityInformation Table>>");
                    f.append("\n" + "#set($opportunityInfTable = $itextGenericPdfData.customData.get('OPPORTUNITY_INFORMATION'))");

                    opportunityInformation.forEach((k, v) -> {
                        f.append("\n" + "$currencyVal = $opportunityInfTable.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<CustomerDetails Table>>");
                    f.append("\n" + "#set($customerDetailTable = $itextGenericPdfData.customData.get('CUSTOMER_DETAIL'))");

                    customerDetail.forEach((k, v) -> {
                        f.append("\n" + "#set($customerLab = $customerDetailTable.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Opportunity Table>>");
                    f.append("\n" + "#set($custom=$itextGenericPdfData.customData.get('CUSTOM_FIELD').customFields.get('OPPORTUNITY'))");
                    getCustomFields(ViewName.Opportunity.toString(), f);

                    f.append("\n" + "\n");
                    f.append("<<Item Table>>");
                    f.append("\n" + "#set($custom = $itextGenericPdfData.customListData.get('CUSTOM_FIELD').get(0).get('GENERAL_INFORMATION')");
                    getItemTableFields("OpportunityCustomItem", f);

                    break;

                case "Contract":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<ContractContent Table>>" + "\n");
                    f.append("#set($contractContentTable = $itextGenericPdfData.customData.get('CONTRACT_CONTENT_TABLE')");

                    contractContentTable.forEach((k, v) -> {
                        f.append("\n" + "#set($contractDetailsLab = $contractContentTable.get(\"" + k + "\").get(\"COLUMN_NAME\"))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "     \n");
                    f.append("<<ProjectPosition Table>>");
                    f.append("\n" + "#set($positionsTable = $itextGenericPdfData.customData.get('PROJECT_POSITION_TABLE'))");

                    projectPositionTable.forEach((k, v) -> {
                        f.append("\n" + "#set($contractDetailsLab = $positionsTable.get(\"" + k + "\").get(\"COLUMN_NAME\"))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    fields.setText(f.toString());

//                    f.append("\n" + "\n");
//
//                    getCustomFields(ViewName.Contract.toString(), f);

                    break;

                case "Pick List View":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<NumberAndDates Table>>");
                    f.append("\n" + "#set($numAndDatesTable = $itextGenericPdfData.baseInvoice.customNumberAndDatesTable.rows)");

                    PLVcustomNumberAndDatesTable.forEach((k, v) -> {
                        f.append("\n" + "#set($customerLabel = $numAndDatesTable.get(\"" + k + "\").get(\"COLUMN_NAME\"))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<Product Table>>");
                    f.append("\n" + "#set($header = $itextGenericPdfData.customData.get('PRODUCT_TABLE'))");

                    PLVproductTable.forEach((k, v) -> {
                        f.append("\n" + "#set($customerLabel = $header.get(\"" + k + "\").get(\"COLUMN_NAME\"))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Quote Table>>");
                    f.append("#set($header = $itextGenericPdfData.customData.get('QUOTE_CUSTOM_FIELD').customFields)");
                    getCustomFields(ViewName.PickLists.toString(), f);


                    break;


                case "Employee Profile ID Cards":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<EmployeeDependents Table>>");
                    f.append("\n" + "#set($employeeDependentsTable = $itextGenericPdfData.customData.get('EMPLOYEE_DEPENDENTS_INFORMATION'))");

                    employeeDependentsInformation.forEach((k, v) -> {
                        f.append("\n" + "#set($empNameLab = $employeeDependentsTable.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<EmployeeInformation Table>>");
                    f.append("\n" + "#set($employeeTable = $itextGenericPdfData.customData.get('EMPLOYEE_INFORMATION'))");

                    employeeInformation.forEach((k, v) -> {
                        f.append("\n" + "#set($empNameLab = $employeeTable.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<MobilePhone Table>>");
                    f.append("\n" + "#set($employeePhoneTable = $itextGenericPdfData.customData.get('MOBILE_PHONE'))");

                    mobilePhone.forEach((k, v) -> {
                        f.append("\n" + "#set($homeAddressTitle = $employeePhoneTable.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<CorporateEmail Table>>");
                    f.append("\n" + "#set($corporateEmail = $itextGenericPdfData.customData.get('CORPORATE_EMAIL'))");

                    corporateEmail.forEach((k, v) -> {
                        f.append("\n" + "#set($homeAddressTitle = $corporateEmail.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<BankInformation Table>>");
                    f.append("\n" + "#set($bankTable = $itextGenericPdfData.customData.get('BANK_INFORMATION'))");

                    bankInformation.forEach((k, v) -> {
                        f.append("\n" + "#set($homeAddressTitle = $bankTable.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<AddressInformation Table>>");
                    f.append("\n" + "#set($addressTable = $itextGenericPdfData.customData.get('ADDRESS_INFORMATION'))");

                    employeeAddressInformation.forEach((k, v) -> {
                        f.append("\n" + "#set($homeAddressTitle = $addressTable.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<PersonalInformation Table>>");
                    f.append("\n" + "#set($personalTable = $itextGenericPdfData.customData.get('PERSONAL_INFORMATION'))");

                    personalInformation.forEach((k, v) -> {
                        f.append("\n" + "#set($homeAddressTitle = $personalTable.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<CorporatePhone Table>>");
                    f.append("\n" + "#set($corporatePhone = $itextGenericPdfData.customData.get('CORPORATE_PHONE'))");

                    corporatePhone.forEach((k, v) -> {
                        f.append("\n" + "#set($homeAddressTitle = $corporatePhone.rows.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Employee Table>>");
                    f.append("\n" + "#set($custom=$itextGenericPdfData.customData.get('CUSTOM_FIELD').getCustomFields.get('EMPLOYEE'))");
                    getCustomFields(ViewName.Employee.toString(), f);

                    f.append("\n" + "\n");
                    f.append("<<Item Table>>");
                    f.append("\n" + "#set($custom = $itextGenericPdfData.customListData.get('CUSTOM_FIELD').get(0).get('GENERAL_INFORMATION')");
                    getItemTableFields("EmployeeCustomItem", f);

                    break;


                case "Single Payrun":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>" + "\n");
                    f.append("#foreach($row in $itextGenericPdfData.baseInvoice.customProductTable.rows.values())");

                    SinglePayrunCustomProductTable.forEach((k, v) -> {
                        f.append("\n" + "$row.get('" + k + "')" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "#end" + "\n       ");

                    f.append("\n" + "\n");
                    f.append("<<Total Table>>");
                    f.append("\n" + "#set($customTotalTable=$itextGenericPdfData.baseInvoice.customTotalTable.rows)");


                    SinglePayrunCustomTotalTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customTotalTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "\n");
                    f.append("<<NumberAndDates Table>>");
                    f.append("\n" + "#set($numAndDatesTable = $itextGenericPdfData.baseInvoice.customNumberAndDatesTable.rows)");

                    SinglePayrunCustomNumberAndDatesTable.forEach((k, v) -> {
                        f.append("\n" + "#set($customerLabel = $numAndDatesTable.get(\"" + k + "\").get(\"COLUMN_NAME\"))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("\n" + "\n");
                    f.append("<<BillToAddress Table>>");
                    f.append("\n" + "#set($customBillToAddress=$itextGenericPdfData.baseInvoice.customBillToAddress.rows)");

                    SinglePayrunCustomBillToAddress.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customBillToAddress.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("#set($custom = $itextGenericPdfData.customData.get('CUSTOM_FIELD'))");
                    getCustomFields(ViewName.SinglePayrun.toString(), f);

                    break;

                case "Project":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<ProjectContent Table>>");
                    f.append("\n" + "#set($projectContentTable = $itextGenericPdfData.customData.get(\"PROJECT_CONTENT_TABLE\").rows)");

                    projectContentTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$projectContentTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<ProjectTasks Table>>");
                    f.append("\n" + "#set($projectTasks = $itextGenericPdfData.customListData.get(\"PROJECT_TASKS\").rows)");

                    projectTasks.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$projectTasks.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("<<MembersInvolved Table>>");
                    f.append("\n" + "#set($membersInvolved = $itextGenericPdfData.customListData.get(\"MEMBERS_INVOLVED\").rows)");

                    membersInvolved.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$membersInvolved.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });


                    f.append("\n" + "\n");
                    f.append("CUSTOM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Project Table>>");
                    f.append("\n" + "#set($custom = $itextGenericPdfData.customData.get('PROJECT_CUSTOM_FIELD').customFields.get('PROJECT'))");
                    getCustomFields(ViewName.Project.toString(), f);


                    break;


                case "Project Based Invoice":

                    f.append("SYSTEM FIELDS");
                    f.append("\n" + "\n");
                    f.append("<<Product Table>>" + "\n");
                    f.append("#foreach($row in $itextGenericPdfData.baseInvoice.customProductTable.rows.values())");


                    customProductTable.forEach((k, v) -> {
                        f.append("\n" + "$row.get('" + k + "')" + "     ========> " + (v.isEmpty() ? k : v));
                    });
                    f.append("\n" + "#end");

                    f.append("      \n");
                    f.append("\n" + "<<Total Table>>");
                    f.append("\n" + "#set($customTotalTable=$itextGenericPdfData.baseInvoice.customTotalTable.rows)");


                    customTotalTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customTotalTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("      \n");
                    f.append("\n" + "<<Expense Table>>");
                    f.append("   \n" + "#set($customExpenseTable=$itextGenericPdfData.baseInvoice.customExpenseTable.rows)");

                    customExpenseTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customExpenseTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    f.append("      \n");
                    f.append("\n" + "<<NumberAndDates Table>>");
                    f.append("   \n" + "#set($customNumberAndDatesTable=$itextGenericPdfData.baseInvoice.customNumberAndDatesTable.rows)");


                    customNumberAndDatesTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customNumberAndDatesTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));
                    });

                    f.append("      \n");
                    f.append("\n" + "<<BillToAddress Table>>");
                    f.append("  \n" + "#set($customBillToAddress=$itextGenericPdfData.baseInvoice.customBillToAddress.rows)");

                    customBillToAddress.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$customBillToAddress.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));
                    });


                    f.append("      \n");
                    f.append("\n" + "<<PaymentHistory Table>>");
                    f.append("  \n" + "#set($paymentHistoryTable=$itextGenericPdfData.baseInvoice.paymentHistoryTable.rows)");


                    paymentHistoryTable.forEach((k, v) -> {
                        f.append("\n" + "#set($clientName=$paymentHistoryTable.get('" + k + "').get('COLUMN_VALUE'))" + "     ========> " + (v.isEmpty() ? k : v));

                    });

                    break;
                default:
                    fields.setText("");
                    break;
            }
        }

    }

    private void getCustomFields(String entityName, StringBuffer f) {

        CommonService.App.get().getCompanyCustomFields(ViewName.valueOf(entityName), new AsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ArrayList<CompanyCustomFieldItem> companyCustomFieldItems) {

                companyCustomFieldItems.forEach((companyCustomFieldItem -> {
                    f.append("\n" + "#set($customfield=$custom.get('" + companyCustomFieldItem.getFieldName() + "').get('COLUMN_VALUE'))" + "  ======> " + companyCustomFieldItem.getAliasName());
                }));

                fields.setText(f.toString());

            }
        });

    }

    private void getItemTableFields(String itemTable, StringBuffer f) {

        CommonService.App.get().getItemTableCustomForms(itemTable, companyId, new AsyncCallback<ArrayList<String>>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ArrayList<String> strings) {
                strings.forEach((item -> {
                    f.append("\n" + "#set($customfield=$custom.get('" + item + "').get('COLUMN_VALUE'))" + "  ======> " + item);
                }));
            }
        });
    }

    private void fillCustomFormItem() {
        StringBuffer f = new StringBuffer();

        //TODO ACCOUNTS
        //got from CREATION_DATA Table
        TreeMap<String, String> creationDate = new TreeMap<>();
        creationDate.put("CREATED_DATE", "");
        creationDate.put("MODIFIED_DATE", "");
        creationDate.put("CREATED_BY", "");
        creationDate.put("MODIFIED_BY", "");

        TreeMap<String, String> customBillToAddress = new TreeMap<>();
        customBillToAddress.put("CLIENT_PHONE", "");
        customBillToAddress.put("CLIENT_CONTACT", "");
        customBillToAddress.put("CLIENT_EMAIL", "");
        customBillToAddress.put("PAYMENT_TERMS", "");
        customBillToAddress.put("CREATOR", "");
        customBillToAddress.put("CURRENT_APPROVER", "");
        customBillToAddress.put("PREV_APPROVER", "");
        customBillToAddress.put("STATUS", "");


        if (customFormItemList.getSelectedValue() != null && !Utils.isNullOrEmpty(customFormItemList.getSelectedValue().getName())) {
            CommonService.App.get().getCustomFieldByEntityCategory(customFormItemList.getSelectedValue().getName(), companyId, new AsyncCallback<HashMap<ArrayList<String>, ArrayList<String>>>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(HashMap<ArrayList<String>, ArrayList<String>> stringStringHashMap) {

                    if (stringStringHashMap.keySet() != null) {
                        f.append("<<CUSTOM TABLE>>");
                        f.append("\n" + "\n");
                        f.append("\n" + "#set($custom = $itextGenericPdfData.customListData.get('CUSTOM_FIELD').get(0).get('GENERAL_INFORMATION')");

                        stringStringHashMap.forEach((k, v) -> {
                            k.forEach((custom) -> {
                                f.append("\n" + "#set($customfield=$custom.get('" + custom + "').get('COLUMN_VALUE'))" + "     ========> " + custom);

                            });
                        });

                        f.append("\n" + "\n");
                        f.append("<<ITEM TABLE>>");
                        f.append("\n" + "\n");
                        if (stringStringHashMap.values() != null) {
                            f.append("\n" + "#set($custom = $itextGenericPdfData.customData.get('PROJECT_CUSTOM_FIELD').customFields.get('PROJECT'))");

                            stringStringHashMap.forEach((k, v) -> {
                                v.forEach((itemTable) -> {
                                    f.append("\n" + "#set($customfield=$custom.get('" + itemTable + "').get('COLUMN_VALUE'))" + "     ========> " + itemTable);

                                });
                            });
                        }


                    } else {
                    }

                    fields.setText(f.toString());
                }
            });


        }
    }
}