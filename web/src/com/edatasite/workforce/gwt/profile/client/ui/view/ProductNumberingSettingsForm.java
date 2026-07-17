package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.HelpTextPanel;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.PMNumberingSettings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dilshod Madrahimov on 7/8/15 6:17 PM
 */
public class ProductNumberingSettingsForm extends CustomForm {

    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private static final String DATE = "date";
    private static final String SPLITTER = "_";
    private static final String CLIENTCODE = "clientcode";
    private static final String PROJECTCODE = "projectcode";
    private static final String INVOICE_SETTINGS = "invoice_settings_";
    private static final String CREDIT_NOTE_SETTINGS = "creditNote_settings_";
    private static final String DEBIT_NOTE_SETTINGS = "debitNote_settings_";
    private static final String PRODUCT_NUMBERING_SETTINGS_VIEW = "product_numbering_settings_view_";

    private static final int BR = 0;
    private static final int BP = 1;
    private static final int CR = 2;
    private static final int CP = 3;
    private static final int MT = 4;
    private static final int PR = 5;
    private static final int SC = 6;
    private static final int SA = 7;
    private static final int CN = 8;
    private static final int RP = 9;
    private static final int PB = 10;
    private static final int GRN = 11;
    private static final int GDN = 12;
    private static final int DN = 13;

    private static final int INV = 14;
    private static final int SQ = 15;
    private static final int SO = 16;
    private static final int PO = 17;
    private static final int PI = 18;
    private static final int ST = 19;
    private static final int EX = 20;

    private final DateTimeFormat dateFormat = DateTimeFormat.getFormat("MM/yyyy");
    private TextBox pPrefix;
    private TextBox pNumCell1;
    private TextBox pNumCell2;
    private TextBox pNumCell3;
    private TextBox pNumCell4;

    private TextBox pcPrefix;
    private TextBox pcNumCell1;
    private TextBox pcNumCell2;
    private TextBox pcNumCell3;
    private TextBox pcNumCell4;

    private TextBox faPrefix;
    private TextBox faNumCell1;
    private TextBox faNumCell2;
    private TextBox faNumCell3;
    private TextBox faNumCell4;

    private TextBox rfpPrefix;
    private TextBox rfpNumCell1;
    private TextBox rfpNumCell2;
    private TextBox rfpNumCell3;
    private TextBox rfpNumCell4;

    private TextBox exPrefix;
    private TextBox exNumCell1;
    private TextBox exNumCell2;
    private TextBox exNumCell3;
    private TextBox exNumCell4;
    private KpiCheckBox exDateNumbering;
    private TextBox exNumberExample;


    private TextBox mtPrefix;
    private TextBox mtNumCell1;
    private TextBox mtNumCell2;
    private TextBox mtNumCell3;
    private TextBox mtNumCell4;
    private KpiCheckBox mtDateNumbering;
    private TextBox mtNumberExample;

    //grn number
    private TextBox grnPrefix;
    private TextBox grnNumCell1;
    private TextBox grnNumCell2;
    private TextBox grnNumCell3;
    private TextBox grnNumCell4;
    private KpiCheckBox grnDateNumbering;
    private TextBox grnNumberExample;

    //gdn number
    private TextBox gdnPrefix;
    private TextBox gdnNumCell1;
    private TextBox gdnNumCell2;
    private TextBox gdnNumCell3;
    private TextBox gdnNumCell4;
    private KpiCheckBox gdnDateNumbering;
    private TextBox gdnNumberExample;

    private TextBox bpPrefix;
    private TextBox bpNumCell1;
    private TextBox bpNumCell2;
    private TextBox bpNumCell3;
    private TextBox bpNumCell4;
    private KpiCheckBox bpDateNumbering;
    private TextBox bpNumberExample;

    private TextBox brPrefix;
    private TextBox brNumCell1;
    private TextBox brNumCell2;
    private TextBox brNumCell3;
    private TextBox brNumCell4;
    private KpiCheckBox brDateNumbering;
    private TextBox brNumberExample;

    private TextBox cpPrefix;
    private TextBox cpNumCell1;
    private TextBox cpNumCell2;
    private TextBox cpNumCell3;
    private TextBox cpNumCell4;
    private KpiCheckBox cpDateNumbering;
    private TextBox cpNumberExample;

    private TextBox crPrefix;
    private TextBox crNumCell1;
    private TextBox crNumCell2;
    private TextBox crNumCell3;
    private TextBox crNumCell4;
    private KpiCheckBox crDateNumbering;
    private TextBox crNumberExample;

    private TextBox rpPrefix;
    private TextBox rpNumCell1;
    private TextBox rpNumCell2;
    private TextBox rpNumCell3;
    private TextBox rpNumCell4;
    private KpiCheckBox rpDateNumbering;
    private TextBox rpNumberExample;

    private TextBox pbPrefix;
    private TextBox pbNumCell1;
    private TextBox pbNumCell2;
    private TextBox pbNumCell3;
    private TextBox pbNumCell4;
    private KpiCheckBox pbDateNumbering;
    private TextBox pbNumberExample;

    // Prepayment
    private TextBox prPrefix;
    private TextBox prNumCell1;
    private TextBox prNumCell2;
    private TextBox prNumCell3;
    private TextBox prNumCell4;
    private KpiCheckBox prDateNumbering;
    private TextBox prNumberExample;

    //Supplier Credit
    private TextBox scPrefix;
    private TextBox scNumCell1;
    private TextBox scNumCell2;
    private TextBox scNumCell3;
    private TextBox scNumCell4;
    private TextBox scNumberExample;
    private KpiCheckBox scDateNumbering;

    // Customer Refund
    private TextBox crfPrefix;
    private TextBox crfNumCell1;
    private TextBox crfNumCell2;
    private TextBox crfNumCell3;
    private TextBox crfNumCell4;
    private KpiCheckBox crfDateNumbering;
    private TextBox crfNumberExample;

    // Supplier Refund
    private TextBox srfPrefix;
    private TextBox srfNumCell1;
    private TextBox srfNumCell2;
    private TextBox srfNumCell3;
    private TextBox srfNumCell4;
    private KpiCheckBox srfDateNumbering;
    private TextBox srfNumberExample;

    // Stock adjustment
    private TextBox saPrefix;
    private TextBox saNumCell1;
    private TextBox saNumCell2;
    private TextBox saNumCell3;
    private TextBox saNumCell4;
    private TextBox saNumberExample;
    private KpiCheckBox saDateNumbering;

    // Stock adjustment
    private TextBox stPrefix;
    private TextBox stNumCell1;
    private TextBox stNumCell2;
    private TextBox stNumCell3;
    private TextBox stNumCell4;
    private TextBox stNumberExample;
    private KpiCheckBox stDateNumbering;

    //Invoice Numbering
    private KpiCheckBox dateNumbering;
    private KpiCheckBox clientNumbering;
    private KpiCheckBox projectNumbering;
    private TextBox numCell1;
    private TextBox numCell2;
    private TextBox numCell3;
    private TextBox numCell4;
    private TextBox invPrefix;
    private TextBox invNumberExample;

    private KpiCheckBox crnoDateNumbering;
    private KpiCheckBox crnoClientNumbering;
    private KpiCheckBox crnoProjectNumbering;
    private TextBox crnoNumCell1;
    private TextBox crnoNumCell2;
    private TextBox crnoNumCell3;
    private TextBox crnoNumCell4;
    private TextBox crnoInvPrefix;
    private TextBox crnoNumberExample;

    private KpiCheckBox dnoDateNumbering;
    private KpiCheckBox dnoClientNumbering;
    private KpiCheckBox dnoProjectNumbering;
    private TextBox dnoNumCell1;
    private TextBox dnoNumCell2;
    private TextBox dnoNumCell3;
    private TextBox dnoNumCell4;
    private TextBox dnoInvPrefix;
    private TextBox dnoNumberExample;
    //Sales Quote Numbering
    private KpiCheckBox sqDateNumbering;
    private KpiCheckBox sqClientNumbering;
    private KpiCheckBox sqProjectNumbering;
    private TextBox sqCell1;
    private TextBox sqCell2;
    private TextBox sqCell3;
    private TextBox sqCell4;
    private TextBox sqPrefix;
    private TextBox sqNumberExample;
    //Sales Order Numbering
    private KpiCheckBox soDateNumbering;
    private KpiCheckBox soClientNumbering;
    private KpiCheckBox soProjectNumbering;
    private TextBox soCell1;
    private TextBox soCell2;
    private TextBox soCell3;
    private TextBox soCell4;
    private TextBox soPrefix;
    private TextBox soNumberExample;
    //Purchase Order Numbering
    private KpiCheckBox poDateNumbering;
    private KpiCheckBox poClientNumbering;
    private KpiCheckBox poProjectNumbering;
    private TextBox poNumberExample;
    private TextBox poPrefix;
    private TextBox poCell1;
    private TextBox poCell2;
    private TextBox poCell3;
    private TextBox poCell4;
    //PI numbering
    private KpiCheckBox dateNumberingPI;
    private KpiCheckBox clientNumberingPI;
    private KpiCheckBox projectNumberingPI;
    private TextBox numCell1PI;
    private TextBox numCell2PI;
    private TextBox numCell3PI;
    private TextBox numCell4PI;
    private TextBox pInvPrefix;
    private TextBox pInvNumberExample;
    //PICN numbering
    private KpiCheckBox cnDateNumbering;
    private KpiCheckBox cnClientNumbering;
    private KpiCheckBox cnProjectNumbering;
    private KpiCheckBox dnDateNumbering;
    private KpiCheckBox dnClientNumbering;
    private KpiCheckBox dnProjectNumbering;
    private TextBox cnNumCell1;
    private TextBox cnNumCell2;
    private TextBox cnNumCell3;
    private TextBox cnNumCell4;
    private TextBox cnInvPrefix;
    private TextBox cnInvNumberExample;
    private TextBox dnNumCell1;
    private TextBox dnNumCell2;
    private TextBox dnNumCell3;
    private TextBox dnNumCell4;
    private TextBox dnInvPrefix;
    private TextBox dnInvNumberExample;

    private TextBox rentPrefix;
    private TextBox rentNumCell1;
    private TextBox rentNumCell2;
    private TextBox rentNumCell3;
    private TextBox rentNumCell4;

    private KpiSwitcher enableRestartNumbering;
    private DataListBox financialStartDate;
    private DataListBox financialStartMonth;
    private PMNumberingSettings settings;

    private FormGroup recievePaymentNumTable;
    private FormGroup payBillNumTable;
    private FormGroup saleOrderNumTable;

    public ProductNumberingSettingsForm() {
        super("productnumberingsettings", wfmStrings.numberingSettings());
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        this.initialize();
        return null;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PRODUCT_NUMBERING_SETTINGS_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected void addButtons() {
        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.getElement().setId("Product_numbering_setting_save_button");
        saveButton.addClickHandler(sender -> save());
        addButton(saveButton);
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        ProfileService.App.get().getPMNumberingSettings(new AbstractAsyncCallback<PMNumberingSettings>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                parseAndSetData(null);
            }

            @Override
            public void success(PMNumberingSettings result) {
                LoadingPanel.loading(false);
                settings = result;
                parseAndSetData(settings);
            }
        });

    }

    private void initialize() {
        pPrefix = this.createPrefCell();
        pPrefix.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "pPrefix");

        pNumCell1 = this.createNumCell(0);
        pNumCell1.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "pNumCell1");

        pNumCell2 = this.createNumCell(0);
        pNumCell2.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "pNumCell2");

        pNumCell3 = this.createNumCell(0);
        pNumCell3.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "pNumCell3");

        pNumCell4 = this.createNumCell(1);
        pNumCell4.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "pNumCell4");

        createProductCategoryCell();

        rentPrefix = this.createPrefCell();
        rentNumCell1 = this.createNumCell(0);
        rentNumCell2 = this.createNumCell(0);
        rentNumCell3 = this.createNumCell(0);
        rentNumCell4 = this.createNumCell(1);

        faPrefix = this.createPrefCell();
        faPrefix.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "faPrefix");

        faNumCell1 = this.createNumCell(0);
        faNumCell1.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "faNumCell1");

        faNumCell2 = this.createNumCell(0);
        faNumCell2.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "faNumCell2");

        faNumCell3 = this.createNumCell(0);
        faNumCell3.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "faNumCell3");

        faNumCell4 = this.createNumCell(1);
        faNumCell4.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "faNumCell4");

        rfpPrefix = this.createPrefCell();
        rfpPrefix.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "rfpPrefix");

        rfpNumCell1 = this.createNumCell(0);
        rfpNumCell1.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "rfpNumCell1");

        rfpNumCell2 = this.createNumCell(0);
        rfpNumCell2.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "rfpNumCell2");

        rfpNumCell3 = this.createNumCell(0);
        rfpNumCell3.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "rfpNumCell3");

        rfpNumCell4 = this.createNumCell(1);
        rfpNumCell4.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "rfpNumCell4");

        //Expense Starts
        exPrefix = this.createPrefCell();
        exPrefix.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "exPrefix");

        exNumCell1 = this.createNumCell(0, EX);
        exNumCell1.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "exNumCell1");

        exNumCell2 = this.createNumCell(0, EX);
        exNumCell2.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "exNumCell2");

        exNumCell3 = this.createNumCell(0, EX);
        exNumCell3.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "exNumCell3");

        exNumCell4 = this.createNumCell(1, EX);
        exNumCell4.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "exNumCell4");

        exDateNumbering = new KpiCheckBox(wfmStrings.date());
        exDateNumbering.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "exDateNumbering");

        exDateNumbering.addValueChangeHandler(sender -> AccountingUtils.drawExampleNumber(exNumCell1,
                exNumCell2,
                exNumCell3,
                exNumCell4,
                exDateNumbering.getValue(),
                exNumberExample,
                dateFormat));

        exNumberExample = new TextBox();
        exNumberExample.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "exNumberExample");
        exNumberExample.setEnabled(false);
        //Expense Ends

        mtPrefix = this.createPrefCell();
        mtPrefix.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "mtPrefix");

        mtNumCell1 = this.createNumCell(0, MT);
        mtNumCell1.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "mtNumCell1");

        mtNumCell2 = this.createNumCell(0, MT);
        mtNumCell2.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "mtNumCell2");

        mtNumCell3 = this.createNumCell(0, MT);
        mtNumCell3.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "mtNumCell3");

        mtNumCell4 = this.createNumCell(1, MT);
        mtNumCell4.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "mtNumCell4");

        mtDateNumbering = new KpiCheckBox(wfmStrings.date());
//        mtDateNumbering.setOffLabel(wfmStrings.date());
        mtDateNumbering.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "mtDateNumbering");

        mtDateNumbering.addValueChangeHandler(sender -> AccountingUtils.drawExampleNumber(mtNumCell1,
                mtNumCell2,
                mtNumCell3,
                mtNumCell4,
                mtDateNumbering.getValue(),
                mtNumberExample,
                dateFormat));

        mtNumberExample = new TextBox();
        mtNumberExample.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "mtNumberExample");
        mtNumberExample.setEnabled(false);

        //PO grn number
        grnPrefix = this.createPrefCell();
        grnPrefix.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "grnPrefix");
        grnNumCell1 = this.createNumCell(0, GRN);
        grnNumCell1.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "grnNumCell1");
        grnNumCell2 = this.createNumCell(0, GRN);
        grnNumCell2.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "grnNumCell2");
        grnNumCell3 = this.createNumCell(0, GRN);
        grnNumCell3.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "grnNumCell3");
        grnNumCell4 = this.createNumCell(1, GRN);
        grnNumCell4.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "grnNumCell4");
        grnDateNumbering = new KpiCheckBox(wfmStrings.date());
//        grnDateNumbering.setOffLabel(wfmStrings.date());
        grnDateNumbering.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "grnDateNumbering");
        grnDateNumbering.addValueChangeHandler(sender -> AccountingUtils.drawExampleNumber(grnNumCell1,
                grnNumCell2,
                grnNumCell3,
                grnNumCell4,
                grnDateNumbering.getValue(),
                grnNumberExample,
                dateFormat));

        grnNumberExample = new TextBox();
        grnNumberExample.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "grnNumberExample");
        grnNumberExample.setEnabled(false);

        gdnPrefix = this.createPrefCell();
        gdnPrefix.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "gdnPrefix");

        gdnNumCell1 = this.createNumCell(0, GDN);
        gdnNumCell1.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "gdnNumCell1");

        gdnNumCell2 = this.createNumCell(0, GDN);
        gdnNumCell2.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "gdnNumCell2");

        gdnNumCell3 = this.createNumCell(0, GDN);
        gdnNumCell3.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "gdnNumCell3");

        gdnNumCell4 = this.createNumCell(1, GDN);
        gdnNumCell4.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "gdnNumCell4");

        gdnDateNumbering = new KpiCheckBox(wfmStrings.date());
//        gdnDateNumbering.setOffLabel(wfmStrings.date());
        gdnDateNumbering.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "gdnDateNumbering");
        gdnDateNumbering.addValueChangeHandler(sender -> AccountingUtils.drawExampleNumber(gdnNumCell1,
                gdnNumCell2,
                gdnNumCell3,
                gdnNumCell4,
                gdnDateNumbering.getValue(),
                gdnNumberExample,
                dateFormat));
        gdnNumberExample = new TextBox();
        gdnNumberExample.setWidth("80px");
        gdnNumberExample.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "grnNumberExample");
        gdnNumberExample.setEnabled(false);

        // Bank Payment
        bpPrefix = this.createPrefCell();
        bpPrefix.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "bpPrefix");

        bpNumCell1 = this.createNumCell(0, BP);
        bpNumCell1.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "bpNumCell1");

        bpNumCell2 = this.createNumCell(0, BP);
        bpNumCell2.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "bpNumCell2");

        bpNumCell3 = this.createNumCell(0, BP);
        bpNumCell3.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "bpNumCell3");

        bpNumCell4 = this.createNumCell(1, BP);
        bpNumCell4.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "bpNumCell4");

        bpDateNumbering = new KpiCheckBox(wfmStrings.date());
//        bpDateNumbering.setOffLabel(wfmStrings.date());
        bpDateNumbering.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "bpDateNumbering");

        bpDateNumbering.addValueChangeHandler(sender -> AccountingUtils.drawExampleNumber(bpNumCell1,
                bpNumCell2,
                bpNumCell3,
                bpNumCell4,
                bpDateNumbering.getValue(),
                bpNumberExample,
                dateFormat));
        bpNumberExample = new TextBox();
        bpNumberExample.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "bpNumberExample");
        bpNumberExample.setEnabled(false);

        //Bank Receipt
        brPrefix = this.createPrefCell();
        brPrefix.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "brPrefix");

        brNumCell1 = this.createNumCell(0, BR);
        brNumCell1.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "brNumCell1");

        brNumCell2 = this.createNumCell(0, BR);
        brNumCell2.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "brNumCell2");

        brNumCell3 = this.createNumCell(0, BR);
        brNumCell3.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "brNumCell3");

        brNumCell4 = this.createNumCell(1, BR);
        brNumCell4.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "brNumCell4");

        brDateNumbering = new KpiCheckBox(wfmStrings.date());
