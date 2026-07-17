package com.edatasite.workforce.gwt.news.client.news;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadPanel;
import com.edatasite.workforce.gwt.news.client.rpc.NewsCategory;
import com.edatasite.workforce.gwt.news.client.rpc.NewsData;
import com.edatasite.workforce.gwt.news.client.rpc.NewsService;
import com.edatasite.workforce.gwt.news.client.rpc.NewsServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * User: Sherzod
 * Date: Oct 23, 2009
 * Time: 5:35:14 AM
 */
public class AddEditNewsView extends CustomForm2 implements Constants, CommandConstants, Colapse {

    //Fields
    private TextBox subject;
    private KpiEditor area;
    private KpiEditor shortArea;
    private DataListBox authorsListBox;
    private DatePicker publishDatePicker;
    private DataListBox visibilityBox;
    private DataListBox categoryListBox;
    private DataListBox locations;

    private FooterUploadPanel fileUploadPanel;

    private Integer objectId;
    private Boolean closeTab;

    private final String addEditNews = "hrms_addEditNews_";

    private Command command;
    private KpiModal dialogBox;
    private NewsData item;
    private LinkedHashMap<String, FormProperty> formPropertyMap;

    private final NewsServiceAsync newsService = NewsService.App.get();

    public AddEditNewsView() {
        super("addnews", wfmStrings.createNews());
    }

