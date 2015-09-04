package com.database.interaction;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class productInterestServlet
 */
@WebServlet("/productInterestServlet")
public class productInterestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public productInterestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("update property called");
		String userid = (String)request.getSession().getAttribute("activeuserid");
		String productlabel = (String)request.getParameter("productlabel");
		String productid = (String)request.getParameter("searchid");
		System.out.println(userid +"\t"+ productlabel +"\t"+productid);
		RecommendationDBQueries.updateProductInterest(userid, productlabel, productid);
		//request.setAttribute("searchfilterlabel", productlabel);
		RequestDispatcher rd = request.getRequestDispatcher("redirectServlet?searchfilterlabel="+productlabel);
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
