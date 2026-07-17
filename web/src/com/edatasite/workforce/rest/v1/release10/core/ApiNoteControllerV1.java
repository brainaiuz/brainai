package com.edatasite.workforce.rest.v1.release10.core;

import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.documents.client.gwtupload.UUID;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.server.GwtUploadServlet;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.gwt.note.server.NoteServiceLocal;
import com.edatasite.workforce.rest.base.enums.NoteEnum;
import com.edatasite.workforce.rest.base.enums.NoteRelationTypeEnum;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.MListingFilterParameter;
import com.edatasite.workforce.rest.base.to.AttachmentTO;
import com.edatasite.workforce.rest.base.to.CommentTO;
import com.edatasite.workforce.rest.base.to.NoteTO;
import com.edatasite.workforce.rest.base.to.SelectItemTO;
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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Dilshod Madrahimov on 3/30/15.
 */
@Tag(name = "Note", description = "Note API")
@RestController
@RequestMapping(value = "/note", headers = {ApiConstants.SESSION_ID, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiNoteControllerV1 extends BaseApiControllerV1 {

    @Autowired
    private NoteServiceLocal noteServiceLocal;
    @Autowired
    private DocumentsServiceLocal documentsServiceLocal;

    @Autowired
    private ServletContext servletContext;

    @RequestMapping(value = "/{relationType}/{relationId}/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getList(@PathVariable(value = "relationType") String relationType,
                          @PathVariable(value = "relationId") Integer relationId,
                          @RequestBody MListingFilterParameter mFilterParameter) {
        if (mFilterParameter == null) {
            return errorResponse(ERROR_INVALID_BODY_PARAM);
        }
        if (relationType == null || relationId == null || NoteRelationTypeEnum.getRelationType(relationType) == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        ListingFilterParameter filterParameter = mFilterParameter.convertToFilterParameters();
        filterParameter.setRelationID(relationId);
        filterParameter.setRelationType(String.valueOf(NoteRelationTypeEnum.getRelationType(relationType)));

        return successResponse(noteServiceLocal.getNoteListForAPI(filterParameter));
    }

    @RequestMapping(value = "/{type}/{relationId}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object add(@PathVariable(value = "type") String relationType,
                      @PathVariable(value = "relationId") Integer relationId,
                      @RequestBody NoteTO noteTO) {
        if (noteTO == null) {
            return errorResponse(ERROR_INVALID_BODY_PARAM);
        }
        if (relationId == null || relationType == null || NoteRelationTypeEnum.getRelationType(relationType) == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }

        noteTO.setRelatedToId(NoteRelationTypeEnum.getRelationType(relationType));
        noteTO.setRelatedId(relationId);

        try {
            Integer id = noteServiceLocal.saveNote(noteTO.wrap(noteTO));
            return get(relationType, id);
        } catch (Exception e) {
            e.printStackTrace();
            return noteTO.getId() != null ? this.errorResponse(ERROR_FAILED_UPDATE) : this.errorResponse(ERROR_FAILED_SAVE);
        }

    }

    @RequestMapping(value = "/{relationType}/{relationId}/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable(value = "relationType") String relationType,
                      @PathVariable(value = "id") Integer id) {
        if (relationType == null || NoteRelationTypeEnum.getRelationType(relationType) == null || id == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }

        NoteTO noteTO = noteServiceLocal.getNoteForAPI(id);

        if (noteTO == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        return successResponse(noteTO);
    }

    @RequestMapping(value = "/{relationType}/{relationId}/{id}", method = RequestMethod.DELETE)
    public Object delete(@PathVariable(value = "relationType") String relationType,
                         @PathVariable(value = "id") Integer id) {

        if (relationType == null || NoteRelationTypeEnum.getRelationType(relationType) == null || id == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        try {
            noteServiceLocal.deleteNote(id);
        } catch (Exception e) {
            e.printStackTrace();
            return this.errorResponse(ERROR_FAIL_DELETE);
        }
        return this.successResponse(SUCCESS_DELETE);
    }

    @RequestMapping(value = "/{relationType}/{relationId}/{id}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object update(@PathVariable(value = "relationType") String relationType,
                         @PathVariable(value = "relationId") Integer relationId,
                         @PathVariable(value = "id") Integer id,
                         @RequestBody NoteTO noteTO) {
        if (id == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }

        noteTO.setId(id);
        return add(relationType, relationId, noteTO);
    }

    @RequestMapping(value = "/{relationType}/{relationId}/{id}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Object updateField(@PathVariable(value = "relationType") String relationType,
                              @PathVariable(value = "relationId") Integer relationId,
                              @PathVariable(value = "id") Integer id,
                              @RequestParam(value = "comment") String comment,
                              @RequestParam(value = "visibility") String visibility) {

        if (id == null || relationId == null || relationType == null || NoteRelationTypeEnum.getRelationType(relationType) == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }

        HistoryListItem noteItem = noteServiceLocal.getNote(id);

        if (noteItem == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }

        if (!StringUtil.isEmpty(comment)) {
            noteItem.setComment(comment);
            noteItem.setVisibility(this.getVisibility(visibility));
            try {
                noteServiceLocal.saveNote(noteItem);
            } catch (Exception e) {
                e.getStackTrace();
                return this.errorResponse(ERROR_FAILED_UPDATE);
            }
        }

        return this.get(relationType, noteItem.getObjectID());
    }

    @RequestMapping(value = "/{relationType}/{relationId}/{noteId}/comment/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getCommentList(@PathVariable(value = "relationType") Integer relationId,
                                 @PathVariable(value = "noteId") Integer noteId,
                                 @RequestBody MListingFilterParameter mFilterParameter) {
        if (relationId == null || noteId == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        if (mFilterParameter == null) {
            return this.errorResponse(ERROR_INVALID_BODY_PARAM);
        }
        ListingFilterParameter filter = mFilterParameter.convertToFilterParameters();
        filter.setRelationID(relationId);
        filter.setRelationToID(noteId);
        return successResponse(noteServiceLocal.getCommentListForAPI(filter));
    }

    @RequestMapping(value = "/{relationType}/{relationId}/{noteId}/comment/{id}", method = RequestMethod.GET)
    public Object getComment(@PathVariable(value = "relationType") String relationType,
                             @PathVariable(value = "relationId") Integer relationId,
                             @PathVariable(value = "noteId") Integer noteId,
                             @PathVariable(value = "id") Integer id) {
        if (relationId == null || noteId == null || id == null || NoteRelationTypeEnum.getRelationType(relationType) == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }

        CommentTO commentTO = noteServiceLocal.getCommentForAPI(id);
        if (commentTO == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        return successResponse(commentTO);
    }

    @RequestMapping(value = "/{relationType}/{relationId}/{noteId}/comment", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object createComment(@PathVariable(value = "relationType") String relationType,
                                @PathVariable(value = "relationId") Integer relationId,
                                @PathVariable(value = "noteId") Integer noteId,
                                @RequestBody CommentTO commentTO) {
        Integer result = null;
        if (relationId == null || noteId == null || NoteRelationTypeEnum.getRelationType(relationType) == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        if (commentTO == null) {
            return this.errorResponse(ERROR_INVALID_BODY_PARAM);
        }
        commentTO.setNoteId(noteId);
        try {
            result = noteServiceLocal.saveNoteComment(commentTO.wrap(commentTO));
        } catch (Exception e) {
            e.getStackTrace();
            return this.errorResponse(ERROR_FAILED_SAVE);
        }

        return getComment(relationType, relationId, noteId, result);
    }

    @RequestMapping(value = "/{relationType}/{relationId}/{noteId}/comment/{id}", method = RequestMethod.DELETE)
    public Object deleteComment(@PathVariable(value = "relationType") String relationType,
                                @PathVariable(value = "relationId") Integer relationId,
                                @PathVariable(value = "noteId") Integer noteId,
                                @PathVariable(value = "id") Integer id) {
        if (relationId == null || noteId == null || id == null || NoteRelationTypeEnum.getRelationType(relationType) == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        try {
            noteServiceLocal.deleteNoteComment(id);
        } catch (Exception e) {
            e.getStackTrace();
            return this.errorResponse(ERROR_FAIL_DELETE);
        }
        return this.errorResponse(SUCCESS_DELETE);
    }

    @RequestMapping(value = "/{relationType}/{relationId}/{noteId}/comment/{id}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Object updateComment(@PathVariable(value = "relationType") String relationType,
                                @PathVariable(value = "relationId") Integer relationId,
                                @PathVariable(value = "noteId") Integer noteId,
                                @PathVariable(value = "id") Integer id,
                                @RequestBody CommentTO commentTO) {
        commentTO.setId(id);
        return createComment(relationType, relationId, noteId, commentTO);
    }

    @RequestMapping(value = "/visibilityTypes", method = RequestMethod.GET)
    public Object visibilityTypes() {
        return successResponse(new ArrayList<SelectItemTO>() {{
            this.add(new SelectItemTO(NoteEnum.PUBLIC.getName(), NoteEnum.PUBLIC.getCode()));
            this.add(new SelectItemTO(NoteEnum.INTERNAL.getName(), NoteEnum.INTERNAL.getCode()));
            this.add(new SelectItemTO(NoteEnum.PRIVATE.getName(), NoteEnum.PRIVATE.getCode()));
        }});
    }

    public Boolean getVisibility(String visibility) {
        if (visibility == null) {
            return null;
        }

        if (NoteEnum.PRIVATE.getCode().equals(visibility)) {
            return true;
        } else if (NoteEnum.PUBLIC.getCode().equals(visibility)) {
            return false;
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/{relationType}/{relationId}/{visibility}/{duration}", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Object uploadVoiceNote(@PathVariable(value = "relationType") String relationType,
                                  @PathVariable(value = "relationId") Integer relationId,
                                  @PathVariable(value = "visibility") String visibility,
                                  @PathVariable(value = "duration") Long duration,
                                  @RequestParam("file") MultipartFile uploadFile) {
        if (relationType == null || relationId == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        if (uploadFile == null) {
            return this.errorResponse(ERROR_INVALID_BODY_PARAM);
        }
        if (GwtUploadServlet.realPath == null) {
            GwtUploadServlet.realPath = servletContext.getRealPath("uploads") + "/";
        }

        Random r = new Random();
        String s = String.valueOf(r.nextLong());
        String fileName = relationId + "_" + relationType + "_" + s.substring(s.length() - 5);
        String fileNameEncode = UUID.uuid() + "_upld_" + fileName;

        String fileExt = uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().lastIndexOf('.')).toLowerCase(Locale.ENGLISH);

        try {
            OutputStream out = new FileOutputStream(new File(GwtUploadServlet.realPath + fileNameEncode + fileExt));
            IOUtils.copy(uploadFile.getInputStream(), out);
            uploadFile.getInputStream().close();
            out.flush();
            out.close();

            ArrayList<FileResource> files = new ArrayList<>();
            FileResource fileResource = new FileResource();
            fileResource.setName(fileNameEncode + fileExt);
            fileResource.setPath(GwtUploadServlet.realPath + fileNameEncode + fileExt);
            fileResource.setUploadType(EdsContextParams.getUploadType());
            files.add(fileResource);

            FolderResource folderResource = documentsServiceLocal.getFolderResource(Constants.F_NOTE, Constants.F_NOTE);
            if (folderResource == null) {
                return errorResponse(ERROR_RESOURCE_NOT_FOUND);
            }
            folderResource.setEntityId(relationId);
            if (duration != null) {
                folderResource.setDuration(duration);
            }
            ArrayList<FileResource> fileResources = documentsServiceLocal.uploadAllFiles(files, folderResource, null);
            if (fileResources.isEmpty()) {
                return errorResponse(ERROR_FAILED_SAVE);
            }
            for (FileResource fr : fileResources) {
                if (fr.getFileName().contains(fileName)) {
                    NoteTO noteTO = new NoteTO();
                    noteTO.setSubject(uploadFile.getOriginalFilename());
                    AttachmentTO attachmentTO = new AttachmentTO();
                    attachmentTO.setId(fr.getObjectId());
                    attachmentTO.setBodyId(fr.getBodyId());
                    noteTO.setAttachment(attachmentTO);
                    if (visibility == null) {
                        noteTO.setVisibility(new SelectItemTO(NoteEnum.INTERNAL.getName(), NoteEnum.INTERNAL.getCode()));
                    } else if (NoteEnum.PRIVATE.getCode().equalsIgnoreCase(visibility)) {
                        noteTO.setVisibility(new SelectItemTO(NoteEnum.PRIVATE.getName(), NoteEnum.PRIVATE.getCode()));
                    } else {
                        noteTO.setVisibility(new SelectItemTO(NoteEnum.PUBLIC.getName(), NoteEnum.PUBLIC.getCode()));
                    }
                    return add(relationType, relationId, noteTO);
                }
            }
            return errorResponse(ERROR_FAILED_SAVE);

        } catch (IOException e) {
            e.printStackTrace();
            return errorResponse(ERROR_FAILED_SAVE);
        }
    }

}
