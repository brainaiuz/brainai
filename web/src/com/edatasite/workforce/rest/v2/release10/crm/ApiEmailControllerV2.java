package com.edatasite.workforce.rest.v2.release10.crm;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsAttachment;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.emailfetching.mongo.EdsEmail;
import com.edatasite.workforce.gwt.core.client.rpc.EmailAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.actions.CreateAttachmentHandler;
import com.edatasite.workforce.gwt.core.server.actions.CreateDocumentCommand;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.mongo.EmailRepository;
import com.edatasite.workforce.gwt.core.server.servlets.WfmMultipartFile;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.messagecenter.client.enumtype.MCFolderType;
import com.edatasite.workforce.gwt.messagecenter.server.MessageCenterServiceLocal;
import com.edatasite.workforce.rest.aspects.CheckPermission;
import com.edatasite.workforce.rest.base.enums.ApiActionEnum;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.PagingListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseListData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.link.LinkTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.LinksTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.email.EmailAttachmentsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.email.EmailContentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.email.EmailInformationTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.email.EmailListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.email.EmailReadTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.email.EmailSaveEditTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.email.EmailSenderTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.email.SendEmailDataTO;
import com.edatasite.workforce.rest.v2.release10.enums.EmailTypeEnum;
import com.edatasite.workforce.rest.v2.release10.enums.OrderByEnum;
import com.edatasite.workforce.rest.v2.release10.enums.OrderFieldEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

/**
 * Created by Dilshod Madrahimov on 04/04/2018.
 */
