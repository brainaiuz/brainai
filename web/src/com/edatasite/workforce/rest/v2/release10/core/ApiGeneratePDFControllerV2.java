package com.edatasite.workforce.rest.v2.release10.core;

import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.accounting.EdsBatchPayment;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransactionPDFObject;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.BatchPaymentManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.IPostPDFHandler;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.to.base.GeneratePdfTO;
import com.edatasite.workforce.rest.v2.release10.enums.EntityTypeEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.Optional;


@Tag(name = "PDF API", description = "Generate PDF API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiGeneratePDFControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiGeneratePDFControllerV2.class);
    @Autowired
    protected BatchPaymentManager batchPaymentManager;
    @Autowired
    @Qualifier("taskViewPDFHandler")
    private IPostPDFHandler taskViewPDFHandler;
    @Autowired
    @Qualifier("batchReceivePaymentViewPDFHandler")
    private IPostPDFHandler batchReceivePaymentViewPDFHandler;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;

    @Operation(summary = "Generate PDF for some entity", description = "PDF linked to Task, Lead, Opportunity...")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/{main_entity_path}/{entity_id}/generate_pdf", method = RequestMethod.GET)
    @Transactional
    public Object generatePDF(@PathVariable(value = "main_entity_path") String entityType,
                              @PathVariable(value = "entity_id") Integer entityId) throws RestException {

        if (StringUtils.isBlank(entityType)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "main_entity_path is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (entityId == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "entity_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        ByteArrayOutputStream pdfStream;
        String filename = "";
        if (EntityTypeEnum.TASKS.name().equalsIgnoreCase(entityType)) {
            EdsTask edsTask = taskManager.get(entityId);
            if (edsTask == null) {
                throw new RestException("Task is not found.", "Task with id " + entityId + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            pdfStream = taskViewPDFHandler.getPDFStream(new RequestObject(entityId, null, true));
            filename = ServerUtils.normalizeFileNameT(Optional.ofNullable(edsTask.getNumber()).orElse(edsTask.getName()).concat(".pdf"));

        } else if (EntityTypeEnum.PAYMENT.name().equalsIgnoreCase(entityType)) {
            EdsBatchPayment edsBatchPayment = batchPaymentManager.get(entityId);
            if (edsBatchPayment == null) {
                throw new RestException("Payment is not found.", "Payment with id " + entityId + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            pdfStream = batchReceivePaymentViewPDFHandler.getPDFStream(new TransactionPDFObject(entityId, null, Constants.RECEIVABLE, null));
            filename = ServerUtils.normalizeFileNameT(Optional.ofNullable(edsBatchPayment.getNumber()).orElse(edsBatchPayment.getName()).concat(".pdf"));
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "main_entity_path should be one of | tasks | projects | opportunities | leads | companies | contacts | activities | payment", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return successResponse(createPDFUploadFile(filename, pdfStream));
    }

    private GeneratePdfTO createPDFUploadFile(String filename, ByteArrayOutputStream pdfStream) {
        EdsUpload edsUpload = new EdsUpload();
        edsUpload.setContentType("application/pdf");

        edsUpload.setOriginalName(filename);
        edsUpload.setInputStream(new ByteArrayInputStream(pdfStream.toByteArray()));
        uploadManager.create(edsUpload);
        String fileUrl = commonServiceLocal.getFileUrl(edsUpload.getObjectID());

        GeneratePdfTO generatePdfTO = new GeneratePdfTO();
        generatePdfTO.setFile_url(fileUrl);
        return generatePdfTO;
    }
}
