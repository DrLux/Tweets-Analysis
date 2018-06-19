package sentiment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sorre
 */
public class FilesHandler {
    Database db = null;
    List<String> listFiles = null;
    Boolean initDbs = true;
    MongoDB mongo = null;
            
    public FilesHandler(Database db, Boolean initDbs, MongoDB mongo){ // initDbs= se true crea da 0 tutto il db
        this.db = db;
        this.mongo = mongo;
        File root = new File( "files/" ); 
        File[] list = root.listFiles();
        this.listFiles = new ArrayList<String>();     
        String filename = "";
        for ( File f : list ) { 
            filename = f.getName().replace(".txt", "");
            filename = filename.replace("-", "_");             
            listFiles.add(filename);
        }
        this.initDbs = initDbs;
        createAllDB();
    }
    
    public void storeFileInDb(String filename) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader("files/"+filename+".txt"));
        String line;
        boolean tableIsPresent = false;
       
        while ((line = br.readLine()) != null) {
            String[] splited = line.split("\\s+");
            
            /*if (tableIsPresent == false){
                if (splited.length == 1 )
                     db.CreateTableFromFile(filename,1);            
                else
                     db.CreateTableFromFile(filename,2);
                tableIsPresent = true;
            }*/
            mongo.processLine(mongo.getCollection(filename), splited);
            /*if (splited.length == 1 )
                db.InsertData(filename, splited.length, splited[0], "0");            
            else
                db.InsertData(filename, splited.length, splited[0], splited[1].replace(".",","));
            */
        }
        try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
    }
    
    public List<String> getListFiles(){
        return this.listFiles;
    }
    
    public void createAllDB(){
        if (this.initDbs){
            for ( String filename : this.listFiles ) { 
                try {                   
                    storeFileInDb(filename);
                } catch (IOException ex) {
                    Logger.getLogger(FilesHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            this.initDbs = false;
        }
    }
    
    public void createSentiment(String sentiment){
        if (!db.TableExists(sentiment.toUpperCase()+"_HASHTAG"))
            db.CrateTable(sentiment, "HASHTAG");
        if (!db.TableExists(sentiment.toUpperCase()+"_EMOTICON"))
            db.CrateTable(sentiment,"EMOTICON");
        if (!db.TableExists(sentiment.toUpperCase()+"_EMOJI"))
            db.CrateTable(sentiment,"EMOJI");
        if (!db.TableExists(sentiment.toUpperCase())){            
            String sql = "CREATE TABLE "+sentiment.toUpperCase()+" ( WORD VARCHAR2(40) NOT NULL, ";
            for ( String filename : this.listFiles ) {
                if (!filename.startsWith("NRC_") && !filename.startsWith("sentisense_") && !filename.startsWith("EmoSN_"))
                    sql += filename.toUpperCase()+" FLOAT(6), ";
            }
            sql += "NRC_"+sentiment.toUpperCase()+" FLOAT(6), ";
            sql += "SENTISENSE_"+sentiment.toUpperCase()+" FLOAT(6), ";
            
            if (sentiment.equals("ANGER") || sentiment.equals("JOY"))
                sql += "EmoSN_"+sentiment.toUpperCase()+" FLOAT(6), ";
            
            sql += "FREQUENCY NUMBER(6) )";
            db.executeSql(sql);
        }
    }
    
    public void print_statistics(String sentiment,String type, int limit){
        List<String> word_frequency = db.getWordFrequency(sentiment, type, limit);
        Path file;
        file = Paths.get("statistics/"+sentiment.toUpperCase()+"_"+type.toUpperCase()+".txt");
        try {
            Files.write(file, word_frequency, Charset.forName("UTF-8"));
        } catch (IOException ex) {
            Logger.getLogger(FilesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
        
    public void insertWords(String original_word, String stem_word, String sentiment){
        String score = "";
        db.insertWordSentiment(original_word, sentiment);
        for ( String filename : this.listFiles ) {
            if (!filename.startsWith("NRC_") && !filename.startsWith("sentisense_") && !filename.startsWith("EmoSN_")){
                score = db.getWordScore(stem_word,filename);
                db.insertScoreSentiment(original_word, score, filename, sentiment);
            }   
        }
        
        String  nameTable = "";
        //NRC_SENTIMENT
        nameTable = "NRC_"+sentiment.toUpperCase();
        score = db.getWordScore(stem_word,nameTable);
        db.insertScoreSentiment(original_word, score, nameTable, sentiment);
         
        //SENTISENSE_SENTIMENT
        nameTable = "SENTISENSE_"+sentiment.toUpperCase();
        score = db.getWordScore(stem_word,nameTable);
        db.insertScoreSentiment(original_word, score, nameTable, sentiment);
         
        //EMOSN_SENTIMENT
        if (sentiment.equals("ANGER") || sentiment.equals("JOY")){  
            nameTable = "EmoSN_"+sentiment.toUpperCase();
            score = db.getWordScore(stem_word,nameTable);
            db.insertScoreSentiment(original_word, score, nameTable, sentiment);
        }
            
    }
    
    
    
    public void splitTweets(String sentiment){
        try {
            BufferedReader br = new BufferedReader(new FileReader("tweets/dataset_dt_" +sentiment+ "_60k.txt"));
            String line = "";
            int i = 0;
            Path path;
            String percorso= "";
            File newFile = null;
            int indice_file = 0;
            try {
                while (line != null) {
                    i = 0;
                    indice_file += 1;
                    percorso = "C:\\Users\\sorre\\Desktop\\Git\\Tweets-Analysis\\tweets\\/"+sentiment+"/"+sentiment+"_"+indice_file+".txt";
                    System.out.println(percorso);
                    path = Paths.get(percorso);
                    newFile = new File(percorso);
                    if (!Files.exists(path))
                        newFile.createNewFile();
                    for (i = 0; i < 10000 && line != null; i++){
                        line = br.readLine();
                        Files.write(path, Arrays.asList(line), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Tweet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Tweet.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
       
}
