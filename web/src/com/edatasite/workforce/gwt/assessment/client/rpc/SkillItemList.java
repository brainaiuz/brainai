package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 7/12/11
 * Time: 4:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class SkillItemList implements IsSerializable {
    private int totalCount;
    private SkillItem[] skillItems;

    public SkillItemList() {
    }

    public SkillItemList(SkillItem[] skillItems, int totalCount) {
        this.skillItems = skillItems;
        this.totalCount = totalCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public SkillItem[] getSkillItems() {
        return skillItems;
    }

    public void setSkillItems(SkillItem[] skillItems) {
        this.skillItems = skillItems;
    }

    public ListData getListData() {
        return new ListData(skillItems, totalCount);
    }
}
