package sentiment;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.spark.sql.catalog.Database;

/**
 *
 * @author Luca Sorrentino
 */
public class OracleDB {
    private Connection connection = null;
    private DatabaseMetaData dbm = null;
    
    
    public OracleDB(String user, String pass){
        System.out.println("Searching for Oracle JDBC driver...");
 
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }

        catch (ClassNotFoundException e) {
            System.out.println("Oracle JDBC driver not found!");
            e.printStackTrace();
            return;
        }

        System.out.println("Oracle JDBC Driver registered.");

        try {
            this.connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", user, pass);
        }

        catch (SQLException e) {
            System.out.println("Connection Failed! Check username and password");
            e.printStackTrace();
            return;
        }

        if (connection != null) {
            System.out.println("Connection to database successful");
            try {
                this.dbm = connection.getMetaData();
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        else {
            System.out.println("Unable to connect to database.");
        }
    }
    
    public void createTable(String sentiment){
        Statement stmt;
        try {
            stmt = this.connection.createStatement();
            String sql = "CREATE TABLE " + sentiment +
                    " ( WORD VARCHAR2(140) NOT NULL"+
                    ", TYPE VARCHAR2(10)"+ 
                    ", FREQUENCY NUMBER(6,0)"+
                    ", GI_NEG NUMBER(1,0)"+
                    ", GI_POS NUMBER(1,0)"+
                    ", HL_negatives NUMBER(1,0)"+
                    ", HL_positives NUMBER(1,0)"+
                    ", listNegEffTerms NUMBER(1,0)"+
                    ", listPosEffTerms NUMBER(1,0)"+
                    ", LIWC_NEG NUMBER(1,0)"+
                    ", LIWC_POS NUMBER(1,0)"+
                    ", afinn FLOAT(6)"+
                    ", anewAro_tab FLOAT(6)"+
                    ", anewDom_tab FLOAT(6)"+
                    ", anewPleas_tab FLOAT(6)"+
                    ", Dal_Activ FLOAT(6)"+
                    ", Dal_Imag FLOAT(6)"+
                    ", Dal_Pleas FLOAT(6)"+
                    ", sentisense_"+sentiment+" FLOAT(6)"+
                    ", NRC_"+sentiment+" FLOAT(6)";
                    if (sentiment.equals("anger") || sentiment.equals("joy"))
                        sql += ", EmoSN_"+sentiment+" FLOAT(6)";
                    sql += " )";
                
                stmt.executeUpdate(sql);
                if (stmt != null) 
                    stmt.close(); 
        } catch (SQLException ex) {
            Logger.getLogger(OracleDB.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
    
    public void insertDocument(String sentiment, String word, String type, Set<String> resources, Map<String, Float> resorcesWithScore, int freq){
         try {
            Statement stmt = this.connection.createStatement();
            String sql = "";
            
            String column = "WORD, TYPE, FREQUENCY";
            String values = "'"+ word.replace("'","''") +"', '"+ type + "', '"+ freq + "'";
            
            
            
            for (String res : resources){
                column += ", "+res;
                values += ", '"+1+"'";
            }
            
            sql = "insert into "+sentiment+" (" + column +" ) values ( "+values +")";
            stmt.executeUpdate(sql);
            
            if (!resorcesWithScore.isEmpty()){
                sql = "UPDATE "+ sentiment +" SET ";
                Iterator it = resorcesWithScore.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    sql += (String)pair.getKey() + " = '"+ (Float.toString((Float)pair.getValue())).replace(".",",") + "'";
                    if (it.hasNext())
                        sql += ", ";
                }
                sql += " WHERE  WORD = '"+word+"'";
                stmt.executeUpdate(sql);
            }
            if (stmt != null) 
                stmt.close(); 
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
