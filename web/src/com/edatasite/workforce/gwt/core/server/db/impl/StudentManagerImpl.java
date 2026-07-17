package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseScheduleStudent;
import com.edatasite.workforce.core.domain.trainingcenter.EdsStudent;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.StudentManager;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Ilhombek
 * Date: 7/19/12
 * Time: 7:26 PM
 */
@Repository("studenttManager")
public class StudentManagerImpl extends BaseManager<EdsStudent> implements StudentManager, TCConstants {

    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    public StudentManagerImpl() {
        super(EdsStudent.class);
    }

    @Override
    public List<EdsStudent> getStudentList(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT st.id, st.* FROM ").append(getCompanyId()).append(".student st \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".crmContact contact ON (contact.id = st.contact_id) \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".crmAccount custom ON (custom.id = st.customer_id) \n");

        sql.append("WHERE contact.deleted is not true \n");

        if (fp.getAccountID() != null) {
            sql.append("AND st.customer_id = ").append(fp.getAccountID()).append(" \n");
        }
        //searching
        if (fp.getSqlSearchKey() != null) {
            sql.append(" AND (");
            sql.append(" LOWER(contact.firstName) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR LOWER(contact.lastName) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR LOWER(st.number) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR LOWER(st.safetyPPNumber) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR LOWER(custom.name) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(") \n");
        }
        sql.append(" ORDER BY ");
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            if (StudentItem.STUDENT_ACTION.equals(fp.getSortField()) || StudentItem.STUDENT_FIRST_NAME.equals(fp.getSortField())) {
                sql.append("contact.firstName");
            } else if (StudentItem.STUDENT_NUMBER.equals(fp.getSortField())) {
                sql.append("st.number");
            } else if (StudentItem.STUDENT_LAST_NAME.equals(fp.getSortField())) {
                sql.append("contact.lastName");
            } else if (StudentItem.STUDENT_CUSTOMER.equals(fp.getSortField())) {
                sql.append("custom.name");
            } else if (StudentItem.STUDENT_PHONE_NUMBER.equals(fp.getSortField())) {
                sql.append("contact.primaryPhone");
            } else if (StudentItem.STUDENT_E_MAIL.equals(fp.getSortField())) {
                sql.append("contact.primaryEmail");
            } else if (StudentItem.STUDENT_LAST_UPDATE_DATE.equals(fp.getSortField())) {
                sql.append("contact.modificationDate");
            } else if (StudentItem.STUDENT_STATUS.equals(fp.getSortField())) {
                sql.append("st.active");
            } else if (StudentItem.STUDENT_RESIDENCE_NUMBER.equals(fp.getSortField())) {
                sql.append("st.safetyPPNumber");
            } else {
                sql.append("contact.modificationDate");
            }
            if (fp.getSortDir() != null) {
                if (Integer.valueOf(1).equals(fp.getSortDir())) {
                    sql.append(" ASC");
                } else {
                    sql.append(" DESC");
                }
            } else {
                sql.append(" DESC");
            }
        } else {
            sql.append(" contact.modificationDate DESC nulls last");
        }

        if (!fp.isLookUp() && fp.getStart() != null && fp.getLimit() != null) {
            sql.append(" OFFSET ").append(fp.getStart()).append(" LIMIT ").append(fp.getLimit());
        }

