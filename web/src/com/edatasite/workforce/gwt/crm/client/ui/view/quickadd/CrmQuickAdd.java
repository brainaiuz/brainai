package com.edatasite.workforce.gwt.crm.client.ui.view.quickadd;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.shortcut.ShortcutItem;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Heading;

import java.util.Map;

/**
 * Author: Azazello
 * Date: 2/8/2018
 * Time: 8:39 PM
 */
public class CrmQuickAdd extends KpiSideNavBox implements Constants {
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private static WfmButton2 save;
    private final String form_id;
    private RelationItem[] relationItems;
    private CrmQuickAddForm quickAddForm;
    private Integer defaultStatusId;
    private Integer crmAccountId;

    public CrmQuickAdd(String form_id, RelationItem... relationItems) {
        super(KpiSideNavBox.DEFAULT_WIDTH);
        this.form_id = form_id;
        this.relationItems = relationItems;
        initialize();
    }

    public CrmQuickAdd(String form_id, Integer defaultStatusId, RelationItem... relationItems) {
        super(KpiSideNavBox.DEFAULT_WIDTH);
        this.form_id = form_id;
        this.defaultStatusId = defaultStatusId;
        this.relationItems = relationItems;
        initialize();

    }

    public CrmQuickAdd(String form_id, Integer crmAccountId, boolean relateContact) {
        super(KpiSideNavBox.DEFAULT_WIDTH);
        this.form_id = form_id;
        this.crmAccountId = crmAccountId;
        initialize();
    }

    private void initialize() {
        Heading header = new Heading(HeadingSize.H1);
        header.setText(getHeaderText());
        addHeader(header);

        quickAddForm = getQuickAddForm();
        addBody(quickAddForm);

        addOpeningHandler(event -> quickAddForm.getQuickData());

        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        save.ensureDebugId(form_id + "_opportunity_quick_add");
        save.addClickHandler(event -> {
            enableButtons(false);
            if (quickAddForm.validate()) {
                quickAddForm.save();
            } else {
                enableButtons(true);
            }
        });
        addFooter(save);

        quickAddForm.setCommand(new ExtendedCommand() {
            @Override
            public void execute(Integer id) {
                enableButtons(true);
                remove();
                refreshStatistics();
            }
        });
        show();
    }

    private void refreshStatistics() {
        ShortcutItem shortcutItem = null;
        Map<String, ShortcutItem> shortcuts = MainLayout.get().getCurrentContainer().getItemsByView();
        if (LayoutRPC.CASE_FORM.equals(form_id)) {
            shortcutItem = shortcuts.get(CASE_LIST);
        }
        if (LayoutRPC.OPPORTUNITY_FORM.equals(form_id)) {
            shortcutItem = shortcuts.get(OPPORTUNITY_LIST);
        }
        if (LayoutRPC.ACCOUNT_FORM.equals(form_id)) {
            shortcutItem = shortcuts.get(CRM_ACCOUNT_LIST);
        }
        if (LayoutRPC.CAMPAIGN_FORM.equals(form_id)) {
            shortcutItem = shortcuts.get(CAMPAIGN_LIST);
        }
        if (LayoutRPC.CONTACT_FORM.equals(form_id)) {
            shortcutItem = shortcuts.get(CRM_CONTACT_LIST);
        }
        if (LayoutRPC.LEAD_FORM.equals(form_id)) {
            shortcutItem = shortcuts.get(LEAD_LIST);
        }
        if (shortcutItem != null && shortcutItem.getStatisticCommand() != null) {
            shortcutItem.getStatisticCommand().execute();
        }
    }

    public static void enableButtons(boolean enable) {
        save.setEnabled(enable);
    }

    private String getHeaderText() {
        if (LayoutRPC.CASE_FORM.equals(form_id)) {
            return Property.get(CASE_LIST, wfmStrings.addMess(), wfmStrings.caseID());
        }
        if (LayoutRPC.OPPORTUNITY_FORM.equals(form_id)) {
            return Property.get(Constants.Opportunities, wfmStrings.addMess(), wfmStrings.opportunity());
        }
        if (LayoutRPC.ACCOUNT_FORM.equals(form_id)) {
            return Property.get(Constants.CRM_ACCOUNT_LIST, wfmStrings.addMess(), wfmStrings.company());
        }
        if (LayoutRPC.CAMPAIGN_FORM.equals(form_id)) {
            return crmStrings.addCampaign();
        }
        if (LayoutRPC.CONTACT_FORM.equals(form_id)) {
            return Property.get(Constants.Contacts, wfmStrings.addMess(), wfmStrings.contact());
        }
        if (LayoutRPC.LEAD_FORM.equals(form_id)) {
            return Property.get(Constants.LEADS, wfmStrings.addMess(), wfmStrings.lead());
        }
        if (LayoutRPC.SOLUTION_FORM.equals(form_id)) {
            return crmStrings.addSolution();
        }
        return null;
    }

    private CrmQuickAddForm getQuickAddForm() {
        if (LayoutRPC.CASE_FORM.equals(form_id)) {
            return new CaseQuickAddForm(defaultStatusId, relationItems);
        }
        if (LayoutRPC.OPPORTUNITY_FORM.equals(form_id)) {
            return new OpportunityQuickAddForm(defaultStatusId, relationItems);
        }
        if (LayoutRPC.ACCOUNT_FORM.equals(form_id)) {
            return new CrmAccountQuickAddForm(relationItems);
        }
        if (LayoutRPC.CAMPAIGN_FORM.equals(form_id)) {
            return new CampaignQuickAddForm(relationItems);
        }
        if (LayoutRPC.SOLUTION_FORM.equals(form_id)) {
            return new SolutionQuickAddForm(relationItems);
        }
        if (LayoutRPC.CONTACT_FORM.equals(form_id) || LayoutRPC.LEAD_FORM.equals(form_id)) {


            ContactQuickAddForm contactQuickAddForm = new ContactQuickAddForm(LayoutRPC.LEAD_FORM.equals(form_id) ? ContactListItem.LEAD_CONTACT : ContactListItem.CRM_CONTACT, crmAccountId, relationItems);
            contactQuickAddForm.setLeadStatus(defaultStatusId);
            return contactQuickAddForm;
        }
        return null;
    }

}
