package com.edatasite.workforce.gwt.reportingsystem.client.enumerable;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.ArrayList;

/**
 * User: ${Dilsh0d}
 * Date: 29-Mar-2010
 * Time: 21:35:52
 */
public enum OperationType {

    IsEqualTo("is equal to") {
        public String getOperator() {
            return "=";
        }
    },
    IsNotEqualTo("is not equal to") {
        public String getOperator() {
            return "<>";
        }
    },
    StartsWith("starts with") {
        public String getOperator() {
            return "like '"; /*like 'value%'*/
        }
    },
    Contains("contains") {
        public String getOperator() {
            return "like '%";/*like '%value%'*/
        }
    },
    DoesNoTContain("does not contain") {
        public String getOperator() {
            return "not like '%";/*not like '%value%'*/
        }
    },
    EndsWith("ends with") {
        public String getOperator() {
            return "like '%";/*like '%value'*/
        }
    },
    IsLessThan("is less than") {
        public String getOperator() {
            return "<";
        }
    },
    IsLessOrEqualTo("is less or equal to") {
        public String getOperator() {
            return "<=";
        }
    },
    IsEqualOrGreaterThan("is equal or greater than") {
        public String getOperator() {
            return ">=";
        }
    },
    IsGreaterThan("is greater than") {
        public String getOperator() {
            return ">";
        }
    };


    private final String name;

    OperationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static OperationType[] getOperations(String type) {
        if (SqlColumnType.STRING.getName().equals(type)) {
            ArrayList<OperationType> operation = new ArrayList<>();
            for (OperationType value : values()) {
                if (!IsLessThan.name().equals(value.name()) && !IsLessOrEqualTo.name().equals(value.name()) && !IsEqualOrGreaterThan.name().equals(value.name()) && !IsGreaterThan.name().equals(value.name())) {
                    operation.add(value);
                }
            }
            return operation.toArray(new OperationType[operation.size()]);
        } else {
            ArrayList<OperationType> operation = new ArrayList<>();
            for (OperationType value : values()) {
                if (!StartsWith.name().equals(value.name()) && !Contains.name().equals(value.name()) && !DoesNoTContain.name().equals(value.name()) && !EndsWith.name().equals(value.name())) {
                    operation.add(value);
                }
            }
            return operation.toArray(new OperationType[operation.size()]);
        }
    }

    public static SelectItem[] asSelectItems(String selectedType) {
        OperationType[] temp = getOperations(selectedType);
        SelectItem[] items = new SelectItem[temp.length];
        int i = 0;
        String s = null;
        for (OperationType type : temp) {
            switch (type.name()) {
                case "IsEqualTo":
                    s = WfmStrings.App.get().equal();
                    break;
                case "IsNotEqualTo":
                    s = WfmStrings.App.get().isNotEqualTo();
                    break;
                case "StartsWith":
                    s = WfmStrings.App.get().startsWith();
                    break;
                case "Contains":
                    s = WfmStrings.App.get().contains();
                    break;
                case "DoesNoTContain":
                    s = WfmStrings.App.get().notContains();
                    break;
                case "EndsWith":
                    s = WfmStrings.App.get().endsWith();
                    break;
                case "IsLessThan":
                    s = WfmStrings.App.get().isLessThan();
                    break;
                case "IsLessOrEqualTo":
                    s = WfmStrings.App.get().isLessOrEqualTo();
                    break;
                case "IsEqualOrGreaterThan":
                    s = WfmStrings.App.get().isEqualOrGreaterThan();
                    break;
                case "IsGreaterThan":
                    s = WfmStrings.App.get().isGreaterThan();
                    break;
            }
            items[i++] = new SelectItem(i, s, type.name());
        }
        return items;
    }

    public static OperationType getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (OperationType type : values()) {
            if (code.equals(type.name())) {
                return type;
            }
        }
        return null;
    }

    public abstract String getOperator();
}
