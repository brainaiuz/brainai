package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveBalanceSettings;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import org.apache.commons.lang.StringUtils;
import org.hibernate.LazyInitializationException;
import org.hibernate.annotations.Where;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "Reference")
@Inheritance(strategy = InheritanceType.JOINED)
public class EdsReference extends EdsObject {

    public static final boolean INCLUDE_HOLIDAY_DEFAULT = false;
    public static final boolean INCLUDE_DAYOFF_DEFAULT = false;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    private EdsReference parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Where(clause = "deleted = 'false'")
    private List<EdsReference> childlist = new ArrayList<>();

    private String code;

    private String name;

    private String antonym;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer sorder;

    private Boolean includeDayOffs;

    private Boolean includeHolidays;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY, mappedBy = "leaveType")
    private List<EdsLeaveBalanceSettings> settingItems = new ArrayList<>();

    @Column(nullable = false, columnDefinition = " boolean DEFAULT false")
    private boolean isSystemReference = false;

    @Column(nullable = false, columnDefinition = " boolean DEFAULT true")
    private boolean isRemovable = true;

    @Column(name = "deleted", nullable = false, columnDefinition = " boolean DEFAULT false")
    private boolean deleted = false;
    private String cssStyle;

    @Column(name = "shared", nullable = false, columnDefinition = " boolean DEFAULT true")
    private final boolean shared = true;

    private String relative;

    @Column(name = "isCustomButton", nullable = false, columnDefinition = " boolean DEFAULT false")
    private boolean isCustomButton = false;

    private String buttonLocation;

    @Column(name = "isActive", nullable = false, columnDefinition = " boolean DEFAULT true")
    private boolean isActive = true;

    @Column(name = "hasProrata", nullable = false, columnDefinition = " boolean DEFAULT false")
    private boolean hasProrata = false;

    @Column(name = "requiredComment", nullable = false, columnDefinition = " boolean DEFAULT false")
    private boolean requiredComment = false;

    @Column(name = "leaveDays", nullable = false, columnDefinition = "Decimal(10,2) default 0.00")
    private Double leaveDays = 0.0;

    @Column(name = "shortname")
    private String shortName;

    @Column(name = "color")
    private String color;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "referenceColorId", insertable = false, updatable = false)
    private EdsReferenceColor referenceColor;
    private Integer referenceColorId;

    private Date updatedDate;

    private Integer updater;


    @Column(name = "attendanceLR", columnDefinition = " boolean DEFAULT false")
    private Boolean attendanceLR;
    @Column(name = "autoApprove", columnDefinition = " boolean DEFAULT false")
    private Boolean autoApprove;

    @ManyToMany(cascade = {CascadeType.PERSIST})
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "reference_permissions",
            joinColumns = {@JoinColumn(name = "reference_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<EdsRole> allowedRoles = new HashSet<>();



    @ManyToMany(cascade = {CascadeType.PERSIST})
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "reference_view_permissions",
            joinColumns = {@JoinColumn(name = "reference_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<EdsRole> viewOnlyRoles = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST})
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "opportunity_edit_button_permissions",
            joinColumns = {@JoinColumn(name = "reference_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<EdsRole> oppEditBtnRole = new HashSet<>();
    @ManyToMany(cascade = {CascadeType.PERSIST})
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "employee_edit_permissions",
            joinColumns = {@JoinColumn(name = "reference_id")},
            inverseJoinColumns = {@JoinColumn(name = "employee_id")}
    )
    private Set<EdsEmployee> employeesCanEdit = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST})
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "employee_view_permissions",
            joinColumns = {@JoinColumn(name = "reference_id")},
            inverseJoinColumns = {@JoinColumn(name = "employee_id")}
    )
    private Set<EdsEmployee> employeesCanView = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST})
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "employee_editbtn_permissions",
            joinColumns = {@JoinColumn(name = "reference_id")},
            inverseJoinColumns = {@JoinColumn(name = "employee_id")}
    )
    private Set<EdsEmployee> employeesCanEditBtn = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localeId")
    private EdsReferenceLocale locale;


    @Column(name = "changed", nullable = false, columnDefinition = " boolean DEFAULT false")
    private Boolean changed = Boolean.FALSE;

    public EdsReference() {
    }

    public EdsReference(String code, String name) {
        super();
        setCode(code);
        setName(name);
    }

    public EdsReference getParent() {
        return parent;
    }

    public void setParent(EdsReference parent) {
        this.parent = parent;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        if (getLocale() != null) {
            String lang = ServerUtils.getUserLocale().getLanguage();
            if (StringUtils.isNotBlank(getLocale().getLocaleByCode(lang))) {
                return getLocale().getLocaleByCode(lang);
            }
        } else if (isSystemReference() && !isChanged()) {
            if (ApplicationContextProvider.applicationContext != null) {
                WfmResourceBundleMessageSource referenceWfmMessageSource = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("referenceWfmMessageSource");
                return referenceWfmMessageSource.localize(code, name);
            }
        }
        return name;
    }

    public String getOriginalName() {
        return name;
    }

    public String getLocalizedName() {
        return ServerUtils.initReferenceLocalizer().localize(getCode(), getName());
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAntonym() {
        return antonym;
    }

    public void setAntonym(String antonym) {
        this.antonym = antonym;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSorder() {
        return sorder;
    }

    public void setSorder(Integer sorder) {
        this.sorder = sorder;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public void setIndexed(Boolean indexed) {
    }

    public String getCssStyle() {
        return cssStyle;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setCssStyle(String cssStyle) {
        if (cssStyle != null && cssStyle.contains("\"")) {
            cssStyle = cssStyle.replace("\"", "");
        }
        this.cssStyle = cssStyle;
    }

    public Set<EdsRole> getAllowedRoles() {
        if (allowedRoles == null) {
            allowedRoles = new HashSet<>();
        }
        return allowedRoles;
    }

    public void setAllowedRoles(Set<EdsRole> accessedRoles) {
        this.allowedRoles = accessedRoles;
    }

    public Set<EdsRole> getViewOnlyRoles() {
        if (viewOnlyRoles == null) {
            viewOnlyRoles = new HashSet<>();
        }
        return this.viewOnlyRoles;
    }

    public void setViewOnlyRoles(final Set<EdsRole> viewOnlyRoles) {
        this.viewOnlyRoles = viewOnlyRoles;
    }

    public Set<EdsRole> getOppEditBtnRole() {
        if (oppEditBtnRole == null) {
            oppEditBtnRole = new HashSet<>();
        }
        return this.oppEditBtnRole;
    }

    public void setOppEditBtnRole(final Set<EdsRole> oppEditBtnRole) {
        this.oppEditBtnRole = oppEditBtnRole;
    }

    public boolean isHasProrata() {
        return hasProrata;
    }

    public void setHasProrata(boolean hasProrata) {
        this.hasProrata = hasProrata;
    }

    public ReferenceItem getRPC() {
        return getRPC(null);
    }

    public ReferenceItem getRPC(List<EdsRole> roles) {
        ReferenceItem item = new ReferenceItem(getObjectID(), getLocalizedName(), getDescription(), getCssStyle(), getAntonym());
        item.setCode(getCode());
        item.setTextDescription(getDescription());
        item.setDescription(StringUtils.isEmpty(getDescription()) ? getCode() : getDescription());
        try {
            if (getParent() != null) {
                item.setParentID(getParent().getObjectID());
                item.setParent(getParent().getName());
                item.setParentCode(getParent().getCode());
            }
        } catch (LazyInitializationException e) {

        }
        if (getReferenceColor() != null) {
            item.setColorId(getReferenceColor().getObjectID());
            item.setColorName(getReferenceColor().getName());
            item.setColorHex(getReferenceColor().getHex());
        }
        item.setActive(isActive());
        item.setOrder(getSorder());
        item.setRemovable(isRemovable());
        item.setSystemReference(isSystemReference());
        item.setCustomButton(isCustomButton());
        item.setButtonLocation(getButtonLocation());
        item.setDayOffIncluded(getIncludeDayOffs() != null ? getIncludeDayOffs() : false);
        item.setLeaveDays(getLeaveDays());
        item.setIncludeHolidays(getIncludeHolidays());
        item.setIncludeDayOffs(getIncludeDayOffs());
        item.setHexColor(getColor());
        item.setShortName(getShortName());
        item.setRequiredComment(isRequiredComment());
        item.setAttendanceLR(getAttendanceLR());
        item.setAutoApprove(getAutoApprove());
        item.setHasProrata(isHasProrata());
        item.setOriginalName(getOriginalName());
        List<EdsLeaveBalanceSettings> elbsList = getSettingItems();
        if (elbsList.size() > 0) {
            ArrayList<LeaveBalanceSettings> leaveBalanceSettingsList = new ArrayList<>();
            for (EdsLeaveBalanceSettings elbs : elbsList) {
                leaveBalanceSettingsList.add(elbs.getRPC());
            }
            item.setLeaveBalanceSettings(leaveBalanceSettingsList);
        }


        ArrayList<SelectItem> roleList = new ArrayList<>();
        if (allowedRoles != null) {
            for (EdsRole role : allowedRoles) {
                roleList.add(new SelectItem(role.getObjectID(), role.getName(), "", true));
            }
        }
        if (roles != null) {
            for (EdsRole role : roles) {
                if (!allowedRoles.contains(role)) {
                    roleList.add(new SelectItem(role.getObjectID(), role.getName()));
                }
            }
        }

        if (getLocale() != null) {
            item.setLocale(getLocale().toRPC());
        }

        ArrayList<SelectItem> viewRoleList = new ArrayList<>();
        if (viewOnlyRoles != null) {
            for (EdsRole role : viewOnlyRoles) {
                viewRoleList.add(new SelectItem(role.getObjectID(), role.getName(), "", true));
            }
        }

        ArrayList<SelectItem> oppEditBtnRoleList = new ArrayList<>();
        if (oppEditBtnRole != null) {
            for (EdsRole role : oppEditBtnRole) {
                oppEditBtnRoleList.add(new SelectItem(role.getObjectID(), role.getName(), "", true));
            }
        }
        if (roles != null) {
            for (EdsRole role : roles) {
                if (!oppEditBtnRole.contains(role)) {
                    oppEditBtnRoleList.add(new SelectItem(role.getObjectID(), role.getName()));
                }
            }
        }


        ArrayList<SelectItem> employeeCanEditList = new ArrayList<>();
        if (employeesCanEdit != null) {
            for (EdsEmployee employee : employeesCanEdit) {
                employeeCanEditList.add(new SelectItem(employee.getObjectID(), employee.getName(), "", true));
            }
        }

        ArrayList<SelectItem> employeeCanViewList = new ArrayList<>();
        if (employeesCanView != null) {
            for (EdsEmployee employee : employeesCanView) {
                employeeCanViewList.add(new SelectItem(employee.getObjectID(), employee.getName(), "", true));
            }
        }

        ArrayList<SelectItem> employeeCanEditBtnList = new ArrayList<>();
        if (employeesCanEditBtn != null) {
            for (EdsEmployee employee : employeesCanEditBtn) {
                employeeCanEditBtnList.add(new SelectItem(employee.getObjectID(), employee.getName(), "", true));
            }
        }

        item.setAllowedRoles(roleList);
        item.setAllowedRolesView(viewRoleList);
        item.setOppEditBtnRole(oppEditBtnRoleList);
        item.setEmployeeCanEdit(employeeCanEditList);
        item.setEmployeeCanView(employeeCanViewList);
        item.setEmployeeCanEditButton(employeeCanEditBtnList);
        return item;
    }

    public static String getSql(String code, String parentCode, boolean isHQL) {
        if (!isHQL) {
            return " from \"" + SecurityContext.getCompanyID() + "\".reference where code = '" + code + "' and parentid = (select id from \"" + SecurityContext.getCompanyID() + "\".reference where code = '" + parentCode + "') ";
        }
        return " from EdsReference r where r.code ='" + code + "' and r.parent.code = '" + parentCode + "'";
    }

    public SelectItem getAsSelectItem() {
        return new SelectItem(getObjectID(), getName(), getCode());
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

    public List<EdsReference> getChildlist() {
        return childlist;
    }

    public void setChildlist(ArrayList<EdsReference> childlist) {
        this.childlist = childlist;
    }

    public Boolean getIncludeDayOffs() {
        return includeDayOffs != null ? includeDayOffs : INCLUDE_DAYOFF_DEFAULT;
    }

    public void setIncludeDayOffs(Boolean includeDayOffs) {
        this.includeDayOffs = includeDayOffs;
    }

    public Boolean getIncludeHolidays() {
        return includeHolidays != null ? includeHolidays : INCLUDE_HOLIDAY_DEFAULT;
    }

    public void setIncludeHolidays(Boolean includeHolidays) {
        this.includeHolidays = includeHolidays;
    }

    public static boolean checkEqaulity(EdsReference realValue, Integer id, String code, String name) {
        if (realValue == null || (id == null && (code == null || "".equals(code)) && (name == null || "".equals(name)))) {
            return false;
        }
        if (id != null) {
            return id.equals(realValue.getObjectID());
        } else if (code != null) {
            return code.equals(realValue.getCode());
        } else if (name != null) {
            return name.equals(realValue.getCode()) || name.equals(realValue.getName());
        }
        return false;
    }

    public Double getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(Double leaveDays) {
        this.leaveDays = leaveDays;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getUpdater() {
        return this.updater;
    }

    public void setUpdater(final Integer updater) {
        this.updater = updater;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<EdsLeaveBalanceSettings> getSettingItems() {
        return settingItems;
    }

    public void setSettingItems(List<EdsLeaveBalanceSettings> settingItems) {
        this.settingItems = settingItems;
    }

    public Boolean getAttendanceLR() {
        if (attendanceLR == null) {
            return false;
        }
        return attendanceLR;
    }

    public void setAttendanceLR(Boolean attendanceLR) {
        this.attendanceLR = attendanceLR;
    }

    public Boolean getAutoApprove() {
        if (autoApprove == null) {
            return false;
        }
        return autoApprove;
    }

    public void setAutoApprove(Boolean autoApprove) {
        this.autoApprove = autoApprove;
    }

    public EdsReferenceColor getReferenceColor() {
        return referenceColor;
    }

    // Not Use this method! Use setReferenceColorId method.
    public void setReferenceColor(EdsReferenceColor referenceColor) {
        this.referenceColor = referenceColor;
    }

    public boolean isRequiredComment() {
        return this.requiredComment;
    }

    public void setRequiredComment(final boolean requiredComment) {
        this.requiredComment = requiredComment;
    }

    public EdsReferenceLocale getLocale() {
        return locale;
    }

    public void setLocale(EdsReferenceLocale locale) {
        this.locale = locale;
    }

    public boolean isChanged() {
        return this.changed;
    }

    public void setChanged(Boolean changed) {
        this.changed = changed;
    }

    public Integer getReferenceColorId() {
        return referenceColorId;
    }

    public void setReferenceColorId(Integer referenceColorId) {
        this.referenceColorId = referenceColorId;
    }

    public Set<EdsEmployee> getEmployeesCanEdit() {
        if (employeesCanEdit == null) {
            employeesCanEdit = new HashSet<>();
        }
        return this.employeesCanEdit;
    }

    public void setEmployeesCanEdit(Set<EdsEmployee> employeesCanEdit) {
        this.employeesCanEdit = employeesCanEdit;
    }

    public Set<EdsEmployee> getEmployeesCanView() {
        if (employeesCanView == null) {
            employeesCanView = new HashSet<>();
        }
        return this.employeesCanView;
    }

    public void setEmployeesCanView(Set<EdsEmployee> employeesCanView) {
        this.employeesCanView = employeesCanView;
    }

    public Set<EdsEmployee> getEmployeesCanEditBtn() {
        if (employeesCanEditBtn == null) {
            employeesCanEditBtn = new HashSet<>();
        }
        return this.employeesCanEditBtn;
    }

    public void setEmployeesCanEditBtn(Set<EdsEmployee> employeesCanEditBtn) {
        this.employeesCanEditBtn = employeesCanEditBtn;
    }
}
