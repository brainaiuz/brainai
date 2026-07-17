package com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.fakeContainer.PseudoContainerServiceAsync;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.EmployeeProfileWidget;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.UiSettings;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.ToolTipOptions;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.upload.ImageUploadDialog;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.CssName;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.*;
import gwt.material.design.client.ui.html.*;
import gwt.material.design.jquery.client.api.Event;
import gwt.material.design.jquery.client.api.Functions;
import gwt.material.design.jquery.client.api.JQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static gwt.material.design.jquery.client.api.JQuery.$;

public class ModulesBar extends Composite implements Constants {
    static final String ICON_ACCOUNTING_MODULE = "accounting";
    static final String ICON_CRM_MODULE = "sales";
    static final String ICON_HRMS_MODULE = "humans";
    static final String ICON_PM_MODULE = "projects";
    static final String ICON_REPORTS_MODULE = "reporting";
    static final String ICON_PAYROLL_MODULE = "payroll";
    static final String ICON_DOCUMENTS_MODULE = "documents";
    static final String COLOR_ACCOUNTING_MODULE = "main-modules__item--accounting";
    static final String COLOR_CRM_MODULE = "main-modules__item--sales";
    static final String COLOR_HRMS_MODULE = "main-modules__item--humans";
    static final String COLOR_PM_MODULE = "main-modules__item--projects";
    static final String COLOR_REPORTS_MODULE = "main-modules__item--reports";
    static final String COLOR_WORKSPACE_MODULE = "main-modules__item--workspace";
    static final String COLOR_PAYROLL_MODULE = "main-modules__item--payroll";
    static final String COLOR_DOCUMENTS_MODULE = "main-modules__item--docs";

    static final String ITEM_CLOSED = "item-closed";
    static final String ITEM_OPEN = "item-open";



    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final ModulesBarUiBinder ourUiBinder = GWT.create(ModulesBarUiBinder.class);
    @UiField
    MaterialPanel accountContainer;
    @UiField
    MaterialLink account;
    @UiField
    MaterialPanel currentModuleContainer;
    @UiField
    Div modulesOverlay;
    @UiField
    UnorderedList moduleList;
    @UiField
    Div footer;

    private SelectableListItem selected;
    private Command sideNavResizeCommand;
    private final boolean isWorkspace;

    public ModulesBar() {
        MaterialPanel main = ourUiBinder.createAndBindUi(this);
        initWidget(main);

        isWorkspace = MODULE_WORKSPACE.equals(GWT.getModuleName()) || MODULE_SETTINGS.equals(GWT.getModuleName());

        main.addMouseOverHandler(event -> {
            onSideMenuMouseOver(isWorkspace);
        });
        if (isWorkspace) {
            RootPanel.get().removeStyleName("left-menu-hover");
            if (MODULE_SETTINGS.equals(GWT.getModuleName())) {
                RootPanel.getBodyElement().addClassName("left-menu-closed");
                RootPanel.getBodyElement().removeClassName("left-menu-open");
            }

        } else {
            MaterialLink menuTrigger = new MaterialLink();
            menuTrigger.addStyleName("left-menu-trigger");
            menuTrigger.add(new SvgIcon(SvgEnum.chevronRight));
            menuTrigger.addClickHandler(ch -> {
                ch.stopPropagation();
                onSideNavPositionChange();
            });
            footer.add(menuTrigger);
        }
        footer.addMouseOverHandler(e -> {
            e.stopPropagation();
            e.preventDefault();
        });

        modulesOverlay.addClickHandler(ch -> {
            $(modulesOverlay.getElement()).toggle(false);
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SIDE_NAV_POSITION_CHANGE, ModulesBar.this, (event, args) -> {
            onSideNavPositionChange();
        });
    }

    private void onSideMenuMouseOver(boolean isWorkspace) {
        if (isWorkspace) {
            if (selected != null && !selected.isCurrentModule()) {
                MainLayout.get().setSideMenuHovered(true);
            }
        } else {
            MainLayout.get().setSideMenuHovered(true);
        }
    }

    private final HashMap<ListItem, ListItem> activeItemMap = new HashMap<>();

    private void setSelected(SelectableListItem li) {
        if (selected != null) {
            selected.removeStyleName("main-modules__item--selected");
        }
        selected = li;
        if (selected != null) {
            selected.addStyleName("main-modules__item--selected");
        }
    }

    private void activate(String title, String url, SelectableListItem li, boolean active, boolean currentModule) {
        if (active) {
            onPseudoAppClicked(title, url);
        } else {
            if (currentModule) {
                MainLayout.get().resetSideNavContent();
            }
        }
        setSelected(li);
    }

    public SelectableListItem generateModuleLink(String title, String url, String iconClass, boolean active, String moduleColor, boolean currentModule) {
        SelectableListItem li = new SelectableListItem("main-modules__item " + moduleColor);
        li.setCurrentModule(currentModule);
        MaterialLink link = new MaterialLink();
        JQuery.$(li).on("click", new Functions.EventFunc() {
            @Override
            public Object call(Event e) {
                activate(title, url, li, active, currentModule);
                if (isWorkspace) {
                    MainLayout.get().setSideMenuHovered(true);
                }
                return null;
            }
        });
        if (!active && currentModule) {
            li.addStyleName("main-modules__item--active");
        }
        SvgIcon icon = new SvgIcon(iconClass);

        link.add(icon);

        link.setText(title);
        li.add(link);
        return li;
    }

    private void onSideNavPositionChange() {
        String position;
        if (RootPanel.getBodyElement().getClassName().contains("left-menu-open")) {
            RootPanel.getBodyElement().addClassName("left-menu-closed");
            RootPanel.getBodyElement().removeClassName("left-menu-open");
            MainLayout.get().setSideMenuHovered(false);
            position = "left-menu-closed";
        } else {
            RootPanel.getBodyElement().removeClassName("left-menu-closed");
            RootPanel.getBodyElement().addClassName("left-menu-open");
            position = "left-menu-open";
        }
        if (sideNavResizeCommand != null) {
            sideNavResizeCommand.execute();
        }
        CommonService.App.get().setSideNavBarPosition(position, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {
                ///There isn't anythintg to do
            }

            @Override
            public void onSuccess(String s) {
                //There isn't anything to do
            }
        });
    }

