package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.FlexPanel;
import com.edatasite.workforce.gwt.core.client.ui.GoogleAuthorizationPanel;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 17:39:36
 * To change this template use File | Settings | File Templates.
 */
public class ImportExportGoogleContactView extends View implements Constants, Colapse {
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final String action;

    public ImportExportGoogleContactView(String actionName) {
        super("gcontact" + actionName, actionName.equals("0") ? crmStrings.exportContacts() : crmStrings.importContacts());
        this.action = actionName.equals("0") ? "export" : "import";
    }

    private ContactListItem[] items;
    private boolean saveAndClose = false;

    private VerticalPanel initialList;
    private ScrollPanel initialPanel;
    private FlexPanel leftPanel;
    private VerticalPanel resultList;
    private ScrollPanel resultPanel;
    private FlexPanel rightPanel;
    private final Map<Integer, Object> initialMap = new HashMap<>();
    private final Map<Integer, Object> resultMap = new HashMap<>();
    //    private RadioButton privateButton;
//    private RadioButton publicButton;
    private SimpleLink selectAll;
    private SimpleLink noneSelect;
    private Label resLabel;
    private WfmButton2 saveAndCloseBt;
    private WfmButton2 cancelBt;
    private final TextBox searchBox = new TextBox();
    private final TextBox resultSearchBox = new TextBox();

    protected Widget onInitialize() {
        ContactService.App.get().validateUserGoogle(new AbstractAsyncCallback<Boolean>() {
            public void success(Boolean isValid) {
                if (isValid) {
                    initialize();
                } else {
                    new GoogleAuthorizationPanel(GOOGLE_CONTACTS, true, GWT.getModuleName(), action.equals("import") ? 1 : 2);
                }
            }
        });
        return null;
    }

    public String getIconStyle() {
        return null;
    }


    public void initialize() {
        String butonName = action.equals("import") ? wfmStrings.importString() : wfmStrings.export();
        saveAndCloseBt = new WfmButton2(butonName);
        cancelBt = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
        LoadingPanel.loading(true);
        int height = 329/*container.getWorkarea().getHeight() - 100*/;
        if (height == (-100)) {
            height = 329;
        }
        initialList = new VerticalPanel();
        initialList.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
        initialPanel = new ScrollPanel();
        initialPanel.setWidget(initialList);
        initialPanel.setSize("405px", "380px");
        leftPanel = new FlexPanel();
        leftPanel.add(initialPanel);
        leftPanel.setBorders(true);

        resultList = new VerticalPanel();
        resultList.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
        resultPanel = new ScrollPanel();
        resultPanel.setWidget(resultList);
        resultPanel.setSize("420px", "380px");
        rightPanel = new FlexPanel();
        rightPanel.add(resultPanel);
        rightPanel.setBorders(true);

        searchBox.setWidth("205px");
        searchBox.setText(wfmStrings.searchContact());
        searchBox.addStyleName("search-textbox");
        searchBox.addFocusListener(new FocusListener() {
            public void onFocus(Widget sender) {
                searchBox.setText("");
                searchBox.removeStyleName("search-textbox");
            }

            public void onLostFocus(Widget sender) {
                String t = ((TextBox) sender).getText();
                if ("".equals(t)) {
                    searchBox.setText(wfmStrings.searchContact());
                    searchBox.addStyleName("search-textbox");
                }
            }
        });
        searchBox.addKeyboardListener(new KeyboardListener() {
            public void onKeyDown(Widget sender, char keyCode, int modifiers) {
                String t = ((TextBox) sender).getText();
                if (wfmStrings.searchContact().equals(t)) {
                    searchBox.setText("");
                    searchBox.removeStyleName("search-textbox");
                }

            }

            public void onKeyPress(Widget sender, char keyCode, int modifiers) {

            }

            public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                String t = ((TextBox) sender).getText();
                if ("".equals(t) || wfmStrings.searchContact().equals(t)) {
                    restoreLeftPanelItems();
                    searchBox.setText(wfmStrings.searchContact());
                    searchBox.setStyleName("search-textbox");
                } else {
                    filterContactList(((TextBox) sender).getText(), false);
                }
            }
        });

        searchBox.addClickHandler(event -> {
            searchBox.setText("");
            searchBox.removeStyleName("search-textbox");
        });

