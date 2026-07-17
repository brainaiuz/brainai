package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.MergeItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.google.gwt.gen2.table.override.client.FlexTable;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

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
    private T mainItem;
    private final HashMap<String, List<String>> fieldNames = new HashMap<>();
    private final HashMap<String, ArrayList<CheckBox>> radioButtons = new HashMap<>();
    private final HashMap<String, ArrayList<CheckBox>> allInOneRadioButtons = new HashMap<>();
    private ArrayList<T> arrayListOfItems;
    private final CrmStrings crmStrings = CrmStrings.App.get();


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
        vp2.setWidget(0, 0, new HTML("<font size=\"+1\" class = \"customTitle\">" + crmStrings.masterRecordAndFieldSelection() + "</font>"));
        vp2.getRowFormatter().addStyleName(0, "subtitle");
        vp2.setWidget(1, 0, table);
        div.add(vp2);
        vp.add(div);
        add(vp);
        drawAllFields();
        setSelectedFirst();
        HorizontalPanel hp = new HorizontalPanel();
        hp.setWidth("200px");
        WfmButton2 saveAndClose = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveAndClose.ensureDebugId("saveAndClose");
        saveAndClose.addClickHandler(clickEvent -> merge());
        hp.add(saveAndClose);
        WfmButton2 close = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT);
        close.ensureDebugId("close_button");
        close.addClickHandler(clickEvent -> closeTab());
        hp.add(close);
        vp.add(hp);
        vp.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_CENTER);
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
            KpiCheckBox button = null;
            if (item.isManyResults()) {
                i = addMultiRowToTable(i, row, item, fieldName, useCheckBox);
            } else {
                i = addSingleRowToTable(i, row, item, null, fieldName, useCheckBox);
            }
        }
    }

    private int addSingleRowToTable(int i, int row, final MergeItem item, final MergeItem parent, final String fieldName, final boolean useCheckBox) {
        final CheckBox button = useCheckBox ? new CheckBox() : new KpiRadioButton(fieldName);
        button.addValueChangeHandler(booleanValueChangeEvent -> onValueChanged(fieldName, item, booleanValueChangeEvent.getValue(), parent == null ? null : (CheckBox) booleanValueChangeEvent.getSource()));
        radioButtons.put(fieldName + "_" + (parent != null ? parent : item).getItemObjectID().toString(), getWidgetInArrayList(radioButtons.get(fieldName + "_" + (parent != null ? parent : item).getItemObjectID().toString()), button));
        if (parent == null && item.isManyResults()) {
            allInOneRadioButtons.put(fieldName, getWidgetInArrayList(allInOneRadioButtons.get(fieldName), button));
        }
        table.setWidget(row, i++, button);
        table.getCellFormatter().setWidth(row, i - 1, "30px");
        HTML value = new HTML(parent == null && item.isManyResults() ? wfmStrings.selectAll() : item.getValue());
        value.addClickHandler(clickEvent -> button.setValue(Boolean.TRUE, true));
        table.setWidget(row, i++, value);
        table.getCellFormatter().setWidth(row, i - 1, "200px");
        if (parent == null && item.isManyResults()) {
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

    private int addMultiRowToTable(int i, int row, final MergeItem parent, final String fieldName, final boolean useCheckBox) {
        if (parent.isManyResults() && parent.getChildren().size() > 0) {
            addSingleRowToTable(i, row, parent, null, fieldName, useCheckBox);
            int s_i = i;
            row++;
            for (final MergeItem item : parent.getChildren()) {
                i = s_i;
                addSingleRowToTable(i, row, item, parent, fieldName, useCheckBox);
                row++;
            }
        } else {
            return addSingleRowToTable(i, row, parent, null, fieldName, useCheckBox);
        }
        return i;
    }

    //do not forget this method must work recursively....
    protected abstract void onValueChanged(String type, MergeItem item, Boolean value, CheckBox source);

    protected void merged() {
        closeTab();
    }

    public T getMainItem() {
        return mainItem;
    }

    public void setMainItem(T mainItem) {
        this.mainItem = mainItem;
    }

    public Map<String, List<String>> getFieldNames() {
        return fieldNames;
    }

    public Map<String, ArrayList<CheckBox>> getRadioButtons() {
        return radioButtons;
    }
}
