package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.ContractCoursePriceItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 09.01.14
 * Time: 17:06
 * To change this template use File | Settings | File Templates.
 */
public class ChangeContractPricesView extends CustomForm2 implements Colapse {
    private static final TCServiceAsync tcService = TCService.App.get();
    private static final TCStrings tcStrings = TCStrings.App.get();

    public static final String COURSE = "COURSE";
    public static final String LOCATION = "LOCATION";
    public static final String PRICE = "PRICE";
    public static final String STOP_FEE = "STOP_FEE";

    private final Integer contractID;
    private ContractCoursePriceItem[] coursePrices;
    private EditableTable coursePriceTable;
    private WfmButton2 updatePrices;

    public ChangeContractPricesView(Integer contractID) {
        super(TCConstants.TC_CHANGE_CONTRACT_PRICE, tcStrings.changePrices());
        this.contractID = contractID;
    }

    @Override
    public Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    @Override
    protected void registerFields() {
        coursePriceTable = new EditableTable(getColumnConfigs(), false);
        addTitleField(CONTRACT.COURSE_PRICES, tcStrings.coursePrices());
        addField(CONTRACT.PRICE_TABLE, coursePriceTable, null);
        show();
    }

    @Override
    protected void initPredefinedValues() {

    }


    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), clickEvent -> save());
        updatePrices = new WfmButton2(wfmStrings.updatePrices(), WfmButton2.BTN_WHITE_OUTLINE);
        updatePrices.addClickHandler(clickEvent -> updatePrices());
        addButton(updatePrices);
    }

    private void updatePrices() {
        LoadingPanel.loading(true);
        tcService.updatePrices(contractID, new AbstractAsyncCallback<ArrayList<ContractCoursePriceItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ArrayList<ContractCoursePriceItem> result) {
                LoadingPanel.loading(false);
                addNewPrices(result);
                Info.show("Course prices have been fully updated", Info.Type.INFO);
            }
        });
    }

    private void save() {
        if (!validate()) {
            return;
        }
        List<ContractCoursePriceItem> contractCoursePriceItems = getPriceItems();
        tcService.changeContractCoursePrices(contractID, contractCoursePriceItems, new AbstractAsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Void result) {
                closeTab();
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), tcStrings.contractCourse()), Info.Type.INFO);
            }
        });
    }

    private boolean validate() {
        int courseValid = 0;
        for (int rowID = 0; rowID < coursePriceTable.getRowCount(); rowID++) {
            int courseValidRow = 0;
            EditableTextBox price = (EditableTextBox) coursePriceTable.getColumnById(rowID, PRICE);
            EditableTextBox stopFee = (EditableTextBox) coursePriceTable.getColumnById(rowID, STOP_FEE);
            if (price.getText() == null || "".equals(price.getText())) {
                courseValid++;
                coursePriceTable.setColumnValid(PRICE);
                coursePriceTable.notValid(rowID, PRICE);
            }
            if (stopFee.getText() == null || "".equals(stopFee.getText())) {
                courseValid++;
                coursePriceTable.setColumnValid(STOP_FEE);
                coursePriceTable.notValid(rowID, STOP_FEE);
            }
            coursePriceTable.setItemValid(rowID, courseValidRow <= 0);
            courseValid += courseValidRow;
            courseValidRow = 0;
        }
        if (courseValid > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private List<ContractCoursePriceItem> getPriceItems() {
        List<ContractCoursePriceItem> items = new ArrayList<>();
        for (int rowID = 0; rowID < coursePriceTable.getRowCount(); rowID++) {
            EditableTextBox course = (EditableTextBox) coursePriceTable.getColumnById(rowID, COURSE);
            EditableTextBox location = (EditableTextBox) coursePriceTable.getColumnById(rowID, LOCATION);
            EditableTextBox price = (EditableTextBox) coursePriceTable.getColumnById(rowID, PRICE);
            EditableTextBox stopFee = (EditableTextBox) coursePriceTable.getColumnById(rowID, STOP_FEE);
            ContractCoursePriceItem item = new ContractCoursePriceItem();
            item.setCourseID((Integer) course.getLayoutData());
            item.setObjectID((Integer) location.getLayoutData());
            item.setCourseName(course.getText());
            item.setLocationName(location.getText());
            item.setCoursePrice(BigDecimal.valueOf(Double.valueOf(price.getText().replaceAll(",", ""))));
            item.setStopFee(BigDecimal.valueOf(Double.valueOf(stopFee.getText().replaceAll(",", ""))));
            items.add(item);
        }
        return items;
    }

    @Override
    protected void getDataToFillFields() {
        tcService.getContractCoursePrices(contractID, new AbstractAsyncCallback<ContractCoursePriceItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(ContractCoursePriceItem[] result) {
                if (result != null) {
                    coursePrices = result;
                    addPriceRows();
                }
            }
        });
    }

    public ColumnConfig[] getColumnConfigs() {
        ColumnConfig[] columnConfigs = new ColumnConfig[4];
        columnConfigs[0] = new ColumnConfig(CustomCell.class, COURSE, tcStrings.courseName(), 400, true);
        columnConfigs[1] = new ColumnConfig(CustomCell.class, LOCATION, Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), 300, true);
        columnConfigs[2] = new ColumnConfig(CustomCell.class, PRICE, wfmStrings.pricePerStudent(), 100, true);
        columnConfigs[3] = new ColumnConfig(CustomCell.class, STOP_FEE, tcStrings.stopFee(), 100, true);
        return columnConfigs;
    }

    public void addPriceRows() {
        for (ContractCoursePriceItem item : coursePrices) {
            coursePriceTable.addRow(drawPriceRows(item));
        }
    }

    public void addNewPrices(ArrayList<ContractCoursePriceItem> items) {
        for (ContractCoursePriceItem item : items) {
            coursePriceTable.addRow(drawPriceRows(item));
        }
    }

    public Object[] drawPriceRows(ContractCoursePriceItem item) {
        Object[] objects = new Object[4];
        EditableTextBox course = new EditableTextBox();
        EditableTextBox location = new EditableTextBox();
        EditableTextBox price = new EditableTextBox();
        EditableTextBox stopFee = new EditableTextBox();
        course.setText(item.getCourseName());
        course.setLayoutData(item.getCourseID());
        location.setText(item.getLocationName());
        location.setLayoutData(item.getObjectID()); // bu objectIDni saqlash uchun faqat
        price.setText(item.getCoursePrice().toString());
        stopFee.setText(item.getStopFee().toString());
        course.setEnabled(false);
        location.setEnabled(false);
        Validation.checkToFocusTextBox(price, AccountingUtils.get().formatUnitPrice(BigDecimal.ZERO));
        Validation.addNumericKeyboardListener(price, AccountingUtils.getUnitPriceScale());
        Validation.checkToFocusTextBox(stopFee, AccountingUtils.get().formatUnitPrice(BigDecimal.ZERO));
        Validation.addNumericKeyboardListener(stopFee, AccountingUtils.getUnitPriceScale());
        objects[0] = course;
        objects[1] = location;
        objects[2] = price;
        objects[3] = stopFee;
        addListeners(price, stopFee);
        return objects;
    }

    public void addListeners(final EditableTextBox price, final EditableTextBox stopFee) {
        price.addChangeHandler(changeEvent -> {
            String uPrice = (price.getValue() != null && !price.getValue().isEmpty()) ?
                    AccountingUtils.get().formatUnitPrice(new BigDecimal(price.getValue())) :
                    AccountingUtils.get().formatUnitPrice(BigDecimal.ZERO);
            price.setText(uPrice);
        });
        stopFee.addChangeHandler(changeEvent -> {
            String uPrice = (stopFee.getValue() != null && !stopFee.getValue().isEmpty()) ?
                    AccountingUtils.get().formatUnitPrice(new BigDecimal(stopFee.getValue())) :
                    AccountingUtils.get().formatUnitPrice(BigDecimal.ZERO);
            stopFee.setText(uPrice);
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PRICE_CHANGE_FORM;
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
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });

    }
}
