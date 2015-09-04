package com.database.interaction;

import java.util.List;

public class Product {

	String label = null;
	String id = null;
	
	public Product(String id)
	{
	 	this.id=id;
	}
	
	public void setLabel(String x)
	{
		this.label = x;
	}
	
	public void setId(String x)
	{
		this.id = x;
	}
	
	public String getID()
	{
		return this.id;
	}
	
	public String getTitle(){
		return RecommendationDBQueries.getProductProperty("PRODUCT", this.id, "title",DBConnection.GDBService);
	} 
	
	public String getPrice(){
		return RecommendationDBQueries.getProductProperty("PRODUCT", this.id, "price",DBConnection.GDBService);
	}
	
	public String getImageURL(){
		return RecommendationDBQueries.getProductProperty("PRODUCT", this.id, "imageUrlStr",DBConnection.GDBService);
	}
	
	public String getDescription(){
		return RecommendationDBQueries.getProductProperty("PRODUCT", this.id, "description",DBConnection.GDBService);
	}
	
	public String getProductBrand(){
		return RecommendationDBQueries.getProductProperty("PRODUCT", this.id, "productBrand",DBConnection.GDBService);
	}
	
	public String getInStock(){
		return RecommendationDBQueries.getProductProperty("PRODUCT", this.id, "inStock",DBConnection.GDBService);
	}
	
	public String getOffers(){
		return RecommendationDBQueries.getProductProperty("PRODUCT", this.id, "offers",DBConnection.GDBService);
	}
	
	public String getDiscounts(){
		return RecommendationDBQueries.getProductProperty("PRODUCT", this.id, "discount",DBConnection.GDBService);
	}
	
	public String getCODAvailability(){
		return RecommendationDBQueries.getProductProperty("PRODUCT", this.id, "codAvailable",DBConnection.GDBService);
	}
	
	public List<String> getAll(){
		return RecommendationDBQueries.getAllProductProperty("PRODUCT", this.id, DBConnection.GDBService);
	}
	
	
}
