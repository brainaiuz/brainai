package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.WordUtils;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.form.formbuild.CustomFormQuickAddView;
import com.edatasite.workforce.gwt.core.client.form.formbuild.CustomizeModulesSettingsPopup;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
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
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Br;

import java.util.ArrayList;
import java.util.List;

public class OrganizeModuleListView extends BaseListView {
    protected static final SettingStrings settingsStrings = SettingStrings.App.get();
    protected static final ProfileServiceAsync profileService = ProfileService.App.get();

    protected ListingPanel<PropertyItem> listing;
    private DataListBox moduleList;

    private CustomFormQuickAddView quickAddView;

    public OrganizeModuleListView() {
        super("organizeModuleList", settingsStrings.organizeModules());
    }

    protected Widget onInitialize() {
        listing = new ListingPanel<>(ListPanelType.OrganizeModulListPanel, getColumns(), getListData(), getDesign());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ORGANIZE_MODULE_RELOAD_PAGE, OrganizeModuleListView.this, (sender, args) -> listing.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CUSTOM_FORM_BUILD, OrganizeModuleListView.this, (sender, args) -> listing.reloadPage());
        add(listing);
        return null;
    }


    //list column properties and configuration

    protected ColumnDefinitionConfig[] getColumns() {
        List<ColumnDefinitionConfig> columnsConfigList = new ArrayList<>();

        ColumnDefinitionConfig column = new ColumnDefinitionConfig<PropertyItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final PropertyItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem edit = new MenuPopItem(wfmStrings.edit());
                edit.getElement().setId("rename");
                edit.setCommand(() -> {
                    if (item.isCustom()) {
                        if (quickAddView == null){
                            quickAddView = new CustomFormQuickAddView(item.getId(), moduleList.isSomethingSelected() ? moduleList.getSelectedItem().getDescription() : null);
                        } else {
                            quickAddView.setValues(item.getId(), moduleList.isSomethingSelected() ? moduleList.getSelectedItem().getDescription() : null, false);
                        }
                    } else {
                        new OrganizeModuleView(item.getId(), item.getObjectName(), moduleList.isSomethingSelected() ? moduleList.getSelectedItem().getDescription() : null);
                    }
                });
                actionItemCount++;
                menuBar.addItem(edit);

                MenuPopItem status = new MenuPopItem(item.isActive() ? wfmStrings.disable() : wfmStrings.enable());
                status.getElement().setId("status");
                status.setCommand(() -> changeStatus(item.getId()));
                actionItemCount++;
                menuBar.addItem(status);

                if (item.isCustom()) {
                    MenuPopItem localization = new MenuPopItem(wfmStrings.localization());
                    localization.getElement().setId("localizationCF");
                    localization.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("customFormLocalization|edit/" + item.getFormID(), item.getDefaultName()));
                    actionItemCount++;
                    menuBar.addItem(localization);

                    MenuPopItem copy = new MenuPopItem(wfmStrings.copy());
                    copy.getElement().setId("copyForm");
                    copy.setCommand(() -> {
                        if (quickAddView == null) {
                            quickAddView = new CustomFormQuickAddView(item.getId(), moduleList.isSomethingSelected() ? moduleList.getSelectedItem().getDescription() : null, true);
                        } else {
                            quickAddView.setValues(item.getId(), moduleList.isSomethingSelected() ? moduleList.getSelectedItem().getDescription() : null, true);
                        }
                    });
                    actionItemCount++;
                    menuBar.addItem(copy);

                    MenuPopItem customize = new MenuPopItem(wfmStrings.customize());
                    customize.getElement().setId("cstomize");
                    String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                    customize.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm2|add/add" + "/" + item.getFormID() + "/" + URL.encodeQueryString(url)));
                    actionItemCount++;
                    menuBar.addItem(customize);

                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete());
                    delete.getElement().setId("delete");
                    delete.setCommand(() -> deleteForm(item.getfID()));
                    actionItemCount++;
                    menuBar.addItem(delete);

                    MenuPopItem link = new MenuPopItem(wfmStrings.links());
                    link.getElement().setId("link");
                    link.setCommand(() -> {
                        openLinksCopyBox(item);
                    });
                    actionItemCount++;
                    menuBar.addItem(link);
                } else {
                    MenuPopItem reset = new MenuPopItem(wfmStrings.reset());
                    reset.getElement().setId("reset");
                    reset.setCommand(() -> resetProperty(item));
                    actionItemCount++;
                    menuBar.addItem(reset);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        columnsConfigList.add(column);


        column = new ColumnDefinitionConfig<PropertyItem, SimpleLink>(wfmStrings.name(), PropertyItem.DEFAULT_NAME, 120) {
            @Override
            public SimpleLink getCellValue(PropertyItem item) {
                SimpleLink link = new SimpleLink(item.getDefaultName());
                link.addClickHandler(click -> {
                    if (item.isCustom()) {
                        if (quickAddView == null) {
                            quickAddView = new CustomFormQuickAddView(item.getId(), moduleList.isSomethingSelected() ? moduleList.getSelectedItem().getDescription() : null);
                        } else {
                            quickAddView.setValues(item.getId(), moduleList.isSomethingSelected() ? moduleList.getSelectedItem().getDescription() : null, false);
                        }
                    } else {
                        new OrganizeModuleView(item.getId(), item.getObjectName(), moduleList.isSomethingSelected() ? moduleList.getSelectedItem().getDescription() : null);
                    }


                });
                return link;
            }
        };
        column.setMinimumColumnWidth(100);
        columnsConfigList.add(column);


        column = new ColumnDefinitionConfig<PropertyItem, String>(settingsStrings.customName(), PropertyItem.CUSTOM_NAME, 120) {
            @Override
            public String getCellValue(PropertyItem item) {
                return item.getSingular();
            }
        };
        column.setMinimumColumnWidth(100);
        columnsConfigList.add(column);


        column = new ColumnDefinitionConfig<PropertyItem, String>(wfmStrings.modifiedDate(), PropertyItem.LAST_MODIFIED, 100) {
            @Override
            public String getCellValue(PropertyItem item) {
                return DateUtils.format(item.getModifiedDate());
            }
        };
        column.setMinimumColumnWidth(30);
        columnsConfigList.add(column);


        column = new ColumnDefinitionConfig<PropertyItem, String>(wfmStrings.modifiedBy(), PropertyItem.MODIFIER, 120) {
            @Override
            public String getCellValue(PropertyItem item) {
                return item.getModifier();
            }
        };
        column.setMinimumColumnWidth(100);
        column.setColumnSortable(false);
        columnsConfigList.add(column);


        column = new ColumnDefinitionConfig<PropertyItem, String>(wfmStrings.status(), PropertyItem.STATUS, 100) {
            @Override
            public String getCellValue(PropertyItem item) {
                return item.isActive() ? wfmStrings.enabled() : wfmStrings.disabled();
            }
        };
        column.setMinimumColumnWidth(80);
        column.setColumnSortable(false);
        columnsConfigList.add(column);


        column = new ColumnDefinitionConfig<PropertyItem, String>(settingsStrings.singular(), PropertyItem.SINGULAR, 100) {
            @Override
            public String getCellValue(PropertyItem item) {
                return !WordUtils.isEmpty(item.getSingular()) ? item.getSingular() : "";
            }
        };
        column.setMinimumColumnWidth(80);
        columnsConfigList.add(column);


        column = new ColumnDefinitionConfig<PropertyItem, String>(wfmStrings.plural(), PropertyItem.PLURAL, 100) {
            @Override
            public String getCellValue(PropertyItem item) {
                return !WordUtils.isEmpty(item.getPlural()) ? item.getPlural() : "";
            }
        };
        column.setMinimumColumnWidth(80);
        columnsConfigList.add(column);


        column = new ColumnDefinitionConfig<PropertyItem, String>(wfmStrings.shortName(), PropertyItem.SHORT_NAME, 100) {
            @Override
            public String getCellValue(PropertyItem item) {
                return !WordUtils.isEmpty(item.getShortcut()) ? item.getShortcut() : "";
            }
        };
        column.setMinimumColumnWidth(80);
        columnsConfigList.add(column);

        return columnsConfigList.toArray(new ColumnDefinitionConfig[]{});
    }


    //Reset Action- List-Action-ResetButton

    private void resetProperty(PropertyItem item) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        messageBox.setMessage(wfmStrings.messAreReset());
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                LoadingPanel.loading(true);
                profileService.resetFormProperty(item, new AbstractAsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        super.onFailure(caught);
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(Void result) {
                        LoadingPanel.loading(false);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ORGANIZE_MODULE_RELOAD_PAGE, null, null);
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.changes()));
                    }
                });
            }
        });
        messageBox.open();
    }


    //Delete CF Action
    private void deleteForm(Integer id) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        messageBox.setMessage(wfmStrings.confirmDeleteDepartment());
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                LoadingPanel.loading(true);
                profileService.deleteForm(id, new AbstractAsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        super.onFailure(caught);
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(Integer result) {
                        LoadingPanel.loading(false);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ORGANIZE_MODULE_RELOAD_PAGE, null, null);
                    }
                });
            }
        });
        messageBox.open();
    }


    //activate/deactivate the CF
    private void changeStatus(Integer id) {
        LoadingPanel.loading(true);
        profileService.updatePropertyStatus(id, new AbstractAsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(Integer result) {
                LoadingPanel.loading(false);
                if (result > 0) {
                    listing.reloadPage();
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.changes()));
                }
            }
        });
    }


    protected ListingRequestProvider<PropertyItem> getListData() {
        return (filterParameter, callback) -> {
            filterParameter.setModule(null);
            if (moduleList.isSomethingSelected()) {
                filterParameter.setModule(moduleList.getSelectedItem().getDescription());
            }
            profileService.getPropertyItems(filterParameter, new AbstractAsyncCallback<ListResult<PropertyItem>>() {
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                public void success(ListResult<PropertyItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }


    protected ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNewPlacement = getAddNewButton();
                addNewPlacement.addClickHandler(event -> {
                    if (quickAddView == null) {
                        quickAddView = new CustomFormQuickAddView(null, moduleList.isSomethingSelected() ? moduleList.getSelectedItem().getDescription() : null);
                    } else {
                        quickAddView.setValues(null, moduleList.isSomethingSelected() ? moduleList.getSelectedItem().getDescription() : null, false);
                    }
                });
                return addNewPlacement;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
            }

            @Override
            public boolean isShowCustomiseButton() {
                return true;
            }

            @Override
            public boolean isShowResetButton() {
                return true;
            }

            @Override
            public HorizontalPanel initTopToolBarWidgets() {
                MaterialLink customizeSection = new MaterialLink(wfmStrings.customizeSections());
                customizeSection.setVisible(false);

                HorizontalPanel topPanel = new HorizontalPanel();
                topPanel.setWidth("auto");
                topPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

                Label moduleLabel = new Label(wfmStrings.apps());

                moduleList = new DataListBox();
                moduleList.addValueChangeHandler(event -> {
                    customizeSection.setVisible(moduleList.isSomethingSelected());
                    listing.reloadPage();
                });

                SelectItem[] selectItem = new SelectItem[]{
                        new SelectItem(1, wfmStrings.accounts(), ModuleEnum.ACCOUNTING.getCode()),
                        new SelectItem(2, wfmStrings.sales(), ModuleEnum.CRM.getCode()),
                        new SelectItem(3, wfmStrings.hrms(), ModuleEnum.HRMS.getCode()),
                        new SelectItem(4, wfmStrings.project(), ModuleEnum.PM.getCode()),
                        new SelectItem(5, wfmStrings.payroll(), ModuleEnum.PAYROLL.getCode()),
//                        new SelectItem(6, wfmStrings.documents(), ModuleEnum.DOCUMENTS.getCode()),
                        new SelectItem(7, wfmStrings.settings(), ModuleEnum.SETTINGS.getCode()),
                        new SelectItem(8, wfmStrings.trainingCenter(), ModuleEnum.TRAINING_CENTER.getCode()),
                };
                moduleList.setItems(selectItem);

                topPanel.add(moduleLabel);
                topPanel.setCellVerticalAlignment(moduleLabel, HasVerticalAlignment.ALIGN_MIDDLE);
                topPanel.add(new HTML(":&nbsp;&nbsp;"));
                topPanel.add(moduleList);
                topPanel.setCellVerticalAlignment(moduleList, HasVerticalAlignment.ALIGN_MIDDLE);

                customizeSection.addClickHandler(clickEvent -> new CustomizeModulesSettingsPopup(moduleList.isSomethingSelected() ? moduleList.getSelectedItem().getDescription() : null));
                topPanel.add(new HTML("&nbsp"));
                topPanel.add(customizeSection);

                return topPanel;
            }
        };
    }


    private void openLinksCopyBox(PropertyItem item) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.OK, true);
        messageBox.setTitle(wfmStrings.info());
        messageBox.setMessage("");

        MaterialLink link = new MaterialLink();

        link.setText(settingsStrings.customFormLinkWithoutAccess());
        link.addClickHandler(event -> {
            Utils.copyToClipBoard(item.getLink());
            messageBox.close();
            Info.show(wfmStrings.successfullyCopiedLinkToClipboard());
        });
        messageBox.add(link);
        messageBox.add(new Br());

        MaterialLink linkWithAccess = new MaterialLink();
        linkWithAccess.setText(settingsStrings.customFormLinkWithAccess());
        linkWithAccess.addClickHandler(event -> {
            Utils.copyToClipBoard(item.getLinkWithAccess());
            messageBox.close();
            Info.show(wfmStrings.successfullyCopiedLinkToClipboard());
        });
        messageBox.add(linkWithAccess);

        messageBox.open();
    }

    @Override
    public String getIconStyle() {
        return null;
    }

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
}
