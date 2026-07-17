/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/4/29 8:56:26                                                                                            *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsAttachment;
import com.edatasite.workforce.core.domain.EdsReference;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 01-Jun-2009
 * Time: 18:09:20
 * To change this template use File | Settings | File Templates.
 */
public interface AttachmentManager extends Manager<EdsAttachment> {

    List<EdsAttachment> getAttachmentsByAttachmentId(Integer attachmentId);

    List<EdsAttachment> getEmploymentAttachmentsById(Integer employmentId);

    List<EdsAttachment> getCaseAttachments(Integer caseTrackerID);

    List<EdsAttachment> getAttachments(Integer id, EdsReference categoryType);

    List<EdsAttachment> getCompanyCaseAttachments();
}
