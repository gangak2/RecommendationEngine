package com.database.interaction;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UpvoteOrDownvoteServlet
 */
@WebServlet("/UpvoteOrDownvoteServlet")
public class UpvoteOrDownvoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpvoteOrDownvoteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	String userID=(String)request.getParameter("userID");
	String productID=(String)request.getParameter("productID");
	String productLabel=(String)request.getParameter("productLabel");
	String tag=(String)request.getParameter("tag");
	Boolean responseType;
	if(request.getParameter("responseType").equals("true"))
	{
		responseType=true;
	}
	else
		responseType=false;
		
	System.out.println("userID"+userID+"\tproductID"+productID+"\tproductLabel"+productLabel+"\ttag"+tag+"\tresponseType"+responseType);
	RecommendationDBQueries.updateProductConnectionResponseUpvoteOrDownvote(productLabel, productID, "TAG", tag, "tag", userID, responseType);
	
	RequestDispatcher rd = request.getRequestDispatcher("redirectServlet?searchfilterlabel="+productLabel+"&searchid="+productID);
	rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
