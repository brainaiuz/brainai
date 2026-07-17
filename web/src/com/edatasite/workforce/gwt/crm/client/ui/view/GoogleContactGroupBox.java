package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactServiceAsync;
import com.edatasite.workforce.gwt.contact.client.rpc.GoogleGroupsSetting;
import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryListItem;
import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryService;
import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryServiceAsync;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.WfmContentPanel;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.localization.CrmMessages;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.google.gwt.gen2.table.override.client.FlexTable;
import com.google.gwt.user.client.ui.HTML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 29.06.11
 * Time: 15:32
 * To change this template use File | Settings | File Templates.
 */
public class GoogleContactGroupBox extends KpiModal implements Constants, CommandConstants {
    private static final CrmMessages crmMessages = CrmMessages.App.get();
    private static final CrmStrings crmStrings = CrmStrings.App.get();

    protected ContactServiceAsync contactService = ContactService.App.get();
    private ContactCategoryServiceAsync contactCategoryService = ContactCategoryService.App.get();

    private WfmContentPanel localCategories;
    private HashMap<String, Object> googleAndLocalMap;
    private FlexTable localCategoriesTable;
    private String storageType = GOOGLE;

    private WfmButton2 saveButton;
    private boolean allSelected;


    public GoogleContactGroupBox(String storage) {
        this.storageType = storage;
        setWidth("600px");
        if(OFFICE_365.equalsIgnoreCase(storageType)) {
            setTitle(crmStrings.settingsOfficeFolders());
        } else if(GOOGLE.equalsIgnoreCase(storageType)) {
            setTitle(crmStrings.googleGroupsSettings());
        }
        build();
        getData();
        open();
    }

