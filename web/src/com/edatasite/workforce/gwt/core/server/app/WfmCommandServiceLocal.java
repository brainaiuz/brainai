package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.gwt.core.server.actions.BankAccountDocumentCommand;
import com.edatasite.workforce.gwt.core.server.actions.CreateDocumentCommand;
import com.edatasite.workforce.gwt.core.server.actions.ProductCategoryDocumentCommand;
import com.edatasite.workforce.gwt.core.server.actions.ProductDocumentCommand;
import com.edatasite.workforce.gwt.core.server.servlets.WfmCommand;
import com.edatasite.workforce.gwt.task.server.actions.CreateTaskCommentCommand;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 5/31/11
 * Time: 7:46 PM
 * To change this template use File | Settings | File Templates.
 */
public interface WfmCommandServiceLocal {

    String[] createLogoHandler(CreateDocumentCommand createDocumentCommand) throws Throwable;

    String[] createAttendanceReportUploadHandler(WfmCommand wfmCommand) throws Throwable;

    String[] createChartOfAccountsBackendHandler(WfmCommand wfmCommand) throws Throwable;

    String[] createChartOfAccountsHandler(WfmCommand wfmCommand) throws Throwable;

    String[] createAttachmentHandler(CreateDocumentCommand documentCommand) throws Throwable;

    String[] createNetworkAttachmentHandler(CreateDocumentCommand documentCommand) throws Throwable;

    String[] createProductCategoryPicturesHandler(ProductCategoryDocumentCommand documentCommand) throws Throwable;

    String[] createProductPicturesHandler(ProductDocumentCommand documentCommand) throws Throwable;

    void createTaskCommentHandler(CreateTaskCommentCommand commentCommand) throws Throwable;

    /*String[] createWfpAttachmentsHandler(WfpDocumentCommand documentCommand) throws Throwable;*/

    String[] importBankTransactionsHandler(BankAccountDocumentCommand documentCommand) throws Throwable;

    String[] createReportingExcelTemplateAttachmentHandler(WfmCommand documentCommand) throws Throwable;

    void mSProjectFileUploadHandler(WfmCommand command) throws Throwable;

    String[] importTestResultsUploadHandler(WfmCommand documentCommand) throws Throwable;

    String putFileToPublicFolder(EdsUpload upload, InputStream stream) throws Throwable;

    String copyUploadDocumentSize();

    String[] uploadProductSerials(WfmCommand command) throws Throwable;

    String[] createAttendanceReportHoursUploadHandler(WfmCommand document) throws Throwable;

}
