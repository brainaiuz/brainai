package com.edatasite.workforce.gwt.core.server.office365.services.impl;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.GoogleContactsManager;
import com.edatasite.workforce.gwt.core.server.office365.constants.Office365Constants;
import com.edatasite.workforce.gwt.core.server.office365.resources.*;
import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseItem;
import com.edatasite.workforce.gwt.core.server.office365.services.Office365ContactService;
import com.edatasite.workforce.gwt.core.server.office365.utils.Office365HttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * Created by umakarimov on 9/30/15.
 */
@Transactional
@Service("office365ContactService")
public class Office365ContactServiceImpl implements Office365ContactService, Office365Constants {
    @Autowired
    private GoogleContactsManager googleContactsManager;

    /**
     * @param token
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/contacts-rest-operations#GetContactCollection
     */
    @Override
    public Office365BaseList<Office365Contact> getContactCollection(Office365AccessTokenDTO token) {
        return this.getContactCollection(token, null);
    }

    /**
     * @param token
     * @param folderId
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/contacts-rest-operations#GetContactCollection
     */
    @Override
    public Office365BaseList<Office365Contact> getContactCollection(Office365AccessTokenDTO token, String folderId) {
        String url = OUTLOOK_CONTACT_LIST_URL;

        if (folderId != null) {
            url = String.format(OUTLOOK_CONTACT_FOLDER_ITEM_CONTACT_LIST_URL, folderId);
        }
        url = url + "?$top=" + Constants.CONTACTS_LIMIT;
        Office365HttpResponse data = Office365HttpClient.doGet(url, null, token);

        if (data.hasError()) {
            return null;
        }

        return new Office365BaseList<>(data, new Office365BaseItem.FieldMapper<Office365Contact>() {
            @Override
            public Office365Contact map(Object item) {
                return new Office365Contact((JSONObject) item);
            }
        });
    }

    /**
     * @param token
     * @param contactId
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/contacts-rest-operations#GetContact
     */
    @Override
    public Office365Contact getContact(Office365AccessTokenDTO token, String contactId) {
        String url = String.format(OUTLOOK_CONTACT_ITEM_URL, contactId);

        Office365HttpResponse data = Office365HttpClient.doGet(url, null, token);

        if (data.hasError()) {
            return null;
        }

        return new Office365Contact(data);
    }


    /**
     * @param token
     * @param contact
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/contacts-rest-operations#CreateAContact
     */
    @Override
    public Office365Contact createAContact(Office365AccessTokenDTO token, Office365Contact contact) {
        return this.createAContact(token, contact, null);
    }

    /**
     * @param token
     * @param contact
     * @param folderId
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/contacts-rest-operations#CreateAContact
     */
    @Override
    public Office365Contact createAContact(Office365AccessTokenDTO token, Office365Contact contact, String folderId) {
        String url = OUTLOOK_CONTACT_LIST_URL;

        if (folderId != null) {
            url = String.format(OUTLOOK_CONTACT_FOLDER_ITEM_CONTACT_LIST_URL, folderId);
        }

        Office365HttpResponse data = Office365HttpClient.doPost(url, contact.toJSON(), token);

        if (data.hasError()) {
            return null;
        }

        return new Office365Contact(data);
    }

    /**
     * @param folderId
     * @param token
     * @param contact
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/contacts-rest-operations#CreateAContact
     */
    @Override
    public Office365Contact addMemberToGroup(Office365AccessTokenDTO token, String groupId, Office365Contact contact) {
/*
        if (contact.getEmailAddresses() != null && contact.getEmailAddresses().size() > 0) {
            String url = String.format(OUTLOOK_CONTACT_GROUP_MEMBERS, groupId) + "/$ref";
            String contactUrl = String.format(OUTLOOK_CONTACT_ITEM_URL, contact.getId());
            String json = "{\"@odata.id\":\"" + contactUrl + "\"}";

            new Office365Fetcher.Request<Office365Contact>(OFFICE_ONE_DRIVE, url, token)
                    .setClass(Office365Contact.class)
                    .setResource(json)
                    .sendJSonPost()
                    .getResource();

        }*/
        return null;

    }

