package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.GoogleGadgetService;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 10/15/12
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class GoogleGadgetAddContactController {

    @Autowired
    private GoogleGadgetService googleGadgetService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private UserManager userManager;

    private static final String DEFAULT_FORM = "getDefaultForm";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email";
    private static final String COMPANY_NAME = "companyName";
    private static final String NOTE = "note";
    private static final String SAVE_CONTACT = "saveContact";
    private static final String LINK_TO_EMAIL = "linkToEmail";
    private static final String EMAIL_SUBJECT = "emailSubject";
    private static final String EMAIL_DESCIPTION = "emailDescription";
    private static final String EMAIL_FROM_EMAIL = "emailFromEmail";
    private static final String EMAIL_TO_EMAIL = "emailToEmail";
    private static final String EMAIL_EMAIL_ID = "emailEmailId";

    private static final String EMAIL_EXIST = "Such email already exists in the system";
    private static final String USER_NAME_EXIST = "Contact already exist in system";


    @RequestMapping(value = "/googleGadget/addContact")
    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(GoogleGadgetService.JSON_CONTENT_TYPE);
        PrintWriter writer = response.getWriter();
        boolean isSigned = googleGadgetService.checkSignedRequest(request);


        String openSocialViewerId = request.getParameter(GoogleGadgetService.OPEN_SOCIAL_VIEWER_ID);
        Integer companyId = googleGadgetService.getInteger(request.getParameter(GoogleGadgetService.COMPANY_ID));

        if (isSigned) {
            boolean isUserExist = googleGadgetService.googleGadgetSignIn(openSocialViewerId, companyId);
            if (isUserExist) {
                if (request.getParameter(DEFAULT_FORM) != null && request.getParameter(DEFAULT_FORM).equals(GoogleGadgetService.TRUE)) {
                    return getForm();
                } else if (request.getParameter(SAVE_CONTACT) != null && request.getParameter(SAVE_CONTACT).equals(GoogleGadgetService.TRUE)) {
                    writer.write(saveContact(request));
                }
            } else {
                writer.write(GoogleGadgetService.YOU_ARE_NOT_AUTHORIZED);
            }
        } else {
            writer.write(GoogleGadgetService.YOUR_REQUEST_IS_NOT_SIGNED);
        }
        writer.close();
        return null;
    }

    private ModelAndView getForm() {
        return new ModelAndView("googleGadgetAddContact");
    }


    private String saveContact(HttpServletRequest leadData) {
        JSONObject jsonResponse = new JSONObject();
        if (validate(leadData)) {

            String firstName = leadData.getParameter(FIRST_NAME);
            String lastName = leadData.getParameter(LAST_NAME);
            String email = !isInvalid(leadData.getParameter(EMAIL)) && leadData.getParameter(EMAIL).matches(Constants.REGEX_EMAIL_SERVERSIDEONLY) ? leadData.getParameter(EMAIL) : null;
            String companyName = !isInvalid(leadData.getParameter(COMPANY_NAME)) ? leadData.getParameter(COMPANY_NAME).trim() : null;
            String note = leadData.getParameter(NOTE);

            String emailSubject = leadData.getParameter(EMAIL_SUBJECT);
            String emailDescription = leadData.getParameter(EMAIL_DESCIPTION);
            String emailFromEmail = leadData.getParameter(EMAIL_FROM_EMAIL);
            String emailToEmail = leadData.getParameter(EMAIL_TO_EMAIL);
            String emailEmailId = leadData.getParameter(EMAIL_EMAIL_ID);
            String linkToEmail = leadData.getParameter(LINK_TO_EMAIL);


            EdsUser user = userManager.getUser();

            ContactListItem contact = new ContactListItem();
            contact.setCheckForDuplicates(true);
            contact.setContactType(ContactListItem.CRM_CONTACT);
            contact.setFirstName(firstName);
            contact.setLastName(lastName);
            CrmAccountItem companyItem = new CrmAccountItem();
            companyItem.setName(companyName);
            companyItem.setEmail(email);
            contact.setPrimaryEmail(email);
            contact.addParam(Constants.CONTACT_EMAILS, Constants.G_WORK, email);
            contact.setCrmAccount(companyItem);
            contact.setNote(note);
            contact.setOwnerId(user.getObjectID());
            contact.setCreatedDate(new Date());
            Integer result = contactService.saveContact(contact, null, true);
            if (result != null) {
                if (result == -1) {
                    jsonResponse.put(GoogleGadgetService.ERROR_MESSAGE, USER_NAME_EXIST);
                    jsonResponse.put(GoogleGadgetService.SAVED, false);
                } else if (result == -2) {
                    jsonResponse.put(GoogleGadgetService.ERROR_MESSAGE, EMAIL_EXIST);
                    jsonResponse.put(GoogleGadgetService.SAVED, false);
                } else {
                    Email emailRel = new Email();
                    emailRel.setSubject(emailSubject);
                    emailRel.setContent(emailDescription);
                    emailRel.setFromEmail(emailFromEmail);
                    emailRel.setToEmails(emailToEmail);
                    emailRel.setGeneratedGoogleID(emailEmailId);

                    boolean saveRelationResult = googleGadgetService.saveRelation(null, result,
                            RelationItem.TYPE_CONTACT, emailRel, GoogleGadgetService.TRUE.equals(linkToEmail));
                    if (!saveRelationResult) {
                        jsonResponse.put(GoogleGadgetService.ERROR_MESSAGE, GoogleGadgetService.RELATION_SAVED_FAILED);
                    }
                    jsonResponse.put(GoogleGadgetService.SAVED, true);
                }

            } else {
                jsonResponse.put(GoogleGadgetService.SAVED, false);
            }
        } else {
            jsonResponse.put(GoogleGadgetService.SAVED, false);
            jsonResponse.put(GoogleGadgetService.ERROR_MESSAGE, GoogleGadgetService.VALIDATION_FAILED);
        }


        return jsonResponse.toJSONString();
    }

    private boolean validate(HttpServletRequest leadData) {
        int errors = 0;
        if (isInvalid(leadData.getParameter(LAST_NAME))) {
            errors++;
        }
        return errors <= 0;
    }

    private boolean isInvalid(String param) {
        return param == null || param.equals("");
    }
}