//        brDateNumbering.setOffLabel(wfmStrings.date());
        brDateNumbering.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "brDateNumbering");

        brDateNumbering.addValueChangeHandler(sender -> AccountingUtils.drawExampleNumber(brNumCell1, brNumCell2, brNumCell3, brNumCell4, brDateNumbering.getValue(), brNumberExample, dateFormat));

        brNumberExample = new TextBox();
        brNumberExample.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "brNumberExample");
        brNumberExample.setEnabled(false);

        // Cash Payment
        cpPrefix = this.createPrefCell();
        cpPrefix.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "cpPrefix");

        cpNumCell1 = this.createNumCell(0, CP);
        cpNumCell1.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "cpNumCell1");

        cpNumCell2 = this.createNumCell(0, CP);
        cpNumCell2.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "cpNumCell2");

        cpNumCell3 = this.createNumCell(0, CP);
        cpNumCell3.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "cpNumCell3");

        cpNumCell4 = this.createNumCell(1, CP);
        cpNumCell4.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "cpNumCell4");

        cpDateNumbering = new KpiCheckBox(wfmStrings.date());
//        cpDateNumbering.setOffLabel(wfmStrings.date());
        cpDateNumbering.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "cpDateNumbering");

        cpDateNumbering.addValueChangeHandler(sender -> AccountingUtils.drawExampleNumber(cpNumCell1,
                cpNumCell2,
                cpNumCell3,
                cpNumCell4,
                cpDateNumbering.getValue(),
                cpNumberExample,
                dateFormat));

        cpNumberExample = new TextBox();
        cpNumberExample.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "cpNumberExample");
        cpNumberExample.setEnabled(false);

        //Cash Receipt
        crPrefix = this.createPrefCell();
        crPrefix.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "crPrefix");

        crNumCell1 = this.createNumCell(0, CR);
        crNumCell1.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "crNumCell1");

        crNumCell2 = this.createNumCell(0, CR);
        crNumCell2.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "crNumCell2");

        crNumCell3 = this.createNumCell(0, CR);
        crNumCell3.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "crNumCell3");

        crNumCell4 = this.createNumCell(1, CR);
        crNumCell4.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "crNumCell4");

        crDateNumbering = new KpiCheckBox(wfmStrings.date());
//        crDateNumbering.setOffLabel(wfmStrings.date());
        crDateNumbering.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "crDateNumbering");

        crDateNumbering.addValueChangeHandler(sender -> AccountingUtils.drawExampleNumber(crNumCell1,
                crNumCell2,
                crNumCell3,
                crNumCell4,
                crDateNumbering.getValue(),
                crNumberExample,
                dateFormat));

        crNumberExample = new TextBox();
        crNumberExample.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "crNumberExample");
        crNumberExample.setEnabled(false);

        //Prepayment
        prPrefix = this.createPrefCell();
        prPrefix.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "prPrefix");
        prNumberExample = new TextBox();

        prNumberExample.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "prNumberExample");
        prNumberExample.setEnabled(false);

        prNumCell1 = this.createNumCell(0, PR);
        prNumCell1.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "prNumCell1");

        prNumCell2 = this.createNumCell(0, PR);
        prNumCell2.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "prNumCell2");

        prNumCell3 = this.createNumCell(0, PR);
        prNumCell3.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "prNumCell3");

        prNumCell4 = this.createNumCell(1, PR);
        prNumCell4.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "prNumCell4");

        prDateNumbering = new KpiCheckBox(wfmStrings.date());
//        prDateNumbering.setOffLabel(wfmStrings.date());
        prDateNumbering.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "prDateNumbering");

        prDateNumbering.addValueChangeHandler(sender -> AccountingUtils.drawExampleNumber(prNumCell1,
                prNumCell2,
                prNumCell3,
                prNumCell4,
                prDateNumbering.getValue(),
                prNumberExample,
                dateFormat));


        //Customer Refund
        crfPrefix = this.createPrefCell();
        crfPrefix.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "crfPrefix");
        crfNumberExample = new TextBox();

        crfNumberExample.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "crfNumberExample");
        crfNumberExample.setEnabled(false);

        crfNumCell1 = this.createNumCell(0, PR);
        crfNumCell1.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "crfNumCell1");

        crfNumCell2 = this.createNumCell(0, PR);
        crfNumCell2.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "crfNumCell2");

        crfNumCell3 = this.createNumCell(0, PR);
        crfNumCell3.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "crfNumCell3");

        crfNumCell4 = this.createNumCell(1, PR);
        crfNumCell4.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "crfNumCell4");

        crfDateNumbering = new KpiCheckBox(wfmStrings.date());
//      crfprDateNumbering.setOffLabel(wfmStrings.date());
        crfDateNumbering.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "crfDateNumbering");

        crfDateNumbering.addValueChangeHandler(sender -> AccountingUtils.drawExampleNumber(crfNumCell1,
                crfNumCell2,
                crfNumCell3,
                crfNumCell4,
                crfDateNumbering.getValue(),
                crfNumberExample,
                dateFormat));

        //Supplier Refund
        srfPrefix = this.createPrefCell();
        srfPrefix.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "srfPrefix");
        srfNumberExample = new TextBox();

        srfNumberExample.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "srfNumberExample");
        srfNumberExample.setEnabled(false);

        srfNumCell1 = this.createNumCell(0, PR);
        srfNumCell1.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "srfNumCell1");

        srfNumCell2 = this.createNumCell(0, PR);
        srfNumCell2.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "srfNumCell2");

        srfNumCell3 = this.createNumCell(0, PR);
        srfNumCell3.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "srfNumCell3");

        srfNumCell4 = this.createNumCell(1, PR);
        srfNumCell4.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "srfNumCell4");

        srfDateNumbering = new KpiCheckBox(wfmStrings.date());
//      srfprDateNumbering.setOffLabel(wfmStrings.date());
        srfDateNumbering.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "srfDateNumbering");

        srfDateNumbering.addValueChangeHandler(sender -> AccountingUtils.drawExampleNumber(srfNumCell1,
                srfNumCell2,
                srfNumCell3,
                srfNumCell4,
                srfDateNumbering.getValue(),
                srfNumberExample,
                dateFormat));


        //Supplier Credit
        scPrefix = this.createPrefCell();
        scPrefix.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "prPrefix");

        scNumberExample = new TextBox();
        scNumberExample.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "scNumberExample");
        scNumberExample.setEnabled(false);
        scNumCell1 = this.createNumCell(0, SC);
        scNumCell1.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "scNumCell1");
        scNumCell2 = this.createNumCell(0, SC);
        scNumCell2.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "scNumCell2");
        scNumCell3 = this.createNumCell(0, SC);
        scNumCell3.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "scNumCell3");
        scNumCell4 = this.createNumCell(1, SC);
        scNumCell4.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "scNumCell4");
        scDateNumbering = new KpiCheckBox(wfmStrings.date());
//        scDateNumbering.setOffLabel(wfmStrings.date());
        scDateNumbering.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "scDateNumbering");

        scDateNumbering.addValueChangeHandler(sender -> AccountingUtils.drawExampleNumber(scNumCell1,
                scNumCell2,
                scNumCell3,
                scNumCell4,
                scDateNumbering.getValue(),
                scNumberExample,
                dateFormat));

        //Stock Adjustment
        saPrefix = this.createPrefCell();
        saPrefix.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "saPrefix");
        saNumberExample = new TextBox();
        saNumberExample.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "saNumberExample");
        saNumCell1 = this.createNumCell(0, SA);
        saNumCell1.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "saNumCell1");
        saNumCell2 = this.createNumCell(0, SA);
        saNumCell2.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "saNumCell2");
        saNumCell3 = this.createNumCell(0, SA);
        saNumCell3.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "saNumCell3");
        saNumCell4 = this.createNumCell(1, SA);
        saNumCell4.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "saNumCell4");
        saDateNumbering = new KpiCheckBox(wfmStrings.date());
//        saDateNumbering.setOffLabel(wfmStrings.date());
        saDateNumbering.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "saDateNumbering");

        saDateNumbering.addValueChangeHandler(sender -> AccountingUtils.drawExampleNumber(saNumCell1,
                saNumCell2,
                saNumCell3,
                saNumCell4,
                saDateNumbering.getValue(),
                saNumberExample,
                dateFormat));

        //Stock Transfer
        stPrefix = this.createPrefCell();
        stPrefix.setEnabled(false);
        stPrefix.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "stPrefix");
        stNumberExample = new TextBox();
        stNumberExample.setEnabled(false);

        stNumberExample.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "stNumberExample");
        stNumberExample.setEnabled(false);
        stNumCell1 = this.createNumCell(0, ST);
        stNumCell1.setEnabled(false);
        stNumCell1.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "stNumCell1");
        stNumCell2 = this.createNumCell(0, ST);
        stNumCell2.setEnabled(false);
        stNumCell2.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "stNumCell2");
        stNumCell3 = this.createNumCell(0, ST);
        stNumCell3.setEnabled(false);
        stNumCell3.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "stNumCell3");
        stNumCell4 = this.createNumCell(1, ST);
        stNumCell4.setEnabled(false);
        stNumCell4.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "stNumCell4");
        stDateNumbering = new KpiCheckBox(wfmStrings.date());
        stDateNumbering.setEnabled(false);
//        saDateNumbering.setOffLabel(wfmStrings.date());
        stDateNumbering.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "stDateNumbering");

        stDateNumbering.addValueChangeHandler(sender -> AccountingUtils.drawExampleNumber(stNumCell1,
                stNumCell2,
                stNumCell3,
                stNumCell4,
                stDateNumbering.getValue(),
                stNumberExample,
                dateFormat));

        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_RP_AND_PI_NUMBERING_SETTINGS)) {
            //Receive Payment numbering
            rpPrefix = this.createPrefCell();
            rpPrefix.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "rpPrefix");

            rpNumCell1 = this.createNumCell(0, RP);
            rpNumCell1.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "rpNumCell1");

            rpNumCell2 = this.createNumCell(0, RP);
            rpNumCell2.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "rpNumCell2");

            rpNumCell3 = this.createNumCell(0, RP);
            rpNumCell3.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "rpNumCell3");

            rpNumCell4 = this.createNumCell(1, RP);
            rpNumCell4.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "rpNumCell4");

            rpDateNumbering = new KpiCheckBox(wfmStrings.date());
//            rpDateNumbering.setOffLabel(wfmStrings.date());
            rpDateNumbering.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "rpDateNumbering");

            rpDateNumbering.addValueChangeHandler(sender -> AccountingUtils.drawExampleNumber(rpNumCell1,
                    rpNumCell2,
                    rpNumCell3,
                    rpNumCell4,
                    rpDateNumbering.getValue(),
                    rpNumberExample,
                    dateFormat));

            rpNumberExample = new TextBox();

            rpNumberExample.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "rpNumberExample");
            rpNumberExample.setEnabled(false);

            //Pay Bill numbering
            pbPrefix = this.createPrefCell();
            pbPrefix.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "pbPrefix");

            pbNumCell1 = this.createNumCell(0, PB);
            pbNumCell1.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "pbNumCell1");

            pbNumCell2 = this.createNumCell(0, PB);
            pbNumCell2.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "pbNumCell2");

            pbNumCell3 = this.createNumCell(0, PB);
            pbNumCell3.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "pbNumCell3");

            pbNumCell4 = this.createNumCell(1, PB);
            pbNumCell4.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "pbNumCell4");

            pbDateNumbering = new KpiCheckBox(wfmStrings.date());
