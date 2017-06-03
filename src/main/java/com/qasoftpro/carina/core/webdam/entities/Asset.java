package com.qasoftpro.carina.core.webdam.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * This is a pojo class for Asset entity.
 * @author amaheshwari
 * @since June 2, 2017
 * @version 1.0
 */


@JsonIgnoreProperties(ignoreUnknown = true)
public class Asset {
	private String asset_id, text, thumbnail;
	private int date_modified, date_created;
	private List<String> keywords = new ArrayList<String>();
   
	public Asset(){}
	
	@JsonSetter("asset_id")
	public void setasset_id(String asset_id){
		this.asset_id=asset_id;
	}

	
	public String getasset_id(){
		return this.asset_id;
	}
	
	@JsonSetter("text")
	public void settext(String text){
		this.text=text;
	}
	
	public String getText(){
		return this.text;
	}
	
	@JsonSetter("thumbnail")
	public void setThumbnail(String thumbnail){
		this.thumbnail=thumbnail;
	}
	
	public String getThumbnail(){
		return this.thumbnail;
	}
	
	@JsonSetter("keywords")
	public void setKeywords(List<String> keywords){
		this.keywords=keywords;
	}
	
	public List<String> getKeywords(){
		return this.keywords;
	}
    
	@JsonSetter("date_modified")
	public void setdate_modified(int date_modified){
		this.date_modified=date_modified;
	}
	
	public int getDateModified(){
		return this.date_modified;
	}
	
	@JsonSetter("date_created")
	public void setdate_created(int date_created){
		this.date_created=date_created;
	}
	
	public int getDateCreated(){
		return this.date_created;
	}
}
