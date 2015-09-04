<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="com.database.interaction.*,java.util.*"%>
<!DOCTYPE html>
<%
	if(request.getSession().getAttribute("activeuserdetails")!= null){
		User userDetails = (User)request.getSession().getAttribute("activeuserdetails");
		List<Movie> movieWishList = userDetails.getAllWishlistedMovies();
		List<Movie> watchedMovies = userDetails.getAllWatchedMovies();
		List<User> followersList = (List<User>)request.getSession().getAttribute("activeuserfollowerslist");
		List<User> followingList = (List<User>)request.getSession().getAttribute("activeuserfollowinglist");
%>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Recommendation Engine</title>

    <!-- Bootstrap Core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/bootstrap.css" rel="stylesheet">
    <!-- MetisMenu CSS -->
    <link href="css/metis/metisMenu.min.css" rel="stylesheet">

    <!-- Timeline CSS -->
    <link href="css/timeline.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="css/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- Editable -->
    <link href="css/bootstrap-editable.css" rel="stylesheet"/>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
 	
 	<style>
 	.searching {    
	    background-color: #ffffff;
	    background-image: url("images/searching.GIF");
	    background-size: 25px 25px;
	    background-position:right;
	    background-repeat: no-repeat;
	}
	.twitter-typeahead{
		width: 100%;
	}
	.typeahead, .tt-query, .tt-hint {
		border: 2px solid #CCCCCC;
		border-radius: 8px;
		font-size: 12px;
		height: 15px;
		line-height: 15px;
		outline: medium none;
		padding: 8px 12px;
		width: 100%;
	}
	.typeahead {
		background-color: #FFFFFF;
	}
	.typeahead:focus {
		border: 2px solid #0097CF;
	}
	.tt-query {
		box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset;
	}
	.tt-hint {
		color: #999999;
	}
	.tt-menu {
		background-color: #FFFFFF;
		border: 1px solid rgba(0, 0, 0, 0.2);
		border-radius: 8px;
		box-shadow: 0 5px 10px rgba(0, 0, 0, 0.2);
		margin-top: 12px;
		padding: 8px 0;
		width: 100%;
	}
	.tt-suggestion {
		font-size: 12px;
		line-height: 15px;
		padding: 3px 20px;
	}
	.tt-suggestion.tt-cursor {
		background-color: #0097CF;
		color: #FFFFFF;
	}
	.tt-suggestion p {
		margin: 0;
	}
	
 	</style>
</head>

<body>

    <div id="wrapper">

        <!-- Navigation -->
         
        <nav class="navbar navbar-default navbar-inverse navbar-fixed-top" role="navigation" style="margin-bottom: 0">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="landingPageServlet">Recommendation Engine</a>
            </div>
            <!-- /.navbar-header -->
            <div class="navbar-left col-lg-6" role="search">
                <form action="redirectServlet">
                <div class="input-group" style="margin-top: 8px;">
                    <span class="input-group-btn">
                        <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
                            <span id="searchfilter">&nbsp;Movies&nbsp;</span>
                        </button>
                        <ul class="dropdown-menu" aria-labelledby="dropdownMenu1" id="searchFilter">
                            <li value="MOVIE"><a href="#" >Movies</a></li>
                            <li value="User"><a href="#" >Users</a></li>
                            <li value="BOOK"><a href="#" >Books</a></li>
                        </ul>
                    </span>
                   	<input type="text" class="form-control" placeholder="Search..." id="supersearch">
                    <span class="input-group-btn">
                    		<input type="hidden" name="searchid" id="searchid" value="none">
							<input type="hidden" name="searchfilterlabel" id="searchfilterlabel" value="MOVIE">
                    		<button class="btn btn-default" type="submit" onclick="redirect();">
	                            <i class="fa fa-search"></i>
	                        </button>
                    </span>
                    
                </div>
                </form>
            </div>
            <ul class="nav navbar-top-links navbar-right">
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <i class="fa fa-user fa-fw"></i>  <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-user">
                        <li><a href="activeUserPageServlet"><i class="fa fa-user fa-fw"></i> <%=userDetails.getfirstname() + " " + userDetails.getlastname() %></a>
                        </li>
                        <li class="divider"></li>
                        <li><a href="logoutSession"><i class="fa fa-sign-out fa-fw"></i> Logout</a>
                        </li>
                    </ul>
                    <!-- /.dropdown-user -->
                </li>
                <!-- /.dropdown -->
            </ul>
            <!-- /.navbar-top-links -->
            <!-- /.navbar-static-side -->
        </nav>

        <section id="main-content">
            <section class="wrapper">            
              <!--overview start-->
                <div class="row" id="profile">
                    <div class="col-md-12 portlets">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h4><strong><%=(String)userDetails.getfirstname() + " " + userDetails.getlastname() %></strong></h4>
                            </div>
                            <div class="panel-body">
                                    <div class="col-lg-6" id="userdetails" >
                                        <h4><strong>Profile</strong></h4>
                                        <table class="table table-advance col-lg-6" id="courseProgress">
                                            <tbody>
                                                <tr>
                                                    <td width="35%">First name</td>
                                                    <td width="65%"><a href="#" class="editable editable-click" id="firstname"><%=(String)userDetails.getfirstname()%></a></td>
                                                </tr>
                                                <tr>
                                                    <td width="35%">Last name</td>
                                                    <td width="65%"><a href="#" class="editable editable-click" id="lastname"><%=(String)userDetails.getlastname()%></a></td>
                                                </tr>
                                                <tr>         
                                                    <td>Date of birth</td>
                                                    <td><a href="#" id="dob" data-type="combodate" data-value="1984-05-15" data-format="YYYY-MM-DD" data-viewformat="DD/MM/YYYY" data-template="D / MMM / YYYY" data-pk="1"  data-title="Select Date of birth"></a></td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>

									<div class="col-lg-3" id="followers">
                                        <h4><strong>Followers</strong></h4>
                                        <table class="table table-advance" id="courseProgress">
                                            <tbody>
                                            	<%
	                                            	if(followersList.size() == 0){
	                                            		out.println("<tr>");
	                                            		out.println("<td>No followers yet</td>");
	                                            		out.println("</tr>");
	                                            	}
	                                            	else{
	                                            		for(User u: followersList){
	                                                		out.println("<tr>");
	                                                		out.println("<td><a href=\"userPageServlet?searchid="+u.getid()+"\"><strong>"+ u.getfirstname() + " " + u.getlastname() +" &nbsp;</strong></a><span class=\"badge pull-right\">"+u.getNoOfConnections("MOVIE", "watched")+"</span></td>");
	                                                		out.println("</tr>");
	                                                	}
	                                            	}
	                                            %>
                                            </tbody>
                                        </table>
                                     </div>
                                     
                                     
                                        
                                     <div class="col-lg-3" id="following">
                                        <h4><strong>Following</strong></h4>
                                        <table class="table table-advance" id="courseProgress">
                                            <tbody>
                                            	<%
	                                            	if(followingList.size() == 0){
	                                            		out.println("<tr>");
	                                            		out.println("<td>You havent followed anyone yet</td>");
	                                            		out.println("</tr>");
	                                            	}
	                                            	else{
	                                            		for(User u: followingList){
	                                                		out.println("<tr>");
	                                                		out.println("<td><a href=\"userPageServlet?searchid="+u.getid()+"\"><strong>"+ u.getfirstname() + " " + u.getlastname() +" &nbsp;</strong></a><span class=\"badge pull-right\">"+u.getNoOfConnections("MOVIE", "watched")+"</span></td>");
	                                                		out.println("</tr>");
	                                                	}
	                                            	}
	                                            %>
                                            </tbody>
                                        </table>
                                     </div>
                                     
                                     <div class="col-lg-12" id="container">
                                     	<ul class="nav nav-tabs">
										  <li role="presentation" class="active"><a href="#movies" role="tab" data-toggle="tab"><strong>Movies</strong></a></li>
										  <li role="presentation"><a href="#books" role="tab" data-toggle="tab"><strong>Books</strong></a></li>
										</ul>
                                     	<div class="tab-content">
	                                     	<div class="row tab-pane fade active in" id="movies">
	                                     		  <div class="col-lg-12" id="recommendations">
	                                     		<div class="col-md-12 portlets">
                                                     
			                                        <h4><strong>Suggested movies to watch</strong></h4>
			                                        <table class="table table-advance col-lg-6" id="courseProgress">
			                                            <tbody>
			                                                <%
			                                           //	 out.println("<div class=\"container\">");
			                                                	List<Movie> suggestionList = (List<Movie>)userDetails.getSuggestionsFromFollowingCircle();
				                                            	if(suggestionList.size() == 0){
				                                            		out.println("<tr>");
				                                            		out.println("<td>You dont have enough activity for system to generate recommendationss</td>");
				                                            		out.println("</tr>");
			                                                	/* if(suggestionList.size() == 0){
			                                                    		//out.println("<tr>");
			                                                    		 out.println("<div class=\"row\">");
			                                                    		out.println("No any ratings yet");
			                                                    		//out.println("</tr>");
			                                                    		out.println("</div>");*/
			                                                    	}
				                                            	//}
				                                            	else{
				                                            		out.println("<tr>");
				                                            		for(Movie m: suggestionList){
				                                                		
				                                                		//out.println("<td><a href=\"moviePageServlet?searchid="+m.getid()+"\"><strong>"+m.getTitle()+" &nbsp;</strong></a><span class=\"label label-default\">"+m.getReleaseYear()+"</span><span class=\"badge pull-right\">"+m.getAvgRating()+"</span></td>");
				                                                	out.println("<td><a href=\"moviePageServlet?searchid="+m.getid()+"\"><img src=\""+m.getImageURL()+"\" class=\"img-responsive\" alt=\"Responsive image\"></a><strong>"+m.getTitle()+"</strong></td>");
				                                                		
				                                                	}
				                                            		out.println("</tr>");
				                                            	}
				                                            	
				                                            		//out.println("<h4 style=\"background: #F0FAFF;\" >Higly rated Movies</h4>");
				                                            		//out.println("<td><strong>Book</strong><span class=\"pull-right\">Rating</span></td>");
				                                            		//out.println("</tr>");
				                                            		
				                                            		/* out.println("<div class=\"row\">");
				                                            		for(Movie m: suggestionList){
				                                                		//out.println("<tr>");
				                                                		out.println("<div class=\"col-md-2\">");
				                                                		//out.println("<a href=\"moviePageServlet?searchid="+m.getid()+"\"><img src=\""+m.getPosterURLFromIMDB()+"\" class=\"img-responsive\" alt=\"Responsive image\"></a><strong>"+m.getTitle()+"</strong>");
				                                                		out.println("<a href=\"moviePageServlet?searchid="+m.getid()+"\"></a><strong>"+m.getTitle()+"</strong>");
				                                                		//out.println("</tr>");
				                                                		out.println("</div>");
				                                                	}
				                                            	}*/
				                                            	
				                                            out.println("</div>");
				                                            out.println("</div>");
				                                            %>
			                                            </tbody>
			                                        </table>
			                                    </div>
			                                    <div class="col-lg-12" id="moviespanel">
			                                     	<ul class="nav nav-tabs">
													  <li role="presentation" class="active"><a href="#wishlist" role="tab" data-toggle="tab"><strong>My Wishlist&nbsp;<span class="badge badge-default"><%=movieWishList.size() %></span></strong></a></li>
													  <li role="presentation"><a href="#watchedMovies" role="tab" data-toggle="tab"><strong>My Ratings&nbsp;<span class="badge badge-default"><%=watchedMovies.size() %></span></strong></a></li>
													</ul>
													<div class="tab-content">
								                    	<div class="row tab-pane fade active in" id="wishlist">
															<table class="table table-advance">
					                                            <tbody>
					                                            <%
					                                            	if(movieWishList.size() == 0){
					                                            		out.println("<tr>");
					                                            		out.println("<td>You havent wishlisted any movies yet</td>");
					                                            		out.println("</tr>");
					                                            	}
					                                            	else{
					                                            		for(Movie m: movieWishList){
					                                                		out.println("<tr>");
					                                                		out.println("<td><a href=\"moviePageServlet?searchid="+m.getid()+"\"><strong>"+m.getTitle()+" &nbsp;</strong></a><span class=\"label label-default\">"+m.getReleaseYear()+"</span><span class=\"badge pull-right\">"+m.getAvgRating()+"</span></td>");
					                                                		out.println("</tr>");
					                                                	}
					                                            	}
					                                            %>
					                                            </tbody>
					                                        </table>
														</div>
														<div class="row tab-pane fade" id="watchedMovies">
					                                        <table class="table table-advance">
					                                            <tbody>
					                                            <%
						                                            	if(watchedMovies.size() == 0){
						                                            		out.println("<tr>");
						                                            		out.println("<td>No record of watched movies</td>");
						                                            		out.println("</tr>");
						                                            	}
						                                            	else{
						                                            		for(Movie m: watchedMovies){
						                                                		ProductResponse pr = m.getUserResponse(userDetails.getid());
						                                            			out.println("<tr>");
						                                                		out.println("<div class=\"panel panel-default col-lg-12\">");
						                                                		out.println("<h4><a href=\"moviePageServlet?searchid="+m.getid()+"\"><strong>"+m.getTitle()+"</strong></a> <span class=\"badge pull-right\">"+pr.getrating()+"</span> </h4>");
						                                                		out.println("<p>"+pr.getcomment()+"</p>");
						                                                		out.println("</div>");
						                                                		out.println("</tr>");
						                                                	}
						                                            	}
						                                            %>
					                                            </tbody>
					                                        </table>
			                                    		</div>
													</div>
												</div>
	                                     	</div>
	                                     	
	                                     	<%
	                                     	List<Book> bookWishList = userDetails.getAllWishlistedBooks();
	                                     	List<Book> readBooks = userDetails.getAllReadBooks();
	                                     	%>
	                                     	<div class="row tab-pane fade in" id="books">
	                                     		<div class="col-lg-12" id="recommendations">
			                                        <h4><strong>Suggested books to read</strong></h4>
			                                        <table class="table table-advance" id="courseProgress">
			                                            <tbody>
			                                                <%
			                                                
			                                                	//List<Movie> suggestionList = (List<Movie>)userDetails.getSuggestionsFromFollowingCircle();
				                                            	if(suggestionList.size() == 0){
				                                            		out.println("<tr>");
				                                            		out.println("<td>You dont have enough activity for system to generate recommendationss</td>");
				                                            		out.println("</tr>");
				                                            	}
				                                            	else{
				                                            		for(Movie m: suggestionList){
				                                                		out.println("<tr>");
				                                                		out.println("<td><a href=\"moviePageServlet?searchid="+m.getid()+"\"><strong>"+m.getTitle()+" &nbsp;</strong></a><span class=\"label label-default\">"+m.getReleaseYear()+"</span><span class=\"badge pull-right\">"+m.getAvgRating()+"</span></td>");
				                                                		//out.println("<a href=\"moviePageServlet?searchid="+m.getid()+"\"><img src=\""+m.getPosterURLFromIMDB()+"\" class=\"img-responsive\" alt=\"Responsive image\"></a><strong>"+m.getTitle()+"</strong>");
				                                                		//out.println("<a href=\"bookPageServlet?searchid="+b.getid()+"\"><img src=\""+b.getSmallImageURL()+"\" class=\"img-responsive\" alt=\"Responsive image\"></a><strong>"+b.getTitle()+"");
				                                                		out.println("</tr>");
				                                                	}
				                                            	}
				                                            %>
			                                            </tbody>
			                                        </table>
			                                    </div>
			                                    <div class="col-lg-12" id="bookspanel">
			                                     	<ul class="nav nav-tabs">
													  <li role="presentation" class="active"><a href="#wishlistedbooks" role="tab" data-toggle="tab"><strong>My Wishlist&nbsp;<span class="badge badge-default"><%=bookWishList.size() %></span></strong></a></li>
													  <li role="presentation"><a href="#readbooks" role="tab" data-toggle="tab"><strong>My Ratings&nbsp;<span class="badge badge-default"><%=readBooks.size() %></span></strong></a></li>
													</ul>
													<div class="tab-content">
								                    	<div class="row tab-pane fade active in" id="wishlistedbooks">
															<table class="table table-advance">
					                                            <tbody>
					                                            <%
					                                            	if(bookWishList.size() == 0){
					                                            		out.println("<tr>");
					                                            		out.println("<td>You havent wishlisted any books yet</td>");
					                                            		out.println("</tr>");
					                                            	}
					                                            	else{
					                                            		for(Book b: bookWishList){
					                                                		out.println("<tr>");
					                                                		out.println("<td><a href=\"bookPageServlet?searchid="+b.getid()+"\"><strong>"+b.getTitle()+" &nbsp;</strong></a><span class=\"label label-default\">"+b.getPublicationYear()+"</span><span class=\"badge pull-right\">"+b.getAvgRating()+"</span></td>");
					                                                		out.println("</tr>");
					                                                	}
					                                            	}
					                                            %>
					                                            </tbody>
					                                        </table>
														</div>
														<div class="row tab-pane fade" id="readbooks">
					                                        <table class="table table-advance">
					                                            <tbody>
					                                            <%
					                                            	if(readBooks.size() == 0){
					                                            		out.println("<tr>");
					                                            		out.println("<td>No record of read books</td>");
					                                            		out.println("</tr>");
					                                            	}
					                                            	else{
					                                            		for(Book b: readBooks){
					                                                		ProductResponse pr = b.getUserResponse(userDetails.getid());
					                                            			out.println("<tr>");
					                                                		out.println("<div class=\"panel panel-default col-lg-12\">");
					                                                		out.println("<div class=\"col-lg-1\">");
					                                                		out.println("<img src=\""+b.getThumbNail()+"\" class=\"img-responsive\" alt=\"Responsive image\">");
					                                                		out.println("</div><div class=\"col-lg-11\">");
					                                                		out.println("<h4><a href=\"bookPageServlet?searchid="+b.getid()+"\"><strong>"+b.getTitle()+"</strong></a> <span class=\"badge pull-right\">"+pr.getrating()+"</span> </h4>");
					                                                		out.println("<p>"+pr.getcomment()+"</p></div>");
					                                                		out.println("</div>");
					                                                		out.println("</tr>");
					                                                	}
					                                            	}
					                                            %>
					                                            </tbody>
					                                        </table>
			                                    		</div>
													</div>
												</div>
	                                     	</div>
                                     	</div>
                                     	<div class="tab-content">
	                                     	<div class="row tab-pane fade active in" id="books">
	                                     	</div>
	                                     </div>
                                    </div>
                                      
                            </div>

                        </div>
                    </div>
                </div>

            </section>
        </section>
    </div>
    <!-- jQuery -->
    <script src="js/jquery.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="js/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="js/metisMenu.min.js"></script>


    <!-- Custom Theme JavaScript -->
    <script src="js/sb-admin-2.js"></script>
    <script src="js/bootstrap-editable.min.js"></script>
    <script src="js/moment.min.js"></script>
    
    <!-- Typeahead suggestions -->
    <script src="js/typeahead.js"></script>
    <script src="js/handlebars.js"></script>
    
    <!-- Search bar autocompletor -->
    <script src="js/searchbar.js"></script>
    
    <script>
        $('#firstname').editable({
    			  type: "text",
    			  title: "Enter firstname",
    			  url: 'updateDatabaseServlet',
    			  send: 'always',
    			  params: function (params) {
    			    return {
    			      label: "User",
    			      id: "<%=(String)userDetails.getid()%>",
    			      property: params.name,
    			      value: params.value
    			    };
    			  }
    			});
        $('#lastname').editable({
			  type: "text",
			  title: "Enter lastname",
			  url: 'updateDatabaseServlet',
			  send: 'always',
			  params: function (params) {
			    return {
			      label: "User",
			      id: "<%=(String)userDetails.getid()%>",
			      property: params.name,
			      value: params.value
			    };
			  }
			});
        $('#dob').editable();
        
        function redirect(){
        	
        }
    </script>
</body>
<%
	}
	else{
		response.sendRedirect("index.jsp");
	}
%>
</html>