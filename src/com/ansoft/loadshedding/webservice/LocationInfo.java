package com.ansoft.loadshedding.webservice;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Ilya on 30.08.2014.
 */
@Root(strict = false)
public class LocationInfo {

	@ElementList(entry = "LOCATION", inline = true)
	private List<Location> locations;

	public List<Location> getLocations() {
		return locations;
	}
}
