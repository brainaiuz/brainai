package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.enums.DeviceTypeEnum;
import com.edatasite.workforce.core.domain.enums.HistoryType;
import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.core.domain.settings.EdsChanges;
import com.edatasite.workforce.core.domain.settings.EdsWfpNotification;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.gwt.core.client.enums.RegistrationTypeEnum;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.Inducer;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.documents.client.rest.resource.UserResource;
import com.edatasite.workforce.rest.base.enums.NameOrder;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * User: Lochin
 * Date: 20.06.2007
 * Time: 14:20:50
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "myUser", uniqueConstraints = @UniqueConstraint(columnNames = {"uuid"}))
public class EdsUser extends EdsTraceable implements Constants, Inducer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String uuid = String.valueOf(UUID.randomUUID());

    @Column(name = "userName")
    private String userName;

    @Transient
    private String password;

    @Transient
    private String oldPassword;

    @Column(name = "email")
    private String email;

    @Column
    private String tempSalt;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "middleName")
    private String middleName;

    @Column(name = "indexed")
    private Boolean indexed;

    @Column(name = "random")
    private String random;

    @Column(name = "active")
    private Boolean active = false;

    @Column(name = "deceased")
    private Boolean deceased = false;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "createdFromFederateLogin")
    private Boolean fromFederated = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photoId")
    private EdsUpload photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localeid")
    @ForeignKey(name = "none")
    private EdsLocale locale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locationId")
    private EdsLocation location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountstatusid")
    private EdsReference accountStatus;

    @OneToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "landingid")
    private EdsLanding landing;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryZoneId")
    @ForeignKey(name = "none")
    private EdsCountryZone countryZone;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "myuser_role",
            joinColumns = {@JoinColumn(name = "users_id")},
            inverseJoinColumns = {@JoinColumn(name = "roles_id")}
    )
    private Set<EdsRole> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "myuser_trusteegroup",
            joinColumns = {@JoinColumn(name = "myuser_id")},
            inverseJoinColumns = {@JoinColumn(name = "membershipgroups_id")})
    private Set<EdsGroup> membershipGroups = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "user")
    private List<EdsWfpNotification> notifications = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "userHistory")
    private Set<EdsChanges> userHistory = new HashSet<>();

    @Column(name = "created_date")
    private Date createdDate = new Date();

    @Column(name = "temporaryKey")
    private String temporaryKey;

    @Column(name = "deviceType")
    @Enumerated(EnumType.STRING)
    private DeviceTypeEnum deviceType = DeviceTypeEnum.Browser;

    @Column(name = "mobileDeviceType")
    @Enumerated(EnumType.STRING)
    private DeviceTypeEnum mobileDeviceType;

    @Column(name = "deviceToken")
    private String deviceToken;

    @Column(name = "registrationType")
    @Enumerated(EnumType.STRING)
    private RegistrationTypeEnum registrationType;

    private String socialImageUrl;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "autoAsync")
    private Boolean autoAsync = false;

    /* 1 - WFT_USER, 2 - CLIENT_CONTACT, 3 - BMT_RESPONDENT */
    @Column(name = "usertype")
    private Integer userType = USER_TYPE_WFT_CORE_USER;

    @Column(name = "facebookToken")
    private String facebookToken;

    private String linkedInToken;

    @Column(name = "calendarScrollToHour")
    private Integer calendarScrollToHour = 0;

    @Column(name = "calendarid")
    @Type(type = "text")
    private String calendarID;

    @Column(name = "taskCalendarid")
    @Type(type = "text")
    private String taskCalendarID;

    ///Just holder field to reduce parameter passing during the session creation. Does not persisted into database:Transient
    @Transient
    private Boolean isNewUser = false;

    @Transient
    private String userAgent;

    @Transient
    private boolean shared;

    @Transient
    private Set<EdsRole> artificialRoles = new HashSet<>();

    public String getFullName() {
        return formatName(ServerUtils.getNameFormat(), firstName, middleName, lastName, true);
    }

    public String getFirstAndLastname(){
        return formatName(NameOrder.FIRST_LAST, firstName, middleName, lastName, true);
    }


    public String getInitialName() {
        return (StringUtils.isNotBlank(firstName) ? firstName.substring(0, 1) : "") + (StringUtils.isNotBlank(lastName) ? lastName.substring(0, 1) : "");
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Set<EdsGroup> getMembershipGroups() {
        return membershipGroups;
    }

    public void setMembershipGroups(Set<EdsGroup> membershipGroups) {
        this.membershipGroups = membershipGroups;
    }

    public String getName() {
        return getFormmattedName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EdsObject unObject)) {
            return false;
        }
        if (getObjectID() == null) {
            return false;
        }

        return getObjectID().equals(unObject.getObjectID());
    }

    /**
     * At that point the username value is taken eather from cache(in case this is ) or from the query result
     *
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName != null ? userName.toLowerCase() : userName;
    }

    /**
     * This method returns md5 hash of user's password. It does NOT give you user's actual password!!!
     *
     * @return md5 hash of user's password
     */
    public String getPassword() {
        if (password != null) {
            return password;
        }
        UserManager userManager = StaticContextAccessor.getBean(UserManager.class);
        this.password = userManager.findActiveAndNonFederateLoginUsers(EdsContextParams.getHostname(), userName);
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTempSalt() {
        return tempSalt;
    }

    public void setTempSalt(String salt) {
        this.tempSalt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!ServerUtils.equalsString(this.email, email != null ? email.toLowerCase() : email)) {
            addHistoryChange("Email", this.email, email != null ? email.toLowerCase() : email);
        }
        this.email = email != null ? email.toLowerCase() : email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (!ServerUtils.equalsString(this.firstName, firstName)) {
            addHistoryChange("First Name", this.firstName, firstName);
        }
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (!ServerUtils.equalsString(this.lastName, lastName)) {
            addHistoryChange("Last Name", this.lastName, lastName);
        }
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        if (!ServerUtils.equalsString(this.middleName, middleName)) {
            addHistoryChange("Middle Name", this.middleName, middleName);
        }
        this.middleName = middleName;
    }

    public boolean isEmployee() {
        return this instanceof EdsEmployee;
    }

    public EdsEmployee getEmployee() {
        return (EdsEmployee) this;
    }

    public EdsClientContact getClientContact() {
        return (EdsClientContact) this;
    }

    public boolean isClientContact() {
        return this instanceof EdsClientContact;
    }

    public boolean isUserContact() {
        return this instanceof EdsUserContact;
    }

    public Set<EdsRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<EdsRole> roles) {
        this.roles = roles;
    }

    @Deprecated
    public Set<Integer> getRoleIds() {
        Set<Integer> roleIds = new HashSet<>();
        for (EdsRole role : getRoles()) {
            roleIds.add(role.getObjectID());
        }
        return roleIds;
    }

    public Set<String> getRoleCODEs() {
        return getRoles().stream().map(EdsRole::getCode).collect(Collectors.toSet());
    }

    public void addRole(EdsRole role) {
        getRoles().add(role);
    }

    public boolean hasRole(EdsRole role) {
        return roles.contains(role);
    }

    public boolean hasRole(String roleCode) {
        return roles == null || roles.isEmpty() || roles.stream().anyMatch(role -> role != null && role.getCode().equals(roleCode));
    }

    public boolean hasRoles(EdsRole... roles) {//and
        return getRoles().containsAll(Arrays.asList(roles));
    }

    @Deprecated
    public boolean hasRoles(Integer... roleIDs) {
        Set<Integer> userRoleIds = getRoleIds();
        if (userRoleIds == null || userRoleIds.isEmpty()) {
            return false;
        }
        return Arrays.stream(roleIDs).allMatch(userRoleIds::contains);
    }

    public boolean hasEitherRoles(String... roleCODEs) {
        Set<String> userRoleCODEs = getRoleCODEs();
        return Arrays.stream(roleCODEs).anyMatch(userRoleCODEs::contains);
    }

    public boolean hasEitherRoles(Integer... roleIDs) {
        Set<Integer> userRoleIds = getRoleIds();
        return Arrays.stream(roleIDs).anyMatch(userRoleIds::contains);
    }

    public boolean hasEitherRoles(EdsRole... roles) {
        Set<EdsRole> userRoles = getRoles();
        return Arrays.stream(roles).anyMatch(userRoles::contains);
    }

    public void setIndexed(Boolean indexed) {
        this.indexed = indexed;
    }

    public Boolean isIndexed() {
        return indexed;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public List<EdsRole> getRolesSorted() {
        return roles.stream().sorted(Comparator.comparing(EdsRole::getName)).collect(Collectors.toList());
    }

    public String getRolesAsIntegersString() {
        return roles.stream()
                .map(EdsRole::getObjectID)
                .map(Object::toString).collect(Collectors.joining(","));
    }

    public String getRolesCodeAsString() {
        return roles.stream()
                .filter(EdsRole::isActive)
                .map(EdsRole::getCode)
                .map(s -> "'".concat(s).concat("'")).collect(Collectors.joining(","));
    }

    public String getRolesNameAsString() {
        return getRoles().stream()
                .map(EdsRole::getName)
                .collect(Collectors.joining(","));
    }

    @Deprecated // use getAccountStatus instead
    public Boolean getActive() {
        return active;
    }

    @Deprecated // use setAccountStatus instead
    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getDeceased() {
        return deceased;
    }

    public void setDeceased(Boolean deceased) {
        this.deceased = deceased;
    }

    public boolean getDeleted() {
        return deleted != null && deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EdsReference getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(EdsReference accountStatus) {
        if (!ServerUtils.equalsEdsObject(this.accountStatus, accountStatus)) {
            addHistoryChange("Account status", this.accountStatus != null ? this.accountStatus.getName() : "", accountStatus != null ? accountStatus.getName() : "");
        }
        this.accountStatus = accountStatus;
    }

    public EdsLanding getLanding() {
        return landing;
    }

    public void setLanding(EdsLanding landing) {
        this.landing = landing;
    }

    public EdsLocation getLocation() {
        return location;
    }

    public void setLocation(EdsLocation location) {
        if (!ServerUtils.equalsEdsObject(this.location, location)) {
            addHistoryChange("Location", this.location != null ? this.location.getName() : "", location != null ? location.getName() : "");
        }
        this.location = location;
    }

    public List<EdsRole> getRolesSortedByPattern() {
        return getRolesSortedByPattern(new Integer[]{EdsRole.ADMIN, EdsRole.DR, EdsRole.ADMIN_LOCATION, EdsRole.PM, EdsRole.TL, EdsRole.ACCOUNTANT, EdsRole.MEM, EdsRole.CLIENT, EdsRole.HR, EdsRole.SALESMAN, EdsRole.SALESPERSON, EdsRole.CUSTOMER_SERVICE_REPRESENTATIVE, EdsRole.CHAT_EXPERT, EdsRole.GUEST, EdsRole.CUSTOM_MEMBER, EdsRole.PROJECTS_DIRECTOR, EdsRole.AUDITOR});
    }

    public List<EdsRole> getRolesSortedByPattern(Integer[] sortPattern) {
        List<EdsRole> rolesList = new ArrayList<>();
        for (Integer aSortPattern : sortPattern) {
            for (EdsRole role : roles) {
                if (role.getObjectID().equals(aSortPattern)) {
                    rolesList.add(role);
                }
            }
        }
        if (rolesList.isEmpty()) {
            rolesList.addAll(roles);
        }
        return rolesList;
    }

    private String getRolesAsString(Set<EdsRole> roles) {
        String rolesString = "";
        for (EdsRole role : roles) {
            rolesString = role.getName();
        }
        return rolesString;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public EdsCompany getCompany() {//if the EdsUser instance exists, the company that contains it(the schema that contains it) obviously exists! :)
        CompanyManager companyManager = StaticContextAccessor.getBean(CompanyManager.class);
        return companyManager.get(Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId()));
    }

    public Boolean isFromFederated() {
        return fromFederated;
    }

    public void setFromFederated(Boolean fromFederated) {
        this.fromFederated = fromFederated;
    }

    public EdsUpload getPhoto() {
        return photo;
    }

    public void setPhoto(EdsUpload photo) {
        this.photo = photo;
    }

    public EdsLocale getLocale() {
        return locale;
    }

    public void setLocale(EdsLocale locale) {
        this.locale = locale;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public TimeZone getUserTimezone() {
        return timezone != null ? TimeZone.getTimeZone(timezone) : TimeZone.getDefault();
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public Boolean isAutoAsync() {
        return autoAsync;
    }

    public void setAutoAsync(Boolean autoAsync) {
        this.autoAsync = autoAsync;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUserDate() throws RuntimeException {
        return getUserDate(new Date());
    }

    public Date getUserDate(Date serverDate) throws RuntimeException {
        if (serverDate == null) {
            return null;
        }
        try {
            SimpleDateFormat dateFormat1 = new SimpleDateFormat();
            dateFormat1.setTimeZone(getUserTimezone());
            String s = dateFormat1.format(serverDate);
            SimpleDateFormat dateFormat2 = new SimpleDateFormat();
            return dateFormat2.parse(s);
        } catch (ParseException pe) {
            throw new RuntimeException(pe);
        }
    }

    public Date getServerDateByUserDate(Date userDate) throws RuntimeException {
        if (userDate == null) {
            return null;
        }
        try {
            SimpleDateFormat dateFormat1 = new SimpleDateFormat();
            String s = dateFormat1.format(userDate);
            SimpleDateFormat dateFormat2 = new SimpleDateFormat();
            dateFormat2.setTimeZone(getUserTimezone());
            return dateFormat2.parse(s);
        } catch (ParseException pe) {
            throw new RuntimeException(pe);
        }
    }

    public UserResource getDTO() {
        final UserResource u = new UserResource();
        u.setObjectId(objectID);
        u.setName(getFullName());
        u.setLastName("");
        u.setEmail(email);
        u.setUsername(email);
        return u;
    }

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public List<EdsWfpNotification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<EdsWfpNotification> notifications) {
        this.notifications = notifications;
    }

    public void addArtificialRole(EdsRole role) {
        if (role != null && role.isActive()) {
            artificialRoles.add(role);
        }
    }

    public void clearArtificialRoles() {
        artificialRoles.clear();
    }

    public EdsCountryZone getCountryZone() {
        return countryZone;
    }

    public void setCountryZone(EdsCountryZone countryZoneID) {
        this.countryZone = countryZoneID;
    }

    public TimeZone getTimeZone() {
        return (countryZone != null && countryZone.getZone() != null) ? TimeZone.getTimeZone(countryZone.getZone().getZoneID()) : null;
    }

    public String getFacebookToken() {
        return (facebookToken != null && !facebookToken.isEmpty()) ? facebookToken : null;
    }

    public void setFacebookToken(String facebookToken) {
        this.facebookToken = facebookToken;
    }

    public String getLinkedInToken() {
        return (linkedInToken != null && !linkedInToken.isEmpty()) ? linkedInToken : null;
    }

    public void setLinkedInToken(String linkedInToken) {
        this.linkedInToken = linkedInToken;
    }

    public String getTemporaryKey() {
        return temporaryKey;
    }

    public void setTemporaryKey(String temporaryKey) {
        this.temporaryKey = temporaryKey;
    }

    public String getCalendarID() {
        return calendarID;
    }

    public void setCalendarID(String calendarID) {
        this.calendarID = calendarID;
    }

    public String getTaskCalendarID() {
        return taskCalendarID;
    }

    public void setTaskCalendarID(String taskCalendarID) {
        this.taskCalendarID = taskCalendarID;
    }

    public DeviceTypeEnum getDeviceType() {
        return deviceType != null ? deviceType : DeviceTypeEnum.Browser;
    }

    public void setDeviceType(DeviceTypeEnum deviceType) {
        this.deviceType = deviceType;
    }

    public DeviceTypeEnum getMobileDeviceType() {
        return mobileDeviceType;
    }

    public void setMobileDeviceType(DeviceTypeEnum mobileDeviceType) {
        this.mobileDeviceType = mobileDeviceType;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Integer getCalendarScrollToHour() {
        return calendarScrollToHour;
    }

    public void setCalendarScrollToHour(Integer calendarScrollToHour) {
        this.calendarScrollToHour = calendarScrollToHour;
    }

    public Set<EdsChanges> getUserHistory() {
        return userHistory;
    }

    public void setUserHistory(Set<EdsChanges> userHistory) {
        if (userHistory == null) {
            userHistory = new HashSet<>();
        }
        this.userHistory = userHistory;
    }

    public Boolean isNewUser() {
        return isNewUser == null ? false : isNewUser;
    }

    public void setNewUser(Boolean newUser) {
        isNewUser = newUser;
    }

    public RegistrationTypeEnum getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(RegistrationTypeEnum registrationType) {
        this.registrationType = registrationType;
    }

    public String getSocialImageUrl() {
        return socialImageUrl;
    }

    public void setSocialImageUrl(String socialImageUrl) {
        this.socialImageUrl = socialImageUrl;
    }

    public void addHistoryChange(String field, Object oldValue, Object newValue, boolean... isFromCandidate) {
        if ((getObjectID() != null && !isNewUser() && isEmployee()) || (isFromCandidate != null && isFromCandidate.length > 0 && isFromCandidate[0])) {
            EdsChanges change = new EdsChanges();
            change.setField(field);
            change.setHistoryType(HistoryType.EMPLOYEE);
            change.setEntityID(getObjectID());
            change.setEntityName(getFullName());
            if (oldValue instanceof String || oldValue instanceof Double) {
                oldValue = oldValue == null ? "" : oldValue;
                change.setFromStringValue(String.valueOf(oldValue));
            } else if (oldValue instanceof Number) {
                change.setFromNumberValue((BigDecimal) oldValue);
            } else if (oldValue instanceof Date) {
                change.setFromDateValue((Date) oldValue);
            } else if (oldValue instanceof Boolean) {
                change.setFromStringValue((Boolean) oldValue ? "Yes" : "No");
            }
            if (newValue instanceof String || newValue instanceof Double) {
                newValue = newValue == null ? "" : newValue;
                change.setToStringValue(String.valueOf(newValue));
            } else if (newValue instanceof Number) {
                change.setToNumberValue((BigDecimal) newValue);
            } else if (newValue instanceof Date) {
                change.setToDateValue((Date) newValue);
            } else if (newValue instanceof Boolean) {
                change.setToStringValue((Boolean) newValue ? "Yes" : "No");
            }
            change.setModificationDate(new Date());
            change.setUpdater((EdsUser) SecurityContext.getInstance().getUser());
            change.setSuperUser(ServerUtils.isSuperUser());
            getUserHistory().add(change);
            addChange(field);
        }
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }


    public String getFormmattedName() {
        return formatName(ServerUtils.getNameFormat(), firstName, middleName, lastName, false);
    }

    public static String formatName(NameOrder order, String firstName, String middleName, String lastName, boolean cutMiddleName) {
        List<String> nameParts = new ArrayList<>();

        switch (order) {
            case FIRST_MIDDLE_LAST:
                addNamePart(nameParts, firstName);
                addMiddleNamePart(nameParts, middleName, cutMiddleName);
                addNamePart(nameParts, lastName);
                break;
            case MIDDLE_FIRST_LAST:
                addMiddleNamePart(nameParts, middleName, cutMiddleName);
                addNamePart(nameParts, firstName);
                addNamePart(nameParts, lastName);
                break;
            case MIDDLE_LAST_FIRST:
                addMiddleNamePart(nameParts, middleName, cutMiddleName);
                addNamePart(nameParts, lastName);
                addNamePart(nameParts, firstName);
                break;
            case LAST_FIRST_MIDDLE:
                addNamePart(nameParts, lastName);
                addNamePart(nameParts, firstName);
                addMiddleNamePart(nameParts, middleName, cutMiddleName);
                break;
            case LAST_MIDDLE_FIRST:
                addNamePart(nameParts, lastName);
                addMiddleNamePart(nameParts, middleName, cutMiddleName);
                addNamePart(nameParts, firstName);
                break;
            case LAST_FIRST:
                addNamePart(nameParts, lastName);
                addNamePart(nameParts, firstName);
                break;
            case FIRST_LAST:
                addNamePart(nameParts, firstName);
                addNamePart(nameParts, lastName);
                break;
            default:
                addNamePart(nameParts, firstName);
                addNamePart(nameParts, lastName);
                addMiddleNamePart(nameParts, middleName, cutMiddleName);
        }

        return String.join(" ", nameParts);
    }

    private static void addNamePart(List<String> nameParts, String namePart) {
        if (namePart != null && !namePart.isEmpty()) {
            String trim = namePart.trim();
            nameParts.add(trim);
        }
    }

    private static void addMiddleNamePart(List<String> nameParts, String middleName, boolean cutMiddleName) {
        if (!cutMiddleName) {
            addNamePart(nameParts, middleName);
        }
    }
}
