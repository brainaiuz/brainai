package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * User: Ilhom
 * Date: 5/29/13
 * Time: 5:24 PM
 */
public class PunishmentsPromotionsItem implements IsSerializable {
    //Наказания/Поощрения  - >  Punishments == Penalties / Promotions

    public static final String ACTION = "ACTION";
    public static final String NAME = "NAME";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String TYPE = "TYPE";
    public static final String EMPLOYEE = "EMPLOYEE";
    public static final String NUMBER_OF_POINTS = "NUMBER_OF_POINTS";
    public static final String AMOUNT_OF_PENALTY = "AMOUNT_OF_PENALTY";
    public static final String EXCEPTIONAL_DATE = "EXCEPTIONAL_DATE";
    public static final String STATUS = "STATUS";

    //penalties/promotions type
    public static final String PUNISHMENTS_PROMOTIONS_TYPE = "PUNISHMENTS_PROMOTIONS_TYPE";                                     //penalties/promotions type
    public static final String PUNISHMENTS_PROMOTIONS_TYPE_PUNISHMENTS = "PUNISHMENTS_PROMOTIONS_TYPE_PUNISHMENTS";             //penalties
    public static final String PUNISHMENTS_PROMOTIONS_TYPE_PROMOTIONS = "PUNISHMENTS_PROMOTIONS_TYPE_PROMOTIONS";               //promotions
    //
    public static final String PUNISHMENTS_PROMOTIONS_TYPE_BONUSES = "PUNISHMENTS_PROMOTIONS_TYPE_BONUSES";                     //bonuses

    //penalties/promotions statuses
    public static final String PROMOTIONS_PENALTIES_STATUS = "PROMOTIONS_PENALTIES_STATUS";                                     //penalties/promotions statuses
    public static final String PROMOTIONS_PENALTIES_STATUS_ACTIVE = "PROMOTIONS_PENALTIES_STATUS_ACTIVE";                       //penalties/promotions status active
    public static final String PROMOTIONS_PENALTIES_STATUS_INACTIVE = "PROMOTIONS_PENALTIES_STATUS_INACTIVE";                   //penalties/promotions status inactive


    private Integer int_objectID;                  //employee penalties/promotions ID
    private String name;                           //employee penalties/promotions name
    private String description;                    //employee penalties/promotions description
    //type
    private SelectItem[] types;                    //penalties/promotions types list
    private String typeCode;                       //penalties/promotions type code
    private String typeName;                       //penalties/promotions type name
    private Integer int_typeID;                    //penalties/promotions type ID
    //status
    private SelectItem[] statuses;                 //penalties/promotions statuses list
    private String statusCode;                     //penalties/promotions status code
    private String statusName;                     //penalties/promotions status name
    private Integer int_statusID;                  //penalties/promotions status ID
    //employee
    private String employee;                       //employee penalties/promotions employee name
    private Integer int_employeeID;                //employee penalties/promotions employee ID
    //
    private BigDecimal numberOfPoints;             //employee penalties/promotions number of points
    private BigDecimal amountOfPenalty;            //employee penalties amount of penalty

    private DateNonConvertable exceptionalDate;                  //employee penalties exceptional date
    private Date creationDate;                     //employee penalties creation date

    private Integer int_parentID;                  //penalties/promotions parent ID
    private Integer int_projectID;                 //penalties/promotions project ID
    private String projectName;                    //penalties/promotions project name

    private boolean hasPermissionToDelete;


    public Integer getInt_objectID() {
        return int_objectID;
    }

    public void setInt_objectID(Integer int_objectID) {
        this.int_objectID = int_objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SelectItem[] getTypes() {
        return types;
    }

    public void setTypes(SelectItem[] types) {
        this.types = types;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getInt_typeID() {
        return int_typeID;
    }

    public void setInt_typeID(Integer int_typeID) {
        this.int_typeID = int_typeID;
    }

    public SelectItem[] getStatuses() {
        return statuses;
    }

    public void setStatuses(SelectItem[] statuses) {
        this.statuses = statuses;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getInt_statusID() {
        return int_statusID;
    }

    public void setInt_statusID(Integer int_statusID) {
        this.int_statusID = int_statusID;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public Integer getInt_employeeID() {
        return int_employeeID;
    }

    public void setInt_employeeID(Integer int_employeeID) {
        this.int_employeeID = int_employeeID;
    }

    public BigDecimal getNumberOfPoints() {
        return numberOfPoints;
    }

    public void setNumberOfPoints(BigDecimal numberOfPoints) {
        this.numberOfPoints = numberOfPoints;
    }

    public BigDecimal getAmountOfPenalty() {
        return amountOfPenalty;
    }

    public void setAmountOfPenalty(BigDecimal amountOfPenalty) {
        this.amountOfPenalty = amountOfPenalty;
    }

    public Date getExceptionalDate() {
        return exceptionalDate.getNonConvertedDate();
    }

    public void setExceptionalDate(Date exceptionalDate) {
        this.exceptionalDate = new DateNonConvertable(exceptionalDate);
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getInt_parentID() {
        return int_parentID;
    }

    public void setInt_parentID(Integer int_parentID) {
        this.int_parentID = int_parentID;
    }

    public Integer getInt_projectID() {
        return int_projectID;
    }

    public void setInt_projectID(Integer int_projectID) {
        this.int_projectID = int_projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public boolean isPunishments() {
        return PUNISHMENTS_PROMOTIONS_TYPE_PUNISHMENTS.equals(getTypeCode());
    }

    public boolean isHasPermissionToDelete() {
        return hasPermissionToDelete;
    }

    public void setHasPermissionToDelete(boolean hasPermissionToDelete) {
        this.hasPermissionToDelete = hasPermissionToDelete;
    }
}