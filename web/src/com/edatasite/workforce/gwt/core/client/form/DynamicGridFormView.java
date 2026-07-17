package com.edatasite.workforce.gwt.core.client.form;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.ColumnType;
import com.edatasite.workforce.gwt.core.client.form.panel.DynamicGridInactivePanel;
import com.edatasite.workforce.gwt.core.client.form.panel.DynamicGridItemPanel;
import com.edatasite.workforce.gwt.core.client.form.panel.DynamicGridPanel;
import com.edatasite.workforce.gwt.core.client.form.panel.GridUIUtils;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSwitch;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_ITEM_TABLE;
import static gwt.material.design.jquery.client.api.JQuery.$;

public class DynamicGridFormView extends CustomForm implements FittedContent, Colapse {

    private AbsolutePanel absolutePanel;
    private final String formID;
    private final Integer formItemId;
    private final HashMap<String, DynamicGridPanel> activePanelMap = new HashMap<>();
    private final HashMap<String, DynamicGridInactivePanel> inactivePanelMap = new HashMap<>();
    private final HashMap<String, SectionHeader> sectionHeaderMap = new HashMap<>();
    private final HashMap<String, DynamicGridItemPanel> gridItemMap = new HashMap<>();
    private final String path;
    private HashMap<String, DynamicSectionsRpc> sections;
    private CustomFieldsBar fieldsBar;
    private SectionSideNavBox orderModal;
    private WfmMessageBox errorModal;
    private String entityName;
    private DynamicGridFormHelper formHelper;

    public DynamicGridFormView(String formID, String path, Integer formItemId) {
        super(formID, wfmStrings.customizeForm());
        this.formID = formID;
        this.path = path != null ? URL.decodeQueryString(path) : "";
        this.formItemId = formItemId;
        Utils.enableLeftMenu(false);
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        AllInOneService.App.get().getCustomizeFormSections(formID, true, new AbstractAsyncCallback<LinkedHashMap<String, DynamicSectionsRpc>>() {
            @Override
            public void success(LinkedHashMap<String, DynamicSectionsRpc> result) {
                if (result != null) {
                    sections = result;

                    initialize();
                }
            }
        });

        return null;
    }

    private void initialize() {

        this.formHelper = this.getFormHelper();

        fieldsBar = new CustomFieldsBar(formID);

        orderModal = new SectionSideNavBox(formID);
        orderModal.setCommand(() -> {
            clear();
            onInitialize();
        });

        orderModal.setDynamicColumn(this::updateSectionSwitch);

        errorModal = new WfmMessageBox(IconEnum.WARN, Action.OK, true);
        errorModal.setTitle(wfmStrings.confirmation());
        errorModal.setMessage("Not enough free space to add the field");

        absolutePanel = new AbsolutePanel();
        absolutePanel.setSize("100%", "100%");
        addField(CustomFormConstants.CONTENT, absolutePanel);
        addField(CustomFormConstants.ADDITIONAL_SETTINGS, fieldsBar);

        for (Map.Entry<String, DynamicSectionsRpc> sectionEntry : sections.entrySet()) {
            String section = sectionEntry.getKey();

            MaterialPanel panel = new MaterialPanel("panel-w-switch");
            MaterialPanel switchHeader = new MaterialPanel("panel-w-switch__header");
            panel.add(switchHeader);

            SectionHeader sectionHeader = new SectionHeader(section);
            switchHeader.add(sectionHeader);
            sectionHeaderMap.put(section, sectionHeader);

            DynamicGridInactivePanel inactivePanel = new DynamicGridInactivePanel(section, formHelper);
            switchHeader.add(inactivePanel);
            inactivePanelMap.put(section, inactivePanel);

            DynamicGridPanel dynamicGridPanel = new DynamicGridPanel(section, formHelper);
            activePanelMap.put(section, dynamicGridPanel);
            switchHeader.add(dynamicGridPanel);

            absolutePanel.add(panel);
        }
        this.getEntityName();
        this.getCustomizeForm();
    }

