package com.edatasite.workforce.gwt.news.client.news;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.NewsCommentsWidget;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.ExportPdfButton;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.DocumentImages;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadPanel;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.news.client.rpc.NewsCategory;
import com.edatasite.workforce.gwt.news.client.rpc.NewsData;
import com.edatasite.workforce.gwt.news.client.rpc.NewsService;
import com.edatasite.workforce.gwt.news.client.rpc.NewsServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 23, 2009
 * Time: 8:53:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewsDetailView extends CustomForm2 implements Constants, Colapse {
    private final NewsServiceAsync newsService = NewsService.App.get();
    private NewsData newsData;
    private final Integer objectId;
    private LinkedHashMap<String, FormProperty> formPropertyMap;


    private final DocumentImages.Images images = DocumentImages.get();

    //    private HorizontalPanel filePanel;
    protected NewsCommentsWidget notesPanel;
    private FooterUploadPanel fileUploadPanel;

    private HTML subject, fulltext, shortText, author, publishDate, category, location, visibility;

    public NewsDetailView(Integer objectId) {
        super("summary", wfmStrings.newsDetails());
        this.objectId = objectId;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.HRMS_COMPANY_NEWS_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            return getLocalizer().localizeByFieldID(getFormID(), fieldID);
        }
        return null;
    }

    protected void registerFields() {

        subject = initHTML();
        fulltext = initHTML();
        shortText = initHTML();
        author = initHTML();
        publishDate = initHTML();
        category = initHTML();
        location = initHTML();
        visibility = initHTML();

        notesPanel = new NewsCommentsWidget(objectId, null);

        addTitleField(CustomFormConstants.HRMS_COMPANY_NEWS, wfmStrings.companyNews());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NEWS_SUBJECT) != null) {
            addField(CustomFormConstants.NEWS_SUBJECT, subject, getTitle(formPropertyMap.get(CustomFormConstants.NEWS_SUBJECT).isChanged() ? formPropertyMap.get(CustomFormConstants.NEWS_SUBJECT).getTitle() : wfmStrings.subject()));
        } else {
            addField(CustomFormConstants.NEWS_SUBJECT, subject, getTitle(wfmStrings.subject()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NEWS_SHORT_DESCRIPTION) != null) {
            addField(CustomFormConstants.NEWS_SHORT_DESCRIPTION, shortText, getTitle(formPropertyMap.get(CustomFormConstants.NEWS_SHORT_DESCRIPTION).isChanged() ? formPropertyMap.get(CustomFormConstants.NEWS_SHORT_DESCRIPTION).getTitle() : wfmStrings.shortDescription()));
        } else {
            addField(CustomFormConstants.NEWS_SHORT_DESCRIPTION, shortText, getTitle(wfmStrings.shortDescription()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NEWS_FULL_TEXT) != null) {
            addField(CustomFormConstants.NEWS_FULL_TEXT, fulltext, getTitle(formPropertyMap.get(CustomFormConstants.NEWS_FULL_TEXT).isChanged() ? formPropertyMap.get(CustomFormConstants.NEWS_FULL_TEXT).getTitle() : wfmStrings.fullText()));
        } else {
            addField(CustomFormConstants.NEWS_FULL_TEXT, fulltext, getTitle(wfmStrings.fullText()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NEWS_CATEGORIES) != null) {
            addField(CustomFormConstants.NEWS_CATEGORIES, category, getTitle(formPropertyMap.get(CustomFormConstants.NEWS_CATEGORIES).isChanged() ? formPropertyMap.get(CustomFormConstants.NEWS_CATEGORIES).getTitle() : wfmStrings.category()));
        } else {
            addField(CustomFormConstants.NEWS_CATEGORIES, category, getTitle(wfmStrings.category()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LOCATION_FIELD) != null) {
            addField(CustomFormConstants.LOCATION_FIELD, location, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), formPropertyMap.get(CustomFormConstants.LOCATION_FIELD).isRequired()));
        } else {
            addField(CustomFormConstants.LOCATION_FIELD, location, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location())));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NEWS_VISIBILITY) != null) {
            addField(CustomFormConstants.NEWS_VISIBILITY, visibility, getTitle(formPropertyMap.get(CustomFormConstants.NEWS_VISIBILITY).isChanged() ? formPropertyMap.get(CustomFormConstants.NEWS_VISIBILITY).getTitle() : wfmStrings.visibility()));
        } else {
            addField(CustomFormConstants.NEWS_VISIBILITY, visibility, getTitle(wfmStrings.visibility()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NEWS_AUTHOR) != null) {
            addField(CustomFormConstants.NEWS_AUTHOR, author, getTitle(formPropertyMap.get(CustomFormConstants.NEWS_AUTHOR).isChanged() ? formPropertyMap.get(CustomFormConstants.NEWS_AUTHOR).getTitle() : wfmStrings.author()));
        } else {
            addField(CustomFormConstants.NEWS_AUTHOR, author, getTitle(wfmStrings.author()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NEWS_PUBLISH_DATE) != null) {
            addField(CustomFormConstants.NEWS_PUBLISH_DATE, publishDate, getTitle(formPropertyMap.get(CustomFormConstants.NEWS_PUBLISH_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.NEWS_PUBLISH_DATE).getTitle() : wfmStrings.date()));
        } else {
            addField(CustomFormConstants.NEWS_PUBLISH_DATE, publishDate, getTitle(wfmStrings.date()));
        }

        addTitleField(CustomFormConstants.FEATURED_IMAGE, getTitle(wfmStrings.featuredImage()));
        if (Utils.hasPermission(PermissionConstants.HRMS_COMPANY_NEWS_ADD_COMMENTS)) {
            addTitleField(CustomFormConstants.COMMENTS, wfmStrings.comments());
            addField(CustomFormConstants.COMMENT_BOX, notesPanel, getTitle(wfmStrings.comments()));
        }
        show();
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        if (objectId != null) {
            newsService.getNews(objectId, new AbstractAsyncCallback<NewsData>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(NewsData result) {
                    fillWithData(result);

                    for (HistoryListItem historyListItem : result.getCommentList()) {
                        notesPanel.createNote(historyListItem, false);
                    }
                }
            });
        }
    }

    private void fillWithData(NewsData o) {
        newsData = o;
        setInnerHTML(subject, newsData.getSubject());
        setInnerHTML(fulltext, newsData.getFullDescription());
        setInnerHTML(shortText, newsData.getShortDescription());
        setInnerHTML(author, newsData.getAuthor());
        setInnerHTML(publishDate, DateUtils.format(newsData.getPublishedDate()));
        StringBuilder listString = new StringBuilder();
        if (newsData.getCategories() != null) {
            for (NewsCategory s : newsData.getCategories()) {
                listString.append(s.getName()).append("\t");
            }
        }
        setInnerHTML(category, listString.toString());
        setInnerHTML(location, newsData.getLocation());
        setInnerHTML(visibility, newsData.isVisibility() ? wfmStrings.internal() : wfmStrings.pub());
        if (!(Utils.isNullOrEmpty(newsData.getFileLink())
                || Utils.isNullOrEmpty(newsData.getFileName())
                || Utils.isNullOrEmpty(newsData.getFileContentType())
                || "null".equals(newsData.getFileName())
                || "null".equals(newsData.getFileLink())
                || "null".equals(newsData.getFileContentType())
        )) {
            FileResource resource = new FileResource();
            if (newsData.getImageUrl() != null) {
                Image image = new Image(newsData.getImageUrl());
                image.setWidth("40px");
                image.setHeight("40px");
            } else {
                resource.setContentType(newsData.getFileContentType());
                Image img = getFileIcon(resource).createImage();
                img.addClickHandler(event -> Window.open(newsData.getFileLink(), "_blank", ""));
            }
        }
        LoadingPanel.loading(false);
    }

    @Override
    protected void addButtons() {
        customizeButton.setVisible(false);

        MaterialDropDown options = addMoreSplitButton(wfmStrings.options());
        if (Utils.hasRole(Constants.ADMIN)) {
            MaterialLink customize = new MaterialLink(wfmStrings.customize());
            customize.addClickHandler(click -> {
                String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + (url != null ? URL.encodeQueryString(url) : ""));
            });
            options.add(customize);
        }

        if (Utils.hasPermission(PermissionConstants.HRMS_COMPANY_NEWS_EDIT) || Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(ADMIN_LOCATION) || Utils.hasRole(HR)) {
            MaterialLink deleteButton = new MaterialLink(wfmStrings.delete());
            deleteButton.addClickHandler(event -> {
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                messageBox.setTitle(wfmStrings.warning());
                messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                messageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        newsService.deleteNews(objectId, new AbstractAsyncCallback() {
                            @Override
                            public void failure(Throwable caught) {
                            }

                            @Override
                            public void success(Object result) {
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_NEWS_ADD, result, NewsDetailView.this);
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.news()), Info.Type.INFO);
                                closeTab();
                            }
                        });
                    }
                });
                messageBox.open();
            });
            options.add(deleteButton);
        }
        ExportPdfButton pdf = new ExportPdfButton(new ExportPdfButton.PdfRequestInterface() {
            @Override
            public String getUrl() {
                return "/newsViewPDFHandler";
            }

            @Override
            public boolean isLandscapeOptionEnabled() {
                return true;
            }

            @Override
            public HashMap<String, String> getParameters() {
                return new RequestObject(objectId).getRequestParams();
            }
        });
        addRightButton(pdf);
        fileUploadPanel = new FooterUploadPanel(F_NEWS, objectId, true, wfmStrings.attachments());
        footer.addToLeftSide(fileUploadPanel);

        if (Utils.hasPermission(PermissionConstants.HRMS_COMPANY_NEWS_EDIT)) {
            WfmButton2 editButton = new WfmButton2(wfmStrings.edit(), WfmButton2.BTN_PRIMARY);
            editButton.addClickHandler(event -> {
                closeTab();
                SinksContainerFactory.entryPoint.onHistoryChanged("news|edit/" + objectId, newsData.getSubject());
            });
            addButton(editButton);
        }


    }


    private AbstractImagePrototype getFileIcon(FileResource file) {
        if (file.isFolder()) {
            return AbstractImagePrototype.create(images.folderYellow());
        }
        String mimetype = file.getContentType();
        if (mimetype == null) {
            return AbstractImagePrototype.create(images.documentShared());
        }
        mimetype = mimetype.toLowerCase();
        if (mimetype.startsWith("application/pdf")) {
            return AbstractImagePrototype.create(images.pdfShared());
        } else if (mimetype.endsWith("excel") || mimetype.endsWith("spreadsheetml.sheet")) {
            return AbstractImagePrototype.create(images.spreadsheetShared());
        } else if (mimetype.endsWith("msword") || mimetype.endsWith("wordprocessingml.document")) {
            return AbstractImagePrototype.create(images.wordprocessorShared());
        } else if (mimetype.endsWith("powerpoint") || mimetype.endsWith("presentationml.presentation")) {
            return AbstractImagePrototype.create(images.presentationShared());
        } else if (mimetype.startsWith("application/zip") ||
                mimetype.startsWith("application/gzip") ||
                mimetype.startsWith("application/x-gzip") ||
                mimetype.startsWith("application/x-tar") ||
                mimetype.startsWith("application/x-gtar")) {
            return AbstractImagePrototype.create(images.zipShared());
        } else if (mimetype.startsWith("text/html")) {
            return AbstractImagePrototype.create(images.htmlShared());
        } else if (mimetype.startsWith("text/plain")) {
            return AbstractImagePrototype.create(images.txtShared());
        } else if (mimetype.startsWith("image/")) {
            return AbstractImagePrototype.create(images.imageShared());
        } else if (mimetype.startsWith("video/")) {
            return AbstractImagePrototype.create(images.videoShared());
        } else if (mimetype.startsWith("audio/")) {
            return AbstractImagePrototype.create(images.audioShared());
        }
        return AbstractImagePrototype.create(images.documentShared());
    }

    public void callItemPDF(String pdfURL, RequestObject requestObject) {
        final HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parametrs, "_blank");
    }

    public String getIconStyle() {
        return "workspace news-detail";  //To change body of implemented methods use File | Settings | File Templates.
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
