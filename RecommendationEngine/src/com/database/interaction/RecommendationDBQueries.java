package com.database.interaction;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.io.fs.DefaultFileSystemAbstraction;
import org.neo4j.kernel.StoreLocker;
import org.neo4j.kernel.impl.util.StringLogger;

public class RecommendationDBQueries {
	/******************** Variable declarations ***************************/
	static String DB_PATH;
	static GraphDatabaseService GDBServices=null;
	static boolean isConnectionSet = false;

	static HashMap<Node,Boolean> Nodes = new HashMap<Node,Boolean>();
	static HashMap<Link,Boolean> Links = new HashMap<Link,Boolean>();
	static HashMap<Link,Boolean> Relationships = new HashMap<Link,Boolean>();
	
	static ArrayList<HashMap<String,String>> tNodes = new ArrayList<HashMap<String,String>>();
	static ArrayList<HashMap<String,String>> tLinks = new ArrayList<HashMap<String,String>>();
	
	static String jsonString = "";
	
	static Metadata mtdt = new Metadata();
	
	
	/***************************** Default constructor ******************************/
	public RecommendationDBQueries(GraphDatabaseService gdbs){
		GDBServices = gdbs;
	}
	
	/******************************* Acquire db connection *************************/
	/*public static void DBConnection(){
		DB_PATH = "C:\\Users\\tulika.mithal\\Desktop\\neo4j-community-2.2.3-windows\\neo4j-community-2.2.3\\bin\\movie2.db";
		StoreLocker lock = new StoreLocker(new DefaultFileSystemAbstraction());
        lock.checkLock(new File(DB_PATH));
        try {
            lock.release();
            GDBServices = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(DB_PATH).setConfig( GraphDatabaseSettings.node_keys_indexable, "ID,SEARCHABLE" ).
            	    setConfig( GraphDatabaseSettings.node_auto_indexing, "true" ).
            	    setConfig( GraphDatabaseSettings.relationship_auto_indexing, "true" ).newGraphDatabase();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
		
		isConnectionSet = true;
	}*/
	

