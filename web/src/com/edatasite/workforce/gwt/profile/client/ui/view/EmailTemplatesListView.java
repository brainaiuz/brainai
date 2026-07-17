package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants;
import com.edatasite.workforce.gwt.profile.client.ui.view.workflow.WorkflowRuleListView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Admin
 * Date: 15.03.2010
 * Time: 13:28:01
 */

public class EmailTemplatesListView extends BaseListView implements Constants {
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private ListingPanel listPanel;


    public EmailTemplatesListView() {
        super("emailTemplateList", wfmStrings.emailTemplates());
    }

    @Override
    protected Widget onInitialize() {
        listPanel = new ListingPanel(ListPanelType.EmailTemplatesListPanel, getColumnConfig(), getListProvider(), getListDesign());


        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMAIL_TEMPLATE_ADD, EmailTemplatesListView.this, (sender, args) -> listPanel.reloadPage());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMAIL_TEMPLATE_EDIT, EmailTemplatesListView.this, (sender, args) -> listPanel.reloadPage());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMAIL_TEMPLATES_LIST_ADD, EmailTemplatesListView.this, (sender, args) -> listPanel.reloadPage());
        add(listPanel);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        ColumnDefinitionConfig[] columnConfig = new ColumnDefinitionConfig[7];

        columnConfig[0] = new ColumnDefinitionConfig<Object, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(Object object) {
                final EmailTemplateItem item = (EmailTemplateItem) object;

                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem templateSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                templateSummary.ensureDebugId("summary");
                templateSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("template|summary/" + item.getObjectId() + "/" + item.getCompanyEmailTemplate(), item.getName()));
                actionItemCount++;
                menuBar.addItem(templateSummary);

                MenuPopItem templateEdit = new MenuPopItem(EmailTemplateConstants.COMPANY_EMAIL_TEMPLATE.equals(item.getCompanyEmailTemplate()) ? wfmStrings.edit() : settingsStrings.createCopy(), "icon-edit");
                templateEdit.ensureDebugId("edit");
                templateEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("templateedit|addtemplate/" + item.getObjectId() + "/" + item.getCompanyEmailTemplate(), item.getName()));
                actionItemCount++;
                menuBar.addItem(templateEdit);
                if (!EmailTemplateConstants.DEFAULT_EMAIL_TEMPLATE.equals(item.getCompanyEmailTemplate())) {
                    MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                    removeItem.ensureDebugId("delete");
                    removeItem.setCommand(() -> {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        message.setTitle(wfmStrings.confirmationMessage());
                        message.setMessage(wfmMessages.sureYouWantToDelete(item.getName(), ""));
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                ProfileService.App.get().deleteEmailTemplate(item.getObjectId(), new AsyncCallback<Void>() {
                                    public void onFailure(Throwable caught) {
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    public void onSuccess(Void result) {
                                        Info.show(settingsStrings.emailTemplateDeleted(), Info.Type.INFO);
                                        listPanel.reloadPage();
                                    }
                                });
                            }

                        });
                        message.open();

                    });
                    menuBar.addItem(removeItem);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig[0].setColumnSortable(false);
        columnConfig[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);

        columnConfig[1] = new ColumnDefinitionConfig<Object, SimpleLink>(wfmStrings.template(), EmailTemplateItem.TEMPLATE_NAME, 150) {
            @Override
            public SimpleLink getCellValue(Object object) {
                EmailTemplateItem item = (EmailTemplateItem) object;
                return getLink(item.getName(), "template|summary/" + item.getObjectId() + "/" +
                        (item.getCompanyEmailTemplate())/* + "/" + "false"*/);//not changed false == not importing eml file
            }
        };
        columnConfig[1].setMinimumColumnWidth(200);

        columnConfig[2] = new ColumnDefinitionConfig<Object, String>(wfmStrings.subject(), EmailTemplateItem.TEMPLATE_SUBJECT, 150) {
            @Override
            public String getCellValue(Object object) {
                EmailTemplateItem item = (EmailTemplateItem) object;
                return item.getSubject();
            }
        };
        columnConfig[2].setMinimumColumnWidth(200);

        columnConfig[3] = new ColumnDefinitionConfig<Object, String>(wfmStrings.category(), EmailTemplateItem.TEMPLATE_CATEGORY, 150) {
            @Override
            public String getCellValue(Object object) {
                EmailTemplateItem item = (EmailTemplateItem) object;
                return item.getCategoryName();
            }
        };
        columnConfig[3].setMinimumColumnWidth(180);

        columnConfig[4] = new ColumnDefinitionConfig<Object, String>(wfmStrings.isDefault(), EmailTemplateItem.TEMPLATE_IS_DEFAULT, 50) {
            @Override
            public String getCellValue(Object object) {
                EmailTemplateItem item = (EmailTemplateItem) object;
                return item.isDefault() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        columnConfig[4].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfig[4].setMinimumColumnWidth(80);
        columnConfig[4].setMaximumColumnWidth(120);

        columnConfig[5] = new ColumnDefinitionConfig<Object, String>(wfmStrings.apps(), EmailTemplateItem.TEMPLATE_MODULE, 80) {
            @Override
            public String getCellValue(Object object) {
                EmailTemplateItem item = (EmailTemplateItem) object;
                return item.getModule() != null ? WorkflowRuleListView.localize(item.getModule()) : wfmStrings.notAvailable();
            }
        };
        columnConfig[5].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfig[5].setMinimumColumnWidth(30);
        columnConfig[5].setMaximumColumnWidth(80);

        columnConfig[6] = new ColumnDefinitionConfig<Object, String>(wfmStrings.priv(), EmailTemplateItem.TEMPLATE_ONLY_MINE, 50) {
            @Override
            public String getCellValue(Object object) {
                EmailTemplateItem item = (EmailTemplateItem) object;
                return item.isOnlyMine() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        columnConfig[6].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfig[6].setMinimumColumnWidth(30);
        columnConfig[6].setMaximumColumnWidth(80);

        return columnConfig;
    }

    private ListingRequestProvider<EmailTemplateItem> getListProvider() {
        return (listingFilterParameter, listingCallback) -> {
            listingFilterParameter.setParams("EmailTemplateListView");
            ProfileService.App.get().getEmailTemplateList(listingFilterParameter, new AsyncCallback<ListResult<EmailTemplateItem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(ListResult<EmailTemplateItem> emailTemplateItemListResult) {
                    listingCallback.onSuccess(emailTemplateItemListResult);
                }
            });
        };
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

                    @Override
                    public long initSimpleFilterType() {
                        ListingChooseFilter.wCategory = false;
                        return ListingChooseFilter.EMAIL_TEMPLATES;
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = getAddNewButton();
                addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("template|add/add"));
                return addNew;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.noEmailTemplateText());
                message.setHref("template|add/add");
                message.setTextBeforeLink(wfmStrings.noEmailTemplateLink());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }


    public String getIconStyle() {
        return "icon-tasks";
    }

    public ImageResource getIconImage() {
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