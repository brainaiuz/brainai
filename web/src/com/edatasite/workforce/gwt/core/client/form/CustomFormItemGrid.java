package com.edatasite.workforce.gwt.core.client.form;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.contact.client.ui.AbstractDataGrid;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.cellview.client.Column;

public class CustomFormItemGrid extends AbstractDataGrid<CustomTableRpc> {

    private String uuid;
    private String formID;
    private Integer id;
    private ColumnConfigs[] columnConfigs;

    public CustomFormItemGrid(Integer id, String uuid, String formID, ColumnConfigs[] columnConfigs, int pageSize) {
        super(pageSize);
        this.id = id;
        this.uuid = uuid;
        this.formID = formID;
        this.columnConfigs = columnConfigs;
        initialize();
    }

    @Override
    protected void addColums() {
        if (columnConfigs != null) {
            for (ColumnConfigs cc : columnConfigs) {
                if (ItemTableConstants.PRODUCT.equals(cc.getCode())) {
                    addColumn(new Column<CustomTableRpc, String>(new TextCell()) {
                        @Override
                        public String getValue(CustomTableRpc item) {
                            return item.getItemName() != null ? item.getItemName() : "";
                        }
                    }, wfmStrings.product());
                } else if (ItemTableConstants.DESCRIPTION.equals(cc.getCode())) {
                    addColumn(new Column<CustomTableRpc, String>(new TextCell()) {
                        @Override
                        public String getValue(CustomTableRpc item) {
                            return item.getDescription() != null ? item.getDescription() : "";
                        }
                    }, wfmStrings.description());
                } else {
                    if (Constants.UI_TYPE_ITEM_WITH_DESCRIPTION.equals(cc.getUiType())) {
                        addColumn(new Column<CustomTableRpc, String>(new TextCell()) {
                            @Override
                            public String getValue(CustomTableRpc item) {
                                String finalValue = "";
                                if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(cc.getCode())) {
                                    CompanyCustomFieldItem cfItem = item.getCustomFieldValuesAsMap().get(cc.getCode());
                                    if (cfItem.getItem() != null && cfItem.getItem().getId() != null) {
                                        finalValue = cfItem.getItem().getName();
                                    }
                                }
                                return finalValue;
                            }
                        }, cc.getTitle());
                        addColumn(new Column<CustomTableRpc, String>(new TextCell()) {
                            @Override
                            public String getValue(CustomTableRpc item) {
                                String finalValue = "";
                                if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(cc.getCode())) {
                                    CompanyCustomFieldItem cfItem = item.getCustomFieldValuesAsMap().get(cc.getCode());
                                    if (cfItem.getItem() != null && cfItem.getItem().getId() != null) {
                                        finalValue = cfItem.getItem().getDescription();
                                    }
                                }
                                return finalValue;
                            }
                        }, wfmStrings.description());
                    } else {
                        addColumn(new Column<CustomTableRpc, String>(new TextCell()) {
                            @Override
                            public String getValue(CustomTableRpc item) {
                                if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(cc.getCode())) {
                                    CompanyCustomFieldItem cfItem = item.getCustomFieldValuesAsMap().get(cc.getCode());
                                    if (Constants.DATA_TYPE_DATE.equals(cfItem.getDataType())) {
                                        return cfItem.getFieldDateNonConvertedValue() != null ? DateUtils.format(cfItem.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                                    } else if (Constants.UI_TYPE_PERCENTAGE.equals(cfItem.getUiType())) {

                                        return cfItem.getFieldStringValue() != null && Utils.isEnableAccountingModule()
                                                ? AccountingUtils.get().getPriceValue(cfItem.getFieldStringValue()) + " % "
                                                : cfItem.getFieldStringValue();

                                    } else if (Constants.DATA_TYPE_NUMBER.equals(cfItem.getDataType())) {

                                        return cfItem.getFieldStringValue() != null && Utils.isEnableAccountingModule()
                                                ? AccountingUtils.get().getPriceValue(cfItem.getFieldStringValue())
                                                : cfItem.getFieldStringValue();

                                    } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(cfItem.getUiType())) {
                                        String finalValue = "";
                                        if (cfItem.getSelectItems() != null && cfItem.getSelectItems().size() > 0) {
                                            for (SelectItem selectItem : cfItem.getSelectItems()) {
                                                finalValue += selectItem.getName() + "; ";
                                            }
                                        }

                                        return finalValue;
                                    } else if (Constants.TYPE_ENTITY_LOOKUP.equals(cfItem.getUiType())) {
                                        String finalValue = "";
                                        if (cfItem.getFieldStringValue() != null) {
                                            Integer id = null;
                                            try {
                                                id = Integer.valueOf(cfItem.getFieldStringValue());
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                            }
                                            if (id != null && cfItem.getQueryItems() != null) {
                                                for (SelectItem selectItem : cfItem.getQueryItems()) {
                                                    if (selectItem.getId().equals(id)) {
                                                        finalValue = selectItem.getName();
                                                        break;
                                                    }
                                                }
                                            }
                                        }

                                        return finalValue;
                                    } else if (Constants.UI_TYPE_ITEM_WITH_DESCRIPTION.equals(cfItem.getUiType())) {
                                        String finalValue = "";
                                        if (cfItem.getItem() != null && cfItem.getItem().getId() != null) {
                                            finalValue = cfItem.getItem().getName();
                                        }
                                        return finalValue;
                                    } else {
                                        return cfItem.getFieldStringValue();
                                    }
                                } else {
                                    return "";
                                }
                            }
                        }, cc.getTitle());
                    }
                }
            }
        }
    }

    @Override
    public void refresher() {
        if (LayoutRPC.OPPORTUNITY_FORM.equals(formID)) {
            CommonService.App.get().getOpportunityItemtable(id, uuid, new AbstractAsyncCallback<CustomTableRpc[]>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(CustomTableRpc[] result) {
                    Scheduler.get().scheduleDeferred(() -> {
                        if (result.length > 0) {
                            supplyProvider(result);
                            reDrawItems();
                        }
                    });
                }
            });
        } else if (LayoutRPC.HRMS_EMPLOYEE_FORM.equals(formID)) {
            CommonService.App.get().getEmployeeItemtable(id, uuid, new AbstractAsyncCallback<CustomTableRpc[]>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(CustomTableRpc[] result) {
                    Scheduler.get().scheduleDeferred(() -> {
                        if (result.length > 0) {
                            supplyProvider(result);
                            reDrawItems();
                        }
                    });
                }
            });
        } else if (LayoutRPC.PLACEMENT_FORM.equals(formID)) {
            CommonService.App.get().getPlacementItemtable(id, uuid, new AbstractAsyncCallback<CustomTableRpc[]>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(CustomTableRpc[] result) {
                    Scheduler.get().scheduleDeferred(() -> {
                        if (result.length > 0) {
                            supplyProvider(result);
                            reDrawItems();
                        }
                    });
                }
            });
        } else if (LayoutRPC.CANDIDATE_FORM.equals(formID)) {
            CommonService.App.get().getCandidateItemTable(id, uuid, new AbstractAsyncCallback<CustomTableRpc[]>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(CustomTableRpc[] result) {
                    Scheduler.get().scheduleDeferred(() -> {
                        if (result.length > 0) {
                            supplyProvider(result);
                            reDrawItems();
                        }
                    });
                }
            });
        } else if (LayoutRPC.PROJECT_FORM.equals(formID)) {
            CommonService.App.get().getProjectItemtable(id, uuid, new AbstractAsyncCallback<CustomTableRpc[]>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(CustomTableRpc[] result) {
                    Scheduler.get().scheduleDeferred(() -> {
                        if (result.length > 0) {
                            supplyProvider(result);
                            reDrawItems();
                        }
                    });
                }
            });
        } else if (LayoutRPC.VACANCY_FORM.equals(formID)) {
            CommonService.App.get().getVacancyItemtable(id, uuid, new AbstractAsyncCallback<CustomTableRpc[]>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(CustomTableRpc[] result) {
                    Scheduler.get().scheduleDeferred(() -> {
                        if (result.length > 0) {
                            supplyProvider(result);
                            reDrawItems();
                        }
                    });
                }
            });
        } else {
            CommonService.App.get().getCustomItemTable(id, uuid, new AbstractAsyncCallback<CustomTableRpc[]>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(CustomTableRpc[] result) {
                    Scheduler.get().scheduleDeferred(() -> {
                        if (result.length > 0) {
                            supplyProvider(result);
                            reDrawItems();
                        }
                    });
                }
            });
        }
    }

}
