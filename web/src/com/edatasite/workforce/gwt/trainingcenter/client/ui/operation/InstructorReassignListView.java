package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.TCHtmlTemplates;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.scheduledcourse.ScheduledCourseListView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 7/30/12
 * Time: 4:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class InstructorReassignListView extends ScheduledCourseListView implements TCConstants {

    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final TCStrings tcStrings = TCStrings.App.get();

    private SelectItem[] instructors = null;
    private DataListBox dwInstructor;

    public InstructorReassignListView() {
        super("instructorReassignList", null);
        setDescription(property.getPlural(tcStrings.instructorReassign()));
    }


    protected ListingRequestProvider<ScheduledCourseItem> getProvider() {
        return (filterParametrs, callback) -> {

            if (instructors == null || instructors != null && instructors.length == 0) {
                TCService.App.get().getInstructorList(new AsyncCallback<SelectItem[]>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        GWT.log(caught.getMessage());
                    }

                    @Override
                    public void onSuccess(SelectItem[] result) {
                        instructors = result;
                        dwInstructor.clear();
                        dwInstructor.setItems(instructors);
                    }
                });
            }

            TCService.App.get().getInstructorReassignCourseList(filterParametrs, new AsyncCallback<ListResult<ScheduledCourseItem>>() {
                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(ListResult<ScheduledCourseItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    protected ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public HorizontalPanel initTopToolBarWidgets() {
                HorizontalPanel topPanel = new HorizontalPanel();
                topPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
                topPanel.setSpacing(5);

                dwInstructor = new DataListBox();
                dwInstructor.setWidth("200px");
                if (instructors != null) {
                    dwInstructor.clear();
                    dwInstructor.setItems(instructors);
                }
                dwInstructor.addValueChangeHandler(event -> {
                    listingPanel.getFilterParametrs().setEmployeeId(dwInstructor.getSelectedId());
                    listingPanel.reloadPage();
                });

                topPanel.add(new HTML(TCHtmlTemplates.getInstance().title("Instructor")));
                topPanel.add(dwInstructor);
                return topPanel;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmMessages.currentlyDonotHaveAny(tcStrings.courseSchedules().toLowerCase()));
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    protected Anchor getActions(final ScheduledCourseItem rowValue) {
        MenuBar menuBar = new MenuBar(true);
        int menuItemCount = 0;

        //Instructor Reassign
        final MenuPopItem reassign = new MenuPopItem(tcStrings.instructorReassign(), "icon-contact-small");
        reassign.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(TC_SCHEDULED_COURSE + "|instructorReassign/" + rowValue.getObjectID()));
        menuItemCount++;
        menuBar.addItem(reassign);

        ToolItem toolItem = new ToolItem(menuItemCount);
        toolItem.setWidget(menuBar);
        return toolItem.getAction();
    }

    protected SimpleLink getSimpleLink(ScheduledCourseItem rowValue) {
        return new SimpleLink(rowValue.getCourseName(), (TC_SCHEDULED_COURSE + "|instructorReassign/" + rowValue.getObjectID()));
    }

    @Override
    public String getIconStyle() {
        return "bgMark reassign-icon";
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
    @Override
    public String getPropertyCode() {
        return "instructorReassignList";
    }
}
