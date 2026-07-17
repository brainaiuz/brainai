package com.edatasite.workforce.rest.v1.release10.document;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.gwtupload.UUID;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.server.GwtUploadServlet;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
import com.edatasite.workforce.rest.base.enums.ApiActionEnum;
import com.edatasite.workforce.rest.base.enums.FolderRelationTypeEnum;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.MListingFilterParameter;
import com.edatasite.workforce.rest.base.to.AttachmentTO;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v1.release10.core.BaseApiControllerV1;
import com.edatasite.workforce.utils.EdsContextParams;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by Dilshod Madrahimov on 3/30/15.
 */
@Tag(name = "Document", description = "Document API")
@RestController
@RequestMapping(value = "/document", headers = {ApiConstants.SESSION_ID, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiDocumentControllerV1 extends BaseApiControllerV1 implements Constants {

    static ArrayList<String> fileNames = new ArrayList<>();
    @Autowired
    private DocumentsServiceLocal documentsServiceLocal;
    @Autowired
    private TaskServiceLocal taskServiceLocal;
    @Autowired
    private ServletContext servletContext;

    @RequestMapping(value = "/{relationType}/{relationId}/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getList(@PathVariable(value = "relationType") String relationType,
                          @PathVariable(value = "relationId") Integer relationId,
                          @RequestBody MListingFilterParameter filterParameter) {

        if (filterParameter.getStart() == null) {
            filterParameter.setStart(0);
            filterParameter.setLimit(20);
        }

        Integer folderId = getFolderId(FolderRelationTypeEnum.getRelationType(relationType), relationId);

        List<FileResource> fileResources = documentsServiceLocal.getFileResources(FolderRelationTypeEnum.getRelationType(relationType), folderId, relationId);
        if (fileResources == null || fileResources.size() == 0) {
            return successResponse();
        }

        if (!ServerUtils.isNullOrEmpty(filterParameter.getSearchKey())) {
            String searchKey = filterParameter.getSearchKey();
            fileResources = fileResources.stream()
                    .filter(item -> item.getFileName().startsWith(searchKey))
                    .collect(Collectors.toList());
        }
        ArrayList<AttachmentTO> attachmentTOs = new ArrayList<>(fileResources.size());

        int size = fileResources.size();
        int start = filterParameter.getStart() > size ? size : filterParameter.getStart();
        int limit = filterParameter.getLimit() > size ? size : filterParameter.getLimit();

        List<FileResource> subList = fileResources.subList(start, limit);
        for (FileResource fileResource : subList) {
            attachmentTOs.add(new AttachmentTO(fileResource));
        }

        return successResponse(new ListResultTO<>(size, attachmentTOs));

    }

    @RequestMapping(value = "/{relationType}/{relationId}/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable(value = "relationType") String relationType,
                      @PathVariable(value = "relationId") Integer relationId,
                      @PathVariable(value = "id") Integer id) {

        Integer folderId = getFolderId(FolderRelationTypeEnum.getRelationType(relationType), relationId);

        List<FileResource> fileResources = documentsServiceLocal.getFileResources(FolderRelationTypeEnum.getRelationType(relationType), folderId, relationId);
        if (fileResources == null || fileResources.size() == 0) {
            return successResponse();
        }
        FileResource fileResource = fileResources.stream().filter(item -> item.getObjectId().equals(id)).findFirst().orElse(null);
        if (fileResource == null) {
            return errorResponse();
        }
        return successResponse(new AttachmentTO(fileResource));

    }

    @RequestMapping(value = "/{relationType}/{relationId}/{id}", method = RequestMethod.DELETE)
    public Object delete(@PathVariable(value = "relationType") String relationType,
                         @PathVariable(value = "relationId") Integer relationId,
                         @PathVariable(value = "id") Integer id) {

        try {
            documentsServiceLocal.deleteFile(id);
            return successResponse(SUCCESS_DELETE);
        } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
            e.printStackTrace();
            return errorResponse(ERROR_FAIL_DELETE);
        }
    }

    @RequestMapping(value = "/{relationType}/{relationId}/upload", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Object upload(@PathVariable(value = "relationType") String relationType,
                         @PathVariable(value = "relationId") Integer relationId,
                         @RequestParam(value = "file") MultipartFile uploadFile) {


        String originalFileName = uploadFile.getOriginalFilename().replace("%20", " ");
        if (GwtUploadServlet.realPath == null) {
            GwtUploadServlet.realPath = servletContext.getRealPath("uploads") + "/";
        }

        String fileName = UUID.uuid() + "_upld_" + originalFileName;

        fileNames.add(fileName);
        try {
            String filename = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
            String url = GwtUploadServlet.realPath + filename;
            final File file = new File(url);
            file.getParentFile().mkdirs();
            FileOutputStream os = new FileOutputStream(file);
            IOUtils.copy(uploadFile.getInputStream(), os);
            uploadFile.getInputStream().close();
            os.flush();
            os.close();
            return successResponse("Successfully uploaded.");
        } catch (IOException e) {
            e.printStackTrace();
            return errorResponse("Upload failed.");
        }
    }

    @RequestMapping(value = "/{relationType}/{relationId}/save", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object save(@PathVariable(value = "relationType") String relationType,
                       @PathVariable(value = "relationId") Integer relationId,
                       @RequestBody AttachmentTO attachmentTO) {

        if (fileNames.isEmpty()) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        ArrayList<FileResource> files = new ArrayList<>(fileNames.size());

        for (String filename : fileNames) {
            FileResource fileResource = new FileResource();
            fileResource.setName(filename);
            fileResource.setPath(GwtUploadServlet.realPath + filename);
            fileResource.setUploadType(EdsContextParams.getUploadType());
            fileResource.setDescription(attachmentTO.getDescription());
            files.add(fileResource);
        }

        Integer entityId = getFolderId(FolderRelationTypeEnum.getRelationType(relationType), relationId);
        FolderResource folderResource = documentsServiceLocal.getFolderResource(FolderRelationTypeEnum.getRelationType(relationType), entityId);
        try {
            folderResource.setEntityId(relationId);
            documentsServiceLocal.uploadAllFiles(files, folderResource, files.get(0).getDescription());
            fileNames.clear();
            return successResponse(SUCCESS_SAVE);
        } catch (Exception e) {
            e.printStackTrace();
            fileNames.clear();
            return errorResponse(ERROR_FAILED_SAVE);
        }

    }

    @RequestMapping(value = "/{relationType}/{relationId}/remove", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object cancel(@PathVariable(value = "relationType") String relationType,
                         @PathVariable(value = "relationId") Integer relationId,
                         @RequestBody AttachmentTO attachmentTO) {

        fileNames.remove(attachmentTO.getName());
        return successResponse();
    }

    private Integer getFolderId(Integer folderType, Integer relationId) {
        switch (folderType) {
            case F_TASK, F_PR_ISSUE -> {
                SelectItem projectItem = taskServiceLocal.getProjectByTask(relationId);
                return projectItem.getId();
            }
            case F_PROJECT, F_CRM_CONTACT, F_LEAD, F_CRM_ACCOUNT, F_LEAVE_REQUEST, F_NOTE, F_EMPLOYEE_PROFILE, F_EXP, F_EXP_DOC, F_OPPORTUNITY -> {
                return relationId;
            }
            default -> {
                return F_DEFAULT;
            }
        }
    }

    @RequestMapping(value = "/{relationType}/{relationId}/{actionType}/{id}", method = RequestMethod.GET)
    public Object doAction(@PathVariable(value = "relationType") String relationType,
                           @PathVariable(value = "relationId") Integer relationId,
                           @PathVariable(value = "actionType") String actionType,
                           @PathVariable(value = "id") Integer id) {

        ApiActionEnum actionTypeEnum = getActionType(actionType);
        if (actionTypeEnum == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        return get(relationType, relationId, id);
    }


    @RequestMapping(value = "/relationTypes", method = RequestMethod.GET)
    public Object getRelationTypes() {
        ArrayList<String> relationTypes = new ArrayList<>();
        relationTypes.add(FolderRelationTypeEnum.ACCOUNT.getCode().toLowerCase());
        relationTypes.add(FolderRelationTypeEnum.CONTACT.getCode().toLowerCase());
        relationTypes.add(FolderRelationTypeEnum.ISSUE.getCode().toLowerCase());
        relationTypes.add(FolderRelationTypeEnum.LEAD.getCode().toLowerCase());
        relationTypes.add(FolderRelationTypeEnum.LEAVE_REQUEST.getCode().toLowerCase());
        relationTypes.add(FolderRelationTypeEnum.PROJECT.getCode().toLowerCase());
        relationTypes.add(FolderRelationTypeEnum.TASK.getCode().toLowerCase());
        relationTypes.add(FolderRelationTypeEnum.NOTE.getCode().toLowerCase());
        relationTypes.add(FolderRelationTypeEnum.EMPLOYEE.getCode().toLowerCase());
        relationTypes.add(FolderRelationTypeEnum.OPPORTUNITY.getCode().toLowerCase());
        relationTypes.add(FolderRelationTypeEnum.EXPENSE.getCode().toLowerCase());
        relationTypes.add(FolderRelationTypeEnum.EXPENSE_ITEM.getCode().toLowerCase());
        return successResponse(relationTypes);
    }
}
