package com.edatasite.workforce.gwt.core.client.ui.components.paging;


import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.gen2.table.client.PagingOptions;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.ListItem;
import gwt.material.design.client.ui.html.UnorderedList;

/***
 * <div class="paging-group opers-2 operPanel--paging">
 *     <div class="pagingStat__wrapper">
 *         <a class="gwt-uid-106 dropdown-button" style="cursor: pointer;">
 *             <div class="gwt-HTML pagingStat">
 *                 <span>1 - 3 of 3</span>
 *             </div>
 *         </a>
 *         <ul id="gwt-uid-106" class="dropdown-content">
 *             <li style="display: block;">
 *                 <a style="cursor: pointer;">
 *                     <span>Newest</span>
 *                 </a>
 *             </li>
 *             <li style="display: block;">
 *                 <a style="cursor: pointer;">
 *                     <span>Oldest</span>
 *                 </a>
 *             </li>
 *         </ul>
 *     </div>
 *     <ul class="paging">
 *         <li class="paging__prevpage">
 *             <a class="btn btn--white btn--icon" style="cursor: pointer;">
 *                 <i class="ficon--chevron-left material-icons" style="cursor: pointer;"></i>
 *             </a>
 *         </li>
 *         <li class="paging__currentpage">
 *             <input type="text" maxlength="255" class="gwt-TextBox" dir="ltr" style="width: 3em; text-align: right;">
 *         </li>
 *         <li class="paging__nextpage">
 *             <a class="btn btn--white btn--icon" style="cursor: pointer;">
 *                 <i class="ficon--chevron-right material-icons" style="cursor: pointer;"></i>
 *             </a>
 *         </li>
 *         <li>
 *             <div class="errorMessage"></div>
 *         </li>
 *     </ul>
 * </div>
 */

public class PagingWidget extends Div {
    private int step = 0;
    private int limit = 20;
    private Integer totalCount;

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private HTML errorLabel;
    private HTML totalLabel;
    private Image firstImage;
    private MaterialLink firstPage;
    private Image lastImage;
    private MaterialLink lastPage;
    private Image loadingImage;
    private Image nextImage;
    private MaterialLink nextPage;
    private HTML numPagesLabel;
    private Image prevImage;
    private MaterialLink prevPage;
    private TextBox curPageBox = new TextBox();
    private Paging paging;
    private boolean topPosition;

    public PagingWidget() {
        this(false);
    }

