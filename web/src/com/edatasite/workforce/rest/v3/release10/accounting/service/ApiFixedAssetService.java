package com.edatasite.workforce.rest.v3.release10.accounting.service;

import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetService;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.rest.base.helpers.ConvertUtils;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.FixedAssetDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import org.jboss.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.NOT_FOUND;


@Service
public class ApiFixedAssetService implements Constants {

    Logger log = Logger.getLogger(ApiFixedAssetService.class);

    private final FixedAssetService fixedAssetService;

    public ApiFixedAssetService(FixedAssetService fixedAssetService) {
        this.fixedAssetService = fixedAssetService;
    }

    public ListResultTO<FixedAssetDTO> getFixedAssetsList(ListingFilterParameter fp) {
        if (fp.getListPanelTool() == null) {
            fp.setListPanelTool(new ListPanelToolRpc());
        }
        ListResult<FixedAssetItem> fixedAssetList = fixedAssetService.getFixedAssets(fp);

        ListResultTO fixedAssets = new ListResultTO();
        if (fixedAssetList != null) {
            ArrayList<FixedAssetDTO> items = fixedAssetList.getList()
                    .stream()
                    .map(ConvertUtils::toDto)
                    .collect(Collectors.toCollection(ArrayList::new));
            fixedAssets.setTotalNumber(items.size());
            fixedAssets.setItems(items);
        }

        return fixedAssets;
    }

    @Transactional(readOnly = true)
    public FixedAssetDTO getFixedAssetById(Integer id) throws RestException {
        return Optional.ofNullable(fixedAssetService.getFixedAssetData(id))
                .map(ConvertUtils::toDto)
                .orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Fixed asset not found", NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Transactional
    public ResultTO save(FixedAssetDTO dto, boolean isNew) throws RestException {
        FixedAssetItem item;
        if (isNew) {
            item = new FixedAssetItem();
        } else {
            item = fixedAssetService.getFixedAssetData(dto.getId());
        }
        item.setName(dto.getName());
        item.setCost(dto.getCost());
        item.setUsefulLife(dto.getUsefulLife());
        item.setTaxCalculationType(dto.getTaxCalculationType());
        item.setCreationDate(new DateNonConvertable());
        Optional.ofNullable(dto.getOwnerId())
                .map(SelectItem::new)
                .ifPresent(item::setOwner);
        item.setNumberData(fixedAssetService.generateFixedAssetNumber());
        Optional.ofNullable(dto.getFixedAssetAccount())
                .map(a -> new SelectItem(a.getId(), a.getName()))
                .ifPresent(item::setFixedAssetAccount);
        Optional.ofNullable(dto.getExpenseAccount())
                .map(a -> new SelectItem(a.getId(), a.getName()))
                .ifPresent(item::setExpenseAccount);
        Optional.ofNullable(dto.getFinancedByAccount())
                .map(a -> new AccountItem(a.getId(), a.getName(), ""))
                .ifPresent(item::setFinancedByAccount);
        Optional.ofNullable(dto.getAccountId())
                .map(a -> new AccountItem(a, ""))
                .ifPresent(item::setAccount);
        Optional.ofNullable(dto.getFinanceAccountId())
                .map(a -> new AccountItem(a, ""))
                .ifPresent(item::setFinancedByAccount);
        Optional.ofNullable(dto.getImageId())
                .ifPresent(item::setImageID);
        Optional.ofNullable(dto.getResidualValue()).ifPresent(item::setResidualValue);
        try {
            if (isNew) {
                fixedAssetService.saveFixedAssetData(item);
            } else {
                fixedAssetService.updateFixedAssetData(item);
            }
        } catch (NumberExistingException e) {
            return ResultTO.failure(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return ResultTO.success();
    }

    @Transactional
    public void deleteFixedAsset(Integer id) {
        fixedAssetService.deleteFixedAsset(id);
    }

}