	public boolean userAuthentication(String username, String password){
		//if(!isConnectionSet)	DBConnection();
		int count = 0;
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			String cypher = "MATCH (u:User {"+mtdt.getuniqueproperty("User")+" : '" + username 
								+ "', password : '" + password
								+ "'}) RETURN u";
			System.out.println("CYPHER :: " + cypher);
			ExecutionResult result = engine.execute(cypher);
			
			for(Map<String,Object> row : result){
				count++;	
			}
			tx.success();
			tx.close();
		}
				System.out.println(count);
		if(count == 1){
			System.out.println("Authenticated");
			return true;
		}
		return false;
	}
	
	/*********************** Autocomplete suggestor ******************************/
	public static String autoCompleteSuggestor(String filter, String prefix,GraphDatabaseService GDBServices) throws IOException{
		/*if(!isConnectionSet){
			System.out.println("Obtaining connection......");
			DBConnection();
		}*/
		System.out.println("Fetching suggestions for prefix " + prefix + " in " + filter);
		String suggestions = "";
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			String cypher = null;
			if(filter.equals("ALL"))	cypher = "MATCH (m) WHERE m.SEARCHABLE =~ '(?i)"+ prefix +".*' RETURN m.ID  as id, m.SEARCHABLE as search, labels(m) as labels LIMIT 5";
			else cypher = "MATCH (m:"+ filter +") WHERE m."+ mtdt.getsearchableproperty(filter) +" =~ '(?i)"+ prefix +".*' RETURN m."+ mtdt.getuniqueproperty(filter) +"  as id, m."+ mtdt.getsecondaryidentifier(filter) +"  as sid, m."+ mtdt.getsearchableproperty(filter) +" as search, labels(m) as labels LIMIT 5";
			System.out.println("Running this query :: " + cypher);
			ExecutionResult result = engine.execute(cypher);
			suggestions = "[";
			for(Map<String,Object> row : result){
				suggestions += "{\"searchable\":\""+ row.get("search") + "\",\"id\":\""+ row.get("id") + "\",\"label\":\"" + row.get("labels") + "\",\"sid\":\"" + row.get("sid") + "\"},";
			}
			if(suggestions.endsWith(",")){
				suggestions = suggestions.substring(0, suggestions.length()-1);
			}
			suggestions += "]";
			tx.success();
			tx.close();
		}
		System.out.println(suggestions);
		return suggestions;
	}
	
	/*********************** Autocomplete suggestor ******************************/
	public static String autoCompleteSuggestorLabel(String filter,String prefix) throws IOException{
		/*if(!isConnectionSet){
			System.out.println("Obtaining connection......");
			DBConnection();
		}*/
		//System.out.println("Fetching suggestions for prefix " + prefix + " in " + filter);
		String suggestions = "";
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			String cypher = "MATCH (m:"+ filter +") WHERE m."+ mtdt.getsearchableproperty(filter) +" =~ '(?i)"+ prefix +".*' RETURN m."+ mtdt.getuniqueproperty(filter) +"  as id LIMIT 5";
			System.out.println("Running this query :: " + cypher);
			ExecutionResult result = engine.execute(cypher);
			suggestions = "[";
			for(Map<String,Object> row : result){
				suggestions += "{\"id\":\""+ row.get("id") + "\"},";
			}
			if(suggestions.endsWith(",")){
				suggestions = suggestions.substring(0, suggestions.length()-1);
			}
			suggestions += "]";
			tx.success();
			tx.close();
		}
		System.out.println(suggestions);
		return suggestions;
	}
	/********************* Find node in the database *********************************************/
	public static String findInDatabase(String nodefilter, String nodeid) {
	/*	if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			String cypher = null;
			if(nodefilter.equals("ALL")) return getAllData();
			else cypher = "MATCH (m:" + nodefilter + "{"+ mtdt.getuniqueproperty(nodefilter) +": '" + nodeid + "'}) RETURN m";
			System.out.println("CYPHER ::" + cypher);
			ExecutionResult result = engine.execute(cypher);
			updateNodes(result);
			writetojson();
			tx.success();
			tx.close();
		}
		return jsonString;
	}
	
	/***************************Get user details ******************************************/
	public static User getUserDetails(String id){
		User u = new User();
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			//Fetch user details
			String cypher = "MATCH (m:User {"+mtdt.getuniqueproperty("User")+" : '"+id+"'}) RETURN m";
			System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				Node node = (Node)row.get("m");
				u.setId((String)node.getProperty(mtdt.getuniqueproperty("User")));
				break;
			}
		}
		return u;
	}
	
	/************************get user watched/read products etc *******************************/
	public static String getUserNoOfConnections(String userid,String productlabel, String relation){
		String count = null;
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			String cypher = "MATCH (m:User {"+mtdt.getuniqueproperty("User")+" : '"+userid+"'})-[:"+relation+"]-(:"+productlabel+") RETURN count(*) AS nr";
			System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				Long nr = (Long) row.get("nr");
				count = nr.toString();
				break;
			}
		}
		return count;
	}
	/************************get user watched movies list **************************************/
	public static List<MovieResponse> getUserWatchedMoviesList(String userid){
		List<MovieResponse> watchedMovies = new ArrayList<MovieResponse>();
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			//Fetch user details
			String cypher = "MATCH (a:User {"+mtdt.getuniqueproperty("User")+" : '"+userid+"'})-[w:watched]->(m) RETURN w,m\n";
			System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				MovieResponse mr = new MovieResponse();
				Node node = (Node)row.get("m");
				Movie m = new Movie();
				m.setId((String)node.getProperty(mtdt.getuniqueproperty("MOVIE")));
				mr.setMovie(m);
				Relationship rel = (Relationship)row.get("w");
				mr.setComment((String)rel.getProperty("comment"));
				mr.setRating((String)rel.getProperty("rating"));
				watchedMovies.add(mr);
			}
		}
		return watchedMovies;
	}
	
	/************************get user connected products list **************************************/
	public static List<String> getUserConnectedProductsList(String userid, String productlabel, String relation){
		List<String> products = new ArrayList<String>();
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			//Fetch user details
			String cypher = "MATCH (a:User {"+mtdt.getuniqueproperty("User")+" : '"+userid+"'})-[w:"+relation+"]->(m:"+productlabel+") RETURN m."+mtdt.getuniqueproperty(productlabel)+" AS id\n";
			System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				products.add((String)row.get("id"));
			}
		}
		return products;
	}
	
	/************************get user watched movies in genre list **************************************/
	public static List<MovieResponse> getUserWatchedMoviesInGenreList(String userid, String genre){
		List<MovieResponse> watchedMoviesGenre = new ArrayList<MovieResponse>();
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			//Fetch user details
			String cypher = "MATCH (a:User {"+mtdt.getuniqueproperty("User")+" : '"+userid+"'})-[w:watched]->(m)-[:genre]->(g:GENRE {"+mtdt.getuniqueproperty("GENRE")+":'"+genre+"'}) RETURN w,m\n";
			System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				MovieResponse mr = new MovieResponse();
				Node node = (Node)row.get("m");
				Movie m = new Movie();
				m.setId((String)node.getProperty(mtdt.getuniqueproperty("MOVIE")));
				mr.setMovie(m);
				Relationship rel = (Relationship)row.get("w");
				mr.setComment((String)rel.getProperty("comment"));
				mr.setRating((String)rel.getProperty("rating"));
				watchedMoviesGenre.add(mr);
			}
		}
		return watchedMoviesGenre;
	}
	
	/************************get movies in genre list **************************************/
	public static List<Movie> getMoviesInGenreList(String genre){
		List<Movie> moviesGenre = new ArrayList<Movie>();
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			//Fetch user details
			String cypher = "MATCH (m)-[:genre]->(g:GENRE {"+mtdt.getuniqueproperty("GENRE")+":'"+genre+"'})\n";
			cypher	+= "WITH m\n";
			cypher += "OPTIONAL MATCH (u)-[w:watched]->(m)\n";
			cypher += "RETURN m,avg(toInt(w.rating)) as avgrating ORDER BY avgrating DESC\n";
			System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				MovieResponse mr = new MovieResponse();
				Node node = (Node)row.get("m");
				Movie m = new Movie();
				m.setId((String)node.getProperty(mtdt.getuniqueproperty("MOVIE")));
				moviesGenre.add(m);
			}
		}
		return moviesGenre;
	}
	
	/*****************************Get movie suggestions from following circle ********************************/
	public static List<Movie> getMovieSuggestions(String userid){
		List<Movie> suggestions = new ArrayList<Movie>();
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		String genre=null;
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			//Fetch user details
			String cypher1 = "match (u:User{ID:'"+userid+"'})-[:watched]->(m:MOVIE)-[:genre]->(g:GENRE) return g,count(*) AS c ORDER BY c DESC limit 1";
			ExecutionResult result1 = engine.execute(cypher1);
			for(Map<String,Object> row : result1){
				Node node = (Node)row.get("g");
				 genre = node.getProperty("genre").toString();
			}
			
			String cypher2  ="match(g:GENRE{genre: '"+genre+"'})-[:genre]-(m:MOVIE),(u:User {ID:'"+userid+"'}) OPTIONAL MATCH (m)-[r:watched]-(f) where NOT((u)-[:watched|waitlisted]->(m)) return m,avg(toInt(r.rating)) as w  order by w DESC limit 6";
			ExecutionResult result2 = engine.execute(cypher2);
			for(Map<String,Object> row : result2){
				MovieResponse mr = new MovieResponse();
				Node node = (Node)row.get("m");
				Movie m = new Movie();
				m.setId((String)node.getProperty(mtdt.getuniqueproperty("MOVIE")));
				suggestions.add(m);
			}
			/*String cypher = "MATCH (g:GENRE)<-[:genre]-(m:MOVIE)<-[w:watched]-(f:User)<-[:follows]-(u:User {"+mtdt.getuniqueproperty("User")+": '"+userid+"'})\n";
			cypher	+= "WHERE NOT((g)<-[:genre]-(m)<-[:watched]-(u))\n";
			cypher += "RETURN m,avg(toInt(w.rating)) as a ORDER BY a DESC LIMIT 5\n";
			System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				MovieResponse mr = new MovieResponse();
				Node node = (Node)row.get("m");
				Movie m = new Movie();
				m.setId((String)node.getProperty(mtdt.getuniqueproperty("MOVIE")));
				suggestions.add(m);
			}*/
			tx.success();
			tx.close();
		}
		return suggestions;
	}
	
	/*****************************Get product property **********************************************/
	public static String getProductProperty(String productlabel, String productid, String productproperty,GraphDatabaseService GDBServices){
		String property = null;
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			//Fetch user details
			String cypher = "MATCH (n:"+productlabel+" {"+mtdt.getuniqueproperty(productlabel)+": '"+productid+"'})\n";
			cypher += "RETURN n."+productproperty+" as value";
			System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				MovieResponse mr = new MovieResponse();
				property = (String)row.get("value");
				break;
			}
			tx.success();
			tx.close();
		}
		return property;
	}
	
	
	/*****************************Get product property **********************************************/
	public static List<String> getAllProductProperty(String productlabel, String productid,GraphDatabaseService GDBServices){
		List<String> property = new ArrayList<String>();
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			//Fetch user details
			String cypher = "MATCH (n:"+productlabel+" {"+mtdt.getuniqueproperty(productlabel)+": '"+productid+"'})\n";
			cypher += "RETURN n.title as title,n.description as description,n.imageUrlStr as imageURL,n.mrp as mrp,n.price as price,n.productBrand as brand,n.inStock as inStock,n.codAvailable as cod,n.offers as offers,n.discount as discount";
			System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				MovieResponse mr = new MovieResponse();
				
				property.add((String)row.get("title"));
				property.add((String)row.get("description"));
				property.add((String)row.get("imageURL"));
				property.add((String)row.get("mrp"));
				property.add((String)row.get("price"));
				property.add((String)row.get("brand"));
				property.add((String)row.get("inStock"));
				property.add((String)row.get("cod"));
				property.add((String)row.get("offers"));
				property.add((String)row.get("discount"));
				break;
			}
			tx.success();
			tx.close();
		}
		return property;
	}
	
	public static List<String> getProductConnection(String productlabel, String productid, String connectionLabel, String connectionRelation){
		List<String> property = new ArrayList<String>();
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			//Fetch user details
			String cypher = "MATCH (n:"+productlabel+" {"+mtdt.getuniqueproperty(productlabel)+": '"+productid+"'})-[r:"+connectionRelation+"]-(c:"+connectionLabel+")\n";
			cypher += "RETURN c."+mtdt.getdisplayproperty(connectionLabel)+" as conn";
			System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				MovieResponse mr = new MovieResponse();
				property.add((String)row.get("conn"));
			}
			tx.success();
			tx.close();
		}
		return property;
	}
	
	
	public static List<String> getTagConnection(String productlabel, String productid, String connectionLabel, String connectionRelation){
		List<String> property = new ArrayList<String>();
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			//Fetch user details
			String cypher = "MATCH (n:"+productlabel+" {"+mtdt.getuniqueproperty(productlabel)+": '"+productid+"'})-[r:"+connectionRelation+"]-(c:"+connectionLabel+")\n";
			cypher += "WHERE NOT (HAS(r.upvote) OR HAS(r.downvote))";
			cypher += "RETURN c."+mtdt.getdisplayproperty(connectionLabel)+" as conn";
			System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				MovieResponse mr = new MovieResponse();
				property.add((String)row.get("conn"));
			}
			tx.success();
			tx.close();
		}
		return property;
	}
	
	
	
	/*****************************Get movie suggestions from following circle ********************************/
	public static List<Movie> getSuggestionsFromMovie(String userid,String movieid){
		List<Movie> suggestions = new ArrayList<Movie>();
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			//Fetch user details
			
			
			String cypher = "MATCH (n:MOVIE {movieId: '"+movieid+"'})<-[w:watched]-(f:User)-[:watched]->(m)\n";
			cypher	+= "WHERE NOT((m)<-[:watched]-(:User {"+mtdt.getuniqueproperty("User")+": '"+userid+"'}))\n";
			cypher += "RETURN m,count(w) as c ORDER BY c DESC LIMIT 12\n";
			System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				MovieResponse mr = new MovieResponse();
				Node node = (Node)row.get("m");
				Movie m = new Movie();
				m.setId((String)node.getProperty(mtdt.getuniqueproperty("MOVIE")));
				suggestions.add(m);
			}
			tx.success();
			tx.close();
		}
		return suggestions;
	}
	
	/*****************************Get movie suggestions from following circle ********************************/
	public static List<String> getSuggestedProductsForUser(String userid,String productlabel, String productid, String relation){
		System.out.println(userid + "\t" + productlabel + "\t" + productid + "\t" + relation);
		List<String> suggestions = new ArrayList<String>();
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			//Fetch user details
			String cypher = "MATCH (n:"+productlabel+" {"+mtdt.getuniqueproperty(productlabel)+": '"+productid+"'})<-[w:"+relation+"]-(f:User)-[k:"+relation+"]->(m:"+productlabel+")\n";
			cypher	+= "WHERE NOT((m)<-[:"+relation+"]-(:User {"+mtdt.getuniqueproperty("User")+": '"+userid+"'}))\n";
			cypher+="WITH m,k\n";
			cypher+="MATCH (n:"+productlabel+" {"+mtdt.getuniqueproperty(productlabel)+": '"+productid+"'})-[ms:PRODUCTSIMILARITY]-(m)\n";
			cypher += "RETURN m."+mtdt.getuniqueproperty(productlabel)+" AS id ORDER BY ms.productSimilarity DESC,k.rating DESC LIMIT 12\n";
			System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				String id = (String)row.get("id");
				suggestions.add(id);
			}
			tx.success();
			tx.close();
		}
		return suggestions;
	}
	/************************ check if user is followed by active user **************************/
	public static boolean checkfollower(String active, String user){
		boolean followed = false;
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			//Fetch user details
			String cypher = "MATCH (a:User {"+mtdt.getuniqueproperty("User")+" : '"+active+"'})-[f:follows]->(u:User {"+mtdt.getuniqueproperty("User")+" : '"+user+"'}) RETURN f\n";
			System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				followed = true;
				break;
			}
			tx.success();
			tx.close();
		}
		return followed;
	}
	
	/**********************Get the number of ratings for a movie***************************/
	public static String getNoOfRatings(String movieid){
		String count = "0";
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			//Fetch user details
			String cypher = "MATCH (m:MOVIE {"+mtdt.getuniqueproperty("MOVIE")+" : '"+movieid+"'})<-[:watched]-(u:User)\n";
			cypher += "RETURN toString(count(*)) AS c\n";
			System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				count = (String)row.get("c");
				break;
			}
			tx.success();
			tx.close();
		}
		return count;
	}
	
	/**********************Get the number of ratings for the product***************************/
	public static String getProductNoOfRatings(String productlabel, String productid, String relation){
		String count = "0";
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			//Fetch user details
			String cypher = "MATCH (m:"+productlabel+" {"+mtdt.getuniqueproperty(productlabel)+" : '"+productid+"'})<-[:"+relation+"]-(u:User)\n";
			cypher += "RETURN toString(count(*)) AS c\n";
			System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				count = (String)row.get("c");
				break;
			}
			tx.success();
			tx.close();
		}
		return count;
	}
	/***********************follow User ********************************************/
	
	public static void followUser(String active, String user){
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			//Fetch user details
			String cypher = "MATCH (a:User {"+mtdt.getuniqueproperty("User")+" : '"+active+"'}),(u:User {"+mtdt.getuniqueproperty("User")+" : '"+user+"'})\n";
			cypher += "MERGE (a)-[:follows]->(u)\n";
			System.out.println(cypher);
			engine.execute(cypher);
			tx.success();
			tx.close();
		}
	}
	/***********************Get user wishList ************************************************/
	public static List<Movie> getUserWishList(String id){
		List<Movie> wishlist = new ArrayList<Movie>(); 
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			//Fetch user details
			String cypher = "MATCH (u:User {"+mtdt.getuniqueproperty("User")+" : '"+id+"'})-[:wishlisted]->(m)\n";
			cypher += "WITH m\n";
			cypher += "OPTIONAL MATCH (m)<-[w:watched]-()\n";
			cypher += "RETURN m, avg(toFloat(w.rating)) as rating";
			System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				Node node = (Node)row.get("m");
				Movie m = new Movie();
				m.setId((String)node.getProperty(mtdt.getuniqueproperty("MOVIE")));
				wishlist.add(m);
			}
		}
		return wishlist;
	}
	/***************************************************get similar product****************************************/
	
	
	public static void getSimilarProducts(String productLabel,String productId)
	{
		//List<Movie> sugg = new ArrayList<Movie>();
		List<Object> products = new ArrayList<Object>();
		/*products.add("MOVIE");
		products.add("BOOK");*/
		Double similarity = null;
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			
			String cypher = "MATCH (m:"+productLabel+"{"+mtdt.getuniqueproperty(productLabel)+":'"+productId+"'})-[s:PRODUCTSIMILARITY]-(p) return p,s as sim ORDER BY sim.productSimilarity desc LIMIT 6";
			 System.out.println(cypher);
		     ExecutionResult result1 = engine.execute(cypher);
		     for(Map<String,Object> row : result1){
					Node node = (Node)row.get("p");
					Relationship r = (Relationship)row.get("sim");
					Double similar = (Double) r.getProperty("productSimilarity");
					if(similar > 0)
					{
						products.add(node.getProperty("ISBN"));
						
					//Class c=Class.forName(node.getClass().toString());
					//m.setId((String)node.getProperty(mtdt.getuniqueproperty("MOVIE")));
					//sugg.add(m);
					}
					
				}
		     for(int i=0;i<6;i++)
		     {
		    	Class c= products.get(i).getClass();
		    	System.out.println(c);
		     }
		     tx.success();
			tx.close();
		}
		//return sugg;
			
	}
	
	
	public static void calculateWeightsForAllUsers()
	{
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			int node=0;
			String cypher="MATCH (m)<-[r:tag]-(t:TAG) WHERE HAS(r.upvote) return r.upvote,count(t) as total";
			System.out.println(cypher);
		     ExecutionResult result1 = engine.execute(cypher);
		     List<Integer> totaltagcount = new ArrayList<Integer>();
		     List<String> userid = new ArrayList<String>();
		     for(Map<String,Object> row : result1){
		    	 	String user =row.get("r.upvote").toString();
		    	    System.out.println(user);
					node = Integer.parseInt(row.get("total").toString());
					totaltagcount.add(node);
					userid.add(user);
					
				}
		     for(int i=0;i<userid.size();i++)
		     {
		     String cypher1="MATCH (m)<-[r:tag{upvote:'"+userid.get(i)+"'}]-(t:TAG),(u:User{ID:'"+userid.get(i)+"'})\n";
			    cypher1 += "WITH u,t,COUNT(*) as tagcount\n";
			    cypher1 += "MERGE (u)-[r:TAGWEIGHT]-(t)\n";
			    cypher1 += "SET r.tagweight = toString(toFloat(tagcount)/"+totaltagcount.get(i)+")";
			    System.out.println(cypher1);
			    ExecutionResult result2 = engine.execute(cypher1);
		     }
		     tx.success();
			tx.close();
		}
	}
	
	
	public static void calculateWeightsForUser(String userID,String connectionLabel,String connectionRelation,String attributeName)
	{
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			int node=0;
			String cypher="MATCH (m)<-[r:"+connectionRelation+"{"+attributeName+":'"+userID+"'}]-(t:"+connectionLabel+") return r."+attributeName+",count(t) as total";
			System.out.println(cypher);
		     ExecutionResult result1 = engine.execute(cypher);
		     for(Map<String,Object> row : result1){
					node = Integer.parseInt(row.get("total").toString());
					
				}
		    String cypher1="MATCH (m)<-[r:"+connectionRelation+"{"+attributeName+":'"+userID+"'}]-(t:"+connectionLabel+"),(u:User{ID:'"+userID+"'})\n";
		    cypher1 += "WITH u,t,COUNT(*) as tagcount\n";
		    cypher1 += "MERGE (u)-[r:TAGWEIGHT]-(t)\n";
		    cypher1 += "SET r.tagweight = toString(toFloat(tagcount)/"+node+")";
		    ExecutionResult result2 = engine.execute(cypher1);
		   
		     tx.success();
			tx.close();
		}
	}
	/*****************************************get similar movies**********************************************/
	public static List<Movie> getSimilarMovies(String productLabel,String productId)
	{
		List<Movie> sugg = new ArrayList<Movie>();
		Double similarity = null;
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			
			String cypher = "MATCH (m:MOVIE{"+mtdt.getuniqueproperty(productLabel)+":'"+productId+"'})-[s:PRODUCTSIMILARITY]-(m1:MOVIE)\n";
			cypher+= "WITH m1,s\n";
			cypher+="MATCH (f:User)-[w:watched]->(m1)\n";
			cypher+="return m1,s as sim,avg(toInt(w.rating)) as avgrating ORDER BY sim.productSimilarity desc,avgrating desc LIMIT 12";
			
			// System.out.println(cypher);
		     ExecutionResult result1 = engine.execute(cypher);
		     for(Map<String,Object> row : result1){
					Node node = (Node)row.get("m1");
					Relationship r = (Relationship)row.get("sim");
					Double similar = (Double) r.getProperty("productSimilarity");
					System.out.println(similar);
					if(similar >0)
					{
					Movie m = new Movie();
					m.setId((String)node.getProperty(mtdt.getuniqueproperty("MOVIE")));
					sugg.add(m);
					}
					
				}
		     tx.success();
				tx.close();
		}
		return sugg;
			
	}
	
	/*****************************************get similar books**********************************************/
	public static List<Book> getSimilarBooks(String productLabel,String productId)
	{
		List<Book> sugg = new ArrayList<Book>();
		Double similarity = null;
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			
			String cypher = "MATCH (m:BOOK{"+mtdt.getuniqueproperty(productLabel)+":'"+productId+"'})-[s:PRODUCTSIMILARITY]-(m1:BOOK) return m1,s as sim ORDER BY sim.productSimilarity desc LIMIT 6";
			 System.out.println(cypher);
		     ExecutionResult result1 = engine.execute(cypher);
		     for(Map<String,Object> row : result1){
					Node node = (Node)row.get("m1");
					Relationship r = (Relationship)row.get("sim");
					Double similar = (Double) r.getProperty("productSimilarity");
					if(similar >0)
					{
					Book m = new Book();
					m.setId((String)node.getProperty(mtdt.getuniqueproperty("BOOK")));
					sugg.add(m);
					}
					
				}
		     tx.success();
				tx.close();
		}
		return sugg;
			
	}
	
	
	/*********************Get top picks ***************************************************/
	public static List<Movie> getTopPicks(){
		List<Movie> topPicks = new ArrayList<Movie>();
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			String cypher = "MATCH (m)<-[w:watched]-(u)\n";
			cypher += "RETURN m,avg(toInt(w.rating)) as avgrating, toString(count(w)) as voteCount ORDER BY avgrating DESC LIMIT 6";
			System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				Node node = (Node)row.get("m");
				Movie m = new Movie();
				m.setId((String)node.getProperty(mtdt.getuniqueproperty("MOVIE")));
				topPicks.add(m);
			}
		}
		return topPicks;
	}
	
	
	public static List<Book> getTopBookPicks(){
		List<Book> topbookPicks = new ArrayList<Book>();
	/*	if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			String cypher = "MATCH (b)<-[r:read]-(u)\n";
			cypher += "RETURN b,avg(toInt(b.rating)) as avgrating, toString(count(r)) as voteCount ORDER BY avgrating DESC LIMIT 6";
			System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				Node node = (Node)row.get("b");
				Book b = new Book();
				b.setId((String)node.getProperty(mtdt.getuniqueproperty("BOOK")));
				topbookPicks.add(b);
			}
		}
		return topbookPicks;
	}
	
	/***************************Get genre wise movies list *********************************/
	public static List<Genre> getGenreWiseMoviesList(){
		List<Genre> genreList = new ArrayList<Genre>();
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			String cypher = "MATCH (g:GENRE)\n";
			cypher += "WITH g\n";
			cypher += "OPTIONAL MATCH (g)<-[:genre]-(m)\n";
			cypher += "RETURN g,toString(count(m)) as num ORDER BY g.genre\n";
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				Node node = (Node)row.get("g");
				Genre g = new Genre();
				g.setGenre((String)node.getProperty("genre"));
				g.setMovieCount((String)row.get("num"));
				genreList.add(g);
			}
		}
		return genreList;
	}
	/********************* Get user followersList *********************************************/
	public static List<User> getUserFollowersList(String id){
		List<User> followerslist = new ArrayList<User>(); 
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			//Fetch user details
			String cypher = "MATCH (u:User {"+mtdt.getuniqueproperty("User")+" : '"+id+"'})<-[:follows]-(f)";
			cypher += "WITH f\n";
			cypher += "OPTIONAL MATCH (f)-[w:watched]->(m)\n";
			cypher += "RETURN f, toString(count(m)) AS movies";
			System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				Node node = (Node)row.get("f");
				User u = new User();
				u.setId((String)node.getProperty("ID"));
				followerslist.add(u);
			}
		}
		return followerslist;
	}
	
	/********************* Get user followersList *********************************************/
	public static List<User> getUserFollowingList(String id){
		List<User> followinglist = new ArrayList<User>(); 
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			//Fetch user details
			String cypher = "MATCH (u:User {"+mtdt.getuniqueproperty("User")+" : '"+id+"'})-[:follows]->(f)";
			cypher += "WITH f\n";
			cypher += "OPTIONAL MATCH (f)-[w:watched]->(m)\n";
			cypher += "RETURN f, toString(count(m)) AS movies";
			System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				Node node = (Node)row.get("f");
				User u = new User();
				u.setId((String)node.getProperty("ID"));
				followinglist.add(u);
			}
		}
		return followinglist;
	}
	
	/*********************** Update properties *******************************************/
	public static void updateProperty(String label, String id, String property, String value){
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			String cypher = "MATCH (n:"+label+" {ID : '"+id+"'}) SET n."+property+" = '"+value+"'\n";
			engine.execute(cypher);
			tx.success();
			tx.close();
			System.out.println(property + " for " + label + " " + id + " updated to " + value);
		}
	}
	
	/*********************************Movie average rating ************************************/
	public static double getProductAvgRating(String productlabel, String productid, String relation){
		double avgRating=0;
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			String cypher = "MATCH (m:"+productlabel+" {"+mtdt.getuniqueproperty(productlabel)+":'"+productid+"'})<-[w:"+relation+"]-(u)\n";
			cypher += "RETURN avg(toInt(w.rating)) as avgrating";
			System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				avgRating = row.get("avgrating")==null?0:(double)row.get("avgrating");
				break;
			}
		}
		avgRating = Math.round(avgRating * 100.0) / 100.0;
		return avgRating;
	}
	
	/*********************************Movie average rating ************************************/
	public static double getMovieAvgRating(String id){
		double avgRating=0;
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			String cypher = "MATCH (m:MOVIE {"+mtdt.getuniqueproperty("MOVIE")+":'"+id+"'})<-[w:watched]-(u)\n";
			cypher += "RETURN avg(toInt(w.rating)) as avgrating";
			System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				avgRating = row.get("avgrating")==null?0:(double)row.get("avgrating");
				break;
			}
		}
		avgRating = Math.round(avgRating * 100.0) / 100.0;
		return avgRating;
	}
	
	/***************************Get product details ******************************************/
	public static Movie getProductDetails(String id){
		Movie m = new Movie();
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			//Fetch user details
			String cypher = "MATCH (m:MOVIE {"+ mtdt.getuniqueproperty("MOVIE")+" : '"+id+"'}) RETURN m";
			System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				Node node = (Node)row.get("m");
				m.setId((String)node.getProperty(mtdt.getuniqueproperty("MOVIE")));
				break;
			}
			cypher = "MATCH (m:MOVIE {"+ mtdt.getuniqueproperty("MOVIE")+" : '"+id+"'})<-[w:watched]-() RETURN avg(toFloat(w.rating)) AS avgr";
			System.out.println(cypher);
			result = engine.execute(cypher);
			double nr = 0;
			for(Map<String,Object> row : result){
				nr = row.get("avgr")==null?0:(double)row.get("avgr");
				break;
			}
			tx.success();
			tx.close();
		}
		return m;
	}
	
	/********************* Get friends response to the product *********************************/
	public static List<MovieResponse> getResponseFromTheFollowingCircle(String userid, String movieid){
		List<MovieResponse> response = new ArrayList<MovieResponse>();
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			String cypher = "MATCH (u:User {ID : '"+userid+"'})-[:follows]->(f)-[w:watched]->(m:MOVIE {"+mtdt.getuniqueproperty("MOVIE")+": '"+movieid+"'}) RETURN f,m,w\n";
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				MovieResponse mr = new MovieResponse();
				Node node = (Node)row.get("f");
				User following = new User();
				following.setId((String)node.getProperty("ID"));
				mr.setUser(following);
				Relationship rel = (Relationship)row.get("w");
				mr.setComment((String)rel.getProperty("comment"));
				mr.setRating((String)rel.getProperty("rating"));
				response.add(mr);
			}
			tx.success();
			tx.close();
		}
		return response;
	}
	
	/********************* Get friends response to the product *********************************/
	public static List<ProductResponse> getProductResponseFromTheFollowingCircle(String userid, String productlabel, String productid, String relation){
		List<ProductResponse> response = new ArrayList<ProductResponse>();
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			String cypher = "MATCH (u:User {ID : '"+userid+"'})-[:follows]->(f)-[w:"+relation+"]->(m:"+productlabel+" {"+mtdt.getuniqueproperty(productlabel)+": '"+productid+"'}) RETURN f,m,w\n";
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				ProductResponse pr = new ProductResponse();
				Node node = (Node)row.get("f");
				User following = new User();
				following.setId((String)node.getProperty("ID"));
				pr.setUser(following);
				Relationship rel = (Relationship)row.get("w");
				pr.setComment((String)rel.getProperty("comment"));
				pr.setRating((String)rel.getProperty("rating"));
				response.add(pr);
			}
			tx.success();
			tx.close();
		}
		return response;
	}
	
	/********************* Get user's response to the product *********************************/
	public static MovieResponse getUserResponse(String userid, String movieid){
		MovieResponse mr = new MovieResponse();
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			String cypher = "MATCH (u:User {ID : '"+userid+"'})-[w:watched]->(m:MOVIE {"+mtdt.getuniqueproperty("MOVIE")+": '"+movieid+"'}) RETURN w.comment AS comment, w.rating AS rating\n";
			System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				mr.hasWatched();
				mr.setComment((String)row.get("comment"));
				mr.setRating((String)row.get("rating"));
				break;
			}
			if(!mr.didWatch()){
				cypher = "MATCH (u:User {ID : '"+userid+"'})-[w:wishlisted]->(m:MOVIE {"+mtdt.getuniqueproperty("MOVIE")+": '"+movieid+"'}) RETURN w\n";
				result = engine.execute(cypher);
				for(Map<String,Object> row : result){
					mr.hasWishlisted();
					break;
				}
			}
			tx.success();
			tx.close();
		}
		return mr;
	}
	
	/********************* Get user's response to the product *********************************/
	public static ProductResponse getUserResponseToProduct(String userid, String productlabel, String productid, String relation){
		ProductResponse pr = new ProductResponse();
		User u = new User(userid);
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			String cypher = "MATCH (u:User {ID : '"+userid+"'})-[w:"+relation+"]->(m:"+productlabel+" {"+mtdt.getuniqueproperty(productlabel)+": '"+productid+"'}) RETURN w.comment AS comment, w.rating AS rating\n";
			System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				pr.setUser(u);
				pr.setComment((String)row.get("comment"));
				pr.setRating((String)row.get("rating"));
				break;
			}
			tx.success();
			tx.close();
		}
		return pr;
	}
	
	/*********************** Update product response *******************************************/
	public static void updateProductResponse(String userid, String label, String id, String property, String value, String relation){
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			String cypher = "MATCH (u:User {"+mtdt.getuniqueproperty("User")+":'"+userid+"'}),(m:"+label+" {"+mtdt.getuniqueproperty(label)+": '"+id+"'})\n";
			cypher += "MERGE (u)-[w:"+relation+"]->(m)\n";
			cypher += "SET w."+property+"='"+value+"'\n";
			engine.execute(cypher);
			tx.success();
			tx.close();
			System.out.println("product response has been updated");
		}
	}
	
	
	/**********************suugestions_according_to_smilarity_with_other_users
	 * @return ***************************************************/
	
	
	public static void updatingSimilarity()
	{
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			String cypher3 = "MATCH(u1:User)-[r1:watched]->(m)<-[r2:watched]-(u2:User) WITH SUM(toInt(r1.rating) * toInt(r2.rating)) AS xyDotProduct,SQRT(REDUCE(xDot = 0.0, a IN COLLECT(toInt(r1.rating)) | xDot + a^2)) AS xLength,SQRT(REDUCE(yDot = 0.0, b IN COLLECT(toInt(r2.rating)) | yDot + b^2)) AS yLength, u1, u2 MERGE (u1)-[s:SIMILARITY]-(u2) SET   s.similarity = xyDotProduct / (xLength * yLength)";
			  ExecutionResult result3 = engine.execute(cypher3);
			  tx.success();
				tx.close();
		}
	}
	
