package edu.ucla.cs.cs144;

import java.util.Calendar;
import java.util.Date;
import java.io.IOException;


import edu.ucla.cs.cs144.AuctionSearch;
import edu.ucla.cs.cs144.SearchResult;
import edu.ucla.cs.cs144.SearchConstraint;
import edu.ucla.cs.cs144.FieldName;

public class AuctionSearchTest {
	public static void main(String[] args1) throws IOException
	{
		AuctionSearch as = new AuctionSearch();

		String message = "Test message";
		String reply = as.echo(message);
		System.out.println("Reply: " + reply);
<<<<<<< HEAD
		/*
		String query = "superman";
		SearchResult[] basicResults = as.basicSearch(query, 1, 2);
=======
		
		String query = "star trek";
		SearchResult[] basicResults = as.basicSearch(query, 0, 1);
>>>>>>> bc3b7a8634d58665b0896e4c229c33941e54daeb
		System.out.println("Basic Seacrh Query: " + query);
		System.out.println("Received " + basicResults.length + " results");
        int i = 0;
		for(SearchResult result : basicResults) {
			System.out.println(result.getItemId() + ": " + result.getName());
            System.out.println(i);
            i++;
<<<<<<< HEAD
		}*/
		
=======
		}
		/*
>>>>>>> bc3b7a8634d58665b0896e4c229c33941e54daeb
		SearchConstraint constraint =
		    new SearchConstraint(FieldName.EndTime, "Dec-14-01 21:00:05");
        SearchConstraint constraint2 =
        new SearchConstraint(FieldName.ItemName, "pan");
        
		SearchConstraint[] constraints = {constraint};
		SearchResult[] advancedResults = as.advancedSearch(constraints, 0, 20);
		System.out.println("Advanced Seacrh");
		System.out.println("Received " + advancedResults.length + " results");
        int i = 0;
		for(SearchResult result : advancedResults) {
			System.out.println(result.getItemId() + ": " + result.getName());
		}
<<<<<<< HEAD
		/*
		String itemId = "1497595357";
		String item = as.getXMLDataForItemId(itemId);
		System.out.println("XML data for ItemId: " + itemId);
		System.out.println(item);
         */
=======
	*/	
		String itemId = "1497497054";
		String item = as.getXMLDataForItemId(itemId);
		System.out.println("XML data for ItemId: " + itemId);
		System.out.println(item);
>>>>>>> bc3b7a8634d58665b0896e4c229c33941e54daeb

		// Add your own test here
	}
}
