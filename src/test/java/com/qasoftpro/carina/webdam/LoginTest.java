package com.qasoftpro.carina.webdam;

//import org.junit.Assert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.testng.annotations.Test;

import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.qaprosoft.apitools.validation.JsonCompareKeywords;
import com.qaprosoft.carina.core.foundation.APITest;
import com.qaprosoft.carina.core.foundation.http.HttpResponseStatusType;
import com.qaprosoft.carina.core.foundation.performance.Timer;
import com.qasoftpro.carina.core.webdam.auth.api.GetLoginMethod;
import com.qasoftpro.carina.core.webdam.auth.api.GetLogoutMethod;
import com.qasoftpro.carina.core.webdam.auth.api.PostTokenMethod;
import com.qasoftpro.carina.core.webdam.utils.AccessToken;
import org.testng.Assert;
import static com.jayway.restassured.RestAssured.given;

/**
 * This class is for login related tests. 
 * @author Amit Maheshwari
 * @since June 2, 1017
 */
public class LoginTest extends APITest
{
	
	@Test(description = "Logout API with valid token")
	public void testLogoutValidToken()throws InterruptedException
	{
		 GetLogoutMethod api = new GetLogoutMethod();
		 api.request.contentType("application/json");
		 api.expectResponseStatus(HttpResponseStatusType.OK_200);
		 api.request.header("Authorization", "Bearer "+AccessToken.get());
		 Response res = api.callAPI();
		 Assert.assertEquals(res.jsonPath().getString("logged_in"), false);
		 Assert.assertEquals(res.getContentType(), "application/json");
	}
	
	@Test(description = "Logout API with long expired token")
	public void testLogoutLongExpiredToken()throws InterruptedException
	{
		 GetLogoutMethod api = new GetLogoutMethod();
		 api.expectResponseStatus(HttpResponseStatusType.UNAUTHORIZED_401);
		 api.request.header("Accept", "application/json");
		 api.request.header("Authorization", "Bearer "+"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjMyNzc4MmU2NDAxNWUzYjUzZjQ1YWEyNjA3YzdhYzRhNjkxYzM4NGE1YTBhYTNhNWUxMjEyZGY5MmQ0YTQyNjA3NjFhYmZhODA5MjAxNGQ3In0.eyJhdWQiOiI0IiwianRpIjoiMzI3NzgyZTY0MDE1ZTNiNTNmNDVhYTI2MDdjN2FjNGE2OTFjMzg0YTVhMGFhM2E1ZTEyMTJkZjkyZDRhNDI2MDc2MWFiZmE4MDkyMDE0ZDciLCJpYXQiOjE0OTYzMzAyMDAsIm5iZiI6MTQ5NjMzMDIwMCwiZXhwIjoxNDk2MzMzODAwLCJzdWIiOiIiLCJzY29wZXMiOltdfQ.GlFrlqPVP49nlGkNzNH5H1kz6q3AqwvP3a7krXulPydG-jywMELg4KjZe4gEmf_Zv0_R5guB-O0heymXjhAugQursmSYOmvI6f6E3IuYk6L-K2xt-ZHmvQbQEMWvvdTkJdqTLTvZChk3LIIYPUK2D2vVrZGvbCLOuC5vVFOUnF-B8iANgkHKk891dJHTGNQneCNfHMpOVop3oKoltZIgWIwXqCGhhMMXG8z7Ij9mxXOGINg3zIYr1wCH0gZWwDlIzLWkO_0852mKT9CbnzcO4G9lh0I52DKXtD4M3-inTn3Mxo3xXilgqBhsRr5-UwpMlRXy6Ei4UCEGK0ou2YSRpYOZ3xZfGOPgFbdbbU1OC7xd3y8WATfddUC8oGs3BbCaGDGBrtmLI4Q8gbeBKMX7W_pNF8W1c5j9JSk7NRywATvLMrmKVId-fUKpaXqfRRlPtDETQEdBKHrk-lsgxgDUM9oZO9y-HdLqV7kNcneDgfC0mU2t2PROdW2GErpYy7-alfjqXyVzl7L2FsZH74isY45zmg5LmGQfplF_7HApE640xjEbg0AYPlLirNgd1jsHnUbT7R113eM9F9Fwdl9iSPJ6BWAoDIhv4wHwwxDUsarW1_FQYPpyMJqG3dSjVZ_gl9Duem_Gf04tzUvoLmgArJDX0SJLYjVZUeZ6y4-VsEY");
		 Response res = api.callAPI();
		 Assert.assertEquals(res.jsonPath().getString("error"), "Unauthenticated.");
		 Assert.assertEquals(res.getContentType(), "application/json");
	}

	
	@Test(description = "Login API with valid token")
	public void testLoginValidToken()throws InterruptedException
	{
		 GetLoginMethod api = new GetLoginMethod();
		 api.expectResponseStatus(HttpResponseStatusType.OK_200);
		 api.request.header("Authorization", "Bearer "+AccessToken.get());
		 Response res = api.callAPI();
		 Assert.assertEquals(res.jsonPath().getString("logged_in"), true);
		 Assert.assertEquals(res.getContentType(), "application/json");
	}
	
