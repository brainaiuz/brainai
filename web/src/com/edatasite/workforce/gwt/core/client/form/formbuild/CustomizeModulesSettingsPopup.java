package com.edatasite.workforce.gwt.core.client.form.formbuild;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandlerAdapter;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.WordUtils;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.form.EditableLabel;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.LocalizationCFModal;
import com.edatasite.workforce.gwt.core.client.ui.LocalizationTypeEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.CollapsiblePanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.*;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSwitch;
import gwt.material.design.client.ui.html.Heading;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * User: Faxriddin Taslimov  * Date: 16/11/2019
 */
public class CustomizeModulesSettingsPopup extends KpiSideNavBox implements ClickHandler {




    private FlexTable content;
    private WfmButton2 save;
    private final String section;
    private String oldModuleName;
    private final LinkedHashMap<SelectItem, VerticalPanel> values = new LinkedHashMap<>();

    private final ProfileServiceAsync profileService = ProfileService.App.get();

    public CustomizeModulesSettingsPopup(String section) {
        this.section = section;
        setStyleName(getElement(), "quick-add", true);
        profileService.getModuleLocalizeData(section, new AbstractAsyncCallback<SelectItem>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
                initialization();
            }

            @Override
            public void success(SelectItem result) {
                super.success(result);
                if (result != null) {
                    oldModuleName = result.getName();
                } else {
                    if (ModuleEnum.ACCOUNTING.getCode().equals(section)) {
                        oldModuleName = wfmStrings.accounts();
                    } else if (ModuleEnum.CRM.getCode().equals(section)) {
                        oldModuleName = wfmStrings.crm();
                    } else if (ModuleEnum.HRMS.getCode().equals(section)) {
                        oldModuleName = wfmStrings.hrms();
                    } else if (ModuleEnum.PM.getCode().equals(section)) {
                        oldModuleName = wfmStrings.projects();
                    } else if (ModuleEnum.PAYROLL.getCode().equals(section)) {
                        oldModuleName = wfmStrings.payroll();
                    } else if (ModuleEnum.REPORTING.getCode().equals(section)) {
                        oldModuleName = wfmStrings.reports();
                    } else if (ModuleEnum.DOCUMENTS.getCode().equals(section)) {
                        oldModuleName = wfmStrings.docs();

                    }
                }
                initialization();
            }
        });
    }

    private void initialization() {
        Heading header = new Heading(HeadingSize.H1);
        header.setText(wfmStrings.customize());
        addHeader(header);

        content = new FlexTable();

        ColumnSetttings columnSetttings = new ColumnSetttings();


        HTML html = new HTML(oldModuleName);
        html.addStyleName("form-control");
        WfmButton2 editModule = new WfmButton2(wfmStrings.edit(), WfmButton2.BTN_WHITE_OUTLINE);

        FormGroup moduleFormGroup = new FormGroup(wfmStrings.moduleSettings(), new InputGroup(html, editModule));
        moduleFormGroup.setStyleName("panel-w-switch");
        content.setWidget(0, 0, moduleFormGroup);


        TextBox moduleName = new TextBox();
        moduleName.getElement().setAttribute("style", "border-color:#fdd850");
        moduleName.setText(oldModuleName);

        WfmButton2 checkButton = new WfmButton2("", "btn--icon");
        checkButton.getElement().setAttribute("style", "border-color:#fdd850");
        SvgIcon check = new SvgIcon(SvgEnum.check);
        checkButton.add(check);

        WfmButton2 cancel = new WfmButton2("", "btn--icon");
        cancel.getElement().setAttribute("style", "border-color:#fdd850");
        SvgIcon cancelSvg = new SvgIcon(SvgEnum.x);
        cancel.add(cancelSvg);

        FormGroup moduleNameFormGroup = new FormGroup(wfmStrings.moduleSettings(), new InputGroup(moduleName, checkButton, cancel));

        editModule.addClickHandler(editClick -> {
            content.remove(moduleFormGroup);
            content.setWidget(0, 0, moduleNameFormGroup);
        });

        cancel.addClickHandler(event -> {
            content.remove(moduleNameFormGroup);
            content.setWidget(0, 0, moduleFormGroup);
        });

        checkButton.addClickHandler(click -> {
            if (!Utils.isNullOrEmpty(oldModuleName) && !Utils.isNullOrEmpty(moduleName.getText()) && !oldModuleName.equals(moduleName.getText())) {
                profileService.renameModuleName(moduleName.getText(), section, new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable throwable) {
                    }

                    @Override
                    public void success(Void result) {
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.item()), Info.Type.INFO);
                        oldModuleName = moduleName.getText();
                        html.setText(oldModuleName);
                        moduleName.setText(oldModuleName);
                        content.remove(moduleNameFormGroup);
                        content.setWidget(0, 0, moduleFormGroup);
                    }
                });
            }
        });

        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        save.ensureDebugId("ListingPanelSettingsPopup_save_button");

        save.addClickHandler(be -> {
            save.setEnabled(false);

            LoadingPanel.loading(true, CustomizeModulesSettingsPopup.this);

            profileService.saveModuleSettings(section, columnSetttings.getActiveItems(), new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(Void v) {
                    LoadingPanel.loading(false);
                    save.setEnabled(true);
                    remove();
                }
            });
        });

        WfmButton2 plus = new WfmButton2(wfmStrings.addSection(), WfmButton2.BTN_PRIMARY);
        plus.addClickHandler(cl -> {
            columnSetttings.createNewSection();
        });

        FlexTable columnWidth = new FlexTable();

        int row = 1;


        row++;
        content.setWidget(row, 0, columnSetttings);

        row++;
        content.setHTML(row, 0, "&nbsp;");

        row++;
        content.setWidget(row, 0, columnWidth);

        addFooter(save);
        addFooter(plus);

        addBody(content);
        show();
    }

    /**
     * Hide Popup
     */
    @Override
    public void onClick(ClickEvent event) {
        hide();
    }


    protected class ColumnSetttings extends FlexTable {

        private MaterialPanel container;
        private AbsolutePanel boundaryPanel;
        private PickupDragController showColumnDragController;


        ColumnSetttings() {
            initilazation();
        }

        private void initilazation() {

            container = new MaterialPanel("drag-tiles");
            setWidget(0, 0, container);

            initColumns();
        }

        private void initColumns() {

            profileService.loadAllListingsByModule(section, new AbstractAsyncCallback<LinkedHashMap<SelectItem, LinkedList<PropertyItem>>>() {
                @Override
                public void failure(Throwable throwable) {
                    super.failure(throwable);
                }

                @Override
                public void success(LinkedHashMap<SelectItem, LinkedList<PropertyItem>> result) {
                    super.success(result);
//                    allColumns = result;

                    int i = 0;
                    if (result != null && result.size() > 0) {
                        i++;
                        for (SelectItem selectItem : result.keySet()) {
                            drawForm(result, i, selectItem);
                        }
                    }
                }
            });
        }

        private void drawForm(LinkedHashMap<SelectItem, LinkedList<PropertyItem>> result, int i, SelectItem selectItem) {
            // dragable container
            boundaryPanel = new AbsolutePanel();

            // initialize vertical panel to hold our columns
            VerticalPanel showVerticalPanel = new VerticalPanel();

            boundaryPanel.add(showVerticalPanel);

            // initialize our column drag controller
            showColumnDragController = new PickupDragController(boundaryPanel, false);
            showColumnDragController.setBehaviorMultipleSelection(false);

            // initialize our column drop controller
            VerticalPanelDropController columnDropController = new VerticalPanelDropController(showVerticalPanel);
            showColumnDragController.registerDropController(columnDropController);
            showColumnDragController.addDragHandler(new DragHandlerAdapter() {
                @Override
                public void onDragEnd(DragEndEvent event) {
                }
            });


            CollapsiblePanel collapsiblePanel = new CollapsiblePanel();
            collapsiblePanel.addWidget(boundaryPanel);


            if (result != null && (ModuleEnum.CRM.getCode().equals(section) ||
                    ModuleEnum.HRMS.getCode().equals(section) ||
                    ModuleEnum.ACCOUNTING.getCode().equals(section) ||
                    ModuleEnum.PAYROLL.getCode().equals(section) ||
                    ModuleEnum.PM.getCode().equals(section))) {
                LinkedList<PropertyItem> propertyItemList = result.get(selectItem);

                createNewColumnPanel(showVerticalPanel, propertyItemList, selectItem);
                container.add(collapsiblePanel);
            } else if (selectItem != null) {
                container.add(collapsiblePanel);
            }

            LinkedList<GColumn> columns = new LinkedList<>();

            MenuBar menuBar = new MenuBar(true);
            menuBar.setAutoOpen(true);
            MenuPopItem localization = new MenuPopItem("Localization");
            localization.ensureDebugId("localization-button");
            localization.setCommand(() -> {
                LocalizationCFModal localizationCFModal = new LocalizationCFModal(selectItem.getId(), LocalizationTypeEnum.CONTAINER);
                localizationCFModal.center();
            });
            menuBar.addItem(localization);


            MenuPopItem deleteButton = new MenuPopItem("Delete");
            deleteButton.ensureDebugId("delete-button");
            deleteButton.setCommand(() -> {
                if (result != null && result.get(selectItem) != null && result.get(selectItem).size() > 0) {
                    Info.show("You can't delete", Info.Type.WARNING);
                    return;
                }
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                messageBox.setTitle(wfmStrings.confirmation());
                messageBox.setMessage(wfmStrings.messAreDelete() + " <b>" + selectItem.getName() + "</b> ?");
                messageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        LoadingPanel.loading(true);
                        profileService.deleteTab(selectItem.getId(), section, new AbstractAsyncCallback<Void>() {
                            @Override
                            public void failure(Throwable throwable) {
                            }

                            @Override
                            public void success(Void result) {
                                LoadingPanel.loading(false);
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), selectItem.getName()), Info.Type.INFO);
                            }
                        });
                    }
                });
                messageBox.open();
            });
            menuBar.addItem(deleteButton);

            final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(2);
            toolItem.setWidget(menuBar);

            EditableLabel label = new EditableLabel(selectItem.getName());
            label.getElement().setAttribute("style", "display: flex");
            label.addValueChangeHandler(event -> {

                profileService.renameTabName(event.getValue(), selectItem.getId(), new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable throwable) {
                    }

                    @Override
                    public void success(Void result) {
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.item()), Info.Type.INFO);
                    }
                });

            });
            GColumn customizeText = new GColumn(GColumnEnum.COL_8, label);
            customizeText.setStyleName("panel-w-switch");
            columns.add(customizeText);

            if (selectItem.isSelected()) {
                columns.add(new GColumn(GColumnEnum.COL_2, toolItem.getAction()));
            }
            collapsiblePanel.setCustomizeHeader(columns);

            if (i == 0) {
                collapsiblePanel.setActive(true);
            }
        }

        private void createNewColumnPanel(VerticalPanel showVerticalPanel, LinkedList<PropertyItem> propertyItemList, SelectItem tab) {
            if (propertyItemList != null && propertyItemList.size() > 0) {

                for (PropertyItem propertyItem : propertyItemList) {
                    MaterialSwitch switcher = new MaterialSwitch();
                    switcher.setLayoutData(propertyItem);
                    switcher.setValue(propertyItem.isActiveModule());

                    MaterialPanel pnlColumn = new MaterialPanel("drag-tile" + (switcher.getValue() ? " state-on" : " state-off"));
                    MaterialPanel pnlGrip = new MaterialPanel("drag-tile__grip");

                    HTML columnTitle = new HTML(WordUtils.capitalizeFirst(!"".equals(propertyItem.getPlural()) ? propertyItem.getPlural() : propertyItem.getSingular()));
                    columnTitle.setStyleName("drag-tile__text");

                    MaterialPanel pnlAction = new MaterialPanel("drag-tile__action");
                    pnlAction.add(switcher);


                    pnlColumn.add(pnlGrip);
                    pnlColumn.add(columnTitle);
                    pnlColumn.add(switcher);
                    pnlColumn.setLayoutData(switcher);

                    showColumnDragController.makeDraggable(pnlColumn, pnlGrip);
                    showVerticalPanel.add(pnlColumn);
                }
                values.put(tab, showVerticalPanel);
            }
        }

        private LinkedHashMap<SelectItem, LinkedList<PropertyItem>> getActiveItems() {
            LinkedHashMap<SelectItem, LinkedList<PropertyItem>> result = new LinkedHashMap<>();
            if (values != null && values.size() > 0) {
                for (SelectItem tab : values.keySet()) {
                    LinkedList<PropertyItem> activeProperties = new LinkedList<>();
                    LinkedList<PropertyItem> inactiveProperties = new LinkedList<>();
                    VerticalPanel showVerticalPanel = values.get(tab);
                    if (showVerticalPanel != null && showVerticalPanel.getWidgetCount() > 0) {
                        for (int i = 0; i < showVerticalPanel.getWidgetCount(); i++) {
                            MaterialSwitch materialSwitch = (MaterialSwitch) (showVerticalPanel.getWidget(i).getLayoutData());
                            PropertyItem item = (PropertyItem) materialSwitch.getLayoutData();
                            if (materialSwitch.getValue()) {
                                item.setActiveModule(true);
                                activeProperties.add(item);
                            } else {
                                item.setActiveModule(false);
                                inactiveProperties.add(item);
                            }
                        }
                    }
                    activeProperties.addAll(inactiveProperties);
                    result.put(tab, activeProperties);
                }
            }
            return result;
        }

        public void createNewSection() {
            KpiModal dialogBox = new KpiModal();
            dialogBox.setTitle(wfmStrings.addSection());
            dialogBox.setWidth(300);

            TextBox textBox = new TextBox();

            WfmButton2 save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
            save.addClickHandler(clickEvent -> {

                String sectionLabel = textBox.getText();

                if (!Utils.isNullOrEmpty(sectionLabel)) {
                    profileService.saveNewTab(section, sectionLabel, new AbstractAsyncCallback<SelectItem>() {
                        @Override
                        public void failure(Throwable throwable) {
                        }

                        @Override
                        public void success(SelectItem result) {
                            dialogBox.close();
                            if (result != null) {
                                drawForm(null, result.getOrderId(), result);
                            }
                        }
                    });
                } else {
                    Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
                }
            });

            WfmButton2 close = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT);
            close.addClickHandler(x -> dialogBox.close());


            dialogBox.addWidget(textBox, wfmStrings.name());

            dialogBox.addButton(close);
            dialogBox.addButton(save);
            dialogBox.open();
        }
    }
}
