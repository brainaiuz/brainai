package com.workforcetrack.mobile.services;

import com.workforcetrack.mobile.rpc.attachment.MFileResource;
import com.workforcetrack.mobile.rpc.attachment.MFileResourceList;
import com.workforcetrack.mobile.rpc.base.MStringList;

/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 20.04.12
 * Time: 16:12
 * To change this template use File | Settings | File Templates.
 */
public interface UploadWebService {

    String generateURL(MFileResource uploadResource);

    MStringList generateURL(MFileResourceList uploadResourceList);

    String deleteFile(Integer objectID);
}
