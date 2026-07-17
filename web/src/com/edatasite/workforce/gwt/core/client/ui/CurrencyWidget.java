package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroupAppend;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Shohruh on 01 Mar 2017.
 */
public class CurrencyWidget extends Composite {
    private final CurrencyServiceAsync currencyService = CurrencyService.App.get();

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private DataListBox currencyListBox;
    private DatePicker datePicker;
    private final ArrayList<Command> listeners = new ArrayList<>();
    private Command onloadListener;
    private Validator validator;
    private boolean enabled = true;
    private boolean showExchangePopUpAlways = false;
    /**
     * equals to !convertInvoice && !editView && !avoidRateLoading
     */
    private boolean autoReload;
    private BigDecimal exchangeRate;
    private CurrencyItem baseCurrency;
    private Integer accountCurrencyID;
    private HashMap<Integer, CurrencyItem> map;

    private String baseCurrencyName;
    private String exRate;
    private Div pnlExchangeRate;
    private Anchor changeAnchor;
    private boolean isCurrencyChanged = false;
    private Span infoContent;
    private Panel loadingContainer;
    private boolean isCustomForm;
    private InputGroupAppend iga;
    private Icon info;
    private ModalCompleteListener modalCompleteListener;
    private Double exRateInSum;
    private boolean accountIsForeign = false;
    private boolean oneToOneMessageShowed = false;

    public CurrencyWidget() {
        initialize();
    }

    public CurrencyWidget(boolean autoReload) {
        this();
        setAutoReload(autoReload);
    }

    public CurrencyWidget(String customForm, boolean autoReload) {
        if (customForm != null && customForm.equals("CustomForm")) {
            isCustomForm = true;
            setAutoReload(autoReload);
        }
        initialize();
    }

    public CurrencyWidget(Integer currencyID, BigDecimal exchangeRate) {
        this();
        setCurrency(currencyID, exchangeRate);
    }

    private void initialize() {
        map = new HashMap<>();


        exchangeRate = BigDecimal.ONE;
        exRate = formatExRate(exchangeRate);

        pnlExchangeRate = new Div();
        infoContent = new Span();
        changeAnchor = new Anchor();
        changeAnchor.setText(wfmStrings.change());
        currencyListBox = new DataListBox();
        currencyListBox.setWithoutNullLabel(true);
        currencyListBox.setStyleName("form-control");

        initializeHandlers();
        loadData();

        InputGroup wrapper = new InputGroup();
        wrapper.addStyleName("currencyList__wrap");
        wrapper.add(currencyListBox);
        info = new Icon();
        info.addStyleName("ficon--info");
        iga = new InputGroupAppend(info);
        wrapper.add(iga);
        initWidget(wrapper);
        setExchangeratePanelContent();
        if (isCustomForm) {
            info.addClickHandler(click -> {

                final KpiModal modal = new KpiModal();
                modal.setWidth(250);
                modal.setTitle(wfmStrings.exchangeRate());
                modal.add(pnlExchangeRate);
                modal.setCloseButton(true);
                modal.open();

            });
        } else {
            new KpiToolTip(iga, pnlExchangeRate);
        }

        if (getElement() != null) {
            getElement().setAttribute("autocomplete", "off");
        }

    }

