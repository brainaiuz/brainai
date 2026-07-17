package com.edatasite.workforce.gwt.core.client.form;


import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.FormItems;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomFormItemView extends AddCustomFormItemView {

    private CustomFormItemGrid itemView;

    public CustomFormItemView(Integer objectID, Integer fID, String formID, String name, boolean isPage) {
        super(Constants.ITEM_LIST + "_summary_" + formID, name != null ? name : wfmStrings.customForms());
        this.objectID = objectID;
        this.fID = fID;
        this.name = name;
        this.formID = formID;
        this.isPage = isPage;
        this.isSummary = true;
    }

    @Override
    protected void registerFields() {

        drawItemTable();

        drawAttributes();

        getCustomFieldUtil().drawCustomFields(this, objectID, true);

        show();
    }

    @Override
    protected void drawItemTable() {
        ItemTableSettingService.App.get().getColumnConfigs(formID, new AbstractAsyncCallback<HashMap<String, ColumnConfigs[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(HashMap<String, ColumnConfigs[]> result) {
                if (result != null && result.size() > 0) {
                    for (Map.Entry<String, ColumnConfigs[]> configMap : result.entrySet()) {
                        itemView = new CustomFormItemGrid(objectID, configMap.getKey(), formID, configMap.getValue(), 1000);
                        addField(configMap.getKey(), itemView, null, true);
                    }
                }
            }
        });
    }

    @Override
    protected void drawAttributes() {
        CommonService.App.get().getCustomFormAttributes(formID, new AbstractAsyncCallback<ArrayList<CustomFormAttributeItem>>() {
            @Override
            public void onFailure(final Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(final ArrayList<CustomFormAttributeItem> result) {
                if (!result.isEmpty()) {
                    for (final CustomFormAttributeItem item : result) {
                        if (Constants.UI_TYPE_APPROVAL_PROCESS.equals(item.getFieldType()) && approver == null) {
                            approver = new ChosenApproversWidget(formID, isCopy ? null : objectID);
                            approver.setEnabled(false);
                            selectApproverChange();
                            addField(item.getFieldId(), approver, item.getLabel());
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        CommonService.App.get().getCustomFormItem(objectID, fID, formID, false, null, null, null, null, new AbstractAsyncCallback<FormItems>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(FormItems result) {
                LoadingPanel.loading(false);
                item = result;
                if (result != null && result.getCustomFieldItems() != null) {
                    getCustomFieldUtil().fillCustomFieldsWithData(result.getCustomFieldItems(), true);
                    getLinkingUtil().getTaggingView().setSelectedRelations(result.getRelations());
                    getLinkingUtil().drawLinks();

                    hasApproval = result.getHasApproval();
                    showButtons();
                    pdfTool(result);
                }
            }
        });
    }

    @Override
    protected void approveOrRejectItem(String statusCode) {
        LoadingPanel.loading(true);
        CommonService.App.get().approveOrRejectCustomFormItem(objectID, statusCode, new AbstractAsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false);
                if (CUSTOM_FORM_ITEM_STATUS_APPROVED.equals(statusCode)) {
                    Info.show(wfmMessages.employeeStepHasBeenApprovedSuccessfully(), Info.Type.INFO);
                } else if (CUSTOM_FORM_ITEM_STATUS_REJECTED.equals(statusCode)) {
                    Info.show(wfmMessages.employeeStepHasBeenRejectedSuccessfully(), Info.Type.INFO);
                }
                closeTab();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CUSTOM_FORM_ITEM_UPDATE, result, CustomFormItemView.this);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CUSTOM_FORM_ITEM_APPROVAL, result, CustomFormItemView.this);
            }
        });
    }

    protected void showButtons() {
        addButton(createFooter(null));
        editButton.setVisible(true);
        if (hasApproval) {
            if (CUSTOM_FORM_ITEM_STATUS_SUBMITTED.equals(item.getStatusCode())
                    && item.getCurrentApproverId() != null
                    && item.getCurrentUserId() != null
                    && (item.getCurrentApproverId().equals(item.getCurrentUserId()) || Utils.hasRoles(Constants.ADMIN))) {
                approveButton.setVisible(true);
                rejectButton.setVisible(true);
            }
        }
        if (item != null && item.getRelations() != null && link != null) {
            link.setBadgeCount(item.getRelations().size());
        }
    }

    private HasLinks linkingUtil;

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(CustomFormItemView.this) {
                @Override
                protected boolean isActionEditing() {
                    return false;
                }

                @Override
                public Integer getRelationID() {
                    return objectID;
                }

                @Override
                public String getRelationType() {
                    return item.getFormID();
                }

                @Override
                public String getRelationName() {
                    return item.getAutoNumber() != null ? item.getAutoNumber() : item.getFormName() + ": " + objectID;
                }

                @Override
                public boolean hasNoSummaryTab() {
                    return true;
                }
            };
        }
        return linkingUtil;
    }
}
