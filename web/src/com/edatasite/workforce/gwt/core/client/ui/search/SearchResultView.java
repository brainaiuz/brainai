package com.edatasite.workforce.gwt.core.client.ui.search;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.DocumentsSearchItem;
import com.edatasite.workforce.gwt.core.client.rpc.SearchResultItem;
import com.edatasite.workforce.gwt.core.client.rpc.SearchResultItemList;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.search.rpc.ModuleSectionConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.OListElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;

/**
 * User: Abdulaziz
 * Date: Nov 11, 2009
 * Time: 3:26:12 PM
 */
public class SearchResultView extends Widget implements Constants {

    private StringBuilder htmlBuffer = new StringBuilder();
    private Element topElement;
    private Element pager;
    private OListElement olElement;
    private ParagraphElement pstat;
    private String keyword;
    private int sectionName = -1;

    public SearchResultView() {
        sinkEvents(Event.ONCLICK);
        topElement = DOM.createDiv();
        olElement = Document.get().createOLElement();
        olElement.setAttribute("class", "srch-results");
        Element div = DOM.createDiv();
        div.setAttribute("class", "stat-bar");
        pstat = Document.get().createPElement();
        pstat.setAttribute("class", "right");
        div.appendChild(pstat);

        topElement.appendChild(div);
        topElement.appendChild(olElement);

        setElement(topElement);

    }

    public SearchResultView(DocumentsSearchItem searchtItem, SearchResultItemList result) {
        this();
        setSearchResult(searchtItem, result);
    }

    private Search searchProvider;

    public void setSearchProvider(Search searchProvider) {
        this.searchProvider = searchProvider;
    }

    public interface Search {
        void searchKeyword(String keyword);
    }

    public void setSearchResult(DocumentsSearchItem searchtItem, SearchResultItemList result) {

        if (searchtItem.getKeyword().length() < 3) {
            keyword = " " + searchtItem.getKeyword() + " ";
        } else {
            keyword = searchtItem.getKeyword();
        }

        sectionName = searchtItem.getSectionName();

        htmlBuffer = new StringBuilder();
        htmlBuffer.append("<ol class='srch-results'>");
        for (SearchResultItem item : result.getFoundItems()) {
            if (!Utils.isNullOrEmpty(item.getPlainLink())) {
                render(item);
            }
        }
        htmlBuffer.append("</ol>");

        olElement.setInnerHTML(htmlBuffer.toString());

        String inner =
                "<div class='stat-bar'><p>" +
                        "Results <b>" + (searchtItem.getStart() == 0 ? 1 : searchtItem.getStart()) + "</b> - <b>" + (searchtItem.getStart() + result.getFoundItems().length) + "</b> of about <b>" + result.getTotalFound() + "</b> " +
                        " for <b>" + keyword + "</b>.  (<b>" + ((float) result.getQTime()) / 1000 + "</b> seconds)" +
                        "</p></div>";

        if (result.getTotalFound() == 0 && searchtItem.getSectionName() != -1) {
            inner = "<div class='stat-bar'><p>" +
                    "Your search - <b>" + keyword + "</b> - did not match any " + getSectionName(sectionName) +
                    ".</p></div>";
        } else if (searchtItem.getSectionName() == -1) {
            inner = "<div class='stat-bar'><p>" +
                    "<b>Please select section ...</b></p></div>";
        }
        topElement.setInnerHTML(inner + htmlBuffer.toString());

    }

    private String getSectionName(int sectionName) {
        switch (sectionName) {
            case ModuleSectionConstants.TASK: {
                return "Tasks";
            }
            case ModuleSectionConstants.PROJECT: {
                return "Projects";
            }
            case ModuleSectionConstants.CRM_CONTACT: {
                return "CRM Contacts";
            }
            case ModuleSectionConstants.CRM_LEAD: {
                return "Leads";
            }
            case ModuleSectionConstants.CRM_ACCOUNT: {
                return "Accounts";
            }
            case ModuleSectionConstants.CRM_OPPORTUNITY: {
                return "Opportunities";
            }
            case ModuleSectionConstants.CRM_CASE: {
                return "Cases";
            }
            case ModuleSectionConstants.SALE_INVOICE: {
                return "Sales Invoices";
            }
            case ModuleSectionConstants.SALE_QUOTE: {
                return "Sales Quotes";
            }
            case ModuleSectionConstants.PURCHASE_ORDER: {
                return "Purchase Orders";
            }
            case ModuleSectionConstants.NEWS: {
                return "News";
            }
            case ModuleSectionConstants.EVENT: {
                return "Events";
            }
            case ModuleSectionConstants.DOCUMENTS: {
                return "Documents";
            }
        }
        return "Documents";
    }

