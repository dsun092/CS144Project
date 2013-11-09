package edu.ucla.cs.cs144;

import java.sql.*;
import java.io.IOException;
import java.lang.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import java.util.Date;
import java.util.Iterator;
import java.text.SimpleDateFormat;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchConstraint;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearch implements IAuctionSearch {

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
         * Your code will need to reference the directory which contains your
	 * Lucene index files.  Make sure to read the environment variable 
         * $LUCENE_INDEX with System.getenv() to build the appropriate path.
	 *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
    private QueryParser parser = null;
    private IndexSearcher searcher = null;
    public AuctionSearch() throws IOException{
        searcher = new IndexSearcher(System.getenv("LUCENE_INDEX") + "/index");
        parser = new QueryParser("description", new StandardAnalyzer());
    }
    
    public Hits performSearch(String queryString)
    throws IOException, ParseException {
        Query query = parser.parse(queryString);
        Hits hits = searcher.search(query);
        return hits;
    }
    
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) throws IOException{
        
        Hits hits = null;
        String newQuery = "name:" + query + " OR description:" + query + " OR category:" + query;
        try{
            hits = performSearch(newQuery);
            System.out.println(hits.length());
        } catch (ParseException pe)
        {
            System.out.println(pe);
        }
        
        SearchResult[] rs = null;
        if(numResultsToSkip > hits.length())
        {
            return new SearchResult[0];
        }
        
        if(numResultsToReturn > 0 && numResultsToReturn < hits.length())
        {
            rs = new SearchResult[numResultsToReturn];
            int i = numResultsToSkip;
            int j = 0;
            while(i < hits.length() && j < numResultsToReturn)
            {
                //System.out.println(i + " " + j + " " + numResultsToReturn);
                Document doc = hits.doc(i);
                String id = doc.get("itemID");
                String name = doc.get("name");
                //System.out.println(id + " " + name);

                rs[j] = new SearchResult(id, name);
                j++;
                i++;
            }
        }
        else
        {
            rs = new SearchResult[hits.length()];
            int i = numResultsToSkip;
            int j = 0;
            while(i < hits.length())
            {
                Document doc = hits.doc(i);
                String id = doc.get("itemID");
                String name = doc.get("name");
                //System.out.println(id + " " + name);
                rs[j] = new SearchResult(id, name);
                j++;
                i++;
            }
        }
		return rs;
	}

	public SearchResult[] advancedSearch(SearchConstraint[] constraints, 
			int numResultsToSkip, int numResultsToReturn) {
		
		return new SearchResult[0];
	}

	public String escape(String s) {
		String newString = "";
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '\"')
				newString = newString + "&quot;";
			else if (s.charAt(i) == '\'')
				newString = newString + "&apos;";
			else if (s.charAt(i) == '&')
				newString = newString + "&amp;";
			else if (s.charAt(i) == '<')
				newString = newString + "&lt;";
			else if (s.charAt(i) == '>')
				newString = newString + "&gt;";
			else
				newString = newString + s.charAt(i);
		}
		return newString;
	}
	public String getXMLDataForItemId(String itemId) {
		// Item table
		String itemName = "";
		String currently = ""; 
		String buyPrice = "";
		String firstBid = "";
	        String numBids = "";
		String started = "";
		String ends = "";
		String sellerID = "";
		String description = "";

		// Category table
		String category = "";
		
		// User table
		String sellerrating = "";
		String bidderrating = "";
		String location = "";
		String country = "";

		// Bid table
		String bidder = "";
		String time = "";
		String amount = "";

		// Misc variables
		String t = "  ";
		String t2 = "    ";
		String t3 = "      ";
		String t4 = "        ";
		String t5 = "          ";


		String xmlResult = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
				    "<!DOCTYPE Items SYSTEM \"items.dtd\">\n" +
				    t + "<Item ItemID=" + itemId + ">";

		/*
		float currently, buyPrice, firstBid;
		int numBids;
		*/
		Connection con = null;	

		try {
			// Connect to the database using JDBC
			Class.forName("com.mysql.jdbc.Driver");		
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CS144", "cs144", "");
			Statement stmt = con.createStatement();

			// Execute the SELECT statement to retrieve the item information
			ResultSet rs = stmt.executeQuery("SELECT *, DATE_FORMAT(started, \"%b-%d-%m %H:%i:%s\") AS s, DATE_FORMAT(ends, \"%b-%d-%m %H:%i:%s\") AS e FROM Item WHERE itemID = "+itemId);

			if (rs.next()) {
				do {
					itemName = rs.getString("name");	
					currently = rs.getString("currently");
					buyPrice = rs.getString("buyPrice");
					firstBid = rs.getString("firstBid");
					numBids = rs.getString("numberOfBids");
					started = rs.getString("s");
					ends = rs.getString("e");
					sellerID = rs.getString("sellerID");
					description = rs.getString("description");
				} while (rs.next());
				rs.close();
				
				// #1 Name of the item
				xmlResult = xmlResult + "\n" + t2 +"<Name>" + escape(itemName) + "</Name>";


				rs = stmt.executeQuery("SELECT * FROM Category WHERE itemID = "+itemId);
				while (rs.next()) {
					// #2 Categories of the item
					category = rs.getString("category");	
					xmlResult = xmlResult + "\n" +
							t2 + "<Category>" + escape(category) + "</Category>";
				}
				rs.close();

				// #3 Current bid price of the item
				xmlResult = xmlResult + "\n" + 
					t2 + "<Currently>$" + currently + "</Currently>"; 

				// #4 Buy Price of the item (if it exists)
				if (buyPrice != null) {
					xmlResult = xmlResult + "\n" +
						t2 + "<Buy_Price>$" + buyPrice + "</Buy_Price>";
				}

				// #5, 6 First bid and Number of bids of the item
				xmlResult = xmlResult + "\n" +
					t2 + "<First_Bid>$" + firstBid + "</First_Bid>\n" + 
					t2 + "<Number_of_Bids>" + numBids + "<Number_of_Bids>";

				// #7 Bids on the item
				rs = stmt.executeQuery("SELECT *, DATE_FORMAT(time, \"%b-%d-%m %H:%i:%s\") AS t FROM Bid WHERE itemID = " + itemId + " ORDER BY time");
			
				if (rs.next()) {	
					xmlResult = xmlResult + "\n" + t2 + "<Bids>";
					do {
						bidder = rs.getString("userID");	
						time = rs.getString("t");
						amount = rs.getString("amount");

						Statement stmt2 = con.createStatement();
						ResultSet rs2 = stmt2.executeQuery("SELECT * FROM User WHERE userID = \"" + bidder + "\"");
						rs2.next();
						bidderrating = rs2.getString("rating");
						location = rs2.getString("location");
						country = rs2.getString("country");

						xmlResult = xmlResult + "\n" +
								t3 + "<Bid>\n" +
								t4 + "<Bidder UserID=\"" + escape(bidder) + "\" Rating=\"" + bidderrating + "\">\n" +
								t5 + "<Location>" + escape(location) + "</Location>\n" +
								t5 + "<Country>" + escape(country) + "</Country>\n" +
								t4 + "</Bidder>\n" +
								t4 + "<Time>" + time + "</Time>\n" +
								t4 + "<Amount>$" + amount + "</Amount>\n" +
								t3 + "</Bid>";	
						rs2.close();
						stmt2.close();
					} while (rs.next()); 
				}
				rs.close();
				xmlResult = xmlResult + "\n" + t2 + "</Bids>";

				// #8~ Location, Country, Start date, End date, Seller info and Description of the item
				rs = stmt.executeQuery("SELECT * FROM User WHERE userID = \"" + sellerID + "\"");	
				rs.next();
				sellerrating = rs.getString("rating");
				location = rs.getString("location");
				country = rs.getString("country");
				xmlResult = xmlResult + "\n" + 
					t2 + "<Location>" + escape(location) + "</Location>\n" + 
					t2 + "<Country>" + escape(country) + "</Country>\n" +
					t2 + "<Started>" + started + "</Started>\n" + 
					t2 + "<Ends>" + ends + "</Ends>\n" +
					t2 + "<Seller UserID=\"" + escape(sellerID) + "\" Rating=\"" + sellerrating + "\"/>\n" +
					t2 + "<Description>" + escape(description) + "</Description>\n" + t + "</Item>";
				}
			else {
				return "";
			}
			

			rs.close();
			stmt.close();
			con.close();
					
		}
	        catch (ClassNotFoundException ex){
			System.out.println(ex);
	       	} 
		catch (SQLException ex){
		        System.out.println("SQLException caught");
		        System.out.println("---");
	    		while ( ex != null ){
				System.out.println("Message   : " + ex.getMessage());
				System.out.println("SQLState  : " + ex.getSQLState());
				System.out.println("ErrorCode : " + ex.getErrorCode());
				System.out.println("---");
				ex = ex.getNextException();
			}
	        }
			
		return xmlResult;
	}
	
	public String echo(String message) {
		return message;
	}

}