/*********************************updating specific product similarity*****************************************/
	
	public static void updatingSpecificProductSimilarityWeight(String productLabel,String productId,String connectionLabel,String connectionId,String connectionRelation,String userID)
	{
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			System.out.println("in db queries");
			Long upvoteCount =getCount(productLabel, productId, connectionLabel, connectionId, connectionRelation, "upvote"); 
			Long downvoteCount=getCount(productLabel, productId, connectionLabel, connectionId, connectionRelation, "downvote"); 
			String cypher2= " MATCH (m1:"+productLabel+"{"+mtdt.getuniqueproperty(productLabel)+":'"+productId+"'})\n";
			cypher2+="MATCH (t:"+connectionLabel+"{"+mtdt.getuniqueproperty(connectionLabel)+":'"+connectionId+"'})\n";
			cypher2+="WITH m1,t\n";
			cypher2+="OPTIONAL MATCH (m1)-[rd:votecount]->(t)\n";
			cypher2+=" DELETE rd\n";
			cypher2+="MERGE (m1)-[c:votecount{upvotecount:'"+upvoteCount+"',downvotecount:'"+downvoteCount+"'}]->(t)" ; 
			//System.out.println(cypher2);
			ExecutionResult result1 = engine.execute(cypher2);
			List<String> products = new ArrayList<String>();
			products.add("MOVIE");
			products.add("BOOK");
			for(String s:products)
			{
			String cypher4= "MATCH(m1:"+productLabel+")-[r1:votecount]->(t)<-[r2:votecount]-(m2:"+s+") WITH SUM((toInt(r1.upvotecount)-toInt(r1.downvotecount)) * (toInt(r2.upvotecount)-toInt(r2.downvotecount))) AS xyDotProduct,SQRT(REDUCE(xDot = 0.0, a IN COLLECT(toInt(r1.upvotecount)-toInt(r1.downvotecount)) | xDot + a^2)) AS xLength,SQRT(REDUCE(yDot = 0.0, b IN COLLECT(toInt(r2.upvotecount)-toInt(r2.downvotecount)) | yDot + b^2)) AS yLength, m1, m2 MERGE (m1)-[s:PRODUCTSIMILARITY]-(m2) SET   s.productSimilarity = xyDotProduct / (xLength * yLength)";
			//System.out.println(cypher4);
			ExecutionResult result2 = engine.execute(cypher4);
			}
			tx.success();
			tx.close();
	
	}
	}
	
	/*********************************updating specific product similarity*****************************************/
	
	public static void updatingSpecificProductSimilarity(String productLabel,String productId,String connectionLabel,String connectionId,String connectionRelation)
	{
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			System.out.println("in db queries");
			Long upvoteCount =getCount(productLabel, productId, connectionLabel, connectionId, connectionRelation, "upvote"); 
			Long downvoteCount=getCount(productLabel, productId, connectionLabel, connectionId, connectionRelation, "downvote"); 
			String cypher2= " MATCH (m1:"+productLabel+"{"+mtdt.getuniqueproperty(productLabel)+":'"+productId+"'})\n";
			cypher2+="MATCH (t:"+connectionLabel+"{"+mtdt.getuniqueproperty(connectionLabel)+":'"+connectionId+"'})\n";
			cypher2+="WITH m1,t\n";
			cypher2+="OPTIONAL MATCH (m1)-[rd:votecount]->(t)\n";
			cypher2+=" DELETE rd\n";
			cypher2+="MERGE (m1)-[c:votecount{upvotecount:'"+upvoteCount+"',downvotecount:'"+downvoteCount+"'}]->(t)" ; 
			//System.out.println(cypher2);
			ExecutionResult result1 = engine.execute(cypher2);
			List<String> products = new ArrayList<String>();
			products.add("MOVIE");
			products.add("BOOK");
			for(String s:products)
			{
			String cypher4= "MATCH(m1:"+productLabel+")-[r1:votecount]->(t)<-[r2:votecount]-(m2:"+s+") WITH SUM((toInt(r1.upvotecount)-toInt(r1.downvotecount)) * (toInt(r2.upvotecount)-toInt(r2.downvotecount))) AS xyDotProduct,SQRT(REDUCE(xDot = 0.0, a IN COLLECT(toInt(r1.upvotecount)-toInt(r1.downvotecount)) | xDot + a^2)) AS xLength,SQRT(REDUCE(yDot = 0.0, b IN COLLECT(toInt(r2.upvotecount)-toInt(r2.downvotecount)) | yDot + b^2)) AS yLength, m1, m2 MERGE (m1)-[s:PRODUCTSIMILARITY]-(m2) SET   s.productSimilarity = xyDotProduct / (xLength * yLength)";
			//System.out.println(cypher4);
			ExecutionResult result2 = engine.execute(cypher4);
			}
			tx.success();
			tx.close();
	
	}
	}
	
