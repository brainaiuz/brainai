package com.edatasite.workforce.gwt.profile.client.rpc;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 09.09.14
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */
public enum AlternativeCalendarEnum {
    NoAlternativeCalendar(0),
    HijriCalendarStandart(1);

    AlternativeCalendarEnum(int id){
        this.id= id;
    }

    private int id;

    public int getId() {
        return id;
    }

    public static AlternativeCalendarEnum getByIdEnum(Integer id){
        if(id!=null){
            for(AlternativeCalendarEnum alternativeCalendar: AlternativeCalendarEnum.values()){
                if(id == alternativeCalendar.getId()){
                    return alternativeCalendar;
                }
            }
        }
        return null;
    }


}
