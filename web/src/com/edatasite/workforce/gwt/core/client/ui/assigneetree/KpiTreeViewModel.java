package com.edatasite.workforce.gwt.core.client.ui.assigneetree;

import com.edatasite.workforce.gwt.core.client.ui.cell.CheckBoxCell;
import com.google.gwt.cell.client.*;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.TreeViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: 26.04.12
 * Time: 11:11
 */
public class KpiTreeViewModel implements TreeViewModel {

    private Cell<KpiTreeInfo> itemCell;
    private Cell<KpiTreeInfo> categoryCell;
    private final DefaultSelectionEventManager<KpiTreeInfo> selectionManager = DefaultSelectionEventManager.createCheckboxManager();
    private final SelectionModel<KpiTreeInfo> selectionModel;
    private LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> treeDatabase = null;
    private KpiTreeInfo lastCheckedItem;
    private Integer limit;
    private String limitErrorMessage;

    public KpiTreeViewModel(final SelectionModel<KpiTreeInfo> selectionModel, LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> treeDatabase) {
        this.selectionModel = selectionModel;
        this.treeDatabase = treeDatabase;

        createCategoryCell();
        createItemCell();
    }

    public KpiTreeViewModel(final SelectionModel<KpiTreeInfo> selectionModel, LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> treeDatabase, Integer limit, String limitErrorMessage) {
        this.selectionModel = selectionModel;
        this.treeDatabase = treeDatabase;
        this.limit = limit;
        this.limitErrorMessage = limitErrorMessage;

        createCategoryCell();
        createItemCell();
    }

    private void createItemCell() {
        ArrayList<HasCell<KpiTreeInfo, ?>> hasCells = cellItems();

        itemCell = new CompositeCell<KpiTreeInfo>(hasCells) {
            @Override
            public void render(Context context, KpiTreeInfo value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<table><tbody><tr>");
                super.render(context, value, sb);
                sb.appendHtmlConstant("</tr></tbody></table>");
            }

            @Override
            protected Element getContainerElement(Element parent) {
                // Return the first TR element in the table.
                return parent.getFirstChildElement().getFirstChildElement().getFirstChildElement();
            }

            @Override
            protected <X> void render(Context context, KpiTreeInfo value,
                                      SafeHtmlBuilder sb, HasCell<KpiTreeInfo, X> hasCell) {
                Cell<X> cell = hasCell.getCell();
                sb.appendHtmlConstant("<td>");
                cell.render(context, hasCell.getValue(value), sb);
                sb.appendHtmlConstant("</td>");
            }
        };
    }

    private ArrayList<HasCell<KpiTreeInfo, ?>> cellItems() {
        ArrayList<HasCell<KpiTreeInfo, ?>> hasCells = new ArrayList<>();
        hasCells.add(new HasCell<KpiTreeInfo, Boolean>() {

            private CheckBoxCell cell = new CheckBoxCell(true, true);

            public Cell<Boolean> getCell() {
                return cell;
            }

            public FieldUpdater<KpiTreeInfo, Boolean> getFieldUpdater() {
                return (index, object, value) -> {
                    object.setSelected(value);
                    selectionModel.setSelected(object, object.isSelected());
                    lastCheckedItem = object;
                };
            }

            public Boolean getValue(KpiTreeInfo object) {
                selectionModel.setSelected(object, object.isSelected());
                return object.isSelected();
            }
        });

        hasCells.add(new HasCell<KpiTreeInfo, KpiTreeInfo>() {
            TreeItemCell cell = new TreeItemCell();

            public Cell<KpiTreeInfo> getCell() {
                return cell;
            }

            public FieldUpdater<KpiTreeInfo, KpiTreeInfo> getFieldUpdater() {
                return null;
            }

            /**
             * Returns the value of type C extracted from the record of type T.
             *
             * @param object a record of type T
             * @return a value of type C suitable for passing to the cell
             */

            public KpiTreeInfo getValue(KpiTreeInfo object) {
                return object;
            }
        });
        return hasCells;
    }

