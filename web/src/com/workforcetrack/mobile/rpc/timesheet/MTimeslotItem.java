package com.workforcetrack.mobile.rpc.timesheet;

import com.edatasite.workforce.gwt.core.client.rpc.Departments;
import com.edatasite.workforce.gwt.core.client.rpc.TimeslotItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Abdulaziz
 * Date: 7/16/11
 * Time: 7:13 PM
 */
@XmlRootElement
public class MTimeslotItem implements Serializable{
    public MTimeslotItem(){

    }

    public MTimeslotItem(TimeslotItem tItem){
        this.objectID = tItem.getObjectID();
        this.name = tItem.getName();
        this.description = tItem.getDescription();
        if(tItem.getDepartments()!=null && tItem.getDepartments().length > 0){
            for(Departments d:tItem.getDepartments()){
                this.departments.add(new MDepartments(d));
            }
        }
        this.monday = fillList(tItem.getMonday(),this.monday);
        this.lunchMo = fillList(tItem.getLunchMo(),this.lunchMo);
        this.coffeeMo = fillList(tItem.getCoffeeMo(),this.coffeeMo);
        this.tuesday = fillList(tItem.getTuesday(),this.tuesday);
        this.lunchTu = fillList(tItem.getLunchTu(),this.lunchTu);
        this.coffeeTu = fillList(tItem.getCoffeeTu(),this.coffeeTu);
        this.wednesday = fillList(tItem.getWednesday(),this.wednesday);
        this.lunchWe = fillList(tItem.getLunchWe(),this.lunchWe);
        this.coffeeWe = fillList(tItem.getCoffeeWe(),this.coffeeWe);
        this.thursday = fillList(tItem.getThursday(),this.thursday);
        this.lunchTh = fillList(tItem.getLunchTh(),this.lunchTh);
        this.coffeeTh = fillList(tItem.getCoffeeTh(),this.coffeeTh);
        this.friday = fillList(tItem.getCoffeeTh(),this.coffeeTh);

        this.lunchFr = fillList(tItem.getLunchFr(),this.lunchFr);
        this.coffeeFr = fillList(tItem.getCoffeeFr(),this.coffeeFr);
        this.saturday = fillList(tItem.getSaturday(),this.saturday);
        this.lunchSa = fillList(tItem.getLunchSa(),this.lunchSa);
        this.coffeeSa = fillList(tItem.getCoffeeSa(),this.coffeeSa);
        this.isDefaultTimeSlot = tItem.isDefaultTimeSlot();

        this.departmentsAsString = tItem.getDepartmentsAsString();
        this.weekDaysPlannedTime = fillList(tItem.getWeekDaysPlannedTime(),this.weekDaysPlannedTime);
    }