    private DynamicGridFormHelper getFormHelper() {
        return new DynamicGridFormHelper() {
            @Override
            public DynamicGridItemPanel getItemById(String elementId) {
                return gridItemMap.get(elementId);
            }

            @Override
            public void removeDroppedItem(String elementId, Boolean fromInactive) {
                if (fromInactive == null) {
                    return;
                }
                DynamicGridItemPanel itemPanel = gridItemMap.get(elementId);
                if (itemPanel == null || itemPanel.getField() == null) {
                    return;
                }
                if (fromInactive) {
                    DynamicGridInactivePanel inactivePanel = inactivePanelMap.get(itemPanel.getField().getSection());
                    inactivePanel.getGridItemMap().remove(elementId);
                } else {
                    DynamicGridPanel activePanel = activePanelMap.get(itemPanel.getField().getSection());
                    activePanel.getGridItemMap().remove(elementId);
                }
            }

            @Override
            public void itemAdded(DynamicGridItemPanel itemPanel) {
                if (itemPanel == null) {
                    return;
                }
                if (gridItemMap.get(itemPanel.getElementId()) != null) {
                    gridItemMap.remove(itemPanel.getElementId());
                    gridItemMap.put(itemPanel.getElementId(), itemPanel);
                }
            }
        };
    }

