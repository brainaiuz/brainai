package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.trainingcenter.EdsContractCoursePrice;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 05.01.14
 * Time: 23:53
 * To change this template use File | Settings | File Templates.
 */
public interface ContractCoursePriceManager extends Manager<EdsContractCoursePrice> {

    List<EdsContractCoursePrice> getContractCoursePrices(Integer contractID);

    List<EdsContractCoursePrice> getCoursePricesForContract(Integer contractID, Integer courseID);

    EdsContractCoursePrice getCoursePricesForContractByLocation(Integer contractID, Integer courseID, Integer locationID);

    List<EdsLocation> getOnlyLocations(Integer contractID, Integer courseID);
}
