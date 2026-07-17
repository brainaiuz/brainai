package com.edatasite.workforce.gwt.core.client.ui.customtabbar;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;

import java.util.Date;

/**
 * User: ${Dilsh0d}
 * Date: 14.10.2009
 * Time: 22:37:06
 */
public abstract class CustomTabWidget extends SimplePanel {

    private Object object;
    private String tabName;
    private CustomTabBar tabBar;
    private boolean refresh = true;
    private ComeToPosition comeToPosition;
    private SimpleLink[] links;


    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public CustomTabBar getTabBar() {
        return tabBar;
    }

    public void setTabBar(CustomTabBar tabBar) {
        this.tabBar = tabBar;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public CustomTabWidget(String tabName, SimpleLink... links) {
        this.tabName = tabName;
        if (links != null && links.length > 0) {
            this.links = links;
        }
        initData();
    }

    public abstract void initData();

    public abstract void viewShow();

    public void setDataObject(Object object, Integer linkId) {
    }

    public void setPanelSize() {
        DOM.setStyleAttribute(this.getElement(), "width", "100%");
        DOM.setStyleAttribute(this.getElement(), "height", "100%");
    }

    public void setScroll(boolean p) {
        if (p) {
            DOM.setStyleAttribute(this.getElement(), "overflow", "auto");
        }
    }

    public interface ComeToPosition {
        void getTabData(Object object);
    }

    public ComeToPosition getComeToPosition() {
        return comeToPosition;
    }

    public void setComeToPosition(ComeToPosition comeToPosition) {
        this.comeToPosition = comeToPosition;
    }

    public void getEmptyPanel(String message, String textBeforeLink, final String link) {
        clear();
        final VerticalPanel vpanel = new VerticalPanel();
        vpanel.setSize("100%", "100%");
        final HorizontalPanel centerPanel = new HorizontalPanel();
        final HorizontalPanel horz = new HorizontalPanel();
        final HTML noNotes = new HTML(message);
        horz.add(noNotes);

        if (textBeforeLink != null) {
            final SimpleLink mylink = new SimpleLink(textBeforeLink);
            horz.add(mylink);
            mylink.addClickHandler(event -> {
                if (link != null) {
                    SinksContainerFactory.entryPoint.onHistoryChanged(link);
                }
            });
        }

        vpanel.add(horz);
        vpanel.setCellHorizontalAlignment(horz, HasHorizontalAlignment.ALIGN_CENTER);
        vpanel.setCellVerticalAlignment(horz, HasVerticalAlignment.ALIGN_MIDDLE);
        DOM.setStyleAttribute(this.getElement(), "overflow", "hidden");
        add(vpanel);
    }

    public SimpleLink[] getSimpleLinks() {
        return links;
    }

    public void setSimpleLinks(SimpleLink... links) {
        this.links = links;
    }

    public String refactor(String s) {
        if (s != null && !"".equals(s)) {
            return s;
        }
        return "";
    }

    public String refactorDate(Object s) {
        if (s != null && s instanceof Date) {
            return formatDate((Date) s, true);
        }
        if (s != null && s instanceof String) {
            return s.toString();
        }
        return "";
    }

    public String formatDate(Date date, boolean withTime) {
        if (date == null) {
            return "";
        }
        if (withTime) {
            return DateUtils.formatInternal(date);
        }
        return DateUtils.format(date);
    }

}
