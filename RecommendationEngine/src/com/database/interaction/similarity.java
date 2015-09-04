package com.database.interaction;

import java.io.File;
import java.io.IOException;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.io.fs.DefaultFileSystemAbstraction;
import org.neo4j.kernel.StoreLocker;
import org.neo4j.kernel.impl.util.StringLogger;

public class similarity implements Runnable{

	static String DB_PATH;
	static GraphDatabaseService GDBService=null;
	static boolean isConnectionSet = false;
	
	public similarity(GraphDatabaseService gdbs){
		GDBService = gdbs;
	}
	
	public GraphDatabaseService getDBConnection(){
		return GDBService;
	}
	@Override
	public void run() {
		RecommendationDBQueries RDBQs = new RecommendationDBQueries(GDBService);
		while(true)
		{
	    System.out.println("updating similarity");
	    RDBQs.updatingSimilarity();
		 try {
		       Thread.sleep(60000);
		      } catch (InterruptedException e) {
		        e.printStackTrace();
		      }
		}
		}
	
	
	/*public static void DBConnection(){
		System.out.println("connectibg database");
		DB_PATH = "C:\\Program Files\\Neo4j Community\\bin\\movie1.db";
		StoreLocker lock = new StoreLocker(new DefaultFileSystemAbstraction());
        lock.checkLock(new File(DB_PATH));
        try {
            lock.release();
            GDBService = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(DB_PATH).setConfig( GraphDatabaseSettings.node_keys_indexable, "ID,SEARCHABLE" ).
            	    setConfig( GraphDatabaseSettings.node_auto_indexing, "true" ).
            	    setConfig( GraphDatabaseSettings.relationship_auto_indexing, "true" ).newGraphDatabase();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
		
		isConnectionSet = true;
	}
	*/

}
