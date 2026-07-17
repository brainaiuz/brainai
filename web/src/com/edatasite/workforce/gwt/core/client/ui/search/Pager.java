package com.edatasite.workforce.gwt.core.client.ui.search;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Abdulaziz
 * Date: Nov 12, 2009
 * Time: 6:30:55 PM
 */
public class Pager extends Widget {
    private int currentPage;
    private int totalCount;
    private int itemsPerPage;
    private int pagingCount;
    private int start;
    private Element currentSelected;

    public Pager() {
        this(10);
    }

    public Pager(int pagingCount) {
        this.pagingCount = pagingCount;
        Element div = DOM.createDiv();
        div.setAttribute("class", "search-pager");
        Element ul = DOM.createElement("ul");
        ul.setAttribute("class", "sort");
        div.appendChild(ul);
        div.setInnerHTML("<ul class='sort'></ul>");
        setElement(div.getFirstChildElement());
    }

    public void renderPager(int totalCount, int itemsPerPager, int currentPage) {
        this.currentPage = currentPage;
        renderPager(totalCount, itemsPerPager);
    }

    private int getPagerBeginning() {
        return pagingCount >= getTotalPagesCount() ? 1 : ((currentPage - pagingCount) <= 1 ? 1 : (currentPage - pagingCount));
    }

    private int getTotalPagesCount() {
        if (totalCount > itemsPerPage) {
            return (totalCount % itemsPerPage > 0 ? (1 + (1 >= (totalCount / itemsPerPage) ? 1 : (totalCount / itemsPerPage))) : (1 >= (totalCount / itemsPerPage) ? 1 : (totalCount / itemsPerPage)));
        } else {
            return 1;
        }
    }

    private int getPagerEnd() {
        return (currentPage + pagingCount) > getTotalPagesCount() ? getTotalPagesCount() : (currentPage + pagingCount);
    }

    public void renderPager(int totalCount, int itemsPerPage) {
        this.totalCount = totalCount;
        this.itemsPerPage = itemsPerPage;
        getElement().setInnerHTML("");
        if (totalCount != 0 && totalCount > itemsPerPage) {
            Element link;

            for (int j = getPagerBeginning(); j <= getPagerEnd(); j++) {
                link = DOM.createElement("li");
                if (j == currentPage) {
                    link.setInnerHTML("<span>" + j + "</span> ");
                    currentSelected = link;
                } else {
                    link.setInnerHTML(j + "");
                }
                addlistener(link, j, getStartAt(j, itemsPerPage));
                DOM.appendChild(getElement(), link);
            }
        }
    }

    private int getStartAt(int page, int itemsPerPage) {
        return (page - 1) * itemsPerPage;
    }

    public interface PageEvents {
        void onClick(int start, int limit);
    }

    private PageEvents pagerEvents;

    public PageEvents getPagerEvents() {
        return pagerEvents;
    }

    public void setPagerEvents(PageEvents pagerEvents) {
        this.pagerEvents = pagerEvents;
    }

    private void addlistener(final Element elem, final int page, final int startAt) {
        DOM.sinkEvents(elem, Event.ONCLICK);
        DOM.setEventListener(elem, event -> {
            switch (DOM.eventGetType(event)) {
                case Event.ONCLICK: {
                    currentSelected.setInnerHTML(currentPage + "");
                    currentPage = page;
                    currentSelected = elem;
                    start = startAt;
                    currentSelected.setInnerHTML("<font color=\"red\">" + currentPage + "</font>");
                    if (pagerEvents != null) {
                        pagerEvents.onClick(start, itemsPerPage);
                    }
                    Utils.scrollToTop();
                }
                break;
            }
        });
    }
}
