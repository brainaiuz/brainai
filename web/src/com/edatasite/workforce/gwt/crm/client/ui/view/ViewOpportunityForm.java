package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomFormItemGrid;
import com.edatasite.workforce.gwt.core.client.interfaces.NoColapse;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.view.OpportunityPercentageStageModal;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityItem;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.crm.client.ui.PhonePopup;
import com.edatasite.workforce.gwt.crm.client.ui.view.tabPanels.CrmActivityGrid;
import com.edatasite.workforce.gwt.crm.client.ui.view.widgets.OpportunitySubItemGrid;
import com.edatasite.workforce.gwt.crm.client.ui.view.widgets.StageHistoryGrid;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.upload.GWTFileUploadDialog;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.task.client.ui.quickadd.TaskQuickAddView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.edatasite.workforce.gwt.core.client.rpc.RelationItem.*;

/**
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 17:39:36
 */
public class ViewOpportunityForm extends AddOpportunityView implements Constants, FormHasCustomFieldInterface, HasLinksInterface, NoColapse {
    private static boolean contractUploaded = false;
    private HTML backupAssignee, opportunityNumber, numbering, labelCurrent, opportunityName, amount, campaign, probability, type, expectedRevenue, createdDate, nextStep, project, closeDateHtml, stageHtml, leadSourceHtml, approvers;
    private String statusCode;
    private WfmButton2 submitButton, approveButton, declineButton;
    private FlowPanel contactPrimaryPhone;
    private Anchor assignee, accountName, contactName, contactPrimaryEmail, rfq;
    private StageHistoryGrid stageHistoryGrid;
    private NoteWidget noteWidget;
    private Integer actionType; // FOR OUTLOOK
    private MaterialLink toProject;
    private MaterialDropDown convert;
    private HasLinks linkingUtil;
    private FooterInformer link;
    private final AtomicBoolean firstClick = new AtomicBoolean(true);
    private MaterialLink logCall;
    private MaterialLink addEvent;
    private WfmMessageBox changeStageMessageBox;
    private SplitButton printPdfSplitButton;
    private MaterialDropDown editButton;
    private OpportunitySubItemGrid itemView;

    public ViewOpportunityForm(Integer objectId) {
        super("viewopportunity");
        boolean tabName = (Utils.hasPermission(CRM_ACTIVITIES_LIST, CRM_MESSAGE_CENTER, CRM_TASKS_LIST, CRM_SALES_QUOTE_LIST,
                CRM_SALES_ORDER_LIST, CRM_OPPORTUNITIES_EXPENSE_CLAIM_LIST, CRM_OPPORTUNITIES_EXPENSE_CLAIM_LIST, ACCOUNTING_REQUEST_FOR_QUOTE_LIST));
        setDescription(!tabName ? null : property.getSingular(wfmStrings.summaryView(), wfmStrings.opportunity()));
        if (objectId != null) {
            setDescription(property.getSingular(wfmStrings.summaryView(), wfmStrings.opportunity()));
            this.objectId = objectId;
        }
    }

    public ViewOpportunityForm(Integer id, String action) {
        this(id);
        if (action != null && action.matches(Constants.REGEX_INTEGER)) {
            this.actionType = Integer.parseInt(action);
        }
    }

    private static boolean validateForClosedWon(OpportunityListItem item) {
        return !(item.getNumberData() == null || item.getOpportunityName() == null || item.getClosingDate() == null || item.getProbability() == null || item.getProbability() < 100f);
    }

