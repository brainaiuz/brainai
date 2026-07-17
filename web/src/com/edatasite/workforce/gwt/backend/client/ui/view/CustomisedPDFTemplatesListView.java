package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.PDFTemplatesListItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.PdfGenerateTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SchemaLookUp;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Mar 18, 2011
 * Time: 3:13:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomisedPDFTemplatesListView extends BaseListView {
    private ListingPanel<PDFTemplatesListItem> list;
    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private Integer companyID;

    public CustomisedPDFTemplatesListView() {
        super("customisedPdfTemplates", backendStrings.customisedPDFTemplates());
    }

    @Override
    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.CustomisedPDFTemplatesListView, drawColumns(), provider(), designer());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PDF_TEMPLATE_SAVED, this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    private ListingRequestProvider<PDFTemplatesListItem> provider() {
        return (filterParametrs, callback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            filterParametrs.setCompanyID(companyID);
            BackendService.App.get().getCompanyPDFTemplates(filterParametrs, new AbstractAsyncCallback<ListResult<PDFTemplatesListItem>>() {
                @Override
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<PDFTemplatesListItem> result) {
                    callback.onSuccess(result);
                }
            });

        };
    }

    private ListingPanelDesign designer() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public HorizontalPanel initTopToolBarWidgets() {
                final HorizontalPanel topPanel = new HorizontalPanel();
                final SchemaLookUp schemaLookUp = new SchemaLookUp();
                schemaLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
                    companyID = schemaLookUp.getSelectedItemID();
                    list.reloadPage();
                });

                ActionButton addPDFTemplate = new ActionButton(ActionButton.getNewString(), ActionButton.Type.TOOLMENU);
                addPDFTemplate.setStyleName("btn btn--success hasicon--right");
                addPDFTemplate.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem itextPdf = new MenuPopItem("Kpi Pdf");
                itextPdf.setScheduledCommand(() -> {
                    if (schemaLookUp.getSelectedItemID() == null || companyID == null) {
                        Window.alert("Please select company");
                        return;
                    }
                    SinksContainerFactory.entryPoint.onHistoryChanged("pdftemplate|add/add/" + companyID);
                });

                menuBar.addItem(itextPdf);

                MenuPopItem phantomPdf = new MenuPopItem("Phantom Pdf");
                phantomPdf.setScheduledCommand(() -> {
                    if (schemaLookUp.getSelectedItemID() == null || companyID == null) {
                        Window.alert("Please select company");
                        return;
                    }
                    SinksContainerFactory.entryPoint.onHistoryChanged("newpdftemplate|add/add/" + companyID);
                });
                menuBar.addItem(phantomPdf);

                MenuPopItem pdftemplatewAi = new MenuPopItem("Ai Phantom Pdf");
                pdftemplatewAi.setScheduledCommand(() -> {
                    if (schemaLookUp.getSelectedItemID() == null || companyID == null) {
                        Window.alert("Please select company");
                        return;
                    }
                    SinksContainerFactory.entryPoint.onHistoryChanged("pdftemplatewAi|add/add/" + companyID);
                });

                menuBar.addItem(pdftemplatewAi);
                addPDFTemplate.setMenu(menuBar);

                topPanel.add(schemaLookUp);
                topPanel.add(addPDFTemplate);
                return topPanel;
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

    private ColumnDefinitionConfig[] drawColumns() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[10];
        int index = 0;
        columns[index] = new ColumnDefinitionConfig<PDFTemplatesListItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final PDFTemplatesListItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem edit = new MenuPopItem("Edit");
                edit.setScheduledCommand(() -> {
                    if (PdfGenerateTypeEnum.PHANTOM_JS.equals(item.getGenerateType())) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("newpdftemplate|edit/" + item.getCompanyID() + "/" + item.getObjectID());
                    } else {
                        SinksContainerFactory.entryPoint.onHistoryChanged("pdftemplate|edit/" + item.getCompanyID() + "/" + item.getObjectID());
                    }
                });
                actionItemCount++;
                menuBar.addItem(edit);

                MenuPopItem delete = new MenuPopItem(wfmStrings.delete());
                delete.setScheduledCommand(() -> {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.warning());
                    messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                    messageBox.addCloseHandler(new CloseHandler() {
                        public void onSubmit() {
                            BackendService.App.get().deletePDFTemplate(item.getCompanyID(), item.getObjectID(), new AbstractAsyncCallback<Boolean>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }
                                @Override
                                public void onSuccess(Boolean result) {
                                    if (result) {
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), item.getTemplateName()), Info.Type.INFO);
                                        list.reloadPage();
                                    } else {
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }
                                }
                            });
                        }
                    });
                    messageBox.open();
                });
                actionItemCount++;
                menuBar.addItem(delete);


                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[index].setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[index].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[index++].setColumnSortable(false);

        columns[index] = new ColumnDefinitionConfig<PDFTemplatesListItem, String>(wfmStrings.company() + "ID", "companyID", 65) {

            @Override
            public String getCellValue(PDFTemplatesListItem item) {
                return item.getCompanyID() != null ? item.getCompanyID().toString() : "";
            }
        };
        columns[index].setColumnSortable(false);
        columns[index++].setMinimumColumnWidth(50);

        columns[index] = new ColumnDefinitionConfig<PDFTemplatesListItem, String>(wfmStrings.companyName(), "companyName", 150) {

            @Override
            public String getCellValue(PDFTemplatesListItem item) {
                return item.getCompanyName() != null ? item.getCompanyName() : "";
            }
        };
        columns[index].setColumnSortable(false);
        columns[index++].setMinimumColumnWidth(80);

        columns[index] = new ColumnDefinitionConfig<PDFTemplatesListItem, SimpleLink>(wfmStrings.name(), "name", 150) {

            @Override
            public SimpleLink getCellValue(PDFTemplatesListItem item) {
                if (PdfGenerateTypeEnum.PHANTOM_JS.equals(item.getGenerateType())) {
                    return getLinkWithTabTitle(item.getTemplateName(), "newpdftemplate|edit/" + item.getCompanyID() + "/" + item.getObjectID(),"" + item.getCompanyID());
                } else {
                    return getLinkWithTabTitle(item.getTemplateName(), "pdftemplate|edit/" + item.getCompanyID() + "/" + item.getObjectID(), "" + item.getCompanyID());
                }
            }
        };
        columns[index].setColumnSortable(false);
        columns[index++].setMinimumColumnWidth(80);

        columns[index] = new ColumnDefinitionConfig<PDFTemplatesListItem, String>(wfmStrings.type(), "type", 150) {

            @Override
            public String getCellValue(PDFTemplatesListItem item) {
                return item.getType() != null ? item.getType() : "";
            }
        };
        columns[index].setColumnSortable(false);
        columns[index++].setMinimumColumnWidth(50);

        columns[index] = new ColumnDefinitionConfig<PDFTemplatesListItem, String>("Font", "font", 150) {

            @Override
            public String getCellValue(PDFTemplatesListItem item) {
                return item.getFont() != null ? item.getFont() : "";
            }
        };
        columns[index].setColumnSortable(false);
        columns[index++].setMinimumColumnWidth(50);

        columns[index] = new ColumnDefinitionConfig<PDFTemplatesListItem, String>("Short Number Format", "shortFormat", 150) {

            @Override
            public String getCellValue(PDFTemplatesListItem item) {
                return item.getShortNumberFormat() != null ? item.getShortNumberFormat() : "";
            }
        };
        columns[index].setColumnSortable(false);
        columns[index++].setMinimumColumnWidth(50);

        columns[index] = new ColumnDefinitionConfig<PDFTemplatesListItem, String>("Extended Number Format", "extendedFormat", 150) {

            @Override
            public String getCellValue(PDFTemplatesListItem item) {
                return item.getExtendedNumberFormat() != null ? item.getExtendedNumberFormat() : "";
            }
        };
        columns[index].setColumnSortable(false);
        columns[index++].setMinimumColumnWidth(50);

        columns[index] = new ColumnDefinitionConfig<PDFTemplatesListItem, String>("Default", "default", 150) {

            @Override
            public String getCellValue(PDFTemplatesListItem item) {
                return item.isDefaultTemplate() ? "Yes" : "No";
            }
        };
        columns[index].setColumnSortable(false);
        columns[index++].setMinimumColumnWidth(60);

        columns[index] = new ColumnDefinitionConfig<PDFTemplatesListItem, String>("Generate Type", "generateType", 150) {

            @Override
            public String getCellValue(PDFTemplatesListItem item) {
                return getGenerateType(item.getGenerateType());
            }
        };
        columns[index].setColumnSortable(false);
        columns[index].setMinimumColumnWidth(60);

        return columns;
    }

    private String getGenerateType(PdfGenerateTypeEnum typeEnum) {
        if (PdfGenerateTypeEnum.PHANTOM_JS.equals(typeEnum)) {
            return "Phantom Generator";
        } else {
            return "Kpi Generator";
        }
    }

    public String getIconStyle() {
        return "backend cusPdfTemListView";
    }

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
