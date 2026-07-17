package com.edatasite.workforce.gwt.dashboardwidget.client.view;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.LoadingWidgets;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.localization.CrmMessages;
import com.edatasite.workforce.gwt.crm.client.ui.PhonePopup;
import com.edatasite.workforce.gwt.crm.client.ui.view.quickadd.CrmQuickAdd;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardContactItem;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialCollapsible;
import gwt.material.design.client.ui.MaterialCollapsibleBody;
import gwt.material.design.client.ui.MaterialCollapsibleHeader;
import gwt.material.design.client.ui.MaterialCollapsibleItem;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTooltip;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.util.List;

/**
 * User: Abror Abdukadirov
 * Date: 02.05.2018 15:24
 */
public class DashboardMyContactsComponent extends DashboardBaseWidget {

    private MaterialPanel abcFilter;
    private MaterialPanel content;
    private MaterialPanel footer;
    private Span loadingbar;
    private Div detailPanel = new Div();
    private DataListBox categoryListBox;
    private MaterialLink selectedLink = new MaterialLink();
    private final DashboardContactItem me = new DashboardContactItem();
    private Div meRow;

    private String searchKey;
    private boolean isLetterSearch;
    private Integer totalCount = 0;
    private Integer listCount = 0;
    private Integer start = 0;

    public DashboardMyContactsComponent(DashboardComponentItem gridItemConfig) {
        this.gridItemConfig = gridItemConfig;
    }

