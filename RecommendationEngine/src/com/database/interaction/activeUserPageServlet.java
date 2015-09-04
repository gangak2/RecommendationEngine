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
 * Servlet implementation class activeUserPageServlet
 */
@WebServlet("/activeUserPageServlet")
public class activeUserPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public activeUserPageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		String userid = (String)session.getAttribute("activeuserid");
		session.setAttribute("activeuserdetails", RecommendationDBQueries.getUserDetails(userid));
		session.setAttribute("activeuserfollowerslist", RecommendationDBQueries.getUserFollowersList(userid));
		session.setAttribute("activeuserfollowinglist", RecommendationDBQueries.getUserFollowingList(userid));
		//response.sendRedirect("authenticateduser.jsp");
		RequestDispatcher rd = request.getRequestDispatcher("authenticateduser.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