    public void initializeModules() {
        initModuleIcons();
        initAccountSettings();
    }

    private ListItem createWorksapceLink() {
        ModuleLink workspaceLink = new ModuleLink(COLOR_WORKSPACE_MODULE, SvgEnum.home, "");
        workspaceLink.setActive(MODULE_WORKSPACE.equals(GWT.getModuleName()));
        workspaceLink.setLinkHref(MYWORKSPACE_URL);
        return workspaceLink;
    }

    private void visualizeModulesList(List<ModuleItem> list) {
        if (Utils.hasRole(ADMIN)) {
            list.forEach(o -> {
                if (!o.enabled) {
                    decorateWithToolTip(o);
                    o.getWidget().addStyleName("main-modules__item--disabled");
                }
            });
            List<ModuleItem> disabledItems = list.stream().filter(o -> !o.enabled).collect(Collectors.toList());
            list.removeAll(disabledItems);
            list.addAll(disabledItems);
            appendToModule(list);
        } else {
            list = list.stream()
                    .filter(o -> o.enabled)
                    .collect(Collectors.toList());
            appendToModule(list);
        }

    }

    private void appendToModule(List<ModuleItem> list) {
        for (int i = 0; i < list.size(); i++) {
            moduleList.add(list.get(i).getWidget());
        }
    }

    private void decorateWithToolTip(ModuleItem o) {
        ToolTipOptions options = new ToolTipOptions();
        options.setSide(Position.RIGHT);
        new KpiToolTip(o.getWidget(), "<a href=\"/Myaccount.html\">" + wfmStrings.upgrade() + "</a>", options);
    }

