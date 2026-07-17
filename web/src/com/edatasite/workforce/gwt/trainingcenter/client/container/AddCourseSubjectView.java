package com.edatasite.workforce.gwt.trainingcenter.client.container;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.CourseSubjectItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Hasan Xo'janazarov
 * Date: 25.12.12
 * Time: 18:25
 * To change this template use File | Settings | File Templates.
 */
public class AddCourseSubjectView extends CustomForm2 implements Constants, Colapse {
    private static final TCStrings tcStrings = TCStrings.App.get();
    private final TCServiceAsync tcService = TCService.App.get();
    private Integer objectId;
    private DataListBox subjectParent;
    private TextBox name;
    private TextArea2 description;
    private WfmButton2 save;
    private CourseSubjectItem resultParent;
    private LinkedHashMap<String, FormProperty> formPropertyMap;


    public AddCourseSubjectView(Integer objectId) {
        super("addcoursesubject", tcStrings.addCourseSubject());
        if (objectId != null) {
            setDescription(tcStrings.editCourseSubject());
            this.objectId = objectId;
        }
    }
    FormHasCustomField customFieldUtil;

    @Override
    protected Widget onInitialize() {

        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.CourseSubject,getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                AddCourseSubjectView.super.onInitialize();
            }
        });        return null;
    }


    @Override
    protected void registerFields() {
        name = new TextBox();
        name.ensureDebugId("Subject Name");
        name.addStyleName(DEFAULT_WIDTH);
        name.addClickHandler(event -> name.removeStyleName(ERROR_FORM_STYLE));

        subjectParent = new DataListBox();
        subjectParent.addStyleName(DEFAULT_WIDTH);
        subjectParent.ensureDebugId("Parent Subject");


        description = new TextArea2(TextArea2.AREA_LENGTH_2);
        description.addStyleName(DEFAULT_WIDTH);
        description.hideCharacterLimitPanel();
        description.setWidth("100%");
        description.setHeight("150px");
        description.ensureDebugId("Subject Description");
        getCustomFieldUtil().drawCustomFields(this, objectId, false);


        addTitleField(INFORMATION, wfmStrings.information());
        if (formPropertyMap != null && formPropertyMap.get(NAME) != null) {
            addField(NAME, name, getTitle(formPropertyMap.get(NAME).isChanged() ? formPropertyMap.get(NAME).getTitle() : wfmStrings.name(), formPropertyMap.get(NAME).isRequired()),false,
                    formPropertyMap.get(NAME).isInformation());
            name.setEnabled(!formPropertyMap.get(NAME).isDisabled());
            if (formPropertyMap.get(NAME).isInformation()) {
                new KpiToolTip(name, formPropertyMap.get(NAME).getInformationText());
            }
        } else {
            addField(NAME, name, getTitle(wfmStrings.name(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(SUBJECT_PARENT) != null) {
            addField(SUBJECT_PARENT, subjectParent, getTitle(formPropertyMap.get(SUBJECT_PARENT).isChanged() ? formPropertyMap.get(SUBJECT_PARENT).getTitle() : wfmStrings.subjectParent(), formPropertyMap.get(SUBJECT_PARENT).isRequired()),false,
                    formPropertyMap.get(SUBJECT_PARENT).isInformation());
            subjectParent.setEnabled(!formPropertyMap.get(SUBJECT_PARENT).isDisabled());
            if (formPropertyMap.get(SUBJECT_PARENT).isInformation()) {
                new KpiToolTip(subjectParent, formPropertyMap.get(SUBJECT_PARENT).getInformationText());
            }
        } else {
            addField(SUBJECT_PARENT, subjectParent, getTitle(wfmStrings.name(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(DESCRIPTION) != null) {
            addField(DESCRIPTION, description, getTitle(formPropertyMap.get(DESCRIPTION).isChanged() ? formPropertyMap.get(DESCRIPTION).getTitle() : wfmStrings.description(), formPropertyMap.get(DESCRIPTION).isRequired()),false,
                    formPropertyMap.get(DESCRIPTION).isInformation());
            description.setEnabled(!formPropertyMap.get(DESCRIPTION).isDisabled());
            if (formPropertyMap.get(DESCRIPTION).isInformation()) {
                new KpiToolTip(description, formPropertyMap.get(DESCRIPTION).getInformationText());
            }
        } else {
            addField(DESCRIPTION, description, getTitle(wfmStrings.name(), false));
        }
//        addField(NAME, name, getTitle(wfmStrings.name(), true));
//        addField(SUBJECT_PARENT, subjectParent, getTitle(tcStrings.courseSubjectParent()));
//        addField(DESCRIPTION, description, getTitle(wfmStrings.description()));
        show();
    }


    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        tcService.getCourseSubject(objectId, new AbstractAsyncCallback<CourseSubjectItem>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            public void success(CourseSubjectItem result) {
                LoadingPanel.loading(false);
                resultParent = result;
                setParent();
                name.setText(result.getName());
                description.setText(result.getDescription());
                getCustomFieldUtil().fillCustomFieldsWithData(result.getCustomFields());

            }
        });
    }

    @Override
    protected void initPredefinedValues() {

    }


    private void setParent() {
        LoadingPanel.loading(true);
        tcService.getCourseSubjectParent(objectId, new AbstractAsyncCallback<List<SelectItem>>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                throwable.printStackTrace();
            }

            @Override
            public void success(List<SelectItem> result) {
                LoadingPanel.loading(false);
                if (result != null) {
                    SelectItem[] items = result.toArray(new SelectItem[]{});
                    subjectParent.setItems(items);
                    subjectParent.setSelected(resultParent.getParent() != null ? resultParent.getParent().getId() : null);
                }
            }
        });
    }


    @Override
    protected void addButtons() {
        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        save.ensureDebugId("save button");
        save.addClickHandler(clickEvent -> save());
        addButton(save);
    }

    private void save() {
        if (validate()) {
            LoadingPanel.loading(true);
            enableButtons(false);
            CourseSubjectItem subjectItem = getSubjectItem();
            tcService.saveCourseSubject(subjectItem, new AbstractAsyncCallback<Integer>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    enableButtons(true);
                }

                @Override
                public void success(Integer result) {
                    LoadingPanel.loading(false);
                    enableButtons(true);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.subject()), Info.Type.INFO);
                    closeTab();
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ADD_COURSE_SUBJECT, result, AddCourseSubjectView.this);
                }
            });

        }
    }

    private void enableButtons(boolean b) {
        save.setEnabled(b);
    }

    private boolean validate() {
        if (name == null || "".equals(name.getText())) {
            name.addStyleName(Constants.ERROR_FORM_STYLE);
            Info.show(tcStrings.pleaseEnteredRequiredData(), Info.Type.WARNING);
            return false;
        }
        if (0 < getCustomFieldUtil().validateCustomFields()){
            return false;
        }

        return true;
    }

    public CourseSubjectItem getSubjectItem() {
        CourseSubjectItem item = new CourseSubjectItem();
        item.setObjectId(objectId);
        item.setParent(subjectParent.getSelectedItem());
        item.setName(name.getText());
        item.setDescription(description.getText());
        item.setCustomFields(getCustomFieldUtil().getCustomFieldsValue());
        return item;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.ADD_COURSE_SUBJECT_VIEW;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
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
    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }
}
