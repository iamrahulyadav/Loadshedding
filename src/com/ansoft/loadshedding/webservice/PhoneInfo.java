package com.ansoft.loadshedding.webservice;

import java.util.List;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class PhoneInfo {

	@ElementList(entry = "LOCATION", inline = true)
	private List<Phone> locations;

	public List<Phone> getLocations() {
		return locations;
	}
}
