package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.LoginService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.GoogleAuthorizationPanel;
import com.edatasite.workforce.gwt.core.client.ui.InsertSiteUlrPopUp;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.Office365AuthorizationPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCustomToolTip;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.enums.FileUploadType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelectIcons;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterFileUploaderList;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.InDatabaseFileWidgetProvider;
import com.edatasite.workforce.gwt.documents.client.gwtupload.FileInfo;
import com.edatasite.workforce.gwt.documents.client.gwtupload.FileUploaderList;
import com.edatasite.workforce.gwt.documents.client.gwtupload.FileWidget;
import com.edatasite.workforce.gwt.documents.client.gwtupload.Options;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.DD;
import gwt.material.design.client.ui.html.DL;
import gwt.material.design.client.ui.html.DT;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;
import gwt.material.design.client.ui.html.Strong;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * The 'File upload' dialog box implementation.
 */
public class FileUploadDialog extends KpiModal implements CommandConstants, Constants {

    public static final WfmMessages wfmMessages = WfmMessages.App.get();

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final FileUploadDialogUiBinder ourUiBinder = GWT.create(FileUploadDialogUiBinder.class);
    protected final DataListBox taskList = new DataListBox();
    private final Label filenameLabel = new Label("");
    protected FolderResource folder;
    @UiField
    WfmButton2 saveClose;
    @UiField
    WfmButton2 cancel;
    @UiField
    Div mainContent;
    @UiField
    Div tabs;
    @UiField
    Div filesPanel;
    @UiField
    Div uploadContent;
    @UiField
    Div headerContent;
    @UiField
    Div uploadTypeLabel;
    @UiField
    DataListBox uploadType;
    @UiField
    Div popupFolders;
    @UiField
    Div uploadTypeGroup;
    @UiField
    Div buttonGroup;
    @UiField
    Div uploadWrapper;
    private CRMLookUp crmLookUp;
    private FileUploadType fileUploadType;
    private DriveFolderList driveFolderList;
    private Options options;
    private FileUploaderList fileUploaderList;
    private final String target;
    private TextBox tbDescr;
    private boolean closeAfterAdd = false;
    private final boolean isFromDocumentSection;
    private final HashMap<String, FileInfo> fileInfos = new HashMap<>();
    private final HashMap<Integer, FileResource> uploadedFiles = new HashMap<>();
    private final HashMap<Integer, FileResource> googleOrOfficeFiles = new HashMap<>();
    private final HashMap<Integer, FileResource> kpiFiles = new HashMap<>();
    private final Map<String, Command> listeners = new HashMap<>();
    private SelectItem listItem;
    private final Integer maxSize;
    private StorageDocsSelectPopup googleDocsPopup;
    private StorageDocsSelectPopup officeDocsPopup;
    private Div toAmazon;
    private Div toGoogle;
    private Div toOffice;
    private Div upload;
    private Div highlightedDiv;
    private boolean isDownloadable = true;
    private boolean fromMessenger;
    private InDatabaseFileWidgetProvider inDatabaseFileWidgetProvider;
    private final ClickHandler saveCloseHandler = event -> {
        closeAfterAdd = true;
        save();
    };

    /**
     * The widget's constructor.
     *
     * @param folder
     * @param fileUploadType
     * @param maxSize
     * @param isFromDocumentSection
     */
    public FileUploadDialog(FolderResource folder, FileUploadType fileUploadType, Integer maxSize, boolean isFromDocumentSection, InDatabaseFileWidgetProvider inDatabaseFileWidgetProvider) {
        super(false);
        this.folder = folder;
        this.closeAfterAdd = false;
        this.fileUploadType = fileUploadType;
        this.maxSize = maxSize;
        this.isFromDocumentSection = isFromDocumentSection;
        this.target = folder.getName();
        this.isDownloadable = isDownloadable;
        this.inDatabaseFileWidgetProvider = inDatabaseFileWidgetProvider;
        configureFileUploadDialog();
    }

    public FileUploadDialog(FolderResource folder, FileUploadType fileUploadType, Integer maxSize, boolean isFromDocumentSection) {
        this(folder, fileUploadType, maxSize, isFromDocumentSection, true);
    }

    public FileUploadDialog(FolderResource folder, FileUploadType fileUploadType, Integer maxSize, boolean isFromDocumentSection, boolean isDownloadable) {
        super(false);
        this.folder = folder;
        this.closeAfterAdd = false;
        this.fileUploadType = fileUploadType;
        this.maxSize = maxSize;
        this.isFromDocumentSection = isFromDocumentSection;
        this.target = folder.getName();
        this.isDownloadable = isDownloadable;

        configureFileUploadDialog();
    }

    public FileUploadDialog(FolderResource folder, FileUploadType fileUploadType, Integer maxSize, boolean isFromDocumentSection, boolean isDownloadable,boolean fromMessenger) {
        super(false);
        this.folder = folder;
        this.closeAfterAdd = false;
        this.fileUploadType = fileUploadType;
        this.maxSize = maxSize;
        this.isFromDocumentSection = isFromDocumentSection;
        this.target = folder.getName();
        this.isDownloadable = isDownloadable;
        this.fromMessenger = fromMessenger;

        configureFileUploadDialog();
    }

