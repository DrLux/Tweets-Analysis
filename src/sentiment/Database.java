/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentiment;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author sorre
 */
public class Database {

    private Connection connection = null;
    private String user = "";
    private String pass = "";
    private DatabaseMetaData dbm = null;
    
    public Database(String user, String pass){
        this.user = user;
        this.pass = pass;
        
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
    
    public void executeSql(String sql){
        try {
            Statement stmt = this.connection.createStatement();
            //System.out.println(sql);
            stmt.executeUpdate(sql);
            if (stmt != null) 
                stmt.close(); 
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void CreateTableFromFile(String NameTable, int numColumn){
        if (!TableExists(NameTable.toUpperCase())){
            try {
                Statement stmt = this.connection.createStatement();
                String sql = "CREATE TABLE " + NameTable.toUpperCase() +
                        " ( WORD VARCHAR2(40) NOT NULL";
                if (numColumn == 2)
                    sql += ", SCORE FLOAT(6)";
                sql += " )";
                System.out.println(sql);
                stmt.executeUpdate(sql);
                if (stmt != null) 
                    stmt.close(); 
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void DropTable(String nameTable){
        if (TableExists(nameTable.toUpperCase())){
            try {
                Statement stmt = this.connection.createStatement();
                String sql = "drop table \"SYSTEM\".\""+nameTable.toUpperCase()+"\"";
                //System.out.println(sql);
                stmt.executeUpdate(sql);
                if (stmt != null) 
                    stmt.close(); 
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
      
    public void CrateTable(String sentiment, String type){
        if (!TableExists(sentiment.toUpperCase())){
            try {
                type = type.toUpperCase();
                int size_word = 15;
                if (type == "HASHTAG")
                    size_word = 80;
                Statement stmt = this.connection.createStatement();
                String sql = "CREATE TABLE " + sentiment.toUpperCase() +"_"+type+" ( "+type+" VARCHAR2("+size_word+") NOT NULL, FREQUENCY NUMBER(4))";
                //System.out.println(sql);
                stmt.executeUpdate(sql);
                if (stmt != null) 
                    stmt.close(); 
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void InsertData(String sentiment, String word, String type){
        //if (TableExists(sentiment.toUpperCase() +"_"+type)){
            try {
                    type = type.toUpperCase();
                    Statement stmt = this.connection.createStatement();
                    String sql = "";
                    int frequency = 1;
                    sql = "select FREQUENCY from \"SYSTEM\".\"" + sentiment.toUpperCase() +"_"+type+"\" where "+type+" = '"+word+"'";
                    
                    //System.out.println(sql);
                    ResultSet rs = stmt.executeQuery(sql);

                    if (rs.next()){
                        frequency = rs.getInt("FREQUENCY");
                        sql = "UPDATE \"SYSTEM\".\"" + sentiment.toUpperCase() +"_"+type+"\" SET FREQUENCY = "+ (frequency+1) + " WHERE "+type+" = '"+ word +"'";                 
                    } else {
                            sql = "insert into \"SYSTEM\".\"" + sentiment.toUpperCase() +"_"+type+"\" ("+type+", FREQUENCY) values ( '" + word +"', '" + 1 +"')";
                    }

                    //System.out.println(sql);
                    stmt.executeUpdate(sql);
                    if (stmt != null) 
                        stmt.close(); 
                } catch (SQLException ex) {
                    Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
                }
        //}
    }
    
    public void insertWordSentiment(String word, String sentiment){
        //if (TableExists(sentiment.toUpperCase())){
            try {
                if (word.contains("'"))
                    word = word.replaceAll("'", "''");
                Statement stmt = this.connection.createStatement();
                String sql = "";
                sql = "select FREQUENCY from \"SYSTEM\".\"" + sentiment.toUpperCase() +"\" where WORD = '"+word+"'";

                //System.out.println(sql);
                ResultSet rs = stmt.executeQuery(sql);
                int frequency = 0;

                if (rs.next()){
                    frequency = rs.getInt("FREQUENCY");
                    sql = "UPDATE \"SYSTEM\".\"" + sentiment.toUpperCase() +"\" SET FREQUENCY = "+ (frequency+1) + " WHERE WORD = '"+ word +"'";                 
                } else {
                        sql = "insert into \"SYSTEM\".\"" + sentiment.toUpperCase() +"\" (WORD, FREQUENCY) values ( '" + word +"', '" + 1 +"')";
                }

                //System.out.println(sql);
                stmt.executeUpdate(sql);
                if (stmt != null) 
                    stmt.close(); 
                } catch (SQLException ex) {
                    Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
                }
        //}
    }
    
    public void insertScoreSentiment(String word, String score, String filename, String sentiment){
        //if (TableExists(sentiment.toUpperCase())){
            try {
                if (word.contains("'"))
                    word = word.replaceAll("'", "''");
                score =  score.replace(".",",");
                Statement stmt = this.connection.createStatement();

                String sql = "UPDATE \"SYSTEM\".\"" + sentiment.toUpperCase() +"\" SET "+filename.toUpperCase()+" = '"+ score + "' WHERE WORD = '"+ word +"'";  

            //System.out.println(sql);
            stmt.executeUpdate(sql);
            if (stmt != null) 
                stmt.close(); 
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        //}        
    }
  
    public void InsertData(String NameTable, int numParameters, String word, String score){
        if (!word.contains("_")){ //&& TableExists(NameTable.toUpperCase())){
            if (word.contains("'"))
                word = word.replaceAll("'", "''");
            try {
                Statement stmt = this.connection.createStatement();
                String sql = "";
                if (numParameters == 2)
                    sql = "insert into \"SYSTEM\".\"" + NameTable.toUpperCase() + "\" (WORD, SCORE) values ( '" + word +"', '" + score +"')";
                else 
                    sql = "insert into \"SYSTEM\".\"" + NameTable.toUpperCase() + "\" (WORD) values ( '" + word +"')";
                //System.out.println(sql);
                stmt.executeUpdate(sql);
                if (stmt != null) 
                    stmt.close(); 
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    public Boolean TableExists(String nameTable){
        boolean res = false;
        try {
            ResultSet tables = dbm.getTables(null, null, nameTable.toUpperCase(), null);
            res = tables.next();
            if (tables != null) 
                tables.close();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    
    public String getWordScore(String word, String nameTable){         
        String res = "0,0";
        nameTable = nameTable.toUpperCase();
        //if (TableExists(nameTable)){
            try {
                Statement stmt = this.connection.createStatement();
                String sql = "select * from \"SYSTEM\".\"" + nameTable +"\" where WORD = '"+word+"'";
                //System.out.println(sql);
                
                ResultSet rs = stmt.executeQuery(sql); 

                if (rs.next() && (nameTable.contains("DAL_") || nameTable.contains("ANEW")) ){
                    res = rs.getString("SCORE");
                }

                stmt.executeUpdate(sql);
                if (stmt != null) 
                    stmt.close();           

            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        //}
        return res;
    }
    
    public void dropDB(String sentiment, List<String> listFiles){
        //System.out.println("Dropping all db");
        String table = "";
        
        table = sentiment.toUpperCase()+"_HASHTAG";
        if (TableExists(table))
            DropTable(table);
        
        table = sentiment.toUpperCase()+"_EMOTICON";
        if (TableExists(table))
            DropTable(table);
        
        table = sentiment.toUpperCase()+"_EMOJI";
        if (TableExists(table))
            DropTable(table);
        
        for (String file : listFiles ){
            table = file.toUpperCase();
            if (TableExists(table))
                DropTable(table);
        }
        
        table = sentiment.toUpperCase();
        if (TableExists(table))
            DropTable(table);
    }
    
    public List<String> getWordFrequency(String sentiment,String type_word, int limit){
        sentiment = sentiment.toUpperCase();
        List<String> word_frequency = null;
        //if (TableExists(sentiment)){           
            try {
                type_word = type_word.toUpperCase();
                sentiment = sentiment.toUpperCase();
                String nameTable = "";
                if (type_word.equalsIgnoreCase("WORD"))
                    nameTable = sentiment;
                else
                    nameTable = sentiment+"_"+type_word;
                word_frequency = new ArrayList<String>();     
                Statement stmt = this.connection.createStatement();
                String sql = "SELECT *" +
                     "FROM (SELECT * FROM "+ nameTable +" ORDER BY FREQUENCY DESC)";
                if (limit > 0)
                    sql += "WHERE ROWNUM <= "+limit;
                ResultSet rs = stmt.executeQuery(sql);
                String temp_ris = "";
                
                while (rs.next()) {
                    temp_ris = "";
                    temp_ris = rs.getString("FREQUENCY")+"\t"+rs.getString(type_word);
                    word_frequency.add(temp_ris);
                }
                
                if (stmt != null) 
                    stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }             
        //}
        return word_frequency;
    }
}

/*
PRENDI LE PAROLE PIU FREQIUENTI

SELECT *
FROM (SELECT * FROM ANGER ORDER BY FREQUENCY DESC)
WHERE ROWNUM <= 5;

*/