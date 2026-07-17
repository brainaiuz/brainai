package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.enums.ReferenceParentEnum;
import com.edatasite.workforce.gwt.core.client.localization.Reference;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterCutomField;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings.KanbanItemColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings.KanbanItemSettingEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrContactRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.ImportFileActionLink;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportFilePopUp;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.ui.PhonePopup;
import com.edatasite.workforce.gwt.crm.client.ui.view.kanban.LeadMaterialCard;
import com.edatasite.workforce.gwt.crm.client.ui.view.quickadd.CrmQuickAdd;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.edatasite.workforce.gwt.materialkanban.client.KanbanBoard;
import com.edatasite.workforce.gwt.materialkanban.client.KanbanBoardDesign;
import com.edatasite.workforce.gwt.materialkanban.client.KanbanDataLoader;
import com.edatasite.workforce.gwt.materialkanban.client.KanbanDataRenderer;
import com.edatasite.workforce.gwt.task.client.ui.quickadd.TaskQuickAddView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 15:26:40
 */
public class LeadListView extends ContactListView {
    private static final Reference reference = Reference.App.get();
    private AssigneePopup assigneePopup;
    private MultiAssigneePopup multiAssigneePopup;
    private Integer leadSourceId;
    private final ImportFilePopUp imp = new ImportFilePopUp(ImportTypeEnum.LEAD, null);

    private final boolean addPermission = Utils.hasPermission(PermissionConstants.ADD_NEW_LEAD);
    private final boolean quickAddPermission = Utils.hasPermission(PermissionConstants.CRM_QUICK_ADD_NEW_LEAD);

    public LeadListView() {
        super(LEAD_LIST);
        setDescription(property.getPlural(wfmStrings.leads()));
        this.isLead = true;
        setIconPlus();
    }

    public LeadListView(Integer leadSourceId) {
        super(LEAD_LIST);
        setDescription(property.getPlural(crmStrings.submittedPlusElement(), wfmStrings.leads()));
        this.leadSourceId = leadSourceId;
        this.isLead = true;
        setIconPlus();
    }

    public LeadListView(Integer campaignID, String campaignName) {
        this();
        this.campaignID = campaignID;
        this.campaignName = campaignName;
        setIconPlus();
    }

    private void setIconPlus() {
        if (quickAddPermission) {
            setAddNew(() -> new CrmQuickAdd(LayoutRPC.LEAD_FORM, RelationItem.newEventRelation(RelationItem.TYPE_CAMPAIGN, campaignID, campaignName)));
        }
    }

    public FlowPanel getHelpContainer() {
        return new FlowPanel();
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        assigneePopup = new AssigneePopup(RelationItem.TYPE_LEAD);
        assigneePopup.setListRefresh(this::refresh);

        multiAssigneePopup = new MultiAssigneePopup();
        multiAssigneePopup.setListRefresh(this::refresh);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEADS_ADD_EDIT, LeadListView.this, (sender, args) -> refresh());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEADS_DELETE, LeadListView.this, (sender, args) -> refresh());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CONTACT_AUTO_ACCOUNT_ADDED, LeadListView.this, (sender, args) -> refresh());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAD_STATUS_CHANGED, LeadListView.this, (sender, args) -> refresh());
        list.setPDFListener(clickEvent -> {
            if (totalCount > 1000) {
                String limit = "1000";
                if (defaultOne != null && defaultOne.getPdfLimit() != null) {
                    limit = defaultOne.getPdfLimit();
                }
                Window.alert(wfmMessages.currentlyLimitedContactExport(limit));
            }
            String pdfURL = CommandConstants.PDF_URL + "/leadListPDFHandler";
            ListingFilterParameter fp = list.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            list.callListPDF(pdfURL, fp);
        });
        list.setExcelListener(clickEvent -> {
            if (totalCount > 1000) {
                String limit = "1000";
                if (defaultOne != null && defaultOne.getExcelLimit() != null) {
                    limit = defaultOne.getExcelLimit();
                }
                Window.alert(wfmMessages.currentlyLimitedContactExport(limit));
            }
            String csvURL = "common/downloadCrmLeadsExcel";
            ListingFilterParameter fp = list.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            list.callListExcel(csvURL, fp);
        });
        KanbanBoard<ContactListItem> kanbanBoard = new KanbanBoard<ContactListItem>(ListPanelType.LeadKanbanPanel, getKanbanDataLoader(), getKanbanBoardDesign()) {

            @Override
            public Widget getColumnAddButton(SelectItem columnMetadata) {

                if (!quickAddPermission && addPermission) {
                    MaterialLink addLeadLink = new MaterialLink();
                    addLeadLink.setStyleName("wg_canban__add-card");
                    Icon plus = new Icon();
                    plus.setStyleName("ficon--plus");
                    addLeadLink.add(plus);
                    addLeadLink.addClickHandler((ClickEvent click) ->
                            SinksContainerFactory.entryPoint.onHistoryChanged("lead|add/add" + (campaignID != null && campaignName != null ? ("/" + campaignID + "/" + campaignName) : "")));

                    return addLeadLink;
                } else if (quickAddPermission || addPermission) {
                    MaterialLink addLeadLink = new MaterialLink();
                    addLeadLink.setStyleName("wg_canban__add-card");
                    Icon plus = new Icon();
                    plus.setStyleName("ficon--plus");
                    addLeadLink.add(plus);
                    addLeadLink.addClickHandler((ClickEvent click) -> new CrmQuickAdd(LayoutRPC.LEAD_FORM, columnMetadata.getId(),
                            RelationItem.newEventRelation(RelationItem.TYPE_CAMPAIGN, campaignID, campaignName)));

                    return addLeadLink;
                }
                return super.getColumnAddButton(columnMetadata);
            }
        };
        kanbanBoard.setKanbanItemSettingsType(KanbanItemSettingEnum.LEAD_ITEM_SETTINGS);
        list.setKanbanBoardView(kanbanBoard);
        return null;
    }

    @Override
    protected void refresh() {
        if (list.isListingPage()) {
            super.refresh();
        } else {
            list.requestKanbanData();
        }
    }

    @Override
    protected String getRelationType() {
        return RelationItem.TYPE_LEAD;
    }

    @Override
    protected CustomColumnDefinitionConfig[] getColumns() {
        ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        ColumnDefinitionConfig column;

        //actions
        column = new ColumnDefinitionConfig<ContactListItem, Anchor>(wfmStrings.actions(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final ContactListItem rowValue) {
                return getActions(rowValue);
            }
        };
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        columns.add(column);
        //Name
        column = new ColumnDefinitionConfig<ContactListItem, SimpleLink>(wfmStrings.name(), ContactListItem.CONTACT_NAME, 130, true) {
            @Override
            public SimpleLink getCellValue(ContactListItem leadListItem) {
                SimpleLink link = getLink(leadListItem.getName(), "lead|summary/" + leadListItem.getObjectId() + "/" + (leadListItem.getCrmAccount() != null ? leadListItem.getCrmAccount().getObjectId() : ""));
                if (leadListItem.getNote() != null && !"".equals(leadListItem.getNote())) {
                    new KpiToolTip(link, leadListItem.getNote());
                }
                link.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("lead|summary/" + leadListItem.getObjectId() + "/" + (leadListItem.getCrmAccount() != null ? leadListItem.getCrmAccount().getObjectId() : ""), leadListItem.getName(), leadListItem.getName()));

                return link;
            }
        };
        column.setShow(ContactListItem.defaultLeadColumnNames.contains(ContactListItem.CONTACT_NAME));
        column.setMinimumColumnWidth(80);
        column.setShowPopup(true);
        columns.add(column);
        //Company
        column = new ColumnDefinitionConfig<ContactListItem, SelectItem>(wfmStrings.company(), ContactListItem.CRM_ACCOUNT, 115) {
            @Override
            public SelectItem getCellValue(ContactListItem leadListItem) {
                return leadListItem.getCrmAccount().asSelectItem();
            }

            @Override
            public void setCellValue(ContactListItem rowValue, SelectItem value) {
                if (value != null) {
                    rowValue.getCrmAccount().setObjectId(value.getId());
                    rowValue.getCrmAccount().setName(value.getName());
                } else {
                    rowValue.getCrmAccount().setName(null);
                    rowValue.getCrmAccount().setObjectId(null);
                }
                saveCellValue(rowValue);
            }
        };
        column.setShow(ContactListItem.defaultLeadColumnNames.contains(ContactListItem.CRM_ACCOUNT));
        column.setMinimumColumnWidth(60);
        column.setShowPopup(true);
        columns.add(column);
        //Email
        column = new ColumnDefinitionConfig<ContactListItem, HTML>(wfmStrings.email(), ContactListItem.EMAIL, 115) {
            @Override
            public HTML getCellValue(final ContactListItem leadListItem) {
                return getEmailLink(leadListItem);
            }

            @Override
            public void setCellValue(ContactListItem leListItem, HTML cellValue) {
                leListItem.setPrimaryEmail(cellValue.getText());
                saveCellValue(leListItem);
            }
        };
        column.setShow(ContactListItem.defaultLeadColumnNames.contains(ContactListItem.EMAIL));
        column.setColumnSortable(true);
        column.setMinimumColumnWidth(60);
        columns.add(column);
        //Phone
