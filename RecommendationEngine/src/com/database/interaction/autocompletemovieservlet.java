package com.database.interaction;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.neo4j.shell.util.json.JSONArray;
import org.neo4j.shell.util.json.JSONParser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.stream.JsonReader;

import scala.util.parsing.json.JSONObject;

/**
 * Servlet implementation class autocompletemovieservlet
 */
@WebServlet("/autocompletemovieservlet")
public class autocompletemovieservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public autocompletemovieservlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String prefix = (String)request.getParameter("prefix");
		String filter = (String)request.getParameter("filter");
		String json;
		String	finalJson="[";
		if(filter.equals("PRODUCT"))
		{
			
			json=RecommendationDBQueries.autoCompleteSuggestor(filter, prefix,DBConnection.GDBService);
			//JsonReader reader = new JsonReader(new StringReader(json.substring(1, json.length()-1)));
			JsonReader reader = new JsonReader(new StringReader(json));
			reader.setLenient(true);
			JsonElement jelement=new JsonParser().parse(reader);
			JsonArray jarray=jelement.getAsJsonArray();
			//String sID=jobject.get("sid").getAsString();
			//JsonArray arr= jobject.getAsJsonArray("sid");
		
			for(JsonElement objList:jarray)
			{
				String sID=((JsonObject) objList).get("sid").getAsString();
				sID=sID.replace("[", "");
				sID=sID.replace("]", "");
				Pattern p=Pattern.compile("(.*?)=(.*?)=(.*?)}");
				Matcher m=p.matcher(sID);
				List<String> tag=new ArrayList<String>();
				while(m.find())
				{
					if(!tag.contains(m.group(3)))
					tag.add(m.group(3));
				}
				
				System.out.println(sID);
				String str ="";
;				for(String s:tag)
				{    
					if(!s.equals("FLIPKART_TREE"))
					str=str+s+",";
					
					
					System.out.print(str);
				}
				if(str.endsWith(",")){
					str = str.substring(0, str.length()-1);
				}
						/*JsonReader reader1 = new JsonReader(new StringReader(sID));
				reader1.setLenient(true);
				JsonElement jelement1=new JsonParser().parse(reader1);
				JsonArray jarray1=jelement1.getAsJsonArray();
				for(JsonElement objList1:jarray1)
				{
					String tags=((JsonObject) objList1).get("nodeName").getAsString();
					System.out.println(tags);
				}*/
			
			finalJson+= "{\"searchable\":\""+ ((JsonObject) objList).get("searchable").getAsString() + "\",\"id\":\""+ ((JsonObject) objList).get("id").getAsString() + "\",\"label\":\"" + ((JsonObject) objList).get("label").getAsString() + "\",\"sid\":\"" + str + "\"},";
			
			System.out.println();
			}
			
			
			System.out.println();
			if(finalJson.endsWith(",")){
				finalJson = finalJson.substring(0, finalJson.length()-1);
			}
			finalJson+="]";
		}
		
		else
		finalJson=RecommendationDBQueries.autoCompleteSuggestor(filter, prefix,DBConnection.GDBService);
		//System.out.println(json);
		response.setContentType("application/json");  
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().write(finalJson);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