//            pbDateNumbering.setOffLabel(wfmStrings.date());
            pbDateNumbering.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "pbDateNumbering");

            pbDateNumbering.addValueChangeHandler(sender -> AccountingUtils.drawExampleNumber(pbNumCell1, pbNumCell2, pbNumCell3, pbNumCell4, pbDateNumbering.getValue(), pbNumberExample, dateFormat));

            pbNumberExample = new TextBox();

            pbNumberExample.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "pbNumberExample");
            pbNumberExample.setEnabled(false);
        }
        final FlexTable pTable = new FlexTable();
        pTable.setStyleName("mod_table--auto");
        final FlexTable pcTable = new FlexTable();
        pcTable.setStyleName("mod_table--auto");
        final FlexTable faTable = new FlexTable();
        faTable.setStyleName("mod_table--auto");
        final FlexTable rfpTable = new FlexTable();
        rfpTable.setStyleName("mod_table--auto");
        final FlexTable exTable = new FlexTable();
        exTable.setStyleName("mod_table--auto");
        final FlexTable mtTable = new FlexTable();
        mtTable.setStyleName("mod_table--auto");
        final FlexTable bpTable = new FlexTable();
        bpTable.setStyleName("mod_table--auto");
        final FlexTable brTable = new FlexTable();
        brTable.setStyleName("mod_table--auto");
        final FlexTable crTable = new FlexTable();
        crTable.setStyleName("mod_table--auto");
        final FlexTable cpTable = new FlexTable();
        cpTable.setStyleName("mod_table--auto");
        final FlexTable rpTable = new FlexTable();
        rpTable.setStyleName("mod_table--auto");
        final FlexTable pbTable = new FlexTable();
        pbTable.setStyleName("mod_table--auto");
        final FlexTable grnTable = new FlexTable();
        grnTable.setStyleName("mod_table--auto");
        final FlexTable gdnTable = new FlexTable();
        gdnTable.setStyleName("mod_table--auto");
        final FlexTable prepaymentTable = new FlexTable();
        prepaymentTable.setStyleName("mod_table--auto");
        final FlexTable customerRefundTable = new FlexTable();
        customerRefundTable.setStyleName("mod_table--auto");
        final FlexTable supplierRefundTable = new FlexTable();
        supplierRefundTable.setStyleName("mod_table--auto");
        final FlexTable supplierCreditTable = new FlexTable();
        supplierCreditTable.setStyleName("mod_table--auto");
        final FlexTable stockAdjustmentTable = new FlexTable();
        stockAdjustmentTable.setStyleName("mod_table--auto");
        FlexTable stockTransferTable = new FlexTable();
        stockTransferTable.setStyleName("mod_table--auto");
        final FlexTable rentTable = new FlexTable();
        rentTable.setStyleName("mod_table--auto");
        //Product numbering table
        pTable.setWidget(0, 0, new InputGroup(pPrefix, pNumCell1, pNumCell2, pNumCell3, pNumCell4));
        //Product Category numbering table
        pcTable.setWidget(0, 0, new InputGroup(pcPrefix, pcNumCell1, pcNumCell2, pcNumCell3, pcNumCell4));
        //Fixed Asset numbering table
        faTable.setWidget(0, 0, new InputGroup(faPrefix, faNumCell1, faNumCell2, faNumCell3, faNumCell4));

        //rent order
        rentTable.setWidget(0, 0, new InputGroup(rentPrefix, rentNumCell1, rentNumCell2, rentNumCell3, rentNumCell4));

        //rfp numberingTable
        rfpTable.setWidget(0, 0, new InputGroup(rfpPrefix, rfpNumCell1, rfpNumCell2, rfpNumCell3, rfpNumCell4));

        //Expense numbering table
        exTable.setWidget(0, 0, new InputGroup(exPrefix, exNumCell1, exNumCell2, exNumCell3, exNumCell4));
        exTable.setWidget(0, 1, exDateNumbering);
        exTable.getFlexCellFormatter().addStyleName(0, 1, "pl-2 pr-2");
        exTable.setWidget(0, 2, exNumberExample);

        AccountingUtils.drawExampleNumber(exNumCell1,
                exNumCell2,
                exNumCell3,
                exNumCell4,
                exDateNumbering.getValue(),
                exNumberExample,
                dateFormat);

        // Manual Transaction numbering table
        mtTable.setWidget(0, 0, new InputGroup(mtPrefix, mtNumCell1, mtNumCell2, mtNumCell3, mtNumCell4));
        mtTable.setWidget(0, 1, mtDateNumbering);
        mtTable.getFlexCellFormatter().addStyleName(0, 1, "pl-2 pr-2");
        mtTable.setWidget(0, 2, mtNumberExample);

        AccountingUtils.drawExampleNumber(mtNumCell1,
                mtNumCell2,
                mtNumCell3,
                mtNumCell4,
                mtDateNumbering.getValue(),
                mtNumberExample,
                dateFormat);

        //Bank Payment numbering table
        bpTable.setWidget(0, 0, new InputGroup(bpPrefix, bpNumCell1, bpNumCell2, bpNumCell3, bpNumCell4));
        bpTable.setWidget(0, 1, bpDateNumbering);
        bpTable.getFlexCellFormatter().addStyleName(0, 1, "pl-2 pr-2");
        bpTable.setWidget(0, 2, bpNumberExample);

        AccountingUtils.drawExampleNumber(bpNumCell1,
                bpNumCell2,
                bpNumCell3,
                bpNumCell4,
                bpDateNumbering.getValue(),
                bpNumberExample,
                dateFormat);

        //Bank Receipt numbering table
        brTable.setWidget(0, 0, new InputGroup(brPrefix, brNumCell1, brNumCell2, brNumCell3, brNumCell4));
        brTable.setWidget(0, 1, brDateNumbering);
        brTable.getFlexCellFormatter().addStyleName(0, 1, "pl-2 pr-2");
        brTable.setWidget(0, 2, brNumberExample);

        AccountingUtils.drawExampleNumber(brNumCell1,
                brNumCell2,
                brNumCell3,
                brNumCell4,
                brDateNumbering.getValue(),
                brNumberExample,
                dateFormat);

        //Cash Payment numbering table
        cpTable.setWidget(0, 0, new InputGroup(cpPrefix, cpNumCell1, cpNumCell2, cpNumCell3, cpNumCell4));
        cpTable.setWidget(0, 1, cpDateNumbering);
        cpTable.getFlexCellFormatter().addStyleName(0, 1, "pl-2 pr-2");
        cpTable.setWidget(0, 2, cpNumberExample);

        AccountingUtils.drawExampleNumber(cpNumCell1,
                cpNumCell2,
                cpNumCell3,
                cpNumCell4,
                cpDateNumbering.getValue(),
                cpNumberExample,
                dateFormat);

        //Cash Receipt numbering table
        crTable.setWidget(0, 0, new InputGroup(crPrefix, crNumCell1, crNumCell2, crNumCell3, crNumCell4));
        crTable.setWidget(0, 1, crDateNumbering);
        crTable.getFlexCellFormatter().addStyleName(0, 1, "pl-2 pr-2");
        crTable.setWidget(0, 2, crNumberExample);

        AccountingUtils.drawExampleNumber(crNumCell1,
                crNumCell2,
                crNumCell3,
                crNumCell4,
                crDateNumbering.getValue(),
                crNumberExample,
                dateFormat);

        //Prepayment numbering table
        prepaymentTable.setWidget(0, 0, new InputGroup(prPrefix, prNumCell1, prNumCell2, prNumCell3, prNumCell4));
        prepaymentTable.setWidget(0, 1, prDateNumbering);
        prepaymentTable.getFlexCellFormatter().addStyleName(0, 1, "pl-2 pr-2");
        prepaymentTable.setWidget(0, 2, prNumberExample);

        AccountingUtils.drawExampleNumber(prNumCell1,
                prNumCell2,
                prNumCell3,
                prNumCell4,
                prDateNumbering.getValue(),
                prNumberExample,
                dateFormat);


        //customer Refund numbering table
        customerRefundTable.setWidget(0, 0, new InputGroup(crfPrefix, crfNumCell1, crfNumCell2, crfNumCell3, crfNumCell4));
        customerRefundTable.setWidget(0, 1, crfDateNumbering);
        customerRefundTable.getFlexCellFormatter().addStyleName(0, 1, "pl-2 pr-2");
        customerRefundTable.setWidget(0, 2, crfNumberExample);

        AccountingUtils.drawExampleNumber(crfNumCell1,
                crfNumCell2,
                crfNumCell3,
                crfNumCell4,
                crfDateNumbering.getValue(),
                crfNumberExample,
                dateFormat);

        //supplier Refund numbering table
        supplierRefundTable.setWidget(0, 0, new InputGroup(srfPrefix, srfNumCell1, srfNumCell2, srfNumCell3, srfNumCell4));
        supplierRefundTable.setWidget(0, 1, srfDateNumbering);
        supplierRefundTable.getFlexCellFormatter().addStyleName(0, 1, "pl-2 pr-2");
        supplierRefundTable.setWidget(0, 2, srfNumberExample);

        AccountingUtils.drawExampleNumber(srfNumCell1,
                srfNumCell2,
                srfNumCell3,
                srfNumCell4,
                srfDateNumbering.getValue(),
                srfNumberExample,
                dateFormat);

        //Supplier credit numbering table
        supplierCreditTable.setWidget(0, 0, new InputGroup(scPrefix, scNumCell1, scNumCell2, scNumCell3, scNumCell4));
        supplierCreditTable.setWidget(0, 1, scDateNumbering);
        supplierCreditTable.getFlexCellFormatter().addStyleName(0, 1, "pl-2 pr-2");
        supplierCreditTable.setWidget(0, 2, scNumberExample);

        AccountingUtils.drawExampleNumber(scNumCell1,
                scNumCell2,
                scNumCell3,
                scNumCell4,
                scDateNumbering.getValue(),
                scNumberExample,
                dateFormat);

        //Stock adjustment numbering table
        stockAdjustmentTable.setWidget(0, 0, new InputGroup(saPrefix, saNumCell1, saNumCell2, saNumCell3, saNumCell4));
        stockAdjustmentTable.setWidget(0, 1, saDateNumbering);
        stockAdjustmentTable.getFlexCellFormatter().addStyleName(0, 1, "pl-2 pr-2");
        stockAdjustmentTable.setWidget(0, 2, saNumberExample);

        AccountingUtils.drawExampleNumber(saNumCell1,
                saNumCell2,
                saNumCell3,
                saNumCell4,
                saDateNumbering.getValue(),
                saNumberExample,
                dateFormat);

        //Stock Transfer numbering table
        stockTransferTable.setWidget(0, 0, new InputGroup(stPrefix, stNumCell1, stNumCell2, stNumCell3, stNumCell4));
        stockTransferTable.setWidget(0, 1, stDateNumbering);
        stockTransferTable.getFlexCellFormatter().addStyleName(0, 1, "pl-2 pr-2");
        stockTransferTable.setWidget(0, 2, stNumberExample);

        AccountingUtils.drawExampleNumber(stNumCell1,
                stNumCell2,
                stNumCell3,
                stNumCell4,
                stDateNumbering.getValue(),
                stNumberExample,
                dateFormat);

        // PO GRN numbering table
        grnTable.setWidget(0, 0, new InputGroup(grnPrefix, grnNumCell1, grnNumCell2, grnNumCell3, grnNumCell4));
        grnTable.setWidget(0, 1, grnDateNumbering);
        grnTable.getFlexCellFormatter().addStyleName(0, 1, "pl-2 pr-2");
        grnTable.setWidget(0, 2, grnNumberExample);
        AccountingUtils.drawExampleNumber(grnNumCell1,
                grnNumCell2,
                grnNumCell3,
                grnNumCell4,
                grnDateNumbering.getValue(),
                grnNumberExample,
                dateFormat);

        //SO GDN numbering table
        gdnTable.setWidget(0, 0, new InputGroup(gdnPrefix, gdnNumCell1, gdnNumCell2, gdnNumCell3, gdnNumCell4));
        gdnTable.setWidget(0, 1, gdnDateNumbering);
        gdnTable.getFlexCellFormatter().addStyleName(0, 1, "pl-2 pr-2");
        gdnTable.setWidget(0, 2, gdnNumberExample);

        AccountingUtils.drawExampleNumber(gdnNumCell1,
                gdnNumCell2,
                gdnNumCell3,
                gdnNumCell4,
                gdnDateNumbering.getValue(),
                gdnNumberExample,
                dateFormat);

        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_RP_AND_PI_NUMBERING_SETTINGS)) {
            //Receive Payment numbering table
            rpTable.setWidget(0, 0, new InputGroup(rpPrefix, rpNumCell1, rpNumCell2, rpNumCell3, rpNumCell4));
            rpTable.setWidget(0, 1, rpDateNumbering);
            rpTable.getFlexCellFormatter().addStyleName(0, 1, "pl-2 pr-2");
            rpTable.setWidget(0, 2, rpNumberExample);

            AccountingUtils.drawExampleNumber(rpNumCell1,
                    rpNumCell2,
                    rpNumCell3,
                    rpNumCell4,
                    rpDateNumbering.getValue(),
                    rpNumberExample,
                    dateFormat);
            recievePaymentNumTable = new FormGroup(accountingStrings.receivePayment(), rpTable);

            //Pay Bill
            pbTable.addStyleName("mod_table--cellpadding");
            pbTable.setWidget(0, 0, new InputGroup(pbPrefix, pbNumCell1, pbNumCell2, pbNumCell3, pbNumCell4));
            pbTable.setWidget(0, 1, pbDateNumbering);
            pbTable.getFlexCellFormatter().addStyleName(0, 1, "pl-2 pr-2");
            pbTable.setWidget(0, 2, pbNumberExample);

            AccountingUtils.drawExampleNumber(pbNumCell1,
                    pbNumCell2,
                    pbNumCell3,
                    pbNumCell4,
                    pbDateNumbering.getValue(),
                    pbNumberExample,
                    dateFormat);
            payBillNumTable = new FormGroup(Property.get(Constants.PAYBILLS_LIST, accountingStrings.payBill()), pbTable);
        }

        this.addTitleField(NUMBERING_SETTINGS, wfmStrings.numberingSettings());
        this.addField(PRODUCT_NUMBERING, pTable, wfmStrings.product());
        this.addField(RENTAL_ORDER_NUMBERING, rentTable, accountingStrings.rentalOrder());
        this.addField(PRODUCT_CATEGORY__NUMBERING, pcTable, wfmStrings.productCategory());
        this.addField(FIXED_ASSET_NUMBERING, faTable, wfmStrings.fixedAsset());
        this.addField(EXPENSE_CLAIM_NUMBERING, exTable, wfmStrings.expenseClaims());
        this.addField(MANUAL_TRANSACTION_NUMBERING, mtTable, wfmStrings.manualTransaction());
        this.addField(PO_GRN_NUMBERING, grnTable, accountingStrings.grnNumber());
        this.addField(SO_GDN_NUMBERING, gdnTable, accountingStrings.gdnNumber());
        this.addField(BANK_PAYMENT_NUMBERING, bpTable, accountingStrings.bankPayments());
        this.addField(BANK_RECEIPT_NUMBERING, brTable, accountingStrings.bankReceipts());
        this.addField(CASH_PAYMENT_NUMBERING, cpTable, wfmStrings.cashPayment());
        this.addField(CASH_RECEIPT_NUMBERING, crTable, wfmStrings.cashReceipt());
        this.addField(PREPAYMENT_NUMBERING, prepaymentTable, wfmStrings.prepayments());
        this.addField(SUPPLIER_CREDIT_NUMBERING, supplierCreditTable, wfmStrings.supplierCredits());
        this.addField(CUSTOMER_REFUND_NUMBERING, customerRefundTable, accountingStrings.customerRefund());
        this.addField(SUPPLIER_REFUND_NUMBERING, supplierRefundTable, accountingStrings.supplierRefund());
        this.addField(STOCK_ADJUSTMENT_NUMBERING, stockAdjustmentTable, accountingStrings.stockAdjustment());
        this.addField(STOCK_TRANSFER_NUMBERING, stockTransferTable, accountingStrings.stockTransfer());
        this.addField(RFP_NUMBERING, rfpTable, wfmStrings.requestForPurchase());

        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_RP_AND_PI_NUMBERING_SETTINGS)) {
            this.addField(RECEIVE_PAYMENT_NUMBERING, recievePaymentNumTable);
            this.addField(PAY_BILL_NUMBERING, payBillNumTable);
        }
        this.drawInvoiceNumberingForm();
        this.drawCreditNoteNumberingForm();
        this.drawDebitNoteNumberingForm();
        this.show();
    }

    private void createProductCategoryCell() {
        pcPrefix = this.createPrefCell();
        pcPrefix.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "pcPrefix");

        pcNumCell1 = this.createNumCell(0);
        pcNumCell1.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "pcNumCell1");

        pcNumCell2 = this.createNumCell(0);
        pcNumCell2.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "pcNumCell2");

        pcNumCell3 = this.createNumCell(0);
        pcNumCell3.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "pcNumCell3");

        pcNumCell4 = this.createNumCell(1);
        pcNumCell4.ensureDebugId(PRODUCT_NUMBERING_SETTINGS_VIEW + "pcNumCell4");
    }

    private void save() {

        if (settings == null) {
            settings = new PMNumberingSettings();
        }
        settings.setProductNumberingFormat(this.getProductNumberingFormat());
        settings.setRentalOrderNumberingFormat(this.getRentalOrderNumberingFormat());
        settings.setProductCategoryNumberingFormat(this.getProductCategoryNumberingFormat());
        settings.setFixedAssetNumberingFormat(this.getFixedAssetNumberingFormat());
        settings.setRfpNumberingFormat(this.getRFPNumberingFormat());
        //settings.setExpenseNumberingFormat(this.getExpenseNumberingFormat());
        settings.setExpenseNumberingFormat(AccountingUtils.getNumberingFormat(exPrefix, exNumCell1, exNumCell2, exNumCell3, exNumCell4, exDateNumbering.getValue()));
        settings.setMtNumberingFormat(AccountingUtils.getNumberingFormat(mtPrefix, mtNumCell1, mtNumCell2, mtNumCell3, mtNumCell4, mtDateNumbering.getValue()));
        settings.setGrnNumberFormat(AccountingUtils.getNumberingFormat(grnPrefix, grnNumCell1, grnNumCell2, grnNumCell3, grnNumCell4, grnDateNumbering.getValue()));
        settings.setGdnNumberFormat(AccountingUtils.getNumberingFormat(gdnPrefix, gdnNumCell1, gdnNumCell2, gdnNumCell3, gdnNumCell4, gdnDateNumbering.getValue()));
        settings.setBpNumberingFormat(AccountingUtils.getNumberingFormat(bpPrefix, bpNumCell1, bpNumCell2, bpNumCell3, bpNumCell4, bpDateNumbering.getValue()));
        settings.setBrNumberingFormat(AccountingUtils.getNumberingFormat(brPrefix, brNumCell1, brNumCell2, brNumCell3, brNumCell4, brDateNumbering.getValue()));
        settings.setCpNumberingFormat(AccountingUtils.getNumberingFormat(cpPrefix, cpNumCell1, cpNumCell2, cpNumCell3, cpNumCell4, cpDateNumbering.getValue()));
        settings.setCrNumberingFormat(AccountingUtils.getNumberingFormat(crPrefix, crNumCell1, crNumCell2, crNumCell3, crNumCell4, crDateNumbering.getValue()));
        settings.setPrNumberingFormat(AccountingUtils.getNumberingFormat(prPrefix, prNumCell1, prNumCell2, prNumCell3, prNumCell4, prDateNumbering.getValue()));
        settings.setScNumberingFormat(AccountingUtils.getNumberingFormat(scPrefix, scNumCell1, scNumCell2, scNumCell3, scNumCell4, scDateNumbering.getValue()));
        settings.setCrfNumberingFormat(AccountingUtils.getNumberingFormat(crfPrefix, crfNumCell1, crfNumCell2, crfNumCell3, crfNumCell4, crfDateNumbering.getValue()));
        settings.setSrfNumberingFormat(AccountingUtils.getNumberingFormat(srfPrefix, srfNumCell1, srfNumCell2, srfNumCell3, srfNumCell4, srfDateNumbering.getValue()));
        settings.setSaNumberingFormat(AccountingUtils.getNumberingFormat(saPrefix, saNumCell1, saNumCell2, saNumCell3, saNumCell4, saDateNumbering.getValue()));
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_RP_AND_PI_NUMBERING_SETTINGS)) {
            settings.setRpNumberingFormat(AccountingUtils.getNumberingFormat(rpPrefix, rpNumCell1, rpNumCell2, rpNumCell3, rpNumCell4, rpDateNumbering.getValue()));
            settings.setPbNumberingFormat(AccountingUtils.getNumberingFormat(pbPrefix, pbNumCell1, pbNumCell2, pbNumCell3, pbNumCell4, pbDateNumbering.getValue()));
        }
        settings.setInvoiceNumberingFormat(getInvoiceNumberingFormat());
        settings.setSalesQuoteNumberingFormat(getSalesQuoteNumberingFormat());
        settings.setSalesOrderNumberingFormat(getSalesOrderNumberingFormat());
        settings.setPurchaseOrderNumberingFormat(getPurchaseOrderNumberingFormat());
        settings.setCnNumberingFormat(getCreditNoteNumberingFormat());
        settings.setDnNumberingFormat(getDebitNoteNumberingFormat());
        settings.setPiNumberingFormat(getPurchaseInvoiceNumberingFormat());
        settings.setInvoiceCreditNoteNumberingFormat(getInvoiceCreditNoteNumberingFormat());

        if (enableRestartNumbering.getValue()) {
            settings.setNumberingRestartEnabled(true);
            settings.setNumberingRestartDate(financialStartDate.getSelectedId());
            settings.setNumberingRestartMonth(financialStartMonth.getSelectedId());
        } else {
            settings.setNumberingRestartEnabled(false);
            settings.setNumberingRestartDate(null);
            settings.setNumberingRestartMonth(null);
        }

        LoadingPanel.loading(true);
        ProfileService.App.get().savePMNumberingSettings(settings, Constants.PRODUCT_NUMBERING_SETTINGS_FORM, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                throwable.printStackTrace();
            }

            @Override
            public void success(Integer id) {
                LoadingPanel.loading(false);
                settings.setObjectID(id);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.numberingSettings()), Info.Type.INFO);
            }
        });
    }

    private void parseAndSetData(PMNumberingSettings numberingSettings) {
        String numFormat;
        if (numberingSettings != null) {
            if (!Utils.isNullOrEmpty(numberingSettings.getProductNumberingFormat())) {
                numFormat = numberingSettings.getProductNumberingFormat();
                int splitterIndex = numFormat.lastIndexOf("_");

                pPrefix.setText(numFormat.substring(0, splitterIndex));
                char[] numbers = numFormat.substring(splitterIndex + 1).toCharArray();
                pNumCell1.setText(String.valueOf(numbers[0]));
                pNumCell2.setText(String.valueOf(numbers[1]));
                pNumCell3.setText(String.valueOf(numbers[2]));
                pNumCell4.setText(String.valueOf(numbers[3]));
            } else {
                pPrefix.setText("PD");
                pNumCell1.setText("0");
                pNumCell2.setText("0");
                pNumCell3.setText("0");
                pNumCell4.setText("1");
            }
            setProductCategoryNumberToTable(numberingSettings.getProductCategoryNumberingFormat());

            if (!Utils.isNullOrEmpty(numberingSettings.getRentalOrderNumberingFormat())) {
                numFormat = numberingSettings.getRentalOrderNumberingFormat();
                int splitterIndex = numFormat.lastIndexOf("_");

                rentPrefix.setText(numFormat.substring(0, splitterIndex));
                char[] numbers = numFormat.substring(splitterIndex + 1).toCharArray();
                rentNumCell1.setText(String.valueOf(numbers[0]));
                rentNumCell2.setText(String.valueOf(numbers[1]));
                rentNumCell3.setText(String.valueOf(numbers[2]));
                rentNumCell4.setText(String.valueOf(numbers[3]));
            } else {
                rentPrefix.setText("BOOKING");
                rentNumCell1.setText("0");
                rentNumCell2.setText("0");
                rentNumCell3.setText("0");
                rentNumCell4.setText("1");
            }
            if (!Utils.isNullOrEmpty(numberingSettings.getFixedAssetNumberingFormat())) {
                numFormat = numberingSettings.getFixedAssetNumberingFormat();
                int splitterIndex = numFormat.lastIndexOf("_");
                faPrefix.setText(numFormat.substring(0, splitterIndex));
                char[] numbers = numFormat.substring(splitterIndex + 1).toCharArray();
                faNumCell1.setText(String.valueOf(numbers[0]));
                faNumCell2.setText(String.valueOf(numbers[1]));
                faNumCell3.setText(String.valueOf(numbers[2]));
                faNumCell4.setText(String.valueOf(numbers[3]));
            } else {
                faPrefix.setText("FA");
                faNumCell1.setText("0");
                faNumCell2.setText("0");
                faNumCell3.setText("0");
                faNumCell4.setText("1");
            }

            if (!Utils.isNullOrEmpty(numberingSettings.getRfpNumberingFormat())) {
                numFormat = numberingSettings.getRfpNumberingFormat();
                int splitterIndex = numFormat.lastIndexOf("_");
                rfpPrefix.setText(numFormat.substring(0, splitterIndex));
                char[] numbers = numFormat.substring(splitterIndex + 1).toCharArray();
                rfpNumCell1.setText(String.valueOf(numbers[0]));
                rfpNumCell2.setText(String.valueOf(numbers[1]));
                rfpNumCell3.setText(String.valueOf(numbers[2]));
                rfpNumCell4.setText(String.valueOf(numbers[3]));
            } else {
                rfpPrefix.setText("RFP");
                rfpNumCell1.setText("0");
                rfpNumCell2.setText("0");
                rfpNumCell3.setText("0");
                rfpNumCell4.setText("1");
            }

            if (!Utils.isNullOrEmpty(numberingSettings.getExpenseNumberingFormat())) {
                numFormat = numberingSettings.getExpenseNumberingFormat(); //EX_0001 or EX_0001-date
                String[] numFormatParts = numFormat.split(SPLITTER);
                String[] numFormatDateParts = numFormatParts[1].split("-");
                String fourDigitNum;
                exPrefix.setText(numFormatParts[0]);
                if (numFormat.contains(DATE)) {
                    fourDigitNum = numFormatDateParts[0];
                    exDateNumbering.setValue(true);
                } else {
                    fourDigitNum = numFormatParts[1];
                }
                char[] numbers = fourDigitNum.toCharArray();
                exNumCell1.setText(String.valueOf(numbers[0]));
                exNumCell2.setText(String.valueOf(numbers[1]));
                exNumCell3.setText(String.valueOf(numbers[2]));
                exNumCell4.setText(String.valueOf(numbers[3]));
                AccountingUtils.drawExampleNumber(exNumCell1, exNumCell2, exNumCell3, exNumCell4, exDateNumbering.getValue(), exNumberExample, dateFormat);
            } else {
                exPrefix.setText("EX");
                exNumCell1.setText("0");
                exNumCell2.setText("0");
                exNumCell3.setText("0");
                exNumCell4.setText("1");
            }

            if (!Utils.isNullOrEmpty(numberingSettings.getMtNumberingFormat())) {
                numFormat = numberingSettings.getMtNumberingFormat(); //MT_0001 or MT_0001-date
                String[] numFormatParts = numFormat.split(SPLITTER);
                String[] numFormatDateParts = numFormatParts[1].split("-");
                String fourDigitNum = "";
                mtPrefix.setText(numFormatParts[0]);
                if (numFormat.contains(DATE)) {
                    fourDigitNum = numFormatDateParts[0];
                    mtDateNumbering.setValue(true);
                } else {
                    fourDigitNum = numFormatParts[1];
                }
                char[] numbers = fourDigitNum.toCharArray();
                mtNumCell1.setText(String.valueOf(numbers[0]));
                mtNumCell2.setText(String.valueOf(numbers[1]));
                mtNumCell3.setText(String.valueOf(numbers[2]));
                mtNumCell4.setText(String.valueOf(numbers[3]));
                AccountingUtils.drawExampleNumber(mtNumCell1, mtNumCell2, mtNumCell3, mtNumCell4, mtDateNumbering.getValue(), mtNumberExample, dateFormat);

            } else {
                mtPrefix.setText("MT");
                mtNumCell1.setText("0");
                mtNumCell2.setText("0");
                mtNumCell3.setText("0");
                mtNumCell4.setText("1");
            }
            if (!Utils.isNullOrEmpty(numberingSettings.getGrnNumberFormat())) {
                final String grnNumFormat = numberingSettings.getGrnNumberFormat();
                final String[] numFormatParts = grnNumFormat.split(SPLITTER);
                final String[] numFormatDateParts = numFormatParts[1].split("-");
                String fourDigitNum = "";

                grnPrefix.setText(numFormatParts[0]);
                if (grnNumFormat.contains(DATE)) {
                    fourDigitNum = numFormatDateParts[0];
                    grnDateNumbering.setValue(true);
                } else {
                    fourDigitNum = numFormatParts[1];
                }
                char[] numbers = fourDigitNum.toCharArray();
                grnNumCell1.setText(String.valueOf(numbers[0]));
                grnNumCell2.setText(String.valueOf(numbers[1]));
                grnNumCell3.setText(String.valueOf(numbers[2]));
                grnNumCell4.setText(String.valueOf(numbers[3]));
                AccountingUtils.drawExampleNumber(grnNumCell1, grnNumCell2, grnNumCell3, grnNumCell4, grnDateNumbering.getValue(), grnNumberExample, dateFormat);
            } else {
                grnPrefix.setText("GRN");
                grnNumCell1.setText("0");
                grnNumCell2.setText("0");
                grnNumCell3.setText("0");
                grnNumCell4.setText("1");
            }
            if (!Utils.isNullOrEmpty(numberingSettings.getGdnNumberFormat())) {
                final String gdnNumFormat = numberingSettings.getGdnNumberFormat();
                final String[] numFormatParts = gdnNumFormat.split(SPLITTER);
                final String[] numFormatDateParts = numFormatParts[1].split("-");
                String fourDigitNum = "";

                gdnPrefix.setText(numFormatParts[0]);
                if (gdnNumFormat.contains(DATE)) {
                    fourDigitNum = numFormatDateParts[0];
                    gdnDateNumbering.setValue(true);
                } else {
                    fourDigitNum = numFormatParts[1];
                }
                final char[] numbers = fourDigitNum.toCharArray();

                gdnNumCell1.setText(String.valueOf(numbers[0]));
                gdnNumCell2.setText(String.valueOf(numbers[1]));
                gdnNumCell3.setText(String.valueOf(numbers[2]));
                gdnNumCell4.setText(String.valueOf(numbers[3]));
                AccountingUtils.drawExampleNumber(gdnNumCell1,
                        gdnNumCell2,
                        gdnNumCell3,
                        gdnNumCell4,
                        gdnDateNumbering.getValue(),
                        gdnNumberExample,
                        dateFormat);
            } else {
                gdnPrefix.setText("GDN");
                gdnNumCell1.setText("0");
                gdnNumCell2.setText("0");
                gdnNumCell3.setText("0");
                gdnNumCell4.setText("1");
            }
            if (!Utils.isNullOrEmpty(numberingSettings.getBpNumberingFormat())) {
                numFormat = numberingSettings.getBpNumberingFormat(); //BP_0001 or BP_0001-date
                String[] numFormatParts = numFormat.split(SPLITTER);
                String[] numFormatDateParts = numFormatParts[1].split("-");
                String fourDigitNum = "";
                bpPrefix.setText(numFormatParts[0]);
                if (numFormat.contains(DATE)) {
                    fourDigitNum = numFormatDateParts[0];
                    bpDateNumbering.setValue(true);
                } else {
                    fourDigitNum = numFormatParts[1];
                }
                char[] numbers = fourDigitNum.toCharArray();
                bpNumCell1.setText(String.valueOf(numbers[0]));
                bpNumCell2.setText(String.valueOf(numbers[1]));
                bpNumCell3.setText(String.valueOf(numbers[2]));
                bpNumCell4.setText(String.valueOf(numbers[3]));
                AccountingUtils.drawExampleNumber(bpNumCell1, bpNumCell2, bpNumCell3, bpNumCell4, bpDateNumbering.getValue(), bpNumberExample, dateFormat);
            } else {
                bpPrefix.setText("SPM");
                bpNumCell1.setText("0");
                bpNumCell2.setText("0");
                bpNumCell3.setText("0");
                bpNumCell4.setText("1");
            }

            if (!Utils.isNullOrEmpty(numberingSettings.getBrNumberingFormat())) {
                numFormat = numberingSettings.getBrNumberingFormat();
                String[] numFormatParts = numFormat.split(SPLITTER);
                String[] numFormatDateParts = numFormatParts[1].split("-");
                String fourDigitNum = "";
                brPrefix.setText(numFormatParts[0]);
                if (numFormat.contains(DATE)) {
                    fourDigitNum = numFormatDateParts[0];
                    brDateNumbering.setValue(true);
                } else {
                    fourDigitNum = numFormatParts[1];
                }
                char[] numbers = fourDigitNum.toCharArray();
                brNumCell1.setText(String.valueOf(numbers[0]));
                brNumCell2.setText(String.valueOf(numbers[1]));
                brNumCell3.setText(String.valueOf(numbers[2]));
                brNumCell4.setText(String.valueOf(numbers[3]));
                AccountingUtils.drawExampleNumber(brNumCell1, brNumCell2, brNumCell3, brNumCell4, brDateNumbering.getValue(), brNumberExample, dateFormat);
            } else {
                brPrefix.setText("REM");
                brNumCell1.setText("0");
                brNumCell2.setText("0");
                brNumCell3.setText("0");
                brNumCell4.setText("1");
            }

            if (!Utils.isNullOrEmpty(numberingSettings.getCrNumberingFormat())) {
                numFormat = numberingSettings.getCrNumberingFormat();
                String[] numFormatParts = numFormat.split(SPLITTER);
                String[] numFormatDateParts = numFormatParts[1].split("-");
                String fourDigitNum = "";
                crPrefix.setText(numFormatParts[0]);
                if (numFormat.contains(DATE)) {
                    fourDigitNum = numFormatDateParts[0];
                    crDateNumbering.setValue(true);
                } else {
                    fourDigitNum = numFormatParts[1];
                }
                char[] numbers = fourDigitNum.toCharArray();
                crNumCell1.setText(String.valueOf(numbers[0]));
                crNumCell2.setText(String.valueOf(numbers[1]));
                crNumCell3.setText(String.valueOf(numbers[2]));
                crNumCell4.setText(String.valueOf(numbers[3]));
                AccountingUtils.drawExampleNumber(crNumCell1, crNumCell2, crNumCell3, crNumCell4, crDateNumbering.getValue(), crNumberExample, dateFormat);
            } else {
                crPrefix.setText("CR");
                crNumCell1.setText("0");
                crNumCell2.setText("0");
                crNumCell3.setText("0");
                crNumCell4.setText("1");
            }

            this.parseAndSetPrepaymentNumberingData(numberingSettings);
            this.parseAndSetSupplierCreditNumberingData(numberingSettings);
            this.parseAndSetCustomerRefundNumberingData(numberingSettings);
            this.parseAndSetSupplierRefundNumberingData(numberingSettings);
            this.parseAndSetStockAdjustmentNumberingData(numberingSettings);
            parseAndSetStockTransferNumberingData(numberingSettings);
            if (!Utils.isNullOrEmpty(numberingSettings.getCpNumberingFormat())) {
                numFormat = numberingSettings.getCpNumberingFormat();
                final String[] numFormatParts = numFormat.split(SPLITTER);
                final String[] numFormatDateParts = numFormatParts[1].split("-");
                String fourDigitNum;

                cpPrefix.setText(numFormatParts[0]);
                if (numFormat.contains(DATE)) {
                    fourDigitNum = numFormatDateParts[0];
                    cpDateNumbering.setValue(true);
                } else {
                    fourDigitNum = numFormatParts[1];
                }
                char[] numbers = fourDigitNum.toCharArray();
                cpNumCell1.setText(String.valueOf(numbers[0]));
                cpNumCell2.setText(String.valueOf(numbers[1]));
                cpNumCell3.setText(String.valueOf(numbers[2]));
                cpNumCell4.setText(String.valueOf(numbers[3]));
                AccountingUtils.drawExampleNumber(cpNumCell1, cpNumCell2, cpNumCell3, cpNumCell4, cpDateNumbering.getValue(), cpNumberExample, dateFormat);
            } else {
                cpPrefix.setText("CP");
                cpNumCell1.setText("0");
                cpNumCell2.setText("0");
                cpNumCell3.setText("0");
                cpNumCell4.setText("1");
            }
            if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_RP_AND_PI_NUMBERING_SETTINGS)) {
                if (!Utils.isNullOrEmpty(numberingSettings.getRpNumberingFormat())) {
                    numFormat = numberingSettings.getRpNumberingFormat();
                    String[] numFormatParts = numFormat.split(SPLITTER);
                    String[] numFormatDateParts = numFormatParts[1].split("-");
                    String fourDigitNum = "";
                    rpPrefix.setText(numFormatParts[0]);
                    if (numFormat.contains(DATE)) {
                        fourDigitNum = numFormatDateParts[0];
                        rpDateNumbering.setValue(true);
                    } else {
                        fourDigitNum = numFormatParts[1];
                    }

                    char[] numbers = fourDigitNum.toCharArray();
                    rpNumCell1.setText(String.valueOf(numbers[0]));
                    rpNumCell2.setText(String.valueOf(numbers[1]));
                    rpNumCell3.setText(String.valueOf(numbers[2]));
                    rpNumCell4.setText(String.valueOf(numbers[3]));
                    AccountingUtils.drawExampleNumber(rpNumCell1, rpNumCell2, rpNumCell3, rpNumCell4, rpDateNumbering.getValue(), rpNumberExample, dateFormat);
                } else {
                    rpPrefix.setText("RP");
                    rpNumCell1.setText("0");
                    rpNumCell2.setText("0");
                    rpNumCell3.setText("0");
                    rpNumCell4.setText("1");
                }

                if (!Utils.isNullOrEmpty(numberingSettings.getPbNumberingFormat())) {
                    numFormat = numberingSettings.getPbNumberingFormat();
                    String[] numFormatParts = numFormat.split(SPLITTER);
                    String[] numFormatDateParts = numFormatParts[1].split("-");
                    String fourDigitNum = "";
                    pbPrefix.setText(numFormatParts[0]);
                    if (numFormat.contains(DATE)) {
                        fourDigitNum = numFormatDateParts[0];
                        pbDateNumbering.setValue(true);
                    } else {
                        fourDigitNum = numFormatParts[1];
                    }
                    char[] numbers = fourDigitNum.toCharArray();
                    pbNumCell1.setText(String.valueOf(numbers[0]));
                    pbNumCell2.setText(String.valueOf(numbers[1]));
                    pbNumCell3.setText(String.valueOf(numbers[2]));
                    pbNumCell4.setText(String.valueOf(numbers[3]));
                    AccountingUtils.drawExampleNumber(pbNumCell1, pbNumCell2, pbNumCell3, pbNumCell4, pbDateNumbering.getValue(), pbNumberExample, dateFormat);
                } else {
                    pbPrefix.setText("PB");
                    pbNumCell1.setText("0");
                    pbNumCell2.setText("0");
                    pbNumCell3.setText("0");
                    pbNumCell4.setText("1");
                }
            }
        } else {

            //Product Numbering
            pPrefix.setText("PD");
            pNumCell1.setText("0");
            pNumCell2.setText("0");
            pNumCell3.setText("0");
            pNumCell4.setText("1");

            //Rental Order Numbering
            rentPrefix.setText("BOOKING");
            rentNumCell1.setText("0");
            rentNumCell2.setText("0");
            rentNumCell3.setText("0");
            rentNumCell4.setText("1");

            //Product Category Numbering
            pcPrefix.setText("PD");
            pcNumCell1.setText("0");
            pcNumCell2.setText("0");
            pcNumCell3.setText("0");
            pcNumCell4.setText("1");

            //Fixed Asset Numbering
            faPrefix.setText("FA");
            faNumCell1.setText("0");
            faNumCell2.setText("0");
            faNumCell3.setText("0");
            faNumCell4.setText("1");

            //RFP Numbering
            rfpPrefix.setText("FA");
            rfpNumCell1.setText("0");
            rfpNumCell2.setText("0");
            rfpNumCell3.setText("0");
            rfpNumCell4.setText("1");

            //Expense Claims Numbering
            exPrefix.setText("EX");
            exNumCell1.setText("0");
            exNumCell2.setText("0");
            exNumCell3.setText("0");
            exNumCell4.setText("1");

            //Manual Transactions Numbering
            mtPrefix.setText("MT");
            mtNumCell1.setText("0");
            mtNumCell2.setText("0");
            mtNumCell3.setText("0");
            mtNumCell4.setText("1");

            //PO grn Number settings
            grnPrefix.setText("GRN");
            grnNumCell1.setText("0");
            grnNumCell2.setText("0");
            grnNumCell3.setText("0");
            grnNumCell4.setText("1");

            //gdn number setttings
            gdnPrefix.setText("GDN");
            gdnNumCell1.setText("0");
            gdnNumCell2.setText("0");
            grnNumCell3.setText("0");
            grnNumCell4.setText("1");

            //Bank Payment Numbering
            bpPrefix.setText("SPM");
            bpNumCell1.setText("0");
            bpNumCell2.setText("0");
            bpNumCell3.setText("0");
            bpNumCell4.setText("1");

            //Bank Receipt Numbering
            brPrefix.setText("REM");
            brNumCell1.setText("0");
            brNumCell2.setText("0");
            brNumCell3.setText("0");
            brNumCell4.setText("1");

            //Cash Payment Numbering
            cpPrefix.setText("CP");
            cpNumCell1.setText("0");
            cpNumCell2.setText("0");
            cpNumCell3.setText("0");
            cpNumCell4.setText("1");

            //Cash Receipt Numbering
            crPrefix.setText("CR");
            crNumCell1.setText("0");
            crNumCell2.setText("0");
            crNumCell3.setText("0");
            crNumCell4.setText("1");

            //Prepayment Numbering
            prPrefix.setText("PR");
            prNumCell1.setText("0");
            prNumCell2.setText("0");
            prNumCell3.setText("0");
            prNumCell4.setText("1");

            //SupplierCredit Numbering
            scPrefix.setText("SC");
            scNumCell1.setText("0");
            scNumCell2.setText("0");
            scNumCell3.setText("0");
            scNumCell4.setText("1");

            //StockAdjustment Numbering
            saPrefix.setText("SA");
            saNumCell1.setText("0");
            saNumCell2.setText("0");
            saNumCell3.setText("0");
            saNumCell4.setText("1");

            //StockTransferNumbering
            stPrefix.setText("ST");
            stNumCell1.setText("0");
            stNumCell2.setText("0");
            stNumCell3.setText("0");
            stNumCell4.setText("1");


        }
        if (settings.isNumberingRestartEnabled()) {
            enableRestartNumbering.setValue(true);
            financialStartDate.setEnabled(true);
            financialStartMonth.setEnabled(true);
            if (settings.getNumberingRestartDate() != null) {
                financialStartDate.setSelected(settings.getNumberingRestartDate());
            }
            if (settings.getNumberingRestartMonth() != null) {
                financialStartMonth.setSelected(settings.getNumberingRestartMonth());
            }
        }
        parseAndSetNumberingData(settings.getInvoiceNumberingFormat(), settings.getSalesQuoteNumberingFormat(), settings.getSalesOrderNumberingFormat(), settings.getPurchaseOrderNumberingFormat(), settings.getPiNumberingFormat(), settings.getCnNumberingFormat(), settings.getDnNumberingFormat(), settings.getInvoiceCreditNoteNumberingFormat());
    }

    private void setProductCategoryNumberToTable(String pcNumberingFormat) {
        if (!Utils.isNullOrEmpty(pcNumberingFormat)) {
            int splitterIndex = pcNumberingFormat.lastIndexOf("_");

            pcPrefix.setText(pcNumberingFormat.substring(0, splitterIndex));
            char[] numbers = pcNumberingFormat.substring(splitterIndex + 1).toCharArray();
            pcNumCell1.setText(String.valueOf(numbers[0]));
            pcNumCell2.setText(String.valueOf(numbers[1]));
            pcNumCell3.setText(String.valueOf(numbers[2]));
            pcNumCell4.setText(String.valueOf(numbers[3]));
        } else {
            pcPrefix.setText("PC");
            pcNumCell1.setText("0");
            pcNumCell2.setText("0");
            pcNumCell3.setText("0");
            pcNumCell4.setText("1");
        }

    }

    private void parseAndSetPrepaymentNumberingData(PMNumberingSettings numberingSettings) {
        String numFormat;
        if (!Utils.isNullOrEmpty(numberingSettings.getPrNumberingFormat())) {
            numFormat = numberingSettings.getPrNumberingFormat();
            String[] numFormatParts = numFormat.split(SPLITTER);
            String[] numFormatDateParts = numFormatParts[1].split("-");
            String fourDigitNum = "";
            prPrefix.setText(numFormatParts[0]);
            if (numFormat.contains(DATE)) {
                fourDigitNum = numFormatDateParts[0];
                prDateNumbering.setValue(true);
            } else {
                fourDigitNum = numFormatParts[1];
            }
            char[] numbers = fourDigitNum.toCharArray();
            prNumCell1.setText(String.valueOf(numbers[0]));
            prNumCell2.setText(String.valueOf(numbers[1]));
            prNumCell3.setText(String.valueOf(numbers[2]));
            prNumCell4.setText(String.valueOf(numbers[3]));
            AccountingUtils.drawExampleNumber(prNumCell1, prNumCell2, prNumCell3, prNumCell4, prDateNumbering.getValue(), prNumberExample, dateFormat);
        } else {
            prPrefix.setText("PR");
            prNumCell1.setText("0");
            prNumCell2.setText("0");
            prNumCell3.setText("0");
            prNumCell4.setText("1");
        }
    }

    private void parseAndSetCustomerRefundNumberingData(PMNumberingSettings numberingSettings) {
        String numFormat;
        if (!Utils.isNullOrEmpty(numberingSettings.getCrfNumberingFormat())) {
            numFormat = numberingSettings.getCrfNumberingFormat();
            String[] numFormatParts = numFormat.split(SPLITTER);
            String[] numFormatDateParts = numFormatParts[1].split("-");
            String fourDigitNum = "";
            crfPrefix.setText(numFormatParts[0]);
            if (numFormat.contains(DATE)) {
                fourDigitNum = numFormatDateParts[0];
                crfDateNumbering.setValue(true);
            } else {
                fourDigitNum = numFormatParts[1];
            }
            char[] numbers = fourDigitNum.toCharArray();
            crfNumCell1.setText(String.valueOf(numbers[0]));
            crfNumCell2.setText(String.valueOf(numbers[1]));
            crfNumCell3.setText(String.valueOf(numbers[2]));
            crfNumCell4.setText(String.valueOf(numbers[3]));
            AccountingUtils.drawExampleNumber(crfNumCell1, crfNumCell2, crfNumCell3, crfNumCell4, crfDateNumbering.getValue(), crfNumberExample, dateFormat);
        } else {
            crfPrefix.setText("CRF");
            crfNumCell1.setText("0");
            crfNumCell2.setText("0");
            crfNumCell3.setText("0");
            crfNumCell4.setText("1");
        }
    }

    private void parseAndSetSupplierRefundNumberingData(PMNumberingSettings numberingSettings) {
        String numFormat;
        if (!Utils.isNullOrEmpty(numberingSettings.getSrfNumberingFormat())) {
            numFormat = numberingSettings.getSrfNumberingFormat();
            String[] numFormatParts = numFormat.split(SPLITTER);
            String[] numFormatDateParts = numFormatParts[1].split("-");
            String fourDigitNum = "";
            srfPrefix.setText(numFormatParts[0]);
            if (numFormat.contains(DATE)) {
                fourDigitNum = numFormatDateParts[0];
                srfDateNumbering.setValue(true);
            } else {
                fourDigitNum = numFormatParts[1];
            }
            char[] numbers = fourDigitNum.toCharArray();
            srfNumCell1.setText(String.valueOf(numbers[0]));
            srfNumCell2.setText(String.valueOf(numbers[1]));
            srfNumCell3.setText(String.valueOf(numbers[2]));
            srfNumCell4.setText(String.valueOf(numbers[3]));
            AccountingUtils.drawExampleNumber(srfNumCell1, srfNumCell2, srfNumCell3, srfNumCell4, srfDateNumbering.getValue(), srfNumberExample, dateFormat);
        } else {
            srfPrefix.setText("SRF");
            srfNumCell1.setText("0");
            srfNumCell2.setText("0");
            srfNumCell3.setText("0");
            srfNumCell4.setText("1");
        }
    }

    private void parseAndSetSupplierCreditNumberingData(PMNumberingSettings numberingSettings) {
        String numFormat;
        if (!Utils.isNullOrEmpty(numberingSettings.getScNumberingFormat())) {
            numFormat = numberingSettings.getScNumberingFormat();
            String[] numFormatParts = numFormat.split(SPLITTER);
            String[] numFormatDateParts = numFormatParts[1].split("-");
            String fourDigitNum = "";
            scPrefix.setText(numFormatParts[0]);
            if (numFormat.contains(DATE)) {
                fourDigitNum = numFormatDateParts[0];
                scDateNumbering.setValue(true);
            } else {
                fourDigitNum = numFormatParts[1];
            }
            char[] numbers = fourDigitNum.toCharArray();
            scNumCell1.setText(String.valueOf(numbers[0]));
            scNumCell2.setText(String.valueOf(numbers[1]));
            scNumCell3.setText(String.valueOf(numbers[2]));
            scNumCell4.setText(String.valueOf(numbers[3]));
            AccountingUtils.drawExampleNumber(scNumCell1, scNumCell2, scNumCell3, scNumCell4, scDateNumbering.getValue(), scNumberExample, dateFormat);
        } else {
            scPrefix.setText("SC");
            scNumCell1.setText("0");
            scNumCell2.setText("0");
            scNumCell3.setText("0");
            scNumCell4.setText("1");
        }
    }

    private void parseAndSetStockAdjustmentNumberingData(PMNumberingSettings numberingSettings) {
        String numFormat;
        if (!Utils.isNullOrEmpty(numberingSettings.getSaNumberingFormat())) {
            numFormat = numberingSettings.getSaNumberingFormat();
            String[] numFormatParts = numFormat.split(SPLITTER);
            String[] numFormatDateParts = numFormatParts[1].split("-");
            String fourDigitNum = "";
            saPrefix.setText(numFormatParts[0]);
            if (numFormat.contains(DATE)) {
                fourDigitNum = numFormatDateParts[0];
                saDateNumbering.setValue(true);
            } else {
                fourDigitNum = numFormatParts[1];
            }
            char[] numbers = fourDigitNum.toCharArray();
            saNumCell1.setText(String.valueOf(numbers[0]));
            saNumCell2.setText(String.valueOf(numbers[1]));
            saNumCell3.setText(String.valueOf(numbers[2]));
            saNumCell4.setText(String.valueOf(numbers[3]));
            AccountingUtils.drawExampleNumber(saNumCell1, saNumCell2, saNumCell3, saNumCell4, saDateNumbering.getValue(), saNumberExample, dateFormat);
        } else {
            saPrefix.setText("SA");
            saNumCell1.setText("0");
            saNumCell2.setText("0");
            saNumCell3.setText("0");
            saNumCell4.setText("1");
        }
    }

    private void parseAndSetStockTransferNumberingData(PMNumberingSettings numberingSettings) {
        String numFormat;
        if (!Utils.isNullOrEmpty(numberingSettings.getStNumberingFormat())) {
            numFormat = numberingSettings.getStNumberingFormat();
            String[] numFormatParts = numFormat.split(SPLITTER);
            String[] numFormatDateParts = numFormatParts[1].split("-");
            String fourDigitNum = "";
            stPrefix.setText(numFormatParts[0]);
            if (numFormat.contains(DATE)) {
                fourDigitNum = numFormatDateParts[0];
                stDateNumbering.setValue(true);
            } else {
                fourDigitNum = numFormatParts[1];
            }
            char[] numbers = fourDigitNum.toCharArray();
            stNumCell1.setText(String.valueOf(numbers[0]));
            stNumCell2.setText(String.valueOf(numbers[1]));
            stNumCell3.setText(String.valueOf(numbers[2]));
            stNumCell4.setText(String.valueOf(numbers[3]));
            AccountingUtils.drawExampleNumber(stNumCell1, stNumCell2, stNumCell3, stNumCell4, stDateNumbering.getValue(), stNumberExample, dateFormat);
        } else {
            stPrefix.setText("ST");
            stNumCell1.setText("0");
            stNumCell2.setText("0");
            stNumCell3.setText("0");
            stNumCell4.setText("1");
        }
    }

    private void parseAndSetCellValues(TextBox cell1, TextBox cell2, TextBox cell3, TextBox cell4, String codeArray) {
        char[] codes = codeArray.toCharArray();
        cell1.setText(String.valueOf(codes[0]));
        cell2.setText(String.valueOf(codes[1]));
        cell3.setText(String.valueOf(codes[2]));
        cell4.setText(String.valueOf(codes[3]));
    }


    private String getProductNumberingFormat() {

        return pPrefix.getText().trim() +
                "_" +
                (pNumCell1.getText().trim().length() > 0 ? pNumCell1.getText().trim() : "0") +
                (pNumCell2.getText().trim().length() > 0 ? pNumCell2.getText().trim() : "0") +
                (pNumCell3.getText().trim().length() > 0 ? pNumCell3.getText().trim() : "0") +
                (pNumCell4.getText().trim().length() > 0 ? pNumCell4.getText().trim() : "1");
    }

    private String getRentalOrderNumberingFormat() {

        return rentPrefix.getText().trim() +
                "_" +
                (rentNumCell1.getText().trim().length() > 0 ? pNumCell1.getText().trim() : "0") +
                (rentNumCell2.getText().trim().length() > 0 ? rentNumCell2.getText().trim() : "0") +
                (rentNumCell3.getText().trim().length() > 0 ? rentNumCell3.getText().trim() : "0") +
                (rentNumCell4.getText().trim().length() > 0 ? rentNumCell4.getText().trim() : "1");
    }

    private String getProductCategoryNumberingFormat() {
        return pcPrefix.getText().trim() +
                "_" +
                (pcNumCell1.getText().trim().length() > 0 ? pcNumCell1.getText().trim() : "0") +
                (pcNumCell2.getText().trim().length() > 0 ? pcNumCell2.getText().trim() : "0") +
                (pcNumCell3.getText().trim().length() > 0 ? pcNumCell3.getText().trim() : "0") +
                (pcNumCell4.getText().trim().length() > 0 ? pcNumCell4.getText().trim() : "1");
    }

    private String getFixedAssetNumberingFormat() {
        String buffer = faPrefix.getText().trim() +
                "_" +
                (faNumCell1.getText().trim().length() > 0 ? faNumCell1.getText().trim() : "0") +
                (faNumCell2.getText().trim().length() > 0 ? faNumCell2.getText().trim() : "0") +
                (faNumCell3.getText().trim().length() > 0 ? faNumCell3.getText().trim() : "0") +
                (faNumCell4.getText().trim().length() > 0 ? faNumCell4.getText().trim() : "1");
        return buffer;
    }

    private String getRFPNumberingFormat() {
        String buffer = rfpPrefix.getText().trim() +
                "_" +
                (rfpNumCell1.getText().trim().length() > 0 ? rfpNumCell1.getText().trim() : "0") +
                (rfpNumCell2.getText().trim().length() > 0 ? rfpNumCell2.getText().trim() : "0") +
                (rfpNumCell3.getText().trim().length() > 0 ? rfpNumCell3.getText().trim() : "0") +
                (rfpNumCell4.getText().trim().length() > 0 ? rfpNumCell4.getText().trim() : "1");
        return buffer;
    }


    private TextBox createPrefCell() {
        TextBox cell = new TextBox();
        cell.setWidth("100px");
        return cell;
    }

    private TextBox createNumCell(int number) {
        final TextBox cell = new TextBox();
        cell.setWidth("35px");
        cell.addStyleName("text-center");

        cell.setMaxLength(1);
        Validation.addNumericKeyboardListener(cell);
        cell.addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent focusEvent) {
                cell.setSelectionRange(0, cell.getValue().length());
            }
        });
        return cell;
    }

    private TextBox createNumCell(int number, final int type) {
        final TextBox cell = new TextBox();
        cell.setWidth("35px");
        cell.addStyleName("text-center");

        cell.setMaxLength(1);
        cell.setText(String.valueOf(number));
        Validation.addNumericKeyboardListener(cell);
        cell.addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent focusEvent) {
                cell.setSelectionRange(0, cell.getValue().length());
            }
        });
        cell.addKeyboardListener(new KeyboardListenerAdapter() {
            public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                if (type == BP) {
                    AccountingUtils.drawExampleNumber(bpNumCell1, bpNumCell2, bpNumCell3, bpNumCell4, bpDateNumbering.getValue(), bpNumberExample, dateFormat);
                } else if (type == BR) {
                    AccountingUtils.drawExampleNumber(brNumCell1, brNumCell2, brNumCell3, brNumCell4, brDateNumbering.getValue(), brNumberExample, dateFormat);
                } else if (type == CR) {
                    AccountingUtils.drawExampleNumber(crNumCell1, crNumCell2, crNumCell3, crNumCell4, crDateNumbering.getValue(), crNumberExample, dateFormat);
                } else if (type == CP) {
                    AccountingUtils.drawExampleNumber(cpNumCell1, cpNumCell2, cpNumCell3, cpNumCell4, cpDateNumbering.getValue(), cpNumberExample, dateFormat);
                } else if (type == EX) {
                    AccountingUtils.drawExampleNumber(exNumCell1, exNumCell2, exNumCell3, exNumCell4, exDateNumbering.getValue(), exNumberExample, dateFormat);
                } else if (type == MT) {
                    AccountingUtils.drawExampleNumber(mtNumCell1, mtNumCell2, mtNumCell3, mtNumCell4, mtDateNumbering.getValue(), mtNumberExample, dateFormat);
                } else if (type == GRN) {
                    AccountingUtils.drawExampleNumber(grnNumCell1, grnNumCell2, grnNumCell3, grnNumCell4, grnDateNumbering.getValue(), grnNumberExample, dateFormat);
                } else if (type == PR) {
                    AccountingUtils.drawExampleNumber(prNumCell1, prNumCell2, prNumCell3, prNumCell4, prDateNumbering.getValue(), prNumberExample, dateFormat);
                } else if (type == SC) {
                    AccountingUtils.drawExampleNumber(scNumCell1, scNumCell2, scNumCell3, scNumCell4, scDateNumbering.getValue(), scNumberExample, dateFormat);
                } else if (type == SA) {
                    AccountingUtils.drawExampleNumber(saNumCell1, saNumCell2, saNumCell3, saNumCell4, saDateNumbering.getValue(), saNumberExample, dateFormat);
                } else if (type == ST) {
                    AccountingUtils.drawExampleNumber(stNumCell1, stNumCell2, stNumCell3, stNumCell4, stDateNumbering.getValue(), stNumberExample, dateFormat);
                } else if (type == RP && Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_RP_AND_PI_NUMBERING_SETTINGS)) {
                    AccountingUtils.drawExampleNumber(rpNumCell1, rpNumCell2, rpNumCell3, rpNumCell4, rpDateNumbering.getValue(), rpNumberExample, dateFormat);
                } else if (type == PB && Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_RP_AND_PI_NUMBERING_SETTINGS)) {
                    AccountingUtils.drawExampleNumber(pbNumCell1, pbNumCell2, pbNumCell3, pbNumCell4, pbDateNumbering.getValue(), pbNumberExample, dateFormat);
                } else if (type == CR) {
                    drawCreditNoteExampleNumber();
                } else if (type == GRN) {
                    AccountingUtils.drawExampleNumber(grnNumCell1, grnNumCell2, grnNumCell3, grnNumCell4, grnDateNumbering.getValue(), grnNumberExample, dateFormat);
                } else if (type == GDN) {
                    AccountingUtils.drawExampleNumber(gdnNumCell1, gdnNumCell2, gdnNumCell3, gdnNumCell4, gdnDateNumbering.getValue(), gdnNumberExample, dateFormat);
                } else if (type == INV) {
                    drawInvoiceExampleNumber();
                } else if (type == SQ) {
                    drawQuoteExampleNumber();
                } else if (type == SO) {
                    drawSalesOrderExampleNumber();
                } else if (type == PO) {
                    drawOrderExampleNumber();
                } else if (type == CN) {
                    drawCreditNoteExampleNumber();
                } else if (type == PI) {
                    drawPInvoiceExampleNumber();
                }
            }
        });
        cell.addFocusListener(new FocusListenerAdapter() {
            public void onLostFocus(Widget sender) {
                if (cell.getText().equals("")) {
                    cell.setText("0");
                }
            }
        });
        return cell;
    }

    private void drawCreditNoteNumberingForm() {
        FlexTable creditNoteNumberFormat = new FlexTable();
        creditNoteNumberFormat.setStyleName("mod_table--auto");

        crnoDateNumbering = new KpiCheckBox(wfmStrings.date());
        crnoDateNumbering.ensureDebugId(CREDIT_NOTE_SETTINGS + "dateNumbering");

        crnoClientNumbering = new KpiCheckBox(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
        crnoClientNumbering.ensureDebugId(CREDIT_NOTE_SETTINGS + "clientNumbering");
        crnoProjectNumbering = new KpiCheckBox(Property.get(Constants.PROJECT, wfmStrings.project()));
        crnoProjectNumbering.ensureDebugId(CREDIT_NOTE_SETTINGS + "projectNumbering");

        crnoDateNumbering.addClickHandler(sender -> drawCreditNoteExampleNumber());
        crnoClientNumbering.addClickHandler(sender -> drawCreditNoteExampleNumber());
        crnoProjectNumbering.addClickHandler(sender -> drawCreditNoteExampleNumber());
        crnoNumCell1 = createNumCell(0, CN);
        crnoNumCell1.ensureDebugId(CREDIT_NOTE_SETTINGS + "numCell1");

        crnoNumCell2 = createNumCell(0, CN);
        crnoNumCell2.ensureDebugId(CREDIT_NOTE_SETTINGS + "numCell2");

        crnoNumCell3 = createNumCell(0, CN);
        crnoNumCell3.ensureDebugId(CREDIT_NOTE_SETTINGS + "numCell3");

        crnoNumCell4 = createNumCell(1, CN);
        crnoNumCell4.ensureDebugId(CREDIT_NOTE_SETTINGS + "numCell4");

        crnoInvPrefix = new TextBox();
        crnoInvPrefix.ensureDebugId(CREDIT_NOTE_SETTINGS + "invPrefix");
        crnoInvPrefix.setText("CN");
        crnoNumberExample = new TextBox();

        crnoNumberExample.ensureDebugId(CREDIT_NOTE_SETTINGS + "credit_note_example");
        crnoNumberExample.setEnabled(false);
        crnoNumberExample.addStyleName("ml-3");
        final HorizontalPanel panel = new HorizontalPanel();
        panel.setStyleName("mod_table--cellpadding");

        panel.setSpacing(2);
//        panel.add(new HTML(wfmStrings.date()));
        panel.add(crnoDateNumbering);
//        panel.add(new HTML(wfmStrings.customer()));
        panel.add(crnoClientNumbering);
//        panel.add(new HTML(wfmStrings.project() + "# "));
        panel.add(crnoProjectNumbering);
//        final HorizontalPanel cell = new HorizontalPanel();

//        cell.add(crnoNumCell1);
//        cell.add(crnoNumCell2);
//        cell.add(crnoNumCell3);
//        cell.add(crnoNumCell4);
        creditNoteNumberFormat.setWidget(0, 0, crnoInvPrefix);
        creditNoteNumberFormat.setWidget(0, 1, panel);
        creditNoteNumberFormat.getFlexCellFormatter().addStyleName(0, 1, "pl-2 pr-2");
        creditNoteNumberFormat.setWidget(0, 2, new InputGroup(crnoNumCell1, crnoNumCell2, crnoNumCell3, crnoNumCell4));
        creditNoteNumberFormat.setWidget(0, 3, crnoNumberExample);
        this.addField(CREDIT_NOTE_NUMBERING, creditNoteNumberFormat, accountingStrings.creditNoteNumbering());
        this.drawCreditNoteExampleNumber();
    }

    private void drawDebitNoteNumberingForm() {
        FlexTable debitNoteNumberFormat = new FlexTable();
        debitNoteNumberFormat.setStyleName("mod_table--auto");

        dnoDateNumbering = new KpiCheckBox(wfmStrings.date());
        dnoDateNumbering.ensureDebugId(DEBIT_NOTE_SETTINGS + "dateNumbering");

        dnoClientNumbering = new KpiCheckBox(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
        dnoClientNumbering.ensureDebugId(DEBIT_NOTE_SETTINGS + "clientNumbering");
        dnoProjectNumbering = new KpiCheckBox(Property.get(Constants.PROJECT, wfmStrings.project()));
        dnoProjectNumbering.ensureDebugId(DEBIT_NOTE_SETTINGS + "projectNumbering");

        dnoDateNumbering.addClickHandler(sender -> drawDebitNoteExampleNumber());
        dnoClientNumbering.addClickHandler(sender -> drawDebitNoteExampleNumber());
        dnoProjectNumbering.addClickHandler(sender -> drawDebitNoteExampleNumber());
        dnoNumCell1 = createNumCell(0, DN);
        dnoNumCell1.ensureDebugId(DEBIT_NOTE_SETTINGS + "numCell1");

        dnoNumCell2 = createNumCell(0, DN);
        dnoNumCell2.ensureDebugId(DEBIT_NOTE_SETTINGS + "numCell2");

        dnoNumCell3 = createNumCell(0, DN);
        dnoNumCell3.ensureDebugId(DEBIT_NOTE_SETTINGS + "numCell3");

        dnoNumCell4 = createNumCell(1, DN);
        dnoNumCell4.ensureDebugId(DEBIT_NOTE_SETTINGS + "numCell4");

        dnoInvPrefix = new TextBox();
        dnoInvPrefix.ensureDebugId(DEBIT_NOTE_SETTINGS + "invPrefix");
        dnoInvPrefix.setWidth("40px");
        dnoInvPrefix.setText("DN");
        dnoNumberExample = new TextBox();
        dnoNumberExample.setWidth("180px");
        dnoNumberExample.ensureDebugId(DEBIT_NOTE_SETTINGS + "debit_note_example");

        dnoNumberExample.setEnabled(false);
        HorizontalPanel panel = new HorizontalPanel();
        panel.setStyleName("mod_table--cellpadding");
        panel.setSpacing(2);
//        panel.add(new HTML(wfmStrings.date()));
        panel.add(dnoDateNumbering);
//        HTML clientLabel = new HTML(wfmStrings.customer());
//        HTML projectLabel = new HTML(wfmStrings.project() + "# ");
//        panel.add(clientLabel);
        panel.add(dnoClientNumbering);
//        panel.add(projectLabel);
        panel.add(dnoProjectNumbering);
//        HorizontalPanel cell = new HorizontalPanel();
//        cell.add(dnoNumCell1);
//        cell.add(dnoNumCell2);
//        cell.add(dnoNumCell3);
//        cell.add(dnoNumCell4);
        debitNoteNumberFormat.setWidget(0, 0, dnoInvPrefix);
        debitNoteNumberFormat.setWidget(0, 1, panel);
        debitNoteNumberFormat.getFlexCellFormatter().addStyleName(0, 1, "pl-2 pr-2");
        debitNoteNumberFormat.setWidget(0, 2, new InputGroup(dnoNumCell1, dnoNumCell2, dnoNumCell3, dnoNumCell4));
        debitNoteNumberFormat.setWidget(0, 3, dnoNumberExample);
        addField(DEBIT_NOTE_NUMBERING, debitNoteNumberFormat, wfmStrings.number());
        drawDebitNoteExampleNumber();
    }

    private void drawInvoiceNumberingForm() {
        final FlexTable titleTable = new FlexTable();

        titleTable.setWidget(0, 0, new HTML(wfmStrings.prefix()));
        titleTable.setWidget(0, 1, new HTML(settingsStrings.alsoUse()));
        titleTable.setWidget(0, 2, new HTML(settingsStrings.startingNumber()));
        titleTable.setWidget(0, 3, new HTML(wfmStrings.preview()));
        titleTable.setCellSpacing(15);
        final FlexTable invNumberFormat = new FlexTable();
        invNumberFormat.setStyleName("mod_table--auto");

        dateNumbering = new KpiCheckBox(wfmStrings.date());
        dateNumbering.ensureDebugId(INVOICE_SETTINGS + "dateNumbering");

        clientNumbering = new KpiCheckBox(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
        clientNumbering.ensureDebugId(INVOICE_SETTINGS + "clientNumbering");
        projectNumbering = new KpiCheckBox(Property.get(Constants.PROJECT, wfmStrings.project()));
        projectNumbering.ensureDebugId(INVOICE_SETTINGS + "projectNumbering");

        dateNumbering.addClickHandler(sender -> drawInvoiceExampleNumber());
        clientNumbering.addClickHandler(sender -> drawInvoiceExampleNumber());
        projectNumbering.addClickHandler(sender -> drawInvoiceExampleNumber());
        numCell1 = createNumCell(0, INV);
        numCell1.ensureDebugId(INVOICE_SETTINGS + "numCell1");

        numCell2 = createNumCell(0, INV);
        numCell2.ensureDebugId(INVOICE_SETTINGS + "numCell2");

        numCell3 = createNumCell(0, INV);
        numCell3.ensureDebugId(INVOICE_SETTINGS + "numCell3");

        numCell4 = createNumCell(1, INV);
        numCell4.ensureDebugId(INVOICE_SETTINGS + "numCell4");

        invPrefix = new TextBox();
        invPrefix.ensureDebugId(INVOICE_SETTINGS + "invPrefix");
        invPrefix.setText(settingsStrings.inv());
        invNumberExample = new TextBox();

        invNumberExample.ensureDebugId(INVOICE_SETTINGS + "invNumberExample");

        invNumberExample.setEnabled(false);
        invNumberExample.addStyleName("ml-3");
        HorizontalPanel panel = new HorizontalPanel();
        panel.setStyleName("mod_table--cellpadding");
        panel.setSpacing(2);
//        panel.add(new HTML(wfmStrings.date()));
        panel.add(dateNumbering);
//        HTML clientLabel = new HTML(wfmStrings.customer());
//        HTML projectLabel = new HTML(wfmStrings.project() + "# ");
//        panel.add(clientLabel);
        panel.add(clientNumbering);
//        panel.add(projectLabel);
        panel.add(projectNumbering);
//        HorizontalPanel cell = new HorizontalPanel();
//        cell.add(numCell1);
//        cell.add(numCell2);
//        cell.add(numCell3);
//        cell.add(numCell4);
        invNumberFormat.setWidget(0, 0, invPrefix);
        invNumberFormat.setWidget(0, 1, panel);
        invNumberFormat.getFlexCellFormatter().addStyleName(0, 1, "pl-2 pr-2");
        invNumberFormat.setWidget(0, 2, new InputGroup(numCell1, numCell2, numCell3, numCell4));
        invNumberFormat.setWidget(0, 3, invNumberExample);
        drawInvoiceExampleNumber();

        //Sales Quote Numbering

        FlexTable sqNumberFormat = new FlexTable();
        sqNumberFormat.setStyleName("mod_table--auto");

        sqPrefix = new TextBox();
        sqPrefix.ensureDebugId(INVOICE_SETTINGS + "sqPrefix");
        sqPrefix.setText(accountingStrings.sq());

        sqNumberExample = new TextBox();

        sqNumberExample.ensureDebugId(INVOICE_SETTINGS + "sqNumberExample");
        sqNumberExample.setEnabled(false);
        sqNumberExample.addStyleName("ml-3");

        sqDateNumbering = new KpiCheckBox(wfmStrings.date());
        sqDateNumbering.ensureDebugId(INVOICE_SETTINGS + "sqDateNumbering");

        sqClientNumbering = new KpiCheckBox(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
        sqClientNumbering.ensureDebugId(INVOICE_SETTINGS + "sqClientNumbering");
        sqProjectNumbering = new KpiCheckBox(Property.get(Constants.PROJECT, wfmStrings.project()));
        sqProjectNumbering.ensureDebugId(INVOICE_SETTINGS + "sqProjectNumbering");


        sqDateNumbering.addClickHandler(sender -> drawQuoteExampleNumber());
        sqClientNumbering.addClickHandler(sender -> drawQuoteExampleNumber());
        sqProjectNumbering.addClickHandler(sender -> drawQuoteExampleNumber());
        panel = new HorizontalPanel();
        panel.setStyleName("mod_table--cellpadding");
        panel.setSpacing(2);
//        panel.add(new HTML(wfmStrings.date()));
        panel.add(sqDateNumbering);
//        clientLabel = new HTML(wfmStrings.customer());
//        projectLabel = new HTML(wfmStrings.project() + "# ");
//        panel.add(clientLabel);
        panel.add(sqClientNumbering);
//        panel.add(projectLabel);
        panel.add(sqProjectNumbering);
//        cell = new HorizontalPanel();
        sqCell1 = createNumCell(0, SQ);
        sqCell1.ensureDebugId(INVOICE_SETTINGS + "sqCell1");

        sqCell2 = createNumCell(0, SQ);
        sqCell2.ensureDebugId(INVOICE_SETTINGS + "sqCell2");

        sqCell3 = createNumCell(0, SQ);
        sqCell3.ensureDebugId(INVOICE_SETTINGS + "sqCell3");

        sqCell4 = createNumCell(1, SQ);
        sqCell4.ensureDebugId(INVOICE_SETTINGS + "sqCell4");

//        cell.add(sqCell1);
//        cell.add(sqCell2);
//        cell.add(sqCell3);
//        cell.add(sqCell4);
        sqNumberFormat.setWidget(0, 0, sqPrefix);
        sqNumberFormat.setWidget(0, 1, panel);
        sqNumberFormat.getFlexCellFormatter().addStyleName(0, 1, "pl-2 pr-2");
        sqNumberFormat.setWidget(0, 2, new InputGroup(sqCell1, sqCell2, sqCell3, sqCell4));
        sqNumberFormat.setWidget(0, 3, sqNumberExample);
        drawQuoteExampleNumber();

        //Sales Order Numbering

        FlexTable soNumberFormat = new FlexTable();
        soNumberFormat.setStyleName("mod_table--auto");

        soPrefix = new TextBox();
        soPrefix.ensureDebugId(INVOICE_SETTINGS + "soPrefix");
        soPrefix.setText(wfmStrings.so());

        soNumberExample = new TextBox();

        soNumberExample.ensureDebugId(INVOICE_SETTINGS + "soNumberExample");
        soNumberExample.setEnabled(false);
        soNumberExample.addStyleName("ml-3");

        soDateNumbering = new KpiCheckBox(wfmStrings.date());
        soDateNumbering.ensureDebugId(INVOICE_SETTINGS + "soDateNumbering");
        soClientNumbering = new KpiCheckBox(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
        soClientNumbering.ensureDebugId(INVOICE_SETTINGS + "soClientNumbering");
        soProjectNumbering = new KpiCheckBox(Property.get(Constants.PROJECT, wfmStrings.project()));
        soProjectNumbering.ensureDebugId(INVOICE_SETTINGS + "soProjectNumbering");


        soDateNumbering.addClickHandler(sender -> drawSalesOrderExampleNumber());
        soClientNumbering.addClickHandler(sender -> drawSalesOrderExampleNumber());
        soProjectNumbering.addClickHandler(sender -> drawSalesOrderExampleNumber());
        panel = new HorizontalPanel();
        panel.setStyleName("mod_table--cellpadding");
        panel.setSpacing(2);
//        panel.add(new HTML(wfmStrings.date()));
        panel.add(soDateNumbering);
//        clientLabel = new HTML(wfmStrings.customer());
//        projectLabel = new HTML(wfmStrings.project() + "# ");
//        panel.add(clientLabel);
        panel.add(soClientNumbering);
//        panel.add(projectLabel);
        panel.add(soProjectNumbering);
//        cell = new HorizontalPanel();
        soCell1 = createNumCell(0, SO);
        soCell1.ensureDebugId(INVOICE_SETTINGS + "soCell1");

        soCell2 = createNumCell(0, SO);
        soCell2.ensureDebugId(INVOICE_SETTINGS + "soCell2");

        soCell3 = createNumCell(0, SO);
        soCell3.ensureDebugId(INVOICE_SETTINGS + "soCell3");

        soCell4 = createNumCell(1, SO);
        soCell4.ensureDebugId(INVOICE_SETTINGS + "soCell4");

//        cell.add(soCell1);
//        cell.add(soCell2);
//        cell.add(soCell3);
//        cell.add(soCell4);
        soNumberFormat.setWidget(0, 0, soPrefix);
        soNumberFormat.setWidget(0, 1, panel);
        soNumberFormat.getFlexCellFormatter().addStyleName(0, 1, "pl-2 pr-2");
        soNumberFormat.setWidget(0, 2, new InputGroup(soCell1, soCell2, soCell3, soCell4));
        soNumberFormat.setWidget(0, 3, soNumberExample);
        saleOrderNumTable = new FormGroup(settingsStrings.salesOrderNumbering(), soNumberFormat);
        drawSalesOrderExampleNumber();

        //Purchase order numbering

        FlexTable poNumberFormat = new FlexTable();
        poNumberFormat.setStyleName("mod_table--auto");

        poPrefix = new TextBox();
        poPrefix.ensureDebugId(INVOICE_SETTINGS + "poPrefix");
        poPrefix.setText("PO");

        poNumberExample = new TextBox();

        poNumberExample.ensureDebugId(INVOICE_SETTINGS + "poNumberExample");
        poNumberExample.setEnabled(false);
        poNumberExample.addStyleName("ml-3");

        poDateNumbering = new KpiCheckBox(wfmStrings.date());
        poDateNumbering.ensureDebugId(INVOICE_SETTINGS + "poDateNumbering");

        poClientNumbering = new KpiCheckBox(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
        poClientNumbering.ensureDebugId(INVOICE_SETTINGS + "poClientNumbering");
        poProjectNumbering = new KpiCheckBox(Property.get(Constants.PROJECT, wfmStrings.project()));
        poProjectNumbering.ensureDebugId(INVOICE_SETTINGS + "poProjectNumbering");


        poDateNumbering.addClickHandler(sender -> drawOrderExampleNumber());
        poClientNumbering.addClickHandler(sender -> drawOrderExampleNumber());
        poProjectNumbering.addClickHandler(sender -> drawOrderExampleNumber());
        HorizontalPanel panelPO = new HorizontalPanel();
        panelPO.setStyleName("mod_table--cellpadding");
        panelPO.setSpacing(2);
//        panelPO.add(new HTML(wfmStrings.date()));
        panelPO.add(poDateNumbering);
//        clientLabel = new HTML(wfmStrings.customer());
//        projectLabel = new HTML(wfmStrings.project() + "# ");
//        panelPO.add(clientLabel);
        panelPO.add(poClientNumbering);
//        panelPO.add(projectLabel);
        panelPO.add(poProjectNumbering);

//        cell = new HorizontalPanel();
        poCell1 = createNumCell(0, PO);
        poCell1.ensureDebugId(INVOICE_SETTINGS + "poCell1");

        poCell2 = createNumCell(0, PO);
        poCell2.ensureDebugId(INVOICE_SETTINGS + "poCell2");

        poCell3 = createNumCell(0, PO);
        poCell3.ensureDebugId(INVOICE_SETTINGS + "poCell3");

        poCell4 = createNumCell(1, PO);
        poCell4.ensureDebugId(INVOICE_SETTINGS + "poCell4");

//        cell.add(poCell1);
//        cell.add(poCell2);
//        cell.add(poCell3);
//        cell.add(poCell4);
        poNumberFormat.setWidget(0, 0, poPrefix);
        poNumberFormat.setWidget(0, 1, panelPO);
        poNumberFormat.getFlexCellFormatter().addStyleName(0, 1, "pl-2 pr-2");
        poNumberFormat.setWidget(0, 2, new InputGroup(poCell1, poCell2, poCell3, poCell4));
        poNumberFormat.setWidget(0, 3, poNumberExample);
        //formatTable(poNumberFormat);
        drawOrderExampleNumber();

        //Purchase Invoice Numbering
        FlexTable pInvNumberFormat = new FlexTable();
        pInvNumberFormat.setStyleName("mod_table--auto");

        dateNumberingPI = new KpiCheckBox(wfmStrings.date());
        dateNumberingPI.ensureDebugId(INVOICE_SETTINGS + "dateNumberingPI");

        clientNumberingPI = new KpiCheckBox(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
        clientNumberingPI.ensureDebugId(INVOICE_SETTINGS + "clientNumberingPI");
        projectNumberingPI = new KpiCheckBox(Property.get(Constants.PROJECT, wfmStrings.project()));
        projectNumberingPI.ensureDebugId(INVOICE_SETTINGS + "projectNumberingPI");

        dateNumberingPI.addClickHandler(sender -> drawPInvoiceExampleNumber());
        clientNumberingPI.addClickHandler(sender -> drawPInvoiceExampleNumber());
        projectNumberingPI.addClickHandler(sender -> drawPInvoiceExampleNumber());
        numCell1PI = createNumCell(0, PI);
        numCell1PI.ensureDebugId(INVOICE_SETTINGS + "numCell1PI");

        numCell2PI = createNumCell(0, PI);
        numCell2PI.ensureDebugId(INVOICE_SETTINGS + "numCell2PI");

        numCell3PI = createNumCell(0, PI);
        numCell3PI.ensureDebugId(INVOICE_SETTINGS + "numCell3PI");

        numCell4PI = createNumCell(1, PI);
        numCell4PI.ensureDebugId(INVOICE_SETTINGS + "numCell4PI");

        pInvPrefix = new TextBox();
        pInvPrefix.ensureDebugId(INVOICE_SETTINGS + "invPrefix");
        pInvPrefix.setText("PI");
        pInvNumberExample = new TextBox();

        pInvNumberExample.ensureDebugId(INVOICE_SETTINGS + "invNumberExample");

        pInvNumberExample.setEnabled(false);
        pInvNumberExample.addStyleName("ml-3");
        HorizontalPanel panelPI = new HorizontalPanel();
        panelPI.setStyleName("mod_table--cellpadding");
        panelPI.setSpacing(2);
//        panelPI.add(new HTML(wfmStrings.date()));
        panelPI.add(dateNumberingPI);
//        HTML clientLabelPI = new HTML(wfmStrings.customer());
//        HTML projectLabelPI = new HTML(wfmStrings.project() + "# ");
//        panelPI.add(clientLabelPI);
        panelPI.add(clientNumberingPI);
//        panelPI.add(projectLabelPI);
        panelPI.add(projectNumberingPI);
//        HorizontalPanel cell2 = new HorizontalPanel();
//        cell2.add(numCell1PI);
//        cell2.add(numCell2PI);
//        cell2.add(numCell3PI);
//        cell2.add(numCell4PI);
        pInvNumberFormat.setWidget(0, 0, pInvPrefix);
        pInvNumberFormat.setWidget(0, 1, panelPI);
        pInvNumberFormat.getFlexCellFormatter().addStyleName(0, 1, "pl-2 pr-2");
        pInvNumberFormat.setWidget(0, 2, new InputGroup(numCell1PI, numCell2PI, numCell3PI, numCell4PI));
        pInvNumberFormat.setWidget(0, 3, pInvNumberExample);
        //formatTable(invNumberFormat);
        drawPInvoiceExampleNumber();

        //Purchase Invoice Credit Note Numbering
        FlexTable cnInvNumberFormat = new FlexTable();
        cnInvNumberFormat.setStyleName("mod_table--auto");

        cnDateNumbering = new KpiCheckBox(wfmStrings.date());
        cnDateNumbering.ensureDebugId(INVOICE_SETTINGS + "cnDateNumbering");

        cnClientNumbering = new KpiCheckBox(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
        cnClientNumbering.ensureDebugId(INVOICE_SETTINGS + "cnClientNumbering");
        cnProjectNumbering = new KpiCheckBox(Property.get(Constants.PROJECT, wfmStrings.project()));
        cnProjectNumbering.ensureDebugId(INVOICE_SETTINGS + "cnProjectNumbering");

        cnDateNumbering.addClickHandler(sender -> drawCNExampleNumber());
        cnClientNumbering.addClickHandler(sender -> drawCNExampleNumber());
        cnProjectNumbering.addClickHandler(sender -> drawCNExampleNumber());
        cnNumCell1 = createNumCell(0, 1);
        cnNumCell1.ensureDebugId(INVOICE_SETTINGS + "cnNumCell1");

        cnNumCell2 = createNumCell(0, 1);
        cnNumCell2.ensureDebugId(INVOICE_SETTINGS + "cnNumCell2");

        cnNumCell3 = createNumCell(0, 1);
        cnNumCell3.ensureDebugId(INVOICE_SETTINGS + "cnNumCell3");

        cnNumCell4 = createNumCell(1, 1);
        cnNumCell4.ensureDebugId(INVOICE_SETTINGS + "cnNumCell4");

        cnInvPrefix = new TextBox();
        cnInvPrefix.ensureDebugId(INVOICE_SETTINGS + "cnInvPrefix");
        cnInvPrefix.setText("CN");
        cnInvNumberExample = new TextBox();

        cnInvNumberExample.ensureDebugId(INVOICE_SETTINGS + "cnInvNumberExample");

        cnInvNumberExample.setEnabled(false);
        cnInvNumberExample.addStyleName("ml-3");
        HorizontalPanel panel2 = new HorizontalPanel();
        panel2.setStyleName("mod_table--cellpadding");
        panel2.setSpacing(2);
//        panel2.add(new HTML(wfmStrings.date()));
        panel2.add(cnDateNumbering);
//        HTML clientLabel2 = new HTML(wfmStrings.customer());
//        HTML projectLabel2 = new HTML(wfmStrings.project() + "# ");
//        panel2.add(clientLabel2);
        panel2.add(cnClientNumbering);
//        panel2.add(projectLabel2);
        panel2.add(cnProjectNumbering);
//        HorizontalPanel _cell = new HorizontalPanel();
//        _cell.add(cnNumCell1);
//        _cell.add(cnNumCell2);
//        _cell.add(cnNumCell3);
//        _cell.add(cnNumCell4);
        cnInvNumberFormat.setWidget(0, 0, cnInvPrefix);
        cnInvNumberFormat.setWidget(0, 1, panel2);
        cnInvNumberFormat.getFlexCellFormatter().addStyleName(0, 1, "pl-2 pr-2");
        cnInvNumberFormat.setWidget(0, 2, new InputGroup(cnNumCell1, cnNumCell2, cnNumCell3, cnNumCell4));
        cnInvNumberFormat.setWidget(0, 3, cnInvNumberExample);
        drawCNExampleNumber();

        //Purchase Invoice Debit Note Numbering
        FlexTable dnInvNumberFormat = new FlexTable();
        dnInvNumberFormat.setStyleName("mod_table--auto");

        dnDateNumbering = new KpiCheckBox(wfmStrings.date());
        dnDateNumbering.ensureDebugId(INVOICE_SETTINGS + "dnDateNumbering");

        dnClientNumbering = new KpiCheckBox(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
        dnClientNumbering.ensureDebugId(INVOICE_SETTINGS + "dnClientNumbering");
        dnProjectNumbering = new KpiCheckBox(Property.get(Constants.PROJECT, wfmStrings.project()));
        dnProjectNumbering.ensureDebugId(INVOICE_SETTINGS + "dnProjectNumbering");

        dnDateNumbering.addClickHandler(sender -> drawDNExampleNumber());
        dnClientNumbering.addClickHandler(sender -> drawDNExampleNumber());
        dnProjectNumbering.addClickHandler(sender -> drawDNExampleNumber());
        dnNumCell1 = createNumCell(0, 1);
        dnNumCell1.ensureDebugId(INVOICE_SETTINGS + "dnNumCell1");

        dnNumCell2 = createNumCell(0, 1);
        dnNumCell2.ensureDebugId(INVOICE_SETTINGS + "dnNumCell2");

        dnNumCell3 = createNumCell(0, 1);
        dnNumCell3.ensureDebugId(INVOICE_SETTINGS + "dnNumCell3");

        dnNumCell4 = createNumCell(1, 1);
        dnNumCell4.ensureDebugId(INVOICE_SETTINGS + "dnNumCell4");

        dnInvPrefix = new TextBox();
        dnInvPrefix.ensureDebugId(INVOICE_SETTINGS + "dnInvPrefix");
        dnInvPrefix.setWidth("40px");
        dnInvPrefix.setText("DN");
        dnInvNumberExample = new TextBox();
        dnInvNumberExample.setWidth("180px");
        dnInvNumberExample.ensureDebugId(INVOICE_SETTINGS + "dnInvNumberExample");

        dnInvNumberExample.setEnabled(false);
        HorizontalPanel panel3 = new HorizontalPanel();
        panel3.setStyleName("mod_table--cellpadding");
        panel3.setSpacing(2);
//        panel3.add(new HTML(wfmStrings.date()));
        panel3.add(dnDateNumbering);
//        HTML clientLabel3 = new HTML(wfmStrings.customer());
//        HTML projectLabel3 = new HTML(wfmStrings.project() + "# ");
//        panel3.add(clientLabel3);
        panel3.add(dnClientNumbering);
//        panel3.add(projectLabel3);
        panel3.add(dnProjectNumbering);
//        HorizontalPanel cell3 = new HorizontalPanel();
//        cell3.add(dnNumCell1);
//        cell3.add(dnNumCell2);
//        cell3.add(dnNumCell3);
//        cell3.add(dnNumCell4);
        dnInvNumberFormat.setWidget(0, 0, dnInvPrefix);
        dnInvNumberFormat.setWidget(0, 1, panel3);
        dnInvNumberFormat.getFlexCellFormatter().addStyleName(0, 1, "pl-2 pr-2");
        dnInvNumberFormat.setWidget(0, 2, new InputGroup(dnNumCell1, dnNumCell2, dnNumCell3, dnNumCell4));
        dnInvNumberFormat.setWidget(0, 3, dnInvNumberExample);
        drawDNExampleNumber();

        FlexTable restartNumberingTable = new FlexTable();
        restartNumberingTable.setStyleName("mod_table--auto");
        enableRestartNumbering = new KpiSwitcher();
        enableRestartNumbering.ensureDebugId(INVOICE_SETTINGS + "enableRestartNumbering");
        enableRestartNumbering.addStyleName("mr-3");

        financialStartDate = new DataListBox();
        financialStartDate.setWidth("75px");
        financialStartDate.ensureDebugId(INVOICE_SETTINGS + "financialStartDate");
        financialStartMonth = new DataListBox();
        financialStartMonth.setWidth("150px");
        financialStartMonth.ensureDebugId(INVOICE_SETTINGS + "financialStartMonth");
        financialStartDate.setEnabled(false);
        financialStartMonth.setEnabled(false);
        financialStartDate.setWithoutNullLabel(true);
        financialStartMonth.setWithoutNullLabel(true);


        financialStartMonth.addValueChangeHandler(changeEvent -> {
            Date date = new Date(2010, financialStartMonth.getSelectedId(), 1);
            setFinancialStartDateItems(date);
        });
        setFinancialStartMonthAndDateItems();
        enableRestartNumbering.addValueChangeHandler(clickEvent -> {
            financialStartDate.setEnabled(enableRestartNumbering.getValue());
            financialStartMonth.setEnabled(enableRestartNumbering.getValue());
        });
        restartNumberingTable.setWidget(0, 0, enableRestartNumbering);
        restartNumberingTable.setWidget(0, 1, new InputGroup(financialStartDate, financialStartMonth));

        HelpTextPanel invoiceNumberingInfo = new HelpTextPanel(settingsStrings.forInvoiceNumbers(), 150);

        addField(TITLE, titleTable, "", true);
        addField(INVOICE_NUMBERING, invNumberFormat, accountingStrings.invoice());
        addField(SQ_NUMBERING, sqNumberFormat, wfmStrings.salesQuote());
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_SALES_ORDER_NUMBERING)) {
            addField(SO_NUMBERING, saleOrderNumTable);
        }
        addField(PO_NUMBERING, poNumberFormat, wfmStrings.purchaseorder());
        addField(PI_NUMBERING, pInvNumberFormat, wfmStrings.purchaseInvoices());
        addField(RESTART_NUMBERING, restartNumberingTable, wfmStrings.restartNumeringEveryYearOn());
        addField(INVOICE_NUMBERING_INFO, invoiceNumberingInfo, "", true);

    }

    private void setFinancialStartMonthAndDateItems() {
        Date monthStart = DateUtil.getYearFirstDay(new Date());
        List<SelectItem> monthItems = new LinkedList<>();
        for (int i = 0; i < 12; i++) {
            monthStart.setMonth(i);
            monthItems.add(new SelectItem(i, DateTimeFormat.getFormat("MMM").format(monthStart)));
        }
        financialStartMonth.setItems(monthItems.toArray(new SelectItem[]{}));
        financialStartMonth.setSelected(0);
        setFinancialStartDateItems(monthStart);
    }

    private void setFinancialStartDateItems(Date date) {
        Integer selectedID = financialStartDate.getSelectedId();
        Date monthEnd = DateUtil.getMonthLastDate(date);
        List<SelectItem> dateItems = new LinkedList<>();
        for (int i = 1; i <= monthEnd.getDate(); i++) {
            dateItems.add(new SelectItem(i, String.valueOf(i)));
        }
        financialStartDate.setItems(dateItems.toArray(new SelectItem[]{}));
        financialStartDate.setSelected(selectedID != null ? selectedID : 1);
    }

    private String getInvoiceNumberingFormat() {
        String numberingFormat = "";
        if (!invPrefix.getText().equals("")) {
            numberingFormat += invPrefix.getText() + SPLITTER;
        }
        if (dateNumbering.getValue()) {
            numberingFormat += DATE + SPLITTER;
        }
        if (clientNumbering.getValue()) {
            numberingFormat += CLIENTCODE + SPLITTER;
        }
        if (projectNumbering.getValue()) {
            numberingFormat += PROJECTCODE + SPLITTER;
        }

        numberingFormat += AccountingUtils.getNumCellValue(numCell1);
        numberingFormat += AccountingUtils.getNumCellValue(numCell2);
        numberingFormat += AccountingUtils.getNumCellValue(numCell3);
        numberingFormat += AccountingUtils.getNumCellValue(numCell4);
        return numberingFormat;
    }

    private String getInvoiceCreditNoteNumberingFormat() {
        String numberingFormat = "";
        if (!crnoInvPrefix.getText().equals("")) {
            numberingFormat += crnoInvPrefix.getText() + SPLITTER;
        }
        if (crnoDateNumbering.getValue()) {
            numberingFormat += DATE + SPLITTER;
        }
        if (crnoClientNumbering.getValue()) {
            numberingFormat += CLIENTCODE + SPLITTER;
        }
        if (crnoProjectNumbering.getValue()) {
            numberingFormat += PROJECTCODE + SPLITTER;
        }

        numberingFormat += AccountingUtils.getNumCellValue(crnoNumCell1);
        numberingFormat += AccountingUtils.getNumCellValue(crnoNumCell2);
        numberingFormat += AccountingUtils.getNumCellValue(crnoNumCell3);
        numberingFormat += AccountingUtils.getNumCellValue(crnoNumCell4);
        return numberingFormat;
    }

    private String getSalesQuoteNumberingFormat() {
        String numberingFormat = "";
        if (!sqPrefix.getText().equals("")) {
            numberingFormat += sqPrefix.getText() + SPLITTER;
        }
        if (sqDateNumbering.getValue()) {
            numberingFormat += DATE + SPLITTER;
        }
        if (sqClientNumbering.getValue()) {
            numberingFormat += CLIENTCODE + SPLITTER;
        }
        if (sqProjectNumbering.getValue()) {
            numberingFormat += PROJECTCODE + SPLITTER;
        }
        numberingFormat += AccountingUtils.getNumCellValue(sqCell1);
        numberingFormat += AccountingUtils.getNumCellValue(sqCell2);
        numberingFormat += AccountingUtils.getNumCellValue(sqCell3);
        numberingFormat += AccountingUtils.getNumCellValue(sqCell4);
        return numberingFormat;
    }

    private String getSalesOrderNumberingFormat() {
        String numberingFormat = "";
        if (!soPrefix.getText().equals("")) {
            numberingFormat += soPrefix.getText() + SPLITTER;
        }
        if (soDateNumbering.getValue()) {
            numberingFormat += DATE + SPLITTER;
        }
        if (soClientNumbering.getValue()) {
            numberingFormat += CLIENTCODE + SPLITTER;
        }
        if (soProjectNumbering.getValue()) {
            numberingFormat += PROJECTCODE + SPLITTER;
        }
        numberingFormat += AccountingUtils.getNumCellValue(soCell1);
        numberingFormat += AccountingUtils.getNumCellValue(soCell2);
        numberingFormat += AccountingUtils.getNumCellValue(soCell3);
        numberingFormat += AccountingUtils.getNumCellValue(soCell4);
        return numberingFormat;
    }

    private String getPurchaseOrderNumberingFormat() {
        String numberingFormat = "";
        if (!poPrefix.getText().equals("")) {
            numberingFormat += poPrefix.getText() + SPLITTER;
        }
        if (poDateNumbering.getValue()) {
            numberingFormat += DATE + SPLITTER;
        }
        if (poClientNumbering.getValue()) {
            numberingFormat += CLIENTCODE + SPLITTER;
        }
        if (poProjectNumbering.getValue()) {
            numberingFormat += PROJECTCODE + SPLITTER;
        }
        numberingFormat += AccountingUtils.getNumCellValue(poCell1);
        numberingFormat += AccountingUtils.getNumCellValue(poCell2);
        numberingFormat += AccountingUtils.getNumCellValue(poCell3);
        numberingFormat += AccountingUtils.getNumCellValue(poCell4);
        return numberingFormat;
    }

    private String getPurchaseInvoiceNumberingFormat() {
        String numberingFormat = "";
        if (!pInvPrefix.getText().equals("")) {
            numberingFormat += pInvPrefix.getText() + SPLITTER;
        }
        if (dateNumberingPI.getValue()) {
            numberingFormat += DATE + SPLITTER;
        }
        if (clientNumberingPI.getValue()) {
            numberingFormat += CLIENTCODE + SPLITTER;
        }
        if (projectNumberingPI.getValue()) {
            numberingFormat += PROJECTCODE + SPLITTER;
        }

        numberingFormat += AccountingUtils.getNumCellValue(numCell1PI);
        numberingFormat += AccountingUtils.getNumCellValue(numCell2PI);
        numberingFormat += AccountingUtils.getNumCellValue(numCell3PI);
        numberingFormat += AccountingUtils.getNumCellValue(numCell4PI);
        return numberingFormat;
    }

    private String getCreditNoteNumberingFormat() {
        String numberingFormat = "";
        if (!cnInvPrefix.getText().equals("")) {
            numberingFormat += cnInvPrefix.getText() + SPLITTER;
        }
        if (cnDateNumbering.getValue()) {
            numberingFormat += DATE + SPLITTER;
        }
        if (cnClientNumbering.getValue()) {
            numberingFormat += CLIENTCODE + SPLITTER;
        }
        if (cnProjectNumbering.getValue()) {
            numberingFormat += PROJECTCODE + SPLITTER;
        }

        numberingFormat += AccountingUtils.getNumCellValue(cnNumCell1);
        numberingFormat += AccountingUtils.getNumCellValue(cnNumCell2);
        numberingFormat += AccountingUtils.getNumCellValue(cnNumCell3);
        numberingFormat += AccountingUtils.getNumCellValue(cnNumCell4);
        return numberingFormat;
    }

    private String getDebitNoteNumberingFormat() {
        String numberingFormat = "";
        if (!dnInvPrefix.getText().equals("")) {
            numberingFormat += dnInvPrefix.getText() + SPLITTER;
        }
        if (dnDateNumbering.getValue()) {
            numberingFormat += DATE + SPLITTER;
        }
        if (dnClientNumbering.getValue()) {
            numberingFormat += CLIENTCODE + SPLITTER;
        }
        if (dnProjectNumbering.getValue()) {
            numberingFormat += PROJECTCODE + SPLITTER;
        }

        numberingFormat += AccountingUtils.getNumCellValue(dnNumCell1);
        numberingFormat += AccountingUtils.getNumCellValue(dnNumCell2);
        numberingFormat += AccountingUtils.getNumCellValue(dnNumCell3);
        numberingFormat += AccountingUtils.getNumCellValue(dnNumCell4);
        return numberingFormat;
    }

    private void parseAndSetNumberingData(String invNumData, String sqNumData, String soNumData, String poNumData, String piNumData, String cnNumData, String dnNumData, String creditNoteNumberData) {
        if (invNumData != null) {
            String[] parts = invNumData.split(SPLITTER);
            if (parts.length == 5) {
                invPrefix.setText(parts[0]);
                dateNumbering.setValue(true);
                clientNumbering.setValue(true);
                projectNumbering.setValue(true);
                parseAndSetCellValues(numCell1, numCell2, numCell3, numCell4, parts[4]);
            }
            if (parts.length == 4) {
                detectPartAndSetValue(parts[0], INV);
                detectPartAndSetValue(parts[1], INV);
                detectPartAndSetValue(parts[2], INV);
                parseAndSetCellValues(numCell1, numCell2, numCell3, numCell4, parts[3]);
            }
            if (parts.length == 3) {
                detectPartAndSetValue(parts[0], INV);
                detectPartAndSetValue(parts[1], INV);
                parseAndSetCellValues(numCell1, numCell2, numCell3, numCell4, parts[2]);
            }
            if (parts.length == 2) {
                detectPartAndSetValue(parts[0], INV);
                parseAndSetCellValues(numCell1, numCell2, numCell3, numCell4, parts[1]);
            }
            if (parts.length == 1) {
                parseAndSetCellValues(numCell1, numCell2, numCell3, numCell4, parts[0]);
            }
        }
        drawInvoiceExampleNumber();
        if (sqNumData != null) {
            String[] parts = sqNumData.split(SPLITTER);
            if (parts.length == 5) {
                sqPrefix.setText(parts[0]);
                sqDateNumbering.setValue(true);
                sqClientNumbering.setValue(true);
                sqProjectNumbering.setValue(true);
                parseAndSetCellValues(sqCell1, sqCell2, sqCell3, sqCell4, parts[4]);
            }
            if (parts.length == 4) {
                detectPartAndSetValue(parts[0], SQ);
                detectPartAndSetValue(parts[1], SQ);
                detectPartAndSetValue(parts[2], SQ);
                parseAndSetCellValues(sqCell1, numCell2, sqCell3, sqCell4, parts[3]);
            }
            if (parts.length == 3) {
                detectPartAndSetValue(parts[0], SQ);
                detectPartAndSetValue(parts[1], SQ);
                parseAndSetCellValues(sqCell1, sqCell2, sqCell3, sqCell4, parts[2]);
            }
            if (parts.length == 2) {
                detectPartAndSetValue(parts[0], SQ);
                parseAndSetCellValues(sqCell1, sqCell2, sqCell3, sqCell4, parts[1]);
            }
            if (parts.length == 1) {
                parseAndSetCellValues(sqCell1, sqCell2, sqCell3, sqCell4, parts[0]);
            }
        }
        drawQuoteExampleNumber();
        if (soNumData != null) {
            String[] parts = soNumData.split(SPLITTER);
            if (parts.length == 5) {
                soPrefix.setText(parts[0]);
                soDateNumbering.setValue(true);
                soClientNumbering.setValue(true);
                soProjectNumbering.setValue(true);
                parseAndSetCellValues(soCell1, soCell2, soCell3, soCell4, parts[4]);
            }
            if (parts.length == 4) {
                detectPartAndSetValue(parts[0], SO);
                detectPartAndSetValue(parts[1], SO);
                detectPartAndSetValue(parts[2], SO);
                parseAndSetCellValues(soCell1, soCell2, soCell3, soCell4, parts[3]);
            }
            if (parts.length == 3) {
                detectPartAndSetValue(parts[0], SO);
                detectPartAndSetValue(parts[1], SO);
                parseAndSetCellValues(soCell1, soCell2, soCell3, soCell4, parts[2]);
            }
            if (parts.length == 2) {
                detectPartAndSetValue(parts[0], SO);
                parseAndSetCellValues(soCell1, soCell2, soCell3, soCell4, parts[1]);
            }
            if (parts.length == 1) {
                parseAndSetCellValues(soCell1, soCell2, soCell3, soCell4, parts[0]);
            }
        }
        drawSalesOrderExampleNumber();
        if (poNumData != null) {
            String[] parts = poNumData.split(SPLITTER);
            if (parts.length == 5) {
                poPrefix.setText(parts[0]);
                poDateNumbering.setValue(true);
                poClientNumbering.setValue(true);
                poProjectNumbering.setValue(true);
                parseAndSetCellValues(poCell1, poCell2, poCell3, poCell4, parts[4]);
            }
            if (parts.length == 4) {
                detectPartAndSetValue(parts[0], PO);
                detectPartAndSetValue(parts[1], PO);
                detectPartAndSetValue(parts[2], PO);
                parseAndSetCellValues(poCell1, poCell2, poCell3, poCell4, parts[3]);
            }
            if (parts.length == 3) {
                detectPartAndSetValue(parts[0], PO);
                detectPartAndSetValue(parts[1], PO);
                parseAndSetCellValues(poCell1, poCell2, poCell3, poCell4, parts[2]);
            }
            if (parts.length == 2) {
                detectPartAndSetValue(parts[0], PO);
                parseAndSetCellValues(poCell1, poCell2, poCell3, poCell4, parts[1]);
            }
            if (parts.length == 1) {
                parseAndSetCellValues(poCell1, poCell2, poCell3, poCell4, parts[0]);
            }
        }
        drawOrderExampleNumber();

        if (piNumData != null) {
            String[] parts = piNumData.split(SPLITTER);
            if (parts.length == 5) {
                pInvPrefix.setText(parts[0]);
                dateNumberingPI.setValue(true);
                clientNumberingPI.setValue(true);
                projectNumberingPI.setValue(true);
                parseAndSetCellValues(numCell1PI, numCell2PI, numCell3PI, numCell4PI, parts[4]);
            }
            if (parts.length == 4) {
                detectPartAndSetValue(parts[0], dateNumberingPI, clientNumberingPI, projectNumberingPI, pInvPrefix);
                detectPartAndSetValue(parts[1], dateNumberingPI, clientNumberingPI, projectNumberingPI, pInvPrefix);
                detectPartAndSetValue(parts[2], dateNumberingPI, clientNumberingPI, projectNumberingPI, pInvPrefix);
                parseAndSetCellValues(numCell1PI, numCell2PI, numCell3PI, numCell4PI, parts[3]);
            }
            if (parts.length == 3) {
                detectPartAndSetValue(parts[0], dateNumberingPI, clientNumberingPI, projectNumberingPI, pInvPrefix);
                detectPartAndSetValue(parts[1], dateNumberingPI, clientNumberingPI, projectNumberingPI, pInvPrefix);
                parseAndSetCellValues(numCell1PI, numCell2PI, numCell3PI, numCell4PI, parts[2]);
            }
            if (parts.length == 2) {
                detectPartAndSetValue(parts[0], dateNumberingPI, clientNumberingPI, projectNumberingPI, pInvPrefix);
                parseAndSetCellValues(numCell1PI, numCell2PI, numCell3PI, numCell4PI, parts[1]);
            }
            if (parts.length == 1) {
                parseAndSetCellValues(numCell1PI, numCell2PI, numCell3PI, numCell4PI, parts[0]);
            }
        }
        drawPInvoiceExampleNumber();

        if (cnNumData != null) {
            String[] parts = cnNumData.split(SPLITTER);
            if (parts.length == 5) {
                cnInvPrefix.setText(parts[0]);
                cnDateNumbering.setValue(true);
                cnClientNumbering.setValue(true);
                cnProjectNumbering.setValue(true);
                parseAndSetCellValues(cnNumCell1, cnNumCell2, cnNumCell3, cnNumCell4, parts[4]);
            }
            if (parts.length == 4) {
                detectPartAndSetValue(parts[0], cnDateNumbering, cnClientNumbering, cnProjectNumbering, cnInvPrefix);
                detectPartAndSetValue(parts[1], cnDateNumbering, cnClientNumbering, cnProjectNumbering, cnInvPrefix);
                detectPartAndSetValue(parts[2], cnDateNumbering, cnClientNumbering, cnProjectNumbering, cnInvPrefix);
                parseAndSetCellValues(cnNumCell1, cnNumCell2, cnNumCell3, cnNumCell4, parts[3]);
            }
            if (parts.length == 3) {
                detectPartAndSetValue(parts[0], cnDateNumbering, cnClientNumbering, cnProjectNumbering, cnInvPrefix);
                detectPartAndSetValue(parts[1], cnDateNumbering, cnClientNumbering, cnProjectNumbering, cnInvPrefix);
                parseAndSetCellValues(cnNumCell1, cnNumCell2, cnNumCell3, cnNumCell4, parts[2]);
            }
            if (parts.length == 2) {
                detectPartAndSetValue(parts[0], cnDateNumbering, cnClientNumbering, cnProjectNumbering, cnInvPrefix);
                parseAndSetCellValues(cnNumCell1, cnNumCell2, cnNumCell3, cnNumCell4, parts[1]);
            }
            if (parts.length == 1) {
                parseAndSetCellValues(cnNumCell1, cnNumCell2, cnNumCell3, cnNumCell4, parts[0]);
            }
        }
        drawCNExampleNumber();

        if (dnNumData != null) {
            String[] parts = dnNumData.split(SPLITTER);
            if (parts.length == 5) {
                dnInvPrefix.setText(parts[0]);
                dnDateNumbering.setValue(true);
                dnClientNumbering.setValue(true);
                dnProjectNumbering.setValue(true);
                parseAndSetCellValues(dnNumCell1, dnNumCell2, dnNumCell3, dnNumCell4, parts[4]);
            }
            if (parts.length == 4) {
                detectPartAndSetValue(parts[0], dnDateNumbering, dnClientNumbering, dnProjectNumbering, dnInvPrefix);
                detectPartAndSetValue(parts[1], dnDateNumbering, dnClientNumbering, dnProjectNumbering, dnInvPrefix);
                detectPartAndSetValue(parts[2], dnDateNumbering, dnClientNumbering, dnProjectNumbering, dnInvPrefix);
                parseAndSetCellValues(dnNumCell1, dnNumCell2, dnNumCell3, dnNumCell4, parts[3]);
            }
            if (parts.length == 3) {
                detectPartAndSetValue(parts[0], dnDateNumbering, dnClientNumbering, dnProjectNumbering, dnInvPrefix);
                detectPartAndSetValue(parts[1], dnDateNumbering, dnClientNumbering, dnProjectNumbering, dnInvPrefix);
                parseAndSetCellValues(dnNumCell1, dnNumCell2, dnNumCell3, dnNumCell4, parts[2]);
            }
            if (parts.length == 2) {
                detectPartAndSetValue(parts[0], dnDateNumbering, dnClientNumbering, dnProjectNumbering, dnInvPrefix);
                parseAndSetCellValues(dnNumCell1, dnNumCell2, dnNumCell3, dnNumCell4, parts[1]);
            }
            if (parts.length == 1) {
                parseAndSetCellValues(dnNumCell1, dnNumCell2, dnNumCell3, dnNumCell4, parts[0]);
            }
        }
        drawDNExampleNumber();

        if (creditNoteNumberData != null) {
            String[] parts = creditNoteNumberData.split(SPLITTER);
            if (parts.length == 5) {
                crnoInvPrefix.setText(parts[0]);
                crnoDateNumbering.setValue(true);
                crnoClientNumbering.setValue(true);
                crnoProjectNumbering.setValue(true);
                parseAndSetCellValues(crnoNumCell1, crnoNumCell2, crnoNumCell3, crnoNumCell4, parts[4]);
            }
            if (parts.length == 4) {
                detectPartAndSetValue(parts[0], CN);
                detectPartAndSetValue(parts[1], CN);
                detectPartAndSetValue(parts[2], CN);
                parseAndSetCellValues(crnoNumCell1, crnoNumCell2, crnoNumCell3, crnoNumCell4, parts[3]);
            }
            if (parts.length == 3) {
                detectPartAndSetValue(parts[0], CN);
                detectPartAndSetValue(parts[1], CN);
                parseAndSetCellValues(crnoNumCell1, crnoNumCell2, crnoNumCell3, crnoNumCell4, parts[2]);
            }
            if (parts.length == 2) {
                detectPartAndSetValue(parts[0], CN);
                parseAndSetCellValues(crnoNumCell1, crnoNumCell2, crnoNumCell3, crnoNumCell4, parts[1]);
            }
            if (parts.length == 1) {
                parseAndSetCellValues(crnoNumCell1, crnoNumCell2, crnoNumCell3, crnoNumCell4, parts[0]);
            }
        }
        drawCreditNoteExampleNumber();
    }

    private void detectPartAndSetValue(String part, KpiCheckBox _dateNumbering, KpiCheckBox _clientNumbering, KpiCheckBox _projectNumbering, TextBox _perfix) {
        if (part.equals(DATE)) {
            _dateNumbering.setValue(true);
        } else if (part.equals(CLIENTCODE)) {
            _clientNumbering.setValue(true);
        } else if (part.equals(PROJECTCODE)) {
            projectNumbering.setValue(true);
        } else {
            _perfix.setText(part);
        }
    }

    private void detectPartAndSetValue(String part, int type) {
        if (type == INV) {
            if (part.equals(DATE)) {
                dateNumbering.setValue(true);
            } else if (part.equals(CLIENTCODE)) {
                clientNumbering.setValue(true);
            } else if (part.equals(PROJECTCODE)) {
                projectNumbering.setValue(true);
            } else {
                invPrefix.setText(part);
            }
        } else if (type == SQ) {
            if (part.equals(DATE)) {
                sqDateNumbering.setValue(true);
            } else if (part.equals(CLIENTCODE)) {
                sqClientNumbering.setValue(true);
            } else if (part.equals(PROJECTCODE)) {
                sqProjectNumbering.setValue(true);
            } else {
                sqPrefix.setText(part);
            }
        } else if (type == SO) {
            if (part.equals(DATE)) {
                soDateNumbering.setValue(true);
            } else if (part.equals(CLIENTCODE)) {
                soClientNumbering.setValue(true);
            } else if (part.equals(PROJECTCODE)) {
                soProjectNumbering.setValue(true);
            } else {
                soPrefix.setText(part);
            }
        } else if (type == PO) {
            if (part.equals(DATE)) {
                poDateNumbering.setValue(true);
            } else if (part.equals(CLIENTCODE)) {
                poClientNumbering.setValue(true);
            } else if (part.equals(PROJECTCODE)) {
                poProjectNumbering.setValue(true);
            } else {
                poPrefix.setText(part);
            }
        } else if (type == CN) {
            if (part.equals(DATE)) {
                crnoDateNumbering.setValue(true);
            } else if (part.equals(CLIENTCODE)) {
                crnoClientNumbering.setValue(true);
            } else if (part.equals(PROJECTCODE)) {
                crnoProjectNumbering.setValue(true);
            } else {
                crnoInvPrefix.setText(part);
            }
        }

    }

    private void drawOrderExampleNumber() {
        String example = "";
        if (poDateNumbering.getValue()) {
            example += dateFormat.format(new Date());
        }
        if (poClientNumbering.getValue()) {
            example += "CUS0001";
        }
        if (poProjectNumbering.getValue()) {
            example += "P0001";
        }
        example += AccountingUtils.getNumCellValue(poCell1);
        example += AccountingUtils.getNumCellValue(poCell2);
        example += AccountingUtils.getNumCellValue(poCell3);
        example += AccountingUtils.getNumCellValue(poCell4);
        poNumberExample.setText(example);
    }

    private void drawCreditNoteExampleNumber() {
        String example = "";
        if (crnoDateNumbering.getValue()) {
            example += dateFormat.format(new Date());
        }
        if (crnoClientNumbering.getValue()) {
            example += "CUS0001";
        }
        if (crnoProjectNumbering.getValue()) {
            example += "P0001";
        }
        example += AccountingUtils.getNumCellValue(crnoNumCell1);
        example += AccountingUtils.getNumCellValue(crnoNumCell2);
        example += AccountingUtils.getNumCellValue(crnoNumCell3);
        example += AccountingUtils.getNumCellValue(crnoNumCell4);
        crnoNumberExample.setText(example);
    }

    private void drawDebitNoteExampleNumber() {
        String example = "";
        if (dnoDateNumbering.getValue()) {
            example += dateFormat.format(new Date());
        }
        if (dnoClientNumbering.getValue()) {
            example += "CUS0001";
        }
        if (dnoProjectNumbering.getValue()) {
            example += "P0001";
        }
        example += AccountingUtils.getNumCellValue(dnoNumCell1);
        example += AccountingUtils.getNumCellValue(dnoNumCell2);
        example += AccountingUtils.getNumCellValue(dnoNumCell3);
        example += AccountingUtils.getNumCellValue(dnoNumCell4);
        dnoNumberExample.setText(example);
    }

    private void drawInvoiceExampleNumber() {
        String example = "";
        if (dateNumbering.getValue()) {
            example += dateFormat.format(new Date());
        }
        if (clientNumbering.getValue()) {
            example += "CUS0001";
        }
        if (projectNumbering.getValue()) {
            example += "P0001";
        }
        example += AccountingUtils.getNumCellValue(numCell1);
        example += AccountingUtils.getNumCellValue(numCell2);
        example += AccountingUtils.getNumCellValue(numCell3);
        example += AccountingUtils.getNumCellValue(numCell4);
        invNumberExample.setText(example);
    }

    private void drawQuoteExampleNumber() {
        String example = "";
        if (sqDateNumbering.getValue()) {
            example += dateFormat.format(new Date());
        }
        if (sqClientNumbering.getValue()) {
            example += "CUS0001";
        }
        if (sqProjectNumbering.getValue()) {
            example += "P0001";
        }
        example += AccountingUtils.getNumCellValue(sqCell1);
        example += AccountingUtils.getNumCellValue(sqCell2);
        example += AccountingUtils.getNumCellValue(sqCell3);
        example += AccountingUtils.getNumCellValue(sqCell4);
        sqNumberExample.setText(example);
    }

    private void drawSalesOrderExampleNumber() {
        String example = "";
        if (soDateNumbering.getValue()) {
            example += dateFormat.format(new Date());
        }
        if (soClientNumbering.getValue()) {
            example += "CUS0001";
        }
        if (soProjectNumbering.getValue()) {
            example += "P0001";
        }
        example += AccountingUtils.getNumCellValue(soCell1);
        example += AccountingUtils.getNumCellValue(soCell2);
        example += AccountingUtils.getNumCellValue(soCell3);
        example += AccountingUtils.getNumCellValue(soCell4);
        soNumberExample.setText(example);
    }

    private void drawCNExampleNumber() {
        String example = "";
        if (cnDateNumbering.getValue()) {
            example += dateFormat.format(new Date());
        }
        if (cnClientNumbering.getValue()) {
            example += "CUS0001";
        }
        if (cnProjectNumbering.getValue()) {
            example += "P0001";
        }
        example += AccountingUtils.getNumCellValue(cnNumCell1);
        example += AccountingUtils.getNumCellValue(cnNumCell2);
        example += AccountingUtils.getNumCellValue(cnNumCell3);
        example += AccountingUtils.getNumCellValue(cnNumCell4);
        cnInvNumberExample.setText(example);
    }

    private void drawDNExampleNumber() {
        String example = "";
        if (dnDateNumbering.getValue()) {
            example += dateFormat.format(new Date());
        }
        if (dnClientNumbering.getValue()) {
            example += "CUS0001";
        }
        if (dnProjectNumbering.getValue()) {
            example += "P0001";
        }
        example += AccountingUtils.getNumCellValue(dnNumCell1);
        example += AccountingUtils.getNumCellValue(dnNumCell2);
        example += AccountingUtils.getNumCellValue(dnNumCell3);
        example += AccountingUtils.getNumCellValue(dnNumCell4);
        dnInvNumberExample.setText(example);
    }

    private void drawPInvoiceExampleNumber() {
        String example = "";
        if (dateNumberingPI.getValue()) {
            example += dateFormat.format(new Date());
        }
        if (clientNumberingPI.getValue()) {
            example += "CUS0001";
        }
        if (projectNumberingPI.getValue()) {
            example += "P0001";
        }
        example += AccountingUtils.getNumCellValue(numCell1PI);
        example += AccountingUtils.getNumCellValue(numCell2PI);
        example += AccountingUtils.getNumCellValue(numCell3PI);
        example += AccountingUtils.getNumCellValue(numCell4PI);
        pInvNumberExample.setText(example);
    }

    @Override
    public String getIconStyle() {
        return "accountMark ac-type-num-settings";//return "icon-settings-invoice";
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