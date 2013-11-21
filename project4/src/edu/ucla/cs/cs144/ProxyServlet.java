package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
	PrintWriter out = response.getWriter();
	URL google_suggest = null;
	BufferedReader reader = null;
	StringBuilder stringBuilder;

	String q = request.getParameter("q");

	try
	{
	  google_suggest = new URL("http://google.com/complete/search?output=toolbar&q="+q);
	  HttpURLConnection connection = (HttpURLConnection) google_suggest.openConnection();
	  connection.setRequestMethod("GET");
	  connection.setReadTimeout(15*1000);
	  connection.connect();

	  reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	  stringBuilder = new StringBuilder();

	  String line = null;
	  while ((line = reader.readLine()) != null)
	  {
		stringBuilder.append(line + "\n");
	  }
	  out.println(stringBuilder.toString());
	  out.close();
	  
	}
	catch (IOException e)
	{
	  e.printStackTrace();
	  throw e;
	}	
	
    }
}
