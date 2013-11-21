package edu.ucla.cs.cs144;

import java.lang.Object;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import javax.servlet.ServletContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;

import org.xml.sax.InputSource;
import java.util.ArrayList;

import edu.ucla.cs.cs144.SearchResult;
import edu.ucla.cs.cs144.AuctionSearchClient;
import edu.ucla.cs.cs144.ItemInfo;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        AuctionSearchClient asc = new AuctionSearchClient();
        PrintWriter out = response.getWriter();
        String id = request.getParameter("id");
        
        String xmlInfo = asc.getXMLDataForItemId(id);
        
        //resp(response, xmlInfo);
        parseXML(request, response, xmlInfo);
        
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
    
    private void parseXML(HttpServletRequest request, HttpServletResponse resp, String xml) throws IOException, ServletException
    {
        /*
        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<body>");
         */
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        org.w3c.dom.Document doc = null;
        try{
            db = dbf.newDocumentBuilder();
        }
        catch(javax.xml.parsers.ParserConfigurationException xs)
        {
            System.out.println(xs);
        }
        
        try{
            doc = db.parse(new InputSource(new StringReader(xml)));
        }
        catch(org.xml.sax.SAXException xs){
            System.out.println(xs);
        }
        
        org.w3c.dom.Element root = null;
        String itemID = null;
        String name = null;
        StringBuilder category = new StringBuilder();
        String currently = null;
        String first_bid = null;
        String numbids = null;
        String location = null;
        String country = null;
        String started = null;
        String ends = null;
        String seller = null;
        String description = null;
        String cat = null;
        
        NodeList ns = doc.getElementsByTagName("Item");
        root = (Element) ns.item(0);
        itemID = root.getAttribute("ItemID");
        name = doc.getElementsByTagName("Name").item(0).getTextContent();
        NodeList cats = doc.getElementsByTagName("Category");
        for(int i = 0; i < cats.getLength(); i++)
        {
            Element e = (Element) cats.item(i);
            category.append(e.getTextContent() + "|");
        }
        cat = category.toString();
        currently = doc.getElementsByTagName("Currently").item(0).getTextContent();
        first_bid = doc.getElementsByTagName("First_Bid").item(0).getTextContent();
        numbids = doc.getElementsByTagName("Number_of_Bids").item(0).getTextContent();
        
        NodeList bid = doc.getElementsByTagName("Bid");
        ArrayList<ItemInfo> bidHistory = new ArrayList<ItemInfo>();
        for(int i = 0; i < bid.getLength(); i++){
            Element r = (Element) bid.item(i);
            Element userid = (Element) r.getElementsByTagName("Bidder").item(0);
            ItemInfo f = new ItemInfo();
            f.setID(userid.getAttribute("UserID"));
            f.setRating(userid.getAttribute("Rating"));
            f.setLocation(userid.getElementsByTagName("Location").item(0).getTextContent());
            f.setCountry(userid.getElementsByTagName("Country").item(0).getTextContent());
            f.setTime(r.getElementsByTagName("Time").item(0).getTextContent());
            f.setAmount(r.getElementsByTagName("Amount").item(0).getTextContent());
            bidHistory.add(f);
        }
        ItemInfo[] finalBidHistory = bidHistory.toArray(new ItemInfo[bidHistory.size()]);

        location = doc.getElementsByTagName("Location").item(0).getTextContent();
        country = doc.getElementsByTagName("Country").item(0).getTextContent();
        started = doc.getElementsByTagName("Started").item(0).getTextContent();
        ends = doc.getElementsByTagName("Ends").item(0).getTextContent();
        Element sell = (Element) root.getElementsByTagName("Seller").item(0);
        seller = sell.getAttribute("UserID");
        description = doc.getElementsByTagName("Description").item(0).getTextContent();
        
        /*
        out.println(itemID + "<br>");
        out.println(name+ "<br>");
        out.println(category+ "<br>");
        out.println(currently+ "<br>");
        out.println(first_bid+ "<br>");
        out.println(numbids+ "<br>");
        out.println("Bid History <br>");
        for(int i = 0; i < bid.getLength(); i++)
        {
            out.println("Bid Number: " + i + "<br>");
            out.println("Bidder ID: " + finalBidHistory[i].getID() + "<br>");
            out.println("Rating: " + finalBidHistory[i].getRating() + "<br>");
            out.println("Time: " + finalBidHistory[i].getTime() + "<br>");
            out.println("Amount: " + finalBidHistory[i].getAmount() + "<br>");
            out.println("Location: " + finalBidHistory[i].getLocation() + "<br>");
            out.println("Country: " + finalBidHistory[i].getCountry() + "<br>");
        }
        out.println(location+ "<br>");
        out.println(country+ "<br>");
        out.println(started+ "<br>");
        out.println(ends+ "<br>");
        out.println(seller+ "<br>");
        out.println(description+ "<br>");
         */
        request.setAttribute("itemID", itemID);
        request.setAttribute("name", name);
        request.setAttribute("category", category);
        request.setAttribute("currently", currently);
        request.setAttribute("first_bid", first_bid);
        request.setAttribute("numbids", numbids);
        request.setAttribute("Bid History", finalBidHistory);
        request.setAttribute("location", location);
        request.setAttribute("country", country);
        request.setAttribute("started", started);
        request.setAttribute("ends", ends);
        request.setAttribute("seller", seller);
        request.setAttribute("description", description);
        
        String url = "/eBay/getItem.jsp";
        
        ServletContext sc = getServletContext();
        RequestDispatcher rd = sc.getRequestDispatcher(url);
        
        rd.forward(request,resp);
        
        //out.println("</body>");
		//out.println("</html>");
        
    }
}
