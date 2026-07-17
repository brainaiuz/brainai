package com.workforcetrack.api.controllers;

import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.workforcetrack.api.aspects.CheckRequest;
import com.workforcetrack.api.base.APIConstants;
import com.workforcetrack.api.exceptions.ApiExceptions;
import com.workforcetrack.api.exceptions.BaseApiException;
import com.workforcetrack.api.presenter.AttachmentApiPresenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: shahob
 * Date: 05/09/12
 * Time: 19:03
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/attachment")
public class AttachmentApiController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private DocumentsService documentsService;

    @RequestMapping(value = "/search", method = RequestMethod.GET, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest
    @ResponseBody
    public Object getAttachments(@RequestParam(value = "taskId", required = false, defaultValue = "0") int taskId,
                                 @RequestParam(value = "projectId", required = false, defaultValue = "0") int projectId) throws BaseApiException {
        try {
            FileResource[] attachments = null;
            if (taskId != 0) {
                attachments = taskService.getTaskAttachments(taskId);
            } else if (projectId != 0) {
                attachments = projectService.getProjectAttachments(projectId);
            }

            AttachmentApiPresenter presenter = new AttachmentApiPresenter();

            return presenter.convertToMap(attachments);
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @CheckRequest
    @ResponseBody
    @RequestMapping(value = "/{Id}", method = RequestMethod.DELETE, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    public Object delete(@PathVariable Integer Id) throws BaseApiException {
        try {
             documentsService.deleteFile(Id);
            return Id;
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }

    }
}
