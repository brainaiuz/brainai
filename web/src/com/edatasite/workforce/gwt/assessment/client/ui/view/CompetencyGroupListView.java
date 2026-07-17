package com.edatasite.workforce.gwt.assessment.client.ui.view;

import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillGroupItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;

public class CompetencyGroupListView extends BaseListView {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private ListingPanel<SkillGroupItem> listingPanel;
    private KpiModal skillGroupShell;
    private Long editObjectId;
    private String editObjectCode;
    private SkillGroupItem item;

    public CompetencyGroupListView() {
        super("competencesGroupView");
        setDescription(Property.getPluralWithObjectCode(
                "competencesGroupView",
                wfmStrings.groups() ));
    }

    @Override
    protected Widget onInitialize() {
        listingPanel = new ListingPanel<>(
                ListPanelType.CompetencyGroupListPanel,
                getColumnsConfig(),
                getRequestProvider(),
                getDesign()
        );

        // Reload after add/delete
        WfmUiEventsBus.addWfmUiListener(
                WfmUiEventType.ON_ADD_COMPETENCY_GROUP,
                CompetencyGroupListView.this,
                (sender, args) -> listingPanel.reloadPage()
        );

        add(listingPanel);
        return null;
    }

    private ColumnDefinitionConfig[] getColumnsConfig() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[3];

