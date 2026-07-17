/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/4/29 8:56:26                                                                                            *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 03-Jun-2009
 * Time: 20:35:04
 * To change this template use File | Settings | File Templates.
 */
public class Attachments implements IsSerializable {

    private FileItem[] attachments;
    private boolean isEmploymentAtt;
    private Integer objectID;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public boolean isEmploymentAtt() {
        return isEmploymentAtt;
    }

    public void setEmploymentAtt(boolean employmentAtt) {
        isEmploymentAtt = employmentAtt;
    }
}
