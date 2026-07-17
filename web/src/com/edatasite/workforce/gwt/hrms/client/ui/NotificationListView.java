package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.NotificationMsgService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.notification.NotificationItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.KpiSelectBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.FigCaption;
import gwt.material.design.client.ui.html.FigureWidget;

/**
 * Created by dilsh0d on 10.07.15.
 */
public class NotificationListView extends BaseListView {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private ListingPanel notificationPanel;
    private KpiSelectBox categoryListBox;
    private KpiCheckBox readCheckBox;

    public NotificationListView() {
        super("notifications");
        setDescription(property.getPlural(wfmStrings.notifications()));
    }

    @Override
    protected Widget onInitialize() {

        notificationPanel = new ListingPanel(ListPanelType.NotificationsListPanel, getColumnType(), getListProvider(), getDesignPanel());
        notificationPanel.hideSearchButton();
        notificationPanel.setOnReset(() -> {
            notificationPanel.getFilterParametrs().setViewType(null);
            notificationPanel.getFilterParametrs().setActive(false);
            notificationPanel.getFilterParametrs().setCategoryID(null);
            if (readCheckBox != null) {
                readCheckBox.setValue(false);
            }
            if (categoryListBox != null) {
                categoryListBox.setSelectedNullLabel();
            }
        });
        add(notificationPanel);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_NOTIFICATION_MSG_LIST_UPDATE, NotificationListView.this, (sender, args) -> notificationPanel.reloadPage());

