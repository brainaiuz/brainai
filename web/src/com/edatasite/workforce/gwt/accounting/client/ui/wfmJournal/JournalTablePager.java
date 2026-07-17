package com.edatasite.workforce.gwt.accounting.client.ui.wfmJournal;

import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Created with IntelliJ IDEA.
 * User: Power
 * Date: 1/29/13
 * Time: 4:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class JournalTablePager extends HorizontalPanel {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final WfmMessages wfmMessages = WfmMessages.App.get();

    private Image ajaxLoader;
    private TextBox pagebox;
    private Label displayitems;
    private Label totalpageslabel;
    private Command pageexecuter;
    private long itemscount;
    private int pagesize;
    private int pageindex;

    public JournalTablePager(Command pageexecuter) {
        addStyleName("journal-pager");
        this.pageexecuter = pageexecuter;
    }

    public void initialize() {
        clear();
        ajaxLoader = new Image("/images/ajax-loader.gif");
        ajaxLoader.setWidth("16px");
        ajaxLoader.setVisible(false);

        Anchor firstpage = new Anchor(wfmStrings.firstPage());
        firstpage.addClickHandler(event -> {
            setPageIndex(0);
            updateBox();
            pageexecuter.execute();
        });
        add(firstpage);

        Anchor prevpage = new Anchor(wfmStrings.prev());
        prevpage.addClickHandler(event -> {
            if (pageindex > 0) {
                pageindex = --pageindex;
            }
            updateBox();
            pageexecuter.execute();
        });
        add(prevpage);


        final int numberofpages = (int) (itemscount % pagesize > 0 ? (itemscount / pagesize) + 1 : itemscount / pagesize);
        pagebox = new TextBox();
        pagebox.setWidth("30px");
        pagebox.setHeight("10px");
        pagebox.setText("" + (pageindex + 1));

        totalpageslabel = new Label();
        totalpageslabel.setText(wfmMessages.ofNumber("" + numberofpages));

        Anchor go = new Anchor(wfmStrings.go());
        go.addClickHandler(event -> {
            String pagenumber = pagebox.getText();
            try {
                pageindex = Integer.parseInt(pagenumber) - 1;
            } catch (Exception e) {
                Window.alert(wfmMessages.invalidValuePleaseEnterNumber());
                return;
            }
            updateBox();
            pageexecuter.execute();
        });

        final ListBox pagesizeitems = new ListBox();
        pagesizeitems.addChangeHandler(changeEvent -> {
            pageindex = 0;
            pagesize = Integer.parseInt(pagesizeitems.getValue(pagesizeitems.getSelectedIndex()));
            updateBox();
            pageexecuter.execute();
        });
        pagesizeitems.addItem("20", "20");
        pagesizeitems.addItem("40", "40");
        pagesizeitems.addItem("50", "50");
        pagesizeitems.addItem("60", "60");
        pagesizeitems.addItem("80", "80");
        pagesizeitems.addItem("100", "100");
        pagesizeitems.addItem("150", "200");
        pagesizeitems.addItem("200", "200");
        pagesizeitems.addItem("250", "250");
        pagesizeitems.addItem("300", "300");
        pagesizeitems.addItem(wfmStrings.all(), "100000");

        HorizontalPanel hrpagepanel = new HorizontalPanel();
        hrpagepanel.add(pagebox);
        hrpagepanel.add(totalpageslabel);
        hrpagepanel.add(go);
        hrpagepanel.add(pagesizeitems);
        add(hrpagepanel);

        Anchor nextpage = new Anchor(wfmStrings.next());
        nextpage.addClickHandler(event -> {
            pageindex++;
            updateBox();
            pageexecuter.execute();
        });
        add(nextpage);

        Anchor lastpage = new Anchor(wfmStrings.lastPage());
        lastpage.addClickHandler(event -> {
            pageindex = numberofpages - 1;
            updateBox();
            pageexecuter.execute();
        });
        add(lastpage);


        Integer first = (pageindex * pagesize) + 1;
        displayitems = new Label();
        displayitems.setText(wfmMessages.displayingItemsFirstOf("" + first, "" + getDisplayLastNumber(), "" + itemscount));
        add(displayitems);
        add(ajaxLoader);

        Anchor refresh = new Anchor(wfmStrings.refresh());
        refresh.addClickHandler(event -> {
            pageindex = 0;
            updateBox();
            pageexecuter.execute();
        });
        add(refresh);
    }

    private void updateBox() {
        pagebox.setText("" + (pageindex + 1));
        ajaxLoader.setVisible(true);
        displayitems.setVisible(false);
        Integer first = (pageindex * pagesize) + 1;
        displayitems.setText(wfmMessages.displayingItemsFirstOf("" + first, "" + getDisplayLastNumber(), "" + itemscount));
        int numberofpages = (int) (itemscount % pagesize > 0 ? (itemscount / pagesize) + 1 : itemscount / pagesize);
        totalpageslabel.setText(" of " + numberofpages);
    }

    public void restoreBox() {
        displayitems.setVisible(true);
        ajaxLoader.setVisible(false);
    }

    private long getDisplayLastNumber() {
        int last = (pageindex * pagesize) + pagesize;
        return last > itemscount ? itemscount : last;
    }

    private boolean isEnableGoToFirstPage() {
        return (pageindex > 0);
    }

    private boolean isEnableGoToLastPage() {
        return ((long) (pageindex + 1) * pagesize < itemscount);
    }

    private boolean isEnablePrevPage() {
        return isEnableGoToFirstPage();
    }

    private boolean isEnableNextPage() {
        return isEnableGoToLastPage();
    }


    public void setItemsCount(long itemscount) {
        this.itemscount = itemscount;
    }

    public long getItemsCount() {
        return this.itemscount;
    }

    public void setPageSize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getPageSize() {
        return this.pagesize;
    }

    public void setPageIndex(int pageindex) {
        this.pageindex = pageindex;
    }

    public int getPageIndex() {
        return this.pageindex;
    }

    public void setVisibleLoader(boolean visible) {
        this.ajaxLoader.setVisible(visible);
    }


}