    public void render(SearchResultItem item) {
        String link = "";
        if (ModuleSectionConstants.TASK == sectionName) {
            if (Utils.isPM()) {
                link = "task|summary/" + item.getEntityID();
                item.setInternal(true);
            } else {
                link = GWT.getHostPageBaseURL() + "ProjectManagement.html?link=" + item.getTitleLink();
            }
        } else if (ModuleSectionConstants.PROJECT == sectionName) {
            if (Utils.isPM()) {
                link = "project|summary/" + item.getEntityID();
                item.setInternal(true);
            } else {
                link = GWT.getHostPageBaseURL() + "ProjectManagement.html?link=" + item.getTitleLink();
            }
        } else if (ModuleSectionConstants.CRM_CONTACT == sectionName) {
            if (Utils.isCRM()) {
                link = "contact|summary/" + item.getEntityID();
                item.setInternal(true);
            } else {
                link = GWT.getHostPageBaseURL() + "Crm.html?link=" + item.getTitleLink();
            }
        } else if (ModuleSectionConstants.CRM_LEAD == sectionName) {
            if (Utils.isCRM()) {
                link = "lead|summary/" + item.getEntityID();
                item.setInternal(true);
            } else {
                link = GWT.getHostPageBaseURL() + "Crm.html?link=" + item.getTitleLink();
            }
        } else if (ModuleSectionConstants.CRM_ACCOUNT == sectionName) {
            if (Utils.isCRM()) {
                link = "account|summary/" + item.getEntityID();
                item.setInternal(true);
            } else {
                link = GWT.getHostPageBaseURL() + "Crm.html?link=" + item.getTitleLink();
            }
        } else if (ModuleSectionConstants.CRM_OPPORTUNITY == sectionName) {
            if (Utils.isCRM()) {
                link = "opportunity|summary/" + item.getEntityID();
                item.setInternal(true);
            } else {
                link = GWT.getHostPageBaseURL() + "Crm.html?link=" + item.getTitleLink();
            }
        } else if (ModuleSectionConstants.CRM_CASE == sectionName) {
            if (Utils.isCRM()) {
                link = "case|summary/" + item.getEntityID();
                item.setInternal(true);
            } else {
                link = GWT.getHostPageBaseURL() + "Crm.html?link=" + item.getTitleLink();
            }
        } else if (ModuleSectionConstants.PURCHASE_ORDER == sectionName) {
            if (Utils.isAccounting()) {
                link = "purchaseorder|summary/" + item.getEntityID();
                item.setInternal(true);
            } else {
                link = GWT.getHostPageBaseURL() + "Accounting.html?link=" + item.getTitleLink();
            }
        } else if (ModuleSectionConstants.SALE_INVOICE == sectionName) {
            if (Utils.isAccounting()) {
                link = "saleinvoice|summary/" + item.getEntityID();
                item.setInternal(true);
            } else {
                link = GWT.getHostPageBaseURL() + "Accounting.html?link=" + item.getTitleLink();
            }
        } else if (ModuleSectionConstants.SALE_QUOTE == sectionName) {
            if (Utils.isAccounting()) {
                link = "salequote|summary/" + item.getEntityID();
                item.setInternal(true);
            } else {
                link = GWT.getHostPageBaseURL() + "Accounting.html?link=" + item.getTitleLink();
            }
        } else if (ModuleSectionConstants.NEWS == sectionName || ModuleSectionConstants.EVENT == sectionName) {
            if (Utils.isHRMS()) {
                link = "news|summary/" + item.getEntityID();
                item.setInternal(true);
            } else {
                link = GWT.getHostPageBaseURL() + "Hrms.html?link=" + item.getTitleLink();
            }
        } else if (ModuleSectionConstants.DOCUMENTS == sectionName) {
                link = item.getPlainLink();
        }

        if (ModuleSectionConstants.DOCUMENTS == sectionName) {
            htmlBuffer.append("<li><a target=\"_blank\" href=\"").
                    append(link).append("\">").append(getCutString(item.getName(), 70)).append("</a><span>&nbsp;&nbsp;&nbsp;Size:").append(item.getSize()).append("</span>");
        } else {
            htmlBuffer.append("<li><a " + (item.getInternal() ? "" : "target=\"_blank\"") + " " + (item.getInternal() ? "id=" + link : "") + " href=\"").
                    append(item.getInternal() ? "#" : link).append("\">").append(getCutString(item.getName(), 70)).append("</a>");
        }

        if (!Utils.isNullOrEmpty(item.getDescription())) {
            htmlBuffer.append("<p>" + getCutString(item.getDescription(), 310) + "</p>");
        }
        String highLigt = getHighlightTitle(item.getHighlits());
        if (!"".equals(highLigt)) {
            htmlBuffer.append(highLigt);
        }
        if (item.getEntityType() != null) {
            htmlBuffer.append("<p class='list-data'><b>Content Type:<i>" + item.getEntityType() + "</i></b></p>");
        }
        if (item.getDateCreated() != null) {
            final String s = "<span class='create-date'>Date Created:<i>" + item.getDateCreated() + "</i></span>";
            htmlBuffer.append("<p>" + s + "</p>");
        }
        htmlBuffer.append("</li>");
    }

    private String getHighlightTitle(HashMap<String, String> highlightMap) {
        StringBuilder highLightString = new StringBuilder();
        if (highlightMap != null && highlightMap.size() != 0) {
            for (String key : highlightMap.keySet()) {
                String title = highlightMap.get(key);
                if (title != null && !"".equals(title)) {
                    highLightString.append("<p class='list-data'><b>").append(key).append(": ").append(title).append("</b></p>");
                }
            }
        }
        return highLightString.toString();
    }

    private String getCutString(String name, int _length) {
        if (name != null && name.length() > _length) {
            return name.substring(0, _length) + " ...".replace("style", "").replace("class", "");
        }
        return name != null ? name.trim() : "";
    }


    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        com.google.gwt.dom.client.Element e = com.google.gwt.dom.client.Element.as(event.getEventTarget());
        String link = e.getAttribute("id");
        if (link != null && !"".equals(link)) {
            View.goTo(link);
        }
    }
}