    public FileUploadDialog(FolderResource folder, FileUploadType fileUploadType, Integer maxSize, boolean isFromDocumentSection, SelectItem listItem) {
        super(false);
        this.folder = folder;
        this.closeAfterAdd = false;
        this.fileUploadType = fileUploadType;
        this.maxSize = maxSize;
        this.isFromDocumentSection = isFromDocumentSection;
        this.target = folder.getName();
        this.listItem = listItem;

        configureFileUploadDialog();
    }

    public void runCommand() {
        if (!listeners.isEmpty()) {
            Command command = listeners.get(FileUploadEvents.FILE_UPLOADED);
            if (command != null) {
                command.execute();
            }
        }
    }

    private void configureFileUploadDialog() {
        addStyleName("modal--kpi-upload");
        DocumentsService.App.get().getEnableUploadTypes(new AbstractAsyncCallback<HashMap<String, Boolean>>() {
            public void failure(Throwable throwable) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(HashMap<String, Boolean> result) {
                DocumentsView.get().getDocumentsService().getStorageSize(new AsyncCallback<Double[]>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        Utils.setMaxStorage(0.0);
                    }

                    @Override
                    public void onSuccess(Double[] aDouble) {
                        Utils.setMaxStorage(aDouble[0]);
                        Utils.setUsedStorage(aDouble[1]);
                        initializeData(result);
                    }
                });
            }
        });
    }

    public void uploadState() {
        headerContent.getElement().setInnerHTML(wfmStrings.uploadedFiles());
        highlightThis(upload);
        clearMainContent();
        resetUploadWrapper();
        mainContent.add(fileUploaderList);
        setFilesPanelVisible(true);
    }

    private void setFilesPanelVisible(boolean visible) {
        filesPanel.setVisible(visible);
    }

    private void highlightThis(Div highlightedDiv) {
        if (this.highlightedDiv != null) {
            this.highlightedDiv.removeStyleName("kpi-upload__tabs-item--active");
        }
        this.highlightedDiv = highlightedDiv;
        this.highlightedDiv.addStyleName("kpi-upload__tabs-item--active");
    }

    private void initializeData(HashMap<String, Boolean> result) {
        add(ourUiBinder.createAndBindUi(FileUploadDialog.this));
        setDismissible(false);
        fileInfos.clear();
        if (getEntityId() != null) {
            uploadedFiles.clear();
        }
        uploadTypeLabel.getElement().setInnerHTML(wfmStrings.selectStorageToUploadFiles());
        tbDescr = new TextBox();
        tbDescr.setPlaceHolder(wfmStrings.addNote());
        googleOrOfficeFiles.clear();
        kpiFiles.clear();
        headerContent.getElement().setInnerHTML(wfmStrings.selectedFiles());
        String language = Utils.userSettings.get(LANGUAGE_FOR_USER);
        if (language != null) {
            if (language.contains("ru")) {
                saveClose.setText("Сохранить");
            } else {
                saveClose.setText(wfmStrings.saveOnlyWithClose());
            }
        } else {
            saveClose.setText(wfmStrings.saveOnlyWithClose());
        }
        saveClose.addStyleName(BTN_PRIMARY);
        saveClose.addClickHandler(saveCloseHandler);
        final WfmForm form = new WfmForm(new String[]{"40%", "60%"});
        saveClose.getElement().setId("saveClose_id");

        //upload type amazon or google docs
        options = new Options(folder, AMAZON_PARAM_NAME, "");
        options.setMaxSize(maxSize);
        fileUploaderList = createFileUploaderList(options, filesPanel);
        fileUploaderList.restart(getEntityId());

        uploadType.setWithoutNullLabel(true);
        uploadType.setVisible(false);
        uploadTypeLabel.setVisible(false);
        uploadType.addValueChangeHandler((event) -> onUploadTypeChange());

        upload = new Div("kpi-upload__tabs-item");
        MaterialIcon fromKpiIcon = new MaterialIcon();
        fromKpiIcon.addStyleName("ficon--folder-bold mr-2");
        upload.add(fromKpiIcon);
        Span fromKpiSpan = new Span(wfmStrings.upload());
        upload.add(fromKpiSpan);
        upload.addClickHandler(clickEvent -> {
            uploadState();
        });
        /*options = new Options(folder, AMAZON, "");
        FileUploadDialog.this.fileUploadType = FileUploadType.AMAZON;
        fileUploaderList.setStorage(AMAZON_PARAM_NAME);*/
        uploadState();
        tabs.add(upload);

        //Amazon Storage
        toAmazon = new Div("kpi-upload__tabs-item");
        Image image = new Image("/mainStyles/new-ui/images/new-kpi-logo.svg");
        image.getElement().setAttribute("width", "25");
        image.addStyleName("mr-2");
        toAmazon.add(image);
        Span imageSpan = new Span();
        imageSpan.getElement().setInnerHTML(wfmStrings.storage());
        toAmazon.add(imageSpan);
        toAmazon.addClickHandler(clickEvent -> {
            headerContent.getElement().setInnerHTML(wfmStrings.folderName());
            highlightThis(toAmazon);
            options = new Options(folder, AMAZON, "");
            showKpiSelectDocsPopup();

        });

        if (result.get(AMAZON) != null && result.get(AMAZON)) {
            uploadType.addListItem(new SelectItem(0, wfmMessages.nameStorage(Utils.getHostName())));
            uploadType.setVisible(true);
            uploadTypeLabel.setVisible(true);
            tabs.add(toAmazon);
        } else {
            toAmazon.addStyleName("kpi-upload__tabs-item--locked");
        }
        if (fileUploadType.equals(FileUploadType.AMAZON)) {
            uploadType.setSelected(0);
            options = new Options(folder, AMAZON, "");
        }

        //Google Storage
        toGoogle = new Div("kpi-upload__tabs-item");
        Div googleIcon = new Div("kpi-upload__tabs-item-google-icon");
        toGoogle.add(googleIcon);
        toGoogle.addClickHandler(clickEvent -> {
            headerContent.getElement().setInnerHTML(wfmStrings.folderName());
            highlightThis(toGoogle);
            fireLinkToGoogle();
        });

        if (result.get(GOOGLE) != null && result.get(GOOGLE)) {
            uploadType.addListItem(new SelectItem(1, wfmMessages.nameStorage(wfmStrings.google())));
            uploadType.setVisible(true);
            uploadTypeLabel.setVisible(true);
            tabs.add(toGoogle);
        } else {
            toGoogle.addStyleName("kpi-upload__tabs-item--locked");
        }
        if (fileUploadType.equals(FileUploadType.GOOGLE_DOCUMENTS)) {
            uploadType.setSelected(1);
            options = new Options(folder, GOOGLE, "");
        }

        //Office Storage
        toOffice = new Div("kpi-upload__tabs-item kpi__tabs-item--locked");
        Div officeIcon = new Div("kpi-upload__tabs-item-drive-365");
        toOffice.add(officeIcon);
        toOffice.addClickHandler(clickEvent -> {
            highlightThis(toOffice);
            options = new Options(folder, LINK_TO_OFFICE_365_DOCS_PARAM_NAME, "");
            showSelectOfficeDocsPopup(OFFICE_365, false);
        });
        if (result.get(OFFICE_365) != null && result.get(OFFICE_365)) {
            //verticalPanelDiv.add(toOffice); //TODO restore
            uploadType.addListItem(new SelectItem(2, wfmMessages.nameStorage(wfmStrings.office365())));
            uploadType.setVisible(true);
            uploadTypeLabel.setVisible(true);
            tabs.add(toOffice);
        }
        if (fileUploadType.equals(FileUploadType.OFFICE_DOCUMENTS)) {
            uploadType.setSelected(2);
            options = new Options(folder, OFFICE_365, "");
        }

        //MinIO Storage
        if (result.get(MINIO) != null && result.get(MINIO)) {
            uploadType.addListItem(new SelectItem(3, wfmMessages.nameStorage(wfmStrings.local())));
            uploadType.setVisible(true);
            uploadTypeLabel.setVisible(true);
//            uploadType.setSelected(3);
        }
        if (fileUploadType.equals(FileUploadType.MINIO)) {
            uploadType.setSelected(3);
            options = new Options(folder, MINIO, "");
        }

        //Local Storage
        if (result.get(LOCAL) != null && result.get(LOCAL)) {
            uploadType.addListItem(new SelectItem(4, wfmMessages.nameStorage(wfmStrings.localFile())));
            uploadType.setVisible(true);
            uploadTypeLabel.setVisible(true);
//            uploadType.setSelected(3);
        }
        if (fileUploadType.equals(FileUploadType.LOCAL)) {
            uploadType.setSelected(4);
            options = new Options(folder, LOCAL, "");
        }

        mainContent.add(fileUploaderList);

        //Storage Usage
        Div divStorageUsage = new Div("storage-informer");
        Strong strong = new Strong("");//2.5 GB of 5GB used
        strong.setStyleName("fs-2");
        divStorageUsage.add(strong);

        DT dt = new DT();
        Div storageProgress = new Div("progress");
        storageProgress.getElement().getStyle().setMargin(0, Style.Unit.PX);

        Div determinate = new Div("determinate");
        determinate.getElement().getStyle().setWidth(30, Style.Unit.PCT);

        storageProgress.add(determinate);
        dt.add(storageProgress);

        DL dl = new DL();
        dl.add(dt);

        MaterialLink upgradeStorageLink = new MaterialLink("");


        DD dd = new DD();

        upgradeStorageLink.addClickHandler(clickEvent -> {
            if (Utils.hasRole(ADMIN) || Utils.hasRole(DR)) {
                Utils.openURLCurrentTab("/Myaccount.html#usageplan|allPricingView/default/2/2");
            }
        });
        dd.add(upgradeStorageLink);
        dl.add(dd);


        divStorageUsage.add(dl);

        fileUploaderList.add(divStorageUsage);

        Double companyMaxStorage = Utils.getMaxStorage();
        Double companyUsedStorage = 0d;
        int progress = 0;
        if (companyMaxStorage != null && companyUsedStorage != null) {
            progress = (int) ((100 * companyUsedStorage) / (companyMaxStorage));
            determinate.getElement().getStyle().setWidth(progress, Style.Unit.PCT);
        }

        String formatted = NumberFormat.getFormat("0.0").format(companyUsedStorage);
        strong.setText(wfmMessages.usedMemory(formatted, companyMaxStorage));
        if (Utils.hasRole(ADMIN) || Utils.hasRole(DR)) {
            upgradeStorageLink.setText(wfmStrings.upgradeStorage());
        }
        if (companyUsedStorage > companyMaxStorage) {
            upgradeStorageLink.add(new KpiCustomToolTip(wfmStrings.sorryYouHaveReachedTheStorageLimitForUploadDocuments(), true, false));
        }
        //End Of Storage Usage

        Div div = new Div("kpi-upload__content-wrapper kpi-upload__description");
        div.add(tbDescr);
        fileUploaderList.add(div);
        driveFolderList = new DriveFolderList(target);
        tbDescr.setPlaceHolder(wfmStrings.addNote());
        tbDescr.getElement().getStyle().clearDisplay();
        tbDescr.getElement().setId("write_a_note");

        // Set the dialog's caption.
        if (fileUploadType.equals(FileUploadType.AMAZON)) {
            setTitle(wfmStrings.fileUpload());
        } else if (fileUploadType.equals(FileUploadType.GOOGLE_DOCUMENTS)) {
            setTitle(wfmStrings.fileUploadToMyGoogleDocs());
        } else if (fileUploadType.equals(FileUploadType.OFFICE_DOCUMENTS) || fileUploadType.equals(FileUploadType.OFFICE_SHARE_POINT_DOCUMENTS)) {
            setTitle(wfmStrings.fileUploadToMyOfficeDocs());
        }
        if (fileUploadType.equals(FileUploadType.LINK_TO_GOOGLE_DOCUMENTS)) {

            setFileUploaderListDisabled();
            showSelectGoogleDocsPopup();
        }

        if (fileUploadType.equals(FileUploadType.LINK_TO_KPI_DOCUMENTS)) {
            uploadState();
            setFileUploaderListDisabled();
            toGoogle.addStyleName("kpi-upload__tabs-item--locked");
            showKpiSelectDocsPopup();
        }

        if (Utils.isDocuments()) {
            if (listItem != null) {
                setFileUploaderListDisabled();
                taskList.setItems(new SelectItem[]{listItem});
                taskList.setSelected(listItem);
            } else if (this.folder.getFileType() == F_COMPANY_PUBLIC_ROOT) {
                toAmazon.removeFromParent();
                uploadType.removeListItem(1);
                toGoogle.addStyleName("kpi-upload__tabs-item--locked");
            } else if (this.folder.getFileType() == F_TASK) {
                setFileUploaderListDisabled();
                DocumentsView.get().getDocumentsService().getTasks(this.folder.getEntityId(), new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void success(SelectItem[] result) {
                        taskList.setItems(result);
                    }
                });
                form.addField(wfmStrings.task(), taskList);
            } else if (this.folder.getFileType() == F_PR_ISSUE) {
                setFileUploaderListDisabled();
                DocumentsView.get().getDocumentsService().getIssues(this.folder.getEntityId(), new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void success(SelectItem[] result) {
                        taskList.setItems(result);
                    }
                });
                form.addField(wfmStrings.issue(), taskList);
            } else if (this.folder.getFileType() == F_PUR_INV) {
                crmLookUp = new CRMLookUp(LookUpConstants.TYPE_PURCHASE_INVOICE);
                form.addField(wfmStrings.purchaseinvoice(), crmLookUp);
            } else if (this.folder.getFileType() == F_EXP_DOC) {
                setFileUploaderListDisabled();
                CommonService.App.get().getExpenseReports(new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void success(SelectItem[] result) {
                        taskList.setItems(result);
                    }
                });
                form.addField(wfmStrings.expenseClaims(), taskList);
            } else if (this.folder.getFileType() == F_VACANCY) {
                setFileUploaderListDisabled();
                DocumentsView.get().getDocumentsService().getVacancyOrCandidateOrPlacement(VACANCY, new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void success(SelectItem[] result) {
                        taskList.setItems(result);
                    }
                });
                form.addField(wfmStrings.vacancy() + " " + wfmStrings.attachments(), taskList);
            } else if (this.folder.getFileType() == F_CANDIDATE) {
                setFileUploaderListDisabled();
                DocumentsView.get().getDocumentsService().getVacancyOrCandidateOrPlacement(CANDIDATE, new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void success(SelectItem[] result) {
                        taskList.setItems(result);
                    }
                });
                form.addField(wfmStrings.candidate() + " " + wfmStrings.attachments(), taskList);
            } else if (this.folder.getFileType() == F_PLACEMENT) {
                setFileUploaderListDisabled();
                DocumentsView.get().getDocumentsService().getVacancyOrCandidateOrPlacement(PLACEMENT, new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void success(SelectItem[] result) {
                        taskList.setItems(result);
                    }
                });
                form.addField(wfmStrings.placement() + " " + wfmStrings.attachments(), taskList);
            } else if (this.folder.getFileType() == F_EMAIL_TEMPLATE) {
                setFileUploaderListDisabled();
                DocumentsView.get().getDocumentsService().getVacancyOrCandidateOrPlacement(ST_EMAIL_TEMPLATE, new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void success(SelectItem[] result) {
                        taskList.setItems(result);
                    }
                });
                form.addField(wfmStrings.emailTemplate() + " " + wfmStrings.attachments(), taskList);
            } else if (this.folder.getFileType() == F_PAST_EMPLOYMENT) {
                final DataListBox employeesListBox = new DataListBox();
                employeesListBox.setEnabled(false);
                if (Utils.hasRole(ADMIN) || Utils.hasRole(DR) || Utils.hasRole(TL) || Utils.hasRole(PM) || Utils.hasRole(HR)) {
                    employeesListBox.setEnabled(true);
                }
                setFileUploaderListDisabled();

                InputGroup inputGroup = new InputGroup(employeesListBox, taskList);
                form.addField(wfmStrings.pastEmployments(), inputGroup);
                inputGroup.getElement().getStyle().clearDisplay();
            } else if (this.folder.getFileType() == F_INTERNAL_EMPLOYMENT) {
                final DataListBox employeesListBox = new DataListBox();
                employeesListBox.setEnabled(false);
                if (Utils.hasRole(ADMIN) || Utils.hasRole(DR) || Utils.hasRole(TL) || Utils.hasRole(PM) || Utils.hasRole(HR)) {
                    employeesListBox.setEnabled(true);
                }
                setFileUploaderListDisabled();
                InputGroup inputGroup = new InputGroup(employeesListBox, taskList);
                form.addField(wfmStrings.internalEmployments(), inputGroup);
                inputGroup.getElement().getStyle().clearDisplay();
            }

            taskList.setAllowFirstItem(false);
            taskList.addValueChangeHandler(changeEvent -> {
                if (!taskList.isSomethingSelected()) {
                    setFileUploaderListDisabled();
                }
            });
        }


        cancel.addClickHandler(closeClickHandler());
        cancel.setText(wfmStrings.close());
        add(form);
        cancel.getElement().setId("buttonClose");

        // Create a panel to hold all of the form widgets.
        filenameLabel.setText("");
        filenameLabel.setVisible(false);
        filenameLabel.setStyleName("props-labels");

        Grid generalTable = new Grid(2, 2);
        generalTable.setText(0, 0, wfmStrings.folder());
        generalTable.setText(1, 0, wfmStrings.file());
        generalTable.setText(0, 1, this.folder.getName());
        generalTable.setWidget(1, 1, filenameLabel);
        generalTable.addStyleName("file--FileUploadDialog");
        generalTable.getCellFormatter().setStyleName(0, 0, "props-labels");
        generalTable.getCellFormatter().setStyleName(1, 0, "props-labels");
        generalTable.setCellSpacing(4);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_FILE_UPLOAD_STARTED, FileUploadDialog.this, (sender, args) -> {
            LoadingPanel.loading(true);
            enable(false);
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_FILE_UPLOAD_FINISHED, FileUploadDialog.this, (sender, args) -> {
            enable(true);
            if (fileUploaderList.getFileInfos() != null && !fileUploaderList.getFileInfos().isEmpty()) {
                for (String key : fileUploaderList.getFileInfos().keySet()) {
                    fileInfos.put(key, fileUploaderList.getFileInfos().get(key));
                }
            }
            LoadingPanel.loading(false);
        });

        if (isFromDocumentSection) {
            center();
        }
    }

    public FileUploaderList createFileUploaderList(Options options, Div filesPanel) {
        if (inDatabaseFileWidgetProvider == null) {
            return new FileUploaderList(options, filesPanel);
        } else {
            return new FooterFileUploaderList(options, filesPanel, inDatabaseFileWidgetProvider);
        }
    }

    private void setFileUploaderListDisabled() {
        fileUploaderList.getElement().setPropertyBoolean("disabled", false);
    }

    private void onUploadTypeChange() {
        if (uploadType.getSelectedItem().getId().equals(0)) {
            onToKpiStorage();
        } else if (uploadType.getSelectedItem().getId().equals(1)) {
            onToGoogleSelected();
        } else if (uploadType.getSelectedItem().getId().equals(2)) {
            onToOfficeDocumentsSelected();
        } else if (uploadType.getSelectedItem().getId().equals(3)) {
            onToMinIOStorage();
        } else if (uploadType.getSelectedItem().getId().equals(4)) {
            onToLocalStorage();
        }
    }

    private void onToKpiStorage() {
        uploadType.setSelected(0);
        options = new Options(folder, AMAZON, "");
        FileUploadDialog.this.fileUploadType = FileUploadType.AMAZON;
        fileUploaderList.setStorage(AMAZON_PARAM_NAME);
    }

    private void onToGoogleSelected() {
        options = new Options(folder, GOOGLE, tbDescr.getText());
        fileUploaderList.setStorage(GOOGLE_DOCS_PARAM_NAME);

        LoginService.App.get().isValid_User_For_Google_Gocs(new AbstractAsyncCallback<Boolean>() {
            public void failure(Throwable throwable) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Boolean googleStoreIsOK) {
                if (googleStoreIsOK) {

                    uploadType.setSelected(1);
                    FileUploadDialog.this.fileUploadType = FileUploadType.GOOGLE_DOCUMENTS;

                    if (Utils.hasGenericAccess(GenericSettingsEnum.CREATE_OBJECT_FOLDER_TO_GOOGLE_DRIVE)) {
                        driveFolderList.loadData(GOOGLE);
                    }
                } else {

                    WfmMessageBox confirm = new WfmMessageBox(IconEnum.QUESTION, Action.OkCancel, wfmStrings.youNeedToAutorizeToYouGoogleDocument());
                    confirm.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            new GoogleAuthorizationPanel(GOOGLE_DOCUMENTS, true);
                        }

                        @Override
                        public void onCancel() {
                            onToKpiStorage();
                        }
                    });
                    confirm.open();
                }
            }
        });
    }

    private void onToOfficeDocumentsSelected() {

        options = new Options(folder, OFFICE_365, tbDescr.getText());
        fileUploaderList.setStorage(OFFICE_365_DOCS_PARAM_NAME);
        LoginService.App.get().isValidUserOfficeAndGoogle(Constants.OFFICE_365, new AbstractAsyncCallback<ArrayList<Boolean>>() {
            public void failure(Throwable throwable) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(ArrayList<Boolean> result12) {
                if (result12.get(0)) {
                    if (result12.get(1)) {
                        uploadType.setSelected(2);
                        FileUploadDialog.this.fileUploadType = FileUploadType.OFFICE_DOCUMENTS;

                        if (Utils.hasGenericAccess(GenericSettingsEnum.CREATE_OBJECT_FOLDER_TO_OFFICE_365_DRIVE)) {
                            driveFolderList.loadData(OFFICE_365);
                        }
                    } else {
                        openOffice365AuthPanel(wfmStrings.youTokenExpiredPleaseTryAgain(), false);
                    }
                } else {
                    openOffice365AuthPanel(wfmStrings.youNeedToAutorizeToYouOfficeDocument(), false);
                }
            }
        });
    }

    private void onToMinIOStorage() {
        uploadType.setSelected(3);
        options = new Options(folder, MINIO, "");
        FileUploadDialog.this.fileUploadType = FileUploadType.MINIO;
        fileUploaderList.setStorage(MINIO_PARAM_NAME);
    }

    private void onToLocalStorage() {
        uploadType.setSelected(4);
        options = new Options(folder, LOCAL, "");
        FileUploadDialog.this.fileUploadType = FileUploadType.LOCAL;
        fileUploaderList.setStorage(LOCAL_PARAM_NAME);
    }

    private void fireLinkToGoogle() {
        options = new Options(folder, LINK_TO_GOOGLE_DOCS_PARAM_NAME, "");
        showSelectGoogleDocsPopup();
    }

    private void resetUploadWrapper() {
        uploadWrapper.clear();
        uploadWrapper.add(uploadTypeGroup);
        uploadWrapper.add(buttonGroup);
    }

    private void clearMainContent() {
        popupFolders.clear();
        mainContent.clear();
        mainContent.add(tabs);
    }

    private ClickHandler closeClickHandler() {
        return event -> {
            fileUploaderList.onCancel(null, null);
            fileUploaderList.getElement().setId("fileUploaderList");
            close();
        };
    }

    private void openOffice365AuthPanel(String message, final boolean isSharepoint) {
        WfmMessageBox confirm = new WfmMessageBox(IconEnum.QUESTION, Action.OkCancel, message);
        confirm.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                if (isSharepoint) {
                    new InsertSiteUlrPopUp(OFFICE_365_DOCUMENTS).open();
                } else {
                    new Office365AuthorizationPanel(OFFICE_365_DOCUMENTS, true);
                }
            }
        });
        confirm.open();
    }

    private void showKpiSelectDocsPopup() {
        FileUploadDialog.this.fileUploadType = FileUploadType.LINK_TO_KPI_DOCUMENTS;
        uploadWrapper.clear();
        DocumentsDocsSelectPopup docsPopup = new DocumentsDocsSelectPopup(getInstance());
        clearMainContent();
        docsPopup.addFoldersWrapperTo(popupFolders);
        mainContent.add(docsPopup);
        setFilesPanelVisible(false);
    }

    private void showSelectGoogleDocsPopup() {
        LoginService.App.get().isValid_User_For_Google_Gocs(new AbstractAsyncCallback<Boolean>() {
            public void failure(Throwable throwable) {
                Info.show(wfmStrings.error(), Info.Type.WARNING);
            }

            public void success(Boolean result) {
                if (result) {
                    FileUploadDialog.this.fileUploadType = FileUploadType.LINK_TO_GOOGLE_DOCUMENTS;
                    googleDocsPopup = new StorageDocsSelectPopup(getInstance(), GOOGLE);
                    clearMainContent();
                    googleDocsPopup.addFoldersWrapperTo(popupFolders);
                    mainContent.add(googleDocsPopup);
                    setFilesPanelVisible(false);
                } else {
                    WfmMessageBox confirm = new WfmMessageBox(IconEnum.QUESTION, Action.OkCancel, wfmStrings.youNeedToAutorizeToYouGoogleDocument());
                    confirm.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            new GoogleAuthorizationPanel(GOOGLE_DOCUMENTS, true);
                        }
                    });
                    confirm.open();
                }
            }
        });
    }

    private void showSelectOfficeDocsPopup(final String officeDriveType, final boolean isSharepoint) {
        LoadingPanel.loading(true, FileUploadDialog.this);
        LoginService.App.get().isValidUserOfficeAndGoogle(officeDriveType, new AbstractAsyncCallback<ArrayList<Boolean>>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, FileUploadDialog.this);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(ArrayList<Boolean> result) {
                LoadingPanel.loading(false, FileUploadDialog.this);
                if (result.get(0)) {
                    if (result.get(1)) {
                        FileUploadDialog.this.fileUploadType = OFFICE_365.equals(officeDriveType) ? FileUploadType.LINK_TO_OFFICE_DOCUMENTS : FileUploadType.LINK_TO_OFFICE_SHARE_POINT_DOCUMENTS;
                        officeDocsPopup = new StorageDocsSelectPopup(getInstance(), officeDriveType);
                        clearMainContent();
                        officeDocsPopup.addFoldersWrapperTo(popupFolders);
                        mainContent.add(officeDocsPopup);
                    } else {
                        openOffice365AuthPanel(wfmStrings.youTokenExpiredPleaseTryAgain(), isSharepoint);
                    }
                } else {
                    openOffice365AuthPanel(wfmStrings.youNeedToAutorizeToYouOfficeDocument(), isSharepoint);
                }
            }
        });
    }

    private Integer getEntityId() {
        Integer entityId = folder.getEntityId();
        if (isFromDocumentSection) {
            if (folder.getFileType() == F_PUR_INV) {
                entityId = crmLookUp != null ? crmLookUp.getSelectedItemID() : null;
            } else if (folder.getFileType() == F_TASK || folder.getFileType() == F_PR_ISSUE ||
                    folder.getFileType() == F_EXP_DOC || folder.getFileType() == F_EMPLOYEE_PROFILE ||
                    folder.getFileType() == F_VACANCY || folder.getFileType() == F_CANDIDATE || folder.getFileType() == F_PLACEMENT ||
                    folder.getFileType() == F_LEAVE_REQUEST || folder.getFileType() == F_INCIDENT || folder.getFileType() == F_EMAIL_TEMPLATE ||
                    folder.getFileType() == F_PAST_EMPLOYMENT || folder.getFileType() == F_INTERNAL_EMPLOYMENT) {
                entityId = taskList.getSelectedId();
            }
            if (entityId == null) {
                entityId = folder.getEntityId();
            }
        }
        return entityId;
    }

    private void save() {
        if (validate()) {
            LoadingPanel.loading(true);
            enable(false);

            ArrayList<FileResource> driverFilesList = new ArrayList<>(googleOrOfficeFiles.values());
            ArrayList<FileResource> kpiFilesList = new ArrayList<>(kpiFiles.values());
            ArrayList<FileResource> files = new ArrayList<>();
            if (fileUploaderList.getFileInfos() != null && fileUploaderList.getFileInfos().size() > 0) {
                for (FileInfo info : fileUploaderList.getFileInfos().values()) {
                    FileResource resource = new FileResource();
                    resource.setName(info.getId() + "_upld_" + info.getName().replace("\\+", ""));
                    resource.setPath(info.getUrl());
                    resource.setUploadType(options.getStorage());
                    resource.setDownloadable(isDownloadable);
                    files.add(resource);
                }
            }
            folder.setEntityId(getEntityId());

            if (driveFolderList != null) {
                if (driveFolderList.getSubFolder() != null && !driveFolderList.getSubFolder().isEmpty()) {
                    folder.setDriveFolderName(driveFolderList.getSubFolder());
                    folder.setDriveFolderId(driveFolderList.getSelectedFolderId());
                } else if (driveFolderList.getSelectedFolderId() != null) {
                    folder.setDriveFolderId(driveFolderList.getSelectedFolderId());
                    folder.setDriveFolderName(null);
                }
            }
            DocumentsService.App.get().uploadAllFiles(files, kpiFilesList, folder, tbDescr.getText(), new AbstractAsyncCallback<ArrayList<FileResource>>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    enable(true);
                }

                @Override
                public void success(ArrayList<FileResource> result) {
                    if (fromMessenger){
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WHATSAPP_ATTACHMENT_ATTACHED,result,FileUploadDialog.this);
                    }
//                    result.remove(0);
                    if (!result.isEmpty()) {
                        if (result.get(0).getName() != null && !"".equals(result.get(0).getName()) && !"noname".equals(result.get(0).getName())) {
                            Info.show(result.get(0).getName());
                        }
                        for (FileResource resource : result) {
                            uploadedFiles.put(resource.getObjectId(), resource);
                        }
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DOCUMENTS_UPLOAD_FILES, null, FileUploadDialog.this);
                        runCommand();

                        if (folder != null && folder.getFileType() == Constants.F_OPPORTUNITY) {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_OPPORTUNITY_LIST_RELOAD, false, FileUploadDialog.this);
                        }
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.file()), Info.Type.INFO);
                    }
                    if (closeAfterAdd) {
                        close();
                    }
                    enable(true);
                    LoadingPanel.loading(false);
                }
            });
        }
    }

    public void enable(boolean b) {
        saveClose.setEnabled(b);
        tbDescr.setEnabled(b);
    }

    public boolean validate() {
        /*int error = 0;
        if (error > 0) {
            enable(true);
            Info.warn(wfmStrings.noFileWasSelected());
            return false;
        }*/
        return true;
    }

    /**
     * Returns the file name from a potential full path argument. Apparently IE
     * insists on sending the full path name of a file when uploading, forcing
     * us to trim the extra path info. Since this is only observed on Windows we
     * get to check for a single path separator value.
     *
     * @param name the potentially full path name of a file
     * @return the file name without extra path information
     */
    protected String getFilename(String name) {
        int pathSepIndex = name.lastIndexOf("\\");
        if (pathSepIndex == -1) {
            pathSepIndex = name.lastIndexOf("/");
            if (pathSepIndex == -1) {
                return name;
            }
        }
        return name.substring(pathSepIndex + 1);
    }

    public void addUploadListener(Command command, String... events) {
        if (events != null && events.length > 0 && command != null) {
            for (String event : events) {
                listeners.put(event, command);
            }
        }
    }

    public HashMap<Integer, FileResource> getUploadedFiles() {
        return uploadedFiles;
    }

    public HashMap<Integer, FileResource> getGoogleOrOfficeFiles() {
        return googleOrOfficeFiles;
    }

    public void setStorageType(String storageType) {
        if (this.options != null) {
            this.options.setStorage(storageType);
        }
    }

    public void putIntoKpiFiles(Integer key, FileResource value) {
        if (!kpiFiles.containsKey(key)) {
            kpiFiles.put(key, value);
        }
    }

    public void removeFromKpiFiles(Integer key) {
        kpiFiles.remove(key);
    }

    public ArrayList<FileResource> getFileResources() {
        //            descrField
        ArrayList<FileResource> fileResources = new ArrayList<>(uploadedFiles.values());
        return fileResources;
    }

    public void setFileResources(List<FileResource> resources) {
        for (FileResource resource : resources) {
            uploadedFiles.put(resource.getObjectId(), resource);
        }
    }


    public boolean isEmpty() {
        return uploadedFiles == null || uploadedFiles.size() == 0;
    }

    public FileUploadDialog getInstance() {
        return this;
    }

    public void setVisibleToGoogleDocsPanel() {
//        uploadContent.getElement().getStyle().setDisplay(Style.Display.);
    }

    public void addToUploadContent(FileWidget widget) {
        fileUploaderList.addFile(widget);
    }

    public void removeFromUploadContent(FileWidget widget) {
        fileUploaderList.removeFile(widget);
    }

    interface FileUploadDialogUiBinder extends UiBinder<HTMLPanel, FileUploadDialog> {
    }

    public interface FileUploadEvents {
        String FILE_UPLOADED = "FILE_UPLOADED";
        String FILE_REMOVED = "FILE_REMOVED";
        String FILE_UPLOAD_ERRROR = "FILE_UPLOAD_ERROR";
    }

    private boolean checkFileTypeLikeImage(ArrayList<FileResource> files) {
        if (files.size() != 1) return false;

        String[] _fileName = files.get(0).getEncodedName().split("_upld_");
        String fileName = _fileName[_fileName.length - 1].replace("\\+", "").replace("\"", "");
        String saveTo = files.get(0).getUploadType();
        if (fileName.contains(".")) {
            String filename = fileName.substring(fileName.lastIndexOf('.')).toLowerCase(Locale.ENGLISH);
            if (filename.contains("jpg") || filename.contains("jpeg") || filename.contains("jpe")) {
                return true;
            } else return filename.contains("png");
        }
        return false;
    }

    class DriveFolderList extends Composite {
        private final TreeSelectIcons icons = GWT.create(TreeSelectIcons.class);

        private FlexTable container;
        private DataListBox dwFolder;

        private TextBox txtSubfolder;
        private Anchor addSubfolder;
        private Icon removeSubfolder;
        private HorizontalPanel pnlSubfolder;

        private final String root;

        public DriveFolderList(String target) {
            this.root = target;
            init();
        }

        private void init() {
            dwFolder = new DataListBox();
            dwFolder.addItem(new SelectItem(null, wfmStrings.loading()));
            dwFolder.setSelectedNullLabel();
            dwFolder.setWidth("120px");
            dwFolder.addValueChangeHandler(changeEvent -> {
                if (dwFolder.getSelectedItem() == null) {
                    pnlSubfolder.clear();
                    txtSubfolder.setValue(null);
                } else {
                    pnlSubfolder.clear();
                    pnlSubfolder.add(addSubfolder);
                }
            });

            txtSubfolder = new TextBox();
            txtSubfolder.setWidth("120px");


            addSubfolder = new Anchor(wfmStrings.addNewSubFolder());
            addSubfolder.addClickHandler(clickEvent -> {
                pnlSubfolder.clear();
                pnlSubfolder.add(txtSubfolder);
                pnlSubfolder.add(removeSubfolder);
            });

            removeSubfolder = new Icon();
            removeSubfolder.setStyleName(WfmButton2.ICON_TRASH);
            removeSubfolder.getElement().getStyle().setCursor(Style.Cursor.POINTER);
            removeSubfolder.addClickHandler(clickEvent -> {
                pnlSubfolder.clear();
                txtSubfolder.setValue(null);
                pnlSubfolder.add(addSubfolder);
            });

            pnlSubfolder = new HorizontalPanel();
            pnlSubfolder.setSpacing(5);

            container = new FlexTable();
            container.setWidget(0, 0, dwFolder);
            /*if (!toOfficeSharePoint.getValue()) {
                container.setWidget(0, 1, pnlSubfolder);
            }*/
            container.setCellSpacing(5);
            initWidget(container);
        }

        public void loadData(String driveType) {
            DocumentsService.App.get().getUsersAllSubFoldersInKpiRoot(target, driveType, new AsyncCallback<ArrayList<TreeSelectItem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(ArrayList<TreeSelectItem> treeSelectItems) {
                    dwFolder.clear();

                    if (treeSelectItems != null && !treeSelectItems.isEmpty()) {
                        GWT.log("Folders count:" + treeSelectItems.size());
                        dwFolder.setItems(treeSelectItems.toArray(new TreeSelectItem[]{}));
                    }
                }
            });
        }

        public String getSelectedFolderId() {
            if (dwFolder.getSelectedItem() != null) {
                return dwFolder.getSelectedItem().getDescription();
            }

            return null;
        }

        public String getSubFolder() {
            return txtSubfolder.getValue();
        }
    }
}
