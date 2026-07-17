package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Hasan Xo'janazarov
 * Date: 19.12.12
 * Time: 17:40
 * To change this template use File | Settings | File Templates.
 */
public class CampaignPopup extends KpiModal {
    private ArrayList<Integer> itemIDs;
    private String type;
    private CRMLookUp campaignSource;
    private WfmButton2 save, cancel;

    public CampaignPopup(String type) {
        this.type = type;
        setTitle(wfmStrings.campaign());
        setWidth(350);
        init();
    }

    private void init() {
        campaignSource = new CRMLookUp(CrmConstants.CRM_CAMPAIGN_ID);
        campaignSource.addStyleName(Constants.DEFAULT_WIDTH);
        add(campaignSource);

        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, clickEvent -> close());
        addButton(cancel);
        addButton(save);
    }

    private void save() {
        if(!Validation.validateLookUpRequired(campaignSource)){
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return;
        }
        save.setEnabled(false);
        cancel.setEnabled(false);
        LoadingPanel.loading(true);
        CRMService.App.get().saveLeadCampaign(getItemIDs(), campaignSource.getSelectedItemID(), type, new AbstractAsyncCallback<Void>() {
            public void failure(Throwable caught) {
                saved(false);
            }

            public void success(Void result) {
                saved(true);
            }
        });
    }

    private void saved(boolean success) {
        LoadingPanel.loading(false);
        save.setEnabled(true);
        cancel.setEnabled(true);
        if (success) {
            if (listRefresh != null) {
                listRefresh.refreshList();
            }
            close();
        }
    }

    public ArrayList<Integer> getItemIDs() {
        if (itemIDs == null) {
            itemIDs = new ArrayList<>();
        }
        return itemIDs;
    }

    private LeadListRefresh listRefresh;

    public interface LeadListRefresh {
        void refreshList();
    }

    public void setListRefresh(LeadListRefresh listRefresh) {
        this.listRefresh = listRefresh;
    }
}
