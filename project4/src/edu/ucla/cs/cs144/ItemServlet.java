package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xml.sax.InputSource;
import org.w3c.dom.Document;
import javax.xml.parsers.*;
import javax.xml.xpath.*;


import edu.ucla.cs.cs144.SearchResult;
import edu.ucla.cs.cs144.AuctionSearchClient;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        AuctionSearchClient asc = new AuctionSearchClient();
        PrintWriter out = response.getWriter();
        String id = request.getParameter("id");
        
        String xmlInfo = asc.getXMLDataForItemId(id);
        
        //resp(response, xmlInfo);
        parseXML(response, xmlInfo);
        
    }
    
    private void resp(HttpServletResponse resp, String msg)
    throws IOException {
		PrintWriter out = resp.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println(msg);
		out.println("</body>");
		out.println("</html>");
	}
    
    private void parseXML(HttpServletResponse resp, String xml) throws IOException
    {
        
        PrintWriter out = resp.getWriter();
        out.println("<html>");
		out.println("<body>");
        InputSource source = new InputSource(new StringReader(xml));
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try{
        db = dbf.newDocumentBuilder();
        }
        catch(javax.xml.parsers.ParserConfigurationException xp)
        {
            System.out.println(xp);
        }
        Document document = null;
        
        try{
        document =db.parse(source);
        }
        catch(org.xml.sax.SAXException xs)
        {
            System.out.println(xs);
        }
        
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        
        String itemID = null;
        String name = null;
        String category = null;
        String currently = null;
        String first_bid = null;
        String numbids = null;
        String location = null;
        String country = null;
        String started = null;
        String ends = null;
        String seller = null;
        String description = null;
        
        try{
        itemID = xpath.evaluate("/Item/@ItemID", document);
        name = xpath.evaluate("/Item/Name", document);
        category = xpath.evaluate("/Item//Category", document);
        currently = xpath.evaluate("/Item/Currenetly", document);
        first_bid = xpath.evaluate("/Item/First_Bid", document);
        numbids = xpath.evaluate("/Item/Number_Of_Bids", document);
        location = xpath.evaluate("/Item/Location", document);
        country = xpath.evaluate("/Item/Country", document);
        started = xpath.evaluate("/Item/Started", document);
        ends = xpath.evaluate("/Item/Ends", document);
        seller = xpath.evaluate("/Item/Seller/@UserID", document);
        description = xpath.evaluate("/Item/Description", document);
        }
        catch (javax.xml.xpath.XPathExpressionException xp)
        {
            System.out.println(xp);
        }
        out.println(itemID + "<br>");
        out.println(name+ "<br>");
        out.println(category+ "<br>");
        out.println(currently+ "<br>");
        out.println(first_bid+ "<br>");
        out.println(numbids+ "<br>");
        out.println(location+ "<br>");
        out.println(country+ "<br>");
        out.println(started+ "<br>");
        out.println(ends+ "<br>");
        out.println(seller+ "<br>");
        out.println(description+ "<br>");
        
        out.println("</body>");
		out.println("</html>");
        
    }
}
