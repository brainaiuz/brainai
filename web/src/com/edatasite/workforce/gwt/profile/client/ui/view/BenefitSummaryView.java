package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.BenefitItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Aziz on 29.09.14.
 */
public class BenefitSummaryView extends CustomForm2 implements Colapse {

    private static final SettingStrings settingsStrings = SettingStrings.App.get();

    private static final DateTimeFormat format = DateTimeFormat.getFormat(Utils.getShortDateFormat()); //"MM/dd/yyyy"
    private BenefitItem item;
    private TextArea2 description;
    private HTML name, type, qtyType, currency, transferrable, qtyRestriction, expireDate, employees, active, debitToAccount, creditToAccount;
    private final Integer objectID;

    public BenefitSummaryView(Integer objectID) {
        super("summary", settingsStrings.benefitView());
        this.objectID = objectID;

    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    protected void registerFields() {
        name = new HTML();
        type = new HTML();
        qtyType = new HTML();
        currency = new HTML();
        transferrable = new HTML();
        qtyRestriction = new HTML();
        expireDate = new HTML();
        employees = new HTML();
        active = new HTML();
        debitToAccount = new HTML();
        creditToAccount = new HTML();

        description = new TextArea2();

        addFieldsToForm();

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ADD_OR_EDIT_BENEFIT, BenefitSummaryView.this, (sender, args) -> getDataToFillFields());
        show();
    }

    private void addFieldsToForm() {
        addTitleField(BENEFIT.INFORMATION, wfmStrings.information());
        addField(BENEFIT.NAME, name, getTitle(wfmStrings.name(), true));
        addField(BENEFIT.TYPE, type, getTitle(wfmStrings.cashableOrNot(), true));
        addField(BENEFIT.QTY_TYPE, qtyType, getTitle(wfmStrings.quantityType(), true));
        addField(BENEFIT.CURRENCY, currency, getTitle(wfmStrings.currency(), true));
        addField(BENEFIT.TRANSFERRABLE, transferrable, getTitle(wfmStrings.transferrable(), false));
//        addField(BENEFIT.QTY_RESTRICTION, qtyRestriction, getTitle("QTY restriction by", false));
        addField(BENEFIT.EXPIRE_DATE, expireDate, wfmStrings.expiryDate());
//        addField(BENEFIT.BY_EMPLOYEES, employees, wfmStrings.employees());
        addField(BENEFIT.STATUS, active, wfmStrings.active());
        addField(BENEFIT.DESCRIPION, description, wfmStrings.description());
        addField(BENEFIT.DEBIT_TO_ACCOUNT, debitToAccount, wfmStrings.debitToAccount());
        addField(BENEFIT.CREDIT_TO_ACCOUNT, creditToAccount, wfmStrings.creditToAccount());
    }


    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        HrmsService.App.get().getBenefitData(objectID, false, new AbstractAsyncCallback<BenefitItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(BenefitItem result) {
                LoadingPanel.loading(false);
                item = result;
                name.setHTML(result.getName());
                type.setHTML(result.getType() != null ? result.getType() : "");
                qtyType.setHTML(result.getQtytype() != null ? result.getQtytype() : wfmStrings.notAvailable());
                currency.setHTML(result.getCurrency() != null ? result.getCurrency() : wfmStrings.notAvailable());
                transferrable.setHTML(result.getTransferrable() != null ? result.getTransferrable() ? wfmStrings.yes().toUpperCase() : wfmStrings.no().toUpperCase() : wfmStrings.notAvailable());
                qtyRestriction.setHTML(result.getQtyRestriction() != null ? result.getQtyRestriction() ?  wfmStrings.yes().toUpperCase() :  wfmStrings.no().toUpperCase() : wfmStrings.notAvailable());
                expireDate.setHTML(result.getExpireDate() != null ? format.format(result.getExpireDate().getNonConvertedDate()) : wfmStrings.notAvailable());
                active.setHTML(result.isActive() ? wfmStrings.yes() : wfmStrings.no());
                debitToAccount.setHTML(result.getDebitToAccount() != null ? result.getDebitToAccount().getName() : wfmStrings.notAvailable());
                creditToAccount.setHTML(result.getCreditToAccount() != null ? result.getCreditToAccount().getName() : wfmStrings.notAvailable());

                description.setReadOnly(true);
                description.setText(result.getDescription());

                StringBuilder empList = new StringBuilder();
                for (SelectItem emp : result.getEmployees()) {
                    empList.append(emp.getName());
                    empList.append("<br>");
                }
                employees.setHTML(empList.toString());

            }
        });
    }

    @Override
    protected void addButtons() {


        if (Utils.hasPermission(PermissionConstants.BENEFIT_TYPE_ADD)) {
            addEditButton().addClickHandler(clickEvent -> {
                if (item != null) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("benefit|add/add/" + item.getObjectId(), item.getName());
                }
            });
        }

        addRemoveButton().addClickHandler(clickEvent -> {
            if (item != null) {
                final WfmMessageBox message = new WfmMessageBox(IconEnum.INFO, Action.YesNo, true);
                message.setSize(300, 150);
                message.setTitle(wfmStrings.warning());
                message.setMessage(wfmStrings.sureYouWantToDelete());
                message.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        LoadingPanel.loading(true);
                        HrmsService.App.get().deleteBenefit(item.getObjectId(), new AbstractAsyncCallback<Integer>() {
                            @Override
                            public void failure(Throwable throwable) {
                                LoadingPanel.loading(false);
                                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                            }

                            @Override
                            public void success(Integer result) {
                                LoadingPanel.loading(false);
                                if (result > 0) {
                                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.benefit()), Info.Type.INFO);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.DELETE_BENEFIT, result, BenefitSummaryView.this);
                                    closeTab();
                                } else if (result == -2) {
                                    Info.show(wfmStrings.youCannotDelete() + item.getName() + ". " + wfmStrings.itIsUsedOAtLeastABenefitRequest(), Info.Type.WARNING);
                                } else {
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }
                            }
                        });
                    }
                });
                message.open();
            }
        });
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    public String getFieldLabel(String fieldID) {
        return wfmStrings.information();
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.BENEFIT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;
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
}
