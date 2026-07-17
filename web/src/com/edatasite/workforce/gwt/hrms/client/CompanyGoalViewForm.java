package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.GoalRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.ExportPdfButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.ui.CompanyGoalAddEditView2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: romeo
 * Date: 5/25/12
 * Time: 6:09 PM
 */
public class CompanyGoalViewForm extends CompanyGoalAddEditView2 implements FormHasCustomFieldInterface, Constants, Colapse {

    private HTML title, outcome, fromDate, toDate, status, validityPeriod;
    private TextArea2 description;
    private final String companyGoalSummaryView = "company_goal_summary_view_";
    private VerticalPanel linkAndLinkPanel;
    private HasLinks linkingUtil;
    private VerticalPanel addLinkAndLinks;

    public CompanyGoalViewForm(Integer objectId) {
        super("summary", wfmStrings.summaryView());
        this.objectId = objectId;
        this.viewName = hrmsStrings.companyGoal();
        this.type = Constants.COMPANY_GOAL;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected void addButtons() {
        customizeButton.setVisible(false);

        if (Utils.hasPermission(HRMS_COMPANY_GOAL_REMOVE)) {
            MaterialDropDown options = addMoreSplitButton(wfmStrings.options());
            if (Utils.hasRole(Constants.ADMIN)) {
                MaterialLink customize = new MaterialLink(wfmStrings.customize());
                customize.addClickHandler(click -> {
                    String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                    SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + (url != null ? URL.encodeQueryString(url) : ""));
                });
                options.add(customize);
            }

            if (Utils.hasPermission(PermissionConstants.HRMS_COMPANY_GOAL_REMOVE)) {
                MaterialLink deleteButton = new MaterialLink(wfmStrings.delete());
                deleteButton.addClickHandler(event -> {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.warning());
                    messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            HrmsService.App.get().deleteGoal(objectId, type, new AbstractAsyncCallback<Void>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                }

                                @Override
                                public void onSuccess(Void result) {
                                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.item()), Info.Type.INFO);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_GOAL_DELETE, result, CompanyGoalViewForm.this);
                                    closeTab();
                                }
                            });


                        }
                    });
                    messageBox.open();
                });
                options.add(deleteButton);
            }
        }

        ExportPdfButton pdf = new ExportPdfButton(new ExportPdfButton.PdfRequestInterface() {
            @Override
            public String getUrl() {
                return "/goalViewPDFHandler";
            }

            @Override
            public boolean isLandscapeOptionEnabled() {
                return true;
            }

            @Override
            public HashMap<String, String> getParameters() {
                GoalRequestObject requestObject = new GoalRequestObject(objectId);
                requestObject.setType(type);
                HashMap<String, String> parametrs = requestObject.getRequestParams();
                return parametrs;
            }
        });
        addRightButton(pdf);

        if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_COMPANY_GOAL)) {
            addButton(wfmStrings.edit(), WfmButton2.BTN_PRIMARY, event -> SinksContainerFactory.entryPoint.onHistoryChanged("companygoal|editcompanygoal/" + objectId, item.getTitle()));
        }
    }

    public void initialize() {
        LoadingPanel.loading(true);
        //company goal notes
        noteWidget = new NoteWidget(objectId, Constants.COMPANY_GOAL);
        noteWidget.ensureDebugId(companyGoalSummaryView + "notes");
        //company goal title
        title = initHTML();
        title.ensureDebugId(companyGoalSummaryView + "title");
        //company goal description
        description = new TextArea2();
        description.setReadOnly(true);
        description.addStyleName("GoalAddEditView2-description");
        description.setSize("100%", "150px");
        description.ensureDebugId(companyGoalSummaryView + "description");
        //company goal outcome
        outcome = initHTML();
        outcome.ensureDebugId(companyGoalSummaryView + "out_come");
        //company goal from Date
        fromDate = initHTML();
        fromDate.ensureDebugId(companyGoalSummaryView + "from_date");
        //company goal to Date
        toDate = initHTML();
        toDate.ensureDebugId(companyGoalSummaryView + "to_date");
        //company goal status
        status = initHTML();
        status.ensureDebugId(companyGoalSummaryView + "status");
        //company goal validity period
        validityPeriod = initHTML();
        validityPeriod.ensureDebugId(companyGoalSummaryView + "validity_period");
        //company goal attachments
        attachment = new GeneralFileUpload(F_COMP_GOAL, objectId, objectId);
        attachment.ensureDebugId(companyGoalSummaryView + "attachments");

        addLinkAndLinks = new VerticalPanel();
        addLinkAndLinks.add(getLinkingUtil().getAddLink());
        addLinkAndLinks.add(getLinkingUtil().getLinksPanel());
        addLinkAndLinks.ensureDebugId(companyGoalSummaryView + "addLinkAndLinks");

        LoadingPanel.loading(true);
        addFieldsToForm();
    }

    public void addFieldsToForm() {
        addTitleField(CustomFormConstants.GOAL_DETAILS, wfmStrings.details());
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        addField(CustomFormConstants.GOAL_TITLE, title, getTitle(wfmStrings.title()));
        addField(CustomFormConstants.GOAL_DESCRIPTION, description, getTitle(wfmStrings.description()));
        addField(CustomFormConstants.GOAL_OUTCOME, outcome, getTitle(wfmStrings.outcome()));
        GColumn column1 = new GColumn(GColumnEnum.COL_6, fromDate);
        GColumn column2 = new GColumn(GColumnEnum.COL_6, toDate);

        addField(CustomFormConstants.GOAL_START_DATE, new GRow(column1, column2), getTitle(wfmStrings.period()));
        addField(CustomFormConstants.GOAL_STATUS, status, getTitle(wfmStrings.status()));
        addTitleField(CustomFormConstants.ATTACHMENTS_TITLE, wfmStrings.attachments());
        addField(CustomFormConstants.ATTACHMENTS, attachment, null);
        addTitleField(CustomFormConstants.NOTES, wfmStrings.notes());
        addField(CustomFormConstants.CRM_NOTE, noteWidget, null);
        addField(CustomFormConstants.GOAL_VALIDITY_PERIOD, validityPeriod, wfmStrings.validityPeriod());
        if (Utils.hasPermission(PermissionConstants.HRMS_GOAL_LINKS)) {
            addTitleField(CustomFormConstants.LINKS2, wfmStrings.links());
            showSection(CustomFormConstants.LINKS2);
            addField(CustomFormConstants.LINKS, addLinkAndLinks, null);
        } else {
            hideSection(CustomFormConstants.LINKS2);
        }
        getCustomFieldUtil().drawCustomFields(this, objectId, true);
        show();
    }

    public void fillFieldWithValue() {
        setInnerHTML(title, item.getTitle());
        description.setText(item.getDescription());
        if (item.getFromDate() != null) {
            setInnerHTML(fromDate, DateUtils.format(item.getFromDate()) + Utils.getHijriDate(item.getFromDate().getNonConvertedDate()));
        }
        if (item.getToDate() != null) {
            setInnerHTML(toDate, DateUtils.format(item.getToDate()) + Utils.getHijriDate(item.getToDate().getNonConvertedDate()));
        }
        setInnerHTML(outcome, item.getOutcome());
        setInnerHTML(status, item.getStatus());
        if (item.getValidityPeriodItem() != null) {
            setInnerHTML(validityPeriod, item.getValidityPeriodItem().getName());
        }
        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFields(), true);
        if (Utils.hasPermission(PermissionConstants.HRMS_GOAL_LINKS)) {
            getLinkingUtil().getTaggingView().setFromName(item.getTitle());
            getLinkingUtil().getTaggingView().setSelectedRelations(item.getRelations());
            getLinkingUtil().drawLinks();
        }
    }

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(CompanyGoalViewForm.this) {
                @Override
                protected boolean isActionEditing() {
                    return false;
                }

                @Override
                protected Integer getRelationID() {
                    return objectId;
                }

                @Override
                protected String getRelationType() {
                    return RelationItem.TYPE_COMPANY_GOAL;
                }

                @Override
                protected String getRelationName() {
                    return item != null ? item.getTitle() : null;
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
    public String getIconStyle() {
        return "hrms employees-goal-list";
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}