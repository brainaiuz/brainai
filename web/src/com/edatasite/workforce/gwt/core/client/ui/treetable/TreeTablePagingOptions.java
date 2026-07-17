package com.edatasite.workforce.gwt.core.client.ui.treetable;

import com.edatasite.workforce.gwt.core.client.ui.listpanel.PagingTreeTable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.gen2.table.client.PagingOptions;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 7/26/12
 * Time: 9:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class TreeTablePagingOptions extends Composite {

    /**
     * The default style name.
     */
    public static final String DEFAULT_STYLENAME = "gwt-PagingOptions";
    public static final String STYLENAME_PREFIX = "pagingOptions";

    /**
     * The label used to display errors.
     */
    private HTML errorLabel;

    /**
     * Goto first page button.
     */
    private Image firstImage;

    /**
     * Goto last page button.
     */
    private Image lastImage;

    /**
     * The loading image.
     */
    private Image loadingImage;

    /**
     * Goto next page button.
     */
    private Image nextImage;

    /**
     * The HTML field that contains the number of pages.
     */
    private HTML numPagesLabel;

    /**
     * Goto previous page button.
     */
    private Image prevImage;

    /**
     * The box where the user can select the current page.
     */
    private TextBox curPageBox = new TextBox();

    /**
     * The table being affected.
     */
    private PagingTreeTable<?> table;


    public TreeTablePagingOptions(PagingTreeTable<?> table) {
        this(table, GWT.create(PagingOptions.PagingOptionsImages.class));
    }

    public TreeTablePagingOptions(final PagingTreeTable<?> table, PagingOptions.PagingOptionsImages images) {
        this.table = table;

        // Create the main widget
        HorizontalPanel hPanel = new HorizontalPanel();
        initWidget(hPanel);
        hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        setStyleName(DEFAULT_STYLENAME);

        // Create the paging image buttons
        createPageButtons(images);

        // Create the current page box
        createCurPageBox();

        // Create the page count label
        numPagesLabel = new HTML();

        // Create the loading image
        AbstractImagePrototype.create(images.scrollTableLoading()).createImage();
        loadingImage = AbstractImagePrototype.create(images.scrollTableLoading()).createImage();
        loadingImage.setVisible(false);

        // Create the error label
        errorLabel = new HTML();
        errorLabel.setStylePrimaryName("errorMessage");

        // Add the widgets to the panel
        hPanel.add(createSpacer());
        hPanel.add(firstImage);
        hPanel.add(createSpacer());
        hPanel.add(prevImage);
        hPanel.add(createSpacer());
        hPanel.add(curPageBox);
        hPanel.add(createSpacer());
        hPanel.add(numPagesLabel);
        hPanel.add(createSpacer());
        hPanel.add(nextImage);
        hPanel.add(createSpacer());
        hPanel.add(lastImage);
        hPanel.add(createSpacer());
        hPanel.add(loadingImage);
        hPanel.add(errorLabel);

        // Add handlers to the table

        Command pageLoadListener = () -> {
            loadingImage.setVisible(false);
            errorLabel.setHTML("");
        };
        table.setPageLoadListener(pageLoadListener);

        Command pageChangeListener = () -> {
            curPageBox.setText((table.getCurrentPage() + 1) + "");
            loadingImage.setVisible(true);
            errorLabel.setHTML("");
        };
        table.setPageChangeListener(pageChangeListener);

        Command pagingFailureListener = () -> {
            loadingImage.setVisible(false);
            errorLabel.setHTML("Error");
        };
        table.setPagingFailureListener(pagingFailureListener);

        Command pageCountChangeListener = () -> setPageCount(table.getPageCount());
        table.setPageCountChangeListener(pageCountChangeListener);

        setPageCount(1);
    }

    /**
     * Create a widget that can be used to add space.
     *
     * @return a spacer widget
     */
    protected Widget createSpacer() {
        return new HTML("&nbsp;&nbsp;");
    }

    /**
     * Create a paging image buttons.
     *
     * @param images the images to use
     */
    private void createPageButtons(PagingOptions.PagingOptionsImages images) {
        // Create the images
        firstImage = AbstractImagePrototype.create(images.pagingOptionsFirstPage()).createImage();
        firstImage.addStyleName(STYLENAME_PREFIX + "FirstPage");
        prevImage = AbstractImagePrototype.create(images.pagingOptionsPrevPage()).createImage();
        prevImage.addStyleName(STYLENAME_PREFIX + "PreviousPage");
        nextImage = AbstractImagePrototype.create(images.pagingOptionsNextPage()).createImage();
        nextImage.addStyleName(STYLENAME_PREFIX + "NextPage");
        lastImage = AbstractImagePrototype.create(images.pagingOptionsLastPage()).createImage();
        lastImage.addStyleName(STYLENAME_PREFIX + "LastPage");

        // Create the listener
        ClickHandler handler = event -> {
            Object source = event.getSource();
            if (source == firstImage) {
                table.gotoFirstPage();
            } else if (source == lastImage) {
                table.gotoLastPage();
            } else if (source == nextImage) {
                table.gotoNextPage();
            } else if (source == prevImage) {
                table.gotoPreviousPage();
            }
        };

        // Add the listener to each image
        firstImage.addClickHandler(handler);
        prevImage.addClickHandler(handler);
        nextImage.addClickHandler(handler);
        lastImage.addClickHandler(handler);
    }

    /**
     * Create a box that holds the current page.
     */
    private void createCurPageBox() {
        // Setup the widget
        curPageBox.setWidth("3em");
        curPageBox.setText("1");
        curPageBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

        // Disallow non-numeric pages
        KeyPressHandler handler = event -> {
            char charCode = event.getCharCode();
            if (charCode == KeyCodes.KEY_ENTER) {
                TreeTablePagingOptions.this.table.gotoPage(getPagingBoxValue(), false);
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
        };

        // Add the handler
        curPageBox.addKeyPressHandler(handler);
    }


    /**
     * Get the value of in the page box. If the value is invalid, it will be set
     * to 1 automatically.
     *
     * @return the value in the page box
     */
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

    /**
     * Set the page count.
     *
     * @param pageCount the current page count
     */
    private void setPageCount(int pageCount) {
        if (pageCount < 0) {
            numPagesLabel.setHTML("");
            lastImage.setVisible(false);
        } else {
            numPagesLabel.setHTML("of&nbsp;&nbsp;" + pageCount);
            numPagesLabel.setVisible(true);
            lastImage.setVisible(true);
        }
    }


}
