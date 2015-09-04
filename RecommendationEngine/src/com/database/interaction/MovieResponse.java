package com.database.interaction;

public class MovieResponse {
	String userid = null;
	String username = null;
	String comment = null;
	String rating = null;
	boolean watched = false;
	boolean wishlisted = false;
	Movie movie = null;
	User user = null;
	public MovieResponse(){
		
	}
	
	public void setUser(String id, String name){
		this.userid = id;
		this.username = name;
	}
	
	public void setUser(User u){
		this.user = u;
	}
	
	public User getUser(){
		return this.user;
	}
	
	public void setMovie(Movie m){
		this.movie = m;
	}
	
	public Movie getMovie(){
		return this.movie;
	}
	
	public void setComment(String comment){
		this.comment = comment;
	}
	
	public void setRating(String rating){
		this.rating = rating;
	}
	
	public String getuserid(){
		return this.userid;
	}
	
	public String getusername(){
		return this.username;
	}
	
	public String getcomment(){
		return this.comment;
	}
	
	public String getrating(){
		return this.rating;
	}
	
	public void hasWatched(){
		this.watched = true;
	}
	
	public boolean didWatch(){
		return this.watched;
	}
	
	public void hasWishlisted(){
		this.wishlisted = true;
	}
	
	public boolean didWishlist(){
		return this.wishlisted;
	}
}
