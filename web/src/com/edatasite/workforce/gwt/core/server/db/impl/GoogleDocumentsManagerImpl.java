package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsSinxDocuments;
import com.edatasite.workforce.core.domain.EdsSinxDocumentsSettings;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.documents.EdsFileBody;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.GoogleDocumentsManager;
import com.edatasite.workforce.gwt.core.server.db.SinxDocumentsSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.gdata.data.MediaContent;
import com.google.gdata.util.ServiceException;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 */
@Repository("googleDocumentsManager")
public class GoogleDocumentsManagerImpl extends BaseManager<EdsSinxDocuments> implements GoogleDocumentsManager, Constants {

    private static final int FILE_MAX_SIZE = 10 * 1024 * 1024;//10 MB
    private static final String propertyKey = "java.io.tmpdir";
    private static final String USER_OWNED_DOCUMENTS_URL = "";

    @Autowired
    private SinxDocumentsSettingsManager sinxDocumentsSettingsManager;

    @Autowired
    private GenericSettingsManager genericSettingsManager;

    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;

    public GoogleDocumentsManagerImpl() {
        super(EdsSinxDocuments.class);
    }

    public EdsSinxDocuments getGoogleDocuments(EdsUser user, boolean withCheck) {
        return (EdsSinxDocuments) findSingle("from EdsSinxDocuments gd where gd.user=?" + (withCheck ? " and (gd.active is null or gd.active=true)" : ""), user);
    }

    public Boolean validateUser() {
        EdsUser user = getUser();
        EdsSinxDocuments googleDocuments = getGoogleDocuments(user, true);
        if (googleDocuments != null) {
            return googleDocuments.getToken() != null;
        } else {
            return false;
        }
    }
    //todo file upload to google drive never call to this method, because EdsSinxDocuments will not be null
    /*public String getGoogleId(EdsSinxDocuments googleDocuments, String sessionToken) throws GeneralSecurityException, IOException, ServiceException {
        Drive service = getService();
        if (service == null) {
            return null;
        }
        return service.about().get().execute().getUser().getDisplayName();
    }*/

    private Drive getService() {
        return getService(null);
    }

