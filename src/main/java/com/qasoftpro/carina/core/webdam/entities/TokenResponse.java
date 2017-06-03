package com.qasoftpro.carina.core.webdam.entities;

/**
 * This is pojo class for Toekn Response entity
 * @author amaheshwari
 * @since June 2, 2017
 * @version 1.0
 *
 */
public class TokenResponse {
	
	private String type, token;
	private int expiresTime;
	
	public TokenResponse(){}
	
	public void setTokenType(String type){
		this.type=type;
	}
	
	public String getTokenType(){
		return this.type;
	}
	
	public void setExpiresIn(int seconds){
		this.expiresTime=seconds;
	}
	
	public int getExpiresIn(){
		return this.expiresTime;
	}
	
	public void setAccessToken(String token){
		this.token=token;
	}
    
	public String getAccessToken(){
		return this.token;
	}
}