    static void goToConvert(final OpportunityListItem item, final Widget widget) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.information());
        messageBox.setMessage(Property.get(Constants.PROJECT, wfmStrings.convertToo(), wfmStrings.project()) + " \"" + item.getOpportunityName() + "\" ?");
        final GWTFileUploadDialog uploadDialog = new GWTFileUploadDialog(F_PROJECT, null, null);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                if (validateForClosedWon(item)) {
                    if (item.getRequireContractUpload()) {
                        uploadDialog.onLoadCommand(() -> {
                            HashMap<Integer, FileResource> files = uploadDialog.getUploadedFiles();
                            if (files != null && files.size() > 0) {
                                FileItem contract = new FileItem();
                                for (Integer fileID : files.keySet()) {
                                    contract.setId(fileID);
                                    contract.setFileName(files.get(fileID).getName());
                                }
                                if (!contractUploaded) {
                                    contractUploaded = true;
                                    convertOpportunityToProject(item, contract, widget);
                                }
                            }
                        });
                        uploadDialog.initialize(true);
                    } else {
                        convertOpportunityToProject(item, null, widget);
                    }
                } else {
                    WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, true);
                    messageBox.setTitle(wfmStrings.error());
                    String error = item.getProbability() == null || item.getProbability() < 100f ? wfmStrings.youCanOnlyConvertOppotunitiesWithStatusOfClosedWon() : wfmStrings.sureEnteredAllData();
                    messageBox.setMessage(error);
                    messageBox.open();
                }
            }
        });
        messageBox.open();
    }

    private static void convertOpportunityToProject(OpportunityListItem item, FileItem contract, final Widget widget) {
        CRMService.App.get().opportunityConvertToProject(item.getObjectId(), item.getAccountId(), contract, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(final Integer _objectId) {
                Info.show(Property.get(Constants.Opportunities, crmStrings.opportunityConverted(), wfmStrings.opportunity()) + " " + Property.get(Constants.PROJECT, crmStrings.toProject(), wfmStrings.project()), Info.Type.INFO);
                if (_objectId != null) {
                    String editProject = GWT.getHostPageBaseURL() + "ProjectManagement.html#" + Constants.PROJECT + "|edit/" + _objectId;
                    Window.open(editProject, "_blank", "");
                }
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_OPPORTUNITY_ADD_EDIT, _objectId, widget);
            }
        });
    }

    private static void setCrmAccountBalance(OpportunityListItem item, MaterialLink customerBalanceLink) {
        CRMService.App.get().getCrmAccountBalance(item.getAccountId(), new AbstractAsyncCallback<Double>() {
            @Override
            public void failure(Throwable throwable) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Double customerBalance) {
                String balance = numberFormat.format(customerBalance);
                customerBalanceLink.setText(balance);
                customerBalanceLink.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("customerBalance|customerBalance/" + item.getCrmAccountItem().getObjectId() + "/" + CrmAccountItem.CUSTOMER,
                        wfmStrings.balance() + ": " + item.getCrmAccountItem().getName()));
            }
        });
    }

    @Override
    public void registerFields() {
        drawForm();
    }

    private void drawForm() {
        FlowPanel fp = new FlowPanel();
        itemView = new OpportunitySubItemGrid(objectId);
        fp.add(itemView);
        opportunityReceiptTable = new OpportunityReceiptTable();
        opportunityReceiptTable.addStyleName("totalsTable");
        fp.add(opportunityReceiptTable);
        initTotalTableWidgets();
        backupAssignee = initHTML();
        project = initHTML();
        opportunityNumber = initHTML();
        stageHtml = initHTML();
        closeDateHtml = initHTML();
        leadSourceHtml = initHTML();
        numbering = initHTML();
        approvers = initHTML();

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_OPPORTUNITY_LOAD_STAGE_HISTORY, ViewOpportunityForm.this, (sender, args) -> {
            stageHistoryGrid.refresher();
        });

        stage = new DataListBox();
        stage.setEnabled(false);
        if (Utils.hasPermission(CRM_OPPORTUNITY_CHANGE_STAGE, CRM_EDIT_OPPORTUNITIES)) {
            stage.setEnabled(true);

            stage.addValueChangeHandler(valueChangeEvent -> {
                if (stage.getSelectedItem() != null && stage.getSelectedItem().isDraggable() && item.isDraggable()) {
                    if (Utils.isDoubleMessageEnable()) {
                        changeStageMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        changeStageMessageBox.setMessage(crmMessages.doYouWantToChangeStatusTo(stage.getSelectedItem().getName()));
                        changeStageMessageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                if ("0".equals(stage.getSelectedItem().getDescription()) || stage.getSelectedItem().isSelected()) {
                                    Integer previousStageId = item.getStageId();
                                    item.setStageId(stage.getSelectedId());
                                    new OpportunityPercentageStageModal(item, stage.getSelectedItem().isSelected(), "0".equals(stage.getSelectedItem().getDescription()), true);
                                    WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_OPPORTUNITY_LIST_RELOAD, ViewOpportunityForm.this, (sender, args) -> {
                                        if ((boolean) args) {
                                            stageHistoryGrid.refresher();
                                        } else {
                                            stage.setSelected(previousStageId);
                                            item.setStageId(previousStageId);
                                        }
                                    });
                                } else {
                                    onChangeField(OpportunityListItem.STAGE);
                                }
                            }

                            @Override
                            public void onCancel() {
                                LoadingPanel.loading(false);
                                stage.setSelected(item.getStageId());
                            }
                        });

                        changeStageMessageBox.setTitle(wfmStrings.warning());
                        changeStageMessageBox.open();
                    } else {
                        if ("0".equals(stage.getSelectedItem().getDescription()) || stage.getSelectedItem().isSelected()) {
                            Integer previousStageId = item.getStageId();
                            new OpportunityPercentageStageModal(item, stage.getSelectedItem().isSelected(), "0".equals(stage.getSelectedItem().getDescription()), true);
                            WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_OPPORTUNITY_LIST_RELOAD, ViewOpportunityForm.this, (sender, args) -> {
                                if ((boolean) args) {
                                    stageHistoryGrid.refresher();
                                } else {
                                    stage.setSelected(previousStageId);
                                    item.setStageId(previousStageId);
                                }
                            });
                        } else {
                            onChangeField(OpportunityListItem.STAGE);
                        }
                    }
                } else {
                    Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                    stage.setSelected(item.getStageId());
                }
            });
        }

        opportunityName = initHTML();
        labelCurrent = initHTML();
        amount = initHTML();

        closingDate = new DateTimePicker(false, true);
        closingDate.dueDate.setEnabled(false);
        if (Utils.hasPermission(CRM_EDIT_OPPORTUNITIES)) {
            closingDate.dueDate.addValueChangeHandler(changeEvent -> onChangeField(OpportunityListItem.CLOSING_DATE));
            closingDate.dueDate.setEnabled(true);
        }

        accountName = new Anchor(wfmStrings.notAvailable());
        contactName = new Anchor(wfmStrings.notAvailable());
        assignee = new Anchor(wfmStrings.notAvailable());
        contactPrimaryEmail = new Anchor(wfmStrings.notAvailable());
        contactPrimaryPhone = new FlowPanel();
        accountName.addClickHandler(event -> {
            if (item.getCrmAccountItem() != null && item.getCrmAccountItem().getObjectId() != null) {
                if (Utils.isAccounting()) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("client|summary/" + item.getCrmAccountItem().getObjectId(), item.getAccountNumber(), item.getAccount());
                } else if (Utils.hasPermission(CRM_ACCOUNTS_SUMMARY)) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("account|summary/" + item.getCrmAccountItem().getObjectId(), item.getAccountNumber(), item.getAccount());
                }
            }
        });
        customerBalanceLink = new MaterialLink("");
        customerBalanceLink.setHref("javaScript:void(0)");
        customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");

        assignee.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("employee|summary/" + item.getAssigneeId(), item.getAssignee()));
        contactName.addClickHandler(event -> {
            if (item.getContactId() != null && Utils.hasPermission(PermissionConstants.CRM_CONTACTS_SUMMARY)) {
                SinksContainerFactory.entryPoint.onHistoryChanged("contact|summary/" + item.getContactId() + "//" + item.getAccountId(), item.getContact());
            }
        });
        contactPrimaryEmail.addClickHandler(clickEvent -> {
            if (Utils.hasPermission(PermissionConstants.CRM_OPPORTUNITY_EMAIL_COMPOSE)) {
                if (item.getContactId() != null) {
                    if (!item.isContactEmailOptOut()) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/" + item.getContactPrimaryEmail() + "/" + RelationItem.TYPE_OPPORTUNITY + "/" + item.getObjectId() + "/" + item.getOpportunityName());
                    } else {
                        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.OK, crmMessages.theEmailOutIsEnabled());
                        messageBox.setTitle(wfmStrings.information());
                        messageBox.open();
                    }
                }
            } else {
                Info.warn(wfmStrings.youDontHavePermission());
            }
        });
        probability = initHTML();
        type = initHTML();
        nextStep = initHTML();
        expectedRevenue = initHTML();
        createdDate = initHTML();

        campaign = initHTML();
        noteWidget = new NoteWidget(objectId, CrmConstants.CRM_OPPORTUNITY);
        crmActivityGrid = new CrmActivityGrid(objectId, RelationItem.TYPE_OPPORTUNITY);
        GeneralFileUpload fileUpload = new GeneralFileUpload(F_OPPORTUNITY, objectId, objectId);
        rfq = new Anchor(Property.getShortName(Constants.REQUEST_FOR_QUOTE, wfmStrings.relatedRFQ(), wfmStrings.requestForQuote()));

        drawItemTable();

        addTitleField(OPPORTUNITY_INFORMATION, property.getSingular(wfmStrings.basicDetails(), wfmStrings.opportunity()));

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE) != null) {
            addField(CRM_OPPORTUNITY_ASSIGNEE, assignee, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE).getTitle() : wfmStrings.assignee()));
        } else {
            addField(CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE, assignee, getTitle(wfmStrings.assignee()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE) != null) {
            addField(CRM_OPPORTUNITY_BACKUP_ASSIGNEE, backupAssignee, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE).getTitle() : wfmStrings.backupAssignee()));
        } else {
            addField(CRM_OPPORTUNITY_BACKUP_ASSIGNEE, backupAssignee, getTitle(wfmStrings.backupAssignee()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT_FIELD) != null) {
            addField(PROJECT_FIELD, project, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT_FIELD).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT_FIELD).getTitle() : Property.get(Constants.PROJECT, wfmStrings.project())));
        } else {
            addField(PROJECT_FIELD, project, getTitle(Property.get(Constants.PROJECT, wfmStrings.project())));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NUMBER) != null) {
            addField(CRM_OPPORTUNITY_NUMBER, numbering, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NUMBER).getTitle() : wfmStrings.number()));
        } else {
            addField(CRM_OPPORTUNITY_NUMBER, numbering, getTitle(wfmStrings.number()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NAME) != null) {
            addField(CRM_OPPORTUNITY_NAME, opportunityName, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NAME).getTitle() : wfmStrings.name()));
        } else {
            addField(CRM_OPPORTUNITY_NAME, opportunityName, getTitle(wfmStrings.name()));
        }
        addField(APPROVERS, approvers, wfmStrings.approvers());

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME) != null) {
            FormGroup clientField = new FormGroup(accountName);
            Div clientFieldLabel = clientField.getGroupLabel();
            clientFieldLabel.addStyleName("label-group");

            Span spanElement = new Span(getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME).getTitle() : wfmStrings.customer()));
            spanElement.setId("customer-label");
            clientFieldLabel.add(spanElement);

            Span balance = new Span(wfmStrings.balance() + ": ");
            balance.add(customerBalanceLink);
            clientFieldLabel.add(balance);

            addField(CRM_OPPORTUNITY_ACCOUNT_NAME, clientField, getFieldLabel(null));
        } else {
            FormGroup clientField = new FormGroup(accountName);
            Div clientFieldLabel = clientField.getGroupLabel();
            clientFieldLabel.addStyleName("label-group");

            Span spanElement = new Span(getTitle(wfmStrings.customer()));
            spanElement.setId("customer-label");
            clientFieldLabel.add(spanElement);

            Span balance = new Span(wfmStrings.balance() + ": ");
            balance.add(customerBalanceLink);
            clientFieldLabel.add(balance);

            addField(CRM_OPPORTUNITY_ACCOUNT_NAME, clientField, getFieldLabel(null));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME) != null) {
            addField(CRM_OPPORTUNITY_CONTACT_NAME, contactName, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME).getTitle() : Property.get(Constants.Contacts, wfmStrings.contact())));
        } else {
            addField(CRM_OPPORTUNITY_CONTACT_NAME, contactName, getTitle(Property.get(Constants.Contacts, wfmStrings.contact())));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_PRIMARY_EMAIL) != null) {
            addField(CRM_OPPORTUNITY_CONTACT_PRIMARY_EMAIL, contactPrimaryEmail, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_PRIMARY_EMAIL).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_PRIMARY_EMAIL).getTitle() : wfmStrings.email()));
        } else {
            addField(CRM_OPPORTUNITY_CONTACT_PRIMARY_EMAIL, contactPrimaryEmail, getTitle(wfmStrings.email()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_PRIMARY_PHONE) != null) {
            addField(CRM_OPPORTUNITY_CONTACT_PRIMARY_PHONE, contactPrimaryPhone, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_PRIMARY_PHONE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_PRIMARY_PHONE).getTitle() : wfmStrings.phone()));
        } else {
            addField(CRM_OPPORTUNITY_CONTACT_PRIMARY_PHONE, contactPrimaryPhone, getTitle(wfmStrings.phone()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE) != null) {
            addField(CRM_OPPORTUNITY_CAMPAIGN_SOURCE, campaign, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE).getTitle() : wfmStrings.campaign()));
        } else {
            addField(CRM_OPPORTUNITY_CAMPAIGN_SOURCE, campaign, getTitle(wfmStrings.campaign()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_TYPE) != null) {
            addField(CRM_OPPORTUNITY_TYPE, type, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_TYPE).getTitle() : wfmStrings.type()));
        } else {
            addField(CRM_OPPORTUNITY_TYPE, type, getTitle(wfmStrings.type()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP) != null) {
            addField(CRM_OPPORTUNITY_NEXT_STEP, nextStep, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP).getTitle() : wfmStrings.nextStep()));
        } else {
            addField(CRM_OPPORTUNITY_NEXT_STEP, nextStep, getTitle(wfmStrings.nextStep()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT) != null) {
            addField(CRM_OPPORTUNITY_AMOUNT, amount, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT).getTitle() : wfmStrings.amount()));
        } else {
            addField(CRM_OPPORTUNITY_AMOUNT, amount, getTitle(wfmStrings.amount()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY) != null) {
            addField(CRM_OPPORTUNITY_PROBABILITY, probability, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY).getTitle() : wfmStrings.probability()));
        } else {
            addField(CRM_OPPORTUNITY_PROBABILITY, probability, getTitle(wfmStrings.probability()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE) != null) {
            addField(CRM_OPPORTUNITY_EXPECTED_REVENUE, expectedRevenue, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE).getTitle() : wfmStrings.expectedRevenue()));
        } else {
            addField(CRM_OPPORTUNITY_EXPECTED_REVENUE, expectedRevenue, getTitle(wfmStrings.expectedRevenue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_NOTE) != null) {
            addField(CRM_NOTE, noteWidget, getTitle(formPropertyMap.get(CustomFormConstants.CRM_NOTE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_NOTE).getTitle() : wfmStrings.notes()), true);
        } else {
            addField(CRM_NOTE, noteWidget, wfmStrings.notes(), true);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CREATED_DATE) != null) {
            addField(CREATED_DATE, createdDate, getTitle(formPropertyMap.get(CustomFormConstants.CREATED_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.CREATED_DATE).getTitle() : wfmStrings.createdDate()));
        } else {
            addField(CREATED_DATE, createdDate, getTitle(wfmStrings.createdDate()));
        }

        addField(CRM_OPPORTUNITY_ATTACHMENTS, fileUpload, wfmStrings.attachments(), true);
        addField(CRM_ACTIVITIES, crmActivityGrid, Property.getPluralWithObjectCodeWithReplace(Constants.EVENT_LIST, wfmStrings.latestOpenActivities(), wfmStrings.activities()), true);
        addField(CRM_OPPORTUNITY_INCLUDE_ITEMS, fp, Property.getPluralWithObjectCode(Constants.PRODUCTS_OR_SERVICES, wfmStrings.productsOrServices()), true);
        addField(CRM_OPPORTUNITY_RFQ, rfq, Property.getShortName(Constants.REQUEST_FOR_QUOTE, wfmStrings.relatedRFQ(), wfmStrings.requestForQuote()));