    private void build() {
        localCategories = new WfmContentPanel();
        if(OFFICE_365.equalsIgnoreCase(storageType)) {
            localCategories.setTitle(crmStrings.localAndOffice365ContactCategories());
            localCategories.setCaptionLeftHTML(crmStrings.localAndOffice365ContactCategories());
        } else if(GOOGLE.equalsIgnoreCase(storageType)) {
            localCategories.setTitle(crmStrings.localAndGoogleContactCategories());
            localCategories.setCaptionLeftHTML(crmStrings.localAndGoogleContactCategories());
        }

        localCategories.setSize("550px", "200px");
        localCategories.getElement().getStyle().setBorderColor("#cccccc");
        add(localCategories);
        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> close()));
        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        addButton(saveButton);
    }

    private void getData() {
        LoadingPanel.loading(true, GoogleContactGroupBox.this);
        contactService.getGoogleContactGroups(storageType, new AbstractAsyncCallback<ArrayList<ContactCategoryListItem>>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, GoogleContactGroupBox.this);
                Info.show(crmMessages.errorWhileRetrivingGroupsFromGoogle(), Info.Type.WARNING);
            }

            @Override
            public void success(final ArrayList<ContactCategoryListItem> contactGroupEntries) {
                getPermissions(contactGroupEntries);
            }
        });
    }

    private void getPermissions(final ArrayList<ContactCategoryListItem> contactGroupEntries) {
        contactCategoryService.getContactCategoriesWithPermissions(new AbstractAsyncCallback<ArrayList<ContactCategoryListItem>>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, GoogleContactGroupBox.this);
                Info.show(crmMessages.errorWhileRetrivingLocalCategories(), Info.Type.WARNING);
            }

            @Override
            public void success(ArrayList<ContactCategoryListItem> contactCategoryListItems) {
                LoadingPanel.loading(false, GoogleContactGroupBox.this);
                localCategoriesTable = new FlexTable();
                googleAndLocalMap = new HashMap<>();
                int i = 0;
                if(contactGroupEntries != null && contactGroupEntries.size() > 0){
                    for (ContactCategoryListItem entry : contactGroupEntries) {
                        DataListBox localCategoriesList = new DataListBox();
                        //For sync we are allowing first and second level categories (parent-child)
                        if (contactCategoryListItems != null && contactCategoryListItems.size() > 0) {
                            for (ContactCategoryListItem catItem : contactCategoryListItems) {
                                if (catItem != null) {
                                    if (catItem.isSystemCategory() || (catItem.isShared() && hasFullPermission(catItem))) {
                                        SelectItem item = new SelectItem();
                                        item.setId(catItem.getObjectID());
                                        item.setName(catItem.getName());
                                        item.setDescription(catItem.getDescription());
                                        localCategoriesList.addListItem(item);
                                        if (catItem.getChildren() != null && catItem.getChildren().length != 0) {
                                            for (ContactCategoryListItem subCatItem : catItem.getChildren()) {
                                                if (subCatItem != null) {
                                                    if (subCatItem.isSystemCategory() || (subCatItem.isShared() && hasFullPermission(subCatItem))) {
                                                        SelectItem subItem = new SelectItem();
                                                        subItem.setId(subCatItem.getObjectID());
                                                        subItem.setName(" -  " + subCatItem.getName());
                                                        subItem.setDescription(" -  " + subCatItem.getDescription());
                                                        localCategoriesList.addListItem(subItem);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        googleAndLocalMap.put(entry.getDescription(), localCategoriesList);
                        localCategoriesTable.setWidget(i, 0, new HTML("" + (i + 1) + "."));
                        localCategoriesTable.setWidget(i, 1, localCategoriesList);
                        localCategoriesTable.setWidget(i, 2, new HTML("<b style='padding-left:5px'>" + entry.getName() + "</b>"));
                        i++;
                    }
                }
                localCategories.add(localCategoriesTable);
                loadAndSaveData();
            }
        });
    }

    public void loadAndSaveData() {
        LoadingPanel.loading(true, GoogleContactGroupBox.this);
        contactService.getUserSettings(storageType, new AbstractAsyncCallback<ArrayList<GoogleGroupsSetting>>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, GoogleContactGroupBox.this);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(ArrayList<GoogleGroupsSetting> googleGroupsSettings) {
                LoadingPanel.loading(false, GoogleContactGroupBox.this);
                if (googleGroupsSettings != null && googleGroupsSettings.size() != 0) {
                    for (GoogleGroupsSetting setting : googleGroupsSettings) {
                        if (googleAndLocalMap.containsKey(setting.getGoogleGroupID())) {
                            DataListBox box = (DataListBox) googleAndLocalMap.get(setting.getGoogleGroupID());
                            box.setSelected(setting.getWftGroupID());
                        }
                    }
                }
            }
        });
    }

    public boolean validate() {
        if (googleAndLocalMap != null) {
            for (String key : googleAndLocalMap.keySet()) {
                DataListBox box = (DataListBox) googleAndLocalMap.get(key);
                if (box.getSelectedItem() == null || box.getSelectedItem().getId() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    private void save() {
        allSelected = validate();
        List<GoogleGroupsSetting> settings = new ArrayList<>();
        for (String key : googleAndLocalMap.keySet()) {
            DataListBox box = (DataListBox) googleAndLocalMap.get(key);
            if (box.getSelectedItem() != null && box.getSelectedItem().getId() != null) {
                GoogleGroupsSetting setting = new GoogleGroupsSetting();
                setting.setGoogleGroupID(key);
                setting.setWftGroupID(box.getSelectedItem().getId());
                settings.add(setting);
            }
        }
        if (settings.size() != 0) {
            saveButton.setEnabled(false);
            LoadingPanel.loading(true);
            contactService.saveGoogleGroupsSettings(storageType, settings.toArray(new GoogleGroupsSetting[]{}), new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    saveButton.setEnabled(true);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void success(Void aVoid) {
                    LoadingPanel.loading(false);
                    saveButton.setEnabled(true);
                    if (allSelected) {
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), crmStrings.googleContacts()), Info.Type.INFO);
                    } else {
                        Info.show(wfmStrings.messSuccessfullySaved() + "<br />" + crmMessages.groupNotice(), Info.Type.INFO);
                    }
                    close();
                }
            });
        } else {
            Info.show(crmMessages.matchAtLeastOneCategory(), Info.Type.INFO);
        }
    }

    private boolean hasFullPermission(ContactCategoryListItem item) {
        return item.getOwner().getObjectId().equals(Utils.getUserID()) || (item.getPermission().isDelete() && item.getPermission().isWrite()) || item.getPermission().isModifyACL();
    }
}