    @Override
    protected void initInternal() {
        mainPanel.addStyleName("widget-contacts");
        MaterialPanel header = new MaterialPanel("widget-row widget-finder");
        content = new MaterialPanel("widget-content");

       /* if (gridItemConfig != null && gridItemConfig.getName() != null) {
            setTitle(gridItemConfig.getName());
        } else {
        }*/
        setTitle(Property.getPluralWithObjectCodeWithReplace(Constants.Contacts, wfmStrings.myContacts(), wfmStrings.contacts()));

        categoryListBox = new DataListBox();
        if (!enableToShowSample) {
            categoryListBox.addValueChangeHandler(event -> {
                selectedLink.removeStyleName("cp_abc__letter--selected");
                searchKey = null;
                isLetterSearch = false;

                resetPaging();
                getMyContacts(true);
            });
            filterPanel.add(categoryListBox);
        }

        Div actionDiv = new Div("widget-heading__action");
        WfmButton2 addContact = new WfmButton2(null, "btn--circle btn--success", "ficon--plus");
        addContact.getElement().setAttribute("style", "align-self:center;min-width:1.92308rem !important;height:1.92308rem");
        addContact.removeHasiconLeftStyle();
        addContact.addClickHandler(clickEvent -> new CrmQuickAdd(LayoutRPC.CONTACT_FORM));
        new MaterialTooltip(addContact, Property.get(Constants.Contacts, wfmStrings.addMess(), wfmStrings.contact()));
        Div iconDiv = new Div("widget-row__icon");
        iconDiv.setHeight("100%");
        iconDiv.add(addContact);
        actionDiv.add(iconDiv);
        actionPanel.add(actionDiv);

        Div nameDiv = new Div("widget-finder-search");
        TextBox nameBox = new TextBox();
        nameBox.setStyleName("form-control");
        nameBox.setPlaceHolder(Property.get(Constants.Contacts, wfmStrings.searchContact(), wfmStrings.contact()));
        if (!enableToShowSample) {
            nameBox.addKeyPressHandler(keyPressEvent -> {
                if (keyPressEvent.getNativeEvent().getKeyCode() == (char) KeyCodes.KEY_ENTER) {
                    selectedLink.removeStyleName("cp_abc__letter--selected");
                    if (nameBox.getText() != null && !nameBox.getText().isEmpty()) {
                        searchKey = nameBox.getText();
                        isLetterSearch = false;

                        resetPaging();
                        getMyContacts(true);
                    }
                }
            });
        }
        nameDiv.add(nameBox);
        header.add(nameDiv);

        Div endDiv = new Div("widget-row__end");
        MaterialLink searchLink = new MaterialLink();
        searchLink.addClickHandler(event -> {
            selectedLink.removeStyleName("cp_abc__letter--selected");
            if (nameBox.getText() != null && !nameBox.getText().isEmpty()) {
                searchKey = nameBox.getText();
                isLetterSearch = false;

                resetPaging();
                getMyContacts(true);
            }
        });
        searchLink.setStyleName("widget-finder-search__button");
        Icon searchIcon = new Icon();
        searchIcon.setStyleName("ficon--search");
        searchLink.add(searchIcon);
        endDiv.add(searchLink);
        header.add(endDiv);

        abcFilter = drawAbcFilterPanel();

        footer = new MaterialPanel("widget-footer");
        loadingbar = new Span();
        loadingbar.setStyleName("blue widget-loading--svg widget-loading");
        loadingbar.setVisible(false);

        WfmButton2 moreButton = new WfmButton2(null, "btn btn-lg btn-block text-center");
        moreButton.getElement().setInnerText(wfmStrings.loadMore());
        moreButton.addClickHandler(clickEvent -> {
            this.start = start + 10;
            getMyContacts(false);
        });
        footer.add(loadingbar);
        footer.add(moreButton);

        contentPanel.add(header);
        contentPanel.add(abcFilter);
        contentPanel.add(content);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CONTACT_ADD, DashboardMyContactsComponent.this, (sender, args) -> loadComponentData());
    }

    @Override
    protected void getData() {
        clearPanelAndFilters();

        LoadingWidgets.get(getCode()).show();
        DashboardWidgetService.App.get().getContactData(new AbstractAsyncCallback<DashboardContactItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingWidgets.get(getCode()).hide();
            }

            @Override
            public void onSuccess(DashboardContactItem result) {
                LoadingWidgets.get(getCode()).hide();
                if (result != null) {
                    if (result.getObjectId() != null) {
                        me.setObjectId(result.getObjectId());
                        me.setFirstName(result.getFirstName());
                        me.setLastName(result.getLastName());
                        me.setCompanyName(result.getCompanyName());
                        me.setImageUrl(result.getImageUrl());

                        meRow = drawRow(me, true);
                        meRow.addStyleName("widget-row-expandable--me");
                    }

                    if (result.getCategories() != null && result.getCategories().length > 0) {
                        categoryListBox.setItems(result.getCategories());
                    }
                }
                getMyContacts(false);
            }
        });
    }

    @Override
    protected void getSampleData(boolean nodata) {
        clearPanelAndFilters();

        DashboardContactItem item = new DashboardContactItem();
        item.setFirstName("John");
        item.setLastName("Smith");
        item.setCompanyName("Deepmind Software Development");
        content.add(drawRow(item, false));

        item = new DashboardContactItem();
        item.setFirstName("Helena");
        item.setLastName("Johnson");
        item.setCompanyName("Global Air Services");
        content.add(drawRow(item, false));

        item = new DashboardContactItem();
        item.setFirstName("Monica");
        item.setLastName("Sandres");
        item.setCompanyName("Global Air Services");
        content.add(drawRow(item, false));

        item = new DashboardContactItem();
        item.setFirstName("Chris");
        item.setLastName("Schroeder");
        item.setCompanyName("Deimos Constructions");
        content.add(drawRow(item, false));

        item = new DashboardContactItem();
        item.setFirstName("Vincent");
        item.setLastName("Ricci");
        item.setCompanyName("Gemini security update procedure");
        content.add(drawRow(item, false));

        item = new DashboardContactItem();
        item.setFirstName("John");
        item.setLastName("Smith");
        item.setCompanyName("Global Air Services");
        content.add(drawRow(item, false));

        item = new DashboardContactItem();
        item.setFirstName("Chris");
        item.setLastName("Schroeder");
        item.setCompanyName("Deepmind Software Development");
        content.add(drawRow(item, false));
    }

    private void getMyContacts(boolean hasClearContent) {
        loadingbar.setVisible(true);
        if (totalCount < (listCount + start)) {
            LoadingWidgets.get(getCode()).show();
        }
        ListingFilterParameter fp = new ListingFilterParameter();
        if (categoryListBox.getSelectedItem() != null) {
            FacetFilterRpc contactFacetFilter = new FacetFilterRpc();
            contactFacetFilter.setType(ListPanelType.ContactListPanel);
            FacetContentRpc categoryFacet = new FacetContentRpc();
            categoryFacet.setFacetItems(new SelectItem[]{categoryListBox.getSelectedItem()});
            contactFacetFilter.getFacetContentMap().put(FacetContentType.ContactFacetFilter.getContentCode()[5], categoryFacet);
            fp.setFacetFilter(contactFacetFilter);
        }
        fp.setFavourite(true);
        fp.setContactID(me.getObjectId());
        if (searchKey != null) {
            fp.setLookUp(true);
            fp.setSearchKey(searchKey);
            if (!isLetterSearch) {
                fp.setLookUp(false);
                fp.setWidgetSearch(true);
            }
            fp.setLetterSearch(isLetterSearch);
        }
        ListLoadConfig config = new ListLoadConfig();
        config.setStart(start);
        config.setLimit(10);
        DashboardWidgetService.App.get().getMyContacts(fp, config, new AbstractAsyncCallback<ListResult<DashboardContactItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingWidgets.get(getCode()).hide();
            }

            @Override
            public void onSuccess(ListResult<DashboardContactItem> result) {
                LoadingWidgets.get(getCode()).hide();
                totalCount = result.getTotal();
                listCount = result.getList().size();
                if (hasClearContent) {
                    clearPanel();
                }
                setData(result.getList(), searchKey != null);
            }
        });
    }

    private void setData(List<DashboardContactItem> result, boolean hasSearch) {
        if (!hasSearch && meRow != null && meRow.getParent() == null) {
            content.add(meRow);
        }
        if (!result.isEmpty()) {
            for (DashboardContactItem item : result) {
                content.add(drawRow(item, false));
            }
        }
        loadingbar.setVisible(false);
        if (totalCount > (listCount + start)) {
            mainPanel.add(footer);
        } else {
            mainPanel.remove(footer);
        }
    }

    private Div drawRow(DashboardContactItem item, boolean isMyContact) {
        Div rowDiv = new Div("widget-row-expandable");

        Div favouriteDiv = new Div("widget-row");
        if (item.isFavourited()) {
            favouriteDiv.addStyleName("widget-row--favourite");
        }
        Div iconDiv = new Div("widget-row__icon");
        if (isMyContact) {
            Span myContactSpan = new Span(wfmStrings.me());
            iconDiv.add(myContactSpan);
        } else {
            Icon icon = new Icon();
            icon.setStyleName("ficon--star");
            icon.addClickHandler(event -> {
                updateContactFavourite(favouriteDiv, item.getObjectId(), !favouriteDiv.getStyleName().contains("widget-row--favourite"));
            });
            iconDiv.add(icon);
        }
        favouriteDiv.add(iconDiv);

        Div itemDiv = new Div("widget-row__item widget-row__item--grow");
        itemDiv.add(drawProfilePanel(item));
        favouriteDiv.add(itemDiv);

        if (!isMyContact) {
            Div endDiv = new Div("widget-row__end");
            endDiv.add(drawButtonGroupPanel(item, rowDiv));
            favouriteDiv.add(endDiv);
        }

        rowDiv.add(favouriteDiv);

        return rowDiv;
    }

    private void updateContactFavourite(Div rowDiv, Integer contactId, boolean isFavourited) {
        LoadingWidgets.get(getCode()).show();
        DashboardWidgetService.App.get().updateContactFavourite(contactId, isFavourited, new AbstractAsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingWidgets.get(getCode()).hide();
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Void result) {
                LoadingWidgets.get(getCode()).hide();
                if (isFavourited) {
                    rowDiv.addStyleName("widget-row--favourite");
                } else {
                    rowDiv.removeStyleName("widget-row--favourite");
                }
            }
        });
    }

    private Div drawProfilePanel(DashboardContactItem item) {
        Div mainPanel = new Div("cp_profile-min");
        Div picPanel = new Div("cp_profile-min__pic");
        Div titlePanel = new Div("cp_profile-min__title");

        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Image image = new Image(item.getImageUrl());
            image.setStyleName("cp_profile-min__img");
            picPanel.add(image);
        } else {
            Div imgDiv = new Div("cp_profile-min__inits");
            imgDiv.getElement().setInnerText(getImageFormat(item.getFirstName(), item.getLastName()));
            picPanel.add(imgDiv);
        }
        Div nameDiv = new Div("cp_profile-min__name");
        nameDiv.getElement().setInnerText(item.getFirstName() + " " + item.getLastName());
        titlePanel.add(nameDiv);

        Div companyDiv = new Div("cp_profile-min__company");
        String companyName = item.getCompanyName() != null ? item.getCompanyName() : wfmStrings.notAvailable();
        companyDiv.getElement().setInnerText(companyName);
        titlePanel.add(companyDiv);
        titlePanel.addClickHandler(event -> {
            if (Utils.isCRM()) {
                Utils.redirect(GWT.getHostPageBaseURL() + "Crm.html#contact|summary/"
                        + item.getObjectId() + "//"
                        + (item.getCrmAccountId() != null ? item.getCrmAccountId() : ""));
            } else {
                Utils.openURL(GWT.getHostPageBaseURL() + "Crm.html#contact|summary/"
                        + item.getObjectId() + "//"
                        + (item.getCrmAccountId() != null ? item.getCrmAccountId() : ""));
            }
        });

        mainPanel.add(picPanel);
        mainPanel.add(titlePanel);
        return mainPanel;
    }

    private Div drawButtonGroupPanel(DashboardContactItem item, Div rowDiv) {
        Div mainPanel = new Div("widget-row__button-group");

        final boolean[] isActive = {false};
        Div infoDiv = new Div("widget-row__button widget-row__button--active");
        Span infoSpan = new Span();
        Icon infoIcon = new Icon();
        infoIcon.setStyleName("ficon--info2");
        infoIcon.addClickHandler(event -> {
            if (isActive[0] && rowDiv.getWidgetCount() > 1) {
                if (detailPanel.getParent() != null) {
                    detailPanel.getParent().removeStyleName("widget-row-expandable--active");
                    detailPanel.removeFromParent();
                }
                isActive[0] = false;
            } else {
                if (detailPanel.getParent() != null) {
                    detailPanel.getParent().removeStyleName("widget-row-expandable--active");
                    detailPanel.removeFromParent();
                }
                LoadingWidgets.get(getCode()).show();
                DashboardWidgetService.App.get().getMyContactDetails(item.getObjectId(), new AbstractAsyncCallback<DashboardContactItem>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        LoadingWidgets.get(getCode()).hide();
                    }

                    @Override
                    public void onSuccess(DashboardContactItem result) {
                        LoadingWidgets.get(getCode()).hide();
                        result.setFirstName(item.getFirstName());
                        result.setLastName(item.getLastName());
                        result.setPrimaryPhone(item.getPrimaryPhone());
                        result.setPrimaryEmail(item.getPrimaryEmail());
                        result.setCompanyName(item.getCompanyName());
                        isActive[0] = true;
                        detailPanel = drawDetailPanel(result);
                        rowDiv.add(detailPanel);
                        rowDiv.addStyleName("widget-row-expandable--active");
                    }
                });
            }
        });
        new MaterialTooltip(infoIcon, Property.get(Constants.Contacts, wfmStrings.contactDetails(), wfmStrings.contact()));
        infoSpan.add(infoIcon);
        infoDiv.add(infoSpan);
        mainPanel.add(infoDiv);

        Div dateDiv = new Div("widget-row__button");
        MaterialLink dateLink = new MaterialLink();
        dateLink.addClickHandler(event -> {
            Utils.fireRelationEvent(RelationItem.TYPE_EVENT, RelationItem.TYPE_CONTACT, item.getObjectId());
            new ActivityQuickAddForm(Appointment.EVENT, RelationItem.newEventRelation(RelationItem.TYPE_CONTACT,
                    item.getObjectId(),
                    item.getContactName()));
        });
        Icon dateIcon = new Icon();
        dateIcon.setStyleName("ficon--calendar2");
        dateLink.add(dateIcon);
        new MaterialTooltip(dateLink, Property.get(Constants.EVENT_LIST, wfmStrings.addMess(), wfmStrings.event()));
        dateDiv.add(dateLink);
        mainPanel.add(dateDiv);

        Div emailDiv = new Div("widget-row__button");
        MaterialLink emailLink = new MaterialLink();
        Icon emailIcon = new Icon();
        emailIcon.setStyleName("ficon--at");
        if (item.getPrimaryEmail() != null) {
            emailLink.addClickHandler(event -> {
                if (!item.getEmailOptOut()) {
                    //new ComposeView(item.getPrimaryEmail(), RelationItem.newEventRelation(RelationItem.TYPE_CONTACT, item.getObjectId(), item.getContactName()));
                    SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/" + item.getPrimaryEmail() + "/" + RelationItem.TYPE_CONTACT + "/" + item.getObjectId() + "/" + item.getContactName());
                } else {
                    WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.OK, CrmMessages.App.get().theEmailOutIsEnabled());
                    messageBox.setTitle(wfmStrings.information());
                    messageBox.open();
                }
            });
            emailLink.add(emailIcon);
        } else {
            Span emailSpan = new Span();
            emailSpan.add(emailIcon);
            emailLink.add(emailSpan);
        }
        new MaterialTooltip(emailLink, wfmStrings.sendEmail());
        emailDiv.add(emailLink);
        mainPanel.add(emailDiv);

        Div commentDiv = new Div("widget-row__button");
        MaterialLink commentLink = new MaterialLink();
        commentLink.setTooltip(wfmStrings.sendSms());
        commentLink.setTooltipPosition(Position.BOTTOM);
        Icon commentIcon = new Icon();
        commentIcon.setStyleName("ficon--sms");
        if (item.getPrimaryPhone() != null) {
            commentLink.add(commentIcon);
            commentLink.addClickHandler(event -> {
                ContactListItem contact = new ContactListItem();
                contact.setObjectId(item.getObjectId());
                new ActivityQuickAddForm(Appointment.SMS, item.getPrimaryPhone(), contact, RelationItem.newEventRelation(RelationItem.TYPE_CONTACT, item.getObjectId(), item.getContactName()));
            });
        } else {
            Span commentSpan = new Span();
            commentSpan.add(commentIcon);
            commentLink.add(commentSpan);
        }
        commentDiv.add(commentLink);
        mainPanel.add(commentDiv);

        Div phoneDiv = new Div("widget-row__button");
        MaterialLink phoneLink = new MaterialLink();
        phoneLink.setTooltip(wfmStrings.call());
        phoneLink.setTooltipPosition(Position.BOTTOM);
        Icon phoneIcon = new Icon();
        phoneIcon.setStyleName("ficon--phone2");
        if (item.getPrimaryPhone() != null) {
            phoneLink.add(phoneIcon);
            phoneLink.addClickHandler(event -> {
                ContactListItem contact = new ContactListItem();
                contact.setObjectId(item.getObjectId());
                new ActivityQuickAddForm(Appointment.CALL_LOG, item.getPrimaryPhone(), contact, RelationItem.newEventRelation(RelationItem.TYPE_CONTACT, item.getObjectId(), item.getContactName()));
            });
        } else {
            Span phoneSpan = new Span();
            phoneSpan.add(phoneIcon);
            phoneLink.add(phoneSpan);
        }
        phoneDiv.add(phoneLink);
        mainPanel.add(phoneDiv);

        return mainPanel;
    }

    private MaterialPanel drawAbcFilterPanel() {
        MaterialPanel panel = new MaterialPanel("cp_abc");

        String[] abc = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
                "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        for (String s : abc) {
            MaterialLink link = new MaterialLink();
            link.setStyleName("cp_abc__letter");
            link.setText(s);
            link.addClickHandler(event -> {
                selectedLink.removeStyleName("cp_abc__letter--selected");
                link.addStyleName("cp_abc__letter--selected");
                selectedLink = link;
                searchKey = s;
                isLetterSearch = true;
                getMyContacts(true);
            });
            panel.add(link);
        }
        return panel;
    }

    private Div drawDetailPanel(DashboardContactItem item) {
        Div detailDiv = new Div("widget-row-expandable__content widget-content widget-list");
        Div contactInfo = new Div("widget-contacts__info");
        Div contactHistry = new Div("widget-contacts__history");

        String companyName = item.getCompanyName() != null ? item.getCompanyName() : wfmStrings.notAvailable();
        FormGroup companyField = new FormGroup(wfmStrings.companyName(), new Span(companyName));

        String jobTitle = item.getJobTitle() != null ? item.getJobTitle() : wfmStrings.notAvailable();
        FormGroup jobTitleField = new FormGroup(wfmStrings.jobTitle(), new Span(jobTitle));

        FormGroup emailField = null;
        if (item.getPrimaryEmail() != null) {
            MaterialLink emailLink = new MaterialLink(item.getPrimaryEmail());
            emailLink.addClickHandler(event -> {
                if (!item.getEmailOptOut()) {
                    //new ComposeView(item.getPrimaryEmail(), RelationItem.newEventRelation(RelationItem.TYPE_CONTACT, item.getObjectId(), item.getContactName()));
                    SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/" + item.getPrimaryEmail() + "/" + RelationItem.TYPE_CONTACT + "/" + item.getObjectId() + "/" + item.getContactName());
                } else {
                    WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.OK, CrmMessages.App.get().theEmailOutIsEnabled());
                    messageBox.setTitle(wfmStrings.information());
                    messageBox.open();
                }
            });
            emailField = new FormGroup(wfmStrings.email(), emailLink);
        } else {
            emailField = new FormGroup(wfmStrings.email(), new Span(wfmStrings.notAvailable()));
        }
        FormGroup phoneField = null;
        if (item.getPrimaryPhone() != null) {
            ContactListItem contactItem = new ContactListItem();
            contactItem.setObjectId(item.getObjectId());
            PhonePopup phonePopup = new PhonePopup(item.getPrimaryPhone(), contactItem, false, true);
            phoneField = new FormGroup(wfmStrings.phone(), phonePopup.getPhoneWidget());
        } else {
            phoneField = new FormGroup(wfmStrings.phone(), new Span(wfmStrings.notAvailable()));
        }

        contactInfo.add(companyField);
        contactInfo.add(jobTitleField);
        contactInfo.add(emailField);
        contactInfo.add(phoneField);
        detailDiv.add(contactInfo);

        if (!item.getLastEvents().isEmpty()) {
            MaterialCollapsible collapsible = new MaterialCollapsible();
            collapsible.addStyleName("updates-list");
            MaterialCollapsibleItem collapsibleItem = new MaterialCollapsibleItem();
            MaterialCollapsibleHeader collapsibleHeader = new MaterialCollapsibleHeader();
            MaterialCollapsibleBody collapsibleBody = new MaterialCollapsibleBody();

            for (MyUpdateItem myUpdateItem : item.getLastEvents()) {
                Div eventRow = DashboardMyUpdatesComponent.drawRow(myUpdateItem, true);
                eventRow.addClickHandler(event -> {
                    String url = GWT.getHostPageBaseURL() + "/Crm.html#event|summary/" + myUpdateItem.getUpdateID();
                    Window.open(url, "_blank", null);
                });
                collapsibleBody.add(eventRow);
            }
            Heading headerH3 = new Heading(HeadingSize.H3);
            Span headerSpan = new Span(Property.getPluralWithObjectCode(Constants.EVENT_LIST, wfmStrings.activities()));
            headerH3.add(headerSpan);
            collapsibleHeader.add(headerH3);

            collapsibleItem.add(collapsibleHeader);
            collapsibleItem.add(collapsibleBody);
            collapsible.add(collapsibleItem);

            contactHistry.add(collapsible);
            collapsible.setActive(0);
            detailDiv.add(contactHistry);
        }

        return detailDiv;
    }

    private String getImageFormat(String first, String second) {
        String result = "";
        if (first != null && !first.isEmpty()) {
            result = result.concat(String.valueOf(first.charAt(0)));
        }
        if (second != null && !second.isEmpty()) {
            result = result.concat(String.valueOf(second.charAt(0)));
        }
        return result;
    }

    private void resetPaging() {
        totalCount = 0;
        listCount = 0;
        start = 0;
    }

    @Override
    protected String getEmptyText() {
        return null;
    }

    @Override
    public String getCode() {
        return DASHBOARD_WIDGET_CODE.MY_CONTACTS;
    }

    @Override
    protected void clearPanel() {
        content.clear();
    }

    private void clearPanelAndFilters() {
        categoryListBox.setSelectedNullLabel();
        selectedLink.removeStyleName("cp_abc__letter--selected");

        searchKey = null;
        isLetterSearch = false;
        resetPaging();

        clearPanel();
    }
}