        return null;
    }

    private ColumnDefinitionConfig[] getColumnType() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[2];

        // Notification icon
        columns[0] = new ColumnDefinitionConfig<NotificationItem, HTMLPanel>(wfmStrings.description(), "fullDescription", 400) {
            @Override
            public HTMLPanel getCellValue(NotificationItem rowValue) {
                return getFullDescriptionHtml(rowValue);
            }
        };
        columns[0].setMinimumColumnWidth(300);


        // Notification icon
        columns[1] = new ColumnDefinitionConfig<NotificationItem, HTMLPanel>(wfmStrings.type(), "type", 140) {
            @Override
            public HTMLPanel getCellValue(NotificationItem rowValue) {
                return getGeneratedDateHtml(rowValue);
            }
        };
        columns[1].setMinimumColumnWidth(100);
        columns[1].setHtml(true);
        columns[1].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        return columns;
    }

    private HTMLPanel getFullDescriptionHtml(final NotificationItem rowValue) {
        HTMLPanel gBox = new HTMLPanel("");
        gBox.addStyleName("group-box group-box--united");
        MaterialPanel groupBoxItems = new MaterialPanel("group-box__items");
        gBox.add(groupBoxItems);

        MaterialPanel imagePanel = new MaterialPanel("group-box__item--split-right");
        //Set Photo
        imagePanel.add(drawImageDiv(rowValue));
        /*Image actorUserPicture = rowValue.getActorUserImg() != null ? new Image(rowValue.getActorUserImg()) : new Image(iconBundle.noPhoto());
        actorUserPicture.getElement().getStyle().setMarginLeft(24, Style.Unit.PX);
        actorUserPicture.getElement().getStyle().setMarginRight(24, Style.Unit.PX);
        actorUserPicture.setSize("30px", "40px");
        imagePanel.add(actorUserPicture);*/

        HTMLPanel divElement = new HTMLPanel("");
        divElement.addStyleName("group-box__item--split-right");
        divElement.getElement().getStyle().setWidth(100, Style.Unit.PCT);
        divElement.getElement().getStyle().setHeight(40, Style.Unit.PX);

        DivElement divAnchorElement = DivElement.as(DOM.createDiv());
        divAnchorElement.getStyle().setMarginTop(5, Style.Unit.PX);
        divAnchorElement.getStyle().setMarginBottom(5, Style.Unit.PX);

        AnchorElement anchorElement = AnchorElement.as(DOM.createAnchor());
        anchorElement.getStyle().setFontSize(12, Style.Unit.PX);
        if (!rowValue.isClicked()) {
            anchorElement.getStyle().setFontWeight(Style.FontWeight.BOLD);
        }
        Element iElement = DOM.createElement("i");
        iElement.setInnerHTML(rowValue.getName());
        anchorElement.appendChild(iElement);
        anchorElement.setHref(rowValue.getActionUrl());

        DOM.sinkEvents(anchorElement, Event.ONCLICK);

        DOM.setEventListener(anchorElement, new EventListener() {
            @Override
            public void onBrowserEvent(Event event) {
                NotificationMsgService.App.get().updateClicked(rowValue.getId(), new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable throwable) {
                        super.failure(throwable);
                    }

                    @Override
                    public void success(Void result) {
                        super.success(result);
                        notificationPanel.reloadPage();
                    }
                });
            }
        });

        divAnchorElement.appendChild(anchorElement);

        DivElement divInfoElement = DivElement.as(DOM.createDiv());
        divInfoElement.getStyle().setFontSize(11, Style.Unit.PX);
        divInfoElement.getStyle().setColor("#515551");


        DivElement divUserNameElement = DivElement.as(DOM.createDiv());
        divUserNameElement.getStyle().setFloat(Style.Float.LEFT);
        divUserNameElement.getStyle().setMarginRight(10, Style.Unit.PX);
        divUserNameElement.setInnerText(rowValue.getActorUserName());

        divInfoElement.appendChild(divUserNameElement);

        DivElement divUserInfoElement = DivElement.as(DOM.createDiv());

        Element divUserInfoIElement = DOM.createElement("i");
        divUserInfoIElement.setInnerText(rowValue.getUserInfo());

        divUserInfoElement.appendChild(divUserInfoIElement);

        divInfoElement.appendChild(divUserInfoElement);

        divElement.getElement().appendChild(divAnchorElement);
        divElement.getElement().appendChild(divInfoElement);

        groupBoxItems.add(imagePanel);
        groupBoxItems.add(divElement);
        return gBox;
    }

    private Div drawImageDiv(NotificationItem notificationItem) {
        Div imgDiv = new Div("updates-row__img");
        imgDiv.getElement().setAttribute("style", "margin: 0 0 0 0%;");
        FigureWidget figure = new FigureWidget();
        figure.addStyleName("img-group img-group--circle");
        imgDiv.add(figure);
        Div imgGrp = new Div("img-group__img");
        if (notificationItem.getActorUserImg() != null && !notificationItem.getActorUserImg().trim().isEmpty()) {
            MaterialImage img = new MaterialImage(notificationItem.getActorUserImg());
            imgGrp.add(img);
            figure.add(imgGrp);
        } else {
            //If no Photo then display initials
            FigCaption figCaption = new FigCaption();
            figCaption.setText(Utils.getFirstTwoLetters(notificationItem.getActorUserName()));

            figure.add(figCaption);
        }
        return imgDiv;
    }

    private HTMLPanel getGeneratedDateHtml(NotificationItem rowValue) {
        String html = "<div style='width=100%;height:40px'>";
        html += ("<div style='margin:5px 0px;'><i style='font-size:12px'>" + rowValue.getModuleName() + "</i></div>");
        html += ("<div style='font-size:11px'>" + DateUtils.formatInternalShort1(rowValue.getDate()) + "</div>");
        return new HTMLPanel(html + "</div>");
    }

    private ListingRequestProvider getListProvider() {
        return (filterParametrs, callback) -> {
            if (!Constants.VIEW_READ.equals(filterParametrs.getViewType())) {
                filterParametrs.setActive(false);
            }
            NotificationMsgService.App.get().getNotificationsList(filterParametrs, new AbstractAsyncCallback<ListResult<NotificationItem>>() {
                @Override
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<NotificationItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    private ListingPanelDesign getDesignPanel() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public Widget getFirstAdditionalPanel() {
                HTML showOnly = new HTML("<span class=customTitle>" + hrmsStrings.showOnly() + "</span>");

                categoryListBox = new KpiSelectBox();
                categoryListBox.setWidth("200px");

                NotificationMsgService.App.get().getCategoriesList(false, new AbstractAsyncCallback<SelectItem[]>() {

                    @Override
                    public void failure(Throwable throwable) {
                        super.failure(throwable);
                    }

                    @Override
                    public void success(SelectItem[] result) {
                        categoryListBox.setItems(result);
                    }
                });

                HorizontalPanel topPanel = new HorizontalPanel();
                topPanel.setWidth("auto");
                topPanel.add(showOnly);
                topPanel.setCellVerticalAlignment(showOnly, HasVerticalAlignment.ALIGN_MIDDLE);
                topPanel.add(new HTML("&nbsp;&nbsp;"));
                topPanel.add(categoryListBox);

                categoryListBox.addChangeHandler(changeEvent -> {
                    notificationPanel.getFilterParametrs().setCategoryID(categoryListBox.getSelectedId());
                    notificationPanel.reloadPage();
                });
                readCheckBox = new KpiCheckBox(hrmsStrings.showRead() + " ", true);
                readCheckBox.addStyleName("listing-inline--space");
                readCheckBox.addValueChangeHandler(event -> {
                    notificationPanel.getFilterParametrs().setActive(event.getValue());
                    notificationPanel.getFilterParametrs().setViewType(Constants.VIEW_READ);
                    notificationPanel.reloadPage();
                });
                topPanel.add(readCheckBox);
                topPanel.setCellVerticalAlignment(readCheckBox, HasVerticalAlignment.ALIGN_MIDDLE);
                return topPanel;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {

            }

            @Override
            public boolean isShowCustomiseButton() {
                return false;
            }

            @Override
            public boolean isShowResetButton() {
                return true;
            }

            @Override
            public String[] oddEvenRowColors() {
                return new String[]{"#ffffff", "#ffffff"};
            }

            @Override
            public String getPageScrollTableId() {
                return "notification-listing";
            }
        };
    }

    @Override
    public String getIconStyle() {
        return "hrms salary-grade-list";
    }

    @Override
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

    public String getPropertyCode() {
        return "notifications";
    }
}
