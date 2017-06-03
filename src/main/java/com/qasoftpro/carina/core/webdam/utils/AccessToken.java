package com.qasoftpro.carina.core.webdam.utils;

import com.jayway.restassured.response.Response;
import com.qaprosoft.carina.core.foundation.http.HttpResponseStatusType;
import com.qasoftpro.carina.core.webdam.auth.api.PostTokenMethod;
import org.skyscreamer.jsonassert.JSONCompareMode;
import com.qaprosoft.apitools.validation.JsonCompareKeywords;
import com.qaprosoft.carina.core.foundation.http.HttpResponseStatusType;
import com.qaprosoft.carina.core.foundation.performance.Timer;

/*
 * @author Amit Maheshwari
 * @since June 2, 2017
 * @description This is a singleton class to return access token for all API calls.
 */

public class AccessToken {
	private static String token="";
	private AccessToken() {}
	
	public static String get(){
		if(token == null || token.length()==0){
		PostTokenMethod api = new PostTokenMethod();
		api.request.contentType("application/json");
		api.setBodyContent("{\"grant_type\":\"client_credentials\",\"client_id\":\"4\",\"client_secret\":\"4ovGa5yXfHnWR47wGRVUfKlDTBxC3WQtnkmO5sgs\",\"scope\":\"\"}");
		Response res = api.callAPI();
		if (res.getStatusCode() >= 200 && res.getStatusCode() <= 300){
			token = res.jsonPath().getString("access_token");
		}
		}
		return token;
	}
}
