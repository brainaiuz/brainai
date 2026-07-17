package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.SimpleUsagePlanItem;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * User: Ilhombek
 * Date: 24.08.2010
 * Time: 15:01:57
 */
public class UsagePlanUpdateView extends FooteredView implements Colapse {

    private static final BackendStrings backendStrings = BackendStrings.App.get();

    private static final WfmStrings wfmStrings = WfmStrings.App.get();


    private Integer usagePlanId;//Usage Plan ID
    private FlexTable generalTable;
    private static final SelectItem[] TRUE_FALSE = new SelectItem[]{
            new SelectItem(0, "True"),
            new SelectItem(1, "False")};
    private static final Integer TRUE_ID = 0;
    private static final Integer FALSE_ID = 1;
    private static final String _PERIOD_TYPE = "_PERIOD_TYPE";
    private static final String _SERVICE_TYPE = "_SERVICE_TYPE";
    private static final String _PAYMENT_STATUS = "_PAYMENT_STATUS";

    private static final SelectItem[] CATEGORY_PLAN_TYPES = new SelectItem[]{
            new SelectItem(0, "Free Trial", Constants.FREE_TRIAL),
            new SelectItem(1, "Small Business", Constants.PP_SMALL_BUSINESS),
            new SelectItem(2, "kpi Pro", Constants.PP_KPI_PRO),
            new SelectItem(3, "Enterprise", Constants.PP_ENTERPRISE),
    };

    private static final SelectItem[] SUPPORT_PACKAGE_NAMES = new SelectItem[]{
            new SelectItem(0, "Basic", Constants.SP_BASIC),
            new SelectItem(1, "Standard", Constants.SP_STANDARD),
            new SelectItem(2, "Bronze", Constants.SP_BRONZE),
            new SelectItem(3, "Silver", Constants.SP_SILVER),
            new SelectItem(4, "Gold", Constants.SP_GOLD),
            new SelectItem(5, "Platinum", Constants.SP_PLATINUM),
            new SelectItem(6, "Diamond", Constants.SP_DIAMOND)
    };

    private DatePicker startDate;//Start Date
    private DatePicker endDate;//End Date
    private TextBox usersCount;//Users Count
    private TextBox noAccessUsersCount;//Users Count
    private TextBox essUsersCount;//Ess Users Count
    private TextBox discount;//Discount
    private TextBox storage;//Storage
    private DataListBox categoryCodes;//Free trial OR Small Business OR KPI Pro OR Enterprise
    private DataListBox supportPackageNames;//Basic OR Standard OR Bronze OR Silver OR Gold OR Platinum OR Diamond

    private DataListBox paymentStatus;//Payment Status
//    private DataListBox serviceType;//Plan Type
//    private DataListBox periodType;//Period Type

    private TextBox totalAmount;//Total Amount

//    private DataListBox isPaypalStatus;//Paypal Status
//    private DataListBox isUKCompany;//is UK Company
//    private DataListBox isCurrencyGBR;//is Currency GBR

//    private DataListBox isMobile;// is Mobile

//    private TextBox taskCount;//Task Count
//    private TextBox projectCount;//Project Count

    private TextBox userRate;

//    private DataListBox isMessageSended;//Message sended

    //    private DataListBox deleted;//Deleted
    private DataListBox isPaid;//is Paid

    private Integer companyID;

    public UsagePlanUpdateView(String usagePlanId, String companyId) {
        super("updateUsage", ((usagePlanId != null && !"".equals(usagePlanId)) ? backendStrings.usagePlanUpdate() : backendStrings.usagePlanCreate()));
        if (usagePlanId != null && !"".equals(usagePlanId)) {
            this.usagePlanId = Integer.valueOf(usagePlanId);
        }
        this.companyID = Integer.valueOf(companyId);
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        generalTable = new FlexTable();
        generalTable.setCellSpacing(15);
        generalTable.setCellPadding(15);
        drawPanel();
        return null;
    }

