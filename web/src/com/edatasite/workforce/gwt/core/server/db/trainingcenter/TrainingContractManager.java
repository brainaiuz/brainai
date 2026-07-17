package com.edatasite.workforce.gwt.core.server.db.trainingcenter;

import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourse;
import com.edatasite.workforce.core.domain.trainingcenter.EdsTrainingContract;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Babayev xushnud
 * Date: 8/16/12
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TrainingContractManager extends Manager<EdsTrainingContract> {

    List<EdsTrainingContract> list(ListingFilterParameter fp);

    Integer getContractTotalCount(ListingFilterParameter fp);

    List<EdsTrainingContract> getKeyClientList(EdsCrmAccount customer, Date nowDate);

    List<EdsTrainingContract> getKeyClientList(Integer customerID, Date nowDate);

    List<EdsCourse> getCourses(Integer contractID);
}
