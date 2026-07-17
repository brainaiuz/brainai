package com.edatasite.workforce.gwt.core.client.form;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.FlowPanelDropController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ColumnType;
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
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.VerticalPanelWithSpacer;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSwitch;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import static gwt.material.design.jquery.client.api.JQuery.$;

public class DynamicFormView extends CustomForm implements FittedContent, Colapse {

    private AbsolutePanel absolutePanel;
    private PickupDragController widgetDragController;
    private static final HashMap<String, HorizontalPanel> widgetMap = new HashMap<>();
    private LinkedHashMap<String, HashMap<ColumnType, LinkedList<CustomizeFormItem>>> modelFields = new LinkedHashMap<>();
    private final String formID;
    public static HashMap<String, FlowPanel> inactivePanelMap = new HashMap<>();
    private final HashMap<String, ArrayList<VerticalPanelDropController>> dropcontrollers = new HashMap<>();
    private final String path;
    private HashMap<String, DynamicSectionsRpc> sections;
    private CustomFieldsBar fieldsBar;
    private SectionSideNavBox orderModal;
    private String entityName;
    private boolean isQuizForm;

    public DynamicFormView(String formID, String path) {
        super(formID, wfmStrings.customizeForm());
        this.formID = formID;
        this.path = path != null ? URL.decodeQueryString(path) : "";
        Utils.enableLeftMenu(false);
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();

        AllInOneService.App.get().getCustomizeFormSections(formID, false, new AbstractAsyncCallback<LinkedHashMap<String, DynamicSectionsRpc>>() {
            @Override
            public void success(LinkedHashMap<String, DynamicSectionsRpc> result) {
                if (result != null) {
                    sections = result;
                }
            }
        });

        initialize();

        return null;
    }

