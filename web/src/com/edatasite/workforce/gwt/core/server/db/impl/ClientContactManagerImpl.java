package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ClientContactManager;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("clientContactManager")
public class ClientContactManagerImpl extends BaseManager<EdsClientContact> implements ClientContactManager, Constants {

    public ClientContactManagerImpl() {
        super(EdsClientContact.class);

    }

    public List<Object[]> getClientsByCompany(EdsCompany company) {
        String companyId = "\"" + company.getObjectID() + "\"";
        return findNative("select distinct c.id, c.name " +
                " from " + companyId + ".crmaccount c " +
                EdsCrmAccount.getByTypeForNativeQueries("c", EdsCrmAccount.CUSTOMER) +
                " and c.deleted is not true " +
                " ORDER BY c.name");
    }

    @Override
    public List<EdsClientContact> getAccessEnabledContacts(EdsCrmAccount client) {
        return find("select cc from EdsClientContact cc, EdsUser u where cc.objectID=u.objectID and (u.deleted<>true or u.deleted is null) and cc.access=true and cc.crmContact.crmAccount.objectID=?", client.getObjectID());
    }

    @Override
    public List<EdsClientContact> getAccessEnabledContactList() {
        return find("select cc from EdsClientContact cc, EdsUser u where cc.objectID=u.objectID and (u.deleted<>true or u.deleted is null) and cc.access=true");
    }

    public EdsCrmContact getPrimaryClientContact(Integer clientID) {
        return (EdsCrmContact) findSingle("select cc from EdsCrmContact  cc where cc.crmAccount.objectID = ? and cc.primaryContact = true and (cc.deleted<>true or cc.deleted is null)", clientID);
//        return (EdsClientContact) findSingle("select cc from EdsClientContact cc where cc.client.objectID = ? and cc.primaryContact = true", clientID);
    }

    public EdsCrmContact getPrimarySupplierContact(Integer supplierID) {
        return (EdsCrmContact) findSingle("select cc from EdsCrmContact  cc where cc.crmAccount.objectID = ? and cc.primaryContact = true", supplierID);
    }

    public Integer getClientIdByClientContact(Integer clientContactID) {
        Integer clientID = (Integer) findSingle("select cc.crmContact.crmAccount.clientID from EdsClientContact cc where cc.objectID = ?", clientContactID);
        if (clientID == null) {
            clientID = (Integer) findSingle("select cc.client.objectID from EdsClientContact cc where cc.objectID = ?", clientContactID);
        }
        return clientID;
    }

    public List<String> getClientContactEmailsByCompany(Integer companyID) {
        return (List<String>) find("select cc.email from EdsClientContact cc where cc.deleted <> true");
    }

    public List<EdsClientContact> getClientContactByImportFile(Integer importFileID) {
        return (List<EdsClientContact>) find("select cc from " +
                "EdsClientContact cc where cc.crmContact.importFileID = " + importFileID);
    }

    private String extractEmail(String email) {
        if (email.contains("<") && email.contains(">")) {
            email = email.substring(email.indexOf("<") + 1, email.indexOf(">"));
            if ((email.contains("<") && email.contains(">")) || email.contains(",") || email.contains("'")) {
                return extractEmail(email);
            } else {
                return email.toLowerCase();
            }
        }
        if (email.contains(",") || email.contains("'")) {
            email = email.replace(",", "");
            email = email.replace("'", "");
            return email.toLowerCase();
        }
        return email.toLowerCase();
    }

