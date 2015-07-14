package org.helianto.home.controller;

/**
 * Search form.
 * 
 * @author mauriciofernandesdecastro
 */
public class SearchForm{

	private String  search = "";
	
	private String  searchString = "";
	
	private Integer qualifierValue = 0;
	
	private Integer pageNumber = 0;
	
	private Character qualifierValueChar ;
	
	private String qualifierValueString = "" ;
	
	private String orderString = "id";

	/**
	 * Construtor.
	 */
	public SearchForm() {
		super();
	}

	public SearchForm(String searchString, Integer qualifierValue) {
		super();
		this.searchString = searchString;
		this.qualifierValue = qualifierValue;
	}
	
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}

	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public Integer getQualifierValue() {
		return qualifierValue;
	}
	public void setQualifierValue(Integer qualifierValue) {
		this.qualifierValue = qualifierValue;
	}
	
	public String getOrderString() {
		return orderString;
	}
	public void setOrderString(String orderString) {
		this.orderString = orderString;
	}
	
	public Character getQualifierValueChar() {
		return qualifierValueChar;
	}
	
	public void setQualifierValueChar(Character qualifierValueChar) {
		this.qualifierValueChar = qualifierValueChar;
	}
	
	public String getQualifierValueString() {
		return qualifierValueString;
	}
	
	public void setQualifierValueString(String qualifierValueString) {
		this.qualifierValueString = qualifierValueString;
	}
	
	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	

	@Override
	public String toString() {
		return "SearchForm [search=" + searchString + ", qualifierValue="
				+ qualifierValue + "]";
	}

}