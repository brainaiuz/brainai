package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.form.LocalizationPermissionItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Fatkhulla Nigmatjonov
 * Date: 2/27/13
 * Time: 2:51 PM
 */
public class LocalizationPermissionView extends BaseListView implements Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private ListingPanel<LocalizationPermissionItem> listingPanel;

    public LocalizationPermissionView() {
        super("localizationPermissionView", "Localization Permission");
    }

    public String getIconStyle() {
        return null;
    }


    protected Widget onInitialize() {
        listingPanel = new ListingPanel<>(ListPanelType.LocalizationPermissionListPanel, getColumns(), getListProvider(), getDesign(), SelectionGrid.SelectionPolicy.ONE_ROW, 2000);
        add(listingPanel);
        return null;
    }

    private ListingRequestProvider<LocalizationPermissionItem> getListProvider() {
        return (filterParametrs, listingCallback) -> {
            BackendService.App.get().getLocalizationPermission(new AsyncCallback<ListResult<LocalizationPermissionItem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(ListResult<LocalizationPermissionItem> result) {
                    listingCallback.onSuccess(result);
                }
            });
        };
    }

    private KpiCheckBox getCheckBox(int companyId, boolean isPermission, int languageCode) {
        final KpiCheckBox ch = new KpiCheckBox();
        ch.setValue(isPermission);
        ch.setName(companyId + "_" + languageCode);
        ch.addValueChangeHandler(booleanValueChangeEvent -> {
            LoadingPanel.loading(true);
            BackendService.App.get().saveLocalizationPermission(ch.getName(), ch.getValue(), new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(Boolean result) {
                    LoadingPanel.loading(false);
                }
            });

        });
        return ch;
    }

    private ColumnDefinitionConfig[] getColumns() {
        ColumnDefinitionConfig[] columnsConfigList = new ColumnDefinitionConfig[13];
        //action column
        columnsConfigList[0] = new ColumnDefinitionConfig<LocalizationPermissionItem, String>(wfmStrings.company(), "company", 50) {
            @Override
            public String getCellValue(final LocalizationPermissionItem item) {
                return item.getCompanName();
            }
        };

        columnsConfigList[1] = new ColumnDefinitionConfig<LocalizationPermissionItem, KpiCheckBox>("Code", "code", 25) {
            @Override
            public KpiCheckBox getCellValue(LocalizationPermissionItem item) {
                return getCheckBox(item.getCompanyID(), item.getCode() == null ? false : item.getCode(), Constants.CODE);
            }
        };

        columnsConfigList[2] = new ColumnDefinitionConfig<LocalizationPermissionItem, KpiCheckBox>("Default", "default", 25) {
            @Override
            public KpiCheckBox getCellValue(LocalizationPermissionItem item) {
                return getCheckBox(item.getCompanyID(), item.getDefaultText() == null ? false : item.getDefaultText(), Constants.DEFAULT_TEXT);
            }
        };

        columnsConfigList[3] = new ColumnDefinitionConfig<LocalizationPermissionItem, KpiCheckBox>("En", "en", 15) {
            @Override
            public KpiCheckBox getCellValue(LocalizationPermissionItem item) {
                return getCheckBox(item.getCompanyID(), item.getEn() == null ? false : item.getEn(), Constants.EN);
            }
        };

        columnsConfigList[4] = new ColumnDefinitionConfig<LocalizationPermissionItem, KpiCheckBox>("Ru", "ru", 15) {
            @Override
            public KpiCheckBox getCellValue(LocalizationPermissionItem item) {
                return getCheckBox(item.getCompanyID(), item.getRu() == null ? false : item.getRu(), Constants.RU);
            }
        };

        columnsConfigList[5] = new ColumnDefinitionConfig<LocalizationPermissionItem, KpiCheckBox>("Arabic", "ar", 25) {
            @Override
            public KpiCheckBox getCellValue(LocalizationPermissionItem item) {
                return getCheckBox(item.getCompanyID(), item.getArabic() == null ? false : item.getArabic(), Constants.ARABIC);
            }
        };

        columnsConfigList[6] = new ColumnDefinitionConfig<LocalizationPermissionItem, KpiCheckBox>("Tur", "tur", 15) {
            @Override
            public KpiCheckBox getCellValue(LocalizationPermissionItem item) {
                return getCheckBox(item.getCompanyID(), item.getTurkish() == null ? false : item.getTurkish(), Constants.TURKISH);
            }
        };

        /*columnsConfigList[7] = new ColumnDefinitionConfig<LocalizationPermissionItem, CheckBox>("Ger", "ger", 15) {
            @Override
            public CheckBox getCellValue(LocalizationPermissionItem item) {
                return getCheckBox(item.getCompanyID(), item.getGer() == null ? false : item.getGer(), Constants.GER);
            }
        };*/

        columnsConfigList[7] = new ColumnDefinitionConfig<LocalizationPermissionItem, KpiCheckBox>("Spa", "spa", 15) {
            @Override
            public KpiCheckBox getCellValue(LocalizationPermissionItem item) {
                return getCheckBox(item.getCompanyID(), item.getSpa() == null ? false : item.getSpa(), Constants.SPA);
            }
        };

        columnsConfigList[8] = new ColumnDefinitionConfig<LocalizationPermissionItem, KpiCheckBox>("Fr", "fr", 15) {
            @Override
            public KpiCheckBox getCellValue(LocalizationPermissionItem item) {
                return getCheckBox(item.getCompanyID(), item.getFr() == null ? false : item.getFr(), Constants.FR);
            }
        };

        columnsConfigList[9] = new ColumnDefinitionConfig<LocalizationPermissionItem, KpiCheckBox>("Por", "por", 15) {
            @Override
            public KpiCheckBox getCellValue(LocalizationPermissionItem item) {
                return getCheckBox(item.getCompanyID(), item.getPor() == null ? false : item.getPor(), Constants.POR);
            }
        };

        columnsConfigList[10] = new ColumnDefinitionConfig<LocalizationPermissionItem, KpiCheckBox>("Neder", "neder", 20) {
            @Override
            public KpiCheckBox getCellValue(LocalizationPermissionItem item) {
                return getCheckBox(item.getCompanyID(), item.getNeder() == null ? false : item.getNeder(), Constants.NEDER);
            }
        };

        columnsConfigList[11] = new ColumnDefinitionConfig<LocalizationPermissionItem, KpiCheckBox>("Ita", "ita", 15) {
            @Override
            public KpiCheckBox getCellValue(LocalizationPermissionItem item) {
                return getCheckBox(item.getCompanyID(), item.getIta() == null ? false : item.getIta(), Constants.ITA);
            }
        };

        columnsConfigList[12] = new ColumnDefinitionConfig<LocalizationPermissionItem, KpiCheckBox>("Thai", "thai", 20) {
            @Override
            public KpiCheckBox getCellValue(LocalizationPermissionItem item) {
                return getCheckBox(item.getCompanyID(), item.getThai() == null ? false : item.getThai(), Constants.THAI);
            }
        };

        return columnsConfigList;
    }

    private ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {

            public boolean isShowCustomiseButton() {
                return false;
            }

            public boolean isShowResetButton() {
                return false;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
