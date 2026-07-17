package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@MappedSuperclass
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "code"))
public class EdsPermissionTable extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "context")
    @Index(name = "permission_context_index")
    private String context;
    @Column(name = "sorder")
    private Integer sorder;
    @Column(name = "isMainMenu")
    private Boolean isMainMenu;
    private Integer parent;
    @Column(name = "description")
    @Type(type = "text")
    private String description;
    @Column(name = "iscore", columnDefinition = "boolean default false")
    private Boolean isCore = false;
    @Column(name = "companyId")
    private Integer companyId;
    @Index(name = "permission_module_index")
    @Column(name = "modulecode")
    private String moduleCode;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "permission_context",
            joinColumns = {@JoinColumn(name = "permissioncode", referencedColumnName = "code")},
            inverseJoinColumns = {@JoinColumn(name = "contextcode", referencedColumnName = "code")})
    private Set<EdsContext> contexts = new HashSet<>();

    @Deprecated
    @Column(name = "isAdvancedMode", columnDefinition = "boolean default false")
    private Boolean isAdvancedMode;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSorder() {
        return sorder;
    }

    public void setSorder(Integer sorder) {
        this.sorder = sorder;
    }


    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCore() {
        return isCore;
    }

    public void setCore(Boolean core) {
        isCore = core;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public Boolean getIsMainMenu() {
        return isMainMenu;
    }

    public void setIsMainMenu(Boolean isMainMenu) {
        this.isMainMenu = isMainMenu;
    }

    public Boolean getIsCore() {
        return isCore;
    }

    public void setIsCore(Boolean isCore) {
        this.isCore = isCore;
    }

    public Boolean isAdvancedMode() {
        return isAdvancedMode;
    }

    public void setAdvancedMode(Boolean advancedMode) {
        this.isAdvancedMode = advancedMode;
    }

    public Set<EdsContext> getContexts() {
        return contexts;
    }

    public void setContexts(Set<EdsContext> contexts) {
        this.contexts = contexts;
    }

}
