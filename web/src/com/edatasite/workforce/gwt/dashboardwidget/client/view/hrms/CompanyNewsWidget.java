package com.edatasite.workforce.gwt.dashboardwidget.client.view.hrms;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.news.client.rpc.NewsListItem;
import com.edatasite.workforce.gwt.news.client.rpc.NewsService;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;

import java.util.List;

/**
 * Created by Dilshod Madrahimov on 9/24/15 12:03 AM
 */
public class CompanyNewsWidget extends DashboardBaseWidget {

    Anchor moreLink;

    public CompanyNewsWidget() {
    }

    @Override
    protected void initInternal() {
        moreLink = new Anchor();
        moreLink.setText(wfmStrings.more());
        moreLink.setHref(HRMS_URL + "#" + HRMS_MAIN + "|" + NEWS_LIST);
        moreLink.setStyleName("link-more right");

        setTitle(wfmStrings.companyNews());
//        emptyPanel = createEmptyPanel(wfmStrings.currentlyThereAreNoNews(), Utils.hasPermission(PermissionConstants.HRMS_COMPANY_NEWS_ADD), "news|add/add");

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_NEWS_ADD, CompanyNewsWidget.this, (sender, args) -> loadComponentData());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_NEWS_COMMENTS_ADD, CompanyNewsWidget.this, (sender, args) -> loadComponentData());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_NEWS_DELETE, CompanyNewsWidget.this, (sender, args) -> loadComponentData());
    }


    @Override
    protected void getData() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setLimit(2);
        NewsService.App.get().getNewsList(fp, new AsyncCallback<ListResult<NewsListItem>>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ListResult<NewsListItem> newsListItemListResult) {
                setData(newsListItemListResult.getList());
            }
        });
    }

    @Override
    protected void getSampleData(boolean nodata) {

    }

    @Override
    public String getCode() {
        return DASHBOARD_WIDGET_CODE.NEWS;
    }

    @Override
    protected String getEmptyText() {
        return accountingStrings.currentlyThereAreNoNews();
    }

    private void setData(List<NewsListItem> newsList) {
        clearPanel();
        if (newsList != null && newsList.size() > 0) {
            for (final NewsListItem item : newsList) {
                Element content = DOM.createDiv();
                content.addClassName("wsp-news-item group");
                Element h3 = DOM.createElement("h3");
                AnchorElement subject = Document.get().createAnchorElement();
                //subject.addClassName("news-subject-link");
                subject.setInnerHTML(item.getSubject().length() > 50 ? item.getSubject().substring(0, 50) : item.getSubject());
                subject.setHref(Constants.HRMS_URL + "#news|summary/" + item.getObjectId());
                h3.appendChild(subject);

                Element fullTextElem = DOM.createElement("span");
                String description = "";
                StringBuilder stringBuffer = new StringBuilder();
                if (item.getShortDescription() != null && !"".equals(item.getShortDescription())) {
                    description = item.getShortDescription();
                } else if (item.getFullText() != null && !"".equals(item.getFullText())) {
                    description = item.getFullText();
                }
                HTML descHTML = new HTML(description);
                description = descHTML.getText();
                if (!"".equals(description) && description != null && description.length() > 120) {
                    int n = description.substring(0, 120).lastIndexOf("");
                    stringBuffer.append(description.substring(0, n));
                    stringBuffer.append("...");
                } else {
                    stringBuffer.append(description);
                }
                fullTextElem.setInnerText(stringBuffer.toString());

                content.appendChild(h3);
                content.appendChild(fullTextElem);

                Element footerElem = DOM.createDiv();
                footerElem.addClassName("news-footer small");
                Element dateElem = DOM.createDiv();
                dateElem.addClassName("date left");
                dateElem.setInnerText(DateUtils.getFormat().format(item.getDate()));

                Element commentElem = DOM.createDiv();
                commentElem.addClassName("comments-informer right");
                Element icon = DOM.createAnchor();
                icon.addClassName("icon-comments");
                icon.setAttribute("href", Constants.HRMS_URL + "#news|summary/" + item.getObjectId());
                Element count = DOM.createSpan();
                count.setInnerText(item.getComments() != null ? item.getComments() + "" : "0");

                commentElem.appendChild(icon);
                commentElem.appendChild(count);

                footerElem.appendChild(dateElem);
                footerElem.appendChild(commentElem);
                content.appendChild(footerElem);

                contentPanel.getElement().appendChild(content);
            }
            contentPanel.add(moreLink);
        } else {
            clearPanel();
        }
    }

}
