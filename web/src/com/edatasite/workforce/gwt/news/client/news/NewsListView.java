/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/24 7:55:45                                                                                            *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.news.client.news;

import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.news.client.rpc.NewsListItem;
import com.edatasite.workforce.gwt.news.client.rpc.NewsService;
import com.edatasite.workforce.gwt.news.client.rpc.NewsServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialDropDown;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 23, 2009
 * Time: 4:33:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class NewsListView extends BaseListView implements Constants {

    private ListingPanel<NewsListItem> list;
    private final NewsServiceAsync serviceAsync = NewsService.App.get();

    private final int searchType = 2;

    public NewsListView() {
        super(NEWS_LIST);
        setDescription(property.getPlural(wfmStrings.companyNews()));
        if (Utils.hasPermission(PermissionConstants.WORKSPACE_COMPANY_NEWS_ADD) || Utils.hasPermission(PermissionConstants.HRMS_COMPANY_NEWS_ADD) || Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(ADMIN_LOCATION) || Utils.hasRole(HR)) {
            setAddNew("news|add/add");
        }
    }

    private ColumnDefinitionConfig[] drawColumns() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[8];

        columns[0] = new ColumnDefinitionConfig<NewsListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final NewsListItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem newsSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-news-small");
                newsSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("news|summary/" + item.getObjectId(), item.getSubject()));
                actionItemCount++;
                menuBar.addItem(newsSummary);


                if (Utils.hasPermission(PermissionConstants.WORKSPACE_COMPANY_NEWS_EDIT) || Utils.hasPermission(PermissionConstants.HRMS_COMPANY_NEWS_EDIT) || Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(ADMIN_LOCATION) || Utils.hasRole(HR)) {
                    MenuPopItem newsEdit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
                    newsEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("news|edit/" + item.getObjectId(), item.getSubject()));
                    actionItemCount++;
                    menuBar.addItem(newsEdit);

                    MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    removeItem.setCommand(() -> {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                serviceAsync.deleteNews(item.getObjectId(), new AbstractAsyncCallback<Void>() {
                                    public void failure(Throwable caught) {
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    public void success(Void result) {
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_NEWS_DELETE, result, NewsListView.this);
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.news()), Info.Type.INFO);
                                        list.reloadPage();
                                    }
                                });
                            }
                        });
                        message.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(removeItem);
                }
                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);
        columns[1] = new ColumnDefinitionConfig<NewsListItem, SimpleLink>(wfmStrings.subject(), NewsListItem.SUBJECT, 180) {
            @Override
            public SimpleLink getCellValue(NewsListItem item) {
                SimpleLink label = new SimpleLink(item.getSubject());
                label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("news|summary/" + item.getObjectId(), item.getSubject()));
                return label;
            }
        };
        columns[1].setMinimumColumnWidth(150);

        columns[2] = new ColumnDefinitionConfig<NewsListItem, String>(wfmStrings.date(), NewsListItem.DATE, 100) {
            @Override
            public String getCellValue(NewsListItem item) {
                return DateUtils.format(item.getDate());
            }
        };
        columns[2].setMinimumColumnWidth(80);

        columns[3] = new ColumnDefinitionConfig<NewsListItem, String>(wfmStrings.author(), NewsListItem.POSTED_BY, 100) {
            @Override
            public String getCellValue(NewsListItem item) {
                return item.getPostedBy();
            }
        };
        columns[3].setMinimumColumnWidth(60);

        columns[4] = new ColumnDefinitionConfig<NewsListItem, String>(wfmStrings.category(), NewsListItem.CATEGORY, 100) {
            @Override
            public String getCellValue(NewsListItem item) {
                return item.getCategoryName() != null ? item.getCategoryName().replace("[", "").replace("]", "") : "";
            }
        };
        columns[4].setMinimumColumnWidth(80);
        columns[4].setColumnSortable(true);

        columns[5] = new ColumnDefinitionConfig<NewsListItem, String>(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), NewsListItem.LOCATION, 120) {
            @Override
            public String getCellValue(NewsListItem item) {
                return item.getLocationName();
            }
        };
        columns[5].setMinimumColumnWidth(100);
        columns[5].setColumnSortable(true);
        columns[5].setShow(false);

        columns[6] = new ColumnDefinitionConfig<NewsListItem, String>(wfmStrings.visibility(), NewsListItem.VISIBILITY, 55) {
            @Override
            public String getCellValue(NewsListItem item) {
                return item.isVisibility() ? wfmStrings.pub() : wfmStrings.internal();
            }
        };
        columns[6].setMinimumColumnWidth(35);
        columns[6].setShow(false);

        columns[7] = new ColumnDefinitionConfig<NewsListItem, String>(wfmStrings.comments(), NewsListItem.COMMENT, 80) {
            @Override
            public String getCellValue(NewsListItem item) {
                return item.getComments() != null ? item.getComments().toString() : "0";
            }
        };
        columns[7].setMinimumColumnWidth(40);
        columns[7].setShow(false);

        return columns;
    }

    protected Widget onInitialize() {
        list = new GuideListingPanel(ListPanelType.NewsWorkspaceListPanel, drawColumns(), getListingRequestProvider(), getListingPanelDesign());

        list.setPDFListener(clickEvent -> {
            String pdfURL;
            pdfURL = CommandConstants.PDF_URL + "/newsListPDFHanlder";
            list.callListPDF(pdfURL, list.getFilterParametrs());
            ListingFilterParameter fp = list.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());

        });
        list.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadNewsListExcelHandler";
            ListingFilterParameter fp = list.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            list.callListExcel(excelURL, fp);
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_NEWS_ADD, NewsListView.this, (sender, args) -> {
            list.reloadPage();
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_NEWS_COMMENTS_ADD, NewsListView.this, (sender, args) -> list.reloadPage());

        add(list);
        return null;
    }

    private GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                if (Utils.hasPermission(PermissionConstants.WORKSPACE_COMPANY_NEWS_ADD) || Utils.hasPermission(PermissionConstants.HRMS_COMPANY_NEWS_ADD) || Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(ADMIN_LOCATION) || Utils.hasRole(HR)) {
                    return () -> SinksContainerFactory.entryPoint.onHistoryChanged("news|add/add");
                }
                return null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public long initSimpleFilterType() {
                        ListingChooseFilter.wCategory = true;
                        return ListingChooseFilter.BY_NEWS_CATEGORIES;
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.WORKSPACE_COMPANY_NEWS_ADD) || Utils.hasPermission(PermissionConstants.HRMS_COMPANY_NEWS_ADD) || Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(ADMIN_LOCATION) || Utils.hasRole(HR)) {
                    ActionButton addNews = getAddNewButton();
                    addNews.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("news|add/add"));
                    return addNews;
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.currentlyNoNews());
                message.setTextBeforeLink(wfmStrings.addYourNewsByClicking());
                HorizontalPanel hp = new HorizontalPanel();
                if (Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(ADMIN_LOCATION) || Utils.hasRole(HR)) {
                    hp.setSpacing(5);
                    hp.add(new Label(wfmStrings.addYourNewsByClicking()));
                    SimpleLink link = new SimpleLink(wfmStrings.here());
                    link.addClickHandler(sender -> SinksContainerFactory.entryPoint.onHistoryChanged("news|add/add"));
                    hp.add(link);
                    message.setPanel(hp);
                    emptyDataTable.initEmptyDataTable(message);
                }
            }
        };
    }

    private ListingRequestProvider<NewsListItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> {

            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            filterParametrs.setSearchType(searchType);
            serviceAsync.getNewsList(filterParametrs, new AsyncCallback<ListResult<NewsListItem>>() {
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(ListResult<NewsListItem> newsListItemListResult) {
                    callback.onSuccess(newsListItemListResult);
                }
            });
        };

    }

    public String getIconStyle() {
        return "workspace news-detail";
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

    public String getPropertyCode() {
        return NEWS_LIST;
    }
}
