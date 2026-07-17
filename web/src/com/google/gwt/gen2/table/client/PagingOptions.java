/*
 * Copyright 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.gen2.table.client;

import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.gen2.table.event.client.*;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.ListItem;
import gwt.material.design.client.ui.html.UnorderedList;

/**
 * A panel that wraps a {@link PagingScrollTable} and includes options to
 * manipulate the page.
 * <p/>
 * <h3>CSS Style Rules</h3>
 * <p/>
 * <ul class="css">
 * <p/>
 * <li>.gwt-PagingOptions { applied to the entire widget }</li>
 * <p/>
 * <li>.gwt-PagingOptions .errorMessage { applied to the error message }</li>
 * <p/>
 * <li>.pagingOptionsFirstPage { the first page button }</li>
 * <p/>
 * <li>.pagingOptionsLastPage { the last page button }</li>
 * <p/>
 * <li>.pagingOptionsNextPage { the next page button }</li>
 * <p/>
 * <li>.pagingOptionsPreviousPage { the previous page button }</li>
 * <p/>
 * </ul>
 */
public class PagingOptions extends Composite {
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
    private MaterialLink firstPage;
    /**
     * Goto last page button.
     */
    private Image lastImage;
    private MaterialLink lastPage;
    /**
     * The loading image.
     */
    private Image loadingImage;
    /**
     * Goto next page button.
     */
    private Image nextImage;
    private MaterialLink nextPage;
    /**
     * The HTML field that contains the number of pages.
     */
    private HTML numPagesLabel;
    /**
     * Goto previous page button.
     */
    private Image prevImage;
    private MaterialLink prevPage;
    /**
     * The box where the user can select the current page.
     */
    private TextBox curPageBox = new TextBox();
    /**
     * The table being affected.
     */
    private PagingScrollTable<?> table;

    /**
     * Constructor.
     *
     * @param table the table being ad
     */
    public PagingOptions(PagingScrollTable<?> table) {
        this(table, GWT.<PagingOptionsImages>create(PagingOptionsImages.class));
    }

