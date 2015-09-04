<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="com.database.interaction.*,java.util.*"%>
<!DOCTYPE html>
<%
if(request.getSession().getAttribute("activeuserid")!=null){
	String userid = (String)request.getSession().getAttribute("activeuserid");
	User userDetails = (User)request.getSession().getAttribute("activeuserdetails");
	Book book = (Book)request.getAttribute("currentbook");
	ProductResponse userResponse = (ProductResponse)request.getAttribute("userresponse");
	List<ProductResponse> responseFromTheFollowingCircle = (List<ProductResponse>)request.getAttribute("responseFromTheFollowingCircle");
	double avgRating = 0;
	for(ProductResponse mr: responseFromTheFollowingCircle){
		avgRating += Double.parseDouble(mr.getrating());
	}
	if(responseFromTheFollowingCircle.size() > 0)	avgRating /= responseFromTheFollowingCircle.size();
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
        <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
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
                    		<button class="btn btn-default" type="submit">
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
                <div class="row" id="movie">
                    <div class="col-md-12 portlets">
                        <div class="panel panel-default">
                            <div class="panel-heading clearfix">
                                <h4 class="col-lg-6"><strong><%=book.getTitle() %>&nbsp;</strong><span class="label label-info"><%=book.getPublicationYear() %></span><span class="badge pull-right"><%=book.getAvgRating() %>&nbsp; | <%=book.getNoOfRatings() %> votes</span></h4><%if(!book.checkIfUserHasRead(userid, "read")&&!book.checkIfUserHasRead(userid, "wishlisted"))out.println("<form action=\"productInterestServlet\"><input type=\"hidden\" id=\"productlabel\" name=\"productlabel\" value=\"BOOK\"><input type=\"hidden\" id=\"searchid\" name=\"searchid\" value=\""+ book.getid() +"\"><button class=\"btn btn-primary pull-right\" type=\"submit\">Add to wishlist</i></button><form>"); %>
                            </div>
                            <div class="panel-body">

                                <div class="col-lg-6" id="moviedetails">
                                    <h4><strong>Details</strong></h4>
                                    <div class="col-lg-3">
                                    	<img src="<%=book.getLargeImageURL() %>" class="img-responsive" alt="Responsive image">
                                    </div>
                                    <div class="col-lg-9">
                                    	<table class="table table-advance" id="courseProgress">
	                                        <tbody>
	                                            <tr style="background: #e2e9f3;">
	                                                <td width="35%">Author</td>
	                                                <td width="65%"><%=book.getAuthor() %></td>
	                                            </tr>
	                                            <tr style="background: #e2e9f3;">
	                                                <td width="35%">Publisher</td>
	                                                <td width="65%"><%=book.getPublisher() %></td>
	                                            </tr>
	                                            <tr>         
	                                                <td>Your rating</td>
	                                                <td width="65%"><a href="#" class="editable editable-click" id="rating"><%=userResponse.getrating()==null?"":userResponse.getrating() %></a> out of 10</td>
	                                            </tr>
	                                            <tr>         
	                                                <td>Your Comment</td>
	                                                <td width="65%"><a href="#" class="editable editable-click" id="comment"><%=userResponse.getcomment()==null?"":userResponse.getcomment() %></a></td>
	                                            </tr>
	                                        </tbody>
	                                    </table>
                                    </div>
                                </div>

                                <div class="col-lg-6" id="friendsactivity">
                                    <h4><strong>Response from the users you are following</strong><span class="badge pull-right"><%=avgRating==0?"no ratings":avgRating %></span></h4>
                                    <table class="table table-advance" id="courseProgress">
                                        <tbody>
                                        	<%
                                            	if(responseFromTheFollowingCircle.size() == 0){
                                            		out.println("<tr>");
                                            		out.println("<td>No one from the circle has seen this movie</td>");
                                            		out.println("</tr>");
                                            	}
                                            	else{
                                            		for(ProductResponse mr: responseFromTheFollowingCircle){
                                                		out.println("<tr>");
                                                		out.println("<div class=\"panel panel-default col-lg-12\">");
                                                		out.println("<h4><a href=\"userPageServlet?searchid="+mr.getUser().getid()+"\"><strong>"+mr.getUser().getfirstname() + " " + mr.getUser().getlastname()+"</strong></a> rated <strong>"+mr.getrating()+"</strong> out of 10</h4>");
                                                		out.println("<p>"+mr.getcomment()+"</p>");
                                                		out.println("</div>");
                                                		out.println("</tr>");
                                                	}
                                            	}
	                                        %>
                                        </tbody>
                                    </table>
                                </div>
								
								<%
								List<Book> suggestedBooks = book.getSuggestedBooks(userid);
								%>
                                <div class="col-lg-6" id="recommendations">
                                    <h4><strong>People who read this book also read</strong></h4>
                                    <table class="table table-advance" id="courseProgress">
                                        <tbody>
                                        	<%
                                        		out.println("<tr>");
                                            	if(suggestedBooks.size() == 0){
                                            		out.println("<td>Not enough activity</td>");
                                            	}
                                            	else{
                                        			out.println("<td>");
                                            		for(Book b: suggestedBooks){
                                                		out.println("<div class=\"col-lg-2\">");
                                                		out.println("<a href=\"bookPageServlet?searchid="+b.getid()+"\"><img src=\""+b.getSmallImageURL()+"\" class=\"img-responsive\" alt=\"Responsive image\"></a>");
                                                		out.println("</div>");
                                                	}
                                            		out.println("</td>");
                                            	} 
                                            	out.println("</tr>");
                                            %>
                                        </tbody>
                                    </table>
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
        $('#rating').editable({
	        type: "select",
	        title: "Select a number",
        	prepend: "not selected",
	        source: [
	            {value: 1, text: '1'},
	            {value: 2, text: '2'},
	            {value: 3, text: '3'},
	            {value: 4, text: '4'},
	            {value: 5, text: '5'},
	            {value: 6, text: '6'},
	            {value: 7, text: '7'},
	            {value: 8, text: '8'},
	            {value: 9, text: '9'},
	            {value: 10, text: '10'}
	        ],
	        url: 'productResponseServlet',
	        send: "always",
	        params: function(params){
	        	return {
	        		label : "BOOK",
	        		id : <%=book.getid()%>,
	        		property : params.name,
	        		value : params.value,
	        		relation: "read"
	        	}
	        }
    }); 
        $('#comment').editable({
        	type: "textarea",
        	title: "Write your comment",
        	showbuttons: 'bottom',
        	url: 'productResponseServlet',
	        send: "always",
	        params: function(params){
	        	return {
	        		label : "BOOK",
	        		id : <%=book.getid()%>,
	        		property : params.name,
	        		value : params.value,
	        		relation: "read"
	        	}
	        }
    });
    </script>
    
</body>
<%
	}
	else{
		response.sendRedirect("index.jsp");
	}
%>
</html>