    public static TimeslotItem convertFromMobile(MTimeslotItem mItem){
        TimeslotItem tItem = new TimeslotItem();
        tItem.setObjectID(mItem.getObjectID());
        tItem.setName(mItem.getName());
        tItem.setDescription(mItem.getDescription());
        if(mItem.getDepartments()!=null && mItem.getDepartments().size()>0){
            tItem.setDepartments(mItem.getDepartments().toArray(new Departments[]{}));
        }
        tItem.setMonday(fillArray(tItem.getMonday(),mItem.getMonday()));
        tItem.setLunchMo(fillArray(tItem.getLunchMo(),mItem.getLunchMo()));
        tItem.setCoffeeMo(fillArray(tItem.getCoffeeMo(),mItem.getCoffeeMo()));
        tItem.setTuesday(fillArray(tItem.getTuesday(),mItem.getTuesday()));
        tItem.setLunchTu(fillArray(tItem.getLunchTu(),mItem.getLunchTu()));
        tItem.setCoffeeTu(fillArray(tItem.getCoffeeTu(),mItem.getCoffeeTu()));
        tItem.setWednesday(fillArray(tItem.getWednesday(),mItem.getWednesday()));
        tItem.setLunchWe(fillArray(tItem.getLunchWe(),mItem.getLunchWe()));
        tItem.setCoffeeWe(fillArray(tItem.getCoffeeWe(),mItem.getCoffeeWe()));
        tItem.setThursday(fillArray(tItem.getThursday(),mItem.getThursday()));
        tItem.setLunchTh(fillArray(tItem.getLunchTh(),mItem.getLunchTh()));
        tItem.setCoffeeTh(fillArray(tItem.getCoffeeTh(),mItem.getCoffeeTh()));
        tItem.setFriday(fillArray(tItem.getFriday(),mItem.getFriday()));
        tItem.setLunchFr(fillArray(tItem.getLunchFr(),mItem.getLunchFr()));
        tItem.setCoffeeFr(fillArray(tItem.getCoffeeFr(),mItem.getCoffeeFr()));
        tItem.setSaturday(fillArray(tItem.getSaturday(),mItem.getSaturday()));
        tItem.setLunchSa(fillArray(tItem.getLunchSa(),mItem.getLunchSa()));
        tItem.setCoffeeSa(fillArray(tItem.getCoffeeSa(),mItem.getCoffeeSa()));
        tItem.setSunday(fillArray(tItem.getSunday(),mItem.getSunday()));
        tItem.setLunchSu(fillArray(tItem.getLunchSu(),mItem.getLunchSu()));
        tItem.setCoffeeSu(fillArray(tItem.getCoffeeSu(),mItem.getCoffeeSu()));

        tItem.setDefaultTimeSlot(mItem.isDefaultTimeSlot());
        //tItem.setDepartmentsAsString(mItem.getDepartmentsAsString());
        tItem.setWeekDaysPlannedTime(fillArray(tItem.getWeekDaysPlannedTime(),mItem.getWeekDaysPlannedTime()));

        return tItem;
    }

    private Integer objectID;
    private String name;
    private String description;
    private List<MDepartments> departments = new ArrayList<>();
    private List<Integer> monday = new ArrayList<>();
    private List<Integer> lunchMo = new ArrayList<>();
    private List<Integer> coffeeMo = new ArrayList<>();
    private List<Integer> tuesday = new ArrayList<>();
    private List<Integer> lunchTu = new ArrayList<>();
    private List<Integer> coffeeTu = new ArrayList<>();
    private List<Integer> wednesday = new ArrayList<>();
    private List<Integer> lunchWe = new ArrayList<>();
    private List<Integer> coffeeWe = new ArrayList<>();
    private List<Integer> thursday = new ArrayList<>();
    private List<Integer> lunchTh = new ArrayList<>();
    private List<Integer> coffeeTh = new ArrayList<>();
    private List<Integer> friday = new ArrayList<>();
    private List<Integer> lunchFr = new ArrayList<>();
    private List<Integer> coffeeFr = new ArrayList<>();
    private List<Integer> saturday = new ArrayList<>();
    private List<Integer> lunchSa = new ArrayList<>();
    private List<Integer> coffeeSa = new ArrayList<>();
    private List<Integer> sunday = new ArrayList<>();
    private List<Integer> lunchSu = new ArrayList<>();
    private List<Integer> coffeeSu = new ArrayList<>();
    private boolean isDefaultTimeSlot=false;

    private List<Integer> departmentsID = new ArrayList<>();
    private String departmentsAsString;

    private List<Integer> weekDaysPlannedTime = new ArrayList<>();

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<MDepartments> getDepartments() {
        return departments;
    }

    public void setDepartments(List<MDepartments> departments) {
        this.departments = departments;
    }

    public List<Integer> getMonday() {
        return monday;
    }

    public void setMonday(List<Integer> monday) {
        this.monday = monday;
    }

    public List<Integer> getLunchMo() {
        return lunchMo;
    }

    public void setLunchMo(List<Integer> lunchMo) {
        this.lunchMo = lunchMo;
    }

    public List<Integer> getCoffeeMo() {
        return coffeeMo;
    }

    public void setCoffeeMo(List<Integer> coffeeMo) {
        this.coffeeMo = coffeeMo;
    }

    public List<Integer> getTuesday() {
        return tuesday;
    }

    public void setTuesday(List<Integer> tuesday) {
        this.tuesday = tuesday;
    }

    public List<Integer> getLunchTu() {
        return lunchTu;
    }

    public void setLunchTu(List<Integer> lunchTu) {
        this.lunchTu = lunchTu;
    }

    public List<Integer> getCoffeeTu() {
        return coffeeTu;
    }

