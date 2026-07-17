package com.edatasite.workforce.gwt.hrms.client.ui.recruitment;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.enums.ReferenceParentEnum;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ConvertItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.LocalizationType;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterCutomField;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings.KanbanItemColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings.KanbanItemSettingEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrContactRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ContextMenu;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.components.ImportFileActionLink;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportFilePopUp;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingCallback;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DateTimePickerCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DropDownCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.TextBoxCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CellChange;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NotesWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.CandidatePercentageStageModal;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.ui.PhonePopup;
import com.edatasite.workforce.gwt.crm.client.ui.view.kanban.CandidateMaterialCard;
import com.edatasite.workforce.gwt.crm.client.ui.view.quickadd.CandidateQuickAddForm;
import com.edatasite.workforce.gwt.crm.client.ui.view.quickadd.CandidateQuickValidate;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentServiceAsync;
import com.edatasite.workforce.gwt.materialkanban.client.KanbanBoard;
import com.edatasite.workforce.gwt.materialkanban.client.KanbanBoardDesign;
import com.edatasite.workforce.gwt.materialkanban.client.KanbanDataLoader;
import com.edatasite.workforce.gwt.materialkanban.client.KanbanDataRenderer;
import com.edatasite.workforce.gwt.materialkanban.client.rpc.KanbanService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.gen2.table.client.InlineCellEditor;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_CANDIDATE_IMPORT;

/**
 * User: Ilxom Lutfullaev
 * Date: 6/21/12
 * Time: 3:35 PM
 */

public class CandidatesListView extends BaseListView implements Constants {

    public static final String SHORT_LIST = "SHORT_LIST_VIEW";
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    protected final RecruitmentServiceAsync candidateService = RecruitmentService.App.get();
    protected Map<String, List<Widget>> widgets = new HashMap<>();
    private final String candidateListViewID = "candidate_list_view_";
    private ListingPanel<ContactListItem> listPanel;
    private String fromView;
    private final String iconStyle;
    private Set<ContactListItem> selectedRows;
    private ContextMenu actions;
    private ContextMenu actionsEmpty;
    private Integer maxNoAccessEmp = 0;
    private KpiSideNavBox quickAddBox;
    private ImportFilePopUp imp;
    private final DataListBox statusList = new DataListBox();
    private Integer vacancyId;

    private NotesWidget notesPanel;

    public CandidatesListView() {
        super("candidatesList");
        setDescription(property.getPlural(wfmStrings.candidates()));
        iconStyle = "bgMark candidates";
        if (hasPermissionToAdd()) {
            setAddNew("candidate|add/add");
        }
    }

    public CandidatesListView(Integer vacancyId) {
        this();
        this.vacancyId = vacancyId;
    }

    public CandidatesListView(String fromView) {
        super("shortListView");
        setDescription(property.getPlural(hrmsStrings.shortlists()));
        this.fromView = fromView;
        iconStyle = "bgMark shortlists";
        if (hasPermissionSelectCandidate()) {
            setAddNew("candidateSelect|selectCandidateT/");
        }
    }

    private boolean hasPermissionToAdd() {
        return Utils.hasPermission(PermissionConstants.HRMS_ADD_CANDIDATE) || Utils.hasPermission(PermissionConstants.HRMS_QUICK_ADD_CANDIDATE);
    }

    private boolean hasPermissionToVacancySummary() {
        return Utils.hasPermission(PermissionConstants.HRMS_CANDIDATE_VACANCY_SUMMARY_VIEW);
    }

    private boolean hasPermissionSelectCandidate() {
        return SHORT_LIST.equals(fromView) && Utils.hasPermission(PermissionConstants.HRMS_SELECT_CANDIDATE);
    }

    @Override
    public String getIconStyle() {
        return iconStyle;
    }

    protected void getEmployeesMaxCount() {
        ReportService.App.get().getEmployeesMaxCount(null, new AbstractAsyncCallback<Integer[]>() {
            @Override
            public void success(Integer[] result) {
                maxNoAccessEmp = result[NO_ACCESS];
            }
        });
    }

    protected Widget onInitialize() {
        if (SHORT_LIST.equals(fromView)) {
            listPanel = new GuideListingPanel(ListPanelType.ShortListPanel, getColumnConfig(), getListProvider(), getListDesign());
            // Short List PDF
            listPanel.setPDFListener(event -> {
                String pdfURL = CommandConstants.PDF_URL + "/shortListPDFHandler";
                ListingFilterParameter filterParameter = listPanel.getFilterParametrs();
                filterParameter.setPropertyCode(getPropertyCode());
                listPanel.callListPDF(pdfURL, filterParameter);
            });
            listPanel.setExcelListener(clickEvent -> {
                String excelURL = CommandConstants.COMMON_URL + "/downloadShortListExcel";
                ListingFilterParameter filterParameter = listPanel.getFilterParametrs();
                filterParameter.setPropertyCode(getPropertyCode());
                listPanel.callListExcel(excelURL, filterParameter);
            });
        } else {
            listPanel = new GuideListingPanel(ListPanelType.CandidateListPanel, getColumnConfig(), getListProvider(), getListDesign(), SelectionGrid.SelectionPolicy.CHECKBOX);
            // Candidate List PDF
            listPanel.setPDFListener(event -> {
                String pdfURL = CommandConstants.PDF_URL + "/candidateListPDFHandler";
                ListingFilterParameter fp = listPanel.getFilterParametrs();
                listPanel.callListPDF(pdfURL, fp);
                fp.setPropertyCode(getPropertyCode());
            });
            listPanel.setExcelListener(clickEvent -> {
                String excelURL = CommandConstants.COMMON_URL + "/downloadCandidateListExcel";
                ListingFilterParameter fp = listPanel.getFilterParametrs();
                listPanel.callListExcel(excelURL, fp);
                fp.setPropertyCode(getPropertyCode());
            });
        }

        listPanel.setCustomFieldsEditCellSaveChanges(new CellChange<ContactListItem>() {
            @Override
            public void saveCell(ContactListItem rowValue, String columnCodeName) {
                saveCandidateEditCellValue(rowValue, columnCodeName);
            }
        });
        listPanel.addSelectionRowHandler(sRows -> selectedRows = sRows);
        add(listPanel);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CANDIDATE_ADD_EDIT, CandidatesListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CANDIDATE_DELETE, CandidatesListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CANDIDATE_SELECT, CandidatesListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PLACEMENT_ADD_EDIT, CandidatesListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_VACANCY_MATCHED, CandidatesListView.this, (sender, args) -> listPanel.reloadPage());

        initQuickAddView();

