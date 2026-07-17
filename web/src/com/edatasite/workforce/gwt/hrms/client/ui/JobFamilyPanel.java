package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

/**
 * User: Ilhom Lutfullaev
 * Date: 20.11.2009
 * Time: 16:28:08
 */
public class JobFamilyPanel extends Composite {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private WfmButton2 button_cancel;
    private WfmButton2 button_save;
    private DataListBox jobFamilyDLB;
    private final JobFamilyProvider provider;

    public JobFamilyPanel(JobFamilyProvider provider) {
        this.provider = provider;
        init();
    }

    private void addJFClicked() {
        final KpiModal shell = new KpiModal();
        shell.setTitle(wfmStrings.addNewJobFamily());
        shell.setWidth(330);
        shell.open();

        final TextBox jobFamilyName = new TextBox();
        jobFamilyName.addStyleName(Constants.DEFAULT_WIDTH);
        TextArea jobFamilyDescription = new TextArea();
        jobFamilyDescription.addStyleName(Constants.DEFAULT_WIDTH);
        //save button
        button_save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, event -> {
            enabledButtons(false);
            SelectItem jobFam = new SelectItem();
            int errors = 0;
            if (!Validation.validateTextBoxRequired(jobFamilyName)) {
                errors++;
            }
            if (!Validation.validateTextAreaRequired(jobFamilyDescription)) {
                errors++;
            }
            if (errors == 0) {
                LoadingPanel.loading(true);
                jobFam.setName(jobFamilyName.getText());
                jobFam.setDescription(jobFamilyDescription.getText());
                provider.createJobFamily(jobFam, new JobFamilyCallback() {
                    public void onFailure() {
                        enabledButtons(true);
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        LoadingPanel.loading(false);
                        shell.close();
                    }

                    public void onSuccess(Object result) {
                        enabledButtons(true);
                        Integer id = (Integer) result;
                        SelectItem sel = new SelectItem(id, jobFamilyName.getText());
                        jobFamilyDLB.addListItem(sel);
                        jobFamilyDLB.setSelected(id);
                        LoadingPanel.loading(false);
                        shell.close();
                    }
                });
            } else {
                enabledButtons(true);
                Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            }
        });
        button_cancel = new WfmButton2(wfmStrings.cancel(), event -> shell.close());

        jobFamilyName.getElement().getStyle().setMarginBottom(15, Style.Unit.PX);
        shell.addWidget(jobFamilyName, wfmStrings.jobFamily());
        shell.addWidget(jobFamilyDescription, wfmStrings.description());

        shell.addButton(button_cancel);
        shell.addButton(button_save);
    }

    private void enabledButtons(boolean b) {
        if (button_save != null) {
            button_save.setEnabled(b);
        }
        if (button_cancel != null) {
            button_cancel.setEnabled(b);
        }
    }

    private void init() {
        //job family list box
        String jobFamilyPanel = "job_family_panel_";
        jobFamilyDLB = new DataListBox();
        jobFamilyDLB.ensureDebugId(jobFamilyPanel + "jobFamilyDLB");
        jobFamilyDLB.setAllowFirstItem(true);

        provider.jobFamilies(new JobFamilyCallback() {
            public void onFailure() {
                jobFamilyDLB.addItem(new SelectItem(-1, wfmStrings.thereArenoJobFamilies()), wfmStrings.thereArenoJobFamilies());
            }

            public void onSuccess(Object result) {
                jobFamilyDLB.setItems((SelectItem[]) result);
            }
        });

        AdvancedInputGroup inputGroup = new AdvancedInputGroup(jobFamilyDLB);
        inputGroup.setAppender("ficon--plus");
        inputGroup.appenderClickHandler(this::addJFClicked);
        initWidget(inputGroup);
    }


    public SelectItem getSelectedJobFamily() {
        SelectItem item = new SelectItem();
        if (jobFamilyDLB.getSelectedItem() != null) {
            item = jobFamilyDLB.getSelectedItem();
        }
        return item;
    }

    public void setSelectedJobFamily(Integer id) {
        if (jobFamilyDLB != null) {
            jobFamilyDLB.setSelected(id);
        }
    }

    public void setSelectedJobFamily(SelectItem item) {
        jobFamilyDLB.setSelected(item);
    }

    public boolean validate(final WfmForm.Field field, String errorMessage) {
        if (jobFamilyDLB.getSelectedId() == null) {
            field.setErrorMessage(errorMessage, "");
            jobFamilyDLB.addValueChangeHandler(event -> {
                if (jobFamilyDLB.getSelectedId() != null) {
                    field.setErrorMessage(null, "");
                }
            });
            return false;
        }
        return true;
    }

    public interface JobFamilyProvider {
        void jobFamilies(JobFamilyCallback callback);

        void createJobFamily(SelectItem newJobFamily, JobFamilyCallback callback);
    }

    public interface JobFamilyCallback {
        void onFailure();

        void onSuccess(Object result);
    }

    public void clearDataListBox() {
        jobFamilyDLB.clearSelected();
    }
}