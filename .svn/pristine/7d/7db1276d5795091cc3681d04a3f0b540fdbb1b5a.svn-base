package com.database.interaction;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class Movie {
	String id = null;
	String title = null;
	String releaseyear = null;
	double avgrating = 0;
	String noofratings = null;
	String genre = null;
	
	public Movie(){
		
	}
	
	public Movie(String id){
		this.id = id;
	}
	
	public void setId(String x){
		this.id = x;
	}
	
	public String getid(){
		return this.id;
	}
	
	public String getTitle(){
		return RecommendationDBQueries.getProductProperty("MOVIE", this.id, "Title");
	}
	//movieId:ID(MOVIE),title,year,genres
	public String getGenre(){
		return RecommendationDBQueries.getProductProperty("MOVIE", this.id, "genre");
	}
	
	public List<String> getActors(){
		return RecommendationDBQueries.getProductConnection("MOVIE", this.id, "ACTOR", "acted");
	}
	
	public List<String> getTags(){
		return RecommendationDBQueries.getTagConnection("MOVIE", this.id, "TAG", "tag");
	}
	
	public List<String> getLanguages(){
		return RecommendationDBQueries.getProductConnection("MOVIE", this.id, "LANGUAGE", "language");
	}
	
	public String getRunTime(){
		return RecommendationDBQueries.getProductProperty("MOVIE", this.id, "Runtime");
	}
	
	public String getReleaseDate(){
		return RecommendationDBQueries.getProductProperty("MOVIE", this.id, "Release_date");
	}
	
	public String getPlot(){
		return RecommendationDBQueries.getProductProperty("MOVIE", this.id, "Plot");
	}
	
	public String getWriters(){
		return RecommendationDBQueries.getProductProperty("MOVIE", this.id, "Writer");
	}
	
	public boolean hasUpvoted(String tag,String userid,String movieID){
		return RecommendationDBQueries.getUserVoteResponse("MOVIE", movieID, "TAG", tag, "tag", "upvote", userid);
	}
	
	public boolean hasDownvoted(String tag,String userid,String movieID){
		return RecommendationDBQueries.getUserVoteResponse("MOVIE", movieID, "TAG", tag, "tag", "downvote", userid);
	}
	
	public String getReleaseYear(){
		return RecommendationDBQueries.getProductProperty("MOVIE", this.id, "Year");
	}
	
	public String getAvgRating(){
		return Double.toString(RecommendationDBQueries.getProductAvgRating("MOVIE", this.id, "watched"));
	}
	
	public String getNoOfRatings(){
		return RecommendationDBQueries.getProductNoOfRatings("MOVIE", this.id, "watched");
	}
	
	public List<Movie> getSuggestedMovies(String userid){
		List<String> productList = RecommendationDBQueries.getSuggestedProductsForUser(userid, "MOVIE", this.id, "watched");
		List<Movie> movieList = new ArrayList<Movie>();
		for(String id: productList){
			Movie m = new Movie(id);
			movieList.add(m);
		}
		return movieList;
	}
	
	public String getImageURL(){
		return RecommendationDBQueries.getProductProperty("MOVIE", this.id, "Poster");
	}
	public ProductResponse getUserResponse(String userid){
		return RecommendationDBQueries.getUserResponseToProduct(userid, "MOVIE", this.id, "watched");
	}
	
	public String getPosterURLFromIMDB() throws JsonIOException, JsonSyntaxException, IOException{
        String sURL = "http://www.imdbapi.com/?t='"+this.getTitle()+"'"; //just a string
        sURL=sURL.replace(" ", "%20");
       //  sURL += this.getTitle();
      // Connect to the URL using java's native library
      URL url = new URL(sURL);
      
      HttpURLConnection request = (HttpURLConnection) url.openConnection();
     request.connect();

      // Convert to a JSON object to print data
      JsonParser jp = new JsonParser(); //from gson
      JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
      JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object. 
      String posterURL = rootobj.get("Poster").getAsString(); //just grab the zipcode
        return posterURL;
  }

}
