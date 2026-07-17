package com.workforcetrack.mobile.rpc.accounting;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import com.workforcetrack.mobile.rpc.base.WebServiceUtils;
import com.workforcetrack.mobile.rpc.expense.MAccountItem;
import com.workforcetrack.mobile.rpc.opportunity.MNumberData;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 24.12.11
 * Time: 17:29
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MFixedAssetItem {
    //private SimpleDateFormat format = new SimpleDateFormat(Utils.getShortDateFormat()); //"MM/dd/yyyy"
    private Integer objectID;
    private MAccountItem account;
    private String code;
    private String name;
    private String description;
    private BigDecimal cost;
    private Date creationDate;
    private BigDecimal usefulLife;
    private BigDecimal residualValue;
    private boolean isEditable;
    private MAccountItem financedByAccount;
    private MNumberData numberData;
    private Integer purchaseInvoiceID;
    private Boolean calculateDepreciation;

    private String barCodeText;


    public MFixedAssetItem() {
    }

    public MFixedAssetItem(FixedAssetItem item) {
        this.objectID = item.getObjectID();
        if (item.getAccount() != null) {
            this.account = new MAccountItem(item.getAccount());
        }
        this.code = item.getCode();
        this.name = item.getName();
        this.description = item.getDescription();
        this.cost = item.getCost();
        if (item.getCreationDate() != null) {
            this.creationDate = item.getCreationDate().getDate();
        }
        this.usefulLife = item.getUsefulLife();
        this.residualValue = item.getResidualValue();
        this.isEditable = item.isEditable();
        if (item.getFinancedByAccount() != null) {
            this.financedByAccount = new MAccountItem(item.getFinancedByAccount());
        }
        if (item.getNumberData() != null) {
            this.numberData = new MNumberData(item.getNumberData());
        }

        this.calculateDepreciation = item.isCalculateDepreciation();
        this.purchaseInvoiceID = item.getPurchaseInvoiceID();
        String companyID = ServerSecurityContext.getInstance().getCompanyId();
        if (companyID != null && !"".equals(companyID)) {
            String formatStr = ServerUtils.getShortDateFormat((EdsUser) ServerSecurityContext.getInstance().getUser());
            SimpleDateFormat format = new SimpleDateFormat(formatStr);
            this.barCodeText = item.getBarcodeGenerateText(companyID, format.format(creationDate));
        }

    }

    public MFixedAssetItem(FixedAssetItem item, Integer companyID, String dateFormat) {
        this.objectID = item.getObjectID();
        if (item.getAccount() != null) {
            this.account = new MAccountItem(item.getAccount());
        }
        this.code = item.getCode();
        this.name = item.getName();
        this.description = item.getDescription();
        this.cost = item.getCost();
        if (item.getCreationDate() != null) {
            this.creationDate = item.getCreationDate().getNonConvertedDate();
        }
        this.usefulLife = item.getUsefulLife();
        this.residualValue = item.getResidualValue();
        this.isEditable = item.isEditable();
        if (item.getFinancedByAccount() != null) {
            this.financedByAccount = new MAccountItem(item.getFinancedByAccount());
        }
        if (item.getNumberData() != null) {
            this.numberData = new MNumberData(item.getNumberData());
        }

        this.calculateDepreciation = item.isCalculateDepreciation();
        this.purchaseInvoiceID = item.getPurchaseInvoiceID();
        if (companyID != null) {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            this.barCodeText = item.getBarcodeGenerateText(companyID.toString(), format.format(creationDate));
        }

    }

    public FixedAssetItem convert(FixedAssetItem item) {
        if (item == null) {
            item = new FixedAssetItem();
        }
        item.setObjectID(WebServiceUtils.getNotZeroValue(getObjectID()));
        item.setCode(getCode());
        item.setName(getName());
        item.setDescription(getDescription());
        item.setCost(getCost());
        item.setUsefulLife(getUsefulLife());
        item.setResidualValue(getResidualValue());
        item.setEditable(isEditable());

        if (getCreationDate() != null) {
            item.setCreationDate(new DateNonConvertable(getCreationDate()));
        }
        if (getAccount() != null) {
            item.setAccount(getAccount().convertToAccountItem(null));
        }
        if (getFinancedByAccount() != null) {
            item.setFinancedByAccount(getFinancedByAccount().convertToAccountItem(null));
        }
        if (getNumberData() != null) {
            item.setNumberData(getNumberData().convertToNumberData(null));
        }
        item.setPurchaseInvoiceID(WebServiceUtils.getNotZeroValue(getPurchaseInvoiceID()));
        item.setCalculateDepreciation(getCalculateDepreciation());

        return item;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public MAccountItem getAccount() {
        return account;
    }

    public void setAccount(MAccountItem account) {
        this.account = account;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public BigDecimal getUsefulLife() {
        return usefulLife;
    }

    public void setUsefulLife(BigDecimal usefulLife) {
        this.usefulLife = usefulLife;
    }

    public BigDecimal getResidualValue() {
        return residualValue;
    }

    public void setResidualValue(BigDecimal residualValue) {
        this.residualValue = residualValue;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public MAccountItem getFinancedByAccount() {
        return financedByAccount;
    }

    public void setFinancedByAccount(MAccountItem financedByAccount) {
        this.financedByAccount = financedByAccount;
    }

    public MNumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(MNumberData numberData) {
        this.numberData = numberData;
    }

    public Integer getPurchaseInvoiceID() {
        return purchaseInvoiceID;
    }

    public void setPurchaseInvoiceID(Integer purchaseInvoiceID) {
        this.purchaseInvoiceID = purchaseInvoiceID;
    }

    public Boolean getCalculateDepreciation() {
        return calculateDepreciation;
    }

    public void setCalculateDepreciation(Boolean calculateDepreciation) {
        this.calculateDepreciation = calculateDepreciation;
    }

    public String getBarCodeText() {
        return barCodeText;
    }

    public void setBarCodeText(String barCodeText) {
        this.barCodeText = barCodeText;
    }
}
