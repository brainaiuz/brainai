package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.ui.view.AddOpportunityView;
import com.edatasite.workforce.gwt.crm.client.ui.view.EditOpportunityForm;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 18:23:07
 * To change this template use File | Settings | File Templates.
 */
public class OpportunityAddSinksContainer extends SinksContainer {

    public OpportunityAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE, 257);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        Integer crmAccountID = null;
        String crmAccountName = null;
        Integer crmContactID = null;
        String crmContactName = null;
        Integer campaignID = null;
        Integer stageID = null;
        String campaignName = null;
        boolean required = false;
        boolean isCopying = false;
        boolean isCampaign = false;
        boolean fromContact = false;
        Integer fromContactID;
        boolean fromCustomForm = false;
        String formType = null;
        Integer customFormId = null;
        if (params.length == 5) {
            fromContactID = Integer.parseInt(params[2]);
            fromContact = true;
            addView(new AddOpportunityView(null, false, fromContactID));
        } else {
            if (params.length == 4 && "CONVERT".equals(params[1])) {
                fromCustomForm = params[1] != null && "CONVERT".equals(params[1]);
                formType = params[2];
                if (params[3] != null && params[3].matches(Constants.REGEX_INTEGER_POSITIVE)) {
                    customFormId = Integer.parseInt(params[3]);
                }
            } else if (params.length > 3) {
                if ("".equals(params[1])) {
                    crmAccountID = Integer.valueOf(params[2]);
                    crmAccountName = Utils.decrypt(params[3]);
                } else if ("true".equals(params[1])) {
                    isCampaign = true;
                    campaignID = Integer.parseInt(params[2]);
                    campaignName = params[3];
                }
                if (params.length > 4) {
                    crmContactID = Integer.valueOf(params[4]);
                    crmContactName = params[5];
                }
            } else if (params.length > 2) {
                isCopying = params[2] != null && COPY.equals(params[2]);
                required = params[2] != null && "REQUIRED".equals(params[2]);
                if (params[1] != null && params[1].matches(Constants.REGEX_INTEGER_POSITIVE)) {
                    id = Integer.parseInt(params[1]);
                } else if ("Kanban".equals(params[1])) {
                    stageID = Integer.parseInt(params[2]);
                }
            } else {
                if (params.length > 1 && params[1] != null && params[1].matches(Constants.REGEX_INTEGER_POSITIVE)) {
                    id = Integer.parseInt(params[1]);
                }
            }
        }
        if (!fromContact) {
            if (crmAccountID != null || crmContactID != null) {
                addView(new AddOpportunityView(id, crmAccountID, crmAccountName, crmContactID, crmContactName));
            } else if (isCampaign) {
                addView(new AddOpportunityView(isCampaign, campaignID, campaignName));
            } else if (fromCustomForm) {
                addView(new AddOpportunityView(formType, customFormId));
            } else if (stageID != null) {
                addView(new AddOpportunityView(id, stageID));
            } else if (id == null || isCopying) {
                addView(new AddOpportunityView(id, isCopying));
            } else {
                addView(new EditOpportunityForm(id));
            }
        }
    }
}
