package com.database.interaction;

public class countTag {

	public Long getUpvoteCount(String productLabel,String productID,String connectionLabel,String connectionID,String connectionRelation){
		
		Long count=null;
		count=RecommendationDBQueries.getCount(productLabel, productID, connectionLabel, connectionID, connectionRelation, "upvote");
		return count;
	}
	
	public Long getDownvoteCount(String productLabel,String productID,String connectionLabel,String connectionID,String connectionRelation){
		
		Long count=null;
		count=RecommendationDBQueries.getCount(productLabel, productID, connectionLabel, connectionID, connectionRelation, "downvote");
		return count;
	}
}
