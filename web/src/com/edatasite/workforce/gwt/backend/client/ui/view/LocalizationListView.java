package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendServiceAsync;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LocalizationItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LocalizationPermissionItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.TextAreaCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CellChange;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * User: Fatkhulla Nigmatjonov
 * Date: 2/27/13
 * Time: 2:51 PM
 */
public class LocalizationListView extends BaseListView implements Constants {
    private static final BackendServiceAsync backendService = BackendService.App.get();
    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private ListingPanel<LocalizationItem> listing;
    private HorizontalPanel postFormPanel;
    private DataListBox box = new DataListBox();
    private final DataListBox missedTranslated = new DataListBox();
    private ActionButton addNew;
    private LocalizationPermissionItem access = new LocalizationPermissionItem();
    private String defaultText = "";

    public LocalizationListView() {
        super("localizationPropertyView", backendStrings.localizationProperty());
    }

    public String getIconStyle() {
        return "doc documents";
    }


    protected Widget onInitialize() {
        LoadingPanel.loading(true);
        BackendService.App.get().getCompanyLocalizationPermissions(new AsyncCallback<LocalizationPermissionItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(LocalizationPermissionItem result) {
                LoadingPanel.loading(false);
                access = result;
                listing = new ListingPanel<>(ListPanelType.LocalizationPropertyListPanel, getColumns(), getListProvider(), getDesign());
                WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LOCALIZATION_EDITED, LocalizationListView.this, (sender, args) -> listing.reloadPage());
                postFormPanel = new HorizontalPanel();
                add(listing);
                add(postFormPanel);
            }
        });

        return null;
    }
    private TextAreaCellEditor<String> getWidget(){
        return new TextAreaCellEditor<String>(250) {
            @Override
            protected String getValue() {
                return getText();
            }

            @Override
            protected void setValue(String cellValue) {
                setText(cellValue);
            }

            @Override
            protected boolean onAccept() {
                boolean isEnd = false;
                boolean value = true;
                int i=0;
                if (!"".equals(getText().trim())){
                    while (!isEnd){
                        if (defaultText.contains("{" + i + "}")){
                            if ("".equals(getText().trim()) || !getText().contains("{" + i + "}")){
                                isEnd = true;
                                value = false;
                            }
                        }else{
                            isEnd = true;
                        }
                        i++;
                    }
                }
                return value;
            }
        };
    }
    private ColumnDefinitionConfig[] getColumns() {
        ArrayList<ColumnDefinitionConfig> columnsConfigList = new ArrayList<>();
        //action column
        ColumnDefinitionConfig columns = new ColumnDefinitionConfig<LocalizationItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final LocalizationItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
                edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("localization|add/add/" + item.getObjectID()));
                menuBar.addItem(edit);

                MenuPopItem remove = new MenuPopItem(wfmStrings.delete(), "icon-employee-edit-profile");
                remove.setCommand(() -> {
                    LoadingPanel.loading(true);
                    CommonService.App.get().removeLocalizationItem(item.getObjectID(), new AsyncCallback<Boolean>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void onSuccess(Boolean result) {
                            LoadingPanel.loading(false);
                            listing.reloadPage();
                        }
                    });
                });
                menuBar.addItem(remove);
                final ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns.setColumnSortable(false);
        columnsConfigList.add(columns);

        columns = new ColumnDefinitionConfig<LocalizationItem, String>("Property", "propertycode", 35) {
            @Override
            public String getCellValue(LocalizationItem rowValue) {
                return rowValue.getPropertyCode();
            }
        };
        columnsConfigList.add(columns);
        if (access.getCode()){
             columns = new ColumnDefinitionConfig<LocalizationItem, String>("Code", "code", 35) {
                @Override
                public String getCellValue(LocalizationItem rowValue) {
                    return rowValue.getCode();
                }
            };
          columnsConfigList.add(columns);
        }

        if (access.getDefaultText()){
            columns= new ColumnDefinitionConfig<LocalizationItem, String>("Updated by/date", "defaultLastUpdate", 35) {
                @Override
                public String getCellValue(LocalizationItem rowValue) {
                    String editor = rowValue.getDefaultLastChanger() != null ? rowValue.getDefaultLastChanger() : "Admin";
                    String date = rowValue.getDefaultLastUpdate()!=null ? " ["+DateUtils.formatInternal(rowValue.getDefaultLastUpdate())+"]" : "";
                    return editor+date;
                }
            };
            columnsConfigList.add(columns);

            columns= new ColumnDefinitionConfig<LocalizationItem, String>("Default", "defaulttext", 35) {
                @Override
                public String getCellValue(LocalizationItem rowValue) {
                    defaultText = rowValue.getDefaultText();
                    return rowValue.getDefaultText();
                }
                @Override
                public void setCellValue(LocalizationItem rowValue, String cellValue) {
                    rowValue.setDefaultText(cellValue);
                    saveCellValue(rowValue);
                }
            };

            columns.setCellEditor(getWidget());
            columns.setCellChangesSave(new CellChange<LocalizationItem>() {
                @Override
                public void saveCell(LocalizationItem rowValue, String columnCodeName) {
                    rowValue.setDefaultLastChanger(Utils.getUserFullName());
                    rowValue.setDefaultLastUpdate(new Date());
                    saveLocalization(rowValue);
                }
            });
            columnsConfigList.add(columns);
        }

        if (access.getEn()){
            columns= new ColumnDefinitionConfig<LocalizationItem, String>("Updated by/date", "enLastUpdate", 35) {
                @Override
                public String getCellValue(LocalizationItem rowValue) {
                    String editor = rowValue.getEnLastChanger() != null ? rowValue.getEnLastChanger() : "Admin";
                    String date = rowValue.getEnLastUpdate()!=null ? " ["+DateUtils.formatInternal(rowValue.getEnLastUpdate())+"]" : "";
                    return editor+date;
                }
            };
            columnsConfigList.add(columns);

             columns = new ColumnDefinitionConfig<LocalizationItem, String>("En", "en", 35) {
                @Override
                public String getCellValue(LocalizationItem rowValue) {
                    defaultText = rowValue.getDefaultText();
                    return rowValue.getEn();
                }
                 @Override
                 public void setCellValue(LocalizationItem rowValue, String cellValue) {
                     rowValue.setEn(cellValue);
                     saveCellValue(rowValue);
                 }
            };
            columns.setCellEditor(getWidget());
            columns.setCellChangesSave(new CellChange<LocalizationItem>() {
                @Override
                public void saveCell(LocalizationItem rowValue, String columnCodeName) {
                    rowValue.setEnLastChanger(Utils.getUserFullName());
                    rowValue.setEnLastUpdate(new Date());
                    saveLocalization(rowValue);
                }
            });
            columnsConfigList.add(columns);
        }

        if (access.getRu()){
            columns= new ColumnDefinitionConfig<LocalizationItem, String>("Updated by/date", "ruLastUpdate", 35) {
                @Override
                public String getCellValue(LocalizationItem rowValue) {
                    String editor = rowValue.getRuLastChanger() != null ? rowValue.getRuLastChanger() : "Admin";
                    String date = rowValue.getRuLastUpdate()!=null ? " ["+DateUtils.formatInternal(rowValue.getRuLastUpdate())+"]" : "";
                    return editor+date;
                }
            };
            columnsConfigList.add(columns);

              columns = new ColumnDefinitionConfig<LocalizationItem, String>("Ru", "ru", 35) {
                @Override
                public String getCellValue(LocalizationItem rowValue) {
                    defaultText = rowValue.getDefaultText();
                    return rowValue.getRu();
                }
                @Override
                public void setCellValue(LocalizationItem rowValue, String cellValue) {
                     rowValue.setRu(cellValue);
                     saveCellValue(rowValue);
                }
            };
            columns.setCellEditor(getWidget());
            columns.setCellChangesSave(new CellChange<LocalizationItem>() {
                @Override
                public void saveCell(LocalizationItem rowValue, String columnCodeName) {
                    rowValue.setRuLastChanger(Utils.getUserFullName());
                    rowValue.setRuLastUpdate(new Date());
                    saveLocalization(rowValue);
                }
            });
            columnsConfigList.add(columns);
        }
        if (access.getArabic()){
            columns= new ColumnDefinitionConfig<LocalizationItem, String>("Updated by/date", "arLastUpdate", 35) {
                @Override
                public String getCellValue(LocalizationItem rowValue) {
                    String editor = rowValue.getArLastChanger() != null ? rowValue.getArLastChanger() : "Admin";
                    String date = rowValue.getArLastUpdate()!=null ? " ["+DateUtils.formatInternal(rowValue.getArLastUpdate())+"]" : "";
                    return editor+date;
                }
            };
            columnsConfigList.add(columns);

             columns = new ColumnDefinitionConfig<LocalizationItem, String>("Arabic", "arabic", 35) {
                @Override
                public String getCellValue(LocalizationItem rowValue) {
                    defaultText = rowValue.getDefaultText();
                    return rowValue.getArabic();
                }
                @Override
                public void setCellValue(LocalizationItem rowValue, String cellValue) {
                    rowValue.setArabic(cellValue);
                    saveCellValue(rowValue);
                }
            };
            columns.setCellEditor(getWidget());
            columns.setCellChangesSave(new CellChange<LocalizationItem>() {
                @Override
                public void saveCell(LocalizationItem rowValue, String columnCodeName) {
                    rowValue.setArLastChanger(Utils.getUserFullName());
                    rowValue.setArLastUpdate(new Date());
                    saveLocalization(rowValue);
                }
            });
            columnsConfigList.add(columns);
        }

        if (access.getTurkish()){
            columns= new ColumnDefinitionConfig<LocalizationItem, String>("Updated by/date", "turLastUpdate", 35) {
                @Override
                public String getCellValue(LocalizationItem rowValue) {
                    String editor = rowValue.getTurLastChanger() != null ? rowValue.getTurLastChanger() : "Admin";
                    String date = rowValue.getTurLastUpdate()!=null ? " ["+DateUtils.formatInternal(rowValue.getTurLastUpdate())+"]" : "";
                    return editor+date;
                }
            };
            columnsConfigList.add(columns);

              columns = new ColumnDefinitionConfig<LocalizationItem, String>("Tur", "turkish", 35) {
                @Override
                public String getCellValue(LocalizationItem rowValue) {
                    defaultText = rowValue.getDefaultText();
                    return rowValue.getTurkish();
                }
                @Override
                public void setCellValue(LocalizationItem rowValue, String cellValue) {
                    rowValue.setTurkish(cellValue);
                    saveCellValue(rowValue);
                }
            };
            columns.setCellEditor(getWidget());
            columns.setCellChangesSave(new CellChange<LocalizationItem>() {
                @Override
                public void saveCell(LocalizationItem rowValue, String columnCodeName) {
                    rowValue.setTurLastChanger(Utils.getUserFullName());
                    rowValue.setTurLastUpdate(new Date());
                    saveLocalization(rowValue);
                }
            });
            columnsConfigList.add(columns);
        }

        if (access.getSpa()){
            columns= new ColumnDefinitionConfig<LocalizationItem, String>("Updated by/date", "spaLastUpdate", 35) {
                @Override
                public String getCellValue(LocalizationItem rowValue) {
                    String editor = rowValue.getSpaLastChanger() != null ? rowValue.getSpaLastChanger() : "Admin";
                    String date = rowValue.getSpaLastUpdate()!=null ? " ["+DateUtils.formatInternal(rowValue.getSpaLastUpdate())+"]" : "";
                    return editor+date;
                }
            };
            columnsConfigList.add(columns);

            columns = new ColumnDefinitionConfig<LocalizationItem, String>("Spa", "spa", 35) {
                @Override
                public String getCellValue(LocalizationItem rowValue) {
                    defaultText = rowValue.getDefaultText();
                    return rowValue.getSpa();
                }
                @Override
                public void setCellValue(LocalizationItem rowValue, String cellValue) {
                    rowValue.setSpa(cellValue);
                    saveCellValue(rowValue);
                }
            };
            columns.setCellEditor(getWidget());
            columns.setCellChangesSave(new CellChange<LocalizationItem>() {
                @Override
                public void saveCell(LocalizationItem rowValue, String columnCodeName) {
                    rowValue.setSpaLastChanger(Utils.getUserFullName());
                    rowValue.setSpaLastUpdate(new Date());
                    saveLocalization(rowValue);
                }
            });
            columnsConfigList.add(columns);
        }

        if (access.getFr()){
            columns= new ColumnDefinitionConfig<LocalizationItem, String>("Updated by/date", "frLastUpdate", 35) {
                @Override
                public String getCellValue(LocalizationItem rowValue) {
                    String editor = rowValue.getFrLastChanger() != null ? rowValue.getFrLastChanger() : "Admin";
                    String date = rowValue.getFrLastUpdate()!=null ? " ["+DateUtils.formatInternal(rowValue.getFrLastUpdate())+"]" : "";
                    return editor+date;
                }
            };
            columnsConfigList.add(columns);

            columns = new ColumnDefinitionConfig<LocalizationItem, String>("Fr", "fr", 35) {
                @Override
                public String getCellValue(LocalizationItem rowValue) {
                    defaultText = rowValue.getDefaultText();
                    return rowValue.getFr();
                }
                @Override
                public void setCellValue(LocalizationItem rowValue, String cellValue) {
                    rowValue.setFr(cellValue);
                    saveCellValue(rowValue);
                }
            };
            columns.setCellEditor(getWidget());
            columns.setCellChangesSave(new CellChange<LocalizationItem>() {
                @Override
                public void saveCell(LocalizationItem rowValue, String columnCodeName) {
                    rowValue.setFrLastChanger(Utils.getUserFullName());
                    rowValue.setFrLastUpdate(new Date());
                    saveLocalization(rowValue);
                }
            });
            columnsConfigList.add(columns);
        }

        if (access.getPor()){
            columns= new ColumnDefinitionConfig<LocalizationItem, String>("Updated by/date", "porLastUpdate", 35) {
                @Override
                public String getCellValue(LocalizationItem rowValue) {
                    String editor = rowValue.getPorLastChanger() != null ? rowValue.getPorLastChanger() : "Admin";
                    String date = rowValue.getPorLastUpdate()!=null ? " ["+DateUtils.formatInternal(rowValue.getPorLastUpdate())+"]" : "";
                    return editor+date;
                }
            };
            columnsConfigList.add(columns);

            columns = new ColumnDefinitionConfig<LocalizationItem, String>("Por", "por", 35) {
                @Override
                public String getCellValue(LocalizationItem rowValue) {
                    defaultText = rowValue.getDefaultText();
                    return rowValue.getPor();
                }
                @Override
                public void setCellValue(LocalizationItem rowValue, String cellValue) {
                    rowValue.setPor(cellValue);
                    saveCellValue(rowValue);
                }
            };
            columns.setCellEditor(getWidget());
            columns.setCellChangesSave(new CellChange<LocalizationItem>() {
                @Override
                public void saveCell(LocalizationItem rowValue, String columnCodeName) {
                    rowValue.setPorLastChanger(Utils.getUserFullName());
                    rowValue.setPorLastUpdate(new Date());
                    saveLocalization(rowValue);
                }
            });
            columnsConfigList.add(columns);
        }

        if (access.getNeder()){
            columns= new ColumnDefinitionConfig<LocalizationItem, String>("Updated by/date", "nederLastUpdate", 35) {
                @Override
                public String getCellValue(LocalizationItem rowValue) {
                    String editor = rowValue.getNederLastChanger() != null ? rowValue.getNederLastChanger() : "Admin";
                    String date = rowValue.getNederLastUpdate()!=null ? " ["+DateUtils.formatInternal(rowValue.getNederLastUpdate())+"]" : "";
                    return editor+date;
                }
            };
            columnsConfigList.add(columns);

            columns = new ColumnDefinitionConfig<LocalizationItem, String>("Ned", "neder", 35) {
                @Override
                public String getCellValue(LocalizationItem rowValue) {
                    defaultText = rowValue.getDefaultText();
                    return rowValue.getNeder();
                }
                @Override
                public void setCellValue(LocalizationItem rowValue, String cellValue) {
                    rowValue.setNeder(cellValue);
                    saveCellValue(rowValue);
                }
            };
            columns.setCellEditor(getWidget());
            columns.setCellChangesSave(new CellChange<LocalizationItem>() {
                @Override
                public void saveCell(LocalizationItem rowValue, String columnCodeName) {
                    rowValue.setNederLastChanger(Utils.getUserFullName());
                    rowValue.setNederLastUpdate(new Date());
                    saveLocalization(rowValue);
                }
            });
            columnsConfigList.add(columns);
        }

        if (access.getIta()){
            columns= new ColumnDefinitionConfig<LocalizationItem, String>("Updated by/date", "itaLastUpdate", 35) {
                @Override
                public String getCellValue(LocalizationItem rowValue) {
                    String editor = rowValue.getItaLastChanger() != null ? rowValue.getItaLastChanger() : "Admin";
                    String date = rowValue.getItaLastUpdate()!=null ? " ["+DateUtils.formatInternal(rowValue.getItaLastUpdate())+"]" : "";
                    return editor+date;
                }
            };
            columnsConfigList.add(columns);

            columns = new ColumnDefinitionConfig<LocalizationItem, String>("Ita", "ita", 35) {
                @Override
                public String getCellValue(LocalizationItem rowValue) {
                    defaultText = rowValue.getDefaultText();
                    return rowValue.getIta();
                }
                @Override
                public void setCellValue(LocalizationItem rowValue, String cellValue) {
                    rowValue.setIta(cellValue);
                    saveCellValue(rowValue);
                }
            };
            columns.setCellEditor(getWidget());
            columns.setCellChangesSave(new CellChange<LocalizationItem>() {
                @Override
                public void saveCell(LocalizationItem rowValue, String columnCodeName) {
                    rowValue.setItaLastChanger(Utils.getUserFullName());
                    rowValue.setItaLastUpdate(new Date());
                    saveLocalization(rowValue);
                }
            });
            columnsConfigList.add(columns);
        }

        if (access.getThai()){
            columns= new ColumnDefinitionConfig<LocalizationItem, String>("Updated by/date", "thaiLastUpdate", 35) {
                @Override
                public String getCellValue(LocalizationItem rowValue) {
                    String editor = rowValue.getThaiLastChanger() != null ? rowValue.getThaiLastChanger() : "Admin";
                    String date = rowValue.getThaiLastUpdate()!=null ? " ["+DateUtils.formatInternal(rowValue.getThaiLastUpdate())+"]" : "";
                    return editor+date;
                }
            };
            columnsConfigList.add(columns);

            columns = new ColumnDefinitionConfig<LocalizationItem, String>("Thai", "thai", 35) {
                @Override
                public String getCellValue(LocalizationItem rowValue) {
                    defaultText = rowValue.getDefaultText();
                    return rowValue.getThai();
                }
                @Override
                public void setCellValue(LocalizationItem rowValue, String cellValue) {
                    rowValue.setThai(cellValue);
                    saveCellValue(rowValue);
                }
            };
            columns.setCellEditor(getWidget());
            columns.setCellChangesSave(new CellChange<LocalizationItem>() {
                @Override
                public void saveCell(LocalizationItem rowValue, String columnCodeName) {
                    rowValue.setThaiLastChanger(Utils.getUserFullName());
                    rowValue.setThaiLastUpdate(new Date());
                    saveLocalization(rowValue);
                }
            });
            columnsConfigList.add(columns);
        }
        columns = new ColumnDefinitionConfig<LocalizationItem, String>("Description", "description", 35) {
            @Override
            public String getCellValue(LocalizationItem rowValue) {
                return rowValue.getDescription();
            }
            @Override
            public void setCellValue(LocalizationItem rowValue, String cellValue) {
                rowValue.setDescription(cellValue);
                saveCellValue(rowValue);
            }
        };
        columns.setCellEditor(getWidget());
        columns.setCellChangesSave(new CellChange<LocalizationItem>() {
            @Override
            public void saveCell(LocalizationItem rowValue, String columnCodeName) {
                saveLocalization(rowValue);
            }
        });
        columnsConfigList.add(columns);
        return columnsConfigList.toArray(new ColumnDefinitionConfig[]{});
    }
   private void saveLocalization(LocalizationItem item){
       backendService.saveLocalization(item, new AsyncCallback<Boolean>() {
           @Override
           public void onFailure(Throwable throwable) {
           }
           @Override
           public void onSuccess(Boolean t) {
           }
       });
   }
    private ListingRequestProvider<LocalizationItem> getListProvider() {
        return (filterParametrs, listingCallback) -> {
            BackendService.App.get().getLocalizations(filterParametrs, box.getSelectedItem() != null ? box.getSelectedItem().getDescription() : "", missedTranslated.getSelectedItem() != null ? missedTranslated.getSelectedItem().getDescription() : "",  new AsyncCallback<ListResult<LocalizationItem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(ListResult<LocalizationItem> result) {
                    listingCallback.onSuccess(result);
                }
            });
        };
    }

    private ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public HorizontalPanel initTopToolBarWidgets() {
                HorizontalPanel panel = new HorizontalPanel();
                box = new DataListBox();
                box.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
                generatePropertyItems(box);
                box.addValueChangeHandler(changeEvent -> listing.reloadPage());
                panel.add(box);
                FlowPanel unTranslated = new FlowPanel();
                unTranslated.getElement().getStyle().setMargin(5, Style.Unit.PX);
                SelectItem[] item = new SelectItem[11];
                item[0] = new SelectItem();
                item[0].setName("Default");
                item[0].setDescription("defaulttext");

                item[1] = new SelectItem();
                item[1].setName("English");
                item[1].setDescription("en");

                item[2] = new SelectItem();
                item[2].setName("Russian");
                item[2].setDescription("ru");

                item[3] = new SelectItem();
                item[3].setName("French");
                item[3].setDescription("fr");


                item[4] = new SelectItem();
                item[4].setName("Nederland");
                item[4].setDescription("neder");

                item[5] = new SelectItem();
                item[5].setName("Portugal");
                item[5].setDescription("por");

                item[6] = new SelectItem();
                item[6].setName("Spain");
                item[6].setDescription("spa");

                item[7] = new SelectItem();
                item[7].setName("Thai");
                item[7].setDescription("thai");

                item[8] = new SelectItem();
                item[8].setName("Turkish");
                item[8].setDescription("turkish");

                item[9] = new SelectItem();
                item[9].setName("Italian");
                item[9].setDescription("ita");

                item[10] = new SelectItem();
                item[10].setName("Arabic");
                item[10].setDescription("arabic");



                missedTranslated.setItems(item);
                missedTranslated.addValueChangeHandler(changeEvent -> listing.reloadPage());
                FlowPanel titlePanel = new FlowPanel();
                titlePanel.getElement().getStyle().setFloat(Style.Float.LEFT);
                titlePanel.add(new HTML("Missing translations "));
                unTranslated.add(titlePanel);
                unTranslated.add(missedTranslated);
                panel.add(unTranslated);
                return panel;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                addNew = getAddNewButton();
                addNew.addClickHandler(e -> {
                    if (box.getSelectedItem() != null && box.getSelectedItem().getDescription() != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("localization|add/add/" + box.getSelectedItem().getDescription());
                    } else{
                        Info.show("Please select localization property", Info.Type.WARNING);
                    }
                });
                return addNew;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                if (Utils.isLocalhostOrLochin("lochin.shodiev@workforcetrack.com") || Utils.isLocalhost()) {
                    ActionButton more = new ActionButton("Excel", "", ActionButton.Type.BUTTON);
                    more.ensureDebugId("localization_list_button_id");
                    more.addClickHandler(clickEvent -> {
                        String excelURL = CommandConstants.COMMON_URL + "/downloadLocalizationExcel";
                        Utils.sendPDFOrExcelRequest(postFormPanel, excelURL, new HashMap<>(), "_blank");
                    });
                    return more;
                }else{
                    return null;
                }
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {

            }

            @Override
            public boolean isShowCustomiseButton() {
                return false;
            }

            @Override
            public boolean isShowResetButton() {
                return false;
            }
        };
    }

    private void generatePropertyItems(final DataListBox box) {
        BackendService.App.get().getPropertyItems(new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onSuccess(SelectItem[] selectItems) {
                box.setItems(selectItems);
            }
        });
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
