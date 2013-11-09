package edu.ucla.cs.cs144;

import java.sql.*;
import java.io.IOException;
import java.lang.*;
import java.util.*;

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
import java.text.DateFormat;

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
        parser = new QueryParser("content", new StandardAnalyzer());
    }
    
    public Hits performSearch(String queryString)
    throws IOException, ParseException {
        Query query = parser.parse(queryString);
        Hits hits = searcher.search(query);
        return hits;
    }
    
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) throws IOException{
        //simpified advanced and basic search to only search on constraints
        //new function called Search will perform searches on Text attributes and both basic and advanced search will refer to it
        String newQuery = constructQueryBasic(query);
        return Search(newQuery, numResultsToSkip, numResultsToReturn);
	}

	public SearchResult[] advancedSearch(SearchConstraint[] constraints, 
			int numResultsToSkip, int numResultsToReturn) throws IOException{
        //create db connection and statement
        boolean onlyText = true;
		for(SearchConstraint sc : constraints)
        {
            //checks to see if the SearchConstraint has any constraints outside of basic text. If there are, then break out of loop and return false.
            if(sc.getFieldName().equals("SellerId") || sc.getFieldName().equals("BuyPrice") || sc.getFieldName().equals("BidderID") || sc.getFieldName().equals("EndTime")){
                onlyText = false;
                break;
            }
        }
        if(onlyText)
        {
            String query = constructQueryAdvanced(constraints);
            System.out.println(query);
            SearchResult[] rs = Search(query, numResultsToSkip, numResultsToReturn);
            return rs;
        }
        else{
            String sqlQuery = constructSQLQuery(constraints);
            String luceneQuery = constructQueryAdvanced(constraints);
            System.out.println(sqlQuery);
            System.out.println(luceneQuery);
            SearchResult[] sqlRS = null;
            SearchResult[] luceneRS = null;
            boolean foundLucene = false;
            if(luceneQuery != null && !luceneQuery.isEmpty())
            {
                luceneRS = Search(luceneQuery, numResultsToSkip, numResultsToReturn);
                foundLucene = true;
            }
            sqlRS = SQLSearch(sqlQuery);
            if(foundLucene)
            {
                SearchResult[] finSearch = findIntersect(sqlRS, luceneRS, numResultsToSkip, numResultsToReturn);
                return finSearch;
            }
            else
            {
                return sqlRS;
            }
        }
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
		String t = "";
		String t2 = "  ";
		String t3 = "    ";
		String t4 = "      ";
		String t5 = "        ";


		String xmlResult = "<Item ItemID=" + itemId + ">";

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
			ResultSet rs = stmt.executeQuery("SELECT *, DATE_FORMAT(started, \"%b-%d-%y %H:%i:%s\") AS s, DATE_FORMAT(ends, \"%b-%d-%y %H:%i:%s\") AS e FROM Item WHERE itemID = "+itemId);

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
				rs = stmt.executeQuery("SELECT *, DATE_FORMAT(time, \"%b-%d-%y %H:%i:%s\") AS t FROM Bid WHERE itemID = " + itemId + " ORDER BY time");
			
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
					t2 + "<Description>" + escape(description) + "</Description>\n</Item>";
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
    
    /**********************************************************/
    //helper functions
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
    public String constructQueryBasic(String query)
    {
        String newQuery = "name:" + query + " OR description:" + query + " OR category:" + query;
        return newQuery;
    }
    public String constructQueryAdvanced(SearchConstraint[] constraints)
    {
        StringBuilder query = new StringBuilder();
        boolean isFirst = true;
        for(SearchConstraint sc : constraints)
        {
            if(sc.getFieldName().equals("ItemName"))
            {
                if(isFirst){
                    query.append("name:" + sc.getValue());
                    isFirst = false;
                }
                else{
                    query.append(" AND name:" + sc.getValue());
                }
            }
            if(sc.getFieldName().equals("Category"))
            {
                if(isFirst){
                    query.append("category:" + sc.getValue());
                    isFirst = false;
                }
                else{
                    query.append(" AND category:" + sc.getValue());
                }
            }
            if(sc.getFieldName().equals("Description"))
            {
                if(isFirst){
                    query.append("description:" + sc.getValue());
                    isFirst = false;
                }
                else{
                    query.append(" AND description:" + sc.getValue());
                }
            }
        }
        String finalQuery = query.toString();
        if (finalQuery != null && !finalQuery.isEmpty())
        {
            return finalQuery;
        }
        return null;
        
    }
    public String constructSQLQuery(SearchConstraint[] constraints){
        
        StringBuilder query = new StringBuilder("SELECT distinct i.itemID, i.name FROM Item i USE INDEX(IndexOnSeller, IndexOnBuyPrice, IndexOnEnds), Bid b USE INDEX(IndexOnBidder) WHERE ");
        boolean isFirst = true;
         
        for(SearchConstraint sc : constraints)
        {
            if(sc.getFieldName().equals("SellerId")){
                if(isFirst)
                {
                query.append("sellerID LIKE " + "'" + sc.getValue() + "'");
                isFirst = false;
                }
                else
                {
                query.append(" AND sellerID LIKE " + "'" + sc.getValue() + "'");
                }
            }
            else if(sc.getFieldName().equals("BuyPrice")){
                if(isFirst)
                {
                    query.append("buyPrice = " + sc.getValue());
                    isFirst = false;
                }
                else
                {
                    query.append(" AND buyPrice = " + sc.getValue());
                }
            }
            else if(sc.getFieldName().equals("EndTime")){
                if(isFirst)
                {
                    query.append("ends LIKE " + "'" + formatTime(sc.getValue()) + "'");
                    isFirst = false;
                }
                else
                {
                    query.append(" AND ends LIKE " + "'" + formatTime(sc.getValue()) + "'");
                }
            }
            else if(sc.getFieldName().equals("BidderId")){
                if(isFirst)
                {
                    query.append("userID LIKE " + "'" + sc.getValue() + "'" + " AND i.itemID = b.itemID");
                    isFirst = false;
                }
                else
                {
                    query.append(" AND userID LIKE " + "'" + sc.getValue() + "'" + " AND i.itemID = b.itemID");
                }
            }
        }
        
        String finalQuery = query.toString();
        if (finalQuery != null && !finalQuery.isEmpty())
        {
            return finalQuery;
        }
        return null;

        
    }
    public SearchResult[] Search(String query, int numResultsToSkip, int numResultsToReturn) throws IOException
    {
        Hits hits = null;
        try{
            hits = performSearch(query);
            System.out.println("Size of searchArray:" + hits.length());
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
    public SearchResult[] SQLSearch(String query) throws IOException{
        //TODO
        Connection conn = null;
        Statement stmt = null;
        ArrayList<SearchResult> results = new ArrayList<SearchResult>();
        
        try{
            conn = DbManager.getConnection(true);
            stmt = conn.createStatement();
        } catch (SQLException se)
        {
            System.out.println(se);
        }
        String itemID, name;
        try{
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next())
            {
                itemID = Integer.toString(rs.getInt("itemID"));
                name = rs.getString("name");
                SearchResult search = new SearchResult(itemID, name);
                results.add(search);
            }
            
        } catch (SQLException ex)
        {
            System.out.println(ex);
        }
        System.out.println("Size of sqlArray: " + results.size());
        SearchResult[] finalResult = results.toArray(new SearchResult[results.size()]);
        return finalResult;
    }
    public SearchResult[] findIntersect(SearchResult[] sqlRS, SearchResult[] luceneRS, int numResultsToSkip, int numResultsToReturn) throws IOException{
        /*
         For every SearchResult in sqlRS and luceneRS, we take out the itemID and place them into lists of SearchResults. 
         Then we find the interesct of the two lists and place them and their corresponding itemID into SearchResult.
         return the new searchresult
         */
        ArrayList<SearchResult> sqlArray = new ArrayList<SearchResult>(Arrays.asList(sqlRS));
        ArrayList<SearchResult> luceneArray = new ArrayList<SearchResult>(Arrays.asList(luceneRS));
        //output the two array
        
        //we will combine the intersection of sqlArray and luceneArray into finalResult
        ArrayList<SearchResult> result = new ArrayList<SearchResult>();
        for(SearchResult s : sqlArray){
            for(SearchResult t : luceneArray)
            {
                if(s.getItemId().equals(t.getItemId())){
                    System.out.println("Match: " + s.getName() + " " + s.getItemId());
                    result.add(s);
                }
            }
        }
        SearchResult[] rs = null;
        if(numResultsToSkip > result.size())
        {
            return new SearchResult[0];
        }
        if(numResultsToReturn > 0 && numResultsToReturn < result.size())
        {
            rs = new SearchResult[numResultsToReturn];
            int i = numResultsToSkip;
            int j = 0;
            while(i < result.size() && j < numResultsToReturn)
            {
                SearchResult temp = result.get(i);
                String id = temp.getItemId();
                String name = temp.getName();
                
                rs[j] = new SearchResult(id, name);
                j++;
                i++;
            }
        }
        else
        {
            rs = new SearchResult[result.size()];
            int i = numResultsToSkip;
            int j = 0;
            while(i < result.size())
            {
                SearchResult temp = result.get(i);
                String id = temp.getItemId();
                String name = temp.getName();
                
                rs[j] = new SearchResult(id, name);
                j++;
                i++;
            }
        }
        return rs;
    }
    
    static String formatTime(String time)
    {
        if(time.equals("NULL"))
            return "";
        SimpleDateFormat timeform = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat input = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        String result = null;
        try{
        result = timeform.format(input.parse(time));
        }
        catch (java.text.ParseException p)
        {
            System.out.println(p);
        }
        return result;
    }
    
}
