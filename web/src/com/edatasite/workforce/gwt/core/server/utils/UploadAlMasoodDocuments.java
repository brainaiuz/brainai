package com.edatasite.workforce.gwt.core.server.utils;

import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.documents.FolderManager;
import com.edatasite.workforce.gwt.documents.client.exceptions.DuplicateNameException;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.exceptions.QuotaExceededException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.server.app.DocumentItem;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceImpl;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Normurod on 3/29/2016.
 */

@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class UploadAlMasoodDocuments implements HttpRequestHandler {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DocumentsServiceLocal documentsService;

    @Autowired
    private FolderManager folderManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;

    private String PATH_EID_1 = "/home/ebs/storage/500005/HRDocs/EID/";

    private String PATH_INS_1 = "/home/ebs/storage/500005/HRDocs/INS/";

    private String PATH_PPV_1 = "/home/ebs/storage/500005/HRDocs/PPV/";

    private String PATH_QUA_1 = "/home/ebs/storage/500005/HRDocs/QUA/";

    private String PATH_PHOTO_1 = "/home/ebs/storage/500005/HRDocs/PHOTO/";

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String fromBucket = request.getParameter("fromBucket");
        String toBucket = request.getParameter("toBucket");

        ServerSecurityContext.getInstance().setSessionId(getCookie(request, "SESSION_ID"));

        int start = 0, limit = 100;
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setStart(start);
        fp.setLimit(limit);

        ListResult<EmployeeListItem> listResult = null;

        EdsFolder folder = folderManager.getFolder(Constants.F_EMPLOYEE_PROFILE, null);
        Integer folderID = folder != null ? folder.getObjectID() : null;
        Set<String> codeList = new HashSet<>();

        do {
            listResult = employeeService.getEmployeeList(fp);

            if (listResult != null && !listResult.getList().isEmpty()) {
                for (EmployeeListItem employee : listResult.getList()) {
                    if (employee.getEmployeeNumber() != null && !employee.getEmployeeNumber().isEmpty()) {
                        codeList.add(employee.getEmployeeNumber());
                        uploadFile(employee, folderID, "EID", PATH_EID_1);

                        uploadFile(employee, folderID, "INS", PATH_INS_1);

                        uploadFile(employee, folderID, "PPV", PATH_PPV_1);

                        //uploadFile(employee, folderID, "QUA", PATH_QUA_1);

                        uploadPhoto(employee, folderID, "PHOTO", PATH_PHOTO_1);
                    }
                }

                System.out.println(codeList);
            }

            start += limit;
            fp.setStart(start);
            System.out.println("START: " + start);
        } while (listResult != null && !listResult.getList().isEmpty());

        ServerSecurityContext.getInstance().setSessionId(null);
    }

    private void uploadFile(EmployeeListItem employee, Integer folderID, String prefix, String path) {
        Path dir = Paths.get(path);
        List<File> fList = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, prefix+employee.getEmployeeNumber()+"*.{jpg,JPG,png,pdf}")) {
            for (Path entry: stream) {
                final DocumentItem fileBody = new DocumentItem();
                fileBody.setInputStream(new FileInputStream(entry.toFile()));
                fileBody.setContentType(DocumentsServiceImpl.identifyMimeType(entry.getFileName().toString()));
                fileBody.setName(entry.getFileName().toString());
                fileBody.setFolderId(folderID);
                fileBody.setDescription(prefix);
                fileBody.setEmployeeDoc(true);

                try {
                    documentsService.createFile(fileBody, Constants.LOCAL, Constants.F_EMPLOYEE_PROFILE, employee.getObjectID());
                } catch (DuplicateNameException | QuotaExceededException | InsufficientPermissionsException | ObjectNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException x) {
            throw new RuntimeException(String.format("error reading folder %s: %s",
                    dir,
                    x.getMessage()),
                    x);
        }
    }

    private void uploadPhoto(EmployeeListItem employee, Integer folderID, String prefix, String path) {
        Path dir = Paths.get(path);
        List<File> fList = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, employee.getEmployeeNumber()+".{jpg,JPG,png,pdf}")) {
            for (Path entry: stream) {
                final DocumentItem fileBody = new DocumentItem();
                fileBody.setInputStream(new FileInputStream(entry.toFile()));
                fileBody.setContentType(DocumentsServiceImpl.identifyMimeType(entry.getFileName().toString()));
                fileBody.setName("PHOTO - " + entry.getFileName().toString());
                fileBody.setFolderId(folderID);
                fileBody.setDescription(prefix);
                fileBody.setEmployeeDoc(true);

                try {
                    FileResource file = documentsService.createFile(fileBody, Constants.LOCAL, Constants.F_EMPLOYEE_PROFILE, employee.getObjectID());

                    /*if (!fileBody.getName().contains(".pdf")) {
                        commonServiceLocal.saveImageUrl(file.getBodyId(), employee.getObjectID());
                    }*/

                } catch (DuplicateNameException | QuotaExceededException | InsufficientPermissionsException | ObjectNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException x) {
            throw new RuntimeException(String.format("error reading folder %s: %s",
                    dir,
                    x.getMessage()),
                    x);
        }
    }
    private String getCookie(HttpServletRequest request, String key) {
        if (request.getCookies() != null) {
            for (int i = 0; i < request.getCookies().length; i++) {
                if (request.getCookies()[i].getName().equals(key)) {
                    return request.getCookies()[i].getValue();
                }
            }
        }
        return null;
    }
}
