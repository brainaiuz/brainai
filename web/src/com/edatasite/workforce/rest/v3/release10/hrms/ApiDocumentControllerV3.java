package com.edatasite.workforce.rest.v3.release10.hrms;

import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ConvertUtils;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.EmployeeDocsTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Tag(name = "Documents", description = "Documents Public API")
@RestController
@RequestMapping(path = "/documents", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
public class ApiDocumentControllerV3 {
    private static final Logger log = LoggerFactory.getLogger(ApiDocumentControllerV3.class);
    private final DocumentsService documentsService;

    public ApiDocumentControllerV3(DocumentsService documentsService) {
        this.documentsService = documentsService;
    }

    @GetMapping(path = "/companies", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<ListResultTO<AttachmentTO>> getCompanyDocuments() {
        log.info("REST request to get all company documents");
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setFolderType(Constants.F_COMPANY_DOCUMENTS);
        fp.setModule(LayoutRPC.HRMS_SECTION);
        fp.setEntityID(null);
        fp.setCrmEntityId(null);
        ListResult<FileResource> companyDocs;
        try {
            companyDocs = documentsService.listFile(fp);
        } catch (ObjectNotFoundException e) {
            throw new RuntimeException(e);
        }
        ListResultTO<AttachmentTO> reponse = new ListResultTO<>(companyDocs.getTotal(), ConvertUtils.toDto(companyDocs.getList()));
        return ResultTO.success(reponse);
    }

    @GetMapping(path = "/employees", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<ListResultTO<EmployeeDocsTO>> getEmployeeDocuments() {
        log.info("REST request to get all employee documents");
        Integer userId = SecurityContext.getInstance().getStaticUserID();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setFolderType(Constants.F_EMPLOYEE_PROFILE);
        fp.setEntityID(userId);
        if (ServerUtils.hasPermission(PermissionConstants.VIEW_ALL_EMPLOYEE_DOCUMENTS)) {
            fp.setHasFullListAccess(true);
        } else {
            fp.setCrmEntityId(userId);
        }
        fp.setModule(LayoutRPC.HRMS_SECTION);
        ListResult<FileResource> employeeDocs;
        try {
            employeeDocs = documentsService.getDocumentList(fp);
        } catch (ObjectNotFoundException e) {
            throw new RuntimeException(e);
        }
        employeeDocs.getList().forEach(e -> e.setAmazonLink(documentsService.getFileLink(e.getBodyId())));
        ArrayList<EmployeeDocsTO> docsItems = ConvertUtils.toEmpDocsTo(employeeDocs.getList());
        ListResultTO<EmployeeDocsTO> response = new ListResultTO<>(employeeDocs.getTotal(), docsItems);
        return ResultTO.success(response);
    }
}