@Tag(name = "Emails", description = "Emails API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiEmailControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiEmailControllerV2.class);
    private final SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

    @Autowired
    private MessageCenterServiceLocal messageCenterServiceLocal;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private AttachmentManager attachmentManager;
    /*@Autowired
    private CreateAttachmentHandler createAttachmentHandler;*/


    @Operation(summary = "Get Emails")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have emails"),
            @ApiResponse(responseCode = "400", description = "id is required")})
    @RequestMapping(value = "/{main_entity_name}/{item_id}/emails", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.CRM_E_MAIL_MARKETING_TAB, PermissionConstants.CRM_MESSAGE_CENTER})
    public Object getEmails(@PathVariable(value = "main_entity_name") String main_entity_name,
                            @PathVariable(value = "item_id") Integer item_id,
                            @RequestParam(value = "query", required = false) String query,
                            @RequestParam(value = "limit", required = false) Integer limit,
                            @RequestParam(value = "offset", required = false) Integer offset,
                            @RequestParam(value = "sort_type") String sort_type,
                            @RequestParam(value = "direction") String direction) throws RestException {

        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(main_entity_name)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "main_entity_name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(sort_type)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "sort_type is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(direction)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "direction is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        Integer start = (offset != null && offset > 0) ? offset : 0;
        Integer maxLimit = (limit != null && limit > 0) ? limit : MAX_LIMIT;

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setSearchKey(query);
        filterParameter.setSearchButton(true);
        filterParameter.setRelationType(getEntityRelation(main_entity_name));
        filterParameter.setRelationID(item_id);
        filterParameter.setStart(start);
        filterParameter.setLimit(maxLimit);
        filterParameter.setSortDir(OrderByEnum.ASC.getId());
        filterParameter.setAscending(OrderByEnum.ASC.getDirection().equalsIgnoreCase(direction));
        filterParameter.setSortField(getSortField(OrderFieldEnum.getOrderField(sort_type), ListPanelType.MessageCenter));

        ListResult<Email> emailListResult;
        try {
            emailListResult = messageCenterServiceLocal.getEmails(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        PagingListResultTO<EmailListTO> pagingListResultTO = new PagingListResultTO<>();
        pagingListResultTO.setTotal_count(emailListResult.getTotal());
        if (emailListResult.getTotal() < (maxLimit + start)) {
            pagingListResultTO.setLeft(0);
        } else {
            pagingListResultTO.setLeft(emailListResult.getTotal() - (maxLimit + start));
        }
        pagingListResultTO.setCount(emailListResult.getList() != null ? emailListResult.getList().size() : 0);
        pagingListResultTO.setOffset(start);

        Integer companyId = userManager.getUser().getCompany().getObjectID();
        ArrayList<EmailListTO> emailList = new ArrayList<>();
        for (Email email : emailListResult.getList()) {
            EmailListTO emailListTO = new EmailListTO();
            emailListTO.setId(email.getObjectID());
            if (StringUtils.isNotBlank(email.getSubject())) {
                emailListTO.setSubject(email.getSubject());
            }
            if (email.getDate() != null) {
                emailListTO.setDate(longDateTimezoneFormat.format(email.getDate()));
            }
            emailListTO.setRead(email.isSeen());
            if (MCFolderType.DRAFT.equals(email.getType())) {
                emailListTO.setType(EmailTypeEnum.DRAFT.name());
            } else if (MCFolderType.INBOX.equals(email.getType())) {
                emailListTO.setType(EmailTypeEnum.INCOME.name());
            } else {
                emailListTO.setType(EmailTypeEnum.OUTCOME.name());
            }

            String fromEmail = email.getFromEmail();
            if (StringUtils.isNotBlank(fromEmail)) {
                if (fromEmail.contains("<") && fromEmail.contains(">") && fromEmail.indexOf("<") < fromEmail.indexOf(">")) {
                    fromEmail = fromEmail.substring(fromEmail.indexOf("<") + 1, fromEmail.indexOf(">")).trim();
                }
                if (fromEmail.startsWith("no-reply")) {
                    emailListTO.setSender_name(fromEmail);
                } else {
                    EdsUser user = userManager.getUserByEmail(email.getFromEmail());
                    if (user != null) {
                        emailListTO.setSender_name(user.getName());
                        if (user.getPhoto() != null) {
                            emailListTO.setSender_avatar(commonServiceLocal.getImageUrl(user.getPhoto().getObjectID()));
                        }
                    } else {
                        EdsCrmContact crmContact = crmContactManager.getContactByEmail(fromEmail, companyId);
                        if (crmContact != null) {
                            emailListTO.setSender_name(crmContact.getName());
                            if (crmContact.getPhoto() != null) {
                                emailListTO.setSender_avatar(commonServiceLocal.getImageUrl(crmContact.getPhoto().getObjectID()));
                            }
                        } else {
                            EdsCrmContact lead = crmContactManager.getLeadByEmail(fromEmail, companyId);
                            if (lead != null) {
                                emailListTO.setSender_name(lead.getName());
                                if (lead.getPhoto() != null) {
                                    emailListTO.setSender_avatar(commonServiceLocal.getImageUrl(lead.getPhoto().getObjectID()));
                                }
                            } else {
                                EdsCrmAccount crmAccount = crmAccountManager.getCrmAccountByEmail(fromEmail, companyId);
                                if (crmAccount != null) {
                                    emailListTO.setSender_name(crmAccount.getName());
                                    if (crmAccount.getLogo() != null) {
                                        emailListTO.setSender_avatar(commonServiceLocal.getImageUrl(crmAccount.getLogo().getObjectID()));
                                    }
                                } else {
                                    emailListTO.setSender_name(fromEmail);
                                }
                            }
                        }

                    }
                }
            }

            emailList.add(emailListTO);
        }

        pagingListResultTO.setList(emailList);

        return successResponse(pagingListResultTO);
    }

    @Operation(summary = "Delete Email", description = "Request to delete particular email.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/emails/{email_id}", method = RequestMethod.DELETE)
    @CheckPermission(permissions = {PermissionConstants.CRM_E_MAIL_MARKETING_TAB, PermissionConstants.CRM_MESSAGE_CENTER})
    public Object deleteEmail(@PathVariable(value = "email_id") String email_id) throws RestException {

        if (email_id == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "email_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        Optional<EdsEmail> edsEmail = emailRepository.findById(email_id);
        if (edsEmail.isEmpty() || edsEmail.get().isDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Email with id " + email_id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        try {
            ArrayList<String> emails = new ArrayList<>();
            emails.add(edsEmail.get().getId());
            messageCenterServiceLocal.setEmailFlags(emails, edsEmail.get().getEmailSettingId(), Constants.FLAG_DELETED);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return successResponse(new ResponseData());
    }

    @Operation(summary = "Mark Email as read/unread", description = "Request to mark particular email as read/unread")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/emails/{email_id}/read", method = RequestMethod.PUT)
    @CheckPermission(permissions = {PermissionConstants.CRM_E_MAIL_MARKETING_TAB, PermissionConstants.CRM_MESSAGE_CENTER})
    public Object readEmail(@PathVariable(value = "email_id") String email_id,
                            @RequestBody EmailReadTO emailReadTO) throws RestException {

        if (email_id == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "email_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        Optional<EdsEmail> edsEmail = emailRepository.findById(email_id);
        if (edsEmail.isEmpty() || edsEmail.get().isDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Email with id " + email_id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        try {
            ArrayList<String> emails = new ArrayList<>();
            emails.add(edsEmail.get().getId());
            messageCenterServiceLocal.setEmailFlags(emails, edsEmail.get().getEmailSettingId(), emailReadTO.isRead() ? Constants.FLAG_READ : Constants.FLAG_UNREAD);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return successResponse(new ResponseData());
    }

    @Operation(summary = "Get Email Information")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have email information"),
            @ApiResponse(responseCode = "400", description = "item_id is required")})
    @RequestMapping(value = "/emails/{email_id}", method = RequestMethod.GET)
    public Object getEmailInformation(@PathVariable(value = "email_id") String email_id) throws RestException {

        if (email_id == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "email_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        /*if (email_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "email_id should be more than 0", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }*/
        Integer companyId = userManager.getUser().getCompany().getObjectID();
        Email email = messageCenterServiceLocal.getEmailWithContent(email_id);
        EmailInformationTO emailInformation = new EmailInformationTO();
        if (email != null) {
            if (StringUtils.isNotBlank(email.getSubject())) {
                emailInformation.setSubject(email.getSubject());
            }
            if (email.getDate() != null) {
                emailInformation.setDate(longDateTimezoneFormat.format(email.getDate()));
            }
            if (StringUtils.isNotBlank(email.getFromEmail())) {

                EdsUser user = userManager.getUserByEmail(email.getFromEmail());

                //Email send
                EmailSenderTO emailSender = null;
                if (user != null) {
                    emailSender = new EmailSenderTO();
                    emailSender.setId(user.getObjectID());
                    if (StringUtils.isNotBlank(user.getName())) {
                        emailSender.setName(user.getName());
                    }
                    if (StringUtils.isNotBlank(user.getEmail())) {
                        emailSender.setEmail(user.getEmail());
                    }
                    if (user.getPhoto() != null) {
                        emailSender.setAvatar(commonServiceLocal.getImageUrl(user.getPhoto().getObjectID()));
                    }
                } else {
                    EdsCrmContact crmContact = crmContactManager.getContactByEmail(email.getFromEmail(), companyId);
                    if (crmContact != null) {
                        emailSender = new EmailSenderTO();
                        emailSender.setId(crmContact.getObjectID());
                        if (StringUtils.isNotBlank(crmContact.getName())) {
                            emailSender.setName(crmContact.getName());
                        }
                        if (StringUtils.isNotBlank(crmContact.getPrimaryEmail())) {
                            emailSender.setEmail(crmContact.getPrimaryEmail());
                        }
                        if (crmContact.getPhoto() != null) {
                            emailSender.setAvatar(commonServiceLocal.getImageUrl(crmContact.getPhoto().getObjectID()));
                        }
                    } else {
                        EdsCrmContact lead = crmContactManager.getLeadByEmail(email.getFromEmail(), companyId);
                        if (lead != null) {
                            emailSender = new EmailSenderTO();
                            emailSender.setId(lead.getObjectID());
                            if (StringUtils.isNotBlank(lead.getName())) {
                                emailSender.setName(lead.getName());
                            }
                            if (StringUtils.isNotBlank(lead.getPrimaryEmail())) {
                                emailSender.setEmail(lead.getPrimaryEmail());
                            }
                            if (lead.getPhoto() != null) {
                                emailSender.setAvatar(commonServiceLocal.getImageUrl(lead.getPhoto().getObjectID()));
                            }
                        } else {
                            EdsCrmAccount crmAccount = crmAccountManager.getCrmAccountByEmail(email.getFromEmail(), companyId);
                            if (crmAccount != null) {
                                emailSender = new EmailSenderTO();
                                emailSender.setId(crmAccount.getObjectID());
                                if (StringUtils.isNotBlank(crmAccount.getName())) {
                                    emailSender.setName(crmAccount.getName());
                                }
                                if (StringUtils.isNotBlank(crmAccount.getEmail())) {
                                    emailSender.setEmail(crmAccount.getEmail());
                                }
                                if (crmAccount.getLogo() != null) {
                                    emailSender.setAvatar(commonServiceLocal.getImageUrl(crmAccount.getLogo().getObjectID()));
                                }

                            }
                        }
                    }

                }
                if (emailSender == null) {
                    emailSender = new EmailSenderTO();
                    emailSender.setId(0);
                    emailSender.setName(email.getFromName());
                    emailSender.setEmail(email.getFromEmail());
                    emailSender.setAvatar(null);
                }
                emailInformation.setSender(emailSender);

                //Reply to
                EmailSenderTO emailReply = null;
                if (user != null) {
                    emailReply = new EmailSenderTO();
                    emailReply.setId(user.getObjectID());
                    if (StringUtils.isNotBlank(user.getName())) {
                        emailReply.setName(user.getName());
                    }
                    if (StringUtils.isNotBlank(user.getEmail())) {
                        emailReply.setEmail(user.getEmail());
                    }
                    if (user.getPhoto() != null) {
                        emailReply.setAvatar(commonServiceLocal.getImageUrl(user.getPhoto().getObjectID()));
                    }
                } else {
                    EdsCrmContact crmContact = crmContactManager.getContactByEmail(email.getFromEmail(), companyId);
                    if (crmContact != null) {
                        emailReply = new EmailSenderTO();
                        emailReply.setId(crmContact.getObjectID());
                        if (StringUtils.isNotBlank(crmContact.getName())) {
                            emailReply.setName(crmContact.getName());
                        }
                        if (StringUtils.isNotBlank(crmContact.getPrimaryEmail())) {
                            emailReply.setEmail(crmContact.getPrimaryEmail());
                        }
                        if (crmContact.getPhoto() != null) {
                            emailReply.setAvatar(commonServiceLocal.getImageUrl(crmContact.getPhoto().getObjectID()));
                        }
                    } else {
                        EdsCrmContact lead = crmContactManager.getLeadByEmail(email.getFromEmail(), companyId);
                        if (lead != null) {
                            emailReply = new EmailSenderTO();
                            emailReply.setId(lead.getObjectID());
                            if (StringUtils.isNotBlank(lead.getName())) {
                                emailReply.setName(lead.getName());
                            }
                            if (StringUtils.isNotBlank(lead.getPrimaryEmail())) {
                                emailReply.setEmail(lead.getPrimaryEmail());
                            }
                            if (lead.getPhoto() != null) {
                                emailReply.setAvatar(commonServiceLocal.getImageUrl(lead.getPhoto().getObjectID()));
                            }
                        } else {
                            EdsCrmAccount crmAccount = crmAccountManager.getCrmAccountByEmail(email.getFromEmail(), companyId);
                            if (crmAccount != null) {
                                emailReply = new EmailSenderTO();
                                emailReply.setId(crmAccount.getObjectID());
                                if (StringUtils.isNotBlank(crmAccount.getName())) {
                                    emailReply.setName(crmAccount.getName());
                                }
                                if (StringUtils.isNotBlank(crmAccount.getEmail())) {
                                    emailReply.setEmail(crmAccount.getEmail());
                                }
                                if (crmAccount.getLogo() != null) {
                                    emailReply.setAvatar(commonServiceLocal.getImageUrl(crmAccount.getLogo().getObjectID()));
                                }
                            }
                        }
                    }

                }
                if (emailReply == null) {
                    emailReply = new EmailSenderTO();
                    emailReply.setId(0);
                    emailReply.setName(email.getFromName());
                    emailReply.setEmail(email.getFromEmail());
                    emailReply.setAvatar(null);
                }
                emailInformation.setReply_to(emailReply);

            }

            if (StringUtils.isNotBlank(email.getToEmails())) {
                String[] toEmails = email.getToEmails().split(",");
                ArrayList<EmailSenderTO> emailList = new ArrayList<>();
                for (String toEmail : toEmails) {

                    if (toEmail.contains("<") && toEmail.contains(">") && toEmail.indexOf("<") < toEmail.indexOf(">")) {
                        toEmail = toEmail.substring(toEmail.indexOf("<") + 1, toEmail.indexOf(">")).trim();
                    }

                    EmailSenderTO emailListTo;
                    EdsUser user = userManager.getUserByEmail(toEmail);
                    if (user != null) {
                        emailListTo = new EmailSenderTO();
                        emailListTo.setId(user.getObjectID());
                        if (StringUtils.isNotBlank(user.getName())) {
                            emailListTo.setName(user.getName());
                        }
                        if (StringUtils.isNotBlank(user.getEmail())) {
                            emailListTo.setEmail(user.getEmail());
                        }
                        if (user.getPhoto() != null) {
                            emailListTo.setAvatar(commonServiceLocal.getImageUrl(user.getPhoto().getObjectID()));
                        }
                        emailList.add(emailListTo);
                    } else {
                        EdsCrmContact crmContact = crmContactManager.getContactByEmail(toEmail, companyId);
                        if (crmContact != null) {
                            emailListTo = new EmailSenderTO();
                            emailListTo.setId(crmContact.getObjectID());
                            if (StringUtils.isNotBlank(crmContact.getName())) {
                                emailListTo.setName(crmContact.getName());
                            }
                            if (StringUtils.isNotBlank(crmContact.getPrimaryEmail())) {
                                emailListTo.setEmail(crmContact.getPrimaryEmail());
                            }
                            if (crmContact.getPhoto() != null) {
                                emailListTo.setAvatar(commonServiceLocal.getImageUrl(crmContact.getPhoto().getObjectID()));
                            }
                            emailList.add(emailListTo);
                        } else {
                            EdsCrmContact lead = crmContactManager.getLeadByEmail(toEmail, companyId);
                            if (lead != null) {
                                emailListTo = new EmailSenderTO();
                                emailListTo.setId(lead.getObjectID());
                                if (StringUtils.isNotBlank(lead.getName())) {
                                    emailListTo.setName(lead.getName());
                                }
                                if (StringUtils.isNotBlank(lead.getPrimaryEmail())) {
                                    emailListTo.setEmail(lead.getPrimaryEmail());
                                }
                                if (lead.getPhoto() != null) {
                                    emailListTo.setAvatar(commonServiceLocal.getImageUrl(lead.getPhoto().getObjectID()));
                                }
                                emailList.add(emailListTo);
                            } else {
                                EdsCrmAccount crmAccount = crmAccountManager.getCrmAccountByEmail(toEmail, companyId);
                                if (crmAccount != null) {
                                    emailListTo = new EmailSenderTO();
                                    emailListTo.setId(crmAccount.getObjectID());
                                    if (StringUtils.isNotBlank(crmAccount.getName())) {
                                        emailListTo.setName(crmAccount.getName());
                                    }
                                    if (StringUtils.isNotBlank(crmAccount.getEmail())) {
                                        emailListTo.setEmail(crmAccount.getEmail());
                                    }
                                    if (crmAccount.getLogo() != null) {
                                        emailListTo.setAvatar(commonServiceLocal.getImageUrl(crmAccount.getLogo().getObjectID()));
                                    }
                                    emailList.add(emailListTo);
                                }
                            }
                        }

                    }
                    if (emailList.size() > 0) {
                        emailInformation.setList_to(emailList);
                    }
                }
            }

            if (StringUtils.isNotBlank(email.getCc())) {
                String[] mailCCs = email.getCc().split(",");
                ArrayList<EmailSenderTO> emailList = new ArrayList<>();
                for (String mailCC : mailCCs) {

                    if (mailCC.contains("<") && mailCC.contains(">") && mailCC.indexOf("<") < mailCC.indexOf(">")) {
                        mailCC = mailCC.substring(mailCC.indexOf("<") + 1, mailCC.indexOf(">")).trim();
                    }

                    EmailSenderTO emailCC;
                    EdsUser user = userManager.getUserByEmail(mailCC);
                    if (user != null) {
                        emailCC = new EmailSenderTO();
                        emailCC.setId(user.getObjectID());
                        if (StringUtils.isNotBlank(user.getName())) {
                            emailCC.setName(user.getName());
                        }
                        if (StringUtils.isNotBlank(user.getEmail())) {
                            emailCC.setEmail(user.getEmail());
                        }
                        if (user.getPhoto() != null) {
                            emailCC.setAvatar(commonServiceLocal.getImageUrl(user.getPhoto().getObjectID()));
                        }
                        emailList.add(emailCC);
                    } else {
                        EdsCrmContact crmContact = crmContactManager.getContactByEmail(mailCC, companyId);
                        if (crmContact != null) {
                            emailCC = new EmailSenderTO();
                            emailCC.setId(crmContact.getObjectID());
                            if (StringUtils.isNotBlank(crmContact.getName())) {
                                emailCC.setName(crmContact.getName());
                            }
                            if (StringUtils.isNotBlank(crmContact.getPrimaryEmail())) {
                                emailCC.setEmail(crmContact.getPrimaryEmail());
                            }
                            if (crmContact.getPhoto() != null) {
                                emailCC.setAvatar(commonServiceLocal.getImageUrl(crmContact.getPhoto().getObjectID()));
                            }
                            emailList.add(emailCC);
                        } else {
                            EdsCrmContact lead = crmContactManager.getLeadByEmail(mailCC, companyId);
                            if (lead != null) {
                                emailCC = new EmailSenderTO();
                                emailCC.setId(lead.getObjectID());
                                if (StringUtils.isNotBlank(lead.getName())) {
                                    emailCC.setName(lead.getName());
                                }
                                if (StringUtils.isNotBlank(lead.getPrimaryEmail())) {
                                    emailCC.setEmail(lead.getPrimaryEmail());
                                }
                                if (lead.getPhoto() != null) {
                                    emailCC.setAvatar(commonServiceLocal.getImageUrl(lead.getPhoto().getObjectID()));
                                }
                                emailList.add(emailCC);
                            } else {
                                EdsCrmAccount crmAccount = crmAccountManager.getCrmAccountByEmail(mailCC, companyId);
                                if (crmAccount != null) {
                                    emailCC = new EmailSenderTO();
                                    emailCC.setId(crmAccount.getObjectID());
                                    if (StringUtils.isNotBlank(crmAccount.getName())) {
                                        emailCC.setName(crmAccount.getName());
                                    }
                                    if (StringUtils.isNotBlank(crmAccount.getEmail())) {
                                        emailCC.setEmail(crmAccount.getEmail());
                                    }
                                    if (crmAccount.getLogo() != null) {
                                        emailCC.setAvatar(commonServiceLocal.getImageUrl(crmAccount.getLogo().getObjectID()));
                                    }
                                    emailList.add(emailCC);
                                }
                            }
                        }

                    }
                }
                if (emailList.size() > 0) {
                    emailInformation.setList_cc(emailList);
                }

            }

            if (StringUtils.isNotBlank(email.getContent())) {
                EmailContentTO contentTO = new EmailContentTO();
                Document htmlDocument = Jsoup.parse(email.getContent().trim());
                if (htmlDocument != null) {
                    if (htmlDocument.text().length() > 150) {
                        contentTO.setPlain_text(htmlDocument.text().substring(0, 150));
                    } else {
                        contentTO.setPlain_text(htmlDocument.text());
                    }
                }
                if (StringUtils.isNotBlank(email.getContent())) {
                    contentTO.setHtml_data(email.getContent());
                }
                emailInformation.setEmail_content(contentTO);
            }

            if (email.getAttachments() != null && email.getAttachments().size() > 0) {
                ArrayList<EmailAttachmentsTO> emailAttachments = new ArrayList<>();
                for (FileResource fileResource : email.getAttachments()) {
                    EmailAttachmentsTO emailAttachment = new EmailAttachmentsTO();
                    emailAttachment.setItem_id(fileResource.getObjectId());
                    if (StringUtils.isNotBlank(fileResource.getFileName())) {
                        emailAttachment.setFile_name(fileResource.getFileName());
                    }
                    if (StringUtils.isNotBlank(fileResource.getDownloadUrl())) {
                        emailAttachment.setLink(fileResource.getDownloadUrl());
                    }
                    emailAttachment.setFile_size(ServerUtils.getSizeAsString(fileResource.getContentLength()));
                    if (fileResource.getCreationDate() != null) {
                        emailAttachment.setUpload_date(longDateTimezoneFormat.format(fileResource.getCreationDate()));
                    }
                    emailAttachments.add(emailAttachment);
                }
                emailInformation.setAttachments(emailAttachments);
            }

            return successResponse(emailInformation);
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Email with id ".concat(email_id).concat(" is not found"), NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get Emails from", description = "Retrieves list of emails received from a particular person")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of  emails received from")})
    @RequestMapping(value = "/emails/from", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.CRM_E_MAIL_MARKETING_TAB, PermissionConstants.CRM_MESSAGE_CENTER})
    public Object getEmailsFrom() throws RestException {
        EdsUser user = userManager.getUser();
        Integer companyId = user.getCompany().getObjectID();
        SelectItem[] fromEmailList = messageCenterServiceLocal.getUserEmailAccounts(true);
        ArrayList<EmailSenderTO> fromEmails = new ArrayList<>();
        if (fromEmailList != null) {
            for (SelectItem edsEmailSetting : fromEmailList) {
                if (edsEmailSetting != null) {
                    EmailSenderTO fromEmail = new EmailSenderTO();
                    fromEmail.setId(edsEmailSetting.getId());
                    if (StringUtils.isNotBlank(edsEmailSetting.getName())) {
                        fromEmail.setEmail(edsEmailSetting.getName());
                    }
                    if (StringUtils.isNotBlank(edsEmailSetting.getName())) {

                        EdsUser edsUser = userManager.getUserByEmail(edsEmailSetting.getName());
                        if (edsUser != null) {
                            if (StringUtils.isNotBlank(edsUser.getName())) {
                                fromEmail.setName(edsUser.getName());
                            }
                            if (edsUser.getPhoto() != null) {
                                fromEmail.setAvatar(commonServiceLocal.getImageUrl(edsUser.getPhoto().getObjectID()));
                            }
                        } else {
                            EdsCrmContact crmContact = crmContactManager.getContactByEmail(edsEmailSetting.getName(), companyId);
                            if (crmContact != null) {
                                if (StringUtils.isNotBlank(crmContact.getName())) {
                                    fromEmail.setName(crmContact.getName());
                                }
                                if (crmContact.getPhoto() != null) {
                                    fromEmail.setAvatar(commonServiceLocal.getImageUrl(crmContact.getPhoto().getObjectID()));
                                }
                            } else {
                                EdsCrmContact lead = crmContactManager.getLeadByEmail(edsEmailSetting.getName(), companyId);
                                if (lead != null) {
                                    if (StringUtils.isNotBlank(lead.getName())) {
                                        fromEmail.setName(lead.getName());
                                    }
                                    if (lead.getPhoto() != null) {
                                        fromEmail.setAvatar(commonServiceLocal.getImageUrl(lead.getPhoto().getObjectID()));
                                    }
                                } else {
                                    EdsCrmAccount crmAccount = crmAccountManager.getCrmAccountByEmail(edsEmailSetting.getName(), companyId);
                                    if (crmAccount != null) {
                                        if (StringUtils.isNotBlank(crmAccount.getName())) {
                                            fromEmail.setName(crmAccount.getName());
                                        }
                                        if (crmAccount.getLogo() != null) {
                                            fromEmail.setAvatar(commonServiceLocal.getImageUrl(crmAccount.getLogo().getObjectID()));
                                        }
                                    }
                                }
                            }

                        }
                    }
                    fromEmail.setDefault(edsEmailSetting.isSelected());

                    fromEmails.add(fromEmail);
                }
            }
        }
        return successResponse(new ResponseListData<>(fromEmails));
    }

    @Operation(summary = "Get Emails Reply to", description = "Retrieves list of emails reply to")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of emails reply to")})
    @RequestMapping(value = "/emails/reply_to", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.CRM_E_MAIL_MARKETING_TAB, PermissionConstants.CRM_MESSAGE_CENTER})
    public Object getEmailsReplyTo(@RequestParam(value = "query", required = false) String query,
                                   @RequestParam(value = "limit", required = false) Integer limit) throws RestException {

        Integer maxLimit = (limit != null && limit > 0) ? limit : MAX_LIMIT;
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setCRM(true);
        filterParameter.setFiltirize(false);
        filterParameter.setLimit(maxLimit);
        filterParameter.setSearchKey(query);
        filterParameter.setLookUpBy(Constants.BY_EMAIL);

        SelectItem[] selectItemListResult = allInOneServiceLocal.getLookUpItems(filterParameter, CrmConstants.CRM_CONTACT_ID, null);

        ArrayList<EmailSenderTO> emailsReplyToList = new ArrayList<>();

        Integer companyId = userManager.getUser().getCompany().getObjectID();

        if (selectItemListResult != null && selectItemListResult.length > 0) {

            ArrayList<SelectItem> stringArrayList = new ArrayList<>(Arrays.asList(selectItemListResult));
            ArrayList<SelectItem> sublist = ListUtils.getSublistSmart(stringArrayList, 0, maxLimit);

            for (SelectItem item : sublist) {
                if (item != null && StringUtils.isNotBlank(item.getName())) {
                    EmailSenderTO emailReplyTo = new EmailSenderTO();
                    emailReplyTo.setId(item.getId());
                    emailReplyTo.setEmail(item.getName());

                    if (StringUtils.isNotBlank(item.getName()) && !item.getName().startsWith("no-reply")) {

                        EdsUser edsUser = userManager.getUserByEmail(item.getName());
                        if (edsUser != null) {
                            if (StringUtils.isNotBlank(edsUser.getName())) {
                                emailReplyTo.setName(edsUser.getName());
                            }
                            if (edsUser.getPhoto() != null) {
                                emailReplyTo.setAvatar(commonServiceLocal.getImageUrl(edsUser.getPhoto().getObjectID()));
                            }
                        } else {
                            EdsCrmContact crmContact = crmContactManager.getContactByEmail(item.getName(), companyId);
                            if (crmContact != null) {
                                if (StringUtils.isNotBlank(crmContact.getName())) {
                                    emailReplyTo.setName(crmContact.getName());
                                }
                                if (crmContact.getPhoto() != null) {
                                    emailReplyTo.setAvatar(commonServiceLocal.getImageUrl(crmContact.getPhoto().getObjectID()));
                                }
                            } else {
                                EdsCrmContact lead = crmContactManager.getLeadByEmail(item.getName(), companyId);
                                if (lead != null) {
                                    if (StringUtils.isNotBlank(lead.getName())) {
                                        emailReplyTo.setName(lead.getName());
                                    }
                                    if (lead.getPhoto() != null) {
                                        emailReplyTo.setAvatar(commonServiceLocal.getImageUrl(lead.getPhoto().getObjectID()));
                                    }
                                } else {
                                    EdsCrmAccount crmAccount = crmAccountManager.getCrmAccountByEmail(item.getName(), companyId);
                                    if (crmAccount != null) {
                                        if (StringUtils.isNotBlank(crmAccount.getName())) {
                                            emailReplyTo.setName(crmAccount.getName());
                                        }
                                        if (crmAccount.getLogo() != null) {
                                            emailReplyTo.setAvatar(commonServiceLocal.getImageUrl(crmAccount.getLogo().getObjectID()));
                                        }
                                    }
                                }
                            }

                        }
                    }
                    emailReplyTo.setDefault(item.isDefaultSelected());
                    emailsReplyToList.add(emailReplyTo);
                }
            }
        }
        return successResponse(new ResponseListData<>(emailsReplyToList));
    }

    @Operation(summary = "Get Draft Email Information")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have draft email information"),
            @ApiResponse(responseCode = "400", description = "id is required")})
    @RequestMapping(value = "/emails/drafts/{id}", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.CRM_E_MAIL_MARKETING_TAB, PermissionConstants.CRM_MESSAGE_CENTER})
    public Object getEmailDraftInfo(@PathVariable(value = "id") String id) throws RestException {

        if (id == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        /*if (id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id should be more than 0", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }*/
        Integer companyId = userManager.getUser().getCompany().getObjectID();
        EmailSaveEditTO draftEmailInfo = new EmailSaveEditTO();
        Email draftEmail = messageCenterServiceLocal.getEmail(id);
        if (draftEmail != null) {
            //Relations
            if (draftEmail.getRelations() != null && draftEmail.getRelations().size() > 0) {
                ArrayList<LinksTO> links = new ArrayList<>();
                draftEmail.getRelations().forEach(relationItem -> {
                    LinksTO link = new LinksTO();
                    link.setId(relationItem.getToID());
                    link.setName(relationItem.getToName());
                    if (StringUtils.isNotBlank(relationItem.getToType())) {
                        link.setLink_type(getLinkType(relationItem.getToType()));
                    }
                    if (getLinkType(relationItem.getToType()) != null) {
                        links.add(link);
                    }
                });
                if (links.size() > 0) {
                    draftEmailInfo.setLinks(links);
                }
            }
            // from
            EmailSenderTO emailSender = null;
            if (StringUtils.isNotBlank(draftEmail.getFromEmail()) && draftEmail.getFromEmail().startsWith("no-reply")) {
                emailSender = new EmailSenderTO();
                emailSender.setId(0);
                emailSender.setName("no-reply");
                emailSender.setEmail(draftEmail.getFromEmail());
                emailSender.setDefault(true);
            } else {
                EdsUser user = userManager.getUserByEmail(draftEmail.getFromEmail());
                if (user != null) {
                    emailSender = new EmailSenderTO();
                    emailSender.setId(user.getObjectID());
                    if (StringUtils.isNotBlank(user.getName())) {
                        emailSender.setName(user.getName());
                    }
                    if (StringUtils.isNotBlank(user.getEmail())) {
                        emailSender.setEmail(user.getEmail());
                    }
                    if (user.getPhoto() != null) {
                        emailSender.setAvatar(commonServiceLocal.getImageUrl(user.getPhoto().getObjectID()));
                    }
                } else {
                    EdsCrmContact crmContact = crmContactManager.getContactByEmail(draftEmail.getFromEmail(), companyId);
                    if (crmContact != null) {
                        emailSender = new EmailSenderTO();
                        emailSender.setId(crmContact.getObjectID());
                        if (StringUtils.isNotBlank(crmContact.getName())) {
                            emailSender.setName(crmContact.getName());
                        }
                        if (StringUtils.isNotBlank(crmContact.getPrimaryEmail())) {
                            emailSender.setEmail(crmContact.getPrimaryEmail());
                        }
                        if (crmContact.getPhoto() != null) {
                            emailSender.setAvatar(commonServiceLocal.getImageUrl(crmContact.getPhoto().getObjectID()));
                        }
                    } else {
                        EdsCrmContact lead = crmContactManager.getLeadByEmail(draftEmail.getFromEmail(), companyId);
                        if (lead != null) {
                            emailSender = new EmailSenderTO();
                            emailSender.setId(lead.getObjectID());
                            if (StringUtils.isNotBlank(lead.getName())) {
                                emailSender.setName(lead.getName());
                            }
                            if (StringUtils.isNotBlank(lead.getPrimaryEmail())) {
                                emailSender.setEmail(lead.getPrimaryEmail());
                            }
                            if (lead.getPhoto() != null) {
                                emailSender.setAvatar(commonServiceLocal.getImageUrl(lead.getPhoto().getObjectID()));
                            }
                        } else {
                            EdsCrmAccount crmAccount = crmAccountManager.getCrmAccountByEmail(draftEmail.getFromEmail(), companyId);
                            if (crmAccount != null) {
                                emailSender = new EmailSenderTO();
                                emailSender.setId(crmAccount.getObjectID());
                                if (StringUtils.isNotBlank(crmAccount.getName())) {
                                    emailSender.setName(crmAccount.getName());
                                }
                                if (StringUtils.isNotBlank(crmAccount.getEmail())) {
                                    emailSender.setEmail(crmAccount.getEmail());
                                }
                                if (crmAccount.getLogo() != null) {
                                    emailSender.setAvatar(commonServiceLocal.getImageUrl(crmAccount.getLogo().getObjectID()));
                                }

                            }
                        }
                    }

                }
            }
            draftEmailInfo.setFrom(emailSender);
            // To
            if (StringUtils.isNotBlank(draftEmail.getToEmails())) {
                String[] toEmails = draftEmail.getToEmails().split(",");
                ArrayList<EmailSenderTO> emailList = new ArrayList<>();
                for (String toEmail : toEmails) {

                    if (toEmail.contains("<") && toEmail.contains(">") && toEmail.indexOf("<") < toEmail.indexOf(">")) {
                        toEmail = toEmail.substring(toEmail.indexOf("<") + 1, toEmail.indexOf(">")).trim();
                    }

                    EmailSenderTO emailListTo;
                    EdsUser user = userManager.getUserByEmail(toEmail);
                    if (user != null) {
                        emailListTo = new EmailSenderTO();
                        emailListTo.setId(user.getObjectID());
                        if (StringUtils.isNotBlank(user.getName())) {
                            emailListTo.setName(user.getName());
                        }
                        if (StringUtils.isNotBlank(user.getEmail())) {
                            emailListTo.setEmail(user.getEmail());
                        }
                        if (user.getPhoto() != null) {
                            emailListTo.setAvatar(commonServiceLocal.getImageUrl(user.getPhoto().getObjectID()));
                        }
                        emailList.add(emailListTo);
                    } else {
                        EdsCrmContact crmContact = crmContactManager.getContactByEmail(toEmail, companyId);
                        if (crmContact != null) {
                            emailListTo = new EmailSenderTO();
                            emailListTo.setId(crmContact.getObjectID());
                            if (StringUtils.isNotBlank(crmContact.getName())) {
                                emailListTo.setName(crmContact.getName());
                            }
                            if (StringUtils.isNotBlank(crmContact.getPrimaryEmail())) {
                                emailListTo.setEmail(crmContact.getPrimaryEmail());
                            }
                            if (crmContact.getPhoto() != null) {
                                emailListTo.setAvatar(commonServiceLocal.getImageUrl(crmContact.getPhoto().getObjectID()));
                            }
                            emailList.add(emailListTo);
                        } else {
                            EdsCrmContact lead = crmContactManager.getLeadByEmail(toEmail, companyId);
                            if (lead != null) {
                                emailListTo = new EmailSenderTO();
                                emailListTo.setId(lead.getObjectID());
                                if (StringUtils.isNotBlank(lead.getName())) {
                                    emailListTo.setName(lead.getName());
                                }
                                if (StringUtils.isNotBlank(lead.getPrimaryEmail())) {
                                    emailListTo.setEmail(lead.getPrimaryEmail());
                                }
                                if (lead.getPhoto() != null) {
                                    emailListTo.setAvatar(commonServiceLocal.getImageUrl(lead.getPhoto().getObjectID()));
                                }
                                emailList.add(emailListTo);
                            } else {
                                EdsCrmAccount crmAccount = crmAccountManager.getCrmAccountByEmail(toEmail, companyId);
                                if (crmAccount != null) {
                                    emailListTo = new EmailSenderTO();
                                    emailListTo.setId(crmAccount.getObjectID());
                                    if (StringUtils.isNotBlank(crmAccount.getName())) {
                                        emailListTo.setName(crmAccount.getName());
                                    }
                                    if (StringUtils.isNotBlank(crmAccount.getEmail())) {
                                        emailListTo.setEmail(crmAccount.getEmail());
                                    }
                                    if (crmAccount.getLogo() != null) {
                                        emailListTo.setAvatar(commonServiceLocal.getImageUrl(crmAccount.getLogo().getObjectID()));
                                    }
                                    emailList.add(emailListTo);
                                }
                            }
                        }

                    }
                    draftEmailInfo.setTo(emailList);
                }
            }

            // reply_to
            if (StringUtils.isNotBlank(draftEmail.getFromEmail()) && !draftEmail.getFromEmail().startsWith("no-reply")) {
                EmailSenderTO emailReply = null;
                EdsUser user = userManager.getUserByEmail(draftEmail.getFromEmail());
                if (user != null) {
                    emailReply = new EmailSenderTO();
                    emailReply.setId(user.getObjectID());
                    if (StringUtils.isNotBlank(user.getName())) {
                        emailReply.setName(user.getName());
                    }
                    if (StringUtils.isNotBlank(user.getEmail())) {
                        emailReply.setEmail(user.getEmail());
                    }
                    if (user.getPhoto() != null) {
                        emailReply.setAvatar(commonServiceLocal.getImageUrl(user.getPhoto().getObjectID()));
                    }
                } else {
                    EdsCrmContact crmContact = crmContactManager.getContactByEmail(draftEmail.getFromEmail(), companyId);
                    if (crmContact != null) {
                        emailReply = new EmailSenderTO();
                        emailReply.setId(crmContact.getObjectID());
                        if (StringUtils.isNotBlank(crmContact.getName())) {
                            emailReply.setName(crmContact.getName());
                        }
                        if (StringUtils.isNotBlank(crmContact.getPrimaryEmail())) {
                            emailReply.setEmail(crmContact.getPrimaryEmail());
                        }
                        if (crmContact.getPhoto() != null) {
                            emailReply.setAvatar(commonServiceLocal.getImageUrl(crmContact.getPhoto().getObjectID()));
                        }
                    } else {
                        EdsCrmContact lead = crmContactManager.getLeadByEmail(draftEmail.getFromEmail(), companyId);
                        if (lead != null) {
                            emailReply = new EmailSenderTO();
                            emailReply.setId(lead.getObjectID());
                            if (StringUtils.isNotBlank(lead.getName())) {
                                emailReply.setName(lead.getName());
                            }
                            if (StringUtils.isNotBlank(lead.getPrimaryEmail())) {
                                emailReply.setEmail(lead.getPrimaryEmail());
                            }
                            if (lead.getPhoto() != null) {
                                emailReply.setAvatar(commonServiceLocal.getImageUrl(lead.getPhoto().getObjectID()));
                            }
                        } else {
                            EdsCrmAccount crmAccount = crmAccountManager.getCrmAccountByEmail(draftEmail.getFromEmail(), companyId);
                            if (crmAccount != null) {
                                emailReply = new EmailSenderTO();
                                emailReply.setId(crmAccount.getObjectID());
                                if (StringUtils.isNotBlank(crmAccount.getName())) {
                                    emailReply.setName(crmAccount.getName());
                                }
                                if (StringUtils.isNotBlank(crmAccount.getEmail())) {
                                    emailReply.setEmail(crmAccount.getEmail());
                                }
                                if (crmAccount.getLogo() != null) {
                                    emailReply.setAvatar(commonServiceLocal.getImageUrl(crmAccount.getLogo().getObjectID()));
                                }
                            }
                        }
                    }

                }
                draftEmailInfo.setReply_to(emailReply);
            }
            // list_cc
            if (StringUtils.isNotBlank(draftEmail.getCc())) {
                ArrayList<EmailSenderTO> emailList = new ArrayList<>();
                String[] mailCCs = draftEmail.getCc().split(",");
                for (String mailCC : mailCCs) {

                    if (mailCC.contains("<") && mailCC.contains(">") && mailCC.indexOf("<") < mailCC.indexOf(">")) {
                        mailCC = mailCC.substring(mailCC.indexOf("<") + 1, mailCC.indexOf(">")).trim();
                    }

                    EmailSenderTO emailCC;
                    EdsUser user = userManager.getUserByEmail(mailCC);
                    if (user != null) {
                        emailCC = new EmailSenderTO();
                        emailCC.setId(user.getObjectID());
                        if (StringUtils.isNotBlank(user.getName())) {
                            emailCC.setName(user.getName());
                        }
                        if (StringUtils.isNotBlank(user.getEmail())) {
                            emailCC.setEmail(user.getEmail());
                        }
                        if (user.getPhoto() != null) {
                            emailCC.setAvatar(commonServiceLocal.getImageUrl(user.getPhoto().getObjectID()));
                        }
                        emailList.add(emailCC);
                    } else {
                        EdsCrmContact crmContact = crmContactManager.getContactByEmail(mailCC, companyId);
                        if (crmContact != null) {
                            emailCC = new EmailSenderTO();
                            emailCC.setId(crmContact.getObjectID());
                            if (StringUtils.isNotBlank(crmContact.getName())) {
                                emailCC.setName(crmContact.getName());
                            }
                            if (StringUtils.isNotBlank(crmContact.getPrimaryEmail())) {
                                emailCC.setEmail(crmContact.getPrimaryEmail());
                            }
                            if (crmContact.getPhoto() != null) {
                                emailCC.setAvatar(commonServiceLocal.getImageUrl(crmContact.getPhoto().getObjectID()));
                            }
                            emailList.add(emailCC);
                        } else {
                            EdsCrmContact lead = crmContactManager.getLeadByEmail(mailCC, companyId);
                            if (lead != null) {
                                emailCC = new EmailSenderTO();
                                emailCC.setId(lead.getObjectID());
                                if (StringUtils.isNotBlank(lead.getName())) {
                                    emailCC.setName(lead.getName());
                                }
                                if (StringUtils.isNotBlank(lead.getPrimaryEmail())) {
                                    emailCC.setEmail(lead.getPrimaryEmail());
                                }
                                if (lead.getPhoto() != null) {
                                    emailCC.setAvatar(commonServiceLocal.getImageUrl(lead.getPhoto().getObjectID()));
                                }
                                emailList.add(emailCC);
                            } else {
                                EdsCrmAccount crmAccount = crmAccountManager.getCrmAccountByEmail(mailCC, companyId);
                                if (crmAccount != null) {
                                    emailCC = new EmailSenderTO();
                                    emailCC.setId(crmAccount.getObjectID());
                                    if (StringUtils.isNotBlank(crmAccount.getName())) {
                                        emailCC.setName(crmAccount.getName());
                                    }
                                    if (StringUtils.isNotBlank(crmAccount.getEmail())) {
                                        emailCC.setEmail(crmAccount.getEmail());
                                    }
                                    if (crmAccount.getLogo() != null) {
                                        emailCC.setAvatar(commonServiceLocal.getImageUrl(crmAccount.getLogo().getObjectID()));
                                    }
                                    emailList.add(emailCC);
                                }
                            }
                        }

                    }
                }
                if (emailList.size() > 0) {
                    draftEmailInfo.setList_cc(emailList);
                }
            }
            //list_bcc
            if (StringUtils.isNotBlank(draftEmail.getBcc())) {
                ArrayList<EmailSenderTO> emailBccList = new ArrayList<>();
                String[] mailBCCs = draftEmail.getBcc().split(",");
                for (String mailBCC : mailBCCs) {

                    if (mailBCC.contains("<") && mailBCC.contains(">") && mailBCC.indexOf("<") < mailBCC.indexOf(">")) {
                        mailBCC = mailBCC.substring(mailBCC.indexOf("<") + 1, mailBCC.indexOf(">")).trim();
                    }


                    EmailSenderTO emailCC;
                    EdsUser user = userManager.getUserByEmail(mailBCC);
                    if (user != null) {
                        emailCC = new EmailSenderTO();
                        emailCC.setId(user.getObjectID());
                        if (StringUtils.isNotBlank(user.getName())) {
                            emailCC.setName(user.getName());
                        }
                        if (StringUtils.isNotBlank(user.getEmail())) {
                            emailCC.setEmail(user.getEmail());
                        }
                        if (user.getPhoto() != null) {
                            emailCC.setAvatar(commonServiceLocal.getImageUrl(user.getPhoto().getObjectID()));
                        }
                        emailBccList.add(emailCC);
                    } else {
                        EdsCrmContact crmContact = crmContactManager.getContactByEmail(mailBCC, companyId);
                        if (crmContact != null) {
                            emailCC = new EmailSenderTO();
                            emailCC.setId(crmContact.getObjectID());
                            if (StringUtils.isNotBlank(crmContact.getName())) {
                                emailCC.setName(crmContact.getName());
                            }
                            if (StringUtils.isNotBlank(crmContact.getPrimaryEmail())) {
                                emailCC.setEmail(crmContact.getPrimaryEmail());
                            }
                            if (crmContact.getPhoto() != null) {
                                emailCC.setAvatar(commonServiceLocal.getImageUrl(crmContact.getPhoto().getObjectID()));
                            }
                            emailBccList.add(emailCC);
                        } else {
                            EdsCrmContact lead = crmContactManager.getLeadByEmail(mailBCC, companyId);
                            if (lead != null) {
                                emailCC = new EmailSenderTO();
                                emailCC.setId(lead.getObjectID());
                                if (StringUtils.isNotBlank(lead.getName())) {
                                    emailCC.setName(lead.getName());
                                }
                                if (StringUtils.isNotBlank(lead.getPrimaryEmail())) {
                                    emailCC.setEmail(lead.getPrimaryEmail());
                                }
                                if (lead.getPhoto() != null) {
                                    emailCC.setAvatar(commonServiceLocal.getImageUrl(lead.getPhoto().getObjectID()));
                                }
                                emailBccList.add(emailCC);
                            } else {
                                EdsCrmAccount crmAccount = crmAccountManager.getCrmAccountByEmail(mailBCC, companyId);
                                if (crmAccount != null) {
                                    emailCC = new EmailSenderTO();
                                    emailCC.setId(crmAccount.getObjectID());
                                    if (StringUtils.isNotBlank(crmAccount.getName())) {
                                        emailCC.setName(crmAccount.getName());
                                    }
                                    if (StringUtils.isNotBlank(crmAccount.getEmail())) {
                                        emailCC.setEmail(crmAccount.getEmail());
                                    }
                                    if (crmAccount.getLogo() != null) {
                                        emailCC.setAvatar(commonServiceLocal.getImageUrl(crmAccount.getLogo().getObjectID()));
                                    }
                                    emailBccList.add(emailCC);
                                }
                            }
                        }

                    }
                }
                if (emailBccList.size() > 0) {
                    draftEmailInfo.setList_bcc(emailBccList);
                }
            }
            if (StringUtils.isNotBlank(draftEmail.getSubject())) {
                draftEmailInfo.setSubject(draftEmail.getSubject());
            }
            if (StringUtils.isNotBlank(draftEmail.getContent())) {
                Document htmlDocument = Jsoup.parse(draftEmail.getContent().trim());
                if (htmlDocument != null) {
                    if (htmlDocument.text().length() > 150) {
                        draftEmailInfo.setContent(htmlDocument.text().substring(0, 150));
                    } else {
                        draftEmailInfo.setContent(htmlDocument.text());
                    }
                }
                draftEmailInfo.setHtml_data(draftEmail.getContent());
            }

            if (draftEmail.getAttachments() != null && draftEmail.getAttachments().size() > 0) {
                ArrayList<EmailAttachmentsTO> emailAttachments = new ArrayList<>();
                for (FileResource fileResource : draftEmail.getAttachments()) {
                    EmailAttachmentsTO emailAttachment = new EmailAttachmentsTO();
                    emailAttachment.setItem_id(fileResource.getBodyId());
                    if (StringUtils.isNotBlank(fileResource.getFileName())) {
                        emailAttachment.setFile_name(fileResource.getFileName());
                    }
                    if (StringUtils.isNotBlank(fileResource.getDownloadUrl())) {
                        emailAttachment.setLink(fileResource.getDownloadUrl());
                    }
                    emailAttachment.setFile_size(ServerUtils.getSizeAsString(fileResource.getContentLength()));
                    EdsAttachment attachment = attachmentManager.get(fileResource.getBodyId());
                    if (attachment != null && attachment.getCreationTime() != null) {
                        emailAttachment.setUpload_date(longDateTimezoneFormat.format(attachment.getCreationTime()));
                    }
                    emailAttachments.add(emailAttachment);
                }
                draftEmailInfo.setDraft_attachments(emailAttachments);
            }

            return successResponse(draftEmailInfo);
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Draft Email with".concat(id).concat(" is not found"), NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }


    @Operation(summary = "Send new email to server", description = "Request to Send new email to server")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Sends new email to server")})
    @RequestMapping(value = "/emails", method = {RequestMethod.PUT, RequestMethod.POST},
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_E_MAIL_MARKETING_TAB, PermissionConstants.CRM_MESSAGE_CENTER})
    public Object sendEmail(MultipartRequest multipartRequest, @RequestParam(name = "body") String jsonString) throws RestException {
        return saveEmail(multipartRequest, jsonString, null, ApiActionEnum.SEND.name());
    }

    private Object saveEmail(MultipartRequest multipartRequest, String jsonString, String draft_id, String action) throws RestException {

        SendEmailDataTO draftEmailAdd;
        EdsUser user = userManager.getUser();
        ObjectMapper mapper = new ObjectMapper();
        try {
            draftEmailAdd = mapper.readValue(jsonString, SendEmailDataTO.class);
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "JSON body format is wrong.".concat(e.getMessage()), REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (ApiActionEnum.SEND.name().equalsIgnoreCase(action)) {
            if (StringUtils.isBlank(draftEmailAdd.getData().getSubject())) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "email subject is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (StringUtils.isBlank(draftEmailAdd.getData().getContent())) {
                throw new RestException("Email content is required", "email content is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (draftEmailAdd.getData().getTo() == null || draftEmailAdd.getData().getTo().size() == 0) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "email receiver is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (draftEmailAdd.getData().getTo() != null || draftEmailAdd.getData().getTo().size() > 0) {
                for (EmailSenderTO to : draftEmailAdd.getData().getTo()) {
                    if (StringUtils.isBlank(to.getEmail()) || !EMAIL_PATTERN.matcher(to.getEmail()).matches()) {
                        throw new RestException(GENERAL_ERROR_MESSAGE, "valid email receiver is required", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
                    }
                }
            }
        }

        Email newEmail = new Email();
        newEmail.setObjectID(draft_id);
        if (draftEmailAdd.getData().getLinks() != null && draftEmailAdd.getData().getLinks().size() > 0) {
            ArrayList<RelationItem> relationItems = new ArrayList<>();
            for (LinkTO link : draftEmailAdd.getData().getLinks()) {
                RelationItem relationItem = new RelationItem();
                relationItem.setFromType(RelationItem.TYPE_EMAIL_TRACKER);
                relationItem.setToID(link.getItem_id());
                relationItem.setToName(link.getName());
                relationItem.setToType(getEntityRelation(link.getLink_type()));
                relationItems.add(relationItem);
                EdsCrmContact lead = crmContactManager.get(link.getItem_id());
                if (lead != null && lead.getCrmAccount() != null) {
                    RelationItem leadRelationItem = new RelationItem();
                    leadRelationItem.setFromType(RelationItem.TYPE_EMAIL_TRACKER);
                    leadRelationItem.setToID(lead.getCrmAccount().getObjectID());
                    leadRelationItem.setToName(lead.getCrmAccount().getName());
                    leadRelationItem.setToType(RelationItem.TYPE_CRM_ACCOUNT);
                    relationItems.add(leadRelationItem);
                }
            }
            newEmail.setRelations(relationItems);
        }
        if (draftEmailAdd.getData().getFrom() != null) {
            newEmail.setFromEmail(draftEmailAdd.getData().getFrom().getEmail());
        }
        if (draftEmailAdd.getData().getTo() != null && draftEmailAdd.getData().getTo().size() > 0) {
            newEmail.setToEmails(loadMultipleContacts(draftEmailAdd.getData().getTo()));
        }
        if (draftEmailAdd.getData().getList_cc() != null && draftEmailAdd.getData().getList_cc().size() > 0) {
            newEmail.setCc(loadMultipleContacts(draftEmailAdd.getData().getList_cc()));
        }
        if (draftEmailAdd.getData().getList_bcc() != null && draftEmailAdd.getData().getList_bcc().size() > 0) {
            newEmail.setBcc(loadMultipleContacts(draftEmailAdd.getData().getList_bcc()));
        }
        if (StringUtils.isNotBlank(draftEmailAdd.getData().getSubject())) {
            newEmail.setSubject(draftEmailAdd.getData().getSubject());
        }
        if (StringUtils.isNotBlank(draftEmailAdd.getData().getContent())) {
            newEmail.setContent(draftEmailAdd.getData().getContent());
        }
        if (multipartRequest != null && multipartRequest.getFileMap() != null && multipartRequest.getFileMap().size() > 0) {
            ArrayList<FileResource> emailAttachments = new ArrayList<>();
            for (MultipartFile file : multipartRequest.getFileMap().values()) {
                if (file.getName().matches(attachmentNameRegex)) {
                    CreateDocumentCommand documentCommand = new CreateDocumentCommand();
                    documentCommand.setImgType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1).toLowerCase(Locale.ENGLISH));
                    documentCommand.setCompanyID(user.getCompany().getObjectID());
                    documentCommand.setNotdownloadable("YES");
                    WfmMultipartFile multipartFile = new WfmMultipartFile("", file);
                    documentCommand.addFile(multipartFile);
                    try {
                        CreateAttachmentHandler createAttachmentHandler = (CreateAttachmentHandler) ApplicationContextProvider.applicationContext.getBean("createAttachmentHandler");
                        createAttachmentHandler.execute(documentCommand);
                        String[] result = createAttachmentHandler.getResult();
                        if (result != null && result.length > 0) {
                            ArrayList<FileResource> attachmentList = messageCenterServiceLocal.getAttachedFilesByAttachmentId(Integer.valueOf(result[0]));
                            if (attachmentList != null && attachmentList.size() > 0) {
                                emailAttachments.addAll(attachmentList);
                            }
                        }
                    } catch (Throwable throwable) {
                        log.error(throwable.getMessage());
                    }
                }
            }

            newEmail.setAttachments(emailAttachments);

        }

        try {
            if (ApiActionEnum.DRAFT.getCode().equalsIgnoreCase(action) || ApiActionEnum.EDIT.getCode().equalsIgnoreCase(action)) {
                messageCenterServiceLocal.saveAsDraft(newEmail);
            } else {
                messageCenterServiceLocal.sendMessage(newEmail);
            }
            return successResponse(new ResponseData());
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Edit email draft on server", description = "Request to edit email draft on server")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Edits email draft on server")})
    @RequestMapping(value = "/emails/drafts/{draft_id}", method = {RequestMethod.PUT, RequestMethod.POST},
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_E_MAIL_MARKETING_TAB, PermissionConstants.CRM_MESSAGE_CENTER})
    public Object editDraftEmail(MultipartRequest multipartRequest, @RequestParam(name = "body") String jsonString,
                                 @PathVariable(value = "draft_id") String draft_id) throws RestException {
        if (draft_id == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "draft email id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        return saveEmail(multipartRequest, jsonString, draft_id, ApiActionEnum.EDIT.name());
    }

    @Operation(summary = "Save email draft on server", description = "Request to save email draft on server")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Saves email draft on server")})
    @RequestMapping(value = "/emails/drafts", method = {RequestMethod.PUT, RequestMethod.POST},
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_E_MAIL_MARKETING_TAB, PermissionConstants.CRM_MESSAGE_CENTER})
    public Object saveDraftEmail(MultipartRequest multipartRequest, @RequestParam(name = "body") String jsonString) throws RestException {
        return saveEmail(multipartRequest, jsonString, null, ApiActionEnum.DRAFT.name());
    }

    private String loadMultipleContacts(ArrayList<EmailSenderTO> items) {
        String mails = "";
        for (EmailSenderTO item : items) {
            if (StringUtils.isNotBlank(item.getEmail())) {
                mails = mails.concat(item.getEmail()).concat(",");
            }
        }
        if (StringUtils.isNotBlank(mails)) {
            return mails.substring(0, mails.length() - 1);
        }
        return null;
    }

    @Operation(summary = "Test Email Account Connection", description = "Request to test email account connection")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/test_connection", method = RequestMethod.POST)
    public Object testConnection(@RequestBody EmailAccountItem emailAccount) throws RestException {
        Integer result;
        try {
            result = messageCenterServiceLocal.testConnection(emailAccount);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException("Couldn't Connect To the server.", e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (result == 0) {
            return successResponse(new ResponseData());
        }
        if (EmailAccountItem.ERROR_CREDENTIAL == result) {
            throw new RestException("Can't connect to mail server using provided Username and password, please check your login details.", GENERAL_ERROR_MESSAGE, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (EmailAccountItem.ERROR_COULDNOTCONNECT == result || EmailAccountItem.ERROR_UNKNOWNHOSTEXCEPTION == result || EmailAccountItem.ERROR_CONNECTIONTIMEDOUT == result) {
            throw new RestException("Couldn't Connect To the server.", GENERAL_ERROR_MESSAGE, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (EmailAccountItem.ERROR_CONNECTIONREFUSED == result) {
            throw new RestException("Connection Refused, the protocol you specified, is not enabled in your email server.", GENERAL_ERROR_MESSAGE, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (EmailAccountItem.ERROR_CANTSENDEMAIL == result) {
            throw new RestException("Couldn't Connect To Outgoing server.", GENERAL_ERROR_MESSAGE, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (EmailAccountItem.SETUP_CORPORATE_EMAIL_NOT_SETUP_USER_EMAIL == result) {
            throw new RestException("The email you entered is already used for corporate email account", GENERAL_ERROR_MESSAGE, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (EmailAccountItem.SETUP_USER_EMAIL_NOT_SETUP_CORPORATE_EMAIL == result) {
            throw new RestException("The email you entered is already used for user email account", GENERAL_ERROR_MESSAGE, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (result > EmailAccountItem.ERROR_SMTP_SERVER && result != EmailAccountItem.ERROR_CREDENTIAL_CHECK_BROWSER) {
            result = result - EmailAccountItem.ERROR_SMTP_SERVER;
            if (EmailAccountItem.ERROR_CREDENTIAL == result) {
                throw new RestException("Can't connect to mail Outgoing server using provided Username and password, please check your login details.", GENERAL_ERROR_MESSAGE, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (EmailAccountItem.ERROR_COULDNOTCONNECT == result || EmailAccountItem.ERROR_UNKNOWNHOSTEXCEPTION == result || EmailAccountItem.ERROR_CONNECTIONTIMEDOUT == result) {
                throw new RestException("Couldn't Connect To Outgoing server.", GENERAL_ERROR_MESSAGE, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (EmailAccountItem.ERROR_CONNECTIONREFUSED == result) {
                throw new RestException("Connection Refused, the Outgoing server protocol you specified, is not enabled in your email server.", GENERAL_ERROR_MESSAGE, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        if (result == EmailAccountItem.ERROR_CREDENTIAL_CHECK_BROWSER) {
            throw new RestException("Please log in via your web browser: https://support.google.com/mail/accounts/answer/78754", GENERAL_ERROR_MESSAGE, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        throw new RestException("Some errors occurred while configuring your settings.", GENERAL_ERROR_MESSAGE, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Operation(summary = "Create Email Account", description = "Request to create email account")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/email_account", method = RequestMethod.POST)
    public Object saveEmailAccount(@RequestBody EmailAccountItem emailAccount) throws RestException {
        Integer result;
        try {
            result = messageCenterServiceLocal.saveEmailAccount(emailAccount);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(e.getMessage(), GENERAL_ERROR_MESSAGE, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (result == 0) {
            return successResponse(new ResponseData());
        }
        throw new RestException("Account with this email address already exists.", GENERAL_ERROR_MESSAGE, CONFLICT, HttpStatus.CONFLICT);
    }

}
