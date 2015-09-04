package com.database.interaction;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class productResponseServlet
 */
@WebServlet("/productResponseServlet")
public class productResponseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public productResponseServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("update property called");
		String userid = (String)request.getSession().getAttribute("activeuserid");
		String label = (String)request.getParameter("label");
		String id = (String)request.getParameter("id");
		String property = (String)request.getParameter("property");
		String value = (String)request.getParameter("value");
		String relation = (String)request.getParameter("relation");
		RecommendationDBQueries.updateProductResponse(userid, label, id, property, value, relation);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}
