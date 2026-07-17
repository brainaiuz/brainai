package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.gwt.client.client.rpc.ClientCurrency;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.server.app.GoogleGadgetLookUpService;
import com.edatasite.workforce.gwt.core.server.app.GoogleGadgetService;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 10/2/12
 * Time: 2:39 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
//@RequestMapping("/googleGadget/addOpportunity")
public class GoogleGadgetAddOpportunityController {


    @Autowired
    private GoogleGadgetService googleGadgetService;
    @Autowired
    private CRMService crmService;
    @Autowired
    @Qualifier("crmService")
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    GoogleGadgetLookUpService gadgetLookUpService;
    @Autowired
    AllInOneService allInOneService;


    private static final String OPPORTUNITY_ASSIGNEE_ITEMS = "assigneeItems";
    private static final String OPPORTUNITY_STAGE_ITEMS = "stageItems";
    private static final String OPPORTUNITY_COMPAIGN_ITEMS = "compaignItems";
    private static final String OPPORTUNITY_LEAD_ITEMS = "leadItems";
    private static final String OPPORTUNITY_CURRENCY_NAME = "currencyName";

    private static final String DEFAULT_FORM = "getDefaultForm";
    private static final String SAVE_OPPORTINITY = "saveOpportunity";

    private static final String OPPORTUNITY_ASSIGNEE = "opportunityAssignee";
    private static final String OPPORTUNITY_NAME = "opportunityName";
    private static final String OPPORTUNITY_ACCOUNT_NAME = "opportunityAccountName";
    private static final String OPPORTUNITY_CONTACT_NAME = "opportunityContactName";
    private static final String OPPORTUNITY_DESCRIPTION = "opportunityDescription";
    private static final String OPPORTUNITY_AMOUNT = "opportunityAmount";
    private static final String OPPORTUNITY_DATE = "opportunityDate";
    private static final String OPPORTUNITY_STAGE = "opportunityStage";
    private static final String OPPORTUNITY_COMPAIGN_SOURCE = "opportunityCompaignSource";
    private static final String OPPORTUNITY_LEAD_SOURCE = "opportunityLeadSource";
    private static final String OPPOROTUNIY_PROBABILITY = "opportunityProbability";
    private static final String OPPORTUNITY_EXPECED_REVENUE = "opportunityExpectedRevenue";


    private static final String LINK_TO_EMAIL = "linkToEmail";
    private static final String EMAIL_SUBJECT = "emailSubject";
    private static final String EMAIL_DESCIPTION = "emailDescription";
    private static final String EMAIL_FROM_EMAIL = "emailFromEmail";
    private static final String EMAIL_TO_EMAIL = "emailToEmail";
    private static final String EMAIL_EMAIL_ID = "emailEmailId";

    private static final String SALES_QUOTE_CUSTOMER = "salesQuoteCustomer";
    private static final String SEARCH_KEYWORD = "searchKeyword";
    private static final String DATA_LIST = "dataList";
    private static final String LOOK_UP_TYPE = "lookUpType";
    private static final String LOOK_UP_ACCOUNT_ID="accountId";
    private static final Integer LIMIT = 200;


    @RequestMapping(value = "/googleGadget/addOpportunity")
    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(GoogleGadgetService.JSON_CONTENT_TYPE);
        PrintWriter writer = response.getWriter();
        boolean isSigned = googleGadgetService.checkSignedRequest(request);


        String openSocialViewerId = request.getParameter(GoogleGadgetService.OPEN_SOCIAL_VIEWER_ID);
        Integer companyId = googleGadgetService.getInteger(request.getParameter(GoogleGadgetService.COMPANY_ID));

