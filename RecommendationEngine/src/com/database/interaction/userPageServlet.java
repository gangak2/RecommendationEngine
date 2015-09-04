package com.database.interaction;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class userPageServlet
 */
@WebServlet("/userPageServlet")
public class userPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public userPageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String activeuserid = (String)request.getSession().getAttribute("activeuserid");
		String userid = (String)request.getParameter("searchid");
		System.out.println(userid + " " + activeuserid);
		if(activeuserid.equals(userid)){
			RequestDispatcher rd = request.getRequestDispatcher("authenticateduser.jsp");
			rd.forward(request, response);
		}
		else{
			request.setAttribute("userdetails", RecommendationDBQueries.getUserDetails(userid));
			request.setAttribute("userwatchedmovies", RecommendationDBQueries.getUserWatchedMoviesList(userid));
			RequestDispatcher rd = request.getRequestDispatcher("user.jsp");
			rd.forward(request, response);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
