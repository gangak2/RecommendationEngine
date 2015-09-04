package com.database.interaction;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class landingPageServlet
 */
@WebServlet("/landingPageServlet")
public class landingPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public landingPageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = (String)request.getSession().getAttribute("activeuserid");
		request.setAttribute("toppicks", RecommendationDBQueries.getTopPicks());
		request.setAttribute("genrelist", RecommendationDBQueries.getGenreWiseMoviesList());
		request.setAttribute("topbookpicks", RecommendationDBQueries.getTopBookPicks()); 
		request.setAttribute("otherSimilarUsersInterest", RecommendationDBQueries.getMovieSuggestionBySimilarityWithOtherUsers(username, "MOVIE"));
		RequestDispatcher rd = request.getRequestDispatcher("landing.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
