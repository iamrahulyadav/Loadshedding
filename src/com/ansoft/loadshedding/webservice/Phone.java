package com.ansoft.loadshedding.webservice;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class Phone implements Comparable<Phone> {

	@Attribute(name = "CODE")
	private String code;
	@Attribute(name = "NAME")
	private String name;
	@Attribute(name = "PHONE")
	private String phone;

	@Attribute(name = "GEO")
	private String geo;

	private double distance;

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String[] getPhones() {
		return phone.split(",");
	}

	public String getGeo() {
		return geo;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
	public int compareTo(Phone another) {
		double diff = distance - another.distance;
		if (diff > 0) {
			return 1;
		} else if (diff < 0) {
			return -1;
		}
		return 0;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setGeo(String geo) {
		this.geo = geo;
	}
}
