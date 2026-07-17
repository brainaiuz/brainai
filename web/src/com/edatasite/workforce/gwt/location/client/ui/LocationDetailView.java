package com.edatasite.workforce.gwt.location.client.ui;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.ExportPdfButton;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.location.client.rpc.EmployeeLocation;
import com.edatasite.workforce.gwt.location.client.rpc.LocationService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * User: Dilshod
 * Date: 03.12.2009
 * Time: 14:29:47
 */
public class LocationDetailView extends CustomForm2 {

    public static final ProvidesKey<EmployeeListItem> KEY_PROVIDER = (EmployeeListItem item) -> item == null ? null : item.getObjectID();
    private final Integer locationId;
    private final int limit = 200;
    private HorizontalPanel postFormPanel;
    private HTML owners;
    private KpiDataGrid<EmployeeListItem> dataGrid;
    private int lastScrollPos = 0;
    private int offset = 0;
    private int employeeCount = 0;
    private boolean isEmpty = false;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private FormHasCustomField customFieldUtil;

    public LocationDetailView(Integer locationId) {
        super("summary", wfmStrings.summaryView());
        this.locationId = locationId;
    }

    @Override
    public ListingActionMenu getActionTools() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return "locations location-list";
    }

    @Override
    protected void addButtons() {

        ExportPdfButton pdf = new ExportPdfButton(new ExportPdfButton.PdfRequestInterface() {
            @Override
            public String getUrl() {
                return "/locationListViewPDFHandler";
            }

            @Override
            public boolean isLandscapeOptionEnabled() {
                return true;
            }

            @Override
            public HashMap<String, String> getParameters() {
                RequestObject requestObject = new RequestObject(locationId);
                HashMap<String, String> params = requestObject.getRequestParams();
                return params;
            }
        });
        addRightButton(pdf);

        //edit button
        if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_LOCATION)) {
            addEditButton().addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("location|edit/" + locationId));
        }
    }

    @Override
    protected void getDataToFillFields() {
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.LOCATION_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Location, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                LocationDetailView.super.onInitialize();
                initialize();
                LoadingPanel.loading(true);
                postFormPanel = new HorizontalPanel();
                LocationService.App.get().getLocationAndEmployees(locationId, new AbstractAsyncCallback<EmployeeLocation>() {
                    @Override
                    public void failure(Throwable throwable) {
                    }

                    @Override
                    public void success(EmployeeLocation compLocation) {
                        LoadingPanel.loading(false);
                        drawLocationData(compLocation);
                        addDeleteButton(compLocation);
                    }
                });
            }
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LOCATION_EDIT, LocationDetailView.this, (sender, args) -> LocationService.App.get().getLocationAndEmployees(locationId, new AbstractAsyncCallback<EmployeeLocation>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(EmployeeLocation compLocation) {
                clear();
                drawLocationData(compLocation);
            }
        }));
