package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Integer;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ucla.cs.cs144.SearchResult;
import edu.ucla.cs.cs144.AuctionSearchClient;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        AuctionSearchClient asc = new AuctionSearchClient();
        PrintWriter out = response.getWriter();
        String q = request.getParameter("q");
        int numResultsToReturn = Integer.parseInt(request.getParameter("numResultsToReturn"));
        int numResultsToSkip = Integer.parseInt(request.getParameter("numResultsToSkip"));
        SearchResult[] results = asc.basicSearch(q, numResultsToSkip, numResultsToReturn);
        
        request.setAttribute("searchresult", results);
        String url = "/eBay/keywordSearch.jsp";
        ServletContext sc = getServletContext();
        RequestDispatcher rd = sc.getRequestDispatcher(url);
        
        rd.forward(request,response);
        
        
        /*
        out.println("<html>");
		out.println("<body>");
        out.println("<table border>");
        out.println("<tr>");
        out.println("<th>Item Name</th>");
        out.println("<th>Item ID</th>");
        for(SearchResult sr: results)
        {
            String name = sr.getName();
            String id = sr.getItemId();
            out.println("<tr>");
            out.println("<td>" + name + "</td>");
            out.println("<td>" + id + "</td>");
            out.println("</tr>");
            
        }
		out.println("</body>");
		out.println("</html>");
        
        */
        
        
    }
}