        Image clearImage = new Image();
        clearImage.addClickHandler(sender -> {
            searchBox.setText(wfmStrings.searchContact());
            searchBox.addStyleName("search-textbox");
            restoreLeftPanelItems();
        });

        // -----------------------------------------------------------------------------------------------------------------
        resultSearchBox.setWidth("205px");
        resultSearchBox.setText(wfmStrings.searchContact());
        resultSearchBox.addStyleName("search-textbox");
        resultSearchBox.addFocusListener(new FocusListener() {
            public void onFocus(Widget sender) {
                resultSearchBox.setText("");
                resultSearchBox.removeStyleName("search-textbox");
            }

            public void onLostFocus(Widget sender) {
                String t = ((TextBox) sender).getText();
                if ("".equals(t)) {
                    resultSearchBox.setText(wfmStrings.searchContact());
                    resultSearchBox.addStyleName("search-textbox");
                }
            }
        });
        resultSearchBox.addKeyboardListener(new KeyboardListener() {
            public void onKeyDown(Widget sender, char keyCode, int modifiers) {
                String t = ((TextBox) sender).getText();
                if (wfmStrings.searchContact().equals(t)) {
                    resultSearchBox.setText("");
                    resultSearchBox.removeStyleName("search-textbox");
                }

            }

            public void onKeyPress(Widget sender, char keyCode, int modifiers) {
            }

            public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                String t = ((TextBox) sender).getText();
                if ("".equals(t) || wfmStrings.searchContact().equals(t)) {
                    restoreLeftPanelItems();
                    resultSearchBox.setText(wfmStrings.searchContact());
                    resultSearchBox.setStyleName("search-textbox");
                } else {
                    filterContactList(((TextBox) sender).getText(), true);
                }
            }
        });

        resultSearchBox.addClickHandler(event -> {
            resultSearchBox.setText("");
            resultSearchBox.removeStyleName("search-textbox");
        });

        Image clearImage1 = new Image();
        clearImage1.addClickHandler(sender -> {
            resultSearchBox.setText(wfmStrings.searchContact());
            resultSearchBox.addStyleName("search-textbox");
            restoreRightPanelItems();
        });

        selectAll = new SimpleLink(wfmStrings.selectAll());
        noneSelect = new SimpleLink(wfmStrings.deselectAll());

