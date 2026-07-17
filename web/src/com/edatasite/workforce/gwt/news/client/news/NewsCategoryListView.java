package com.edatasite.workforce.gwt.news.client.news;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
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
import com.edatasite.workforce.gwt.news.client.rpc.NewsCategory;
import com.edatasite.workforce.gwt.news.client.rpc.NewsService;
import com.edatasite.workforce.gwt.news.client.rpc.NewsServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Nov 12, 2010
 * Time: 7:27:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewsCategoryListView extends BaseListView implements Constants {
    private ListingPanel<NewsCategory> list;
    private final NewsServiceAsync serviceAsync = NewsService.App.get();

    public NewsCategoryListView() {
        super(NEWS_CATEGORY_LIST, wfmStrings.newsCategories());
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.NewsWorkspaceCategory, getColumns(), getListingRequestProvider(), getListingPanelDesign(), SelectionGrid.SelectionPolicy.CHECKBOX);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_NEWS_CATEGORY_SAVED, NewsCategoryListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    private ListingRequestProvider<NewsCategory> getListingRequestProvider() {
        return (filterParametrs, callback) -> serviceAsync.getNewsCategoriesByFilter(filterParametrs, new AbstractAsyncCallback<ListResult<NewsCategory>>() {
            @Override
            public void failure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void success(ListResult<NewsCategory> newsCategoryListResult) {
                callback.onSuccess(newsCategoryListResult);
            }

        });
    }

    private ListingPanelDesign getListingPanelDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasRole(PM) || Utils.hasRole(DR) || Utils.hasRole(ADMIN)) {
                    ActionButton news = getAddNewButton();
                    news.addClickHandler(event -> new EditNewsCategoryView());
                    return news;
                }
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.currentlyThereAreNoNewsCategories());
                if ((Utils.hasRole(PM) || Utils.hasRole(DR) || Utils.hasRole(ADMIN))) {
                    message.setTextBeforeLink(wfmStrings.youCanStartAddingNewsCategories());
                    message.setHref(clickEvent -> new EditNewsCategoryView());
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };


    }

    private ColumnDefinitionConfig[] getColumns() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[2];

        columns[0] = new ColumnDefinitionConfig<NewsCategory, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {


            @Override
            public Anchor getCellValue(final NewsCategory item) {

                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem categoryEdit = new MenuPopItem(wfmStrings.edit());
                categoryEdit.getElement().setId("news_category_edit_id");
                categoryEdit.setCommand(() -> new EditNewsCategoryView(item.getId()));
                actionItemCount++;
                menuBar.addItem(categoryEdit);

                if (Utils.hasRole(ADMIN)) {
                    MenuPopItem deleteCategory = new MenuPopItem(wfmStrings.delete());
                    deleteCategory.getElement().setId("news_category_delete_id");
                    deleteCategory.setCommand(() -> {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                serviceAsync.deleteNewsCategory(item.getId(), new AbstractAsyncCallback<Boolean>() {
                                    public void failure(Throwable throwable) {
                                        Info.show(wfmStrings.errorDelete(), Info.Type.WARNING);
                                    }

                                    public void success(Boolean deleted) {
                                        if (deleted) {
                                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.category()), Info.Type.INFO);
                                            list.reloadPage();
                                        } else {
                                            Info.show(wfmStrings.categoryCouldNotBeDeleted(), Info.Type.INFO);
                                        }
                                    }
                                });
                            }
                        });
                        message.open();
                    });
                    menuBar.addItem(deleteCategory);

                }

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);

        columns[1] = new ColumnDefinitionConfig<NewsCategory, String>(wfmStrings.name(), "name", 300) {

            @Override
            public String getCellValue(NewsCategory newsCategory) {
                return newsCategory.getName();
            }
        };
        columns[1].setMinimumColumnWidth(200);

        return columns;
    }


    @Override
    public String getIconStyle() {
        return "workspace news-category";  //To change body of implemented methods use File | Settings | File Templates.
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
