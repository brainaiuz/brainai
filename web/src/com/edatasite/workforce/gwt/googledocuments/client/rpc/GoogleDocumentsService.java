package com.edatasite.workforce.gwt.googledocuments.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.Attachments;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 13.11.2008
 * Time: 20:44:17
 * To change this template use File | Settings | File Templates.
 */
public interface GoogleDocumentsService {

    void saveToken(String token) throws Exception;

    void saveAttachments(Attachments attachments);

}
