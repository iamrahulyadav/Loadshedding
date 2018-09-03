package com.ansoft.loadshedding.webservice;

import org.simpleframework.xml.Element;

public class LoadsheddingDayInfo {

	@Element(name = "Group")
	private int group;

	@Element(name = "Day")
	private String day;

	@Element(name = "Time")
	private String time;

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
