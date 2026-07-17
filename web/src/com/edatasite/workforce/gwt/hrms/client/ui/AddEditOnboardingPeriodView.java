package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsServiceAsync;
import com.edatasite.workforce.gwt.hrms.client.rpc.OnboardingItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 8/25/12
 * Time: 12:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddEditOnboardingPeriodView extends CustomForm implements Constants, Colapse {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    HrmsServiceAsync hrmsServiceAsync = HrmsService.App.get();

    private TextBox periodName;
    private TextArea2 periodDescription;
    private DataListBox periodDays;
    private DataListBox periodType;
    private DataListBox durationDays;
    private DataListBox durationType;
    private KpiCheckBox active;
    private DataListBox fromDatalistBox;
    private HorizontalPanel phaseStart;
    private InputGroup duration;

    private static final Integer DAY = 0;
    private static final Integer WEEK = 1;
    private static final Integer MONTH = 2;
    private static final Integer COUNT_OF_DAYS = 100;

    private final Integer from = 0;
    private final Integer before = 1;

    private Integer objectId;
    private boolean saveCloseClicked = false;

    private final String addEditPeriodView = "add_edit_onboarding_period_view_";

    public AddEditOnboardingPeriodView() {
        super("onboardingperiodadd", hrmsStrings.addOnboardingPeriod());
    }

    public AddEditOnboardingPeriodView(Integer objectID) {
        super("onboardingperiodadd", hrmsStrings.onboardingEditPeriod());
        if (objectID != null) {
            this.objectId = objectID;
            setDescription(hrmsStrings.onboardingEditPeriod());
        }
    }

    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    @Override
    protected String getWikiCode() {
        return objectId == null ? PermissionConstants.HRMS_ONBOARDING_ADD : PermissionConstants.HRMS_ONBOARDING_EDIT;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.ONBOARDING_PERIOD_FORM;
    }

    @Override
    protected String getFormType() {
        return objectId != null ? LayoutRPC.EDIT : LayoutRPC.ADD;
    }

    @Override
    protected void getDataToFillFields() {
        if (objectId != null) {
            LoadingPanel.loading(true);

            hrmsServiceAsync.getOnboardingPeriod(objectId, new AbstractAsyncCallback<OnboardingItem>() {
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                public void success(OnboardingItem object) {
                    LoadingPanel.loading(false);
                    fillTable(object);
                }
            });

            setEditValues();
        }
    }

    @Override
    protected void addButtons() {

        addButton(wfmStrings.save(), event -> {
            saveCloseClicked = true;
            save();
        });

    }

    public void initialize() {
        KeyboardListenerAdapter adapter = new KeyboardListenerAdapter() {
            public void onKeyPress(Widget sender, char key, int modifiers) {
                if ((!Character.isDigit(key)) && (key != (char) KEY_TAB)
                        && (key != (char) KEY_BACKSPACE)
                        && (key != (char) KEY_DELETE) && (key != (char) KEY_ENTER)
                        && (key != (char) KEY_HOME) && (key != (char) KEY_END)
                        && (key != (char) KEY_LEFT) && (key != (char) KEY_UP)
                        && (key != (char) KEY_RIGHT) && (key != (char) KEY_DOWN)) {
                    ((TextBox) sender).cancelKey();
                }
            }
        };

        periodName = new TextBox();
        periodName.ensureDebugId(addEditPeriodView + "periodName");
        periodName.addStyleName(DEFAULT_WIDTH);

        periodDescription = new TextArea2(wfmStrings.description());
        periodDescription.ensureDebugId(addEditPeriodView + "periodDescription");
//        periodDescription.addStyleName(DEFAULT_WIDTH);

        SelectItem[] days = new SelectItem[COUNT_OF_DAYS];
        for (Integer i = 0; i < COUNT_OF_DAYS; i++) {
            days[i] = new SelectItem(i, i.toString());

        }
        SelectItem[] type = new SelectItem[3];
        type[0] = new SelectItem(DAY, wfmStrings.day());
        type[1] = new SelectItem(WEEK, wfmStrings.week());
        type[2] = new SelectItem(MONTH, wfmStrings.month());


        periodDays = new DataListBox();
        periodDays.ensureDebugId(addEditPeriodView + "periodDays");
        periodDays.setItems(days);

        periodType = new DataListBox();
        periodType.ensureDebugId(addEditPeriodView + "periodType");
        periodType.setItems(type);

        durationDays = new DataListBox();
        durationDays.ensureDebugId(addEditPeriodView + "durationDays");
        durationDays.setItems(days);

        durationType = new DataListBox();
        durationType.ensureDebugId(addEditPeriodView + "durationType");
        durationType.setItems(type);

        fromDatalistBox = new DataListBox();

        SelectItem[] selectItems = new SelectItem[2];
        selectItems[0] = new SelectItem(from, hrmsStrings.fromEmployeeHireDate());
        selectItems[1] = new SelectItem(before, hrmsStrings.beforeEmployeeHireDate());
        fromDatalistBox.setItems(selectItems);

        fromDatalistBox.addValueChangeHandler(changeEvent -> disableWidgets(before.equals(fromDatalistBox.getSelectedId())));

        phaseStart = new HorizontalPanel();
        phaseStart.getElement().addClassName("cellSpace-x");
        InputGroup inputGroup = new InputGroup(periodDays, periodType);
        inputGroup.getElement().getStyle().setPaddingRight(5, Style.Unit.PX);
        phaseStart.add(new AdvancedInputGroup(new HTML(hrmsStrings.within() + " "), inputGroup, null, true, true));
        phaseStart.add(new AdvancedInputGroup(new HTML(wfmStrings.from().toLowerCase() + " "), fromDatalistBox, null, true, true));

        duration = new InputGroup(durationDays, durationType);


        active = new KpiCheckBox();
        active.setValue(true);
        active.ensureDebugId(addEditPeriodView + "active");


        addTitleField(ONBOARDING.ONBOARDING_PERIOD_TITLE, hrmsStrings.addOnboardingPeriod());


        addField(ONBOARDING.ONBOARDING_PERIOD_NAME, periodName, getTitle(wfmStrings.name(), true));
        addField(ONBOARDING.ONBOARDING_PERIOD_DESCRIPTION, periodDescription, null);
        addField(ONBOARDING.ONBOARDING_PERIOD_PHASE_STARTS, phaseStart, getTitle(hrmsStrings.starts(), true));
        addField(ONBOARDING.ONBOARDING_PERIOD_DURATION, duration, getTitle(wfmStrings.duration(), true));
        addField(ONBOARDING.ONBOARDING_PERIOD_ACTIVE, active, getTitle(wfmStrings.active(), true));

        show();

    }

    private void disableWidgets(Boolean disable) {
        if (disable) {
            periodDays.setEnabled(false);
            periodType.setEnabled(false);
            durationDays.setEnabled(false);
            durationType.setEnabled(false);
            periodDays.setSelectedItem(null);
            periodType.setSelectedItem(null);
            durationDays.setSelectedItem(null);
            durationType.setSelectedItem(null);
        } else {
            periodDays.setEnabled(true);
            periodType.setEnabled(true);
            durationDays.setEnabled(true);
            durationType.setEnabled(true);
        }
    }


    private void fillTable(OnboardingItem item) {
        if (item.getPeriodName() != null) {
            periodName.setValue(item.getPeriodName());
        }
        if (item.getPeriodDescription() != null) {
            periodDescription.setText(item.getPeriodDescription());
        }
        if (item.getPeriodRelativeStart() != null) {
            if (item.getPeriodRelativeStart() < COUNT_OF_DAYS) {
                periodDays.setSelected(item.getPeriodRelativeStart());
                periodType.setSelected(DAY);
            } else if (item.getPeriodRelativeStart() > COUNT_OF_DAYS && item.getPeriodRelativeStart() < COUNT_OF_DAYS * 7) {
                periodDays.setSelected(item.getPeriodRelativeStart() / 7);
                periodType.setSelected(WEEK);
            } else if (item.getPeriodRelativeStart() > COUNT_OF_DAYS * 7 && item.getPeriodRelativeStart() < COUNT_OF_DAYS * 30) {
                periodDays.setSelected(item.getPeriodRelativeStart() / 30);
                periodType.setSelected(MONTH);
            }
        }

        if (item.getDuration() != null) {
            if (item.getDuration() < COUNT_OF_DAYS) {
                durationDays.setSelected(item.getDuration());
                durationType.setSelected(DAY);
            } else if (item.getDuration() > COUNT_OF_DAYS && item.getDuration() < COUNT_OF_DAYS * 7) {
                durationDays.setSelected(item.getDuration() / 7);
                durationType.setSelected(WEEK);
            } else if (item.getDuration() > COUNT_OF_DAYS * 7 && item.getDuration() < COUNT_OF_DAYS * 30) {
                durationDays.setSelected(item.getDuration() / 30);
                durationType.setSelected(MONTH);
            }
        }

        if (item.getBeforeHireDate()) {
            fromDatalistBox.setSelected(before);
            disableWidgets(true);
        } else {
            fromDatalistBox.setSelected(from);
        }

        if (item.getPeriodActive() != null) {
            active.setValue(item.getPeriodActive());
        }

    }

    private Integer onboardingPeriodRelativeStart() {
        if (DAY.equals(periodType.getSelectedId())) {
            return periodDays.getSelectedId();
        } else if (WEEK.equals(periodType.getSelectedId())) {
            return periodDays.getSelectedId() * 7;
        } else if (MONTH.equals(periodType.getSelectedId())) {
            return periodDays.getSelectedId() * 30;
        }
        return 0;
    }

    private Integer onboardingPeriodDuration() {
        if (DAY.equals(durationType.getSelectedId())) {
            return durationDays.getSelectedId();
        } else if (WEEK.equals(durationType.getSelectedId())) {
            return durationDays.getSelectedId() * 7;
        } else if (MONTH.equals(durationType.getSelectedId())) {
            return durationDays.getSelectedId() * 30;
        }
        return 0;
    }


    private void save() {
        if (!validate()) {
            return;
        }
        OnboardingItem item = new OnboardingItem();
        if (objectId != null) {
            item.setPeriodId(objectId);
        }

        item.setPeriodName(periodName.getValue());
        item.setPeriodDescription(periodDescription.getText());
        item.setPeriodRelativeStart(onboardingPeriodRelativeStart());
        item.setDuration(onboardingPeriodDuration());
        if (fromDatalistBox.getSelectedId()!=null && fromDatalistBox.getSelectedId().equals(before)) {
            item.setBeforeHireDate(true);
        } else if (fromDatalistBox.getSelectedId()!=null && fromDatalistBox.getSelectedId().equals(from)) {
            item.setBeforeHireDate(false);
        }

        item.setPeriodActive(active.getValue());


        if (objectId == null) {
            LoadingPanel.loading(true);
        } else {
            LoadingPanel.loading(true);
        }

        HrmsService.App.get().saveOnboardingPeriod(item, new AbstractAsyncCallback<Void>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Void o) {
                LoadingPanel.loading(false);
                Info.show(successMessage, Info.Type.INFO);
                onShellOk();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ONBOARDING_PERIOD_ADD_EDIT, o, AddEditOnboardingPeriodView.this);
            }
        });
    }

    private boolean validate() {
        int errors = 0;
        clearErrorStyle();
        errors += markAsError(ONBOARDING.ONBOARDING_PERIOD_NAME, periodName, periodName.getValue() == null || "".equals(periodName.getValue()));
        errors += markAsError(ONBOARDING.ONBOARDING_PERIOD_DESCRIPTION, periodDescription, periodDescription.getText() == null || "".equals(periodDescription.getText()));
        if (fromDatalistBox.getSelectedId()!=null && !fromDatalistBox.getSelectedId().equals(before)) {
            errors += markAsError(ONBOARDING.ONBOARDING_PERIOD_PHASE_STARTS, phaseStart, periodDays.getSelectedId() == null && periodType.getSelectedId() == null);
            errors += markAsError(ONBOARDING.ONBOARDING_PERIOD_DURATION, duration, durationDays.getSelectedId() == null & durationType.getSelectedId() == null);
        }
        errors += markAsError(ONBOARDING.ONBOARDING_PERIOD_ACTIVE, active, active.getValue() == null);

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private String successMessage = Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.period());
    private String errorMessage = wfmStrings.errorOccurredSavingChanges();

    private void setEditValues() {
        successMessage = Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.period());
        errorMessage = hrmsStrings.periodUpdateError();
    }

    private void onShellOk() {
        if (saveCloseClicked) {
            closeTab();
        } else {
            reinit();
        }
    }

    public void reinit() {
        objectId = null;
        initForm();
        initialize();
    }


    public String getIconStyle() {
        return "icon-edit";
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