        KanbanBoard<ContactListItem> kanbanBoard = new KanbanBoard<ContactListItem>(ListPanelType.CandidateKanbanPanel, getCandidateKanbanDataLoader(), getKanbanBoardDesign()) {
            @Override
            public Widget getColumnAddButton(SelectItem columnMetadata) {

                MaterialLink addCandidateLink = new MaterialLink();
                addCandidateLink.setStyleName("wg_canban__add-card");
                Icon plus = new Icon();
                plus.setStyleName("ficon--plus");
                addCandidateLink.add(plus);
                addCandidateLink.addClickHandler(click -> new CandidateQuickAddForm());
                return addCandidateLink;

            }
        };
        kanbanBoard.setKanbanItemSettingsType(KanbanItemSettingEnum.CANDIDATE_ITEM_SETTINGS);
        notesPanel = new NotesWidget(false);
        listPanel.setKanbanBoardView(kanbanBoard);
        return null;

    }

    private KanbanDataLoader<ContactListItem> getCandidateKanbanDataLoader() {
        return new KanbanDataLoader<ContactListItem>() {
            @Override
            public void loadData(ListingFilterParameter filterParameter, KanbanDataRenderer<ContactListItem> dataRenderer) {
                LoadingPanel.loading(true);

                candidateService.getNewKanbanCandidates(filterParameter, dataRenderer.getColumnMetadata(), new AsyncCallback<ListResult<ContactListItem>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(ListResult<ContactListItem> result) {
                        dataRenderer.setResults(result);
                        LoadingPanel.loading(false);

                    }
                });

            }

            @Override
            public void onDropKanbanItem(Object sourceColumnLayoutData, Object targetColumnLayoutData, Object candidateListItem, Integer widgetIndex, Object prevItem, Object afterItem, KanbanBoard kanbanBoard, KanbanBoard.OnDropCard onDropCard) {
                SelectItem targetColumnLayoutDataSelectItem = (SelectItem) targetColumnLayoutData;
                if (targetColumnLayoutDataSelectItem.isDraggable() && ((SelectItem) sourceColumnLayoutData).isDraggable()) {
                    if ((targetColumnLayoutDataSelectItem.getDescription()).equals("0")) {
                        CandidatePercentageStageModal widgets1 = new CandidatePercentageStageModal(targetColumnLayoutDataSelectItem, (Integer) candidateListItem);
                        widgets1.save((o) -> {
                            changeKanbanCandidateStatus((SelectItem) o, (Integer) candidateListItem, widgetIndex, (Integer) prevItem, (Integer) afterItem, onDropCard);
                        });
                        widgets1.cancel((o) -> {
                            kanbanBoard.reloadColumn(((SelectItem) o).getId());
                            kanbanBoard.reloadColumn(((SelectItem) sourceColumnLayoutData).getId());
                        });
                    } else if (((SelectItem) targetColumnLayoutData).isSelected()) {
                        notesPanel.setNoteListener(() -> {
                            String comment = notesPanel.getLastHistoryItem().getComment();
                            targetColumnLayoutDataSelectItem.setCategory(comment);
                            changeKanbanCandidateStatus(targetColumnLayoutDataSelectItem, (Integer) candidateListItem, widgetIndex, (Integer) prevItem, (Integer) afterItem, onDropCard);
                        });
                        notesPanel.setCloseListener(() -> listPanel.requestKanbanData());
                        notesPanel.noteShell();
                    } else {
                        changeKanbanCandidateStatus(targetColumnLayoutDataSelectItem, (Integer) candidateListItem, widgetIndex, (Integer) prevItem, (Integer) afterItem, onDropCard);
                    }
                } else {
                    Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                    listPanel.requestKanbanData();
                }
            }

        };
    }

    private void changeKanbanCandidateStatus(SelectItem targetColumnLayoutData, Integer candidateListItem, Integer
            widgetIndex, Integer prevItem, Integer afterItem, KanbanBoard.OnDropCard onDropCard) {
        candidateService.changeCandidateKanbanOrder(targetColumnLayoutData, candidateListItem, widgetIndex, prevItem, afterItem, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable throwable) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Integer integer) {
                if (onDropCard != null) {
                    onDropCard.onDropCard();
                }
            }
        });
    }

    private KanbanBoardDesign<ContactListItem> getKanbanBoardDesign() {
        return new KanbanBoardDesign<ContactListItem>() {
            @Override
            public void loadDefaultColumns(AbstractAsyncCallback callback) {
                LoadingPanel.loading(true);
                KanbanService.App.get().getKanbanDefaultColumns(ReferenceParentEnum._CANDIDATE_STATUS, new AsyncCallback<ArrayList<SelectItem>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        callback.failure(throwable);
                    }

                    @Override
                    public void onSuccess(ArrayList<SelectItem> selectItems) {
                        LoadingPanel.loading(false);
                        callback.success(selectItems);

                    }
                });
            }

            @Override
            public Widget getBoardItem(ContactListItem kanbanItem, KanbanBoard<ContactListItem> kanbanBoard, Object... obj) {
                MaterialPanel p = new MaterialPanel();
                if (obj != null && obj.length > 0 && (obj[0] instanceof HashMap)) {
                    HashMap<String, KanbanItemColumnConfigs> strMap = (HashMap) obj[0];
                    p.add(new CandidateMaterialCard(kanbanItem, strMap));
                } else {
                    p.add(new CandidateMaterialCard(kanbanItem));
                }
                p.setLayoutData(kanbanItem.getObjectId());
                if (!Utils.hasPermission(PermissionConstants.HRMS_CHANGE_STATUS_CANDIDATE)) {
                    p.setEnabled(false);
                }
                return p;

            }

            @Override
            public boolean canDnD(ContactListItem kanbanItem) {
                return kanbanItem.isDraggable();
            }
        };
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        ColumnDefinitionConfig[] columnConfig = new ColumnDefinitionConfig[12];
        ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();

        int i = 0;
        //action menu
        columnConfig[i] = new ColumnDefinitionConfig<ContactListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final ContactListItem item) {
                return getActionAnchor(item);
            }
        };
        columnConfig[i].setColumnSortable(false);
        columnConfig[i].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig[i].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns.add(columnConfig[i]);
        //candidate number
        columnConfig[++i] = new

                ColumnDefinitionConfig<ContactListItem, SimpleLink>(wfmStrings.number(), ContactListItem.CONTACT_ID, 60) {
                    @Override
                    public SimpleLink getCellValue(final ContactListItem item) {
                        return new SimpleLink(item.getNumberData() != null ? item.getNumberData().getNumberString() : "", "candidate|summary/" + item.getObjectId(), item.getName(), item.getNumberData() != null ? item.getNumberData().getNumberString() : item.getName());
                    }
                };
        columnConfig[i].setMinimumColumnWidth(50);
        columnConfig[i].setShow(ContactListItem.defaultCandidateColumnNames.contains(ContactListItem.CONTACT_ID));
        columnConfig[i].setColumnSortable(true);
        columns.add(columnConfig[i]);
        //candidate name
        columnConfig[++i] = new

                ColumnDefinitionConfig<ContactListItem, SimpleLink>(wfmStrings.name(), ContactListItem.CONTACT_NAME, 200) {
                    @Override
                    public SimpleLink getCellValue(final ContactListItem item) {
                        return new SimpleLink(item.getName(), "candidate|summary/" + item.getObjectId(), item.getName(), item.getNumberData() != null ? item.getNumberData().getNumberString() : item.getName());
                    }
                };
        columnConfig[i].setMinimumColumnWidth(150);
        columnConfig[i].setShow(ContactListItem.defaultCandidateColumnNames.contains(ContactListItem.CONTACT_NAME));
        columnConfig[i].setColumnSortable(true);
        columns.add(columnConfig[i]);

        //candidate matched vacancies
        columnConfig[++i] = new

                ColumnDefinitionConfig<ContactListItem, Div>(wfmStrings.vacancy(), ContactListItem.VACANCIES, 80) {
                    @Override
                    public Div getCellValue(final ContactListItem item) {
                        if (item.getVacancies() != null && !item.getVacancies().isEmpty()) {
                            Div div = new Div();
                            int i = 0;
                            for (SelectItem selectItem : item.getVacancies()) {
                                Span delimiter = new Span(", ");
                                String link = "vacancy|summary/" + selectItem.getId();
                                SimpleLink name = new SimpleLink(selectItem.getName());
                                name.addClickHandler(handler -> {
                                    if (hasPermissionToVacancySummary()) {
                                        SinksContainerFactory.entryPoint.onHistoryChanged(link);
                                    } else {
                                        Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                                    }
                                });
                                div.add(name);
                                if (i != item.getVacancies().size() - 1) {
                                    div.add(delimiter);
                                }
                                i++;
                            }
                            return div;
                        }
                        return null;
                    }
                };
        columnConfig[i].setMinimumColumnWidth(70);
        columnConfig[i].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig[i].setColumnSortable(false);
        columns.add(columnConfig[i]);

        //candidate status

        if (Utils.hasPermission(PermissionConstants.HRMS_CHANGE_STATUS_CANDIDATE)) {
            columnConfig[++i] = new ColumnDefinitionConfig<ContactListItem, SelectItem>(wfmStrings.status(), ContactListItem.LEAD_STATUS, 80) {
                @Override
                public SelectItem getCellValue(final ContactListItem item) {
                    return item.getLeadStatus(true);
                }

                @Override
                public void setCellValue(ContactListItem rowValue, SelectItem cellValue) {
                    if (cellValue.isDraggable() && (rowValue.isDraggable() || rowValue.getCandidateStatus() == null)) {
                        if (!rowValue.isHasPlacement() && (ContactListItem.C_S_PLACED.equals(cellValue.getCode()) || ContactListItem.C_S_OFFER_MADE.equals(cellValue.getCode()) || ContactListItem.C_S_HIRED.equals(cellValue.getCode()))) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("placement|add/add/" + rowValue.getObjectId());
                        } else if (rowValue.isHasPlacement() && ContactListItem.C_S_HIRED.equals(cellValue.getCode())) {
                            hireCandidate(rowValue.getPlacementId());
                        } else {
                            if ((cellValue.getDescription()).equals("0")) {
                                CandidatePercentageStageModal widgets1 = new CandidatePercentageStageModal(cellValue, rowValue.getObjectId());
                                widgets1.save((o) -> {
                                    rowValue.setLeadStatus((SelectItem) o);
                                    saveCellValue(rowValue);
                                });
                                widgets1.cancel((o) -> {
                                    listPanel.reloadPage();
                                });
                            } else if ((cellValue).isSelected()) {
                                notesPanel.setNoteListener(() -> {
                                    String comment = notesPanel.getLastHistoryItem().getComment();
                                    cellValue.setCategory(comment);
                                    rowValue.setLeadStatus(cellValue);
                                    saveCellValue(rowValue);
                                });
                                notesPanel.setCloseListener(() -> listPanel.reloadPage());
                                notesPanel.noteShell();
                            } else {
                                rowValue.setLeadStatus(cellValue);
                                saveCellValue(rowValue);
                            }
                        }
                    } else {
                        Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                    }
                }
            };
        } else {
            columnConfig[++i] = new ColumnDefinitionConfig<ContactListItem, ReferenceItem>(wfmStrings.status(), ContactListItem.LEAD_STATUS, 80) {
                @Override
                public ReferenceItem getCellValue(final ContactListItem item) {
                    return item.getLeadStatus(true);
                }
            };
        }
        columnConfig[i].setMinimumColumnWidth(70);
        columnConfig[i].setShow(ContactListItem.defaultCandidateColumnNames.contains(ContactListItem.LEAD_STATUS));
        columnConfig[i].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig[i].setColumnSortable(true);
        columns.add(columnConfig[i]);

        //candidate phone
        //Phone
