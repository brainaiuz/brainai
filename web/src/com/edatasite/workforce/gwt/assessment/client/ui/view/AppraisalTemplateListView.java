package com.edatasite.workforce.gwt.assessment.client.ui.view;

import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
import com.edatasite.workforce.gwt.assessment.client.rpc.TemplateListItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
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
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

public class AppraisalTemplateListView extends BaseListView implements Constants {
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private ListingPanel<TemplateListItem> list;

    public AppraisalTemplateListView() {
        super("templates");
        setDescription(property.getPlural(wfmStrings.templates()));
        if (Utils.hasPermission(PermissionConstants.HRMS_NEW_APPRAISALS_TEMPLATES)) {
            setAddNew("addTemplate/");
        }
    }

    @Override
    public String getIconStyle() {
        return "assessment app-template-list";
    }

    @Override
    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.AppraisalTemplateListPanel, getColumnsConfig(), getRequestProvider(), getDesign());

        list.setPDFListener(clickEvent -> {
            String pdfURL;
            pdfURL = CommandConstants.PDF_URL + "/appraisalTemplateListPDFHandler";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            list.callListPDF(pdfURL, filterParametrs);
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TEMPLATE_ADD_DELETE, AppraisalTemplateListView.this, (sender, args) -> list.reloadPage());

        add(list);
        list.addStyleName("file--AppraisalTemplateListVIew");
        return null;
    }

    private ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {

            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.HRMS_NEW_APPRAISALS_TEMPLATES)) {
                    ActionButton newAppraisalTemplateItem = getAddNewButton();
                    newAppraisalTemplateItem.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("addTemplate/"));
                    return newAppraisalTemplateItem;
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
                list.getXlsVersion().setVisible(false);
                list.addStyleName("file--AppraisalTemplateListVIew--2");
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                String labelText = hrmsStrings.notSavedTemplates() + hrmsStrings.addAppraisalTemplateByClicking();
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(labelText);
                if (Utils.hasPermission(PermissionConstants.HRMS_NEW_APPRAISALS_TEMPLATES)) {
                    message.setHref("addTemplate/");
                    emptyDataTable.initEmptyDataTable(message);
                }
            }
        };
    }

    private ListingRequestProvider<TemplateListItem> getRequestProvider() {
        return (filterParametrs, callback) -> AssessmentService.App.get().getTemplates(filterParametrs, new AsyncCallback<ListResult<TemplateListItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess(ListResult<TemplateListItem> templateList) {
                callback.onSuccess(templateList);
            }
        });
    }

    private ColumnDefinitionConfig[] getColumnsConfig() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[2];
        //action column
        columns[0] = new ColumnDefinitionConfig<TemplateListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final TemplateListItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                //edit appraisal template
                if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_TEMPLATED)) {
                    MenuPopItem editTemplate = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    editTemplate.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("addTemplate/" + item.getId(), item.getName()));
                    actionItemCount++;
                    menuBar.addItem(editTemplate);
                }
                //copy appraisal template
                if (Utils.hasPermission(PermissionConstants.HRMS_COPY_TEMPLATED)) {
                    MenuPopItem copyTemplate = new MenuPopItem(wfmStrings.copy(), "list-action-menu-icon icon-copy");
                    copyTemplate.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("addTemplate/copyTemplate/" + item.getId(), item.getName()));
                    actionItemCount++;
                    menuBar.addItem(copyTemplate);
                }
                //remove appraisal template
                if (Utils.hasPermission(PermissionConstants.HRMS_REMOVE_TEMPLATED)) {
                    MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    removeItem.setCommand(() -> {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        message.setTitle(hrmsStrings.deleteTemplate());
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                AssessmentService.App.get().deleteTemplate(item.getId(), new AbstractAsyncCallback<Void>() {
                                    @Override
                                    public void failure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    @Override
                                    public void success(Void result) {
                                        LoadingPanel.loading(false);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TEMPLATE_ADD_DELETE, result, AppraisalTemplateListView.this);
                                        Info.show(wfmStrings.messSuccessfulyyDeleted(), Info.Type.INFO);
                                        list.reloadPage();
                                        list.addStyleName("file--AppraisalTemplateListVIew--3");
                                    }
                                });
                            }
                        });
                        message.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(removeItem);
                }

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setColumnSortable(false);
        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        //template name
        columns[1] = new ColumnDefinitionConfig<TemplateListItem, SimpleLink>(wfmStrings.name(), TemplateListItem.NAME, 250) {
            @Override
            public SimpleLink getCellValue(TemplateListItem item) {
                return getLink(item.getName(), "addTemplate/" + item.getId(), item.getName());
            }
        };
        columns[1].setMinimumColumnWidth(180);

        return columns;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    public String getPropertyCode() {
        return "appraisalTemplate";
    }
}