package com.edatasite.workforce.rest.base.helpers;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.rest.base.to.CheckListItemTO;
import com.edatasite.workforce.rest.base.to.SelectItemTO;
import com.edatasite.workforce.rest.base.to.UserTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by Umidbek.
 */
public class WrapUtils {

    private static final String EMPTY_STRING = "";
    private static final String ARRAY_DELIMITER = ",";

    public static Integer timeToMinutes(String time) {
        if (time == null) {
            return null;
        }

        try {
            String[] chunks = time.split(":");
            return (Integer.valueOf(chunks[0]) * 60) + Integer.valueOf(chunks[1]);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    public static String getFormattedHour(String hour) {
        int hr;
        int mn;
        String formattedHour = "00:00";
        if (hour != null) {
            try {
                hr = Integer.parseInt(hour) / 60;
                mn = Integer.parseInt(hour) % 60;
                String hourStr = Integer.toString(hr);
                if (hourStr.length() < 2) {
                    hourStr = "0" + hourStr;
                }
                String minutesStr = Integer.toString(mn);
                if (minutesStr.length() < 2) {
                    minutesStr = "0" + minutesStr;
                }
                formattedHour = hourStr + ":" + minutesStr;
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                return hour;
            }
        }
        return formattedHour;
    }

    public static Long dateToLong(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }

    public static Long dateToLong(DateNonConvertable dateNonConvertable) {
        if (dateNonConvertable == null || dateNonConvertable.getDate() == null) {
            return null;
        } else {
            return dateNonConvertable.getDate().getTime();
        }
    }

    public static Date longToDate(Long longValue) {
        if (longValue == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(longValue);
        return calendar.getTime();
    }

    public static String getString(Object object) {
        if (object == null) {
            return EMPTY_STRING;
        }

        return String.valueOf(object);
    }

    public static ArrayList<String> getStringArray(Object object) {
        return getStringArray(object, ARRAY_DELIMITER);
    }

    public static ArrayList<String> getStringArray(Object object, String delimiter) {
        if (object == null) {
            return null;
        }
        List<String> list = Arrays.asList(object.toString().split(delimiter));
        return new ArrayList<>(list);
    }

    public static Integer getInteger(Object object, Integer defaultValue) {
        Integer result = getInteger(object);

        if (result == null) {
            return defaultValue;
        }

        return result;
    }

    public static Integer getInteger(Object object) {
        if (object == null) {
            return null;
        }

        if (object instanceof Integer) {
            return (Integer) object;
        }

        if (object instanceof Double) {
            return ((Double) object).intValue();
        }

        try {
            return Integer.parseInt(object.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Float getFloat(Object object) {
        if (object == null) {
            return null;
        }

        if (object instanceof Double) {
            return ((Double) object).floatValue();
        }

        try {
            return Float.parseFloat(object.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Double getDouble(Object object, Double defaultValue) {
        Double result = getDouble(object);

        if (result == null) {
            result = defaultValue;
        }

        return result;
    }

    public static Double getDouble(Object object) {
        if (object == null) {
            return null;
        }

        if (object instanceof Double) {
            return (Double) object;
        }

        if (object instanceof Float) {
            return ((Float) object).doubleValue();
        }

        try {
            return Double.parseDouble(object.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Boolean getBoolean(Object object, Boolean defaultValue) {
        Boolean result = getBoolean(object);

        if (result == null) {
            return defaultValue;
        }

        return result;
    }

    public static Boolean getBoolean(Object object) {
        if (object == null) {
            return null;
        }

        return Boolean.valueOf(String.valueOf(object));
    }

    public static List<SelectItemTO> wrapSelectItemTOs(SelectItem[] selectItems) {
        List<SelectItemTO> selectItemTOs = new ArrayList<>();
        if (selectItems != null) {
            for (SelectItem item : selectItems) {
                selectItemTOs.add(wrapSelectItemTO(item));
            }
        }
        return selectItemTOs;
    }

    public static List<SelectItemTO> wrapSelectItemTOs(List<EdsReference> selectItems) {
        List<SelectItemTO> selectItemTOs = new ArrayList<>();
        if (selectItems != null && !selectItems.isEmpty()) {
            for (EdsReference item : selectItems) {
                selectItemTOs.add(wrapSelectItemTO(item));
            }
        }
        return selectItemTOs;
    }

    public static SelectItemTO wrapSelectItemTO(SelectItem selectItem) {
        if (selectItem == null) {
            return null;
        }
        SelectItemTO item = new SelectItemTO(selectItem.getId());
        item.setName(selectItem.getName());
        if (selectItem.getName() != null && selectItem.getName().contains("->")) {
            item.setName(selectItem.getName().split("->")[1].trim());
        }
        item.setCode(selectItem.getCode());
        item.setDescription(selectItem.getDescription());
        return item;
    }

    public static SelectItemTO wrapSelectItemTO(EdsReference reference) {
        return reference == null
                ? null
                : new SelectItemTO(reference.getObjectID(), reference.getName(), reference.getCode(), reference.getDescription());
    }

    public static List<CheckListItemTO> wrapCheckListItemTOs(SelectItem[] selectItems) {
        List<CheckListItemTO> checkListItemTOs = new ArrayList<>();
        if (selectItems != null) {
            for (SelectItem item : selectItems) {
                checkListItemTOs.add(wrapCheckListItemTO(item));
            }
        }
        return checkListItemTOs;
    }

    public static CheckListItemTO wrapCheckListItemTO(SelectItem selectItem) {
        return selectItem == null ? null : new CheckListItemTO(selectItem.getId(), selectItem.getName(), selectItem.getCode(), selectItem.getDescription(), selectItem.isSelected());
    }

    public static List<SelectItem> wrapSelectItems(SelectItemTO[] selectItemTOs) {
        List<SelectItem> selectItems = new ArrayList<>();
        if (selectItemTOs != null) {
            for (SelectItemTO itemTO : selectItemTOs) {
                selectItems.add(wrapSelectItem(itemTO));
            }
        }
        return selectItems;
    }

    public static ArrayList<SelectItemTO> wrapSelectItemList(SelectItem[] selectItems) {
        ArrayList<SelectItemTO> selectItemList = new ArrayList<>();
        if (selectItems != null) {
            for (SelectItem item : selectItems) {
                selectItemList.add(wrapSelectItemTO(item));
            }
        }
        return selectItemList;
    }

    public static ArrayList<SelectItemTO> wrapSelectItemList(ArrayList<SelectItem> selectItems) {
        ArrayList<SelectItemTO> selectItemList = new ArrayList<>();
        if (selectItems != null && selectItems.size() > 0) {
            for (SelectItem item : selectItems) {
                selectItemList.add(wrapSelectItemTO(item));
            }
        }
        return selectItemList;
    }

    public static SelectItem wrapSelectItem(SelectItemTO selectItemTO) {
        return selectItemTO == null ? null : new SelectItem(selectItemTO.getId(), selectItemTO.getName(), selectItemTO.getCode(), selectItemTO.getDescription(), "");
    }

    public static List<UserTO> wrapUserTOs(SelectItem[] selectItems, boolean... getUniqueUser) {
        boolean isUniqueUser = false;
        if (getUniqueUser != null && getUniqueUser.length > 0) {
            isUniqueUser = getUniqueUser[0];
        }
        List<UserTO> userTOs = new ArrayList<>();
        List<Integer> existingUsers = new ArrayList<>();
        if (selectItems != null) {
            for (SelectItem item : selectItems) {
                if (isUniqueUser) {
                    if (!existingUsers.contains(item.getId())) {
                        existingUsers.add(item.getId());
                        userTOs.add(wrapUserTO(item));
                    }
                } else {
                    userTOs.add(wrapUserTO(item));
                }
            }
        }
        return userTOs;
    }

    public static UserTO wrapUserTO(SelectItem selectItem) {
        return selectItem == null ? new UserTO() : new UserTO(selectItem.getId(), selectItem.getName());
    }

    public static BigDecimal getTotal(BigDecimal amount, Integer calculationScale) {
        if (amount == null) {
            return BigDecimal.ZERO;
        }
        return amount.setScale(calculationScale, RoundingMode.HALF_UP);
    }

    public static ArrayList<SelectItemTO> wrapSelectItemList(List<EdsReference> references) {
        ArrayList<SelectItemTO> selectItemList = new ArrayList<>();
        if (references != null && references.size() > 0) {
            for (EdsReference reference : references) {
                selectItemList.add(wrapSelectItemTO(reference));
            }
        }
        return selectItemList;
    }

    public static ArrayList<SelectItemTO> wrapSelectItemObjectList(List listOfObject) {
        ArrayList<SelectItemTO> selectItemList = new ArrayList<>();
        if (listOfObject != null && listOfObject.size() > 0) {
            for (Object object : listOfObject) {
                if (object instanceof EdsObject) {
                    selectItemList.add(new SelectItemTO(((EdsObject) object).getObjectID(), ((EdsObject) object).getName()));
                }
            }
        }
        return selectItemList;
    }

}
