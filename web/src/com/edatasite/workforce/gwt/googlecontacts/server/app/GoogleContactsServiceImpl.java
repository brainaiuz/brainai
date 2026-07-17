package com.edatasite.workforce.gwt.googlecontacts.server.app;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleContactsManager;
import com.edatasite.workforce.gwt.googlecontacts.client.rpc.GoogleContactsService;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 13.11.2008
 * Time: 20:43:01
 */

@Transactional
@Service("googleContactsService")
public class GoogleContactsServiceImpl implements GoogleContactsService {

    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private GoogleContactsManager googleContactsManager;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean validateCurrentUser() {
        return googleContactsManager.validateUser(googleContactsManager.getUser());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean validateCurrentOfficeUser() {
        return googleContactsManager.validateOfficeUser(googleContactsManager.getUser());
    }

    public void saveToken(String token) throws Exception {
        try {
            googleContactsManager.createContactDetails(token);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new IOException(ex.getMessage());
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
            throw new GeneralSecurityException(ex.getMessage());
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            throw new AuthenticationException(ex.getMessage());
        } catch (ServiceException ex) {
            ex.printStackTrace();
            throw new ServiceException(ex);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ContactListItem[] getImportedContacts() throws Exception {
        List<EdsEmployee> employees = employeeManager.getCompanyEmployees();
        List<ContactListItem> contacts = new ArrayList<>();

        for (EdsEmployee employee : employees) {
            SelectItem email = new SelectItem(employee.getObjectID(), employee.getEmail());
            try {
                if (!isLoggedUser(email.getName()) && !googleContactsManager.existsEmail(email.getName())) {
                    if (employee.getEmployeeTeam() != null && employee.getEmployeeTeam().getTeam() != null) {
                        String teamName = employee.getEmployeeTeam().getTeam().getName();
                        ContactListItem contactListItem = new ContactListItem();
                        contactListItem.setObjectId(employee.getObjectID());
                        contactListItem.setContactName(employee.getName());
                        contactListItem.setDepartment(teamName);
                        SelectItem[] emails = new SelectItem[]{email};
                        contactListItem.setEmails(emails);
                        contacts.add(contactListItem);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new IOException(ex.getMessage());
            } catch (GeneralSecurityException ex) {
                ex.printStackTrace();
                throw new GeneralSecurityException(ex.getMessage());
            } catch (ServiceException ex) {
                ex.printStackTrace();
                throw new ServiceException(ex);
            }
        }

        return contacts.toArray(new ContactListItem[] {});
    }

    private boolean isLoggedUser(String email) {
        EdsUser user = googleContactsManager.getUser();
        return user.getEmail().equals(email);
    }

    public void importContacts(ContactListItem[] items) throws Exception {
        try {
            ContactsService service = googleContactsManager.getLoggedService();
            String groupID = googleContactsManager.createGroupAndGetID(service);

            for (ContactListItem item : items) {
                googleContactsManager.createContact(service, item.getName(), item.getPrimaryEmail(), groupID);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new IOException(ex.getMessage());
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
            throw new GeneralSecurityException(ex.getMessage());
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            throw new AuthenticationException(ex.getMessage());
        } catch (ServiceException ex) {
            ex.printStackTrace();
            throw new ServiceException(ex);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getUserTeamName() {
        return googleContactsManager.getUser().getEmployee().getEmployeeTeam().getTeam().getName();
    }

    public void deleteGoogleContactToken() {
        googleContactsManager.delete(googleContactsManager.getGoogleContact(employeeManager.getUser(), true));
    }
}