        // ACTION column
        columns[0] = new ColumnDefinitionConfig<SkillGroupItem, Anchor>(
                wfmStrings.action(),
                Constants.LISTING_ACTION.COLUMN_CODE,
                Constants.LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final SkillGroupItem item) {
                editObjectId = Long.valueOf(item.getId());
                editObjectCode = item.getCode();
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                // edit action
                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                edit.setCommand(() -> {
                    if (item.getLocalization() != null) {
                        showAddGroup(item.getId(), item.getName(), item.getParentName(), item.getLocalization().getId());
                    } else {
                        showAddGroup(item.getId(), item.getName(), item.getParentName(), null);
                    }
                });
                actionItemCount++;
                menuBar.addItem(edit);

                // delete action
                MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                removeItem.setCommand(() -> {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(wfmStrings.sureYouWantToDelete());
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            AssessmentService.App.get().deleteCompetencyGroup(item.getId(),
                                    new AbstractAsyncCallback<Void>() {
                                        @Override
                                        public void failure(Throwable throwable) {
                                            LoadingPanel.loading(false);
                                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                        }

                                        @Override
                                        public void success(Void result) {
                                            LoadingPanel.loading(false);
                                            Info.show(wfmStrings.messSuccessfulyyDeleted(), Info.Type.INFO);
                                            listingPanel.reloadPage();
                                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_COMPETENCY_DELETE,
                                                    result, CompetencyGroupListView.this);
                                        }
                                    });
                        }
                    });
                    message.open();
                });
                if (Utils.hasPermission(PermissionConstants.HRMS_REMOVE_COMPETENCES)) {
                    actionItemCount++;
                    menuBar.addItem(removeItem);
                }

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setColumnSortable(false);
        columns[0].setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);

        // parent column
        columns[1] = new ColumnDefinitionConfig<SkillGroupItem, String>(
                wfmStrings.parent(),
                SkillGroupItem.COMPETENCY_GROUP_PARENT_NAME,
                150) {
            @Override
            public String getCellValue(SkillGroupItem item) {
                if (item == null) return "";
                String parent = item.getParentName();
                return (parent == null) ? "" : parent;
            }
        };
        columns[1].setColumnSortable(true);

        // name column
        columns[2] = new ColumnDefinitionConfig<SkillGroupItem, String>(
                wfmStrings.name(),
                SkillGroupItem.COMPETENCY_GROUP_NAME,
                150) {
            @Override
            public String getCellValue(SkillGroupItem item) {
                return item.getLocalization() != null && !Utils.isNullOrEmpty(item.getLocalization().getLocalizedName())
                        ? item.getLocalization().getLocalizedName()
                        : item.getName();
            }
        };
        columns[2].setColumnSortable(true);

        return columns;
    }

    // Popup for Add/Edit
    private void showAddGroup(Integer id, String itemName, String parentName, Integer localizationId) {
        skillGroupShell = new KpiModal();

        final TextBox newGroup = new TextBox();
        newGroup.setText(itemName);

        final TextBox parentGroup = new TextBox();
        if (!Utils.isNullOrEmpty(parentName)) {
            parentGroup.setText(parentName);
        }

        MaterialLink localeLink = new MaterialLink(wfmStrings.localization());

        WfmButton2 ok = new WfmButton2(
                wfmStrings.save(),
                WfmButton2.BTN_PRIMARY,
                event -> saveCompetencyGroup(id, newGroup, parentGroup.getText())
        );

        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), event -> skillGroupShell.close());

        FormGroup nameFormGroup = new FormGroup(wfmStrings.name(), newGroup, true);
        nameFormGroup.getGroupLabel().add(localeLink);
        FormGroup parentFormGroup = new FormGroup(wfmStrings.parent(), parentGroup, false);

        skillGroupShell.add(nameFormGroup);
        skillGroupShell.add(parentFormGroup);
        skillGroupShell.setTitle(wfmStrings.name());
        skillGroupShell.addButton(cancel);
        skillGroupShell.addButton(ok);
        skillGroupShell.setWidth(400);
        skillGroupShell.open();
    }

    private void saveCompetencyGroup(Integer id, TextBox textBox, String parentName) {
        String competencyGroupName = textBox.getText();

        if (Utils.isNullOrEmpty(competencyGroupName)) {
            textBox.addStyleName("x-form-invalid");
            return;
        }

        if (item == null) item = new SkillGroupItem();

        boolean isEdit = id != null;

        item.setId(id);
        item.setName(competencyGroupName);

        if (!Utils.isNullOrEmpty(parentName)) item.setParentName(parentName);

        LoadingPanel.loading(true);

        AssessmentService.App.get().addSkillGroup(item, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer result) {
                LoadingPanel.loading(false);

                if (result == null) {
                    Info.show(wfmStrings.dataAlreadyExist(), Info.Type.WARNING);
                    return;
                }

                if (isEdit) {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.group()), Info.Type.INFO);
                } else {
                    Info.show(wfmStrings.messSuccessfullyAdded(), Info.Type.INFO);
                }

                listingPanel.reloadPage();
                skillGroupShell.close();
            }
        });
    }

    private ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.HRMS_ADD_COMPETENCES)) {
                    ActionButton newSkillGroup = getAddNewButton();
                    newSkillGroup.addClickHandler(event -> showAddGroup(null, "", null, null));
                    return newSkillGroup;
                }
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {}
        };
    }

    private ListingRequestProvider<SkillGroupItem> getRequestProvider() {
        return (filterParams, callback) -> {
            ListingFilterParameter fp = filterParams;

            // Map UI column to entity field
            String sortField = fp.getSortField();
            switch (sortField) {
                case "name":
                case "competencyGroupName":
                    fp.setSortField("name");
                    break;
                case "parent":
                case "parentName":
                    fp.setSortField("parentName");
                    break;
                default:
                    fp.setSortField("id");
            }

            boolean asc = fp.isAscending();

            AssessmentService.App.get().getCompetencyGroupList(
                    fp,
                    fp.getSortField(),
                    asc,
                    new AsyncCallback<ListResult<SkillGroupItem>>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            callback.onFailure(throwable);
                        }

                        @Override
                        public void onSuccess(ListResult<SkillGroupItem> result) {
                            callback.onSuccess(result);
                        }
                    });
        };
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

    @Override
    public String getIconStyle() {
        return "assessment competency-group-list";
    }

    public String getPropertyCode() {
        return "competencesGroupView";
    }
}
