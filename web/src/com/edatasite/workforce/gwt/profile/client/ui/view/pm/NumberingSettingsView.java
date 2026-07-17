package com.edatasite.workforce.gwt.profile.client.ui.view.pm;

import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.PMNumberingSettings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Sherzod
 * Date: 20.11.2010
 */
public class NumberingSettingsView extends FooteredView implements FittedContent {

    private WfmForm mainFormDragon;
    protected WfmButton2 saveButton;
    protected PMNumberingSettings settings;
    private ProjectNumberingSettingsView prNumberingSettingsView;
    private TaskNumberingSettingsView taskNumberingSettingsView;
    private WorksreamSettingsView worksreamNumberingSettingsView;
    private EmployeeNumberingSettingsView employeeNumberingSettingsView;
    private DepartmentNumberingSettingsView departmentNumberingSettingsView;
    private LeaveRequestNumberingSettingsView leaveRequestNumberingSettingsView;
    private PositionNumberingSettingsView positionNumberingSettingsView;
    private LocationNumberingSettingsView locationNumberingSettingsView;
    private PersonalGoalNumberingSettingsView personalGoalNumberingSettingsView;
    private ProjectGoalNumberingSettingsView projectGoalNumberingSettingsView;
    private PlacementNumberingSettingsView placementNumberingSettingsView;

    protected static final SettingStrings settingsStrings = SettingStrings.App.get();

    public NumberingSettingsView() {
        super("numberingsettings", wfmStrings.numberingSettings());
    }

    @Override
    protected Widget onInitialize() {
        HorizontalPanel title = new HorizontalPanel();

        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.getElement().setId("Numbering_setting_save_button");
        title.add(new HTML("<div class=pg_numb-settings__title>" + wfmStrings.numberingSettings() + "</div>"));
        title.setSpacing(10);
        title.setStyleName("box-bg--1 box-radius--top pg_numb-settings__header");

        add(title);
        add(createFooter());

        mainFormDragon = new WfmForm();
        prNumberingSettingsView = new ProjectNumberingSettingsView();
        taskNumberingSettingsView = new TaskNumberingSettingsView();
        worksreamNumberingSettingsView = new WorksreamSettingsView();
        employeeNumberingSettingsView = new EmployeeNumberingSettingsView();
        departmentNumberingSettingsView = new DepartmentNumberingSettingsView();
        leaveRequestNumberingSettingsView = new LeaveRequestNumberingSettingsView();
        positionNumberingSettingsView = new PositionNumberingSettingsView();
        locationNumberingSettingsView = new LocationNumberingSettingsView();
        personalGoalNumberingSettingsView = new PersonalGoalNumberingSettingsView();
        projectGoalNumberingSettingsView = new ProjectGoalNumberingSettingsView();
        placementNumberingSettingsView = new PlacementNumberingSettingsView();

        mainFormDragon.addField(settingsStrings.projectNumbering(), prNumberingSettingsView.onInitialize());
        mainFormDragon.addField(settingsStrings.taskNumbering(), taskNumberingSettingsView.onInitialize());
        mainFormDragon.addField(settingsStrings.workstreamNumbering(), worksreamNumberingSettingsView.onInitialize());
        mainFormDragon.addField(settingsStrings.employeeNumbering(), employeeNumberingSettingsView.onInitialize());
        mainFormDragon.addField(settingsStrings.departmentNumbering(), departmentNumberingSettingsView.onInitialize());
        mainFormDragon.addField(settingsStrings.leaveRequestNumbering(), leaveRequestNumberingSettingsView.onInitialize());
        mainFormDragon.addField(settingsStrings.positionNumbering(), positionNumberingSettingsView.onInitialize());
        mainFormDragon.addField(settingsStrings.locationNumbering(), locationNumberingSettingsView.onInitialize());
        mainFormDragon.addField(settingsStrings.personalGoalNumbering(), personalGoalNumberingSettingsView.onInitialize());
        mainFormDragon.addField(settingsStrings.projectGoalNumbering(), projectGoalNumberingSettingsView.onInitialize());
        mainFormDragon.addField(settingsStrings.placementNumbering(), placementNumberingSettingsView.onInitialize());
        //mainFormDragon.addButton(saveButton);
        mainFormDragon.addStyleName("box-bg--1 pg_numb-settings");
        add(mainFormDragon);

        saveButton.addClickHandler(clickEvent -> {
            final PMNumberingSettings settings = new PMNumberingSettings();
            prNumberingSettingsView.setSettings(settings);
            taskNumberingSettingsView.setSettings(settings);
            worksreamNumberingSettingsView.setSettings(settings);
            employeeNumberingSettingsView.setSettings(settings);
            departmentNumberingSettingsView.setSettings(settings);
            leaveRequestNumberingSettingsView.setSettings(settings);
            positionNumberingSettingsView.setSettings(settings);
            locationNumberingSettingsView.setSettings(settings);
            personalGoalNumberingSettingsView.setSettings(settings);
            projectGoalNumberingSettingsView.setSettings(settings);
            placementNumberingSettingsView.setSettings(settings);
            LoadingPanel.loading(true);
            ProfileService.App.get().savePMNumberingSettings(settings, "NumberingSettingsView", new AbstractAsyncCallback<Integer>() {
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, wfmStrings.errorOccurredSavingChanges());
                    messageBox.setTitle("Error");
                    messageBox.open();
                }

                public void success(Integer id) {
                    LoadingPanel.loading(false);
                    settings.setObjectID(id);
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.OK, Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.numberingSettings()));
                    messageBox.setTitle("Success");
                    messageBox.open();
                }
            });
        });
        return null;
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return NumberingSettingsView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return NumberingSettingsView.this.getFooterRightSideWidgets();
            }
        });
    }

    public List<Widget> getFooterLeftSideWidgets() {
        return null;
    }

    public List<Widget> getFooterRightSideWidgets() {
        List<Widget> rightSideWidgets = new ArrayList<>();

        Div saveWrapper = new Div();
        saveWrapper.add(saveButton);
        rightSideWidgets.add(saveWrapper);

        return rightSideWidgets;
    }

    @Override
    public String getIconStyle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
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
