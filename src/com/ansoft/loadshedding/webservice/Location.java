package com.ansoft.loadshedding.webservice;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by Ilya on 30.08.2014.
 */
@Root(strict = false)
public class Location implements Comparable<Location> {

	@Attribute(name = "NAME")
	private String name;

	@Attribute(name = "GROUP")
	private String group;

	@Attribute(name = "CITY")
	private String city;

	public String getName() {
		return name;
	}

	public String getGroup() {
		return group;
	}

	public String getCity() {
		return city;
	}

	@Override
	public int compareTo(Location another) {
		return name.compareTo(another.name);
	}
}
