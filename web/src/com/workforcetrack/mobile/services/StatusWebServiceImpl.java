package com.workforcetrack.mobile.services;

import com.edatasite.workforce.gwt.core.client.rpc.StatusService;
import com.edatasite.workforce.gwt.employee.server.app.EmployeeServiceLocal;
import com.workforcetrack.mobile.rpc.client.MFilterParametrs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: HAveANiceDay
 * Date: 10.07.11
 * Time: 6:46
 */
@Transactional
@Service("statusWebService")
public class StatusWebServiceImpl implements StatusWebService {

    @Autowired
    private StatusService statusService;

    @Autowired
    @Qualifier("employeeService")
    private EmployeeServiceLocal employeeServiceLocal;

    @Override
    public String setUserStatus(String changeStatusCode, Boolean timeSpentRequared) {
        if (changeStatusCode.equalsIgnoreCase("GET_RESULT")) {
            changeStatusCode = "AVAILABLE";
        }
        String result = statusService.setUserStatus(changeStatusCode, timeSpentRequared, false);
        return result == null ? "0" : result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Boolean setUserLocation(MFilterParametrs fp) {
        return setUserLocation(fp, null);
    }

    @Override
    @Transactional
    public Boolean setUserLocation(MFilterParametrs fp, Integer userID) {
        if (fp == null || fp.getLatitude() == null || fp.getLongitude() == null) {
            return Boolean.FALSE;
        }

        try {
            return employeeServiceLocal.setEmployeeLocation(userID, fp.getLatitude(), fp.getLongitude());
            /*          EdsEmployee employee = (userID == null) ? employeeManager.getUser().getEmployee() : employeeManager.get(userID);
           employee.setLongitude(fp.getLongitude());
           employee.setLatitude(fp.getLatitude());
           //employeeManager.update(employee);
           jpaTemplate.getHibernateEntityManager().persist(employee);
           return Boolean.TRUE;*/
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }
}