//        addField(CRM_OPPORTUNITY_LINKS, getLinkingUtil().getLinkAndLinksPanelInVerticalPanel(), wfmStrings.links(), true);
//        addField(LINKS2, getLinkingUtil().getAddLink(), getTitle(wfmStrings.relatedTo()));
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, objectId, true);
        show();
    }


    @Override
    protected void drawItemTable() {
        ItemTableSettingService.App.get().getColumnConfigs(LayoutRPC.OPPORTUNITY_FORM, new AbstractAsyncCallback<HashMap<String, ColumnConfigs[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(HashMap<String, ColumnConfigs[]> result) {
                if (result != null && result.size() > 0) {
                    for (Map.Entry<String, ColumnConfigs[]> configMap : result.entrySet()) {
                        CustomFormItemGrid itemView = new CustomFormItemGrid(objectId, configMap.getKey(), LayoutRPC.OPPORTUNITY_FORM, configMap.getValue(), 1000);
                        addField(configMap.getKey(), itemView, null, true);
                    }
                }
            }
        });
    }

    private void onChangeField(String field) {
        switch (field) {
            case OpportunityListItem.CLOSING_DATE:
                item.setClosingDate(closingDate.dueDate.getDate());
                break;
            case OpportunityListItem.STAGE:
                item.setStageId(stage.getSelectedId());
                break;
        }

        LoadingPanel.loading(true);
        crmService.saveOppotunityEditCellValue(item, field, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Boolean result) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_OPPORTUNITY_ADD_EDIT, item, ViewOpportunityForm.this);
                if (OpportunityListItem.STAGE.equals(field)) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_OPPORTUNITY_LOAD_STAGE_HISTORY, true, ViewOpportunityForm.this);
                }
            }
        });
    }

    @Override
    protected void addButtons() {
        customizeButton.setVisible(false);

        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);
        if (objectId != null && Utils.hasPermission(PermissionConstants.CRM_OPPORTUNITY_LINKS)) {
            footer.addToLeftSide(link);
        }

        MaterialLink options = new MaterialLink(wfmStrings.options());
        MaterialSplitButton optionsButton = new MaterialSplitButton(options, Constants.BTN_DEFAULT_OUTLINE);
        if (Utils.hasPermission(CUSTOM_FORM_2_CUSTOMIZE_FORM)) {
            MaterialLink customize = new MaterialLink(wfmStrings.customize());
            customize.addClickHandler(click -> {
                String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + URL.encodeQueryString(url));
            });
            optionsButton.addItem(customize);
        }

        if (Utils.hasPermission(PermissionConstants.CRM_REMOVE_OPPORTUNITIES)) {
            MaterialLink delete = new MaterialLink(wfmStrings.delete());
            delete.addClickHandler(event -> {
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                messageBox.setTitle(wfmStrings.warning());
                messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                messageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        if (item != null) {
                            ArrayList<Integer> ids = new ArrayList<>();
                            ids.add(item.getObjectId());
                            crmService.deleteOpportunity(ids, new AbstractAsyncCallback<ArrayList<Integer>>() {
                                @Override
                                public void failure(Throwable caught) {
                                }

                                @Override
                                public void success(ArrayList<Integer> result) {
                                    Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.opportunity()), Info.Type.INFO);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_OPPORTUNITY_DELETED, result, ViewOpportunityForm.this);
                                    closeTab();
                                }
                            });
                        }
                    }
                });
                messageBox.open();
            });
            optionsButton.addItem(delete);
        }
        if (Utils.hasPermission(CONVERT_OPPORTUNITY_TO_PROJECT)) {
            toProject = new MaterialLink(wfmStrings.convertTo() + " " + wfmStrings.project());
            toProject.addClickHandler(clickEvent -> {
                if (item.getAccountId() != null) {
                    goToConvert(item, ViewOpportunityForm.this);
                } else {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.information());
                    messageBox.setMessage(wfmMessages.convertToProjectRequiresAccount(item.getOpportunityName()));
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            crmService.addAccountOrContactToOpportunity(objectId, true, new AbstractAsyncCallback<OpportunityListItem>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                }

                                @Override
                                public void onSuccess(OpportunityListItem result) {
                                    item.setContactId(result.getContactId());
                                    item.setCrmAccountItem(result.getCrmAccountItem());
                                    goToConvert(item, ViewOpportunityForm.this);
                                }
                            });
                        }

                        @Override
                        public void onCancel() {
                            if (Utils.hasPermission(PermissionConstants.CRM_EDIT_OPPORTUNITIES)) {
                                SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|add/add/" + item.getObjectId() + "/REQUIRED");
                            }
                        }
                    });
                    messageBox.open();
                }
            });
            optionsButton.addItem(toProject);
        }
        addRightButton(optionsButton);

        PropertyItem propertyItem = Utils.getProperTy(Constants.Opportunities);
        if (propertyItem != null && propertyItem.getConvertItems() != null && propertyItem.getConvertItems().length > 0) {

            MaterialLink convert = new MaterialLink(wfmStrings.convert());
            MaterialSplitButton convertButton = new MaterialSplitButton(convert, Constants.BTN_DEFAULT_OUTLINE);
            int convertItems = 0;
            for (ConvertItem convertItem : propertyItem.getConvertItems()) {
                if (convertItem != null) {
                    convertItems = getConvertItems(convertButton, convertItems, convertItem);
                }
            }

            if (convertItems > 0) {
                addRightButton(convertButton);
            }
        }

        if (Utils.hasPermission(CRM_TASKS_ADD, CRM_ADD_NEW_ACTIVITY_LOG_A_CALL, CRM_ADD_NEW_ACTIVITY_EVENT)) {
            MaterialLink addButton = new MaterialLink(wfmStrings.add());
            MaterialSplitButton addSplitButton = new MaterialSplitButton(addButton, Constants.BTN_DEFAULT_OUTLINE);
            if (Utils.hasPermission(PermissionConstants.CRM_TASKS_ADD)) {
                MaterialLink addTask = new MaterialLink(wfmStrings.task());
                addTask.ensureDebugId("addTask");
                addTask.addClickHandler(event -> new TaskQuickAddView(RelationItem.newEventRelation(RelationItem.TYPE_OPPORTUNITY, item.getObjectId(), item.getOpportunityName()),
                        RelationItem.newEventRelation(TYPE_CONTACT, item.getContactId(), item.getContact()),
                        RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getAccountId(), item.getAccount())));
                addSplitButton.addItem(addTask);
            }

            if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_ACTIVITY_LOG_A_CALL)) {
                logCall = new MaterialLink(Property.get(Constants.LOGACALL, wfmStrings.logCall()));
                logCall.ensureDebugId("callALog");
                addSplitButton.addItem(logCall);
            }

            if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_ACTIVITY_EVENT)) {
                addEvent = new MaterialLink(Property.get(Constants.EVENT_LIST, wfmStrings.event()));
                addEvent.ensureDebugId("addEvent");
                addSplitButton.addItem(addEvent);
            }
            approveButton = addButton(wfmStrings.approve(), BTN_SUCCESS, clickEvent -> saveStatus(OPPORTUNITY_APPROVED));
            approveButton.setVisible(false);

            declineButton = addButton(wfmStrings.reject(), BTN_REJECT, clickEvent -> saveStatus(Constants.OPPORTUNITY_REJECTED));
            declineButton.setVisible(false);

            submitButton = addButton(Constants.OPPORTUNITY_REJECTED.equals(statusCode) ? wfmStrings.resubmitForApproval() : wfmStrings.submitForApproval(), wfmStrings.submitForApproval(), Constants.BTN_DEFAULT_OUTLINE, clickEvent -> {
                submitButton.setEnabled(false);
                save(Constants.OPPORTUNITY_SUBMITTED);
            });
            submitButton.setVisible(false);
            addRightButton(addSplitButton);
        }

        if (Utils.hasPermission(CRM_EDIT_OPPORTUNITIES, CRM_OPPORTUNITY_CHANGE_STAGE)) {
            editButton = addMoreSplitButton(wfmStrings.edit(), () -> {
                if (Utils.hasPermission(PermissionConstants.CRM_EDIT_OPPORTUNITIES) && item.isAllowEdit()) {
                    closeTab("opportunity|add/add/" + objectId, item.getNumberData().getNumberString(), item.getOpportunityName());
                } else {
                    Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                }
            });
        }
        if (Utils.hasPermission(CRM_OPPORTUNITY_PDF)) {
            printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
            addRightButton(printPdfSplitButton);
        }

        if (Utils.hasPermission(CONVERT_OPPORTUNITY_TO_SQ, CONVERT_OPPORTUNITY_TO_SO, CONVERT_OPPORTUNITY_TO_RFQ,
                CONVERT_OPPORTUNITY_TO_PO, CONVERT_OPPORTUNITY_TO_PI, CONVERT_OPPORTUNITY_TO_SI)) {
            MaterialLink send = new MaterialLink(wfmStrings.send());
            MaterialSplitButton sendButton = new MaterialSplitButton(send);
            if (Utils.hasPermission(CONVERT_OPPORTUNITY_TO_SQ)) {
                MaterialLink toSaleQuote = new MaterialLink(Property.getShortName(Constants.SALE_QUOTE, wfmStrings.salesQuote()));
                toSaleQuote.addClickHandler(clickEvent -> {
                    if ("true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP))) {
                        if (item.getAccountId() != null) {
                            SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_QUOTE + "|add/add/opportunity/" + item.getObjectId());
                        } else {
                            LoadingPanel.loading(true);
                            crmService.addAccountOrContactToOpportunity(objectId, true, new AbstractAsyncCallback<OpportunityListItem>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    LoadingPanel.loading(false);
                                }

                                @Override
                                public void onSuccess(OpportunityListItem result) {
                                    LoadingPanel.loading(false);
                                    item.setContactId(result.getContactId());
                                    item.setCrmAccountItem(result.getCrmAccountItem());
                                    SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_QUOTE + "|add/add/opportunity/" + item.getObjectId());
                                }
                            });
                        }
                    } else {
                        Info.show(wfmStrings.accountingAndFinanceModuleNoSetupYet() + " " + Utils.getSupportEmail() + " " + wfmStrings.accountingAndFinanceModuleNoSetupYet1(), Info.Type.WARNING);
                    }
                });
                sendButton.addItem(toSaleQuote);
            }
            if (Utils.hasPermission(CONVERT_OPPORTUNITY_TO_SO)) {
                MaterialLink toSalesOrder = new MaterialLink(Property.getShortName(Constants.SALE_ORDER_CODE, wfmStrings.saleorder()));
                toSalesOrder.addClickHandler(clickEvent -> {
                    if ("true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP))) {
                        if (item.getAccountId() != null) {
                            SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_ORDER_CODE + "|add/add/opportunity/" + item.getObjectId());
                        } else {
                            LoadingPanel.loading(true);
                            crmService.addAccountOrContactToOpportunity(objectId, true, new AbstractAsyncCallback<OpportunityListItem>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    LoadingPanel.loading(false);
                                }

                                @Override
                                public void onSuccess(OpportunityListItem result) {
                                    LoadingPanel.loading(false);
                                    item.setContactId(result.getContactId());
                                    item.setCrmAccountItem(result.getCrmAccountItem());
                                    SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_ORDER_CODE + "|add/add/opportunity/" + item.getObjectId());
                                }
                            });
                        }
                    } else {
                        Info.show(wfmStrings.accountingAndFinanceModuleNoSetupYet() + " " + Utils.getSupportEmail() + " " + wfmStrings.accountingAndFinanceModuleNoSetupYet1(), Info.Type.WARNING);
                    }
                });
                sendButton.addItem(toSalesOrder);
            }
            if (Utils.hasPermission(CONVERT_OPPORTUNITY_TO_RFQ)) {
                MaterialLink toRfq = new MaterialLink(Property.getShortName(REQUEST_FOR_QUOTE, wfmStrings.requestForQuote()));
                toRfq.addClickHandler(clickEvent -> {
                    if ("true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP))) {
                        if (item.getAccountId() != null) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("requestforquote|add/add/opportunity/" + item.getObjectId());
                        } else {
                            LoadingPanel.loading(true);
                            crmService.addAccountOrContactToOpportunity(objectId, true, new AbstractAsyncCallback<OpportunityListItem>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    LoadingPanel.loading(false);
                                }

                                @Override
                                public void onSuccess(OpportunityListItem result) {
                                    LoadingPanel.loading(false);
                                    item.setContactId(result.getContactId());
                                    item.setCrmAccountItem(result.getCrmAccountItem());
                                    SinksContainerFactory.entryPoint.onHistoryChanged("requestforquote|add/add/opportunity/" + item.getObjectId());
                                }
                            });
                        }
                    } else {
                        Info.show(wfmStrings.accountingAndFinanceModuleNoSetupYet() + " " + Utils.getSupportEmail() + " " + wfmStrings.accountingAndFinanceModuleNoSetupYet1(), Info.Type.WARNING);
                    }
                });
                sendButton.addItem(toRfq);
            }
            if (Utils.hasPermission(CONVERT_OPPORTUNITY_TO_PO)) {
                MaterialLink toPurchaseOrder = new MaterialLink(Property.getShortName(PURCHASE_ORDER, wfmStrings.purchaseorder()));
                toPurchaseOrder.addClickHandler(clickEvent -> {
                    if ("true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP))) {
                        if (item.getAccountId() != null) {
                            SinksContainerFactory.entryPoint.onHistoryChanged(Constants.PURCHASE_ORDER + "|add/add/opportunity/" + item.getObjectId());
                        } else {
                            LoadingPanel.loading(true);
                            crmService.addAccountOrContactToOpportunity(objectId, false, new AbstractAsyncCallback<OpportunityListItem>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    LoadingPanel.loading(false);
                                }

                                @Override
                                public void onSuccess(OpportunityListItem result) {
                                    LoadingPanel.loading(false);
                                    item.setContactId(result.getContactId());
                                    item.setCrmAccountItem(result.getCrmAccountItem());
                                    SinksContainerFactory.entryPoint.onHistoryChanged(Constants.PURCHASE_ORDER + "|add/add/opportunity/" + item.getObjectId());
                                }
                            });
                        }
                    } else {
                        Info.show(wfmStrings.accountingAndFinanceModuleNoSetupYet() + " " + Utils.getSupportEmail() + " " + wfmStrings.accountingAndFinanceModuleNoSetupYet1(), Info.Type.WARNING);
                    }
                });
                sendButton.addItem(toPurchaseOrder);
            }
            if (Utils.hasPermission(CONVERT_OPPORTUNITY_TO_SI)) {
                MaterialLink toSalesInvoice = new MaterialLink(Property.getShortName(Constants.SALE_INVOICE, wfmStrings.salesInvoice()));
                toSalesInvoice.addClickHandler(clickEvent -> {
                    if ("true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP))) {
                        if (item.getAccountId() != null) {
                            SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_INVOICE + "|add/add/opportunity/" + item.getObjectId());
                        } else {
                            LoadingPanel.loading(true);
                            crmService.addAccountOrContactToOpportunity(objectId, true, new AbstractAsyncCallback<OpportunityListItem>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    LoadingPanel.loading(false);
                                }

                                @Override
                                public void onSuccess(OpportunityListItem result) {
                                    LoadingPanel.loading(false);
                                    item.setContactId(result.getContactId());
                                    item.setCrmAccountItem(result.getCrmAccountItem());
                                    SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_INVOICE + "|add/add/opportunity/" + item.getObjectId());
                                }
                            });
                        }
                    } else {
                        Info.show(wfmStrings.accountingAndFinanceModuleNoSetupYet() + " " + Utils.getSupportEmail() + " " + wfmStrings.accountingAndFinanceModuleNoSetupYet1(), Info.Type.WARNING);
                    }
                });
                sendButton.addItem(toSalesInvoice);
            }
            if (Utils.hasPermission(CONVERT_OPPORTUNITY_TO_PI)) {
                MaterialLink toPurchaseInvoise = new MaterialLink(Property.getShortName(Constants.PURCHASE_INVOICE, wfmStrings.purchaseinvoice()));
                toPurchaseInvoise.addClickHandler(clickEvent -> {
                    if ("true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP))) {
                        if (item.getAccountId() != null) {
                            SinksContainerFactory.entryPoint.onHistoryChanged(Constants.PURCHASE_INVOICE + "|add/add/opportunity/" + item.getObjectId());
                        } else {
                            LoadingPanel.loading(true);
                            crmService.addAccountOrContactToOpportunity(objectId, true, new AbstractAsyncCallback<OpportunityListItem>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    LoadingPanel.loading(false);
                                }

                                @Override
                                public void onSuccess(OpportunityListItem result) {
                                    LoadingPanel.loading(false);
                                    item.setContactId(result.getContactId());
                                    item.setCrmAccountItem(result.getCrmAccountItem());
                                    SinksContainerFactory.entryPoint.onHistoryChanged(Constants.PURCHASE_INVOICE + "|add/add/opportunity/" + item.getObjectId());
                                }
                            });
                        }
                    } else {
                        Info.show(wfmStrings.accountingAndFinanceModuleNoSetupYet() + " " + Utils.getSupportEmail() + " " + wfmStrings.accountingAndFinanceModuleNoSetupYet1(), Info.Type.WARNING);
                    }
                });
                sendButton.addItem(toPurchaseInvoise);
            }
            if (Utils.hasPermission(CRM_OPPORTUNITY_SEND_SMS)) {
                MaterialLink sendSms = new MaterialLink(wfmStrings.sms());
                sendSms.addClickHandler(clickEvent -> {
                    new ActivityQuickAddForm(Appointment.SMS, item.getContactPrimaryPhone(), new ProfileItem(), RelationItem.newEventRelation(TYPE_OPPORTUNITY, item.getObjectId(), item.getOpportunityName()), RelationItem.newEventRelation(TYPE_CONTACT, item.getContactId(), item.getContact()), RelationItem.newEventRelation(TYPE_CRM_ACCOUNT, item.getAccountId(), item.getAccount()));
                });
                sendButton.addItem(sendSms);
            }
            addRightButton(sendButton);
        }
    }

    private int getConvertItems(MaterialSplitButton convertMenu, int convertItems, ConvertItem convertItem) {
        if (convertItem.getCode().contains("_FORM") && Utils.hasPermission(convertItem.getCode() + "_ADD_" + Utils.getCompanyID())) {
            MaterialLink convertToCF = new MaterialLink(convertItem.getName());
            convertToCF.addClickHandler((clickEvent) -> SinksContainerFactory.entryPoint.onHistoryChanged(Constants.ITEM_LIST + "|add/add/" + convertItem.getEntityId() + "/" + convertItem.getCode() + "/CONVERT/" + RelationItem.TYPE_OPPORTUNITY + "/" + item.getObjectId()));
            convertToCF.ensureDebugId("convert_to_" + convertItem.getName());
            convertMenu.addItem(convertToCF);
            convertItems++;
        }
        return convertItems;
    }

    private void pdfTool(OpportunityListItem result) {
        if (printPdfSplitButton == null) {
            return;
        }
        List<SplitButtonItem> pdfTemplatesList = new ArrayList<>();
        Integer defaultTemplateId = null;
        if (result != null && result.getTemplates() != null && result.getTemplates().length > 0) {
            for (SelectItem pdfItem : result.getTemplates()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(panel, pdfItem.getId(), false)));
            }
        } else {
            pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_", wfmStrings.landscape(), () -> generatePDF(panel, null, true)));
        }
        Integer finalDefaultTemplateId = defaultTemplateId;

        SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> generatePDF(panel, finalDefaultTemplateId, false), true);
        pdfTemplatesList.add(pdfVersion);
        printPdfSplitButton.addItemList(pdfTemplatesList);
    }

    private void generatePDF(HTMLPanel panel, Integer templateID, boolean landscape) {
        CrmAccountRequestObject requestObject = new CrmAccountRequestObject(objectId);
        HashMap<String, String> parameters = requestObject.getRequestParams();
        if (templateID != null) {
            parameters.put("pdfTemplateID", String.valueOf(templateID));
        }
        if (landscape) {
            parameters.put("IS_LANDSCAPE", "true");
        }
        String pdfURL = CommandConstants.PDF_URL + "/opportunityViewPDFHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }

    @Override
    protected void getDataToFillFields() {
        contractUploaded = false;
        LoadingPanel.loading(true);
        crmService.getOpportunity(objectId, new AbstractAsyncCallback<OpportunityListItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final OpportunityListItem o) {
                Scheduler.get().scheduleDeferred(() -> {
                    LoadingPanel.loading(false);
                    item = o;

                    if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE) != null) {
                        addField(CRM_OPPORTUNITY_LEAD_SOURCE, !formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE).isDisabled() ? leadSourceHtml : leadSource, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE).getTitle() : wfmStrings.source()));
                    } else {
                        addField(CRM_OPPORTUNITY_LEAD_SOURCE, leadSourceHtml, getTitle(wfmStrings.source()));
                    }

                    if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE) != null) {
                        addField(CRM_OPPORTUNITY_CLOSING_DATE, Utils.hasPermission(CRM_EDIT_OPPORTUNITIES) && item.isAllowEdit() ||
                                !formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE).isDisabled() && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE).getRoleEdit() != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE).getRoleEdit().size() > 0 ? closingDate.dueDate : closeDateHtml, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE).getTitle() : wfmStrings.closeDate(), true));
                        closingDate.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE).isDisabled());
                    } else {
                        addField(CRM_OPPORTUNITY_CLOSING_DATE, Utils.hasPermission(CRM_EDIT_OPPORTUNITIES) && item.isAllowEdit() ? closingDate.dueDate : closeDateHtml, getTitle(wfmStrings.closeDate(), true));
                    }

                    if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_STAGE) != null) {
                        addField(CRM_OPPORTUNITY_STAGE, Utils.hasPermission(CRM_EDIT_OPPORTUNITIES) && item.isDraggable() ||
                                Utils.hasPermission(CRM_OPPORTUNITY_CHANGE_STAGE) && item.isDraggable() ||
                                !formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_STAGE).isDisabled() && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_STAGE).getRoleEdit() != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_STAGE).getRoleEdit().size() > 0 ? stage : stageHtml, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_STAGE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_STAGE).getTitle() : wfmStrings.stage()));
                        stage.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_STAGE).isDisabled());
                    } else {
                        addField(CRM_OPPORTUNITY_STAGE, Utils.hasPermission(CRM_EDIT_OPPORTUNITIES) && item.isAllowEdit() ||
                                Utils.hasPermission(CRM_OPPORTUNITY_CHANGE_STAGE) && item.isAllowEdit() ? stage : stageHtml, getTitle(wfmStrings.stage()));
                    }

                    stageHistoryGrid = new StageHistoryGrid(objectId, item.getStageHistoryColConf());
                    addField(CRM_OPPORTUNITY_STAGE_HISTORY, stageHistoryGrid, wfmStrings.stageHistory(), true);
                    initPredefinedValues();
                    Utils.registrRelation(item);
                    fillFormWithData();
                    pdfTool(item);
                });
            }
        });
    }

    @Override
    protected void initPredefinedValues() {
        addPredefinedValues(CRM_OPPORTUNITY_STAGE, item.getStages());
        addPredefinedValues(CRM_OPPORTUNITY_LEAD_SOURCE, item.getLeadSources());
    }

    @Override
    protected void fillFormWithData() {

        if (item.getContactId() != null) {
            if (logCall != null) {
                logCall.addClickHandler(event -> new ActivityQuickAddForm(Appointment.CALL_LOG, RelationItem.newEventRelation(RelationItem.TYPE_OPPORTUNITY, objectId, item.getOpportunityName()), RelationItem.newEventRelation(TYPE_CONTACT, item.getContactId(), item.getContact())));
            }
            if (addEvent != null) {
                addEvent.addClickHandler(event -> new ActivityQuickAddForm(Appointment.EVENT, RelationItem.newEventRelation(RelationItem.TYPE_OPPORTUNITY, item.getObjectId(), item.getOpportunityName()), RelationItem.newEventRelation(TYPE_CONTACT, item.getContactId(), item.getContact())));
            }
        } else {
            if (logCall != null) {
                logCall.addClickHandler(event -> new ActivityQuickAddForm(Appointment.CALL_LOG, RelationItem.newEventRelation(RelationItem.TYPE_OPPORTUNITY, objectId, item.getOpportunityName()), RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getAccountId(), item.getAccount())));
            }
            if (addEvent != null) {
                addEvent.addClickHandler(event -> new ActivityQuickAddForm(Appointment.EVENT, RelationItem.newEventRelation(RelationItem.TYPE_OPPORTUNITY, objectId, item.getOpportunityName()), RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getAccountId(), item.getAccount())));
            }
        }
        if (item.getOverallStatus() != null) {
            statusCode = item.getOverallStatus().getCode();
        }
        if (companyName != null) {
            companyName.getElement().setInnerHTML(item.getAccount() != null ? "<a style='text-decoration:none;' href=\"javascript:\">" + item.getAccount() + "</a>" : "");
            companyName.addDomHandler(event -> {
                if (!Utils.isNullOrEmpty(item.getAccount())) {
                    if (Utils.isAccounting()) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("client|summary/" + item.getCrmAccountItem().getObjectId(), item.getAccountNumber(), item.getAccount());
                    } else if (Utils.hasPermission(CRM_ACCOUNTS_SUMMARY)) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("account|summary/" + item.getCrmAccountItem().getObjectId(), item.getAccountNumber(), item.getAccount());
                    }
                }
            }, ClickEvent.getType());
        }
        if (email != null) {
            email.getElement().setInnerHTML(item.getCrmAccountItem().getEmail() != null ? "<a href=\"javascript:\">" + item.getCrmAccountItem().getEmail() + "</a>" : "&nbsp;");
        }
        if (phone != null) {
            phone.getElement().setInnerHTML(item.getCrmAccountItem().getPhone());
        }
        if (fax != null) {
            fax.getElement().setInnerHTML(item.getCrmAccountItem().getFax());
        }
        if (item.getClosingDate() != null) {
            closingDate.dueDate.setDate(item.getClosingDate());
        }
        stage.setItems(item.getStages());
        if (item.getStageId() != null) {
            stage.setSelected(item.getStageId());
        }

        NumberData numberData = item.getNumberData();
        labelCurrent.setHTML(item.getCurrency() != null ? "&nbsp;(" + item.getCurrency() + ")" : "");
        assignee.setHTML(item.getAssignee());
        setInnerHTML(backupAssignee, item.getBackupAssignee());
        setInnerHTML(project, item.getProject() != null ? item.getProject().getName() : "");
        setInnerHTML(numbering, numberData.getNumberString());
        setInnerHTML(opportunityNumber, numberData.getNumberString());
        setInnerHTML(opportunityName, item.getOpportunityName());
        setInnerHTML(amount, numberFormat.format(item.getAmount().doubleValue()) + labelCurrent.getHTML());

        calculateTotalTable();
        accountName.setHTML(item.getAccount());
        setCrmAccountBalance(item, customerBalanceLink);
        setInnerHTML(type, item.getType());
        contactName.setHTML(item.getContact());
        contactPrimaryEmail.setHTML(item.getContactPrimaryEmail());
        setInnerHTML(probability, (item.getProbability() != null ? item.getProbability().toString() : "0") + "%");
        setInnerHTML(nextStep, item.getNextStep());
        setInnerHTML(expectedRevenue, numberFormat.format(item.getExpectedRevenue() != null ? item.getExpectedRevenue() : 0));
        if (item.getCreatedDate() != null) {
            setInnerHTML(createdDate, DateUtils.dateFormatWithHour(item.getCreatedDate()));
        }
        if (item.getClosingDate() != null) {
            setInnerHTML(closeDateHtml, DateUtils.dateFormatWithHour(item.getClosingDate()));
        }
        setInnerHTML(stageHtml, item.getStageName());
        setInnerHTML(leadSourceHtml, item.getLeadSource());
        if (item.getCampaignId() != null) {
            campaign.setHTML("<a href=\"javascript:\">" + item.getCampaign());
            campaign.addClickHandler(clickEvent ->
            {
                if (Utils.hasPermission(PermissionConstants.CRM_CAMPAIGNS_SUMMARY)) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("campaign|summary/" + item.getCampaignId() + "/" + item.getCampaign(), item.getCampaign());
                } else {
                    Info.warn(wfmStrings.youDontHavePermission());
                }
            });
        }

        if (item.getContactItem() != null) {
            setMultiTableItems(Constants.CONTACT_PHONES);
        }


        if (item.getRFQId() != null) {
            rfq.setHTML(item.getRFQId().toString());
            rfq.addClickHandler(clickEvent -> {
                String addSalesInvoice = GWT.getHostPageBaseURL() + "Accounting.html#requestforquote|summary/" + item.getRFQId();
                Window.open(addSalesInvoice, "_blank", "");
            });
        } else {
            rfq.setHTML("");
        }

        link.addClickHandler(event -> {
            if (firstClick.get()) {
                getLinkingUtil().getAddLinkSideNavBox();
                getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(item.getRelations(), false);
                firstClick.set(false);
            } else {
                getLinkingUtil().getAddLinkSideNavBox().show();
            }
        });
        link.setBadgeCount(item.getRelations().size());

