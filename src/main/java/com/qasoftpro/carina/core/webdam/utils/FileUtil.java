package com.qasoftpro.carina.core.webdam.utils;

import java.io.File;
import java.net.URLDecoder;

/**
 * This utility class for file related operations.
 * @author amaheshwari
 * @since June 2, 2017
 */
public class FileUtil {

	
	/**
	 * This function gets the file from system
	 * @param path it is the file path
	 * @return File it returns the file
	 * @throws Exception
	 */
	
	public static File getFile(String path) throws Exception
	{
		path = URLDecoder.decode(ClassLoader.getSystemResource(path).getFile(), "utf-8");
		return new File(path);
	}
}