    /**
     * Constructor.
     *
     * @param table  the table being affected
     * @param images the images to use
     */
    public PagingOptions(PagingScrollTable<?> table, PagingOptionsImages images) {
        this.table = table;

        UnorderedList pList = new UnorderedList();
        pList.setStylePrimaryName("paging");
        initWidget(pList);

        //First page
        firstPage = new MaterialLink();
//        firstPage.setClass("btn btn--reset btn--icon");
//        MaterialIcon fpIcon = new MaterialIcon();
//        fpIcon.setClass("ficon--chevrons-left");
//        firstPage.add(fpIcon);

        //Prev page
        prevPage = new MaterialLink();
        prevPage.setClass("btn btn--white btn--icon");
        prevPage.add(new SvgIcon(SvgEnum.chevronLeft));

        //Next page
        nextPage = new MaterialLink();
        nextPage.setClass("btn btn--white btn--icon");
        nextPage.add(new SvgIcon(SvgEnum.chevronRight));

        lastPage = new MaterialLink();
//        lastPage.setClass("btn btn--reset btn--icon");
//        MaterialIcon lpIcon = new MaterialIcon();
//        lpIcon.setClass("ficon--chevrons-right");
//        lastPage.add(lpIcon);

        // Create the main widget
        //HorizontalPanel hPanel = new HorizontalPanel();
        //initWidget(hPanel);
        //hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        //setStyleName(DEFAULT_STYLENAME);

        // Create the paging image buttons
        //createPageButtons(images);

        // Create the listener
        ClickHandler handler = new ClickHandler() {
            public void onClick(ClickEvent event) {
                Object source = event.getSource();
                if (source == firstPage) {
                    table.gotoFirstPage();
                } else if (source == lastPage) {
                    table.gotoLastPage();
                } else if (source == nextPage) {
                    table.gotoNextPage();
                } else if (source == prevPage) {
                    table.gotoPreviousPage();
                }
            }
        };

        // Add the listener to each image
        firstPage.addClickHandler(handler);
        prevPage.addClickHandler(handler);
        nextPage.addClickHandler(handler);
        lastPage.addClickHandler(handler);

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
        //pList.add(firstPage);
        ListItem liPrev = new ListItem("paging__prevpage");
        liPrev.add(prevPage);
        liPrev.ensureDebugId("prevpage_id");
        pList.add(liPrev);

        ListItem liCurrent = new ListItem("paging__currentpage");
        liCurrent.add(curPageBox);
        liCurrent.ensureDebugId("current_page_id");
        pList.add(liCurrent);

        ListItem liNext = new ListItem("paging__nextpage");
        liNext.add(nextPage);
        liNext.ensureDebugId("nextPage_id");
        pList.add(liNext);

        //pList.add(lastPage);
        pList.add(errorLabel);

        // Add handlers to the table
        table.addPageLoadHandler(new PageLoadHandler() {
            public void onPageLoad(PageLoadEvent event) {
                loadingImage.setVisible(false);
                errorLabel.setHTML("");
            }
        });
        table.addPageChangeHandler(new PageChangeHandler() {
            public void onPageChange(PageChangeEvent event) {
                curPageBox.setText((event.getNewPage() + 1) + "");
                loadingImage.setVisible(true);
                errorLabel.setHTML("");
            }
        });
        table.addPagingFailureHandler(new PagingFailureHandler() {
            public void onPagingFailure(PagingFailureEvent event) {
                loadingImage.setVisible(false);
                errorLabel.setHTML(event.getException().getMessage());
            }
        });
        table.addPageCountChangeHandler(new PageCountChangeHandler() {
            public void onPageCountChange(PageCountChangeEvent event) {
                setPageCount(event.getNewPageCount());
            }
        });
        setPageCount(table.getPageCount());
    }

    /**
     * @return the {@link PagingScrollTable}.
     */
    public PagingScrollTable<?> getPagingScrollTable() {
        return table;
    }

    public MaterialLink getFirstPage() {
        return firstPage;
    }

    public MaterialLink getLastPage() {
        return lastPage;
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
     * Create a box that holds the current page.
     */
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
                    PagingOptions.this.table.gotoPage(getPagingBoxValue(), false);
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

    /**
     * Create a paging image buttons.
     *
     * @param images the images to use
     */
    private void createPageButtons(PagingOptionsImages images) {
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
        ClickHandler handler = new ClickHandler() {
            public void onClick(ClickEvent event) {
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
            }
        };

        // Add the listener to each image
        firstImage.addClickHandler(handler);
        prevImage.addClickHandler(handler);
        nextImage.addClickHandler(handler);
        lastImage.addClickHandler(handler);
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
            //lastImage.setVisible(false);
            lastPage.setVisible(false);
        } else {
            numPagesLabel.setHTML("of&nbsp;&nbsp;" + pageCount);
            numPagesLabel.setVisible(true);
            //lastImage.setVisible(true);
            lastPage.setVisible(true);
        }
    }

    /**
     * An {@link com.google.gwt.user.client.ui.ImageBundle} that provides images
     * for {@link PagingOptions}.
     */
    public static interface PagingOptionsImages extends ClientBundle {
        /**
         * An image used to navigate to the first page.
         *
         * @return a prototype of this image
         */
        ImageResource pagingOptionsFirstPage();

        /**
         * An image used to navigate to the last page.
         *
         * @return a prototype of this image
         */
        ImageResource pagingOptionsLastPage();

        /**
         * An image used to navigate to the next page.
         *
         * @return a prototype of this image
         */
        ImageResource pagingOptionsNextPage();

        /**
         * An image used to navigate to the previous page.
         *
         * @return a prototype of this image
         */
        ImageResource pagingOptionsPrevPage();

        ImageResource scrollTableLoading();
    }
}
