package com.edatasite.workforce.gwt.core.client.rpc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA. User: iskan Date: Jan 13, 2008 Time: 10:00:29 PM To
 * change this template use File | Settings | File Templates.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SelectItem extends AbstractRpcMap implements IsSerializable, Serializable, Key, Comparable {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String CODE = "code";
    private static final String NEW_ITEM = "newItem";
    private static final String DESCRIPTION = "description";
    private static final String DEFAULT_SELECTED = "defaultSelected";
    private static final String SELECTED = "selected";
    private static final String DRAGGABLE = "draggable";
    private static final String ALLOW_EDIT = "allowedit";
    private static final String CATEGORY = "category";
    private static final String NUMBER = "number";
    private static final String ENTITY_ID = "entityid";
    private static final String SELECTED_ID = "selectedid";
    private static final String TOTAL_COUNT = "totalcount";

    private static final String CLIENT_ID = "clientid";
    private static final String ORDER_ID = "orderid";
    private static final String COLOR_ID = "colorid";
    private static final String COLOR_NAME = "colorname";
    private static final String COLOR_HEX = "colorhex";
    private static final String TOTAL_AMOUNT = "totalamount";
    private static final String STYLE_NAME = "styleName";
    private static final String ROW_ID = "rowId";
    private static final String MODULE = "module";
    private static final String EXECUTION_CRITERIA = "executionCriteria";
    private static final String WORKFLOW_RULE_NAME = "workflowRuleName";
    private static final String ITEM_TABLE_ENTITY = "itemTableEntity";
    private static final String ITEM_TABLE_ENTITY_ID = "itemTableEntityId";
    private static final String ITEM_TABLE_RELATION = "itemTableRelation";
    private static final String ITEM_TABLE_UUID = "itemTableUuid";
    private static final String ITEM_TABLE_RELATION_ID = "itemTableRelationId";
    private HashMap<String, String> valueMap;
    private String param;
    private BigDecimal duration;

    private BigDecimal qtyAmount;
    private Integer minLeaveDays;
    private Boolean isOk;
    private SelectItem[] relatedItems;
    private DateNonConvertable date;

    public SelectItem() {

    }

    public SelectItem(final Integer id, final String name) {
        this.setId(id);
        this.setName(name);
    }

    public SelectItem(Integer id, String name, boolean isDefaultSelected, String description) {
        this(id, name, description);
        this.setDefaultSelected(isDefaultSelected);
    }

    public SelectItem(final Integer id, final String name, final String description, final Double totalAmount) {
        this(id, name, description);
        this.setTotalAmount(totalAmount);
    }

    public SelectItem(final Integer id, final boolean selected) {
        this.setId(id);
        this.setSelected(selected);
    }


    public SelectItem(final Integer id, final String name, final boolean newItem) {
        this(id, name);
        this.setNewItem(newItem);
    }

    public SelectItem(final Integer id, final String name, final String description, final boolean selected) {
        this(id, name, description);
        this.setSelected(selected);
    }

    public SelectItem(final Integer id, final String name, final String description, final String category) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
        this.setCategory(category);
    }

    public SelectItem(final Integer id, final String name, final String description, final String category, final boolean fake) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
        this.setCategory(category);
        this.setNewItem(fake);
    }

    public SelectItem(final Integer id, final String name, final String description) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
    }

    public SelectItem(final Integer id, final String name, final String description, int rowId) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
        this.setRowId(rowId);
    }

    public SelectItem(final Integer id, final Integer entityId, final String name, final String description) {
        this.setId(id);
        this.setEntityId(entityId);
        this.setName(name);
        this.setDescription(description);
    }

    public SelectItem(final Integer id, final String name, final Integer entityId, final String code, final String description) {
        this.setId(id);
        this.setName(name);
        this.setEntityId(entityId);
        this.setCode(code);
        this.setDescription(description);
    }

    public SelectItem(final Integer id, final String name, final String code, final String description, final String category) {
        this.setId(id);
        this.setName(name);
        this.setCode(code);
        this.setDescription(description);
        this.setCategory(category);
    }

    public SelectItem(final Integer id) {
        this.setId(id);
    }

    public SelectItem(final Integer id, final String name, SelectItem[] relatedItems) {
        this.setId(id);
        this.setName(name);
        this.setRelatedItems(relatedItems);
    }

    public SelectItem(final Integer id, SelectItem[] relatedItems) {
        this.setId(id);
        this.setRelatedItems(relatedItems);
    }


    public SelectItem(final String code) {
        this.setCode(code);
    }

    public static LinkedHashMap<Integer, SelectItem> asMap(final SelectItem[] selectItems) {
        final LinkedHashMap<Integer, SelectItem> map = new LinkedHashMap<>();
        if (selectItems == null || selectItems.length == 0) {
            return map;
        }
        for (final SelectItem selectItem : selectItems) {
            map.put(selectItem.getId(), selectItem);
        }
        return map;
    }

    public static void setSelected(SelectItem[] items, final boolean toAllItems, final Integer... ids) {
        if (items != null && items.length > 0) {
            final Map<Integer, SelectItem> itemsMap = new HashMap<>();
            for (SelectItem item : items) {
                if (item != null) {
                    if (toAllItems) {
                        item.setSelected(true);
                    }
                    itemsMap.put(item.getId(), item);
                }
            }
            if (ids != null && ids.length > 0) {
                for (final Integer id : ids) {
                    if (itemsMap.containsKey(id) || toAllItems) {
                        itemsMap.get(id).setSelected(true);
                    }
                }
            }
        }
    }

    public static SelectItem[] getOnlySelecteds(final SelectItem[] selectItems) {
        final List<SelectItem> list = new ArrayList<>();
        if (selectItems != null && selectItems.length > 0) {
            for (final SelectItem selectItem : selectItems) {
                if (selectItem.isSelected()) {
                    list.add(selectItem);
                }
            }
        }
        return list.toArray(new SelectItem[0]);
    }

    public static ArrayList<Integer> getIDs(final ArrayList<SelectItem> selectItems) {
        return selectItems.stream().map(SelectItem::getId).collect(Collectors.toCollection(ArrayList::new));
    }

    public static boolean isDifferent(final SelectItem value, final SelectItem selectItem) {
        return (value == null && selectItem != null) || (value != null && selectItem == null) || (value != null && selectItem != null && ((value.getId() != null && !value.getId().equals(selectItem.getId())) || (value.getName() != null && !value.getName().equals(selectItem.getName()))));
    }

    public static String asCommaDelimited(final List<SelectItem> selectedCategories) {
        final StringBuilder buffer = new StringBuilder();
        if (selectedCategories != null && selectedCategories.size() > 0) {
            for (final SelectItem item : selectedCategories) {
                if (!"".equals(buffer.toString())) {
                    buffer.append(", ");
                }
                buffer.append(item.getName());
            }
        }
        return buffer.toString();
    }

    public static SelectItem[] asItems(final String[] all) {
        final ArrayList<SelectItem> result = new ArrayList<>();
        if (all != null && all.length > 0) {
            int i = 0;
            for (final String item : all) {
                result.add(new SelectItem(i, item, item));
                i++;
            }
        }
        return result.toArray(new SelectItem[result.size()]);
    }

    public static String getSelectItemsAsCommaDelimeted(final SelectItem[] selectItems, final boolean onlySelected) {
        final StringBuilder s = new StringBuilder();
        final SelectItem[] selecteds = onlySelected ? getOnlySelecteds(selectItems) : selectItems;
        if (selectItems != null && selectItems.length > 0) {
            String delimitr = "";
            for (final SelectItem selectItem : selecteds) {
                if (selectItem != null) {
                    s.append(delimitr).append(selectItem.getName());
                    delimitr = ", ";
                }
            }
        }
        return s.toString();
    }

    protected HashMap<String, String> getInstance() {
        return this.valueMap = this.valueMap == null ? new HashMap<>() : this.valueMap;
    }

    public Integer getId() {
        return this.getInteger(SelectItem.ID);
    }

    public void setId(final Integer id) {
        this.addInteger(SelectItem.ID, id);
    }

    public String getName() {
        return this.getString(SelectItem.NAME);
    }

    public void setName(final String name) {
        this.addString(SelectItem.NAME, name);
    }

    public String getCode() {
        return this.getString(SelectItem.CODE);
    }

    public void setCode(final String code) {
        this.addString(SelectItem.CODE, code);
    }

    public Boolean isNewItem() {
        return this.getBool(SelectItem.NEW_ITEM);
    }

    public void setNewItem(final Boolean newItem) {
        this.addBoolean(SelectItem.NEW_ITEM, newItem);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof SelectItem)) {
            return false;
        }
        final SelectItem other = (SelectItem) obj;
        if (this.getId() != null && other.getId() != null) {
            return this.getId().equals(other.getId());
        } else {
            return this.getName() != null && this.getName().equals(other.getName());
        }
    }

    @Override
    public int hashCode() {
        if (this.getId() != null) {
            return this.getId();
        }
        if (this.getName() != null) {
            return this.getName().hashCode();
        }
        return -1;
    }

    public String getReferenceCode() {
        return this.getString(SelectItem.DESCRIPTION);
    }

    public void setReferenceCode(final String code) {
        this.addString(SelectItem.DESCRIPTION, code);
    }

    public String getDescription() {
        return this.getString(SelectItem.DESCRIPTION);
    }

    public void setDescription(final String description) {
        this.addString(SelectItem.DESCRIPTION, description);
    }

    public Long getTotalCount() {
        return this.getLong(SelectItem.TOTAL_COUNT);
    }

    public void setTotalCount(final Long totalCount) {
        this.addLong(SelectItem.TOTAL_COUNT, totalCount);
    }

    public Integer getClientId() {
        return this.getInteger(SelectItem.CLIENT_ID);
    }

    public void setClientId(final Integer clientId) {
        this.addInteger(SelectItem.CLIENT_ID, clientId);
    }


    public Integer getOrderId() {
        return this.getInteger(SelectItem.ORDER_ID);
    }

    public void setOrderId(final Integer orderid) {
        this.addInteger(SelectItem.ORDER_ID, orderid);
    }

    public Integer getColorId() {
        return this.getInteger(SelectItem.COLOR_ID);
    }

    public void setColorId(final Integer colorId) {
        this.addInteger(SelectItem.COLOR_ID, colorId);
    }

    public String getColorName() {
        return this.getString(SelectItem.COLOR_NAME);
    }

    public void setColorName(final String colorName) {
        this.addString(SelectItem.COLOR_NAME, colorName);
    }

    public String getColorHex() {
        return this.getString(SelectItem.COLOR_HEX);
    }

    public void setColorHex(final String colorHex) {
        this.addString(SelectItem.COLOR_HEX, colorHex);
    }

    public String getWorkflowRuleName() {
        return getString(SelectItem.WORKFLOW_RULE_NAME);
    }

    public void setWorkflowRuleName(String workflowRuleName) {
        this.addString(SelectItem.WORKFLOW_RULE_NAME, workflowRuleName);
    }

    public String getItemTableEntity() {
        return getString(SelectItem.ITEM_TABLE_ENTITY);
    }

    public void setItemTableEntity(String itemTableEntity) {
        this.addString(SelectItem.ITEM_TABLE_ENTITY, itemTableEntity);
    }

    public Integer getItemTableEntityId() {
        return getInteger(SelectItem.ITEM_TABLE_ENTITY_ID);
    }

    public void setItemTableEntityId(Integer itemTableEntity) {
        this.addInteger(SelectItem.ITEM_TABLE_ENTITY_ID, itemTableEntity);
    }

    public String getItemTableRelation() {
        return getString(SelectItem.ITEM_TABLE_RELATION);
    }

    public void setItemTableRelation(String itemTableRelation) {
        this.addString(SelectItem.ITEM_TABLE_RELATION, itemTableRelation);
    }

    public String getItemTableUuid() {
        return getString(SelectItem.ITEM_TABLE_UUID);
    }

    public void setItemTableUuid(String itemTableUuid) {
        this.addString(SelectItem.ITEM_TABLE_UUID, itemTableUuid);
    }

    public Integer getItemTableRelationId() {
        return getInteger(SelectItem.ITEM_TABLE_RELATION_ID);
    }

    public void setItemTableRelationId(Integer itemTableRelation) {
        this.addInteger(SelectItem.ITEM_TABLE_RELATION_ID, itemTableRelation);
    }

    public Double getTotalAmount() {
        return this.getDouble(SelectItem.TOTAL_AMOUNT);
    }

    public void setTotalAmount(final Double totalAmount) {
        this.addDouble(SelectItem.TOTAL_AMOUNT, totalAmount);
    }

    public boolean isDefaultSelected() {
        return this.getBool(SelectItem.DEFAULT_SELECTED);
    }

    public void setDefaultSelected(final boolean defaultSelected) {
        this.addBool(SelectItem.DEFAULT_SELECTED, defaultSelected);
    }

    public boolean isSelected() {
        return this.getBool(SelectItem.SELECTED);
    }

    public void setSelected(final boolean selected) {
        this.addBool(SelectItem.SELECTED, selected);
    }

    public boolean isDraggable() {
        return this.getBool(SelectItem.DRAGGABLE);
    }

    public void setDraggable(final boolean draggable) {
        this.addBool(SelectItem.DRAGGABLE, draggable);
    }

    public boolean isAllowEdit() {
        return this.getBool(SelectItem.ALLOW_EDIT);
    }

    public void setAllowEdit(final boolean allowEdit) {
        this.addBool(SelectItem.ALLOW_EDIT, allowEdit);
    }

    public Integer getEntityId() {
        return this.getInteger(SelectItem.ENTITY_ID);
    }

    public void setEntityId(final Integer entityId) {
        this.addInteger(SelectItem.ENTITY_ID, entityId);
    }

    public  String getModule() {
        return this.getString(SelectItem.MODULE);
    }
    public void setModule(String module){
        this.addString(SelectItem.MODULE,module);
    }

    public  String getExecutionCriteria() {
        return this.getString(SelectItem.EXECUTION_CRITERIA);
    }

    public void setExecutionCriteria(String executionCriteria){
         this.addString(SelectItem.EXECUTION_CRITERIA,executionCriteria);
    }

    public Integer getSelectedId() {
        return this.getInteger(SelectItem.SELECTED_ID);
    }

    public void setSelectedId(final Integer selectedId) {
        this.addInteger(SelectItem.SELECTED_ID, selectedId);
    }

    public String getCategory() {
        return this.getString(SelectItem.CATEGORY);
    }

    public void setCategory(final String category) {
        this.addString(SelectItem.CATEGORY, category);
    }

    public String getNumber() {
        return this.getString(SelectItem.NUMBER);
    }

    public void setNumber(final String number) {
        this.addString(SelectItem.NUMBER, number);
    }

    public String getStyleName() {
        return this.getString(SelectItem.STYLE_NAME);
    }

    public void setStyleName(final String className) {
        this.addString(SelectItem.STYLE_NAME, className);
    }

    public HashMap<String, String> getValueMap() {
        return valueMap;
    }

    @Override
    public String getKey() {
        return "" + this.getId();
    }

    public String asString() {
        return this.getName() + "|" + this.getId();
    }

    public String getParam() {
        return this.param;
    }

    public void setParam(final String param) {
        this.param = param;
    }

    public BigDecimal getDuration() {
        return this.duration;
    }

    public void setDuration(final BigDecimal duration) {
        this.duration = duration;
    }

    public int getRowId() {
        return this.getInt(SelectItem.ROW_ID);
    }

    public void setRowId(final int id) {
        this.addInt(SelectItem.ROW_ID, id);
    }

    public Integer getMinLeaveDays() {
        return minLeaveDays;
    }

    public void setMinLeaveDays(Integer minLeaveDays) {
        this.minLeaveDays = minLeaveDays;
    }

    @Override
    public String toString() {
        return this.getId() != null ? String.valueOf(this.getId()) : String.valueOf(-1);
    }

    @Override
    public int compareTo(Object o) {
        return this.getName().compareTo(((SelectItem) o).getName());
    }

    public SelectItem[] getRelatedItems() {
        return relatedItems;
    }

    public void setRelatedItems(SelectItem[] relatedItems) {
        this.relatedItems = relatedItems;
    }

    public Boolean isOk() {
        return isOk;
    }

    public void setOk(Boolean ok) {
        isOk = ok;
    }


    public BigDecimal getQtyAmount() {
        return qtyAmount;
    }

    public void setQtyAmount(BigDecimal qtyAmount) {
        this.qtyAmount = qtyAmount;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }
}