    private ArrayList<HasCell<KpiTreeInfo, ?>> cellCategory() {
        ArrayList<HasCell<KpiTreeInfo, ?>> hasCells = new ArrayList<>();
        hasCells.add(new HasCell<KpiTreeInfo, Boolean>() {

            private CheckBoxCell cell = new CheckBoxCell(true, true);

            FieldUpdater<KpiTreeInfo, Boolean> fieldUpdater = new FieldUpdater<KpiTreeInfo, Boolean>() {
                @Override
                public void update(int index, KpiTreeInfo object, Boolean value) {
                    Integer checkedCount = 0;
                    for (KpiTreeInfo item : treeDatabase.get(object)) {
                        if (limit != null && checkedCount.equals(limit)) {
                            break;
                        }
                        item.setSelected(value);
                        selectionModel.setSelected(item, value);
                        checkedCount++;
                    }
                }
            };

            public Cell<Boolean> getCell() {
                return cell;
            }

            public FieldUpdater<KpiTreeInfo, Boolean> getFieldUpdater() {
                return fieldUpdater;
            }

            public Boolean getValue(KpiTreeInfo object) {
                return selectionModel.isSelected(object);
            }
        });

        hasCells.add(new HasCell<KpiTreeInfo, KpiTreeInfo>() {
            TreeItemCell cell = new TreeItemCell();

            public Cell<KpiTreeInfo> getCell() {
                return cell;
            }

            public FieldUpdater<KpiTreeInfo, KpiTreeInfo> getFieldUpdater() {
                return null;
            }

            /**
             * Returns the value of type C extracted from the record of type T.
             *
             * @param object a record of type T
             * @return a value of type C suitable for passing to the cell
             */

            public KpiTreeInfo getValue(KpiTreeInfo object) {
                return object;
            }
        });
        return hasCells;
    }

    private void createCategoryCell() {
        ArrayList<HasCell<KpiTreeInfo, ?>> hasCells = cellCategory();

        categoryCell = new CompositeCell<KpiTreeInfo>(hasCells) {
            @Override
            public void render(Context context, KpiTreeInfo value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<table><tbody><tr>");
                super.render(context, value, sb);
                sb.appendHtmlConstant("</tr></tbody></table>");
            }

            @Override
            protected Element getContainerElement(Element parent) {
                // Return the first TR element in the table.
                return parent.getFirstChildElement().getFirstChildElement().getFirstChildElement();
            }

            @Override
            protected <X> void render(Context context, KpiTreeInfo value,
                                      SafeHtmlBuilder sb, HasCell<KpiTreeInfo, X> hasCell) {
                Cell<X> cell = hasCell.getCell();
                sb.appendHtmlConstant("<td>");
                cell.render(context, hasCell.getValue(value), sb);
                sb.appendHtmlConstant("</td>");
            }
        };
    }

    /**
     * Get the {@link com.google.gwt.view.client.TreeViewModel.NodeInfo} that will provide the {@link com.google.gwt.view.client.ProvidesKey},
     * {@link com.google.gwt.cell.client.Cell}, and {@link com.google.gwt.view.client.HasData} instances to retrieve and display the
     * children of the specified value.
     *
     * @param value the value in the parent node
     * @return the {@link com.google.gwt.view.client.TreeViewModel.NodeInfo}
     */
    public <T> NodeInfo<?> getNodeInfo(T value) {
        if (value == null) {
            ArrayList<KpiTreeInfo> category = new ArrayList<>(treeDatabase.keySet());
            return new DefaultNodeInfo<>(new ListDataProvider<>(category), categoryCell, selectionModel, selectionManager, null);
        } else if (value instanceof KpiTreeInfo) {
            KpiTreeInfo count = (KpiTreeInfo) value;
            List<KpiTreeInfo> items = treeDatabase.get(count);
            Collections.sort(items);
            ListDataProvider<KpiTreeInfo> dataProvider = new ListDataProvider<>(items, KpiTreeInfo.KEY_PROVIDER);
            return new DefaultNodeInfo<>(dataProvider, itemCell, selectionModel, selectionManager, null);
        }

        // Unhandled type.
        String type = value.getClass().getName();
        throw new IllegalArgumentException("Unsupported object type in treeCell: " + type);
    }

    /**
     * Check if the value is known to be a leaf node.
     *
     * @param value the value at the node
     * @return true if the node is known to be a leaf node, false otherwise
     */
    public boolean isLeaf(Object value) {
        if (value instanceof KpiTreeInfo) {
            KpiTreeInfo item = (KpiTreeInfo) value;
            if (item.getDepartmentId() != null) {
                return true;
            }
        }
        return false;
    }

    public KpiTreeInfo getLastCheckedItem() {
        return lastCheckedItem;
    }

    public class TreeItemCell extends AbstractCell<KpiTreeInfo> {

        @Override
        public void render(Context context, KpiTreeInfo value, SafeHtmlBuilder sb) {
            if (value == null) {
                return;
            }
            sb.appendHtmlConstant(value.getName());
        }
    }
}
