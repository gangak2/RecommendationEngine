package com.database.interaction;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class addNewProductConnection
 */
@WebServlet("/addNewProductConnection")
public class addNewProductConnection extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addNewProductConnection() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String tagID = (String)request.getParameter("tagSearch");
		String productID= (String)request.getParameter("productid");
		String productLabel= (String)request.getParameter("productlabel");
		System.out.println(tagID + "\t "+ productID);
		RecommendationDBQueries.updateProductConnection(productLabel, productID, "TAG", tagID, "tag");
		
		/*request.setAttribute("searchfilterlabel", productLabel);
		request.setAttribute("searchid", productID);*/
		
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
