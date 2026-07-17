/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/26 7:31:0                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsImportFile;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 08-Jul-2009
 * Time: 18:36:21
 * To change this template use File | Settings | File Templates.
 */
public interface ImportFileManager extends Manager<EdsImportFile> {

    EdsImportFile getQueueByUser(EdsUser employee, String fromView, ImportTypeEnum type);

    List<EdsImportFile> getImportEventList(ListingFilterParameter fp);

    Integer getImportEventsCount(ListingFilterParameter fp);

    ArrayList<EdsImportFile> getImportFileStatusByType(String type, String status);

}
