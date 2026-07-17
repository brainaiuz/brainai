package com.edatasite.workforce.gwt.core.client.rpc.workflow;

/**
 * Created by Hayot on 3/16/14.
 */
public interface Operands {
    interface Core {
        String EQUAL = "EQUAL";
        String NOT_EQUAL = "NOT_EQUAL";
        String[] ALL = new String[]{EQUAL, NOT_EQUAL};
    }

    interface StringT extends Core {
        String CONTAINS = "CONTAINS";
        String NOT_CONTAINS = "NOT_CONTAINS";
        String MATCHES = "MATCHES";
        String[] SOME = new String[]{CONTAINS, NOT_CONTAINS};
        String[] ALL = new String[]{EQUAL, NOT_EQUAL, CONTAINS, NOT_CONTAINS, MATCHES};
    }

    interface NumberT extends Core {
        String GREATER = "GREATER";
        String GREATER_OR_EQUAL = "GREATER_OR_EQUAL";
        String LOWER = "LOWER";
        String LOWER_OR_EQUAL = "LOWER_OR_EQUAL";
        String[] ALL = new String[]{EQUAL, NOT_EQUAL, GREATER, GREATER_OR_EQUAL, LOWER, LOWER_OR_EQUAL};
    }

    interface DateT extends Core {
        String IS = "IS";
        String IS_NOT = "IS_NOT";
        String IS_BEFORE = "IS_BEFORE";
        String IS_AFTER = "IS_AFTER";
        String BETWEEN = "BETWEEN";
        String NOT_BETWEEN = "NOT_BETWEEN";
        String TODAY = "TODAY";
        String YESTERDAY = "YESTERDAY";
        String TOMORROW = "TOMORROW";
        String AGE_IN_DAYS = "AGE_IN_DAYS";
        String CURRENT_DAY = "CURRENT_DAY";

        String AGE_IN_HOURS = "AGE_IN_HOURS";
        String HAS_DAYS_LEFT = "HAS_DAYS_LEFT";
        String[] ALL = new String[]{IS, IS_NOT, IS_BEFORE, IS_AFTER, BETWEEN, NOT_BETWEEN, TODAY, YESTERDAY, TOMORROW, AGE_IN_DAYS, AGE_IN_HOURS, HAS_DAYS_LEFT, CURRENT_DAY};
        String[] RANGERS = new String[]{">", "<", ">=", "=<", "="};
    }
}
