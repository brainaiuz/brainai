package com.edatasite.workforce.gwt.core.server.db.impl.trainingcenter;

import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.trainingcenter.EdsContractCoursePrice;
import com.edatasite.workforce.gwt.core.server.db.ContractCoursePriceManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 05.01.14
 * Time: 23:55
 * To change this template use File | Settings | File Templates.
 */
@Repository("contractCoursePriceManager")
public class ContractCoursePriceManagerImpl extends BaseManager<EdsContractCoursePrice> implements ContractCoursePriceManager {

    public ContractCoursePriceManagerImpl() {
        super(EdsContractCoursePrice.class);
    }

    @Override
    public List<EdsContractCoursePrice> getContractCoursePrices(Integer contractID) {
        return find("SELECT cp FROM EdsContractCoursePrice cp WHERE cp.deleted<>true AND cp.contract.objectID=? order by cp.objectID, cp.contract.name asc", contractID);
    }

    @Override
    public List<EdsContractCoursePrice> getCoursePricesForContract(Integer contractID, Integer courseID) {
        return find("SELECT cp FROM EdsContractCoursePrice cp WHERE cp.deleted<>true AND cp.contract.objectID=? AND cp.course.objectID=?", contractID, courseID);
    }

    @Override
    public EdsContractCoursePrice getCoursePricesForContractByLocation(Integer contractID, Integer courseID, Integer locationID) {
        return (EdsContractCoursePrice) findSingle("SELECT cp FROM EdsContractCoursePrice cp WHERE cp.deleted<>true AND cp.contract.objectID=? AND cp.course.objectID=? AND cp.location.objectID=?", contractID, courseID, locationID);
    }

    @Override
    public List<EdsLocation> getOnlyLocations(Integer contractID, Integer courseID) {
        return find("SELECT cp.location FROM EdsContractCoursePrice cp WHERE cp.deleted<>true AND cp.contract.objectID=? AND cp.course.objectID=?", contractID, courseID);
    }
}
