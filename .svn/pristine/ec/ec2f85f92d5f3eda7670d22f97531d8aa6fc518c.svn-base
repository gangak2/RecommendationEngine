package com.database.interaction;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class authenticationServlet
 */
@WebServlet("/authenticationServlet")
public class authenticationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public authenticationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = (String) request.getParameter("username");
		String password = (String) request.getParameter("password");
		RecommendationDBQueries RDBQs = new RecommendationDBQueries(similarity.GDBService);
		if(RDBQs.userAuthentication(username, password)){
			HttpSession session=request.getSession();
			session.setAttribute("activeuserid", username);
			session.setAttribute("activeuserdetails", RecommendationDBQueries.getUserDetails(username));
			request.setAttribute("toppicks", RecommendationDBQueries.getTopPicks());
			request.setAttribute("genrelist", RecommendationDBQueries.getGenreWiseMoviesList());
			request.setAttribute("topbookpicks", RecommendationDBQueries.getTopBookPicks()); 
			request.setAttribute("otherSimilarUsersInterest", RecommendationDBQueries.getMovieSuggestionBySimilarityWithOtherUsers(username, "MOVIE"));
			RequestDispatcher rd = request.getRequestDispatcher("landing.jsp");
			rd.forward(request, response);
		}
		else{
			RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}

}
