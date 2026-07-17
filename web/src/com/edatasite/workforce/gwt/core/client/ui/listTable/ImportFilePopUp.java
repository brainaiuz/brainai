package com.edatasite.workforce.gwt.core.client.ui.listTable;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import gwt.material.design.client.ui.html.Italic;

import java.util.HashMap;

import static com.edatasite.workforce.gwt.core.client.CommandConstants.ATTACHMENT_PARAM_BASE;
import static com.edatasite.workforce.gwt.core.client.CommandConstants.DESCRIPTION_PARAM_NAME;
import static com.edatasite.workforce.gwt.core.client.CommandConstants.UPLOAD_TYPE_PARAM_NAME;

/**
 * Created by IntelliJ IDEA.
 * User: Azazello
 * Date: March 12, 2018
 * Time: 1:43:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImportFilePopUp extends KpiModal {
    private Command submitSuccessfullyCompleted;
    private Integer objectId;
    private ImportTypeEnum type;
    private String viewType;
    private DataListBox currencyListBox;
    //Sample CSV files URLs
    public static final String SAMPLE_CONTACT_CSV = "https://s3.amazonaws.com/workforcetrack/000000000000/public/Contact_Sample_CSV.csv";
    public static final String SAMPLE_LEAD_CSV = "https://s3.amazonaws.com:443/workforcetrack/000000000000/public/3737/Lead_Sample_CSV.csv";
    public static final String SAMPLE_CANDIDATE_CSV = "https://s3.amazonaws.com/workforcetrack/000000000000/public/Candidate_sample_csv.csv";
    public static final String SAMPLE_CRMACCOUNT_CSV = "https://s3.amazonaws.com:443/workforcetrack/000000000000/public/3737/CRM_Account_Sample_CSV.csv";
    public static final String SAMPLE_OPPORTUNITY_CSV = "https://s3.amazonaws.com:443/workforcetrack/000000000000/public/3737/Opportunity_Sample_CSV.csv";
    public static final String SAMPLE_CLIENT_CSV = "https://s3.amazonaws.com:443/workforcetrack/000000000000/public/Client_import_template.csv";
    public static final String SAMPLE_SUPPLIER_CSV = "https://s3.amazonaws.com:443/workforcetrack/000000000000/public/Supplier_import_template.csv";
    public static final String SAMPLE_CHARTOFACCOUNTS_CSV = "https://s3.amazonaws.com:443/workforcetrack/000000000000/public/CHOA_import_template.csv";
    public static final String SAMPLE_PRODUCT_CSV = "https://s3.amazonaws.com:443/workforcetrack/000000000000/public/Product_import_template.csv";
    public static final String SAMPLE_PRODUCT_WITH_WAREHOUSE_CSV = "https://s3.amazonaws.com:443/workforcetrack/000000000000/public/Product_with_warehouse_import_template.csv";
    public static final String SAMPLE_INVENTORY_ITEMS_CSV = "https://s3.amazonaws.com:443/workforcetrack/000000000000/public/InventoryItems_import_template.csv";
    public static final String SAMPLE_SALESINVOICE_CSV = "https://s3.amazonaws.com:443/workforcetrack/000000000000/public/SalesInvoice_import_template.csv";
    public static final String SAMPLE_ASSEMBLY_ITEMS_CSV = "https://s3.amazonaws.com:443/workforcetrack/000000000000/public/InventoryItems_import_template.csv";
    public static final String SAMPLE_EMPLOYEE_CSV = "https://s3.amazonaws.com/workforcetrack/000000000000/public/Employee_Import_template.csv";
    public static final String SAMPLE_EXPENSE_CSV = "https://s3.amazonaws.com/workforcetrack/000000000000/public/Expence_claims.csv";
    public static final String SAMPLE_PROJECT_CSV = "https://s3.amazonaws.com/workforcetrack/000000000000/public/Project_import_template.csv";
    public static final String SAMPLE_MONTHLY_TIMESHEET_CSV = "https://s3.amazonaws.com/workforcetrack/000000000000/public/Monthly_timesheet_import_template.csv";
    public static final String SAMPLE_PROJECT_WITH_PARENT_CSV = "https://s3.amazonaws.com/workforcetrack/000000000000/public/Project_with_parent_import_template.csv";
    //    public static final String SAMPLE_MANUAL_TRANSACTIONS_XLS = CommandConstants.COMMON_URL + "/downloadManualTransactionsSampleExcel";
    public static final String SAMPLE_MANUAL_TRANSACTIONS_XLS = "https://s3.amazonaws.com/workforcetrack/000000000000/public/Manual_Entries_Sample_XLS.csv";
    public static final String SAMPLE_TALLY_MANUAL_TRANSACTIONS_CSV = "https://s3.amazonaws.com/workforcetrack/000000000000/public/Manual_Transaction_Import_Tally_Template.csv";
    public static final String SAMPLE_ADDITIONAL_PAYMENT_CSV = "https://s3.amazonaws.com/workforcetrack/000000000000/public/Additional_payment.csv";
    public static final String SAMPLE_BANK_PAYMENT_CSV = "https://s3.amazonaws.com/workforcetrack/000000000000/public/Bank_Payment_import_template.csv";
    public static final String SAMPLE_BANK_RECEIPT_CSV = "https://s3.amazonaws.com/workforcetrack/000000000000/public/Bank_receipt_import_template.csv";
    public static final String SAMPLE_CASH_PAYMENT_CSV = "https://s3.amazonaws.com/workforcetrack/000000000000/public/Cash_payment_import_template.csv";
    public static final String SAMPLE_CASH_RECEIPT_CSV = "https://s3.amazonaws.com/workforcetrack/000000000000/public/Cash_receipt_import_template.csv";
    public static final String SAMPLE_REPORT_DATE_CSV = "https://s3.amazonaws.com/workforcetrack/000000000000/public/Expence_claims.csv";
    public static final String SAMPLE_GROUP_PAYRUN_CSV = "https://workforcetrack.s3.amazonaws.com/000000000000/public/Group_Payrun_Import_template.csv";
    public static final String SAMPLE_PRODUCT_CATEGORY_CSV = "https://workforcetrack.s3.amazonaws.com/000000000000/public/Product_Category_Import_template.csv";
    public static final String SAMPLE_BUDGET_MANAGER_CSV = "https://workforcetrack.s3.amazonaws.com/000000000000/public/Budget_manager_import_Template.csv";
    public static final String SAMPLE_BRAND_CSV = "https://workforcetrack.s3.amazonaws.com/000000000000/public/Brand_import_Template.csv";
    public static final String SAMPLE_PURCHASE_ORDER_CSV = "https://workforcetrack.s3.amazonaws.com/000000000000/public/Purchase_Order_Import_template.csv";
    public static final String SAMPLE_LEAVE_ALLOWANCE_CSV = "https://workforcetrack.s3.amazonaws.com/000000000000/public/Leave_Allowance_Sample_Import_template.csv";
    public static final String SAMPLE_POSITION_CSV = "https://workforcetrack.s3.amazonaws.com/000000000000/public/Position_Import_template.csv";
    public static final String SAMPLE_EMPLOYEE_LEAVE_ALLOWANCES_CSV = "https://workforcetrack.s3.amazonaws.com/000000000000/public/Employee_Leave_Allowances_template.csv";
    public static final String SAMPLE_DEPARTMENT_CSV = "https://workforcetrack.s3.amazonaws.com/000000000000/public/Department_Import_template.csv";

    public ImportFilePopUp(ImportTypeEnum type, String viewType) {
        super();
        this.type = type;
        this.viewType = viewType;
        this.addStyleName("file--importFilePopUp");
        setWidth(650);
        init();
    }

    public void init() {
        FlexTable cont = new FlexTable();

        HTML label = new HTML(wfmStrings.messSelectFile());
        final FileUpload upload = new FileUpload();
        upload.setName(ATTACHMENT_PARAM_BASE + 0);
        TextArea description = new TextArea();
        description.setName(DESCRIPTION_PARAM_NAME);
        description.setVisible(false);
        description.setText("");
        TextBox uploadType = new TextBox();
        uploadType.setName(UPLOAD_TYPE_PARAM_NAME);
        uploadType.setVisible(false);
        uploadType.setText(Utils.getUploadTypeParam());

        final WfmFormPanel form = new WfmFormPanel("/CreateAttachment");
        form.addFormHandler(new FormHandler() {
            public void onSubmit(FormSubmitEvent event) {
            }

            public void onSubmitComplete(FormSubmitCompleteEvent event) {
                LoadingPanel.loading(false);
                if (form.getErrorString() == null) {
                    objectId = form.getObjectID();
                    close();
                    if (submitSuccessfullyCompleted != null) {
                        submitSuccessfullyCompleted.execute();
                    }
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.file()), Info.Type.INFO);
                } else {
                    Info.show(wfmStrings.messParseErrorCompareFile(), Info.Type.WARNING);
                }
            }
        });

        HorizontalPanel hp = new HorizontalPanel();
        hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        hp.add(upload);
        hp.add(description);
        hp.add(uploadType);

        final VerticalPanel vp = new VerticalPanel();
        vp.add(hp);
        String descText = wfmStrings.messCSVutf8Format();
        Anchor downloadLink = new Anchor(wfmStrings.downloadSample(), false, GWT.getHostPageBaseURL() + "", "_blank");


        HorizontalPanelDiv hpDiv = new HorizontalPanelDiv();
        hpDiv.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);

        if (ImportTypeEnum.CRM_ACCOUNT.equals(type)) {
            setTitle(wfmMessages.messImportName(wfmStrings.companies()));
            descText = wfmMessages.messImportDescription(wfmStrings.companies(), wfmStrings.companies());
            downloadLink.setHref(SAMPLE_CRMACCOUNT_CSV);
            hpDiv.add(downloadLink);
        } else if (ImportTypeEnum.BUDGET_MANAGER.equals(type)) {
            setTitle(wfmMessages.messImportName(Property.getPluralWithObjectCode(Constants.BUDGET_MANAGER, wfmStrings.importBudgetManager())));
            descText = wfmMessages.messImportDescription(wfmStrings.budgetManager(), wfmStrings.budgetManager());
            downloadLink.setHref(SAMPLE_BUDGET_MANAGER_CSV);
            hpDiv.add(downloadLink);
        } else if (ImportTypeEnum.LEAD.equals(type)) {
            setTitle(wfmMessages.messImportName(Property.getPluralWithObjectCode(Constants.LEADS, wfmStrings.leads())));
            descText = wfmMessages.messImportDescription(Property.getPluralWithObjectCode(Constants.LEADS, wfmStrings.leads()), Property.getPluralWithObjectCode(Constants.LEADS, wfmStrings.leads()));
            downloadLink.setHref(SAMPLE_LEAD_CSV);
            hpDiv.add(downloadLink);
        } else if (ImportTypeEnum.CONTACT.equals(type)) {
            setTitle(wfmMessages.messImportName(Property.getPluralWithObjectCode(Constants.Contacts, wfmStrings.contacts())));
            descText = wfmMessages.messImportDescription(Property.getPluralWithObjectCode(Constants.Contacts, wfmStrings.contacts()), Property.getPluralWithObjectCode(Constants.Contacts, wfmStrings.contacts()));
            downloadLink.setHref(SAMPLE_CONTACT_CSV);
            hpDiv.add(downloadLink);
        } else if (ImportTypeEnum.CANDIDATE.equals(type)) {
            setTitle(wfmMessages.messImportName(wfmStrings.candidates()));
            descText = wfmMessages.messImportDescription(wfmStrings.candidates(), wfmStrings.candidates());
            downloadLink.setHref(SAMPLE_CANDIDATE_CSV);
            hpDiv.add(downloadLink);
        } else if (ImportTypeEnum.OPPORTUNITY.equals(type)) {
            setTitle(wfmMessages.messImportName(Property.getPluralWithObjectCode(Constants.Opportunities, wfmStrings.opportunities())));
            descText = wfmMessages.messImportDescription(Property.getPluralWithObjectCode(Constants.Opportunities, wfmStrings.opportunities()), Property.getPluralWithObjectCode(Constants.Opportunities, wfmStrings.opportunities()));
            downloadLink.setHref(SAMPLE_OPPORTUNITY_CSV);
            hpDiv.add(downloadLink);
        } else if (ImportTypeEnum.PRODUCT.equals(type)) {
            setTitle(wfmMessages.messImportName(wfmStrings.products()));
            descText = wfmMessages.messImportDescription(wfmStrings.products(), wfmStrings.products());
            if (Utils.isMultiWarehouseEnabled()) {
                downloadLink.setHref(SAMPLE_PRODUCT_WITH_WAREHOUSE_CSV);
            } else {
                downloadLink.setHref(SAMPLE_PRODUCT_CSV);
            }
            hpDiv.add(downloadLink);
        } else if (ImportTypeEnum.INVENTORY_ITEMS.equals(type)) {
            setTitle(wfmMessages.messImportName(wfmStrings.inventoryItems()));
            descText = wfmMessages.messImportDescription(wfmStrings.inventoryItems(), wfmStrings.inventoryItems());
            downloadLink.setHref(SAMPLE_INVENTORY_ITEMS_CSV);
            hpDiv.add(downloadLink);
        } else if (ImportTypeEnum.NIMBLE_COMMERCE.equals(type)) {
            setTitle(wfmMessages.messImportName("Nimble Commerces"));
            descText = wfmMessages.messImportDescription("Nimble Commerces", "Nimble Commerces");
        } else if (ImportTypeEnum.CUSTOM_INVOICE.equals(type)) {
            setTitle(wfmMessages.messImportName(wfmStrings.saleInvoices()));
            descText = wfmMessages.messImportDescription(wfmStrings.saleInvoices(), wfmStrings.saleInvoices());
            downloadLink.setHref(SAMPLE_SALESINVOICE_CSV);
            hpDiv.add(downloadLink);
        } else if (ImportTypeEnum.CHART_OF_ACCOUNTS.equals(type)) {
            setTitle(wfmMessages.messImportName(wfmStrings.chartOfAccounts()));
            descText = wfmMessages.messImportDescription(wfmStrings.chartOfAccounts(), wfmStrings.chartOfAccounts());
            downloadLink.setHref(SAMPLE_CHARTOFACCOUNTS_CSV);
            hp.add(downloadLink);
        } else if (ImportTypeEnum.CUSTOMER.equals(type)) {
            setTitle(wfmMessages.messImportName(wfmStrings.customers()));
            descText = wfmMessages.messImportDescription(wfmStrings.customers(), wfmStrings.customers());
            downloadLink.setHref(SAMPLE_CLIENT_CSV);
            hpDiv.add(downloadLink);
        } else if (ImportTypeEnum.SUPPLIER.equals(type)) {
            setTitle(wfmMessages.messImportName(wfmStrings.suppliers()));
            descText = wfmMessages.messImportDescription(wfmStrings.suppliers(), wfmStrings.suppliers());
            downloadLink.setHref(SAMPLE_SUPPLIER_CSV);
            hpDiv.add(downloadLink);
        } else if (ImportTypeEnum.EMPLOYEE.equals(type)) {
            setTitle(wfmMessages.messImportName(wfmStrings.employees()));
            descText = wfmMessages.messImportDescription(wfmStrings.employees(), wfmStrings.employees());
            downloadLink.setHref(SAMPLE_EMPLOYEE_CSV);
            hpDiv.add(downloadLink);
        } else if (ImportTypeEnum.EXPENSE.equals(type)) {
            setTitle(wfmMessages.messImportName(wfmStrings.expenses()));
            descText = wfmMessages.messImportDescription(wfmStrings.expenses(), wfmStrings.expenses());
            downloadLink.setHref(SAMPLE_EXPENSE_CSV);
            hpDiv.add(downloadLink);
        } else if (ImportTypeEnum.COMPANY_EXPENSE.equals(type)) {
            setTitle(wfmMessages.messImportName(wfmStrings.companyExpense()));
            descText = wfmMessages.messImportDescription(wfmStrings.companyExpense(), wfmStrings.companyExpense());
            downloadLink.setHref(SAMPLE_EXPENSE_CSV);
            hpDiv.add(downloadLink);
        } else if (ImportTypeEnum.PROJECT.equals(type)) {
            setTitle(wfmMessages.messImportName(wfmStrings.projects()));
            descText = wfmMessages.messImportDescription(wfmStrings.projects(), wfmStrings.projects());
            if (Utils.isSetupSubProject()) {
                downloadLink.setHref(SAMPLE_PROJECT_WITH_PARENT_CSV);
            } else {
                downloadLink.setHref(SAMPLE_PROJECT_CSV);
            }
            hpDiv.add(downloadLink);
        } else if (ImportTypeEnum.MONTHLY_TIMESHEET.equals(type)) {
            setTitle(wfmMessages.messImportName(Property.get(Constants.TIMESHEET, wfmStrings.monthlyTimeSheet(), wfmStrings.timesheet())));
            descText = wfmMessages.messImportDescription(Property.get(Constants.TIMESHEET, wfmStrings.monthlyTimeSheet(), wfmStrings.timesheet()), Property.get(Constants.TIMESHEET, wfmStrings.monthlyTimeSheet(), wfmStrings.timesheet()));
            downloadLink.setHref(SAMPLE_MONTHLY_TIMESHEET_CSV);
            hpDiv.add(downloadLink);
        } else if (ImportTypeEnum.ADDITIONAL_PAYMENT.equals(type)) {
            setTitle(wfmMessages.messImportName(wfmStrings.additionalPayment()));
            descText = wfmMessages.messImportDescription(wfmStrings.additionalPayment(), wfmStrings.additionalPayment());
            downloadLink.setHref(SAMPLE_ADDITIONAL_PAYMENT_CSV);
            hpDiv.add(downloadLink);
        } else if (ImportTypeEnum.BANK_TRANSFER_TRANSACTION.equals(type)) {
            if ("RECEIVE_MONEY".equals(viewType)) {
                setTitle(wfmMessages.messImportName("Bank Receipts"));
                descText = wfmMessages.messImportDescription("Bank Receipts", "Bank Receipts");
                downloadLink.setHref(SAMPLE_BANK_RECEIPT_CSV);
            } else if ("SPEND_MONEY".equals(viewType)) {
                setTitle(wfmMessages.messImportName("Bank Payments"));
                descText = wfmMessages.messImportDescription("Bank Payments", "Bank Payments");
                downloadLink.setHref(SAMPLE_BANK_PAYMENT_CSV);
            } else if ("CASH_RECEIPT".equals(viewType)) {
                setTitle(wfmMessages.messImportName("Cash Receipts"));
                descText = wfmMessages.messImportDescription("Cash Receipts", "Cash Receipts");
                downloadLink.setHref(SAMPLE_CASH_RECEIPT_CSV);
            } else if ("CASH_PAYMENT".equals(viewType)) {
                setTitle(wfmMessages.messImportName("Cash Payments"));
                descText = wfmMessages.messImportDescription("Cash Payments", "Cash Payments");
                downloadLink.setHref(SAMPLE_CASH_PAYMENT_CSV);
            }
            hpDiv.add(downloadLink);
        } else if (ImportTypeEnum.MANUAL_TRANSACTION.equals(type)) {
            setTitle(wfmMessages.messImportName(wfmStrings.manualTransactions()));
            descText = wfmMessages.messImportDescription(wfmStrings.manualTransactions(), wfmStrings.manualTransactions());
            final HashMap<String, String> parametersMap = new HashMap<>();
            final FlowPanel content = new FlowPanel();
            currencyListBox = new DataListBox();
            currencyListBox.addStyleName("currency_list");
//            SimpleLink excelLink = new SimpleLink(wfmStrings.downloadSampleExcel());
            downloadLink.setHref(SAMPLE_MANUAL_TRANSACTIONS_XLS);

            GRow row = new GRow();
            GColumn currencyCol = new GColumn(GColumnEnum.COL_5);
            currencyCol.add(currencyListBox);
            row.add(currencyCol);

            GColumn downloadCol = new GColumn(GColumnEnum.COL_7);
            downloadCol.add(downloadLink);
            row.add(downloadCol);

            content.add(row);
            hpDiv.add(content);
            AllInOneService.App.get().getCurrencyAsSelectItems(new AsyncCallback<CurrencyItem[]>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(CurrencyItem[] result) {
                    currencyListBox.setItems(result);
                    for (CurrencyItem item : result) {
                        if (item.isCompanyCurrency()) {
                            currencyListBox.setSelected(item);
                        }
                    }
                    currencyListBox.setItems(result);
                }
            });
//            excelLink.addClickHandler(clickEvent -> {
//                parametersMap.put("currencyID", currencyListBox.getSelectedItem() != null ? currencyListBox.getSelectedItem().getId() + "" : null);
//                Utils.sendPDFOrExcelRequest(content, SAMPLE_MANUAL_TRANSACTIONS_XLS, parametersMap, "_blank");
//            });
        } else if (ImportTypeEnum.MANUAL_TRANSACTION_TALLY.equals(type)) {
            setTitle(wfmMessages.messImportName(wfmStrings.manualTransactions()));
            descText = wfmMessages.messImportDescription(wfmStrings.manualTransactions(), wfmStrings.manualTransactions());
            downloadLink.setHref(SAMPLE_TALLY_MANUAL_TRANSACTIONS_CSV);
            hpDiv.add(downloadLink);
        } else if (ImportTypeEnum.REPORT_DATA.equals(type)) {
            setTitle(wfmMessages.messImportName("Report data"));
            descText = wfmMessages.messImportDescription("Report data", "Report data");
            downloadLink.setHref(SAMPLE_REPORT_DATE_CSV);
            hpDiv.add(downloadLink);
        } else if (ImportTypeEnum.GROUP_PAYRUN.equals(type)) {
            setTitle(wfmMessages.messImportName(wfmStrings.groupPayrun()));
            descText = wfmMessages.messImportDescription(wfmStrings.groupPayrun(), wfmStrings.groupPayrun());
            downloadLink.setHref(SAMPLE_GROUP_PAYRUN_CSV);
            hpDiv.add(downloadLink);
        } else if (ImportTypeEnum.PAYMENT.equals(type)) {
            setTitle(wfmMessages.messImportName(wfmStrings.payments()));
            descText = wfmMessages.messImportDescription(wfmStrings.payments(), wfmStrings.payments());
        } else if (ImportTypeEnum.DEDUCTION.equals(type)) {
            setTitle(wfmMessages.messImportName(wfmStrings.deductions()));
            descText = wfmMessages.messImportDescription(wfmStrings.deductions(), wfmStrings.deductions());
        } else if (ImportTypeEnum.PRODUCT_CATEGORIES.equals(type)) {
            setTitle(wfmMessages.messImportName(wfmStrings.productCategory()));
            descText = wfmMessages.messImportDescription(wfmStrings.productCategory(), wfmStrings.productCategory());
            downloadLink.setHref(SAMPLE_PRODUCT_CATEGORY_CSV);
            hpDiv.add(downloadLink);
        } else if (ImportTypeEnum.BRAND.equals(type)) {
            setTitle(wfmMessages.messImportName(wfmStrings.brand()));
            descText = wfmMessages.messImportDescription(wfmStrings.brand(), wfmStrings.brand());
            downloadLink.setHref(SAMPLE_BRAND_CSV);
            hpDiv.add(downloadLink);
        } else if (ImportTypeEnum.PURCHASE_ORDER.equals(type)) {
            setTitle(wfmMessages.messImportName(wfmStrings.purchaseorder()));
            descText = wfmMessages.messImportDescription(wfmStrings.purchaseorder(), wfmStrings.purchaseorder());
            downloadLink.setHref(SAMPLE_PURCHASE_ORDER_CSV);
            hpDiv.add(downloadLink);
        } else if (ImportTypeEnum.ANNUAL_ALLOWANCE.equals(type)) {
            setTitle(wfmMessages.messImportName(wfmStrings.leaveAllowance()));
            descText = wfmMessages.messImportDescription(wfmStrings.leaveAllowance(), wfmStrings.leaveAllowance());
            downloadLink.setHref(SAMPLE_EMPLOYEE_LEAVE_ALLOWANCES_CSV);
            hpDiv.add(downloadLink);
        } else if (ImportTypeEnum.POSITION.equals(type)) {
            setTitle(wfmMessages.messImportName(wfmStrings.position()));
            descText = wfmMessages.messImportDescription(wfmStrings.position(), wfmStrings.position());
            downloadLink.setHref(SAMPLE_POSITION_CSV);
            hpDiv.add(downloadLink);
        } else if (ImportTypeEnum.DEPARTMENT.equals(type)) {
            setTitle(wfmMessages.messImportName(wfmStrings.department()));
            descText = wfmMessages.messImportDescription(wfmStrings.department(), wfmStrings.department());
            downloadLink.setHref(SAMPLE_DEPARTMENT_CSV);
            hpDiv.add(downloadLink);
        } else {
            setTitle(wfmStrings.importString());
        }

        Italic infoIcon = new Italic();
        infoIcon.setClass("ficon--info");
        new KpiToolTip(infoIcon, descText);
        hpDiv.add(infoIcon);

        hpDiv.setSpacing(5);
        hp.add(hpDiv);
        String importRequirements = "<p>" + wfmStrings.importRequirements() + "</p>";
        if (ImportTypeEnum.REPORT_DATA.equals(type)) {
            importRequirements = importRequirements + "<p>" + wfmStrings.reporDataRequirements() + "</p>";
        }
        vp.add(new HTML(importRequirements));
        vp.setSpacing(10);
        form.setWidget(vp);

        cont.setWidget(0, 0, label);
        cont.setWidget(1, 0, form);

        add(cont);

        WfmButton2 b = new WfmButton2(wfmStrings.uploadFile(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            if (!Utils.isNullOrEmpty(upload.getFilename())) {
                if (".csv".equals(upload.getFilename().substring(upload.getFilename().lastIndexOf("."))) || ImportTypeEnum.REPORT_DATA.equals(type)) {
                    form.submit();
                    LoadingPanel.loading(true);
                } else {
                    Info.show(wfmStrings.messSelectCSVFile(), Info.Type.WARNING);
                }
            }
        });

        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, clickEvent -> {
            close();
        });

        addButton(cancel);
        addButton(b);
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setSubmitCompleted(Command submitSuccessfullyCompleted) {
        this.submitSuccessfullyCompleted = submitSuccessfullyCompleted;
    }
}