    private void initialize() {

        fieldsBar = new CustomFieldsBar(formID);

        orderModal = new SectionSideNavBox(formID);
        orderModal.setCommand(() -> {
            clear();
            onInitialize();
        });

        orderModal.setDynamicColumn(this::updateSectionSwitch);

        absolutePanel = new AbsolutePanel();
        absolutePanel.setSize("100%", "100%");
        addField(CustomFormConstants.CONTENT, absolutePanel);
        addField(CustomFormConstants.ADDITIONAL_SETTINGS, fieldsBar);

        widgetDragController = new PickupDragController(absolutePanel, false);
        widgetDragController.setBehaviorMultipleSelection(false);

        LoadingPanel.loading(true);
        AllInOneService.App.get().getCustomizeForm(formID, new AbstractAsyncCallback<LinkedHashMap<String, HashMap<ColumnType, LinkedList<CustomizeFormItem>>>>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(LinkedHashMap<String, HashMap<ColumnType, LinkedList<CustomizeFormItem>>> result) {
                CommonService.App.get().customFormIsQuizForm(formID, new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        isQuizForm = false;
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        modelFields = result;
                        isQuizForm = aBoolean;
                        modelFields.forEach(DynamicFormView.this::createContainers);
                        LoadingPanel.loading(false);
                    }
                });
            }
        });
    }

    private void createContainers(String section, HashMap<ColumnType, LinkedList<CustomizeFormItem>> fields) {
        MaterialPanel panel = new MaterialPanel("panel-w-switch");
        MaterialPanel switchHeader = new MaterialPanel("panel-w-switch__header");
        panel.add(switchHeader);

        int size = fields.size();
        if (size == 0) {
            size = 1; //for an empty draggable area
            fields.put(ColumnType.COL_1, new LinkedList<>());
        }

        SectionHeader sectionHeader = new SectionHeader(section, size);
        switchHeader.add(sectionHeader);

        MaterialPanel inactiveContainer = new MaterialPanel("panel-w-switch__container");
        inactiveContainer.getElement().getStyle().setDisplay(Style.Display.NONE);

        Div div = new Div("panel-w-switch__container-header");
        div.add(new HTML(wfmStrings.unusedFields()));

        FlowPanel flowPanelDropTarget = new FlowPanel();
        flowPanelDropTarget.setWidth("100%");
        flowPanelDropTarget.addStyleName("drag-tiles drag-tiles--inline");
        flowPanelDropTarget.setLayoutData(section);

        inactiveContainer.add(div);
        inactiveContainer.add(flowPanelDropTarget);
        switchHeader.add(inactiveContainer);


        FlowPanelDropController flowPanelDropController = new InactiveFieldsDropController(flowPanelDropTarget);
        widgetDragController.registerDropController(flowPanelDropController);

        fields.forEach((key, value) -> value.stream()
                .filter(f -> !f.isActive())
                .forEach(field -> {
                    if (!field.isCustomField()) {
                        if (field.getFormProperty() != null && field.getFormProperty().isChanged()) {
                            field.setLabel(field.getFormProperty().getTitle());
                        } else {
                            field.setLabel(Localize.getInstance().localizeByFieldID(formID, field.getName()));
                        }
                    }
                    DynamicField dynamicField = new DynamicField(field, widgetDragController, formID, false, isQuizForm);
                    dynamicField.setActiveCommand(() -> activateField(section, flowPanelDropTarget, dynamicField));
                    flowPanelDropTarget.add(dynamicField);
                }));

        inactivePanelMap.put(section, flowPanelDropTarget);

        HorizontalPanel horizontalPanel = new HorizontalPanel();
        widgetMap.put(section, horizontalPanel);

        MaterialPanel horizontalDiv = new MaterialPanel("panel-w-switch__container-columns");
        horizontalDiv.add(horizontalPanel);
        switchHeader.add(horizontalDiv);

        drawColumns(fields, horizontalPanel, section, flowPanelDropTarget);

        absolutePanel.add(panel);
        if (fields.get(ColumnType.COL_1) != null && fields.get(ColumnType.COL_1).size() == 1 && fields.get(ColumnType.COL_1).getFirst().isCustomForm()) {
            modelFields.get(section).clear();
        }
    }

    public static void inactivateField(String section, FlowPanel inactivePanel, VerticalPanel fieldsPanel, DynamicField dynamicField) {
        HTML html = new HTML(wfmStrings.areYouSureYouWanttoDeleteThe_field());
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
                if (fieldsPanel.getWidgetIndex(dynamicField) >= 0 && (!dynamicField.getField().isSystemMandatory() ||
                        (dynamicField.getField().getFormProperty() != null && ((dynamicField.getField().getFormProperty().getDefaultValue() != null &&
                                !"".equals(dynamicField.getField().getFormProperty().getDefaultValue())) || dynamicField.getField().getFormProperty().getSelectedId() != null)))) {
                    fieldsPanel.remove(dynamicField);

                    dynamicField.setActiveCommand(() -> activateField(section, inactivePanel, dynamicField));
                    dynamicField.setInactiveCommand(null);
                    dynamicField.setActive(false);
                    inactivePanel.add(dynamicField);

                    focusUnusedFieldPanel(section);
                } else {
                    Info.warn(wfmStrings.mandatoryFieldCannotBeInactive());
                }
            }
        });
        wfmMessageBox.open();
    }

    private void drawColumns(HashMap<ColumnType, LinkedList<CustomizeFormItem>> fields,
                             HorizontalPanel horizontalPanel,
                             String section, FlowPanel inactivePanel) {

        if (!dropcontrollers.isEmpty() && dropcontrollers.get(section) != null) {
            dropcontrollers.get(section).forEach(v -> widgetDragController.unregisterDropController(v));
        }

        final VerticalPanel[] verticalPanel = new VerticalPanel[1];
        fields.forEach((column, draggableFields) -> {
            MaterialPanel columnCompositePanel = new MaterialPanel("drag-tiles");

            VerticalPanel fieldsPanel = new VerticalPanelWithSpacer(1);
            verticalPanel[0] = fieldsPanel;
            fieldsPanel.setLayoutData(section + Constants.DELIMITR + column);//section column

            DynamicPanelDropController dropController = new DynamicPanelDropController(fieldsPanel, modelFields);

            dropcontrollers.computeIfAbsent(section, v -> new ArrayList<>()).add(dropController);
            widgetDragController.registerDropController(dropController);

            horizontalPanel.add(columnCompositePanel);

            columnCompositePanel.add(fieldsPanel);

            draggableFields.stream()
                    .filter(CustomizeFormItem::isActive)
                    .forEach(field -> {
                        if (!field.isCustomField()) {
                            if (field.getFormProperty() != null && field.getFormProperty().isChanged()) {
                                field.setLabel(field.getFormProperty().getTitle());
                            } else {
                                field.setLabel(Localize.getInstance().localizeByFieldID(formID, field.getName()));
                            }
                        }
                        DynamicField dynamicField = new DynamicField(field, widgetDragController, formID, true, isQuizForm);
                        fieldsPanel.add(dynamicField);

                        dynamicField.setInactiveCommand(() -> inactivateField(section, inactivePanel, fieldsPanel, dynamicField));
                    });

            if (fieldsPanel.getWidgetCount() < 2) {
                fieldsPanel.addStyleName("drop-target--empty");
            }

            if (entityName == null && draggableFields.size() > 0 && draggableFields.get(0) != null) {
                entityName = draggableFields.get(0).getEntityName();
            }
        });

        if (entityName != null) {
            WfmButton2 cfield = new WfmButton2(wfmStrings.addNewField(), "btn btn--lightgrey btn-medium btn-text-i btn-addFields");
            Span spanBtn = new Span();
            spanBtn.addStyleName("btn-text-i__i");

            Span span = new Span();
            span.addStyleName("btn btn-small btn--circle btn--white");
            span.add(new SvgIcon(SvgEnum.plus));

            spanBtn.add(span);
            cfield.add(spanBtn);
            verticalPanel[0].add(cfield);
            DOM.getParent(cfield.getElement()).addClassName("has-btn-addFields");
            cfield.addClickHandler(click -> {
                fieldsBar.show(object -> addFieldToSection(section, object, inactivePanel));
                fieldsBar.updateCustomFieldsCount();
            });
        }
    }

    public static void activateField(String section, FlowPanel inactivePanel, DynamicField dynamicField) {
        HTML html = new HTML(wfmStrings.areYouSureYouWanttoActivateThe_field());
        html.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);

        WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        wfmMessageBox.setTitle(wfmStrings.confirmationMessage());
        wfmMessageBox.setMessage(wfmStrings.activateConfirmationMessage());
        wfmMessageBox.setContent(html);
        wfmMessageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onSubmit() {
                inactivePanel.remove(dynamicField);

                HorizontalPanel horizontalPanel = widgetMap.get(section);
                MaterialPanel columnCompositePanel = (MaterialPanel) horizontalPanel.getWidget(0);
                VerticalPanel fieldsPanel = (VerticalPanel) columnCompositePanel.getWidget(columnCompositePanel.getWidgetCount() - 1);
                dynamicField.setInactiveCommand(() -> inactivateField(section, inactivePanel, fieldsPanel, dynamicField));
                dynamicField.setActiveCommand(null);
                dynamicField.setActive(true);

                fieldsPanel.add(dynamicField);
            }
        });
        wfmMessageBox.open();
    }


    private void addFieldToSection(String section, SelectItem item, FlowPanel inactivePanel) {
        HorizontalPanel horizontalPanel = widgetMap.get(section);
        MaterialPanel columnCompositePanel = (MaterialPanel) horizontalPanel.getWidget(0);
        VerticalPanel fieldsPanel = (VerticalPanel) columnCompositePanel.getWidget(columnCompositePanel.getWidgetCount() - 1);

        CustomizeFormItem field = new CustomizeFormItem();
        field.setSection(section);
        field.setColumnType(ColumnType.COL_1);
        field.setName(item.getName());
        field.setLabel(item.getName());
        field.setCustomField(true);
        field.setUiType(item.getDescription());
        field.setEntityName(entityName);
        DynamicField dynamicField = new DynamicField(field, widgetDragController, formID, true, isQuizForm);

        dynamicField.setInactiveCommand(() -> inactivateField(section, inactivePanel, fieldsPanel, dynamicField));

        fieldsPanel.add(dynamicField);

        modelFields.get(section).computeIfAbsent(ColumnType.COL_1, x -> new LinkedList<>()).add(field);
    }

    private static void focusUnusedFieldPanel(String section) {
        DOM.getElementById("id_" + section.toLowerCase()).addClassName("action--attention");
        Timer timer = new Timer() {
            @Override
            public void run() {
                DOM.getElementById("id_" + section.toLowerCase()).removeClassName("action--attention");
            }
        };
        timer.schedule(2500);
    }

    private void save() {

        LoadingPanel.loading(true);
        AllInOneService.App.get().saveCustomizeForm(formID, modelFields, sections, new AbstractAsyncCallback<Void>() {
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
        final boolean[] hasMandatoryField = {false};
        modelFields.get(section).forEach((k, v) -> {
            if (v != null && !v.isEmpty()) {
                for (CustomizeFormItem x : v) {
                    if (x.isSystemMandatory()) {
                        hasMandatoryField[0] = true;
                        break;
                    }
                }
            }
        });

        if (hasMandatoryField[0]) {
            Info.warn((localizedSection != null ? localizedSection.toUpperCase() : section) + " " + wfmStrings.containsMandatoryField(), Info.Position.BOTTOM_LEFT);
            return true;
        }
        return false;
    }

    @Override
    protected void addButtons() {
     if (Utils.hasPermission(PermissionConstants.ADD_WEBHOOK_FROM_CUSTOMIZE)) {
            addButton(wfmStrings.webHook(), WfmButton2.BTN_PRIMARY, click ->
            SinksContainerFactory.entryPoint.onHistoryChanged("webhooklist|workflowWebHooks/" + formID, formID));
      }

        addButton(wfmStrings.sections() + " " + wfmStrings.order(), WfmButton2.BTN_WHITE, click -> orderModal.show());

        addButton(wfmStrings.addSection(), WfmButton2.BTN_PRIMARY, click -> createNewSectionDialog());

        addButton(wfmStrings.applyChanges(), WfmButton2.BTN_PRIMARY, click -> save());

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

                    String type = rpc.getId() == null ? wfmStrings.createdDate() : wfmStrings.updated();
                    Info.show(wfmStrings.section() + " " + wfmStrings.successfully() + " " + type, Info.Position.TOP_RIGHT);

                    clear();
                    onInitialize();
                }
            }
        });
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
        private final String section;
        private final int column;
        private Span columnCount;

        SectionHeader(String section, int column) {
            this.section = section;
            this.column = column;
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
                sectionsRpc.setLabel(event.getValue());
                sectionsRpc.setName(event.getValue());
                saveSectionName(sectionsRpc);
            });
            panel.add(label);

            column1 = new MaterialLink();
            column1.setTooltip(wfmStrings.oneColumn());
            column1.setTooltipPosition(Position.TOP);
            column1.setLayoutData(ColumnType.COL_1);
            addStyle(column1);
            column1.addClickHandler(event -> redrawColumns(column1));

            column2 = new MaterialLink();
            column2.setTooltip(wfmStrings.twoColumn());
            column2.setTooltipPosition(Position.TOP);
            column2.setLayoutData(ColumnType.COL_2);
            addStyle(column2);
            column2.addClickHandler(event -> redrawColumns(column2));

            column3 = new MaterialLink();
            column3.setTooltip(wfmStrings.threeColumn());
            column3.setTooltipPosition(Position.TOP);
            column3.setLayoutData(ColumnType.COL_3);
            addStyle(column3);
            column3.addClickHandler(event -> {
                redrawColumns(column3);
            });

            column4 = new MaterialLink();
            column4.setLayoutData(ColumnType.COL_4);
            addStyle(column4);
            column4.addClickHandler(event -> redrawColumns(column4));

            Div columnsQtyAction = new Div("columns-qty__actions");
