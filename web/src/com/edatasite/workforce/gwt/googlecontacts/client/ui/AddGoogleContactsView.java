//package com.edatasite.workforce.gwt.googlecontacts.client.ui;
//
//import com.edatasite.workforce.gwt.core.client.View;
//import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
//import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
//import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
//import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
//import com.edatasite.workforce.gwt.core.client.ui.Constants;
//import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
//import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
//import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
//import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Icon;
//import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
//import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
//import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
//import com.edatasite.workforce.gwt.googlecontacts.client.localization.GoogleContactsStrings;
//import com.edatasite.workforce.gwt.googlecontacts.client.rpc.GoogleContactsService;
//import com.edatasite.workforce.gwt.googlecontacts.client.rpc.GoogleContactsServiceAsync;
//import com.google.gwt.core.client.GWT;
//import com.google.gwt.core.client.RunAsyncCallback;
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.user.client.DOM;
//import com.google.gwt.dom.client.Element;
//import com.google.gwt.user.client.rpc.AsyncCallback;
//import com.google.gwt.user.client.ui.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import com.edatasite.workforce.gwt.core.client.Utils;
//
///**
// * Created by IntelliJ IDEA.
// * User: Ruslan Muhammadov
// * Date: 14.11.2008
// * Time: 20:05:39
// * To change this template use File | Settings | File Templates.
// */
//public class AddGoogleContactsView extends View implements Constants, Colapse {
//
//    private static final WfmStrings wfmStrings = WfmStrings.App.get();
//    private static final GoogleContactsServiceAsync googleTalkService = GoogleContactsService.App.get();
//    private static final GoogleContactsStrings googleContactsStrings = GoogleContactsStrings.App.get();
//
//    private GroupItem grouped;
//    private IFrame iframe;
//    private HorizontalPanel thePanel;
//    private VerticalPanel contactsPanel;
//    private Image image;
//    private boolean hascontacts;
//    private List allImpList;
//    private List depImpList;
//    private SimpleLink depLink;
//    private CheckBox chkDep;
//    private Button importButton;
//    private String teamName;
//
//    public AddGoogleContactsView() {
//        super(GOOGLE_CONTACTS, googleContactsStrings.chat());
//    }
//
//    protected Widget onInitialize() {
//        initialize();
//        /*googleTalkService.validateCurrentUser(new AbstractAsyncCallback() {
//            public void success(Object result) {
//                Boolean isValid = (Boolean)result;
//                if(isValid.booleanValue()) {
//                    initialize();
//                } else {
//                    new GoogleAuthorizationPanel(GOOGLE_CONTACTS);
//                }
//            }
//        });*/
//        return null;
//    }
//
//    private void initialize() {
//        String link = "http://talkgadget.google.com/talkgadget/client";
//        iframe = new IFrame(link);
//        DOM.setStyleAttribute(iframe.getElement(), "frameborder", "0");
//        DOM.setStyleAttribute(iframe.getElement(), "position", "relative");
////        DOM.setStyleAttribute(iframe.getElement(),"top","130px");
////        DOM.setStyleAttribute(iframe.getElement(),"left","300px");
//        DOM.setStyleAttribute(iframe.getElement(), "width", "350px");
//        DOM.setStyleAttribute(iframe.getElement(), "height", "90%");
//
//        thePanel = new HorizontalPanel();
//        thePanel.setSpacing(10);
//        thePanel.setSize("100%", "100%");
//        thePanel.add(iframe);
//        add(thePanel);
//
//        /*googleTalkService.getImportedContacts(new AbstractAsyncCallback() {
//            public void success(Object result) {
//                GoogleContactsItem[] gcontacts = (GoogleContactsItem[])result;
//                hascontacts = (gcontacts != null && gcontacts.length > 0);
//                if (hascontacts) {
//                    createContactsPanel(gcontacts);
//                    thePanel.setCellHorizontalAlignment(iframe, HasHorizontalAlignment.ALIGN_CENTER);
//
//                    IconsBundle imageBundle = (IconsBundle) GWT.create(IconsBundle.class);
//                    image = imageBundle.sectionCheckboxImage().createImage();
//                    thePanel.add(image);
//                    thePanel.setCellHeight(image, "100%");
//                    thePanel.setCellWidth(image, "50%");
//                    thePanel.setCellHorizontalAlignment(image, HasAlignment.ALIGN_CENTER);
//                    thePanel.setCellVerticalAlignment(image, HasAlignment.ALIGN_MIDDLE);
//
//                    thePanel.add(contactsPanel);
////                    thePanel.setCellWidth(contactsPanel, "48%");
//                    thePanel.setCellHorizontalAlignment(contactsPanel, HasHorizontalAlignment.ALIGN_CENTER);
//                } else
//                    iframe.setWidth("400px");
//                layout(true);
//            }
//
//            public void failure(Throwable throwable) {
//                 Info.show("", "Error occured during getting your contacts.", "");
//            }
//        });*/
//    }
//
//    interface Groupable {
//        public void group(Object obj);
//    }
//
//    class GroupItem implements Groupable {
//        private HashMap hash = new HashMap();
//
//        public GroupItem(Object[] a) {
//            if (a != null) {
//                for (int i = 0; i < a.length; ++i) {
//                    group(a[i]);
//                }
//            }
//        }
//
//        public void group(Object obj) {
//            ContactListItem item = (ContactListItem) obj;
//            String key = item.getDepartment();
//            if (key == null) {
//                key = googleContactsStrings.companyEmployees()/*"Company Employees"*/;
//            }
//            ArrayList list = (ArrayList) hash.get(key);
//            if (list == null) {
//                list = new ArrayList();
//                hash.put(key, list);
//            }
//            list.add(item);
//        }
//
//        public Iterator getKeys() {
//            return hash.keySet().iterator();
//        }
//
//        public ArrayList getValues(String key) {
//            return (ArrayList) hash.get(key);
//        }
//
//        public boolean remove(String teamName, ContactListItem item) {
//            ArrayList list = getValues(teamName);
//            if (list == null) {
//                return false;
//            }
//            for (int i = 0; i < list.size(); ++i) {
//                if (((ContactListItem) list.get(i)).equals(item)) {
//                    list.remove(i);
//                    return true;
//                }
//            }
//            return true;
//        }
//    }
//
//    private void createContactsPanel(ContactListItem[] contactsItem) {
//        contactsPanel = new VerticalPanel();
//
//        allImpList = new List(Style.SINGLE | Style.FLAT | Style.CHECK);
//        allImpList.setSize(250, 500);
//        allImpList.setScrollEnabled(true);
//
//        depImpList = new List(Style.SINGLE | Style.FLAT | Style.CHECK);
//        depImpList.setSize(250, 500);
//        depImpList.setScrollEnabled(true);
//        depImpList.setVisible(false);
//
//        grouped = new GroupItem(contactsItem);
//        Iterator iterator = grouped.getKeys();
//        while (iterator.hasNext()) {
//            String tName = (String) iterator.next();
//            ListItem listItem = new ListItem(tName);
//            listItem.disable();
//            listItem.setStyleName("eds-shortcutitem-hdr");
//            allImpList.add(listItem);
//
//            Element elem = Utils.findChild("my-listitem-check", listItem.getElement());
//
//            DOM.removeChild(elem, listItem.getCheckBtn().getElement());
//
//            ArrayList list = grouped.getValues(tName);
//            for (int i = 0; i < list.size(); ++i) {
//                ContactListItem citem = (ContactListItem) list.get(i);
//                ListItem item = new ListItem();
//                item.setId("" + citem.getObjectId());
//                item.setText(citem.getName() + " &lt;" + citem.getPrimaryEmail() + "&gt;");
//                item.setChecked(true);
//                item.setAdditionalData(citem);
//                allImpList.add(item);
//            }
//        }
//
//        googleTalkService.getUserTeamName(new AbstractAsyncCallback<String>() {
//            public void success(String result) {
//                teamName = result;
//                ListItem listItem = new ListItem(teamName);
//                listItem.disable();
//                listItem.setStyleName("eds-shortcutitem-hdr");
//                depImpList.add(listItem);
//                Element elem = Utils.findChild("my-listitem-check", listItem.getElement());
//                DOM.removeChild(elem, listItem.getCheckBtn().getElement());
//
//                ArrayList list = grouped.getValues(teamName);
//                for (int i = 0; i < list.size(); ++i) {
//                    ContactListItem citem = (ContactListItem) list.get(i);
//                    ListItem item = new ListItem();
//                    item.setId("" + citem.getObjectId());
//                    item.setText(citem.getName() + " &lt;" + citem.getPrimaryEmail() + "&gt;");
//                    item.setChecked(true);
//                    item.setAdditionalData(citem);
//                    depImpList.add(item);
//                }
//                if (list.size() > 0) {
//                    chkDep.setEnabled(true);
//                }
//            }
//
//            public void failure(Throwable caught) {
//                 Info.show("", googleContactsStrings.errorOccuredDuringGettingYourDepartment(), Info.Type.ERROR);
//            }
//        });
//
//        importButton = new WfmButton2();
//        importButton.setWidth("80px");
//        importButton.setText(googleContactsStrings.importContacts());
//        importButton.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent be) {
//                String strcontacts = getContactsAsStr("<br>", true);
//                if (strcontacts == null) {
//                     Info.show("", googleContactsStrings.selectContacts(), Info.Type.INFO);
//                    return;
//                }
//                final WfmMessageBox dialog = new WfmMessageBox(Icon.QUESTION, Action.YesNo,true);
//                dialog.setText(googleContactsStrings.importContacts());
//                dialog.addCloseHandler(new CloseHandler() {
//                    @Override
//                    public void onSubmit() {
//                        importSelected();
//                    }
//                });
//                HTML hhtml = new HTML(googleContactsStrings.doYouReallyWantAddTheseContacts());
//                HTML chtml = new HTML(strcontacts);
//
//                Panel cp;   //content panel
//                Panel hp;   //header panel
//                Panel bp;   //body panel
//
//                HorizontalPanel hpanel = new HorizontalPanel();
//                hpanel.setSpacing(5);
//                hpanel.add(hhtml);
//                hpanel.setCellHorizontalAlignment(hhtml, HasHorizontalAlignment.ALIGN_LEFT);
//                hp = hpanel;
//
//                ScrollPanel sp = new ScrollPanel();
//                sp.setWidth("100%");
//                sp.setHeight("200px");
//                VerticalPanel vpanel = new VerticalPanel();
//                vpanel.setSpacing(5);
//                vpanel.add(chtml);
//                vpanel.setCellHorizontalAlignment(chtml, HasHorizontalAlignment.ALIGN_LEFT);
//                vpanel.setCellVerticalAlignment(chtml, HasVerticalAlignment.ALIGN_TOP);
//                sp.add(vpanel);
//                bp = sp;
//
//                cp = new VerticalPanel();
//                cp.add(hp);
//                cp.add(bp);
//
//                dialog.setWidth(350);
//                dialog.add(cp);
//                dialog.open();
//            }
//        });
//
//        chkDep = new CheckBox();
//        chkDep.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent sender) {
//                showDepImpList(allImpList.isVisible());
//            }
//        });
//        chkDep.setEnabled(false);
//        depLink = new SimpleLink(googleContactsStrings.myDepartment());
//        depLink.setWidth("80px");
//        depLink.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent sender) {
//                if (chkDep.isEnabled()) {
//                    showDepImpList(allImpList.isVisible());
//                }
//            }
//        });
//
//        SimpleLink allLink = new SimpleLink(googleContactsStrings.all());
//        allLink.setWidth("20px");
//        allLink.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent sender) {
//                setCheckedImpList(true);
//            }
//        });
//
//        SimpleLink noneLink = new SimpleLink(googleContactsStrings.none());
//        noneLink.setWidth("20px");
//        noneLink.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent sender) {
//                setCheckedImpList(false);
//            }
//        });
//
//        HorizontalPanel hpanel = new HorizontalPanel();
//        hpanel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
//        hpanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
//        hpanel.add(chkDep);
//        hpanel.add(depLink);
//        hpanel.add(allLink);
//        hpanel.add(noneLink);
//
//        contactsPanel.setWidth("250px");
//        contactsPanel.setSpacing(2);
//        contactsPanel.add(allImpList);
//        contactsPanel.add(depImpList);
//        contactsPanel.add(hpanel);
//        contactsPanel.add(importButton);
//        contactsPanel.setCellHeight(importButton, "60px");
//        contactsPanel.setCellHorizontalAlignment(importButton, HasHorizontalAlignment.ALIGN_CENTER);
//        contactsPanel.setCellVerticalAlignment(importButton, HasVerticalAlignment.ALIGN_MIDDLE);
//
//        HorizontalPanel note = new HorizontalPanel();
//        note.setSpacing(10);
//        note.add(new HTML(googleContactsStrings.note()));
//        note.add(new HTML(googleContactsStrings.contactsOfSelectedUsersWillBeImportedIntoYourGoogleContacts()));
//        contactsPanel.add(note);
//    }
//
//    private List getImportList(boolean blvisible) {
//        if (blvisible && allImpList.isVisible()) {
//            return allImpList;
//        } else if (!blvisible && !allImpList.isVisible()) {
//            return allImpList;
//        }
//        return depImpList;
//    }
//
//    private void setCheckedImpList(boolean checked) {
//        List visImpList = getImportList(true);
//        for (int i = 0; i < visImpList.getItemCount(); ++i) {
//            visImpList.getProductItem(i).setChecked(checked);
//        }
//    }
//
//    private String getContactsAsStr(String splitter, boolean ashtml) {
//        final List visImpList = getImportList(true);
//        final ListItem[] listItems = visImpList.getChecked();
//        if (listItems.length == 0) {
//            return null;
//        }
//        String gt = "&gt;";
//        String lt = "&lt;";
//        if (!ashtml) {
//            gt = ">";
//            lt = "<";
//        }
//        String str = "";
//        for (int i = 0; i < listItems.length; i++) {
//            ContactListItem gitem = (ContactListItem) listItems[i].getAdditionalData();
//            if (gitem == null) {
//                continue;
//            }
//            if (str != "") {
//                str += splitter;
//            }
//            str += gitem.getName() + " " + lt + gitem.getPrimaryEmail() + gt;
//        }
//        if (str != "") {
//            return str;
//        }
//        return null;
//    }
//
//    private void importSelected() {
//        final List visImpList = getImportList(true);
//        final ListItem[] vlistItems = visImpList.getChecked();
//        ArrayList clist = new ArrayList();
//        ArrayList ilist = new ArrayList();
//        for (int i = 0; i < vlistItems.length; i++) {
//            ContactListItem gitem = (ContactListItem) vlistItems[i].getAdditionalData();
//            if (gitem == null) {
//                continue;
//            }
//            ilist.add(vlistItems[i]);
//            clist.add(gitem);
//        }
//        final ListItem[] listItems = (ListItem[]) ilist.toArray(new ListItem[0]);
//        ContactListItem[] cntactsItem = (ContactListItem[]) clist.toArray(new ContactListItem[0]);
//        importButton.setEnabled(false);
//        googleTalkService.importContacts(cntactsItem, new AbstractAsyncCallback() {
//            public void success(Object result) {
//                final List hidImpList = getImportList(false);
//                for (int i = 0; i < listItems.length; i++) {
//                    ContactListItem cntactsItem1 = (ContactListItem) listItems[i].getAdditionalData();
//                    visImpList.remove(listItems[i]);
//                    grouped.remove(cntactsItem1.getDepartment(), cntactsItem1);
//                    if (teamName != null && cntactsItem1.getDepartment() != null) {
//                        if (cntactsItem1.getDepartment().equals(teamName)) {
//                            for (int j = 0; j < hidImpList.getItemCount(); j++) {
//                                if (listItems[i].getId().equals(hidImpList.getProductItem(j).getId())) {
//                                    hidImpList.remove(hidImpList.getProductItem(j));
//                                }
//                            }
//                        }
//                    }
//                }
//                Iterator iterator = grouped.getKeys();
//                while (iterator.hasNext()) {
//                    String tName = (String) iterator.next();
//                    ArrayList list = grouped.getValues(tName);
//                    if (list.size() == 0) {
//                        for (int i = 0; i < allImpList.getItemCount(); i++) {
//                            if (tName.equals(allImpList.getProductItem(i).getText())) {
//                                allImpList.remove(allImpList.getProductItem(i));
//                            }
//                        }
//                    }
//                }
//                if (allImpList.getItemCount() == 0) {
//                    thePanel.remove(image);
//                    thePanel.remove(contactsPanel);
//                    iframe.setSize("100%", "100%");
//                } else if (depImpList.getItemCount() == 1) {
//                    showDepImpList(false);
//                    chkDep.setEnabled(false);
//                }
//                 Info.show("", googleContactsStrings.yourContactsHasBeenSuccessfullyImported(), Info.Type.INFO);
//                importButton.setEnabled(true);
//
//            }
//
//            public void failure(Throwable caught) {
//                 Info.show("", googleContactsStrings.errorOccuredDuringImportingYourContacts(), Info.Type.ERROR);
//                importButton.setEnabled(true);
//            }
//        });
//    }
//
//    private void showDepImpList(boolean showDep) {
//        allImpList.setVisible(!showDep);
//        depImpList.setVisible(showDep);
//        chkDep.setChecked(showDep);
//    }
//
//    public String getIconStyle() {
//        return "icon-googlecontacts";
//    }
//
//
//    class IFrame extends Frame {
//        public IFrame(String url) {
//            super(url);
//        }
//
//        public void setScrolling(String scrolling) {
//            DOM.setElementProperty(getElement(), "scrolling", scrolling);
//        }
//
//        public void setFrameBorder(String frameborder) {
//            DOM.setElementProperty(getElement(), "frameborder", "0");
//        }
//
//        public void setAlign(String align) {
//            DOM.setElementAttribute(getElement(), "align", align);
//        }
//    }
//
//    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
//        GWT.runAsync(new RunAsyncCallback() {
//
//            public void onFailure(Throwable caught) {
//                callback.onFailure(caught);
//            }
//
//            public void onSuccess() {
//                callback.onSuccess(onInitialize());
//            }
//        });
//    }
//
//}
