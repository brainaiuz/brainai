package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.server.app.GoogleGadgetService;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 10/2/12
 * Time: 2:39 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class GoogleGadgetAddCaseController {


    @Autowired
    private GoogleGadgetService googleGadgetService;
    @Autowired
    private CRMService crmService;
    @Autowired
    @Qualifier("crmService")
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    AllInOneService allInOneService;

    private static final String CASE_SUBJECT = "caseSubject";
    private static final String CASE_DESCRIPTION = "caseDescription";
    private static final String STATUS_ITEMS = "statusItems";
    private static final String CASE_STATUS = "caseStatus";

    private static final String DEFAULT_FORM = "getDefaultForm";
    private static final String SAVE_CASE = "saveCase";
    private static final String RELATIONS = "relations";
    private static final String LINK_TO_EMAIL = "linkToEmail";
    private static final String EMAIL_SUBJECT = "emailSubject";
    private static final String EMAIL_DESCIPTION = "emailDescription";
    private static final String EMAIL_FROM_EMAIL = "emailFromEmail";
    private static final String EMAIL_TO_EMAIL = "emailToEmail";
    private static final String EMAIL_EMAIL_ID = "emailEmailId";

    @RequestMapping(value = "/googleGadget/addCase")
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
                } else if (request.getParameter(SAVE_CASE) != null && request.getParameter(SAVE_CASE).equals(GoogleGadgetService.TRUE)) {
                    writer.write(saveCase(request));
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
        ModelAndView modelAndView = new ModelAndView("googleGadgetAddCase");
        modelAndView.addObject(STATUS_ITEMS, getStatus());
        return modelAndView;
    }

    private SelectItem[] getStatus() {
        return crmServiceLocal.getCaseStatus();
    }

    private String saveCase(HttpServletRequest caseData) {
        JSONObject jsonResponse = new JSONObject();
        if (validate(caseData)) {

            String subject = caseData.getParameter(CASE_SUBJECT);
            String description = caseData.getParameter(CASE_DESCRIPTION);
            Integer status = Integer.parseInt(caseData.getParameter(CASE_STATUS));

            String emailSubject = caseData.getParameter(EMAIL_SUBJECT);
            String emailDescription = caseData.getParameter(EMAIL_DESCIPTION);
            String emailFromEmail = caseData.getParameter(EMAIL_FROM_EMAIL);
            String emailToEmail = caseData.getParameter(EMAIL_TO_EMAIL);
            String emailEmailId = caseData.getParameter(EMAIL_EMAIL_ID);

            String linkToEmail = caseData.getParameter(LINK_TO_EMAIL);
            EdsReference caseOriginsList = referenceManager.findReference(EdsCase._CASE_ORIGIN, EdsCase.EMAIL);
            EdsUser user = userManager.getUser();
            EdsCrmContact crmContact = crmContactManager.getContactByPrimaryEmail(user.getEmail());


            CaseItem item = new CaseItem();
            item.setSubject(subject);
            item.setDescription(description);
            item.setStatus(new SelectItem(status));
            item.setCaseOriginId(caseOriginsList.getObjectID());
            item.setAccountId(null);
            item.setLeadId(null);
            if (crmContact != null) {
                item.setCrmContactID(crmContact.getObjectID());
                item.setCrmContact(crmContact.getName());
            }
            Integer objectId = crmService.saveCase(item, false).getId();
            if (objectId != null) {
                if (!isArrayInvalid(caseData.getParameterValues(RELATIONS)) || GoogleGadgetService.TRUE.equals(linkToEmail)) {
                    Email email = new Email();
                    email.setSubject(emailSubject);
                    email.setContent(emailDescription);
                    email.setFromEmail(emailFromEmail);
                    email.setToEmails(emailToEmail);
                    email.setGeneratedGoogleID(emailEmailId);

                    boolean saveRelationResult = googleGadgetService.saveRelation(caseData.getParameterValues(RELATIONS),
                            objectId, RelationItem.TYPE_CASE, email, GoogleGadgetService.TRUE.equals(linkToEmail));
                    if (!saveRelationResult) {
                        jsonResponse.put(GoogleGadgetService.ERROR_MESSAGE, GoogleGadgetService.RELATION_SAVED_FAILED);
                    }
                }
                jsonResponse.put(GoogleGadgetService.SAVED, true);
            } else {
                jsonResponse.put(GoogleGadgetService.SAVED, false);
            }
        } else {
            jsonResponse.put(GoogleGadgetService.SAVED, false);
            jsonResponse.put(GoogleGadgetService.ERROR_MESSAGE, GoogleGadgetService.VALIDATION_FAILED);
        }


        return jsonResponse.toJSONString();
    }

    private boolean validate(HttpServletRequest taskData) {
        int errors = 0;
        if (isInvalid(taskData.getParameter(CASE_SUBJECT))) {
            errors++;
        }
        if (isInvalid(taskData.getParameter(CASE_DESCRIPTION))) {
            errors++;
        }
        if (isInvalid(taskData.getParameter(CASE_STATUS))) {
            errors++;
        }

        return errors <= 0;
    }

    private boolean isInvalid(String param) {
        return param == null || param.equals("");
    }

    private boolean isArrayInvalid(String[] array) {
        return array == null || array.length <= 0;
    }

}