//        RadioButton privButton = new RadioButton("visib", "&nbsp;" + wfmStrings.priv(), true);
//        privButton.setChecked(true);
//        privButton.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent sender) {
//                if (resultMap.size() > 0) {
//                    ContactListItem[] items = (ContactListItem[]) resultMap.values().toArray(new ContactListItem[]{});
//                    for (int i = 0; i < items.length; i++) {
//                        items[i].setVisible(false);
//                        ((RadioButton) ((FlexTable) resultList.getWidget(i)).getWidget(0, 2)).setChecked(true);
//                    }
//                }
//            }
//        });
//
//        RadioButton pubButton = new RadioButton("visib", "&nbsp;" + wfmStrings.pub(), true);
//        pubButton.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent event) {
//                if (resultMap.size() > 0) {
//                    ContactListItem[] items = (ContactListItem[]) resultMap.values().toArray(new ContactListItem[]{});
//                    for (int i = 0; i < items.length; i++) {
//                        items[i].setVisible(true);
//                        ((RadioButton) ((FlexTable) resultList.getWidget(i)).getWidget(0, 3)).setChecked(true);
//                    }
//                }
//            }
//        });

        FlexTable ft = new FlexTable();
        HorizontalPanel hp = new HorizontalPanel();
        hp.setWidth("370px");
        resLabel = new Label(crmStrings.selected() + ": 0");
        resLabel.setWidth("120px");
        hp.add(resLabel);
        hp.setCellHorizontalAlignment(resLabel, HasHorizontalAlignment.ALIGN_LEFT);

        if (action.equals("import")) {
//            HorizontalPanel rbPanel = new HorizontalPanel();
//            rbPanel.add(privButton);
//            rbPanel.add(new HTML("&nbsp;&nbsp;&nbsp;"));
//            rbPanel.add(pubButton);
//            hp.add(rbPanel);
//            hp.setCellHorizontalAlignment(rbPanel, HasHorizontalAlignment.ALIGN_RIGHT);
//
            ft.setWidget(0, 0, new Label(crmStrings.googleContacts()));

        } else {
            ft.setWidget(0, 0, new Label(Utils.getProductName() + " " + wfmStrings.contacts()));
        }
        ft.setWidget(0, 2, hp);

        Image image = new Image();
        HorizontalPanel imagePanel = new HorizontalPanel();
        HorizontalPanel searchPanel = new HorizontalPanel();
        imagePanel.setSize("30px", (height - 20) + "px");
        imagePanel.add(image);
        imagePanel.setCellHorizontalAlignment(image, HasAlignment.ALIGN_CENTER);
        imagePanel.setCellVerticalAlignment(image, HasAlignment.ALIGN_MIDDLE);
        searchPanel.add(searchBox);
        searchPanel.add(clearImage);
        Label selectLabel = new Label();
        Label spaceLabel = new Label();
        spaceLabel.setWidth("5px");
        selectLabel.setWidth("85px");
        searchPanel.add(selectLabel);
        searchPanel.add(selectAll);
        searchPanel.add(spaceLabel);
        searchPanel.add(noneSelect);

        HorizontalPanel searchPanel1 = new HorizontalPanel();
        searchPanel1.add(resultSearchBox);
        searchPanel1.add(clearImage1);

        ft.setWidget(1, 0, searchPanel);
        ft.setWidget(1, 2, searchPanel1);
        ft.setWidget(2, 1, imagePanel);
        ft.getFlexCellFormatter().setAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_MIDDLE);
        ft.getFlexCellFormatter().setAlignment(1, 0, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_MIDDLE);
        ft.getFlexCellFormatter().setAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
        ft.setWidget(2, 0, leftPanel);
        ft.setWidget(2, 2, rightPanel);
        HorizontalPanel butonP = new HorizontalPanel();
        butonP.add(saveAndCloseBt);
        butonP.add(cancelBt);
        butonP.setSpacing(5);
        ft.setWidget(3, 2, butonP);
        ft.getFlexCellFormatter().setColSpan(0, 2, 3);
        ft.setCellSpacing(5);
        add(ft);

        saveAndCloseBt.addClickHandler(sender -> {
            saveAndClose = true;
            save();
        });

        cancelBt.addClickHandler(sender -> closeTab());

        selectAll.addClickHandler(event -> {
            if (initialMap != null && initialMap.size() > 0) {
                ContactListItem[] contactItems = initialMap.values().toArray(new ContactListItem[]{});
                for (ContactListItem contactItem : contactItems) {
                    initResult(contactItem, true);
                    resLabel.setText(crmStrings.selected() + ": " + resultList.getWidgetCount());
                    initialMap.remove(contactItem.getIndex());
                    initialList.clear();
                }
            }
        });

        noneSelect.addClickHandler(event -> {
            if (resultMap != null && resultMap.size() > 0) {
                ContactListItem[] contactItems = resultMap.values().toArray(new ContactListItem[]{});
                for (ContactListItem contactItem : contactItems) {
                    initResult(contactItem, false);
                    resLabel.setText(crmStrings.selected() + ": " + resultList.getWidgetCount());
                    resultMap.remove(contactItem.getIndex());
                    resultList.clear();
                }
            }
        });

        if (action.equals("import")) {
            ContactService.App.get().getGoogleContacts(false, new AbstractAsyncCallback<ContactListItem[]>() {
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    closeTab();
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                public void success(final ContactListItem[] list) {
                    LoadingPanel.loading(false);
                    items = list;
                    if (items != null && items.length > 0) {
                        for (int i = 0; i < items.length; i++) {
                            items[i].setIndex(i);
                            initResult(items[i], false);
                        }
                    } else {
                        Info.show(crmStrings.noNewGoogleContact(), Info.Type.INFO);
                    }
                }
            });
        } else {
            ContactService.App.get().getWFTContacts(false, new AbstractAsyncCallback<ContactListItem[]>() {
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    closeTab();
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                public void success(final ContactListItem[] list) {
                    LoadingPanel.loading(false);
                    items = list;
                    if (items != null && items.length > 0) {
                        for (int i = 0; i < items.length; i++) {
                            items[i].setIndex(i);
                            initResult(items[i], false);
                        }
                    } else {
                        Info.show(crmStrings.noNewWFTContact() + " " + Utils.getProductName() + " " + crmStrings.noNewWFTContact1(), Info.Type.INFO);
                    }
                }
            });
        }
    }

    private void restoreLeftPanelItems() {
        if (initialList.getWidgetCount() > 0) {
            for (int i = 0; i < initialList.getWidgetCount(); i++) {
                initialList.getWidget(i).setVisible(true);
            }
        }
    }

    private void restoreRightPanelItems() {
        if (resultList.getWidgetCount() > 0) {
            for (int i = 0; i < resultList.getWidgetCount(); i++) {
                resultList.getWidget(i).setVisible(true);
            }
        }
    }

    private void filterContactList(String s, boolean useResultList) {
        VerticalPanel list = useResultList ? resultList : initialList;
        for (int i = 0; i < list.getWidgetCount(); i++) {
            list.getWidget(i).setVisible(((Label) ((FlexTable) list.getWidget(i)).getWidget(0, 1)).getText().toLowerCase().contains(s.toLowerCase()));
        }
    }

    public void initResult(final ContactListItem contactListItem, final boolean isResult) {
        if (!isResult || !resultMap.containsKey(contactListItem.getIndex())) {
            final FlexTable table = new FlexTable();
            table.setHeight("30px");
            final KpiCheckBox checkBox = new KpiCheckBox();
            checkBox.addClickHandler(event -> {
                if (checkBox.isChecked()) {
                    if (isResult) {
                        initResult((ContactListItem) resultMap.get(contactListItem.getIndex()), false);
                        table.removeFromParent();
                        resultMap.remove(contactListItem.getIndex());
                        resLabel.setText(crmStrings.selected() + ": " + resultList.getWidgetCount());
                    } else {
                        initResult(contactListItem, true);
                        resLabel.setText(crmStrings.selected() + ": " + resultList.getWidgetCount());
                        initialMap.remove(contactListItem.getIndex());
                        table.removeFromParent();
                    }
                }
            });

            table.setWidget(0, 0, checkBox);
            StringBuilder fullText = new StringBuilder();
            String email = contactListItem.getPrimaryEmail();
            String fullName = contactListItem.getContactName();
            if (fullName != null && !"".equals(fullName.trim())) {
                fullText.append(fullName.trim());
                if (email != null && !"".equals(email)) {
                    fullText.append(": " + email);
                }
            } else if (email != null && !"".equals(email)) {
                fullText.append(email);
            }
            if (isResult) {
                table.setWidget(0, 1, new Label(fullText.toString()));
                table.getFlexCellFormatter().setWidth(0, 0, "20px");
                table.getFlexCellFormatter().setWidth(0, 1, "250px");

                resultMap.put(contactListItem.getIndex(), contactListItem);
                resultList.add(table);
            } else {
                table.setWidget(0, 1, new HTML("&nbsp;" + fullText));
                initialMap.put(contactListItem.getIndex(), contactListItem);
                initialList.add(table);
            }
        }
    }

    protected void save() {
        if (!validate()) {
            return;
        }

        if (resultMap != null && resultMap.size() > 0) {
            ContactListItem[] contactListItems = new ContactListItem[resultMap.size()];
            contactListItems = resultMap.values().toArray(new ContactListItem[]{});
            if (action.equals("import")) {
                LoadingPanel.loading(true);
                ContactService.App.get().importGoogleContacts(contactListItems, true, new AbstractAsyncCallback<Void>() {
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }

                    public void success(final Void o) {
                        LoadingPanel.loading(false);
                        Info.show(crmStrings.messContactsSucImported(), Info.Type.INFO);
                        //refreshOnDemand(new String[]{CRM_CONTACT_LIST, CRM_CONTACT_LIST_2, GLOBAL_CONTACT_LIST});
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONTACT_ADD, o, ImportExportGoogleContactView.this);
                        onShellOk();
                    }
                });
            } else {
                LoadingPanel.loading(true);
                ContactService.App.get().exportToGoogleContact(contactListItems, true, new AbstractAsyncCallback<Void>() {
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }

                    public void success(Void result) {
                        LoadingPanel.loading(false);
                        Info.show(crmStrings.contactsSucExported(), Info.Type.INFO);
                        onShellOk();
                    }
                });
            }
        } else {
            LoadingPanel.loading(false);
            Info.show(crmStrings.pleaseSelectContact(), Info.Type.INFO);
        }
    }

    int errors = 0;

    private boolean validate() {
        errors = 0;
        if (resultList.getWidgetCount() == 0) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void onShellOk() {
        if (saveAndClose) {
            closeTab();
        } else {
            reinit();
        }
    }

    public void reinit() {
        clear();
        initialize();
    }

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
}