package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.PMNumberingSettings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import java.util.Date;

/**
 * Created by Omonullo on 5/13/2017.
 */
public class PayrollNumberingSettingsUIForm {
    interface IPayrollNumberingSettingsUIForm extends UiBinder<HTMLPanel, PayrollNumberingSettingsUIForm> {
    }

    private static final IPayrollNumberingSettingsUIForm ourUiBinder = GWT.create(IPayrollNumberingSettingsUIForm.class);
    private final HTMLPanel rootElement;
    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);
    private static final WfmStrings wfmStrings = GWT.create(WfmStrings.class);
    private static final int CA = 0;
    private static final int EOS = 1;
    private static final int MCA = 2;
    private final DateTimeFormat dateFormat = DateTimeFormat.getFormat("MM/yyyy");
    private static final String DATE = "date";
    private static final String SPLITTER = "_";
    private PMNumberingSettings settings;

    private TextBox caPrefix, mcaPrefix, eosPrefix;
    private TextBox caNumCell1, mcaNumCell1, eosNumCell1;
    private TextBox caNumCell2, mcaNumCell2, eosNumCell2;
    private TextBox caNumCell3, mcaNumCell3, eosNumCell3;
    private TextBox caNumCell4, mcaNumCell4, eosNumCell4;
    private KpiSwitcher caDateNumbering, mcaDateNumbering, eosDateNumbering;
    private TextBox caNumberExample, mcaNumberExample, eosNumberExample;

    @UiField
    FormGroup layoutBox;

    public PayrollNumberingSettingsUIForm(PMNumberingSettings settings) {
        rootElement = ourUiBinder.createAndBindUi(this);
        this.settings = settings;
        initialize();
        parseAndSetData(this.settings);
    }

    private void initialize() {
        /*WfmButton2 saveButton = new WfmButton2(payrollStrings.saveSettings(), WfmButton2.BTN_PRIMARY);
        saveButton.addStyleName(WfmButton2.BTN_PRIMARY);
        saveButton.addClickHandler(e -> save());
        saveButton.setText(wfmStrings.save());
        MaterialPanel actionPanel = MainLayout.get().getActionsContainer();
        MaterialPanel buttonList = new MaterialPanel("btns-group");
        buttonList.add(saveButton);
        actionPanel.clear();
        actionPanel.add(buttonList);*/

        //buttonPanel.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        //buttonPanel.add(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save()));

//        saveButton.setText(wfmStrings.save());
//        saveButton.setStyleName(WfmButton2.BTN_PRIMARY);
//        saveButton.ensureDebugId("Payroll_numbering_save_button");
//        saveButton.addClickHandler(event -> save());

        //MainLayout.get().getFrameContainer().addStyleName("has-tabs");
        FlexTable settingsTable = new FlexTable();
        settingsTable.addStyleName("numbering-settings table-info");
        caPrefix = new TextBox();
        caPrefix.setWidth("70px");
        caNumberExample = new TextBox();
        caNumberExample.setWidth("160px");
        caNumberExample.setEnabled(false);

        caNumCell1 = createNumCell(0, CA);
        caNumCell2 = createNumCell(0, CA);
        caNumCell3 = createNumCell(0, CA);
        caNumCell4 = createNumCell(1, CA);

        caDateNumbering = new KpiSwitcher();
        caDateNumbering.addValueChangeHandler(clickEvent -> drawCAExampleNumber());

        mcaPrefix = new TextBox();
        mcaPrefix.setWidth("70px");
        mcaNumberExample = new TextBox();
        mcaNumberExample.setWidth("160px");
        mcaNumberExample.setEnabled(false);

        mcaNumCell1 = createNumCell(0, MCA);
        mcaNumCell2 = createNumCell(0, MCA);
        mcaNumCell3 = createNumCell(0, MCA);
        mcaNumCell4 = createNumCell(1, MCA);

        mcaDateNumbering = new KpiSwitcher();
        mcaDateNumbering.addValueChangeHandler(clickEvent -> drawMCAExampleNumber());

        eosPrefix = new TextBox();
        eosPrefix.setWidth("70px");
        eosNumberExample = new TextBox();
        eosNumberExample.setWidth("160px");
        eosNumberExample.setEnabled(false);

        eosNumCell1 = createNumCell(0, EOS);
        eosNumCell2 = createNumCell(0, EOS);
        eosNumCell3 = createNumCell(0, EOS);
        eosNumCell4 = createNumCell(1, EOS);

        eosDateNumbering = new KpiSwitcher();
        eosDateNumbering.addValueChangeHandler(clickEvent -> drawEOSExampleNumber());


        //CashAdvance
        HorizontalPanel cellPRPanel = new HorizontalPanel();
        HorizontalPanel datePRPanel = new HorizontalPanel();
        datePRPanel.setSpacing(2);
        datePRPanel.add(new Label(wfmStrings.date()));
        datePRPanel.add(caDateNumbering);
        cellPRPanel.add(caNumCell1);
        cellPRPanel.add(caNumCell2);
        cellPRPanel.add(caNumCell3);
        cellPRPanel.add(caNumCell4);
        settingsTable.setWidget(0, 0, new HTML(wfmStrings.cashAdvance() + ":"));
        settingsTable.setWidget(0, 1, caPrefix);
        settingsTable.setWidget(0, 2, datePRPanel);
        settingsTable.setWidget(0, 3, cellPRPanel);
        settingsTable.setWidget(0, 4, caNumberExample);
        settingsTable.getColumnFormatter().setWidth(1, "10px");

        HorizontalPanel eosCellPRPanel = new HorizontalPanel();
        HorizontalPanel eosDatePRPanel = new HorizontalPanel();
        eosDatePRPanel.setSpacing(2);
        eosDatePRPanel.add(new Label(wfmStrings.date()));
        eosDatePRPanel.add(eosDateNumbering);
        eosCellPRPanel.add(eosNumCell1);
        eosCellPRPanel.add(eosNumCell2);
        eosCellPRPanel.add(eosNumCell3);
        eosCellPRPanel.add(eosNumCell4);
        settingsTable.setWidget(1, 0, new HTML(payrollStrings.endOfService() + ":"));
        settingsTable.setWidget(1, 1, eosPrefix);
        settingsTable.setWidget(1, 2, eosDatePRPanel);
        settingsTable.setWidget(1, 3, eosCellPRPanel);
        settingsTable.setWidget(1, 4, eosNumberExample);
        settingsTable.getColumnFormatter().setWidth(1, "10px");

        HorizontalPanel mcaCellPRPanel = new HorizontalPanel();
        HorizontalPanel mcaDatePRPanel = new HorizontalPanel();
        mcaDatePRPanel.setSpacing(2);
        mcaDatePRPanel.add(new Label(wfmStrings.date()));
        mcaDatePRPanel.add(mcaDateNumbering);
        mcaCellPRPanel.add(mcaNumCell1);
        mcaCellPRPanel.add(mcaNumCell2);
        mcaCellPRPanel.add(mcaNumCell3);
        mcaCellPRPanel.add(mcaNumCell4);
        settingsTable.setWidget(1, 0, new HTML(wfmStrings.multiCashAdvance() + ":"));
        settingsTable.setWidget(1, 1, mcaPrefix);
        settingsTable.setWidget(1, 2, mcaDatePRPanel);
        settingsTable.setWidget(1, 3, mcaCellPRPanel);
        settingsTable.setWidget(1, 4, mcaNumberExample);
        settingsTable.getColumnFormatter().setWidth(1, "10px");

        drawCAExampleNumber();
        drawEOSExampleNumber();
        drawMCAExampleNumber();
        layoutBox.addToContent(settingsTable);
    }

    private void drawCAExampleNumber() {
        String example = "";
        example += getNumCellValue(caNumCell1);
        example += getNumCellValue(caNumCell2);
        example += getNumCellValue(caNumCell3);
        example += getNumCellValue(caNumCell4);
        if (caDateNumbering.getValue()) {
            example += "-" + dateFormat.format(new Date());
        }
        caNumberExample.setText(example);
    }

    private void drawMCAExampleNumber() {
        String example = "";
        example += getNumCellValue(mcaNumCell1);
        example += getNumCellValue(mcaNumCell2);
        example += getNumCellValue(mcaNumCell3);
        example += getNumCellValue(mcaNumCell4);
        if (mcaDateNumbering.getValue()) {
            example += "-" + dateFormat.format(new Date());
        }
        mcaNumberExample.setText(example);
    }

    private void drawEOSExampleNumber() {
        String example = "";
        example += getNumCellValue(eosNumCell1);
        example += getNumCellValue(eosNumCell2);
        example += getNumCellValue(eosNumCell3);
        example += getNumCellValue(eosNumCell4);
        if (eosDateNumbering.getValue()) {
            example += "-" + dateFormat.format(new Date());
        }
        eosNumberExample.setText(example);
    }

    private String getNumCellValue(TextBox txtBox) {
        if (txtBox.getText().equals("")) {
            return "0";
        } else {
            return txtBox.getText();
        }
    }

    private void parseAndSetData(PMNumberingSettings numberingSettings) {
        if (numberingSettings != null) {
            parseAndSetCashAdvanceNumberingData(numberingSettings, CA);
            parseAndSetCashAdvanceNumberingData(numberingSettings, EOS);
            parseAndSetCashAdvanceNumberingData(numberingSettings, MCA);
        } else {
            setDefaultNumber(CA);
            setDefaultNumber(EOS);
            setDefaultNumber(MCA);
        }
    }

    private void setDefaultNumber(int type) {
        if (type == CA) {
            caPrefix.setText("CA");
            caNumCell1.setText("0");
            caNumCell2.setText("0");
            caNumCell3.setText("0");
            caNumCell4.setText("1");
        }
        if (type == MCA) {
            caPrefix.setText("MCA");
            caNumCell1.setText("0");
            caNumCell2.setText("0");
            caNumCell3.setText("0");
            caNumCell4.setText("1");
        }

        if (type == EOS) {
            eosPrefix.setText("EOS");
            eosNumCell1.setText("0");
            eosNumCell2.setText("0");
            eosNumCell3.setText("0");
            eosNumCell4.setText("1");
        }
    }

    private void parseAndSetCashAdvanceNumberingData(PMNumberingSettings numberingSettings, int type) {
        String numFormat;
        String fourDigitNum = "";
        if (type == CA) {
            if (numberingSettings.getCaNumberingFormat() != null && !"".equals(numberingSettings.getCaNumberingFormat())) {
                numFormat = numberingSettings.getCaNumberingFormat();
                String[] numFormatParts = numFormat.split(SPLITTER);
                String[] numFormatDateParts = numFormatParts[1].split("-");
                caPrefix.setText(numFormatParts[0]);
                if (numFormat.contains(DATE)) {
                    fourDigitNum = numFormatDateParts[0];
                    caDateNumbering.setValue(true);
                } else {
                    fourDigitNum = numFormatParts[1];
                }
                char[] numbers = fourDigitNum.toCharArray();
                caNumCell1.setText(String.valueOf(numbers[0]));
                caNumCell2.setText(String.valueOf(numbers[1]));
                caNumCell3.setText(String.valueOf(numbers[2]));
                caNumCell4.setText(String.valueOf(numbers[3]));
                drawCAExampleNumber();
            } else {
                setDefaultNumber(CA);
            }
        } else if (type == MCA) {
            if (numberingSettings.getMcaNumberingFormat() != null && !"".equals(numberingSettings.getMcaNumberingFormat())) {
                numFormat = numberingSettings.getMcaNumberingFormat();
                String[] numFormatParts = numFormat.split(SPLITTER);
                String[] numFormatDateParts = numFormatParts[1].split("-");
                mcaPrefix.setText(numFormatParts[0]);
                if (numFormat.contains(DATE)) {
                    fourDigitNum = numFormatDateParts[0];
                    mcaDateNumbering.setValue(true);
                } else {
                    fourDigitNum = numFormatParts[1];
                }
                char[] numbers = fourDigitNum.toCharArray();
                mcaNumCell1.setText(String.valueOf(numbers[0]));
                mcaNumCell2.setText(String.valueOf(numbers[1]));
                mcaNumCell3.setText(String.valueOf(numbers[2]));
                mcaNumCell4.setText(String.valueOf(numbers[3]));
                drawMCAExampleNumber();
            } else {
                setDefaultNumber(MCA);
            }
        } else if (type == EOS) {
            if (numberingSettings.getSaNumberingFormat() != null && !"".equals(numberingSettings.getSaNumberingFormat())) {
                numFormat = numberingSettings.getSaNumberingFormat();
                String[] numFormatParts = numFormat.split(SPLITTER);
                String[] numFormatDateParts = numFormatParts[1].split("-");
                eosPrefix.setText(numFormatParts[0]);
                if (numFormat.contains(DATE)) {
                    fourDigitNum = numFormatDateParts[0];
                    eosDateNumbering.setValue(true);
                } else {
                    fourDigitNum = numFormatParts[1];
                }
                char[] numbers = fourDigitNum.toCharArray();
                eosNumCell1.setText(String.valueOf(numbers[0]));
                eosNumCell2.setText(String.valueOf(numbers[1]));
                eosNumCell3.setText(String.valueOf(numbers[2]));
                eosNumCell4.setText(String.valueOf(numbers[3]));
                drawEOSExampleNumber();
            } else {
                setDefaultNumber(EOS);
            }
        }
    }

    protected boolean save() {
        if (settings == null) {
            settings = new PMNumberingSettings();
        }
        settings.setCaNumberingFormat(AccountingUtils.getNumberingFormat(caPrefix, caNumCell1, caNumCell2, caNumCell3, caNumCell4, caDateNumbering.getValue()));
        settings.setMcaNumberingFormat(AccountingUtils.getNumberingFormat(mcaPrefix, mcaNumCell1, mcaNumCell2, mcaNumCell3, mcaNumCell4, mcaDateNumbering.getValue()));
        settings.setSaNumberingFormat(AccountingUtils.getNumberingFormat(eosPrefix, eosNumCell1, eosNumCell2, eosNumCell3, eosNumCell4, eosDateNumbering.getValue()));
        LoadingPanel.loading(true);
        ProfileService.App.get().savePayrollNumberingSettings(settings, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                throwable.printStackTrace();
            }

            @Override
            public void success(Integer id) {
                LoadingPanel.loading(false);
                settings.setObjectID(id);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.payrollSettings()), Info.Type.INFO);
            }
        });
        return true;
    }

    private TextBox createNumCell(int number, final int type) {
        final TextBox cell = new TextBox();
        cell.setWidth("48px");
        cell.setMaxLength(1);
        cell.setText(String.valueOf(number));
        Validation.addNumericKeyboardListener(cell);
        cell.addKeyUpHandler(keyUpEvent -> {
            if (type == CA) {
                drawCAExampleNumber();
            } else if (type == EOS) {
                drawEOSExampleNumber();
            } else if (type == MCA) {
                drawMCAExampleNumber();
            }
        });
        cell.addFocusHandler(focusHandler -> {
            if (cell.getText().equals("")) {
                cell.setText("0");
            }
        });
        return cell;
    }


    public HTMLPanel getRootElement() {
        return rootElement;
    }
}
