package com.database.interaction;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class redirectServlet
 */
@WebServlet("/redirectServlet")
public class redirectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public redirectServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String label;
		String id;
		/*if((String)request.getAttribute("searchfilterlabel") != null)
		{
			label = (String)request.getAttribute("searchfilterlabel");
		}
		else*/
			label = (String)request.getParameter("searchfilterlabel");
		
		/*if((String)request.getAttribute("searchid") != null)
		{
			id = (String)request.getAttribute("searchid");
		}
		else*/
			id = (String)request.getParameter("searchid");
		
		//request.setAttribute("searchid", id);
		System.out.println(label +"\t"+ id );
		if(!id.isEmpty()){
			if(label.equals("MOVIE")){
				RequestDispatcher rd  = request.getRequestDispatcher("moviePageServlet?searchid="+id);
				rd.forward(request, response);
			}
			else if(label.equals("User")){
				RequestDispatcher rd  = request.getRequestDispatcher("userPageServlet");
				rd.forward(request, response);
			}
			else if(label.equals("BOOK")){
				RequestDispatcher rd  = request.getRequestDispatcher("bookPageServlet");
				rd.forward(request, response);
			}
		}
		else{
			RequestDispatcher rd  = request.getRequestDispatcher("index.jsp");
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