    private void getEntityName() {
        LoadingPanel.loading(true);
        AllInOneService.App.get().getModelEntityName(formID, new AbstractAsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(String result) {
                entityName = result;
                if (entityName != null) {
                    sectionHeaderMap.values().forEach(item -> item.showAddButton());
                }
                LoadingPanel.loading(false);
            }
        });
    }

    private void getCustomizeForm() {
        LoadingPanel.loading(true);
        AllInOneService.App.get().getCustomizeGridForm(formID, new AbstractAsyncCallback<LinkedHashMap<String, LinkedList<CustomizeFormItem>>>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(LinkedHashMap<String, LinkedList<CustomizeFormItem>> result) {
                GWT.runAsync(new RunAsyncCallback() {
                    @Override
                    public void onFailure(Throwable throwable) {
                    }

                    @Override
                    public void onSuccess() {
                        result.forEach(DynamicGridFormView.this::fillSection);
                    }
                });
                LoadingPanel.loading(false);
            }
        });
    }

    private void fillSection(String section, LinkedList<CustomizeFormItem> fields) {
        int size = fields.size();
        if (inactivePanelMap.get(section) != null) {
            DynamicGridInactivePanel inactivePanel = inactivePanelMap.get(section);

            fields.stream().filter(f -> !f.isActive()).forEach(field -> {
                if (!field.isCustomField()) {
                    field.setLabel(Localize.getInstance().localizeByFieldID(formID, field.getName()));
                }
                DynamicGridItemPanel itemPanel = new DynamicGridItemPanel();
                DynamicField2 fieldWidget = new DynamicField2(field, formID, false);
                itemPanel.initConfig(field);
                itemPanel.setContent(fieldWidget);
                itemPanel.setField(fieldWidget);
                gridItemMap.put(itemPanel.getElementId(), itemPanel);
                inactivePanel.addNewItem(itemPanel, false);

                fieldWidget.setInactiveCommand(() -> inactivateField(section, itemPanel.getElementId()));
            });
            inactivePanel.commit();
        }
        if (sectionHeaderMap.get(section) != null) {
            sectionHeaderMap.get(section).setColumnCount(size);
        }
        if (activePanelMap.get(section) != null) {
            DynamicGridPanel dynamicGridPanel = activePanelMap.get(section);
            drawColumns(fields, dynamicGridPanel, section);
        }
    }

    private void drawColumns(LinkedList<CustomizeFormItem> fields, DynamicGridPanel dynamicGridPanel, String section) {
        fields.stream().filter(CustomizeFormItem::isActive).forEach(field -> {
            if (!field.isCustomField()) {
                field.setLabel(Localize.getInstance().localizeByFieldID(formID, field.getName()));
            }
            DynamicGridItemPanel itemPanel = new DynamicGridItemPanel();
            DynamicField2 fieldWidget = new DynamicField2(field, formID, true);
            itemPanel.initConfig(field);
            itemPanel.setContent(fieldWidget);
            itemPanel.setField(fieldWidget);
            gridItemMap.put(itemPanel.getElementId(), itemPanel);
            dynamicGridPanel.addNewItem(itemPanel, false);
            if (itemPanel.getWidth() < 2) {
                new KpiToolTip(fieldWidget, field.getLabel());
            }

            fieldWidget.setInactiveCommand(() -> inactivateField(section, itemPanel.getElementId()));
        });
        dynamicGridPanel.commit();
    }

    private void inactivateField(String section, String itemElementId) {
        HTML html = new HTML(wfmStrings.areYouSureWantToDeleteThe() + " " + wfmStrings.field());
        html.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);

        WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        wfmMessageBox.setTitle(wfmStrings.confirmationMessage());
        wfmMessageBox.setMessage(wfmStrings.inactivateConfirmationMessage());
        wfmMessageBox.setContent(html);
        wfmMessageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onSubmit() {
                DynamicGridPanel activePanel = activePanelMap.get(section);
                DynamicGridItemPanel itemPanel = gridItemMap.get(itemElementId);
                if (activePanel != null && activePanel.getGridItemMap().get(itemPanel.getElementId()) != null
                        && itemPanel.getField() != null && (!itemPanel.getField().getField().isSystemMandatory())) {
                    activePanel.removeItem(itemPanel);

                    DynamicGridInactivePanel inactivePanel = inactivePanelMap.get(section);
                    inactivePanel.addNewItem(itemPanel, true);

                    itemPanel.getField().setSection(section);
                    itemPanel.getField().setActive(false);

                    focusUnusedFieldPanel(section);
                }
            }
        });
        wfmMessageBox.open();
    }


    private void addFieldToSection(String section, SelectItem item) {
        DynamicGridPanel dynamicGridPanel = activePanelMap.get(section);

        if (!dynamicGridPanel.hasWillItFit()) {
            errorModal.open();
            return;
        }
        CustomizeFormItem field = new CustomizeFormItem();
        field.setSection(section);
        field.setColumnType(ColumnType.COL_1);
        field.setName(item.getName());
        field.setLabel(item.getName());
        field.setCustomField(true);
        field.setUiType(item.getDescription());
        field.setEntityName(entityName);
        if (UI_TYPE_ITEM_TABLE.equals(item.getDescription())) {
            field.setWidth(12);
        }

        DynamicField2 dynamicField = new DynamicField2(field, formID, true);
        dynamicField.setSection(section);

        DynamicGridItemPanel itemPanel = new DynamicGridItemPanel();
        itemPanel.initConfig(field);
        itemPanel.setContent(dynamicField);
        itemPanel.setField(dynamicField);
        gridItemMap.put(itemPanel.getElementId(), itemPanel);
        dynamicGridPanel.addNewItem(itemPanel, false);

        dynamicField.setInactiveCommand(() -> inactivateField(section, itemPanel.getElementId()));
    }

    private void focusUnusedFieldPanel(String section) {
        DOM.getElementById("id_" + section.toLowerCase()).addClassName("action--attention");
        Timer timer = new Timer() {
            @Override
            public void run() {
                Element element = DOM.getElementById("id_" + section.toLowerCase());
                if (element != null) {
                    element.removeClassName("action--attention");
                }
            }
        };
        timer.schedule(2500);
    }

    private void save() {
        LoadingPanel.loading(true);

        LinkedHashMap<String, LinkedList<CustomizeFormItem>> activeFieldMap = new LinkedHashMap<>();
        LinkedHashMap<String, LinkedList<CustomizeFormItem>> inactiveFieldMap = new LinkedHashMap<>();
        for (Map.Entry<String, DynamicGridItemPanel> entry : gridItemMap.entrySet()) {
            DynamicField2 fieldWidget = entry.getValue().getField();

            CustomizeFormItem field = fieldWidget.getField();
            field.setSection(fieldWidget.getSection());
            field.setX(entry.getValue().getX());
            field.setY(entry.getValue().getY());
            field.setWidth(entry.getValue().getWidth());
            field.setHeight(entry.getValue().getHeight());
            if (field.isActive()) {
                if (activeFieldMap.get(fieldWidget.getSection()) == null) {
                    LinkedList<CustomizeFormItem> fields = new LinkedList<>();
                    fields.add(field);
                    activeFieldMap.put(fieldWidget.getSection(), fields);
                } else {
                    activeFieldMap.get(fieldWidget.getSection()).add(field);
                }
            } else {
                if (inactiveFieldMap.get(fieldWidget.getSection()) == null) {
                    LinkedList<CustomizeFormItem> fields = new LinkedList<>();
                    fields.add(field);
                    inactiveFieldMap.put(fieldWidget.getSection(), fields);
                } else {
                    inactiveFieldMap.get(fieldWidget.getSection()).add(field);
                }
            }
        }
        activeFieldMap.forEach((k, v) -> GridUIUtils.sort(v));
        inactiveFieldMap.forEach((k, v) -> GridUIUtils.sort(v));

        for (Map.Entry<String, LinkedList<CustomizeFormItem>> inactiveEntry : inactiveFieldMap.entrySet()) {
            if (activeFieldMap.get(inactiveEntry.getKey()) != null) {
                activeFieldMap.get(inactiveEntry.getKey()).addAll(inactiveEntry.getValue());
            }
        }
        AllInOneService.App.get().saveCustomizeGridForm(formID, activeFieldMap, sections, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);

                if (Utils.isNullOrEmpty(path)) {
                    Window.open(Utils.getPathName(), "_self", "");
                } else {
                    closeTab();
                }
            }
        });
    }

    private boolean updateSectionSwitch(String section, boolean isSideNav) {
        String localizedSection = Localize.getInstance().localizeByFieldID(formID, section);
        boolean hasMandatoryField = false;

        for (DynamicGridItemPanel itemPanel : gridItemMap.values()) {
            if (itemPanel == null || itemPanel.getField() == null) {
                continue;
            }
            if (itemPanel.getField().getField().isSystemMandatory()) {
                hasMandatoryField = true;
                break;
            }
        }
        if (hasMandatoryField) {
            Info.warn((localizedSection != null ? localizedSection.toUpperCase() : section) + " " + wfmStrings.containsMandatoryField(), Info.Position.BOTTOM_LEFT);
            return true;
        }
        return false;
    }

    @Override
    protected void addButtons() {

        //Memorized  item
        if (Utils.hasRole(Constants.ADMIN) && formItemId != null) {
            addButton(wfmStrings.addMemorized(), WfmButton2.BTN_WHITE, click -> memorizedOption(formItemId));
        }

        addButton(wfmStrings.sectionOrder(), WfmButton2.BTN_WHITE, click -> orderModal.show());

        MaterialLink addSection = new MaterialLink(wfmStrings.addSection());
        MaterialSplitButton splitButton = new MaterialSplitButton(addSection);
        addSection.addClickHandler(event -> createNewSectionDialog());

        MaterialLink addPage = new MaterialLink(wfmStrings.addPage());
        addPage.setLayoutPosition(Style.Position.ABSOLUTE);
        addPage.addClickHandler(event -> createNewPage());
        splitButton.addItem(addPage);

        MaterialLink addIntro = new MaterialLink(wfmStrings.introductionPage());
        addIntro.setLayoutPosition(Style.Position.ABSOLUTE);
        addIntro.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("introPage|add/add/"+ formID +"/"+ LayoutRPC.ADD));
        splitButton.addItem(addIntro);
        addButton(splitButton);