    private void setExchangeratePanelContent() {
        pnlExchangeRate.clear();
        if (!Utils.isNullOrEmpty(baseCurrencyName) && !Utils.isNullOrEmpty(exRate)) {
            infoContent.setText("1 " + baseCurrencyName + " = " + exRate);
        } else {
            infoContent.setText(wfmStrings.notAvailable());
        }
        if (exRate != null) {
            int lastIndexOf = exRate.lastIndexOf(" ");
            String exRateWithOutCurrency = lastIndexOf != -1 ? exRate.substring(0, exRate.lastIndexOf(" ")) : exRate;
            getElement().setAttribute("currency-value", exRateWithOutCurrency);
        }
        pnlExchangeRate.add(infoContent);

        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_CURRENCY_RATE_EDIT)) {

            if (baseCurrency != null && !baseCurrency.equals(currencyListBox.getSelectedItem())) {
                Div anchDiv = new Div();
                anchDiv.add(changeAnchor);
                pnlExchangeRate.add(anchDiv);

                if (isCurrencyChanged && (isEnabled() || isShowExchangePopUpAlways())) {
                    if (Utils.hasPermission(PermissionConstants.ACCOUNTING_CURRENCY_RATE_EDIT)) {
                        oneToOneMessageShowed=false;
                        new CurrencyViewPopup(currencyListBox.getSelectedId(false), (datePicker != null && datePicker.getDate() != null) ? datePicker.getDate() : new Date(), o -> loadExchangeRate(true));
                    }
                    isCurrencyChanged = false;
                }
            }
        }

        if (pnlExchangeRate.getWidgetCount() == 0) {
            pnlExchangeRate.setVisible(false);
        } else {
            pnlExchangeRate.setVisible(true);
        }
    }

    private void initializeHandlers() {
        currencyListBox.addValueChangeHandler(event -> {
            if (Utils.hasGenericAccess(GenericSettingsEnum.MULTICURRENCY_ENABLED) && !validateMultiCurrencyRequirements()) {
                return;
            }
            isCurrencyChanged = true;
            oneToOneMessageShowed=true;
            loadExchangeRate(true);
        });

        changeAnchor.addClickHandler(clickEvent -> {
            if (isEnabled() || isShowExchangePopUpAlways()) {
                if (!currencyListBox.getSelectedItem().equals(baseCurrency) && Utils.hasPermission(PermissionConstants.ACCOUNTING_CURRENCY_RATE_EDIT)) {
                    new CurrencyViewPopup(currencyListBox.getSelectedId(false), (datePicker != null && datePicker.getDate() != null) ? datePicker.getDate() : new Date(), o -> loadExchangeRate(true));
                }
            }
        });
    }

    private void loadData() {
        currencyService.getCurrencies(true, new AbstractAsyncCallback<CurrencyItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
                currencyService.getBaseCurrency(new AbstractAsyncCallback<CurrencyItem>() {
                    @Override
                    public void onSuccess(CurrencyItem result) {
                        currencyListBox.addListItem(new SelectItem(result.getId(), result.getFullName()));
                        baseCurrency = result;
                        baseCurrencyName = result.getName();
                        if (!isCustomForm) {
                            currencyListBox.setSelectedItem(baseCurrency);
                        }

                        if (onloadListener != null) {
                            onloadListener.execute();
                        }
                        setExchangeratePanelContent();
                    }
                });
            }

            @Override
            public void onSuccess(CurrencyItem[] result) {
                setCurrencies(result);
            }
        });
    }

    private void loadAccountCurrencyDataWithBase() {
        currencyService.getAccountCurrencyWithBase(accountCurrencyID, new AbstractAsyncCallback<CurrencyItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
                currencyService.getBaseCurrency(new AbstractAsyncCallback<CurrencyItem>() {
                    @Override
                    public void onSuccess(CurrencyItem result) {
                        currencyListBox.addListItem(new SelectItem(result.getId(), result.getFullName()));
                        baseCurrency = result;
                        baseCurrencyName = result.getName();
                        if (!isCustomForm) {
                            currencyListBox.setSelectedItem(baseCurrency);
                        }

                        if (onloadListener != null) {
                            onloadListener.execute();
                        }
                        setExchangeratePanelContent();
                    }
                });
            }

            @Override
            public void onSuccess(CurrencyItem[] result) {
                setCurrencies(result);
            }
        });
    }

    public void setCurrencies(CurrencyItem[] items) {
        setAccountIsForeign(false);
        if (items != null && items.length > 1) {
            setAccountIsForeign(true);
        }
        regenerateCurrencies(items);
        currencyListBox.setItems(items);
        baseCurrency = items[0];
        baseCurrencyName = items[0].getName();
        if (!isCustomForm) {
            currencyListBox.setSelectedItem(baseCurrency);
        }

        if (onloadListener != null) {
            onloadListener.execute();
        }
        setExchangeratePanelContent();
    }

    public boolean isAccountIsForeign() {
        return accountIsForeign;
    }

    public void setAccountIsForeign(boolean accountIsForeign) {
        this.accountIsForeign = accountIsForeign;
    }

    private SelectItem[] regenerateCurrencies(CurrencyItem[] result) {
        ArrayList<SelectItem> cList = new ArrayList<>();

        for (CurrencyItem item : result) {
            map.put(item.getId(), item);
            cList.add(new SelectItem(item.getId(), item.getFullName()));
        }

        return cList.toArray(new SelectItem[]{});
    }

    private void loadExchangeRate(boolean forceReload) {

        if (forceReload || isAutoReload()) {
            DateNonConvertable dateNC = new DateNonConvertable((datePicker != null && datePicker.getDate() != null) ? datePicker.getDate() : new Date());
            LoadingPanel.loading(true, loadingContainer != null ? loadingContainer : MainLayout.get().getContentBody());
            currencyService.getCurrencyRateByDate(currencyListBox.getSelectedId(), dateNC, new AbstractAsyncCallback<CurrencyListItem>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    exRate = null;
                    exchangeRate = BigDecimal.ONE;
                    setExRateInSum(0d);
                }

                @Override
                public void success(CurrencyListItem result) {
                    LoadingPanel.loading(false);
                    applyExchangeRate(new BigDecimal(result.getExchangeRate()));
                    setExRateInSum(result.getExRateInSum());
                    notifyCompleteListener();
//                    if (currencyLoaded != null) {
//                        currencyLoaded.execute();
//                    }
                }
            });
        }
    }

    public void applyExchangeRate(BigDecimal exchangeRate) {
        // check if the exchange rate is 1:1
        if (oneToOneMessageShowed == false && !baseCurrency.getId().equals(getCurrencyID()) && exchangeRate.compareTo(BigDecimal.ONE) == 0) {
            oneToOneMessageShowed=true;
            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK);
            messageBox.setTitle(wfmStrings.warning());
            messageBox.setMessage(wfmStrings.currencyRateOneToOne());
            messageBox.open();
        }

        this.exchangeRate = exchangeRate.setScale(Utils.getAccountingCustomExRateScale() != null ? Utils.getAccountingCustomExRateScale() : 5, BigDecimal.ROUND_HALF_UP);
        exRate = formatExRate(exchangeRate) + (map != null && map.get(currencyListBox.getSelectedId()) != null ? " " + map.get(currencyListBox.getSelectedId()).getName() : "");

        // addListener() -> execute
        for (Command listener : listeners) {
            listener.execute();
        }
        setExchangeratePanelContent();
    }

    public String formatExRate(BigDecimal bigDecimal) {
        int scale = Utils.getAccountingCustomExRateScale() != null ? Utils.getAccountingCustomExRateScale() : 4;
        StringBuilder format = new StringBuilder(",##0.");
        for (int i = 0; i < scale; i++) {
            format.append("0");
        }
        return NumberFormat.getFormat(format.toString()).format(bigDecimal.setScale(scale, RoundingMode.HALF_UP).doubleValue());
    }

    public void setCurrency(Integer currencyID, boolean... restricted) {
        setCurrency(currencyID, null, restricted);
    }

    public void setCurrency(SelectItem selectItem) {
        isCustomForm = true;
        currencyListBox.setSelected(selectItem);
        setCurrency(selectItem.getId());
    }

    public void setCurrency(Integer currencyID, BigDecimal exchangeRate, boolean... restricted) {

        Scheduler.get().scheduleFixedDelay(() -> {
            if (currencyListBox.getItems() == null || currencyListBox.getItems().length == 0) {
                return true;
            }

            boolean isChanged = currencyListBox.getSelectedId() == null || !currencyListBox.getSelectedId().equals(currencyID);

            if (currencyID != null) {
                currencyListBox.setSelected(currencyID);

                if (restricted != null && restricted.length > 0 && restricted[0]) {
                    CurrencyItem item = (CurrencyItem) currencyListBox.getSelectedItem();
                    currencyListBox.removeListItems();
                    currencyListBox.addListItem(item);
                    currencyListBox.setSelected(item);
                }
            }
            if (exchangeRate != null) {
                applyExchangeRate(exchangeRate);
            } else if (currencyListBox.getSelectedId() != null) {
                loadExchangeRate(isChanged);
            }

            return false;
        }, 100);
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate != null ? exchangeRate : BigDecimal.ONE;
    }

    public CurrencyItem getCurrency() {
        return (CurrencyItem) currencyListBox.getSelectedItem();
    }

    public Integer getCurrencyID() {
        return currencyListBox.getSelectedId();
    }

    public String getCurrencyName() {
        return currencyListBox.getSelectedItem() != null ? currencyListBox.getSelectedItem().getName() : "";
    }

    public CurrencyItem getBaseCurrency() {
        return baseCurrency;
    }

    public String getBaseCurrencyName() {
        return baseCurrencyName;
    }

    public void addListener(Command listener) {
        listeners.add(listener);
    }

    public void setOnloadListener(Command onloadListener) {
        this.onloadListener = onloadListener;
    }

    public boolean isAutoReload() {
        return autoReload;
    }

    public void setAutoReload(boolean autoReload) {
        this.autoReload = autoReload;
    }

    public void setDatePicker(DatePicker datePicker) {
        setDatePicker(datePicker, false);
    }

    public void setDatePicker(DatePicker datePicker, final boolean forceLoad) {
        this.datePicker = datePicker;
        if (this.datePicker != null) {
            this.datePicker.addChangeHandler(changeEvent -> loadExchangeRate(forceLoad));
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        currencyListBox.setEnabled(enabled);
        setAutoReload(enabled);
    }

    public void setListBoxEnabled(boolean enabled) {
        currencyListBox.setEnabled(enabled);
    }

    private boolean isEnabled() {
        return enabled;
    }

    public void clear() {
        currencyListBox.clearSelected();
    }

    public void setShowOnlyAccountCurrenyWithBase(int accountCurrencyId) {
        this.accountCurrencyID =accountCurrencyId;
        loadAccountCurrencyDataWithBase();
    }

    public interface Validator {
        boolean isRequirementsValid();
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    private boolean validateMultiCurrencyRequirements() {
        return validator == null || validator.isRequirementsValid();
    }

//    public GBoxItem getGBoxItem() {
//        if (item == null) {
//            item = new GBoxItem(invoiceStrings.currency(), this);
//            item.addStyleName("invoice__currency");
//        }
//        return item;
//    }

    public void setLoadingContainer(Panel container) {
        this.loadingContainer = container;
    }

    public DataListBox getCurrencyListBox() {
        return this.currencyListBox;
    }

    public boolean isShowExchangePopUpAlways() {
        return this.showExchangePopUpAlways;
    }

    public void setShowExchangePopUpAlways(final boolean showExchangePopUpAlways) {
        this.showExchangePopUpAlways = showExchangePopUpAlways;
    }

    public Double getExRateInSum() {
        return exRateInSum;
    }

    public void setExRateInSum(Double exRateInSum) {
        this.exRateInSum = exRateInSum;
    }

    public void setModalCompleteListener(ModalCompleteListener listener) {
        this.modalCompleteListener = listener;
    }

    private void notifyCompleteListener() {
        if (modalCompleteListener != null) {
            modalCompleteListener.onModalComplete();
        }
    }

    public interface ModalCompleteListener {
        void onModalComplete();
    }
}
