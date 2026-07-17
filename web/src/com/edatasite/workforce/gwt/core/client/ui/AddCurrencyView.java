package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutInterface;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.SmartCurrencyLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.view.ObjectCommand;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: Shohruh
 * Date: 22.01.16
 * Time: 12:40:24
 * To change this template use File | Settings | File Templates.
 */
public class AddCurrencyView extends CustomForm implements Constants {

    private final CurrencyServiceAsync currencyService = CurrencyService.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();


    private Integer objectID;
    private final ObjectCommand command;
    private final Command closeCommand;
    private final Date date;

    private final String addCurrencyView = "add_currency_view_";
    private final String XEcom = "Currencylayer.com";

    private SmartCurrencyLookUp currencyLookUp;
    private TextBox exRateTxtBox;
    private TextBox swichtExRateTxtBox;

    private VerticalPanelDiv xePanel;
    private VerticalPanelDiv exPanel;
    private VerticalPanelDiv mPanel;
    private VerticalPanelDiv bottomPanel;

    //Radio Buttons
    private KpiRadioButton xeRadioButton;
    private KpiRadioButton exRadioButton;
    private KpiRadioButton mRadioButton;

    //CheckBox for fixed rate
    private KpiCheckBox checkBox;

    private WfmButton2 updateButton;

    private Double xeRate;
    private Double exRate;
    private Double rate;
    private boolean fromService = false;
    private boolean editView = false;

    public AddCurrencyView(Integer objectID, Date date, ObjectCommand command, Command closeCommand) {
        super(objectID == null ? "currencyadd" : "currencyedit", objectID == null ? wfmStrings.addCurrency() : wfmStrings.setExchangeRate());
        this.objectID = objectID;
        this.date = date;
        this.command = command;
        this.closeCommand = closeCommand;
        editView = objectID != null;
    }

    protected void addPanel(LayoutInterface layoutInterface) {
        panel = new HTMLPanel(replaceControls(layoutInterface.getLayout()));
    }

    protected Widget onInitialize() {
        super.onInitialize();

        initInternal();
        onCurrencyChange();

        return null;
    }

    private void initInternal() {
        currencyLookUp = new SmartCurrencyLookUp(false);
        currencyLookUp.ensureDebugId(addCurrencyView + "currencyLookUp");
        currencyLookUp.setWidth("100%");

        currencyLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> {
            if (currencyLookUp.getSelectedItemID() != null) {
                objectID = currencyLookUp.getSelectedItemID();
                onCurrencyChange();
            }
        });

        xePanel = new VerticalPanelDiv();
        exPanel = new VerticalPanelDiv();
        mPanel = new VerticalPanelDiv();
        bottomPanel = new VerticalPanelDiv();


