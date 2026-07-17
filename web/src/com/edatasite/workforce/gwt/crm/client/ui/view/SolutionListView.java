package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
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
import com.edatasite.workforce.gwt.core.client.ui.landing.HelpPanelGenerator;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.SolutionItem;
import com.edatasite.workforce.gwt.crm.client.ui.view.quickadd.CrmQuickAdd;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 18:11:32
 * To change this template use File | Settings | File Templates.
 */
public class SolutionListView extends BaseListView implements Constants {
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private ListingPanel<SolutionItem> listingTable;

    public SolutionListView() {
        super(SOLUTION_LIST, wfmStrings.solutions());
        setDescription(property.getPlural(wfmStrings.solutions()));
        if (Utils.hasPermission(PermissionConstants.ADD_NEW_SOLUTION)) {
            setAddNew(() -> new CrmQuickAdd(LayoutRPC.SOLUTION_FORM));
        }
    }

    protected Widget onInitialize() {
        listingTable = new GuideListingPanel(ListPanelType.SolutionListPanel, drawColumns(), getListingRequestProvider(), getListingPanelDesign());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SOLUTION_ADD_EDIT, SolutionListView.this, (sender, args) -> listingTable.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SOLUTION_DELETED, SolutionListView.this, (sender, args) -> listingTable.reloadPage());
        listingTable.setPDFListener(clickEvent -> {
            if (listingTable.getItemCount() > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String pdfURL;
            pdfURL = CommandConstants.PDF_URL + "/solutionListPDFHandler";
            ListingFilterParameter listingFilterParameter = listingTable.getFilterParametrs();
            listingFilterParameter.setPropertyCode(getPropertyCode());
            listingTable.callListPDF(pdfURL, listingFilterParameter);

        });
        listingTable.setExcelListener(clickEvent -> {
            if (listingTable.getItemCount() > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String excelURL;
            excelURL = CommandConstants.COMMON_URL + "/downloadSolutionsListViewExcel";
            ListingFilterParameter listingFilterParameter = listingTable.getFilterParametrs();
            listingFilterParameter.setPropertyCode(getPropertyCode());
            listingTable.callListExcel(excelURL, listingFilterParameter);

        });
        listingTable.reloadPage();
        add(listingTable);
        return null;
    }

    private ColumnDefinitionConfig[] drawColumns() {
        final ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();

        ColumnDefinitionConfig column = new ColumnDefinitionConfig<SolutionItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final SolutionItem solutionItem) {
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem solutionSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-view", () -> SinksContainerFactory.entryPoint.onHistoryChanged("solution|summary/" + solutionItem.getObjectId(), solutionItem.getTitle()));
                solutionSummary.ensureDebugId("solutionView");
                menuBar.addItem(solutionSummary);

                if (Utils.hasPermission(PermissionConstants.CRM_EDIT_SOLUTION)) {
                    MenuPopItem solutionEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit", () -> SinksContainerFactory.entryPoint.onHistoryChanged("solution|add/add/" + solutionItem.getObjectId(), solutionItem.getTitle()));
                    solutionEdit.ensureDebugId("editSolution");
                    menuBar.addItem(solutionEdit);
                }

                if (Utils.hasPermission(PermissionConstants.CRM_REMOVE_SOLUTION)) {
                    MenuPopItem removeSolution = new MenuPopItem(wfmStrings.delete(), "icon-remove", () -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                CRMService.App.get().deleteSolution(solutionItem.getObjectId(), new AbstractAsyncCallback() {
                                    @Override
                                    public void failure(Throwable caught) {
                                    }

                                    @Override
                                    public void success(Object result) {
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), crmStrings.solution()), Info.Type.INFO);
                                        listingTable.reloadPage();
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SOLUTION_DELETED, result, SolutionListView.this);
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    removeSolution.ensureDebugId("delete");
                    menuBar.addItem(removeSolution);
                }

                ToolItem toolItem = new ToolItem(3);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setColumnSortable(false);
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns.add(column);
        //Title
        column = new ColumnDefinitionConfig<SolutionItem, SimpleLink>(wfmStrings.title(), SolutionItem.TITLE, 100) {
            @Override
            public SimpleLink getCellValue(SolutionItem solutionItem) {
                return getLink(solutionItem.getTitle(), "solution|summary/" + solutionItem.getObjectId(), solutionItem.getTitle());
            }
        };
        columns.add(column);
        //Assignee
        column = new ColumnDefinitionConfig<SolutionItem, String>(wfmStrings.assignee(), SolutionItem.ASSIGNEE, 60) {
            @Override
            public String getCellValue(SolutionItem solutionItem) {
                return solutionItem.getAssignee();
            }
        };
        columns.add(column);
        //Status
        column = new ColumnDefinitionConfig<SolutionItem, String>(wfmStrings.status(), SolutionItem.STATUS, 50) {
            @Override
            public String getCellValue(SolutionItem solutionItem) {
                return solutionItem.getStatus();
            }
        };
        columns.add(column);
        //Question
        column = new ColumnDefinitionConfig<SolutionItem, String>(wfmStrings.question(), SolutionItem.QUESTION, 150) {
            @Override
            public String getCellValue(SolutionItem solutionItem) {
                return solutionItem.getQuestion();
            }
        };
        columns.add(column);
        //Answer
        column = new ColumnDefinitionConfig<SolutionItem, String>(wfmStrings.answer(), SolutionItem.ANSWER, 150) {
            @Override
            public String getCellValue(SolutionItem solutionItem) {
                return solutionItem.getAnswer();
            }
        };
        columns.add(column);

        return columns.toArray(new ColumnDefinitionConfig[columns.size()]);
    }

    private ListingRequestProvider<SolutionItem> getListingRequestProvider() {
        return (filterParametrs, solutionItemListingCallback) -> CRMService.App.get().getSolutionList(filterParametrs, new AbstractAsyncCallback<ListResult<SolutionItem>>() {

            @Override
            public void failure(Throwable caught) {
                solutionItemListingCallback.onFailure(caught);
            }

            @Override
            public void success(ListResult<SolutionItem> result) {
                solutionItemListingCallback.onSuccess(result);
            }
        });
    }

    private GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {

            @Override
            public Command getAddNewItemCommand() {
                return Utils.hasPermission(PermissionConstants.ADD_NEW_SOLUTION) ? (() -> new CrmQuickAdd(LayoutRPC.SOLUTION_FORM)) : null;
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
                if (Utils.hasPermission(PermissionConstants.ADD_NEW_SOLUTION)) {
                    ActionButton addNew = getAddNewButton();
                    addNew.ensureDebugId("addNew_button");
                    addNew.addClickHandler(clickEvent -> new CrmQuickAdd(LayoutRPC.SOLUTION_FORM));
                    return addNew;
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, Utils.hasPermission(PermissionConstants.CRM_SOLUTIONS_EXPORT));
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(crmStrings.messCurrentlySolutions());
                if (Utils.hasPermission(PermissionConstants.ADD_NEW_SOLUTION)) {
                    message.setHref(clickEvent -> new CrmQuickAdd(LayoutRPC.SOLUTION_FORM));
                    message.setTextBeforeLink(crmStrings.messStartAddingSolutionsClicking());
                }
                emptyDataTable.initEmptyDataTable(message);

            }
        };
    }

    @Override
    public String getPropertyCode() {
        return SOLUTION_LIST;
    }

    public FlowPanel getHelpContainer() {
        return HelpPanelGenerator.getHelpPanel(PermissionConstants.CRM_CONTEXT, PermissionConstants.CRM_SOLUTIONS_LIST);
    }

    public String getIconStyle() {
        return "crm solitions-list";
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
