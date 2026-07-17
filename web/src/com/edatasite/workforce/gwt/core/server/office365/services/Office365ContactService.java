package com.edatasite.workforce.gwt.core.server.office365.services;

import com.edatasite.workforce.gwt.core.server.office365.resources.*;

import java.util.ArrayList;

/**
 * Created by umakarimov on 9/30/15.
 */
public interface Office365ContactService {

    Office365BaseList<Office365Contact> getContactCollection(Office365AccessTokenDTO token);

    Office365BaseList<Office365Contact> getContactCollection(Office365AccessTokenDTO token, String folderId);

    Office365Contact getContact(Office365AccessTokenDTO token, String contactId);

    Office365Contact createAContact(Office365AccessTokenDTO token, Office365Contact contact);

    Office365Contact createAContact(Office365AccessTokenDTO token, Office365Contact contact, String folderId);

    Office365Contact updateAContact(Office365AccessTokenDTO token, Office365Contact contact, String folderId);

    void deleteAContact(Office365AccessTokenDTO token, String contactId);

    Office365BaseList<Office365ContactFolder> getContactFolderCollection(Office365AccessTokenDTO token);

    Office365BaseList<Office365ContactFolder> getContactFolderCollection(Office365AccessTokenDTO token, String folderId);

    Office365BaseList<Office365ContactGroups> getContactGroupCollection(Office365AccessTokenDTO token);

    Office365BaseList<Office365ContactGroups> getContactGroupCollection(Office365AccessTokenDTO token, String folderId);

    ArrayList<String> getContactGroupMembers(Office365AccessTokenDTO token, String groupid);

    Office365Contact addMemberToGroup(Office365AccessTokenDTO token, String groupid, Office365Contact contact);

    void createContactDetails(String objectId, Office365AccessTokenDTO token);
}