        if (!editView) {
            addField(CURRENCY, currencyLookUp, wfmStrings.selectCurrency());
        }
        addField("xePanel", xePanel);
        addField("exPanel", exPanel);
        addField("mPanel", mPanel);
        addField("bottomPanel", bottomPanel);
        show();
    }

    private void drawForm(CurrencyListItem item) {
        xeRate = item.getExchangeRate(true);
        exRate = item.getExchangeRate(false);

        xeRadioButton = new KpiRadioButton("ratesGroup");
        exRadioButton = new KpiRadioButton("ratesGroup");
        mRadioButton = new KpiRadioButton("ratesGroup");


        xeRadioButton.setText(XEcom + " " + wfmStrings.exchangeRate() + " <small class='ml-3'>(" + item.getBaseCurrency().getName() + " = " + formatExRate(xeRate) + " " + item.getCurrency().getName() + ")</small>");
        Div panel = new Div();

        xePanel.clear();
        xePanel.add(xeRadioButton);
        xePanel.add(new HTML("<p class='mt-2' style=\"margin-left: 21px;\"><small>" + wfmStrings.midMarketRate() + " " + DateUtils.getDateAndTimeFormatShort1(item.getXeUpdateTime() != null ? item.getXeUpdateTime().getNonConvertedDate() : new Date()) + "</small></p>"));

        if (exRateTxtBox == null) {
            exRateTxtBox = new TextBox();
        }
        if (swichtExRateTxtBox == null) {
            swichtExRateTxtBox = new TextBox();
        }
        if (editView) {
            if (exRate != null && !item.isFixedRate()) {
                Div usPanel = new Div();
                exRadioButton.setText(wfmStrings.userDefinedRate() + " <small class='ml-3'>(" + item.getBaseCurrency().getName() + " = " + accountSettingsFormatExchangeRate(exRate) + " " + item.getCurrency().getName() + ")</small>");
                usPanel.add(exRadioButton);

                exPanel.clear();
                exPanel.add(exRadioButton);
                exPanel.add(new HTML("<p class='mt-2' style=\"margin-left: 21px;\"><small>" + wfmStrings.exRateProvided() + " " + DateUtils.getDateAndTimeFormatShort1(item.getDate().getNonConvertedDate()) + "</small></p>"));
            }

            exRateTxtBox.ensureDebugId(addCurrencyView + "exchangeRateTxtBox");
            exRateTxtBox.setWidth("200px");
            Validation.addNumericKeyboardListener(exRateTxtBox, Utils.getAccountingCustomExRateScale() != null ? Utils.getAccountingCustomExRateScale() : 4);
            exRateTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

            swichtExRateTxtBox.ensureDebugId(addCurrencyView + "exchangeRateTxtBox");
            swichtExRateTxtBox.setWidth("200px");
            Validation.addNumericKeyboardListener(swichtExRateTxtBox, Utils.getAccountingCustomExRateScale() != null ? Utils.getAccountingCustomExRateScale() : 4);
            swichtExRateTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

            HorizontalPanelDiv manualPanel = new HorizontalPanelDiv();
            mRadioButton.setText(wfmStrings.insertRateManually());
            manualPanel.add(new HTML(item.getBaseCurrency().getName() + " = "));
            manualPanel.add(exRateTxtBox);
            manualPanel.add(new HTML(" " + item.getCurrency().getName()));
            manualPanel.getElement().getStyle().setMarginLeft(21, Style.Unit.PX);
            manualPanel.addStyleName("mt-2 mb-1");

            checkBox = new KpiCheckBox("<small>" + wfmStrings.applyToAllTransactions() + "</small>");
            checkBox.ensureDebugId(addCurrencyView + "checkBox");
            checkBox.getElement().getStyle().setMarginLeft(21, Style.Unit.PX);
            checkBox.setValue(item.isFixedRate());
            checkBox.setVisible(item.isFixedRate());

            MaterialLink linkSwitch = new MaterialLink();
            linkSwitch.setText("Switch");

            Div linkDiv = new Div();
            linkDiv.add(linkSwitch);
            linkDiv.setTextAlign(TextAlign.CENTER);

            mPanel.clear();
            mPanel.add(mRadioButton);
            mPanel.add(linkDiv);
            mPanel.add(manualPanel);
            mPanel.add(checkBox);
            checkBox.getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);
            updateButton = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);

            linkSwitch.addClickHandler(event -> {
                HorizontalPanelDiv switchInnerPanel = new HorizontalPanelDiv();
                switchInnerPanel.add(new HTML(item.getCurrency().getName() + " = "));
                BigDecimal exchangeRateValue = parsePriceToBigDecimal(exRateTxtBox.getText());
                String exchangeRateTextValue = "";
                if (!Objects.equals(exchangeRateValue, BigDecimal.ZERO)) {
                    Integer calculationScale = Utils.getAccountingCustomExRateScale() != null ? Utils.getAccountingCustomExRateScale() : 4;
                    BigDecimal foreignCurExRateValue = BigDecimal.ONE.divide(exchangeRateValue, calculationScale, BigDecimal.ROUND_HALF_UP);
                    exchangeRateTextValue = accountSettingsFormatExchangeRate(foreignCurExRateValue);
                }
                swichtExRateTxtBox.setText(exchangeRateTextValue);
                switchInnerPanel.add(swichtExRateTxtBox);
                switchInnerPanel.add(new HTML(" " + item.getBaseCurrency().getName()));
                switchInnerPanel.getElement().getStyle().setMarginLeft(21, Style.Unit.PX);
                switchInnerPanel.addStyleName("mt-2 mb-1");

                mPanel.clear();
                mPanel.add(mRadioButton);
                mPanel.add(switchInnerPanel);
                mPanel.add(checkBox);
                Div updateButtonDiv = new Div();
                updateButtonDiv.add(updateButton);
                updateButtonDiv.setTextAlign(TextAlign.CENTER);
                mPanel.add(updateButtonDiv);
                checkBox.getElement().getStyle().setVisibility(Style.Visibility.VISIBLE);
            });

            updateButton.addClickHandler(event -> {
                BigDecimal swtichExchangeRateValue = parsePriceToBigDecimal(swichtExRateTxtBox.getText());
                if (!Objects.equals(swtichExchangeRateValue, BigDecimal.ZERO)) {
                    Integer calculationScale = Utils.getAccountingCustomExRateScale() != null ? Utils.getAccountingCustomExRateScale() : 4;
                    BigDecimal foreignCurExRateValue = BigDecimal.ONE.divide(swtichExchangeRateValue, calculationScale, BigDecimal.ROUND_HALF_UP);
                    String swtichExchangeRateTextValue = accountSettingsFormatExchangeRate(foreignCurExRateValue);
                    exRateTxtBox.setText(swtichExchangeRateTextValue);
                    if (!Objects.equals(foreignCurExRateValue, BigDecimal.ZERO)) {
                        HorizontalPanelDiv exchangeRatePanel = new HorizontalPanelDiv();
                        exchangeRatePanel.add(new HTML("<p class='mt-2' style=\"margin-left: 21px;\">" + "1 " + item.getBaseCurrency().getName() + " = " + swtichExchangeRateTextValue + " " + item.getCurrency().getName() + "</p>"));
                        bottomPanel.clear();
                        bottomPanel.add(exchangeRatePanel);
                    }
                }

            });
        }

        if (fromService) {
            xeRadioButton.setValue(true);
            rate = xeRate;
        } else if (!item.isFixedRate()) {
            exRadioButton.setValue(true);
            rate = exRate;
        } else {
            mRadioButton.setValue(true);
            rate = exRate;
            exRateTxtBox.setValue(accountSettingsFormatExchangeRate(rate));
        }
        initializeHandlers();
    }

    private void initializeHandlers() {
        exRateTxtBox.addClickHandler(clickEvent -> {
            mRadioButton.setValue(true);
            checkBox.getElement().getStyle().setVisibility(Style.Visibility.VISIBLE);
        });
        xeRadioButton.addValueChangeHandler(valueChangeEvent -> {
            if (xeRadioButton.getValue()) {
                rate = xeRate;
                checkBox.setValue(false);
                checkBox.getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);
            }
        });
        exRadioButton.addValueChangeHandler(valueChangeEvent -> {
            if (exRadioButton.getValue()) {
                rate = exRate;
                checkBox.setValue(false);
                checkBox.getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);
            }
        });
        mRadioButton.addValueChangeHandler(valueChangeEvent -> {
            if (mRadioButton.getValue()) {
                rate = exRate;
                checkBox.setValue(false);
                checkBox.getElement().getStyle().setVisibility(Style.Visibility.VISIBLE);
            }
        });
    }

    private void onCurrencyChange() {
        if (objectID == null) {
            return;
        }
        LoadingPanel.loading(true);
        currencyService.getCurrencyRateByDate(objectID, new DateNonConvertable(date), new AbstractAsyncCallback<CurrencyListItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(CurrencyListItem result) {
                LoadingPanel.loading(false);
                fromService = result.isFromService();
                drawForm(result);
            }
        });
    }

    @Override
    protected void addButtons() {
    }

    public boolean save() {
        if (validation()) {
            if (mRadioButton.getValue()) {
                rate = parsePriceToBigDecimal(exRateTxtBox.getText()).doubleValue();
            }

            LoadingPanel.loading(true);
            currencyService.createOrUpdateCurrency(getData(), new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    closeCommand.execute();
                }

                @Override
                public void success(Void result) {
                    LoadingPanel.loading(false);
                    command.execute(result);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EXCHANGE_RATE_ADDED, result, AddCurrencyView.this);
                    closeCommand.execute();
                }
            });
        }
        return false;
    }

    private boolean validation() {
        if (objectID == null) {
            Info.show(wfmStrings.pleaseSelectCurrency(), Info.Type.WARNING);
            return false;
        }
        if (mRadioButton.getValue() && parsePriceToBigDecimal(exRateTxtBox.getText()).compareTo(BigDecimal.ZERO) == 0) {
            Info.show(wfmStrings.rateCannotBeEmptyErrorMessage(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private CurrencyListItem getData() {
        CurrencyListItem item = new CurrencyListItem();
        item.setCurrency(new CurrencyItem(objectID, ""));
        item.setDate(new DateNonConvertable(date));
        item.setFromService(xeRadioButton.getValue());
        item.setExchangeRate(rate);
        if (editView) {
            item.setFixedRate(checkBox.getValue());
        } else {
            item.setFixedRate(true);
        }
        return item;
    }

    public String formatExRate(Double value) {
        return NumberFormat.getFormat(",##0.0000").format(value);
    }

    public String accountSettingsFormatExchangeRate(BigDecimal bigDecimal) {
        Integer customExRateScale = getExRateScale();
        NumberFormat exRateNumberFormat = getNumberFormat(customExRateScale);
        return exRateNumberFormat.format(bigDecimal.setScale(customExRateScale, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    public String accountSettingsFormatExchangeRate(Double value) {
        Integer customExRateScale = getExRateScale();
        NumberFormat exRateNumberFormat = getNumberFormat(customExRateScale);
        return exRateNumberFormat.format(value);
    }

    public static int getExRateScale() {
        if (Utils.getAccountingCustomExRateScale() != null) {
            return Utils.getAccountingCustomExRateScale();
        }
        return 4;
    }

    private static NumberFormat getNumberFormat(Integer productPrice) {
        if (productPrice == 0) {
            return NumberFormat.getFormat(",##0");
        } else {
            String s = ".";
            for (int i = 0; i < productPrice; i++) {
                s = s.concat("0");
            }
            return NumberFormat.getFormat(",##0" + s);
        }
    }

    public BigDecimal parsePriceToBigDecimal(String text) {
        if (text != null && text.length() > 0) {
            NumberFormat priceFormat = NumberFormat.getFormat(",##0.00000");
            return new BigDecimal(Utils.universalParse(priceFormat, text));
        }
        return BigDecimal.ZERO;
    }

    @Override
    protected void getDataToFillFields() {

    }

    @Override
    protected String getFormID() {
        return LayoutRPC.CURRENCY_FORM;
    }

    @Override
    protected String getFormType() {
        return objectID == null ? LayoutRPC.ADD : LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    public void asyncOnInitialize() {
        asyncOnInitialize(new AbstractAsyncCallback<Widget>() {
            public void failure(Throwable reason) {
                Info.show(wfmStrings.failedToDownloadCodeForThisWidget() + " (" + reason + ")", Info.Type.WARNING);
            }
        });
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
}
