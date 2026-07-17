package com.edatasite.workforce.gwt.core.client.ui.view.recurring;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentCategoryItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.CategoryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ScrollPanel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.UnorderedList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

public class AddRecurringDeductionView extends AbstractRecurringPayDeductionView {
    List<PaymentDeductionSelectItem> selectedCategories = new ArrayList<>();

    /*private DatePicker toDate;
    private TextBox limitAmount;
    private DataListBox limitType;
    private Div limitDiv;*/
    private MaterialLink allowanceLink;
    private KpiModal categoriesDialogBox;
    private Div categoriesDiv;
    private PaymentCategoryItem categoryItemSelectAll;
    private List<PaymentCategoryItem> allowanceItems = new ArrayList<>();;

    public AddRecurringDeductionView() {
        super("add");
        setDescription(property.getSingular(payrollStrings.recurringDeductionCategory()));
    }

    public AddRecurringDeductionView(Integer objectId) {
        super("edit");
        this.objectID = objectId;
        setDescription(property.getSingular(payrollStrings.recurringDeductionCategory()));
    }

    @Override
    protected void initAdditionalFields() {
        categoryLookUp = new CategoryLookUp(PayrollConstants.CATEGORY_DEDUCTION);
        categoryLookUp.addStyleName(DEFAULT_WIDTH);

        allowanceLink = new MaterialLink(wfmStrings.allowances());
        allowanceLink.addClickHandler(clickEvent -> {
            categoriesDialogBox.open();
            restoreSelectedCategories();
        });

        createCategoriesPopUp();

        /*toDate = new DatePicker();
        toDate.addStyleName(DEFAULT_WIDTH);

        limitAmount = new TextBox();
        limitAmount.addStyleName(DEFAULT_WIDTH);
        limitAmount.addChangeHandler(c -> {
            limitAmount.setText(AccountingUtils.get().formatQty(AccountingUtils.parsePriceToBigDecimal(limitAmount.getText())));
        });
        Validation.addNumericKeyboardListener(limitAmount, calculationScale);

        limitType = new DataListBox();
        limitType.setWithoutNullLabel(true);
        limitType.setItems(new SelectItem[]{
                new SelectItem(0, "By Date"),
                new SelectItem(1, "By Amount")});
        limitType.setSelected(1);

        limitType.addValueChangeHandler(event -> onChangeLimitType());

        limitDiv = new Div();
        onChangeLimitType();

        InputGroup inputGroup = new InputGroup();
        inputGroup.add(limitType, false);
        inputGroup.add(limitDiv, true);

        addField(PAYROLL_STARTER.PAY_TO_LIMIT, inputGroup, getTitle(wfmStrings.limit()));*/
    }

    @Override
    protected void setAdditionalData() {
        /*if (transferObject.getToDate() != null) {
            limitType.setSelected(0);
            toDate.setDate(transferObject.getToDate().getDate());
        } else if (transferObject.getTotalLimit() != null) {
            limitType.setSelected(1);
            limitAmount.setText(PayrollClientUtils.format(transferObject.getTotalLimit()));
        }
        onChangeLimitType();*/
        if (transferObject.getLinkedCategories() != null && transferObject.getLinkedCategories().size() > 0) {
            selectedCategories = transferObject.getLinkedCategories();
        } else if (transferObject.isFromAllAllowances()) {
            categoryItemSelectAll.chooseAsSelected();
            onAllCategorySelected();
        }
        updateSelectedCategories();
    }

    /*private void onChangeLimitType() {
        limitDiv.clear();
        if (limitType.getSelectedId().equals(0)) {
            limitDiv.add(toDate);
        } else {
            limitDiv.add(limitAmount);
        }
    }*/

    private void createCategoriesPopUp() {
        categoriesDialogBox = new KpiModal();
        categoriesDialogBox.setTitle(wfmStrings.deductionDetails());
        categoriesDialogBox.getElement().getStyle().setProperty("minWidth", "600px");
        categoriesDialogBox.getElement().getStyle().setProperty("margin", "30px auto");
        categoriesDialogBox.addStyleName("deductionDetailsModal");
        categoriesDiv = new Div();
        categoriesDiv.setWidth("100%");
        categoriesDiv.setHeight("300px");
        createCategoryList();
        categoriesDialogBox.add(categoriesDiv);
        ScrollPanel scrollPanel = new ScrollPanel();
        scrollPanel.getElement().getStyle().setProperty("maxHeight", "300px");
        categoriesDialogBox.add(scrollPanel);
    }