//        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_TWILIO)) {
        columnConfig[++i] = new ColumnDefinitionConfig<ContactListItem, Div>(wfmStrings.phone(), ContactListItem.PHONE, 100) {
            @Override
            public Div getCellValue(final ContactListItem rowValue) {
                PhonePopup phonePopup = new PhonePopup(rowValue.getPrimaryPhone(), rowValue, false, true);
                return phonePopup.getPhoneWidget();
            }
        };
        /*} else {
            columnConfig[++i] = new ColumnDefinitionConfig<ContactListItem, HTML>(wfmStrings.phone(), ContactListItem.PHONE, 100) {
                @Override
                public HTML getCellValue(final ContactListItem rowValue) {
                    if(rowValue.getPrimaryPhone()!=null && !"N/A".equalsIgnoreCase(rowValue.getPrimaryPhone())) {
                        return new HTML("<a href=\"tel:" + rowValue.getPrimaryPhone() + "\">" + rowValue.getPrimaryPhone() + "</a>" );
                    } else {
                        return new HTML(rowValue.getPrimaryPhone());
                    }
                }
            };
        }*/
        columnConfig[i].setMinimumColumnWidth(100);
        columnConfig[i].setShow(ContactListItem.defaultCandidateColumnNames.contains(ContactListItem.PHONE));
        columnConfig[i].setColumnSortable(true);
        columns.add(columnConfig[i]);

        //candidate source
        columnConfig[++i] = new

                ColumnDefinitionConfig<ContactListItem, SelectItem>(wfmStrings.source(), ContactListItem.LEAD_SOURCE, 80) {
                    @Override
                    public SelectItem getCellValue(final ContactListItem item) {
                        return new SelectItem(item.getLeadSourceID(), item.getLeadSource());
                    }

                    @Override
                    public void setCellValue(ContactListItem rowValue, SelectItem cellValue) {
                        rowValue.setLeadSourceID(cellValue != null ? cellValue.getId() : null);
                        rowValue.setLeadSource(cellValue != null && cellValue.getId() != null && cellValue.getId() > 0 ? cellValue.getName() : null);
                        saveCellValue(rowValue);
                    }
                };
        columnConfig[i].setMinimumColumnWidth(60);
        columnConfig[i].setShow(ContactListItem.defaultCandidateColumnNames.contains(ContactListItem.LEAD_SOURCE));
        columnConfig[i].setColumnSortable(true);
        columns.add(columnConfig[i]);

        //candidate Date of birth (DOB)
        columnConfig[++i] = new

                ColumnDefinitionConfig<ContactListItem, String>(wfmStrings.dateOfBirth(), ContactListItem.DATE_OF_BIRTH, 150) {
                    @Override
                    public String getCellValue(final ContactListItem item) {
                        return item.getBirthDate() != null ? DateUtils.format(item.getBirthDate().getDate()) + Utils.getHijriDate(item.getBirthDate().getDate()) : "";
                    }

                    @Override
                    public void setCellValue(ContactListItem rowValue, String cellValue) {
                        try {
                            rowValue.setBirthDate(getNonConvertable(DateUtils.parse(cellValue)));
                        } catch (DateFormatException e) {
                            e.printStackTrace();
                        }
                        saveCellValue(rowValue);
                    }
                };
        columnConfig[i].setMinimumColumnWidth(100);
        columnConfig[i].setShow(false);
        columnConfig[i].setColumnSortable(true);
        columns.add(columnConfig[i]);
        //candidate email
        columnConfig[++i] = new

                ColumnDefinitionConfig<ContactListItem, SimpleLink>(wfmStrings.email(), ContactListItem.EMAIL, 130) {
                    @Override
                    public SimpleLink getCellValue(final ContactListItem item) {
                        SimpleLink sendEmailLink = new SimpleLink(!Utils.isNullOrEmpty(item.getPrimaryEmail()) ? item.getPrimaryEmail() : "");
                        if (!Utils.isNullOrEmpty(item.getPrimaryEmail())) {
                            //sendEmailLink.addClickHandler(clickEvent -> new ComposeView(item.getPrimaryEmail(), RelationItem.newEventRelation(RelationItem.TYPE_CANDIDATE, item.getObjectId(), item.getName())));
                            sendEmailLink.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/" + item.getPrimaryEmail() + "/" + RelationItem.TYPE_CANDIDATE + "/" + item.getObjectId() + "/" + item.getName()));
                        }
                        return sendEmailLink;
                    }
                };
        columnConfig[i].setMinimumColumnWidth(120);
        columnConfig[i].setShow(false);
        columnConfig[i].setColumnSortable(true);
        columns.add(columnConfig[i]);