//            columnsQtyAction.add(column4);
            columnsQtyAction.add(column3);
            columnsQtyAction.add(column2);
            columnsQtyAction.add(column1);

            Div columnsQtyText = new Div("columns-qty__text");
            columnCount = new Span(this.column + " " + wfmStrings.column().toLowerCase());
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

            final boolean[] expanded = {sections != null && sections.get(section) != null && sections.get(section).isExpanded()};
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

            add(collapsePanel);
            add(sectionSwitcher);
            add(switchPanel);
        }

        private void redrawColumns(MaterialLink materialLink) {
            ColumnType col = (ColumnType) materialLink.getLayoutData();

            columnCount.setText(1 + col.ordinal() + " " + wfmStrings.column().toLowerCase());

            removeActiveStyleName("columns-qty__action-col--active");
            materialLink.addStyleName("columns-qty__action-col--active");
            HorizontalPanel widget = widgetMap.get(section);
            widget.clear();

            HashMap<ColumnType, LinkedList<CustomizeFormItem>> fields = modelFields.get(section);
            HashMap<ColumnType, LinkedList<CustomizeFormItem>> newMap = getMap(col, fields);
            drawColumns(newMap, widget, section, inactivePanelMap.get(section));

            modelFields.get(section).clear();
            modelFields.get(section).putAll(newMap);
        }

        private HashMap<ColumnType, LinkedList<CustomizeFormItem>> getMap(ColumnType col, HashMap<ColumnType, LinkedList<CustomizeFormItem>> fields) {
            HashMap<ColumnType, LinkedList<CustomizeFormItem>> map = new LinkedHashMap<>();
            map.put(ColumnType.COL_1, getList(fields.get(ColumnType.COL_1)));

            if (col.equals(ColumnType.COL_1)) {

                if (fields.get(ColumnType.COL_2) != null) {
                    fields.get(ColumnType.COL_2).forEach(f -> {
                        f.setColumnType(ColumnType.COL_1);
                        map.get(ColumnType.COL_1).addLast(f);
                    });
                }

                if (fields.get(ColumnType.COL_3) != null) {
                    fields.get(ColumnType.COL_3).forEach(f -> {
                        f.setColumnType(ColumnType.COL_1);
                        map.get(ColumnType.COL_1).addLast(f);
                    });
                }

                if (fields.get(ColumnType.COL_4) != null) {
                    fields.get(ColumnType.COL_4).forEach(f -> {
                        f.setColumnType(ColumnType.COL_1);
                        map.get(ColumnType.COL_1).addLast(f);
                    });
                }

            } else if (col.equals(ColumnType.COL_2)) {
                map.put(ColumnType.COL_2, getList(fields.get(ColumnType.COL_2)));

                if (fields.get(ColumnType.COL_3) != null) {
                    fields.get(ColumnType.COL_3).forEach(f -> {
                        f.setColumnType(ColumnType.COL_2);
                        map.get(ColumnType.COL_2).addLast(f);
                    });
                }

                if (fields.get(ColumnType.COL_4) != null) {
                    fields.get(ColumnType.COL_4).forEach(f -> {
                        f.setColumnType(ColumnType.COL_2);
                        map.get(ColumnType.COL_2).addLast(f);
                    });
                }

            } else if (col.equals(ColumnType.COL_3)) {
                map.put(ColumnType.COL_2, getList(fields.get(ColumnType.COL_2)));
                map.put(ColumnType.COL_3, getList(fields.get(ColumnType.COL_3)));

                if (fields.get(ColumnType.COL_4) != null) {
                    fields.get(ColumnType.COL_4).forEach(f -> {
                        f.setColumnType(ColumnType.COL_3);
                        map.get(ColumnType.COL_3).addLast(f);
                    });
                }

            } else if (col.equals(ColumnType.COL_4)) {
                map.put(ColumnType.COL_2, getList(fields.get(ColumnType.COL_2)));
                map.put(ColumnType.COL_3, getList(fields.get(ColumnType.COL_3)));
                map.put(ColumnType.COL_4, getList(fields.get(ColumnType.COL_4)));
            }
            return map;
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
            $(switcher.getElement()).closest(".panel-w-switch__header").find(".panel-w-switch__container").slideToggle();
        }
    }
}
