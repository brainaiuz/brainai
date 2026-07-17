package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.trainingcenter.EdsStudent;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Ilhombek
 * Date: 7/19/12
 * Time: 7:26 PM
 */
public interface StudentManager extends Manager<EdsStudent> {

    List<EdsStudent> getStudentList(ListingFilterParameter fp);

    List<EdsStudent> getScheduledCourseStudents(Integer courseID, ListingFilterParameter fp);

    Integer getScheduledCourseStudentsTotalCount(ListingFilterParameter fp);

    List<EdsStudent> getStudentWithExistEmail(Integer customerID, List<String> existEmails);

    List<EdsStudent> getScheduledCourseStudentsForCSV(ListingFilterParameter fp);

    Integer getStudentListTotalCount(ListingFilterParameter fp);

    EdsStudent findStudentByResidenceNum(String residenceNum, EdsCrmAccount customer);

    EdsStudent findExistingStudentByResidenceNum(Integer objectID, String residenceNum, EdsCrmAccount customer);

    List<EdsStudent> getCoursePassedStudents();

    EdsStudent findStudentByCompanyEmplopyeeNum(String companyEmpNum, EdsCrmAccount customer);

    List<Object[]> getStudentCustomerList();

    boolean validateExistingEmail(String email, EdsCrmAccount customer);

    List<Object[]> getDuplicateStudentListOfCustomer(Integer customerID);

    List<EdsStudent> getStudentListByIds(String ids);

    void mergeDuplicateStudentsToMaster(ArrayList<Integer> ids, Integer masterID);

    void mergeStudentCustomers(ArrayList<Integer> ids, Integer masterID);

    List<ContactListItem> getStudentsForLookUp(ListingFilterParameter fp);

    EdsStudent getStudentByCrmAccountId (Integer crmAccountId);

}