//        addButton(wfmStrings.addSection(), WfmButton2.BTN_PRIMARY, click -> createNewSectionDialog());

        addButton(wfmStrings.applyChanges(), WfmButton2.BTN_PRIMARY, click -> save());
    }

    private void createNewPage() {
        LoadingPanel.loading(true);
        AllInOneService.App.get().getDefaultPaginationName(formID, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(String defaultName) {
                LoadingPanel.loading(false);
                DynamicSectionsRpc rpc = new DynamicSectionsRpc();
                rpc.setFormID(formID);
                rpc.setCustom(true);
                rpc.setLabel(defaultName);
                rpc.setPagination(true);
                saveSectionName(rpc);
            }
        });


//        dialogBox = new KpiModal();
//        dialogBox.setTitle(wfmStrings.addPage());
//        dialogBox.setWidth(350);
//
//        TextBox textBox = new TextBox();
//
//        WfmButton2 save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
//        save.addClickHandler(clickEvent -> {
//
//            String sectionLabel = textBox.getText();
//
//            if (sectionLabel != null && sectionLabel.trim().length() > 0) {
//                DynamicSectionsRpc rpc = new DynamicSectionsRpc();
//                rpc.setFormID(formID);
//                rpc.setCustom(true);
//                rpc.setLabel(sectionLabel);
//
//                rpc.setPagination(true);
//                saveSectionName(rpc);
//            }
//        });
//
//        WfmButton2 close = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT);
//        close.addClickHandler(x -> dialogBox.close());
//
//
//        dialogBox.addWidget(textBox, wfmStrings.name());
//
//        dialogBox.addButton(close);
//        dialogBox.addButton(save);
//        dialogBox.open();
    }

    private KpiModal dialogBox;

    private void createNewSectionDialog() {
        dialogBox = new KpiModal();
        dialogBox.setTitle(wfmStrings.addSection());
        dialogBox.setWidth(300);

        TextBox textBox = new TextBox();

        WfmButton2 save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        save.addClickHandler(clickEvent -> {

            String sectionLabel = textBox.getText();

            if (sectionLabel != null && sectionLabel.trim().length() > 0) {
                DynamicSectionsRpc rpc = new DynamicSectionsRpc();
                rpc.setFormID(formID);
                rpc.setCustom(true);
                rpc.setLabel(sectionLabel);
                rpc.setPagination(false);
                saveSectionName(rpc);
            }
        });

        WfmButton2 close = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT);
        close.addClickHandler(x -> dialogBox.close());


        dialogBox.addWidget(textBox, wfmStrings.name());

        dialogBox.addButton(close);
        dialogBox.addButton(save);
        dialogBox.open();
    }

    private void saveSectionName(DynamicSectionsRpc rpc) {
        LoadingPanel.loading(true);
        AllInOneService.App.get().saveCustomDynamicFormSection(rpc, new AbstractAsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Integer result) {
                LoadingPanel.loading(false);
                if (Constants.VALIDATION == result) {
                    Info.warn(wfmStrings.sectionName() + " '" + rpc.getLabel() + "' " + wfmStrings.isAlreadyExist(), Info.Position.TOP_RIGHT);
                } else if (Constants.LIMIT_EXCEEDED == result) {
                    Info.warn(wfmStrings.youCanNotAddMoreThan() + " 25 " + wfmStrings.sections(), Info.Position.TOP_RIGHT);
                } else {
                    if (dialogBox != null) {
                        dialogBox.close();
                    }

                    String type = rpc.getId() == null ? wfmStrings.created() : wfmStrings.updated();
                    Info.show(wfmStrings.section() + " " + wfmStrings.successfully() + " " + type, Info.Position.TOP_RIGHT);

                    clear();
                    onInitialize();
                }
            }
        });
    }

    private void memorizedOption(final Integer objectID) {
        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        messageBox.setTitle(View.wfmStrings.confirmation());
        messageBox.setMessage(" <b>" + View.wfmStrings.youWantMemorized() + "</b> ");
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                LoadingPanel.loading(true);
                CommonService.App.get().memorizedCustomFormItem(objectID, formID, new AbstractAsyncCallback<Void>() {
                            @Override
                            public void failure(final Throwable throwable) {
                                LoadingPanel.loading(false);
                            }

                            @Override
                            public void success(final Void result) {
                                LoadingPanel.loading(false);
                                Info.show(View.wfmStrings.memorizedSuccessfully(), Info.Type.INFO);
                            }
                        }
                );
            }
        });
        messageBox.open();
    }

    @Override
    protected void getDataToFillFields() {

    }

    @Override
    protected String getFormID() {
        return LayoutRPC.CUSTOMIZE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
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

    class SectionHeader extends MaterialPanel {

        private MaterialLink column1;
        private MaterialLink column2;
        private MaterialLink column3;
        private MaterialLink column4;
        private WfmButton2 addFieldButton;
        private final String section;
        private int column;
        private Span columnCount;

        SectionHeader(String section) {
            this.section = section;
            initColumnsHeader();
        }

        private void initColumnsHeader() {
            addStyleName("panel-w-switch__header-bar");
            MaterialPanel panel = new MaterialPanel("panel-w-switch__header-text");

            String localizedSection = null;
            if (sections == null || sections.get(section) == null || sections.get(section).getLabel() == null) {
                localizedSection = Localize.getInstance().localizeByFieldID(formID, section);
            } else {
                localizedSection = sections.get(section).getLabel().toUpperCase();
            }
            String sectionName = localizedSection != null ? localizedSection.toUpperCase() : section;
            EditableLabel label = new EditableLabel(sectionName);
            label.addValueChangeHandler(event -> {
                DynamicSectionsRpc sectionsRpc = sections.get(section);
                if (sectionsRpc.isPagination()){
                    Info.show("You can not to change Pagination section name ", Info.Type.WARNING);
                    label.setText(sectionName);
                    return;
                }
                sectionsRpc.setLabel(event.getValue());
                sectionsRpc.setName(event.getValue());
                saveSectionName(sectionsRpc);
            });
            panel.add(label);

            column1 = new MaterialLink();
            column1.setTooltip(wfmStrings.oneColumn());
            column1.setTooltipPosition(Position.TOP);
            column1.setLayoutData(ColumnType.COL_1);
            column1.addClickHandler(event -> redrawColumns(column1));

            column2 = new MaterialLink();
            column2.setTooltip(wfmStrings.twoColumn());
            column2.setTooltipPosition(Position.TOP);
            column2.setLayoutData(ColumnType.COL_2);
            column2.addClickHandler(event -> redrawColumns(column2));

            column3 = new MaterialLink();
            column3.setTooltip(wfmStrings.threeColumn());
            column3.setTooltipPosition(Position.RIGHT);
            column3.setLayoutData(ColumnType.COL_3);
            column3.addClickHandler(event -> redrawColumns(column3));

            column4 = new MaterialLink();
            column4.setLayoutData(ColumnType.COL_4);
            column4.addClickHandler(event -> redrawColumns(column4));

            Div columnsQtyAction = new Div("columns-qty__actions");
//            columnsQtyAction.add(column4);
            columnsQtyAction.add(column3);
            columnsQtyAction.add(column2);
            columnsQtyAction.add(column1);

            Div columnsQtyText = new Div("columns-qty__text");
            columnCount = new Span(wfmStrings.column().toLowerCase());
            columnCount.addStyleName("columns-qty__text-qty");
            columnsQtyText.add(columnCount);

            Div columnsQty = new Div("columns-qty");
            columnsQty.add(columnsQtyText);
            columnsQty.add(columnsQtyAction);

            MaterialPanel switchPanel = new MaterialPanel("panel-w-switch__header-col-qty");
            switchPanel.add(columnsQty);

            add(panel);

            MaterialSwitch sectionSwitcher = new MaterialSwitch(false);
            sectionSwitcher.getElement().setId("id_" + section.toLowerCase());
            sectionSwitcher.setOffLabel(wfmStrings.unusedFields() + " ");
            sectionSwitcher.addValueChangeHandler(ch -> collapseInactivePanel(sectionSwitcher));

            MaterialPanel collapsePanel = new MaterialPanel("collapse-switcher");
            MaterialLink collapse = new MaterialLink();
            SvgIcon svgIcon = new SvgIcon("collapse");
            collapse.add(svgIcon);
            Span span = new Span();
            span.addStyleName("collapse-switcher__txt");

            final boolean[] expanded = {sections != null && sections.get(section).isExpanded()};
            span.setText(expanded[0] ? wfmStrings.collapseSection() : wfmStrings.expandSection());
            collapsePanel.addClickHandler(clickEvent -> {
                if (sections != null) {
                    expanded[0] = sections.get(section).isExpanded();
                    expanded[0] = !expanded[0];
                    span.setText(expanded[0] ? wfmStrings.collapseSection() : wfmStrings.expandSection());

                    sections.get(section).setExpanded(expanded[0]);
                }
            });

            collapsePanel.add(span);
            collapsePanel.add(collapse);

            Div addButtonWrapper = new Div("has-btn-addFields");
            addFieldButton = new WfmButton2(wfmStrings.addNewField(), "btn btn--lightgrey btn-medium btn-text-i btn-addFields");
            addFieldButton.setVisible(false);
            Span spanBtn = new Span();
            spanBtn.addStyleName("btn-text-i__i");

            Span spanPlus = new Span();
            spanPlus.addStyleName("btn btn-small btn--circle btn--white");
            spanPlus.add(new SvgIcon(SvgEnum.plus));

            spanBtn.add(spanPlus);
            addFieldButton.add(spanBtn);
            addFieldButton.addClickHandler(click -> {
                fieldsBar.show(object -> {
                    addFieldToSection(section, object);
                });
                fieldsBar.updateCustomFieldsCount();
            });
            addButtonWrapper.add(addFieldButton);

            add(addButtonWrapper);
            add(collapsePanel);
            add(sectionSwitcher);
//            add(switchPanel);
        }

        private void redrawColumns(MaterialLink materialLink) {
            ColumnType col = (ColumnType) materialLink.getLayoutData();

            columnCount.setText(1 + col.ordinal() + " " + wfmStrings.column().toLowerCase());

            removeActiveStyleName("columns-qty__action-col--active");
            materialLink.addStyleName("columns-qty__action-col--active");
            DynamicGridPanel widget = activePanelMap.get(section);
            LinkedList<CustomizeFormItem> list = new LinkedList<>();
            for (DynamicGridItemPanel itemPanel : widget.getGridItemMap().values()) {
                if (itemPanel.getField() == null || itemPanel.getField().getField() == null) {
                    continue;
                }
                list.add(itemPanel.getField().getField());
            }
            widget.removeAllItem();

            DynamicGridPanel gridPanel = activePanelMap.get(section);

            drawColumns(list, widget, section);
        }

        private LinkedList<CustomizeFormItem> getList(LinkedList<CustomizeFormItem> list) {
            if ((list != null && !list.isEmpty())) {
                return list;
            } else {
                return new LinkedList<>();
            }
        }

        private void removeActiveStyleName(String s) {
            if (column1.getStyleName().contains(s)) {
                column1.removeStyleName(s);
            }
            if (column2.getStyleName().contains(s)) {
                column2.removeStyleName(s);
            }
            if (column3.getStyleName().contains(s)) {
                column3.removeStyleName(s);
            }
            if (column4.getStyleName().contains(s)) {
                column4.removeStyleName(s);
            }
        }

        private void addStyle(MaterialLink link) {
            ColumnType columnType = (ColumnType) link.getLayoutData();
            link.setStyleName("columns-qty__action-col");
            if (1 + columnType.ordinal() == column) {
                link.addStyleName("columns-qty__action-col--active");
            }
        }

        private void collapseInactivePanel(MaterialSwitch switcher) {
            $(switcher.getElement()).closest(".panel-w-switch__header").find(".custom-form-grid--unused").slideToggle();
        }

        public void setColumnCount(int column) {
            this.column = column;
            columnCount.setText(this.column + " " + wfmStrings.column().toLowerCase());
            addStyle(column1);
            addStyle(column2);
            addStyle(column3);
            addStyle(column4);
        }

        public void showAddButton() {
            this.addFieldButton.setVisible(true);
        }
    }
}
