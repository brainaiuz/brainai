package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.MergeItem;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.gen2.table.override.client.FlexTable;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 6/30/11
 * Time: 3:36 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class MergeAbstractView<T> extends View implements Constants, Colapse {
    protected final static WfmStrings wfmStrings = WfmStrings.App.get();
    protected final static String MAINITEM = "MAINITEM";
    protected FlexTable table = new FlexTable();
    protected HashMap<Integer, T> mapOfRPCs = new LinkedHashMap<>();
    private int numberOfRPCs;
    private T mainItem;
    private final HashMap<String, List<String>> fieldNames = new HashMap<>();
    private final HashMap<String, ArrayList<CheckBox>> radioButtons = new HashMap<>();
    private final HashMap<String, ArrayList<CheckBox>> allInOneRadioButtons = new HashMap<>();
    private ArrayList<T> arrayListOfItems;

    protected MergeAbstractView(String name, String description) {
        super(name, description);
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    protected void setItems(ArrayList<T> items) {
        mapOfRPCs.clear();
        this.arrayListOfItems = items;
        if (items != null && items.size() > 0) {
            setMainItem(items.get(0));
            if (items.size() > 1) {
                for (T item : items) {
                    if (items.indexOf(item) > 0) {
                        mapOfRPCs.put(getItemObjectID(item), item);
                    }
                }
            }
        }
        initView();
        table.setStyleName("mergeTable");
    }

    protected abstract Integer getItemObjectID(T item);

    private void initView() {
        drawHeader();
    }

    private void drawHeader() {
        VerticalPanel vp = new VerticalPanel();
        vp.setSpacing(10);
        vp.setWidth("100%");
        vp.add(new HTML("<font size=\"+1\" class = \"customTitle\">" + wfmStrings.mergeRecords() + "</font>"));
        vp.add(new HTML("<font style = \"font:arial 12px; color:black;\"> </font>"));
        vp.add(new HTML("&nbsp;"));
        FlowPanel div = new FlowPanel();
        div.getElement().setId("mergeFieldsTable");
        FlexTable vp2 = new FlexTable();
        vp2.setStyleName("fieldsTable");
        vp2.setWidget(1, 0, table);
        div.add(vp2);
        vp.add(div);

        drawAllFields();
        Scheduler.get().scheduleDeferred(() -> {
            setSelectedFirst();
        });
//        setSelectedFirst();
        HTMLPanel panel = new HTMLPanel("");
        panel.setStyleName("add-form");
        panel.add(vp);
        panel.add(createFooter());
        add(panel);
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return null;
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return MergeAbstractView.this.getFooterRightSideWidgets();
            }
        });
    }

    public List<Widget> getFooterRightSideWidgets() {
        List<Widget> rightSideWidgets = new ArrayList<>();
        WfmButton2 saveAndClose = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveAndClose.addClickHandler(clickEvent -> merge());
        Div saveButtonWrapper = new Div();
        saveButtonWrapper.add(saveAndClose);
        rightSideWidgets.add(saveButtonWrapper);

        return rightSideWidgets;
    }

    private void addButtons() {
        MaterialPanel buttonList = new MaterialPanel("btns-group");
//            buttonList.add(btnSaveAndClose);
        WfmButton2 saveAndClose = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveAndClose.addClickHandler(clickEvent -> merge());
        buttonList.add(saveAndClose);
        WfmButton2 close = new WfmButton2(wfmStrings.close());
        close.addClickHandler(clickEvent -> closeTab());
        buttonList.add(close);

        MainLayout.get().addToActionsContainer(buttonList);
        MainLayout.get().makeFrameContainerHaveTabsStyle(true);
    }

    private void setSelectedFirst() {
        String value = MAINITEM + "_" + getItemObjectID(arrayListOfItems.get(1));
        if (radioButtons.containsKey(value) && radioButtons.get(value).size() > 0 && radioButtons.get(value).get(0) != null) {
            radioButtons.get(value).get(0).setValue(Boolean.TRUE, true);
        }
    }

    protected abstract void merge();

    protected abstract void drawAllFields();

    protected abstract ArrayList<MergeItem> getMergeItems(String fieldName);

    //methods.
    protected void addFieldInOneRow(final String fieldName, final String parentFieldName, final String leftTitle, boolean... isCheckBox) {
        addFieldInOneRow(fieldName, parentFieldName, leftTitle, getMergeItems(fieldName), isCheckBox);
    }

    protected void addFieldInOneRow(final String fieldName, final String parentFieldName, final String leftTitle, ArrayList<MergeItem> items, boolean... isCheckBox) {
        if (parentFieldName != null && fieldNames.containsKey(parentFieldName)) {
            fieldNames.get(parentFieldName).add(fieldName);
        }
        final boolean useCheckBox = isCheckBox != null && isCheckBox.length > 0 && isCheckBox[0];
        if (!fieldNames.containsKey(fieldName)) {
            fieldNames.put(fieldName, new ArrayList<>());
            fieldNames.get(fieldName).add(fieldName);
        }
        int row = table.getRowCount();
        table.setHTML(row, 0, "<font class = \"customTitle\">" + leftTitle + "</font>");
        if (MAINITEM.equals(fieldName)) {
            table.getRowFormatter().setStyleName(row, "subtitle1");
        }
        table.getCellFormatter().setWidth(row, 0, "200px");
        int i = 1;
        for (final MergeItem item : items) {
            if (item.isManyResults()) {
                i = addMultiRowToTable(i, row, item, fieldName, useCheckBox);
            } else {
                i = addSingleRowToTable(i, row, item, fieldName, useCheckBox);
            }
        }
    }

    private int addSingleRowToTable(int i, int row, final MergeItem item, final String fieldName, final boolean useCheckBox) {
        final CheckBox button = useCheckBox ? new KpiCheckBox() : new KpiRadioButton(fieldName);
        button.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                onValueChanged(fieldName, item, event.getValue(), (CheckBox) event.getSource());
            }
        });
        String key = fieldName + "_" + item.getItemObjectID().toString();
        radioButtons.put(key, getWidgetInArrayList(radioButtons.get(key), button));
        if (item.isManyResults()) {
            allInOneRadioButtons.put(fieldName, getWidgetInArrayList(allInOneRadioButtons.get(fieldName), button));
        }
        table.setWidget(row, i++, button);
        table.getCellFormatter().setWidth(row, i - 1, "30px");
        HTML value = new HTML(item.isManyResults() ? wfmStrings.selectAll() : item.getValue());
        value.addClickHandler(clickEvent -> button.setValue(Boolean.TRUE, true));
        table.setWidget(row, i++, value);
        table.getCellFormatter().setWidth(row, i - 1, "200px");
        if (item.isManyResults()) {
            table.getRowFormatter().setStyleName(row, "subtitle3");
        }
        return i;
    }

    private ArrayList<CheckBox> getWidgetInArrayList(ArrayList<CheckBox> checkBoxes, CheckBox button) {
        if (checkBoxes == null) {
            checkBoxes = new ArrayList<>();
        }
        checkBoxes.add(button);
        return checkBoxes;
    }

    private int addMultiRowToTable(int i, int row, final MergeItem item, final String fieldName, final boolean useCheckBox) {
        if (item.isManyResults() && item.getChildren().size() > 0) {
            addSingleRowToTable(i, row, item, fieldName, useCheckBox);
            int s_i = i;
            row++;
            String key = fieldName + "_" + item.getItemObjectID().toString();
            for (final MergeItem childItem : item.getChildren()) {
                i = s_i;
                final CheckBox button = useCheckBox ? new KpiCheckBox() : new KpiRadioButton(fieldName);
                button.addValueChangeHandler(booleanValueChangeEvent -> onValueChanged(fieldName, childItem, booleanValueChangeEvent.getValue(), (CheckBox) booleanValueChangeEvent.getSource()));
                radioButtons.put(key, getWidgetInArrayList(radioButtons.get(key), button));
                table.setWidget(row, i++, button);
                table.getCellFormatter().setWidth(row, i - 1, "30px");
                HTML value = new HTML(childItem.getValue());
                value.addClickHandler(clickEvent -> button.setValue(Boolean.TRUE, true));
                table.setWidget(row, i++, value);
                table.getCellFormatter().setWidth(row, i - 1, "200px");
                row++;
            }
        } else {
            return addSingleRowToTable(i, row, item, fieldName, useCheckBox);
        }
        return i;
    }

    //do not forget this method must work recursively....
    protected void onValueChanged(String fieldName, MergeItem item, Boolean value, CheckBox source) {
        if (fieldName != null && getFieldNames().containsKey(fieldName)) {
            if (!item.isManyResults()) {
                changeByMergeItem(fieldName, item, value);
            }
            if (getFieldNames().get(fieldName).size() > 0) {
                for (String childFieldName : getFieldNames().get(fieldName)) {
                    childFieldName += "_" + item.getItemObjectID().toString();
                    if (getRadioButtons().containsKey(childFieldName)) {
                        if (getRadioButtons().get(childFieldName) != null && getRadioButtons().get(childFieldName).size() > 0 && !getRadioButtons().get(childFieldName).contains(source)) {
                            for (CheckBox checkBox : getRadioButtons().get(childFieldName)) {
                                checkBox.setValue(value, true);
                            }
                        }
                    }
                }
            }
        }

    }

    protected abstract void changeByMergeItem(String fieldName, MergeItem item, Boolean value);

    protected void merged() {
        closeTab();
    }

    public T getMainItem() {
        return mainItem;
    }

    public void setMainItem(T mainItem) {
        this.mainItem = mainItem;
    }

    public int getNumberOfRPCs() {
        return numberOfRPCs;
    }

    public void setNumberOfRPCs(int numberOfRPCs) {
        this.numberOfRPCs = numberOfRPCs;
    }

    public Map<String, List<String>> getFieldNames() {
        return fieldNames;
    }

    public Map<String, ArrayList<CheckBox>> getRadioButtons() {
        return radioButtons;
    }

    public Map<String, ArrayList<CheckBox>> getAllInOneRadioButtons() {
        return allInOneRadioButtons;
    }
}