    private void onPseudoAppClicked(String title, String url) {
        UiSettings urls = UiSettings.getInstance();
        PseudoContainerRPC pseudoContainer = Utils.getPseudoContainer();
        List<PseudoMenuItem> menus = null;
        if (urls.ACCOUNTING.equals(url)) {
            List<PseudoMenuItem> accountingPseudoMenuItems = pseudoContainer.getAccountingPseudoMenuItems();
            menus = accountingPseudoMenuItems;
            if (accountingPseudoMenuItems.size() == 0) {
                PseudoContainerServiceAsync.App.get().getAccountingMenuItems(MODULE_ACCOUNTING, getPseudoMenuItemCallback(title, url, accountingPseudoMenuItems));
                return;
            }
        } else if (urls.CRM.equals(url)) {
            List<PseudoMenuItem> crmPseudoMenuItems = pseudoContainer.getCrmPseudoMenuItems();
            menus = crmPseudoMenuItems;
            if (crmPseudoMenuItems.size() == 0) {
                PseudoContainerServiceAsync.App.get().getCrmMenuItems(MODULE_CRM, getPseudoMenuItemCallback(title, url, crmPseudoMenuItems));
                return;
            }
        } else if (urls.HRMS.equals(url)) {
            List<PseudoMenuItem> hrmsPseudoMenuItems = pseudoContainer.getHrmsPseudoMenuItems();
            menus = hrmsPseudoMenuItems;
            if (hrmsPseudoMenuItems.size() == 0) {
                PseudoContainerServiceAsync.App.get().getHRMSMenuItems(MODULE_HRMS, getPseudoMenuItemCallback(title, url, hrmsPseudoMenuItems));
                return;
            }
        } else if (urls.PM.equals(url)) {
            List<PseudoMenuItem> pmPseudoMenuItems = pseudoContainer.getPmPseudoMenuItems();
            menus = pmPseudoMenuItems;
            if (pmPseudoMenuItems.size() == 0) {
                PseudoContainerServiceAsync.App.get().getPMMenuItems(MODULE_PM, getPseudoMenuItemCallback(title, url, pmPseudoMenuItems));
                return;
            }
        } else if (urls.PAYROLL.equals(url)) {
            List<PseudoMenuItem> payrollMenus = pseudoContainer.getPayrollPseudoMenuItems();
            menus = payrollMenus;
            if (payrollMenus.size() == 0) {
                PseudoContainerServiceAsync.App.get().getPayrollMenuItems(MODULE_PAYROLL, getPseudoMenuItemCallback(title, url, payrollMenus));
                return;
            }
        } else if (urls.REPORTING.equals(url)) {
            List<PseudoMenuItem> reportingMenus = pseudoContainer.getReportingPseudoMenuItems();
            menus = reportingMenus;
            if (reportingMenus.size() == 0) {
                PseudoContainerServiceAsync.App.get().getReportingMenuItems(getPseudoMenuItemCallback(title, url, reportingMenus));
                return;
            }
        } else if (urls.DOCUMENTS.equals(url)) {
            List<PseudoMenuItem> docsPseudoMenuItems = pseudoContainer.getDocsPseudoMenuItems();
            menus = docsPseudoMenuItems;
            if (docsPseudoMenuItems.size() == 0) {
                PseudoContainerServiceAsync.App.get().getDocsMenuItems(getPseudoMenuItemCallback(title, url, docsPseudoMenuItems));
                return;
            }
        }
        else if (urls.TC.equals(url)) {
            List<PseudoMenuItem> tcPseudoMenuItems = pseudoContainer.getTcPseudoMenuItems();
            menus = tcPseudoMenuItems;
            if (tcPseudoMenuItems.size() == 0) {
                PseudoContainerServiceAsync.App.get().getTrainingCentesMenuItems(getPseudoMenuItemCallback(title, url, tcPseudoMenuItems));
                return;
            }
        }
        generatePseudoSideNavMenu(url, title, menus);

    }

    private void setCurrentUserUnavailablePhoto() {
        Span initialName = new Span();
        initialName.setClass("user-profile-img__initials");
        initialName.setText(Utils.getUserInitialName());

        Span wrapper = new Span();
        wrapper.setClass("user-profile-img");
        wrapper.add(initialName);

        account.add(wrapper);
    }

    private void onEmployeeProfileBlur() {
        if (selected != null) {
            selected.fireEvent(new ClickEvent() {
            });
        } else {
            MainLayout.get().resetSideNavContent();
        }
    }

