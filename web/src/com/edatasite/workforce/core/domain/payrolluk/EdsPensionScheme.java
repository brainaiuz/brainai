package com.edatasite.workforce.core.domain.payrolluk;


import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReference;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 10, 2009
 * Time: 6:42:45 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "pensionscheme")
public class EdsPensionScheme extends EdsObject {

    public static final String PENSION_SCHEME_TYPE = "PENSION_SCHEME_TYPE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "providerid")
    private EdsPensionProvider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "typeid")
    private EdsReference type;

    private String otherAcRef;

    private Integer deductionType; /*0 - Fixed, 1- Percentage*/
    private BigDecimal deductionValue;
    private BigDecimal nonLocalDeductionValue;
    private Integer deductFrom; /*0 - from Basic Salary, 1 - from Basic Salary+Allowance*/
    private Integer allowTaxRelief;
    private Integer reduceByBasicRateTax;
    private Integer sspPayment;
    private Integer smpPayment;
    private Integer sapPayment;
    private Integer sppPayment;

    private Integer employerDeductionType;
    private BigDecimal employerDeductionValue;
    private BigDecimal employerNonLocalDeductionValue;
    private Integer employerSspPayment;
    private Integer employerSmpPayment;
    private Integer employerSapPayment;
    private Integer employerSppPayment;
    @Column(name = "empMaxTaxableAmount", columnDefinition = "numeric default 0", precision = 25, scale = 5)
    private BigDecimal empMaxTaxableAmount = BigDecimal.ZERO;
    @Column(name = "compMaxTaxableAmount", columnDefinition = "numeric default 0", precision = 25, scale = 5)
    private BigDecimal compMaxTaxableAmount = BigDecimal.ZERO;
    private Integer wagesInsufficient;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "pensionScheme")
    @Where(clause = "(deleted = 'false' or deleted is null)")
    private List<EdsPayrollCategory> categories = new ArrayList<>();

    private String countryCode;

    private Boolean deleted;

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

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

    public EdsPensionProvider getProvider() {
        return provider;
    }

    public void setProvider(EdsPensionProvider provider) {
        this.provider = provider;
    }

    public EdsReference getType() {
        return type;
    }

    public void setType(EdsReference type) {
        this.type = type;
    }

    public String getOtherAcRef() {
        return otherAcRef;
    }

    public void setOtherAcRef(String otherAcRef) {
        this.otherAcRef = otherAcRef;
    }

    public Integer getDeductionType() {
        return deductionType;
    }

    public void setDeductionType(Integer deductionType) {
        this.deductionType = deductionType;
    }

    public BigDecimal getDeductionValue() {
        return deductionValue;
    }

    public void setDeductionValue(BigDecimal deductionValue) {
        this.deductionValue = deductionValue;
    }

    public BigDecimal getNonLocalDeductionValue() {
        return nonLocalDeductionValue;
    }

    public void setNonLocalDeductionValue(BigDecimal nonLocalDeductionValue) {
        this.nonLocalDeductionValue = nonLocalDeductionValue;
    }

    public Integer getDeductFrom() {
        return deductFrom;
    }

    public void setDeductFrom(Integer deductFrom) {
        this.deductFrom = deductFrom;
    }

    public Integer getAllowTaxRelief() {
        return allowTaxRelief;
    }

    public void setAllowTaxRelief(Integer allowTaxRelief) {
        this.allowTaxRelief = allowTaxRelief;
    }

    public Integer getReduceByBasicRateTax() {
        return reduceByBasicRateTax;
    }

    public void setReduceByBasicRateTax(Integer reduceByBasicRateTax) {
        this.reduceByBasicRateTax = reduceByBasicRateTax;
    }

    public Integer getSspPayment() {
        return sspPayment;
    }

    public void setSspPayment(Integer sspPayment) {
        this.sspPayment = sspPayment;
    }

    public Integer getSmpPayment() {
        return smpPayment;
    }

    public void setSmpPayment(Integer smpPayment) {
        this.smpPayment = smpPayment;
    }

    public Integer getSapPayment() {
        return sapPayment;
    }

    public void setSapPayment(Integer sapPayment) {
        this.sapPayment = sapPayment;
    }

    public Integer getSppPayment() {
        return sppPayment;
    }

    public void setSppPayment(Integer sppPayment) {
        this.sppPayment = sppPayment;
    }

    public Integer getEmployerDeductionType() {
        return employerDeductionType;
    }

    public void setEmployerDeductionType(Integer employerDeductionType) {
        this.employerDeductionType = employerDeductionType;
    }

    public BigDecimal getEmployerDeductionValue() {
        return employerDeductionValue;
    }

    public void setEmployerDeductionValue(BigDecimal employerDeductionValue) {
        this.employerDeductionValue = employerDeductionValue;
    }

    public BigDecimal getEmployerNonLocalDeductionValue() {
        return employerNonLocalDeductionValue;
    }

    public void setEmployerNonLocalDeductionValue(BigDecimal employerNonLocalDeductionValue) {
        this.employerNonLocalDeductionValue = employerNonLocalDeductionValue;
    }

    public List<EdsPayrollCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<EdsPayrollCategory> categories) {
        this.categories = categories;
    }

    public Integer getEmployerSspPayment() {
        return employerSspPayment;
    }

    public void setEmployerSspPayment(Integer employerSspPayment) {
        this.employerSspPayment = employerSspPayment;
    }

    public Integer getEmployerSmpPayment() {
        return employerSmpPayment;
    }

    public void setEmployerSmpPayment(Integer employerSmpPayment) {
        this.employerSmpPayment = employerSmpPayment;
    }

    public Integer getEmployerSapPayment() {
        return employerSapPayment;
    }

    public void setEmployerSapPayment(Integer employerSapPayment) {
        this.employerSapPayment = employerSapPayment;
    }

    public Integer getEmployerSppPayment() {
        return employerSppPayment;
    }

    public void setEmployerSppPayment(Integer employerSppPayment) {
        this.employerSppPayment = employerSppPayment;
    }

    public Integer getWagesInsufficient() {
        return wagesInsufficient;
    }

    public void setWagesInsufficient(Integer wagesInsufficient) {
        this.wagesInsufficient = wagesInsufficient;
    }

    public BigDecimal getEmpMaxTaxableAmount() {
        return empMaxTaxableAmount;
    }

    public void setEmpMaxTaxableAmount(BigDecimal empMaxTaxableAmount) {
        this.empMaxTaxableAmount = empMaxTaxableAmount;
    }

    public BigDecimal getCompMaxTaxableAmount() {
        return compMaxTaxableAmount;
    }

    public void setCompMaxTaxableAmount(BigDecimal compMaxTaxableAmount) {
        this.compMaxTaxableAmount = compMaxTaxableAmount;
    }
}
