package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingCallback;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
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

public class MessageClickTrackListView extends BaseListView implements Constants {
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private ListingPanel<MessageTrackListItem> list;
    private final Integer objectID;

    public MessageClickTrackListView(Integer objectID) {
        super("clickTracking", crmStrings.clickTracking());
        this.objectID = objectID;
    }

    private ColumnDefinitionConfig[] drawColumns() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[5];

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
        columns[3] = new ColumnDefinitionConfig<MessageTrackListItem, String>(wfmStrings.link(), MailMessageItem.LINK, 70) {
            @Override
            public String getCellValue(MessageTrackListItem item) {
                return item.getLink();
            }
        };
        columns[3].setMinimumColumnWidth(30);

        ///////////////////////////////////////////---------------------------------(5)---------------------------------
        columns[4] = new ColumnDefinitionConfig<MessageTrackListItem, Integer>(crmStrings.clickCount(), MailMessageItem.CLICK_COUNT, 70) {
            @Override
            public Integer getCellValue(MessageTrackListItem item) {
                return item.getClickCount() != null ? item.getClickCount() : 0;
            }
        };
        columns[4].setMinimumColumnWidth(30);

        return columns;
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.MessageClickTrackListPanel, drawColumns(), getDataList(), getDesign());
        list.getPdfVersion().setVisible(false);
        list.setExcelListener(clickEvent -> {
            if (list.getItemCount() > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String excelURL = CommandConstants.COMMON_URL + "/downloadMailMessagesViewExcel";
            ListingFilterParameter filterParameter = list.getFilterParametrs() != null ? list.getFilterParametrs() : new ListingFilterParameter();
            filterParameter.setMessageStatus("CLICKED");
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(crmStrings.clickTrackMessCurrentlyMessages());
                emptyDataTable.initEmptyDataTable(message);
            }
        };

    }

    private ListingRequestProvider<MessageTrackListItem> getDataList() {
        return (ListingFilterParameter filterParametrs, ListingCallback<MessageTrackListItem> callback) -> {
            filterParametrs = filterParametrs == null ? new ListingFilterParameter() : filterParametrs;
            filterParametrs.setObjectId(objectID);
            MassMailService.App.get().getMessageClickTrackList(filterParametrs, new AsyncCallback<ListResult<MessageTrackListItem>>() {
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
