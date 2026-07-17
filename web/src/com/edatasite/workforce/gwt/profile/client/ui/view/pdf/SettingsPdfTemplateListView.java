package com.edatasite.workforce.gwt.profile.client.ui.view.pdf;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfTemplateService;
import com.edatasite.workforce.gwt.profile.client.rpc.SettingsPdfTemplateListItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Abror Abdukadirov
 * Date: 06.12.2018 14:50
 */
public class SettingsPdfTemplateListView extends BaseListView {
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private ListingPanel listPanel;

    public SettingsPdfTemplateListView() {
        super("pdfTemplateList", wfmStrings.pdfTemplates());
    }

    @Override
    protected Widget onInitialize() {
        listPanel = new ListingPanel(ListPanelType.SettingsPdfTemplateListPanel, getColumnConfig(), getListProvider(), getListDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SETTINGS_PDF_TEMPLATE_ADD_EDIT, SettingsPdfTemplateListView.this, (sender, args) -> listPanel.reloadPage());
        add(listPanel);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        ColumnDefinitionConfig[] columnConfig = new ColumnDefinitionConfig[7];

        columnConfig[0] = new ColumnDefinitionConfig<SettingsPdfTemplateListItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final SettingsPdfTemplateListItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem templateEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                templateEdit.ensureDebugId("edit");
                templateEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("pdftemplate|summary/" + item.getObjectId() + "/" + item.getPdfType()));
                actionItemCount++;
                menuBar.addItem(templateEdit);
                MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                removeItem.ensureDebugId("delete");
                removeItem.setCommand(() -> {
                    WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    message.setTitle(wfmStrings.confirmationMessage());
                    message.setMessage(wfmMessages.sureYouWantToDelete("<b>" + item.getName() + "</b>", ""));
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            PdfTemplateService.App.get().deleteSettingsPdfTemplate(item.getObjectId(), new AbstractAsyncCallback<Void>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void onSuccess(Void result) {
                                    Info.show(settingsStrings.pdfTemplateDeleted(), Info.Type.INFO);
                                    listPanel.reloadPage();
                                }
                            });
                        }
                    });
                    message.open();
                });
                menuBar.addItem(removeItem);

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig[0].setColumnSortable(false);
        columnConfig[0].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columnConfig[0].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);

        columnConfig[1] = new ColumnDefinitionConfig<SettingsPdfTemplateListItem, SimpleLink>(wfmStrings.name(), SettingsPdfTemplateListItem.NAME, 90) {
            @Override
            public SimpleLink getCellValue(SettingsPdfTemplateListItem item) {
                return getLink(item.getName(), "pdftemplate|summary/" + item.getObjectId() + "/" + item.getPdfType());
            }
        };
        columnConfig[1].setMinimumColumnWidth(50);

        columnConfig[2] = new ColumnDefinitionConfig<SettingsPdfTemplateListItem, String>(wfmStrings.category(), SettingsPdfTemplateListItem.CATEGORY, 90) {
            @Override
            public String getCellValue(SettingsPdfTemplateListItem item) {
                return item.getCategory();
            }
        };
        columnConfig[2].setMinimumColumnWidth(50);

        columnConfig[3] = new ColumnDefinitionConfig<SettingsPdfTemplateListItem, String>(wfmStrings.isDefault(), SettingsPdfTemplateListItem.IS_DEFAULT, 50) {
            @Override
            public String getCellValue(SettingsPdfTemplateListItem item) {
                return item.isDefault() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        columnConfig[3].setMinimumColumnWidth(20);
        columnConfig[3].setColumnSortable(false);

        columnConfig[4] = new ColumnDefinitionConfig<SettingsPdfTemplateListItem, String>(wfmStrings.modifiedBy(), SettingsPdfTemplateListItem.MODIFIED_BY, 90) {
            @Override
            public String getCellValue(SettingsPdfTemplateListItem item) {
                return item.getModifiedBy();
            }
        };
        columnConfig[4].setMinimumColumnWidth(50);

        columnConfig[5] = new ColumnDefinitionConfig<SettingsPdfTemplateListItem, String>(wfmStrings.modifiedDate(), SettingsPdfTemplateListItem.MODIFIED_DATE, 80) {
            @Override
            public String getCellValue(SettingsPdfTemplateListItem item) {
                return DateUtils.format1(item.getModifiedDate());
            }
        };
        columnConfig[5].setMinimumColumnWidth(40);

        columnConfig[6] = new ColumnDefinitionConfig<SettingsPdfTemplateListItem, String>(wfmStrings.createdDate(), SettingsPdfTemplateListItem.CREATION_DATE, 80) {
            @Override
            public String getCellValue(SettingsPdfTemplateListItem item) {
                return DateUtils.format1(item.getCreationDate());
            }
        };
        columnConfig[6].setMinimumColumnWidth(40);

        return columnConfig;
    }

    private ListingRequestProvider<SettingsPdfTemplateListItem> getListProvider() {
        return (listingFilterParameter, listingCallback) -> PdfTemplateService.App.get().getSettingsPdfTemplateList(listingFilterParameter, new AsyncCallback<ListResult<SettingsPdfTemplateListItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(ListResult<SettingsPdfTemplateListItem> result) {
                listingCallback.onSuccess(result);
            }
        });
    }

    private ListingPanelDesign getListDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {

                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return null;
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return null;
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(settingsStrings.noPdfTemplateText());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
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
