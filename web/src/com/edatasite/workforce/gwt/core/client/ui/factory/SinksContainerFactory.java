package com.edatasite.workforce.gwt.core.client.ui.factory;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.history.CustomizeFormHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.history.CustomizeGridFormHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.history.IntroductionPageHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.rpc.AnchorParam;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.Workarea;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.GeneralEntryPoint;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.SideNav;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.shortcut.ShortcutItem;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;

import java.util.HashMap;
import java.util.Map;

public abstract class SinksContainerFactory implements Constants {

    public static String tabStyle = "wft-tab-feature";
    public static GeneralEntryPoint entryPoint;

    protected Workarea workarea = new Workarea();


    private HashMap<String, SinksContainer> containerMap = new HashMap<>();
    private HashMap<String, HistoryProcessor> historyProcessors = new HashMap<>();
    private String defaultContainer;

    public SinksContainerFactory(GeneralEntryPoint entryPoint) {
        this.entryPoint = entryPoint;
        registerProcessors();
        registerHistoryProcessor("introPage", new IntroductionPageHistoryProcessor());
        registerHistoryProcessor("customizeForm", new CustomizeFormHistoryProcessor());
        registerHistoryProcessor("customizeForm2", new CustomizeGridFormHistoryProcessor());
    }

    public abstract void initDefaultContainers();

    public abstract void registerProcessors();

    public abstract void registerMenuItems();


    public void putContainer(SinksContainer container) {
        containerMap.put(container.getName(), container);
    }

    public void removeFromContainer(String name) {
        containerMap.remove(name);
    }

    public SinksContainer getContainerByName(String name) {
        return containerMap.get(name);
    }

    public void setSinksContainer(SinksContainer container) {
        setCustomSinksContainer(container);
    }

    public void setDashboardContainer(SinksContainer container) {
        MainLayout.get().getSideNavBar().addDashboardContainer(container);
    }

