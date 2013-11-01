/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;

class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    static String formatTime(String time)
    {
        if(time.equals("NULL"))
            return "";
        SimpleDateFormat timeform = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat input = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        
        String result = "";
        try { result = timeform.format(input.parse(time)); }
        catch (ParseException e) {
            System.out.println("This method should work for all date/" +
                               "time strings you find in our data.");
            System.exit(20);
        }
        return result;
        
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        Element x = doc.getDocumentElement();
        
        getItems(x);
        getBid(x);
        getUser(x);
	getCategory(x);
        
    }
    
    public static void getUser(Element n)
    {
        File userFile = new File("./user.dat");
        PrintWriter pw = null;
        try{
            pw = new PrintWriter(new FileWriter(userFile, true));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        
        NodeList itemNodes = n.getElementsByTagName("Item");
        for(int i = 0; i < itemNodes.getLength(); i++)
        {
            Element itemElement = (Element) itemNodes.item(i);
            
            String UserID = getElementByTagNameNR(itemElement, "Seller").getAttributes().item(1).getNodeValue();
            String Rating = getElementByTagNameNR(itemElement, "Seller").getAttributes().item(0).getNodeValue();
            String Location = getElementText(getElementByTagNameNR(itemElement, "Location"));
            String Country = getElementText(getElementByTagNameNR(itemElement, "Country"));
            
            pw.println(UserID + columnSeparator +
                       Rating + columnSeparator +
                       Location + columnSeparator +
                       Country);
        }
        for(int i = 0; i < itemNodes.getLength(); i++)
        {
            Element itemElement = (Element) itemNodes.item(i);
            NodeList bidNodes = ((Element)itemElement).getElementsByTagName("Bid");
            
            for(int j = 0; j < bidNodes.getLength(); j++)
            {
                Element bid = (Element)bidNodes.item(j);
                String Rating = getElementByTagNameNR(bid, "Bidder").getAttributes().item(0).getNodeValue();
                String UserID = getElementByTagNameNR(bid, "Bidder").getAttributes().item(1).getNodeValue();
                String Location;
                String Country;

		if (getElementByTagNameNR(getElementByTagNameNR(bid, "Bidder"), "Location") != null)
                {
		     Location = getElementText(getElementByTagNameNR(getElementByTagNameNR(bid, "Bidder"), "Location"));
                }
                else
                {
                    Location = "\\N";
                }
                if (getElementByTagNameNR(getElementByTagNameNR(bid, "Bidder"), "Country") != null)
                {
		    Country = getElementText(getElementByTagNameNR(getElementByTagNameNR(bid, "Bidder"), "Country"));
                }
                else
                {
                    Country = "\\N";
                }
                pw.println(UserID + columnSeparator +
                           Rating + columnSeparator +
                           Location + columnSeparator +
                           Country);
            }
        }
        
        pw.close();
    }
    
    public static void getBid(Element n)
    {
        File bidFile = new File("./bid.dat");
        PrintWriter pw = null;
        try{
            pw = new PrintWriter(new FileWriter(bidFile, true));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        
        NodeList itemNodes = n.getElementsByTagName("Item");
        for(int i = 0; i < itemNodes.getLength(); i++)
        {
            Element itemElement = (Element) itemNodes.item(i);
            String ItemID = itemElement.getAttributes().item(0).getNodeValue();
            NodeList bidNodes = ((Element)itemElement).getElementsByTagName("Bid");
            
            for(int j = 0; j < bidNodes.getLength(); j++)
            {
                Element bid = (Element)bidNodes.item(j);
                String UserID = getElementByTagNameNR(bid, "Bidder").getAttributes().item(1).getNodeValue();
                
                String Time = formatTime(getElementText(getElementByTagNameNR(bid, "Time")));
                
                String Amount = strip(getElementText(getElementByTagNameNR(bid, "Amount")));
                
                
                pw.println(ItemID + columnSeparator +
                           UserID + columnSeparator +
                           Time + columnSeparator +
                           Amount);
            }
        }
        pw.close();
    }
    
    public static void getItems(Element n)
    {
        File itemFile = new File("./item.dat");
        PrintWriter pw = null;
        try{
            pw = new PrintWriter(new FileWriter(itemFile, true));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        
        NodeList itemNodes = n.getElementsByTagName("Item");
        for(int i = 0; i < itemNodes.getLength(); i++)
        {
            Element itemElement = (Element) itemNodes.item(i);
            String ItemID = itemElement.getAttributes().item(0).getNodeValue();
            String Name = getElementText(getElementByTagNameNR(itemElement, "Name"));
            
            String Currently;
            if(getElementByTagNameNR(itemElement, "Currently")!= null)
            {
                Currently = strip(getElementText(getElementByTagNameNR(itemElement, "Currently")));
            }
            else
            {
                Currently = "\\N";
            }
            
            String Buy_Price;
            if(getElementByTagNameNR(itemElement, "Buy_Price") != null)
            {
                Buy_Price = strip(getElementText(getElementByTagNameNR(itemElement, "Buy_Price")));
            }
            else
            {
                Buy_Price = "\\N";
            }
                                  
            String First_Bid;
            if(getElementByTagNameNR(itemElement, "First_Bid") != null)
            {
                First_Bid = strip(getElementText(getElementByTagNameNR(itemElement, "First_Bid")));
            }
            else{
                First_Bid = "\\N";
            }
                                  
            String Number_Of_Bids = getElementText(getElementByTagNameNR(itemElement, "Number_of_Bids"));
            
            String Location = getElementText(getElementByTagNameNR(itemElement, "Location"));
            String Country = getElementText(getElementByTagNameNR(itemElement, "Country"));
            String Started = formatTime(getElementText(getElementByTagNameNR(itemElement, "Started")));
            String Ends = formatTime(getElementText(getElementByTagNameNR(itemElement, "Ends")));
            String SellerID = getElementByTagNameNR(itemElement, "Seller").getAttributes().item(1).getNodeValue();
            
            String Description;
            if(getElementByTagNameNR(itemElement, "Description") != null)
            {
                if(getElementText(getElementByTagNameNR(itemElement, "Description")).length() > 4000)
                {
                    Description = getElementText(getElementByTagNameNR(itemElement, "Description")).substring(0, 3999);
                }
                else
                {
                    Description = getElementText(getElementByTagNameNR(itemElement, "Description"));
                }
            }
            else
            {
                Description = "\\N";
            }

            pw.println(ItemID + columnSeparator +
                       Name + columnSeparator +
                       Currently + columnSeparator +
                       Buy_Price + columnSeparator +
                       First_Bid + columnSeparator +
                       Number_Of_Bids + columnSeparator +
                       Started + columnSeparator +
                       Ends + columnSeparator +
                       SellerID + columnSeparator +
                       Description);
        }
        pw.close();
    }
    public static void getCategory(Node n) {
	File categoryFile = new File("./category.dat");	
	PrintWriter pw = null;
	try 
	{
	    pw = new PrintWriter(new FileWriter(categoryFile, true));
	}
	catch (IOException e){
	    e.printStackTrace();
	}
	
	// Retrieve the itemNodes list containing all items
	Element[] itemNodes = getElementsByTagNameNR((Element)n, "Item"); 
	for (int i=0; i<itemNodes.length; i++)
	{
//	    String itemID = getElementText(getElementByTagNameNR(itemNodes[i],"itemID"));
	    String itemID = itemNodes[i].getAttributes().item(0).getNodeValue();
	    Element[] categoryNodes = getElementsByTagNameNR(itemNodes[i], "Category");
	    for (int j=0; j<categoryNodes.length; j++)
	    {
		String category = getElementText(categoryNodes[j]);
	    	pw.println(itemID + columnSeparator + category);
	    }
	}
	pw.close();
    }


    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
    }
}
