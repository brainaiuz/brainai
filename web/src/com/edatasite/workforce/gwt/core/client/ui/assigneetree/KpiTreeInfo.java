package com.edatasite.workforce.gwt.core.client.ui.assigneetree;


import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: 26.04.12
 * Time: 11:13
 */
public class KpiTreeInfo implements Comparable<KpiTreeInfo>, IsSerializable {

    private Integer id;
    private String name;
    public static final ProvidesKey<KpiTreeInfo> KEY_PROVIDER_BUDGET = item -> item == null ? null : item.getDepartmentId() != null ? item.getId() + "__" + item.getDepartmentId() : item.getId() + "$" + "c";
    private Integer departmentId;
    private String imageUrl;
    private String label;
    private Boolean subMenu;
    private Boolean subMenu2;
    private boolean myself;
    private boolean selected = false;
    private Integer positionId;
    private String positionName;
    private String assignedPositionName;
    private Integer employeeId;
    private String roleName;
    private Boolean essRole;
    private Integer time;       // set estimated time
    private Integer timeSpent;
    private Integer actualTime;
    private Double wageRate;
    private Double clientChargeRate;
    private Float workloadPercentage; //workload percentage (the percentage of employees in the project workload)
    private Float percent;
    private Integer statusId;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private Date startDate;
    private Date endDate;
    private Date lastUpdateDate;
    private Date createdDate;
    private Boolean isDeleted;
    private Boolean isNew;
    private Integer fullPartTime;

    private String employeeNumber;
    private String skills;
    private Date availableFrom;
    private String currenctProjecs;

    private DateNonConvertable contractStart;
    private DateNonConvertable contractEnd;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private DateNonConvertable lastContractDate;
    private SelectItem lastContractedProject;
    private boolean isRejected;
    private String matchSortString;
    private int matchSortInteger = 2;
    private String key;
    private Integer projectEmployeeId;
    private String unit;
    private String email;
    private String phone;

    //    public KpiTreeInfo(Integer id, String name, KpiTreeInfo categoryInfo) {
//        this.id = id;
//        this.name = name;
//        this.categoryInfo = categoryInfo;
//    }

    public KpiTreeInfo(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public KpiTreeInfo() {
    }

    /**
     * The key provider that provides the unique ID of a contact.
     */
    public static final ProvidesKey<KpiTreeInfo> KEY_PROVIDER = item -> item == null ? null : item.departmentName != null ? item.getId() : item.getId() + "$" + "c";
    private String departmentName;

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p/>
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p/>
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p/>
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p/>
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p/>
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws ClassCastException if the specified object's type prevents it
     *                            from being compared to this object.
     */
    public int compareTo(KpiTreeInfo o) {
        return (o == null || o.name == null) ? -1
                : ((o.matchSortInteger != matchSortInteger) ? matchSortInteger - o.matchSortInteger : -o.name.compareTo(name));

    }

    public boolean equals(Object obj) {
        if (!(obj instanceof KpiTreeInfo)) {
            return false;
        }
        KpiTreeInfo other = (KpiTreeInfo) obj;
        if (getId() != null && other.getId() != null) {
            return getId().equals(other.getId());
        } else {
            return getName() != null && getName().equals(other.getName());
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getSubMenu() {
        return subMenu;
    }

    public void setSubMenu(Boolean subMenu) {
        this.subMenu = subMenu;
    }

    public Boolean getSubMenu2() {
        return subMenu2;
    }

    public void setSubMenu2(Boolean subMenu2) {
        this.subMenu2 = subMenu2;
    }


    //    public KpiTreeInfo getCategoryInfo() {
//        return categoryInfo;
//    }
//
//    public void setCategoryInfo(KpiTreeInfo categoryInfo) {
//        this.categoryInfo = categoryInfo;
//    }

    public boolean isMyself() {
        return myself;
    }

    public void setMyself(boolean myself) {
        this.myself = myself;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getAssignedPositionName() {
        return assignedPositionName;
    }

    public void setAssignedPositionName(String assignedPositionName) {
        this.assignedPositionName = assignedPositionName;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Integer getActualTime() {
        return actualTime;
    }

    public void setActualTime(Integer actualTime) {
        this.actualTime = actualTime;
    }

    public Double getWageRate() {
        return wageRate;
    }

    public void setWageRate(Double wageRate) {
        this.wageRate = wageRate;
    }

    public Double getClientChargeRate() {
        return clientChargeRate;
    }

    public void setClientChargeRate(Double clientChargeRate) {
        this.clientChargeRate = clientChargeRate;
    }

    public Float getWorkloadPercentage() {
        return workloadPercentage;
    }

    public void setWorkloadPercentage(Float workloadPercentage) {
        this.workloadPercentage = workloadPercentage;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Boolean getNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    public Integer getFullPartTime() {
        return fullPartTime;
    }

    public void setFullPartTime(Integer fullPartTime) {
        this.fullPartTime = fullPartTime;
    }

    public Float getPercent() {
        return percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public Date getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(Date availableFrom) {
        this.availableFrom = availableFrom;
    }

    public String getCurrenctProjecs() {
        return currenctProjecs;
    }

    public void setCurrenctProjecs(String currenctProjecs) {
        this.currenctProjecs = currenctProjecs;
    }

    public DateNonConvertable getContractStart() {
        return contractStart;
    }

    public void setContractStart(DateNonConvertable contractStart) {
        this.contractStart = contractStart;
    }

    public DateNonConvertable getContractEnd() {
        return contractEnd;
    }

    public void setContractEnd(DateNonConvertable contractEnd) {
        this.contractEnd = contractEnd;
    }

    public DateNonConvertable getLastContractDate() {
        return lastContractDate;
    }

    public void setLastContractDate(DateNonConvertable lastContractDate) {
        this.lastContractDate = lastContractDate;
    }

    public SelectItem getLastContractedProject() {
        return lastContractedProject;
    }

    public void setLastContractedProject(SelectItem lastContractedProject) {
        this.lastContractedProject = lastContractedProject;
    }

    public boolean isRejected() {
        return isRejected;
    }

    public void setRejected(boolean rejected) {
        isRejected = rejected;
    }

    public String getMatchSortString() {
        return matchSortString;
    }

    public void setMatchSortString(String matchSortString) {
        this.matchSortString = matchSortString;
    }

    public int getMatchSortInteger() {
        return matchSortInteger;
    }

    public void setMatchSortInteger(int matchSortInteger) {
        this.matchSortInteger = matchSortInteger;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public Integer getProjectEmployeeId() {
        return projectEmployeeId;
    }

    public void setProjectEmployeeId(Integer projectEmployeeId) {
        this.projectEmployeeId = projectEmployeeId;
    }

    public Boolean getEssRole() {
        return essRole;
    }

    public void setEssRole(Boolean essRole) {
        this.essRole = essRole;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}