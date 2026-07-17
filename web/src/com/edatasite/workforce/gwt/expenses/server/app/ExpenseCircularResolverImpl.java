/*
package com.edatasite.workforce.gwt.expenses.server.app;

import com.edatasite.workforce.core.domain.EdsExpense;
import com.edatasite.workforce.core.domain.EdsExpenseHistory;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.ExpenseHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

*/
/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 01.12.2008
 * Time: 16:34:39
 *//*

@Service("expenseCircularResolver")
public class ExpenseCircularResolverImpl implements ExpenseCircularResolver, Constants {

    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private ExpenseHistoryManager historyManager;
    @Autowired
    private ExpenseManager expenseManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FileItem[] getAttachments(Integer expenseId) {
        EdsExpense expense = expenseManager.get(expenseId);
        List<FileResource> expAttachments = attachmentUtilsManager.getAttachments(F_EXP, expense.getObjectID(), expense.getObjectID());
        FileItem[] fileItems = new FileItem[expAttachments.size()];
        for (int i = 0; i < expAttachments.size(); i++) {
            FileResource fileResource = expAttachments.get(i);
            FileItem fileItem = new FileItem();
            fileItem.setId(fileResource.getObjectId());
            fileItem.setAttachmentId(fileResource.getBodyId());
            fileItem.setFileName(fileResource.getEncodedName());
            fileItem.setDescription(fileResource.getDescription());
            fileItem.setSize(fileResource.getContentLength());
            fileItem.setUploadType(fileResource.getUploadType());
            fileItem.setDate(fileResource.getCreationDate());
            switch (fileResource.getUploadType()) {
                case GOOGLE:
                    fileItem.setGoogleDocumentLink(fileResource.getGoogleDownloadLink());
                    break;
                case OFFICE_365:
                case OFFICE_365_SHARE_POINT:
                    fileItem.setDocumentID(fileResource.getDocumentID());
                    fileItem.setDocumentOpenID(fileResource.getDocumentOpenID());
                    fileItem.setOfficeDocumentLink(fileResource.getOfficeDownloadLink());
                    break;
                default:
                    fileItem.setAmazonLink(fileResource.getAmazonLink());
                    break;
            }
            fileItems[i] = fileItem;
        }
        return fileItems;
    }

    public FileResource[] getFileResources(Integer expenseID) {
        List<FileResource> expAttachments = attachmentUtilsManager.getAttachments(F_EXP, expenseID, expenseID);
        return expAttachments.toArray(new FileResource[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EdsExpenseHistory getEventLastHistoryRecord(String statusCode, Integer reportId) {

        EdsReference reference = referenceManager.findReference(Constants.EXPENSE_STATUS, statusCode);
        return historyManager.getLastEventRecord(reference, reportId);
    }
}
*/
