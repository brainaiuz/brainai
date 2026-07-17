package com.edatasite.workforce.gwt.core.client.rpc;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 7/14/11
 * Time: 5:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReferenceItem extends SelectItem {

    public static final int SHORT_NAME_EXISTS = -1;
    public static final int CODE_EXISTS = -2;
    public static final int NAME_EXISTS = -3;

    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String CODE = "code";
    private String textDescription;
    private String cssStyle;
    private String antonym;
    private String code;
    private Integer order;
    private Integer parentID;
    private String parent;
    private String parentCode;
    private boolean isSystemReference;
    private boolean isRemovable = true;
    private String relative;
    private boolean isCustomButton = false;
    private String buttonLocation;
    private boolean isLeaveToAll = false;
    private boolean probationToAll = false;
    private Double leaveDays = 0.0;
    private Double probationDays = 0.0;
    private boolean isActive;
    private boolean canDelete = false;
    private boolean dayOffIncluded;
    private boolean includeHolidays;
    private boolean includeDayOffs;
    private String shortName;
    private String hexColor;
    private boolean attendanceLR;
    private boolean autoApprove;
    private boolean hasProrata;
    private boolean requiredComment;
    private DateNonConvertable openingBalanceDate;
    private ArrayList<LeaveBalanceSettings> leaveBalanceSettings;
    private ArrayList<SelectItem> allRoles;
    private ArrayList<SelectItem> allowedRoles;
    private ArrayList<SelectItem> allowedRolesView;
    private ArrayList<SelectItem> employeeCanEdit;
    private ArrayList<SelectItem> employeeCanView;
    private ArrayList<SelectItem> employeeCanEditButton;
    private ArrayList<SelectItem> oppEditBtnRole;
    private ReferenceLocale locale;
    private String originalName;

    public ReferenceItem(Integer id, String name, String description) {
        super(id, name, description);
        setCode(description);
    }

    public ReferenceItem(Integer id, String name, String description, String cssStyle, String antonym) {
        this(id, name, description);
        setCssStyle(cssStyle);
        setAntonym(antonym);
    }

    public ReferenceItem(Integer id) {
        super(id);
    }

    public ReferenceItem() {
        super();
    }

    public ReferenceItem(Integer id, String name) {
        super(id, name);
    }

    public ReferenceItem(Integer id, String name, String description, ReferenceLocale locale) {
        super(id, name);
        setCode(description);
        setLocale(locale);
    }

    public Integer getObjectID() {
        return getId();
    }

    public void setObjectID(Integer objectID) {
        setId(objectID);
    }

    public String getName(boolean isAntonym) {
        return isAntonym ? getAntonym() : getName();
    }

    public String getTextDescription() {
        return textDescription;
    }

    public void setTextDescription(String textDescription) {
        this.textDescription = textDescription;
    }

    public String getCssStyle(boolean... doNotReturnNull) {
        return cssStyle == null && doNotReturnNull != null && doNotReturnNull.length > 0 && doNotReturnNull[0] ? "" : cssStyle;
    }

    public void setCssStyle(String cssStyle) {
        this.cssStyle = cssStyle;
    }

    public String getAntonym() {
        return antonym == null ? getName() : antonym;
    }

    public void setAntonym(String antonym) {
        this.antonym = antonym;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getParentID() {
        return parentID;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public boolean isSystemReference() {
        return isSystemReference;
    }

    public void setSystemReference(boolean systemReference) {
        isSystemReference = systemReference;
    }

    public boolean isRemovable() {
        return isRemovable;
    }

    public void setRemovable(boolean removable) {
        isRemovable = removable;
    }

    public String getRelative() {
        return relative;
    }

    public void setRelative(String relative) {
        this.relative = relative;
    }

    public boolean isCustomButton() {
        return isCustomButton;
    }

    public void setCustomButton(boolean customButton) {
        isCustomButton = customButton;
    }

    public String getButtonLocation() {
        return buttonLocation;
    }

    public void setButtonLocation(String buttonLocation) {
        this.buttonLocation = buttonLocation;
    }

    public String getReferenceCode() {
        return code;
    }

    public boolean isLeaveToAll() {
        return isLeaveToAll;
    }

    public void setLeaveToAll(boolean isLeaveToAll) {
        this.isLeaveToAll = isLeaveToAll;
    }

    public void setLeaveDays(Double leaveDays) {
        this.leaveDays = leaveDays;
    }

    public Double getLeaveDays() {
        return leaveDays;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public boolean isDayOffIncluded() {
        return dayOffIncluded;
    }

    public void setDayOffIncluded(boolean dayOffIncluded) {
        this.dayOffIncluded = dayOffIncluded;
    }

    public boolean isIncludeHolidays() {
        return includeHolidays;
    }

    public void setIncludeHolidays(boolean includeHolidays) {
        this.includeHolidays = includeHolidays;
    }

    public boolean isIncludeDayOffs() {
        return includeDayOffs;
    }

    public void setIncludeDayOffs(boolean includeDayOffs) {
        this.includeDayOffs = includeDayOffs;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public boolean isAttendanceLR() {
        return attendanceLR;
    }

    public void setAttendanceLR(boolean attendanceLR) {
        this.attendanceLR = attendanceLR;
    }

    public boolean isAutoApprove() {
        return autoApprove;
    }

    public void setAutoApprove(boolean autoApprove) {
        this.autoApprove = autoApprove;
    }

    public ArrayList<LeaveBalanceSettings> getLeaveBalanceSettings() {
        return leaveBalanceSettings;
    }

    public void setLeaveBalanceSettings(ArrayList<LeaveBalanceSettings> leaveBalanceSettings) {
        this.leaveBalanceSettings = leaveBalanceSettings;
    }

    public boolean isHasProrata() {
        return hasProrata;
    }

    public void setHasProrata(boolean hasProrata) {
        this.hasProrata = hasProrata;
    }

    public boolean isProbationToAll() {
        return probationToAll;
    }

    public void setProbationToAll(boolean probationToAll) {
        this.probationToAll = probationToAll;
    }

    public Double getProbationDays() {
        return probationDays;
    }

    public void setProbationDays(Double probationDays) {
        this.probationDays = probationDays;
    }

    public DateNonConvertable getOpeningBalanceDate() {
        return openingBalanceDate;
    }

    public void setOpeningBalanceDate(DateNonConvertable openingBalanceDate) {
        this.openingBalanceDate = openingBalanceDate;
    }

    public ArrayList<SelectItem> getAllowedRoles() {
        return this.allowedRoles;
    }

    public void setAllowedRoles(ArrayList<SelectItem> allowedRoles) {
        this.allowedRoles = allowedRoles;
    }

    public boolean isRequiredComment() {
        return this.requiredComment;
    }

    public void setRequiredComment(boolean requiredComment) {
        this.requiredComment = requiredComment;
    }

    public ReferenceLocale getLocale() {
        return locale;
    }

    public void setLocale(ReferenceLocale locale) {
        this.locale = locale;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public ArrayList<SelectItem> getAllowedRolesView() {
        return this.allowedRolesView;
    }

    public void setAllowedRolesView(ArrayList<SelectItem> allowedRolesView) {
        this.allowedRolesView = allowedRolesView;
    }

    public ArrayList<SelectItem> getAllRoles() {
        return this.allRoles;
    }

    public void setAllRoles(ArrayList<SelectItem> allRoles) {
        this.allRoles = allRoles;
    }

    public ArrayList<SelectItem> getOppEditBtnRole() {
        return this.oppEditBtnRole;
    }

    public void setOppEditBtnRole(ArrayList<SelectItem> oppEditBtnRole) {
        this.oppEditBtnRole = oppEditBtnRole;
    }

    public ArrayList<SelectItem> getEmployeeCanEdit() {
        return employeeCanEdit;
    }

    public void setEmployeeCanEdit(ArrayList<SelectItem> employeeCanEdit) {
        this.employeeCanEdit = employeeCanEdit;
    }

    public ArrayList<SelectItem> getEmployeeCanView() {
        return employeeCanView;
    }

    public void setEmployeeCanView(ArrayList<SelectItem> employeeCanView) {
        this.employeeCanView = employeeCanView;
    }

    public ArrayList<SelectItem> getEmployeeCanEditButton() {
        return employeeCanEditButton;
    }

    public void setEmployeeCanEditButton(ArrayList<SelectItem> employeeCanEditButton) {
        this.employeeCanEditButton = employeeCanEditButton;
    }
}
