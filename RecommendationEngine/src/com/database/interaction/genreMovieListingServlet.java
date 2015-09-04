package com.database.interaction;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class genreMovieListingServlet
 */
@WebServlet("/genreMovieListingServlet")
public class genreMovieListingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public genreMovieListingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String genre = (String)request.getParameter("genre");
		String user = (String)request.getSession().getAttribute("activeuserid");
		request.setAttribute("watchedmoviesinthisgenre", RecommendationDBQueries.getUserWatchedMoviesInGenreList(user, genre));
		request.setAttribute("moviesinthisgenre", RecommendationDBQueries.getMoviesInGenreList(genre));
		RequestDispatcher rd = request.getRequestDispatcher("genreWiseMovieListing.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
