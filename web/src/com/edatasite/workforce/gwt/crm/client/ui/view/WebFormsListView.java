package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMServiceAsync;
import com.edatasite.workforce.gwt.webforms.client.WebFormConstants;
import com.edatasite.workforce.gwt.webforms.client.forms.WebForm;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Jul 29, 2010
 * Time: 2:35:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class WebFormsListView extends BaseListView implements Constants, WebFormConstants {
    private ListingPanel<WebForm> list;
    private ListingFilterParameter filterParametr;
    private final CRMServiceAsync crmService = CRMService.App.get();
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private SelectItem[] formTypes;
    private MenuBar menuBar;
    private ActionButton addMenu;
    private Boolean isButtonNotNull = false;

    public WebFormsListView() {
        super("webFormsList", crmStrings.crmForms());
        if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_WEB_FORM)) {
            setAddNew("webform|add/add/" + LEAD_FORM);
        }
    }

    public void refresh() {
        list.reloadPage();
    }

    private GuideListingPanelDesign getDisagn() {
        return new GuideListingPanelDesign() {

            @Override
            public Command getAddNewItemCommand() {
                return Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_WEB_FORM) ? () -> SinksContainerFactory.entryPoint.onHistoryChanged("webform|add/add/" + LEAD_FORM) : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_WEB_FORM)) {
                    if (!isButtonNotNull) {
                        addNewButton();
                    }
                    return addMenu;
                }
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(crmStrings.messCurrentlyWebForms());
                if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_WEB_FORM)) {
                    message.setHref("webform|add/add/" + LEAD_FORM);
                    message.setTextBeforeLink(crmStrings.messYouCrmFormsClicking());
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private void addNewButton() {
        isButtonNotNull = true;
        addMenu = getAddNewButton(ActionButton.Type.TOOLMENU);
        menuBar = new MenuBar(true);
        menuBar.setAutoOpen(true);
        menuBar.addCloseHandler(popupPanelCloseEvent -> {
            if (!initAddNewItemsOnce) {
                initAddNewButtonItems();
            }
        });
        addMenu.setMenu(menuBar);
    }

    boolean initAddNewItemsOnce = false;

    private void initAddNewButtonItems() {
        if (formTypes != null && formTypes.length > 0 && !initAddNewItemsOnce) {
            initAddNewItemsOnce = true;
            for (final SelectItem item : formTypes) {
                MenuPopItem addType = new MenuPopItem(item.getName());
                addType.ensureDebugId(item.getName());
                addType.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("webform|add/add/" + item.getCode()));
                if (menuBar == null) {
                    addNewButton();
                }
                menuBar.addItem(addType);
            }
        }
    }

    protected Widget onInitialize() {
        try {
            list = new GuideListingPanel(getPanelType(), getColumnConfigs(), getListData(), getDisagn(), getListType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        getFormType();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_WEB_FORM_ADD_EDIT, WebFormsListView.this, (sender, args) -> refresh());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_WEB_FORM_DELETE, WebFormsListView.this, (sender, args) -> refresh());
        add(list);
        return null;
    }

    private SelectionGrid.SelectionPolicy getListType() {
        /*if (Utils.isMediaCom()) {
            return SelectionGrid.SelectionPolicy.CHECKBOX;
        }*/
        return SelectionGrid.SelectionPolicy.ONE_ROW;
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[5];
        int index = -1;

        columns[++index] = new ColumnDefinitionConfig<WebForm, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(WebForm webForm) {
                final WebForm item = webForm;

                /*
                * Cel Menu  Bar fields
                * */
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                /*
                * Summery Menu Item
                * */
                MenuPopItem summeryMenuPopItem = new MenuPopItem(wfmStrings.summaryView(), "icon-leads");
                summeryMenuPopItem.ensureDebugId("summeryMenuPopItem");
                summeryMenuPopItem.setCommand(() -> {
                    if (item != null) {
                        String webFormId = item.getWebFormSource() != null ? item.getWebFormSource().getObjectID().toString() : "";
                        String type = item.getWebFormType() != null ? item.getWebFormType().getCode() : "";
                        SinksContainerFactory.entryPoint.onHistoryChanged("webform|summary/" + item.getObjectId() + "/" + webFormId + "/" + type, item.getTitle());
                    }
                });
                actionItemCount++;
                menuBar.addItem(summeryMenuPopItem);
                /*
                * Preview Menu Item
                * */
                MenuPopItem previewMenuPopItem = new MenuPopItem(wfmStrings.preview(), "icon-fulldetails");
                previewMenuPopItem.ensureDebugId("webFormPreview");
                previewMenuPopItem.setCommand(() -> {
                    if (item != null) {
                        Window.open(Utils.getHostURL() + "WebForms.html?link=" + item.getiFrameUrl(), crmStrings.crmForm(), null);
                    }
                });
                actionItemCount++;
                menuBar.addItem(previewMenuPopItem);
                /*
                * Edit Menu Item
                * */
                if (Utils.hasPermission(PermissionConstants.CRM_EDIT_WEB_FORM)) {
                    MenuPopItem editMenuPopItem = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    editMenuPopItem.ensureDebugId("editCrmForm");
                    editMenuPopItem.setCommand(() -> {
                        if (item != null) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("webformedit|editwebform/" + item.getObjectId(), item.getTitle());
                        }
                    });
                    actionItemCount++;
                    menuBar.addItem(editMenuPopItem);
                }

                /*
                * This conditions for if user role is "Admin",the simple: "lochin.shodiyev@gmail.com"
                * */
//                if (Utils.hasRole(ADMIN)) {
                if (Utils.hasPermission(PermissionConstants.CRM_REMOVE_WEB_FORM)) {

                    /*
                    * Delete Menu Item
                    * */
                    MenuPopItem deleteMenuPopItem = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                    deleteMenuPopItem.ensureDebugId("delete");
                    deleteMenuPopItem.setCommand(() -> {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                crmService.deleteWebForm(item.getObjectId(), new AbstractAsyncCallback<Void>() {
                                    public void failure(Throwable caught) {
                                        Info.show(wfmStrings.error(), Info.Type.WARNING);
                                    }

                                    public void success(Void result) {
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), crmStrings.crmForm()), Info.Type.INFO);
                                        refresh();
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WEB_FORM_DELETE, result, WebFormsListView.this);
                                    }
                                });
                            }
                        });
                        message.open();
                    });
                    menuBar.addItem(deleteMenuPopItem);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);

                return toolItem.getAction();
            }
        };
        columns[index].setColumnSortable(false);
        columns[index].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[index].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        ///////////////////////----------------------------(1)----------------------------------------------------------
        columns[++index] = new ColumnDefinitionConfig<WebForm, SimpleLink>(crmStrings.webFormType(), WebForm.TYPE, 70) {
            public String getColumnStyle(Object object) {
                return null;
            }

            @Override
            public SimpleLink getCellValue(WebForm webForm) {
                String webFormId = webForm.getWebFormSource() != null ? webForm.getWebFormSource().getObjectID().toString() : "";
                String type = webForm.getWebFormType() != null ? webForm.getWebFormType().getCode() : "";
                return getLink(webForm.getWebFormTypeName(), "webform|summary/" + webForm.getObjectId() + "/" + webFormId + "/" + type, webForm.getTitle(), webForm.getWebFormTypeName());
            }
        };
        columns[index].setMinimumColumnWidth(40);
        ///////////////////-------------------------------(2)-----------------------------------------------------------
        columns[++index] = new ColumnDefinitionConfig<WebForm, String>(wfmStrings.title(), WebForm.TITLE, 100) {

            @Override
            public String getCellValue(WebForm webForm) {
                return webForm.getTitle();
            }
        };
        columns[index].setMinimumColumnWidth(40);
        ////////////////////////////////-----------------------(3)------------------------------------------------------
        columns[++index] = new ColumnDefinitionConfig<WebForm, SimpleLink>(crmStrings.webFormURL(), WebForm.URL, 105) {

            @Override
            public SimpleLink getCellValue(final WebForm webForm) {
                SimpleLink links = getLink(webForm.getiFrameUrl(), "");
                links.addClickHandler(event -> Window.open(Utils.getHostURL() + "WebForms.html?link=" + webForm.getiFrameUrl(), crmStrings.crmForm(), null));
                return links;
            }
        };
        columns[index].setColumnSortable(false);
        columns[index].setMinimumColumnWidth(40);
        /////////////////////////---------------------------(4)---------------------------------------------------------
        columns[++index] = new ColumnDefinitionConfig<WebForm, Integer>(crmStrings.numberOfFields(), WebForm.NUMBER_OF_FIELDS, 100) {

            @Override
            public Integer getCellValue(WebForm webForm) {
                return webForm.getWebFieldsCount();
            }
        };
        columns[index].setColumnSortable(false);
        columns[index].setMinimumColumnWidth(40);

        return columns;
    }

    private ListingRequestProvider getListData() {
        return (ListingRequestProvider<WebForm>) (filterParametrs, callback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            filterParametr = filterParametrs;
            crmService.getWebForms(filterParametr, new AbstractAsyncCallback<ListResult<WebForm>>() {
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<WebForm> list) {
                    callback.onSuccess(list);
                }
            });
        };

    }

    public void getFormType() {
        crmService.editWebForm(null, null, new AsyncCallback<WebForm>() {
            @Override
            public void onFailure(Throwable caught) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onSuccess(WebForm result) {
                formTypes = result.getFormTypes();
                initAddNewButtonItems();
            }
        });

    }

    public String getIconStyle() {
        return "lead lead-list";
    }

    protected ListPanelType getPanelType() {
        return ListPanelType.WebFormListPanel;
    }

    /*
    public AbstractImagePrototype getIconImage() {
        return CrmSalesBundles.App.get().leads();
    }
    */
    public FlowPanel getHelpContainer() {
        FlowPanel wc = new FlowPanel();
        final TextBox companyIdText = new TextBox();
        com.google.gwt.user.client.ui.Button bt = new com.google.gwt.user.client.ui.Button(wfmStrings.updateWebForms());
        if (Utils.isLocalhostOrLochin("anvar.akramov@edatasite.com")) {
            wc.add(companyIdText);
            wc.add(bt);
        }
        bt.addClickHandler(clickEvent -> {
            Integer companyId = null;
            if (!"".equals(companyIdText.getText())) {
                companyId = Integer.valueOf(companyIdText.getText());
            }
            LoadingPanel.loading(true);
            crmService.updateCompanyWebForms(companyId, new AbstractAsyncCallback() {

                @Override
                public void failure(Throwable throwable) {
                    Info.show(wfmStrings.error(), Info.Type.WARNING);
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(Object o) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.updated(), Info.Type.WARNING);
                }
            });
        });
        return wc;
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

    @Override
    public String getPropertyCode() {
        return "webFormsList";
    }
}
