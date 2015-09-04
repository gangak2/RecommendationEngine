package com.database.interaction;

import org.neo4j.graphdb.Relationship;

public class Link {
	public String from;
	public String to;
	public String linktype;
	public int linkDistance;
	
	public Link(){
		
	}
	
	public Link(String s,  String t, String type, int distance){
		this.from = s;
		this.to = t;
		this.linktype = type;
		this.linkDistance = distance;
	}
	
	public void setsource(String s){
		this.from = s;
	}
	
	public void settarget(String t){
		this.to = t;
	}
	
	
	public void setlinktype(String t){
		this.linktype = t;
	}
	
	public void setlinkDistance(int distance){
		this.linkDistance = distance;
	}
}