//
//        //phone number
//        columnConfig = new ColumnDefinitionConfig<EmployeeListItem, Div>(wfmStrings.phoneNumber(), EmployeeListItem.PHONE_NUMBER, 140) {
//            @Override
//            public Div getCellValue(EmployeeListItem item) {
//                PhonePopup phonePopup = new PhonePopup(item.getPhoneNumber(), RelationItem.TYPE_EMPLOYEE, item.getObjectID(), item.getFullName(), false);
//                return phonePopup.getPhoneWidget();
//            }
//        };

        //candidate owner
        columnConfig[++i] = new

                ColumnDefinitionConfig<ContactListItem, SelectItem>(wfmStrings.owner(), ContactListItem.OWNER, 150) {
                    @Override
                    public SelectItem getCellValue(final ContactListItem item) {
                        return new SelectItem(item.getOwnerId(), item.getOwner());
                    }

                    @Override
                    public void setCellValue(ContactListItem rowValue, SelectItem cellValue) {
                        rowValue.setOwnerId(getSelectedItemID(cellValue));
                        rowValue.setOwner(getSelectedItemName(cellValue));
                        saveContactsCellValue(rowValue, ContactListItem.OWNER);
                    }
                };
        columnConfig[i].setMinimumColumnWidth(100);
        columnConfig[i].setShow(false);
        columnConfig[i].setColumnSortable(true);
        columns.add(columnConfig[i]);

        //candidate project
        columnConfig[++i] = new

                ColumnDefinitionConfig<ContactListItem, SelectItem>(Property.get(Constants.PROJECT, wfmStrings.project()), ContactListItem.PROJECT, 150) {
                    @Override
                    public SelectItem getCellValue(final ContactListItem item) {
                        return item.getProjectItem();
                    }
                };
        columnConfig[i].setMinimumColumnWidth(100);
        columnConfig[i].setColumnSortable(true);
        columnConfig[i].setShow(false);
        columns.add(columnConfig[i]);

        //candidate creator
        columnConfig[++i] = new

                ColumnDefinitionConfig<ContactListItem, String>(wfmStrings.createdBy(), ContactListItem.CREATED_BY, 200) {
                    @Override
                    public String getCellValue(final ContactListItem item) {
                        return item.getCreator() != null ? item.getCreator() : "";
                    }
                };
        columnConfig[i].setMinimumColumnWidth(150);
        columnConfig[i].setColumnSortable(true);
        if (!CandidatesListView.SHORT_LIST.equals(this.fromView)) {
            columnConfig[i].setShow(false);
        }
        columns.add(columnConfig[i]);
        columnConfig[i].setShow(false);

        //candidate updater
        columnConfig[++i] = new

                ColumnDefinitionConfig<ContactListItem, String>(wfmStrings.modifiedBy(), ContactListItem.UPDATED_BY, 200) {
                    @Override
                    public String getCellValue(final ContactListItem item) {
                        return item.getUpdater() != null ? item.getUpdater() : "";
                    }
                };
        columnConfig[i].setMinimumColumnWidth(150);
        columnConfig[i].setColumnSortable(true);
        if (!CandidatesListView.SHORT_LIST.equals(this.fromView)) {
            columnConfig[i].setShow(false);
        }
        columns.add(columnConfig[i]);
        columnConfig[i].setShow(false);

        //candidate created date
        columnConfig[++i] = new

                ColumnDefinitionConfig<ContactListItem, String>(wfmStrings.createdDate(), ContactListItem.CREATION_DATE, 200) {
                    @Override
                    public String getCellValue(final ContactListItem item) {
                        return item.getCreatedDate() != null ? DateUtils.formatInternal(item.getCreatedDate()) : "";
                    }
                };
        columnConfig[i].setMinimumColumnWidth(150);
        columnConfig[i].setColumnSortable(true);
        if (!CandidatesListView.SHORT_LIST.equals(this.fromView)) {
            columnConfig[i].setShow(false);
        }
        columns.add(columnConfig[i]);
        columnConfig[i].setShow(false);

        //candidate last modified date
        columnConfig[++i] = new

                ColumnDefinitionConfig<ContactListItem, String>(wfmStrings.modifiedDate(), ContactListItem.LAST_MODIFIED, 200) {
                    @Override
                    public String getCellValue(final ContactListItem item) {
                        return item.getUpdatedDate() != null ? DateUtils.formatInternal(item.getUpdatedDate()) : "";
                    }
                };
        columnConfig[i].setMinimumColumnWidth(150);
        columnConfig[i].setColumnSortable(true);
        if (!CandidatesListView.SHORT_LIST.equals(this.fromView)) {
            columnConfig[i].setShow(false);
        }
        columns.add(columnConfig[i]);
        columnConfig[i].setShow(false);
        //candidate skills
        columnConfig[++i] = new

                ColumnDefinitionConfig<ContactListItem, String>(wfmStrings.skills(), ContactListItem.CANDIDATE_SKILLS, 150) {
                    @Override
                    public String getCellValue(final ContactListItem item) {
                        return item.getSkills();
                    }

                    @Override
                    public void setCellValue(ContactListItem rowValue, String cellValue) {
                        rowValue.setSkills(cellValue);
                        saveCellValue(rowValue);
                    }
                };
        columnConfig[i].setMinimumColumnWidth(100);
        columnConfig[i].setColumnSortable(true);
        columnConfig[i].setShow(false);
        columns.add(columnConfig[i]);

        initCellEdit(CustomColumnDefinitionConfig.getEditableColumns(columns));
        return columns.toArray(new ColumnDefinitionConfig[]{});
    }

    private Integer getSelectedItemID(SelectItem cellValue) {
        return cellValue != null ? cellValue.getId() : null;
    }

    private String getSelectedItemName(SelectItem cellValue) {
        return cellValue != null ? cellValue.getName() : null;
    }

    private void initCellEdit(Map<String, CustomColumnDefinitionConfig> columns) {
        for (final Map.Entry<String, CustomColumnDefinitionConfig> entry : columns.entrySet()) {
            InlineCellEditor widget = null;
            CustomColumnDefinitionConfig column = entry.getValue();
            if (ContactListItem.CANDIDATE_SKILLS.equals(entry.getKey())) {
                widget = new TextBoxCellEditor<String>() {
                    @Override
                    protected String getValue() {
                        String cellValue = getText();
                        return cellValue == null || "".equals(cellValue) || wfmStrings.notAvailable().equalsIgnoreCase(cellValue) ? null : getText();
                    }

                    @Override
                    protected void setValue(String cellValue) {
                        if (!(cellValue == null || "".equals(cellValue) || wfmStrings.notAvailable().equalsIgnoreCase(cellValue))) {
                            setText(cellValue);
                        }
                    }
                };
            } else if (ContactListItem.LEAD_STATUS.equals(entry.getKey()) && Utils.hasPermission(PermissionConstants.HRMS_CHANGE_STATUS_CANDIDATE)) {
                widget = new DropDownCellEditor<SelectItem>() {
                    @Override
                    protected SelectItem getValue() {
                        return getListBox().getSelectedItem(true);
                    }

                    @Override
                    protected void setValue(SelectItem cellValue) {
                        getListBox().setAllowFirstItem(true);
                        setItemsAndSelect(getListBox(), entry.getKey(), cellValue);
                        if (cellValue != null && cellValue.getId() != null) {
                            getListBox().setSelected(cellValue.getId());
                        } else {
                            getListBox().setSelectedNullLabel();
                        }
                    }
                };
            } else if (ContactListItem.OWNER.equals(entry.getKey())
                    || ContactListItem.LEAD_SOURCE.equals(entry.getKey())) {
                widget = new DropDownCellEditor<SelectItem>() {
                    @Override
                    protected SelectItem getValue() {
                        return getListBox().getSelectedItem(true);
                    }

                    @Override
                    protected void setValue(SelectItem cellValue) {
                        getListBox().setAllowFirstItem(true);
                        setItemsAndSelect(getListBox(), entry.getKey(), cellValue);
                        if (cellValue != null && cellValue.getId() != null) {
                            getListBox().setSelected(cellValue.getId());
                        } else {
                            getListBox().setSelectedNullLabel();
                        }
                    }
                };
            } else if (ContactListItem.DATE_OF_BIRTH.equals(entry.getKey())) {
                widget = new DateTimePickerCellEditor<String>(true) {
                    @Override
                    protected String getValue() {
                        return DateUtils.format1(getDate());
                    }

                    @Override
                    protected void setValue(String cellValue) {
                        try {
                            if (cellValue != null && !"".equals(cellValue)) {
                                setDate(DateUtils.parse(cellValue), true);
                            }
                        } catch (DateFormatException e) {
                            e.printStackTrace();
                        }
                    }
                };
            }

            if (widget != null) {
                column.setCellEditor(widget);
                column.setCellChangesSave(new CellChange<ContactListItem>() {
                    @Override
                    public void saveCell(ContactListItem rowValue, String columnCodeName) {
                        saveContactsCellValue(rowValue, columnCodeName);
                    }
                });
            }
        }
    }

    private DateNonConvertable getNonConvertable(Object value) {
        if (value != null) {
            if (value instanceof Date) {
                return new DateNonConvertable((Date) value);
            } else if (value instanceof DateNonConvertable) {
                return (DateNonConvertable) value;
            }
        }
        return null;
    }

    private void setItemsAndSelect(final DataListBox listBox, String key, SelectItem selectedItem) {
        if (listBox.getItems() == null || listBox.getItems().length < 1) {
            if (ContactListItem.LEAD_STATUS.equals(key)) {
                LoadingPanel.loading(true);
                RecruitmentService.App.get().getCandidateStatuses(new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void failure(Throwable throwable) {
                        super.failure(throwable);
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(SelectItem[] result) {
                        if (result != null && result.length > 0) {
                            listBox.setItems(result);
                        }
                        LoadingPanel.loading(false);
                    }
                });
            } else if (ContactListItem.LEAD_SOURCE.equals(key)) {
                LoadingPanel.loading(true);
                RecruitmentService.App.get().getCandidateSources(new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void failure(Throwable throwable) {
                        super.failure(throwable);
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(SelectItem[] result) {
                        if (result != null && result.length > 0) {
                            listBox.setItems(result);
                        }
                        LoadingPanel.loading(false);
                    }
                });
            } else if (ContactListItem.OWNER.equals(key)) {
                LoadingPanel.loading(true);
                RecruitmentService.App.get().getOwners(new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void failure(Throwable throwable) {
                        super.failure(throwable);
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(SelectItem[] result) {
                        if (result != null && result.length > 0) {
                            listBox.setItems(result);
                        }
                        LoadingPanel.loading(false);
                    }
                });
            }
        }
    }

    private void saveContactsCellValue(ContactListItem rowValue, String columnCodeName) {
        ContactService.App.get().saveContactEditCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                listPanel.reloadPage();
            }
        });
    }

    private Anchor getActionAnchor(final ContactListItem item) {
        int actionItemCount = 0;
        MenuBar menuBar = new MenuBar(true);
        menuBar.setAutoOpen(true);
        //candidate summary
        MenuPopItem summary = new MenuPopItem(wfmStrings.summaryView(), "icon-candidates-small");
        summary.getElement().setId(candidateListViewID + "candidate_summary");
        summary.setCommand(() -> {
            if (item != null) {
                SinksContainerFactory.entryPoint.onHistoryChanged("candidate|summary/" + item.getObjectId(), item.getNumberData() != null ? item.getNumberData().getNumberString() : item.getName(), item.getName());
            }
        });
        actionItemCount++;
        menuBar.addItem(summary);
        //edit candidate
        if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_CANDIDATE) && (item.isAllowEdit() || item.getCandidateStatus() == null)) {
            final MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
            edit.getElement().setId(candidateListViewID + "edit_candidate");
            edit.setCommand(() -> {
                if (item != null) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("candidateedit|editcandidate/" + item.getObjectId(), item.getNumberData() != null ? item.getNumberData().getNumberString() : item.getName(), item.getName());
                }
            });
            actionItemCount++;
            menuBar.addItem(edit);
        }
        //match to vacancies
        if (Utils.hasPermission(PermissionConstants.HRMS_CANDIDATE_MATCH_TO_VACANCY)) {
            final MenuPopItem matchToVacancies = new MenuPopItem(hrmsStrings.matchToVacancies(), "icon-task-small");
            matchToVacancies.getElement().setId(candidateListViewID + "match_to_vacancies");
            matchToVacancies.setCommand(() -> {
                //match to vacancies logic
                new MatchToVacancies(item.getObjectId());
            });
            actionItemCount++;
            menuBar.addItem(matchToVacancies);
        }

        PropertyItem propertyItem = Utils.getProperTy(Constants.CANDIDATE);
        if (propertyItem != null && propertyItem.getConvertItems() != null && propertyItem.getConvertItems().length > 0) {
            MenuPopItem convertMenuPopItem = new MenuPopItem(wfmStrings.convert(), "icon-add-green");

            MenuBar convertMenu = new MenuBar(true);
            convertMenu.setAutoOpen(true);
            int convertItems = 0;
            for (ConvertItem convertItem : propertyItem.getConvertItems()) {
                if (convertItem != null) {
                    convertItems = getConvertItems(item, menuBar, convertMenu, convertItems, convertItem);
                }
            }

            if (convertItems > 0) {
                convertMenuPopItem.setSubMenu(convertMenu);
                menuBar.addItem(convertMenuPopItem);
                actionItemCount++;
            }
        }

        final MenuBar bar = new MenuBar(true);
        bar.setAutoOpen(true);

        boolean callQuickAddPermission = Utils.hasPermission(PermissionConstants.HRMS_QUICK_CALL_CANDIDATE);
        boolean callAddPermission = Utils.hasPermission(PermissionConstants.HRMS_CALL_CANDIDATE);
        //Log a Call
        if (callAddPermission || callQuickAddPermission) {
            final MenuPopItem logCall = new MenuPopItem(Property.get(Constants.LOGACALL, wfmStrings.logCall()), "icon-call");
            logCall.getElement().setId(candidateListViewID + "log_a_call");
            logCall.setCommand(() -> {
                logCall.closeAll(menuBar);
                if (item != null) {
                    if (callQuickAddPermission) {
                        new ActivityQuickAddForm(Appointment.CALL_LOG, RelationItem.newEventRelation(RelationItem.TYPE_CANDIDATE, item.getObjectId(), item.getName()), RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getCrmAccount().getObjectId(), item.getCrmAccount().getName()));
                    } else {
                        SinksContainerFactory.entryPoint.onHistoryChanged("event|add/add/" + Appointment.CALL_LOG + "/" + item.getObjectId() + "/" + RelationItem.TYPE_CANDIDATE);
                    }
                }
            });
            bar.addItem(logCall);
        }

        boolean interviewQuickAddPermission = Utils.hasPermission(PermissionConstants.HRMS_QUICK_INTERVIEW_CANDIDATE);
        boolean interviewAddPermission = Utils.hasPermission(PermissionConstants.HRMS_INTERVIEW_CANDIDATE);
        //interview to candidate
        if (interviewAddPermission || interviewQuickAddPermission) {
            final MenuPopItem inviteToInterview = new MenuPopItem(wfmStrings.interview(), "icon-recruitmentHome-small");
            inviteToInterview.getElement().setId(candidateListViewID + "interview");
            inviteToInterview.setCommand(() -> {
                inviteToInterview.closeAll(menuBar);
                if (item != null) {
                    if (interviewQuickAddPermission) {
                        ActivityQuickAddForm view = new ActivityQuickAddForm(Appointment.INTERVIEW, RelationItem.newEventRelation(RelationItem.TYPE_CANDIDATE, item.getObjectId(), item.getName()), RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getCrmAccount().getObjectId(), item.getCrmAccount().getName()));
                        view.setCommand(() -> {
                            changeStatusWithCode(item, ContactListItem.C_S_INTERVIEW);
                        });
                    } else {
                        SinksContainerFactory.entryPoint.onHistoryChanged("event|add/add/" + Appointment.INTERVIEW + "/" + item.getObjectId() + "/" + RelationItem.TYPE_CANDIDATE);
                    }
                }
            });
            bar.addItem(inviteToInterview);
        }

        if (Utils.hasPermission(PermissionConstants.HRMS_CONDIDATE_SMS_SEND)) {
            final MenuPopItem addSms = new MenuPopItem(wfmStrings.sms(), "icon-sms");
            addSms.ensureDebugId(wfmStrings.sendSms());
            addSms.setCommand(() -> {
                addSms.closeAll(menuBar);
                item.setContactType(ContactListItem.CANDIDATE);
                new ActivityQuickAddForm(Appointment.SMS, item, null, RelationItem.newEventRelation(RelationItem.TYPE_CANDIDATE, item.getObjectId(), item.getName()));
            });
            bar.addItem(addSms);
        }

        if (Utils.hasPermission(PermissionConstants.HRMS_CALL_CANDIDATE, PermissionConstants.HRMS_INTERVIEW_CANDIDATE, PermissionConstants.HRMS_INTERVIEW_CANDIDATE)) {
            actionItemCount++;
            menuBar.addItem(new MenuPopItem(wfmStrings.add(), "icon-add-green", bar));
        }

        if (Utils.hasPermission(PermissionConstants.HRMS_CHANGE_STATUS_CANDIDATE)) {
            final MenuBar changeStatusBar = new MenuBar(true);
            changeStatusBar.setAutoOpen(true);


            final MenuPopItem makePlacement;

            if (!SHORT_LIST.equals(fromView)) {
                //Select Candidate / Add to shortlist
                if (Utils.hasPermission(PermissionConstants.HRMS_SELECT_CANDIDATE)) {
                    MenuPopItem selectCandidate = new MenuPopItem(hrmsStrings.shortlistOnly(), "icon-shortlists-small");
                    selectCandidate.getElement().setId(candidateListViewID + "add_to_shortlist");
                    selectCandidate.setCommand(() -> {
                        selectCandidate.closeAll(menuBar);
                        changeStatusWithCode(item, ContactListItem.C_S_SHORTLIST);
                    });
                    changeStatusBar.addItem(selectCandidate);
                }

                if (Utils.hasPermission(PermissionConstants.HRMS_MAKE_PLACEMENT) && !item.isHasPlacement()) {
                    makePlacement = new MenuPopItem(hrmsStrings.makePlacement(), "icon-recruitment-small");
                    makePlacement.getElement().setId(candidateListViewID + "make_placement");
                    makePlacement.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("placement|add/add/" + item.getObjectId()));
                    actionItemCount++;
                    menuBar.addItem(makePlacement);
                }
            }

            final MenuPopItem hireCandidate = new MenuPopItem(wfmStrings.hireOnly(), "icon-candidates-small");
            hireCandidate.getElement().setId(candidateListViewID + "hire");
            hireCandidate.setCommand(() -> {
                if (!item.isHasPlacement()) {
                    Info.show(wfmStrings.pleaseFirstAddPlacementForThisCandidate(), Info.Type.WARNING);
                } else {
                    hireCandidate(item.getPlacementId());
                }
            });
            changeStatusBar.addItem(hireCandidate);

            if (statusList.getItems() != null) {
                statusList.getItems();
                for (SelectItem status : statusList.getItems()) {
                    if (!ContactListItem.C_S_HIRED.equals(status.getCode()) && !ContactListItem.C_S_SHORTLIST.equals(status.getCode()) && !status.getCode().equals(item.getLeadStatus(true).getCode())) {
                        MenuPopItem selectCandidate = new MenuPopItem(status.getName(), "icon-shortlists-small");
                        selectCandidate.getElement().setId(candidateListViewID + "add_to_shortlist");
                        changeStatusBar.addItem(selectCandidate);
                        selectCandidate.setCommand(() -> {
                            selectCandidate.closeAll(menuBar);
                            if ((item.isDraggable() || item.getCandidateStatus() == null) && status.isDraggable()) {
                                if (!item.isHasPlacement() && (ContactListItem.C_S_PLACED.equals(status.getCode()) || ContactListItem.C_S_OFFER_MADE.equals(status.getCode()) || ContactListItem.C_S_HIRED.equals(status.getCode()))) {
                                    SinksContainerFactory.entryPoint.onHistoryChanged("placement|add/add/" + item.getObjectId());
                                } else {
                                    changeStatusWithCode(item, status.getCode());
                                }
                            } else {
                                Info.warn(wfmStrings.youDontHavePermission());
                            }
                        });
                    }
                }
            }

            actionItemCount++;
            menuBar.addItem(new MenuPopItem(wfmStrings.changeStatus(), "icon-add-green", changeStatusBar));
        }

        //remove candidate
        if (Utils.hasPermission(PermissionConstants.HRMS_DELETE_CANDIDATE)) {
            final MenuPopItem removeCandidate = new MenuPopItem(wfmStrings.delete(), "icon-remove");
            removeCandidate.getElement().setId(candidateListViewID + "remove_candidate");
            removeCandidate.setCommand(() -> {
                if (item != null) {
                    deleteCandidateItem(Utils.asArrayList(item));
                }
            });
            actionItemCount++;
            menuBar.addItem(removeCandidate);
        }

        final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
        toolItem.setWidget(menuBar);
        return toolItem.getAction();
    }

    private void hireCandidate(Integer placementId) {
        SinksContainerFactory.entryPoint.onHistoryChanged("singleemployee|add/add/" + FROM_HRMS + "/true/" + placementId);
    }

    private void deleteCandidateItem(final ArrayList<ContactListItem> items) {
        ContactListItem contactListItem = items.iterator().next();

        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());

        String candidateNameF = "&nbsp <font color='#15428B'><b>\"" + (contactListItem != null ? contactListItem.getName() : "") + "\"</b></font> " /*+ wfmStrings.candidate()*/;
        String message = wfmMessages.sureYouWantToDelete(candidateNameF, " ?");
        if (items.size() > 1) {
            message = wfmStrings.areYouSureYouWantToDeleteTheSelectedRecords();
        }

        messageBox.setMessage(message);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                LoadingPanel.loading(true);
                ContactService.App.get().canDeleteCandidate(getIDs(items), new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        LoadingPanel.loading(false);
                        if (aBoolean) {
                            deleteCanndidate(items);
                        } else {
                            Info.show(wfmStrings.youCannotDelete(), Info.Type.WARNING);
                        }
                    }
                });
            }
        });
        messageBox.open();
    }

    private void deleteCanndidate(ArrayList<ContactListItem> items) {
        ContactService.App.get().deleteContacts(getIDs(items), null, false, new AbstractAsyncCallback<ArrayList<Integer>>() {
            @Override
            public void failure(Throwable caught) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(ArrayList<Integer> result) {
                Info.show(wfmMessages.yourSomethingHasBeenDeleted(wfmStrings.candidate().toLowerCase()), Info.Type.INFO);
                listPanel.reloadPage();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CANDIDATE_DELETE, result, CandidatesListView.this);
            }
        });
    }

    private ArrayList<Integer> getIDs(ArrayList<ContactListItem> selectedContacts) {
        ArrayList<Integer> ids = new ArrayList<>();
        if (selectedContacts.size() > 0) {
            for (ContactListItem item : selectedContacts) {
                if (item != null && !ids.contains(item.getObjectId())) {
                    ids.add(item.getObjectId());
                }
            }
        }
        return ids;
    }

    private GuideListingPanelDesign getListDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionSelectCandidate() ? CandidatesListView.this::selectCandidate : hasPermissionToAdd() ? CandidatesListView.this::addNewCandidate : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return !CandidatesListView.SHORT_LIST.equals(fromView) ? CandidatesListView.this::openImportTab : null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> {
                            data.setCustomDataPut(FacetFilterCutomField.ISSHORTLIST, "" + (SHORT_LIST.equals(fromView)));
                            RbacService.App.get().getCRMFacetFilterData(CrmConstants.CANDIDATE, data, new AbstractAsyncCallback<FacetFilterRpc>() {
                                public void failure(Throwable caught) {
                                    callback.onFailure(caught);
                                }

                                public void success(FacetFilterRpc data) {
                                    callback.onSuccess(data);
                                }
                            });
                        };
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return getCandidateContentConfigure();
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (SHORT_LIST.equals(fromView)) {
                    if (Utils.hasPermission(PermissionConstants.HRMS_SELECT_CANDIDATE)) {
                        ActionButton selectCandidate = getAddNewButton();
                        selectCandidate.getElement().setId(candidateListViewID + "shortlist_select_candidate");
                        selectCandidate.addClickHandler(event -> selectCandidate());
                        return selectCandidate;
                    } else {
                        return null;
                    }
                } else if (hasPermissionToAdd()) {
                    ActionButton addNew = getAddNewButton(ActionButton.Type.TOOLMENU);
                    MenuBar menuBar = new MenuBar(true);

                    if (Utils.hasPermission(PermissionConstants.HRMS_ADD_CANDIDATE)) {
                        MenuPopItem newAdd = new MenuPopItem(wfmStrings.candidate());
                        newAdd.getElement().setId("addCandidate");
                        newAdd.setCommand(() -> addNewCandidate());
                        menuBar.addItem(newAdd);
                    }

                    if (!Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_EMPLOYEE_CODE_INTEGER) && Utils.hasPermission(PermissionConstants.HRMS_QUICK_ADD_CANDIDATE)) {
                        MenuPopItem quickAdd = new MenuPopItem(wfmStrings.quickAdd());
                        quickAdd.getElement().setId("quickAdd");
                        quickAdd.setCommand(() -> quickAddBox.show());
                        menuBar.addItem(quickAdd);
                    } else if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_EMPLOYEE_CODE_INTEGER)) {
                        MenuPopItem quickAdd = new MenuPopItem(wfmStrings.quickAdd());
                        quickAdd.getElement().setId("validate");
                        CandidateQuickValidate candidateQuickValidate = new CandidateQuickValidate();

                        quickAdd.setCommand(() -> candidateQuickValidate.show());
                        menuBar.addItem(quickAdd);
                    }

                    addNew.setMenu(menuBar);
                    return addNew;
                }
                return null;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                if (Utils.hasPermission(PermissionConstants.HRMS_DELETE_CANDIDATE) || Utils.hasPermission(PermissionConstants.HRMS_CHANGE_STATUS_CANDIDATE)) {
                    if (!SHORT_LIST.equals(fromView)) {
                        final ActionButton more = new ActionButton(ActionButton.getMoreString(), ActionButton.Type.TOOLMENU);
                        more.getElement().setId(candidateListViewID + "more_button");
                        more.addDomHandler(event -> {
                            MenuBar menu = getActionsForSelections();
                            menu.setAutoOpen(true);
                            more.setMenu(menu);
                        }, MouseOverEvent.getType());
                        return more;
                    }
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                if (Utils.hasPermission(HRMS_CANDIDATE_IMPORT)) {
                    imp = new ImportFilePopUp(ImportTypeEnum.CANDIDATE, null);
                    imp.setSubmitCompleted(() -> {
                        if (imp.getObjectId() != null) {
                            goTo("importcandidate|add/add/" + imp.getObjectId());
                        }
                    });

                    ImportFileActionLink link = new ImportFileActionLink();
                    link.addClickHandler(ch -> openImportTab());
                    if (!CandidatesListView.SHORT_LIST.equals(fromView)) {
                        menuContainer.add(link);
                    }
                }
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.hasPermission(PermissionConstants.HRMS_EDIT_CANDIDATE);
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(SHORT_LIST.equals(fromView) ? wfmStrings.currentlyShortListMessage() : wfmStrings.currentlyCandidateMessage());
                if ((!SHORT_LIST.equals(fromView)) && !Utils.hasRole(CLIENT)) {
                    message.setHref(clickEvent -> quickAddBox.show());
                    message.setTextBeforeLink(hrmsStrings.pleaseAddNewCandidate());
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private SinksContainer selectCandidate() {
        return SinksContainerFactory.entryPoint.onHistoryChanged("candidateSelect|selectCandidateT/");
    }

    private void openImportTab() {
        imp.open();
    }

    private SinksContainer addNewCandidate() {
        return SinksContainerFactory.entryPoint.onHistoryChanged("candidate|add/add");
    }

    private FacetContentConfigure getCandidateContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(8, wfmStrings.filter());

        contentConfigure.addContentConfigure(FacetContentType.CandidateFacetFilter.getContentCode()[3], wfmStrings.status(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrContactRepresenter.FIELD_LEAD_STATUS_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrContactRepresenter.FIELD_LEAD_STATUS_ID_CODE_NAME;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.REFERENCE;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.CandidateFacetFilter.getContentCode()[0], wfmStrings.matchedVacancies(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrContactRepresenter.FIELD_VACANCY_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrContactRepresenter.FIELD_VACANCY_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.CandidateFacetFilter.getContentCode()[1], wfmStrings.source(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrContactRepresenter.FIELD_LEAD_SOURCE_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrContactRepresenter.FIELD_LEAD_SOURCE_ID_CODE_NAME;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.REFERENCE;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.CandidateFacetFilter.getContentCode()[2], Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrContactRepresenter.FIELD_PREFERRED_LOCATION_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrContactRepresenter.FIELD_PREFERRED_LOCATION_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return true;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.CandidateFacetFilter.getContentCode()[4], wfmStrings.owner(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrContactRepresenter.FIELD_OWNER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrContactRepresenter.FIELD_OWNER_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.CandidateFacetFilter.getContentCode()[5], Property.get(Constants.PROJECT, wfmStrings.project()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrContactRepresenter.FIELD_CANDIDATE_PROJECT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrContactRepresenter.FIELD_CANDIDATE_PROJECT_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.CandidateFacetFilter.getContentCode()[6],wfmStrings.department(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrContactRepresenter.FIELD_CANDIDATE_DEPARTMENT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrContactRepresenter.FIELD_CANDIDATE_DEPARTMENT_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return true;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.CandidateFacetFilter.getContentCode()[7],wfmStrings.position(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrContactRepresenter.FIELD_CANDIDATE_POSITION_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrContactRepresenter.FIELD_CANDIDATE_POSITION_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return true;
            }
        });

        contentConfigure.addContentConfigureDateListBox(SolrContactRepresenter.FIELD_CREATION_DATE, wfmStrings.createdDate());
        contentConfigure.addContentConfigureDateListBox(SolrContactRepresenter.FIELD_UPDATE_DATE, wfmStrings.modifiedDate());
        return contentConfigure;
    }

    private MenuBar getActionsForSelections() {
        if (selectedRows != null && selectedRows.size() > 0) {
            if (actions == null) {
                actions = new ContextMenu();
                actions.getMenuBar().setAutoOpen(true);

                if (Utils.hasPermission(PermissionConstants.HRMS_CHANGE_STATUS_CANDIDATE)) {
                    final MenuBar changeStatus = new MenuBar(true);
                    changeStatus.ensureDebugId("leadListChangeStatus");
                    changeStatus.setAutoOpen(true);
                    statusList.getItems();
                    for (SelectItem status : statusList.getItems()) {
                        changeStatus.addItem("<span>" + status.getName() + "</span>", true, () -> {
                            actions.hide();
                            if (selectedRows == null || selectedRows.size() == 0) {
                                listPanel.showSelectOneMessage();
                            } else {
                                ArrayList<Integer> ids = new ArrayList<>();
                                for (ContactListItem item : selectedRows) {
                                    ids.add(item.getObjectId());
                                }
                                changeStatus(ids, status.getId());
                            }
                        });
                    }
                    actions.addMenuItemWithMenuBar(wfmStrings.changeStatus(), "", true, changeStatus);
                }

                /*MenuItem selectCandidates = new MenuItem("<span>&nbsp;&nbsp;" + hrmsStrings.addToShortList() + "</span>", true, (Command) () -> {
                    ArrayList<ContactListItem> contactListItemArrayList = new ArrayList<>(selectedRows);
                    LoadingPanel.loading(true);
                    RecruitmentService.App.get().changeStatus(HRMS.RECRUITMENT.CANDIDATE, getIDs(contactListItemArrayList),
                            ContactListItem._CANDIDATE_STATUS, ContactListItem.C_S_MATCHED, new AbstractAsyncCallback<String>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    LoadingPanel.loading(false);
                                }

                                @Override
                                public void onSuccess(String result) {
                                    LoadingPanel.loading(false);
                                    if (result.equals(PermissionConstants.DENY)) {
                                        Info.show(wfmMessages.youDoNotHaveEnoughPermission(hrmsStrings.addToShortList(), hrmsStrings.candidate()), Info.Type.WARNING);
                                    }
                                    listPanel.reloadPage();
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CANDIDATE_SELECT, result, CandidatesListView.this);
                                }
                            });
                });
                selectCandidates.getElement().setId(candidateListViewID + "more_add_to_shortlist");
                actions.getMenuBar().addItem(selectCandidates);*/
                if (Utils.hasPermission(PermissionConstants.HRMS_DELETE_CANDIDATE)) {
                    MenuItem removeCandidates = new MenuItem("<span>&nbsp;&nbsp;" + wfmStrings.delete() + "</span>", true, (Command) () -> {
                        //delete candidates
                        deleteCandidateItem(new ArrayList<>(selectedRows));
                    });
                    removeCandidates.getElement().setId(candidateListViewID + "more_remove_candidate");
                    actions.getMenuBar().addItem(removeCandidates);
                }
            }
            actions.getMenuBar().setAutoOpen(true);
            return actions.getMenuBar();
        } else {
            if (actionsEmpty == null) {
                actionsEmpty = new ContextMenu();
                actionsEmpty.getMenuBar().setAutoOpen(true);
                MenuItem emptyMessageItem = new MenuItem("<span>&nbsp;&nbsp;" + wfmStrings.selectAnyItemToActivateBatchActions() + "</span>", true, (Command) () -> {
                });
                emptyMessageItem.getElement().setId(candidateListViewID + "more_empty_select");
                actionsEmpty.getMenuBar().addItem(emptyMessageItem);
            }
            actionsEmpty.getMenuBar().setAutoOpen(true);
            return actionsEmpty.getMenuBar();
        }
    }

    private void changeStatus(ArrayList<Integer> ids, Integer statusId) {
        LoadingPanel.loading(true);
        RecruitmentService.App.get().changeCandidateStatus(ids, statusId, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Void s) {
                LoadingPanel.loading(false);
                actions.getMenuBar().removeFromParent();
                listPanel.reloadPage();
            }
        });
    }

    private void changeStatusWithCode(ContactListItem item, String statusCode) {
        LoadingPanel.loading(true);
        ArrayList<ContactListItem> contactListItemArrayList = new ArrayList<>();
        contactListItemArrayList.add(item);
        RecruitmentService.App.get().changeStatus(HRMS.RECRUITMENT.CANDIDATE, getIDs(contactListItemArrayList),
                ContactListItem._CANDIDATE_STATUS, statusCode, new AbstractAsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(String result) {
                        LoadingPanel.loading(false);
                        listPanel.reloadPage();
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CANDIDATE_SELECT, result, CandidatesListView.this);
                    }
                });
    }


    private ListingRequestProvider<ContactListItem> getListProvider() {
        return (filterParametrs, callback) -> initCandidateList(filterParametrs, callback, null);
    }

    private void initCandidateList(ListingFilterParameter filterParametrs, ListingCallback<ContactListItem> listingCallback, Span container) {
        filterParametrs = filterParametrs == null ? new ListingFilterParameter() : filterParametrs;
        filterParametrs.setShortList(SHORT_LIST.equals(fromView));
        filterParametrs.setRelationID(vacancyId);
        getEmployeesMaxCount();
        RecruitmentService.App.get().getCandidateStatuses(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(SelectItem[] result) {
                if (result != null && result.length > 0) {
                    statusList.setItems(result);
                }
            }
        });
        RecruitmentService.App.get().listCandidates(filterParametrs, new AsyncCallback<ListResult<ContactListItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                listingCallback.onFailure(caught);
            }

            @Override
            public void onSuccess(ListResult<ContactListItem> result) {
//                listingCallback.onSuccess(result);
                if (listingCallback != null) {
                    listingCallback.onSuccess(result);
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (result.getTotal() != null && result.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(result.getTotal()));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
    }

    private void saveCandidateEditCellValue(ContactListItem rowValue, String columnCodeName) {
        ContactService.App.get().saveContactEditCellValue(rowValue, columnCodeName, new AbstractAsyncCallback() {
        });
    }

    private void initQuickAddView() {
        quickAddBox = new KpiSideNavBox();
        setStyleName(quickAddBox.getElement(), "quick-add", true);
        CandidateQuickAddForm quickAddForm = new CandidateQuickAddForm();
        Heading header = new Heading(HeadingSize.H1);
        header.setText(hrmsStrings.addCandidate());

        WfmButton2 saveBtn = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        WfmButton2 cancelBtn = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_RESET);
        saveBtn.addClickHandler(event -> {
            saveBtn.setEnabled(false);
            cancelBtn.setEnabled(false);
            if (quickAddForm.validate()) {
                quickAddForm.save();

                saveBtn.setEnabled(true);
                cancelBtn.setEnabled(true);

            } else {
                saveBtn.setEnabled(true);
                cancelBtn.setEnabled(true);
            }
        });
        cancelBtn.addClickHandler(event -> {
            quickAddForm.clearForm();
            quickAddBox.hide();
        });
        quickAddBox.getCloseContent().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                quickAddForm.clearForm();
                quickAddBox.hide();
            }
        });
        quickAddForm.setCommand(new ExtendedCommand() {
            @Override
            public void execute(Integer id) {
                cancelBtn.setEnabled(true);
                saveBtn.setEnabled(true);
                if (id > 0) {
                    quickAddForm.clearForm();
                    quickAddBox.hide();

                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CANDIDATE_ADD_EDIT, id, CandidatesListView.this);
                }
            }
        });
        quickAddBox.addOpeningHandler(event -> quickAddForm.getCandidateQuickData());

        quickAddBox.addHeader(header);
        quickAddBox.addBody(quickAddForm);
        quickAddBox.addFooter(saveBtn);
        quickAddBox.addFooter(cancelBtn);
        saveBtn.getElement().setId("saveButton");
        cancelBtn.ensureDebugId("cancelButton");
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

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setLimit(1);
        if (parentId != null) {
            initCandidateList(fp, null, container);
            onInitialize();
            clear();
        }
    }


    private int getConvertItems(ContactListItem rowValue, MenuBar menuBar, MenuBar convertMenu, int convertItems, ConvertItem convertItem) {
        if (RelationItem.TYPE_PLACEMENT.equals(convertItem.getCode()) && Utils.hasPermission(PermissionConstants.HRMS_ADD_PLACEMENT)) {
            MenuPopItem convertToPlacement = new MenuPopItem(Property.get(Constants.PLACEMENT, wfmStrings.placement()), "icon-send-sales-invoice");
            convertToPlacement.setCommand(() -> {
                convertToPlacement.closeAll(menuBar);
                if (Utils.getPathName().contains("Hrms.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("placement|add/add/CONVERT/" + RelationItem.TYPE_CANDIDATE + "/" + rowValue.getObjectId());
                } else {
                    Utils.openURL("Hrms.html#placement|add/add/CONVERT/" + RelationItem.TYPE_CANDIDATE + "/" + rowValue.getObjectId());
                }
            });
            convertToPlacement.ensureDebugId("convert_placement");
            convertMenu.addItem(convertToPlacement);
            convertItems++;
        }
        return convertItems;
    }
}