    /**
     * @param token
     * @param contact
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/contacts-rest-operations#UpdateAContact
     */
    @Override
    public Office365Contact updateAContact(Office365AccessTokenDTO token, Office365Contact contact, String folderId) {
        String url = String.format(OUTLOOK_CONTACT_ITEM_URL, contact.getId());

//        String url = OUTLOOK_CONTACT_LIST_URL;

        if (folderId != null) {
            url = String.format(OUTLOOK_CONTACT_FOLDER_ITEM_CONTACT_LIST_URL, folderId) + "/" + contact.getId();
        }

        Office365HttpResponse data = Office365HttpClient.doPatch(url, contact.toJSON(), token);

        if (data.hasError()) {
            return null;
        }

        return new Office365Contact(data);
    }

    /**
     * @param token
     * @param contactId
     * @see https://msdn.microsoft.com/office/office365/APi/contacts-rest-operations#DeleteAContact
     */
    @Override
    public void deleteAContact(Office365AccessTokenDTO token, String contactId) {
        String url = String.format(OUTLOOK_CONTACT_ITEM_URL, contactId);
        Office365HttpResponse data = Office365HttpClient.doDelete(url, null, token);

        if (data.hasError()) {

        }
    }

    /**
     * @param token
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/contacts-rest-operations#GetContactFolderCollection
     */
    @Override
    public Office365BaseList<Office365ContactFolder> getContactFolderCollection(Office365AccessTokenDTO token) {
        return this.getContactFolderCollection(token, null);
    }

    /**
     * @param token
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/contacts-rest-operations#GetContactFolderCollection
     */
    @Override
    public Office365BaseList<Office365ContactGroups> getContactGroupCollection(Office365AccessTokenDTO token) {
        return this.getContactGroupCollection(token, null);
    }

    /**
     * @param token
     * @param folderId
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/contacts-rest-operations#GetContactFolderCollection
     */
    @Override
    public Office365BaseList<Office365ContactFolder> getContactFolderCollection(Office365AccessTokenDTO token, String folderId) {
        String url = OUTLOOK_CONTACT_FOLDER_LIST_URL;

        if (folderId != null) {
            url = String.format(OUTLOOK_CONTACT_FOLDER_ITEM_CHILD_FOLDER_LIST_URL, folderId);
        }

        Office365HttpResponse data = Office365HttpClient.doGet(url, null, token);

        if (data.hasError()) {
            return null;
        }

        return new Office365BaseList<>(data, new Office365BaseItem.FieldMapper<Office365ContactFolder>() {
            @Override
            public Office365ContactFolder map(Object item) {
                return new Office365ContactFolder((JSONObject) item);
            }
        });
    }

    /**
     * @param token
     * @param folderId
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/contacts-rest-operations#GetContactFolderCollection
     */
    @Override
    public Office365BaseList<Office365ContactGroups> getContactGroupCollection(Office365AccessTokenDTO token, String folderId) {
        String url = OUTLOOK_CONTACT_GROUP_LIST_URL;

        if (folderId != null) {
            url = String.format(OUTLOOK_CONTACT_FOLDER_ITEM_CHILD_FOLDER_LIST_URL, folderId);
        }

        Office365HttpResponse data = Office365HttpClient.doGet(url, null, token);

        if (data.hasError()) {
            return null;
        }

        return new Office365BaseList<>(data, new Office365BaseItem.FieldMapper<Office365ContactGroups>() {
            @Override
            public Office365ContactGroups map(Object item) {
                return new Office365ContactGroups((JSONObject) item);
            }
        });
    }

    @Override
    public ArrayList<String> getContactGroupMembers(Office365AccessTokenDTO token, String groupId) {
        String url = String.format(OUTLOOK_CONTACT_GROUP_MEMBERS, groupId);

        Office365HttpResponse data = Office365HttpClient.doGet(url, null, token);

        if (data.hasError()) {
            return null;
        }
        JSONArray valuesList = (JSONArray) data.get("value");
        ArrayList<String> groupMembers = new ArrayList<>();
        if (valuesList != null) {
            for (Object value : valuesList) {
                JSONObject item = (JSONObject) value;
                if (item.get("mail") != null) {
                    groupMembers.add(String.valueOf(item.get("mail")));
                }
            }
        }
        return groupMembers;
    }

    @Override
    public void createContactDetails(String objectId, Office365AccessTokenDTO token) {
        googleContactsManager.createOfficeContactDetails(objectId, token);
    }
}
