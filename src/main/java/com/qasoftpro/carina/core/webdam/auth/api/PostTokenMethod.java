package com.qasoftpro.carina.core.webdam.auth.api;

import com.qaprosoft.carina.core.foundation.api.AbstractApiMethodV2;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * This is helper class for Token API
 * @author amaheshwari
 * @since June 2, 1017
 */

public class PostTokenMethod extends AbstractApiMethodV2
{

	public PostTokenMethod()
	{
		super(null, "api/token/_post/rs.json", new Properties());
		replaceUrlPlaceholder("base_url", Configuration.getEnvArg("webdam_api_url"));
	}
}