    private void initAccountSettings() {
        EmployeeProfileWidget empWidget = new EmployeeProfileWidget();
        if (accountContainer != null) {
            accountContainer.addClickHandler(e -> {
                if (!empWidget.isAttached()) {
                    MainLayout.get().replaceSideNavContent(empWidget);
                    if (isWorkspace) {
                        MainLayout.get().setSideMenuHovered(true);
                    }
                } else {
                    onEmployeeProfileBlur();
                }
            });
        }
        CommonService.App.get().getEmployeeImageURL(new AbstractAsyncCallback<SelectItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                setCurrentUserUnavailablePhoto();
            }

            @Override
            public void onSuccess(SelectItem result) {

                if (result != null && result.getName() != null && !result.getName().isEmpty()) {
                    account.getElement().setInnerHTML("<span class=\"user-profile-img\" style=\" background-image:url('" + result.getName() + "');\" > <img src=\"" + result.getName() + "\"></span>");
                } else {
                    setCurrentUserUnavailablePhoto();
                }
            }
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_USER_OWN_IMAGE_UPLOAD_ADD, ModulesBar.this, (sender, args) -> {

            if (args != null && sender instanceof ImageUploadDialog) {
                String resultUrl = (String) args;
                account.clear();
                account.getElement().setInnerHTML("<span class=\"user-profile-img\" style=\" background-image:url('" + resultUrl + "');\" > <img src=\"" + resultUrl + "\"></span>");
            }
        });
    }

    private AbstractAsyncCallback<ArrayList<PseudoMenuItem>> getPseudoMenuItemCallback(String title, String url, List<PseudoMenuItem> menus) {
        MainLayout.get().loadSideNavContent(true);
        return new AbstractAsyncCallback<ArrayList<PseudoMenuItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                MainLayout.get().loadSideNavContent(false);
            }

            @Override
            public void onSuccess(ArrayList<PseudoMenuItem> result) {
                MainLayout.get().loadSideNavContent(false);
                menus.clear();
                menus.addAll(result);
                generatePseudoSideNavMenu(url, title, menus);
            }
        };
    }

    public void generatePseudoSideNavMenu(String url, String title, List<PseudoMenuItem> menus) {
        if (Constants.SETTINGS_URL.equals(url)) {
            UnorderedList pseudoNavItems = generatePseudoSettingsMenus(menus);
            pseudoNavItems.addStyleName("main-directories collapsible collapsible-accordion");
            MainLayout.get().replaceSideNavContent(generatePseudoMenuHeader(title, url), pseudoNavItems);
        } else {
            MaterialCollapsible pseudoNavItems = generatePseudoMenus(url, menus);
            pseudoNavItems.addStyleName("main-directories collapsible collapsible-accordion");
            MainLayout.get().replaceSideNavContent(generatePseudoMenuHeader(title, url), pseudoNavItems);
        }
    }

    private Div generatePseudoMenuHeader(String title, String url) {
        Div div = new Div("frame__nav-title");
        Heading h3 = new Heading(HeadingSize.H3);
        h3.setText(title);
        div.add(h3);
        h3.addClickHandler(event -> Utils.redirect(GWT.getHostPageBaseURL() + url));
        h3.setTitle(url);
        h3.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        return div;
    }

    private MaterialCollapsible generatePseudoMenus(String url, List<PseudoMenuItem> result) {
        MaterialCollapsible pseudoNavItems = new MaterialCollapsible();
        boolean isDocs = UiSettings.getInstance().DOCUMENTS.equals(url);
        boolean hasDashboard = false;
        for (PseudoMenuItem container : result) {
            boolean childrenAvailable = container.getChildren() != null && !container.getChildren().isEmpty();
            if (childrenAvailable || isDocs) {
                MaterialCollapsibleItem item = new MaterialCollapsibleItem();
                MaterialCollapsibleBody body = new MaterialCollapsibleBody();
                if (container.isDashboard()) {
                    hasDashboard = true;
                    item.setInitialClasses("main-dir__dashboard");
                    int count = container.getChildren().size();
                    if (count == 1) {
                        item.addStyleName("noncollapsible-item");
                    }
                    body.getElement().getStyle().setDisplay(Style.Display.BLOCK);
                } else {
                    String className = "main-dir__";
                    if (container.getUrl() != null) {
                        className += container.getUrl().replace(" ", "_").toLowerCase();
                    }
                    item.setInitialClasses(className);
                }
                MaterialCollapsibleHeader header = new MaterialCollapsibleHeader();
                header.add(new MaterialLink(container.getName()));
                if (!container.isDashboard() || (container.getChildren() != null && container.getChildren().size() > 1)) {
                    item.add(header);
                }
                item.add(body);
                UnorderedList ul = new UnorderedList();
                pseudoNavItems.add(item);
                if (childrenAvailable) {
                    for (PseudoMenuItem _container : container.getChildren()) {
                        MaterialLink link = new MaterialLink(_container.getName());
                        if (_container.isPage()) {
                            link.setHref(GWT.getHostPageBaseURL() + url + "#" + _container.getUrl());
                        } else {
                            link.setHref(GWT.getHostPageBaseURL() + url + "#" + container.getUrl() + "|" + _container.getUrl());
                        }
                        ListItem li = new ListItem();
                        li.add(link);
                        ul.add(li);

                    }
                } else if (isDocs) {
                    item.addClickHandler(e -> Utils.redirect(GWT.getHostPageBaseURL() + url + "#" + container.getUrl() + "|" + container.getUrl()));
                }
                body.add(ul);
            }
        }
        if (hasDashboard) {
            pseudoNavItems.open(1);
        } else {
            pseudoNavItems.open(0);
        }
        return pseudoNavItems;
    }

    private void initModuleIcons() {
        UiSettings urls = UiSettings.getInstance();
        ArrayList<ModuleItem> list = new ArrayList<>();

        HashMap<String, String> moduleLocalizes = Utils.getModuleLocalizeMap();


//        if (Utils.hasPermission(PermissionConstants.WORKSPACE_MAIN_MENU)) {
//            list.add(new ModuleItem(true, createWorksapceLink()));
//        }
        boolean isAccountingActive = !MODULE_ACCOUNTING.equals(GWT.getModuleName()) && Utils.hasPermission(PermissionConstants.ACCOUNTING_MAIN_MENU);
        String title = wfmStrings.accounts();
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
            title = wfmStrings.inventory();
        }
        ListItem accountingModule = generateModuleLink(moduleLocalizes != null && moduleLocalizes.get(MODULE_ACCOUNTING) != null ? moduleLocalizes.get(MODULE_ACCOUNTING) : title
                , urls.ACCOUNTING
                , ICON_ACCOUNTING_MODULE
                , isAccountingActive
                , COLOR_ACCOUNTING_MODULE
                , MODULE_ACCOUNTING.equals(GWT.getModuleName())
        );
        list.add(new ModuleItem(isAccountingActive || MODULE_ACCOUNTING.equals(GWT.getModuleName()), accountingModule));

        boolean isCrmActive = !MODULE_CRM.equals(GWT.getModuleName()) && Utils.hasPermission(PermissionConstants.CRM_MAIN_MENU);
        ListItem crmModule = generateModuleLink(moduleLocalizes != null && moduleLocalizes.get(MODULE_CRM) != null ? moduleLocalizes.get(MODULE_CRM) : wfmStrings.crm()
                , urls.CRM
                , ICON_CRM_MODULE
                , isCrmActive
                , COLOR_CRM_MODULE
                , MODULE_CRM.equals(GWT.getModuleName())
        );
        list.add(new ModuleItem(isCrmActive || MODULE_CRM.equals(GWT.getModuleName()), crmModule));

        boolean isHrmsActive = !MODULE_HRMS.equals(GWT.getModuleName()) && Utils.hasPermission(PermissionConstants.HRMS_MAIN_MENU);
        ListItem hrmsModule = generateModuleLink(moduleLocalizes != null && moduleLocalizes.get(MODULE_HRMS) != null ? moduleLocalizes.get(MODULE_HRMS) : wfmStrings.hrms()
                , urls.HRMS
                , ICON_HRMS_MODULE
                , isHrmsActive
                , COLOR_HRMS_MODULE
                , MODULE_HRMS.equals(GWT.getModuleName())
        );
        list.add(new ModuleItem(isHrmsActive || MODULE_HRMS.equals(GWT.getModuleName()), hrmsModule));

        boolean isPMActive = !MODULE_PM.equals(GWT.getModuleName()) && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU);
        ListItem pmModule = generateModuleLink(moduleLocalizes != null && moduleLocalizes.get(MODULE_PM) != null ? moduleLocalizes.get(MODULE_PM) : wfmStrings.projects()
                , urls.PM, ICON_PM_MODULE
                , isPMActive
                , COLOR_PM_MODULE
                , MODULE_PM.equals(GWT.getModuleName())
        );
        list.add(new ModuleItem(isPMActive || MODULE_PM.equals(GWT.getModuleName()), pmModule));

        boolean isPayrollActive = !MODULE_PAYROLL.equals(GWT.getModuleName()) && Utils.hasPermission(PermissionConstants.PAYROLL_MAIN_MENU);
        ListItem payrollModule = generateModuleLink(moduleLocalizes != null && moduleLocalizes.get(MODULE_PAYROLL) != null ? moduleLocalizes.get(MODULE_PAYROLL) : wfmStrings.payroll()
                , urls.PAYROLL
                , ICON_PAYROLL_MODULE
                , isPayrollActive
                , COLOR_PAYROLL_MODULE
                , MODULE_PAYROLL.equals(GWT.getModuleName())
        );
        list.add(new ModuleItem(isPayrollActive || MODULE_PAYROLL.equals(GWT.getModuleName()), payrollModule));

        boolean isReportingActive = !MODULE_REPORTING.equals(GWT.getModuleName()) && Utils.hasPermission(PermissionConstants.REPORTING_MAIN_MENU);
        ListItem reportingModule = generateModuleLink(moduleLocalizes != null && moduleLocalizes.get(MODULE_REPORTING) != null ? moduleLocalizes.get(MODULE_REPORTING) : wfmStrings.reports()
                , urls.REPORTING
                , ICON_REPORTS_MODULE
                , isReportingActive
                , COLOR_REPORTS_MODULE
                , MODULE_REPORTING.equals(GWT.getModuleName())
        );
        list.add(new ModuleItem(isReportingActive || MODULE_REPORTING.equals(GWT.getModuleName()), reportingModule));

        boolean isDocsActive = !MODULE_DOCUMENTS.equals(GWT.getModuleName()) && Utils.hasPermission(PermissionConstants.DOCUMENTS_MAIN_MENU);
        ListItem docsModule = generateModuleLink(moduleLocalizes != null && moduleLocalizes.get(MODULE_DOCUMENTS) != null ? moduleLocalizes.get(MODULE_DOCUMENTS) : wfmStrings.docs()
                , urls.DOCUMENTS, ICON_DOCUMENTS_MODULE
                , isDocsActive, COLOR_DOCUMENTS_MODULE
                , MODULE_DOCUMENTS.equals(GWT.getModuleName())
        );
        list.add(new ModuleItem(isDocsActive || MODULE_DOCUMENTS.equals(GWT.getModuleName()), docsModule));

        boolean isTCActive = !MODULE_TC.equals(GWT.getModuleName()) && Utils.hasPermission(PermissionConstants.TC_MAIN_MENU);
        ListItem tcModule = generateModuleLink(moduleLocalizes != null && moduleLocalizes.get(MODULE_TC) != null ? moduleLocalizes.get(MODULE_TC) : wfmStrings.trainingCenter()
                , urls.TC, ICON_PM_MODULE
                , isTCActive
                , COLOR_PM_MODULE
                , MODULE_TC.equals(GWT.getModuleName())
        );
        if (isTCActive) {
            list.add(new ModuleItem(isTCActive || MODULE_TC.equals(GWT.getModuleName()), tcModule));
        }

        visualizeModulesList(list);
    }

    public UnorderedList generatePseudoSettingsMenus(List<PseudoMenuItem> result) {
        UnorderedList pseudoNavItems = new UnorderedList();
        pseudoNavItems.setStylePrimaryName("collapsible");
        for (PseudoMenuItem container : result) {
            boolean childrenAvailable = container.getChildren() != null && container.getChildren().size() > 0;
            boolean noChildrenAvailable = container.getChildren() == null || container.getChildren().size() == 0;
            if (childrenAvailable) {
                ListItem item = new ListItem();
                Div body = new Div();
                body.setStylePrimaryName("collapsible-body");
                String className = "main-dir__";
                if (container.getUrl() != null) {
                    className += container.getUrl().replace(" ", "_").toLowerCase();
                }
                item.setInitialClasses(className);

                Div header = new Div();
                header.setStylePrimaryName("collapsible-header");
                MaterialLink linkContainer = new MaterialLink(container.getName());
                linkContainer.addClickHandler(clickEvent -> {
                    if (body.getStyleName().contains(ITEM_OPEN)) {
                        body.removeStyleName(ITEM_OPEN);
                        body.addStyleName(ITEM_CLOSED);
                        item.removeStyleName(ITEM_OPEN);
                        item.addStyleName(ITEM_CLOSED);
                    } else {
                        body.removeStyleName(ITEM_CLOSED);
                        body.addStyleName(ITEM_OPEN);
                        item.removeStyleName(ITEM_CLOSED);
                        item.addStyleName(ITEM_OPEN);
                    }
                });
                header.add(linkContainer);
                item.add(header);
                item.add(body);
                UnorderedList ul = new UnorderedList();
                pseudoNavItems.add(item);

                for (PseudoMenuItem subContainer : container.getChildren()) {
                    ListItem li = new ListItem();
                    boolean subChildrenAvailable = subContainer.getChildren() != null && subContainer.getChildren().size() > 0;
                    if (subChildrenAvailable) {
                        Div subHeader = new Div();
                        subHeader.setStylePrimaryName("collapsible-header");

                        Div subBody = new Div();
                        subBody.setStylePrimaryName("collapsible-body");

                        MaterialLink linkSubContainer = new MaterialLink(subContainer.getName());
                        linkSubContainer.addClickHandler(clickEvent -> {
                            linkSubContainer.addStyleName(CssName.ACTIVE);
                            if (subBody.getStyleName().contains(ITEM_OPEN)) {
                                subBody.removeStyleName(ITEM_OPEN);
                                subBody.addStyleName(ITEM_CLOSED);
                                li.removeStyleName(ITEM_OPEN);
                                li.addStyleName(ITEM_CLOSED);
                            } else {
                                subBody.removeStyleName(ITEM_CLOSED);
                                subBody.addStyleName(ITEM_OPEN);
                                li.removeStyleName(ITEM_CLOSED);
                                li.addStyleName(ITEM_OPEN);
                            }
                        });
                        subHeader.add(linkSubContainer);
                        li.add(subHeader);
                        li.setClass("collapsible-nested");

                        UnorderedList subUL = new UnorderedList();
                        for (PseudoMenuItem subChildContainer : subContainer.getChildren()) {
                            ListItem subLI = new ListItem();
                            MaterialLink link = new MaterialLink(subChildContainer.getName());
                            link.setHref(GWT.getHostPageBaseURL().concat(Constants.SETTINGS_URL).concat("#").concat(container.getUrl()).concat("|").concat(subChildContainer.getUrl()));
                            link.addClickHandler(clickEvent -> {
                                clearActiveClass(subLI);
                                if (subBody.getStyleName().contains(ITEM_OPEN)) {
                                    subBody.removeStyleName(ITEM_OPEN);
                                    subBody.addStyleName(ITEM_CLOSED);
                                } else {
                                    subBody.removeStyleName(ITEM_CLOSED);
                                    subBody.addStyleName(ITEM_OPEN);
                                }
                            });
                            subLI.add(link);
                            subUL.add(subLI);
                        }
                        subBody.add(subUL);
                        li.add(subBody);
                    } else {
                        MaterialLink link = new MaterialLink(subContainer.getName());
                        link.setHref(GWT.getHostPageBaseURL().concat(Constants.SETTINGS_URL).concat("#").concat(container.getUrl()).concat("|").concat(subContainer.getUrl()));
                        link.addClickHandler(clickEvent -> clearActiveClass(li));
                        li.add(link);
                    }
                    ul.add(li);

                }

                body.add(ul);
            } else if (noChildrenAvailable) {
                ListItem item = new ListItem();
                item.setStylePrimaryName("collapsible-header");
                String className = "main-dir__";
                if (container.getUrl() != null) {
                    className += container.getUrl().replace(" ", "_").toLowerCase();
                }
                item.setInitialClasses(className);

                MaterialLink link = new MaterialLink(container.getName());
                link.setHref(GWT.getHostPageBaseURL().concat(Constants.SETTINGS_URL).concat("#").concat(container.getUrl()));
                link.addClickHandler(clickEvent -> clearActiveClass(item));
                item.add(link);

                pseudoNavItems.add(item);

            }
        }

        return pseudoNavItems;
    }

    private void clearActiveClass(ListItem li) {
        for (ListItem activeLi : activeItemMap.keySet()) {
            activeLi.removeStyleName(CssName.ACTIVE);
        }
        activeItemMap.clear();
        li.addStyleName(CssName.ACTIVE);
        activeItemMap.put(li, li);
    }

    public void setSideNavResizeCommand(Command sideNavResizeCommand) {
        this.sideNavResizeCommand = sideNavResizeCommand;
    }

    private class ModuleItem {
        private final boolean enabled;
        private final Widget widget;

        public ModuleItem(boolean enabled, Widget widget) {
            this.enabled = enabled;
            this.widget = widget;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public Widget getWidget() {
            return widget;
        }
    }

    interface ModulesBarUiBinder extends UiBinder<MaterialPanel, ModulesBar> {
    }
}
