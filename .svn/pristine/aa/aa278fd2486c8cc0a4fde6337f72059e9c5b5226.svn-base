package com.database.interaction;

import java.io.File;
import java.io.IOException;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.io.fs.DefaultFileSystemAbstraction;
import org.neo4j.kernel.StoreLocker;

public class DBConnection {

	
	static String DB_PATH;
	static GraphDatabaseService GDBService=null;
	public DBConnection(){
		
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
		

		
	}
	
	public GraphDatabaseService getDBConnection(){
		return this.GDBService;
		
	}
}
