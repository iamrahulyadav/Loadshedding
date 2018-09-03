package com.ansoft.loadshedding.webservice;

import retrofit.http.GET;

/**
 *
 */
public interface LoadsheddingWebService {
	@GET("/schedule.php")
	// @GET("/u/930617/nls_schedule.xml")
	LoadsheddingInfo getLoadsheddingInfo();
}