	@Test(description = "Login API with long expired token")
	public void testLoginExpiredTokenDefaultAccept()throws InterruptedException
	{
		 GetLoginMethod api = new GetLoginMethod();
		 api.expectResponseStatus(HttpResponseStatusType.UNAUTHORIZED_401);
		 api.request.config(RestAssuredConfig.config().encoderConfig(com.jayway.restassured.config.EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)));
		 api.request.header("Content-Type","application/json");
		 api.request.header("Authorization", "Bearer "+"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjMyNzc4MmU2NDAxNWUzYjUzZjQ1YWEyNjA3YzdhYzRhNjkxYzM4NGE1YTBhYTNhNWUxMjEyZGY5MmQ0YTQyNjA3NjFhYmZhODA5MjAxNGQ3In0.eyJhdWQiOiI0IiwianRpIjoiMzI3NzgyZTY0MDE1ZTNiNTNmNDVhYTI2MDdjN2FjNGE2OTFjMzg0YTVhMGFhM2E1ZTEyMTJkZjkyZDRhNDI2MDc2MWFiZmE4MDkyMDE0ZDciLCJpYXQiOjE0OTYzMzAyMDAsIm5iZiI6MTQ5NjMzMDIwMCwiZXhwIjoxNDk2MzMzODAwLCJzdWIiOiIiLCJzY29wZXMiOltdfQ.GlFrlqPVP49nlGkNzNH5H1kz6q3AqwvP3a7krXulPydG-jywMELg4KjZe4gEmf_Zv0_R5guB-O0heymXjhAugQursmSYOmvI6f6E3IuYk6L-K2xt-ZHmvQbQEMWvvdTkJdqTLTvZChk3LIIYPUK2D2vVrZGvbCLOuC5vVFOUnF-B8iANgkHKk891dJHTGNQneCNfHMpOVop3oKoltZIgWIwXqCGhhMMXG8z7Ij9mxXOGINg3zIYr1wCH0gZWwDlIzLWkO_0852mKT9CbnzcO4G9lh0I52DKXtD4M3-inTn3Mxo3xXilgqBhsRr5-UwpMlRXy6Ei4UCEGK0ou2YSRpYOZ3xZfGOPgFbdbbU1OC7xd3y8WATfddUC8oGs3BbCaGDGBrtmLI4Q8gbeBKMX7W_pNF8W1c5j9JSk7NRywATvLMrmKVId-fUKpaXqfRRlPtDETQEdBKHrk-lsgxgDUM9oZO9y-HdLqV7kNcneDgfC0mU2t2PROdW2GErpYy7-alfjqXyVzl7L2FsZH74isY45zmg5LmGQfplF_7HApE640xjEbg0AYPlLirNgd1jsHnUbT7R113eM9F9Fwdl9iSPJ6BWAoDIhv4wHwwxDUsarW1_FQYPpyMJqG3dSjVZ_gl9Duem_Gf04tzUvoLmgArJDX0SJLYjVZUeZ6y4-VsEY");		 Response res = api.callAPI();
		 Assert.assertEquals(res.jsonPath().getString("error"), "Unauthenticated");
		 Assert.assertEquals(res.getContentType(), "application/json");
	}

	@Test(description = "Login API with recently expired token")
	public void testLoginRecentlyExpiredToken()throws InterruptedException
	{
		 GetLoginMethod api = new GetLoginMethod();
		 api.expectResponseStatus(HttpResponseStatusType.UNAUTHORIZED_401);
		 api.request.header("Content-Type","application/json");
		 api.request.header("Accept","application/json");
		 api.request.header("Authorization", "Bearer "+"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjMyNzc4MmU2NDAxNWUzYjUzZjQ1YWEyNjA3YzdhYzRhNjkxYzM4NGE1YTBhYTNhNWUxMjEyZGY5MmQ0YTQyNjA3NjFhYmZhODA5MjAxNGQ3In0.eyJhdWQiOiI0IiwianRpIjoiMzI3NzgyZTY0MDE1ZTNiNTNmNDVhYTI2MDdjN2FjNGE2OTFjMzg0YTVhMGFhM2E1ZTEyMTJkZjkyZDRhNDI2MDc2MWFiZmE4MDkyMDE0ZDciLCJpYXQiOjE0OTYzMzAyMDAsIm5iZiI6MTQ5NjMzMDIwMCwiZXhwIjoxNDk2MzMzODAwLCJzdWIiOiIiLCJzY29wZXMiOltdfQ.GlFrlqPVP49nlGkNzNH5H1kz6q3AqwvP3a7krXulPydG-jywMELg4KjZe4gEmf_Zv0_R5guB-O0heymXjhAugQursmSYOmvI6f6E3IuYk6L-K2xt-ZHmvQbQEMWvvdTkJdqTLTvZChk3LIIYPUK2D2vVrZGvbCLOuC5vVFOUnF-B8iANgkHKk891dJHTGNQneCNfHMpOVop3oKoltZIgWIwXqCGhhMMXG8z7Ij9mxXOGINg3zIYr1wCH0gZWwDlIzLWkO_0852mKT9CbnzcO4G9lh0I52DKXtD4M3-inTn3Mxo3xXilgqBhsRr5-UwpMlRXy6Ei4UCEGK0ou2YSRpYOZ3xZfGOPgFbdbbU1OC7xd3y8WATfddUC8oGs3BbCaGDGBrtmLI4Q8gbeBKMX7W_pNF8W1c5j9JSk7NRywATvLMrmKVId-fUKpaXqfRRlPtDETQEdBKHrk-lsgxgDUM9oZO9y-HdLqV7kNcneDgfC0mU2t2PROdW2GErpYy7-alfjqXyVzl7L2FsZH74isY45zmg5LmGQfplF_7HApE640xjEbg0AYPlLirNgd1jsHnUbT7R113eM9F9Fwdl9iSPJ6BWAoDIhv4wHwwxDUsarW1_FQYPpyMJqG3dSjVZ_gl9Duem_Gf04tzUvoLmgArJDX0SJLYjVZUeZ6y4-VsEY");
		 Response res = api.callAPI();
		 Assert.assertEquals(res.jsonPath().getString("error"), "Unauthenticated.");
		 Assert.assertEquals(res.getContentType(), "application/json");
	}
	
	@Test(description = "Login API with junk value in token")
	public void testLoginJunkToken()throws InterruptedException
	{
		 GetLoginMethod api = new GetLoginMethod();
		 api.request.header("Accept", "application/json");
		 api.request.header("Authorization", "Bearer "+"dlkfjkjfjfjsfjsjfjfsjfsjl");
		 Response res = api.callAPI();
		 Assert.assertEquals(res.statusCode(), 500);
		 Assert.assertEquals(res.getContentType(), "application/json");
	}
	
	@Test(description = "token api status code check")
	public void testTokenPost() throws InterruptedException
	{
			PostTokenMethod api = new PostTokenMethod();
			api.request.contentType("application/json");
			//check for success API call and default content type for response
			api.setBodyContent("{\"grant_type\":\"client_credentials\",\"client_id\":\"4\",\"client_secret\":\"4ovGa5yXfHnWR47wGRVUfKlDTBxC3WQtnkmO5sgs\",\"scope\":\"\"}");
			Response res = api.callAPI();
			Assert.assertEquals(res.statusCode(), HttpResponseStatusType.CREATED_201);
			Assert.assertEquals(res.getContentType(), "application/json; charset=UTF-8");
			// check for default content type for error condition
			api.setBodyContent("{\"grant_type\":\"client_credentials1\",\"client_id\":\"4\",\"client_secret\":\"4ovGa5yXfHnWR47wGRVUfKlDTBxC3WQtnkmO5sgs\",\"scope\":\"\"}");
			res = api.callAPI();
			Assert.assertEquals(res.contentType(), "application/json");
			// missing attribute in pay load
			api.setBodyContent("{\"grant_type\":\"client_credentials\",\"client_secret\":\"4ovGa5yXfHnWR47wGRVUfKlDTBxC3WQtnkmO5sgs\",\"scope\":\"\"}");
			res = api.callAPI();
			Assert.assertEquals(res.contentType(), "application/json");
            //null value for an attribute
			api.setBodyContent("{\"grant_type\":\"client_credentials1\",\"client_id\":null,\"client_secret\":\"4ovGa5yXfHnWR47wGRVUfKlDTBxC3WQtnkmO5sgs\",\"scope\":\"\"}");
			res = api.callAPI();
			Assert.assertEquals(res.contentType(), "application/json");
	}
}