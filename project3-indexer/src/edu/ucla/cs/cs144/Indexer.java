package edu.ucla.cs.cs144;

import java.sql.*;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;

public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }
 
    public void rebuildIndexes() {

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
        Statement stmt = conn.createStatement();
        int itemID;
        String name, description, sellerID;
        Timestamp started;
        Timestamp ends;
        BigDecimal buyPrice;
        
        ResultSet rs = stmt.executeQuery("SELECT * FROM Item");
        while(rs.next())
        {
            itemID = rs.getInt("itemID");
            name = rs.getString("name");
            description = rs.getString("description");
            Items item = new Items(itemID, name, description);
            indexItem(item);
        }
        
        
        closeIndexWriter();


        // close the database connection
	try {
	    conn.close();
	} catch (SQLException ex) {
	    System.out.println(ex);
	}
    }
    
    public void indexItem(Items item)
    {
        IndexWriter writer = getIndexWriter(false);
        Document doc = new Document();
        doc.add(new Field("itemID", item.getID(), Field.Store.YES, Field.Index.UN_TOKENIZED));
        doc.add(new Field("name", item.getName(), Field.Store.YES, Field.Index.TOKENIZED));
        doc.add(new Field("description", item.getDescription(), Field.Store.NO, Field.Index.TOKENIZED));
        writer.addDocument(doc);
    }
    
    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }   
}