//        getLinkingUtil().getTaggingView().setSelectedRelations(item.getRelations());
//        getLinkingUtil().drawLinks();
        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFields(), true);
        if (item.isConvertedToProject()) {
            if (convert != null) {
                toProject.removeFromParent();
                convert.getElement().getLastChild().removeFromParent();
            }
        }

        MaterialDropDown stageMenuBar = new MaterialDropDown();
        stageMenuBar.setHover(true);
        stageMenuBar.setBelowOrigin(true);
        MaterialLink changeStatus = new MaterialLink(wfmStrings.changeStage());
        changeStatus.add(stageMenuBar);

        if (Utils.hasPermission(CRM_OPPORTUNITY_CHANGE_STAGE)) {
            editButton.add(changeStatus);
        }

        for (final SelectItem stage : item.getStages()) {
            if (item.getStage() == null || !stage.getId().equals(item.getStageId())) {
                MaterialLink stageItem = new MaterialLink(stage.getName());
                stageItem.addClickHandler(event -> {
                    if (stage.isDraggable() && item.isDraggable()) {
                        if (Utils.isDoubleMessageEnable()) {
                            changeStageMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                            changeStageMessageBox.setMessage(crmMessages.doYouWantToChangeStatusTo(stage.getName()));
                            changeStageMessageBox.addCloseHandler(new CloseHandler() {
                                @Override
                                public void onSubmit() {
                                    LoadingPanel.loading(true);
                                    if ("0".equals(stage.getDescription()) || stage.isSelected()) {
                                        item.setStageId(stage.getId());
                                        new OpportunityPercentageStageModal(item, stage.isSelected(), "0".equals(stage.getDescription()), true);
                                    } else {
                                        changeStage(stage.getId());
                                    }
                                }
                            });
                            changeStageMessageBox.setTitle(wfmStrings.warning());
                            changeStageMessageBox.open();
                        } else {
                            if ("0".equals(stage.getDescription()) || stage.isSelected()) {
                                item.setStageId(stage.getId());
                                new OpportunityPercentageStageModal(item, stage.isSelected(), "0".equals(stage.getDescription()), true);
                            } else {
                                changeStage(stage.getId());
                            }
                        }
                    } else {
                        Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                    }
                });
                stageMenuBar.add(stageItem);
            }
            if (item.getApproverEmployee() != null) {
                approvers.setHTML(item.getApproverEmployee().getName());
            }

            initButtons();
        }

        if (actionType != null && actionType > 0) {
            switch (actionType) {
                case OPEN_WRITE_NOTE:
                    noteWidget.getElement().focus();
                    break;
                case OPEN_LOG_CALL:
                    new ActivityQuickAddForm(Appointment.CALL_LOG, RelationItem.newEventRelation(RelationItem.TYPE_OPPORTUNITY, item.getObjectId(), item.getOpportunityName()));
                    break;
                case OPEN_ADD_EVENT:
                    new ActivityQuickAddForm(Appointment.EVENT, RelationItem.newEventRelation(RelationItem.TYPE_OPPORTUNITY, item.getObjectId(), item.getOpportunityName()));
                    break;
            }
        }
    }

    private void calculateTotalTable() {

        OpportunityItem[] items = item.getItems();
        Integer calculationType = item.getTaxCalculationType() != null ? item.getTaxCalculationType() : 0;
        BigDecimal subTotalAmount = BigDecimal.ZERO, discountAmount = BigDecimal.ZERO, quantity = BigDecimal.ZERO, totalAmount = BigDecimal.ZERO;
        if (items != null) {
            for (OpportunityItem item : items) {
                BigDecimal subtotal = BigDecimal.ZERO, discount = BigDecimal.ZERO, itemTaxAmount = BigDecimal.ZERO;
                BigDecimal priceCol = null;
                if (itemView.getColumnConfigs().containsKey(ItemTableConstants.UNITPRICE)) {
                    priceCol = item.getPrice() != null ? item.getPrice() : BigDecimal.ZERO;
                }
                if (itemView.getColumnConfigs().containsKey(ItemTableConstants.QTY)) {
                    if (item.getQty() != null) {
                        if (priceCol != null) {
                            subtotal = subtotal.add(priceCol).multiply(item.getQty());
                        }
                        quantity = quantity.add(item.getQty());
                    }
                }
                if (itemView.getColumnConfigs().containsKey(ItemTableConstants.DISCOUNT_AMT)) {
                    if (item.getDiscountPercent() != null) {
                        discount = subtotal.multiply(item.getDiscountPercent()).divide(new BigDecimal(100));
                    } else if (item.getDiscountAmount() != null) {
                        discount = item.getDiscountAmount();
                    }
                }
                if (itemView.getColumnConfigs().containsKey(ItemTableConstants.TAX_LIST)) {
                    itemTaxAmount = item.getTaxAmount();
                }

                subTotalAmount = subTotalAmount.add(subtotal);
                discountAmount = discountAmount.add(discount);
                BigDecimal effectiveDiscount = Utils.hasGenericAccess(GenericSettingsEnum.DISCOUNT_NOT_EFFECTED) ? BigDecimal.ZERO : discount;
                totalAmount = totalAmount.add(subtotal.subtract(effectiveDiscount));
                if (item.getTaxItem() != null && AccountingConstants.TAX_CALCULATION_EXCLUSIVE.equals(calculationType)) {
                    totalAmount = totalAmount.add(itemTaxAmount);
                }
            }
        }

        subTotal.setHTML(subTotalAmount != null ? numberFormat.format(subTotalAmount) : "0.00");
        discount.setHTML(discountAmount != null ? numberFormat.format(discountAmount) : "0.00");
        taxTotal.setHTML(item.getTaxTotal() != null ? numberFormat.format(item.getTaxTotal()) : "0.00");
        totalPrice.setHTML(totalAmount != null ? numberFormat.format(totalAmount) : "0.00");
        baseTotal.setHTML(totalAmount != null && totalAmount.compareTo(BigDecimal.ZERO) != 0 ? numberFormat.format(item.getExchangeRate() != null ?
                totalAmount.divide(item.getExchangeRate(), 8, RoundingMode.HALF_UP) : totalAmount) : "0.00");
        quantityTotal.setHTML(quantity != null ? numberFormat.format(quantity) : "0.00");
        drawTotalsTable();
    }

    public void drawTotalsTable() {
        opportunityReceiptTable.clear();
        if (itemView.getColumnConfigs().containsKey(ItemTableConstants.UNITPRICE)) {
            opportunityReceiptTable.setSubtotalItem(subTotalLabel, subTotal);
        }

        if (itemView.getColumnConfigs().containsKey(ItemTableConstants.DISCOUNT_AMT)) {
            opportunityReceiptTable.setDiscountItem(discountLabel, discount);
        }

        if (itemView.getColumnConfigs().containsKey(ItemTableConstants.TAX_LIST)) {
            opportunityReceiptTable.addItem(totalTaxLabel, taxTotal);
        }

        if (item.getCurrency() != null) {
            totalLabel.setHTML(accountingMessages.dynamicTotal(item.getCurrency()));
        } else {
            totalLabel.setHTML(wfmStrings.total());
        }
        if (itemView.getColumnConfigs().containsKey(ItemTableConstants.UNITPRICE)) {
            opportunityReceiptTable.addGrossItem(totalLabel, totalPrice);
        }

        if (item.getExchangeRate() != null && item.getExchangeRate().compareTo(BigDecimal.ONE) != 0 && itemView.getColumnConfigs().containsKey(ItemTableConstants.UNITPRICE)) {
            baseTotalLabel.setHTML(accountingMessages.dynamicTotal(item.getBaseCurrencyName()));
            opportunityReceiptTable.addGrossItem(baseTotalLabel, baseTotal);
        }
        if (itemView.getColumnConfigs().containsKey(ItemTableConstants.QTY)) {
            opportunityReceiptTable.addGrossItem(new HTML(wfmStrings.qty()), quantityTotal);
        }
    }

    private void changeStage(final Integer stageId) {
        LoadingPanel.loading(true);
        item.setStageId(stageId);
        crmService.saveOppotunityEditCellValue(item, OpportunityListItem.STAGE, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Boolean result) {
                LoadingPanel.loading(false);
                //setInnerHTML(ViewOpportunityForm.this.stage, stage.getName());
                Info.show(property.getSingular(wfmStrings.messSuccessfullyUpdated(), wfmStrings.opportunity()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_OPPORTUNITY_ADD_EDIT, objectId, ViewOpportunityForm.this);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_OPPORTUNITY_LOAD_STAGE_HISTORY, true, ViewOpportunityForm.this);
            }
        });
    }

    private void setMultiTableItems(int param) {
        Map<Integer, ArrayList<String>> itemParamsAsMap = ContactListItem.getItemParamsAsMap(item.getContactItem(), param);
        if (itemParamsAsMap != null && itemParamsAsMap.size() > 0) {
            for (Map.Entry<Integer, ArrayList<String>> entry : itemParamsAsMap.entrySet()) {
                int relation = entry.getKey();
                for (String value : entry.getValue()) {
                    if (value != null && !"".equals(value.trim())) {
                        value = value.replace("|", "-");
                        getKeyValuElement(value, relation, param);
                    }
                }
            }
        }
    }

    private void getKeyValuElement(final String value, int relation, int param) {
        FlowPanel f = new FlowPanel();
        f.getElement().getStyle().setDisplay(Style.Display.FLEX);
        f.addStyleName("firstChildHasPadding mb-2");
        PhonePopup popup;
        String phoneNumber = null;
        boolean mobile = false;
        HTML relationS = null;
        if (param == Constants.CONTACT_PHONES) {
            switch (relation) {
                case Constants.G_HOME:
                    relationS = new HTML(getTitle(wfmStrings.home()));
                    phoneNumber = value;
                    break;
                case Constants.G_WORK:
                    relationS = new HTML(getTitle(wfmStrings.contactwork()));
                    phoneNumber = value;
                    break;
                case Constants.G_OTHER:
                    relationS = new HTML(getTitle(wfmStrings.other()));
                    phoneNumber = value;
                    break;
                case Constants.G_MOBILE:
                    relationS = new HTML(getTitle(wfmStrings.mobile()));
                    phoneNumber = value;
                    mobile = true;
                    break;
                case Constants.G_HOME_FAX:
                    relationS = new HTML(getTitle(wfmStrings.homeFax()));
                    phoneNumber = value;
                    break;
                case Constants.G_WORK_FAX:
                    relationS = new HTML(getTitle(wfmStrings.workFax()));
                    phoneNumber = value;
                    break;
                case Constants.G_PAGER:
                    relationS = new HTML(getTitle(wfmStrings.pager()));
                    phoneNumber = value;
                    break;
                case Constants.G_EXTENSION:
                    relationS = new HTML(getTitle(wfmStrings.extension()));
                    phoneNumber = value;
                    break;
                case Constants.G_FAX:
                    relationS = new HTML(getTitle(wfmStrings.fax()));
                    phoneNumber = value;
                    break;
                case Constants.G_WHATS_APP:
                    relationS = new HTML(getTitle(wfmStrings.whatsApp()));
                    phoneNumber = value;
                    break;
                case Constants.G_TELEGRAM:
                    relationS = new HTML(getTitle(wfmStrings.telegram()));
                    phoneNumber = value;
                    break;
                case Constants.G_VIBER:
                    relationS = new HTML(getTitle(wfmStrings.viber()));
                    phoneNumber = value;
                    break;
            }
            f.getElement().setAttribute("style", "display: flex; align-items: center");
            popup = new PhonePopup(phoneNumber, item.getContactItem(), mobile, false, RelationItem.newEventRelation(TYPE_OPPORTUNITY, item.getObjectId(), item.getOpportunityName()));
            Div phoneWidget = popup.getPhoneWidget();
            f.add(relationS);
            f.add(phoneWidget);
            contactPrimaryPhone.add(f);
        }
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    public String getIconStyle() {
        return "oport opportunities-list";
    }

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(ViewOpportunityForm.this) {
                @Override
                protected boolean isActionEditing() {
                    return false;
                }

                @Override
                public Integer getRelationID() {
                    return objectId;
                }

                @Override
                public String getRelationType() {
                    return RelationItem.TYPE_OPPORTUNITY;
                }

                @Override
                public String getRelationName() {
                    return item != null ? item.getOpportunityName() : null;
                }

                @Override
                public boolean hasNoSummaryTab() {
                    return true;
                }
            };
        }
        return linkingUtil;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    private void initButtons() {
        if (item.getApprover()) {
            Integer currentApproverId = item.getApproverEmployee() != null ? item.getApproverEmployee().getId() : null;
            Integer currentUserId = Utils.getUserID();
            if (Constants.OPPORTUNITY_SUBMITTED.equals(statusCode) && currentUserId.equals(currentApproverId)) {
                approveButton.setVisible(true);
                declineButton.setVisible(true);
                submitButton.setVisible(false);
            }

            if (Constants.OPPORTUNITY_REJECTED.equals(statusCode) && item.getCreatorID() != null && currentUserId.equals(item.getCreatorID())) {
                submitButton.setVisible(true);

            }
        }
    }

    private void saveStatus(String statusCode) {
        item.setStatusCode(statusCode);
        LoadingPanel.loading(true);
        crmService.updateOpportunityStatus(item, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void objectId) {
                closeTab();
                LoadingPanel.loading(false);
            }
        });
    }

    @Override
    public String getPropertyCode() {
        return Constants.Opportunities;
    }
}
