package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.availability.client.localization.AvailabilityMessages;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityServiceAsync;
import com.edatasite.workforce.gwt.availability.client.rpc.BenefitRequestItem;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeLeaveStatusListItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.UiSettings;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.grayForm.GrayForm;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Created by Djuraev on 8/6/15.
 */
public class AddBenefitRequestView extends CustomForm2 implements Constants, CustomFormConstants, CommandConstants, FormHasCustomFieldInterface, Colapse {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final AvailabilityServiceAsync availabilityService = AvailabilityService.App.get();
    private static final AvailabilityMessages availabilityMessages = AvailabilityMessages.App.get();

    private final Integer objectID;
    private Integer employeeID = null;
    private Integer benefitID = null;
    private BenefitRequestItem item;
    private EmployeeLookUpWithCode requester, approver;
    private AdvancedInputGroup inputGroup;
    private DataListBox benefit;
    private TextBox requestedQuantity;
    private TextArea2 description;
    private HTML rejectionReason;
    private DatePicker datePicker;
    private VerticalPanel leftQuantityPanel;
    private Label takenLiveDayLabel;
    private Label leftLiveDayLabel;
    private Double totalLeft;
    private Double totalUsed;
    private String qtyType = "";
    private WfmButton2 approveButton;
    private WfmButton2 rejectButton;
    private WfmButton2 updateButton;
    private WfmButton2 addRequestButton;
    private FormHasCustomField customFieldUtil;
    private LinkedHashMap<String, FormProperty> formPropertyMap;

    public AddBenefitRequestView(Integer objectID) {
        super("benefitRequesrtadd", wfmStrings.addRequest());
        this.objectID = objectID;
    }

