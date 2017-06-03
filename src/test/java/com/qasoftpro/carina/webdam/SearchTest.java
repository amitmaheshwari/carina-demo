package com.qasoftpro.carina.webdam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import javax.validation.OverridesAttribute.List;
import java.io.File;
import java.net.URLDecoder;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.jayway.restassured.filter.Filter;
import com.jayway.restassured.filter.FilterContext;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.FilterableRequestSpecification;
import com.jayway.restassured.specification.FilterableResponseSpecification;
import com.qaprosoft.carina.core.foundation.dataprovider.annotations.XlsDataSourceParameters;
import com.qaprosoft.carina.core.foundation.http.HttpResponseStatusType;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import com.qasoftpro.carina.core.webdam.auth.api.GetAssetMethod;
import com.qasoftpro.carina.core.webdam.auth.api.GetAssetMethods;
import com.qasoftpro.carina.core.webdam.auth.api.GetLogoutMethod;
import com.qasoftpro.carina.core.webdam.auth.api.GetSearchMethod;
import com.qasoftpro.carina.core.webdam.entities.Asset;
import com.qasoftpro.carina.core.webdam.utils.AccessToken;
import com.qasoftpro.carina.core.webdam.utils.FileUtil;

import java.io.FileReader;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.qaprosoft.carina.core.foundation.APITest;
import com.qaprosoft.carina.core.foundation.dataprovider.annotations.XlsDataSourceParameters;

/*
 * This test class is for Search API test cases.
 * @author Amit Maheshwari
 * @since June 2, 2017
 */


public class SearchTest extends APITest{
	
	protected static final Logger LOGGER = Logger.getLogger(SearchTest.class);
    // data files
	private static final String WEBDAM_XLS_DATA_SOURCE = "xls/webdam.xlsx";
	private static final String PATH_TO_GOLDEN_DATA_JSON = "testdata/assets.json";

	
	@Test(dataProvider = "DataProvider", description = "Search API for Assets")
	@XlsDataSourceParameters(path = WEBDAM_XLS_DATA_SOURCE, sheet = "search", dsUid = "keyword")
	public void testSearch(Map<String, String> params)throws Exception
	{
		 GetSearchMethod api = new GetSearchMethod();
		 api.request.contentType("application/json");
		 api.expectResponseStatus(HttpResponseStatusType.OK_200);
		 api.request.header("Authorization", "Bearer "+AccessToken.get());
		 Map<String, String> q = new HashMap<String, String>();
		 if(params.get("encoded").length()==0 || params.get("encoded") == null){
			 q.put("query", params.get("keyword"));
		}else{
			q.put("query", params.get("encoded"));
		 }
		 q.put("sort", "asc");
		 q.put("limit", "10");
		 api.request.queryParams(q);
		 Response res = api.callAPI();
		 Asset[] asset = res.as(Asset[].class);
         // verify number of records
		 Assert.assertEquals(asset.length, Integer.valueOf(params.get("count")).intValue(), "number of search results mismatch");
		 JSONParser parser = new JSONParser();	
		 JSONArray testdata = (JSONArray) parser.parse(new FileReader(FileUtil.getFile(PATH_TO_GOLDEN_DATA_JSON)));
		 JSONArray t = new JSONArray();
		 JSONObject jo;
		 //create expected data
		 for(int i=0; i<testdata.size();i++){
			jo = (JSONObject)testdata.get(i);
			for(String s: this.getIds(params.get("ids"), ":")){
			  if(jo.containsValue(s)){
				  t.add(jo);
			  }
			}
		 }
		 LOGGER.info("expected test data size:"+t.size());
		 JSONArray mJSONArray = new JSONArray();
		 for(Asset ass: asset){
			LOGGER.info("asset id:"+ass.getasset_id());
			Map<Object, Object> m = new HashMap<Object, Object>();
			m.put("asset_id",ass.getasset_id());
			m.put("text",ass.getText());
			m.put("thumbnail", ass.getThumbnail());
			m.put("keywords", ass.getKeywords());
			m.put("date_modified", ass.getDateModified());
			m.put("date_created", ass.getDateCreated());
			JSONObject j = new JSONObject();
			j.putAll(m);
			mJSONArray.add(j);
		 }
		 //ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		 //String json = ow.writeValueAsString(object);
		 LOGGER.info("Expected:"+t.toJSONString());
		 LOGGER.info("Actual:"+mJSONArray.toJSONString());
//		 JSONCompareResult scr = JSONCompare.compareJSON(t.toJSONString(), mJSONArray.toJSONString(), JSONCompareMode.STRICT_ORDER);
		 JSONAssert.assertEquals(t.toJSONString(), mJSONArray.toJSONString(), JSONCompareMode.LENIENT);
//		 LOGGER.info(scr.getMessage());
		 // pause to avoid 60 requests per minute limit on api calls
		 pause(5);
	}

	
	@Test(dataProvider = "DataProvider", description = "Search API for Assets with limit")
	@XlsDataSourceParameters(path = WEBDAM_XLS_DATA_SOURCE, sheet = "limit", dsUid = "keyword")
	public void testSearchLimit(Map<String, String> params)throws Exception
	{
		 GetSearchMethod api = new GetSearchMethod();
		 api.request.contentType("application/json");
		 api.expectResponseStatus(HttpResponseStatusType.OK_200);
		 api.request.header("Authorization", "Bearer "+AccessToken.get());
		 Map<String, String> q = new HashMap<String, String>();
		 if(params.get("encoded").length()==0 || params.get("encoded") == null){
			 q.put("query", params.get("keyword"));
		}else{
			q.put("query", params.get("encoded"));
		 }
		 q.put("sort", "asc");
		 q.put("limit", params.get("limit") );
		 api.request.queryParams(q);
		 Response res = api.callAPI();
		 Asset[] asset = res.as(Asset[].class);
		 Assert.assertEquals(asset.length, Integer.valueOf(params.get("count")).intValue() < Integer.valueOf(params.get("limit")).intValue() ? Integer.valueOf(params.get("count")).intValue():Integer.valueOf(params.get("limit")).intValue(), "limited number of search results");
		 // pause to avoid 60 requests per minute limit on api calls
		 pause(5);
	}
	

