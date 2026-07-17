package com.edatasite.workforce.gwt.news.client.news;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.news.client.rpc.NewsCategory;
import com.edatasite.workforce.gwt.news.client.rpc.NewsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Nov 12, 2010
 * Time: 7:26:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class EditNewsCategoryView extends Composite implements Constants, Colapse {


    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static Property property;

    protected TextBox txtName;
    private DataListBox dlParent;
    private KpiModal popup;
    protected WfmButton2 saveAndCloseButton;
    protected WfmButton2 closeButton;

    protected Integer objectID;
    private NewsCategory newsCategory;

    public EditNewsCategoryView() {
        this(null);
    }

    public EditNewsCategoryView(Integer objectID) {
        this.objectID = objectID;
        initPopup();
    }

    private void initPopup() {
        initInternal();

        loadData();
    }

    protected void initInternal() {
        popup = new KpiModal();
        popup.setTitle(objectID != null ? wfmStrings.editCategory() : wfmStrings.addCategory());
        popup.setWidth(315);

        txtName = new TextBox();
        txtName.addStyleName(DEFAULT_WIDTH);
        txtName.getElement().getStyle().setMarginBottom(15, Style.Unit.PX);

        dlParent = new DataListBox();
        dlParent.setAllowFirstItem(true);
        dlParent.addStyleName(DEFAULT_WIDTH);

        popup.add(txtName);
        popup.add(dlParent);

        //init buttons
        saveAndCloseButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        closeButton = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);

        saveAndCloseButton.addClickHandler(sender -> save());

        closeButton.addClickHandler(event -> popup.close());

        popup.addButton(closeButton);
        popup.addButton(saveAndCloseButton);

        popup.open();
    }

    protected void loadData() {
        if (objectID != null) {
            NewsService.App.get().getNewsCategory(objectID, new AbstractAsyncCallback<NewsCategory>() {
                @Override
                public void failure(Throwable throwable) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void success(NewsCategory result) {
                    newsCategory = result;
                    if (result != null) {
                        txtName.setText(result.getName());
                        final Integer parentID = result.getParentId();
                        NewsService.App.get().getNewsCategories(new AbstractAsyncCallback<SelectItem[]>() {
                            @Override
                            public void failure(Throwable throwable) {
                                GWT.log(throwable.getMessage());
                            }

                            @Override
                            public void success(SelectItem[] result) {
                                dlParent.setItems(result);
                                if (parentID != null && parentID > 0) {
                                    dlParent.setSelected(parentID);
                                }
                            }
                        });

                    }
                }
            });
        } else {
            NewsService.App.get().getNewsCategories(new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable throwable) {
                    GWT.log(throwable.getMessage());
                }

                @Override
                public void success(SelectItem[] result) {
                    dlParent.setItems(result);
                }
            });

        }
    }

    protected void save() {
        if (validate()) {
            if (newsCategory == null) {
                newsCategory = new NewsCategory();
            }
            newsCategory.setName(txtName.getText());
            newsCategory.setParentId(dlParent.getSelectedId());

            NewsService.App.get().saveNewsCategory(newsCategory, new AbstractAsyncCallback<Integer>() {
                @Override
                public void failure(Throwable throwable) {
                    GWT.log(throwable.getMessage());
                    popup.close();
                }

                @Override
                public void success(Integer result) {
                    LoadingPanel.loading(false);

                    if (result != null && result > 0) {
                        popup.close();
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.newCategory()), Info.Type.INFO);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_NEWS_CATEGORY_SAVED, result, null);
                    } else {
                        Info.show(wfmStrings.newsCategoryCouldNotSaved(), Info.Type.INFO);
                    }
                }
            });
        }
    }

    protected Boolean validate() {
        int errors = 0;

        txtName.removeStyleName("x-form-invalid");
        if (!Validation.validateTextBoxRequired(txtName)) {
            txtName.addStyleName("x-form-invalid");
            errors++;
        }

        if (objectID != null && objectID > 0 && dlParent.getSelectedId() != null && dlParent.getSelectedId() > 0 && objectID.equals(dlParent.getSelectedId())) {
            Info.show(wfmStrings.categoryCannotImplement(), Info.Type.WARNING);
            return false;
        }

        if (errors > 0) {

            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);

            return false;
        }

        return true;
    }
}
