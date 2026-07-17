package com.edatasite.workforce.gwt.materialkanban.client.rpc;

import com.edatasite.workforce.gwt.core.client.enums.ReferenceParentEnum;

import java.util.ArrayList;

public interface KanbanServiceLocal {

    void setCrmContactKanbanOrder(Integer prev, Integer current, Integer next, Integer createdByID);

    void setOpportunityKanbanOrder(Integer prev, Integer current, Integer next, Integer createdByID);

    void setCaseKanbanOrder(Integer prev, Integer current, Integer next, Integer createdByID);

    void setTaskKanbanOrder(Integer pId, Integer cId, Integer nId, Integer createdByID);

    Long calculateOrder(Long prev, Long current, Long next);

    ArrayList<KanbanColumn> getKanbanDefaultColumns(ReferenceParentEnum parentCode);
}
