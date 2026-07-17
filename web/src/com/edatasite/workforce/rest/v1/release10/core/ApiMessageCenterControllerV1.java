package com.edatasite.workforce.rest.v1.release10.core;

import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.messagecenter.server.MessageCenterServiceLocal;
import com.edatasite.workforce.rest.base.enums.ApiActionEnum;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.MessageCenterTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * Created by dilshod madrahimov on 3/24/15.
 */
@Tag(name = "Message Center", description = "Message Center API")
@RestController
@RequestMapping(value = "/messageCenter", headers = {ApiConstants.SESSION_ID, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiMessageCenterControllerV1 extends BaseApiControllerV1 {

    @Autowired
    private MessageCenterServiceLocal messageCenterServiceLocal;


    @RequestMapping(value = "/{relationType}/{relationId}/{status}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object sendEmail(@PathVariable(value = "relationType") String relationType,
                            @PathVariable(value = "relationId") Integer relationId,
                            @PathVariable(value = "status") String status,
                            @RequestBody MessageCenterTO messageCenterTO) {
        if (relationType == null || relationId == null || status == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        if (messageCenterTO == null) {
            return errorResponse(ERROR_INVALID_BODY_PARAM);
        }
        Email email = new Email();
        email.setContent(messageCenterTO.getContent());
        email.setFromEmail(messageCenterTO.getFromEmail());
        if (messageCenterTO.getToEmail() != null) {
            email.setToEmail(ServerUtils.getAsCommoDelimited(messageCenterTO.getToEmail(), "", ","));
        }
        if (messageCenterTO.getBcc() != null) {
            email.setBcc(ServerUtils.getAsCommoDelimited(messageCenterTO.getBcc(), "", ","));
        }
        if (messageCenterTO.getCc() != null) {
            email.setCc(ServerUtils.getAsCommoDelimited(messageCenterTO.getCc(), "", ","));
        }
        if (messageCenterTO.getEmailTemplate() != null) {
            email.setTemplateId(messageCenterTO.getEmailTemplate().getId());
        }
        ArrayList<RelationItem> relations = new ArrayList<>();
        //TODO set relation
        if (ApiActionEnum.DRAFT.getCode().equalsIgnoreCase(status)) {
            try {
                messageCenterServiceLocal.saveAsDraft(email);
                return successResponse();
            } catch (Exception e) {
                e.printStackTrace();
                return errorResponse();
            }
        } else if (ApiActionEnum.SEND.getCode().equalsIgnoreCase(status)) {
            try {
                messageCenterServiceLocal.sendMessage(email);
                return successResponse();
            } catch (Exception e) {
                e.printStackTrace();
                return errorResponse();
            }
        }

        return errorResponse();
    }


}
