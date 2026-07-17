/*
package com.edatasite.workforce.gwt.workstream.client.ui.extTreeTable;

import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.google.gwt.user.client.ui.Widget;

public class ExtTreeTable extends TreeTable {

    private TreeTableViewer viewer;
    private WfmTreeItem[] data;
    private boolean isRender = false;

    private static final Integer ROOT = Integer.valueOf(0);

    public ExtTreeTable(int style, final ExtTreeTableColumn[] columns) {
        super(style, new TreeTableColumnModel(columns));
        viewer = new TreeTableViewer(this);
        for (int i = 0; i < columns.length; i++) {
            viewer.getViewerColumn(i).setLabelProvider(new CellLabelProvider() {
                public void update(ViewerCell cell) {
                    */
/* if (!isRender) *//*

                    {
//                        isRender = true;
                        ExtTreeTableColumn column = columns[cell.getColumnIndex()];
                        Object value = column.getColumnLabelProvider().getColumnText(cell.getElement());
                        if (value instanceof String) {
                            cell.setText((String) value);
                        } else if (value instanceof Widget) {
                            cell.setWidget((Widget) value);
                        }
                    }
                }
            });
            if (columns[i].getSorter() != null) {
                viewer.getViewerColumn(i).setViewerSorter(columns[i].getSorter());
            }
        }
    }

    public void setProvider(final ExtTreeProvider provider) {
        viewer.setContentProvider(new ITreeContentProvider() {
            public void getChildren(Object parent, final IAsyncContentCallback callback) {
                if (parent.equals(ROOT)) {
                    callback.setElements(data);
                } else {
                    final WfmTreeItem parentItem = (WfmTreeItem) parent;
                    provider.getChildren(parentItem, new ExtTreeTableCallback() {
                        public void setData(WfmTreeItem[] data) {
                            for (int i = 0; i < data.length; i++) {
                                data[i].setParent(parentItem);
                            }
                            callback.setElements(data);
                        }
                    });
                }
            }

            public Object getParent(Object element) {
                WfmTreeItem item = (WfmTreeItem) element;
                return item.getParent();
            }

            public boolean hasChildren(Object element) {
                WfmTreeItem item = (WfmTreeItem) element;
                return item.hasChildren();
            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

            }
        });
    }

    public void setData(WfmTreeItem[] data) {
        this.data = data;
        viewer.setInput(ROOT);
    }

    public void refresh(WfmTreeItem item) {
        viewer.refresh(item);
    }

    public void refresh() {
        viewer.refresh();
    }

    public void addSelectionListener(ISelectionChangedListener listener) {
        viewer.addSelectionListener(listener);
    }
}
*/
