package com.edatasite.workforce.gwt.trainingcenter.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * User: Ilxom Lutfullaev
 * Date: 7/18/12
 * Time: 5:58 PM
 */

public class CourseItem implements IsSerializable, ListingCustomFields {

    public static final String NAME = "name";
    public static final String SUBJECT = "subject";
    public static final String NUMBER = "number";
    public static final String DESCRIPTION = "description";
    public static final String ALIAS = "alias";
    public static final String TYPE = "type";
    public static final String VALIDITY = "validity";
    public static final String DURATION = "duration";
    public static final String PRICEPERSTUDENT = "priceperstudent";
    public static final String PREREQUISITE = "prerequisite";
    public static final String INSTRUCTOR = "instructor";
    public static final String COURSE_REQUIREMENTS = "courserequirements";
    public static final String ACCOUNT = "account";
    public static final String EXAMREQUIRED = "examRequired";
    public static final String OPITO = "opito";
    public static final String MEDCLEARANCE = "medClearance";

    private Integer objectID;
    private SelectItem subject;
    private TreeSelectItem[] categories;
    private NumberData numberData;
    private String courseName;
    private String description;
    private String aliasName;
    private SelectItem courseType;
    private Integer validity;
    private Integer duration;
    private BigDecimal pricePerStudent;
    private HashMap<Integer, BigDecimal> pricePerLocationStudent;
    private HashMap<Integer, BigDecimal> stopFeePerLocationStudent;
    private SelectItem[] preRequisite;
    private String otherPreRequisites;
    //private AccountItem accountItem;
    private boolean examRequired = true;
    private boolean opito = false;
    private boolean medClearance = false;
    private ArrayList<SelectItem> instructors;
    private LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> instructorMap;
    private SelectItem[] courseTypes;
    private SelectItem[] courses;
    private Integer accountID;

    private String number;
    private Date courseDate;
    private Date expireDate;

    private SelectItem[] courseRequirementList;
    private SelectItem[] courseRequirements;

    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private HashMap<String, Object> customFieldValues;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public SelectItem getSubject() {
        return subject;
    }

    public void setSubject(SelectItem subject) {
        this.subject = subject;
    }

    public TreeSelectItem[] getCategories() {
        return categories;
    }

    public void setCategories(TreeSelectItem[] categories) {
        this.categories = categories;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public SelectItem getCourseType() {
        return courseType;
    }

    public void setCourseType(SelectItem courseType) {
        this.courseType = courseType;
    }

    public Integer getValidity() {
        return validity;
    }

    public void setValidity(Integer validity) {
        this.validity = validity;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public BigDecimal getPricePerStudent() {
        return pricePerStudent;
    }

    public void setPricePerStudent(BigDecimal pricePerStudent) {
        this.pricePerStudent = pricePerStudent;
    }

    public HashMap<Integer, BigDecimal> getPricePerLocationStudent() {
        return pricePerLocationStudent;
    }

    public void setPricePerLocationStudent(HashMap<Integer, BigDecimal> pricePerLocationStudent) {
        this.pricePerLocationStudent = pricePerLocationStudent;
    }

    public HashMap<Integer, BigDecimal> getStopFeePerLocationStudent() {
        return stopFeePerLocationStudent;
    }

    public void setStopFeePerLocationStudent(HashMap<Integer, BigDecimal> stopFeePerLocationStudent) {
        this.stopFeePerLocationStudent = stopFeePerLocationStudent;
    }

    public SelectItem[] getPreRequisite() {
        return preRequisite;
    }

    public void setPreRequisite(SelectItem[] preRequisite) {
        this.preRequisite = preRequisite;
    }

//	public AccountItem getAccountItem() {
//		return accountItem;
//	}
//
//	public void setAccountItem(AccountItem accountItem) {
//		this.accountItem = accountItem;
//	}

    public boolean isExamRequired() {
        return examRequired;
    }

    public void setExamRequired(boolean examRequired) {
        this.examRequired = examRequired;
    }

    public boolean isOpito() {
        return opito;
    }

    public void setOpito(boolean opito) {
        this.opito = opito;
    }

    public boolean isMedClearance() {
        return medClearance;
    }

    public void setMedClearance(boolean medClearance) {
        this.medClearance = medClearance;
    }

    public ArrayList<SelectItem> getInstructors() {
        return instructors;
    }

    public void setInstructors(ArrayList<SelectItem> instructors) {
        this.instructors = instructors;
    }

    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getInstructorMap() {
        return instructorMap;
    }

    public void setInstructorMap(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> instructorMap) {
        this.instructorMap = instructorMap;
    }

    public SelectItem[] getCourseTypes() {
        return courseTypes;
    }

    public void setCourseTypes(SelectItem[] courseTypes) {
        this.courseTypes = courseTypes;
    }

    public SelectItem[] getCourses() {
        return courses;
    }

    public void setCourses(SelectItem[] courses) {
        this.courses = courses;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public SelectItem[] getCourseRequirementList() {
        return courseRequirementList;
    }

    public void setCourseRequirementList(SelectItem[] courseRequirementList) {
        this.courseRequirementList = courseRequirementList;
    }

    public SelectItem[] getCourseRequirements() {
        return courseRequirements;
    }

    public void setCourseRequirements(SelectItem[] courseRequirements) {
        this.courseRequirements = courseRequirements;
    }

    public String getOtherPreRequisites() {
        return otherPreRequisites;
    }

    public void setOtherPreRequisites(String otherPreRequisites) {
        this.otherPreRequisites = otherPreRequisites;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getCourseDate() {
        return courseDate;
    }

    public void setCourseDate(Date courseDate) {
        this.courseDate = courseDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public HashMap<String, Object> getCustomFieldValuesItems() {
        return customFieldValues;
    }

    public void setCustomFieldValuesItems(HashMap<String, Object> customFieldValues) {
        this.customFieldValues = customFieldValues;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return customFieldValues.get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        customFieldValues.put(columnCodeKey, cellValue);
    }

    public String getCourseRequirementsAsString() {
        if (getCourseRequirements() != null && getCourseRequirements().length > 0) {
            StringBuilder builder = new StringBuilder();
            boolean isFirst = true;

            for (SelectItem item : getCourseRequirements()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    builder.append(", ");
                }

                builder.append(item.getName());
            }

            return builder.toString();
        }
        return null;
    }

    public String getInstructorsAsString() {
        if (getInstructors() != null && getInstructors().size() > 0) {
            StringBuilder builder = new StringBuilder();
            boolean isFirst = true;

            for (SelectItem item : getInstructors()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    builder.append(", ");
                }

                builder.append(item.getName());
            }

            return builder.toString();
        }
        return null;
    }
}
