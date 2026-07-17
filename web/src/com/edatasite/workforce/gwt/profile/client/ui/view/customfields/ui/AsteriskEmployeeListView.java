package com.edatasite.workforce.gwt.profile.client.ui.view.customfields.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEmployeeRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.communication.AsteriskSettings;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
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
import com.edatasite.workforce.gwt.profile.client.localization.ProfileMessages;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

/**
 * Created by Azazello on 2/5/16.
 */
public class AsteriskEmployeeListView extends BaseListView implements Constants {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private static final ProfileMessages profileMessages = ProfileMessages.App.get();
    private static final ProfileServiceAsync profileService = ProfileService.App.get();
    private ListingPanel<EmployeeListItem> list;
    Integer asteriskSettingsId;
    private static int totalCount;

    public AsteriskEmployeeListView(Integer asteriskSettingsId) {
        super();
        this.asteriskSettingsId = asteriskSettingsId;
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.AsteriskEmployeeListPanel, getColumnConfigs(), getListData(), getDisagn());
        initExporters();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ASTERISK_SETTINGS_ADD_EDIT, AsteriskEmployeeListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[4];
        //////////////////////////---------(0)----------
        columns[0] = new ColumnDefinitionConfig<EmployeeListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final EmployeeListItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                edit.getElement().setId("Twilio_setting_edit_button");
                edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("asteriskEmployeeSettings|add/" + item.getObjectID() + "/" + asteriskSettingsId));
                actionItemCount++;
                menuBar.addItem(edit);

                MenuPopItem changeStatus = new MenuPopItem(item.getStatus().equals("Connected") ? wfmStrings.deactivate() : wfmStrings.activate(), "icon-edit");
                changeStatus.getElement().setId("Twilio_setting_status_button");
                changeStatus.setCommand(() -> {
                    profileService.getAsteriskSettings(item.getObjectID(), asteriskSettingsId, new AsyncCallback<AsteriskSettings>() {
                        @Override
                        public void onFailure(Throwable throwable) {

                        }

                        @Override
                        public void onSuccess(AsteriskSettings asteriskSettings) {
                            if (asteriskSettings.getAsteriskUsername() != null && asteriskSettings.getAsteriskPassword() != null && !asteriskSettings.getAsteriskUsername().equals("") && !asteriskSettings.getAsteriskPassword().equals("")) {
                                profileService.saveEmployeeAsteriskSettings(asteriskSettings, !item.getStatus().equals("Connected"), new AsyncCallback<Integer>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {

                                    }

                                    @Override
                                    public void onSuccess(Integer integer) {

                                    }
                                });
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ASTERISK_SETTINGS_ADD_EDIT, asteriskSettings, AsteriskEmployeeListView.this);
                            } else {
                                WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, true);
                                messageBox.setTitle(wfmStrings.errorOccurred());
                                messageBox.setMessage(settingsStrings.enterUsernameAndPassword());
                                messageBox.open();

                            }
                        }
                    });
                });
                actionItemCount++;
                menuBar.addItem(changeStatus);

                /*MenuPopItem MenuItem = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                MenuItem.getElement().setId("Twilio_setting_delete_button");
                MenuItem.setCommand(() -> {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.warning());
                    messageBox.setMessage(profileMessages.messAreDeleteSMSAccount(item.getAsteriskNumber()));
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            service.deleteAsteriskSettings(item.getId(), new AbstractAsyncCallback() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    LoadingPanel.loading(false);
                                }

                                @Override
                                public void onSuccess(Object result) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.successfullyDeleted(), Info.Type.INFO);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ASTERISK_SETTINGS_ADD_EDIT, result, AsteriskEmployeeListView.this);
                                }
                            });
                        }
                    });
                    messageBox.open();
                });
                menuBar.addItem(MenuItem);*/

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);

        //////////////////////////---------(1)----------
        columns[1] = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.employee(), EmployeeListItem.EMPLOYEE_NAME, 100) {
            @Override
            public String getCellValue(EmployeeListItem item) {
                return item.getFullName();
            }
        };
        columns[1].setMinimumColumnWidth(40);
        columns[1].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        //////////////////////////---------(2)----------
        columns[2] = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.status(), EmployeeListItem.STATUS, 100) {
            @Override
            public String getCellValue(EmployeeListItem item) {
                return item.getStatus();
            }
        };
        columns[2].setMinimumColumnWidth(40);
        columns[2].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        //////////////////////////---------(2)----------
        columns[3] = new ColumnDefinitionConfig<EmployeeListItem, String>(settingsStrings.internalNumber(), EmployeeListItem.USERNAME, 100) {
            @Override
            public String getCellValue(EmployeeListItem item) {
                return item.getAsteriskUsername();
            }
        };
        columns[3].setMinimumColumnWidth(40);
        columns[3].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
       /* columns[3] = new ColumnDefinitionConfig<EmployeeListItem, SimpleLink>(settingsStrings.action(), EmployeeListItem.ACTION, 40) {
            @Override
            public SimpleLink getCellValue(EmployeeListItem item) {
                return new SimpleLink(settingsStrings.edit(), "asteriskEmployeeList|add/add/" + item.getObjectID(), settingsStrings.edit());
            }
        };
        columns[3].setMinimumColumnWidth(20);
        columns[3].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);*/

        return columns;
    }

    /*protected ListPanelType getPanelType() {
        return ListPanelType.EmailFilterListPanel;
    }*/

    private ListingPanelDesign getDisagn() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> {
                            data.setName(PermissionConstants.PM_CONTEXT);
                            RbacService.App.get().getEmployeeFacetFilterData(data, new AsyncCallback<FacetFilterRpc>() {
                                @Override
                                public void onFailure(Throwable throwable) {
                                    callback.onFailure(throwable);
                                }

                                @Override
                                public void onSuccess(FacetFilterRpc facetFilterRpc) {
                                    callback.onSuccess(facetFilterRpc);
                                }
                            });
                        };
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return getFacetContentConfigure();
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                /*ActionButton addNew = getAddNewButton();
                addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("asteriskSetting|add/add"));
                return addNew;*/
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption option, MaterialDropDown menuContainer) {
                option.initExport(null, true);
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(settingsStrings.currentlyNoAsteriskAccounts());
//                message.setHref("asteriskSetting|add/add");
                message.setTextBeforeLink(settingsStrings.addingAsteriskAccountClicking());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider<EmployeeListItem> getListData() {
        return (filterParametrs, callback) -> {
            filterParametrs = filterParametrs == null ? new ListingFilterParameter() : filterParametrs;
            filterParametrs.setResignedEmployeesIncluded(false);
            filterParametrs.setShowActive(true);
            filterParametrs.setAllEmployees(true);
            profileService.getAsteriskEmployeeList(asteriskSettingsId, filterParametrs, new AbstractAsyncCallback<ListResult<EmployeeListItem>>() {
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void onSuccess(ListResult<EmployeeListItem> result) {
                    callback.onSuccess(result);
                    totalCount = result.getTotal();
                }
            });
        };
    }

    public String getIconStyle() {
        return "icon-sms";
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

    private void initExporters() {
        list.setPDFListener(clickEvent -> {
            if (totalCount > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String pdfUrl = CommandConstants.PDF_URL + "/asteriskEmployeeListPDFHandler";
            ListingFilterParameter listingFilterParameter = list.getFilterParametrs();
            listingFilterParameter.setRelationID(asteriskSettingsId);
            list.callListPDF(pdfUrl, listingFilterParameter);
        });

        list.setExcelListener(clickEvent -> {
            String excelUrl = CommandConstants.COMMON_URL + "/downloadAsteriskEmployeeListExcel";
            ListingFilterParameter filterParameter = list.getFilterParametrs();
            filterParameter.setRelationID(asteriskSettingsId);
            list.callListExcel(excelUrl, filterParameter);
        });

    }

    private FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(3,wfmStrings.filter());
        contentConfigure.addContentConfigure("department", Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrEmployeeRepresenter.SORTABLE_DEPARTMENT_NAME;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrEmployeeRepresenter.SORTABLE_DEPARTMENT_NAME;
            }
        });
        contentConfigure.addContentConfigure("position", wfmStrings.position(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrEmployeeRepresenter.SORTABLE_POSITION_NAME;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrEmployeeRepresenter.SORTABLE_POSITION_NAME;
            }
        });
        contentConfigure.addContentConfigure("status in asterisk", wfmStrings.status(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrEmployeeRepresenter.SORTABLE_STATUS_NAME;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrEmployeeRepresenter.SORTABLE_STATUS_NAME;
            }
        });
        return contentConfigure;
    }
}
