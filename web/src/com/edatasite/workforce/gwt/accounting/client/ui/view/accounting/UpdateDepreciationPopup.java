package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.TrialBalanceFilterData;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * User: Dilsh0d Madrahimov
 * Date: 2/27/12
 * Time: 1:45 PM
 */
class UpdateDepreciationPopup extends KpiModal {
    private static final String DATE_FORMAT = "MMMM-yyyy";
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private DataListBox dateListBox;
    private LinkedHashMap<Integer, Date> startDateMap;
    private WfmButton2 updateDepreciation;
    private Date updateDepreciationDate;


    UpdateDepreciationPopup() {
        initialize();
    }

    private void initialize() {
        setTitle(accountingStrings.updateDepreciation());
        dateListBox = new DataListBox();
        dateListBox.setSelectedNullLabel();
        updateDepreciation = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_DEFAULT);
        updateDepreciation.addClickHandler(action -> {
            if (dateListBox.isSomethingSelected())
                close();
             else
                dateListBox.addStyleName("x-form-invalid");
        });
        startDateMap = new LinkedHashMap<>();
        add(dateListBox);
        addButton(updateDepreciation);

        AccountingService.App.get().getTrialBalanceFilterData(new AsyncCallback<TrialBalanceFilterData>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(TrialBalanceFilterData result) {
                Date currentDate = new Date();
                Date financialStartDate = DateUtil.addDays(result.getFinancialYearEnd().getNonConvertedDate(), 1);
                financialStartDate.setYear(currentDate.getYear());
                Date conversionDate = DateUtil.getMonthFirstDay(result.getConversationDate());

                DateTimeFormat dateFormat = DateTimeFormat.getFormat(DATE_FORMAT);
                Date startDate = new Date();
                startDate = DateUtil.getMonthLastDate(startDate);
                while (startDate.compareTo(conversionDate) >= 0) {
                    SelectItem item = new SelectItem(Integer.valueOf(clarifyID(startDate)), dateFormat.format(startDate));
                    dateListBox.addListItem(item);
                    startDateMap.put(item.getId(), startDate);
                    startDate = DateUtil.addMonths(startDate, -1, 1);
                }
            }
        });

        initHandler();

    }

    private void initHandler() {
        dateListBox.addValueChangeHandler(event -> {
            if (dateListBox.isSomethingSelected()) dateListBox.removeStyleName("x-form-invalid");
            updateDepreciationDate = startDateMap.get(dateListBox.getSelectedId());
        });
        updateDepreciation.addClickHandler(clickEvent -> {
            if (updateDepreciationDate != null) {
                enableButton(false);
                LoadingPanel.loading(true);
                FixedAssetService.App.get().sendToUpdateDeprecationMQ(new DateNonConvertable(DateUtil.getMonthLastDate(updateDepreciationDate)), new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        enableButton(true);
                        LoadingPanel.loading(false);
                        close();
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }

                    @Override
                    public void onSuccess(Boolean hasInProgressDeprication) {
                        LoadingPanel.loading(false);
                        close();
                        if (hasInProgressDeprication) {
                            WfmMessageBox messageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.OK);
                            messageBox.setTitle(wfmStrings.information());
                            messageBox.setMessage(wfmStrings.depricationisInProgressPleaseSeeImportLog(), wfmStrings.updateingAndNotification());
                            messageBox.open();
                        } else {
                            Info.show(wfmStrings.updateingAndNotification(), Info.Type.INFO);
                        }
                    }
                });
            } else {
                Info.show(accountingStrings.updateDepreciationDate(), Info.Type.WARNING);
            }
        });
    }

    private void enableButton(boolean enable) {
        updateDepreciation.setEnabled(enable);
    }

    private String clarifyID(Date date) {
        return date.getYear() + Integer.toString(date.getMonth());
    }
}
