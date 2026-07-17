package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.SignatureItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 09.02.13
 * Time: 12:46
 * To change this template use File | Settings | File Templates.
 */
public class SignatureListView extends BaseListView implements Constants {
    private ListingPanel listPanel;
    private int totalCount = -1;

    public SignatureListView() {
        super("signatureList", wfmStrings.email() + " " + wfmStrings.signature());
    }

    @Override
    protected Widget onInitialize() {
        listPanel = new ListingPanel(ListPanelType.SignatureListPanel, getColumnConfig(), getListProvider(), getListDesign());


        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SIGNATURE_ADD, SignatureListView.this, (sender, args) -> listPanel.reloadPage());
        add(listPanel);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        ColumnDefinitionConfig[] columnConfig = new ColumnDefinitionConfig[2];

        columnConfig[0] = new ColumnDefinitionConfig<Object, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(Object object) {
                final SignatureItem item = (SignatureItem) object;

                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem signatureSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                signatureSummary.ensureDebugId("summary");
                signatureSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("signature|summary/" + item.getObjectID() + "/" + item.getSignature(), item.getUserName()));
                actionItemCount++;
                menuBar.addItem(signatureSummary);

                MenuPopItem signatureEdit = new MenuPopItem(wfmStrings.edit(), "icon-issue-edit-small");
                signatureEdit.ensureDebugId("edit");
                signatureEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("signatureedit|addsignature/" + item.getObjectID(), item.getUserName()));
                actionItemCount++;
                menuBar.addItem(signatureEdit);

                MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                removeItem.ensureDebugId("delete");
                removeItem.setCommand(() -> {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    message.setTitle(wfmStrings.confirmationMessage());
                    message.setMessage(wfmMessages.sureYouWantToDelete(wfmStrings.signature().toLowerCase() + " ?", ""));
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            ProfileService.App.get().deleteSignature(item.getObjectID(), new AsyncCallback<Void>() {
                                public void onFailure(Throwable caught) {
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                public void onSuccess(Void result) {
                                    Info.show(wfmStrings.signatureDeleted(), Info.Type.INFO);
                                    listPanel.reloadPage();
                                }
                            });
                        }

                    });
                    message.open();

                });
                menuBar.addItem(removeItem);

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig[0].setColumnSortable(false);
        columnConfig[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);

        columnConfig[1] = new ColumnDefinitionConfig<Object, SimpleLink>(wfmStrings.username(), "signatureOwner", 150) {
            @Override
            public SimpleLink getCellValue(Object object) {
                SignatureItem item = (SignatureItem) object;
                return getLink(item.getUserName(), "signature|summary/" + item.getObjectID() + "/");
            }
        };
        columnConfig[1].setMinimumColumnWidth(200);
        columnConfig[1].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        return columnConfig;
    }

    private ListingRequestProvider<SignatureItem> getListProvider() {
        return (listingFilterParameter, listingCallback) -> {
            listingFilterParameter.setShowInListing(Utils.hasPermission(PermissionConstants.SETTINGS_SIGNATURE_LIST));
            listingFilterParameter.setClientId(Utils.getUserID());
            ProfileService.App.get().getSignatureList(listingFilterParameter, new AsyncCallback<ListResult<SignatureItem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(ListResult<SignatureItem> signatureList) {
                    listingCallback.onSuccess(signatureList);
                    totalCount = signatureList.getTotal();
                }
            });
        };
    }

    private ListingPanelDesign getListDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {

                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return null;
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return null;
                    }

                    @Override
                    public long initSimpleFilterType() {
                        return -1;
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = getAddNewButton();
                addNew.addClickHandler(clickEvent -> {
                    if (totalCount == 0 || Utils.hasPermission(PermissionConstants.SETTINGS_SIGNATURE_ADD)) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("signature|add/add");
                    } else {
                        Info.show(wfmMessages.alreadyHaveSignature(), Info.Type.WARNING);
                    }
                });
                return addNew;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.noSignatureText());
                message.setHref("signature|add/add");
                message.setTextBeforeLink(wfmStrings.noSignatureLink());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    public String getIconStyle() {
        return "icon-signature";
    }

    public ImageResource getIconImage() {
        return null;
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
