package com.database.interaction;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class searchInDataBaseMovieServlet
 */
@WebServlet("/searchInDataBaseMovieServlet")
public class searchInDataBaseMovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public searchInDataBaseMovieServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nodefilter = (String) request.getParameter("nodefilter");
		String nodeid = (String) request.getParameter("nodeid");
		String jsonString = null;
		if(!nodefilter.isEmpty() && !nodeid.isEmpty()){
			jsonString = RecommendationDBQueries.findInDatabase(nodefilter,nodeid);
		}
		System.out.println(jsonString);
		response.setContentType("text/plain");  
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().write(jsonString);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
