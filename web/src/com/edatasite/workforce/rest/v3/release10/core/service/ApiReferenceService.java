package com.edatasite.workforce.rest.v3.release10.core.service;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ApiReferenceService {
    private final ReferenceManager referenceManager;
    private final WfmMessageSource referenceWfmMessageSource;

    public ApiReferenceService(ReferenceManager referenceManager,
                               @Qualifier("referenceWfmMessageSource") WfmMessageSource referenceWfmMessageSource) {
        this.referenceManager = referenceManager;
        this.referenceWfmMessageSource = referenceWfmMessageSource;
    }

    public List<ItemDto> getListByParentCode(String parentCode) {
        List<EdsReference> references = referenceManager.listReferences(parentCode);
        if (CollectionUtils.isEmpty(references)) {
            return new ArrayList<>();
        }

        boolean isOpportunityStage = parentCode.equals(EdsOpportunity._OPPORTUNITY_STAGE);

        return references.stream()
                .map(r -> convertToItemDto(r, isOpportunityStage))
                .toList();
    }

    private ItemDto convertToItemDto(EdsReference r, boolean isOpportunityStage) {
        ItemDto itemDto = isOpportunityStage
                ? new ItemDto(r.getObjectID(), opportunityName(r), r.getCode(), r.getColor())
                : new ItemDto(r.getObjectID(), r.getName(), r.getCode(), r.getColor());

        if (isOpportunityStage) {
            itemDto.addProperty("description", r.getShortName());
            itemDto.addProperty("percentage", r.getDescription());
        } else {
            itemDto.addProperty("description", r.getDescription());
        }
        itemDto.addProperty("requiredComment", r.isRequiredComment());
        itemDto.addProperty("order", r.getSorder());


        addRoles(itemDto, "viewOnlyRoles", r.getViewOnlyRoles());
        addRoles(itemDto, "viewOnlyEmployee", r.getEmployeesCanView());

        addRoles(itemDto, "changeStatusRole", r.getAllowedRoles());
        addRoles(itemDto, "changeStatusEmployee", r.getEmployeesCanEdit());


        addRoles(itemDto, "canEditEmployee", r.getEmployeesCanEditBtn());
        addRoles(itemDto, "canEditRole", r.getOppEditBtnRole());

        return itemDto;
    }

    private void addRoles(ItemDto itemDto, String key, Set<? extends EdsObject> entities) {
        if (entities == null || entities.isEmpty()) {
            return;
        }
        List<Map<String, Object>> mapped = entities.stream()
                .map(e -> Map.<String, Object>of(
                        "id", e.getObjectID(),
                        "name", e.getName()
                ))
                .toList();
        itemDto.addProperty(key, mapped);
    }

    private String opportunityName(EdsReference r) {
        if (!r.isSystemReference() || r.isChanged()) return r.getName();
        return referenceWfmMessageSource.localize(r.getCode());
    }
}
