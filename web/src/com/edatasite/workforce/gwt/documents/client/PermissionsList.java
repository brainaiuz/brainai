package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.PermissionHolder;
import com.edatasite.workforce.gwt.documents.client.rest.resource.RestResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.UserResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import gwt.material.design.client.ui.html.Icon;

import java.util.HashSet;


/**
 * @author Sherali
 */
public class PermissionsList extends Composite {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final DocumentImages.Images images = DocumentImages.get();
    private HashSet<PermissionHolder> permissions = new HashSet<>();
    private UserResource owner;
    private PermissionHolder toRemove;
    private FolderResource folderResource;
    private FileResource fileResource;
    private boolean onlyShare;

    private FlexTable permTable = new FlexTable();

    public PermissionsList(HashSet<PermissionHolder> thePermissions, UserResource _owner, RestResource resource, boolean isOnlyShare) {
        onlyShare = isOnlyShare;
        permissions.addAll(thePermissions);

        if (resource != null) {
            if (resource instanceof FolderResource) {
                folderResource = (FolderResource) resource;
                owner = folderResource.getOwner();
            } else {
                fileResource = (FileResource) resource;
                owner = fileResource.getOwner();
            }
        } else {
            owner = _owner;
        }

        if (!onlyShare) {
            permTable.addStyleName("file--PermissionsList props-table");
            permTable.setText(0, 0, wfmStrings.usersGroups());
            permTable.setText(0, 1, wfmStrings.summaryView());
            permTable.setText(0, 2, wfmStrings.edit());
            permTable.setText(0, 3, wfmStrings.delete());
            permTable.setText(0, 4, wfmStrings.modifyAcces());
            permTable.setText(0, 5, "");

            permTable.getFlexCellFormatter().setStyleName(0, 0, "props-labels");
            permTable.getFlexCellFormatter().setStyleName(0, 1, "props-labels");
            permTable.getFlexCellFormatter().setStyleName(0, 2, "props-labels");
            permTable.getFlexCellFormatter().setStyleName(0, 3, "props-labels");
            permTable.getFlexCellFormatter().setStyleName(0, 4, "props-labels");
        }
        initWidget(permTable);

        if (permissions.isEmpty()) {
            getFolderPermissions();
        } else {
            updateTable();
        }
    }

    private void getFolderPermissions() {
        if (folderResource != null) {
            DocumentsService.App.get().getFolderPermissions(folderResource.getObjectId(), new AbstractAsyncCallback<HashSet<PermissionHolder>>() {
                @Override
                public void success(HashSet<PermissionHolder> result) {
                    permissions = result;
                    folderResource.setPermissions(permissions);
                    updateTable();
                }
            });
        }
    }

    public void updateTable() {
        int i = onlyShare ? 0 : 1;
        if (toRemove != null) {
            permissions.remove(toRemove);
            toRemove = null;
        }
        for (final PermissionHolder dto : permissions) {
            Icon removeButton = new Icon();
            removeButton.getElement().getStyle().setCursor(Style.Cursor.POINTER);
            removeButton.setStyleName("ficon--trash");
            removeButton.addClickHandler(event -> {
                toRemove = dto;
                updateTable();
            });
            if (dto.getUser() != null) {
                if (dto.getUser() != null && dto.getUser().getObjectId().equals(owner.getObjectId()) && (null != dto.getRole() || onlyShare)) {
                    String msg = !onlyShare ? dto.getRole() : "Owner";
                    permTable.setHTML(i, 0, "<span>" + AbstractImagePrototype.create(images.myShared()).getHTML() + "&nbsp;" + dto.getUser().getName() + "(" + msg + ")</span>");
                    removeButton.setVisible(false);
                } else {
                    permTable.setHTML(i, 0, "<span>" + AbstractImagePrototype.create(images.myShared()).getHTML() + "&nbsp;" + dto.getUser().getName() + "</span>");
                    if (!dto.isCanChange()) {
                        removeButton.setVisible(false);
                    }
                }
            } else if (dto.getGroup() != null) {
                permTable.setHTML(i, 0, "<span>" + AbstractImagePrototype.create(images.sharing()).getHTML() + "&nbsp;" + dto.getGroup().getGroupName() + "</span>");
                if (!dto.isCanChange()) {
                    removeButton.setVisible(false);
                }
            }
            if (!onlyShare) {
                KpiCheckBox read = new KpiCheckBox();
                read.setValue(true);
                KpiCheckBox write = new KpiCheckBox();
                write.setValue(dto.isWrite());
                KpiCheckBox delete = new KpiCheckBox();
                delete.setValue(dto.isDelete());
                KpiCheckBox modify = new KpiCheckBox();
                modify.setValue(dto.isModifyACL());
                read.setEnabled(false);
                if (dto.getUser() != null && dto.getUser().equals(owner)) {
                    read.setEnabled(false);
                    write.setEnabled(false);
                    delete.setEnabled(false);
                    modify.setEnabled(false);
                }
                permTable.setWidget(i, 1, read);
                permTable.setWidget(i, 2, write);
                permTable.setWidget(i, 3, delete);
                permTable.setWidget(i, 4, modify);
                permTable.getFlexCellFormatter().setHorizontalAlignment(i, 1, HasHorizontalAlignment.ALIGN_CENTER);
                permTable.getFlexCellFormatter().setHorizontalAlignment(i, 2, HasHorizontalAlignment.ALIGN_CENTER);
                permTable.getFlexCellFormatter().setHorizontalAlignment(i, 3, HasHorizontalAlignment.ALIGN_CENTER);
                permTable.getFlexCellFormatter().setHorizontalAlignment(i, 4, HasHorizontalAlignment.ALIGN_CENTER);
                permTable.setWidget(i, 5, removeButton);
            } else {
                permTable.setWidget(i, 1, removeButton);
            }
            i++;
        }
        while (i < permTable.getRowCount()) {
            permTable.removeRow(i);
            i++;
        }
    }

    public void updatePermissionsAccordingToInput() {
        if (!onlyShare) {
            int i = 1;
            for (PermissionHolder dto : permissions) {
                KpiCheckBox r = (KpiCheckBox) permTable.getWidget(i, 1);
                KpiCheckBox w = (KpiCheckBox) permTable.getWidget(i, 2);
                KpiCheckBox d = (KpiCheckBox) permTable.getWidget(i, 3);
                KpiCheckBox m = (KpiCheckBox) permTable.getWidget(i, 4);
                dto.setRead(r.getValue());
                dto.setWrite(w.getValue());
                dto.setDelete(d.getValue());
                dto.setModifyACL(m.getValue());
                i++;
            }
        }
    }

    public HashSet<PermissionHolder> getPermissions() {
        return permissions;
    }

    public void addPermission(PermissionHolder permission) {
        permissions.add(permission);
    }
}