/****************************update tag user**************************************************************************/
	
	
	/******************************************************************************************************************/
	
	/*****************************************updating movie similarity******************************************/
	public static void updatingMovieSimilarity()
	{
		List<String> list1 = new ArrayList<String>();
		List<String> list2 = new ArrayList<String>();
		List<String> list3 = new ArrayList<String>();
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			
		String cypher = "MATCH (m1:MOVIE)<-[r1:tag]-(t)-[r2:tag]->(m2:MOVIE)\n";
		cypher+="WHERE  (HAS(r1.upvote) OR HAS(r1.downvote) OR HAS(r2.upvote) OR HAS(r2.downvote))\n";
		cypher+= "RETURN m1,m2,t";
	    ExecutionResult result= engine.execute(cypher);
			 // System.out.println(cypher);
			for(Map<String,Object> row:result)
			{
				Node node1 = (Node)row.get("m1");
				Node node2 = (Node)row.get("m2");
				Node node3 =  (Node)row.get("t");
				String movie1 = (String) node1.getProperty(mtdt.getuniqueproperty("MOVIE"));
				String movie2 = (String) node2.getProperty(mtdt.getuniqueproperty("MOVIE"));
				String tag = (String) node3.getProperty(mtdt.getuniqueproperty("TAG"));
				
				list1.add(movie1);
				list2.add(movie2);
				list3.add(tag);
				
			}
			
			
			for(int i=0;i<list1.size();i++)
			{
				Long movie1upvote = getCount("MOVIE", list1.get(i), "TAG", list3.get(i), "tag", "upvote");
				Long movie1downvote = getCount("MOVIE", list1.get(i), "TAG", list3.get(i), "tag", "downvote");
				Long movie2upvote = getCount("MOVIE", list2.get(i), "TAG", list3.get(i), "tag", "upvote");
				Long movie2downvote = getCount("MOVIE", list2.get(i), "TAG", list3.get(i), "tag", "downvote");
				String cypher2 = " MATCH (m1:MOVIE{"+mtdt.getuniqueproperty("MOVIE")+":'"+list1.get(i)+"'})\n";
				cypher2+="MATCH (t:TAG{"+mtdt.getuniqueproperty("TAG")+":'"+list3.get(i)+"'})\n";
				cypher2+="WITH m1,t\n";
				cypher2+="OPTIONAL MATCH (m1)-[rd:votecount]->(t)\n";
				cypher2+=" DELETE rd\n";
				cypher2+="MERGE (m1)-[c:votecount{upvotecount:'"+movie1upvote+"',downvotecount:'"+movie1downvote+"'}]->(t)" ;
				
				String cypher3 = " MATCH (m2:MOVIE{"+mtdt.getuniqueproperty("MOVIE")+":'"+list2.get(i)+"'})\n";
				cypher3+="MATCH (t:TAG{"+mtdt.getuniqueproperty("TAG")+":'"+list3.get(i)+"'})\n";
				cypher3+="WITH m2,t\n";
				cypher3+="OPTIONAL MATCH (m2)-[ru:votecount]->(t)\n";
				cypher3+=" DELETE ru\n";
				cypher3+="MERGE (m2)-[c:votecount{upvotecount:'"+movie2upvote+"',downvotecount:'"+movie2downvote+"'}]->(t)";
				
				ExecutionResult result1 = engine.execute(cypher2);
				ExecutionResult result2 = engine.execute(cypher3);
				
				/*System.out.println(cypher2);
				System.out.println(cypher3);*/
			}
			
			String cypher4= "MATCH(m1:MOVIE)-[r1:votecount]->(t)<-[r2:votecount]-(m2:MOVIE) WITH SUM((toInt(r1.upvotecount)-toInt(r1.downvotecount)) * (toInt(r2.upvotecount)-toInt(r2.downvotecount))) AS xyDotProduct,SQRT(REDUCE(xDot = 0.0, a IN COLLECT(toInt(r1.upvotecount)-toInt(r1.downvotecount)) | xDot + a^2)) AS xLength,SQRT(REDUCE(yDot = 0.0, b IN COLLECT(toInt(r2.upvotecount)-toInt(r2.downvotecount)) | yDot + b^2)) AS yLength, m1, m2 MERGE (m1)-[s:PRODUCTSIMILARITY]-(m2) SET   s.productSimilarity = xyDotProduct / (xLength * yLength)";
			//System.out.println(cypher4);
			ExecutionResult result3= engine.execute(cypher4);
			
			  tx.success();
				tx.close();
		}
	}
	
	
	
	
	public static List<Movie> getMovieSuggestionBySimilarityWithOtherUsers(String userid,String productlabel)
	{
		List<Movie> suggestions = new ArrayList<Movie>();
		try(Transaction tx = GDBServices.beginTx()){
		ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
		
		String cypher = "MATCH (u:User {"+mtdt.getuniqueproperty("User")+":'"+userid+"'})-[s:SIMILARITY]-(p:User) return p ORDER BY s.similarity desc LIMIT 1";
		//System.out.println(cypher);
		ExecutionResult result = engine.execute(cypher);
		String similarUser = null;
		for(Map<String,Object> row : result){
			Node node = (Node)row.get("p");
		    similarUser = ((String)node.getProperty(mtdt.getuniqueproperty("User")));
		}
		    String cypher2 = "MATCH (u:User {"+mtdt.getuniqueproperty("User")+":'"+similarUser+"'})-[w:watched]->(m:MOVIE) return m ORDER BY (toInt(w.rating)) desc LIMIT 6";
		   // System.out.println(cypher2);
			ExecutionResult result1 = engine.execute(cypher2);
			for(Map<String,Object> row : result1){
				Node node = (Node)row.get("m");
				Movie m = new Movie();
				m.setId((String)node.getProperty(mtdt.getuniqueproperty("MOVIE")));
				suggestions.add(m);
			}
		
			tx.success();
			tx.close();
		}
		
		return suggestions;
	}
	
	
	/***************************************get similarity*******************************************************/
	
	public static Double getSimilarity(String searchUser, String activeUser)
	{
		Double similarity = null;
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			
			String cypher = "MATCH (u:User{"+mtdt.getuniqueproperty("User")+":'"+searchUser+"'})-[s:SIMILARITY]-(u1:User{"+mtdt.getuniqueproperty("User")+":'"+activeUser+"'}) return u,s as sim";
			// System.out.println(cypher);
		     ExecutionResult result1 = engine.execute(cypher);
		     for(Map<String,Object> row : result1){
					Node node = (Node)row.get("u");
					Relationship r = (Relationship)row.get("sim");
					similarity = (Double) r.getProperty("similarity");
					
				}
		     tx.success();
				tx.close();
		}
		if(similarity!=null)
			return similarity;
			else
				return (double) 0;
			
	}
	
	
	/************************Update user's interest in the product*******************************/
	public static void updateProductInterest(String userid, String productlabel, String productid){
	/*	if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			String cypher = "MATCH (u:User {"+mtdt.getuniqueproperty("User")+":'"+userid+"'}),(m:"+productlabel+" {"+mtdt.getuniqueproperty(productlabel)+": '"+productid+"'})\n";
			cypher += "MERGE (u)-[w:wishlisted]->(m)\n";
			//System.out.println(cypher);
			engine.execute(cypher);
			tx.success();
			tx.close();
			System.out.println("product interest has been updated");
		}
	}
	
	/************************Check if the connection exists****************************/
	public static boolean checkConnectionExistence(String userid, String productlabel, String productid, String relation){
		boolean ret = false;
	/*	if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			String cypher = "MATCH (u:User {"+mtdt.getuniqueproperty("User")+":'"+userid+"'})-[r:"+relation+"]->(m:"+productlabel+" {"+mtdt.getuniqueproperty(productlabel)+": '"+productid+"'})\n";
			cypher += "RETURN count(*) AS c\n";
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row : result){
				Long count = (Long)row.get("c");
				if(count == 1){
					ret = true;
				}
				break;
			}
			tx.success();
			tx.close();
		}
		return ret;
	}
	/********************* Update the database *********************************************/
	public static String updateDatabase(String user, String movie, String status, String comment, String rated) {
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			String cypher = "MATCH (m:MOVIE {movieId:"+movie+"}),(u:User{UserId:"+user+"})\n";
			cypher += "MERGE (u)-[s:"+status+"]->(m)\n";
			cypher += "SET s.rating = '"+rated+"'\n";
			cypher += "MERGE (u)-[c:Commented]->(m)\n";
			cypher += "SET c.comment = '"+comment.replaceAll("'", "\'")+"'\n";
			ExecutionResult result = engine.execute(cypher);
			tx.success();
			tx.close();
		}
		return jsonString;
	}
	
	
	/********************* Update the database *********************************************/
	public static void updateProductConnection(String productLabel, String productID, String connectionLabel, String connectionID, String connectionRelation) {
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			String cypher = "MATCH (m:"+productLabel+" {"+mtdt.getuniqueproperty(productLabel)+":'"+productID+"'})\n";
			cypher += "MERGE (t:"+connectionLabel+"{"+mtdt.getuniqueproperty(connectionLabel)+":'"+connectionID+"'})\n";
			cypher += "MERGE (t)-[c:"+connectionRelation+"]->(m)";
		//	System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			
			tx.success();
			tx.close();
		}
		
	}
	
	
	/********************* Update the database *********************************************/
	public static String updateProductConnectionResponse(String productLabel, String productID, String connectionLabel, String connectionID, String connectionRelation,boolean responseType) {
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			String cypher = "MATCH (m:"+productLabel+" {"+mtdt.getuniqueproperty(productLabel)+":"+productID+"})\n";
			cypher += "MERGE (t:"+connectionLabel+"{"+mtdt.getuniqueproperty(connectionLabel)+":"+connectionID+"})\n";
			cypher += "MERGE (t)-[c:"+connectionRelation+"]->(m) RETURN c\n";
			ExecutionResult result = engine.execute(cypher);
			for(Map<String,Object> row:result){
				Relationship r = (Relationship)row.get("c");
				if(responseType)	r.setProperty("upvote", (int)r.getProperty("upvote", 0) + 1);
				else r.setProperty("downvote", (int)r.getProperty("downvote",0) + 1);
				break;
			}
			tx.success();
			tx.close();
		}
		return jsonString;
	}
	
	
	/********************* Update the database *********************************************/
	public static String updateProductConnectionResponseUpvoteOrDownvote(String productLabel, String productID, String connectionLabel, String connectionID, String connectionRelation,String userID,boolean responseType) {
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		System.out.println("at start of query");
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			String cypher = "MATCH (m:"+productLabel+" {"+mtdt.getuniqueproperty(productLabel)+":'"+productID+"'}), (t:"+connectionLabel+"{"+mtdt.getuniqueproperty(connectionLabel)+":'"+connectionID+"'})\n";
			
			if(responseType)
			{
			cypher += "MERGE (t)-[c:"+connectionRelation+"{upvote:'"+userID+"'}]->(m)\n";
			cypher += "WITH m,t\n";
			cypher += "OPTIONAL MATCH (m:MOVIE)<-[rd:"+connectionRelation+"{downvote:'"+userID+"'}]-(t:TAG)";
			//cypher += "OPTIONAL MATCH (m)-[ru:"+connectionRelation+"{upvote:'"+userID+"'}]-(t)";
			cypher += " DELETE rd";
			}
			else
			{
				cypher += "MERGE (t)-[c:"+connectionRelation+"{downvote:'"+userID+"'}]->(m)\n";
				cypher += "WITH m,t\n";
				cypher += "OPTIONAL MATCH (m:MOVIE)<-[ru:"+connectionRelation+"{upvote:'"+userID+"'}]-(t:TAG)";
				//cypher += "OPTIONAL MATCH (m)-[rd:"+connectionRelation+"{downvote:'"+userID+"'}]-(t)";
				cypher += " DELETE ru";
			}
		
			System.out.println(cypher);
			engine.execute(cypher);
			System.out.println("at end of query");
			tx.success();
			tx.close();
		}
		return jsonString;
	}
	
	
	
	/*************************Get all the data from the database ****************************************/
	public static String getAllData() {
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			String cypher = "MATCH p=(start)-[]-(end) RETURN p LIMIT 500";
			ExecutionResult result = engine.execute(cypher);
			Nodes.clear();
			Links.clear();
			System.out.println("executed");
			Iterator parentrels = result.columnAs("p");
			while(parentrels.hasNext()){
				Path path = (Path) parentrels.next();
	            Iterable<Relationship>  relationships = path.relationships();
	            java.util.Iterator<Relationship> relIterator = relationships.iterator();
	            while (relIterator.hasNext()){
	                Relationship rel = relIterator.next();
	                Node aNode = (Node) rel.getStartNode();
	                mergeNode(aNode);
	                Node zNode = (Node) rel.getEndNode();
	                mergeNode(zNode);
	                String type = rel.getType().name();
	                mergeLink(new Link((String)aNode.getProperty("ID"),(String)zNode.getProperty("ID"),type,100));
	            }
			}
			writetojson();
			tx.success();
			tx.close();
		}
		return jsonString;
	}
	
	/********************* Retrieve count of votes (Tag) from the database *********************************************/
	public static Long getCount(String productLabel, String productID, String connectionLabel, String connectionID, String connectionRelation, String voteType) {
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		Long count = null;
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			String cypher = "MATCH (m:"+productLabel+" {"+mtdt.getuniqueproperty(productLabel)+":'"+productID+"'})\n";
			//System.out.println("check"+cypher);
			cypher += "MATCH (t:"+connectionLabel+"{"+mtdt.getuniqueproperty(connectionLabel)+":'"+connectionID+"'})\n";
			cypher += "WITH m,t\n";
			cypher += "MATCH (t)-[c:"+connectionRelation+"]->(m)";
			cypher += "WHERE HAS(c."+voteType+")";
			cypher += "RETURN COUNT(*) AS COUNT";
			//System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String, Object> row : result)
			{
				count =(Long)row.get("COUNT");
			}
			tx.success();
			tx.close();
		}
		return count;
	}
	
	/********************* Retrieve vote of user (Tag) from the database *********************************************/
	public static boolean getUserVoteResponse(String productLabel, String productID, String connectionLabel, String connectionID, String connectionRelation, String voteType, String userID) {
		/*if(!isConnectionSet){
			DBConnection();
		}*/
		Long count = null;
		try(Transaction tx = GDBServices.beginTx()){
			ExecutionEngine engine = new ExecutionEngine(GDBServices,StringLogger.SYSTEM);
			String cypher = "MATCH (m:"+productLabel+" {"+mtdt.getuniqueproperty(productLabel)+":'"+productID+"'})\n";
			cypher += "MATCH (t:"+connectionLabel+"{"+mtdt.getuniqueproperty(connectionLabel)+":'"+connectionID+"'})\n";
			cypher += "WITH m,t\n";
			cypher += "MATCH (t)-[c:"+connectionRelation+"{"+voteType+":'"+userID+"'}]->(m)";
			cypher += "RETURN COUNT(*) AS COUNT";
			//System.out.println(cypher);
			ExecutionResult result = engine.execute(cypher);
			for(Map<String, Object> row : result)
			{
				count =(Long)row.get("COUNT");
			}
			
			
			tx.success();
			tx.close();
		}
		if(count==0)
			return false;
		else
			return true;
	}
	
	private static void updateNodes(ExecutionResult result){
		Nodes.clear();
		Links.clear();
		for(Map<String,Object> row : result){
			Node node = (Node)row.get("m");
			Nodes.put(node,true);
		}
	}
	
	public static void mergeNode(Node n){
		Nodes.put(n, true);
	}
	
	public static void mergeLink(Link l){
		Links.put(l, true);
	}
	
	public static void writetojson(){
		jsonString = "{\\\"nodes\\\":[";
		boolean isfirstnode = true;
		for(Node n : Nodes.keySet()){
			String label = null;
			for(Label lbl : n.getLabels()){
				label = lbl.name();
				break;
			}
			if(isfirstnode){
				jsonString += "{";
				isfirstnode = false;
			}
			else{
				jsonString += ",{";
			}
			//get the properties
			boolean isfirst = true;
			jsonString += "\\\"ID\\\":"+"\\\""+n.getProperty(mtdt.getuniqueproperty(label))+"\\\"";
			jsonString += ",\\\"DISPLAY\\\":"+"\\\""+n.getProperty(mtdt.getdisplayproperty(label))+"\\\"";
			jsonString += ",\\\"LABEL\\\":"+"\\\""+label+"\\\"";
			/*for(String prop : n.getPropertyKeys()){
            	if(isfirst){
            		jsonString += "\\\""+prop+"\\\":"+"\\\""+n.getProperty(prop)+"\\\"";
            		isfirst=false;
            	}
            	else{
            		jsonString += ",\\\""+prop+"\\\":"+"\\\""+n.getProperty(prop)+"\\\"";
            	}
            }*/
			//if(MyNodes.indexOf(n) != -1)	jsonString += ",\\\"Iamconnected\\\":"+"\\\"yes\\\"";
			//if(MySuggestions.indexOf(n) != -1)	jsonString += ",\\\"Suggestion\\\":"+"\\\"yes\\\"";
			jsonString += "}";
		}
		jsonString += "],\\\"links\\\":[";
		boolean isfirstlink = true;
		//for(int i=0;i<Links.size();i++){
		for(Link l: Links.keySet()){
			if(isfirstlink){
				jsonString += "{";
				isfirstlink = false;
			}
			else{
				jsonString += ",{";
			}
			jsonString += "\\\"from\\\": \\\"" + l.from + "\\\",\\\"to\\\": \\\"" + l.to + "\\\",\\\"type\\\": \\\"" + l.linktype +"\\\",\\\"length\\\": " + l.linkDistance + "}";
		}
		jsonString += "]}";
		//System.out.println(jsonString);
	}


}
