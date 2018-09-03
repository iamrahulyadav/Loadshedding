package com.ansoft.loadshedding.webservice;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(strict = false)
public class LoadsheddingInfo {

	@Attribute
	private String effdate;

	@Attribute(name = "LOC_UPDATE")
	private String locUpdate;

	@ElementList(entry = "NLS", inline = true)
	private List<LoadsheddingDayInfo> dayInfos;

	public String getEffdate() {
		return effdate;
	}

	public void setEffdate(String effdate) {
		this.effdate = effdate;
	}

	public String getLocUpdate() {
		return locUpdate;
	}

	public void setLocUpdate(String locUpdate) {
		this.locUpdate = locUpdate;
	}

	public List<LoadsheddingDayInfo> getDayInfos() {
		return dayInfos;
	}

	public void setDayInfos(List<LoadsheddingDayInfo> dayInfos) {
		this.dayInfos = dayInfos;
	}
}