    public PagingWidget(boolean topPosition) {
        super("paging-group");
        this.topPosition = topPosition;
        initialize();
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    private void initialize() {
        UnorderedList pList = new UnorderedList();
        pList.setStylePrimaryName("paging");

        //First page
        lastPage = new MaterialLink();
        firstPage = new MaterialLink();
        totalLabel = new HTML();
        totalLabel.addStyleName("pagingStat");

        MaterialLink totalLink = new MaterialLink();
        if (this.topPosition) {
            totalLink.addStyleName("dropdown-split__toggle");
        }
        totalLink.add(totalLabel);

        MaterialDropDown flPages = new MaterialDropDown(totalLink);
        flPages.setBelowOrigin(true);
        totalLink.add(flPages);

        firstPage.setText(wfmStrings.newest());
        lastPage.setText(wfmStrings.oldest());
        flPages.add(firstPage);
        flPages.add(lastPage);

        String className = "pagingStat__wrapper";
        if (this.topPosition) {
            className += " dropdown-split--top";
        }
        MaterialPanel pnlTotalsContainer = new MaterialPanel(className);
        pnlTotalsContainer.add(totalLink);
        add(pnlTotalsContainer);
        //Prev page
        prevPage = new MaterialLink();
        prevPage.setClass("btn btn--white btn--icon");
        MaterialIcon ppIcon = new MaterialIcon();
        ppIcon.setClass("ficon--chevron-left");
        prevPage.add(ppIcon);

        //Next page
        nextPage = new MaterialLink();
        nextPage.setClass("btn btn--white btn--icon");
        MaterialIcon npIcon = new MaterialIcon();
        npIcon.setClass("ficon--chevron-right");
        nextPage.add(npIcon);

        // Create the current page box
        createCurPageBox();

        // Create the page count label
        numPagesLabel = new HTML();

        // Create the loading image
        PagingOptions.PagingOptionsImages images = GWT.<PagingOptions.PagingOptionsImages>create(PagingOptions.PagingOptionsImages.class);
        AbstractImagePrototype.create(images.scrollTableLoading()).createImage();
        loadingImage = AbstractImagePrototype.create(images.scrollTableLoading()).createImage();
        loadingImage.setVisible(false);

        // Create the error label
        errorLabel = new HTML();
        errorLabel.setStylePrimaryName("errorMessage");

        // Add the widgets to the panel
        //pList.add(firstPage);
        ListItem liPrev = new ListItem("paging__prevpage");
        liPrev.add(prevPage);
        pList.add(liPrev);

        ListItem liCurrent = new ListItem("paging__currentpage");
        liCurrent.add(curPageBox);
        pList.add(liCurrent);

        ListItem liNext = new ListItem("paging__nextpage");
        liNext.add(nextPage);
        pList.add(liNext);

        //pList.add(lastPage);
        pList.add(errorLabel);
        add(pList);
        initHandlers();
        setTotal(0, 0, 0);
    }

    private void initHandlers() {
        prevPage.addClickHandler(ch -> {
            if (step > 0) {
                step--;
                setCurrentPageBox();
                int start = step * limit;
                loadData(start + 1, limit);
            }
        });
        nextPage.addClickHandler(ch -> {
            int start = (step + 1) * limit;
            if (start < totalCount) {
                step++;
                setCurrentPageBox();
                loadData(start + 1, limit);
            }
        });
        firstPage.addClickHandler(event -> {
            step = 0;
            setCurrentPageBox();
            loadData((step * limit) + 1, limit);
        });
        lastPage.addClickHandler(event -> {
            if (totalCount != null) {
                step = totalCount / limit;
                setCurrentPageBox();
                reloadData();
            }
        });
    }

    private void setCurrentPageBox() {
        curPageBox.setValue(String.valueOf(step + 1));
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        if (step * limit + limit < totalCount) {
            setTotal((step * limit) + 1, step * limit + limit, totalCount);
        } else {
            setTotal((step * limit) + 1, totalCount, totalCount);
        }
    }

    public void resetAndReload() {
        step = 0;
        setCurrentPageBox();
        reloadData();
    }

    private void reloadData() {
        loadData((step * limit) + 1, limit);
    }

    private void loadData(int start, int limit) { // pay attention start value begins with 1, NOT 0
        if (paging != null) {
            paging.loadData(start, limit);
        }
    }

    private void createCurPageBox() {
        // Setup the widget
        curPageBox.setWidth("3em");
        curPageBox.setText("1");
        curPageBox.setTextAlignment(TextBoxBase.ALIGN_RIGHT);

        // Disallow non-numeric pages
        KeyPressHandler handler = new KeyPressHandler() {
            public void onKeyPress(KeyPressEvent event) {
                char charCode = event.getCharCode();
                if (charCode == KeyCodes.KEY_ENTER) {
                    if (totalCount / limit >= Integer.valueOf(curPageBox.getValue())) {
                        step = Integer.valueOf(curPageBox.getValue()) - 1;
                        reloadData();
                    }
                } else if (!Character.isDigit(charCode)
                        && (charCode != KeyCodes.KEY_TAB)
                        && (charCode != KeyCodes.KEY_BACKSPACE)
                        && (charCode != KeyCodes.KEY_DELETE)
                        && (charCode != KeyCodes.KEY_ENTER)
                        && (charCode != KeyCodes.KEY_HOME)
                        && (charCode != KeyCodes.KEY_END)
                        && (charCode != KeyCodes.KEY_LEFT) && (charCode != KeyCodes.KEY_UP)
                        && (charCode != KeyCodes.KEY_RIGHT)
                        && (charCode != KeyCodes.KEY_DOWN)) {
                    curPageBox.cancelKey();
                }
            }
        };

        // Add the handler
        curPageBox.addKeyPressHandler(handler);
    }

    private int getPagingBoxValue() {
        int page = 0;
        try {
            page = Integer.parseInt(curPageBox.getText()) - 1;
        } catch (NumberFormatException e) {
            // This will catch an empty box
            curPageBox.setText("1");
        }

        // Replace values less than 1
        if (page < 1) {
            curPageBox.setText("1");
            page = 0;
        }

        // Return the 0 based page, not the 1 based visible value
        return page;
    }


    private void setTotal(int from, int to, int of) {
        totalLabel.setText(wfmMessages.fromToOf(from, to, of));
    }

    public int getOffset() {
        return step * limit;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
//        loadData(stage * limit + 1, limit);
    }

    public interface Paging {

        void loadData(int start, int limit);

    }
}
