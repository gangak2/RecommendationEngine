package com.database.interaction;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class moviePageServlet
 */
@WebServlet("/moviePageServlet")
public class moviePageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public moviePageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userid = (String)request.getSession().getAttribute("activeuserid");
		String movieid = (String)request.getParameter("searchid");
		/*if(movieid==null)
		{
			movieid=(String) request.getAttribute("searchid");
		}*/
		Movie moviedetails = new Movie(movieid);
		MovieResponse userResponse = RecommendationDBQueries.getUserResponse(userid,movieid);
		List<MovieResponse> responseFromTheFollowingCircle = RecommendationDBQueries.getResponseFromTheFollowingCircle(userid, movieid);
		request.setAttribute("moviedetails", moviedetails);
		request.setAttribute("userresponse", userResponse);
		request.setAttribute("responseFromTheFollowingCircle", responseFromTheFollowingCircle);
		RequestDispatcher rd = request.getRequestDispatcher("movie.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}