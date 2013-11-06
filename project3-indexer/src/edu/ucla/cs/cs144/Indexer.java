package edu.ucla.cs.cs144;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.IOException;
import java.lang.*;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;

public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }
    private IndexWriter indexWriter = null;
    public IndexWriter getIndexWriter(boolean create) throws IOException {
        if (indexWriter == null) {
            indexWriter = new IndexWriter(System.getenv("LUCENE_INDEX") + "/index",
                                          new StandardAnalyzer(),
                                          create);
        }
        return indexWriter;
    }
    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
    }
    public void rebuildIndexes() throws IOException{

        Connection conn = null;

        // create a connection to the database to retrieve Items from MySQL
	try {
	    conn = DbManager.getConnection(true);
	} catch (SQLException ex) {
	    System.out.println(ex);
	}
        getIndexWriter(true);

	/*
	 * Add your code here to retrieve Items using the connection
	 * and add corresponding entries to your Lucene inverted indexes.
         *
         * You will have to use JDBC API to retrieve MySQL data from Java.
         * Read our tutorial on JDBC if you do not know how to use JDBC.
         *
         * You will also have to use Lucene IndexWriter and Document
         * classes to create an index and populate it with Items data.
         * Read our tutorial on Lucene as well if you don't know how.
         *
         * As part of this development, you may want to add 
         * new methods and create additional Java classes. 
         * If you create new classes, make sure that
         * the classes become part of "edu.ucla.cs.cs144" package
         * and place your class source files at src/edu/ucla/cs/cs144/.
	 * 
	 */
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
        } catch (SQLException ex)
        {
            System.out.println(ex);
        }
        
        String itemID;
        String name, description;
        StringBuilder categories = new StringBuilder();
        
    
        try{
            ResultSet rs = stmt.executeQuery("SELECT i.itemID, i.name, i.description, c.category FROM Item i, Category c WHERE i.itemID = c.itemID");
            while(rs.next())
            {
                itemID = Integer.toString(rs.getInt("itemID"));
                name = rs.getString("name");
                description = rs.getString("description");
                categories.append(rs.getString("category"));
                while(rs.next());
                      {
                          if(itemID == Integer.toString(rs.getInt("itemID")))
                          {
                              categories.append(" " + rs.getString("category"));
                          }
                          else
                          {
                              rs.previous();
                              break;
                          }
                      }
                String finalCategory = categories.toString();
                Items item = new Items(itemID, name, description, finalCategory);
                indexItem(item);
            }
        } catch (SQLException ex)
        {
            System.out.println(ex);
        }
    
        
        closeIndexWriter();


        // close the database connection
	try {
	    conn.close();
	} catch (SQLException ex) {
	    System.out.println(ex);
	}
    }
    
    public void indexItem(Items item)throws IOException
    {
        IndexWriter writer = getIndexWriter(false);
        Document doc = new Document();
        doc.add(new Field("itemID", item.getID(), Field.Store.YES, Field.Index.NO));
        doc.add(new Field("name", item.getName(), Field.Store.YES, Field.Index.TOKENIZED));
        doc.add(new Field("description", item.getDescription(), Field.Store.NO, Field.Index.TOKENIZED));
        doc.add(new Field("category", item.getCategory(), Field.Store.NO, Field.Index.TOKENIZED));
        writer.addDocument(doc);
    }
    
    public static void main(String args[]) throws IOException{
        Indexer idx = new Indexer();
        try{
        idx.rebuildIndexes();
        } catch (IOException io)
        {
            System.out.println(io);
        }
    }
}
