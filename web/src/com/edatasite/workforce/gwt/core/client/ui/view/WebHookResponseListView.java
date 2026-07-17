package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingCallback;
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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Span;

/**
 * User : Akhror
 * Date : 11.01.2022
 */
public class WebHookResponseListView extends BaseListView implements Constants, Constants.WorkflowActionConstants {
    private static final SettingStrings settingStrings = SettingStrings.App.get();
    private static final ProfileServiceAsync profileService = ProfileService.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final Integer webHookId;
    private ListingPanel<SelectItem> list;
    private int totalCount = 0;
    private ListingFilterParameter filterParameter;
    private String type;
    private Integer typeId;

    public WebHookResponseListView(Integer webHookId) {
        super("webHookResponses", "WebHook Responses");
        setDescription(wfmStrings.responses());
        this.webHookId = webHookId;
    }

    public WebHookResponseListView(Integer typeId, String type) {
        super("webHookResponses", "WebHook Responses");
        setDescription(wfmStrings.webHook());
        this.typeId = typeId;
        this.type = type;
        this.webHookId = null;
    }

    public void refresh() {
        filterParameter.setStart(0);
        list.reloadPage();
    }

    public FlowPanel getHelpContainer() {
        return null;
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[7];
        int index = -1;

        columns[++index] = new ColumnDefinitionConfig<SelectItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, 20) {

            @Override
            public Anchor getCellValue(SelectItem rowValue) {
                final MenuBar menuBar = new MenuBar(true);
                menuBar.setAutoOpen(true);
                int actionItemCount = 1;

                menuBar.addItem(new MenuPopItem(settingStrings.retry(), "", () -> {
                    LoadingPanel.loading(true);
                    profileService.retryWebhook(rowValue.getId(), new AbstractAsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            LoadingPanel.loading(false);
                            Info.warn(wfmStrings.sorrySomethingWentWrong());
                        }

                        @Override
                        public void onSuccess(Void result) {
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.success());
                        }
                    });
                }));


                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);


                return toolItem.getAction();
            }
        };
        columns[index].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[index].setShow(true);

        columns[++index] = new ColumnDefinitionConfig<SelectItem, String>(wfmStrings.ruleName(), "ruleName", 40) {
            @Override
            public String getCellValue(SelectItem item) {
                return item.getWorkflowRuleName();
            }
        };
        columns[index].setMinimumColumnWidth(40);
        columns[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns[index].setShow(true);

        columns[++index] = new ColumnDefinitionConfig<SelectItem, String>(wfmStrings.status(), "status", 40) {
            @Override
            public String getCellValue(SelectItem item) {
                return item.getParam();
            }
        };
        columns[index].setMinimumColumnWidth(40);
        columns[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        columns[++index] = new ColumnDefinitionConfig<SelectItem, Anchor>(wfmStrings.response(), "response", 250) {
            @Override
            public Anchor getCellValue(SelectItem item) {
                Anchor anchor = new Anchor(item.getName());
                anchor.addClickHandler(event -> {
                    boolean success = copyToClipboard(item.getName());
                    if (!success) {
                        Info.warn("Copy Failed()");
                        return;
                    }
                    Info.show("Copied To Clipboard()");
                });
                return anchor;
            }
        };
        columns[index].setMinimumColumnWidth(40);
        columns[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns[index].setShow(true);


        columns[++index] = new ColumnDefinitionConfig<SelectItem, String>(wfmStrings.createdDate(), "createdDate", 40) {
            @Override
            public String getCellValue(SelectItem item) {
                return item.getDescription();
            }
        };
        columns[index].setMinimumColumnWidth(40);
        columns[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns[index].setShow(true);


        columns[++index] = new ColumnDefinitionConfig<SelectItem, String>(wfmStrings.webhookName(), "webhookName", 40) {
            @Override
            public String getCellValue(SelectItem item) {
                return item.getCategory();
            }
        };
        columns[index].setMinimumColumnWidth(40);
        columns[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns[index].setShow(true);

        columns[++index] = new ColumnDefinitionConfig<SelectItem, Anchor>(wfmStrings.request(), "request", 40) {
            @Override
            public Anchor getCellValue(SelectItem item) {
                Anchor anchor = new Anchor(item.getNumber());
                anchor.addClickHandler(event -> {
                    boolean success = copyToClipboard(item.getNumber());
                    if (!success) {
                        Info.warn("Copy Failed()");
                        return;
                    }
                    Info.show("Copied To Clipboard()");
                });

                return anchor;
            }
        };
        columns[index].setMinimumColumnWidth(40);
        columns[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns[index].setShow(true);



        return columns;
    }

    protected ListPanelType getPanelType() {
        return ListPanelType.WebHookResponsesListPanel;
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(getPanelType(), getColumnConfigs(), getListData(), getListingDesign());

        add(list);
        return null;
    }

    private ListingPanelDesign getListingDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public ActionButton initTopToolBarNew() {
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {

            }
        };
    }

    private ListingRequestProvider<SelectItem> getListData() {
        return (filterParameters, callback) -> loadResponsesList(filterParameters, callback, null);
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        loadResponsesList(fp, null, container);
    }

    private void loadResponsesList(ListingFilterParameter fp, ListingCallback callback, Span container) {
        if (filterParameter == null) {
            filterParameter = new ListingFilterParameter();
        }
        filterParameter.setStart(fp.getStart());
        filterParameter = fp;
        filterParameter.setSearchKey(fp.getSearchKey());
        filterParameter.setStart(fp.getStart());
        filterParameter.setLimit(fp.getLimit());
        filterParameter.setRelationID(webHookId);
        if (webHookId != null) {
            profileService.getWebHookResponses(filterParameter, new AsyncCallback<ListResult<SelectItem>>() {
                public void onFailure(Throwable throwable) {
                    if (callback != null) {
                        callback.onFailure(throwable);
                    }
                }

                @Override
                public void onSuccess(ListResult<SelectItem> webHooks) {
                    totalCount = webHooks.getTotal();
                    if (callback != null) {
                        callback.onSuccess(webHooks);
                    }
                    statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                    if (statisticShortcut != null) {
                        if (totalCount > 0) {
                            statisticShortcut.setText(countFormat(totalCount));
                            statisticShortcut.setClass("tab-label");
                        } else {
                            statisticShortcut.setText("");
                            statisticShortcut.removeStyleName("tab-label");
                        }
                    }
                }
            });
        } else {
            profileService.getWebHookResponsesByType(typeId, type, new AsyncCallback<ListResult<SelectItem>>() {
                public void onFailure(Throwable throwable) {
                    if (callback != null) {
                        callback.onFailure(throwable);
                    }
                }

                @Override
                public void onSuccess(ListResult<SelectItem> webHooks) {
                    totalCount = webHooks.getTotal();
                    if (callback != null) {
                        callback.onSuccess(webHooks);
                    }
                    statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                    if (statisticShortcut != null) {
                        if (totalCount > 0) {
                            statisticShortcut.setText(countFormat(totalCount));
                            statisticShortcut.setClass("tab-label");
                        } else {
                            statisticShortcut.setText("");
                            statisticShortcut.removeStyleName("tab-label");
                        }
                    }
                }
            });
        }
    }

    public String getIconStyle() {
        return "accountMark report-list";
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
    private native boolean copyToClipboard(String text) /*-{
        $wnd.navigator.clipboard.writeText(text).then(function() {
            console.log("Text copied successfully: " + text);
        }, function(err) {
            console.error("Failed to copy: ", err);
        });
        return true;
    }-*/;

}