//        getLocationEmployees();
        add(postFormPanel);
        return null;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    protected void registerFields() {
        owners = new HTML();
        owners.addStyleName(DEFAULT_WIDTH);
    }

    @Override
    protected void initPredefinedValues() {

    }

    private void addDeleteButton(EmployeeLocation employeeLocation) {
        if (employeeLocation != null && !employeeLocation.isLocationUsed() && (Utils.hasPermission(PermissionConstants.HRMS_REMOVE_LOCATION))) {
            addRemoveButton().addClickHandler(event -> {
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                messageBox.setTitle(wfmStrings.confirmation());
                messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                messageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        LocationService.App.get().deleteLocation(locationId, new AbstractAsyncCallback<Void>() {
                            @Override
                            public void failure(Throwable caught) {
                            }

                            @Override
                            public void success(Void result) {
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location())), Info.Type.INFO);
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LOCATION_EDIT, null, LocationDetailView.this);
                                closeTab();
                            }
                        });
                    }
                });
                messageBox.open();
            });
        }
    }

    @Override
    protected String getWikiCode() {
        return PermissionConstants.HRMS_EDIT_LOCATION;
    }


    private void drawLocationData(EmployeeLocation compLocation) {
        //
        addTitleField(LOCATION.GENERAL_DETAILS, wfmStrings.generalInformation());
        addTitleField(LOCATION.GEOLOCATION, wfmStrings.location());
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.NAME) != null) {
            addField(LOCATION.NAME, new Label(compLocation.getLocation().getName()), getTitle(formPropertyMap.get(LOCATION.NAME).isChanged() ? formPropertyMap.get(LOCATION.NAME).getTitle() : wfmStrings.name()));
        } else {
            addField(LOCATION.NAME, new Label(compLocation.getLocation().getName()), getTitle(wfmStrings.name(), false));
        }
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.CODE) != null) {
            addField(LOCATION.CODE, new Label(compLocation.getLocation().getNumberData().getNumberString()), getTitle(formPropertyMap.get(LOCATION.CODE).isChanged() ? formPropertyMap.get(LOCATION.CODE).getTitle() : wfmStrings.number()));
        } else {
            addField(LOCATION.CODE, new Label(compLocation.getLocation().getNumberData().getNumberString()), getTitle(wfmStrings.number(), false));
        }
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.COUNTRY) != null) {
            addField(LOCATION.COUNTRY, new Label(compLocation.getLocation().getCountryName()), getTitle(formPropertyMap.get(LOCATION.COUNTRY).isChanged() ? formPropertyMap.get(LOCATION.COUNTRY).getTitle() : wfmStrings.country()));
        } else {
            addField(LOCATION.COUNTRY, new Label(compLocation.getLocation().getCountryName()), getTitle(wfmStrings.country(), false));
        }
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.STATE) != null) {
            addField(LOCATION.STATE, new Label(compLocation.getLocation().getStateName()), getTitle(formPropertyMap.get(LOCATION.STATE).isChanged() ? formPropertyMap.get(LOCATION.STATE).getTitle() : wfmStrings.state()));
        } else {
            addField(LOCATION.STATE, new Label(compLocation.getLocation().getStateName()), getTitle(wfmStrings.state(), false));
        }
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.CITY_DESTRICT) != null) {
            addField(LOCATION.CITY_DESTRICT, new Label(compLocation.getLocation().getCityOrDestrictName()), getTitle(formPropertyMap.get(LOCATION.CITY_DESTRICT).isChanged() ? formPropertyMap.get(LOCATION.CITY_DESTRICT).getTitle() : wfmStrings.cityOrDistrict()));
        } else {
            addField(LOCATION.CITY_DESTRICT, new Label(compLocation.getLocation().getCityOrDestrictName()), getTitle(wfmStrings.cityOrDistrict(), false));
        }
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.CITY) != null) {
            addField(LOCATION.CITY, new Label(compLocation.getLocation().getCityName()), getTitle(formPropertyMap.get(LOCATION.CITY).isChanged() ? formPropertyMap.get(LOCATION.CITY).getTitle() : wfmStrings.city()));
        } else {
            addField(LOCATION.CITY, new Label(compLocation.getLocation().getCityName()), getTitle(wfmStrings.city(), false));
        }


        if (formPropertyMap != null && formPropertyMap.get(LOCATION.GEO_LATITUDE) != null) {
            addField(LOCATION.GEO_LATITUDE, new Label(compLocation.getLocation().getLatitudeValue()), getTitle(formPropertyMap.get(LOCATION.GEO_LATITUDE).isChanged() ? formPropertyMap.get(LOCATION.GEO_LATITUDE).getTitle() : wfmStrings.latitude()));
        } else {
            addField(LOCATION.GEO_LATITUDE, new Label(compLocation.getLocation().getLatitudeValue()), getTitle(wfmStrings.latitude(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(LOCATION.GEO_LONGITUDE) != null) {
            addField(LOCATION.GEO_LATITUDE, new Label(compLocation.getLocation().getLongitudeValue()), getTitle(formPropertyMap.get(LOCATION.GEO_LONGITUDE).isChanged() ? formPropertyMap.get(LOCATION.GEO_LONGITUDE).getTitle() : wfmStrings.longitude()));
        } else {
            addField(LOCATION.GEO_LONGITUDE, new Label(compLocation.getLocation().getLongitudeValue()), getTitle(wfmStrings.longitude(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(LOCATION.GEO_RADIUS) != null) {
            addField(LOCATION.GEO_RADIUS, new Label(compLocation.getLocation().getRadiusValue()), getTitle(formPropertyMap.get(LOCATION.GEO_RADIUS).isChanged() ? formPropertyMap.get(LOCATION.GEO_RADIUS).getTitle() : wfmStrings.radius()));
        } else {
            addField(LOCATION.GEO_RADIUS, new Label(compLocation.getLocation().getRadiusValue()), getTitle(wfmStrings.radius(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(LOCATION.EMAIL) != null) {
            addField(LOCATION.EMAIL, new Label(compLocation.getLocation().getEmail()), getTitle(formPropertyMap.get(LOCATION.EMAIL).isChanged() ? formPropertyMap.get(LOCATION.EMAIL).getTitle() : wfmStrings.email()));
        } else {
            addField(LOCATION.EMAIL, new Label(compLocation.getLocation().getEmail()), getTitle(wfmStrings.email(), false));
        }
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.PHONE) != null) {
            addField(LOCATION.PHONE, new Label(compLocation.getLocation().getPhoneNumber()), getTitle(formPropertyMap.get(LOCATION.PHONE).isChanged() ? formPropertyMap.get(LOCATION.PHONE).getTitle() : wfmStrings.phone()));
        } else {
            addField(LOCATION.PHONE, new Label(compLocation.getLocation().getPhoneNumber()), getTitle(wfmStrings.phone(), false));
        }
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.FAX) != null) {
            addField(LOCATION.FAX, new Label(compLocation.getLocation().getFax()), getTitle(formPropertyMap.get(LOCATION.FAX).isChanged() ? formPropertyMap.get(LOCATION.FAX).getTitle() : wfmStrings.fax()));
        } else {
            addField(LOCATION.FAX, new Label(compLocation.getLocation().getFax()), getTitle(wfmStrings.fax(), false));
        }
        if (formPropertyMap != null && formPropertyMap.get(ZIP_CODE) != null) {
            addField(ZIP_CODE, new Label(compLocation.getLocation().getZipCode()), getTitle(formPropertyMap.get(ZIP_CODE).isChanged() ? formPropertyMap.get(ZIP_CODE).getTitle() : wfmStrings.postCode()));
        } else {
            addField(ZIP_CODE, new Label(compLocation.getLocation().getZipCode()), getTitle(wfmStrings.postCode()));
        }

        if (formPropertyMap != null && formPropertyMap.get(PARENT) != null) {
            addField(PARENT, new Label(compLocation.getLocation().getParent() != null ? compLocation.getLocation().getParent().getName() : ""), getTitle(formPropertyMap.get(PARENT).isChanged() ? formPropertyMap.get(PARENT).getTitle() : wfmStrings.parent()));
        } else {
            addField(PARENT, new Label(compLocation.getLocation().getParent() != null ? compLocation.getLocation().getParent().getName() : ""), getTitle(wfmStrings.parent()));
        }

        addField(LOCATION.OWNERS, owners, wfmStrings.owners());
        List<SelectItem> ownersList = compLocation.getLocation().getOwners();
        if (ownersList != null) {
            String ownersHTML = ownersList.stream()
                    .map(SelectItem::getName)
                    .collect(Collectors.joining(", "));
            owners.setHTML(ownersHTML);
        }

        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, locationId, true);
        getCustomFieldUtil().fillCustomFieldsWithData(compLocation.getLocation().getCustomFieldItems(), true);
        show();
    }

    private void initialize() {
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.setSize("100%", "200px");
        dataGrid.addStyleName("cellBasedWidget-mod cellBasedWidget-mod--static-body");
        dataGrid.getElement().getStyle().setOverflow(Style.Overflow.AUTO);
        dataGrid.getScrollPanel().addScrollHandler(scroll -> {
            int oldScrollPos = lastScrollPos;
            lastScrollPos = dataGrid.getScrollPanel().getVerticalScrollPosition();
            if (oldScrollPos >= lastScrollPos) {
                return;
            }
            int maxScrollTop = dataGrid.getScrollPanel().getWidget().getOffsetHeight() - dataGrid.getScrollPanel().getOffsetHeight();
            if (lastScrollPos >= maxScrollTop) {
            }
        });
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}