    private void drawPanel() {
        //general table
        generalTable.setHTML(0, 0, "<b class=customTitle style='font-size:14px;'>" +
                ((usagePlanId != null && usagePlanId != 0) ? backendStrings.usagePlanUpdate() : backendStrings.usagePlanCreate()) + "</b>");
        generalTable.getFlexCellFormatter().setColSpan(0, 0, 7);
        generalTable.setWidth("100%");
        generalTable.addStyleName("usagePlan-table");
        generalTable.getFlexCellFormatter().addStyleName(1, 1, "usagePlan-table__th");
        //discount
        discount = new TextBox();
        discount.addStyleName(DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(discount);
        //user count
        usersCount = new TextBox();
        usersCount.addStyleName(DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(usersCount);

        noAccessUsersCount = new TextBox();
        noAccessUsersCount.addStyleName(DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(noAccessUsersCount);

        essUsersCount = new TextBox();
        essUsersCount.addStyleName(DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(essUsersCount);
        //storage
        storage = new TextBox();
        storage.addStyleName(DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(storage);
        //start date
        if (usagePlanId != null) {
            startDate = new DatePicker(DateUtils.getDateFormatShort());
        } else {
            startDate = new DatePicker(new Date(), DateUtils.getDateFormatShort());
        }
        startDate.addStyleName(DEFAULT_WIDTH);
        //end date
        endDate = new DatePicker(DateUtils.getDateFormatShort());
        endDate.addStyleName(DEFAULT_WIDTH);
        //payment status
        paymentStatus = new DataListBox();
        paymentStatus.addStyleName(DEFAULT_WIDTH);
        //service type
//        serviceType = new DataListBox();
//        serviceType.addStyleName(DEFAULT_WIDTH);
        //period type
//        periodType = new DataListBox();
//        periodType.addStyleName(DEFAULT_WIDTH);
        //total amount
        totalAmount = new TextBox();
        totalAmount.addStyleName(DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(totalAmount);
        //paypal status
//        isPaypalStatus = new DataListBox();
//        isPaypalStatus.addStyleName(DEFAULT_WIDTH);
//        isPaypalStatus.setItems(TRUE_FALSE);
        //is UK company
//        isUKCompany = new DataListBox();
//        isUKCompany.addStyleName(DEFAULT_WIDTH);
//        isUKCompany.setItems(TRUE_FALSE);
        //is currency GPB
//        isCurrencyGBR = new DataListBox();
//        isCurrencyGBR.addStyleName(DEFAULT_WIDTH);
//        isCurrencyGBR.setItems(TRUE_FALSE);
        //is mobile
//        isMobile = new DataListBox();
//        isMobile.addStyleName(DEFAULT_WIDTH);
//        isMobile.setItems(TRUE_FALSE);
//        task count
//        taskCount = new TextBox();
//        taskCount.addStyleName(DEFAULT_WIDTH);
//        Validation.addNumericKeyboardListener(taskCount);
//        project count
//        projectCount = new TextBox();
//        projectCount.addStyleName(DEFAULT_WIDTH);
//        Validation.addNumericKeyboardListener(projectCount);
        //user rate
        userRate = new TextBox();
        userRate.addStyleName(DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(userRate);
        //is message send
//        isMessageSended = new DataListBox();
//        isMessageSended.addStyleName(DEFAULT_WIDTH);
//        isMessageSended.setItems(TRUE_FALSE);
        //deleted
//        deleted = new DataListBox();
//        deleted.addStyleName(DEFAULT_WIDTH);
//        deleted.setItems(TRUE_FALSE);
        //is paid
        isPaid = new DataListBox();
        isPaid.addStyleName(DEFAULT_WIDTH);
        isPaid.setItems(TRUE_FALSE);

        //current plan/package
        categoryCodes = new DataListBox();
        categoryCodes.addStyleName(DEFAULT_WIDTH);
        categoryCodes.setItems(CATEGORY_PLAN_TYPES);
        //category plan type
        supportPackageNames = new DataListBox();
        supportPackageNames.addStyleName(DEFAULT_WIDTH);
        supportPackageNames.setItems(SUPPORT_PACKAGE_NAMES);

        //generate table
        generalTable.setWidget(1, 0, generateTitle(wfmStrings.startDate(), true));
        generalTable.setWidget(1, 1, startDate);
        generalTable.setWidget(1, 2, generateTitle(backendStrings.countActiveUsers(), false));
        generalTable.setWidget(1, 3, usersCount);

        generalTable.setWidget(2, 0, generateTitle(wfmStrings.endDate(), true));
        generalTable.setWidget(2, 1, endDate);
        generalTable.setWidget(2, 2, generateTitle(backendStrings.essUserCount(), false));
        generalTable.setWidget(2, 3, essUsersCount);

        generalTable.setWidget(3, 0, generateTitle(wfmStrings.storage(), true));
        generalTable.setWidget(3, 1, storage);
        generalTable.setWidget(3, 2, generateTitle(backendStrings.noAccessUserCount2(), false));
        generalTable.setWidget(3, 3, noAccessUsersCount);

        generalTable.setWidget(4, 0, generateTitle(wfmStrings.isPaid(), true));
        generalTable.setWidget(4, 1, isPaid);

        generalTable.setWidget(5, 0, generateTitle(backendStrings.paymentStatus(), true));
        generalTable.setWidget(5, 1, paymentStatus);

        generalTable.setWidget(6, 0, generateTitle(wfmStrings.userRate(), true));
        generalTable.setWidget(6, 1, userRate);

        if (usagePlanId != null && usagePlanId > 0) {
            LoadingPanel.loading(true);
            BackendService.App.get().getUsagePlanItem(usagePlanId, new AbstractAsyncCallback<SimpleUsagePlanItem>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(final SimpleUsagePlanItem usagePlanItem) {
                    LoadingPanel.loading(false);
                    if (usagePlanItem != null) {
                        companyID = usagePlanItem.getCompanyID();
                        discount.setText(String.valueOf(usagePlanItem.getDiscount()));
                        totalAmount.setText(String.valueOf(usagePlanItem.getTotalAmount()));
                        if (usagePlanItem.getUserCount() != null) {
                            usersCount.setText(String.valueOf(usagePlanItem.getUserCount()));
                        }
                        if (usagePlanItem.getNonAccessUserCount() != null) {
                            noAccessUsersCount.setText(String.valueOf(usagePlanItem.getNonAccessUserCount()));
                        }
                        if (usagePlanItem.getEssUserCount() != null) {
                            essUsersCount.setText(String.valueOf(usagePlanItem.getEssUserCount()));
                        }
                        startDate.setDate(usagePlanItem.getStartDate());
                        BackendService.App.get().getItemsByParent(_PAYMENT_STATUS, new AbstractAsyncCallback<SelectItem[]>() {
                            @Override
                            public void failure(Throwable caught) {
                            }

                            @Override
                            public void success(SelectItem[] result) {
                                paymentStatus.setItems(result);
                                paymentStatus.setSelected(usagePlanItem.getPaymentStatusId());
                            }
                        });
//                        if (usagePlanItem.getTaskCount() != null) {
//                            taskCount.setText(String.valueOf(usagePlanItem.getTaskCount()));
//                        }
                        endDate.setDate(usagePlanItem.getEndDate());
//                        if (usagePlanItem.getProjectCount() != null) {
//                            projectCount.setText(String.valueOf(usagePlanItem.getProjectCount()));
//                        }
                        if (usagePlanItem.getUserRate() != null) {
                            userRate.setText(String.valueOf(usagePlanItem.getUserRate()));
                        }
                        if (usagePlanItem.isPaid()) {
                            isPaid.setSelected(TRUE_ID);
                        } else {
                            if (!usagePlanItem.isPaid()) {
                                isPaid.setSelected(FALSE_ID);
                            }
                        }
//                        BackendService.App.get().getItemsByParent(_SERVICE_TYPE, new AbstractAsyncCallback<SelectItem[]>() {
//                            @Override
//                            public void failure(Throwable caught) {
//                            }
//
//                            @Override
//                            public void success(SelectItem[] result) {
//                                serviceType.setItems(result);
//                                serviceType.setSelected(usagePlanItem.getServiceTypeId());
//                            }
//                        });
                        if (usagePlanItem.getStorageCount() != null) {
                            storage.setText(String.valueOf(usagePlanItem.getStorageCount()));
                        }
//                        if (usagePlanItem.isCompanyUk()) {
//                            isUKCompany.setSelected(TRUE_ID);
//                        } else {
//                            if (!usagePlanItem.isCompanyUk()) {
//                                isUKCompany.setSelected(FALSE_ID);
//                            }
//                        }
//                        BackendService.App.get().getItemsByParent(_PERIOD_TYPE, new AbstractAsyncCallback<SelectItem[]>() {
//                            @Override
//                            public void failure(Throwable caught) {
//                            }
//
//                            @Override
//                            public void success(SelectItem[] result) {
//                                periodType.setItems(result);
//                                periodType.setSelected(usagePlanItem.getPeriodTypeId());
//                            }
//                        });
//                        if (usagePlanItem.isPaypalStatus()) {
//                            isPaypalStatus.setSelected(TRUE_ID);
//                        } else {
//                            if (!usagePlanItem.isPaypalStatus()) {
//                                isPaypalStatus.setSelected(FALSE_ID);
//                            }
//                        }
//                        if (usagePlanItem.isCurrencyGBP()) {
//                            isCurrencyGBR.setSelected(TRUE_ID);
//                        } else {
//                            if (!usagePlanItem.isCurrencyGBP()) {
//                                isCurrencyGBR.setSelected(FALSE_ID);
//                            }
//                        }
//                        if (usagePlanItem.isMessageSended()) {
//                            isMessageSended.setSelected(TRUE_ID);
//                        } else {
//                            if (!usagePlanItem.isMessageSended()) {
//                                isMessageSended.setSelected(FALSE_ID);
//                            }
//                        }
//                        if (usagePlanItem.isMobile()) {
//                            isMobile.setSelected(TRUE_ID);
//                        } else {
//                            if (!usagePlanItem.isMobile()) {
//                                isMobile.setSelected(FALSE_ID);
//                            }
//                        }
//                        if (usagePlanItem.isDeleted()) {
//                            deleted.setSelected(TRUE_ID);
//                        } else {
//                            if (!usagePlanItem.isDeleted()) {
//                                deleted.setSelected(FALSE_ID);
//                            }
//                        }
                    }
                }
            });
        } else {
//            BackendService.App.get().getItemsByParent(_PERIOD_TYPE, new AbstractAsyncCallback<SelectItem[]>() {
//                @Override
//                public void failure(Throwable caught) {
//                }
//
//                @Override
//                public void success(SelectItem[] result) {
//                    periodType.setItems(result);
//                }
//            });
//            BackendService.App.get().getItemsByParent(_SERVICE_TYPE, new AbstractAsyncCallback<SelectItem[]>() {
//                @Override
//                public void failure(Throwable caught) {
//                }
//
//                @Override
//                public void success(SelectItem[] result) {
//                    serviceType.setItems(result);
//                }
//            });
            BackendService.App.get().getItemsByParent(_PAYMENT_STATUS, new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable caught) {
                }

                @Override
                public void success(SelectItem[] result) {
                    paymentStatus.setItems(result);
                }
            });
        }
        add(generalTable);
        add(createFooter());
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return UsagePlanUpdateView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return UsagePlanUpdateView.this.getFooterRightSideWidgets();
            }
        });
    }

    private List<Widget> getFooterRightSideWidgets() {
        List<Widget> buttonList = new ArrayList<>();

        //save button
        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.addClickHandler(event -> save());

        Div saveWrapper = new Div();
        saveWrapper.add(saveButton);
        buttonList.add(saveWrapper);

        return buttonList;
    }

    private List<Widget> getFooterLeftSideWidgets() {
        return null;
    }

    private HTML generateTitle(String title, boolean required) {
        return new HTML("<span class=customTitle>" + title + (required ? "<font color=red>*</font>" : "") + ":</span>");
    }

    private boolean isSmebuOrTjiloHost() {
        return Utils.getHostName().contains("smebu.com") || Utils.getHostName().contains("tjilo.com");
    }

    private void save() {
        if (validate()) {
            return;
        }

        SimpleUsagePlanItem simpleUsagePlanItem = new SimpleUsagePlanItem();
        if (usagePlanId != null) {
            simpleUsagePlanItem.setObjectID(usagePlanId);
        }
        simpleUsagePlanItem.setCompanyID(companyID);
        if (discount.getText() != null && !"".equals(discount.getText())) {
            simpleUsagePlanItem.setDiscount(Float.parseFloat(discount.getText()) > 0 ? Float.parseFloat(discount.getText()) : 0f);
        }
        if (totalAmount.getText() != null && !"".equals(totalAmount.getText())) {
            simpleUsagePlanItem.setTotalAmount(Float.parseFloat(totalAmount.getText()));
        }
        if (usersCount.getText() != null && !"".equals(usersCount.getText())) {
            simpleUsagePlanItem.setUserCount(Integer.valueOf(usersCount.getText()));
        }
        if (noAccessUsersCount.getText() != null && !"".equals(noAccessUsersCount.getText().trim())) {
            simpleUsagePlanItem.setNonAccessUserCount(Integer.valueOf(noAccessUsersCount.getText()));
        }
        if (essUsersCount.getText() != null && !"".equals(essUsersCount.getText().trim())) {
            simpleUsagePlanItem.setEssUserCount(Integer.valueOf(essUsersCount.getText()));
        }
        if (startDate.getDate() != null) {
            simpleUsagePlanItem.setStartDate(startDate.getDate());
        }
        if (paymentStatus.isSomethingSelected()) {
            simpleUsagePlanItem.setStatus(paymentStatus.getSelectedItem().getDescription());
        }
//        if (taskCount.getText() != null && !"".equals(taskCount.getText())) {
//            simpleUsagePlanItem.setTaskCount(Integer.valueOf(taskCount.getText()));
//        }
        if (endDate.getDate() != null) {
            simpleUsagePlanItem.setEndDate(endDate.getDate());
        }
//        if (projectCount.getText() != null && !"".equals(projectCount.getText())) {
//            simpleUsagePlanItem.setProjectCount(Integer.valueOf(projectCount.getText()));
//        }
        if (userRate.getText() != null && !"".equals(userRate.getText())) {
            simpleUsagePlanItem.setUserRate(Float.valueOf(userRate.getText()));
        }
        if (isPaid.isSomethingSelected()) {
            if (TRUE_ID.equals(isPaid.getSelectedItem().getId())) {
                simpleUsagePlanItem.setPaid(true);
            } else {
                if (FALSE_ID.equals(isPaid.getSelectedItem().getId())) {
                    simpleUsagePlanItem.setPaid(false);
                }
            }
        }
//        if (periodType.isSomethingSelected()) {
//            simpleUsagePlanItem.setPlanType(periodType.getSelectedItem().getDescription());
//        }
        if (storage.getText() != null && !"".equals(storage.getText())) {
            simpleUsagePlanItem.setStorageCount(Integer.valueOf(storage.getText()));
        }
//        if (isUKCompany.isSomethingSelected()) {
//            if (TRUE_ID.equals(isUKCompany.getSelectedItem().getId())) {
//                simpleUsagePlanItem.setCompanyUk(true);
//            } else {
//                if (FALSE_ID.equals(isUKCompany.getSelectedItem().getId())) {
//                    simpleUsagePlanItem.setCompanyUk(false);
//                }
//            }
//        }
//        if (periodType.isSomethingSelected()) {
//            simpleUsagePlanItem.setService(serviceType.getSelectedItem().getDescription());
//        }
//        if (isPaypalStatus.isSomethingSelected()) {
//            if (TRUE_ID.equals(isPaypalStatus.getSelectedItem().getId())) {
//                simpleUsagePlanItem.setPaypalStatus(true);
//            } else {
//                if (FALSE_ID.equals(isPaypalStatus.getSelectedItem().getId())) {
//                    simpleUsagePlanItem.setPaypalStatus(false);
//                }
//            }
//        }
//        if (isCurrencyGBR.isSomethingSelected()) {
//            if (TRUE_ID.equals(isCurrencyGBR.getSelectedItem().getId())) {
//                simpleUsagePlanItem.setCurrencyGBP(true);
//            } else {
//                if (FALSE_ID.equals(isCurrencyGBR.getSelectedItem().getId())) {
//                    simpleUsagePlanItem.setCurrencyGBP(false);
//                }
//            }
//        }
//        if (isMessageSended.isSomethingSelected()) {
//            if (TRUE_ID.equals(isMessageSended.getSelectedItem().getId())) {
//                simpleUsagePlanItem.setMessageSended(true);
//            } else {
//                if (FALSE_ID.equals(isMessageSended.getSelectedItem().getId())) {
//                    simpleUsagePlanItem.setMessageSended(false);
//                }
//            }
//        }
//        if (isMobile.isSomethingSelected()) {
//            if (TRUE_ID.equals(isMobile.getSelectedItem().getId())) {
//                simpleUsagePlanItem.setMobile(true);
//            } else {
//                if (FALSE_ID.equals(isMobile.getSelectedItem().getId())) {
//                    simpleUsagePlanItem.setMobile(false);
//                }
//            }
//        }
//        if (deleted.isSomethingSelected()) {
//            if (TRUE_ID.equals(deleted.getSelectedItem().getId())) {
//                simpleUsagePlanItem.setDeleted(true);
//            } else {
//                if (FALSE_ID.equals(deleted.getSelectedItem().getId())) {
//                    simpleUsagePlanItem.setDeleted(false);
//                }
//            }
//        }
        BackendService.App.get().saveUsagePlan(simpleUsagePlanItem, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable caught) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void result) {
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_USAGE_PLAN_ADD_EDIT, result, UsagePlanUpdateView.this);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), backendStrings.usagePlan()), Info.Type.INFO);
                closeTab();
            }
        });
    }

    private boolean validate() {
        int errors = 0;

//        if (!Validation.validateListBoxRequired(periodType, new HTML(), "")) {
//            errors++;
//        }
//        if (!Validation.validateListBoxRequired(serviceType, new HTML(), "")) {
//            errors++;
//        }
        if (!Validation.validateListBoxRequired(paymentStatus, new HTML(), "")) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(isPaid, new HTML(), "")) {
            errors++;
        }
        if (!Validation.validateDate(startDate, new HTML(), true)) {
            errors++;
            startDate.setStyleName("x-form-invalid");
            startDate.setTitle(wfmStrings.pleaseSelectDate());
        } else {
            startDate.setStyleName("");
            startDate.setTitle("");
        }
        if (!Validation.validateDate(endDate, new HTML(), true)) {
            errors++;
            endDate.setStyleName("x-form-invalid");
            endDate.setTitle(wfmStrings.pleaseSelectDate());
        } else {
            endDate.setStyleName("");
            endDate.setTitle("");
        }
        if (!Validation.validateDateOrder(startDate.getDate(), endDate.getDate())) {
            errors++;
            startDate.setStyleName("x-form-invalid");
            startDate.setTitle(wfmStrings.startDateNotLaterDueDate());
        } else {
            startDate.setStyleName("");
            startDate.setTitle("");
        }
        if (isSmebuOrTjiloHost() && !Validation.validateTextBoxRequired(discount)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(totalAmount)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(usersCount)) {
            errors++;
        }
//        if (!Validation.validateTextBoxRequired(noAccessUsersCount)) {
//            errors++;
//        }
        if (!Validation.validateTextBoxRequired(essUsersCount)) {
            errors++;
        }
//        if (!Validation.validateTextBoxRequired(taskCount)) {
//            errors++;
//        }
//        if (!Validation.validateTextBoxRequired(projectCount)) {
//            errors++;
//        }
        if (!Validation.validateTextBoxRequired(userRate)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(storage)) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return true;
        }
        return false;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}