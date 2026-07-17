package com.edatasite.workforce.gwt.core.client.rpc.resourceUtil;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Ilhombek
 * Date: 5/23/12
 * Time: 11:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeResourceUtilItem implements IsSerializable {

    private Integer employee_id;                          //Employee id
    private String employee_name;                         //Employee name
	private String employee_code;                         //Employee code
	private String employee_photo;                        //Employee photo
	private String employee_position;                     //Employee position
	private Date date;                                  //Date

	private int[] totalHours;

    private int[] totalTimeSlotHours;                     //Daily total timeSlot hour
    private int[] totalInOutHours;                     //Daily total inOut hour
    private int[] totalTimeSheetHours;                    //Daily total timeSheet hour
	private boolean[] dayOff;

    private int[] withHoliday_INT;                        //Daily holiday time
    private int[] with_LR_INT;                            //With LR time
	private String[] shortNameLR;                          //Short name


	public EmployeeResourceUtilItem() {
	}

	public EmployeeResourceUtilItem(Integer employee_id, String employee_name, String employee_code, String employee_photo, String employee_position, int maxMonthDay) {
		this.employee_id = employee_id;
		this.employee_name = employee_name;
		this.employee_code = employee_code;
		this.employee_photo = employee_photo;
		this.employee_position = employee_position;
		this.totalHours = new int[maxMonthDay];
		this.totalTimeSlotHours = new int[maxMonthDay];
		this.totalInOutHours = new int[maxMonthDay];
		this.totalTimeSheetHours = new int[maxMonthDay];
		this.dayOff = new boolean[maxMonthDay];
		this.withHoliday_INT = new int[maxMonthDay];
		this.with_LR_INT = new int[maxMonthDay];
		this.shortNameLR = new String[maxMonthDay];
	}

	public Integer getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(Integer employee_id) {
		this.employee_id = employee_id;
	}

	public String getEmployee_name() {
		return employee_name;
	}

	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}


	public int[] getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(int[] totalHours) {
		this.totalHours = totalHours;
	}

	public int[] getTotalTimeSlotHours() {
		return totalTimeSlotHours;
	}

	public void setTotalTimeSlotHours(int[] totalTimeSlotHours) {
		this.totalTimeSlotHours = totalTimeSlotHours;
	}

	public int[] getTotalInOutHours() {
		return totalInOutHours;
	}

	public void setTotalInOutHours(int[] totalInOutHours) {
		this.totalInOutHours = totalInOutHours;
	}

	public int[] getTotalTimeSheetHours() {
		return totalTimeSheetHours;
	}

	public void setTotalTimeSheetHours(int[] totalTimeSheetHours) {
		this.totalTimeSheetHours = totalTimeSheetHours;
	}

	public boolean[] getDayOff() {
		return dayOff;
	}

	public void setDayOff(boolean[] dayOff) {
		this.dayOff = dayOff;
	}

	public int[] getWithHoliday_INT() {
		return withHoliday_INT;
	}

	public void setWithHoliday_INT(int[] withHoliday_INT) {
		this.withHoliday_INT = withHoliday_INT;
	}

	public int[] getWith_LR_INT() {
		return with_LR_INT;
	}

	public void setWith_LR_INT(int[] with_LR_INT) {
		this.with_LR_INT = with_LR_INT;
	}

	public String getEmployee_code() {
		return employee_code;
	}

	public void setEmployee_code(String employee_code) {
		this.employee_code = employee_code;
	}

	public String getEmployee_photo() {
		return employee_photo;
	}

	public void setEmployee_photo(String employee_photo) {
		this.employee_photo = employee_photo;
	}

	public String getEmployee_position() {
		return employee_position;
	}

	public void setEmployee_position(String employee_position) {
		this.employee_position = employee_position;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String[] getShortNameLR() {
		return shortNameLR;
	}

	public void setShortNameLR(String[] shortNameLR) {
		this.shortNameLR = shortNameLR;
	}
}