    public Drive getService(EdsUser user) {
        EdsSinxDocuments googleDocuments = getGoogleDocuments(user != null ? user : getUser(), true);

        if (googleDocuments == null || EdsContextParams.getOauth2ConsumerKey() == null || EdsContextParams.getOauth2ConsumerSecret() == null) {
            return null;
        }
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport).setJsonFactory(jsonFactory)
                .setClientSecrets(EdsContextParams.getOauth2ConsumerKey(), EdsContextParams.getOauth2ConsumerSecret()).build().setRefreshToken(googleDocuments.getToken());
        return new Drive.Builder(httpTransport, jsonFactory, credential).setApplicationName("KPI").build();
    }

    public void uploadFile(EdsUpload upload) throws GeneralSecurityException, IOException, ServiceException {
        if (upload.getInputStream().available() == 0) {
            copyAttachment(upload);
            return;
        }

        Drive service = getService();
        //upload the file into the specific folder
        String folderId = null;

        if (upload.getDriveFolderName() != null && !upload.getDriveFolderName().isEmpty()) {
            List<com.google.api.services.drive.model.File> dfs = service.files().list().setPageSize(1).setQ("mimeType = 'application/vnd.google-apps.folder' and name contains '" + upload.getDriveFolderName() + "' and '" + upload.getDriveFolderId() + "' in parents and trashed = false ").execute().getFiles();
            if (dfs.isEmpty()) {
                com.google.api.services.drive.model.File file = new com.google.api.services.drive.model.File();
                file.setName(upload.getDriveFolderName());
                file.setMimeType("application/vnd.google-apps.folder");
//                file.setParents(Collections.singletonList(new ParentReference().setId(upload.getDriveFolderId())));
                file.setParents(Collections.singletonList(upload.getDriveFolderId()));
                file = service.files().create(file).execute();
                folderId = file.getId();
            } else {
                folderId = dfs.get(0).getId();
            }

        } else if (upload.getDriveFolderId() != null && !upload.getDriveFolderId().isEmpty()) {
            folderId = upload.getDriveFolderId();
        } else {
            List<com.google.api.services.drive.model.File> dfs = service.files().list().setPageSize(1).setQ("mimeType = 'application/vnd.google-apps.folder' and name contains '" + EdsContextParams.getGoogleDriveRootFolder() + "' and trashed = false").execute().getFiles();

            if (!dfs.isEmpty()) {
                folderId = dfs.get(0).getId();
            }
        }

        com.google.api.services.drive.model.File file = new com.google.api.services.drive.model.File();
        file.setName(upload.getOriginalName());
        file.setDescription(upload.getName());
        file.setMimeType(upload.getContentType());

        //if the folder's gevin, then upload the file into folder
        if (folderId != null && !folderId.isEmpty()) {
//            file.setParents(Collections.singletonList(new ParentReference().setId(folderId)));
            file.setParents(List.of(folderId));
        }
        file = service.files().create(file, new InputStreamContent(upload.getContentType(), upload.getInputStream())).setFields("*").execute();
        System.out.println("File ID: " + file.getId());

        EdsSinxDocuments googleDocuments = getGoogleDocuments(getUser(), true);
        EdsSinxDocumentsSettings googleDocsSettings = new EdsSinxDocumentsSettings();
        googleDocsSettings.setDocumentLink(file.getWebViewLink());
        googleDocsSettings.setSinxDocuments(googleDocuments);
        googleDocsSettings.setUpload(upload);
        googleDocsSettings.setDocumentID(file.getId());
        googleDocsSettings.setDownloadLink(file.getWebContentLink());
        sinxDocumentsSettingsManager.create(googleDocsSettings);

    }

    @Override
    public Boolean checkExistingFoldersIntoGoogleDrive(Drive service, List<TreeSelectItem> folders, String parentId) throws IOException {
        com.google.api.services.drive.model.File dFolder = null;

        if (parentId == null || parentId.isEmpty()) {
            List<com.google.api.services.drive.model.File> dfs = service.files().list().setPageSize(1).setQ("mimeType = 'application/vnd.google-apps.folder' and name = '" + EdsContextParams.getGoogleDriveRootFolder() + "' and trashed = false").execute().getFiles();

            if (!dfs.isEmpty()) {
                parentId = dfs.get(0).getId();
            } else {
                com.google.api.services.drive.model.File kpiRootFolder = createKpiRootFolderIntoGoogleDrive(service);
                parentId = kpiRootFolder.getId();
            }
        }

        for (TreeSelectItem folder : folders) {
            List<com.google.api.services.drive.model.File> dfs = service.files().list().setPageSize(1).setQ("mimeType = 'application/vnd.google-apps.folder' and name = '" + folder.getName() + "' and '" + parentId + "' in parents and trashed = false").execute().getFiles();

            if (dfs.isEmpty()) {
                return false;
            } else {
                dFolder = dfs.get(0);
            }

            if (folder.hasChildren() && folder.getChildren().size() == 1) {
                createFoldersIntoGoogleDrive(service, folder.getChildren(), dFolder.getId());
            }

        }
        return true;
    }

    /**
     * @param folders
     * @param parentId
     * @throws IOException
     */
    public void createFoldersIntoGoogleDrive(Drive service, List<TreeSelectItem> folders, String parentId) throws IOException {
        com.google.api.services.drive.model.File dFolder = null;

        if (parentId == null || parentId.isEmpty()) {
            List<com.google.api.services.drive.model.File> dfs = service.files().list().setPageSize(1).setQ("mimeType = 'application/vnd.google-apps.folder' and name = '" + EdsContextParams.getGoogleDriveRootFolder() + "' and trashed = false").execute().getFiles();

            if (!dfs.isEmpty()) {
                parentId = dfs.get(0).getId();
            } else {
                com.google.api.services.drive.model.File kpiRootFolder = createKpiRootFolderIntoGoogleDrive(service);
                parentId = kpiRootFolder.getId();
            }
        }

        for (TreeSelectItem folder : folders) {
            List<com.google.api.services.drive.model.File> dfs = service.files().list().setPageSize(1).setQ("mimeType = 'application/vnd.google-apps.folder' and name = '" + folder.getName() + "' and '" + parentId + "' in parents and trashed = false").execute().getFiles();

            if (dfs.isEmpty()) {
                dFolder = new com.google.api.services.drive.model.File();
                dFolder.setName(folder.getName());
                dFolder.setMimeType("application/vnd.google-apps.folder");

                if (parentId != null && !parentId.isEmpty()) {
//                    dFolder.setParents(Collections.singletonList(new ParentReference().setId(parentId)));
                    dFolder.setParents(List.of(parentId));
                }
                dFolder = service.files().create(dFolder).execute();
            } else {
                dFolder = dfs.get(0);
            }

            if (folder.hasChildren()) {
                createFoldersIntoGoogleDrive(service, folder.getChildren(), dFolder.getId());
            }

        }
    }

    /**
     * @param root
     * @return
     * @throws IOException
     */
    public ArrayList<TreeSelectItem> getAllSubFoldersInKpiRoot(String root) throws IOException {
        ArrayList<TreeSelectItem> result = new ArrayList<>();

        Drive service = getService();
        if (service == null) {
            return null;
        }

        List<com.google.api.services.drive.model.File> folders = null;

        if (root != null && !root.isEmpty()) {
            folders = service.files().list().setPageSize(1).setQ("mimeType = 'application/vnd.google-apps.folder' and name = '" + root + "' and trashed = false ").execute().getFiles();
        }

        if (folders == null || folders.isEmpty()) {
            folders = service.files().list().setPageSize(1).setQ("mimeType = 'application/vnd.google-apps.folder' and name = '" + EdsContextParams.getGoogleDriveRootFolder() + "' trashed = false").execute().getFiles();
        }

        if (!folders.isEmpty()) {
            com.google.api.services.drive.model.File rf = folders.get(0);

            TreeSelectItem rootFolder = new TreeSelectItem();
            rootFolder.setName(rf.getName());
            rootFolder.setDescription(rf.getId());
            rootFolder.setShowInDropDown(true);
            rootFolder.setSelected(false);

            initializeSubFolders(rf.getId(), rootFolder, service);

            result.add(rootFolder);
        } else {
            com.google.api.services.drive.model.File dFolder = createKpiRootFolderIntoGoogleDrive(service);

            TreeSelectItem rootFolder = new TreeSelectItem();
            rootFolder.setName(EdsContextParams.getGoogleDriveRootFolder());
            rootFolder.setDescription(dFolder.getId());
            rootFolder.setShowInDropDown(true);
            rootFolder.setSelected(false);

            result.add(rootFolder);
        }

        return result;
    }

    private File createKpiRootFolderIntoGoogleDrive(Drive service) throws IOException {
        com.google.api.services.drive.model.File kpiRootFolder = new com.google.api.services.drive.model.File();
        kpiRootFolder.setName(EdsContextParams.getGoogleDriveRootFolder());
        kpiRootFolder.setMimeType("application/vnd.google-apps.folder");
        kpiRootFolder = service.files().create(kpiRootFolder).execute();

        return kpiRootFolder;
    }

    private void initializeSubFolders(String parentId, TreeSelectItem parent, Drive service) throws IOException {
        List<com.google.api.services.drive.model.File> subFolders = service.files().list().setPageSize(100).setQ("trashed = false and mimeType = 'application/vnd.google-apps.folder' and '" + parentId + "' in parents ").execute().getFiles();

        if (!subFolders.isEmpty()) {
            for (com.google.api.services.drive.model.File subFolder : subFolders) {
                TreeSelectItem item = new TreeSelectItem();
                item.setName(subFolder.getName());
                item.setDescription(subFolder.getId());
                item.setShowInDropDown(true);
                item.setParent(parent);
                parent.getChildren().add(item);
                initializeSubFolders(subFolder.getId(), item, service);
            }
        }

    }

    public FolderResource[] getAllGoogleFolders() throws GeneralSecurityException, IOException, ServiceException {
        ArrayList<FolderResource> result = new ArrayList<>();

        Drive service = getService();

        boolean showKpiRootFolder = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.SHOW_ONLY_KPI_ROOT_FOLDER_FROM_GOOGLE_DRIVE);
        if (showKpiRootFolder) {
            File kpiRootFolder = service.files().list().setPageSize(1).setQ("mimeType = 'application/vnd.google-apps.folder' and name = '" + EdsContextParams.getGoogleDriveRootFolder() + "' and trashed = false").execute().getFiles().get(0);
            FolderResource kpiRoot = new FolderResource();
            kpiRoot.setName(kpiRootFolder.getName());
            kpiRoot.setDriveFolderId(kpiRootFolder.getId());
            kpiRoot.setSubfolders(getGoogleFolders(service, kpiRootFolder.getId()));
            result.add(kpiRoot);
        } else {
            result.addAll(getAllGoogleFolders(service));

        }

        return result.toArray(new FolderResource[]{});
    }

    private ArrayList<FolderResource> getAllGoogleFolders(Drive service) throws GeneralSecurityException, IOException, ServiceException {
        ArrayList<FolderResource> result = new ArrayList<>();

        HashMap<String, FolderResource> foldersMap = new HashMap<>();
        HashMap<String, FolderResource> parentFoldersMap = new HashMap<>();
        HashMap<String, String> subFoldersMap = new HashMap<>();

        //Get Folders within the Drive
        Drive.Files.List list = service.files().list().setFields("*");
        list.setPageSize(1000);
        list.setQ("trashed=false and mimeType = 'application/vnd.google-apps.folder' and ('root' in parents or sharedWithMe) ");//('root' in parents or sharedWithMe)
        String pageToken = null;

        do{
            if (pageToken != null && !pageToken.isEmpty()) {
                list.setPageToken(pageToken);
            }
            FileList fileList = list.execute();
            List<File> folders = fileList.getFiles();

            if (folders != null && folders.size() > 0) {
                for (File fld : folders) {
                    String folderLink = fld.getId();
                    FolderResource folder = new FolderResource();
                    folder.setName(fld.getName());
                    folder.setCreatedBy(folderLink);
                    folder.setDriveFolderId(fld.getId());
                    if (!foldersMap.containsKey(folderLink)) {
                        foldersMap.put(folderLink, folder);
                    }
                }
            }

            if (fileList.getNextPageToken() == null) {
                break;
            }
            pageToken = fileList.getNextPageToken();

        } while (pageToken != null && !pageToken.isEmpty());

        parentFoldersMap = (HashMap<String, FolderResource>) foldersMap.clone();

        List<File> kpiRootFolder = service.files().list().setPageSize(1).setQ("mimeType = 'application/vnd.google-apps.folder' and name = '" + EdsContextParams.getGoogleDriveRootFolder() + "' and trashed = false").execute().getFiles();
        FolderResource kpiRoot = null;

        if (kpiRootFolder != null && !kpiRootFolder.isEmpty()) {
            kpiRoot = parentFoldersMap.get(kpiRootFolder.get(0).getId());
            parentFoldersMap.remove(kpiRootFolder.get(0).getId());

            //Google Drive folders sorting process
            sortingfolder(kpiRoot.getSubfolders());
        }

        ArrayList<FolderResource> fl = new ArrayList<>(parentFoldersMap.values());

        fl.sort(Comparator.comparing(o -> o.getName().toLowerCase()));

        for (FolderResource folder : fl) {
            if (!folder.getSubfolders().isEmpty()) {
                sortingfolder(folder.getSubfolders());
            }
        }
        //All files contains
        FolderResource allFilesFolder = new FolderResource();
        allFilesFolder.setName(commonLocalizer.localize("allFiles", "All Files"));
        allFilesFolder.setDriveFolderId("all_files");
        result.add(allFilesFolder);

        //Kpi root folder like "kpi Documents"
        if (kpiRoot != null) {
            result.add(kpiRoot);
        }
        result.addAll(fl);

        return result;
    }

    public ArrayList<FolderResource> getGoogleFolders(Drive service, String parentId) throws GeneralSecurityException, IOException, ServiceException {
        ArrayList<FolderResource> result = new ArrayList<>();

        //Get Folders within the Drive
        Drive.Files.List list = service.files().list();
        list.setPageSize(1000);
        list.setQ("trashed=false and mimeType = 'application/vnd.google-apps.folder' and '"+parentId+"' in parents");
        String pageToken = null;

        do{
            if (pageToken != null && !pageToken.isEmpty()) {
                list.setPageToken(pageToken);
            }
            FileList fileList = list.execute();
            List<File> folders = fileList.getFiles();

            if (folders != null && folders.size() > 0) {
                for (File folder : folders) {
                    FolderResource folderResource = new FolderResource();
                    folderResource.setName(folder.getName());
                    folderResource.setDriveFolderId(folder.getId());
                    result.add(folderResource);
                }
            }

            if (fileList.getNextPageToken() == null) {
                break;
            }
            pageToken = fileList.getNextPageToken();

        } while (pageToken != null && !pageToken.isEmpty());

        result.sort(Comparator.comparing(o -> o.getName().toLowerCase()));

        return result;
    }

    public ArrayList<FileResource> getGoogleFiles(Drive service, String folderId) throws GeneralSecurityException, IOException, ServiceException {
        ArrayList<FileResource> fileList = new ArrayList<>();

        Drive.Files.List list = service.files().list().setFields("*");
        list.setPageSize(1000);
        list.setQ("trashed=false and mimeType != 'application/vnd.google-apps.folder' " + (folderId != null && !folderId.isEmpty() ? " and '" + folderId + "' in parents" : ""));
        String pageToken = null;
        int step = 0;

        do {
            step++;

            if (pageToken != null && !pageToken.isEmpty()) {
                list.setPageToken(pageToken);
            }
            FileList fList = list.execute();
            List<File> files = fList.getFiles();

            if (files != null && files.size() > 0) {
                for (File document : files) {
                    FileResource fileItem = new FileResource();
                    fileItem.setName(document.getName());
                    fileItem.setGoogleDownloadLink(document.getWebViewLink());
                    fileItem.setDescription(document.getId());
                    fileItem.setUploadType(CommandConstants.LINK_TO_GOOGLE_DOCS_PARAM_NAME);
                    fileItem.setGoogleOrOffice365Id(document.getId());
                    fileList.add(fileItem);
                }
            }

            if (fList.getNextPageToken() == null) {
                break;
            }
            pageToken = fList.getNextPageToken();

        } while (step < 2);

        fileList.sort(Comparator.comparing(o -> o.getEncodedName().toLowerCase()));

        return fileList;
    }

    public FolderResource[] getAllGoogleDocuments() throws GeneralSecurityException, IOException, ServiceException {
        Drive service = getService();

        FolderResource allFilesFolder = new FolderResource();
        allFilesFolder.setName("All Files");

        HashMap<String, FolderResource> foldersMap = new HashMap<>();
        HashMap<String, FolderResource> parentFoldersMap = new HashMap<>();
        HashMap<String, String> subFoldersMap = new HashMap<>();

        //Get Folders within the Drive
        Drive.Files.List list = service.files().list();
        list.setPageSize(1000);
        list.setQ("trashed=false and mimeType = 'application/vnd.google-apps.folder'");
        String pageToken = null;
        int step = 0;

        do{
            step++;

            if (pageToken != null && !pageToken.isEmpty()) {
                list.setPageToken(pageToken);
            }
            FileList fileList = list.execute();
            List<com.google.api.services.drive.model.File> folders = fileList.getFiles();

            if (folders != null && folders.size() > 0) {
                for (com.google.api.services.drive.model.File fld : folders) {
                    String folderLink = fld.getId();
                    FolderResource folder = new FolderResource();
                    folder.setName(fld.getName());
                    folder.setCreatedBy(folderLink);
                    folder.setDriveFolderId(fld.getId());

                    if (fld.getParents() != null && !fld.getParents().isEmpty()) {
                        for (String folderId : fld.getParents()) {
                            subFoldersMap.put(folderLink, folderId);
                        }
                        /*for (ParentReference folderURL : fld.getParents()) {
                            subFoldersMap.put(folderLink, folderURL.getId());
                        }*/
                    }
                    if (!foldersMap.containsKey(folderLink)) {
                        foldersMap.put(folderLink, folder);
                    }
                }
            }

            if (fileList.getNextPageToken() == null) {
                break;
            }
            pageToken = fileList.getNextPageToken();

        } while (step < 2);

        //Get Files within the Drive
        list = service.files().list();
        list.setPageSize(1000);
        list.setQ("trashed=false and mimeType != 'application/vnd.google-apps.folder'");
        pageToken = null;
        step = 0;

        do {
            step++;

            if (pageToken != null && !pageToken.isEmpty()) {
                list.setPageToken(pageToken);
            }
            FileList fList = list.execute();
            List<com.google.api.services.drive.model.File> files = fList.getFiles();

            if (files != null && files.size() > 0) {
                ArrayList<FileResource> fileList = new ArrayList<>();
                for (com.google.api.services.drive.model.File document : files) {
                    FileResource fileItem = new FileResource();
                    fileItem.setName(document.getName());
                    fileItem.setGoogleDownloadLink(document.getWebViewLink());
                    fileItem.setDescription(document.getId());
                    fileItem.setUploadType(CommandConstants.LINK_TO_GOOGLE_DOCS_PARAM_NAME);
                    fileList.add(fileItem);

                    if (document.getParents() != null && !document.getParents().isEmpty()) {
                        for (String folderId : document.getParents()) {
                            foldersMap.get(folderId).getFiles().add(fileItem);
                        }
                        /*for (ParentReference parent : document.getParents()) {
                            if (foldersMap.containsKey(parent.getId())) {
                                foldersMap.get(parent.getId()).getFiles().add(fileItem);
                            }
                        }*/
                    }
                }
                FileResource[] resultArray = fileList.toArray(new FileResource[]{});
                Arrays.sort(resultArray, Comparator.comparing(o -> o.getEncodedName().toLowerCase()));
                ArrayList<FileResource> resources = new ArrayList<>();
                Collections.addAll(resources, resultArray);
                allFilesFolder.setFiles(resources);
            }

            if (fList.getNextPageToken() == null) {
                break;
            }
            pageToken = fList.getNextPageToken();

        } while (step < 2);

        parentFoldersMap = (HashMap<String, FolderResource>) foldersMap.clone();
        for (String folderURL : subFoldersMap.keySet()) {
            if (foldersMap.get(subFoldersMap.get(folderURL)) != null) {
                foldersMap.get(subFoldersMap.get(folderURL)).getSubfolders().add(foldersMap.get(folderURL));
                parentFoldersMap.remove(folderURL);
            }
        }
        ArrayList<FolderResource> result = new ArrayList<>(parentFoldersMap.values());
        FolderResource[] resultArray = result.toArray(new FolderResource[]{});
        Arrays.sort(resultArray, Comparator.comparing(o -> o.getName().toLowerCase()));
        ArrayList<FolderResource> res = new ArrayList<>();
        res.add(allFilesFolder);
        Collections.addAll(res, resultArray);

        for (FolderResource folder : resultArray) {
            if (!folder.getSubfolders().isEmpty()) {
                sortingfolder(folder.getSubfolders());
            }
        }
        return res.toArray(new FolderResource[]{});
    }

    private void sortingfolder(List<FolderResource> folders) {
        folders.sort(Comparator.comparing(o -> o.getName().toLowerCase()));

        for (FolderResource folder : folders) {
            if (!folder.getSubfolders().isEmpty()) {
                sortingfolder(folder.getSubfolders());
            }
        }
    }

    public void deleteDocument(EdsUpload upload) throws GeneralSecurityException, IOException, ServiceException {
        EdsSinxDocumentsSettings docSettings = sinxDocumentsSettingsManager.getSinxDocsSettings(upload);
        if (docSettings != null) {
            String documentID = getDocumentID(docSettings);
            if (documentID != null)
                getService().files().delete(documentID);
            sinxDocumentsSettingsManager.delete(docSettings);
        }
    }

    private void copyAttachment(EdsUpload upload) {
        EdsSinxDocumentsSettings googleDocsSettings = new EdsSinxDocumentsSettings();
        googleDocsSettings.setUpload(upload);
        sinxDocumentsSettingsManager.create(googleDocsSettings);
    }

    public InputStream getFileInputStream(EdsUpload upload) {
        EdsSinxDocumentsSettings documentLink = sinxDocumentsSettingsManager.getSinxDocsSettings(upload);
        InputStream inputStream = null;
        try {
            boolean userIsNull = false;
            if (getUser() == null) {
                //this logic need for get google document's inputStream in background services
                ServerSecurityContext.getInstance().setStaticUserID(documentLink.getSinxDocuments().getUser().getObjectID());
                userIsNull = true;
            }
            Drive service = getService();
			if (service != null) {
				inputStream = service.files().get(getDocumentID(documentLink)).executeMediaAsInputStream();
                        //downloadFile(service, service.files().get(getDocumentID(documentLink)).execute());
			}

            if (userIsNull) {
                ServerSecurityContext.getInstance().setStaticUserID(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    Pattern linkPat1 = Pattern.compile("/[\\w-]{25,}/");
    Pattern linkPat2 = Pattern.compile("id=[\\w-]{25,}&");
    Pattern linkPat3 = Pattern.compile("key=[\\w-]{25,}&");

    public String getDocumentID(EdsSinxDocumentsSettings documentLink) {
        String documentID = documentLink.getDocumentID();
        if (documentID == null) {//old uploaded documents
            Matcher m = linkPat1.matcher(documentLink.getDocumentLink());
            while (m.find()) {
                return m.group().replace("/", "");
            }
            m = linkPat2.matcher(documentLink.getDocumentLink());
            while (m.find()) {
                return m.group().replace("/", "");
            }
            m = linkPat3.matcher(documentLink.getDocumentLink());
            while (m.find()) {
                return m.group().replace("/", "");
            }
        }
        return documentID;
    }

    /**
     * Download a file's content.
     *
     * @return InputStream containing the file's content if successful,
     *         {@code null} otherwise.
     */
    /*private InputStream downloadFile(Drive service, File file) {

        if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
            try {
                HttpResponse resp =
                        service.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl()))
                                .execute();
                return resp.getContent();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            // The file doesn't have any content stored on Drive.
            return null;
        }
    }*/

    public void updateFile(EdsFileBody upload) throws GeneralSecurityException, IOException, ServiceException {
        EdsSinxDocumentsSettings docSettings = sinxDocumentsSettingsManager.getSinxDocsSettings(upload);
        if (docSettings != null) {
            Drive service = getService();
            File f = service.files().get(getDocumentID(docSettings)).execute();
            f.setName(upload.getOriginalName());
            service.files().update(getDocumentID(docSettings), f);
        }
    }

    public String[] getDocumentParameters(String googleDocResourceId) {
        String contentType = "application/octet-stream";
        String extension = "";
        String downloadUrl = null;
        MediaContent mc = null;
        int fileSize = 0;
        Drive service = getService();
        com.google.api.services.drive.model.File document = null;
        try {
            document = service.files().get(googleDocResourceId).setFields("*").execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        downloadUrl = document.getWebViewLink();
        fileSize = document.getQuotaBytesUsed().intValue();
        if (document != null) {
            String docType = document.getMimeType();
            extension = document.getFileExtension();
            String filename = document.getOriginalFilename();
            contentType = document.getMimeType();
            if ("spreadsheet".equals(docType)) {
                contentType = "application/vnd.ms-excel";
            } else if ("document".equals(docType) || "file".equals(docType)) {
                if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".jpe")) {
                    contentType = "image/jpeg";
                } else if (filename.endsWith(".png")) {
                    contentType = "image/png";
                } else if (filename.endsWith(".bmp")) {
                    contentType = "image/bmp";
                } else if (filename.endsWith(".gif")) {
                    contentType = "image/gif";
                } else if (filename.endsWith(".html") || filename.endsWith(".htm")) {
                    contentType = "text/html";
                } else if (filename.endsWith(".sxw")) {
                    contentType = "application/vnd.sun.xml.writer";
                } else if (filename.endsWith(".rtf")) {
                    contentType = "application/rtf";
                } else if (filename.endsWith(".pps") || filename.endsWith(".ppt")) {
                    contentType = "application/vnd.ms-powerpoint";
                } else if (filename.endsWith(".pptx")) {
                    contentType = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
                } else if (filename.endsWith(".csv")) {
                    contentType = "text/csv";
                } else if (filename.endsWith(".tsv") || filename.endsWith(".tab")) {
                    contentType = "text/tab-separated-values";
                } else if (filename.endsWith(".swf")) {
                    contentType = "application/x-shockwave-flash";
                } else if (filename.endsWith(".txt")) {
                    contentType = "text/plain";
                } else if (filename.endsWith(".doc")) {
                    contentType = "application/msword";
                } else if (filename.endsWith(".docx")) {
                    contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                } else if (filename.endsWith(".xls")) {
                    contentType = "application/vnd.ms-excel";
                } else if (filename.endsWith(".xlsx")) {
                    contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                } else if (filename.endsWith(".pdf")) {
                    contentType = "application/pdf";
                } else if (filename.endsWith(".pages")) {
                    contentType = "application/vnd.apple.pages";
                } else if (filename.endsWith(".odt")) {
                    contentType = "application/vnd.oasis.opendocument.text";
                } else if (filename.endsWith(".ods")) {
                    contentType = "application/vnd.oasis.opendocument.spreadsheet";
                } else if (filename.endsWith(".ai")) {
                    contentType = "application/postscript";
                } else if (filename.endsWith(".psd")) {
                    contentType = "application/octet-stream";
                } else if (filename.endsWith(".tiff")) {
                    contentType = "image/tiff";
                } else if (filename.endsWith(".dxf")) {
                    contentType = "application/dxf";
                } else if (filename.endsWith(".svg")) {
                    contentType = "image/svg+xml";
                } else if (filename.endsWith(".eps") || filename.endsWith(".ps")) {
                    contentType = "application/postscript";
                } else if (filename.endsWith(".otf")) {
                    contentType = "application/vnd.oasis.opendocument.formula-template";
                } else if (filename.endsWith(".ttf")) {
                    contentType = "application/x-font-ttf";
                } else if (filename.endsWith(".xps")) {
                    contentType = "application/vnd.ms-xpsdocument";
                } else if (filename.endsWith(".zip")) {
                    contentType = "application/zip";
                } else if (filename.endsWith(".rar")) {
                    contentType = "application/rar";
                }else if (filename.contains("json")){
                    contentType = "application/json";
                }
            } else if ("presentation".equals(docType)) {
                contentType = "application/vnd.ms-powerpoint";
            } else if ("drawing".equals(docType)) {
                contentType = "image/png";
            }
            String[] result = new String[4];
            result[0] = contentType;                // file content type
            result[1] = extension;                  // file extension: doc, xls, pdf,...
            result[2] = downloadUrl;                // file download URL address
            result[3] = String.valueOf(fileSize);   // file size
            return result;
        }
        return null;
    }
}