    @Override
    public String getIconStyle() {
        return "hrms add-leave-request";
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.BenefitRequestList, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                initialize();

            }
        });
        return null;
    }

    @Override
    protected void registerFields() {

    }
    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    protected void initPredefinedValues() {

    }

    private void initialize() {
        String benefit_request = "benefit_request_";

        requester = new EmployeeLookUpWithCode();
        requester.ensureDebugId(benefit_request + "requester");
        requester.addStyleName(DEFAULT_WIDTH);
        requester.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (suggestionSelectionEvent.getSelectedItem() != null) {
                employeeID = requester.getSelectedItemID();
                getTotalLeftAllowance();
            }
        });

        approver = new EmployeeLookUpWithCode();
        approver.setPermissionCode(PermissionConstants.BENEFIT_REQUEST_APPROVER);
        approver.ensureDebugId(benefit_request + "approver");
        approver.addStyleName(DEFAULT_WIDTH);

        benefit = new DataListBox();
        benefit.ensureDebugId(benefit_request + "benefit");
        benefit.addValueChangeHandler(changeEvent -> {
            if (benefit.isSomethingSelected()) {
                benefitID = benefit.getSelectedId();
                getTotalLeftAllowance();
            } else {
                takenLiveDayLabel.setText("");
                leftLiveDayLabel.setText("");
            }
        });
        inputGroup = new AdvancedInputGroup(benefit);
        if (Utils.hasPermission(PermissionConstants.BENEFIT_TYPE_ADD)) {
            inputGroup.setAppender("ficon--plus");
            inputGroup.appenderClickHandler(() -> Window.open(GWT.getHostPageBaseURL() + UiSettings.getInstance().SETTINGS + "#benefit|add/add", null, null));

        }

        requestedQuantity = new TextBox();
        requestedQuantity.ensureDebugId("benifit_request-quantity");
        Validation.addNumericKeyboardListener(requestedQuantity);
        requestedQuantity.addKeyPressHandler(event -> requestedQuantity.removeStyleName(ERROR_FORM_STYLE));
        requestedQuantity.setWidth("9em");
        requestedQuantity.addFocusHandler(focusEvent -> {
            if (("0.0".equals(requestedQuantity.getValue()) || "0".equals(requestedQuantity.getValue()))) {
                requestedQuantity.setValue("");
            }
        });
        requestedQuantity.addBlurHandler(blurEvent -> {
            if ("".equals(requestedQuantity.getValue().trim())) {
                requestedQuantity.setValue("0.0");
            }
        });

        datePicker = new DatePicker();
        datePicker.addStyleName(Constants.DEFAULT_WIDTH);
        datePicker.ensureDebugId(benefit_request + "date");
        datePicker.setDate(new Date());
        datePicker.addChangeHandler(changeEvent -> getBenefitTypes());

        description = new TextArea2(wfmStrings.description());
        description.ensureDebugId("benifit_request-description");
        description.addStyleName("file--AddBenefitRequestVew");

        rejectionReason = new HTML();
        rejectionReason.ensureDebugId(benefit_request + "rejectionReason");

        takenLiveDayLabel = new Label();
        takenLiveDayLabel.ensureDebugId(benefit_request + "taken");

        leftLiveDayLabel = new Label();
        leftLiveDayLabel.ensureDebugId(benefit_request + "taken");

        leftQuantityPanel = new VerticalPanel();
        leftQuantityPanel.add(takenLiveDayLabel);
        leftQuantityPanel.add(leftLiveDayLabel);

        addFields();
    }

    private void addFields() {
        addTitleField(CustomFormConstants.INFORMATION, wfmStrings.generalInformation());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REQUESTER) != null) {
            addField(CustomFormConstants.REQUESTER, requester, getTitle(formPropertyMap.get(CustomFormConstants.REQUESTER).isChanged() ? formPropertyMap.get(CustomFormConstants.REQUESTER).getTitle() : wfmStrings.requester(), formPropertyMap.get(CustomFormConstants.REQUESTER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.REQUESTER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.REQUESTER).isInformation()) {
                new KpiToolTip(requester, formPropertyMap.get(CustomFormConstants.REQUESTER).getInformationText());
            }

            requester.setEnabled(!formPropertyMap.get(CustomFormConstants.REQUESTER).isDisabled());
        } else {
            addField(CustomFormConstants.REQUESTER, requester, getTitle(wfmStrings.requester(), true));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.APPROVER) != null) {
            addField(CustomFormConstants.APPROVER, approver, getTitle(formPropertyMap.get(CustomFormConstants.APPROVER).isChanged() ? formPropertyMap.get(CustomFormConstants.APPROVER).getTitle() : wfmStrings.approver(), formPropertyMap.get(CustomFormConstants.APPROVER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.APPROVER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.APPROVER).isInformation()) {
                new KpiToolTip(approver, formPropertyMap.get(CustomFormConstants.APPROVER).getInformationText());
            }

            approver.setEnabled(!formPropertyMap.get(CustomFormConstants.APPROVER).isDisabled());
        } else {
            addField(CustomFormConstants.APPROVER, approver, getTitle(wfmStrings.approver(), true));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DATE_PERIOD) != null) {
            addField(CustomFormConstants.DATE_PERIOD, datePicker, getTitle(formPropertyMap.get(CustomFormConstants.DATE_PERIOD).isChanged() ? formPropertyMap.get(CustomFormConstants.DATE_PERIOD).getTitle() : wfmStrings.date(), formPropertyMap.get(CustomFormConstants.DATE_PERIOD).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.DATE_PERIOD).isInformation());
            if (formPropertyMap.get(CustomFormConstants.DATE_PERIOD).isInformation()) {
                new KpiToolTip(datePicker, formPropertyMap.get(CustomFormConstants.DATE_PERIOD).getInformationText());
            }

            datePicker.setEnabled(!formPropertyMap.get(CustomFormConstants.DATE_PERIOD).isDisabled());
        } else {
            addField(CustomFormConstants.DATE_PERIOD, datePicker, getTitle(wfmStrings.date(), true));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BENEFIT_TYPE) != null) {
            addField(CustomFormConstants.BENEFIT_TYPE, inputGroup, getTitle(formPropertyMap.get(CustomFormConstants.BENEFIT_TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.BENEFIT_TYPE).getTitle() : wfmStrings.type(), formPropertyMap.get(CustomFormConstants.BENEFIT_TYPE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.BENEFIT_TYPE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.BENEFIT_TYPE).isInformation()) {
                new KpiToolTip(inputGroup, formPropertyMap.get(CustomFormConstants.BENEFIT_TYPE).getInformationText());
            }

            inputGroup.setEnabled(!formPropertyMap.get(CustomFormConstants.BENEFIT_TYPE).isDisabled());
        } else {
            addField(CustomFormConstants.BENEFIT_TYPE, inputGroup, getTitle(wfmStrings.type(), true));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LEFT_QUANTITY) != null) {
            addField(CustomFormConstants.LEFT_QUANTITY, leftQuantityPanel, getTitle(formPropertyMap.get(CustomFormConstants.LEFT_QUANTITY).isChanged() ? formPropertyMap.get(CustomFormConstants.LEFT_QUANTITY).getTitle() : " "), true, formPropertyMap.get(CustomFormConstants.LEFT_QUANTITY).isInformation());
            if (formPropertyMap.get(CustomFormConstants.LEFT_QUANTITY).isInformation()) {
                new KpiToolTip(leftQuantityPanel, formPropertyMap.get(CustomFormConstants.LEFT_QUANTITY).getInformationText());
            }
            requester.setEnabled(!formPropertyMap.get(CustomFormConstants.REQUESTER).isDisabled());
        } else {
            addField(CustomFormConstants.LEFT_QUANTITY, leftQuantityPanel, "", true);
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.APPROVER) != null) {
            addField(CustomFormConstants.REQUESTED_QUANTITY, requestedQuantity, getTitle(formPropertyMap.get(CustomFormConstants.APPROVER).isChanged() ? formPropertyMap.get(CustomFormConstants.APPROVER).getTitle() : wfmStrings.qty(), formPropertyMap.get(CustomFormConstants.APPROVER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.APPROVER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.APPROVER).isInformation()) {
                new KpiToolTip(requestedQuantity, formPropertyMap.get(CustomFormConstants.APPROVER).getInformationText());
            }

            requestedQuantity.setEnabled(!formPropertyMap.get(CustomFormConstants.REQUESTER).isDisabled());
        } else {
            addField(CustomFormConstants.REQUESTED_QUANTITY, requestedQuantity, getTitle(wfmStrings.qty(), true));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null) {
            addField(CustomFormConstants.DESCRIPTION, description, null, false, formPropertyMap.get(CustomFormConstants.DESCRIPTION).isInformation());
            if (formPropertyMap.get(CustomFormConstants.DESCRIPTION).isInformation()) {
                new KpiToolTip(description, formPropertyMap.get(CustomFormConstants.DESCRIPTION).getInformationText());
            }

            requester.setEnabled(!formPropertyMap.get(CustomFormConstants.REQUESTER).isDisabled());
        } else {
            addField(CustomFormConstants.DESCRIPTION, description, null);
            description = new TextArea2(wfmStrings.description());
        }
        getCustomFieldUtil().drawCustomFields(this, objectID);
        description.ensureDebugId("benifit_request-description");
        description.addStyleName("file--AddBenefitRequestVew");

        show();
    }

    private void getTotalLeftAllowance() {
        if (employeeID != null && benefitID != null && datePicker.getDate() != null && !"".equals(datePicker.getDate()) && benefit.isSomethingSelected()) {
            LoadingPanel.loading(true);
            availabilityService.getTotalAndLeftRequest(employeeID, benefitID, new DateNonConvertable(datePicker.getDate()), new AbstractAsyncCallback<EmployeeLeaveStatusListItem>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(EmployeeLeaveStatusListItem result) {
                    LoadingPanel.loading(false);
                    totalLeft = Double.valueOf(result.getTotalLeftRequest());
                    totalUsed = Double.valueOf(result.getTotalUsedRequest());
                    qtyType = result.getQtyType();
                    takenLiveDayLabel.setText(hrmsStrings.takenBefore() + ": " + totalUsed + " " + result.getQtyType());
                    leftLiveDayLabel.setText(wfmStrings.left() + ": " + totalLeft + " " + result.getQtyType());
                }
            });
        } else {
            takenLiveDayLabel.setText("");
            leftLiveDayLabel.setText("");
        }
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        availabilityService.getBenefitRequests(objectID, new AbstractAsyncCallback<BenefitRequestItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(BenefitRequestItem result) {
                LoadingPanel.loading(false);
                item = result;
                buttonChange();
                if (BR_REJECTED.equals(item.getStatus().getCode())) {
                    rejectionReason.setText(item.getRejectionReason());
                    addField(CustomFormConstants.REJECTION_REASON, rejectionReason, getTitle(wfmStrings.rejectionReason(), false));
                }

                if (item.getRequesterID() != null) {
                    employeeID = item.getRequesterID();
                    requester.setSelected(item.getRequesterID(), item.getUserID().equals(item.getRequesterID()) ? item.getRequester() + " (" + wfmStrings.myself() + ")" : item.getRequester());
                } else {
                    employeeID = item.getUserID();
                    requester.setSelected(item.getUserID(), item.getUser());
                }
                if (!Utils.hasPermission(PermissionConstants.ADD_BENEFIT_REQUEST_ANYBODY)) {
                    requester.setEnabled(false);
                }
                getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFields());


                benefitID = item.getBenefitID();

                if (item.getDate() != null && item.getDate().getNonConvertedDate() != null) {
                    datePicker.setDate(item.getDate().getNonConvertedDate());
                }
                requestedQuantity.setValue(String.valueOf(item.getRequestedQuantity()));
                description.setText(item.getDescription());

                if (item.getApproverID() != null) {
                    approver.setSelected(item.getApproverID(), item.getApprover());
                }
                if (objectID != null) {
                    setDefaultValues();
                    if (!Utils.hasPermission(PermissionConstants.EDIT_BENEFIT_REQUEST)) {
                        enableItems(false);
                    }
                    if (Utils.hasPermission(PermissionConstants.CHANGE_BENEFIT_REQUEST_APPROVER)) {
                        approver.setEnabled(true);
                    }
                } else {

                    setDefaultValuesByFormProperty();
                }

                getBenefitTypes();
            }
        });
    }

    private void setDefaultValuesByFormProperty() {
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REQUESTER) != null && formPropertyMap.get(CustomFormConstants.REQUESTER).getDefaultValue() != null) {
//            requester.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.REQUESTER).getDefaultValue()));
            requester.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.REQUESTER).getSelectedId(), formPropertyMap.get(CustomFormConstants.REQUESTER).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.APPROVER) != null && formPropertyMap.get(CustomFormConstants.APPROVER).getDefaultValue() != null) {
            approver.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.APPROVER).getSelectedId(), formPropertyMap.get(CustomFormConstants.APPROVER).getDefaultValue()));
