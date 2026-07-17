package com.edatasite.workforce.gwt.assessment.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
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
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

/**
 * User: Ilhombek
 * Date: 7/11/11
 * Time: 7:11 PM
 */
public class CompetencyListView extends BaseListView implements Constants {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private ListingPanel<SkillItem> list;

    public CompetencyListView() {
        super("competencesView");
        setDescription(Property.getPluralWithObjectCode("competencesView", wfmStrings.competencies()));
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_COMPETENCES)) {
            setAddNew("addSkill/");
        }
    }

    @Override
    public String getIconStyle() {
        return "assessment competency-list";
    }

    @Override
    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.CompetencyListPanel, getColumnsConfig(), getRequestProvider(), getDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ADD_COMPETENCY, CompetencyListView.this, (sender, args) -> list.reloadPage());

        if (Utils.hasPermission(PermissionConstants.COMPETENCES_LIST_PDF_EXCEL_EXPORT)) {
            list.setPDFListener(clickEvent -> {
                String pdfURL = CommandConstants.PDF_URL + "/competenceListPDFHandler";
                ListingFilterParameter fp = list.getFilterParametrs();
                fp.setPropertyCode(getPropertyCode());
                list.callListPDF(pdfURL, fp);
            });
            list.setExcelListener(clickEvent -> {
                String excelURL = CommandConstants.COMMON_URL + "/downloadCompetenceListExcel";
                ListingFilterParameter fp = list.getFilterParametrs();
                fp.setPropertyCode(getPropertyCode());
                list.callListExcel(excelURL, fp);
            });
        }

        add(list);
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
                if (Utils.hasPermission(PermissionConstants.HRMS_ADD_COMPETENCES)) {
                    ActionButton newSkillItem = getAddNewButton();
                    newSkillItem.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("addSkill/"));
                    return newSkillItem;
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                String labelText = hrmsStrings.noCompetency() + " "+hrmsStrings.pleaseAddCompetency();
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(labelText);
                if (Utils.hasPermission(PermissionConstants.HRMS_ADD_COMPETENCES)) {
                    message.setHref("addSkill/");
                    emptyDataTable.initEmptyDataTable(message);
                }
            }
        };
    }

    private ListingRequestProvider<SkillItem> getRequestProvider() {
        return (filterParametrs, callback) -> AssessmentService.App.get().getCompetencies(filterParametrs, new AsyncCallback<ListResult<SkillItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess(ListResult<SkillItem> list) {
                callback.onSuccess(list);
            }
        });
    }

    private ColumnDefinitionConfig[] getColumnsConfig() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[4];

        columns[0] = new ColumnDefinitionConfig<SkillItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final SkillItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                removeItem.setCommand(() -> {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(wfmStrings.sureYouWantToDelete());
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            AssessmentService.App.get().deleteCompetency(item.getId(), new AbstractAsyncCallback<Void>() {

                                @Override
                                public void failure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void success(Void result) {
                                    LoadingPanel.loading(false);
                                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.competency()), Info.Type.INFO);
                                    list.reloadPage();
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_COMPETENCY_DELETE, result, CompetencyListView.this);
                                }
                            });
                        }

                    });
                    message.open();
                });

                if (Utils.hasPermission(PermissionConstants.HRMS_REMOVE_COMPETENCES)) {
                    actionItemCount++;
                    menuBar.addItem(removeItem);
                }

                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("editskill//" + item.getId(), item.getGroupName(), item.getName()));
                actionItemCount++;
                menuBar.addItem(edit);

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setColumnSortable(false);
        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);

        columns[1] = new ColumnDefinitionConfig<SkillItem, SimpleLink>(hrmsStrings.skillGroup(), SkillItem.COMPETENCY_GROUP_NAME, 150) {
            @Override
            public SimpleLink getCellValue(SkillItem item) {
                return getLink(item.getGroupName(), "editskill//" + item.getId(), item.getGroupName(), item.getName());
            }
        };
        columns[1].setMinimumColumnWidth(110);

        columns[2] = new ColumnDefinitionConfig<SkillItem, String>(hrmsStrings.competencyName(), SkillItem.COMPETENCY_NAME, 150) {
            @Override
            public String getCellValue(SkillItem item) {
                return item.getName();
            }
        };
        columns[2].setMinimumColumnWidth(110);

        columns[3] = new ColumnDefinitionConfig<SkillItem, String>(wfmStrings.description(), SkillItem.COMPETENCY_DESCRIPTION, 300) {
            @Override
            public String getCellValue(SkillItem item) {
                return item.getDescription();
            }
        };
        columns[3].setMinimumColumnWidth(230);

        return columns;
    }

    @Override
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
        return "competencesView";
    }
}