    public void setCoffeeTu(List<Integer> coffeeTu) {
        this.coffeeTu = coffeeTu;
    }

    public List<Integer> getWednesday() {
        return wednesday;
    }

    public void setWednesday(List<Integer> wednesday) {
        this.wednesday = wednesday;
    }

    public List<Integer> getLunchWe() {
        return lunchWe;
    }

    public void setLunchWe(List<Integer> lunchWe) {
        this.lunchWe = lunchWe;
    }

    public List<Integer> getCoffeeWe() {
        return coffeeWe;
    }

    public void setCoffeeWe(List<Integer> coffeeWe) {
        this.coffeeWe = coffeeWe;
    }

    public List<Integer> getThursday() {
        return thursday;
    }

    public void setThursday(List<Integer> thursday) {
        this.thursday = thursday;
    }

    public List<Integer> getLunchTh() {
        return lunchTh;
    }

    public void setLunchTh(List<Integer> lunchTh) {
        this.lunchTh = lunchTh;
    }

    public List<Integer> getCoffeeTh() {
        return coffeeTh;
    }

    public void setCoffeeTh(List<Integer> coffeeTh) {
        this.coffeeTh = coffeeTh;
    }

    public List<Integer> getFriday() {
        return friday;
    }

    public void setFriday(List<Integer> friday) {
        this.friday = friday;
    }

    public List<Integer> getLunchFr() {
        return lunchFr;
    }

    public void setLunchFr(List<Integer> lunchFr) {
        this.lunchFr = lunchFr;
    }

    public List<Integer> getCoffeeFr() {
        return coffeeFr;
    }

    public void setCoffeeFr(List<Integer> coffeeFr) {
        this.coffeeFr = coffeeFr;
    }

    public List<Integer> getSaturday() {
        return saturday;
    }

    public void setSaturday(List<Integer> saturday) {
        this.saturday = saturday;
    }

    public List<Integer> getLunchSa() {
        return lunchSa;
    }

    public void setLunchSa(List<Integer> lunchSa) {
        this.lunchSa = lunchSa;
    }

    public List<Integer> getCoffeeSa() {
        return coffeeSa;
    }

    public void setCoffeeSa(List<Integer> coffeeSa) {
        this.coffeeSa = coffeeSa;
    }

    public List<Integer> getSunday() {
        return sunday;
    }

    public void setSunday(List<Integer> sunday) {
        this.sunday = sunday;
    }

    public List<Integer> getLunchSu() {
        return lunchSu;
    }

    public void setLunchSu(List<Integer> lunchSu) {
        this.lunchSu = lunchSu;
    }

    public List<Integer> getCoffeeSu() {
        return coffeeSu;
    }

    public void setCoffeeSu(List<Integer> coffeeSu) {
        this.coffeeSu = coffeeSu;
    }

    public boolean isDefaultTimeSlot() {
        return isDefaultTimeSlot;
    }

    public void setDefaultTimeSlot(boolean defaultTimeSlot) {
        isDefaultTimeSlot = defaultTimeSlot;
    }

    public List<Integer> getDepartmentsID() {
        return departmentsID;
    }

    public void setDepartmentsID(List<Integer> departmentsID) {
        this.departmentsID = departmentsID;
    }

    public String getDepartmentsAsString() {
        return departmentsAsString;
    }

    public void setDepartmentsAsString(String departmentsAsString) {
        this.departmentsAsString = departmentsAsString;
    }

    public List<Integer> getWeekDaysPlannedTime() {
        return weekDaysPlannedTime;
    }

    public void setWeekDaysPlannedTime(List<Integer> weekDaysPlannedTime) {
        this.weekDaysPlannedTime = weekDaysPlannedTime;
    }

    private List<Integer> fillList(int[] arr,List<Integer> list){
           if(arr!=null&&arr.length>0){
               for (int anArr : arr) {
                   list.add(anArr);
               }
           }
           return  list;
       }
    private static int[] fillArray(int[] arr, List<Integer> list){
           if(list!=null&&list.size()>0){
               arr = new int[list.size()];
               int i = 0;
               for(Integer ints:list){
                   arr[i] = ints;
                   i++;
               }
               return arr;
           }
           return null;
    }



}
