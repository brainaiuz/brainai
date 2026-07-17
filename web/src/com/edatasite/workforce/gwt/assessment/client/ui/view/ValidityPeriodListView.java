package com.edatasite.workforce.gwt.assessment.client.ui.view;

import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.ValidityPeriodItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
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
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

public class ValidityPeriodListView extends BaseListView implements Constants {

    private static final DateTimeFormat format = DateTimeFormat.getFormat(Utils.getShortDateFormat()); //"MM/dd/yyyy"

    private ListingPanel<ValidityPeriodItem> list;

    public ValidityPeriodListView() {
        super(VALIDITY_PERIOD_LIST, wfmStrings.validityPeriods());
    }

    @Override
    public String getIconStyle() {
        return "assessment assessment-archive";
    }

    @Override
    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.ValidityPeriodListPanel, getColumnsConfig(), getRequestProvider(), getDesign());
        add(list);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_VALIDITY_PERIOD_CHANGED, ValidityPeriodListView.this, (sender, args) -> list.reloadPage());
        return null;
    }

    private Anchor getActionMenuItems(final ValidityPeriodItem item) {
        int actionItemCount = 0;
        MenuBar menuBar = new MenuBar(true);
        //edit validity period
        MenuPopItem editItem = new MenuPopItem(wfmStrings.edit(), "icon-edit");
        editItem.getElement().setId("Validity_period_edit_id");
        editItem.setCommand(() -> showValidityPeriodBox(item));
        //remove validity period
        MenuPopItem deleteItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
        deleteItem.getElement().setId("Validity_period_delete_id");
        deleteItem.setCommand(() -> {
            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
            messageBox.setTitle(wfmStrings.confirmation());
            messageBox.setMessage(wfmMessages.sureYouWantToDelete(item.getName(), "?"));
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    LoadingPanel.loading(true);
                    AssessmentService.App.get().deletedValidityPeriodItem(item, new AbstractAsyncCallback<Void>() {
                        @Override
                        public void failure(Throwable throwable) {
                            LoadingPanel.loading(false);
                            try {
                                throw throwable;
                            } catch (InsufficientPermissionsException e) {
                                Info.show(wfmStrings.youCannotDeleteThisItem(), Info.Type.WARNING);
                            } catch (Throwable e) {
                                // last resort  a very unexpected exception
                            }
                        }

                        @Override
                        public void success(Void result) {
                            list.reloadPage();
                            LoadingPanel.loading(false);
                        }
                    });
                }
            });
            messageBox.open();
        });

        actionItemCount++;
        menuBar.addItem(editItem);
        actionItemCount++;
        menuBar.addItem(deleteItem);

        ToolItem toolItem = new ToolItem(actionItemCount);
        toolItem.setWidget(menuBar);
        if (actionItemCount == 0) {
            return null;
        }
        return toolItem.getAction();
    }

    private ColumnDefinitionConfig[] getColumnsConfig() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[5];
        int columnSize = 0;
        //validity period action
        columns[columnSize] = new ColumnDefinitionConfig<ValidityPeriodItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final ValidityPeriodItem item) {
                return getActionMenuItems(item);
            }
        };
        columns[columnSize].setColumnSortable(false);
        columns[columnSize].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[columnSize++].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        //validity period name
        columns[columnSize] = new ColumnDefinitionConfig<ValidityPeriodItem, String>(wfmStrings.name(), ValidityPeriodItem.NAME, 130) {
            @Override
            public String getCellValue(ValidityPeriodItem item) {
                return item.getName();
            }
        };
        columns[columnSize++].setMinimumColumnWidth(100);
        //validity period description
        columns[columnSize] = new ColumnDefinitionConfig<ValidityPeriodItem, String>(wfmStrings.description(), ValidityPeriodItem.DESCRIPTION, 100) {
            @Override
            public String getCellValue(ValidityPeriodItem item) {
                return item.getDescription();
            }
        };
        columns[columnSize++].setMinimumColumnWidth(80);
        //validity period
        columns[columnSize] = new ColumnDefinitionConfig<ValidityPeriodItem, String>(wfmStrings.period(), ValidityPeriodItem.PERIOD, 100) {
            @Override
            public String getCellValue(ValidityPeriodItem item) {
                if (item.getFromDate() != null) {
                    return format.format(item.getFromDate()) + " - " + format.format(item.getToDate());
                }
                return "";
            }
        };
        columns[columnSize++].setMinimumColumnWidth(80);
        //validity period type
        columns[columnSize] = new ColumnDefinitionConfig<ValidityPeriodItem, String>(wfmStrings.periodType(), ValidityPeriodItem.PERIOD_TYPE, 70) {
            @Override
            public String getCellValue(ValidityPeriodItem item) {
                StringBuilder str = new StringBuilder();
                int i = 0;
                for (SelectItem periodTypeItem : item.getPeriodTypeItems()) {
                    if (i > 0) {
                        str.append(", ");
                    }
                    str.append(periodTypeItem.getName());
                    i++;
                }
                return str.toString();
            }
        };
        columns[columnSize++].setMinimumColumnWidth(50);
        return columns;
    }

    private ListingPanelDesign getDesign() {

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
                ActionButton actionButton = getAddNewButton();
                actionButton.addClickHandler(event -> showValidityPeriodBox(null));
                return actionButton;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
            }

        };
    }

    private ListingRequestProvider<ValidityPeriodItem> getRequestProvider() {
        return (filterParameters, callback) -> AssessmentService.App.get().getValidityPeriodList(filterParameters, new AbstractAsyncCallback<ListResult<ValidityPeriodItem>>() {
            @Override
            public void failure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void success(ListResult<ValidityPeriodItem> result) {
                callback.onSuccess(result);
            }
        });
    }

    private void showValidityPeriodBox(ValidityPeriodItem item) {
        new ValidityPeriodsPopup(item);
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
}