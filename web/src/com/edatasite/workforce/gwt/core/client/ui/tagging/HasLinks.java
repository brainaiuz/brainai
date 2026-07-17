package com.edatasite.workforce.gwt.core.client.ui.tagging;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.AddTaggingView;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.addLinkSideNavBox.AddLinkSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.customtabbar.CustomTabWidget;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.HasDirection;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.impl.ClippedImagePrototype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Hayot
 * Date: 8/22/11
 * Time: 12:20 PM
 */
public abstract class HasLinks {
    private AddTaggingView taggingView;
    private AddLinkSideNavBox addLinkSideNavBox;
    private FlowPanel linksPanel;
    private CustomTabWidget summaryTab;

    private static final WfmStrings wfmStrings = WfmStrings.App.get();


    protected HasLinks(View view) {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ADD_RELATION, view, (sender, args) -> drawLinks());
    }

    public void drawLinks() {
        if (isActionEditing() || hasNoSummaryTab()) {
            getLinksPanel().clear();
            if (getTaggingView().getSelectedRelations().size() > 0) {
                getLinksPanel().add(AddTaggingView.drawRelationTags(taggingView));
            }
        } else if (summaryTab != null) {
            summaryTab.viewShow();
        }
    }

    public boolean hasNoSummaryTab() {
        return false;
    }

    protected abstract boolean isActionEditing();

    public AddTaggingView getTaggingView() {
        if (taggingView == null) {
            taggingView = new AddTaggingView(getRelationID(), getRelationType(), getRelationName(), wfmStrings.addLinks(), isActionEditing());
        }
        return taggingView;
    }

    public AddLinkSideNavBox getAddLinkSideNavBox() {
        if (addLinkSideNavBox == null) {
            addLinkSideNavBox = new AddLinkSideNavBox(getRelationID(), getRelationType(), getRelationName(), isActionEditing());
        }
        return addLinkSideNavBox;
    }

    public AddLinkSideNavBox getAddLinkSideNavBoxSecondWay() {
        return addLinkSideNavBox;
    }

    public ClickHandler getAddHandler() {
        return clickEvent -> {
            if (taggingView != null && !taggingView.isShowing()) {
                taggingView.center();
            }
        };
    }

    public SimpleLink getAddLink() {
        return AddTaggingView.getAddLink(getTaggingView(), wfmStrings.addLinks(), getRelationType(), getRelationID());
    }

    public MenuItem getAddLinkMenuItem() {
        AbstractImagePrototype image = new ClippedImagePrototype("/mainStyles/icons/new-pm-icons/add-icon.png", 0, 0, 20, 20);
        MenuItem item = new MenuItem(Utils.getButtonText(image.getHTML(), wfmStrings.addLinks(), 5), true, (Command) () -> {
            if (getTaggingView() != null && !getTaggingView().isShowing()) {
                getTaggingView().center();
            }
        });
        item.setStyleName("menu-bar");
        return item;
    }

    public FlowPanel getLinksPanel() {
        if (linksPanel == null) {
            linksPanel = new FlowPanel();
        }
        return linksPanel;
    }

    //this is for Summary/Detailed Views not edit or add views
    public CustomTabWidget getSummaryTab(ArrayList<RelationItem> existingLinks) {
        if (summaryTab == null) {
            initializeSummaryTab();
            getTaggingView().setSelectedRelations(existingLinks);
        }
        return summaryTab;
    }

    private void initializeSummaryTab() {
        summaryTab = new CustomTabWidget(wfmStrings.links(), AddTaggingView.getAddLink(getTaggingView(), wfmStrings.addLinks(), getRelationType(), getRelationID())) {
            @Override
            public void initData() {
                drawLinks();
            }

            @Override
            public void viewShow() {
                summaryTab.getTabBar().insertLinks(summaryTab.getSimpleLinks());
                clear();
                if (getTaggingView().getSelectedRelations().size() == 0) {
                    summaryTab.getEmptyPanel(wfmStrings.thereAreNoRelatedLinksYet(), null, null);
                } else {
                    add(AddTaggingView.drawRelationTags(getTaggingView()));
                }
            }
        };
    }

    public void showAddingPanel() {
        if (getTaggingView() != null && !getTaggingView().isShowing()) {
            getTaggingView().center();
        }
    }

    public void showRelationLinks() {
        if (summaryTab.getSimpleLinks() != null) {
            summaryTab.getSimpleLinks();
            for (SimpleLink link : summaryTab.getSimpleLinks()) {
                link.setVisible(true);
            }
        }
    }

    public void hideRelationLinks() {
        if (summaryTab.getSimpleLinks() != null) {
            summaryTab.getSimpleLinks();
            for (SimpleLink link : summaryTab.getSimpleLinks()) {
                link.setVisible(false);
            }
        }
    }

    public VerticalPanel getLinkAndLinksPanelInVerticalPanel() {
        VerticalPanel vp = new VerticalPanel();
        vp.add(getAddLink());
        vp.add(getLinksPanel());
        return vp;
    }

    protected abstract Integer getRelationID();

    protected abstract String getRelationType();

    protected abstract String getRelationName();

    public static String getTypeAsReadable(String relationType) {
        if (relationType != null) {
            if (RelationItem.TYPE_PROJECT.equals(relationType)) {
                return Property.get(Constants.PROJECT, wfmStrings.project());
            }
            if (RelationItem.TYPE_TASK.equals(relationType)) {
                return wfmStrings.task();
            }
            if (RelationItem.TYPE_CAMPAIGN.equals(relationType)) {
                return wfmStrings.campaigns();
            }
            if (RelationItem.TYPE_ISSUE.equals(relationType)) {
                return wfmStrings.issue();
            }
            if (RelationItem.TYPE_EVENT.equals(relationType)) {
                return Property.getPluralWithObjectCode(Constants.EVENT_LIST, wfmStrings.events());
            }
            if (RelationItem.TYPE_CONTACT.equals(relationType)) {
                return wfmStrings.contacts();
            }
            if (RelationItem.TYPE_LEAD.equals(relationType)) {
                return Property.get(Constants.LEADS, wfmStrings.lead());
            }
            if (RelationItem.TYPE_CRM_ACCOUNT.equals(relationType)) {
                return wfmStrings.accounts();
            }
            if (RelationItem.TYPE_CLIENT.equals(relationType)) {
                return wfmStrings.customers();
            }
            if (RelationItem.TYPE_SUPPLIER.equals(relationType)) {
                return Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier());
            }
            if (RelationItem.TYPE_EMPLOYEE.equals(relationType)) {
                return wfmStrings.employee();
            }
            if (RelationItem.TYPE_DEPARTMENT.equals(relationType)) {
                return Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department());
            }
            if (RelationItem.TYPE_OPPORTUNITY.equals(relationType)) {
                return wfmStrings.opportunities();
            }
            if (RelationItem.TYPE_CASE.equals(relationType)) {
                return wfmStrings.cases();
            }
            if (RelationItem.TYPE_EMAIL_TRACKER.equals(relationType)) {
                return wfmStrings.email();
            }
            if (RelationItem.TYPE_SALEQUOTE.equals(relationType)) {
                return wfmStrings.salesQuotes();
            }
            if (RelationItem.TYPE_PRODUCT.equals(relationType)) {
                return wfmStrings.productsOrServices();
            }
            if (RelationItem.TYPE_PURCHASE_ORDER.equals(relationType)) {
                return wfmStrings.purchaseOrders();
            }
        }
        return "N/A";
    }

    public static ArrayList<ArrayList<Widget>> relationToListingPopup(HashMap<Integer, ArrayList<RelationItem>> result, ArrayList<Integer> sortedIDs) {
        ArrayList<ArrayList<Widget>> widgets = new ArrayList<>();
        if (sortedIDs != null && sortedIDs.size() > 0) {
            for (Integer itemID : sortedIDs) {
                ArrayList<Widget> taskWidgets = new ArrayList<>();
                widgets.add(taskWidgets);
                if (result.get(itemID) != null && result.get(itemID).size() > 0) {
                    FlexTable table = new FlexTable();
                    table.setWidth("100%");
                    table.setWidget(0, 0, new HTML("<b class=\"customTitle\">" + wfmStrings.relatedTo() + "</b>"));
                    table.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
                    table.getCellFormatter().setWidth(0, 0, "100%");
                    table.getFlexCellFormatter().setColSpan(0, 0, 2);
                    Map<String, ArrayList<SelectItem>> map = new HashMap<>();
                    for (RelationItem relationItem : result.get(itemID)) {
                        Integer relationID;
                        String relationName;
                        String relationType;
                        if (!relationItem.isFromLinkage(RelationItem.TYPE_TASK, itemID)) {
                            relationID = relationItem.getFromID();
                            relationName = relationItem.getFromName();
                            relationType = relationItem.getFromType();
                        } else {
                            relationID = relationItem.getToID();
                            relationName = relationItem.getToName();
                            relationType = relationItem.getToType();
                        }
                        if (!map.containsKey(relationType)) {
                            map.put(relationType, new ArrayList<>());
                        }
                        map.get(relationType).add(new SelectItem(relationID, relationName, relationType));
                    }
                    if (!map.isEmpty()) {
                        int row = 1;
                        table.getColumnFormatter().setWidth(0, "120px");
                        table.getColumnFormatter().setWidth(1, "230px");
                        for (Map.Entry<String, ArrayList<SelectItem>> entry : map.entrySet()) {
                            table.getCellFormatter().setWordWrap(row, 0, false);
                            if (entry.getValue() != null && entry.getValue().size() > 0) {
                                table.setWidget(row, 0, new HTML("<span class=\"customTitle\">" + getTypeAsReadable(entry.getKey()) + ":</span>"));
                                table.getCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.HorizontalAlignmentConstant.startOf(HasDirection.Direction.DEFAULT));
                                StringBuilder buffer = new StringBuilder();
                                String delimitr = "";
                                for (SelectItem relation : entry.getValue()) {
                                    buffer.append(delimitr);
                                    buffer.append(relation.getName());
                                    delimitr = ",";
                                }
                                table.setWidget(row, 1, new HTML(buffer.toString()));
                                table.getCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.HorizontalAlignmentConstant.startOf(HasDirection.Direction.DEFAULT));
                                row++;
                            }
                        }
                    }
                    taskWidgets.add(table);
                }
            }
        }
        return widgets;
    }
}