    private void createCategoryList() {
        ListingFilterParameter filterParameters = new ListingFilterParameter();
        filterParameters.setAccountType(PayrollConstants.CATEGORY_PAYMENT);
        filterParameters.setActive(false);
        filterParameters.setCorporate(Utils.isArabicCompany());
        filterParameters.setPayment(true);
        UnorderedList ul = new UnorderedList();
        categoryItemSelectAll = new PaymentCategoryItem(new PaymentDeductionSelectItem(-1, wfmStrings.selectAll(), "SELECT_ALL", null));
        categoryItemSelectAll.getCheckBox().addValueChangeHandler(event -> onAllCategorySelected());
        ul.add(categoryItemSelectAll.getWidget());
        LoadingPanel.loading(true);
        AllInOneService.App.get().getCategoriesForLookUp(filterParameters, new AsyncCallback<PaymentDeductionSelectItem[]>() {

            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(PaymentDeductionSelectItem[] paymentDeductionSelectItems) {
                LoadingPanel.loading(false);

                List<Integer> selectedIds = selectedCategories.stream().map(PaymentDeductionSelectItem::getId).collect(Collectors.toList());
                for (PaymentDeductionSelectItem item : paymentDeductionSelectItems) {
                    PaymentCategoryItem categoryItem = new PaymentCategoryItem(item);
                    if (selectedIds.contains(item.getId())) {
                        categoryItem.chooseAsSelected();
                    } else if (categoryItemSelectAll.isSelected()) {
                        categoryItem.chooseAsSelected();
                    }
                    ul.add(categoryItem.getWidget());
                    allowanceItems.add(categoryItem);
                }
                categoriesDiv.add(ul);
                categoriesDiv.setVisible(true);
            }
        });
        WfmButton2 apply = new WfmButton2(wfmStrings.apply(), WfmButton2.BTN_PRIMARY);
        apply.addClickHandler(clickEvent -> {
            updateSelectedCategories();
            categoriesDialogBox.close();
        });
        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
        cancel.addClickHandler(event -> {
            restoreSelectedCategories();
            categoriesDialogBox.close();
        });
        categoriesDialogBox.addButton(apply);
        categoriesDialogBox.addButton(cancel);
    }

    private void onAllCategorySelected() {
        boolean isSelectedAll = categoryItemSelectAll.getCheckBox().getValue();
        allowanceItems.forEach(item -> item.chooseSelected(isSelectedAll));
    }

    private void updateSelectedCategories() {
        selectedCategories = new ArrayList<>();
        for (PaymentCategoryItem item : allowanceItems) {
            if (item.isSelected()) {
                selectedCategories.add(item.getItem());
            }
        }
    }

    private void restoreSelectedCategories() {
        List<Integer> selectedIds = selectedCategories.stream().map(PaymentDeductionSelectItem::getId).collect(Collectors.toList());
        boolean isSelectedAll = true;
        for (PaymentCategoryItem item : allowanceItems) {
            if (selectedIds.contains(item.getItemId())) {
                item.chooseAsSelected();
            } else {
                isSelectedAll = false;
                item.chooseAsUnSelected();
            }
        }
        categoryItemSelectAll.getCheckBox().setValue(isSelectedAll);
    }

    @Override
    protected SelectItem[] getTerms() {
        return new SelectItem[]{
                new SelectItem(0, wfmStrings.fixed()),
                new SelectItem(1, wfmStrings.basicOfPersentage()),
                new SelectItem(2, wfmStrings.ofBasicAllowances())
//                new SelectItem(3, payrollStrings.minimumWage())
        };
    }

    @Override
    protected Command onChangeTerms() {
        return () -> {
            termsFormGroup.getGroupLabel().clear();
            if (terms.getSelectedItem().getId() == 0) {
                paymentAmount.setPlaceHolder(wfmStrings.amount());
            } else if (terms.getSelectedItem().getId() == 2) {
                paymentAmount.setPlaceHolder("%");
                termsFormGroup.getGroupLabel().add(allowanceLink);
            } /*else if (terms.getSelectedItem().getId() == 3) {
                paymentAmount.setPlaceHolder("%");
                onMinimumWageSelected();
                termsFormGroup.getGroupLabel().add(minimumWageLink);
            }*/ else {
                paymentAmount.setPlaceHolder("%");
            }
        };
    }

    @Override
    protected void getAdditionalData(RecurringPayDeductItem item) {
        if (terms.getSelectedId() == 2) {
            if (categoryItemSelectAll.getCheckBox().getValue()) {
                item.setFromAllAllowances(true);
            } else {
                item.setLinkedCategories(selectedCategories);
            }
        }
        /*if (limitType.getSelectedId() == 0 && toDate.getDate() != null) {
            item.setToDate(new DateNonConvertable(toDate.getDate()));
        } else if (limitType.getSelectedId() == 1 && limitAmount.getText() != null && limitAmount.getText().length() > 0) {
            item.setTotalLimit(PayrollClientUtils.parseToBigDecimal(limitAmount.getText()));
        }*/
    }

    @Override
    protected PayType getPayType() {
        return PayType.DEDUCTION;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PAYROLL_RECURRING_DEDUCTION_FORM;
    }
}
