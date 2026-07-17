package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.MailMessageItem;
import com.edatasite.workforce.gwt.crm.client.rpc.MassMailService;
import com.edatasite.workforce.gwt.crm.client.rpc.MessageTrackListItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 25-Jan-2010
 * Time: 18:11:32
 * To change this template use File | Settings | File Templates.
 */
public class MessageTrackListView extends BaseListView implements Constants {
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private ListingPanel<MessageTrackListItem> list;
    private final Integer objectID;

    public MessageTrackListView(Integer objectID) {
        super("tracking", wfmStrings.tracking());
        this.objectID = objectID;
    }

    private ColumnDefinitionConfig[] drawColumns() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[4];

        ///////////////////////////////////////////---------------------------------(1)---------------------------------
        columns[0] = new ColumnDefinitionConfig<MessageTrackListItem, HTML>(wfmStrings.email(), MailMessageItem.RECIPIENT, 150) {
            @Override
            public HTML getCellValue(MessageTrackListItem item) {
                if (item.getContactType().equals(CrmConstants.TYPE_LEAD_CONTACT)) {
                    return getLink(item.getEmail() == null ? "" : item.getEmail(), "lead|summary/" + item.getEntityID() + "//" + (item.getCrmAccountID() != null ? item.getCrmAccountID() : ""));
                } else {
                    return getLink(item.getEmail() == null ? "" : item.getEmail(), "contact|summary/" + item.getEntityID() + "//" + (item.getCrmAccountID() != null ? item.getCrmAccountID() : ""));
                }
            }
        };
        columns[0].setMinimumColumnWidth(30);

        ///////////////////////////////////////////---------------------------------(2)---------------------------------
        columns[1] = new ColumnDefinitionConfig<MessageTrackListItem, HTML>(wfmStrings.firstName(), MailMessageItem.FIRSTNAME, 150) {
            @Override
            public HTML getCellValue(MessageTrackListItem item) {
                if (item.getContactType().equals(CrmConstants.TYPE_LEAD_CONTACT)) {
                    return getLink(item.getFirstName() == null ? "" : item.getFirstName(), "lead|summary/" + item.getEntityID() + "//" + (item.getCrmAccountID() != null ? item.getCrmAccountID() : ""));
                } else {
                    return getLink(item.getFirstName() == null ? "" : item.getFirstName(), "contact|summary/" + item.getEntityID() + "//" + (item.getCrmAccountID() != null ? item.getCrmAccountID() : ""));
                }
            }
        };
        columns[1].setMinimumColumnWidth(30);

        ///////////////////////////////////////////---------------------------------(3)---------------------------------
        columns[2] = new ColumnDefinitionConfig<MessageTrackListItem, HTML>(wfmStrings.lastName(), MailMessageItem.LASTNAME, 160) {
            @Override
            public HTML getCellValue(MessageTrackListItem item) {
                if (item.getContactType().equals(CrmConstants.TYPE_LEAD_CONTACT)) {
                    return getLink(item.getLastName() == null ? "" : item.getLastName(), "lead|summary/" + item.getEntityID() + "//" + (item.getCrmAccountID() != null ? item.getCrmAccountID() : ""));
                } else {
                    return getLink(item.getLastName() == null ? "" : item.getLastName(), "contact|summary/" + item.getEntityID() + "//" + (item.getCrmAccountID() != null ? item.getCrmAccountID() : ""));
                }
            }
        };
        columns[2].setMinimumColumnWidth(30);

        ///////////////////////////////////////////---------------------------------(4)---------------------------------
        columns[3] = new ColumnDefinitionConfig<MessageTrackListItem, Integer>(crmStrings.viewCount(), MailMessageItem.VIEW_COUNT, 70) {
            @Override
            public Integer getCellValue(MessageTrackListItem item) {
                return item.getOpenedCount() != null ? item.getOpenedCount() : 0;
            }
        };
        columns[3].setMinimumColumnWidth(30);

        return columns;
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.MessageTrackListPanel, drawColumns(), getDataList(), getDesign());
        list.getPdfVersion().setVisible(false);
        list.setExcelListener(clickEvent -> {
            if (list.getItemCount() > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String excelURL = CommandConstants.COMMON_URL + "/downloadMailMessagesViewExcel";
            ListingFilterParameter filterParameter = list.getFilterParametrs() != null ? list.getFilterParametrs() : new ListingFilterParameter();
            filterParameter.setMessageStatus("VIEWED");
            filterParameter.setObjectId(objectID);
            list.callListExcel(excelURL, filterParameter);
        });
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
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(crmStrings.trackMessCurrentlyMessages());
                emptyDataTable.initEmptyDataTable(message);
            }
        };

    }

    private ListingRequestProvider<MessageTrackListItem> getDataList() {
        return (filterParametrs, callback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            filterParametrs.setObjectId(objectID);
            MassMailService.App.get().getMessageViewTrackList(filterParametrs, new AsyncCallback<ListResult<MessageTrackListItem>>() {
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                public void onSuccess(ListResult<MessageTrackListItem> list1) {
                    callback.onSuccess(list1);
                }
            });
        };
    }

    public String getIconStyle() {
        return "crm queued-message-list";
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