        return findNative(sql.toString(), EdsStudent.class);
    }

    @Override
    public Integer getStudentListTotalCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(st.id) FROM ").append(getCompanyId()).append(".student st \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".crmContact contact ON (contact.id = st.contact_id) \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".crmAccount custom ON (custom.id = st.customer_id) \n");
        sql.append("WHERE contact.deleted is not true");

        if (fp.getAccountID() != null) {
            sql.append("AND st.customer_id = ").append(fp.getAccountID()).append(" \n");
        }
        //searching
        if (fp.getSqlSearchKey() != null) {
            sql.append(" AND (");
            sql.append(" LOWER(contact.firstName) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR LOWER(contact.lastName) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR LOWER(st.number) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR LOWER(st.safetyPPNumber) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR LOWER(custom.name) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(") \n");
        }

        return ((BigInteger) findNativeSingle(sql.toString())).intValue();
    }

    @Override
    public EdsStudent findStudentByResidenceNum(String residenceNum, EdsCrmAccount customer) {
        return (EdsStudent) findSingle("SELECT s FROM EdsStudent s " +
                " INNER JOIN s.contact c " +
                " WHERE lower(s.safetyPPNumber)='" + residenceNum.toLowerCase() + "' AND (c.deleted is null OR c.deleted is false) AND s.customer=?", customer);
    }

    @Override
    public EdsStudent findExistingStudentByResidenceNum(Integer objectID, String residenceNum, EdsCrmAccount customer) {
        return (EdsStudent) findSingle("SELECT s FROM EdsStudent s " +
                " INNER JOIN s.contact c " +
                " WHERE lower(s.safetyPPNumber)='" + residenceNum.toLowerCase() + "' AND (c.deleted is null OR c.deleted is false) AND s.customer=? " + (objectID != null ? " AND s.objectID != " + objectID + " " : ""), customer);
    }

    public List<EdsStudent> getScheduledCourseStudents(Integer courseID, ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT st.id, st.* FROM ").append(getCompanyId()).append(".student st ");
        sql.append("inner join ").append(getCompanyId()).append(".crmContact contact on st.contact_id = contact.id ");
        sql.append("inner join ").append(getCompanyId()).append(".courseschedulestudent cs on cs.student_id = st.id ");
        sql.append("inner join ").append(getCompanyId()).append(".reference ss on ss.id = cs.stutus_id ");
        sql.append("inner join ").append(getCompanyId()).append(".coursebooking cb on cb.id = cs.coursebooking_id ");
        sql.append("left join ").append(getCompanyId()).append(".crmAccount sa on sa.id = st.customer_id ");
        sql.append("WHERE ss.code != '").append(EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_REJECTED).append("' and cs.courseschedule_id = ").append(courseID);//.append(" and ");
        //searching
        String searchKey = fp.getSqlSearchKey();
        if (searchKey != null && !searchKey.isEmpty()) {
            if (fp.getSqlSearchKey() != null) {
                sql.append(" AND (");
                sql.append(" lower(contact.firstname) like '").append(searchKey).append("' ");      //ishladi
                sql.append(" OR lower(contact.lastName) like '").append(searchKey).append("' "); //ishladi
                sql.append(" OR lower(sa.name) like '").append(searchKey).append("' ");          //ishladi
                sql.append(" OR lower(cb.number) like '").append(searchKey).append("' ");
                sql.append(" OR lower(st.safetyppnumber) like '").append(searchKey).append("' ");
                sql.append(") \n");
            }
        } //sorting
        if (fp != null && fp.getSortField() != null) {
            if (StudentItem.STUDENT_NUMBER.equals(fp.getSortField())) {
                sql.append(" order by st.number" + (!fp.isAscending() ? " desc" : ""));
            } else if (StudentItem.STUDENT_FIRST_NAME.equals(fp.getSortField())) {
                sql.append(" order by contact.firstname" + (!fp.isAscending() ? " desc" : ""));
            } else if (StudentItem.STUDENT_LAST_NAME.equals(fp.getSortField())) {
                sql.append(" order by contact.lastname" + (!fp.isAscending() ? " desc" : ""));
            } else if (StudentItem.STUDENT_CUSTOMER.equals(fp.getSortField())) {
                sql.append(" order by sa.name" + (!fp.isAscending() ? " desc" : ""));
            } else if (StudentItem.STUDENT_EXAM_STATUS.equals(fp.getSortField())) {
                sql.append(" order by ss.code" + (!fp.isAscending() ? " desc" : ""));
            } else {
                sql.append(" order by contact.modificationDate desc");
            }
        } else {
            sql.append(" order by contact.modificationDate desc");
        }
        if (fp.getStart() != null && fp.getLimit() != null) {
            sql.append(" OFFSET ").append(fp.getStart()).append(" LIMIT ").append(fp.getLimit());
        }

        return findNative(sql.toString(), EdsStudent.class);
    }

    @Override
    public Integer getScheduledCourseStudentsTotalCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(st.id) FROM ").append(getCompanyId()).append(".student st ");
        sql.append("inner join ").append(getCompanyId()).append(".crmContact contact on st.contact_id = contact.id ");
        sql.append("inner join ").append(getCompanyId()).append(".courseschedulestudent cs on cs.student_id = st.id ");
        sql.append("inner join ").append(getCompanyId()).append(".reference ss on ss.id = cs.stutus_id ");
        sql.append("inner join ").append(getCompanyId()).append(".coursebooking cb on cb.id = cs.coursebooking_id ");
        sql.append("left join ").append(getCompanyId()).append(".crmAccount sa on sa.id = st.customer_id ");
        sql.append("WHERE ss.code != '").append(EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_REJECTED).append("' and cs.courseschedule_id = ").append(fp.getScheduledCourseID());

        String searchKey = fp.getSqlSearchKey();
        if (searchKey != null && !searchKey.isEmpty()) {
            if (fp.getSqlSearchKey() != null) {
                sql.append(" AND (");
                sql.append(" lower(contact.firstname) like '").append(searchKey).append("' ");
                sql.append(" OR lower(contact.lastName) like '").append(searchKey).append("' ");
                sql.append(" OR lower(sa.name) like '").append(searchKey).append("' ");
                sql.append(" OR lower(cb.number) like '").append(searchKey).append("' ");
                sql.append(" OR lower(st.safetyppnumber) like '").append(searchKey).append("' ");
                sql.append(") \n");
            }
        } //sorting
        return ((BigInteger) findNativeSingle(sql.toString())).intValue();
    }

    public List<EdsStudent> getStudentWithExistEmail(Integer customerID, List<String> existEmails) {

        StringBuilder existEmailStrings = new StringBuilder();
        boolean isFirst = false;
        for (String existEmailS : existEmails) {
            if (!isFirst) {
                existEmailStrings.append("LOWER(cont.primaryemail)='").append(existEmailS).append("'");
                isFirst = true;
            } else {
                existEmailStrings.append(" OR ");
                existEmailStrings.append("LOWER(cont.primaryemail)='").append(existEmailS).append("'");
            }
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT st.id, st.* FROM ").append(getCompanyId()).append(".student st \n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".crmcontact cont ON (cont.id = st.contact_id) \n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".crmaccount cust ON (cust.id = st.customer_id) \n");
        sql.append("WHERE cust.id=").append(customerID).append(" AND \n");
        sql.append("(");
        sql.append("cont.primaryemail is not null \n");
        if (!"".contentEquals(existEmailStrings)) {
            sql.append("AND ( ").append(existEmailStrings).append(" ) \n");
        }
        sql.append(") AND \n");
        sql.append("(cont.deleted is not true)");

        if ("".contentEquals(existEmailStrings)) {
            return new ArrayList<>();
        }
        return findNative(sql.toString(), EdsStudent.class);
    }

    public List<EdsStudent> getScheduledCourseStudentsForCSV(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT st.id, st.*,contact.firstName FROM ").append(getCompanyId()).append(".student st ");
        sql.append("inner join ").append(getCompanyId()).append(".courseschedulestudent cs on cs.student_id = st.id ");
        sql.append("inner join ").append(getCompanyId()).append(".crmContact contact on st.contact_id = contact.id ");
        sql.append("inner join ").append(getCompanyId()).append(".reference ref on ref.id = cs.stutus_id ");
        sql.append("WHERE 1=1 ");
        sql.append("and ref.code ='" + EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_ATTENDED + "' ");
        sql.append("and cs.courseschedule_id='" + fp.getScheduledCourseID() + "' ");
        sql.append("order by contact.firstName ASC ").append(" LIMIT ").append(fp.getLimit());

        return findNative(sql.toString(), EdsStudent.class);
    }

    @Override
    public List<EdsStudent> getCoursePassedStudents() {
        StringBuilder sql = new StringBuilder();                                         // WHERE " + ServerUtils.checkForDeleted("st.deleted")
        sql.append("SELECT DISTINCT st.id, st.* FROM ").append(getCompanyId()).append(".student st ");
        sql.append("inner join ").append(getCompanyId()).append(".crmContact contact on st.contact_id = contact.id ");
        sql.append("WHERE " + ServerUtils.checkForDeleted("contact.deleted"));

        return findNative(sql.toString(), EdsStudent.class);
    }

    @Override
    public EdsStudent findStudentByCompanyEmplopyeeNum(String companyEmpNum, EdsCrmAccount customer) {
        return (EdsStudent) findSingle("SELECT s FROM EdsStudent s " +
                " INNER JOIN s.contact c " +
                " WHERE lower(s.compEmplNumber)='" + companyEmpNum.toLowerCase() + "' AND c.deleted IS NOT TRUE AND s.customer=?", customer);
    }

    @Override
    public List<Object[]> getStudentCustomerList() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM (SELECT customer.id, max(trim(customer.name)) cname FROM ").append(getCompanyId()).append(".student s ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".crmContact contact ON contact.id = s.contact_id ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".crmAccount customer ON customer.id = s.customer_id ");
        sql.append("WHERE contact.deleted IS NOT TRUE ");
        sql.append("GROUP BY customer.id) t ORDER BY t.cname ");
        return findNative(sql.toString());
    }

    @Override
    public boolean validateExistingEmail(String email, EdsCrmAccount customer) {
        EdsStudent edsStudent = (EdsStudent) findSingle("SELECT s FROM EdsStudent s " +
                " INNER JOIN s.contact c " +
                " WHERE c.deleted IS NOT TRUE AND c.primaryEmail='" + email + "' AND s.customer=?", customer);
        return edsStudent != null;
    }

    @Override
    public List<Object[]> getDuplicateStudentListOfCustomer(Integer customerID) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ");
        sql.append("(select t.customer_id, max(customer_name) customer_name, t.safetyPPNumber, t.student, count(t.id) c, array_to_string(array_agg(t.id), ',') as sids from ");
        sql.append("(select s.id, (coalesce(lower(trim(con.firstname)), '') || ' ' || coalesce(lower(trim(con.lastname)), '')) student, s.safetyPPNumber, customer.name customer_name, customer.id customer_id  from ").
                append(getCompanyId()).append(".student s ");
        sql.append("inner join ").append(getCompanyId()).append(".crmcontact con on con.id = s.contact_id ");
        sql.append("inner join ").append(getCompanyId()).append(".crmaccount customer on customer.id = s.customer_id ");
        sql.append("where con.deleted is not true) t ");
        sql.append("group by t.customer_id, t.safetyPPNumber, t.student ");
        sql.append("order by t.customer_id, t.safetyPPNumber, t.student) tt ");
        sql.append("where tt.c > 1 ");

        if (customerID != null) {
            sql.append(" and tt.customer_id = ").append(customerID);
        }
        return findNative(sql.toString());
    }

    @Override
    public List<EdsStudent> getStudentListByIds(String ids) {
        return findNative("SELECT s.* FROM " + getCompanyId() + ".student s WHERE s.id in (" + ids + ")", EdsStudent.class);
    }

    @Override
    public void mergeDuplicateStudentsToMaster(ArrayList<Integer> ids, Integer masterID) {
        //update duplicate students in course schedule student list
        updateNative("UPDATE " + getCompanyId() + ".courseschedulestudent SET student_id = " + masterID + " WHERE student_id in (" + ServerUtils.getAsCommoDelimited(ids, "0", ",") + ")");
        //update duplicate students in certificate list
        updateNative("UPDATE " + getCompanyId() + ".certificate SET studentid = " + masterID + " WHERE studentid in (" + ServerUtils.getAsCommoDelimited(ids, "0", ",") + ")");
        //update duplicate students in
        updateNative("UPDATE " + getCompanyId() + ".studentattended  SET student_id = " + masterID + " WHERE student_id in (" + ServerUtils.getAsCommoDelimited(ids, "0", ",") + ")");
    }

    @Override
    public void mergeStudentCustomers(ArrayList<Integer> ids, Integer masterID) {
        updateNative("UPDATE " + getCompanyId() + ".student SET customer_id = " + masterID + " WHERE customer_id in (" + ServerUtils.getAsCommoDelimited(ids, "0", ",") + ")");
    }

    @Override
    public List<ContactListItem> getStudentsForLookUp(ListingFilterParameter fp) {
        if (fp == null) fp = new ListingFilterParameter();

        StringBuilder query = new StringBuilder();
        query
                .append("SELECT st.id as objectId, contact.firstname, contact.middlename, contact.lastname  FROM ").append(getCompanyId()).append(".student st ")
                .append("INNER JOIN ").append(getCompanyId()).append(".crmContact contact ON (contact.id = st.contact_id) ")
                .append("WHERE contact.deleted is not true and ((COALESCE(contact.firstName, '') <> '' OR COALESCE(contact.lastName, '') <> '')) ");

        if (fp.getSqlSearchKey() != null) {
            query.append(" AND (")
                    .append(" LOWER(contact.firstName) LIKE '").append(fp.getSqlSearchKey()).append("' ")
                    .append(" OR LOWER(contact.lastName) LIKE '").append(fp.getSqlSearchKey()).append("' ")
                    .append(" OR LOWER(st.number) LIKE '").append(fp.getSqlSearchKey()).append("' ")
                    .append(" OR LOWER(st.safetyPPNumber) LIKE '").append(fp.getSqlSearchKey()).append("' ")
                    .append(") ");
        }
        query.append(" ORDER BY contact.firstName ASC");

        if (fp.getLimit() != null) {
            query.append(" LIMIT ").append(fp.getLimit());
        } else {
            query.append(" LIMIT ").append(20);
        }

        return jdbcSpringManager.getSimpleJdbcTemplate().query(query.toString(), BeanPropertyRowMapper.newInstance(ContactListItem.class));
    }

    public EdsStudent getStudentByCrmAccountId (Integer crmAccountId) {
        StringBuilder sql = new StringBuilder();
        sql.append("Select st.* from ").append(getCompanyId()).append(".student st \n");
        sql.append("join ").append(getCompanyId()).append(".crmContact contact on contact.id = st.contact_id \n");
        sql.append("left join ").append(getCompanyId()).append(".crmAccount custom on custom.id = st.customer_id \n");
        sql.append("where contact.deleted is not true \n");
        sql.append("and st.customer_id = ").append(crmAccountId).append(" \n");
        sql.append("order by st.id desc limit 1");
        return (EdsStudent) slaveEntityManager.createNativeQuery(sql.toString(), EdsStudent.class).getSingleResult();
    };
}
