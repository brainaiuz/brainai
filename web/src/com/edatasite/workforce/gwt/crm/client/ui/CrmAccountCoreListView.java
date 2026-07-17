package com.edatasite.workforce.gwt.crm.client.ui;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactServiceAsync;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ContextMenu;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.ImportFileActionLink;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportFilePopUp;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: hayot
 * Date: 4/27/11
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class CrmAccountCoreListView extends BaseListView implements Constants, PermissionConstants {
    protected static final ContactServiceAsync contactService = ContactService.App.get();

    protected ContextMenu actions;
    protected ContextMenu emptyActions;
    protected ListingPanel list;
    protected int totalCount = 0;
    protected HashSet<CrmAccountItem> selectedItems = new HashSet<>();
    protected HashSet<CrmAccountItem> lastSelectedItems = new HashSet<>();
    protected CrmAccountItem defaultOne;
    protected boolean detectDuplicates = false;
    protected CrmAccountItem currentItem;
    //protected ActionButton deleteBtn;

    private final ImportFilePopUp imp = new ImportFilePopUp(getImportType(), null);

    public CrmAccountCoreListView() {
        super(CRM_ACCOUNT_LIST);
        setDescription(property.getSingular(wfmStrings.companies()));
    }

    public CrmAccountCoreListView(String name) {
        super(name);
    }

    public CrmAccountCoreListView(String name, String description) {
        super(name, description);
    }

    protected Widget onInitialize() {

        WfmUiEventsBus.addWfmUiListener(CrmAccountCoreListView.this, (sender, args) -> list.reloadPage(), WfmUiEventType.ON_ACCOUNTS_ADD_EDIT, WfmUiEventType.ON_CRM_ACCOUNT_DELETED, WfmUiEventType.ON_CLIENT_ADD, WfmUiEventType.ON_CLIENT_DELETED, WfmUiEventType.ON_SUPPLIER_ADD, WfmUiEventType.ON_SUPPLIER_EDIT, WfmUiEventType.ON_SUPPLIER_DELETED, WfmUiEventType.ON_CONTACT_AUTO_ACCOUNT_ADDED, WfmUiEventType.ON_OPPORTUNITY_AUTO_ACCOUNT_ADDED);
        list = initializeList();
        list.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> {
            saveAccountsCellValue((CrmAccountItem) rowValue, columnCodeName);
        });
        list.addSelectionRowHandler(selectedRows -> {
            if (!selectedRows.isEmpty()) {
                selectedItems = selectedRows;
            }
//                if (deleteBtn != null) {
////                    deleteBtn.setVisible(true);
//                }
//            } else {
//                if (deleteBtn != null) {
//                    deleteBtn.setVisible(false);
//                }
//            }
        });
        list.setOnReset(() -> {
            detectDuplicates = false;
            lastSelectedItems.clear();
        });
        list.setPDFListener(clickEvent -> {
            if (list.getItemCount() > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String pdfURL = CommandConstants.PDF_URL + "/" + getPDFExporterLink();
            ListingFilterParameter listingFilterParameter = list.getFilterParametrs();
            listingFilterParameter.setPropertyCode(getPropertyCode());
            if (listingFilterParameter == null) {
                listingFilterParameter = new ListingFilterParameter();
            }
            list.callListPDF(pdfURL, listingFilterParameter);
        });
        list.setExcelListener(clickEvent -> {
            if (list.getItemCount() > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String excelURL = CommandConstants.COMMON_URL + "/" + getExcelExporterLink();
            ListingFilterParameter listingFilterParameter = list.getFilterParametrs();
            listingFilterParameter.setPropertyCode(getPropertyCode());
            if (listingFilterParameter == null) {
                listingFilterParameter = new ListingFilterParameter();
            }
            list.callListPDF(excelURL, listingFilterParameter);
        });
        add(list);
        return null;
    }

    protected abstract ListingPanel initializeList();

    protected abstract ListPanelType getListPanelType();

    protected SelectionGrid.SelectionPolicy getSelectionPolicy() {
        return SelectionGrid.SelectionPolicy.CHECKBOX;
    }

    protected abstract CustomColumnDefinitionConfig[] getColumns();

    protected abstract <T extends CrmAccountItem> ListingRequestProvider<T> getRequestProvider();

    protected GuideListingPanelDesign getPanelDesigner() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return getAddNewAction();
            }

            @Override
            public Command getUploadButtonCommand() {
                return hasImportButton() ? imp::open : null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return getFacetFilterCallbackProvider();
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return getContentConfigure();
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                return initializeTopMenuNew();
            }

            @Override
            public ActionButton initTopToolBarMore() {
                if (!Utils.isCRM()) {
                    return getRemoveMoreButton(clickEvent -> deleteSelection());
                } else {
                    if (Utils.hasPermission(PermissionConstants.CRM_ACCOUNTS_CONVERT, PermissionConstants.CRM_ACCOUNTS_MERGE, PermissionConstants.CRM_ACCOUNTS_DETECT_DUBLICATES, PermissionConstants.CRM_ACCOUNTS_DELETE)) {
                        final ActionButton action = new ActionButton(ActionButton.getMoreString(), WfmButton2.BTN_DEFAULT, ActionButton.Type.TOOLMENU);
                        action.ensureDebugId("crm_companies_more_id");
                        action.addDomHandler(event -> {
                            MenuBar menu = getActionsForSelections();
                            menu.setAutoOpen(true);
                            action.setMenu(menu);
                            menu.setLayoutData(action);
                        }, MouseOverEvent.getType());
                        return action;
                    }
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                imp.setSubmitCompleted(() -> {
                    if (imp.getObjectId() != null) {
                        goTo("" + getImporterLink() + imp.getObjectId());
                    }
                });
                if (hasImportButton()) {
                    ImportFileActionLink link = new ImportFileActionLink();
                    link.addClickHandler(ch -> imp.open());
                    menuContainer.add(link);
                }
                exportOption.initExport(null, isShowExport());
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                emptyDataTable.initEmptyDataTable(new DefaultNoItemsMessage() {
                    @Override
                    public VerticalPanel getWholeMessage() {
                        return getEmptyDataTable();
                    }
                });
            }

            @Override
            public boolean isShowResetButton() {
                return true;
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return (Utils.adminOrDirector() || Utils.hasRole(Constants.SALESMAN) || Utils.hasRole(Constants.SALESPERSON) || hasPermissionToEdit());
            }

            @Override
            public boolean isShowCustomiseButton() {
                return Utils.hasPermission(PermissionConstants.CRM_ACCOUNTS_CUSTOMIZE_LIST);
            }
        };
    }

    protected abstract ActionButton initializeTopMenuNew();

    protected abstract boolean hasImportButton();

    protected abstract String getImporterLink();

    protected abstract void saveAccountsCellValue(CrmAccountItem rowValue, String columnCodeName);

    protected abstract ImportTypeEnum getImportType();

    protected abstract String getPDFExporterLink();

    protected abstract String getExcelExporterLink();

    protected abstract FacetContentConfigure getContentConfigure();

    protected abstract FacetCallbackProvider getFacetFilterCallbackProvider();

    protected boolean isShowExport() {
        return false;
    }

    protected boolean hasPermissionToEdit() {
        return false;
    }

    protected Command getAddNewAction() {
        return null;
    }

    protected MenuBar getActionsForSelections() {
        if (!(list.getPagingScrollTable().getSelectedRowValues() == null || list.getPagingScrollTable().getSelectedRowValues().size() < 1)) {
            if (actions == null) {
                actions = new ContextMenu();
                actions.getMenuBar().setAutoOpen(false);
                addActionsToActionButton();
                if (Utils.hasPermission(PermissionConstants.CRM_ACCOUNTS_DELETE)) {
                    actions.addMenuItem(wfmStrings.delete(), true, () -> {
                        actions.hide();
                        deleteSelection();
                    });
                }
            }
            return actions.getMenuBar();
        } else {
            if (emptyActions == null) {
                emptyActions = new ContextMenu();
                emptyActions.getMenuBar().setAutoOpen(false);
                emptyActions.addMenuItem(wfmStrings.selectAnyItemToActivateBatchActions(), null, true, null);
            }
            return emptyActions.getMenuBar();
        }
    }

    protected void addActionsToActionButton() {
    }

    protected abstract void deleteSelection();

    protected String refactor(String s) {
        return s == null ? "N/A" : s;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    protected abstract VerticalPanel getEmptyDataTable();

    @Override
    public String getPropertyCode() {
        return CRM_ACCOUNT_LIST;
    }
}