    /**
     * We have to show  the tabs that  have to be  opened with close feature
     * as a new tab. Others will be shrinked according counts of the opened.
     *
     * @param container SinksContainer
     */
    private void setCustomSinksContainer(SinksContainer container) {
        putContainer(container);

        if (!container.isDynamic()) {
            MainLayout.get().getSideNavBar().addContainer(container);
        } else {
            MainLayout.get().addDynamicContainer(container);
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ADD_TAB, container, MainLayout.get().getNavToolBar());
        }

    }


    /**
     * <i>... This method set selected own container tab ...</i>
     * <br/>
     * <i>... Changed by developer {Mohirbek.M} ...</i>
     * <br/>
     * <i>... Update date {15:00 02/06/2022} ...</i>
     *
     * @param container SinksContainer
     */
    public void setSelection(SinksContainer container) {
        if (container != null && entryPoint.moduleSetting.isAddContainer(container.getName())) {
            //entryPoint.tabs.setSelection(container);
            entryPoint.getMainLayout().getSideNavBar().setSelection(container);
        }
    }
    public void openSecondContainer() {
        entryPoint.getMainLayout().getSideNavBar().openSecondContainer();
    }

    /**
     * <i>... This method regestration history processor to container ...</i>
     * <br/>
     * <i>... Changed by developer {Dilshod.T} ...</i>
     * <br/>
     * <i>... Update date {19:57 11/05/2011} ...</i>
     *
     * @param name      String
     * @param processor HistoryProcessor
     */
    public void registerHistoryProcessor(String name, HistoryProcessor processor) {
        historyProcessors.put(name, processor);
    }

    public Map getHistoryProcessors() {
        return historyProcessors;
    }

    public SinksContainer getSinksContainer(String historyToken, boolean... onLogin) {
        if ("googlecalendar".equals(historyToken)) {
            historyToken = "calendar";
        }
        String containerName = Utils.getSinksContainer(historyToken);
        String viewName = Utils.getSinkName(historyToken);
        SinksContainer container;

        if (historyToken != null && historyToken.contains("/")) {
            historyToken = historyToken.replace("%7C", "|");

            AnchorParam param = Utils.parseAnchorParam(historyToken);
            HistoryProcessor hp = (HistoryProcessor) getHistoryProcessors().get(containerName);

            if (onLogin != null && onLogin.length > 0 && onLogin[0]) {
                entryPoint.getMainLayout().getSideNavBar().clearSelection();
            }
            if (param.getTokens()[0].equals("add")) {
//                container = getContainerByName(containerName + param.getTokens()[0]);
//                container = getContainerByName(containerName + (viewName != null ? viewName.trim() : "") + param.getTokens()[0].trim());

                /**
                 * There is a exception for onboarding steps
                 */
                if ("employeeStep".equalsIgnoreCase(containerName)) {
                    container = getContainerByName(containerName + (viewName != null ? viewName.trim() : "") + param.getTokens()[1].trim());
                } else {
                    container = getContainerByName(containerName + param.getTokens()[0]);
                }

                if (hp == null) {
                    hp = (HistoryProcessor) getHistoryProcessors().get(containerName.substring(0, containerName.length() - 3));
                }
                if (container == null) {
                    // history processor dynamic build container names add to module setting {write by developer Dilshod.T}
                    container = hp.processAdd(param.getTokens());
                    container.setHistoryToken(historyToken);

                    /**
                     * If it came from onboarding then change container name for mapping thing
                     */
                    if ("employeeStep".equalsIgnoreCase(containerName)) {
                        container.setName(containerName + (viewName != null ? viewName.trim() : "") + param.getTokens()[1].trim());
                    }
                    setCustomSinksContainer(container);
                    container.show(viewName);
                } else {
                    container.reInit();
                    container.show(viewName);
                    container.setHistoryToken(historyToken);
                    MainLayout.get().addDynamicContainer(container);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SELECT_TAB, container, MainLayout.get().getNavToolBar());
                }
            } else {
                //container = getContainersByName().get(containerName + (viewName != null ? viewName.trim() : "") + param.getTokens()[0].trim()); //TODO don't delete this

                if ("vattransaction".equalsIgnoreCase(containerName)) {
                    container = getContainerByName(containerName + param.getTokens()[0].trim() + param.getTokens()[1].trim());
                } else {
                    container = getContainerByName(containerName + param.getTokens()[0].trim());
                }

                if (container != null) {
                    container.reInit();
                    container.show(viewName);

                    if (container.isDynamic()) {
                        MainLayout.get().addDynamicContainer(container);
                    }
                    container.setHistoryToken(historyToken);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SELECT_TAB, container, MainLayout.get().getNavToolBar());
                } else {
                    // history processor dynamic build container names add to module setting {write by developer Dilshod.T}
                    entryPoint.moduleSetting.getHistoryProccessorName().add(containerName + param.getTokens()[0]);
                    container = hp.process(containerName, param.getTokens());
                    container.setHistoryToken(historyToken);

                    if (container.getItemsByView().get(viewName) != null) {
                        container.getItemsByView().get(viewName).setParams(param.getTokens());
                    }
                    container.setPreparedView(viewName);
                    container.show(viewName);

                    if ("vattransaction".equalsIgnoreCase(containerName)) {
                        container.setName(containerName + param.getTokens()[0].trim() + param.getTokens()[1].trim());
                    }
                    setCustomSinksContainer(container);
                }
            }
        } else if (Utils.isDocuments()) {
            final SinksContainer[] finalContainer = new SinksContainer[1];

            Timer scheduler = new Timer() {

                @Override
                public void run() {
                    finalContainer[0] = MainLayout.get().getSideNavBar().getCurrentContainer();
                    SideNav sideNavBar = MainLayout.get().getSideNavBar();
                    switch (viewName) {
                        case DOCUMENTS_FOLDER_ALL:
                            sideNavBar.selectDocumentView(finalContainer[0], new SelectItem(0, DOCUMENTS_FOLDER_ALL));
                            break;
                        case DOCUMENTS_FOLDER_SYSTEM:
                            sideNavBar.selectDocumentView(finalContainer[0], new SelectItem(0, DOCUMENTS_FOLDER_SYSTEM));
                            break;
                        case DOCUMENTS_FOLDER_MYFOLDERS:
                            sideNavBar.selectDocumentView(finalContainer[0], new SelectItem(0, DOCUMENTS_FOLDER_MYFOLDERS));
                            break;
                        case DOCUMENTS_FOLDER_PUBLIC:
                            sideNavBar.selectDocumentView(finalContainer[0], new SelectItem(0, DOCUMENTS_FOLDER_PUBLIC));
                            break;
                        case DOCUMENTS_FOLDER_SHARED:
                            sideNavBar.selectDocumentView(finalContainer[0], new SelectItem(0, DOCUMENTS_FOLDER_SHARED));
                            break;
                        case DOCUMENTS_FOLDER_TRASH:
                            sideNavBar.selectDocumentView(finalContainer[0], new SelectItem(0, DOCUMENTS_FOLDER_TRASH));
                            break;
                        case DOCUMENTS_FOLDER_OTHERS:
                            sideNavBar.selectDocumentView(finalContainer[0], new SelectItem(0, DOCUMENTS_FOLDER_OTHERS));
                            break;
                    }
                }
            };
            scheduler.schedule(6000);
            container = finalContainer[0];
        } else {
            container = getContainerByName(containerName);

            if (container == null) {
                container = MainLayout.get().getSideNavBar().getCurrentContainer() != null ? MainLayout.get().getSideNavBar().getCurrentContainer() : getContainerByName(getDefaultContainer());
            }

            if (container != null) {
                if (container.getViewsName().contains(viewName)) {
                    container.setPreparedView(viewName);
                    ShortcutItem shortcut = container.getItemsByView().get(viewName);
                    MainLayout.get().getSideNavBar().makeActive(container, shortcut);
                } else if (container.getPreparedView() != null) {
                    MainLayout.get().getSideNavBar().setSelection(container);
                }
            }
        }

        return container;
    }

    /**
     * Decides which view should be shown - Welcome Page or Home Page.
     * Removes Welcome Page if user has Client Role.
     *
     * @param container   your container name
     * @param firstView   column name in EdsLanding field that describes which page should be first.
     * @param homeView    name of Home Page view.
     * @param landingView name of Welcome Page view.
     */
    public void showPrepairedView(SinksContainer container, String firstView,
                                  String homeView, String landingView) {
        if (!Utils.hasRole(CLIENT)) {
            if (Utils.getFirstPage(firstView).equals(HOME_PAGE)) {
                container.setPreparedView(homeView);
            } else if (Utils.getFirstPage(firstView).equals(LANDING_PAGE)) {
                container.setPreparedView(landingView);
            }
        } else {
            container.setPreparedView(homeView);
        }
        setSinksContainer(container);
    }


    public String getDefaultContainer() {
        return defaultContainer;
    }

    public void setDefaultContainer(String defaultContainer) {
        this.defaultContainer = defaultContainer;
    }

    public void activateSinksContainer(String containerName, String viewName) {
        SinksContainer container = getContainerByName(containerName);

        if (container != null) {
            container.setPreparedView(viewName);
            entryPoint.getMainLayout().getSideNavBar().setSelection(container);
        }
    }

    /**
     * <i>... This is method add Popup link if show header equals true ...</i>
     * <br/>
     * <i>... Changed by developer {Dilshod.T} ...</i>
     * <br/>
     * <i>... Updated method date {18:06 11/05/2011} ...</i>
     * <br/>
     */
    protected void addNewMenuItem(String tabName, String historyToken) {
        MainLayout.get().getSideNavBar().addNewMenuItem(tabName, historyToken);
    }

    protected void addNewMenuItem(String tabName, String historyToken, Character accessKey) {
        MainLayout.get().getSideNavBar().addNewMenuItem(tabName, historyToken, accessKey);
    }

    protected void addNewMenuItem(String tabName, String historyToken, Character accessKey, String ID) {
        MainLayout.get().getSideNavBar().addNewMenuItem(tabName, historyToken, accessKey, ID);
    }

    protected void addNewMenuItem(String name, ClickHandler clickHandler) {
        MainLayout.get().getSideNavBar().addNewMenuItem(name, clickHandler);
    }

    protected void disableAddNew() {
        MainLayout.get().getSideNavBar().disableAddNew();
    }

    public String getModuleParam(String paramName) {
        return SinksContainerFactory.entryPoint.moduleSetting.getParams().get(paramName);
    }

    public Workarea getWorkarea() {
        return this.workarea;
    }
}
