package com.database.interaction;

import java.util.ArrayList;
import java.util.List;

public class Book{
	
	String id = null;
	String title = null;
	String publicationYear = null;
	String author = null;
	String publisher = null;
	String thumbnail = null;
	String mediumimage = null;
	String largeimage = null;
	double avgrating = 0;
	String noofratings = null;
	String genre = null;
	
	public Book(){
		
	}
	
	public Book(String id){
		this.id = id;
	}
	
	public void setId(String x){
		this.id = x;
	}
	
	public String getid(){
		return this.id;
	}
	
	public String getTitle(){
		return RecommendationDBQueries.getProductProperty("BOOK", this.id, "title");
	}
	
	public String getPublicationYear(){
		return RecommendationDBQueries.getProductProperty("BOOK", this.id, "publicationYear");
	}
	
	public String getAuthor(){
		return RecommendationDBQueries.getProductProperty("BOOK", this.id, "author");
	}
	//ISBN:ID(BOOK),title,author,publicationYear,publisher,imageURLS,imageURLM,imageURLL
	public String getPublisher(){
		return RecommendationDBQueries.getProductProperty("BOOK", this.id, "publisher");
	}
	
	public String getThumbNail(){
		return RecommendationDBQueries.getProductProperty("BOOK", this.id, "imageURLS");
	}
	
	public String getSmallImageURL(){
		return RecommendationDBQueries.getProductProperty("BOOK", this.id, "imageURLM");
	}
	
	public String getLargeImageURL(){
		return RecommendationDBQueries.getProductProperty("BOOK", this.id, "imageURLL");
	}
	
	public String getAvgRating(){
		return Double.toString(RecommendationDBQueries.getProductAvgRating("BOOK", this.id, "read"));
	}
	
	public String getNoOfRatings(){
		return RecommendationDBQueries.getProductNoOfRatings("BOOK", this.id, "read");
	}
	
	public ProductResponse getUserResponse(String userid){
		return RecommendationDBQueries.getUserResponseToProduct(userid, "BOOK", this.id, "read");
	}
	
	public List<ProductResponse> getUserFollowingCircleResponse(String userid){
		return RecommendationDBQueries.getProductResponseFromTheFollowingCircle(userid, "BOOK", this.id, "read");
	}
	
	public boolean checkIfUserHasRead(String userid, String relation){
		return RecommendationDBQueries.checkConnectionExistence(userid, "BOOK", this.id, relation);
	}
	
	public List<Book> getSuggestedBooks(String userid){
		List<String> productList = RecommendationDBQueries.getSuggestedProductsForUser(userid, "BOOK", this.id, "read");
		List<Book> bookList = new ArrayList<Book>();
		for(String id: productList){
			Book b = new Book(id);
			bookList.add(b);
		}
		return bookList;
	}
}
