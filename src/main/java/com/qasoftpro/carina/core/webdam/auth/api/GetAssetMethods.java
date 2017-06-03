package com.qasoftpro.carina.core.webdam.auth.api;

import com.jayway.restassured.config.RestAssuredConfig;
import com.qaprosoft.carina.core.foundation.api.AbstractApiMethodV2;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import java.util.Properties;

/**
 * This is Get Asset API helper class.
 * @author amaheshwari
 * @since June 2, 2017
 */

public class GetAssetMethods  extends AbstractApiMethodV2{

	public GetAssetMethods()
	{
		super(null, null, new Properties());
		replaceUrlPlaceholder("base_url", Configuration.getEnvArg("webdam_api_url"));
		super.request.config(RestAssuredConfig.config().encoderConfig(com.jayway.restassured.config.EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)));
	}
}
