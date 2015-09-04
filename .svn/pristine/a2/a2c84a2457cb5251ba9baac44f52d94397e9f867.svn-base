/**
 * 
 */

$(document).ready(function () {
    		var suggestionsfromdatabase = new Bloodhound({
            	datumTokenizer: function(datum){
            		return Bloodhound.tokenizers.whitespace(datum.searchable);
            	},
            	queryTokenizer: Bloodhound.tokenizers.whitespace,
            	remote: {
            		url: 'autocompletemovieservlet?prefix=%PREFIX&filter=%FILTER',
    			    replace: function(url,query){
    			    	$('#search').addClass('searching');
    			    	var filter = $('#searchfilterlabel').val();
    			    	return url.replace('%PREFIX', query).replace('%FILTER',filter);
    			  	},
    			  	ajax: {
    			  		type: 'get',
    			  		beforeSend: function() {
    		  	        	$("#search").addClass('searching');
    		  	      	},
    		  	      	complete: function() {
    		  	        	$("#search").removeClass('searching');
    		  	      }
    			  	},
    			  	filter: function(parsedResponse) {
    					var dataset = [];
    					for (i = 0; i < parsedResponse.length; i++) {
    						dataset.push({
    							searchable: parsedResponse[i].searchable,
    	                        id: parsedResponse[i].id,
    	                        label: parsedResponse[i].label,
    	                        sid: parsedResponse[i].sid
    						});
    					}
    					console.log(dataset);
    					$("#search").removeClass('searching');
    					return dataset;
    				}
            	}
            });
    		
            	var tagSuggestionsfromdatabase = new Bloodhound({
                	datumTokenizer: function(datum){
                		return Bloodhound.tokenizers.whitespace(datum.searchable);
                	},
                	queryTokenizer: Bloodhound.tokenizers.whitespace,
                	remote: {
                		url: 'autoCompleteTagServlet?filter=TAG&prefix=%PREFIX',
                		replace: function(url,query){
        			    	return url.replace('%PREFIX', query);
                		},
                		ajax: {
        			  		type: 'get',
        			  		beforeSend: function() {
        		  	        	$("#search").addClass('searching');
        		  	      	},
        		  	      	complete: function() {
        		  	        	$("#search").removeClass('searching');
        		  	      }
        			  	},
        			  	filter: function(parsedResponse) {
        					var dataset = [];
        					for (i = 0; i < parsedResponse.length; i++) {
        						
        					
        						dataset.push({
        	                        id: parsedResponse[i].id
        						});
        					}
        					console.log(dataset);
        					$("#search").removeClass('searching');
        					return dataset;
        				}
                	}
            	
            	
            });
            	tagSuggestionsfromdatabase.initialize();
            	
    		$('#supersearch').typeahead({
    	        hint: false,
    	        highlight: true,
    	        minLength: 3
    	    	},
    	    	{
    			  source: suggestionsfromdatabase.ttAdapter(),
    			  display: 'searchable',
    			  templates: {
    				  suggestion: Handlebars.compile('<div><h5>{{searchable}}&nbsp;<p class="pull-right">{{sid}}</p></h5><p>In {{label}}</p></div>'),
    				  footer: '<div style="border-top: 1px solid #ccc; text-align: right;"><p>Top 5 suggestions from the database</p></div>',
    				  empty: '<div style="border-top: 1px solid #ccc; border-bottom: 1px solid #ccc; margin-right: 2%; margin-left: 2%;"><p>Nothing found in the database, Perhaps you have misspelt something? You can still hit search though.</p></div>'
    			  },
    			  onselect: function(item){
    				  console.log(item);
    			  }
    			}).on('typeahead:selected', function (obj, datum) {
    				$('#searchid').val(datum.id);
    				$('#globalsearchform').submit();
    			});
    		
    		
    		$('#tagSearch').typeahead(
    				{
    	    	        hint: false,
    	    	        highlight: true,
    	    	        minLength: 2
    	    	    	},
    	    	    	{
    	    			  source: tagSuggestionsfromdatabase.ttAdapter(),
    	    			  display: 'id',
    	    			  templates: {
    	    				  suggestion: Handlebars.compile('<div><h5>{{id}}</h5></div>'),
    	    				  empty: '<div style="border-top: 1px solid #ccc; border-bottom: 1px solid #ccc; margin-right: 2%; margin-left: 2%;"><p>Nothing found in the database, Perhaps you have misspelt something? You can still hit search though.</p></div>'
    	    			  },
    	    			  onselect: function(item){
    	    				  console.log(item);
    	    			  }
    	    			}).on('typeahead:selected', function (obj, datum) {
    	    				$('#newTagForm').submit();
    	    			}
    		);
    		
    		
    	    	
    	    	$('#tagSearch').keydown(function(e){
    	    		var key =e.which;
    	    		if(key==13)
    	        	{
    	        		$('#newTagForm').submit();
    	        	}
    	    		
    	    	});
    	
    		
    		//When the user selects a value from the dropdown
    		$('#searchFilter li').on('click',function(event){
    			$('#searchFilter li').removeClass('selected');
    			var val = $(this).attr('value');
    			$('#searchfilterlabel').val($(this).attr('value'));
    			console.log($(this).html());
    			var filter = this.innerHTML;
    			$('#searchfilter').html($(this).children('a').html());
    			$(this).addClass('selected');
    		});
        });
  