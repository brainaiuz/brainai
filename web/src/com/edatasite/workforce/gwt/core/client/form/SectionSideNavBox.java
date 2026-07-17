package com.edatasite.workforce.gwt.core.client.form;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.LocalizationCFModal;
import com.edatasite.workforce.gwt.core.client.ui.LocalizationTypeEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSwitch;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Span;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by Hurshid on 2/21/2018.
 */
public class SectionSideNavBox extends KpiSideNavBox {

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    private final String formID;
    private VerticalPanel verticalPanel;
    private PickupDragController pickupDragController;
    private MaterialPanel container;
    private final LinkedList<DynamicSectionsRpc> sections = new LinkedList<>();
    private SectionDropController columnDropController;
    private IDynamicColumn dynamicColumn;

    SectionSideNavBox(String formID) {
        this.formID = formID;
        addStyleName("quick-add");

        initalize();
        initalizeBody();
    }

    private void initalize() {
        Heading header = new Heading(HeadingSize.H1);
        header.addStyleName("hasicon--left");

        Span span = new Span();
        span.setText(wfmStrings.orderSections());
        header.add(span);

        WfmButton2 save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);


        save.addClickHandler(clickEvent -> {

            Long activeRpc = sections.stream().filter(DynamicSectionsRpc::isActive).count();

            if (activeRpc.intValue() < 1) {
                Info.warn(wfmStrings.atLeastOne().toLowerCase(), Info.Position.BOTTOM_LEFT);
                return;
            }

            save.setEnabled(false);

            LoadingPanel.loading(true, this);
            AllInOneService.App.get().saveSectionOrder(formID, sections, new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                    save.setEnabled(true);
                }

                @Override
                public void success(Void result) {
                    LoadingPanel.loading(false);
                    save.setEnabled(true);

                    if (command != null)
                        command.execute();

                    hide();
                }
            });
        });

        ActionButton addNew = new ActionButton(wfmStrings.add(), ActionButton.Type.BUTTON);
        addNew.setStyleName("btn btn--success hasicon--right");
        DynamicSectionsRpc rpc = new DynamicSectionsRpc();
        rpc.setFormID(formID);
        addNew.addClickHandler(event -> createNewSectionDialog(rpc));

        addHeader(header);
        addHeader(addNew);
        addFooter(save);

    }

    private void initalizeBody() {
        container = new MaterialPanel();
        AbsolutePanel boundaryPanel = new AbsolutePanel();

        verticalPanel = new VerticalPanel();

        boundaryPanel.add(verticalPanel);
        container.add(boundaryPanel);

        pickupDragController = new PickupDragController(boundaryPanel, false);
        pickupDragController.setBehaviorMultipleSelection(false);

        columnDropController = new SectionDropController(verticalPanel);
        pickupDragController.registerDropController(columnDropController);

        addOpeningHandler(event -> {
            if (sections == null || sections.isEmpty())
                getSections();
        });

        addBody(container);
    }

    private void getSections() {
        LoadingPanel.loading(true, this);
        AllInOneService.App.get().getCustomizeFormSections(formID, false, new AbstractAsyncCallback<LinkedHashMap<String, DynamicSectionsRpc>>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
                LoadingPanel.loading(false);
            }

            @Override
            public void success(LinkedHashMap<String, DynamicSectionsRpc> result) {
                result.forEach((k, v) -> sections.add(v));

                sections.stream()
                        .sorted(Comparator.comparingInt(DynamicSectionsRpc::getSorder))
                        .forEach(Section::new);

                columnDropController.setList(sections);

                LoadingPanel.loading(false);
            }
        });
    }

    private void createNewSectionDialog(DynamicSectionsRpc rpc) {
        dialogBox = new KpiModal();
        dialogBox.setTitle(rpc.getId() != null ? wfmStrings.editSection() : wfmStrings.addSection());
        dialogBox.setWidth(300);

        TextBox textBox = new TextBox();
        if (rpc.getLabel() != null && rpc.getLabel().length() > 0) {
            textBox.setValue(rpc.getLabel());
        } else {
            textBox.setValue(rpc.getName());
        }

        WfmButton2 save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        save.addClickHandler(clickEvent -> {

            String sectionLabel = textBox.getText();

            if (sectionLabel != null && sectionLabel.trim().length() > 0) {
                rpc.setLabel(sectionLabel);
                if (rpc.getName() == null || rpc.getName().length() == 0) {
                    rpc.setName(sectionLabel);
                }

                LoadingPanel.loading(true, container);
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
                            dialogBox.close();

                            String type = rpc.getId() == null ? wfmStrings.created() : wfmStrings.updated();
                            Info.show(wfmStrings.section() + " " + wfmStrings.successfully() + " " + type, Info.Position.TOP_RIGHT);
                            reinit();
                        }
                    }
                });
            }
        });

        WfmButton2 close = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT);
        close.addClickHandler(x -> dialogBox.close());


        dialogBox.addWidget(textBox, wfmStrings.name());

        dialogBox.addButton(close);
        dialogBox.addButton(save);
        dialogBox.open();
    }


    private KpiModal dialogBox;

    class Section extends MaterialPanel {

        private final DynamicSectionsRpc sectionsRpc;

        public Section(DynamicSectionsRpc sectionsRpc) {
            this.sectionsRpc = sectionsRpc;

            addStyleName("drag-tile" + (this.sectionsRpc.isActive() ? " state-on" : " state-off"));

            createNewColumnPanel(sectionsRpc);
        }

        private void createNewColumnPanel(DynamicSectionsRpc sectionsRpc) {
            MaterialSwitch switcher = new MaterialSwitch();
            switcher.setValue(sectionsRpc.isActive());
            switcher.setTooltip(sectionsRpc.isActive() ? wfmStrings.inactivateSection() : wfmStrings.activateSection());


            MaterialPanel pnlGrip = new MaterialPanel("drag-tile__grip");

            String s = sectionsRpc.getLabel();
            if (s == null || s.length() == 0) {
                s = Localize.getInstance().localizeByFieldID(formID, sectionsRpc.getName());
                sectionsRpc.setLabel(s != null ? s.toUpperCase() : sectionsRpc.getName());
            }
            HTML columnTitle = new HTML(s != null ? s.toUpperCase() : sectionsRpc.getName());
            columnTitle.setStyleName("drag-tile__text wg_dragswtich__title");

            MaterialPanel pnlAction = new MaterialPanel("drag-tile__actions");
            pnlAction.add(switcher);

            switcher.addValueChangeHandler(vh -> {

                sectionsRpc.setActive(switcher.getValue());
                switcher.setTooltip(sectionsRpc.isActive() ? wfmStrings.inactivateSection() : wfmStrings.activateSection());

                if (!switcher.getValue() && dynamicColumn != null) {
                    boolean active = dynamicColumn.execute(sectionsRpc.getName(), true);
                    switcher.setValue(active);
                }

                if (switcher.getValue()) {
                    removeStyleName("state-off");
                    addStyleName("state-on");

                } else {
                    removeStyleName("state-on");
                    addStyleName("state-off");
                }
            });

            add(pnlGrip);
            add(columnTitle);

            MenuBar menuBar = new MenuBar(true);
            menuBar.setAutoOpen(true);

            if (sectionsRpc.isCustom()) {
                MenuPopItem localization = new MenuPopItem("Localization");
                localization.ensureDebugId("localization-button");
                localization.setCommand(() -> {
                    LocalizationCFModal localizationCFModal = new LocalizationCFModal(sectionsRpc.getId(), LocalizationTypeEnum.SECTION);
                    localizationCFModal.center();
                });
                menuBar.addItem(localization);
            }

            MenuPopItem editButton = new MenuPopItem("Edit");
            editButton.ensureDebugId("edit-button");
            editButton.setCommand(() -> {
                createNewSectionDialog(sectionsRpc);
            });
            menuBar.addItem(editButton);

            if (sectionsRpc.isCustom()) {
                MenuPopItem deleteButton = new MenuPopItem("Delete");
                deleteButton.ensureDebugId("delete-button");
                deleteButton.setCommand(() -> {
                    deleteCustomSection(sectionsRpc.getId());
                });
                menuBar.addItem(deleteButton);
            }

            final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(sectionsRpc.isCustom() ? 3 : 1);
            toolItem.setWidget(menuBar);
            add(toolItem.getAction());
            add(pnlAction);

            verticalPanel.add(this);
            pickupDragController.makeDraggable(this, pnlGrip);
        }

        private void deleteCustomSection(Integer id) {
            final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
            message.setTitle(wfmStrings.confirmationMessage());
            message.setMessage(wfmStrings.areYouSureYouWanttoDeleteThisEntry());
            message.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    AllInOneService.App.get().deletCustomSection(id, new AbstractAsyncCallback<Integer>() {
                        @Override
                        public void failure(Throwable throwable) {
                            Info.warn(wfmStrings.sorrySomethingWentWrong());
                        }

                        @Override
                        public void success(Integer result) {
                            if (result == Constants.ERROR) {
                                Info.warn(wfmStrings.beforeDeleting(), Info.Position.TOP_RIGHT);
                            } else {
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.section()));

                                reinit();
                            }
                        }
                    });
                }
            });
            message.open();
        }

        DynamicSectionsRpc getSectionsRpc() {
            return sectionsRpc;
        }
    }

    private void reinit() {
        clearBody();
        initalizeBody();
        sections.clear();
        getSections();
    }

    void setDynamicColumn(IDynamicColumn dynamicColumn) {
        this.dynamicColumn = dynamicColumn;
    }
}