//        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_TWILIO)) {
        column = new ColumnDefinitionConfig<ContactListItem, Div>(wfmStrings.phone(), ContactListItem.PHONE, 115) {
            @Override
            public Div getCellValue(final ContactListItem rowValue) {
                PhonePopup phonePopup = new PhonePopup(rowValue.getPrimaryPhone(), rowValue, false, true);
                return phonePopup.getPhoneWidget();
            }
        };
       /* } else {
            column = new ColumnDefinitionConfig<ContactListItem, HTML>(crmStrings.phone(), ContactListItem.PHONE, 115) {
                @Override
                public HTML getCellValue(final ContactListItem rowValue) {
                    if (rowValue.getPrimaryPhone() != null && !"N/A".equalsIgnoreCase(rowValue.getPrimaryPhone())) {
                        return new HTML("<a href=\"tel:" + rowValue.getPrimaryPhone() + "\">" + rowValue.getPrimaryPhone() + "</a>");
                    } else {
                        return new HTML(rowValue.getPrimaryPhone());
                    }
                }
            };
        }*/
        column.setShow(ContactListItem.defaultLeadColumnNames.contains(ContactListItem.PHONE));
        column.setColumnSortable(false);
        column.setMinimumColumnWidth(60);
        columns.add(column);
        //Status
        column = new ColumnDefinitionConfig<ContactListItem, SelectItem>(wfmStrings.status(), ContactListItem.LEAD_STATUS, 90) {

            @Override
            public SelectItem getCellValue(ContactListItem leadListItem) {
                return leadListItem.getLeadStatus(true);
            }

            @Override
            public void setCellValue(ContactListItem rowValue, SelectItem cellValue) {
                rowValue.setLeadStatus(cellValue);
                if (Utils.isDoubleMessageEnable()) {
                    WfmMessageBox changeStatusMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    changeStatusMessageBox.setMessage(crmMessages.doYouWantToChangeStatusTo(cellValue.getName()));
                    changeStatusMessageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            saveCellValue(rowValue);
                        }

                        @Override
                        public void onCancel() {
                            list.reloadPage();
                        }
                    });
                    changeStatusMessageBox.setTitle(wfmStrings.warning());
                    changeStatusMessageBox.open();
                } else {
                    saveCellValue(rowValue);
                }
            }
        };
        column.setShow(ContactListItem.defaultLeadColumnNames.contains(ContactListItem.LEAD_STATUS));

        column.addColor(new ColumnColor(reference.ATTEMPTED_TO_CONTACT(), "r", "2BBF57"));
        column.addColor(new ColumnColor(reference.CONTACT_IN_FUTURE(), "r", "007DE7"));
        column.addColor(new ColumnColor(reference.NOT_CONTACTED(), "c", "DC0C0C"));
        column.setColumnSortable(true);
        column.setMinimumColumnWidth(50);
        columns.add(column);
        //Source
        column = new ColumnDefinitionConfig<ContactListItem, SelectItem>(wfmStrings.source(), ContactListItem.LEAD_SOURCE, 60) {

            @Override
            public SelectItem getCellValue(ContactListItem leadListItem) {
                SelectItem selectItem = new SelectItem(leadListItem.getLeadSourceID(), leadListItem.getLeadSource());
                if (Constants.OTHER.equals(leadListItem.getLeadSource()) &&
                        leadListItem.getOtherLeadSource() != null && !"".equals(leadListItem.getOtherLeadSource())) {
                    selectItem.setName(leadListItem.getLeadSource() + "/" + (leadListItem.getOtherLeadSource() == null ? "" : leadListItem.getOtherLeadSource()));
                }
                return selectItem;
            }

            @Override
            public void setCellValue(ContactListItem rowValue, SelectItem cellValue) {
                if (cellValue != null) {
                    rowValue.setLeadSource(cellValue.getId() != null && cellValue.getId() > 0 ? cellValue.getName() : null);
                    rowValue.setLeadSourceID(cellValue.getId());
                } else {
                    rowValue.setLeadSource(null);
                    rowValue.setLeadSourceID(null);
                }
                saveCellValue(rowValue);
            }
        };
        column.setShow(ContactListItem.defaultLeadColumnNames.contains(ContactListItem.LEAD_SOURCE));
        column.setMinimumColumnWidth(30);
        columns.add(column);
        if (Utils.hasPermission(CHANGE_LEADS_ASSIGNEE)) {
            //Assignee
            column = new ColumnDefinitionConfig<ContactListItem, SelectItem>(wfmStrings.assignee(), ContactListItem.LEAD_ASSIGNEE, 120) {
                @Override
                public SelectItem getCellValue(ContactListItem leadListItem) {
                    return new SelectItem(leadListItem.getLeadAssigneeID(), leadListItem.getLeadAssignee());
                }

                @Override
                public void setCellValue(ContactListItem rowValue, SelectItem cellValue) {
                    rowValue.setLeadAssigneeID(cellValue != null ? cellValue.getId() : null);
                    rowValue.setLeadAssignee(cellValue != null && cellValue.getId() != null && cellValue.getId() > 0 ? cellValue.getName() : null);
                    saveCellValue(rowValue);
                }

            };
            column.setShow(ContactListItem.defaultLeadColumnNames.contains(ContactListItem.LEAD_ASSIGNEE));
            column.setMinimumColumnWidth(80);
            columns.add(column);

            //Backup Assignee
            column = new ColumnDefinitionConfig<ContactListItem, SelectItem>(wfmStrings.backupAssignee(), ContactListItem.LEAD_BACKUP_ASSIGNEE, 120) {
                @Override
                public SelectItem getCellValue(ContactListItem leadListItem) {
                    return new SelectItem(leadListItem.getLeadBackupAssigneeID(), leadListItem.getLeadBackupAssignee());
                }

                @Override
                public void setCellValue(ContactListItem rowValue, SelectItem cellValue) {
                    rowValue.setLeadBackupAssigneeID(cellValue != null ? cellValue.getId() : null);
                    rowValue.setLeadBackupAssignee(cellValue != null && cellValue.getId() != null && cellValue.getId() > 0 ? cellValue.getName() : null);
                    saveCellValue(rowValue);
                }
            };
            column.setShow(ContactListItem.defaultLeadColumnNames.contains(ContactListItem.LEAD_BACKUP_ASSIGNEE));
            column.setMinimumColumnWidth(80);
            columns.add(column);
        } else {
            //Assignee
            column = new ColumnDefinitionConfig<ContactListItem, String>(wfmStrings.assignee(), ContactListItem.LEAD_ASSIGNEE, 120) {
                @Override
                public String getCellValue(ContactListItem leadListItem) {
                    return leadListItem.getLeadAssignee();
                }

            };
            column.setShow(ContactListItem.defaultLeadColumnNames.contains(ContactListItem.LEAD_ASSIGNEE));
            column.setMinimumColumnWidth(80);
            columns.add(column);

            //Backup Assignee
            column = new ColumnDefinitionConfig<ContactListItem, String>(wfmStrings.backupAssignee(), ContactListItem.LEAD_BACKUP_ASSIGNEE, 120) {
                @Override
                public String getCellValue(ContactListItem leadListItem) {
                    return leadListItem.getLeadBackupAssignee();
                }
            };
            column.setShow(ContactListItem.defaultLeadColumnNames.contains(ContactListItem.LEAD_BACKUP_ASSIGNEE));
            column.setMinimumColumnWidth(80);
            columns.add(column);
        }
        //Country
        column = new ColumnDefinitionConfig<ContactListItem, SelectItem>(wfmStrings.country(), ContactListItem.COUNTRY, 80) {
            @Override
            public SelectItem getCellValue(ContactListItem rowValue) {
                rowValue.setPrimaryAddress(rowValue.getPrimaryAddress(true));
                Address addressItems = rowValue.getPrimaryAddress();
                return addressItems.getCountryId() != null ? new SelectItem(addressItems.getCountryId(), addressItems.getCountry()) : null;
            }

            @Override
            public void setCellValue(ContactListItem rowValue, SelectItem value) {
                rowValue.setPrimaryAddress(rowValue.getPrimaryAddress(true));
                if (SelectItem.isDifferent(value, new SelectItem(rowValue.getPrimaryAddress().getCountryId(), rowValue.getPrimaryAddress().getCountry()))) {
                    rowValue.getPrimaryAddress().setCountry(value != null && value.getId() != null && value.getId() > 0 ? value.getName() : null);
                    rowValue.getPrimaryAddress().setCountryId(value != null ? value.getId() : null);
                    rowValue.getPrimaryAddress().setState(null);
                    saveCellValue(rowValue);
                }
            }
        };
        column.setShow(ContactListItem.defaultLeadColumnNames.contains(ContactListItem.COUNTRY));
        column.setMinimumColumnWidth(40);
        columns.add(column);
        //Job title
        column = new ColumnDefinitionConfig<ContactListItem, String>(wfmStrings.jobTitle(), ContactListItem.JOB_TITLE, 80) {

            @Override
            public String getCellValue(ContactListItem leadListItem) {
                return leadListItem.getJobTitle();
            }

            @Override
            public void setCellValue(ContactListItem rowValue, String cellValue) {
                rowValue.setJobTitle(cellValue);
                saveCellValue(rowValue);
            }
        };
        column.setShow(ContactListItem.defaultLeadColumnNames.contains(ContactListItem.JOB_TITLE));
        column.setMinimumColumnWidth(80);
        columns.add(column);
        //City
        column = new ColumnDefinitionConfig<ContactListItem, String>(wfmStrings.city(), ContactListItem.CITY, 80) {

            @Override
            public String getCellValue(ContactListItem leadListItem) {
                return leadListItem.getPrimaryAddress(true).getCity();
            }

            @Override
            public void setCellValue(ContactListItem rowValue, String cellValue) {
                if (rowValue.getPrimaryAddress() == null) {
                    rowValue.setPrimaryAddress(new Address());
                }
                rowValue.getPrimaryAddress().setCity(cellValue);
                saveCellValue(rowValue);
            }
        };
        column.setShow(ContactListItem.defaultLeadColumnNames.contains(ContactListItem.CITY));
        column.setMinimumColumnWidth(40);
        columns.add(column);
        //Campaign
        column = new ColumnDefinitionConfig<ContactListItem, SelectItem>(wfmStrings.campaign(), ContactListItem.CAMPAIGN, 60) {

            @Override
            public SelectItem getCellValue(ContactListItem leadListItem) {
                return new SelectItem(leadListItem.getCampaignId(), leadListItem.getCampaign());
            }

            @Override
            public void setCellValue(ContactListItem rowValue, SelectItem cellValue) {
                rowValue.setCampaignSI(cellValue);
                saveCellValue(rowValue);
            }
        };
        column.setShow(ContactListItem.defaultLeadColumnNames.contains(ContactListItem.CAMPAIGN));
        column.setMinimumColumnWidth(30);
        columns.add(column);
        //Mobile
        column = new ColumnDefinitionConfig<ContactListItem, Div>(wfmStrings.mobile(), ContactListItem.MOBILE, 115) {

            @Override
            public Div getCellValue(final ContactListItem leadListItem) {
                if (leadListItem.getMobile() != null && leadListItem.getMobile().size() > 0) {
                    PhonePopup phonePopup = new PhonePopup(leadListItem.getMobile().get(0), leadListItem, true, true);
                    return phonePopup.getPhoneWidget();
                }
                return null;
            }
        };
        column.setShow(ContactListItem.defaultLeadColumnNames.contains(ContactListItem.MOBILE));
        column.setColumnSortable(false);
        column.setMinimumColumnWidth(60);
        columns.add(column);
        //Rating
        column = new ColumnDefinitionConfig<ContactListItem, SelectItem>(wfmStrings.rating(), ContactListItem.LEAD_RATING, 60) {

            @Override
            public SelectItem getCellValue(ContactListItem leadListItem) {
                return new SelectItem(leadListItem.getLeadRatingID(), leadListItem.getLeadRating());
            }

            @Override
            public void setCellValue(ContactListItem rowValue, SelectItem cellValue) {
                if (cellValue != null) {
                    rowValue.setLeadRating(cellValue.getId() != null && cellValue.getId() > 0 ? cellValue.getName() : null);
                    rowValue.setLeadRatingID(cellValue.getId());
                } else {
                    rowValue.setLeadRating(null);
                    rowValue.setLeadRatingID(null);
                }
                saveCellValue(rowValue);
            }
        };
        column.setShow(ContactListItem.defaultLeadColumnNames.contains(ContactListItem.LEAD_RATING));
        column.setColumnSortable(true);
        column.setMinimumColumnWidth(60);
        columns.add(column);
        //Modified date
        column = new ColumnDefinitionConfig<ContactListItem, String>(wfmStrings.modifiedDate(), ContactListItem.LAST_MODIFIED, 95) {

            @Override
            public String getCellValue(ContactListItem leadListItem) {
                return leadListItem.getUpdatedDate() != null ? DateUtils.formatInternal(leadListItem.getUpdatedDate()) : "N/A";
            }
        };
        column.setShow(ContactListItem.defaultLeadColumnNames.contains(ContactListItem.LAST_MODIFIED));
        column.setMinimumColumnWidth(40);
        columns.add(column);
        //Creation Date
        column = new ColumnDefinitionConfig<ContactListItem, String>(wfmStrings.createdDate(), ContactListItem.CREATION_DATE, 95) {

            @Override
            public String getCellValue(ContactListItem leadListItem) {
                return DateUtils.formatInternal(leadListItem.getCreatedDate());
            }
        };
        column.setShow(ContactListItem.defaultLeadColumnNames.contains(ContactListItem.CREATION_DATE));
        column.setMinimumColumnWidth(40);
        columns.add(column);
        //Created By
        column = new ColumnDefinitionConfig<ContactListItem, String>(wfmStrings.createdBy(), ContactListItem.CREATOR, 100) {

            @Override
            public String getCellValue(ContactListItem leadListItem) {
                return leadListItem.getCreator();
            }
        };
        column.setShow(ContactListItem.defaultLeadColumnNames.contains(ContactListItem.CREATOR));
        column.setMinimumColumnWidth(60);
        columns.add(column);
        //Modified By
        column = new ColumnDefinitionConfig<ContactListItem, String>(wfmStrings.modifiedBy(), ContactListItem.UPDATER, 100) {

            @Override
            public String getCellValue(ContactListItem leadListItem) {
                return leadListItem.getUpdater();
            }
        };
        column.setShow(ContactListItem.defaultLeadColumnNames.contains(ContactListItem.UPDATER));
        column.setMinimumColumnWidth(60);
        columns.add(column);
        //Owner
        column = new ColumnDefinitionConfig<ContactListItem, SelectItem>(wfmStrings.owner(), ContactListItem.OWNER, 100) {
            @Override
            public SelectItem getCellValue(ContactListItem rowValue) {
                return new SelectItem(rowValue.getOwnerId(), rowValue.getOwner());
            }

            @Override
            public void setCellValue(ContactListItem rowValue, SelectItem cellValue) {
                rowValue.setOwnerId(cellValue != null ? cellValue.getId() : null);
                rowValue.setOwner(cellValue != null && cellValue.getId() != null && cellValue.getId() > 0 ? cellValue.getName() : null);
                saveCellValue(rowValue);
            }
        };
        column.setShow(ContactListItem.defaultLeadColumnNames.contains(ContactListItem.OWNER));
        column.setMinimumColumnWidth(70);
        columns.add(column);

        if (Utils.hasPermission(CRM_LEAD_EDIT)) {
            initCellEdit(CustomColumnDefinitionConfig.getEditableColumns(columns));
        }
        return columns.toArray(new CustomColumnDefinitionConfig[0]);
    }

    @Override
    protected ListingRequestProvider<ContactListItem> getRequestProvider() {
        return new ListingRequestProvider() {
            @Override
            public void getRequest(final ListingFilterParameter filterParametrs, final ListingCallback listingCallback) {
                if (Utils.hasPermission(CRM_LEAD_EDIT)) {
                    crmService.editLead(null, null, new AbstractAsyncCallback<ContactListItem>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            super.onFailure(caught);
                        }

                        @Override
                        public void onSuccess(ContactListItem result) {
                            defaultOne = result;
                            list.setDefaultOne(defaultOne);
                        }
                    });
                }
                initContactList(filterParametrs, listingCallback, null);
            }
        };
    }

    @Override
    protected void initContactList(ListingFilterParameter filterParametrs, ListingCallback<ContactListItem> listingCallback, Span container) {
        filterParametrs.setWebFormID(leadSourceId);
        if (campaignID != null) {
            filterParametrs.setCampaignID(campaignID);
        }
        filterParametrs.setDetectDuplicates(detectDuplicates);
        filterParametrs.setObjectIDs(ContactListItem.getIDsOnly(lastSelectedItems));
        crmService.getNewLeads(filterParametrs, new AbstractAsyncCallback<ListResult<ContactListItem>>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(ListResult<ContactListItem> result) {
                totalCount = result.getTotal();
                if (listingCallback != null) {
                    listingCallback.onSuccess(result);
                    if (list.getPagingScrollTable().isShowPopups()) {
                        list.setPopupWidgets(getNotesAsWidgets(result));
                    }
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (totalCount > 0) {
                        statisticShortcut.setText(countFormat(totalCount));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
    }

    private ArrayList<ArrayList<Widget>> getNotesAsWidgets(ListResult<ContactListItem> result) {
        ArrayList<ArrayList<Widget>> widgets = new ArrayList<>();
        if (result != null && result.getList() != null && result.getList().size() > 0) {
            for (ContactListItem lead : result.getList()) {
                ArrayList<Widget> leadsWidgets = new ArrayList<>();
                if (lead.getNote() != null && !"".equals(lead.getNote().trim())) {
                    FlexTable panel = new FlexTable();
                    panel.setWidth("100%");
                    panel.setWidget(0, 0, new HTML("<b class=\"customTitle\">" + wfmStrings.lastNote() + "</b>"));
                    panel.setHTML(1, 0, lead.getNote());
                    leadsWidgets.add(panel);
                }
                widgets.add(leadsWidgets);
            }
        }
        return widgets;
    }

    @Override
    protected GuideListingPanelDesign getPanelDesigner() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return Utils.hasPermission(PermissionConstants.ADD_NEW_LEAD) ? () -> SinksContainerFactory.entryPoint.onHistoryChanged("lead|add/add" + (campaignID != null && campaignName != null ? ("/" + campaignID + "/" + campaignName) : "")) : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return isShowImport() ? () -> imp.open() : null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> {
                            if (leadSourceId != null) {
                                SelectItem[] item = new SelectItem[]{new SelectItem(leadSourceId)};
                                if (data != null) {
                                    data.getFacetContentMap().get(FacetContentType.LeadFacetFilter.getContentCode()[1]).setFacetItems(item);
                                }
                            }
                            if (campaignID != null) {
                                SelectItem[] item = new SelectItem[]{new SelectItem(campaignID)};
                                if (data != null && data.getFacetContentMap().get(FacetContentType.LeadFacetFilter.getContentCode()[0]) != null) {
                                    data.getFacetContentMap().get(FacetContentType.LeadFacetFilter.getContentCode()[0]).setFacetItems(item);
                                }
                                data.setCustomDataPut(FacetFilterCutomField.RELATION_TYPE, RelationItem.TYPE_CAMPAIGN);
                                data.setCustomDataPut(FacetFilterCutomField.RELATION_ID, campaignID.toString());
                            }
                            RbacService.App.get().getCRMFacetFilterData(CrmConstants.CRM_LEAD, data, new AbstractAsyncCallback<FacetFilterRpc>() {
                                public void failure(Throwable caught) {
                                    callback.onFailure(caught);
                                }

                                public void success(FacetFilterRpc result) {
                                    callback.onSuccess(result);
                                }
                            });
                        };
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return getLeadFacetContentConfigure();
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addMenu = getAddNewButton(ActionButton.Type.TOOLMENU);
                MenuBar menuBar = new MenuBar(true);
                menuBar.setAutoOpen(true);

                if (Utils.hasPermission(PermissionConstants.ADD_NEW_LEAD)) {
                    MenuPopItem addLead = new MenuPopItem(property.getSingular(wfmStrings.lead()));
                    addLead.ensureDebugId("lead_detailed_view");
                    addLead.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("lead|add/add" + (campaignID != null && campaignName != null ? ("/" + campaignID + "/" + campaignName) : "")));

                    menuBar.addItem(addLead);
                }
                if (Utils.hasPermission(PermissionConstants.CRM_QUICK_ADD_NEW_LEAD)) {
                    MenuPopItem quickAdd = new MenuPopItem(wfmStrings.quickAdd());
                    quickAdd.ensureDebugId("lead_quick_add");
                    quickAdd.setScheduledCommand(() -> new CrmQuickAdd(LayoutRPC.LEAD_FORM,
                            RelationItem.newEventRelation(RelationItem.TYPE_CAMPAIGN, campaignID, campaignName)));

                    menuBar.addItem(quickAdd);
                }
                if (Utils.hasPermission(PermissionConstants.CRM_MULTIPLE_ADD_NEW_LEADS)) {
                    MenuPopItem addMultipleLead = new MenuPopItem(property.getPlural(crmStrings.addNewMulti(), wfmStrings.leads()));
                    addMultipleLead.ensureDebugId("Crm_add_Multiple_Lead");
                    if (campaignID != null) {
                        addMultipleLead.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("multilead|add/add" + "/" + campaignID + "/" + RelationItem.TYPE_CAMPAIGN));
                    } else {
                        addMultipleLead.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("multilead|add/add"));
                    }

                    menuBar.addItem(addMultipleLead);
                }

                if (Utils.hasPermission(CRM_ADD_NEW_MAILING_LIST) && Utils.hasPermission(CRM_E_MAIL_MARKETING_TAB)) {
                    MenuPopItem createMailingList = new MenuPopItem(wfmStrings.createMailingList());
                    createMailingList.ensureDebugId("Contact_List_Create_Mailing_List");
                    createMailingList.setCommand(() -> {
                        if (selectedItems.size() > 0) {
                            new MailListPopup(null, selectedItems.toArray(new ContactListItem[]{}), true, new ListingFilterParameter());
                        } else {
                            ListingFilterParameter filterParam = list.getFilterParametrs() == null ? new ListingFilterParameter() : list.getFilterParametrs();
                            filterParam.setFacetFilterJson(Utils.facetFilterRpcToJsonString(filterParam.getFacetFilter()));
                            filterParam.setListPanelToolJson(Utils.listPanelToolRpcConvertJsonData(filterParam.getListPanelTool()));
                            new MailListPopup(null, null, true, filterParam);
                        }
                    });
                    menuBar.addItem(createMailingList);
                }

                addMenu.setMenu(menuBar);
                return addMenu;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                final ActionButton more = new ActionButton(ActionButton.getMoreString(), "", ActionButton.Type.TOOLMENU);
                more.ensureDebugId("leadListMore");
                more.addDomHandler(event -> {
                    MenuBar menuBar = getActionsForSelections();
                    menuBar.setAutoOpen(true);
                    more.setMenu(menuBar);
                    menuBar.setLayoutData(more);
                }, MouseOverEvent.getType());
                return more;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                imp.setSubmitCompleted(() -> {
                    if (imp.getObjectId() != null) {
                        goTo("importlead|add/add/" + imp.getObjectId());
                    }
                });
                if (isShowImport()) {
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getPlural(crmStrings.messCurrentlyLeads(), wfmStrings.leads()));
                if (quickAddPermission) {
                    message.setHref(clickEvent -> new CrmQuickAdd(LayoutRPC.LEAD_FORM,
                            RelationItem.newEventRelation(RelationItem.TYPE_CAMPAIGN, campaignID, campaignName)));
                    message.setTextBeforeLink(property.getPlural(crmStrings.messYouLeadsClicking(), wfmStrings.leads()));
                }
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isShowResetButton() {
                return true;
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.hasPermission(CRM_LEAD_EDIT);
            }

            @Override
            public boolean isShowCustomiseButton() {
                return Utils.hasPermission(PermissionConstants.CRM_LEADS_LIST_CUSTOMIZE_BUTTON);
            }
        };
    }

    protected boolean isShowImport() {
        return Utils.hasPermission(PermissionConstants.CRM_LEADS_IMPORT);
    }

    protected boolean isShowExport() {
        return Utils.hasPermission(PermissionConstants.CRM_LEADS_EXPORT);
    }

    private FacetContentConfigure getLeadFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(4, wfmStrings.filter());

        if (Utils.hasRole(ADMIN) || Utils.hasRole(SALESMAN) || Utils.hasRole(DR)) {
            contentConfigure.addContentConfigure(FacetContentType.LeadFacetFilter.getContentCode()[5], wfmStrings.assignedTo(), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrContactRepresenter.FIELD_LEAD_ASSIGNEE_ID;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrContactRepresenter.FIELD_LEAD_ASSIGNEE_ID_NAME;
                }
            });
        }

        contentConfigure.addContentConfigure(FacetContentType.LeadFacetFilter.getContentCode()[2], wfmStrings.status(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrContactRepresenter.FIELD_LEAD_STATUS_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrContactRepresenter.FIELD_LEAD_STATUS_ID_CODE_NAME;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.REFERENCE;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.LeadFacetFilter.getContentCode()[6], wfmStrings.owner(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrContactRepresenter.FIELD_OWNER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrContactRepresenter.FIELD_OWNER_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.LeadFacetFilter.getContentCode()[1], wfmStrings.source(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrContactRepresenter.FIELD_LEAD_SOURCE_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrContactRepresenter.FIELD_LEAD_SOURCE_ID_CODE_NAME;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.REFERENCE;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.LeadFacetFilter.getContentCode()[3], wfmStrings.country(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrContactRepresenter.FIELD_COUNTRY_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrContactRepresenter.FIELD_COUNTRY_ID_CODE_NAME;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.COUNTRY;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.LeadFacetFilter.getContentCode()[4], wfmStrings.jobTitle(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrContactRepresenter.FIELD_JOB_TITLE;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrContactRepresenter.FIELD_JOB_TITLE;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }

            @Override
            public boolean isConditionItemId() {
                return false;
            }
        });


        contentConfigure.addContentConfigure(FacetContentType.LeadFacetFilter.getContentCode()[0], wfmStrings.campaign(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrContactRepresenter.FIELD_CAMPAIGN_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrContactRepresenter.FIELD_CAMPAIGN_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigureDateListBox(SolrContactRepresenter.FIELD_UPDATE_DATE, wfmStrings.modifiedDate());
        contentConfigure.addContentConfigureDateListBox(SolrContactRepresenter.FIELD_CREATION_DATE, wfmStrings.createdDate());
        return contentConfigure;
    }


    @Override
    protected Anchor getActions(final ContactListItem item) {
        int actionItemCount = 0;
        final MenuBar menuBar = new MenuBar(true);
        menuBar.setAutoOpen(true);

        MenuPopItem summeryMenuPopItem = new MenuPopItem(wfmStrings.summaryView(), "icon-leads", () -> {
            if (item != null) {
                SinksContainerFactory.entryPoint.onHistoryChanged("lead|summary/" + item.getObjectId() + "/" + (item.getCrmAccount() != null ? item.getCrmAccount().getObjectId() : ""), item.getName(), item.getName());
            }
        });
        summeryMenuPopItem.ensureDebugId("Lead View");
        actionItemCount++;
        menuBar.addItem(summeryMenuPopItem);

        if (Utils.hasPermission(CRM_LEAD_EDIT)) {
            MenuPopItem leadEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit", () -> {
                if (item != null) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("leadedit|editlead/" + item.getObjectId() + "/" + (item.getCrmAccount() != null ? item.getCrmAccount().getObjectId() : ""), item.getName(), item.getName());
                }
            });
            leadEdit.ensureDebugId(property.getSingular(wfmStrings.edit(), wfmStrings.lead()));
            actionItemCount++;
            menuBar.addItem(leadEdit);
        }

        MenuPopItem addNoteMenuPopItem = new MenuPopItem(wfmStrings.addNote(), "icon-add-node", () -> new NotePopup(item.getObjectId(), RelationItem.TYPE_LEAD));
        addNoteMenuPopItem.ensureDebugId(wfmStrings.addNote());
        actionItemCount++;
        menuBar.addItem(addNoteMenuPopItem);

        if (Utils.hasPermission(PermissionConstants.CRM_LEAD_STATUS, CRM_LEAD_EDIT)) {
            MenuPopItem statusMenuPopItem = new MenuPopItem(wfmStrings.changeStatus(), "icon-leads");
            statusMenuPopItem.ensureDebugId(wfmStrings.changeStatus());

            MenuBar changeStat = new MenuBar(true);
            changeStat.setAutoOpen(true);
            for (final SelectItem status : item.getLeadStatuses()) {
                final MenuPopItem menuItem = new MenuPopItem(status.getName());
                if (Objects.equals(status.getId(), item.getLeadStatus(true).getId())) {
                    menuItem.setSelection(true);
                }
                menuItem.setCommand(() -> {
                    menuItem.closeAll(menuBar);
                    item.setLeadStatus(status);
                    if (Utils.isDoubleMessageEnable()) {
                        WfmMessageBox changeStatusMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        changeStatusMessageBox.setMessage(crmMessages.doYouWantToChangeStatusTo(status.getName()));
                        changeStatusMessageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                changeStatus(Utils.asArrayList(item.getObjectId()), item.getLeadStatus(true).getId());
                            }
                        });
                        changeStatusMessageBox.setTitle(wfmStrings.warning());
                        changeStatusMessageBox.open();
                    } else {
                        changeStatus(Utils.asArrayList(item.getObjectId()), item.getLeadStatus(true).getId());
                    }
                });
                changeStat.addItem(menuItem);
            }
            statusMenuPopItem.setSubMenu(changeStat);
            actionItemCount++;
            menuBar.addItem(statusMenuPopItem);
        }

        if (Utils.hasPermission(CRM_ADD_NEW_ACTIVITY_EVENT, CRM_ADD_NEW_ACTIVITY_LOG_A_CALL, CRM_TASKS_ADD, CRM_MESSAGE_CENTER, ADD_LEAD_SMS)) {
            MenuPopItem addActivityMenuPopItem = new MenuPopItem(wfmStrings.add(), "icon-add-green");
            addActivityMenuPopItem.ensureDebugId(wfmStrings.add());

            MenuBar addActivityMenu = new MenuBar(true);
            addActivityMenu.setAutoOpen(true);
            if (Utils.hasPermission(CRM_ADD_NEW_ACTIVITY_LOG_A_CALL)) {
                final MenuPopItem logItem = new MenuPopItem(Property.get(Constants.LOGACALL, wfmStrings.logCall()), "icon-call");
                logItem.ensureDebugId(Property.get(Constants.LOGACALL, wfmStrings.logCall()));
                logItem.setCommand(() -> {
                    logItem.closeAll(menuBar);
                    new ActivityQuickAddForm(Appointment.CALL_LOG, RelationItem.newEventRelation(getRelationType(), item.getObjectId(), item.getName()), RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getCrmAccount().getObjectId(), item.getCrmAccount().getName()));
                });
                addActivityMenu.addItem(logItem);
            }
            if (Utils.hasPermission(CRM_ADD_NEW_ACTIVITY_EVENT)) {
                final MenuPopItem scheduleItem = new MenuPopItem(Property.get(Constants.EVENT_LIST, crmStrings.scheduleEventMeeting()), "icon-schedile");
                scheduleItem.ensureDebugId(Property.get(Constants.EVENT_LIST, crmStrings.scheduleEventMeeting()));
                scheduleItem.setCommand(() -> {
                    scheduleItem.closeAll(menuBar);
                    new ActivityQuickAddForm(Appointment.EVENT, RelationItem.newEventRelation(getRelationType(), item.getObjectId(), item.getName()), RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getCrmAccount().getObjectId(), item.getCrmAccount().getName()));
                });
                addActivityMenu.addItem(scheduleItem);
            }
            if (Utils.hasPermission(CRM_TASKS_ADD)) {
                final MenuPopItem taskItem = new MenuPopItem(wfmStrings.task(), "icon-add-task");
                taskItem.ensureDebugId(Property.get(Constants.TASK, wfmStrings.addMess(), wfmStrings.task()));
                taskItem.setCommand(() -> {
                    taskItem.closeAll(menuBar);
                    new TaskQuickAddView(RelationItem.newEventRelation(getRelationType(), item.getObjectId(), item.getName()),
                            RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getCrmAccount() != null ? item.getCrmAccount().getObjectId() : null, item.getCrmAccount() != null ? item.getCrmAccount().getName() : null));
                });
                addActivityMenu.addItem(taskItem);
            }
            if (Utils.hasPermission(CRM_ADD_NEW_MAILING_LIST) && Utils.hasPermission(PermissionConstants.CRM_E_MAIL_MARKETING_TAB)) {
                final MenuPopItem editSubscriptions = new MenuPopItem(wfmStrings.editSubscription(), "icon-edit-subscriptions", () -> showEditSubscriptionsShell(item.getObjectId()));
                editSubscriptions.ensureDebugId("edit_Subscriptions");
                addActivityMenu.addItem(editSubscriptions);
            }
            addActivityMenuPopItem.setSubMenu(addActivityMenu);
            actionItemCount++;
            menuBar.addItem(addActivityMenuPopItem);
        }

        final MenuBar bar = new MenuBar(true);
        bar.setAutoOpen(true);

        MenuPopItem sendSalesInv = new MenuPopItem(wfmStrings.salesInvoice(), "icon-send-sales-invoice", () -> sendInvoiceOrQuote(item, true));
        sendSalesInv.ensureDebugId("sendSales_Invoice");
        bar.addItem(sendSalesInv);

        if (Utils.hasPermission(CRM_SALES_QUOTE_ADD)) {
            MenuPopItem sendSalesQuote = new MenuPopItem(wfmStrings.salesQuote(), "icon-send-sales-quote", () -> sendInvoiceOrQuote(item, false));
            sendSalesQuote.ensureDebugId("sendSales_Quote");
            bar.addItem(sendSalesQuote);
        }

        if (Utils.hasPermission(CRM_MESSAGE_CENTER) && !Utils.isNullOrEmpty(item.getPrimaryEmail())) {
            final MenuPopItem emailItem = new MenuPopItem(wfmStrings.email(), "icon-send-message");
            emailItem.ensureDebugId(wfmStrings.sendEmail());
            emailItem.setCommand(() -> {
                emailItem.closeAll(menuBar);
                if (!item.isEmailOptOut()) {
                    //new ComposeView(item.getPrimaryEmail(), RelationItem.newEventRelation(RelationItem.TYPE_LEAD, item.getObjectId(), item.getName()));
                    goTo("emailcompose|add/add/" + item.getPrimaryEmail() + "/" + RelationItem.TYPE_LEAD + "/" + item.getObjectId() + "/" + item.getName());
                } else {
                    WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.OK, crmMessages.theEmailOutIsEnabled());
                    messageBox.setTitle(wfmStrings.information());
                    messageBox.open();
                }
            });
            bar.addItem(emailItem);
        }
        if (Utils.hasPermission(ADD_LEAD_SMS)) {
            final MenuPopItem addSms = new MenuPopItem(wfmStrings.sms(), "icon-sms");
            addSms.ensureDebugId(wfmStrings.sendSms());
            addSms.setCommand(() -> {
                addSms.closeAll(menuBar);
                new ActivityQuickAddForm(Appointment.SMS, item, null, RelationItem.newEventRelation(getRelationType(), item.getObjectId(), item.getName()));
            });
            bar.addItem(addSms);
        }

        actionItemCount++;
        menuBar.addItem(new MenuPopItem(wfmStrings.send(), "icon-send", bar));

        if (Utils.hasPermission(PermissionConstants.CRM_LEAD_CONVERT)) {
            MenuPopItem convertItem = new MenuPopItem(wfmStrings.convert(), "icon-convert-small", () -> new ConvertLeadView(item, null));
            convertItem.ensureDebugId(wfmStrings.convert());
            actionItemCount++;
            menuBar.addItem(convertItem);
        }


        if (Utils.hasPermission(PermissionConstants.CRM_LEAD_DELETE)) {
            MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "icon-remove", () -> {
                final WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                message.setTitle(wfmStrings.warning());
                message.setMessage(wfmStrings.sureYouWantToDelete() );
                message.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        ArrayList<Integer> ids = new ArrayList<>();
                        ids.add(item.getObjectId());
                        contactService.deleteContacts(ids, null, false, new AbstractAsyncCallback<ArrayList<Integer>>() {
                            public void failure(Throwable caught) {
                                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                            }

                            public void success(ArrayList<Integer> result) {
                                Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.lead()), Info.Type.INFO);
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEADS_DELETE, result, LeadListView.this);
                            }
                        });
                    }
                });
                message.open();
            });
            removeItem.ensureDebugId(wfmStrings.delete());
            menuBar.addItem(removeItem);
        }

        ToolItem toolItem = new ToolItem(actionItemCount);
        toolItem.setWidget(menuBar);
        return toolItem.getAction();
    }

    private void sendInvoiceOrQuote(final ContactListItem item, final boolean isInvoice) {
        final String link = GWT.getHostPageBaseURL() + "Accounting.html#" + (isInvoice ? Constants.SALE_INVOICE : Constants.SALE_QUOTE) + "|add/add/lead/" + item.getObjectId();
        if ("true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP))) {
            if (isInvoice ? (Utils.hasPermission(CRM_SALES_INVOICE_ADD)) : (Utils.hasPermission(CRM_SALES_QUOTE_ADD))) {
                if (item.getCrmAccount().isNew()) {
                    crmService.addAccountToContact(item, true, new AbstractAsyncCallback<ContactListItem>() {
                        @Override
                        public void onFailure(Throwable caught) {
                        }

                        @Override
                        public void onSuccess(ContactListItem result) {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONTACT_AUTO_ACCOUNT_ADDED, result.getCrmAccount().getObjectId(), LeadListView.this);
                            Window.open(link, "_blank", "");
                        }
                    });
                } else {
                    Window.open(link, "_blank", "");
                }
            } else {
                Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
            }
        } else {
            showIsAccountingSetUpMessage();
        }
    }

    private MenuBar getActionsForSelections() {
        if (!(list.getPagingScrollTable().getSelectedRowValues() == null || list.getPagingScrollTable().getSelectedRowValues().size() < 1)) {
            if (actions == null) {
                actions = new ContextMenu();
                actions.getMenuBar().setAutoOpen(false);
                if (Utils.hasPermission(PermissionConstants.CRM_LEAD_STATUS)) {
                    final MenuBar changeStatus = new MenuBar(true);
                    changeStatus.ensureDebugId("leadListChangeStatus");
                    changeStatus.setAutoOpen(true);
                    if (list.getDefaultOne() != null) {
                        for (final SelectItem status : list.getDefaultOne().getLeadStatuses()) {
                            changeStatus.addItem("<span>" + status.getName() + "</span>", true, () -> {
                                actions.hide();
                                if (selectedItems == null || selectedItems.size() == 0) {
                                    list.showSelectOneMessage();
                                } else {
                                    ArrayList<Integer> ids = new ArrayList<>();
                                    for (ContactListItem item : selectedItems) {
                                        ids.add(item.getObjectId());
                                    }
                                    changeStatus(ids, status.getId());
                                }
                            });
                        }
                    }
                    actions.addMenuItemWithMenuBar(wfmStrings.changeStatus(), "", true, changeStatus);
                }
                if (Utils.hasPermission(CHANGE_LEADS_ASSIGNEE)) {
                    actions.addMenuItem(crmStrings.changeAssignee(), true, () -> {
                        actions.hide();
                        assigneePopup.getItemIDs().clear();
                        for (ContactListItem item : selectedItems) {
                            assigneePopup.getItemIDs().add(item.getObjectId());
                        }
                        assigneePopup.open();
                    });
                }
                if (Utils.hasPermission(CHANGE_LEADS_MUlTI_ASSIGNEE)) {
                    actions.addMenuItem(crmStrings.changeMultiAssignee(), true, () -> {
                        actions.hide();
                        multiAssigneePopup.getItemIDs().clear();
                        if (selectedItems.size() > 0) {
                            hasCheckedAllTableItems = list.hasCheckedAllTableItems();
                            crmService.getOwnersListByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE, new AbstractAsyncCallback<SelectItem[]>() {
                                public void failure(Throwable throwable) {
                                }

                                public void success(SelectItem[] items) {
                                    multiAssigneePopup.setDefaultItems(hasCheckedAllTableItems, list.getFilterParametrs());
                                    multiAssigneePopup.setDataItems(totalCount, selectedItems, items);
                                    multiAssigneePopup.open();
                                }
                            });
                        } else {
                            list.showSelectOneMessage();
                        }
                    });
                }
                if (Utils.hasPermission(CRM_ADD_NEW_MAILING_LIST) && Utils.hasPermission(CRM_E_MAIL_MARKETING_TAB)) {
                    actions.addMenuItem(crmStrings.addToMailingList(), null, true, () -> {
                        if (list.getItemCount() > 0) {
                            showEditSubscriptionsShell(null);
                        } else {
                            Info.show(property.getPlural(crmStrings.noLeadsToAddToMailList(), wfmStrings.leads()), Info.Type.WARNING);
                        }
                    });
                }
                if (Utils.hasPermission(CHANGE_LEADS_CAMPAIGN)) {
                    actions.addMenuItem(crmStrings.changeCampaign(), true, () -> {
                        actions.hide();
                        campaignPopup.getItemIDs().clear();
                        if (selectedItems.size() > 0) {
                            for (ContactListItem item : selectedItems) {
                                campaignPopup.getItemIDs().add(item.getObjectId());
                            }
                            campaignPopup.open();
                        } else {
                            list.showSelectOneMessage();
                        }
                    });
                }
                actions.addMenuItem(wfmStrings.detectDuplicates(), true, () -> detectDuplicates());
                if (Utils.hasPermission(PermissionConstants.CRM_LEAD_DELETE)) {
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
                if (Utils.hasPermission(CRM_ADD_NEW_MAILING_LIST) && Utils.hasPermission(CRM_E_MAIL_MARKETING_TAB)) {
                    emptyActions.addMenuItem(wfmStrings.createMailingList(), null, true, () -> {
                        if (totalCount > 0) {
                            ListingFilterParameter filterParam = list.getFilterParametrs() == null ? new ListingFilterParameter() : list.getFilterParametrs();
                            filterParam.setFacetFilterJson(Utils.facetFilterRpcToJsonString(filterParam.getFacetFilter()));
                            filterParam.setListPanelToolJson(Utils.listPanelToolRpcConvertJsonData(filterParam.getListPanelTool()));
                            new MailListPopup(null, null, true, filterParam);
                        } else {
                            Info.show(property.getPlural(crmStrings.noLeadsToAddToMailList(), wfmStrings.leads()), Info.Type.WARNING);
                        }
                    });
                }
                emptyActions.addMenuItem(wfmStrings.selectAnyItemToActivateBatchActions(), null, true, null);
            }
            return emptyActions.getMenuBar();
        }
    }

    private void detectDuplicates(ContactListItem... items) {
        boolean detectDuplicatesByArguments = items != null && items.length > 0;
        if (!detectDuplicatesByArguments && selectedItems.size() == 0) {
            Info.show(wfmMessages.pleaseSelectOneToDetectDuplicates(property.getSingular(wfmStrings.lead())), Info.Type.WARNING);
        } else {
            detectDuplicates = true;
            lastSelectedItems.clear();
            lastSelectedItems.addAll(detectDuplicatesByArguments ? Arrays.asList(items) : selectedItems);
            refresh();
        }
    }

    private void changeStatus(ArrayList<Integer> ids, Integer statusId) {
        LoadingPanel.loading(true);
        crmService.changeLeadStatus(ids, statusId, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Boolean aBoolean) {
                LoadingPanel.loading(false);
                Info.show(property.getSingular(wfmStrings.messSuccessfullyUpdated(), wfmStrings.lead()), Info.Type.INFO);
                list.reloadPage();
            }
        });
    }

    @Override
    protected void deleteContactItem(ArrayList<ContactListItem> items, boolean massDeletion) {
        final WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
        message.setTitle(wfmStrings.warning());
        String messageTitle = wfmStrings.sureYouWantToDelete();
        if (massDeletion) {
            messageTitle = wfmStrings.areYouSureYouWantToDeleteTheSelectedRecords();
        }
        message.setMessage(messageTitle);
        message.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                LoadingPanel.loading(true);
                contactService.deleteContacts(getIDs(selectedItems), null, false, new AbstractAsyncCallback<ArrayList<Integer>>() {
                    public void failure(Throwable caught) {
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }

                    public void success(ArrayList<Integer> result) {
                        LoadingPanel.loading(false);
//                        Info.show(crmStrings.messLeadSucDeleted(), Info.Type.INFO);
                        if (items.size() == 1) {
                            Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.lead()), Info.Type.INFO);
                        } else {
                            Info.show(property.getPlural(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.leads()), Info.Type.INFO);
//                            Info.show(crmMessages.messContactsSucDeleted(crmStrings.leads().toLowerCase()), Info.Type.INFO);
                        }
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEADS_DELETE, result, LeadListView.this);
                    }
                });
            }
        });
        message.open();
    }

    private KanbanDataLoader<ContactListItem> getKanbanDataLoader() {
        return new KanbanDataLoader<ContactListItem>() {
            @Override
            public void loadData(ListingFilterParameter filterParameter, KanbanDataRenderer dataRenderer) {
                LoadingPanel.loading(true);
                crmService.getNewKanbanLeads(filterParameter, dataRenderer.getColumnMetadata(), new AbstractAsyncCallback<ListResult<ContactListItem>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(ListResult<ContactListItem> result) {
                        dataRenderer.setResults(result);
                        LoadingPanel.loading(false);
                    }
                });
            }

            @Override
            public void onDropKanbanItem(Object sourceColumnLayoutData, Object targetColumnLayoutData, Object contactListItem,
                                         Integer widgetIndex, Object prevItem, Object afterItem, KanbanBoard kanbanBoard,
                                         KanbanBoard.OnDropCard onDropCard) {

                if (Utils.isDoubleMessageEnable() && targetColumnLayoutData != sourceColumnLayoutData) {
                    WfmMessageBox changeStatusMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    changeStatusMessageBox.setMessage(crmMessages.doYouWantToChangeStatusTo(((SelectItem) targetColumnLayoutData).getName()));
                    changeStatusMessageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            crmService.changeLeadKanbanOrder((SelectItem) targetColumnLayoutData, (Integer) contactListItem, widgetIndex,
                                    (Integer) prevItem, (Integer) afterItem, new AsyncCallback<Integer>() {
                                        @Override
                                        public void onFailure(Throwable throwable) {
                                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                        }

                                        @Override
                                        public void onSuccess(Integer integer) {
                                            if (onDropCard != null) {
                                                onDropCard.onDropCard();
                                            }
                                        }
                                    });
                        }

                        @Override
                        public void onCancel() {
                            kanbanBoard.reloadColumn(((SelectItem) targetColumnLayoutData).getId());
                            kanbanBoard.reloadColumn(((SelectItem) sourceColumnLayoutData).getId());
                        }
                    });

                    changeStatusMessageBox.setTitle(wfmStrings.warning());
                    changeStatusMessageBox.open();
                } else {
                    crmService.changeLeadKanbanOrder((SelectItem) targetColumnLayoutData, (Integer) contactListItem, widgetIndex,
                            (Integer) prevItem, (Integer) afterItem, new AsyncCallback<Integer>() {
                                @Override
                                public void onFailure(Throwable throwable) {
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void onSuccess(Integer integer) {
                                    if (onDropCard != null) {
                                        onDropCard.onDropCard();
                                    }
                                }
                            });
                }
            }
        };
    }

    private KanbanBoardDesign<ContactListItem> getKanbanBoardDesign() {
        return new KanbanBoardDesign<ContactListItem>() {
            @Override
            public Widget getBoardItem(ContactListItem kanbanItem, KanbanBoard kanbanBoard, Object... obj) {
                //Return card item
                MaterialPanel p = new MaterialPanel();
                if (obj != null && obj.length > 0 && (obj[0] instanceof HashMap)) {
                    HashMap<String, KanbanItemColumnConfigs> strMap = (HashMap) obj[0];
                    p.add(new LeadMaterialCard(kanbanItem, strMap));
                } else {
                    p.add(new LeadMaterialCard(kanbanItem));
                }
                p.setLayoutData(kanbanItem.getObjectId());
                return p;
            }

            @Override
            public void loadDefaultColumns(AbstractAsyncCallback callback) {
                LoadingPanel.loading(true);
                kanbanService.getKanbanDefaultColumns(ReferenceParentEnum._LEAD_STATUS, new AsyncCallback<ArrayList<SelectItem>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        callback.failure(throwable);
                    }

                    @Override
                    public void onSuccess(ArrayList<SelectItem> selectItems) {
                        LoadingPanel.loading(false);
                        callback.success(selectItems);
                    }
                });
            }

            @Override
            public boolean canDnD(ContactListItem kanbanItem) {
                return Utils.hasPermission(PermissionConstants.CRM_LEAD_STATUS, CRM_LEAD_EDIT);
            }
        };
    }

    public String getIconStyle() {
        return "lead lead-list";
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
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setLimit(1);
        initContactList(fp, null, container);
    }

    public void restoreState() {
        if (list == null) {
            return;
        }
        if (list.isListingPage()) {
            super.restoreState();
        } else {
            list.getKanbanBoardView().resetScrollPositions();
        }
    }

    @Override
    public String getPropertyCode() {
        return LEADS;
    }
}