	@Test(dataProvider = "DataProvider", description = "Search API for Assets with ascending order by asset id")
	@XlsDataSourceParameters(path = WEBDAM_XLS_DATA_SOURCE, sheet = "sort", dsUid = "keyword")
	public void testSearchSortAsc(Map<String, String> params)throws Exception
	{
		 GetSearchMethod api = new GetSearchMethod();
		 api.request.contentType("application/json");
		 api.expectResponseStatus(HttpResponseStatusType.OK_200);
		 api.request.header("Authorization", "Bearer "+AccessToken.get());
		 Map<String, String> q = new HashMap<String, String>();
		 if(params.get("encoded").length()==0 || params.get("encoded") == null){
			 q.put("query", params.get("keyword"));
		}else{
			q.put("query", params.get("encoded"));
		 }
		 q.put("sort", "asc");
		 q.put("limit", "10");
		 api.request.queryParams(q);
		 Response res = api.callAPI();
		 Asset[] asset = res.as(Asset[].class);
		 Assert.assertEquals(asset.length, Integer.valueOf(params.get("count")).intValue(), "number of search results mismatch");
		 String[] actualIds = new String[asset.length];
		 for(int i=0; i<asset.length;i++) {
			 actualIds[i]= asset[i].getasset_id();
		 }
         Assert.assertEquals(isSortedArray(actualIds, "asc"), true);	 
		 // pause to avoid 60 requests per minute limit on api calls
         pause(5);
	}
	
	@Test(dataProvider = "DataProvider", description = "Search API for Sorted Assets with descending order by asset id")
	@XlsDataSourceParameters(path = WEBDAM_XLS_DATA_SOURCE, sheet = "sort", dsUid = "keyword")
	public void testSearchSortDesc(Map<String, String> params)throws Exception
	{
		 GetSearchMethod api = new GetSearchMethod();
		 api.request.contentType("application/json");
		 api.expectResponseStatus(HttpResponseStatusType.OK_200);
		 api.request.header("Authorization", "Bearer "+AccessToken.get());
		 Map<String, String> q = new HashMap<String, String>();
		 if(params.get("encoded").length()==0 || params.get("encoded") == null){
			 q.put("query", params.get("keyword"));
		}else{
			q.put("query", params.get("encoded"));
		 }
		 q.put("sort", "desc");
		 q.put("limit", "10");
		 api.request.queryParams(q);
		 Response res = api.callAPI();
		 Asset[] asset = res.as(Asset[].class);
		 Assert.assertEquals(asset.length, Integer.valueOf(params.get("count")).intValue(), "number of search results mismatch");
 		 String[] actualIds = new String[asset.length];
		 for(int i=0; i<asset.length;i++) {
			 actualIds[i]= asset[i].getasset_id();
		 }
         Assert.assertEquals(isSortedArray(actualIds, "desc"), true);	 
		 // pause to avoid 60 requests per minute limit on api calls
         pause(5);
	}
	
/**
 * This function helps in checking if an array is sorted in ascending or descending order	
 * @param ids array with list of values
 * @param order sort order
 * @return boolean if given array is sorted by order specified
 */
		
public boolean isSortedArray(String[] ids, String order){
	boolean ret = true;
	for(int i=0; i<ids.length-1;i++){
		if(order.equals("asc")){
		  if(! (Integer.valueOf(ids[i]).intValue() <= Integer.valueOf(ids[i+1]).intValue())) ret=false;
		}else if(order.equals("desc")){
			if(! (Integer.valueOf(ids[i]).intValue() >= Integer.valueOf(ids[i+1]).intValue())) ret=false;	
		}else{
			ret=false;
		}
	}
	return ret;	
}

/**
 * This function slits the string by given delimiter
 * @param ids a string of values with delimited by a separator
 * @param sep the delimiter
 * @return array, a list of separated string
*/
		
public String[] getIds(String ids, String sep){
	String[] list;
	list = ids.split(sep);
	return list;
}

}
