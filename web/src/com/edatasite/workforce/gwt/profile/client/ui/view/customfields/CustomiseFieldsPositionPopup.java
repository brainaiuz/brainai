package com.edatasite.workforce.gwt.profile.client.ui.view.customfields;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandlerAdapter;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.WfmContentPanel;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gen2.table.client.PagingOptions;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 19-Nov-2010
 * Time: 14:48:51
 */
public class CustomiseFieldsPositionPopup extends DialogBox implements ClickHandler {

    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private WfmButton2 close;
    private WfmButton2 save;
    private FieldsTool fieldsTool;
    private FlexTable content;

    private boolean change = false;
    private boolean isDefautCustomFieldsToShow;
    private Integer companyID;
    private ArrayList<String> addViewFieldsName;
    private ViewAddFiledsCodeName viewFieldsCode;
    private ArrayList<String> onlyViewShowfieldCodeName;

    /**
     * viewFieldsCode.getFields()[i] = addViewFieldsName.get(i) i=1..n;
     *
     * @param viewFieldsCode    Add View Fields Code Name
     * @param addViewFieldsName Add View Fields Name
     */
    public CustomiseFieldsPositionPopup(Integer companyID, ViewAddFiledsCodeName viewFieldsCode, ArrayList<String> addViewFieldsName, boolean isDefautCustomFieldsToShow) {
        this.companyID = companyID;
        this.viewFieldsCode = viewFieldsCode;
        this.addViewFieldsName = addViewFieldsName;
        this.isDefautCustomFieldsToShow = isDefautCustomFieldsToShow;
        setText(" " + wfmStrings.customize() + " " + viewFieldsCode.name() + " " + settingsStrings.viewFields());
        setModal(true);
        initialization();
    }

    private void initialization() {
        content = new FlexTable();
        content.setCellPadding(5);
        content.setCellSpacing(5);
        content.setSize("400px", "380px");
        setWidget(content);

        fieldsTool = new FieldsTool();

        close = new WfmButton2(wfmStrings.close());
        close.addClickHandler(event -> hide());
        save = new WfmButton2(wfmStrings.save(), (ClickHandler) be -> {
            if (change) {
                save.setEnabled(false);
                close.setEnabled(false);
                LoadingPanel.loading(true);
                CommonService.App.get().saveAddViewPosition(companyID, viewFieldsCode, onlyViewShowfieldCodeName, new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable caught) {
                        LoadingPanel.loading(false);
                        save.setEnabled(true);
                        close.setEnabled(true);
                    }

                    @Override
                    public void success(Void result) {
                        LoadingPanel.loading(false);
                        save.setEnabled(true);
                        close.setEnabled(true);
                        change = false;
                        hide();
                    }
                });
            }
        });
        save.setEnabled(false);
        close.setEnabled(false);

        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setSpacing(10);
        buttonPanel.add(save);
        buttonPanel.add(close);

        content.setWidget(0, 0, fieldsTool);
        content.setWidget(1, 0, buttonPanel);
        content.getFlexCellFormatter().setAlignment(1, 0, HorizontalPanel.ALIGN_RIGHT, VerticalPanel.ALIGN_MIDDLE);

        getAddViewSettings();
    }

    private void getAddViewSettings() {
        LoadingPanel.loading(true);
        CommonService.App.get().getCompanyAddViewFieldsPosition(companyID, viewFieldsCode, new AbstractAsyncCallback<ListPanelToolRpc>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                save.setEnabled(true);
                close.setEnabled(true);
            }

            @Override
            public void success(ListPanelToolRpc result) {
                LoadingPanel.loading(false);
                save.setEnabled(true);
                close.setEnabled(true);
                fieldsTool.showFieldsTools(result);
            }
        });
    }

    @Override
    public void onClick(ClickEvent event) {
        //  hide();
    }
