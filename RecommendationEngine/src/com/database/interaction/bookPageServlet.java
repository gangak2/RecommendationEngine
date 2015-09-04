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
 * Servlet implementation class bookPageServlet
 */
@WebServlet("/bookPageServlet")
public class bookPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public bookPageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userid = (String)request.getSession().getAttribute("activeuserid");
		String productid = (String)request.getParameter("searchid");
		Book book = new Book(productid);
		ProductResponse userResponse = book.getUserResponse(userid);
		List<ProductResponse> responseFromTheFollowingCircle = book.getUserFollowingCircleResponse(userid);
		request.setAttribute("currentbook", book);
		request.setAttribute("userresponse", userResponse);
		request.setAttribute("responseFromTheFollowingCircle", responseFromTheFollowingCircle);
		RequestDispatcher rd = request.getRequestDispatcher("book.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
