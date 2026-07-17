package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountsByCategory;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 2/27/12
 * Time: 1:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class FixedAssetDisposeDialogBox extends KpiModal {
    public static FixedAssetItem fixedAssetItem;
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private WfmDropdown cashAccountDropdown;
    private TextBox cashAmountTxtBox;
    private CrmAccountLookUp crmAccountLookUp;
    private DatePicker disposalDateBox;
    private DatePicker justDateBox;
    private final Command reloadListListener;
    private Command viewCloseListener;

    public FixedAssetDisposeDialogBox(FixedAssetItem faItem, Command reloadListListener) {
        fixedAssetItem = faItem;
        this.reloadListListener = reloadListListener;
        initialize();
        setWidth("550");
    }

    private void initialize() {
        setTitle(wfmStrings.howWouldYouLikeDispose());
        cashAccountDropdown = new WfmDropdown();
        cashAmountTxtBox = new TextBox();
        crmAccountLookUp = new CrmAccountLookUp(CrmAccountLookUp.CUSTOMER, true);
        disposalDateBox = new DatePicker();
        disposalDateBox.setDate(DateUtil.resetTime(new Date()));

        justDateBox = new DatePicker(true);
        justDateBox.setDate(DateUtil.resetTime(new Date()));

        cashAmountTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(cashAmountTxtBox, 2);
        cashAmountTxtBox.setText(AccountingUtils.getZero());

        AccountingService.App.get().getAccountsForPayment(new AbstractAsyncCallback<AccountsByCategory>() {
            public void success(AccountsByCategory accByCat) {
                cashAccountDropdown.addItems(wfmStrings.assets(), accByCat.getAssets());
                cashAccountDropdown.addItems(wfmStrings.liabilities(), accByCat.getLiabilities());
                cashAccountDropdown.addItems(wfmStrings.equity(), accByCat.getEquity());
                cashAccountDropdown.addItems(wfmStrings.revenue(), accByCat.getRevenue());
                cashAccountDropdown.addItems(wfmStrings.expenses(), accByCat.getExpenses());
            }
        });


        KpiRadioButton cashRButton = new KpiRadioButton("disposeType", wfmStrings.type());
        KpiRadioButton accountsReceivableRButton = new KpiRadioButton("disposeType", wfmStrings.accountsReceivable());
        KpiRadioButton justDisposeRButton = new KpiRadioButton("disposeType", accountingStrings.justDispose());
        cashRButton.addValueChangeHandler(booleanValueChangeEvent -> {
            fixedAssetItem.setDisposeType(AccountingConstants.DISPOSE_CASH);
        });
        accountsReceivableRButton.addValueChangeHandler(booleanValueChangeEvent -> {
            fixedAssetItem.setDisposeType(AccountingConstants.DISPOSE_ACCOUNTS_RECEIVABLE);
        });
        justDisposeRButton.addValueChangeHandler(booleanValueChangeEvent -> {
            fixedAssetItem.setDisposeType(AccountingConstants.DISPOSE_JUST);
        });

        FormGroup amount = new FormGroup(wfmStrings.amount(), cashAmountTxtBox);
        FormGroup disposalDateField = new FormGroup(wfmStrings.disposalDate(), disposalDateBox);

        GRow cashRow = new GRow();
        GColumn typeCol = new GColumn(GColumnEnum.COL_6);
        typeCol.add(inputGroupFields(cashRButton, cashAccountDropdown));
        cashRow.add(typeCol);

        GColumn amountCol = new GColumn(GColumnEnum.COL_3);
        amountCol.add(amount);
        cashRow.add(amountCol);

        GColumn dateCol = new GColumn(GColumnEnum.COL_3);
        dateCol.add(disposalDateField);
        cashRow.add(dateCol);

        add(cashRow);

        GRow crmAccountRow = new GRow();
        GColumn accountReceivableCol = new GColumn(GColumnEnum.COL_6);
        typeCol.add(inputGroupFields(accountsReceivableRButton, crmAccountLookUp));
        crmAccountRow.add(accountReceivableCol);
        add(crmAccountRow);

        GRow justDispRow = new GRow();
        GColumn justDispCol = new GColumn(GColumnEnum.COL_6);
        justDispCol.add(inputGroupFields(justDisposeRButton, justDateBox));
        justDispRow.add(justDispCol);
        add(justDispRow);

        WfmButton2 disposeButton = new WfmButton2(wfmStrings.dispose(), WfmButton2.BTN_PRIMARY);
        WfmButton2 cancelButton = new WfmButton2(wfmStrings.cancel());
        disposeButton.addClickHandler(event -> disposeFixedAsset());
        cancelButton.addClickHandler(event -> close());
        addButton(cancelButton);
        addButton(disposeButton);

        cashRButton.setValue(Boolean.TRUE);
        fixedAssetItem.setDisposeType(AccountingConstants.DISPOSE_CASH);

    }

    private FormGroup inputGroupFields(Widget widget, Widget widget2) {
        Div inputGroup = new Div("input-group");
        Div prepend = new Div("input-group-prepend");
        inputGroup.add(prepend);

        Div prependedContent = new Div("input-group-text");
        prependedContent.add(widget);
        prepend.add(prependedContent);

        inputGroup.add(widget2);

        return new FormGroup(inputGroup);
    }

    private void disposeFixedAsset() {

        if (!validate()) {
            return;
        }

        if (AccountingConstants.DISPOSE_ACCOUNTS_RECEIVABLE.equals(fixedAssetItem.getDisposeType())) {
            close();
            SinksContainerFactory.entryPoint.onHistoryChanged("saleinvoice|add/add/copyFromFixedAsset/" + crmAccountLookUp.getSelectedItemID().toString());
        } else if (AccountingConstants.DISPOSE_CASH.equals(fixedAssetItem.getDisposeType()) || AccountingConstants.DISPOSE_JUST.equals(fixedAssetItem.getDisposeType())) {
            if (AccountingConstants.DISPOSE_CASH.equals(fixedAssetItem.getDisposeType())) {
                fixedAssetItem.setDisposeAccountID(cashAccountDropdown.getSelectedId());
                fixedAssetItem.setDisposeAmount(AccountingUtils.get().parseToBigDecimal(cashAmountTxtBox.getText()));
            }
            if (AccountingConstants.DISPOSE_JUST.equals(fixedAssetItem.getDisposeType())) {
                fixedAssetItem.setDisposedDate(new DateNonConvertable(justDateBox.getDate()));
            } else {
                fixedAssetItem.setDisposedDate(new DateNonConvertable(disposalDateBox.getDate()));
            }
            FixedAssetService.App.get().disposeFixedAssetItem(fixedAssetItem, new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable caught) {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void onSuccess(Void result) {
                    close();
                    Info.show("Fixed asset disposed successfully", Info.Type.INFO);
                    reloadListListener.execute();
                    if (viewCloseListener != null)
                        viewCloseListener.execute();
                }
            });
        }
    }

    public boolean validate() {
        int errors = 0;
        if (AccountingConstants.DISPOSE_CASH.equals(fixedAssetItem.getDisposeType())) {
            if (!Validation.validateWfmDropdown(cashAccountDropdown)) {
                errors++;
            }
            if (!Validation.validateTextBoxRequired(cashAmountTxtBox)) {
                errors++;
            }
        } else if (AccountingConstants.DISPOSE_ACCOUNTS_RECEIVABLE.equals(fixedAssetItem.getDisposeType())) {
            if (!Validation.validateSuggestBoxExist(crmAccountLookUp, null)) {
                errors++;
            }
        }
        if (AccountingConstants.DISPOSE_CASH.equals(fixedAssetItem.getDisposeType())) {
            if ((Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(disposalDateBox.getDate()))) {
                Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate(wfmStrings.disposalDate(), Utils.getTransactionLockDate()), Info.Type.WARNING);
                return false;
            }
        }
        if (AccountingConstants.DISPOSE_JUST.equals(fixedAssetItem.getDisposeType())) {
            if ((Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(justDateBox.getDate()))) {
                Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate(wfmStrings.disposalDate(), Utils.getTransactionLockDate()), Info.Type.WARNING);
                return false;
            }
        }
        return errors <= 0;
    }

    public void setViewCloseListener(Command viewCloseListener) {
        this.viewCloseListener = viewCloseListener;
    }
}