        if (isSigned = true) {
            boolean isUserExist = googleGadgetService.googleGadgetSignIn(openSocialViewerId, companyId);
            if (isUserExist) {
                if (!isInvalid(request.getParameter(DEFAULT_FORM)) && request.getParameter(DEFAULT_FORM).equals(GoogleGadgetService.TRUE)) {
                    return getForm();
                } else if (!isInvalid(request.getParameter(LOOK_UP_TYPE))) {
                    String keyword = "";
                    Integer accountId=null;
                    if (!isInvalid(request.getParameter(SEARCH_KEYWORD))) {
                        keyword = request.getParameter(SEARCH_KEYWORD);
                    }
                    if (!isInvalid(request.getParameter(LOOK_UP_ACCOUNT_ID))) {
                        accountId = Integer.parseInt(request.getParameter(LOOK_UP_ACCOUNT_ID).split("::")[0]);
                    }
                    writer.write(getJsonString(gadgetLookUpService.getItems(request.getParameter(LOOK_UP_TYPE), companyId, null, keyword,accountId)));
                } else if (!isInvalid(request.getParameter(SAVE_OPPORTINITY)) && request.getParameter(SAVE_OPPORTINITY).equals(GoogleGadgetService.TRUE)) {
                    writer.write(saveOpportunity(request));
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
        ModelAndView modelAndView = new ModelAndView("googleGadgetAddOpportunity");

        OpportunityListItem data = getOpportunityData();
        for (SelectItem item : data.getCurrencies()) {
            if (item.getId().equals(data.getCurrencyId())) {

            }
        }


        modelAndView.addObject(OPPORTUNITY_ASSIGNEE_ITEMS, data.getAssignees());
        modelAndView.addObject(OPPORTUNITY_STAGE_ITEMS, data.getStages());
        modelAndView.addObject(OPPORTUNITY_LEAD_ITEMS, data.getLeadSources());
        modelAndView.addObject(OPPORTUNITY_CURRENCY_NAME, data.getLeadSources());

        return modelAndView;
    }

    private OpportunityListItem getOpportunityData() {
        return crmService.editOpportunity(null);

    }


    private String saveOpportunity(HttpServletRequest opportunityData) {
        JSONObject jsonResponse = new JSONObject();
        if (validate(opportunityData)) {

            Integer opportunityAssigneeId = Integer.parseInt(opportunityData.getParameter(OPPORTUNITY_ASSIGNEE));
            String opportunityName = opportunityData.getParameter(OPPORTUNITY_NAME);
            Integer opportunityAccountId = Integer.parseInt(opportunityData.getParameter(OPPORTUNITY_ACCOUNT_NAME).split("::")[0]);
            Integer opportunityContactId = !isInvalid(opportunityData.getParameter(OPPORTUNITY_CONTACT_NAME)) ? Integer.parseInt(opportunityData.getParameter(OPPORTUNITY_CONTACT_NAME).split("::")[0]) : null;
            String opportunityDescription = opportunityData.getParameter(OPPORTUNITY_DESCRIPTION);
            String opportunityAmount = opportunityData.getParameter(OPPORTUNITY_AMOUNT);
            String opportunityDateString = opportunityData.getParameter(OPPORTUNITY_DATE);
            Integer opportunityStageId = Integer.parseInt(opportunityData.getParameter(OPPORTUNITY_STAGE));
            Integer opportunityCompaignSourceId = !isInvalid(opportunityData.getParameter(OPPORTUNITY_COMPAIGN_SOURCE)) ? Integer.parseInt(opportunityData.getParameter(OPPORTUNITY_COMPAIGN_SOURCE).split("::")[0]) : null;
            Integer opportunityLeadSourceId = !isInvalid(opportunityData.getParameter(OPPORTUNITY_LEAD_SOURCE)) ? Integer.parseInt(opportunityData.getParameter(OPPORTUNITY_LEAD_SOURCE)) : null;
            String opportunityProbability = opportunityData.getParameter(OPPOROTUNIY_PROBABILITY);
            String opportunityExpectedRevenue = opportunityData.getParameter(OPPORTUNITY_EXPECED_REVENUE);

            String emailSubject = opportunityData.getParameter(EMAIL_SUBJECT);
            String emailDescription = opportunityData.getParameter(EMAIL_DESCIPTION);
            String emailFromEmail = opportunityData.getParameter(EMAIL_FROM_EMAIL);
            String emailToEmail = opportunityData.getParameter(EMAIL_TO_EMAIL);
            String emailEmailId = opportunityData.getParameter(EMAIL_EMAIL_ID);

            String linkToEmail = opportunityData.getParameter(LINK_TO_EMAIL);


            OpportunityListItem item = new OpportunityListItem();
            NumberData number = crmService.generateOpportunityNumber();
            ClientCurrency currency = crmServiceLocal.getClientCurrency();

            ArrayList<HistoryListItem> notes = new ArrayList<>();
            HistoryListItem historyListItem = new HistoryListItem(opportunityDescription);
            historyListItem.setVisibility(true);
            notes.add(historyListItem);

            SimpleDateFormat dateFormat = new SimpleDateFormat(GoogleGadgetService.DATE_PATTERN, Locale.US);
            Date opportunityDate = new Date();
            try {
                opportunityDate = dateFormat.parse(opportunityDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            item.setOpportunityName(opportunityName);
            item.setNumberData(number);
            item.setNotes(notes);
            item.setAmount(parseByNumberFormat(opportunityAmount).doubleValue());
            item.setCurrencyId(currency.getUserCurrencyId());
            item.setClosingDate(opportunityDate);

            item.setAccountId(opportunityAccountId);
            item.setContactId(opportunityContactId);
            item.setStageId(opportunityStageId);

            item.setProbability(parseByNumberFormat(opportunityProbability).floatValue());
            item.setExpectedRevenue(parseByNumberFormat(opportunityExpectedRevenue).doubleValue());

            item.setLeadSourceId(opportunityLeadSourceId);
            item.setCampaignId(opportunityCompaignSourceId);
            item.setAssigneeId(opportunityAssigneeId);


            Integer objectId = crmService.saveOpportunity(item);
            if (objectId != null) {
                if (GoogleGadgetService.TRUE.equals(linkToEmail)) {
                    Email email = new Email();
                    email.setSubject(emailSubject);
                    email.setContent(emailDescription);
                    email.setFromEmail(emailFromEmail);
                    email.setToEmails(emailToEmail);
                    email.setGeneratedGoogleID(emailEmailId);

                    boolean saveRelationResult = googleGadgetService.saveRelation(null,
                            objectId, RelationItem.TYPE_OPPORTUNITY, email, GoogleGadgetService.TRUE.equals(linkToEmail));
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

    private boolean validate(HttpServletRequest opportunityData) {
        int errors = 0;

        if (isInvalid(opportunityData.getParameter(OPPORTUNITY_ASSIGNEE))) {
            errors++;
        }
        if (isInvalid(opportunityData.getParameter(OPPORTUNITY_NAME))) {
            errors++;
        }
        if (isInvalid(opportunityData.getParameter(OPPORTUNITY_ACCOUNT_NAME))) {
            errors++;
        }
        if (isInvalid(opportunityData.getParameter(OPPORTUNITY_DATE))) {
            errors++;
        }
        if (isInvalid(opportunityData.getParameter(OPPORTUNITY_STAGE))) {
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

    private Number parseByNumberFormat(String text) {
        DecimalFormat nf = new DecimalFormat("#,##0.00");
        try {
            return nf.parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    private String getJsonString(SelectItem[] items) {
        JSONObject jsonResponse = new JSONObject();

        jsonResponse.put(DATA_LIST, wrapToArrayList(items));

        return jsonResponse.toJSONString();
    }

    private ArrayList<String> wrapToArrayList(SelectItem[] selectItems) {
        ArrayList<String> wrap = new ArrayList<>();
        if (selectItems != null) {
            for (SelectItem item : selectItems) {
                wrap.add(item.getId() + "::" + item.getName());
            }
        }
        return wrap;
    }

}