    public AddEditNewsView(Command command, Integer objectId) {
        this.command = command;
        this.objectId = objectId;
        dialogBox = new KpiModal();
        dialogBox.setSize(500, 300);
        LoadingPanel.loading(true);
        asyncOnInitialize(new AbstractAsyncCallback<Widget>() {
            public void failure(Throwable reason) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.failedToDownloadCodeForThisWidget() + " (" + reason + ")", Info.Type.WARNING);
            }

            public void success(Widget result) {
                LoadingPanel.loading(false);
            }
        });
    }

    public AddEditNewsView(Integer objectId) {
        super("edit", wfmStrings.editNews());
        this.objectId = objectId;
    }

    @Override
    public String getIconStyle() {
        return "workspace w-edit";
    }

    @Override
    protected Widget onInitialize() {
//        super.onInitialize();
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.News, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
//                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                AddEditNewsView.super.onInitialize();
            }
        });

        return null;
    }

    @Override
    protected void initPredefinedValues() {
        addPredefinedValues(LOCATION_FIELD, item.getLocationItems());
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            return getLocalizer().localizeByFieldID(getFormID(), fieldID);
        }
        return null;
    }

    protected void registerFields() {

        subject = new TextBox();
        subject.addStyleName(DEFAULT_WIDTH);

        visibilityBox = new DataListBox();
        visibilityBox.addStyleName(DEFAULT_WIDTH);
        visibilityBox.setAllowFirstItem(false);
        visibilityBox.setWithoutNullLabel(true);
        visibilityBox.setItems(new SelectItem[]{
                new SelectItem(0, wfmStrings.internal()),
                new SelectItem(1, wfmStrings.pub())
        });
        visibilityBox.setSelected(0);

        authorsListBox = new DataListBox();
        authorsListBox.addStyleName(DEFAULT_WIDTH);

        initNewsAuthors();

        publishDatePicker = new DatePicker(new Date(), true);
        publishDatePicker.addStyleName(DEFAULT_WIDTH);

        categoryListBox = new DataListBox();
        categoryListBox.setAllowFirstItem(true);

        locations = new DataListBox();
        locations.addStyleName(DEFAULT_WIDTH);
        locations.setAllowFirstItem(true);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_NEWS_CATEGORY_SAVED, AddEditNewsView.this, (sender, args) -> initNewsCategories((Integer) args));
        initNewsCategories(categoryListBox.getSelectedIndex());

        AdvancedInputGroup inputGroup = new AdvancedInputGroup(categoryListBox);
        inputGroup.setAppender("ficon--plus");
        inputGroup.appenderClickHandler(EditNewsCategoryView::new);


        FlexTable visibilityPanel = new FlexTable();
        visibilityPanel.setWidget(0, 0, visibilityBox);

        shortArea = new KpiEditor(false, true);
        shortArea.ensureDebugId(addEditNews + "shortDescription");

        area = new KpiEditor(false, true);
        area.ensureDebugId(addEditNews + "fullText");

        //add fields to form
        subject.ensureDebugId(addEditNews + "subject");
        addTitleField(CustomFormConstants.HRMS_COMPANY_NEWS, wfmStrings.addNews());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NEWS_SUBJECT) != null) {
            addField(CustomFormConstants.NEWS_SUBJECT, subject, getTitle(formPropertyMap.get(CustomFormConstants.NEWS_SUBJECT).isChanged() ? formPropertyMap.get(CustomFormConstants.NEWS_SUBJECT).getTitle() : wfmStrings.subject(),
                    formPropertyMap.get(CustomFormConstants.NEWS_SUBJECT).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.NEWS_SUBJECT).isInformation());
            subject.setEnabled(!formPropertyMap.get(CustomFormConstants.NEWS_SUBJECT).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.NEWS_SUBJECT).isInformation()){
                new KpiToolTip(subject,formPropertyMap.get(CustomFormConstants.NEWS_SUBJECT).getInformationText());
            }
        } else {
            addField(CustomFormConstants.NEWS_SUBJECT, subject, getTitle(wfmStrings.subject(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NEWS_SHORT_DESCRIPTION) != null) {
            addField(CustomFormConstants.NEWS_SHORT_DESCRIPTION, shortArea, getTitle(formPropertyMap.get(CustomFormConstants.NEWS_SHORT_DESCRIPTION).isChanged() ? formPropertyMap.get(CustomFormConstants.NEWS_SHORT_DESCRIPTION).getTitle() : wfmStrings.shortDescription(),
                    formPropertyMap.get(CustomFormConstants.NEWS_SHORT_DESCRIPTION).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.NEWS_SHORT_DESCRIPTION).isInformation());
            shortArea.setEnabled(formPropertyMap.get(CustomFormConstants.NEWS_SHORT_DESCRIPTION).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.NEWS_SHORT_DESCRIPTION).isInformation()){
                new KpiToolTip(shortArea,formPropertyMap.get(CustomFormConstants.NEWS_SHORT_DESCRIPTION).getInformationText());
            }
        } else {
            addField(CustomFormConstants.NEWS_SHORT_DESCRIPTION, shortArea, wfmStrings.shortDescription());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NEWS_FULL_TEXT) != null) {
            addField(CustomFormConstants.NEWS_FULL_TEXT, area, getTitle(formPropertyMap.get(CustomFormConstants.NEWS_FULL_TEXT).isChanged() ? formPropertyMap.get(CustomFormConstants.NEWS_FULL_TEXT).getTitle() : wfmStrings.fullText(),
                    formPropertyMap.get(CustomFormConstants.NEWS_FULL_TEXT).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.NEWS_FULL_TEXT).isInformation());
            area.setEnabled(!formPropertyMap.get(CustomFormConstants.NEWS_FULL_TEXT).isDisabled());
            if (formPropertyMap.get(NEWS_FULL_TEXT).isInformation()){
                new KpiToolTip(area,formPropertyMap.get(NEWS_FULL_TEXT).getInformationText());
            }
        } else {
            addField(CustomFormConstants.NEWS_FULL_TEXT, area, getTitle(wfmStrings.fullText(), true));
        }

        authorsListBox.ensureDebugId(addEditNews + "employeeList");
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NEWS_AUTHOR) != null) {
            addField(CustomFormConstants.NEWS_AUTHOR, authorsListBox, getTitle(formPropertyMap.get(CustomFormConstants.NEWS_AUTHOR).isChanged() ? formPropertyMap.get(CustomFormConstants.NEWS_AUTHOR).getTitle() : wfmStrings.author(),
                    formPropertyMap.get(CustomFormConstants.NEWS_AUTHOR).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.NEWS_AUTHOR).isInformation());
            authorsListBox.setEnabled(!formPropertyMap.get(CustomFormConstants.NEWS_AUTHOR).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.NEWS_AUTHOR).isInformation()){
                new KpiToolTip(authorsListBox,formPropertyMap.get(NEWS_AUTHOR).getInformationText());
            }
        } else {
            addField(CustomFormConstants.NEWS_AUTHOR, authorsListBox, wfmStrings.author());
        }

        publishDatePicker.ensureDebugId(addEditNews + "publishDate");
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NEWS_PUBLISH_DATE) != null) {
            addField(CustomFormConstants.NEWS_PUBLISH_DATE, publishDatePicker, getTitle(formPropertyMap.get(CustomFormConstants.NEWS_PUBLISH_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.NEWS_PUBLISH_DATE).getTitle() : wfmStrings.date(),
                    formPropertyMap.get(CustomFormConstants.NEWS_PUBLISH_DATE).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.NEWS_PUBLISH_DATE).isInformation());
            publishDatePicker.setEnabled(!formPropertyMap.get(CustomFormConstants.NEWS_PUBLISH_DATE).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.NEWS_PUBLISH_DATE).isInformation()){
                new KpiToolTip(publishDatePicker,formPropertyMap.get(CustomFormConstants.NEWS_PUBLISH_DATE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.NEWS_PUBLISH_DATE, publishDatePicker, wfmStrings.date());
        }

        //add visibility Box
        visibilityBox.ensureDebugId(addEditNews + "visibilityBox");
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NEWS_VISIBILITY) != null) {
            addField(CustomFormConstants.NEWS_VISIBILITY, visibilityPanel, getTitle(formPropertyMap.get(CustomFormConstants.NEWS_VISIBILITY).isChanged() ? formPropertyMap.get(CustomFormConstants.NEWS_VISIBILITY).getTitle() : wfmStrings.visibility(),
                    formPropertyMap.get(CustomFormConstants.NEWS_VISIBILITY).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.NEWS_VISIBILITY).isInformation());
            visibilityBox.setEnabled(!formPropertyMap.get(CustomFormConstants.NEWS_VISIBILITY).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.NEWS_VISIBILITY).isInformation()){
                new KpiToolTip(visibilityPanel,formPropertyMap.get(CustomFormConstants.NEWS_VISIBILITY).getInformationText());
            }
        } else {
            addField(CustomFormConstants.NEWS_VISIBILITY, visibilityPanel, wfmStrings.visibility());
        }
        visibilityBox.setTitle(wfmStrings.newsVisibilityTitle());

        fileUploadPanel = new FooterUploadPanel(Constants.F_NEWS, objectId, true, wfmStrings.attachments());

        //categories
        categoryListBox.ensureDebugId(addEditNews + "categoryList");
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NEWS_CATEGORIES) != null) {
            addField(CustomFormConstants.NEWS_CATEGORIES, inputGroup, getTitle(formPropertyMap.get(CustomFormConstants.NEWS_CATEGORIES).isChanged() ? formPropertyMap.get(CustomFormConstants.NEWS_CATEGORIES).getTitle() : wfmStrings.category(),
                    formPropertyMap.get(CustomFormConstants.NEWS_CATEGORIES).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.NEWS_CATEGORIES).isInformation());
            categoryListBox.setEnabled(!formPropertyMap.get(CustomFormConstants.NEWS_CATEGORIES).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.NEWS_CATEGORIES).isInformation()){
                new KpiToolTip(inputGroup,formPropertyMap.get(CustomFormConstants.NEWS_CATEGORIES).getInformationText());
            }
        } else {
            addField(CustomFormConstants.NEWS_CATEGORIES, inputGroup, getTitle(wfmStrings.category(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LOCATION_FIELD) != null) {
            addField(CustomFormConstants.LOCATION_FIELD, locations, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()),
                    formPropertyMap.get(CustomFormConstants.LOCATION_FIELD).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.LOCATION_FIELD).isInformation());
            locations.setEnabled(!formPropertyMap.get(CustomFormConstants.LOCATION_FIELD).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.LOCATION_FIELD).isInformation()){
                new KpiToolTip(locations,formPropertyMap.get(CustomFormConstants.LOCATION_FIELD).getInformationText());
            }
        } else {
            addField(CustomFormConstants.LOCATION_FIELD, locations, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), false));
        }

        footer.addToLeftSide(fileUploadPanel);
        show();
    }


    private void saveData() {
        enableButton(false);
        NewsData data = new NewsData();
        data.setObjectId(objectId);
        data.setSubject(subject.getText());
        data.setShortDescription(shortArea.getData());
        data.setFullDescription(area.getData());
        data.setShowHomePage(false);
        data.setNews(true);
        data.setTopNews(false);
        data.setSponsoredArticle(false);
        data.setEventArchive(false);
        data.setLocationID(locations.getSelectedId());
        if (authorsListBox.getSelectedItem() != null) {
            data.setAuthor(authorsListBox.getSelectedItem().getName());
            data.setCreatorId(authorsListBox.getSelectedItem().getId());
        }
        data.setPublishedDate(publishDatePicker.getDate());
        data.setVisibility(visibilityBox.getSelectedId() == 0);
        data.setFeatures(false);
        data.setIsPressRelease(false);
        data.setIsOpinion(false);
        data.setIsWhitePaper(false);
        data.setFileItems(fileUploadPanel.getAttachedFiles());

        ArrayList<NewsCategory> categories = new ArrayList<>();
        SelectItem selectedCategory = categoryListBox.getSelectedItem();
        if (selectedCategory != null) {
            NewsCategory newsCategory = new NewsCategory(selectedCategory.getId(), selectedCategory.getName());
            categories.add(newsCategory);
            data.setCategories(categories);
        }

        enableButton(false);
        LoadingPanel.loading(true);
        newsService.saveNews(data, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                enableButton(true);
            }

            @Override
            public void success(Integer result) {
                enableButton(true);
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.companyNews()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_NEWS_ADD, result, AddEditNewsView.this);
                if (closeTab) {
                    if (command != null) {
                        dialogBox.close();
                    } else {
                        closeTab();
                    }
                } else {
                    refresh();
                }
            }
        });
    }

    private void initNewsCategories(final Integer selectedId) {
        newsService.getNewsCategories(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {
                GWT.log(throwable.getMessage());
            }

            @Override
            public void success(SelectItem[] result) {
                categoryListBox.setItems(result);
                categoryListBox.setSelected(selectedId);
            }
        });
    }

    private void initNewsAuthors() {
        newsService.getEmployeeSelectItem(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {
                GWT.log(throwable.getMessage());
            }

            @Override
            public void success(SelectItem[] result) {
                authorsListBox.setItems(result);
                authorsListBox.setSelected(getDefaultAthor());
            }
        });
    }

    @Override
    protected void getDataToFillFields() {
        newsService.getNews(objectId, new AbstractAsyncCallback<NewsData>() {
            @Override
            public void failure(Throwable caught) {
//                BillboardPanel.get().hide();
            }

            @Override
            public void success(NewsData result) {
                item = result;
                fillWithData(item);
            }
        });
    }

    protected void fillWithData(final NewsData result) {
        initPredefinedValues();
        locations.setItems(result.getLocationItems());
        if (result.getObjectId() != null) {
            subject.setText(result.getSubject());
            shortArea.setData(result.getShortDescription());
            area.setData(result.getFullDescription());

            SelectItem itemEmp = new SelectItem(result.getCreatorId(), result.getAuthor());
            authorsListBox.setSelected(itemEmp);
            publishDatePicker.setDate(result.getPublishedDate());
            if (result.getCategories() != null && result.getCategories().size() > 0) {
                SelectItem item = new SelectItem(result.getCategories().get(0).getId(), result.getCategories().get(0).getName());
                categoryListBox.setSelected(item);
            }
            if (result.getLocationID() != null) {
                locations.setSelected(result.getLocationID());
            }
            visibilityBox.setSelectedIndex(result.isVisibility() ? 0 : 1);
        }

        if (objectId == null) {
            setDefaultValuesByFormProperty();
        }
    }

    private void setDefaultValuesByFormProperty() {
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NEWS_SUBJECT) != null && formPropertyMap.get(CustomFormConstants.NEWS_SUBJECT).getDefaultValue() != null) {
            subject.setText(formPropertyMap.get(CustomFormConstants.NEWS_SUBJECT).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NEWS_AUTHOR) != null && formPropertyMap.get(CustomFormConstants.NEWS_FULL_TEXT).getDefaultValue() != null) {
            authorsListBox.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.NEWS_AUTHOR).getSelectedId(), formPropertyMap.get(CustomFormConstants.NEWS_AUTHOR).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NEWS_PUBLISH_DATE) != null && formPropertyMap.get(CustomFormConstants.NEWS_PUBLISH_DATE).getDefaultValue() != null) {
            publishDatePicker.setDate(new Date(formPropertyMap.get(CustomFormConstants.DEPARTMENT_START_DATE).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NEWS_VISIBILITY) != null && formPropertyMap.get(CustomFormConstants.NEWS_VISIBILITY).getDefaultValue() != null) {
            visibilityBox.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.NEWS_VISIBILITY).getSelectedId(), formPropertyMap.get(CustomFormConstants.NEWS_VISIBILITY).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NEWS_CATEGORIES) != null && formPropertyMap.get(CustomFormConstants.NEWS_CATEGORIES).getDefaultValue() != null) {
            categoryListBox.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.NEWS_CATEGORIES).getSelectedId(), formPropertyMap.get(CustomFormConstants.NEWS_CATEGORIES).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LOCATION_FIELD) != null && formPropertyMap.get(CustomFormConstants.LOCATION_FIELD).getDefaultValue() != null) {
            locations.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.LOCATION_FIELD).getSelectedId(), formPropertyMap.get(CustomFormConstants.LOCATION_FIELD).getDefaultValue()));
        }
    }


    @Override
    protected String getFormID() {
        return LayoutRPC.HRMS_COMPANY_NEWS_FORM;
    }

    @Override
    protected String getFormType() {
        return objectId == null ? LayoutRPC.ADD : LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }


    protected void save(final boolean closeTab) {
        this.closeTab = closeTab;
        if (validate()) {
            saveData();
        }
    }

    protected boolean validate() {
        int errors = 0;
        clearErrorStyle();
        errors = customValidate();
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NEWS_SUBJECT) != null && formPropertyMap.get(CustomFormConstants.NEWS_SUBJECT).isRequired()) {
            errors += markAsError(CustomFormConstants.NEWS_SUBJECT, subject, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.NEWS_SUBJECT).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.NEWS_SUBJECT).getTitle() : wfmStrings.subject(), subject, formPropertyMap.get(CustomFormConstants.NEWS_SUBJECT).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NEWS_SHORT_DESCRIPTION) != null && formPropertyMap.get(CustomFormConstants.NEWS_SHORT_DESCRIPTION).isRequired()) {
            errors += markAsError(shortArea, !Validation.validateHTMLTextAreaRequired(shortArea));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NEWS_FULL_TEXT) != null && formPropertyMap.get(CustomFormConstants.NEWS_FULL_TEXT).isRequired()) {
            errors += markAsError(CustomFormConstants.NEWS_FULL_TEXT, area, !Validation.validateMaterialEditorRequired(area));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NEWS_AUTHOR) != null && formPropertyMap.get(CustomFormConstants.NEWS_AUTHOR).isRequired()) {
            errors += markAsError(authorsListBox, !Validation.validateListBoxRequired(authorsListBox));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NEWS_PUBLISH_DATE) != null && formPropertyMap.get(CustomFormConstants.NEWS_PUBLISH_DATE).isRequired()) {
            errors += markAsError(publishDatePicker, !Validation.validateDate(publishDatePicker));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NEWS_VISIBILITY) != null && formPropertyMap.get(CustomFormConstants.NEWS_VISIBILITY).isRequired()) {
            errors += markAsError(visibilityBox, !Validation.validateListBoxRequired(visibilityBox));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NEWS_CATEGORIES) != null && formPropertyMap.get(CustomFormConstants.NEWS_CATEGORIES).isRequired()) {
            errors += markAsError(categoryListBox, !Validation.validateListBoxRequired(categoryListBox));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LOCATION_FIELD) != null && formPropertyMap.get(CustomFormConstants.LOCATION_FIELD).isRequired()) {
            errors += markAsError(locations, !Validation.validateListBoxRequired(locations));
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    @Override
    protected int validateNonStandartFields() {
        int error = 0;
        for (String fieldCode : getRequiredCodes()) {
            if (fieldCode != null) {
                if (NEWS_CATEGORIES.equals(fieldCode)) {
                    error += markAsError(categoryListBox, categoryListBox.getSelectedId() == null);
                }
                if (NEWS_VISIBILITY.equals(fieldCode)) {
                    error += markAsError(visibilityBox, visibilityBox.getSelectedId() == null);
                }
            }
        }
        return error;
    }

    @Override
    protected void addButtons() {
        // init buttons
        //Buttons
        MaterialLink saveAndClose = new MaterialLink(wfmStrings.save());
        MaterialLink saveAndAdd = new MaterialLink(wfmStrings.saveAndNew());

//        cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
//        cancel.ensureDebugId(addEditNews + "cancel");

        saveAndClose.addClickHandler(sender -> save(true));
        saveAndAdd.addClickHandler(sender -> save(false));
//        cancel.addClickHandler(sender -> closeTab());

        MaterialSplitButton splitButton = new MaterialSplitButton(saveAndClose);
        splitButton.addItem(saveAndAdd);

        addButton(splitButton);
//        addButton(cancel);
    }

    protected void refresh() {
        objectId = null;
        subject.setText("");
        area.setData("");
        shortArea.setData("");
        authorsListBox.setSelected(getDefaultAthor());
        visibilityBox.setSelected(0);
        categoryListBox.setSelectedNullLabel();
    }

    private SelectItem getDefaultAthor() {
        return new SelectItem(Utils.getUserID());
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
}
