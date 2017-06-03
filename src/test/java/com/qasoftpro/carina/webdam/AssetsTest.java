package com.qasoftpro.carina.webdam;

import java.util.ArrayList;
import java.util.Arrays;
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

/**
 * This is test class for Asset API.
 * @author amaheshwari
 * @since June 2, 2017
 * 
 */

public class AssetsTest extends APITest{
	
	protected static final Logger LOGGER = Logger.getLogger(AssetsTest.class);
    // data files
	private static final String WEBDAM_XLS_DATA_SOURCE = "xls/webdam.xlsx";
	private static final String PATH_TO_GOLDEN_DATA_JSON = "testdata/assets.json";

	@Test(description = "Asset API to Get All")
	@XlsDataSourceParameters(path = WEBDAM_XLS_DATA_SOURCE, sheet = "assets", dsUid = "asset_id")
	public void testAssets()throws Exception
	{
		 GetAssetMethods api = new GetAssetMethods();
		 api.request.contentType("application/json");
		 api.expectResponseStatus(HttpResponseStatusType.OK_200);
		 api.request.header("Authorization", "Bearer "+AccessToken.get());
		 Response res = api.callAPI();
		 Asset[] asset = res.as(Asset[].class);
		 Assert.assertEquals(asset.length, 5, "number of assets mismatch");
		 JSONParser parser = new JSONParser();	
		 JSONArray testdata = (JSONArray) parser.parse(new FileReader(FileUtil.getFile(PATH_TO_GOLDEN_DATA_JSON)));
		 LOGGER.info("test data size:"+testdata.size());
		 JSONArray mJSONArray = new JSONArray();
		 JSONObject j;
		 for(Asset ass: asset){
			Map<Object, Object> m = new HashMap<Object, Object>();
			m.put("asset_id",ass.getasset_id());
			m.put("text",ass.getText());
			m.put("thumbnail", ass.getThumbnail());
			m.put("keywords", ass.getKeywords());
			m.put("date_modified", ass.getDateModified());
			m.put("date_created", ass.getDateCreated());
			j = new JSONObject();
			j.putAll(m);
			mJSONArray.add(j);
		 }
		 //ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		 //String json = ow.writeValueAsString(object);
		 //JSONCompareResult scr = JSONCompare.compareJSON(testdata.toJSONString(), mJSONArray.toJSONString(), JSONCompareMode.STRICT_ORDER);
		 JSONAssert.assertEquals(testdata.toJSONString(), mJSONArray.toJSONString(), JSONCompareMode.STRICT_ORDER);
		 //LOGGER.info(scr.getMessage());
	}
	
	
	@Test(dataProvider = "DataProvider", description = "Asset API Get By Id Test")
	@XlsDataSourceParameters(path = "xls/webdam.xlsx", sheet = "assets", dsUid = "asset_id", dsArgs = "Execute,asset_id, expectedJSON, expectedStatusCode, elementCount")
	public void testGetAssetById(String Execute, String asset_id, String expectedJSON, String expectedStatusCode, String elementCount) throws Exception
	{
		 GetAssetMethod api = new GetAssetMethod();
		 api.request.contentType("application/json");
		 api.request.header("Accept","application/json");
		 if(Integer.valueOf(expectedStatusCode).intValue() != 401)
			 api.request.header("Authorization", "Bearer "+AccessToken.get());
		 if(Integer.valueOf(expectedStatusCode).intValue() == 200){
		 api.expectResponseStatus(HttpResponseStatusType.OK_200);
		 }else if(Integer.valueOf(expectedStatusCode).intValue() == 404){
			 api.expectResponseStatus(HttpResponseStatusType.NOT_FOUND_404);
		 }else if(Integer.valueOf(expectedStatusCode).intValue() == 400){
			 api.expectResponseStatus(HttpResponseStatusType.BAD_REQUEST_400);
		 }else if(Integer.valueOf(expectedStatusCode).intValue() == 403){
			 api.expectResponseStatus(HttpResponseStatusType.FORBIDDEN_403);
		 }else if(Integer.valueOf(expectedStatusCode).intValue() == 401){
			 api.expectResponseStatus(HttpResponseStatusType.UNAUTHORIZED_401);
		 }else if(Integer.valueOf(expectedStatusCode).intValue() == 415){
			 api.expectResponseStatus(HttpResponseStatusType.UNSUPPORTED_MEDIA_TYPE_415);
		 }else{
			 api.expectResponseStatus(HttpResponseStatusType.OK_200);
		 }		 
		 api.replaceUrlPlaceholder("id", asset_id);
		 Response res = api.callAPI();
         if(res.statusCode() == Integer.valueOf(expectedStatusCode).intValue() && Integer.valueOf(expectedStatusCode).intValue() == 200 ){
        	 JSONAssert.assertEquals(expectedJSON, res.asString(),  JSONCompareMode.STRICT_ORDER);
        	 Asset[] asset = res.as(Asset[].class);
        	 Assert.assertEquals(asset.length, Integer.valueOf(elementCount).intValue());
         }else if(res.statusCode() == Integer.valueOf(expectedStatusCode).intValue() && Integer.valueOf(expectedStatusCode).intValue() == 401 ){
        		 Assert.assertEquals(res.jsonPath().getString("error"), "Unauthenticated.");   
         }else {
        	 Assert.fail("Get By Asset Id:"+asset_id+" failed. HTTP STATUS:"+res.statusCode()+"  Status Line:"+res.statusLine()+" Response Content Type:"+res.getContentType()+" Response:"+res.body().prettyPrint());
         }
	}	
}