//Fields show and order table

    protected class FieldsTool extends FlexTable {

        private final PagingOptions.PagingOptionsImages settingsBundle = GWT.create(PagingOptions.PagingOptionsImages.class);

        private Image prev;
        private VerticalPanel showVerticalPanel;
        private VerticalPanel showFieldOrder;
        private PickupDragController showColumnDragController;
        private WfmContentPanel showColumns;
        private WfmContentPanel allColumns;

        private List<String> fieldCodeNameList = new ArrayList<>();// Field Code Name
        private Map<String, String> fieldCodeNameMap = new HashMap<>();// Field Code Name,Field Name
        private Map<String, KpiCheckBox> allFieldCheckMap = new HashMap<>();// Field Code Name,Checked
        private Map<String, CustomFieldLabel> showColumnMap = new HashMap<>();// Field Code Name, Field Label
        private Map<String, Integer> widgetsPos = new HashMap<>();// Field Name,Field Position

        public FieldsTool() {
            initilazation();
        }

        private void initilazation() {
            onlyViewShowfieldCodeName = new ArrayList<>();

            showColumns = new WfmContentPanel();
            showColumns.setSize("220px", "300px");
            showColumns.setCaptionLeftHTML(settingsStrings.viewShowFields());
            allColumns = new WfmContentPanel();
            allColumns.setSize("220px", "300px");
            allColumns.setCaptionLeftHTML(settingsStrings.viewAllFields());
            prev = new Image(settingsBundle.pagingOptionsPrevPage());
            setCellPadding(0);
            setCellSpacing(3);

            setWidget(0, 0, showColumns);
            getFlexCellFormatter().setAlignment(0, 0, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_TOP);
            setWidget(0, 1, prev);
            getFlexCellFormatter().setAlignment(0, 1, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_MIDDLE);
            setWidget(0, 2, allColumns);
            getFlexCellFormatter().setAlignment(0, 2, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_TOP);
        }

        /**
         * @param addViewTools
         */
        public void showFieldsTools(ListPanelToolRpc addViewTools) {
            String[] fieldCodeNameArray = viewFieldsCode.getFields();
            for (int i = 0; i < fieldCodeNameArray.length; i++) {
                fieldCodeNameList.add(i, fieldCodeNameArray[i]);
                fieldCodeNameMap.put(fieldCodeNameArray[i], addViewFieldsName.get(i));
            }
            if (addViewTools.getListViewCustomFields() != null) {
                for (int i = 0; i < addViewTools.getListViewCustomFields().size(); i++) {
                    fieldCodeNameList.add(addViewTools.getListViewCustomFields().get(i).getColumnCode());
                    fieldCodeNameMap.put(addViewTools.getListViewCustomFields().get(i).getColumnCode(), addViewTools.getListViewCustomFields().get(i).getFieldName());
                }
            }
            if (addViewTools.getColumnCodeName() != null && addViewTools.getColumnCodeName().size() != 0) {
                setShowOnlyFieldList(addViewTools.getColumnCodeName());
            } else {
                if (isDefautCustomFieldsToShow) {
                    setShowOnlyFieldList(fieldCodeNameList);
                } else {
                    List<String> fieldCodeName = new ArrayList<>();
                    for (int i = 0; i < fieldCodeNameArray.length; i++) {
                        fieldCodeName.add(i, fieldCodeNameArray[i]);
                    }
                    setShowOnlyFieldList(fieldCodeName);
                }
            }
            initShowAllColumn();
            initOnlyShowColumn();
        }

        /**
         * Set View Show Only Fileds Code Name
         *
         * @param fieldCodeName
         */
        private void setShowOnlyFieldList(List<String> fieldCodeName) {
            for (int i = 0; i < fieldCodeName.size(); i++) {
                onlyViewShowfieldCodeName.add(i, fieldCodeName.get(i));
            }
        }

        /**
         * All Columns
         */
        public void initShowAllColumn() {
            VerticalPanel allColumnPanel = new VerticalPanel();
            allColumns.add(allColumnPanel);
            for (int i = 0; i < fieldCodeNameList.size(); i++) {
                final int index = i;

                final KpiCheckBox check = new KpiCheckBox();
                check.setValue(onlyViewShowfieldCodeName.contains(fieldCodeNameList.get(i)));
                check.addClickHandler(event -> {
                    if (check.getValue()) {
                        if (!showColumnMap.containsKey(fieldCodeNameList.get(index))) {
                            allFieldCheckMap.put(fieldCodeNameList.get(index), check);
                            addNewColumn(fieldCodeNameList.get(index));
                            change = true;
                        }
                    } else if (showColumnMap.size() != 1) {
                        if (allFieldCheckMap.containsKey(fieldCodeNameList.get(index))) {
                            removeUnCheckColumn(fieldCodeNameList.get(index));
                            change = true;
                        }
                    } else {
                        check.setValue(true);
                    }
                });

                allFieldCheckMap.put(fieldCodeNameList.get(i), check);

                CustomFieldLabel label = getColumnWidget(fieldCodeNameMap.get(fieldCodeNameList.get(i)));

                HorizontalPanel panel = new HorizontalPanel();
                panel.setSpacing(3);
                panel.add(check);
                panel.setCellVerticalAlignment(check, VerticalPanel.ALIGN_MIDDLE);
                panel.add(label);
                panel.setCellVerticalAlignment(label, VerticalPanel.ALIGN_MIDDLE);

                allColumnPanel.add(panel);
            }
        }

        /**
         * IsShow=true Shows Columns
         */
        public void initOnlyShowColumn() {

            AbsolutePanel boundaryPanel = new AbsolutePanel();
            boundaryPanel.getElement().getStyle().setOverflow(Style.Overflow.VISIBLE);
            boundaryPanel.setSize("100%", "100%");
//            boundaryPanel.getElement().getStyle().setOverflow(Style.Overflow.AUTO);

            // initialize vertical panel to hold our columns
            showVerticalPanel = new VerticalPanel();
            showVerticalPanel.setWidth("100%");
            showVerticalPanel.setSpacing(0);

            showFieldOrder = new VerticalPanel();
            showFieldOrder.setWidth("8%");
            showFieldOrder.setSpacing(0);

            HorizontalPanel showPanel = new HorizontalPanel();
            showPanel.add(showFieldOrder);
            showPanel.add(boundaryPanel);

            boundaryPanel.add(showVerticalPanel);
            showColumns.add(showPanel);

            // initialize our column drag controller
            showColumnDragController = new PickupDragController(boundaryPanel, false);
            showColumnDragController.setBehaviorMultipleSelection(false);

            // initialize our column drop controller
            VerticalPanelDropController columnDropController = new VerticalPanelDropController(showVerticalPanel);
            showColumnDragController.registerDropController(columnDropController);
            showColumnDragController.addDragHandler(new DragHandlerAdapter() {
                @Override
                public void onDragEnd(DragEndEvent event) {
                    CustomFieldLabel label = (CustomFieldLabel) event.getSource();
                    int nowPos = showVerticalPanel.getWidgetIndex(label);
                    int oldPos = widgetsPos.get(label.getFeildCodeName());
                    if (nowPos != oldPos) {
                        change = true;
                        moveColumnUserPut(nowPos, oldPos, label.getFeildCodeName());
                    }
                }

            });

            for (String fieldCode : onlyViewShowfieldCodeName) {
                if (fieldCodeNameList.contains(fieldCode)) {
                    createNewColumnPanel(fieldCode);
                } else {
                    onlyViewShowfieldCodeName.remove(fieldCode);
                }
            }
        }

        /**
         * Change move column position
         *
         * @param nowPos
         * @param oldPos
         */
        private void moveColumnUserPut(int nowPos, int oldPos, String moveFieldCodeName) {
            String codeName = onlyViewShowfieldCodeName.get(oldPos);
            String[] columnCodeName = onlyViewShowfieldCodeName.toArray(new String[]{});
            int arg;
            if (nowPos > oldPos) {
                System.arraycopy(columnCodeName, oldPos + 1, columnCodeName, oldPos, nowPos - oldPos);
                arg = -1;
                columnCodeName[nowPos] = codeName;
            } else {
                System.arraycopy(columnCodeName, nowPos, columnCodeName, nowPos + 1, oldPos - nowPos);
                arg = 1;
                columnCodeName[nowPos] = codeName;
            }
            ArrayList<String> listCodeName = new ArrayList<>();
            listCodeName.addAll(Arrays.asList(columnCodeName));
            onlyViewShowfieldCodeName = listCodeName;
            widgetsPos.put(codeName, nowPos);
            for (String keyName : widgetsPos.keySet()) {
                if (!moveFieldCodeName.equals(keyName)) {
                    if (nowPos > oldPos && widgetsPos.get(keyName) > oldPos && widgetsPos.get(keyName) <= nowPos) {
                        widgetsPos.put(keyName, widgetsPos.get(keyName) + arg);
                    } else if (nowPos < oldPos && widgetsPos.get(keyName) >= nowPos && widgetsPos.get(keyName) < oldPos) {
                        widgetsPos.put(keyName, widgetsPos.get(keyName) + arg);
                    }
                }
            }
        }


        /**
         * Revome Unchecked column in the list
         *
         * @param columnConfig
         */
        private void removeUnCheckColumn(String fieldCodeName) {
            change = true;
            onlyViewShowfieldCodeName.remove(fieldCodeName);

            allFieldCheckMap.remove(fieldCodeName);
            Widget widget = showColumnMap.get(fieldCodeName);

            showVerticalPanel.remove(widget);
            showFieldOrder.remove(widgetsPos.size() - 1);
            showColumnMap.remove(fieldCodeName);

            int pos = widgetsPos.get(fieldCodeName);
            widgetsPos.remove(fieldCodeName);
            for (String keyName : widgetsPos.keySet()) {
                if (widgetsPos.get(keyName) > pos) {
                    widgetsPos.put(keyName, widgetsPos.get(keyName) - 1);
                }
            }
        }

        /**
         * add New Column
         *
         * @param columnConfig
         */
        private void addNewColumn(String fieldCodeName) {
            change = true;
            createNewColumnPanel(fieldCodeName);
            onlyViewShowfieldCodeName.add(fieldCodeName);
        }

        /**
         * Create new List Column
         *
         * @param columnCode
         */
        private void createNewColumnPanel(String fieldCodeName) {
            CustomFieldLabel label = getColumnWidget(fieldCodeNameMap.get(fieldCodeName));
            label.setFeildCodeName(fieldCodeName);
            label.setStyleName("wfm-listing-panel-label");

            showColumnMap.put(fieldCodeName, label);
            widgetsPos.put(label.getFeildCodeName(), widgetsPos.size());
            showVerticalPanel.add(label);
            showColumnDragController.makeDraggable(label);

            Label orderLabel = new Label(String.valueOf(widgetsPos.size()));
            orderLabel.setStyleName("wfm-listing-panel-label");
            showFieldOrder.add(orderLabel);
        }

        private CustomFieldLabel getColumnWidget(Object fieldName) {
            if (fieldName instanceof Label) {
                return new CustomFieldLabel(((Label) fieldName).getText(), false);
            }

            return new CustomFieldLabel(fieldName.toString(), false);
        }
    }
}