    @Override
    public Map<String, String[]> checkEmailExistence(Integer entityID, String[] email) {
        String emails = "";
        if (email != null && email.length == 1 && !"".equals(email[0]) && email[0].matches(Constants.REGEX_EMAIL_SERVERSIDEONLY)) {
            emails = " = " + "'" + extractEmail(email[0]) + "' ";
        }
        if (email != null && email.length > 1) {
            String delimitr = "";
            boolean wroteIn = false;
            StringBuilder emailsBuilder = new StringBuilder(emails);
            for (String email_ : email) {
                if (email_ != null && !"".equals(email_) && email_.matches(Constants.REGEX_EMAIL_SERVERSIDEONLY)) {
                    if (!wroteIn) {
                        emailsBuilder = new StringBuilder(" in (");
                        wroteIn = true;
                    }
                    emailsBuilder.append(delimitr).append("'").append(extractEmail(email_)).append("'");
                    delimitr = ",";
                }
            }
            emails = emailsBuilder.toString();
            if (emails.contains(" in (")) {
                emails += ")";
            }
        }
        if (emails != null && !"".equals(emails)) {
            emails = " and lower(cc.primaryEmail) " + emails;
        }
        String expression = "";
        if (entityID != null) {
            expression = " and cc.entityID <> " + entityID + " ";
        }
        List<EdsCrmContact> crmContacts = (List<EdsCrmContact>) find("select cc from EdsCrmContact cc where (cc.deleted is null or cc.deleted <> true)" +
                emails + " " + expression);
        Map<String, String[]> result = new HashMap<>();
        if (crmContacts != null && crmContacts.size() > 0) {
            List<String> emailList = Arrays.asList(email);
            for (EdsCrmContact crmContact : crmContacts) {
                if (emailList.contains(crmContact.getPrimaryEmail()) && crmContact.getClientID() != null) {
                    result.put(crmContact.getPrimaryEmail(), new String[]{crmContact.getClientID().toString(), "clientContact", getEncryptEncodedLink("client|contacts/" + crmContact.getClientID())});
                }
            }
        }
        return result;
    }

    @Override
    public EdsClientContact getClientContactByCrmContact(Integer crmContactID) {
        return (EdsClientContact) findSingle("from EdsClientContact where crmContact.objectID = ?", crmContactID);
    }


    public Map<Integer, Boolean> getMapIdAndIsActive(List<Integer> clientContactIDs) {
        Map<Integer, Boolean> hashMap = new HashMap<>();
        List<Object[]> objects = findNative("select cc.crmcontactid, CASE WHEN re.code = '" + EMPLOYEE_STATUS_ACTIVE + "' THEN true ELSE false END from " + getCompanyId() + ".clientcontact cc " +
                "left join " + getCompanyId() + ".myuser mu on cc.id = mu.id " +
                "left join " + getCompanyId() + ".reference re on re.id=mu.accountstatusid " +
                "where cc.access is true and cc.crmcontactid in (" + ServerUtils.getAsCommoDelimited(clientContactIDs, "0", ",") + ")");
        if (objects != null && objects.size() > 0) {
            for (Object[] values : objects) {
                if (values[0] != null && values[0] instanceof Integer) {
                    Boolean isActive = values[1] == null || !(Boolean) values[1] ? Boolean.FALSE : Boolean.TRUE;
                    hashMap.put((Integer) values[0], isActive);
                }
            }
        }
        return hashMap;
    }

    public boolean isContactExist(Integer objectID, String type) {
        if (Constants.CLIENT_STR.equals(type)) {
            return find("select cc.objectID from EdsCrmContact cc where cc.crmAccount.objectID = ? and (cc.deleted<>true or cc.deleted is null)", objectID).size() > 0;
        } else if (Constants.SUPPLIER_STR.equals(type)) {
            return find("select cc.objectID from EdsCrmContact cc where cc.crmAccount.objectID = ? and (cc.deleted is null or cc.deleted<>true)", objectID).size() > 0;
        }
        return false;
    }

    private String getEncryptEncodedLink(String plainText) {
        return EncryptionHelper.encodeURL(EncryptionHelper.encryptURL(plainText));
    }

    public Integer getClientContactsCount(String email) {
        return find("select distinct cc.objectID from EdsClientContact cc where lower(cc.email) = ? and cc.deleted<>true", email.toLowerCase()).size();
    }

    @Override
    public EdsClientContact getClientContactByEmail(String email) {
        return (EdsClientContact) findSingle("select cc from EdsClientContact cc where lower(cc.email) = ? and (cc.deleted is null or cc.deleted is false)", email.toLowerCase());
    }

    @Override
    public EdsClientContact getAccessEnabledContactByEmail(String email) {
        return (EdsClientContact) findSingle("select cc from EdsClientContact cc where cc.access is true and lower(cc.email) = ? and (cc.deleted is null or cc.deleted is false)", email.toLowerCase());
    }

    @Override
    public EdsClientContact getClientByCrmContactAndByUserId(Integer crmContactID, Integer userID) {
        return (EdsClientContact) findSingle("SELECT cc FROM EdsClientContact cc WHERE cc.crmContact.objectID = ? AND cc.id = ? ", crmContactID, userID);
    }
}