//            approver.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.APPROVER).getSelectedId(), formPropertyMap.get(CustomFormConstants.APPROVER).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DATE_PERIOD) != null && formPropertyMap.get(CustomFormConstants.DATE_PERIOD).getDefaultValue() != null) {
//           datePicker.setDate(DateUtils.fullDateFormat.parse());
            if (!"".equals(formPropertyMap.get(CustomFormConstants.DATE_PERIOD).getDefaultValue()) && ("TODAY".equals(formPropertyMap.get(CustomFormConstants.DATE_PERIOD).getDefaultValue()) || "TOMORROW".equals(formPropertyMap.get(CustomFormConstants.DATE_PERIOD).getDefaultValue())
                    || "YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.DATE_PERIOD).getDefaultValue()))) {
                Date currentDate = new Date();
                if ("TOMORROW".equals(formPropertyMap.get(CustomFormConstants.DATE_PERIOD).getDefaultValue())) {
                    currentDate = DateUtil.addDays(currentDate, 1);
                } else if ("YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.DATE_PERIOD).getDefaultValue())) {
                    currentDate = DateUtil.minusDays(currentDate, 1);
                }
                datePicker.setDate(currentDate);
            } else {
                try {
                    datePicker.setDate(DateUtils.parse(formPropertyMap.get(CustomFormConstants.DATE_PERIOD).getDefaultValue()));
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }

        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BENEFIT_TYPE) != null && formPropertyMap.get(CustomFormConstants.BENEFIT_TYPE).getDefaultValue() != null) {

            benefit.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.BENEFIT_TYPE).getSelectedId(), formPropertyMap.get(CustomFormConstants.BENEFIT_TYPE).getDefaultValue()));

        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LEFT_QUANTITY) != null && formPropertyMap.get(CustomFormConstants.LEFT_QUANTITY).getDefaultValue() != null) {
//            leftQuantityPanel.setHorizontalAlignment(new HorizontalPanel());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REQUESTED_QUANTITY) != null && formPropertyMap.get(CustomFormConstants.REQUESTED_QUANTITY).getDefaultValue() != null) {
            requestedQuantity.setValue(String.valueOf(formPropertyMap.get(CustomFormConstants.REQUESTED_QUANTITY).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION).getDefaultValue() != null) {
            description.setText(formPropertyMap.get(CustomFormConstants.DESCRIPTION).getDefaultValue());
        }

    }
    private void enableItems(boolean enabled) {
        requester.setEnabled(enabled);
        approver.setEnabled(enabled);
        datePicker.setEnabled(enabled);
        description.setEnabled(enabled);
        benefit.setEnabled(enabled);
        requestedQuantity.setEnabled(enabled);
    }

    private void getBenefitTypes() {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setActive(true);
        filterParameter.setStartDateWithoutOffset(datePicker.getDate());
        availabilityService.getBenefitListAsSelectItems(filterParameter, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(SelectItem[] items) {
                benefit.setItems(items);
                if (item.getBenefitID() != null) {
                    benefit.setSelected(item.getBenefitID());
                }
                getTotalLeftAllowance();
            }
        });
    }

    private void buttonChange() {
        boolean isBenefitOwnerOrManager = Utils.getUserID().equals(item.getApproverID()) || Utils.hasPermission(PermissionConstants.APPROVE_REJECT_ALL_BENEFIT_REQUESTS);
        if (isBenefitOwnerOrManager) {
            if (BR_WAITING_FOR_APPROVAL.equals(item.getStatus().getCode())) {
                approveButton.setVisible(true);
                rejectButton.setVisible(true);
            }
        } else {
            approveButton.setVisible(false);
            rejectButton.setVisible(false);
        }
        updateButton.setVisible(BR_WAITING_FOR_APPROVAL.equals(item.getStatus().getCode()));
        addRequestButton.setVisible(item.getObjectID() == null);
    }

    @Override
    protected void addButtons() {
        addRequestButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        addRequestButton.addClickHandler(clickEvent -> save(BR_WAITING_FOR_APPROVAL));
        addRequestButton.setVisible(false);
        addButton(addRequestButton);

        approveButton = new WfmButton2(wfmStrings.approve(), WfmButton2.BTN_SUCCESS);
        approveButton.addClickHandler(clickEvent -> changeStatus(item.getObjectID(), BR_APPROVED, null));
        approveButton.setVisible(false);
        addButton(approveButton);

        rejectButton = new WfmButton2(wfmStrings.reject(), WfmButton2.BTN_REJECT);
        rejectButton.addClickHandler(event -> {
            final GrayForm grayForm = new GrayForm();
            grayForm.noteShell(wfmStrings.rejectionReason(), true, false, null);
            grayForm.addHistoryPanel(false);
            Command noteListener = () -> changeStatus(item.getObjectID(), BR_REJECTED, grayForm.getHistory().getComment());
            grayForm.setNoteListener(noteListener);
        });
        rejectButton.setVisible(false);
        addButton(rejectButton);

        updateButton = new WfmButton2(wfmStrings.update());
        updateButton.addClickHandler(clickEvent -> save(item.getStatus().getCode()));
        updateButton.setVisible(false);
        addButton(updateButton);

    }

    private void changeStatus(Integer objectID, String referenceCode, String note) {
        if (!validate() && !BR_REJECTED.equals(referenceCode)) {
            enableButton(true);
            return;
        }
        LoadingPanel.loading(true);
        availabilityService.changeBenefitRequestStatus(objectID, referenceCode, note, Double.valueOf(requestedQuantity.getValue()), new AbstractAsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Integer result) {
                LoadingPanel.loading(false);
                if (result > 0) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BENEFIT_REQUEST_UPDATE, result, AddBenefitRequestView.this);
                    closeTab();
                } else if (result == 0) {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);

                } else if (result == -1) {
                    Info.show(availabilityMessages.benefitRequestLimitExceeded(benefit.getSelectedItem().getName(), String.valueOf(datePicker.getDate().getYear() + 1900), totalLeft + " " + qtyType), Info.Type.WARNING);
                }
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.BENEFIT_REQUEST_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    private void save(String statusCode) {
        enableButton(false);
        if (!validate()) {
            enableButton(true);
            return;
        }
        item.setRequesterID(null);
        if (requester.getSelectedItem() != null) {
            item.setRequesterID(requester.getSelectedItem().getId());
        }
        item.setApproverID(null);
        if (approver.getSelectedItem() != null) {
            item.setApproverID(approver.getSelectedItem().getId());
        }
        item.setRequestedQuantity(Double.parseDouble(requestedQuantity.getValue()));
        item.setDescription(description.getText());
        item.setDate(new DateNonConvertable(datePicker.getDate()));
        SelectItem status = new SelectItem();
        status.setCode(statusCode);
        item.setStatus(status);
        item.setBenefitID(null);
        if (benefit.getSelectedItem() != null) {
            item.setBenefitID(benefit.getSelectedItem().getId());
        }
        item.setCustomFields(getCustomFieldUtil().getCustomFieldsValue());

        LoadingPanel.loading(true);
        availabilityService.saveBenefitRequest(item, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer objectID) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.request()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BENEFIT_REQUEST_ADD, null, AddBenefitRequestView.this);
                closeTab();
            }
        });

    }

    private boolean validate() {
        int errors = 0;
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REQUESTER) != null) {
            if (formPropertyMap.get(CustomFormConstants.REQUESTER).isRequired()) {
                errors += markAsError(requester, !Validation.validateLookUpRequired(requester));
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.APPROVER) != null) {
            if (formPropertyMap.get(CustomFormConstants.APPROVER).isRequired()) {
                errors += markAsError(approver, !Validation.validateLookUpRequired(approver));
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BENEFIT_TYPE) != null) {
            if (formPropertyMap.get(CustomFormConstants.BENEFIT_TYPE).isRequired()) {
                errors += markAsError(benefit, !Validation.validateListBoxRequired(benefit, new HTML(), wfmStrings.pleaseSpecifyApprover1()));
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REQUESTED_QUANTITY) != null) {
            if (formPropertyMap.get(CustomFormConstants.REQUESTED_QUANTITY).isRequired()) {
                errors += markAsError(requestedQuantity, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.REQUESTED_QUANTITY).isChanged() ?
                        formPropertyMap.get(CustomFormConstants.REQUESTED_QUANTITY).getTitle() : wfmStrings.requestedQuantity(), requestedQuantity, formPropertyMap.get(CustomFormConstants.REQUESTED_QUANTITY).getMinChar()));
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DATE_PERIOD) != null) {
            if (formPropertyMap.get(CustomFormConstants.DATE_PERIOD).isRequired()) {
                errors += markAsError(datePicker, !Validation.validateDate(datePicker));
            }
        }
        ////LEFT Quantity Left
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null) {
            if (formPropertyMap.get(CustomFormConstants.DESCRIPTION).isRequired()) {
                errors += markAsError(description, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.DESCRIPTION).isChanged() ? formPropertyMap.get(CustomFormConstants.DESCRIPTION).getTitle() : wfmStrings.description(), description.getTextArea(), formPropertyMap.get(CustomFormConstants.DESCRIPTION).getMinChar()));
            }
        }
        errors += getCustomFieldUtil().validateCustomFields();


        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }

        double requestedQuantityValue = Double.valueOf(requestedQuantity.getValue());
        if (requestedQuantityValue == 0) {
            Info.show(wfmStrings.requestedQuantityMoreThanZero(), Info.Type.WARNING);
            requestedQuantity.setStyleName(ERROR_FORM_STYLE);
            return false;
        }
        if (requestedQuantityValue - totalLeft > 0) {
            Info.show(availabilityMessages.benefitRequestLimitExceeded(benefit.getSelectedItem().getName(), String.valueOf(datePicker.getDate().getYear() + 1900), totalLeft + " " + qtyType), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
